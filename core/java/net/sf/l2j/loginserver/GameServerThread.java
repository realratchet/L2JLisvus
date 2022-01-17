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
package net.sf.l2j.loginserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.loginserver.GameServerTable.GameServerInfo;
import net.sf.l2j.loginserver.crypt.NewCrypt;
import net.sf.l2j.loginserver.gameserverpackets.BlowFishKey;
import net.sf.l2j.loginserver.gameserverpackets.ChangeAccessLevel;
import net.sf.l2j.loginserver.gameserverpackets.GameServerAuth;
import net.sf.l2j.loginserver.gameserverpackets.PlayerAuthRequest;
import net.sf.l2j.loginserver.gameserverpackets.PlayerInGame;
import net.sf.l2j.loginserver.gameserverpackets.PlayerLogout;
import net.sf.l2j.loginserver.gameserverpackets.ServerStatus;
import net.sf.l2j.loginserver.loginserverpackets.AuthResponse;
import net.sf.l2j.loginserver.loginserverpackets.InitLS;
import net.sf.l2j.loginserver.loginserverpackets.KickPlayer;
import net.sf.l2j.loginserver.loginserverpackets.LoginServerFail;
import net.sf.l2j.loginserver.loginserverpackets.PlayerAuthResponse;
import net.sf.l2j.loginserver.serverpackets.ServerBasePacket;
import net.sf.l2j.util.Util;

/**
 * @author -Wooden-
 * @author KenM
 */
public class GameServerThread extends Thread
{
	protected static Logger _log = Logger.getLogger(GameServerThread.class.getName());
	private final Socket _connection;
	private InputStream _in;
	private OutputStream _out;
	private final RSAPublicKey _publicKey;
	private final RSAPrivateKey _privateKey;
	private NewCrypt _blowfish;
	private byte[] _blowfishKey;
	
	private final String _connectionIp;

	private GameServerInfo _gsi;
	
	/** Authed Clients on a GameServer */
	private final Set<String> _accountsOnGameServer = ConcurrentHashMap.newKeySet();
	
	private String _connectionIPAddress;

