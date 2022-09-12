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

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.Server;
import net.sf.l2j.gameserver.cache.CrestCache;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.communitybbs.Manager.ForumsBBSManager;
import net.sf.l2j.gameserver.datatables.AdminCommandRightsData;
import net.sf.l2j.gameserver.datatables.ArmorSetsTable;
import net.sf.l2j.gameserver.datatables.BufferTable;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.datatables.CharTemplateTable;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.ExtractableItemsData;
import net.sf.l2j.gameserver.datatables.FishTable;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.HelperBuffTable;
import net.sf.l2j.gameserver.datatables.HennaTable;
import net.sf.l2j.gameserver.datatables.HennaTreeTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.NobleSkillTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.NpcWalkerRoutesTable;
import net.sf.l2j.gameserver.datatables.OfflineTradersTable;
import net.sf.l2j.gameserver.datatables.PetDataTable;
import net.sf.l2j.gameserver.datatables.SkillSpellbookTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SkillTreeTable;
import net.sf.l2j.gameserver.datatables.SoulCrystalData;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.datatables.StaticObjects;
import net.sf.l2j.gameserver.datatables.SummonItemsData;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.geoengine.geoeditorcon.GeoEditorListener;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.ItemHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.handler.UserCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.AuctionManager;
import net.sf.l2j.gameserver.instancemanager.BoatManager;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.CoupleManager;
import net.sf.l2j.gameserver.instancemanager.DayNightSpawnManager;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.instancemanager.FourSepulchersManager;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.instancemanager.ItemsOnGroundManager;
import net.sf.l2j.gameserver.instancemanager.MercTicketManager;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.instancemanager.games.Lottery;
import net.sf.l2j.gameserver.model.AutoChatHandler;
import net.sf.l2j.gameserver.model.AutoSpawnHandler;
import net.sf.l2j.gameserver.model.L2Manor;
import net.sf.l2j.gameserver.model.L2Multisell;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.PartyMatchRoomList;
import net.sf.l2j.gameserver.model.entity.AutoRewarder;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.L2GamePacketHandler;
import net.sf.l2j.gameserver.pathfinding.PathFinding;
import net.sf.l2j.gameserver.scripting.L2ScriptEngineManager;
import net.sf.l2j.gameserver.scripting.scriptengine.faenor.FaenorScriptEngine;
import net.sf.l2j.gameserver.taskmanager.AutoAnnounceTaskManager;
import net.sf.l2j.gameserver.taskmanager.KnownListUpdateTaskManager;
import net.sf.l2j.gameserver.taskmanager.TaskManager;
import net.sf.l2j.gameserver.util.DynamicExtension;
import net.sf.l2j.mmocore.SelectorConfig;
import net.sf.l2j.mmocore.SelectorThread;
import net.sf.l2j.status.Status;
import net.sf.l2j.util.DeadLockDetector;
import net.sf.l2j.util.IPv4Filter;
import net.sf.l2j.util.UPnPService;

/**
 * This class ...
 * @version $Revision: 1.29.2.15.2.19 $ $Date: 2005/04/05 19:41:23 $
 */
public class GameServer
{
	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	private final SelectorThread<L2GameClient> _selectorThread;
	private final DeadLockDetector _deadDetectThread;
	private static GameServer _gameServer;
	
	private final Shutdown _shutdownHandler;
	private final LoginServerThread _loginThread;

	private static Status _statusServer;

	private static final Calendar _dateTimeServerStarted = Calendar.getInstance();

	public static GameServer getGameServer()
	{
		return _gameServer;
	}
	
	public static Status getStatusServer()
	{
		return _statusServer;
	}
	
	public static Calendar getDateTimeServerStarted()
	{
		return _dateTimeServerStarted;
	}
	
