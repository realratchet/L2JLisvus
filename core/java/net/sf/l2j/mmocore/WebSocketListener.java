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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
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
 * Binary messages carry the regular length-prefixed legacy packets. Outgoing packets are sent one per message.<br>
 * Connections from trusted proxies (e.g. Cloudflare) are attributed to the address in the configured real IP header instead of the proxy address.
 */
public final class WebSocketListener extends WebSocketServer
{
	private static final Logger _log = Logger.getLogger(WebSocketListener.class.getName());

	private static final int HEADER_SIZE = 2;
	private static final int MAX_FRAME_SIZE = 0xFFFF;
	// Close a browser that stops reading instead of queueing without bound
	private static final int MAX_QUEUED_FRAMES = 4096;
	// Close a browser that sends faster than the hand-off socket drains
	private static final int MAX_QUEUED_BYTES = 256 * 1024;

	private final SelectorThread<?> _selectorThread;
	private final InetSocketAddress _handOffTarget;
	private final boolean _allowAnyOrigin;
	private final Set<String> _allowedOrigins = new HashSet<>();
	private final List<Subnet> _trustedProxies = new ArrayList<>();
	private final String _realIpHeader;

	/**
	 * @param selectorThread the selector that serves the regular TCP listener
	 * @param bindAddress WebSocket bind address, null for all available addresses
	 * @param port WebSocket port
	 * @param tcpBindAddress bind address of the regular TCP listener, null for all available addresses
	 * @param tcpPort port of the regular TCP listener
	 * @param allowedOrigins comma separated Origin allowlist, * allows any, empty rejects every browser
	 * @param trustedProxies comma separated proxy addresses or CIDR ranges allowed to set the real IP header, empty trusts none
	 * @param realIpHeader header holding the browser address on connections from a trusted proxy, the last entry is used when it is a list
	 */
	public WebSocketListener(SelectorThread<?> selectorThread, InetAddress bindAddress, int port, InetAddress tcpBindAddress, int tcpPort, String allowedOrigins, String trustedProxies, String realIpHeader)
	{
		super(bindAddress == null ? new InetSocketAddress(port) : new InetSocketAddress(bindAddress, port), Collections.<Draft> singletonList(new Draft_6455(Collections.emptyList(), MAX_FRAME_SIZE)));

		_selectorThread = selectorThread;
		_handOffTarget = new InetSocketAddress(tcpBindAddress == null ? InetAddress.getLoopbackAddress() : tcpBindAddress, tcpPort);

		final List<String> origins = Arrays.asList(allowedOrigins.trim().toLowerCase(Locale.ROOT).split("\\s*,\\s*"));
		_allowAnyOrigin = origins.contains("*");
		for (String origin : origins)
		{
			if (!origin.isEmpty() && !origin.equals("*"))
			{
				_allowedOrigins.add(origin);
			}
		}
		if (!_allowAnyOrigin && _allowedOrigins.isEmpty())
		{
			_log.warning("WebSocketAllowedOrigins is empty, every browser connection on port " + port + " will be refused.");
		}

		for (String proxy : trustedProxies.trim().split("\\s*,\\s*"))
		{
			if (proxy.isEmpty())
			{
				continue;
			}
			try
			{
				_trustedProxies.add(Subnet.parse(proxy));
			}
			catch (IllegalArgumentException e)
			{
				_log.warning("WebSocketTrustedProxies: ignoring invalid entry " + proxy + ".");
			}
		}
		_realIpHeader = realIpHeader.trim();
		if (!_trustedProxies.isEmpty() && _realIpHeader.isEmpty())
		{
			_log.warning("WebSocketRealIpHeader is empty, every browser connection through a trusted proxy on port " + port + " will be refused.");
		}

		setReuseAddr(true);
		setTcpNoDelay(true);
		setDaemon(true);
	}

