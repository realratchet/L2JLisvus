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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.instancemanager.EventHandleManager.EventType;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;

/**
 * This class ...
 * 
 * @version $Revision: $ $Date: $
 * @author  godson
 */
public class DayNightSpawnManager
{
    private final static Logger _log = Logger.getLogger(DayNightSpawnManager.class.getName());

    private final List<L2Spawn> _dayCreatures;
	private final List<L2Spawn> _nightCreatures;
	private final Map<L2Spawn, L2RaidBossInstance> _bosses;
	
	private int _mode = -1; // Day/Night flag
    
    public static DayNightSpawnManager getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private DayNightSpawnManager()
    {
    	_dayCreatures = new ArrayList<>();
    	_nightCreatures = new ArrayList<>();
    	_bosses = new HashMap<>();
    	
    	EventHandleManager.getInstance().addEventHandler(EventType.DAY_NIGHT_CHANGED, (e) ->
    	{
    		notifyChangeMode();
    	});
        _log.info("DayNightSpawnManager: Day/Night handler initialized");
    }
    public void addDayCreature(L2Spawn spawnDat)
    { 
    	_dayCreatures.add(spawnDat);
    }
    
    public void addNightCreature(L2Spawn spawnDat)
    {
    	_nightCreatures.add(spawnDat);
    }
    
    /**
	 * Spawn Day Creatures, and Unspawn Night Creatures
	 */
	private void spawnDayCreatures()
	{
		spawnCreatures(_nightCreatures, _dayCreatures, "night", "day");
	}
	
	/**
	 * Spawn Night Creatures, and Unspawn Day Creatures
	 */
	private void spawnNightCreatures()
	{
		spawnCreatures(_dayCreatures, _nightCreatures, "day", "night");
	}
    
	/**
	 * Manage Spawn/Respawn
	 * @param unSpawnCreatures List with spawns must be unspawned
	 * @param spawnCreatures List with spawns must be spawned
	 * @param UnspawnLogInfo String for log info for unspawned L2NpcInstance
	 * @param SpawnLogInfo String for log info for spawned L2NpcInstance
	 */
	private void spawnCreatures(List<L2Spawn> unSpawnCreatures, List<L2Spawn> spawnCreatures, String UnspawnLogInfo, String SpawnLogInfo)
	{
		try
		{
			if (!unSpawnCreatures.isEmpty())
			{
				int i = 0;
				for (L2Spawn spawn : unSpawnCreatures)
				{
					if (spawn == null)
					{
						continue;
					}
					
					spawn.stopRespawn();
					L2NpcInstance last = spawn.getLastSpawn();
					if (last != null)
					{
						last.deleteMe();
						i++;
					}
				}
				_log.info("DayNightSpawnManager: Removed " + i + " " + UnspawnLogInfo + " creatures");
			}
			
			int i = 0;
			for (L2Spawn spawnDat : spawnCreatures)
			{
				if (spawnDat == null)
				{
					continue;
				}
				spawnDat.startRespawn();
				spawnDat.doSpawn();
				i++;
			}
			
			_log.info("DayNightSpawnManager: Spawned " + i + " " + SpawnLogInfo + " creatures");
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Error while spawning creatures: " + e.getMessage(), e);
		}
	}

    public void changeMode(int mode)
    {
        if (_nightCreatures.isEmpty() && _dayCreatures.isEmpty() && _bosses.isEmpty())
            return;
        
        // Avoid duplicate spawns
        if (_mode == mode)
        {
        	_log.warning("DayNightSpawnManager: Already in " + (_mode == 0 ? "day" : "night") + " mode.");
        	return;
        }
        
        _mode = mode;
        
        switch(_mode)
        {
            case 0:
                spawnDayCreatures();
                break;
            case 1:
                spawnNightCreatures();
                break;
            default:
                _log.warning("DayNightSpawnManager: Wrong mode sent");
                break;
        }
        
        specialNightBoss();
    }
    
    public void notifyChangeMode()
    {
        try
        {
            if (GameTimeController.getInstance().isNight())
                changeMode(1);
            else
                changeMode(0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void cleanUp()
    {
    	// Reset mode
    	_mode = -1;
    	
        _nightCreatures.clear();
        _dayCreatures.clear();
        _bosses.clear();
    }
    
    private void specialNightBoss()
	{
		try
		{
			L2RaidBossInstance boss;
			for (L2Spawn spawn : _bosses.keySet())
			{
				boss = _bosses.get(spawn);
				if (boss == null)
				{
					if (_mode == 1)
					{
						boss = (L2RaidBossInstance) spawn.doSpawn();
						RaidBossSpawnManager.getInstance().notifySpawnNightBoss(boss);
						_bosses.put(spawn, boss);
					}
					continue;
				}
				
				if (boss.getNpcId() == 10328 && boss.getRaidStatus().equals(RaidBossSpawnManager.StatusEnum.ALIVE))
				{
					handleHellmans(boss);
				}
				return;
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Error while specialNightBoss(): " + e.getMessage(), e);
		}
	}

    private void handleHellmans(L2RaidBossInstance boss)
    {
        switch(_mode)
        {
            case 0:
                boss.deleteMe();
                _log.info("DayNightSpawnManager: Deleting Hellman raidboss");
                break;
            case 1:
                boss.spawnMe();
                _log.info("DayNightSpawnManager: Spawning Hellman raidboss");
                break;
        }
    }
    
    public L2RaidBossInstance handleBoss(L2Spawn spawnDat)
    {
        if (_bosses.containsKey(spawnDat))
            return _bosses.get(spawnDat);

        if (GameTimeController.getInstance().isNight())
        {    
            L2RaidBossInstance raidboss = (L2RaidBossInstance)spawnDat.doSpawn();
            _bosses.put(spawnDat, raidboss);
            return raidboss;
        }
        _bosses.put(spawnDat, null);

        return null;
    }
    
    private static class SingletonHolder
	{
		protected static final DayNightSpawnManager _instance = new DayNightSpawnManager();
	}
}