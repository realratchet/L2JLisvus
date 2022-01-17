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
package net.sf.l2j.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SepulcherMonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SepulcherNpcInstance;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author sandman
 */
public class FourSepulchersManager extends GrandBossManager
{
	private static final Logger _log = Logger.getLogger(FourSepulchersManager.class.getName());
	
	private static final String QUEST_ID = "620_FourGoblets";
	
	private static final int ENTRANCE_PASS = 7075;
	private static final int USED_PASS = 7261;
	private static final int CHAPEL_KEY = 7260;
	private static final int ANTIQUE_BROOCH = 7262;
	
	protected boolean _firstTimeRun;
	protected boolean _inEntryTime = false;
	protected boolean _inAttackTime = false;
	
	protected ScheduledFuture<?> _changePendingTimeTask = null;
	protected ScheduledFuture<?> _changeEntryTimeTask = null;
	protected ScheduledFuture<?> _changeCoolDownTimeTask = null;
	
	private final int[][] _startHallSpawn =
	{
		{181632, -85587, -7218},
		{179963, -88978, -7218},
		{173217, -86132, -7218},
		{175608, -82296, -7218}
	};
	
	private final int[][][] _shadowSpawnLoc =
	{
		{
			{10339, 191231, -85574, -7216, 33380},
			{10349, 189534, -88969, -7216, 32768},
			{10346, 173195, -76560, -7215, 49277},
			{10342, 175591, -72744, -7215, 49317}
		},
		{
			{10342, 191231, -85574, -7216, 33380},
			{10339, 189534, -88969, -7216, 32768},
			{10349, 173195, -76560, -7215, 49277},
			{10346, 175591, -72744, -7215, 49317}
		},
		{
			{10346, 191231, -85574, -7216, 33380},
			{10342, 189534, -88969, -7216, 32768},
			{10339, 173195, -76560, -7215, 49277},
			{10349, 175591, -72744, -7215, 49317}
		},
		{
			{10349, 191231, -85574, -7216, 33380},
			{10346, 189534, -88969, -7216, 32768},
			{10342, 173195, -76560, -7215, 49277},
			{10339, 175591, -72744, -7215, 49317}
		},
	};
	
	protected Map<Integer, Boolean> _archonSpawned = new HashMap<>();
	protected Map<Integer, Boolean> _hallInUse = new HashMap<>();
	protected Map<Integer, int[]> _startHallSpawns = new HashMap<>();
	protected Map<Integer, Integer> _hallGateKeepers = new HashMap<>();
	protected Map<Integer, Integer> _keyBoxNpc = new HashMap<>();
	protected Map<Integer, Integer> _victim = new HashMap<>();
	protected Map<Integer, L2Spawn> _executionerSpawns = new HashMap<>();
	protected Map<Integer, L2Spawn> _keyBoxSpawns = new HashMap<>();
	protected Map<Integer, L2Spawn> _mysteriousBoxSpawns = new HashMap<>();
	protected Map<Integer, L2Spawn> _shadowSpawns = new HashMap<>();
	protected Map<Integer, List<L2Spawn>> _dukeFinalMobs = new HashMap<>();
	protected Map<Integer, List<L2SepulcherMonsterInstance>> _dukeMobs = new HashMap<>();
	protected Map<Integer, List<L2Spawn>> _emperorsGraveNpcs = new HashMap<>();
	protected Map<Integer, List<L2Spawn>> _magicalMonsters = new HashMap<>();
	protected Map<Integer, List<L2Spawn>> _physicalMonsters = new HashMap<>();
	protected Map<Integer, List<L2SepulcherMonsterInstance>> _viscountMobs = new HashMap<>();
	
	protected List<L2Spawn> _physicalSpawns;
	protected List<L2Spawn> _magicalSpawns;
	protected List<L2Spawn> _managers;
	protected List<L2Spawn> _dukeFinalSpawns;
	protected List<L2Spawn> _emperorsGraveSpawns;
	protected List<L2NpcInstance> _allMobs = new ArrayList<>();
	
	protected long _coolDownTimeEnd = 0;
	protected long _pendingTimeEnd = 0;
	protected long _entryTimeEnd = 0;
	
	protected byte _newCycleMin = 55;
	
	public static final FourSepulchersManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void init()
	{
		if (_changeCoolDownTimeTask != null)
		{
			_changeCoolDownTimeTask.cancel(true);
		}
		if (_changePendingTimeTask != null)
		{
			_changePendingTimeTask.cancel(true);
		}
		if (_changeEntryTimeTask != null)
		{
			_changeEntryTimeTask.cancel(true);
		}
		
		_changeCoolDownTimeTask = null;
		_changePendingTimeTask = null;
		_changeEntryTimeTask = null;
		
		_inAttackTime = false;
		_inEntryTime = false;
		
		_firstTimeRun = true;
		
		initFixedInfo();
		loadMysteriousBox();
		initKeyBoxSpawns();
		loadPhysicalMonsters();
		loadMagicalMonsters();
		initLocationShadowSpawns();
		initExecutionerSpawns();
		loadDukeMonsters();
		loadEmperorsGraveMonsters();
		spawnManagers();
		timeSelector();
	}
	
	// phase select on server launch
	protected void timeSelector()
	{
		timeCalculator();
		long currentTime = Calendar.getInstance().getTimeInMillis();
		
		// just to be sure
		clean();
		
		// if current time >= time of entry beginning and if current time < time
		// of entry beginning + time of entry end
		if ((currentTime >= _coolDownTimeEnd) && (currentTime < _entryTimeEnd)) // check entry time
		{
			_changeEntryTimeTask = ThreadPoolManager.getInstance().scheduleGeneral(new ChangeEntryTime(), 0);
			_log.info("FourSepulchersManager: Beginning in Entry time");
		}
		else if ((currentTime >= _entryTimeEnd) && (currentTime < _pendingTimeEnd)) // check pending time
		{
			_changePendingTimeTask = ThreadPoolManager.getInstance().scheduleGeneral(new ChangePendingTime(), 0);
			_log.info("FourSepulchersManager: Beginning in Pending time");
		}
		// else cooldown time and without cleanup because it's already implemented
		else
		{
			_changeCoolDownTimeTask = ThreadPoolManager.getInstance().scheduleGeneral(new ChangeCoolDownTime(), 0);
			_log.info("FourSepulchersManager: Beginning in Cooldown time");
		}
	}
	