	@Override
	public void run()
	{
		_connectionIPAddress = _connection.getInetAddress().getHostAddress();
		if (GameServerThread.isBannedGameserverIP(_connectionIPAddress))
		{
			_log.info("GameServerRegistration: IP Address " + _connectionIPAddress + " is on Banned IP list.");
			forceClose(LoginServerFail.REASON_IP_BANNED);
			// ensure no further processing for this connection
			return;
		}

		InitLS startPacket = new InitLS(_publicKey.getModulus().toByteArray());
		try
		{
			sendPacket(startPacket);

			int lengthHi = 0;
			int lengthLo = 0;
			int length = 0;
			boolean checksumOk = false;
			while (true)
			{
				lengthLo = _in.read();
				lengthHi = _in.read();
				length = (lengthHi * 256) + lengthLo;
				
				if ((lengthHi < 0) || _connection.isClosed())
				{
					_log.finer("LoginServerThread: Login terminated the connection.");
					break;
				}

				byte[] data = new byte[length - 2];
				
				int receivedBytes = 0;
				int newBytes = 0;
				while (newBytes != -1 && receivedBytes < length - 2)
				{
					newBytes = _in.read(data, 0, length - 2);
					receivedBytes = receivedBytes + newBytes;
				}

				if (receivedBytes != (length - 2))
				{
					_log.warning("Incomplete Packet is sent to the server, closing connection.(LS)");
					break;
				}
				
				// decrypt if we have a key
				data = _blowfish.decrypt(data);
				checksumOk = NewCrypt.verifyChecksum(data);
				if (!checksumOk)
				{
					_log.warning("Incorrect packet checksum, closing connection. (LS)");
					return;
				}
				
				if (Config.DEBUG)
				{
					_log.warning("[C]\n" + Util.printData(data));
				}
				
				int packetType = data[0] & 0xff;
				switch (packetType)
				{
					case 00:
						BlowFishKey bfk = new BlowFishKey(data, _privateKey);
						_blowfishKey = bfk.getKey();
						_blowfish = new NewCrypt(_blowfishKey);
						if (Config.DEBUG)
						{
							_log.info("New BlowFish key received, Blowfish engine initialized:");
						}
						break;
					case 01:
						GameServerAuth gsa = new GameServerAuth(data);
						if (Config.DEBUG)
						{
							_log.info("Auth request received");
						}
						handleRegProcess(gsa);
						if (isAuthed())
						{
							AuthResponse ar = new AuthResponse(getGameServerInfo().getId());
							sendPacket(ar);
							if (Config.DEBUG)
							{
								_log.info("Authed: id: " + getGameServerInfo().getId());
							}
							broadcastToTelnet("GameServer [" + getServerId() + "] " + GameServerTable.getInstance().getServerNameById(getServerId()) + " is connected.");
						}
						break;
					case 02:
						if (isAuthed())
						{
							PlayerInGame pig = new PlayerInGame(data);
							List<String> newAccounts = pig.getAccounts();
							for (String account : newAccounts)
							{
								_accountsOnGameServer.add(account);
								if (Config.DEBUG)
								{
									_log.info("Account "+account+" logged in GameServer: ["+getServerId()+"] "+GameServerTable.getInstance().getServerNameById(getServerId()));
								}

								broadcastToTelnet("Account "+account+" logged in GameServer "+getServerId());
							}
						}
						else
						{
							forceClose(LoginServerFail.NOT_AUTHED);
						}
						break;
					case 03:
						if (isAuthed())
						{
							PlayerLogout plo = new PlayerLogout(data);
							_accountsOnGameServer.remove(plo.getAccount());
							if (Config.DEBUG)
							{
								_log.info("Player "+plo.getAccount()+" logged out from gameserver ["+getServerId()+"] "+GameServerTable.getInstance().getServerNameById(getServerId()));
							}

							broadcastToTelnet("Player "+plo.getAccount()+" disconnected from GameServer "+getServerId());
						}
						else
						{
							forceClose(LoginServerFail.NOT_AUTHED);
						}
						break;
					case 04:
						if (isAuthed())
						{
							ChangeAccessLevel cal = new ChangeAccessLevel(data);
							LoginController.getInstance().setAccountAccessLevel(cal.getAccount(),cal.getLevel());
							_log.info("Changed "+cal.getAccount()+" access level to " + cal.getLevel());
						}
						else
						{
							forceClose(LoginServerFail.NOT_AUTHED);
						}
						break;
					case 05:
						if (isAuthed())
						{
							PlayerAuthRequest par = new PlayerAuthRequest(data);
							PlayerAuthResponse authResponse;
							if (Config.DEBUG)
							{
								_log.info("auth request received for Player "+par.getAccount());
							}
							SessionKey key = LoginController.getInstance().getKeyForAccount(par.getAccount());
							if (key != null && key.equals(par.getKey()))
							{
								if (Config.DEBUG)
								{
									_log.info("auth request: OK");
								}
								LoginController.getInstance().removeAuthedLoginClient(par.getAccount());
								authResponse = new PlayerAuthResponse(par.getAccount(), true);
							}
							else
							{
								if (Config.DEBUG)
								{
									_log.info("auth request: NO");
									_log.info("session key from self: "+key);
									_log.info("session key sent: "+par.getKey());
								}
								authResponse = new PlayerAuthResponse(par.getAccount(), false);
							}
							sendPacket(authResponse);
						}
						else
						{
							forceClose(LoginServerFail.NOT_AUTHED);
						}
						break;
					case 06:
						if (isAuthed())
						{
							if (Config.DEBUG)
							{
								_log.info("ServerStatus received");
							}
							@SuppressWarnings("unused")
							ServerStatus ss = new ServerStatus(data, getServerId()); // will do the actions by itself
						}
						else
						{
							forceClose(LoginServerFail.NOT_AUTHED);
						}
						break;
				}
			}
		}
		catch (IOException e)
		{
			String serverName = (getServerId() != -1 ? "["+getServerId()+"] "+GameServerTable.getInstance().getServerNameById(getServerId()) : "("+_connectionIPAddress+")");
			String msg = "GameServer " + serverName + ": Connection lost: " + e.getMessage();
			_log.info(msg);
			broadcastToTelnet(msg);
		}
		finally
		{
			if (isAuthed())
			{
				_gsi.setDown();
				_log.info("Server [" + getServerId() + "] "+GameServerTable.getInstance().getServerNameById(getServerId()) + " is now set as disconnected");
			}
			L2LoginServer.getGameServerListener().removeGameServer(this);
			L2LoginServer.getGameServerListener().removeFloodProtection(_connectionIp);
		}
	}
	