	public long getUsedMemoryMB()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576; // 1024 * 1024 = 1048576
	}

	public SelectorThread<L2GameClient> getSelectorThread()
	{
		return _selectorThread;
	}

	public GameServer() throws Exception
	{
		long serverLoadStart = System.currentTimeMillis();
		_gameServer = this;
		_log.finest("used mem:" + getUsedMemoryMB() + "MB");

		_log.info("L2JLisvus tag: " + Config.PROJECT_TAG);
		
		IdFactory.getInstance();
		ThreadPoolManager.getInstance();

		new File(Config.DATAPACK_ROOT, "data/crests").mkdirs();
		new File("log/game").mkdirs();

		// Load script engines
		L2ScriptEngineManager.getInstance();

		// Initialize game time controller
		GameTimeController.getInstance();
		
		// NPC and AIO Buffs
		BufferTable.getInstance();

		CharTemplateTable.getInstance();
		SkillTreeTable.getInstance();
		SkillTable.getInstance();
		
		ItemTable.getInstance();
		ExtractableItemsData.getInstance();
		SummonItemsData.getInstance();

		TradeController.getInstance();
		L2Multisell.getInstance();

		// L2EMU_ADD by Rayan. L2J - BigBro
		if (Config.ALLOW_NPC_WALKERS)
		{
			NpcWalkerRoutesTable.getInstance();
		}

		RecipeController.getInstance();

		ArmorSetsTable.getInstance();
		FishTable.getInstance();

		if (Config.SP_BOOK_NEEDED)
		{
			SkillSpellbookTable.getInstance();
		}

		CharNameTable.getInstance();
		NobleSkillTable.getInstance();

		// Call to load caches
		HtmCache.getInstance();
		CrestCache.getInstance();

		// forum has to be loaded before clan data, because of last forum id used should have also memo included
		if (Config.COMMUNITY_TYPE > 0)
		{
			ForumsBBSManager.getInstance().initRoot();
		}

		ClanTable.getInstance();

		GeoData.getInstance();
		if (Config.PATHFINDING > 0)
		{
			PathFinding.getInstance();
		}

		NpcTable.getInstance();

		HennaTable.getInstance();
		HennaTreeTable.getInstance();
		SoulCrystalData.getInstance();
		HelperBuffTable.getInstance();

		L2World.getInstance();
		
		Announcements.getInstance();
		
		// Load clan hall data before zone data
		ClanHallManager.getInstance();
		CastleManager.getInstance();
		SiegeManager.getInstance().initializeCastleSieges();

		TeleportLocationTable.getInstance();
		GrandBossManager.getInstance();

		// Zone Data
		ZoneManager.getInstance();
		GrandBossManager.getInstance().initZones();

		// Spawn Data
		DayNightSpawnManager.getInstance();
		SpawnTable.getInstance();
		RaidBossSpawnManager.getInstance();
		DayNightSpawnManager.getInstance().notifyChangeMode();
		DimensionalRiftManager.getInstance();
		FourSepulchersManager.getInstance().init();
		
		// Start game time controller task
		GameTimeController.getInstance().start();

		MapRegionTable.getInstance();
		EventDroplist.getInstance();

		/** Load Manor Data */
		L2Manor.getInstance();

		AuctionManager.getInstance();
		
		Lottery.getInstance();

		BoatManager.getInstance();
		CastleManorManager.getInstance();

		MercTicketManager.getInstance();
		PartyMatchRoomList.getInstance();
		PetitionManager.getInstance();
		QuestManager.getInstance();

		_log.info("Loading Server Scripts");
		File scripts = new File(Config.DATAPACK_ROOT + "/data/scripts.cfg");
		L2ScriptEngineManager.getInstance().executeScriptList(scripts);
		
		QuestManager.getInstance().report();

		if (Config.SAVE_DROPPED_ITEM)
		{
			ItemsOnGroundManager.getInstance();
		}

		if (Config.AUTODESTROY_ITEM_AFTER > 0)
		{
			ItemsAutoDestroy.getInstance();
		}

		MonsterRace.getInstance();

		DoorTable.getInstance();
		CastleManager.getInstance().loadDoorUpgrades();

		StaticObjects.getInstance();
		
		SevenSigns.getInstance();
		SevenSignsFestival.getInstance();
		AutoSpawnHandler.getInstance();
		AutoChatHandler.getInstance();

		// Spawn the Orators/Preachers if in the Seal Validation period.
		SevenSigns.getInstance().spawnSevenSignsNPC();

		Olympiad.getInstance();
		Hero.getInstance();

		FaenorScriptEngine.getInstance();

		_log.config("AutoSpawnHandler: Loaded " + AutoSpawnHandler.getInstance().size() + " handlers in total.");
		_log.config("AutoChatHandler: Loaded " + AutoChatHandler.getInstance().size() + " handlers in total.");

		// Handlers
		ItemHandler.getInstance().load();
		SkillHandler.getInstance().load();
		AdminCommandHandler.getInstance().load();
		UserCommandHandler.getInstance().load();
		VoicedCommandHandler.getInstance().load();
		
		AdminCommandRightsData.getInstance();
		
		TaskManager.getInstance();
		GmListTable.getInstance();

		// Load pet data from db
		PetDataTable.getInstance().loadPetData();

		if (Config.ACCEPT_GEOEDITOR_CONN)
		{
			GeoEditorListener.getInstance();
		}

		_shutdownHandler = Shutdown.getInstance();
		Runtime.getRuntime().addShutdownHook(_shutdownHandler);

		_log.config("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());

		// Initialize the dynamic extension loader
		try
		{
			DynamicExtension.getInstance();
		}
		catch (Exception ex)
		{
			_log.log(Level.WARNING, "DynamicExtension could not be loaded and initialized", ex);
		}
		
		// Wedding system
		CoupleManager.getInstance();

		// Offline store system
		if ((Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) && Config.RESTORE_OFFLINERS)
		{
			OfflineTradersTable.restoreOfflineTraders();
		}

		if (Config.ALLOW_AUTO_REWARDER)
		{
			AutoRewarder.load();
		}

		// Initialize Event Engine
		EventEngine.getInstance();
		
		KnownListUpdateTaskManager.getInstance();
		if (Config.DEADLOCK_DETECTOR)
		{
			_deadDetectThread = new DeadLockDetector();
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		}
		else
		{
			_deadDetectThread = null;
		}

		System.gc();
		// maxMemory is the upper limit the JVM can use, totalMemory the size of the current allocation pool, freeMemory the unused memory in the allocation pool
		long freeMem = ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / 1048576; // 1024 * 1024 = 1048576
		long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
		_log.info("GameServer Started, free memory " + freeMem + " Mb of " + totalMem + " Mb");
		Toolkit.getDefaultToolkit().beep();
		_loginThread = LoginServerThread.getInstance();
		_loginThread.start();

		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = Config.MMO_MAX_READ_PER_PASS;
		sc.MAX_SEND_PER_PASS = Config.MMO_MAX_SEND_PER_PASS;
		sc.SLEEP_TIME = Config.MMO_SELECTOR_SLEEP_TIME;
		sc.HELPER_BUFFER_COUNT = Config.MMO_HELPER_BUFFER_COUNT;
		
		final L2GamePacketHandler gph = new L2GamePacketHandler();
		_selectorThread = new SelectorThread<>(sc, gph, gph, gph, new IPv4Filter());

		InetAddress bindAddress = null;
		if (!Config.GAMESERVER_HOSTNAME.equals("*"))
		{
			try
			{
				bindAddress = InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
			}
			catch (UnknownHostException e1)
			{
				_log.log(Level.SEVERE, "WARNING: The GameServer bind address is invalid, using all avaliable IPs. Reason: " + e1.getMessage(), e1);
			}
		}

		try
		{
			_selectorThread.openServerSocket(bindAddress, Config.PORT_GAME);
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, "FATAL: Failed to open server socket. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		_selectorThread.start();

		_log.config("Maximum Number of Connected Players: " + Config.MAXIMUM_ONLINE_USERS);
		_log.log(Level.INFO, getClass().getSimpleName() + ": Server loaded in " + ((System.currentTimeMillis() - serverLoadStart) / 1000) + " seconds.");

		AutoAnnounceTaskManager.getInstance();
		UPnPService.getInstance();
	}

	public static void main(String[] args) throws Exception
	{
		Server.SERVER_MODE = Server.MODE_GAMESERVER;

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

		// Initialize config
		Config.load();

		L2DatabaseFactory.getInstance();
		_gameServer = new GameServer();

		if (Config.IS_TELNET_ENABLED)
		{
			_statusServer = new Status(Server.SERVER_MODE);
			_statusServer.start();
		}
		else
		{
			_log.info("Telnet server is currently disabled.");
		}
	}
}