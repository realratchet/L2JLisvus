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
package net.sf.l2j.gameserver;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.OfflineTradersTable;
import net.sf.l2j.gameserver.geoengine.geoeditorcon.GeoEditorListener;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.instancemanager.ItemsOnGroundManager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.gameserverpackets.ServerStatus;
import net.sf.l2j.util.UPnPService;

/**
 * This class provides the functions for shutting down and restarting the server It closes all open client connections and saves all data.
 * @version $Revision: 1.2.4.5 $ $Date: 2005/03/27 15:29:09 $
 */
public class Shutdown extends Thread
{
	private static Logger _log = Logger.getLogger(Shutdown.class.getName());
	private static Shutdown _counterInstance = null;
	
	private int _secondsShut;
	private int _shutdownMode;
	
	public static final int SIGTERM = 0;
	public static final int GM_SHUTDOWN = 1;
	public static final int GM_RESTART = 2;
	public static final int ABORT = 3;
	
	private static String[] _modeText =
	{
		"SIGTERM",
		"shutting down",
		"restarting",
		"aborting"
	};
	
	public static Shutdown getInstance()
	{
		return SingletonHolder._instance;
	}
	
	/**
	 * Default constructor is only used internal to create the shutdown-hook instance
	 */
	public Shutdown()
	{
		_secondsShut = -1;
		_shutdownMode = SIGTERM;
	}
	
	/**
	 * This creates a countdown instance of Shutdown.
	 * @param seconds how many seconds until shutdown
	 * @param restart true is the server shall restart after shutdown
	 */
	public Shutdown(int seconds, boolean restart)
	{
		if (seconds < 0)
		{
			seconds = 0;
		}
		
		_secondsShut = seconds;
		
		if (restart)
		{
			_shutdownMode = GM_RESTART;
		}
		else
		{
			_shutdownMode = GM_SHUTDOWN;
		}
	}
	
	/**
	 * this function is called, when a new thread starts if this thread is the thread of getInstance, then this is the shutdown hook and we save all data and disconnect all clients.
	 * After this thread ends, the server will completely exit if this is not the thread of getInstance, then this is a countdown thread.
	 * We start the countdown, and when we finished it, and it was not aborted, we tell the shutdown-hook why we call exit, and then call exit when the exit status of the server is 1, startServer.sh / startServer.bat will restart the server.
	 */
	@Override
	public void run()
	{
		// Disallow new logins
		if (this == SingletonHolder._instance)
		{
			try
			{
				UPnPService.getInstance().removeAllPorts();
				_log.info("UPnP Service: All port mappings have been deleted.");
			}
			catch (Throwable t)
			{
				_log.warning("Error while removing UPnP port mappings!" + t);
			}
			
			if (Config.ACCEPT_GEOEDITOR_CONN)
			{
				GeoEditorListener.getInstance().quit();
			}
			
			try
			{
				if ((Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) && Config.RESTORE_OFFLINERS)
				{
					OfflineTradersTable.storeOffliners();
				}
			}
			catch (Throwable t)
			{
			}
			
			try
			{
				disconnectAllCharacters();
				_log.info("All players have been disconnected.");
			}
			catch (Throwable t)
			{
			}
			
			// Ensure all services are stopped
			try
			{
				GameTimeController.getInstance().stopTimer();
			}
			catch (Throwable t)
			{
			}
			
			// Stop all thread pools
			try
			{
				ThreadPoolManager.getInstance().shutdown();
			}
			catch (Throwable t)
			{
			}
			
			try
			{
				LoginServerThread.getInstance().interrupt();
			}
			catch (Throwable t)
			{
			}
			
			// Last bye bye, save all data and quit this server
			saveData();
			
			// saveData sends messages to exit players, so shutdown selector after it
			try
			{
				GameServer.getGameServer().getSelectorThread().shutdown();
			}
			catch (Throwable t)
			{
			}
			
			// Commit data, last chance
			try
			{
				L2DatabaseFactory.getInstance().shutdown();
			}
			catch (Throwable t)
			{
			}
			
			// Server will quit, when this function ends.
			if (_shutdownMode == GM_RESTART)
			{
				Runtime.getRuntime().halt(2);
			}
			else
			{
				Runtime.getRuntime().halt(0);
			}
		}
		else
		{
			// gm shutdown: send warnings and then call exit to start shutdown sequence
			countdown();
			
			// last point where logging is operational :(
			_log.warning("GM shutdown countdown is over. " + _modeText[_shutdownMode] + " NOW!");
			switch (_shutdownMode)
			{
				case GM_SHUTDOWN:
					SingletonHolder._instance.setMode(GM_SHUTDOWN);
					System.exit(0);
					break;
				case GM_RESTART:
					SingletonHolder._instance.setMode(GM_RESTART);
					System.exit(2);
					break;
			}
		}
	}
	