	// phase end times calculator
	protected void timeCalculator()
	{
		Calendar tmp = Calendar.getInstance();
		if (tmp.get(Calendar.MINUTE) < _newCycleMin)
		{
			tmp.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR) - 1);
		}
		tmp.set(Calendar.MINUTE, _newCycleMin);
		_coolDownTimeEnd = tmp.getTimeInMillis();
		_entryTimeEnd = _coolDownTimeEnd + (Config.FS_ENTRY_TIME * 60000);
		_pendingTimeEnd = _entryTimeEnd + (Config.FS_PENDING_TIME * 60000);
	}
	
	public void clean()
	{
		_hallInUse.clear();
		
		for (int i = 13189; i <= 13192; i++)
		{
			int[] Location = _startHallSpawns.get(i);
			L2BossZone zone = GrandBossManager.getInstance().getZone(Location[0], Location[1], Location[2]);
			if (zone == null)
			{
				_log.warning("FourSepulchersManager: Missing zone data from datapack.");
				return;
			}
			
			zone.oustAllPlayers();
			_hallInUse.put(i, false);
		}
		
		deleteAllMobs();
		closeAllDoors();
		
		if (_archonSpawned.size() != 0)
		{
			Set<Integer> npcIdSet = _archonSpawned.keySet();
			for (int npcId : npcIdSet)
			{
				_archonSpawned.put(npcId, false);
			}
		}
	}
	
	protected void spawnManagers()
	{
		_managers = new ArrayList<>();
		
		int i = 13189;
		for (L2Spawn spawnDat; i <= 13192; i++)
		{
			if ((i < 13189) || (i > 13192))
			{
				continue;
			}
			
			L2NpcTemplate template1 = NpcTable.getInstance().getTemplate(i);
			if (template1 == null)
			{
				continue;
			}
			
			try
			{
				spawnDat = new L2Spawn(template1);
				spawnDat.setAmount(1);
				spawnDat.setRespawnDelay(60);
				
				switch (i)
				{
					case 13189: // conquerors
						spawnDat.setLocX(181061);
						spawnDat.setLocY(-85595);
						spawnDat.setLocZ(-7200);
						spawnDat.setHeading(-32584);
						break;
					case 13190: // emperors
						spawnDat.setLocX(179292);
						spawnDat.setLocY(-88981);
						spawnDat.setLocZ(-7200);
						spawnDat.setHeading(-33272);
						break;
					case 13191: // sages
						spawnDat.setLocX(173202);
						spawnDat.setLocY(-87004);
						spawnDat.setLocZ(-7200);
						spawnDat.setHeading(-16248);
						break;
					case 13192: // judges
						spawnDat.setLocX(175606);
						spawnDat.setLocY(-82853);
						spawnDat.setLocZ(-7200);
						spawnDat.setHeading(-16248);
						break;
				}
				
				_managers.add(spawnDat);
				SpawnTable.getInstance().addNewSpawn(spawnDat, false);
				spawnDat.doSpawn();
				spawnDat.startRespawn();
				_log.info("FourSepulchersManager: spawned " + spawnDat.getTemplate().name);
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected void initFixedInfo()
	{
		_startHallSpawns.put(13189, _startHallSpawn[0]);
		_startHallSpawns.put(13190, _startHallSpawn[1]);
		_startHallSpawns.put(13191, _startHallSpawn[2]);
		_startHallSpawns.put(13192, _startHallSpawn[3]);
		
		_hallInUse.put(13189, false);
		_hallInUse.put(13190, false);
		_hallInUse.put(13191, false);
		_hallInUse.put(13192, false);
		
		_hallGateKeepers.put(13193, 25150012);
		_hallGateKeepers.put(13194, 25150013);
		_hallGateKeepers.put(13195, 25150014);
		_hallGateKeepers.put(13196, 25150015);
		_hallGateKeepers.put(13197, 25150016);
		_hallGateKeepers.put(13198, 25150002);
		_hallGateKeepers.put(13199, 25150003);
		_hallGateKeepers.put(13200, 25150004);
		_hallGateKeepers.put(13201, 25150005);
		_hallGateKeepers.put(13202, 25150006);
		_hallGateKeepers.put(13203, 25150032);
		_hallGateKeepers.put(13204, 25150033);
		_hallGateKeepers.put(13205, 25150034);
		_hallGateKeepers.put(13206, 25150035);
		_hallGateKeepers.put(13207, 25150036);
		_hallGateKeepers.put(13208, 25150022);
		_hallGateKeepers.put(13209, 25150023);
		_hallGateKeepers.put(13210, 25150024);
		_hallGateKeepers.put(13211, 25150025);
		_hallGateKeepers.put(13212, 25150026);
		
		// Halisha\'s Officer
		_keyBoxNpc.put(12955, 8455);
		_keyBoxNpc.put(12956, 8455);
		_keyBoxNpc.put(12957, 8455);
		_keyBoxNpc.put(12958, 8455);
		_keyBoxNpc.put(12959, 8456);
		_keyBoxNpc.put(12960, 8456);
		_keyBoxNpc.put(12961, 8456);
		_keyBoxNpc.put(12962, 8456);
		_keyBoxNpc.put(12963, 8457);
		_keyBoxNpc.put(12964, 8457);
		_keyBoxNpc.put(12965, 8457);
		_keyBoxNpc.put(12966, 8457);
		
		// Beetle of Grave
		_keyBoxNpc.put(12984, 8458);
		
		// Victim
		_keyBoxNpc.put(12985, 8459);
		_keyBoxNpc.put(12986, 8459);
		_keyBoxNpc.put(12987, 8459);
		_keyBoxNpc.put(12988, 8459);
		_keyBoxNpc.put(12989, 8460);
		_keyBoxNpc.put(12990, 8460);
		_keyBoxNpc.put(12991, 8460);
		_keyBoxNpc.put(12992, 8460);
		
		// Executioner of Halisha
		_keyBoxNpc.put(12993, 8461);
		_keyBoxNpc.put(12994, 8461);
		_keyBoxNpc.put(12995, 8461);
		_keyBoxNpc.put(12996, 8461);
		_keyBoxNpc.put(12997, 8462);
		_keyBoxNpc.put(12998, 8462);
		_keyBoxNpc.put(12999, 8462);
		_keyBoxNpc.put(13000, 8462);
		
		// Halisha\'s Foreman
		_keyBoxNpc.put(13018, 8463);
		_keyBoxNpc.put(13019, 8464);
		
		// Archon of Halisha
		_keyBoxNpc.put(13047, 8465);
		_keyBoxNpc.put(13048, 8465);
		_keyBoxNpc.put(13049, 8465);
		_keyBoxNpc.put(13050, 8465);
		_keyBoxNpc.put(13051, 8466);
		_keyBoxNpc.put(13052, 8466);
		_keyBoxNpc.put(13053, 8466);
		_keyBoxNpc.put(13054, 8466);
		
		// Victim
		_victim.put(12985, 12993);
		_victim.put(12986, 12994);
		_victim.put(12987, 12995);
		_victim.put(12988, 12996);
		_victim.put(12989, 12997);
		_victim.put(12990, 12998);
		_victim.put(12991, 12999);
		_victim.put(12992, 13000);
	}
	
	private void loadMysteriousBox()
	{
		_mysteriousBoxSpawns.clear();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, key_npc_id FROM four_sepulchers_spawnlist Where spawntype = ? ORDER BY id"))
		{
			statement.setInt(1, 0);
			try (ResultSet rset = statement.executeQuery())
			{
				L2Spawn spawnDat;
				L2NpcTemplate template1;
				
				while (rset.next())
				{
					template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
					if (template1 != null)
					{
						spawnDat = new L2Spawn(template1);
						spawnDat.setAmount(rset.getInt("count"));
						spawnDat.setLocX(rset.getInt("locx"));
						spawnDat.setLocY(rset.getInt("locy"));
						spawnDat.setLocZ(rset.getInt("locz"));
						spawnDat.setHeading(rset.getInt("heading"));
						spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
						SpawnTable.getInstance().addNewSpawn(spawnDat, false);
						int keyNpcId = rset.getInt("key_npc_id");
						_mysteriousBoxSpawns.put(keyNpcId, spawnDat);
					}
					else
					{
						_log.warning("FourSepulchersManager.LoadMysteriousBox: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
					}
				}
			}
			
			_log.info("FourSepulchersManager: Loaded " + _mysteriousBoxSpawns.size() + " Mysterious-Box spawns.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("FourSepulchersManager.LoadMysteriousBox: Spawn could not be initialized: " + e);
		}
	}
	
	private void initKeyBoxSpawns()
	{
		L2Spawn spawnDat;
		L2NpcTemplate template;
		
		for (int keyNpcId : _keyBoxNpc.keySet())
		{
			try
			{
				template = NpcTable.getInstance().getTemplate(_keyBoxNpc.get(keyNpcId));
				if (template != null)
				{
					spawnDat = new L2Spawn(template);
					spawnDat.setAmount(1);
					spawnDat.setLocX(0);
					spawnDat.setLocY(0);
					spawnDat.setLocZ(0);
					spawnDat.setHeading(0);
					spawnDat.setRespawnDelay(3600);
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_keyBoxSpawns.put(keyNpcId, spawnDat);
				}
				else
				{
					_log.warning("FourSepulchersManager.InitKeyBoxSpawns: Data missing in NPC table for ID: " + _keyBoxNpc.get(keyNpcId) + ".");
				}
			}
			catch (Exception e)
			{
				_log.warning("FourSepulchersManager.InitKeyBoxSpawns: Spawn could not be initialized: " + e);
			}
		}
	}
	
	private void loadPhysicalMonsters()
	{
		_physicalMonsters.clear();
		
		int loaded = 0;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement1 = con.prepareStatement("SELECT Distinct key_npc_id FROM four_sepulchers_spawnlist Where spawntype = ? ORDER BY key_npc_id"))
		{
			statement1.setInt(1, 1);
			try (ResultSet rset1 = statement1.executeQuery())
			{
				while (rset1.next())
				{
					int keyNpcId = rset1.getInt("key_npc_id");
					
					try (PreparedStatement statement2 = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, key_npc_id FROM four_sepulchers_spawnlist Where key_npc_id = ? and spawntype = ? ORDER BY id"))
					{
						statement2.setInt(1, keyNpcId);
						statement2.setInt(2, 1);
						try (ResultSet rset2 = statement2.executeQuery())
						{
							L2Spawn spawnDat;
							L2NpcTemplate template1;
							
							_physicalSpawns = new ArrayList<>();
							
							while (rset2.next())
							{
								template1 = NpcTable.getInstance().getTemplate(rset2.getInt("npc_templateid"));
								if (template1 != null)
								{
									spawnDat = new L2Spawn(template1);
									spawnDat.setAmount(rset2.getInt("count"));
									spawnDat.setLocX(rset2.getInt("locx"));
									spawnDat.setLocY(rset2.getInt("locy"));
									spawnDat.setLocZ(rset2.getInt("locz"));
									spawnDat.setHeading(rset2.getInt("heading"));
									spawnDat.setRespawnDelay(rset2.getInt("respawn_delay"));
									SpawnTable.getInstance().addNewSpawn(spawnDat, false);
									_physicalSpawns.add(spawnDat);
									loaded++;
								}
								else
								{
									_log.warning("FourSepulchersManager.LoadPhysicalMonsters: Data missing in NPC table for ID: " + rset2.getInt("npc_templateid") + ".");
								}
							}
						}
					}
					
					_physicalMonsters.put(keyNpcId, _physicalSpawns);
				}
			}
			
			_log.info("FourSepulchersManager: Loaded " + loaded + " Physical type monsters spawns.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("FourSepulchersManager.LoadPhysicalMonsters: Spawn could not be initialized: " + e);
		}
	}
	
	private void loadMagicalMonsters()
	{
		_magicalMonsters.clear();
		
		int loaded = 0;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement1 = con.prepareStatement("SELECT Distinct key_npc_id FROM four_sepulchers_spawnlist Where spawntype = ? ORDER BY key_npc_id"))
		{
			statement1.setInt(1, 2);
			try (ResultSet rset1 = statement1.executeQuery())
			{
				while (rset1.next())
				{
					int keyNpcId = rset1.getInt("key_npc_id");
					
					try (PreparedStatement statement2 = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, key_npc_id FROM four_sepulchers_spawnlist Where key_npc_id = ? and spawntype = ? ORDER BY id"))
					{
						statement2.setInt(1, keyNpcId);
						statement2.setInt(2, 2);
						try (ResultSet rset2 = statement2.executeQuery())
						{
							L2Spawn spawnDat;
							L2NpcTemplate template1;
							
							_magicalSpawns = new ArrayList<>();
							
							while (rset2.next())
							{
								template1 = NpcTable.getInstance().getTemplate(rset2.getInt("npc_templateid"));
								if (template1 != null)
								{
									spawnDat = new L2Spawn(template1);
									spawnDat.setAmount(rset2.getInt("count"));
									spawnDat.setLocX(rset2.getInt("locx"));
									spawnDat.setLocY(rset2.getInt("locy"));
									spawnDat.setLocZ(rset2.getInt("locz"));
									spawnDat.setHeading(rset2.getInt("heading"));
									spawnDat.setRespawnDelay(rset2.getInt("respawn_delay"));
									SpawnTable.getInstance().addNewSpawn(spawnDat, false);
									_magicalSpawns.add(spawnDat);
									loaded++;
								}
								else
								{
									_log.warning("FourSepulchersManager.LoadMagicalMonsters: Data missing in NPC table for ID: " + rset2.getInt("npc_templateid") + ".");
								}
							}
						}
					}
					
					_magicalMonsters.put(keyNpcId, _magicalSpawns);
				}
			}
			
			_log.info("FourSepulchersManager: Loaded " + loaded + " Magical type monsters spawns.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("FourSepulchersManager.LoadMagicalMonsters: Spawn could not be initialized: " + e);
		}
	}
	
	private void loadDukeMonsters()
	{
		_dukeFinalMobs.clear();
		_archonSpawned.clear();
		
		int loaded = 0;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement1 = con.prepareStatement("SELECT Distinct key_npc_id FROM four_sepulchers_spawnlist Where spawntype = ? ORDER BY key_npc_id"))
		{
			statement1.setInt(1, 5);
			try (ResultSet rset1 = statement1.executeQuery())
			{
				while (rset1.next())
				{
					int keyNpcId = rset1.getInt("key_npc_id");
					
					try (PreparedStatement statement2 = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, key_npc_id FROM four_sepulchers_spawnlist Where key_npc_id = ? and spawntype = ? ORDER BY id"))
					{
						statement2.setInt(1, keyNpcId);
						statement2.setInt(2, 5);
						try (ResultSet rset2 = statement2.executeQuery())
						{
							L2Spawn spawnDat;
							L2NpcTemplate template1;
							
							_dukeFinalSpawns = new ArrayList<>();
							
							while (rset2.next())
							{
								template1 = NpcTable.getInstance().getTemplate(rset2.getInt("npc_templateid"));
								if (template1 != null)
								{
									spawnDat = new L2Spawn(template1);
									spawnDat.setAmount(rset2.getInt("count"));
									spawnDat.setLocX(rset2.getInt("locx"));
									spawnDat.setLocY(rset2.getInt("locy"));
									spawnDat.setLocZ(rset2.getInt("locz"));
									spawnDat.setHeading(rset2.getInt("heading"));
									spawnDat.setRespawnDelay(rset2.getInt("respawn_delay"));
									SpawnTable.getInstance().addNewSpawn(spawnDat, false);
									_dukeFinalSpawns.add(spawnDat);
									loaded++;
								}
								else
								{
									_log.warning("FourSepulchersManager.LoadDukeMonsters: Data missing in NPC table for ID: " + rset2.getInt("npc_templateid") + ".");
								}
							}
						}
					}
					
					_dukeFinalMobs.put(keyNpcId, _dukeFinalSpawns);
					_archonSpawned.put(keyNpcId, false);
				}
			}
			
			_log.info("FourSepulchersManager: Loaded " + loaded + " Church of duke monsters spawns.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("FourSepulchersManager.LoadDukeMonsters: Spawn could not be initialized: " + e);
		}
	}
	
	private void loadEmperorsGraveMonsters()
	{
		_emperorsGraveNpcs.clear();
		
		int loaded = 0;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement1 = con.prepareStatement("SELECT Distinct key_npc_id FROM four_sepulchers_spawnlist Where spawntype = ? ORDER BY key_npc_id"))
		{
			statement1.setInt(1, 6);
			try (ResultSet rset1 = statement1.executeQuery())
			{
				while (rset1.next())
				{
					int keyNpcId = rset1.getInt("key_npc_id");
					
					try (PreparedStatement statement2 = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, key_npc_id FROM four_sepulchers_spawnlist Where key_npc_id = ? and spawntype = ? ORDER BY id"))
					{
						statement2.setInt(1, keyNpcId);
						statement2.setInt(2, 6);
						try (ResultSet rset2 = statement2.executeQuery())
						{
							L2Spawn spawnDat;
							L2NpcTemplate template1;
							
							_emperorsGraveSpawns = new ArrayList<>();
							
							while (rset2.next())
							{
								template1 = NpcTable.getInstance().getTemplate(rset2.getInt("npc_templateid"));
								if (template1 != null)
								{
									spawnDat = new L2Spawn(template1);
									spawnDat.setAmount(rset2.getInt("count"));
									spawnDat.setLocX(rset2.getInt("locx"));
									spawnDat.setLocY(rset2.getInt("locy"));
									spawnDat.setLocZ(rset2.getInt("locz"));
									spawnDat.setHeading(rset2.getInt("heading"));
									spawnDat.setRespawnDelay(rset2.getInt("respawn_delay"));
									SpawnTable.getInstance().addNewSpawn(spawnDat, false);
									_emperorsGraveSpawns.add(spawnDat);
									loaded++;
								}
								else
								{
									_log.warning("FourSepulchersManager.LoadEmperorsGraveMonsters: Data missing in NPC table for ID: " + rset2.getInt("npc_templateid") + ".");
								}
							}
						}
					}
					_emperorsGraveNpcs.put(keyNpcId, _emperorsGraveSpawns);
				}
			}
			
			_log.info("FourSepulchersManager: Loaded " + loaded + " Emperor's grave NPC spawns.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("FourSepulchersManager.LoadEmperorsGraveMonsters: Spawn could not be initialized: " + e);
		}
	}
	
	protected void initLocationShadowSpawns()
	{
		int locNo = Rnd.get(4);
		final int[] gateKeeper =
		{
			13197,
			13202,
			13207,
			13212
		};
		
		L2Spawn spawnDat;
		L2NpcTemplate template;
		
		_shadowSpawns.clear();
		
		for (int i = 0; i <= 3; i++)
		{
			template = NpcTable.getInstance().getTemplate(_shadowSpawnLoc[locNo][i][0]);
			if (template != null)
			{
				try
				{
					spawnDat = new L2Spawn(template);
					spawnDat.setAmount(1);
					spawnDat.setLocX(_shadowSpawnLoc[locNo][i][1]);
					spawnDat.setLocY(_shadowSpawnLoc[locNo][i][2]);
					spawnDat.setLocZ(_shadowSpawnLoc[locNo][i][3]);
					spawnDat.setHeading(_shadowSpawnLoc[locNo][i][4]);
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					int keyNpcId = gateKeeper[i];
					_shadowSpawns.put(keyNpcId, spawnDat);
				}
				catch (Exception e)
				{
					_log.log(Level.SEVERE, "Error on InitLocationShadowSpawns", e);
				}
			}
			else
			{
				_log.warning("FourSepulchersManager.InitLocationShadowSpawns: Data missing in NPC table for ID: " + _shadowSpawnLoc[locNo][i][0] + ".");
			}
		}
	}
	
	protected void initExecutionerSpawns()
	{
		L2Spawn spawnDat;
		L2NpcTemplate template;
		
		for (int keyNpcId : _victim.keySet())
		{
			try
			{
				template = NpcTable.getInstance().getTemplate(_victim.get(keyNpcId));
				if (template != null)
				{
					spawnDat = new L2Spawn(template);
					spawnDat.setAmount(1);
					spawnDat.setLocX(0);
					spawnDat.setLocY(0);
					spawnDat.setLocZ(0);
					spawnDat.setHeading(0);
					spawnDat.setRespawnDelay(3600);
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_executionerSpawns.put(keyNpcId, spawnDat);
				}
				else
				{
					_log.warning("FourSepulchersManager.InitExecutionerSpawns: Data missing in NPC table for ID: " + _victim.get(keyNpcId) + ".");
				}
			}
			catch (Exception e)
			{
				_log.warning("FourSepulchersManager.InitExecutionerSpawns: Spawn could not be initialized: " + e);
			}
		}
	}
	
	public boolean isEntryTime()
	{
		return _inEntryTime;
	}
	
	public boolean isAttackTime()
	{
		return _inAttackTime;
	}
	
	public synchronized void tryEntry(L2NpcInstance npc, L2PcInstance player)
	{
		int npcId = npc.getNpcId();
		switch (npcId)
		{
			// ID ok
			case 13189:
			case 13190:
			case 13191:
			case 13192:
				break;
			// ID not ok
			default:
				if (!player.isGM())
				{
					_log.warning("Player " + player.getName() + "(" + player.getObjectId() + ") tried to cheat in four sepulchers.");
					Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " tried to enter four sepulchers with invalid npc id.", Config.DEFAULT_PUNISH);
				}
				return;
		}
		
		if (_hallInUse.get(npcId).booleanValue())
		{
			showHtmlFile(player, npcId + "-FULL.htm", npc, null);
			return;
		}
		
		if (Config.FS_PARTY_MEMBER_COUNT > 1)
		{
			if (!player.isInParty() || (player.getParty().getMemberCount() < Config.FS_PARTY_MEMBER_COUNT))
			{
				showHtmlFile(player, npcId + "-SP.htm", npc, null);
				return;
			}
			
			if (!player.getParty().isLeader(player))
			{
				showHtmlFile(player, npcId + "-NL.htm", npc, null);
				return;
			}
			
			for (L2PcInstance mem : player.getParty().getPartyMembers())
			{
				QuestState qs = mem.getQuestState(QUEST_ID);
				if ((qs == null) || (!qs.isStarted() && !qs.isCompleted()))
				{
					showHtmlFile(player, npcId + "-NS.htm", npc, mem);
					return;
				}
				
				if (mem.getInventory().getItemByItemId(ENTRANCE_PASS) == null)
				{
					showHtmlFile(player, npcId + "-SE.htm", npc, mem);
					return;
				}
				
				if (!Util.checkIfInRange(700, player, mem, true))
				{
					showHtmlFile(player, npcId + "-AWAY.htm", npc, mem);
					return;
				}
				
				if (mem.getWeightPenalty() >= 3)
				{
					mem.sendPacket(new SystemMessage(SystemMessage.INVENTORY_80_PERCENT_FOR_QUEST));
					return;
				}
			}
		}
		else
		{
			if (player.isInParty())
			{
				if (!player.getParty().isLeader(player))
				{
					showHtmlFile(player, npcId + "-NL.htm", npc, null);
					return;
				}
				
				for (L2PcInstance mem : player.getParty().getPartyMembers())
				{
					QuestState qs = mem.getQuestState(QUEST_ID);
					if ((qs == null) || (!qs.isStarted() && !qs.isCompleted()))
					{
						showHtmlFile(player, npcId + "-NS.htm", npc, mem);
						return;
					}
					
					if (mem.getInventory().getItemByItemId(ENTRANCE_PASS) == null)
					{
						showHtmlFile(player, npcId + "-SE.htm", npc, mem);
						return;
					}
					
					if (!Util.checkIfInRange(700, player, mem, true))
					{
						showHtmlFile(player, npcId + "-away.htm", npc, mem);
						return;
					}
					
					if (mem.getWeightPenalty() >= 3)
					{
						mem.sendPacket(new SystemMessage(SystemMessage.INVENTORY_80_PERCENT_FOR_QUEST));
						return;
					}
				}
			}
			else
			{
				QuestState qs = player.getQuestState(QUEST_ID);
				if ((qs == null) || (!qs.isStarted() && !qs.isCompleted()))
				{
					showHtmlFile(player, npcId + "-NS.htm", npc, player);
					return;
				}
				
				if (player.getInventory().getItemByItemId(ENTRANCE_PASS) == null)
				{
					showHtmlFile(player, npcId + "-SE.htm", npc, player);
					return;
				}
				
				if (player.getWeightPenalty() >= 3)
				{
					player.sendPacket(new SystemMessage(SystemMessage.INVENTORY_80_PERCENT_FOR_QUEST));
					return;
				}
			}
		}
		
		if (!isEntryTime())
		{
			showHtmlFile(player, npcId + "-NE.htm", npc, null);
			return;
		}
		
		showHtmlFile(player, npcId + "-OK.htm", npc, null);
		
		entry(npcId, player);
	}
	
	private void entry(int npcId, L2PcInstance player)
	{
		int[] Location = _startHallSpawns.get(npcId);
		int driftx;
		int drifty;
		
		if (Config.FS_PARTY_MEMBER_COUNT > 1)
		{
			for (L2PcInstance mem : player.getParty().getPartyMembers())
			{
				GrandBossManager.getInstance().getZone(Location[0], Location[1], Location[2]).allowPlayerEntry(mem, 30000);
				driftx = Rnd.get(-80, 80);
				drifty = Rnd.get(-80, 80);
				
				if (mem.isDead())
				{
					mem.setIsPendingRevive(true);
				}
				
				mem.teleToLocation(Location[0] + driftx, Location[1] + drifty, Location[2]);
				mem.destroyItemByItemId("Quest", ENTRANCE_PASS, 1, mem, true);
				if (mem.getInventory().getItemByItemId(ANTIQUE_BROOCH) == null)
				{
					mem.addItem("Quest", USED_PASS, 1, mem, true);
				}
				
				L2ItemInstance hallsKey = mem.getInventory().getItemByItemId(CHAPEL_KEY);
				if (hallsKey != null)
				{
					mem.destroyItemByItemId("Quest", CHAPEL_KEY, hallsKey.getCount(), mem, true);
				}
			}
		}
		else
		{
			if (player.isInParty())
			{
				for (L2PcInstance mem : player.getParty().getPartyMembers())
				{
					GrandBossManager.getInstance().getZone(Location[0], Location[1], Location[2]).allowPlayerEntry(mem, 30000);
					driftx = Rnd.get(-80, 80);
					drifty = Rnd.get(-80, 80);
					
					if (mem.isDead())
					{
						mem.setIsPendingRevive(true);
					}
					
					mem.teleToLocation(Location[0] + driftx, Location[1] + drifty, Location[2]);
					mem.destroyItemByItemId("Quest", ENTRANCE_PASS, 1, mem, true);
					if (mem.getInventory().getItemByItemId(ANTIQUE_BROOCH) == null)
					{
						mem.addItem("Quest", USED_PASS, 1, mem, true);
					}
					
					L2ItemInstance hallsKey = mem.getInventory().getItemByItemId(CHAPEL_KEY);
					if (hallsKey != null)
					{
						mem.destroyItemByItemId("Quest", CHAPEL_KEY, hallsKey.getCount(), mem, true);
					}
				}
			}
			else
			{
				GrandBossManager.getInstance().getZone(Location[0], Location[1], Location[2]).allowPlayerEntry(player, 30000);
				driftx = Rnd.get(-80, 80);
				drifty = Rnd.get(-80, 80);
				player.teleToLocation(Location[0] + driftx, Location[1] + drifty, Location[2]);
				player.destroyItemByItemId("Quest", ENTRANCE_PASS, 1, player, true);
				if (player.getInventory().getItemByItemId(ANTIQUE_BROOCH) == null)
				{
					player.addItem("Quest", USED_PASS, 1, player, true);
				}
				
				L2ItemInstance hallsKey = player.getInventory().getItemByItemId(CHAPEL_KEY);
				if (hallsKey != null)
				{
					player.destroyItemByItemId("Quest", CHAPEL_KEY, hallsKey.getCount(), player, true);
				}
			}
		}
		
		_hallInUse.put(npcId, true);
	}
	
	public void spawnMysteriousBox(int npcId)
	{
		if (!isAttackTime())
		{
			return;
		}
		
		L2Spawn spawnDat = _mysteriousBoxSpawns.get(npcId);
		if (spawnDat != null)
		{
			spawnDat.stopRespawn();
			_allMobs.add(spawnDat.doSpawn());
		}
	}
	
	public void spawnMonster(int npcId)
	{
		if (!isAttackTime())
		{
			return;
		}
		
		List<L2Spawn> monsterList;
		List<L2SepulcherMonsterInstance> mobs = new ArrayList<>();
		L2Spawn keyBoxMobSpawn;
		
		if (Rnd.get(2) == 0)
		{
			monsterList = _physicalMonsters.get(npcId);
		}
		else
		{
			monsterList = _magicalMonsters.get(npcId);
		}
		
		if (monsterList != null)
		{
			boolean spawnKeyBoxMob = false;
			boolean spawnedKeyBoxMob = false;
			
			for (L2Spawn spawnDat : monsterList)
			{
				if (spawnedKeyBoxMob)
				{
					spawnKeyBoxMob = false;
				}
				else
				{
					switch (npcId)
					{
						case 8469:
						case 8474:
						case 8479:
						case 8484:
							if (Rnd.get(48) == 0)
							{
								spawnKeyBoxMob = true;
							}
							break;
						default:
							spawnKeyBoxMob = false;
					}
				}
				
				L2SepulcherMonsterInstance mob = null;
				
				if (spawnKeyBoxMob)
				{
					try
					{
						L2NpcTemplate template = NpcTable.getInstance().getTemplate(12984);
						if (template != null)
						{
							keyBoxMobSpawn = new L2Spawn(template);
							keyBoxMobSpawn.setAmount(1);
							keyBoxMobSpawn.setLocX(spawnDat.getLocX());
							keyBoxMobSpawn.setLocY(spawnDat.getLocY());
							keyBoxMobSpawn.setLocZ(spawnDat.getLocZ());
							keyBoxMobSpawn.setHeading(spawnDat.getHeading());
							keyBoxMobSpawn.setRespawnDelay(3600);
							SpawnTable.getInstance().addNewSpawn(keyBoxMobSpawn, false);
							keyBoxMobSpawn.stopRespawn();
							mob = (L2SepulcherMonsterInstance) keyBoxMobSpawn.doSpawn();
						}
						else
						{
							_log.warning("FourSepulchersManager.SpawnMonster: Data missing in NPC table for ID: 12984");
						}
					}
					catch (Exception e)
					{
						_log.warning("FourSepulchersManager.SpawnMonster: Spawn could not be initialized: " + e);
					}
					
					spawnedKeyBoxMob = true;
				}
				else
				{
					spawnDat.stopRespawn();
					mob = (L2SepulcherMonsterInstance) spawnDat.doSpawn();
				}
				
				if (mob != null)
				{
					mob.setMysteriousBoxId(npcId);
					switch (npcId)
					{
						case 8469:
						case 8474:
						case 8479:
						case 8484:
						case 8472:
						case 8477:
						case 8482:
						case 8487:
							mobs.add(mob);
					}
					_allMobs.add(mob);
				}
			}
			
			switch (npcId)
			{
				case 8469:
				case 8474:
				case 8479:
				case 8484:
					_viscountMobs.put(npcId, mobs);
					break;
				case 8472:
				case 8477:
				case 8482:
				case 8487:
					_dukeMobs.put(npcId, mobs);
					break;
			}
		}
	}
	
	public synchronized boolean isViscountMobsAnnihilated(int npcId)
	{
		List<L2SepulcherMonsterInstance> mobs = _viscountMobs.get(npcId);
		if (mobs == null)
		{
			return true;
		}
		
		for (L2SepulcherMonsterInstance mob : mobs)
		{
			if (!mob.isDead())
			{
				return false;
			}
			
			// Stop mobs respawn
			if (mob.getNpcId() == 12984)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public synchronized boolean isDukeMobsAnnihilated(int npcId)
	{
		List<L2SepulcherMonsterInstance> mobs = _dukeMobs.get(npcId);
		if (mobs == null)
		{
			return true;
		}
		
		for (L2SepulcherMonsterInstance mob : mobs)
		{
			if (!mob.isDead())
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void spawnKeyBox(L2NpcInstance activeChar)
	{
		if (!isAttackTime())
		{
			return;
		}
		
		L2Spawn spawnDat = _keyBoxSpawns.get(activeChar.getNpcId());
		
		if (spawnDat != null)
		{
			spawnDat.setAmount(1);
			spawnDat.setLocX(activeChar.getX());
			spawnDat.setLocY(activeChar.getY());
			spawnDat.setLocZ(activeChar.getZ());
			spawnDat.setHeading(activeChar.getHeading());
			spawnDat.setRespawnDelay(3600);
			spawnDat.stopRespawn();
			_allMobs.add(spawnDat.doSpawn());
		}
	}
	
	public void spawnExecutionerOfHalisha(L2NpcInstance activeChar)
	{
		if (!isAttackTime())
		{
			return;
		}
		
		L2Spawn spawnDat = _executionerSpawns.get(activeChar.getNpcId());
		if (spawnDat != null)
		{
			spawnDat.setAmount(1);
			spawnDat.setLocX(activeChar.getX());
			spawnDat.setLocY(activeChar.getY());
			spawnDat.setLocZ(activeChar.getZ());
			spawnDat.setHeading(activeChar.getHeading());
			spawnDat.setRespawnDelay(3600);
			spawnDat.stopRespawn();
			_allMobs.add(spawnDat.doSpawn());
		}
	}
	
	public void spawnArchonOfHalisha(int npcId)
	{
		if (!isAttackTime())
		{
			return;
		}
		
		if (_archonSpawned.get(npcId))
		{
			return;
		}
		
		List<L2Spawn> monsterList = _dukeFinalMobs.get(npcId);
		if (monsterList != null)
		{
			for (L2Spawn spawnDat : monsterList)
			{
				spawnDat.stopRespawn();
				L2SepulcherMonsterInstance mob = (L2SepulcherMonsterInstance) spawnDat.doSpawn();
				
				if (mob != null)
				{
					mob.setMysteriousBoxId(npcId);
					_allMobs.add(mob);
				}
			}
			_archonSpawned.put(npcId, true);
		}
	}
	
	public void spawnEmperorsGraveNpc(int npcId)
	{
		if (!isAttackTime())
		{
			return;
		}
		
		List<L2Spawn> monsterList = _emperorsGraveNpcs.get(npcId);
		if (monsterList != null)
		{
			for (L2Spawn spawnDat : monsterList)
			{
				spawnDat.stopRespawn();
				_allMobs.add(spawnDat.doSpawn());
			}
		}
	}
	
	protected void locationShadowSpawns()
	{
		int locNo = Rnd.get(4);
		final int[] gateKeeper =
		{
			13197,
			13202,
			13207,
			13212
		};
		
		L2Spawn spawnDat;
		
		for (int i = 0; i <= 3; i++)
		{
			int keyNpcId = gateKeeper[i];
			spawnDat = _shadowSpawns.get(keyNpcId);
			spawnDat.setLocX(_shadowSpawnLoc[locNo][i][1]);
			spawnDat.setLocY(_shadowSpawnLoc[locNo][i][2]);
			spawnDat.setLocZ(_shadowSpawnLoc[locNo][i][3]);
			spawnDat.setHeading(_shadowSpawnLoc[locNo][i][4]);
			_shadowSpawns.put(keyNpcId, spawnDat);
		}
	}
	
	protected void handleSepulchers()
	{
		// Check if any sepulcher is busy
		for (int i = 13189; i <= 13192; i++)
		{
			if (_hallInUse.get(i).booleanValue())
			{
				// set to attacking state
				_inAttackTime = true;
				
				locationShadowSpawns();
				spawnMysteriousBox(i);
			}
		}
	}
	
	public void spawnShadow(int npcId)
	{
		if (!isAttackTime())
		{
			return;
		}
		
		L2Spawn spawnDat = _shadowSpawns.get(npcId);
		if (spawnDat != null)
		{
			spawnDat.stopRespawn();
			L2SepulcherMonsterInstance mob = (L2SepulcherMonsterInstance) spawnDat.doSpawn();
			
			if (mob != null)
			{
				mob.setMysteriousBoxId(npcId);
				_allMobs.add(mob);
			}
		}
	}
	
	public void deleteAllMobs()
	{
		for (L2NpcInstance mob : _allMobs)
		{
			try
			{
				mob.getSpawn().stopRespawn();
				mob.deleteMe();
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "FourSepulchersManager: Failed deleting mob.", e);
			}
		}
		_allMobs.clear();
	}
	
	protected void closeAllDoors()
	{
		for (int doorId : _hallGateKeepers.values())
		{
			try
			{
				L2DoorInstance door = DoorTable.getInstance().getDoor(doorId);
				if (door != null)
				{
					door.closeMe();
				}
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "FourSepulchersManager: Failed closing door", e);
			}
		}
	}
	
	protected byte minuteSelect(byte min)
	{
		if (((double) min % 5) != 0)// if doesn't divide on 5 fully
		{
			// mad table for selecting proper minutes...
			// maybe there is a better way to do this
			switch (min)
			{
				case 6:
				case 7:
					min = 5;
					break;
				case 8:
				case 9:
				case 11:
				case 12:
					min = 10;
					break;
				case 13:
				case 14:
				case 16:
				case 17:
					min = 15;
					break;
				case 18:
				case 19:
				case 21:
				case 22:
					min = 20;
					break;
				case 23:
				case 24:
				case 26:
				case 27:
					min = 25;
					break;
				case 28:
				case 29:
				case 31:
				case 32:
					min = 30;
					break;
				case 33:
				case 34:
				case 36:
				case 37:
					min = 35;
					break;
				case 38:
				case 39:
				case 41:
				case 42:
					min = 40;
					break;
				case 43:
				case 44:
				case 46:
				case 47:
					min = 45;
					break;
				case 48:
				case 49:
				case 51:
				case 52:
					min = 50;
					break;
				case 53:
				case 54:
				case 56:
				case 57:
					min = 55;
					break;
			}
		}
		return min;
	}
	
	public void managerSay(byte min)
	{
		// for attack phase, sending message every 5 minutes
		if (_inAttackTime)
		{
			if (min < 5)
			{
				return; // do not shout when < 5 minutes
			}
			
			min = minuteSelect(min);
			
			String msg = min + " minute(s) have passed."; // now this is a proper message^^
			
			if (min == 90)
			{
				msg = "Game over. The teleport will appear momentarily.";
			}
			
			for (L2Spawn temp : _managers)
			{
				if (temp == null)
				{
					_log.warning("FourSepulchersManager: managerSay(): manager is null");
					continue;
				}
				
				if (!(temp.getLastSpawn() instanceof L2SepulcherNpcInstance))
				{
					_log.warning("FourSepulchersManager: managerSay(): manager is not Sepulcher instance");
					continue;
				}
				
				// hall not used right now, so its manager will not tell you anything :)
				// if you don't need this - delete next two lines.
				if (!_hallInUse.get(temp.getNpcId()).booleanValue())
				{
					continue;
				}
				
				((L2SepulcherNpcInstance) temp.getLastSpawn()).sayInShout(msg);
			}
		}
		else if (_inEntryTime)
		{
			String msg1 = "You may now enter the Sepulcher";
			String msg2 = "If you place your hand on the stone statue in front of each sepulcher," + " you will be able to enter";
			for (L2Spawn temp : _managers)
			{
				if (temp == null)
				{
					_log.warning("FourSepulchersManager: Something goes wrong in managerSay()...");
					continue;
				}
				
				if (!(temp.getLastSpawn() instanceof L2SepulcherNpcInstance))
				{
					_log.warning("FourSepulchersManager: Something goes wrong in managerSay()...");
					continue;
				}
				
				((L2SepulcherNpcInstance) temp.getLastSpawn()).sayInShout(msg1);
				((L2SepulcherNpcInstance) temp.getLastSpawn()).sayInShout(msg2);
			}
		}
	}
	
	protected class ManagerSay implements Runnable
	{
		@Override
		public void run()
		{
			if (_inAttackTime)
			{
				Calendar tmp = Calendar.getInstance();
				tmp.setTimeInMillis(Calendar.getInstance().getTimeInMillis() - _entryTimeEnd);
				if ((tmp.get(Calendar.MINUTE) + 5) <= Config.FS_PENDING_TIME)
				{
					// byte because minute cannot be more than 59
					managerSay((byte) tmp.get(Calendar.MINUTE));
					ThreadPoolManager.getInstance().scheduleGeneral(new ManagerSay(), 5 * 60000);
				}
			}
		}
	}
	
	protected class ChangeEntryTime implements Runnable
	{
		@Override
		public void run()
		{
			_inEntryTime = true;
			_inAttackTime = false;
			
			long interval = 0;
			
			// if this is first launch - search time when entry time will be ended:
			// counting difference between time when entry time ends and current time
			// and then launching change time task
			if (_firstTimeRun)
			{
				interval = _entryTimeEnd - Calendar.getInstance().getTimeInMillis();
			}
			else
			{
				interval = Config.FS_ENTRY_TIME * 60000; // else use stupid method
			}
			
			// launching saying process...
			managerSay((byte) 0);
			
			_changePendingTimeTask = ThreadPoolManager.getInstance().scheduleGeneral(new ChangePendingTime(), interval);
			
			if (_changeEntryTimeTask != null)
			{
				_changeEntryTimeTask.cancel(true);
				_changeEntryTimeTask = null;
			}
		}
	}
	
	protected class ChangePendingTime implements Runnable
	{
		@Override
		public void run()
		{
			_inEntryTime = false;
			
			// prepare sepulchers if occupied
			handleSepulchers();
			
			if (!_firstTimeRun)
			{
				_entryTimeEnd = Calendar.getInstance().getTimeInMillis();
			}
			
			if (_inAttackTime)
			{
				// say task
				if (_firstTimeRun)
				{
					for (double min = Calendar.getInstance().get(Calendar.MINUTE); min < _newCycleMin; min++)
					{
						// looking for next shout time....
						if ((min % 5) == 0) // check if min can be divided by 5
						{
							_log.info(Calendar.getInstance().getTime() + " Atk announce scheduled to " + min + " minute of this hour.");
							Calendar inter = Calendar.getInstance();
							inter.set(Calendar.MINUTE, (int) min);
							ThreadPoolManager.getInstance().scheduleGeneral(new ManagerSay(), inter.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
							break;
						}
					}
				}
				else
				{
					ThreadPoolManager.getInstance().scheduleGeneral(new ManagerSay(), 5 * 60400);
				}
			}
			
			long interval = 0;
			
			// searching time when pending time will be ended:
			// counting difference between time when attack time ends and current time
			// and then launching change time task
			if (_firstTimeRun)
			{
				interval = _pendingTimeEnd - Calendar.getInstance().getTimeInMillis();
			}
			else
			{
				interval = Config.FS_PENDING_TIME * 60000;
			}
			_changeCoolDownTimeTask = ThreadPoolManager.getInstance().scheduleGeneral(new ChangeCoolDownTime(), interval);
			
			if (_changePendingTimeTask != null)
			{
				_changePendingTimeTask.cancel(true);
				_changePendingTimeTask = null;
			}
		}
	}
	
	protected class ChangeCoolDownTime implements Runnable
	{
		@Override
		public void run()
		{
			_inEntryTime = false;
			
			if (_inAttackTime)
			{
				managerSay((byte) 90); // sending a unique id :D
				_inAttackTime = false;
				clean();
			}
			
			Calendar time = Calendar.getInstance();
			
			// one hour = 55th min to 55 min of next hour, so we check for this,
			// also check for first launch
			if ((Calendar.getInstance().get(Calendar.MINUTE) > _newCycleMin) && !_firstTimeRun)
			{
				time.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR) + 1);
			}
			time.set(Calendar.MINUTE, _newCycleMin);
			
			_log.info("FourSepulchersManager: Entry time: " + time.getTime());
			
			if (_firstTimeRun)
			{
				_firstTimeRun = false; // cooldown phase ends event hour, so it will be not first run
			}
			
			long interval = time.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
			_changeEntryTimeTask = ThreadPoolManager.getInstance().scheduleGeneral(new ChangeEntryTime(), interval);
			
			if (_changeCoolDownTimeTask != null)
			{
				_changeCoolDownTimeTask.cancel(true);
				_changeCoolDownTimeTask = null;
			}
		}
	}
	
	public Map<Integer, Integer> getHallGateKeepers()
	{
		return _hallGateKeepers;
	}
	
	public void showHtmlFile(L2PcInstance player, String file, L2NpcInstance npc, L2PcInstance member)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		html.setFile("data/html/SepulcherNpc/" + file);
		
		if (file.contains("-SP.htm"))
		{
			html.replace("%count%", String.valueOf(Config.FS_PARTY_MEMBER_COUNT));
		}
		
		if (member != null)
		{
			html.replace("%member%", member.getName());
		}
		
		player.sendPacket(html);
	}
	
	private static class SingletonHolder
	{
		protected static final FourSepulchersManager _instance = new FourSepulchersManager();
	}
}