	private void handleRegProcess(GameServerAuth gameServerAuth)
	{
		GameServerTable gameServerTable = GameServerTable.getInstance();
		
		int id = gameServerAuth.getDesiredID();
		byte[] hexId = gameServerAuth.getHexID();
		
		GameServerInfo gsi = gameServerTable.getRegisteredGameServerById(id);
		// Is there a gameserver registered with this id?
		if (gsi != null)
		{
			// Does the hex id match?
			if (Arrays.equals(gsi.getHexId(), hexId))
			{
				// Check to see if this GS is already connected
				synchronized (gsi)
				{
					if (gsi.isAuthed())
					{
						forceClose(LoginServerFail.REASON_ALREADY_LOGGED8IN);
					}
					else
					{
						attachGameServerInfo(gsi, gameServerAuth);
					}
				}
			}
			else
			{
				// There is already a server registered with the desired id and different hex id
				// Try to register this one with an alternative id
				if (Config.ACCEPT_NEW_GAMESERVER && gameServerAuth.acceptAlternateID())
				{
					gsi = new GameServerInfo(id, hexId, this);
					if (gameServerTable.registerWithFirstAvailableId(gsi))
					{
						attachGameServerInfo(gsi, gameServerAuth);
						gameServerTable.registerServerOnDB(gsi);
					}
					else
					{
						forceClose(LoginServerFail.REASON_NO_FREE_ID);
					}
				}
				else
				{
					// Server id is already taken, and we cant get a new one for you
					forceClose(LoginServerFail.REASON_WRONG_HEXID);
				}
			}
		}
		else
		{
			// Can we register on this id?
			if (Config.ACCEPT_NEW_GAMESERVER)
			{
				gsi = new GameServerInfo(id, hexId, this);
				if (gameServerTable.register(id, gsi))
				{
					attachGameServerInfo(gsi, gameServerAuth);
					gameServerTable.registerServerOnDB(gsi);
				}
				else
				{
					// Some one took this ID meanwhile
					forceClose(LoginServerFail.REASON_ID_RESERVED);
				}
			}
			else
			{
				forceClose(LoginServerFail.REASON_WRONG_HEXID);
			}
		}
	}
	
	public boolean hasAccountOnGameServer(String account)
	{
		return _accountsOnGameServer.contains(account);
	}
	
	public int getPlayerCount()
	{
		return _accountsOnGameServer.size();
	}
	
	/**
	 * Attaches a GameServerInfo to this Thread <li>Updates the GameServerInfo values based on GameServerAuth packet</li> <li><b>Sets the GameServerInfo as Authed</b></li>
	 * @param gsi The GameServerInfo to be attached.
	 * @param gameServerAuth The server info.
	 */
	private void attachGameServerInfo(GameServerInfo gsi, GameServerAuth gameServerAuth)
	{
		setGameServerInfo(gsi);
		gsi.setGameServerThread(this);
		gsi.setPort(gameServerAuth.getPort());
		setGameHosts(gameServerAuth.getExternalHost(), gameServerAuth.getInternalHost());
		gsi.setMaxPlayers(gameServerAuth.getMaxPlayers());
		gsi.setAuthed(true);
	}
	
	private void forceClose(int reason)
	{
		LoginServerFail lsf = new LoginServerFail(reason);
		try
		{
			sendPacket(lsf);
		}
		catch (IOException e)
		{
			_log.finer("GameServerThread: Failed kicking banned server. Reason: " + e.getMessage());
		}
		
		try
		{
			_connection.close();
		}
		catch (IOException e)
		{
			_log.finer("GameServerThread: Failed disconnecting banned server, server already disconnected.");
		}
	}

