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
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class SiegeGuardManager
{
    private final static Logger _log = Logger.getLogger(SiegeGuardManager.class.getName());
	
    // =========================================================
    // Data Field
    private Castle _castle;
    private List<L2Spawn> _siegeGuardSpawn  = new ArrayList<>();

    // =========================================================
    // Constructor
    public SiegeGuardManager(Castle castle)
    {
        _castle = castle;
    }

    /**
     * Add guard.<BR><BR>
     * @param activeChar 
     * @param npcId 
     */
    public void addSiegeGuard(L2PcInstance activeChar, int npcId)
    {
        if (activeChar == null)
        	return;
        
        addSiegeGuard(activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar.getHeading(), npcId);
    }

    /**
     * Add guard.<BR><BR>
     * @param x 
     * @param y 
     * @param z 
     * @param heading 
     * @param npcId 
     */
    public void addSiegeGuard(int x, int y, int z, int heading, int npcId)
    {
        saveSiegeGuard(x, y, z, heading, npcId, 0);
    }

    /**
     * Hire merc.<BR><BR>
     * @param activeChar 
     * @param npcId 
     */
    public void hireMerc(L2PcInstance activeChar, int npcId)
    {
        if (activeChar == null)
        	return;
        
        hireMerc(activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar.getHeading(), npcId);
    }

    /**
     * Hire merc.<BR><BR>
     * @param x 
     * @param y 
     * @param z 
     * @param heading 
     * @param npcId 
     */
    public void hireMerc(int x, int y, int z, int heading, int npcId)
    {
        saveSiegeGuard(x, y, z, heading, npcId, 1);
    }

    /**
     * Remove a single mercenary, identified by the npcId and location.  
     * Presumably, this is used when a castle lord picks up a previously dropped ticket 
     * @param npcId 
     * @param x 
     * @param y 
     * @param z 
     */
    public void removeMerc(int npcId, int x, int y, int z)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM castle_siege_guards WHERE npcId = ? And x = ? AND y = ? AND z = ? AND isHired = 1"))
        {
            statement.setInt(1, npcId);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, z);
            statement.execute();
        }
        catch (Exception e1)
        {
            _log.warning("Error deleting hired siege guard at " + x +','+y+','+z + ":" + e1);
        }
    }
    
    /**
     * Remove mercenaries.<BR><BR>
     */
    public void removeMercs()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM castle_siege_guards WHERE castleId = ? AND isHired = 1"))
        {
            statement.setInt(1, getCastle().getCastleId());
            statement.execute();
        }
        catch (Exception e1)
        {
            _log.warning("Error deleting hired siege guard for castle " + getCastle().getName() + ":" + e1);
        }
    }
    
    /**
     * Spawn guards.<BR><BR>
     */
    public void spawnSiegeGuard()
    {
        loadSiegeGuard();
        for (L2Spawn spawn: getSiegeGuardSpawn())
        {
            if (spawn != null)
            {
            	spawn.init();
            }
        }
    }

    /**
     * Unspawn guards.<BR><BR>
     */
    public void unspawnSiegeGuard()
    {
        for (L2Spawn spawn: getSiegeGuardSpawn())
        {
            if (spawn == null)
                continue;

            spawn.stopRespawn();
            spawn.getLastSpawn().doDie(spawn.getLastSpawn());
        }

        getSiegeGuardSpawn().clear();
    }

    /**
     * Load guards.<BR><BR>
     */
    private void loadSiegeGuard()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM castle_siege_guards WHERE castleId = ? AND isHired = ?"))
        {
            statement.setInt(1, getCastle().getCastleId());
            if (getCastle().getOwnerId() > 0)   // If castle is owned by a clan, then don't spawn default guards
                statement.setInt(2, 1);
            else
                statement.setInt(2, 0);
            try (ResultSet rs = statement.executeQuery())
            {
                L2Spawn spawn;
                L2NpcTemplate template;

                while (rs.next())
                {
                    template = NpcTable.getInstance().getTemplate(rs.getInt("npcId"));
                    if (template != null)
                    {
                        spawn = new L2Spawn(template);
                        spawn.setId(rs.getInt("id"));
                        spawn.setAmount(1);
                        spawn.setLocX(rs.getInt("x"));
                        spawn.setLocY(rs.getInt("y"));
                        spawn.setLocZ(rs.getInt("z"));
                        spawn.setHeading(rs.getInt("heading"));
                        spawn.setRespawnDelay(rs.getInt("respawnDelay"));
                        spawn.setLocation(0);

                        _siegeGuardSpawn.add(spawn);
                    }
                    else
                    {
                        _log.warning("Missing npc data in npc table for id: " + rs.getInt("npcId"));
                    }
                }
            }
        }
        catch (Exception e1)
        {
            _log.warning("Error loading siege guard for castle " + getCastle().getName() + ":" + e1);
        }
    }

    /**
     * Save guards.<BR><BR>
     * @param x 
     * @param y 
     * @param z 
     * @param heading 
     * @param npcId 
     * @param isHire 
     */
    private void saveSiegeGuard(int x, int y, int z, int heading, int npcId, int isHire)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("INSERT INTO castle_siege_guards (castleId, npcId, x, y, z, heading, respawnDelay, isHired) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            statement.setInt(1, getCastle().getCastleId());
            statement.setInt(2, npcId);
            statement.setInt(3, x);
            statement.setInt(4, y);
            statement.setInt(5, z);
            statement.setInt(6, heading);
            if (isHire == 1)
                statement.setInt(7, 0);
            else
                statement.setInt(7, 600);
            statement.setInt(8, isHire);
            statement.execute();
        }
        catch (Exception e1)
        {
            _log.warning("Error adding siege guard for castle " + getCastle().getName() + ":" + e1);
        }
    }

    public final Castle getCastle()
    {
        return _castle;
    }
    
    public final List<L2Spawn> getSiegeGuardSpawn()
    {
        return _siegeGuardSpawn;
    }
}