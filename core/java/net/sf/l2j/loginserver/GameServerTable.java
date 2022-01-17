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

import java.io.File;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.loginserver.gameserverpackets.ServerStatus;
import net.sf.l2j.util.Rnd;

public class GameServerTable
{
	protected static Logger _log = Logger.getLogger(GameServerTable.class.getName());
	
	private static final int KEYS_SIZE = 10;
	
	// Server Names Config
	private final Map<Integer, String> _serverNames = new HashMap<>();

	// Game Server Table
	private final Map<Integer, GameServerInfo> _gameServerTable = new HashMap<>();
	
	
	private KeyPair[] _keyPairs;
	
	public static GameServerTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public GameServerTable()
	{
		loadServerNames();
		loadRegisteredGameServers();
		try
		{
			loadRSAKeys();
		}
		catch (GeneralSecurityException e)
		{
			_log.log(Level.SEVERE, "FATAL: Failed to load GameServerTable. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
	}
	
	private void loadServerNames()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(new File(Config.DATAPACK_ROOT + "/servername.xml"));
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if ("list".equalsIgnoreCase(n.getNodeName()))
                {
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if (d.getNodeName().equalsIgnoreCase("server"))
						{
							NamedNodeMap attrs = d.getAttributes();
							
							int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
							String name = attrs.getNamedItem("name").getNodeValue();
							
							_serverNames.put(id, name);
						}
					}
                }
            }
			_log.info("Loaded " + _serverNames.size() + " server names.");
		}
		catch (Exception e)
		{
			_log.severe(getClass().getSimpleName() + ": Error reading servername.xml file: " + e);
		}
	}
	
	private void loadRegisteredGameServers()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM gameservers");
			ResultSet rset = statement.executeQuery())
		{
			while (rset.next())
			{
				int id = rset.getInt("server_id");
				GameServerInfo gsi = new GameServerInfo(id, stringToHex(rset.getString("hexid")));
				_gameServerTable.put(id, gsi);
			}
			
			_log.info("Loaded " + _gameServerTable.size() + " registered Game Servers.");
		}
		catch (Exception e)
		{
			_log.warning("Error while loading Server List from gameservers table.");
			e.printStackTrace();
		}
	}
	
	private void loadRSAKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
	{
		KeyPairGenerator _keyGen = KeyPairGenerator.getInstance("RSA");
		RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4);
		_keyGen.initialize(spec);
		
		_keyPairs = new KeyPair[KEYS_SIZE];
		for (int i = 0; i < KEYS_SIZE; i++)
		{
			_keyPairs[i] = _keyGen.generateKeyPair();
		}
		
		_log.info("Cached " + _keyPairs.length + " RSA keys for Game Server communication.");
	}
	
	public Map<Integer, GameServerInfo> getRegisteredGameServers()
	{
		return _gameServerTable;
	}
	
	public GameServerInfo getRegisteredGameServerById(int id)
	{
		return _gameServerTable.get(id);
	}
	
	public boolean hasRegisteredGameServerOnId(int id)
	{
		return _gameServerTable.containsKey(id);
	}
	
	public boolean registerWithFirstAvailableId(GameServerInfo gsi)
	{
		// Avoid two servers registering with the same "free" id
		synchronized (_gameServerTable)
		{
			for (Entry<Integer, String> entry : _serverNames.entrySet())
			{
				if (!_gameServerTable.containsKey(entry.getKey()))
				{
					_gameServerTable.put(entry.getKey(), gsi);
					gsi.setId(entry.getKey());
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean register(int id, GameServerInfo gsi)
	{
		// Avoid two servers registering with the same id
		synchronized (_gameServerTable)
		{
			if (!_gameServerTable.containsKey(id))
			{
				_gameServerTable.put(id, gsi);
				gsi.setId(id);
				return true;
			}
		}
		return false;
	}
	
	public void registerServerOnDB(GameServerInfo gsi)
	{
		this.registerServerOnDB(gsi.getHexId(), gsi.getId(), gsi.getExternalHost());
	}
	
	public void registerServerOnDB(byte[] hexId, int id, String externalHost)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO gameservers (hexid,server_id,host) values (?,?,?)"))
		{
			statement.setString(1, hexToString(hexId));
			statement.setInt(2, id);
			statement.setString(3, externalHost);
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			_log.warning("SQL error while saving gameserver :" + e);
		}
	}
	
	public String getServerNameById(int id)
	{
		return getServerNames().get(id);
	}
	
	public Map<Integer, String> getServerNames()
	{
		return _serverNames;
	}
	
	public KeyPair getKeyPair()
	{
		return _keyPairs[Rnd.nextInt(10)];
	}
	
	private byte[] stringToHex(String string)
	{
		return new BigInteger(string, 16).toByteArray();
	}
	
	private String hexToString(byte[] hex)
	{
		if (hex == null)
		{
			return "null";
		}
		return new BigInteger(hex).toString(16);
	}
	
	/**
	 * A status report used by Telnet.
	 * 
	 * @return
	 */
	public List<String> getStatusReport()
	{
		List<String> report = new ArrayList<>();
		report.add("There are " + _gameServerTable.size() + " GameServers");
		for (GameServerInfo gsi : _gameServerTable.values())
		{
			String content = "GameServer: " + _serverNames.get(gsi.getId()) + " Server ID: " + gsi.getId() + " Hex ID: " + hexToString(gsi.getHexId()) 
			+ " External IP: " + gsi.getExternalIp() + " Port: " + gsi.getPort() + " Status: " + ServerStatus.statusString[gsi.getStatus()];
			report.add(content);
		}
		return report;
	}
	
	public static class GameServerInfo
	{
		// auth
		private int _id;
		private byte[] _hexId;
		private boolean _isAuthed;
		
		// status
		private GameServerThread _gst;
		private int _status;
		
		// network
		private String _internalIp;
		private String _externalIp;
		private String _externalHost;
		private int _port;
		
		// config
		private boolean _isPvp = true;
		private boolean _isTestServer;
		private boolean _isShowingClock;
		private boolean _isShowingBrackets;
		private int _maxPlayers;
		
		public GameServerInfo(int id, byte[] hexId, GameServerThread gst)
		{
			_id = id;
			_hexId = hexId;
			_gst = gst;
			_status = ServerStatus.STATUS_DOWN;
		}
		
		public GameServerInfo(int id, byte[] hexId)
		{
			this(id, hexId, null);
		}
		
		public void setId(int id)
		{
			_id = id;
		}
		
		public int getId()
		{
			return _id;
		}
		
		public byte[] getHexId()
		{
			return _hexId;
		}
		
		public void setAuthed(boolean isAuthed)
		{
			_isAuthed = isAuthed;
		}
		
		public boolean isAuthed()
		{
			return _isAuthed;
		}
		
		public void setGameServerThread(GameServerThread gst)
		{
			_gst = gst;
		}
		
		public GameServerThread getGameServerThread()
		{
			return _gst;
		}
		
		public void setStatus(int status)
		{
			_status = status;
		}
		
		public int getStatus()
		{
			return _status;
		}
		
		public int getCurrentPlayerCount()
		{
			if (_gst == null)
				return 0;
			return _gst.getPlayerCount();
		}
		
		public void setInternalIp(String internalIp)
		{
			_internalIp = internalIp;
		}
		
		public String getInternalIp()
		{
			return _internalIp;
		}
		
		public void setExternalIp(String externalIp)
		{
			_externalIp = externalIp;
		}
		
		public String getExternalIp()
		{
			return _externalIp;
		}
		
		public void setExternalHost(String externalHost)
		{
			_externalHost = externalHost;
		}
		
		public String getExternalHost()
		{
			return _externalHost;
		}
		
		public int getPort()
		{
			return _port;
		}
		
		public void setPort(int port)
		{
			_port = port;
		}
		
		public void setMaxPlayers(int maxPlayers)
		{
			_maxPlayers = maxPlayers;
		}
		
		public int getMaxPlayers()
		{
			return _maxPlayers;
		}
		
		public boolean isPvp()
		{
			return _isPvp;
		}
		
		public void setTestServer(boolean val)
		{
			_isTestServer = val;
		}
		
		public boolean isTestServer()
		{
			return _isTestServer;
		}
		
		public void setShowingClock(boolean clock)
		{
			_isShowingClock = clock;
		}
		
		public boolean isShowingClock()
		{
			return _isShowingClock;
		}
		
		public void setShowingBrackets(boolean val)
		{
			_isShowingBrackets = val;
		}
		
		public boolean isShowingBrackets()
		{
			return _isShowingBrackets;
		}
		
		public void setDown()
		{
			setAuthed(false);
			setPort(0);
			setGameServerThread(null);
			setStatus(ServerStatus.STATUS_DOWN);
		}
	}
	
	private static class SingletonHolder
	{
		protected static final GameServerTable _instance = new GameServerTable();
	}
}