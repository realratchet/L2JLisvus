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
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;

/**
 *
 * @author godson
 */
public class RaidBossSpawnManager
{
    private final static Logger _log = Logger.getLogger(RaidBossSpawnManager.class.getName());
    
    private final Map<Integer, L2RaidBossInstance> _bosses = new ConcurrentHashMap<>();
    private final Map<Integer, L2Spawn> _spawns = new ConcurrentHashMap<>();
    private final Map<Integer, StatsSet> _storedInfo = new ConcurrentHashMap<>();
    private final Map<Integer, ScheduledFuture<?>> _schedules = new ConcurrentHashMap<>();
    
    public static enum StatusEnum
    {
        ALIVE,
        DEAD,
        UNDEFINED
    }

    public static final RaidBossSpawnManager getInstance()
	{
		return SingletonHolder._instance;
	}
    
    public RaidBossSpawnManager()
    {
        init();
    }
    
    private void init()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * from raidboss_spawnlist ORDER BY boss_id");
            ResultSet rset = statement.executeQuery())
        {
            L2Spawn spawnDat;
            L2NpcTemplate template;
            long respawnTime;
            
            while (rset.next())
            {
                template = getValidTemplate(rset.getInt("boss_id"));
                if (template != null)
                {
                    spawnDat = new L2Spawn(template);
                    spawnDat.setLocX(rset.getInt("loc_x"));
                    spawnDat.setLocY(rset.getInt("loc_y"));
                    spawnDat.setLocZ(rset.getInt("loc_z"));
                    spawnDat.setAmount(rset.getInt("amount"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnMinDelay(rset.getInt("respawn_min_delay"));
                    spawnDat.setRespawnMaxDelay(rset.getInt("respawn_max_delay"));
                    
                    respawnTime = rset.getLong("respawn_time");
                    
                    addNewSpawn(spawnDat, respawnTime, rset.getDouble("currentHP"), rset.getDouble("currentMP"), false);
                }
                else
                {
                    _log.warning("RaidBossSpawnManager: Could not load raidboss #" + rset.getInt("boss_id") + " from DB");
                }
            }
            
            _log.info("RaidBossSpawnManager: Loaded " + _bosses.size() + " Instances");
            _log.info("RaidBossSpawnManager: Scheduled " + _schedules.size() + " Instances");
        }
        catch (SQLException e)
        {
            _log.warning("RaidBossSpawnManager: Couldnt load raidboss_spawnlist table");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private class spawnSchedule implements Runnable
    {
        private int bossId;

        public spawnSchedule(int npcId)
        {
        	bossId = npcId;
        }

        @Override
		public void run()
        {
            L2RaidBossInstance raidboss = null;

            if (bossId == 10328)
                raidboss = DayNightSpawnManager.getInstance().handleBoss(_spawns.get(bossId));
            else 
                raidboss = (L2RaidBossInstance)_spawns.get(bossId).doSpawn();

            if (raidboss != null)
            {
                raidboss.setRaidStatus(StatusEnum.ALIVE);
                
                StatsSet info = new StatsSet();
                info.set("currentHP", raidboss.getCurrentHp());
                info.set("currentMP", raidboss.getCurrentMp());
                info.set("respawnTime", 0L);
                
                _storedInfo.put(bossId, info);
                
                GmListTable.getInstance().broadcastMessageToGMs("Spawning Raid Boss " + raidboss.getName());
                
                _bosses.put(bossId, raidboss);
            }
            
            _schedules.remove(bossId);
        }
    }
    
    public void updateStatus(L2RaidBossInstance boss, boolean isBossDead)
    {
        if (!_storedInfo.containsKey(boss.getNpcId()))
        {
            return;
        }
        
        StatsSet info = _storedInfo.get(boss.getNpcId());
        
        if (isBossDead)
        {
            boss.setRaidStatus(StatusEnum.DEAD);
            
            int respawnMinDelay = boss.getSpawn().getRespawnMinDelay();
            int respawnMaxDelay = boss.getSpawn().getRespawnMaxDelay();
            long respawnDelay = Rnd.get((int)(respawnMinDelay*1000*Config.RAID_MIN_RESPAWN_MULTIPLIER),(int)(respawnMaxDelay*1000*Config.RAID_MAX_RESPAWN_MULTIPLIER));
            long respawnTime = Calendar.getInstance().getTimeInMillis() + respawnDelay;
            
            info.set("currentHP", boss.getMaxHp());
            info.set("currentMP", boss.getMaxMp());
            info.set("respawnTime", respawnTime);
            
            _log.info("RaidBossSpawnManager: Updated " + boss.getName() + " respawn time to " + respawnTime);
            
            if (!_schedules.containsKey(boss.getNpcId()))
            {
                _schedules.put(boss.getNpcId(), ThreadPoolManager.getInstance().scheduleGeneral(new spawnSchedule(boss.getNpcId()), respawnDelay));
                // To update immediately Database uncomment on the following line, to post the hour of respawn raid boss on your site for example or to envisage a crash landing of the waiter.
                updateDb();
            }
        }
        else
        {
            boss.setRaidStatus(StatusEnum.ALIVE);

            info.set("currentHP", boss.getCurrentHp());
            info.set("currentMP", boss.getCurrentMp());
            info.set("respawnTime", 0L);
        }

        _storedInfo.put(boss.getNpcId(), info);
    }

    public void addNewSpawn(L2Spawn spawnDat, long respawnTime, double currentHP, double currentMP, boolean storeInDb)
    {
    	if (spawnDat == null)
    		return;
    	
    	int bossId = spawnDat.getNpcId();
    	if (_spawns.containsKey(bossId))
    		return;

    	long time = Calendar.getInstance().getTimeInMillis();

        SpawnTable.getInstance().addNewSpawn(spawnDat, false);

        if (respawnTime == 0L || (time > respawnTime))
        {
            L2RaidBossInstance raidboss = null;
            
            if (bossId == 10328)
                raidboss = DayNightSpawnManager.getInstance().handleBoss(spawnDat);
            else
                raidboss = (L2RaidBossInstance)spawnDat.doSpawn();

            if (raidboss != null)
            {
                raidboss.setCurrentHp(currentHP);
                raidboss.setCurrentMp(currentMP);
                raidboss.setRaidStatus(StatusEnum.ALIVE);
                
                _bosses.put(bossId, raidboss);
                
                StatsSet info = new StatsSet();
                info.set("currentHP", currentHP);
                info.set("currentMP", currentMP);
                info.set("respawnTime", 0L);
                
                _storedInfo.put(bossId, info);
            }
        }
        else
        {
            long respawnDelay = respawnTime - Calendar.getInstance().getTimeInMillis();
            
            StatsSet info = new StatsSet();
            info.set("currentHP", currentHP);
            info.set("currentMP", currentMP);
            info.set("respawnTime", respawnTime);
            
            _storedInfo.put(bossId, info);
            
            _schedules.put(bossId, ThreadPoolManager.getInstance().scheduleGeneral(new spawnSchedule(bossId), respawnDelay));
        }

        _spawns.put(bossId, spawnDat);

        if (storeInDb)
        {
            try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("INSERT INTO raidboss_spawnlist (boss_id,amount,loc_x,loc_y,loc_z,heading,respawn_time,currentHp,currentMp) values(?,?,?,?,?,?,?,?,?)"))
            {
                statement.setInt(1, spawnDat.getNpcId());
                statement.setInt(2, spawnDat.getAmount());
                statement.setInt(3, spawnDat.getLocX());
                statement.setInt(4, spawnDat.getLocY());
                statement.setInt(5, spawnDat.getLocZ());
                statement.setInt(6, spawnDat.getHeading());

                statement.setLong(7, respawnTime);
                statement.setDouble(8, currentHP);
                statement.setDouble(9, currentMP);
                statement.execute();
            }
            catch (Exception e)
            {
                // problem with storing spawn
                _log.warning("RaidBossSpawnManager: Could not store raidboss #" + bossId + " in the DB:" + e);
            }
        }
    }

    public void deleteSpawn(L2Spawn spawnDat, boolean updateDb)
    {
    	if (spawnDat == null)
    		return;
    	
    	int bossId = spawnDat.getNpcId();
    	if (!_spawns.containsKey(spawnDat.getNpcId()))
    		return;

    	SpawnTable.getInstance().deleteSpawn(spawnDat, false);
        _spawns.remove(bossId);
    	
        if (_bosses.containsKey(bossId))
        	_bosses.remove(bossId);
        
        if (_schedules.containsKey(bossId))
        {
           	ScheduledFuture<?> f = _schedules.get(bossId);
           	f.cancel(true);
           	_schedules.remove(bossId);
        }
        
        if (_storedInfo.containsKey(bossId))
        	_storedInfo.remove(bossId);

        if (updateDb)
        {
            try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("DELETE FROM raidboss_spawnlist WHERE boss_id=?"))
            {
                statement.setInt(1, bossId);
                statement.execute();
            }
            catch (Exception e)
            {
                // problem with deleting spawn
                _log.warning("RaidBossSpawnManager: Could not remove raidboss #" + bossId + " from DB: " + e);
            }
        }
    }
    
    private void updateDb()
    {
        for (Integer bossId : _storedInfo.keySet())
        {
            L2RaidBossInstance boss = _bosses.get(bossId);
            if (boss == null)
                continue;

            if (boss.getRaidStatus().equals(StatusEnum.ALIVE))
                updateStatus(boss, false);

            StatsSet info = _storedInfo.get(bossId);
            if (info == null)
                continue;

            try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("UPDATE raidboss_spawnlist set respawn_time = ?, currentHP = ?, currentMP = ? where boss_id = ?"))
            {
                statement.setLong(1, info.getLong("respawnTime"));
                statement.setDouble(2, info.getDouble("currentHP"));
                statement.setDouble(3, info.getDouble("currentMP"));
                statement.setInt(4, bossId);
                statement.execute();
            }
            catch (SQLException e)
            {
                _log.warning("RaidBossSpawnManager: Couldnt update raidboss_spawnlist table");
            }
        }
    }
    
    public String[] getAllRaidBossStatus()
    {
        String[] msg = new String[_bosses == null ? 0 : _bosses.size()];
        
        if (_bosses == null)
        {
            msg[0] = "None";
            return msg;
        }
        
        int index = 0;
        
        for (int i : _bosses.keySet())
        {
            L2RaidBossInstance boss = _bosses.get(i);
            
            msg[index] = boss.getName() + ": " + boss.getRaidStatus().name();
            index++;
        }
        
        return msg;
    }
    
    public String getRaidBossStatus(int bossId)
    {
        String msg = "RaidBoss Status....\n";
        
        if (_bosses == null)
        {
            msg += "None";
            return msg;
        }
        
        if (_bosses.containsKey(bossId))
        {
            L2RaidBossInstance boss = _bosses.get(bossId);
            
            msg += boss.getName() + ": " + boss.getRaidStatus().name();
        }
        
        return msg;
    }

    public StatusEnum getRaidBossStatusId(int bossId)
    {
        if (_bosses.containsKey(bossId))
            return _bosses.get(bossId).getRaidStatus();

        if (_schedules.containsKey(bossId))
            return StatusEnum.DEAD;

        return StatusEnum.UNDEFINED;
    }

    public L2NpcTemplate getValidTemplate(int bossId)
    {
        L2NpcTemplate template = NpcTable.getInstance().getTemplate(bossId);
        if (template == null)
            return null;
        if (!template.type.equalsIgnoreCase("L2RaidBoss"))
            return null;
        return template;
    }
    
    public void notifySpawnNightBoss(L2RaidBossInstance raidboss)
    {
        StatsSet info = new StatsSet();
        info.set("currentHP", raidboss.getCurrentHp());
        info.set("currentMP", raidboss.getCurrentMp());
        info.set("respawnTime", 0L);
        
        raidboss.setRaidStatus(StatusEnum.ALIVE);

        _storedInfo.put(raidboss.getNpcId(), info);

        GmListTable.getInstance().broadcastMessageToGMs("Spawning Raid Boss " + raidboss.getName());
        
        _bosses.put(raidboss.getNpcId(), raidboss);
    }

    public boolean isDefined(int bossId)
    {
    	return _spawns.containsKey(bossId);
    }
    
    public Map<Integer, L2RaidBossInstance> getBosses()
    {
        return _bosses;
    }
    
    public Map<Integer, L2Spawn> getSpawns()
    {
        return _spawns;
    }
    
    public Map<Integer, StatsSet> getStoredInfo()
    {
    	return _storedInfo;
    }
    
    public void reloadBosses()
    {
        init();
    }
    
    /**
     * Saves all raidboss status and then clears all info from memory,
     * including all schedules.
     */
    
    public void cleanUp()
    {
        updateDb();

        _bosses.clear();

        if (_schedules != null)
        {
            for (Integer bossId : _schedules.keySet())
            {
            	ScheduledFuture<?> f = _schedules.get(bossId);
                f.cancel(true);
            }
        }

        _schedules.clear();
        _storedInfo.clear();
        _spawns.clear();
    }

    private static class SingletonHolder
	{
		protected static final RaidBossSpawnManager _instance = new RaidBossSpawnManager();
	}
}