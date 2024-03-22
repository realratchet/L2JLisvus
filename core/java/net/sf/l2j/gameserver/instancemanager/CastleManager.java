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
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;

public class CastleManager
{
    private final static Logger _log = Logger.getLogger(CastleManager.class.getName());
    
    public static final CastleManager getInstance()
    {
        return SingletonHolder._instance;
    }
    
    // =========================================================
    // Data Field
    private List<Castle> _castles;
    
    private static final int _castleCirclets[] =
    {
        0,
        6838,
        6835,
        6839,
        6837,
        6840,
        6834,
        6836
    };
    public static final int CASTLE_LORD_CROWN = 6841;
    
    // =========================================================
    // Constructor
    public CastleManager()
    {
        _log.info(getClass().getSimpleName() + ": Initializing CastleManager");
        load();
    }
    
    public final int findNearestCastleIndex(L2Object obj)
    {
        int index = getCastleIndex(obj);
        if (index < 0)
        {
            double closestDistance = 99999999;
            double distance;
            Castle castle;
            
            int size = getCastles().size();
            for (int i = 0; i < size; i++)
            {
                castle = getCastles().get(i);
                if (castle == null)
                    continue;
                
                distance = castle.getDistance(obj);
                if (closestDistance > distance)
                {
                    closestDistance = distance;
                    index = i;
                }
            }
        }
        return index;
    }
    
    // =========================================================
    // Method - Private
    private final void load()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("Select id from castle order by id");
            ResultSet rs = statement.executeQuery())
        {
            while (rs.next())
            {
                getCastles().add(new Castle(rs.getInt("id")));
            }
            
            _log.info(getClass().getSimpleName() + ": Loaded: " + getCastles().size() + " castles");
        }
        catch (Exception e)
        {
            _log.warning(getClass().getSimpleName() + ": Exception: loadCastleData(): " + e.getMessage());
        }
    }
    
    // =========================================================
    // Property - Public
    public final Castle getCastleById(int castleId)
    {
        for (Castle temp : getCastles())
        {
            if (temp.getCastleId() == castleId)
                return temp;
        }
        return null;
    }
    
    public final Castle getCastleByOwner(L2Clan clan)
    {
        for (Castle temp : getCastles())
        {
            if (temp.getOwnerId() == clan.getClanId())
                return temp;
        }
        return null;
    }
    
    public final Castle getCastle(String name)
    {
        for (Castle temp : getCastles())
        {
            if (temp.getName().equalsIgnoreCase(name.trim()))
                return temp;
        }
        return null;
    }
    
    public final Castle getCastle(int x, int y, int z)
    {
        for (Castle temp : getCastles())
        {
            if (temp.checkIfInZone(x, y, z))
                return temp;
        }
        return null;
    }
    
    public final Castle getCastle(L2Object activeObject)
    {
        return getCastle(activeObject.getX(), activeObject.getY(), activeObject.getZ());
    }
    
    public final int getCastleIndex(int castleId)
    {
        Castle castle;

        int size = getCastles().size();
        for (int i = 0; i < size; i++)
        {
            castle = getCastles().get(i);
            if (castle != null && castle.getCastleId() == castleId)
                return i;
        }
        return -1;
    }
    
    public final int getCastleIndex(L2Object activeObject)
    {
        return getCastleIndex(activeObject.getX(), activeObject.getY(), activeObject.getZ());
    }
    
    public final int getCastleIndex(int x, int y, int z)
    {
        Castle castle;
        
        int size = getCastles().size();
        for (int i = 0; i < size; i++)
        {
            castle = getCastles().get(i);
            if (castle != null && castle.checkIfInZone(x, y, z))
                return i;
        }
        return -1;
    }
    
    public final List<Castle> getCastles()
    {
        if (_castles == null)
            _castles = new ArrayList<>();
        return _castles;
    }
    
    public final void validateTaxes(int sealStrifeOwner)
    {
        int maxTax;
        switch (sealStrifeOwner)
        {
            case SevenSigns.CABAL_DUSK:
                maxTax = 5;
                break;
            case SevenSigns.CABAL_DAWN:
                maxTax = 25;
                break;
            default: // no owner
                maxTax = 15;
                break;
        }
        
        for (Castle castle : _castles)
        {
            if (castle.getTaxPercent() > maxTax)
            {
                castle.setTaxPercent(maxTax);
            }
        }
    }
    
    public final void loadDoorUpgrades()
    {
        for (Castle temp : getCastles())
        {
            temp.loadDoorUpgrade();
        }
    }
    
    public int getCircletByCastleId(int castleId)
    {
        if (castleId > 0 && castleId < 8)
            return _castleCirclets[castleId];
        
        return 0;
    }
    
    /**
     * Removes castle circlets from all members.
     * @param clan
     * @param castleId
     */
    public void removeCirclet(L2Clan clan, int castleId)
    {
        for (L2ClanMember member : clan.getMembers())
        {
            removeCirclet(member, castleId, member.getObjectId() == clan.getLeaderId());
        }
    }
    
    /**
     * Removes castle circlets from member.
     * @param member
     * @param castleId
     * @param isLeader
     */
    public void removeCirclet(L2ClanMember member, int castleId, boolean isLeader)
    {
        if (member == null)
        {
            return;
        }
        
        L2PcInstance player = member.getPlayerInstance();
        int circletId = getCircletByCastleId(castleId);
        
        if (circletId != 0)
        {
            // Online-player circlet removal
            if (player != null)
            {
                try
                {
                    L2ItemInstance circlet = player.getInventory().getItemByItemId(circletId);
                    if (circlet != null)
                    {
                        if (circlet.isEquipped())
                            player.getInventory().unEquipItemInSlotAndRecord(circlet.getEquipSlot());
                        player.destroyItemByItemId("CastleCircletRemoval", circletId, 1, player, true);
                    }
                    
                    if (isLeader)
                    {
                        L2ItemInstance crown = player.getInventory().getItemByItemId(CASTLE_LORD_CROWN);
                        if (crown != null)
                        {
                            if (crown.isEquipped())
                                player.getInventory().unEquipItemInSlotAndRecord(crown.getEquipSlot());
                            player.destroyItemByItemId("CastleLordCrownRemoval", CASTLE_LORD_CROWN, 1, player, true);
                        }
                    }
                    
                    return;
                }
                catch (NullPointerException e)
                {
                    // Continue removing offline
                }
            }
            
            // Else offline-player circlet removal
            try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("DELETE FROM items WHERE owner_id = ? and item_id = ?"))
            {
                statement.setInt(1, member.getObjectId());
                statement.setInt(2, circletId);
                statement.execute();
                
                // If member is leader, delete castle lord's crown too
                if (isLeader)
                {
                    statement.setInt(2, CASTLE_LORD_CROWN);
                    statement.execute();
                }
            }
            catch (Exception e)
            {
                _log.warning(getClass().getSimpleName() + ": Failed to remove castle circlets for offline player " + member.getName());
                e.printStackTrace();
            }
        }
    }
    
    private static class SingletonHolder
    {
        protected static final CastleManager _instance = new CastleManager();
    }
}