	/**
	 * This functions starts a shutdown countdown
	 * @param activeChar GM who issued the shutdown command
	 * @param seconds seconds until shutdown
	 * @param restart true if the server will restart after shutdown
	 */
	public void startShutdown(L2PcInstance activeChar, int seconds, boolean restart)
	{
		Announcements an = Announcements.getInstance();
		
		if (activeChar != null)
		{
			_log.warning("GM: " + activeChar.getName() + "(" + activeChar.getObjectId() + ") issued shutdown command. " + _modeText[_shutdownMode] + " in " + seconds + " seconds!");
		}
		
		if (restart)
		{
			_shutdownMode = GM_RESTART;
		}
		else
		{
			_shutdownMode = GM_SHUTDOWN;
		}
		
		if (_shutdownMode > 0)
		{
			an.announceToAll("Attention players!");
			an.announceToAll("Server is " + _modeText[_shutdownMode] + " in " + seconds + " seconds!");
			if ((_shutdownMode == 1) || (_shutdownMode == 2))
			{
				an.announceToAll("Please, avoid using Gatekeepers/SoE");
				an.announceToAll("during server " + _modeText[_shutdownMode] + " procedure.");
			}
		}
		
		if (_counterInstance != null)
		{
			_counterInstance._abort();
		}
		
		// The main instance should only run for shutdown hook, so we start a new instance
		_counterInstance = new Shutdown(seconds, restart);
		_counterInstance.start();
	}
	
	/**
	 * This function aborts a running countdown
	 * @param activeChar GM who issued the abort command
	 */
	public void abort(L2PcInstance activeChar)
	{
		_log.warning("GM: " + activeChar.getName() + "(" + activeChar.getObjectId() + ") issued shutdown ABORT. " + _modeText[_shutdownMode] + " has been stopped!");
		
		if (_counterInstance != null)
		{
			_counterInstance._abort();
			Announcements _an = Announcements.getInstance();
			_an.announceToAll("Server aborts " + _modeText[_shutdownMode] + " and continues normal operation!");
		}
	}
	
	/**
	 * set the shutdown mode
	 * @param mode what mode shall be set
	 */
	private void setMode(int mode)
	{
		_shutdownMode = mode;
	}
	
	/**
	 * set shutdown mode to ABORT
	 */
	private void _abort()
	{
		_shutdownMode = ABORT;
	}
	
	/**
	 * this counts the countdown and reports it to all players countdown is aborted if mode changes to ABORT
	 */
	private void countdown()
	{
		Announcements _an = Announcements.getInstance();
		
		try
		{
			while (_secondsShut > 0)
			{
				switch (_secondsShut)
				{
					case 540:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 9 minutes.");
						break;
					case 480:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 8 minutes.");
						break;
					case 420:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 7 minutes.");
						break;
					case 360:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 6 minutes.");
						break;
					case 300:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 5 minutes.");
						break;
					case 240:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 4 minutes.");
						break;
					case 180:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 3 minutes.");
						break;
					case 120:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 2 minutes.");
						break;
					case 60:
						LoginServerThread.getInstance().setServerStatus(ServerStatus.STATUS_DOWN); // prevents new players from logging in
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 1 minute.");
						break;
					case 30:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 30 seconds.");
						break;
					case 5:
						_an.announceToAll("The server is " + _modeText[_shutdownMode] + " in 5 seconds, please log out NOW !");
						break;
				}
				
				_secondsShut--;
				
				Thread.sleep(1000);
				
				if (_shutdownMode == ABORT)
				{
					break;
				}
			}
		}
		catch (InterruptedException e)
		{
			// this will never happen
		}
	}
	
	/**
	 * This sends a last bye bye, disconnects all players and saves data.
	 */
	private void saveData()
	{
		switch (_shutdownMode)
		{
			case SIGTERM:
				_log.info("SIGTERM received. Shutting down NOW!");
				break;
			case GM_SHUTDOWN:
				_log.info("GM shutdown received. Shutting down NOW!");
				break;
			case GM_RESTART:
				_log.info("GM restart received. Restarting NOW!");
				break;
		}

		// Seven Signs data is now saved along with Festival data.
		if (!SevenSigns.getInstance().isSealValidationPeriod())
		{
			SevenSignsFestival.getInstance().saveFestivalData(false);
		}
		
		// Save Seven Signs data before closing. :)
		SevenSigns.getInstance().saveSevenSignsData(null, true);
		
		// Cancel timers and save all global (non-player specific) Quest data that needs to persist after reboot
		QuestManager.getInstance().save();
		
		// Save all raidboss and grandboss status ^_^
		RaidBossSpawnManager.getInstance().cleanUp();
		_log.info("RaidBossSpawnManager: All Raid Boss info saved!!");
		GrandBossManager.getInstance().cleanUp();
		_log.info("GrandBossManager: All Grand Boss info saved!!");
		
		TradeController.getInstance().dataCountStore();
		_log.info("TradeController: All count Item Saved!!");
		
		Olympiad.getInstance().save();
		_log.info("Olympiad System: Data saved!!");
		
		// Save all manor data
		CastleManorManager.getInstance().save();
		
		// Save items on ground before closing
		if (Config.SAVE_DROPPED_ITEM)
		{
			ItemsOnGroundManager.getInstance().saveInDb();
			ItemsOnGroundManager.getInstance().cleanUp();
			_log.info("ItemsOnGroundManager: All items on ground saved!");
		}
		
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			// never happens :p
		}
	}
	
	/**
	 * this disconnects all clients from the server
	 */
	private void disconnectAllCharacters()
	{
		for (L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
			// Logout Character
			try
			{
				player.logout(false);
			}
			catch (Throwable t)
			{
			}
		}
	}
	
	private static class SingletonHolder
	{
		private static final Shutdown _instance = new Shutdown();
	}
}