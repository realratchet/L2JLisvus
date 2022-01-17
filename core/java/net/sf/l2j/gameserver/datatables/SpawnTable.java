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
package net.sf.l2j.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.instancemanager.DayNightSpawnManager;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * This class ...
 * 
 * @author Nightmare
 * @version $Revision: 1.5.2.6.2.7 $ $Date: 2005/03/27 15:29:18 $
 */
public class SpawnTable
{
    private static Logger _log = Logger.getLogger(SpawnTable.class.getName());

    private final Map<Integer, Set<L2Spawn>> _spawnTable = new ConcurrentHashMap<>();
    
    private int _npcSpawnCount;
    private int _customSpawnCount;
    private int _highestId;
    
    public static SpawnTable getInstance()
    {
    	return SingletonHolder._instance;
    }

    private SpawnTable()
    {
        if (!Config.ALT_DEV_NO_SPAWNS)
            fillSpawnTable();
    }

    private void fillSpawnTable()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, loc_id, periodOfDay FROM spawnlist ORDER BY id");
            ResultSet rset = statement.executeQuery())
        {
            L2Spawn spawnDat;
            L2NpcTemplate template;

            while (rset.next())
            {
            	int npcId = rset.getInt("npc_templateid");
                template = NpcTable.getInstance().getTemplate(npcId);
                if (template != null)
                {
                    if (template.type.equalsIgnoreCase("L2SiegeGuard"))
                    {
                        // Don't spawn
                    }
                    else if (template.type.equalsIgnoreCase("L2RaidBoss"))
                    {
                        // Don't spawn raidboss
                    }
                    else if (!Config.ALLOW_CLASS_MASTERS && template.type.equals("L2ClassMaster"))
                    {
                        // Don't spawn class masters
                    }
                    else
                    {
                        spawnDat = new L2Spawn(template);
                        spawnDat.setId(rset.getInt("id"));
                        spawnDat.setAmount(rset.getInt("count"));
                        spawnDat.setLocX(rset.getInt("locx"));
                        spawnDat.setLocY(rset.getInt("locy"));
                        spawnDat.setLocZ(rset.getInt("locz"));
                        spawnDat.setHeading(rset.getInt("heading"));
                        spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                        int loc_id = rset.getInt("loc_id");
                        spawnDat.setLocation(loc_id);                             
                        
                        switch(rset.getInt("periodOfDay"))
                        {
                            case 0: // default
                                _npcSpawnCount += spawnDat.init();
                                break;
                            case 1: // Day
                                DayNightSpawnManager.getInstance().addDayCreature(spawnDat);
                                _npcSpawnCount++;
                                break;
                            case 2: // Night
                                DayNightSpawnManager.getInstance().addNightCreature(spawnDat);
                                _npcSpawnCount++;
                                break;     
                        }
                        
                        _spawnTable.computeIfAbsent(npcId, k -> ConcurrentHashMap.newKeySet(1)).add(spawnDat);
                        if (spawnDat.getId() > _highestId)
                            _highestId = spawnDat.getId();
                    }
                }
                else
                    _log.warning("SpawnTable: Data missing in NPC table for ID: " + npcId + ".");
            }
        }
        catch (Exception e)
        {
            // problem with initializing spawn, go to next one
            _log.warning("SpawnTable: Spawn could not be initialized: " + e);
        }

        _log.config("SpawnTable: Loaded " + _npcSpawnCount + " Npc Spawn Locations.");

        if (Config.CUSTOM_SPAWNLIST_TABLE)
        {
            try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, loc_id, periodOfDay FROM custom_spawnlist ORDER BY id");
                ResultSet rset = statement.executeQuery())
            {
                L2Spawn spawnDat;
                L2NpcTemplate template;

                while (rset.next())
                {
                	int npcId = rset.getInt("npc_templateid");
                    template = NpcTable.getInstance().getTemplate(npcId);
                    if (template != null)
                    {
                        if (template.type.equalsIgnoreCase("L2SiegeGuard"))
                        {
                            // Don't spawn
                        }
                        else if (template.type.equalsIgnoreCase("L2RaidBoss"))
                        {
                            // Don't spawn raidboss
                        }
                        else if (!Config.ALLOW_CLASS_MASTERS && template.type.equals("L2ClassMaster"))
                        {
                            // Don't spawn class masters
                        }
                        else
                        {
                            spawnDat = new L2Spawn(template);
                            spawnDat.setId(rset.getInt("id"));
                            spawnDat.setAmount(rset.getInt("count"));
                            spawnDat.setLocX(rset.getInt("locx"));
                            spawnDat.setLocY(rset.getInt("locy"));
                            spawnDat.setLocZ(rset.getInt("locz"));
                            spawnDat.setHeading(rset.getInt("heading"));
                            spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                            spawnDat.setCustom(true);
                            int loc_id = rset.getInt("loc_id");
                            spawnDat.setLocation(loc_id);                             
                        
                            switch(rset.getInt("periodOfDay"))
                            {
                                case 0: // default
                                    _customSpawnCount += spawnDat.init();
                                    break;
                                case 1: // Day
                                    DayNightSpawnManager.getInstance().addDayCreature(spawnDat);
                                    _customSpawnCount++;
                                    break;
                                case 2: // Night
                                    DayNightSpawnManager.getInstance().addNightCreature(spawnDat);
                                    _customSpawnCount++;
                                    break;     
                            }
                        
                            _spawnTable.computeIfAbsent(npcId, k -> ConcurrentHashMap.newKeySet(1)).add(spawnDat);
                            if (spawnDat.getId() > _highestId)
                                _highestId = spawnDat.getId();
                        }
                    }
                    else
                        _log.warning("SpawnTable: Data missing in Custom NPC table for ID: " + npcId + ".");
                }
            }
            catch (Exception e)
            {
                // problem with initializing custom spawn, go to next one
                _log.warning("SpawnTable: Custom spawn could not be initialized: " + e);
            }
            _log.config("CustomSpawnTable: Loaded " + _customSpawnCount + " Custom Npc Spawn Locations.");
        }

        if (Config.DEBUG)
            _log.fine("SpawnTable: Spawning completed, total number of NPCs in the world: " + (_npcSpawnCount+_customSpawnCount));
    }

    public Map<Integer, Set<L2Spawn>> getSpawnTable()
    {
        return _spawnTable;
    }
    
    /**
	 * Gets the spawns for the NPC Id.
	 * @param npcId the NPC Id
	 * @return the spawn set for the given npcId
	 */
	public Set<L2Spawn> getSpawns(int npcId)
	{
		return _spawnTable.getOrDefault(npcId, Collections.emptySet());
	}
	
	/**
	 * Finds a spawn for the given NPC ID.
	 * @param npcId the NPC Id
	 * @return a spawn for the given NPC ID or {@code null}
	 */
	public L2Spawn findAny(int npcId)
	{
		return getSpawns(npcId).stream().findFirst().orElse(null);
	}

    public void addNewSpawn(L2Spawn spawn, boolean storeInDb)
    {
        _highestId++;
        spawn.setId(_highestId);
        _spawnTable.computeIfAbsent(spawn.getNpcId(), k -> ConcurrentHashMap.newKeySet(1)).add(spawn);

        if (storeInDb)
        {
            try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("INSERT INTO " + (spawn.isCustom() ? "custom_spawnlist" : "spawnlist")+ " (id,count,npc_templateid,locx,locy,locz,heading,respawn_delay,loc_id) values(?,?,?,?,?,?,?,?,?)"))
            {
                statement.setInt(1, spawn.getId());
                statement.setInt(2, spawn.getAmount());
                statement.setInt(3, spawn.getNpcId());
                statement.setInt(4, spawn.getLocX());
                statement.setInt(5, spawn.getLocY());
                statement.setInt(6, spawn.getLocZ());
                statement.setInt(7, spawn.getHeading());
                statement.setInt(8, spawn.getRespawnDelay() / 1000);
                statement.setInt(9, spawn.getLocation());
                statement.execute();
            }
            catch (Exception e)
            {
                // problem with storing spawn
                _log.warning("SpawnTable: Could not store spawn in the DB:" + e);
            }
        }
    }

    public void deleteSpawn(L2Spawn spawn, boolean updateDb)
    {
    	// Remove spawn from corresponding set
    	final Set<L2Spawn> set = _spawnTable.get(spawn.getNpcId());
		if (set != null)
		{
			boolean removed = set.remove(spawn);
			if (!removed)
			{
				return;
			}
			
			if (set.isEmpty())
			{
				_spawnTable.remove(spawn.getId());
			}
		}

        if (updateDb)
        {
            try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("DELETE FROM " + (spawn.isCustom() ? "custom_spawnlist" : "spawnlist") + " WHERE id=?"))
            {
                statement.setInt(1, spawn.getId());
                statement.execute();
            }
            catch (Exception e)
            {
                // problem with deleting spawn
                _log.warning("SpawnTable: Spawn " + spawn.getId() + " could not be removed from DB: " + e);
            }
        }
    }

    /**
     * Reloads spawns from database.
     */
    public void reloadAll()
    { 
    	_spawnTable.clear();
        fillSpawnTable();
    }
    
    private static class SingletonHolder
	{
		protected static final SpawnTable _instance = new SpawnTable();
	}
}