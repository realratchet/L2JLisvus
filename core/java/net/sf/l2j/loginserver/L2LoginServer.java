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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.Server;
import net.sf.l2j.mmocore.SelectorConfig;
import net.sf.l2j.mmocore.SelectorThread;
import net.sf.l2j.status.Status;
import net.sf.l2j.util.UPnPService;

/**
 *
 * @author  KenM
 */
public class L2LoginServer
{
	private static final Logger _log = Logger.getLogger(L2LoginServer.class.getName());

	private static L2LoginServer _loginServer;
	private static GameServerListener _gameServerListener;
	private static Status _statusServer;
	
	private SelectorThread<L2LoginClient> _selectorThread;

	public static int PROTOCOL_REV = 0x0102;

	public static void main(String[] args)
	{
		_loginServer = new L2LoginServer();
	}
	
	public static L2LoginServer getInstance()
	{
		return _loginServer;
	}
	
	private L2LoginServer()
	{
		Server.SERVER_MODE = Server.MODE_LOGINSERVER;

		// Load log folder first
		loadLogFolder();

		// Initialize config
		Config.load();
		
		// Prepare Database
		L2DatabaseFactory.getInstance();

		LoginController.getInstance();
		GameServerTable.getInstance();
		
		// Load Ban file
		loadBanFile();

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				shutdown(false);
			}
	    });

		InetAddress bindAddress = null;
		if (!Config.LOGIN_BIND_ADDRESS.equals("*"))
		{
			try
			{
				bindAddress = InetAddress.getByName(Config.LOGIN_BIND_ADDRESS);
			}
			catch (UnknownHostException e)
			{
				_log.warning("WARNING: The LoginServer bind address is invalid, using all avaliable IPs. Reason: " + e.getMessage());
				if (Config.DEVELOPER)
				{
					e.printStackTrace();
				}
			}
		}
		
		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = Config.MMO_MAX_READ_PER_PASS;
		sc.MAX_SEND_PER_PASS = Config.MMO_MAX_SEND_PER_PASS;
		sc.SLEEP_TIME = Config.MMO_SELECTOR_SLEEP_TIME;
		sc.HELPER_BUFFER_COUNT = Config.MMO_HELPER_BUFFER_COUNT;
		
		final L2LoginPacketHandler lph = new L2LoginPacketHandler();
		final SelectorHelper sh = new SelectorHelper();
		try
		{
			_selectorThread = new SelectorThread<>(sc, sh, lph, sh, sh);
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, "FATAL: Failed to open Selector. Reason: " + e.getMessage(), e);
			if (Config.DEVELOPER)
			{
				e.printStackTrace();
			}
			System.exit(1);
		}

		try
		{
			_gameServerListener = new GameServerListener();
			_gameServerListener.start();
			_log.info("Listening for GameServers on " + Config.GAME_SERVER_LOGIN_HOST + ":" + Config.GAME_SERVER_LOGIN_PORT);
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, "FATAL: Failed to start the Game Server Listener. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		
		if (Config.IS_TELNET_ENABLED)
		{
			try
			{
				_statusServer = new Status(Server.SERVER_MODE);
				_statusServer.start();
			}
			catch (IOException e)
			{
				_log.severe("Failed to start the Telnet Server. Reason: " + e.getMessage());
				if (Config.DEVELOPER)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			_log.info("Telnet server is currently disabled.");
		}
		
		try
		{
			_selectorThread.openServerSocket(bindAddress, Config.PORT_LOGIN);
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, "FATAL: Failed to open server socket. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		_selectorThread.start();
		
		_log.info("Login Server ready on " + Config.LOGIN_BIND_ADDRESS + ":" + Config.PORT_LOGIN);
		
		UPnPService.getInstance();
	}

	private static void loadLogFolder()
	{
		// Local Constants
		final String LOG_FOLDER = "log"; // Name of folder for log file
		final String LOG_NAME = "./log.cfg"; // Name of log file

		/*** Main ***/
		// Create log folder
		File logFolder = new File(Config.DATAPACK_ROOT, LOG_FOLDER);
		logFolder.mkdir();

		// Create input stream for log file -- or store file data into memory
		try (InputStream is = new FileInputStream(new File(LOG_NAME)))
		{
			LogManager.getLogManager().readConfiguration(is);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void loadBanFile()
	{
		File bannedFile = new File("./banned_ip.cfg");
		if (bannedFile.exists() && bannedFile.isFile())
		{
			String line;
			String[] parts;

			try (FileInputStream fis = new FileInputStream(bannedFile);
				InputStreamReader ir = new InputStreamReader(fis);
				LineNumberReader reader = new LineNumberReader(ir))
			{
				while ((line = reader.readLine()) != null)
				{
					line = line.trim();
					// check if this line isnt a comment line
					if ((line.length() > 0) && (line.charAt(0) != '#'))
					{
						// split comments if any
						parts = line.split("#");

						// discard comments in the line, if any
						line = parts[0];
						parts = line.split(" ");

						String address = parts[0];
						long duration = 0;

						if (parts.length > 1)
						{
							try
							{
								duration = Long.parseLong(parts[1]);
							}
							catch (NumberFormatException e)
							{
								_log.warning("Skipped: Incorrect ban duration (" + parts[1] + ") on (" + bannedFile.getName() + "). Line: " + reader.getLineNumber());
								continue;
							}
						}

						LoginController.getInstance().addBannedIP(address, duration);
					}
				}
			}
			catch (IOException e)
			{
				_log.warning("Error while reading the bans file (" + bannedFile.getName() + "). Details: " + e.getMessage());
				if (Config.DEVELOPER)
				{
					e.printStackTrace();
				}
			}
			_log.config("Loaded " + LoginController.getInstance().getBannedIps().size() + " IP Bans.");
		}
		else
		{
			_log.config("IP Bans file (" + bannedFile.getName() + ") is missing or is a directory, skipped.");
		}
	}

	public static Status getStatusServer()
	{
		return _statusServer;
	}

	public static GameServerListener getGameServerListener()
	{
		return _gameServerListener;
	}

	public static class ForeignConnection
	{
		/**
		 * @param time
		 */
		public ForeignConnection(long time)
		{
			lastConnection = time;
			connectionNumber = 1;
		}

		public int connectionNumber;
		public long lastConnection;
	}
	
	public void shutdown(boolean restart)
	{
		Runtime.getRuntime().halt(restart ? 2 : 0);
	}
}