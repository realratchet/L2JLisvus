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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.Siege;

public class SiegeManager
{
	private final static Logger _log = Logger.getLogger(SiegeManager.class.getName());
	
	public static final SiegeManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	// Data Fields
	private int _attackerMaxClans = 500; // Max number of clans
	private int _attackerRespawnDelay = 0; // In millis
	private int _defenderMaxClans = 500; // Max number of clans
	
	// Siege settings
	private Map<Integer, List<SiegeSpawn>> _controlTowerSpawnList;
	
	private int _flagMaxCount = 1;
	private int _siegeClanMinLevel = 4;
	private int _siegeLength = 2; // In hours
	
	// =========================================================
	// Constructor
	public SiegeManager()
	{
		_log.info("Initializing SiegeManager");
		load();
	}
	
	// =========================================================
	// Method - Public
	public final void addSiegeSkills(L2PcInstance character)
	{
		character.addSkill(SkillTable.getInstance().getInfo(246, 1), false);
		character.addSkill(SkillTable.getInstance().getInfo(247, 1), false);
	}
	
	/**
	 * Return true if the clan is registered or owner of a castle<BR>
	 * <BR>
	 * @param clan The L2Clan of the player
	 * @param castleId
	 * @return
	 */
	public final boolean checkIsRegistered(L2Clan clan, int castleId)
	{
		if (clan == null)
			return false;
		
		if (clan.getHasCastle() > 0)
			return true;
		
		boolean register = false;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT clan_id FROM siege_clans where clan_id=? and castle_id=?"))
		{
			statement.setInt(1, clan.getClanId());
			statement.setInt(2, castleId);
			try (ResultSet rs = statement.executeQuery())
			{
				while (rs.next())
				{
					register = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Exception: checkIsRegistered(): " + e.getMessage());
		}
		return register;
	}
	
	public final void removeSiegeSkills(L2PcInstance character)
	{
		character.removeSkill(SkillTable.getInstance().getInfo(246, 1));
		character.removeSkill(SkillTable.getInstance().getInfo(247, 1));
	}
	
	// =========================================================
	// Method - Private
	private final void load()
	{
		Properties siegeSettings = new Properties();
		try (InputStream is = new FileInputStream(new File(Config.SIEGE_FILE)))
		{
			siegeSettings.load(is);
		}
		catch (Exception e)
		{
			_log.warning("Error while loading siege data.");
			e.printStackTrace();
		}
		
		// Siege settings
		_attackerMaxClans = Integer.parseInt(siegeSettings.getProperty("AttackerMaxClans", "500"));
		_attackerRespawnDelay = Integer.parseInt(siegeSettings.getProperty("AttackerRespawn", "0"));
		
		_defenderMaxClans = Integer.parseInt(siegeSettings.getProperty("DefenderMaxClans", "500"));
		
		_flagMaxCount = Integer.parseInt(siegeSettings.getProperty("MaxFlags", "1"));
		_siegeClanMinLevel = Integer.parseInt(siegeSettings.getProperty("SiegeClanMinLevel", "4"));
		_siegeLength = Integer.parseInt(siegeSettings.getProperty("SiegeLength", "2"));
		
		// Siege spawns settings
		_controlTowerSpawnList = new HashMap<>();
		
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			List<SiegeSpawn> _controlTowerSpawns = new ArrayList<>();
			
			for (int i = 1;; i++)
			{
				String spawnParams = siegeSettings.getProperty(castle.getName() + "ControlTower" + i, "");
				if (spawnParams.isEmpty())
					break;
				
				StringTokenizer st = new StringTokenizer(spawnParams.trim(), ",");
				
				try
				{
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					int z = Integer.parseInt(st.nextToken());
					int npcId = Integer.parseInt(st.nextToken());
					int hp = Integer.parseInt(st.nextToken());
					
					_controlTowerSpawns.add(new SiegeSpawn(castle.getCastleId(), x, y, z, 0, npcId, hp));
				}
				catch (Exception e)
				{
					_log.warning("Error while loading control tower(s) for " + castle.getName() + " castle.");
				}
			}
			
			_controlTowerSpawnList.put(castle.getCastleId(), _controlTowerSpawns);
		}
	}
	
	public final void initializeCastleSieges()
	{
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			castle.getSiege();
		}
	}
	
	public final List<SiegeSpawn> getControlTowerSpawnList(int _castleId)
	{
		if (_controlTowerSpawnList.containsKey(_castleId))
			return _controlTowerSpawnList.get(_castleId);
		return null;
	}
	
	public final int getAttackerMaxClans()
	{
		return _attackerMaxClans;
	}
	
	public final int getAttackerRespawnDelay()
	{
		return _attackerRespawnDelay;
	}
	
	public final int getDefenderMaxClans()
	{
		return _defenderMaxClans;
	}
	
	public final int getFlagMaxCount()
	{
		return _flagMaxCount;
	}
	
	public final Siege getSiege(L2Object activeObject)
	{
		return getSiege(activeObject.getX(), activeObject.getY(), activeObject.getZ());
	}
	
	public final Siege getSiege(int x, int y, int z)
	{
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			if (castle.getSiege().checkIfInZone(x, y, z))
				return castle.getSiege();
		}
		return null;
	}
	
	public final int getSiegeClanMinLevel()
	{
		return _siegeClanMinLevel;
	}
	
	public final int getSiegeLength()
	{
		return _siegeLength;
	}
	
	public final List<Siege> getSieges()
	{
		List<Siege> sieges = new ArrayList<>();
		for (Castle castle : CastleManager.getInstance().getCastles())
			sieges.add(castle.getSiege());
		return sieges;
	}
	
	public class SiegeSpawn
	{
		Location _location;
		private int _npc_id;
		private int _heading;
		private int _castle_id;
		private int _hp;
		
		public SiegeSpawn(int castle_id, int x, int y, int z, int heading, int npc_id)
		{
			_castle_id = castle_id;
			_location = new Location(x, y, z, heading);
			_heading = heading;
			_npc_id = npc_id;
		}
		
		public SiegeSpawn(int castle_id, int x, int y, int z, int heading, int npc_id, int hp)
		{
			_castle_id = castle_id;
			_location = new Location(x, y, z, heading);
			_heading = heading;
			_npc_id = npc_id;
			_hp = hp;
		}
		
		public int getCastleId()
		{
			return _castle_id;
		}
		
		public int getNpcId()
		{
			return _npc_id;
		}
		
		public int getHeading()
		{
			return _heading;
		}
		
		public int getHp()
		{
			return _hp;
		}
		
		public Location getLocation()
		{
			return _location;
		}
	}
	
	private static class SingletonHolder
	{
		protected static final SiegeManager _instance = new SiegeManager();
	}
}