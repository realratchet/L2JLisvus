/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.mmocore;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;

/**
 * Native WebSocket entry point for browser clients.<br>
 * Each browser connection is handed to the selector through a loopback socket. The selector is told the browser address beforehand, so the resulting {@link MMOConnection} reports the real peer and every existing protection (accept filter, bans, flood protectors) applies unchanged.<br>
 * Binary messages carry the regular length-prefixed legacy packets. Outgoing packets are sent one per message.
 */
public final class WebSocketListener extends WebSocketServer
{
	private static final Logger _log = Logger.getLogger(WebSocketListener.class.getName());

	private static final int HEADER_SIZE = 2;
	private static final int MAX_FRAME_SIZE = 0xFFFF;
	// Close a browser that stops reading instead of queueing without bound
	private static final int MAX_QUEUED_FRAMES = 4096;

	private final SelectorThread<?> _selectorThread;
	private final InetSocketAddress _handOffTarget;
	private final Set<String> _allowedOrigins;

	/**
	 * @param selectorThread the selector that serves the regular TCP listener
	 * @param bindAddress WebSocket bind address, null for all available addresses
	 * @param port WebSocket port
	 * @param tcpBindAddress bind address of the regular TCP listener, null for all available addresses
	 * @param tcpPort port of the regular TCP listener
	 * @param allowedOrigins comma separated Origin allowlist, * allows any
	 */
	public WebSocketListener(SelectorThread<?> selectorThread, InetAddress bindAddress, int port, InetAddress tcpBindAddress, int tcpPort, String allowedOrigins)
	{
		super(bindAddress == null ? new InetSocketAddress(port) : new InetSocketAddress(bindAddress, port), Collections.<Draft> singletonList(new Draft_6455(Collections.emptyList(), MAX_FRAME_SIZE)));

		_selectorThread = selectorThread;
		_handOffTarget = new InetSocketAddress(tcpBindAddress == null ? InetAddress.getLoopbackAddress() : tcpBindAddress, tcpPort);

		final List<String> origins = Arrays.asList(allowedOrigins.trim().toLowerCase(Locale.ROOT).split("\\s*,\\s*"));
		_allowedOrigins = origins.contains("*") ? null : new HashSet<>(origins);

		setReuseAddr(true);
		setTcpNoDelay(true);
		setDaemon(true);
	}

	@Override
	public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket ws, Draft draft, ClientHandshake request) throws InvalidDataException
	{
		if (_allowedOrigins != null)
		{
			final String origin = request.getFieldValue("Origin").toLowerCase(Locale.ROOT);
			if (!_allowedOrigins.contains(origin))
			{
				throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Origin not allowed");
			}
		}

		final IAcceptFilter filter = _selectorThread.getAcceptFilter();
		if ((filter != null) && !filter.accept(ws.getRemoteSocketAddress().getAddress()))
		{
			throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Connection refused");
		}

		return super.onWebsocketHandshakeReceivedAsServer(ws, draft, request);
	}

	@Override
	public void onOpen(WebSocket ws, ClientHandshake handshake)
	{
		SocketChannel handOff = null;
		SocketAddress handOffAddress = null;
		try
		{
			handOff = SocketChannel.open();
			handOff.bind(new InetSocketAddress(_handOffTarget.getAddress(), 0));
			handOffAddress = handOff.getLocalAddress();

			// Register before connecting, the selector may accept as soon as the connection is established
			_selectorThread.registerWebSocketPeer(handOffAddress, ws.getRemoteSocketAddress());
			handOff.connect(_handOffTarget);
			handOff.socket().setTcpNoDelay(true);
		}
		catch (IOException e)
		{
			_log.log(Level.WARNING, "WebSocket hand-off to " + _handOffTarget + " failed: " + e.getMessage());
			if (handOffAddress != null)
			{
				_selectorThread.unregisterWebSocketPeer(handOffAddress);
			}
			closeQuietly(handOff);
			ws.close(CloseFrame.TRY_AGAIN_LATER);
			return;
		}

		ws.setAttachment(handOff);

		final SocketChannel channel = handOff;
		Thread.ofVirtual().name("WebSocketHandOff-" + ws.getRemoteSocketAddress()).start(() -> pump(ws, channel));
	}

	/**
	 * Forwards server packets from the hand-off socket to the browser, one legacy packet per message.
	 * @param ws
	 * @param handOff
	 */
	private static void pump(WebSocket ws, SocketChannel handOff)
	{
		final ByteBuffer buf = ByteBuffer.allocate(MAX_FRAME_SIZE * 2).order(ByteOrder.LITTLE_ENDIAN);
		try
		{
			while (handOff.read(buf) >= 0)
			{
				buf.flip();
				while (buf.remaining() >= HEADER_SIZE)
				{
					final int size = buf.getShort(buf.position()) & 0xFFFF;
					if (size < HEADER_SIZE)
					{
						throw new IOException("Invalid packet length " + size);
					}
					if (buf.remaining() < size)
					{
						break;
					}

					if (((WebSocketImpl) ws).outQueue.size() >= MAX_QUEUED_FRAMES)
					{
						_log.warning("WebSocket client " + ws.getRemoteSocketAddress() + " is not reading, closing.");
						ws.close(CloseFrame.POLICY_VALIDATION, "Too slow");
						return;
					}

					final byte[] packet = new byte[size];
					buf.get(packet);
					ws.send(packet);
				}
				buf.compact();
			}
		}
		catch (Exception e)
		{
			if (ws.isOpen())
			{
				_log.log(Level.FINE, "WebSocket hand-off for " + ws.getRemoteSocketAddress() + " failed: " + e.getMessage());
			}
		}
		finally
		{
			closeQuietly(handOff);
			ws.close(CloseFrame.NORMAL);
		}
	}

	@Override
	public void onMessage(WebSocket ws, ByteBuffer message)
	{
		final SocketChannel handOff = ws.getAttachment();
		if (handOff == null)
		{
			return;
		}

		try
		{
			while (message.hasRemaining())
			{
				handOff.write(message);
			}
		}
		catch (IOException e)
		{
			closeQuietly(handOff);
			ws.close(CloseFrame.NORMAL);
		}
	}

	@Override
	public void onMessage(WebSocket ws, String message)
	{
		ws.close(CloseFrame.REFUSE, "Binary messages only");
	}

	@Override
	public void onClose(WebSocket ws, int code, String reason, boolean remote)
	{
		closeQuietly(ws.getAttachment());
	}

	@Override
	public void onError(WebSocket ws, Exception e)
	{
		if (ws == null)
		{
			_log.log(Level.WARNING, "WebSocket listener error: " + e.getMessage(), e);
			return;
		}
		closeQuietly(ws.getAttachment());
	}

	@Override
	public void onStart()
	{
		setConnectionLostTimeout(30);
	}

	private static void closeQuietly(SocketChannel channel)
	{
		if (channel == null)
		{
			return;
		}

		try
		{
			channel.close();
		}
		catch (IOException e)
		{
		}
	}
}