	/**
	 * @param ipAddress
	 * @return
	 */
	public static boolean isBannedGameserverIP(String ipAddress)
	{
		return LoginController.getInstance().getBannedIps().containsKey(ipAddress);
	}

	public GameServerThread(Socket con)
	{
		_connection = con;
		_connectionIp = con.getInetAddress().getHostAddress();
		try
		{
			_in = _connection.getInputStream();
			_out = new BufferedOutputStream(_connection.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		KeyPair pair = GameServerTable.getInstance().getKeyPair();
		_privateKey = (RSAPrivateKey) pair.getPrivate();
		_publicKey = (RSAPublicKey) pair.getPublic();
		_blowfish = new NewCrypt("_;v.]05-31!|+-%xT!^[$\00");
		start();
	}

	/**
	 * @param sl
	 * @throws IOException
	 */
	private void sendPacket(ServerBasePacket sl) throws IOException
	{
		byte[] data = sl.getContent();
		NewCrypt.appendChecksum(data);
		if (Config.DEBUG)
		{
			_log.finest("[S]\n" + Util.printData(data));
		}
		data = _blowfish.crypt(data);

		int len = data.length + 2;
		synchronized (_out)
		{
			_out.write(len & 0xff);
			_out.write((len >> 8) & 0xff);
			_out.write(data);
			_out.flush();
		}
	}
	
	private void broadcastToTelnet(String msg)
	{
		if (L2LoginServer.getStatusServer() != null)
		{
			L2LoginServer.getStatusServer().sendMessageToTelnets(msg);
		}
	}
	
	public void kickPlayer(String account)
	{
		KickPlayer kp = new KickPlayer(account);
		try
		{
			sendPacket(kp);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param gameExternalHost 
	 * @param gameInternalHost
	 */
	public void setGameHosts(String gameExternalHost, String gameInternalHost)
	{
		String oldInternal = _gsi.getInternalIp();
		String oldExternal = _gsi.getExternalHost();

		_gsi.setExternalHost(gameExternalHost);
		_gsi.setInternalIp(gameInternalHost);

		if (!gameExternalHost.equals("*"))
		{
			try
			{
				_gsi.setExternalIp(InetAddress.getByName(gameExternalHost).getHostAddress());
			}
			catch (UnknownHostException e)
			{
				_log.warning("Couldn't resolve hostname \""+gameExternalHost+"\"");
			}
		}
		else
		{
			_gsi.setExternalIp(_connectionIp);
		}
		if (!gameInternalHost.equals("*"))
		{
			try
			{
				_gsi.setInternalIp(InetAddress.getByName(gameInternalHost).getHostAddress());
			}
			catch (UnknownHostException e)
			{
				_log.warning("Couldn't resolve hostname \""+gameInternalHost+"\"");
			}
		}
		else
		{
			_gsi.setInternalIp(_connectionIp);
		}

		_log.info("Updated Gameserver ["+getServerId()+"] "+GameServerTable.getInstance().getServerNameById(getServerId())+" IP's:");
		if (oldInternal == null || !oldInternal.equalsIgnoreCase(gameInternalHost))
			_log.info("InternalIP: "+gameInternalHost);
		if (oldExternal == null || !oldExternal.equalsIgnoreCase(gameExternalHost))
			_log.info("ExternalIP: "+gameExternalHost);
	}

	/**
	 * @return Returns the isAuthed.
	 */
	public boolean isAuthed()
	{
		if (getGameServerInfo() == null)
		{
			return false;
		}
		return getGameServerInfo().isAuthed();
	}
	
	public void setGameServerInfo(GameServerInfo gsi)
	{
		_gsi = gsi;
	}
	
	public GameServerInfo getGameServerInfo()
	{
		return _gsi;
	}
	
	/**
	 * @return Returns the connectionIpAddress.
	 */
	public String getConnectionIpAddress()
	{
		return _connectionIPAddress;
	}
	
	private int getServerId()
	{
		if (getGameServerInfo() != null)
		{
			return getGameServerInfo().getId();
		}
		return -1;
	}
}