	@Override
	public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket ws, Draft draft, ClientHandshake request) throws InvalidDataException
	{
		if (!_allowAnyOrigin)
		{
			// A missing Origin header is refused as well
			final String origin = request.getFieldValue("Origin");
			if ((origin == null) || !_allowedOrigins.contains(origin.trim().toLowerCase(Locale.ROOT)))
			{
				throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Origin not allowed");
			}
		}

		final InetSocketAddress peer = resolvePeer(ws.getRemoteSocketAddress(), request);
		if (peer == null)
		{
			throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Connection refused");
		}

		final IAcceptFilter filter = _selectorThread.getAcceptFilter();
		if ((filter != null) && !filter.accept(peer.getAddress()))
		{
			throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Connection refused");
		}

		final ServerHandshakeBuilder response = super.onWebsocketHandshakeReceivedAsServer(ws, draft, request);
		// Picked up by onOpen
		ws.setAttachment(peer);
		return response;
	}

	/**
	 * @param remote the TCP peer of the WebSocket
	 * @param request
	 * @return the browser address, the real IP header value when the TCP peer is a trusted proxy, or null when a trusted proxy did not send a valid one
	 */
	private InetSocketAddress resolvePeer(InetSocketAddress remote, ClientHandshake request)
	{
		if (!isTrustedProxy(remote.getAddress()))
		{
			return remote;
		}

		final String header = _realIpHeader.isEmpty() ? null : request.getFieldValue(_realIpHeader);
		if ((header != null) && !header.isBlank())
		{
			// Proxies append to forwarding lists, so the last entry is the one the trusted proxy saw
			final String value = header.substring(header.lastIndexOf(',') + 1).trim();
			try
			{
				return new InetSocketAddress(InetAddress.ofLiteral(value), remote.getPort());
			}
			catch (IllegalArgumentException e)
			{
			}
		}

		_log.warning("WebSocket connection from trusted proxy " + remote.getAddress().getHostAddress() + " has no valid " + _realIpHeader + " header, refusing.");
		return null;
	}

	private boolean isTrustedProxy(InetAddress address)
	{
		for (Subnet subnet : _trustedProxies)
		{
			if (subnet.contains(address))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void onOpen(WebSocket ws, ClientHandshake handshake)
	{
		final InetSocketAddress peer = ws.getAttachment();
		SocketChannel handOff = null;
		SocketAddress handOffAddress = null;
		try
		{
			handOff = SocketChannel.open();
			handOff.bind(new InetSocketAddress(_handOffTarget.getAddress(), 0));
			handOffAddress = handOff.getLocalAddress();

			// Register before connecting, the selector may accept as soon as the connection is established
			_selectorThread.registerWebSocketPeer(handOffAddress, peer);
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

		final HandOff connection = new HandOff(handOff, peer);
		ws.setAttachment(connection);

		Thread.ofVirtual().name("WebSocketHandOff-" + peer).start(() -> pump(ws, connection));
		Thread.ofVirtual().name("WebSocketWriter-" + peer).start(() -> write(ws, connection));
	}

	/**
	 * Forwards server packets from the hand-off socket to the browser, one legacy packet per message.
	 * @param ws
	 * @param connection
	 */
	private static void pump(WebSocket ws, HandOff connection)
	{
		final SocketChannel handOff = connection._channel;
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
						_log.warning("WebSocket client " + connection._peer + " is not reading, closing.");
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
				_log.log(Level.FINE, "WebSocket hand-off for " + connection._peer + " failed: " + e.getMessage());
			}
		}
		finally
		{
			connection.close();
			ws.close(CloseFrame.NORMAL);
		}
	}

	/**
	 * Forwards browser messages to the hand-off socket in arrival order, off the WebSocket decoder thread.
	 * @param ws
	 * @param connection
	 */
	private static void write(WebSocket ws, HandOff connection)
	{
		try
		{
			byte[] data;
			while ((data = connection._queue.take()) != HandOff.CLOSED)
			{
				final ByteBuffer buf = ByteBuffer.wrap(data);
				while (buf.hasRemaining())
				{
					connection._channel.write(buf);
				}
				connection._queuedBytes.addAndGet(-data.length);
			}
		}
		catch (Exception e)
		{
			if (ws.isOpen())
			{
				_log.log(Level.FINE, "WebSocket hand-off write for " + connection._peer + " failed: " + e.getMessage());
			}
		}
		finally
		{
			connection.close();
			ws.close(CloseFrame.NORMAL);
		}
	}

	@Override
	public void onMessage(WebSocket ws, ByteBuffer message)
	{
		if (!(ws.<Object> getAttachment() instanceof HandOff connection) || !connection._channel.isOpen() || !message.hasRemaining())
		{
			return;
		}

		final byte[] data = new byte[message.remaining()];
		message.get(data);
		if (connection._queuedBytes.addAndGet(data.length) > MAX_QUEUED_BYTES)
		{
			_log.warning("WebSocket client " + connection._peer + " is sending too fast, closing.");
			connection.close();
			ws.close(CloseFrame.POLICY_VALIDATION, "Too fast");
			return;
		}
		connection._queue.add(data);
	}

	@Override
	public void onMessage(WebSocket ws, String message)
	{
		ws.close(CloseFrame.REFUSE, "Binary messages only");
	}

	@Override
	public void onClose(WebSocket ws, int code, String reason, boolean remote)
	{
		// The attachment is still the browser address until onOpen
		if (ws.<Object> getAttachment() instanceof HandOff connection)
		{
			connection.close();
		}
	}

	@Override
	public void onError(WebSocket ws, Exception e)
	{
		if (ws == null)
		{
			_log.log(Level.WARNING, "WebSocket listener error: " + e.getMessage(), e);
			return;
		}
		// The attachment is still the browser address until onOpen
		if (ws.<Object> getAttachment() instanceof HandOff connection)
		{
			connection.close();
		}
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

	/**
	 * Hand-off socket of one browser with its pending browser to server data.
	 */
	private static final class HandOff
	{
		// Wakes up and stops the writer
		static final byte[] CLOSED = new byte[0];

		final SocketChannel _channel;
		final InetSocketAddress _peer;
		final LinkedBlockingQueue<byte[]> _queue = new LinkedBlockingQueue<>();
		final AtomicInteger _queuedBytes = new AtomicInteger();

		HandOff(SocketChannel channel, InetSocketAddress peer)
		{
			_channel = channel;
			_peer = peer;
		}

		void close()
		{
			closeQuietly(_channel);
			_queue.clear();
			_queue.add(CLOSED);
		}
	}

	/**
	 * IPv4 or IPv6 address range in CIDR notation, a plain address matches only itself.
	 */
	private static final class Subnet
	{
		final byte[] _network;
		final int _prefix;

		private Subnet(byte[] network, int prefix)
		{
			_network = network;
			_prefix = prefix;
		}

		static Subnet parse(String cidr)
		{
			final int slash = cidr.indexOf('/');
			final byte[] network = InetAddress.ofLiteral(slash < 0 ? cidr : cidr.substring(0, slash)).getAddress();
			final int prefix = slash < 0 ? network.length * 8 : Integer.parseInt(cidr.substring(slash + 1));
			if ((prefix < 0) || (prefix > (network.length * 8)))
			{
				throw new IllegalArgumentException("Invalid prefix length " + prefix);
			}
			return new Subnet(network, prefix);
		}

		boolean contains(InetAddress address)
		{
			final byte[] bytes = address.getAddress();
			if (bytes.length != _network.length)
			{
				return false;
			}

			final int fullBytes = _prefix / 8;
			for (int i = 0; i < fullBytes; i++)
			{
				if (bytes[i] != _network[i])
				{
					return false;
				}
			}

			final int remainingBits = _prefix % 8;
			if (remainingBits == 0)
			{
				return true;
			}
			final int mask = (0xFF << (8 - remainingBits)) & 0xFF;
			return (bytes[fullBytes] & mask) == (_network[fullBytes] & mask);
		}
	}
}
