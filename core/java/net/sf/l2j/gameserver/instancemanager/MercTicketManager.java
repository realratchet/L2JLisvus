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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.AutoChatHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author yellowperil & Fulminus
 * This class is similar to the SiegeGuardManager, except it handles
 * the loading of the mercenary tickets that are dropped on castle floors 
 * by the castle lords.
 * These tickets (aka badges) need to be read again after each server reboot
 * except when the server crashed in the middle of an ongoing siege.
 * In addition, this class keeps track of the added tickets, in order to 
 * properly limit the number of mercenaries in each castle and the 
 * number of mercenaries from each mercenary type.
 * Finally, we provide auxiliary functions to identify the castle in 
 * which each item (and its corresponding NPC) belong to, in order to 
 * help avoid mixing them up.
 *  
 */
public class MercTicketManager
{
    private final static Logger _log = Logger.getLogger(MercTicketManager.class.getName());

    public static final MercTicketManager getInstance()
    {
        return SingletonHolder._instance;
    }

    // =========================================================
    // Data Field
    private List<L2ItemInstance> _droppedTickets = new CopyOnWriteArrayList<>();	// To keep track of items on the ground

    //TODO move all these values into siege.properties
    // max tickets per merc type = 10 + (castleid * 2)?
    // max ticker per castle = 40 + (castleid * 20)?
    private final int[] _maxMercPerType = 
    {
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, // Gludio
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15, // Dion
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, // Giran
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, // Oren
        20, 20, 20, 20, 20, 20, 20, 20, 20, 20, // Aden
        20, 20, 20, 20, 20, 20, 20, 20, 20, 20, // Heine
        20, 20, 20, 20, 20, 20, 20, 20, 20, 20  // Goddard
    };

    private final int[] _mercsMaxPerCastle = 
    {
        50,  // Gludio
        75,  // Dion
        100, // Giran
        150, // Oren
        200, // Aden
        200, // Heine
        200  // Goddard
    };

    private final int[] _itemIds = 
    {
        3960, 3961, 3962, 3963, 3964, 3965, 3966, 3967, 3968, 3969, // Gludio
        3973, 3974, 3975, 3976, 3977, 3978, 3979, 3980, 3981, 3982, // Dion
        3986, 3987, 3988, 3989, 3990, 3991, 3992, 3993, 3994, 3995, // Giran
        3999, 4000, 4001, 4002, 4003, 4004, 4005, 4006, 4007, 4008, // Oren
        4012, 4013, 4014, 4015, 4016, 4017, 4018, 4019, 4020, 4021, // Aden
        5205, 5206, 5207, 5208, 5209, 5210, 5211, 5212, 5213, 5214, // Heine
        6779, 6780, 6781, 6782, 6783, 6784, 6785, 6786, 6787, 6788  // Goddard
    };

    private final int[] _npcIds = 
    {
        12301, 12302, 12303, 12304, 12305, 12306, 12307, 12308, 12309, 12310, // Gludio
        12301, 12302, 12303, 12304, 12305, 12306, 12307, 12308, 12309, 12310, // Dion
        12301, 12302, 12303, 12304, 12305, 12306, 12307, 12308, 12309, 12310, // Giran
        12301, 12302, 12303, 12304, 12305, 12306, 12307, 12308, 12309, 12310, // Oren
        12301, 12302, 12303, 12304, 12305, 12306, 12307, 12308, 12309, 12310, // Aden
        12301, 12302, 12303, 12304, 12305, 12306, 12307, 12308, 12309, 12310, // Heine
        12301, 12302, 12303, 12304, 12305, 12306, 12307, 12308, 12309, 12310  // Goddard
    };

    // =========================================================
    // Constructor
    public MercTicketManager()
    {
    	_log.info("Initializing MercTicketManager");
    	load();
    }

    // =========================================================
    // Method - Public
    // returns the castleId for the passed ticket item id
    public int getTicketCastleId(int itemId)
    {
        if (itemId >= _itemIds[0] &&  itemId <= _itemIds[9])
            return 1;	// Gludio
    	if (itemId >= _itemIds[10] &&  itemId <= _itemIds[19])
    	    return 2;	// Dion
    	if (itemId >= _itemIds[20] &&  itemId <= _itemIds[29])
    	    return 3;	// Giran
    	if (itemId >= _itemIds[30] &&  itemId <= _itemIds[39])
    	    return 4;	// Oren
    	if (itemId >= _itemIds[40] &&  itemId <= _itemIds[49])
    	    return 5;	// Aden
    	if (itemId >= _itemIds[50] &&  itemId <= _itemIds[59])
            return 6;	// Heine
        if (itemId >= _itemIds[60] && itemId <= _itemIds[69])
            return 7;   // Goddard
        return -1;
    }

    public void reload()
    {
        _droppedTickets.clear();
        load();
    }

    // =========================================================
    // Method - Private
    private final void load()
    {
        // load merc tickets into the world
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
	    PreparedStatement statement = con.prepareStatement("SELECT * FROM castle_siege_guards WHERE isHired = 1");
	    ResultSet rs = statement.executeQuery())
        {
            int npcId;
            int itemId;
            int x,y,z;
            int mercPlaced[] = new int[10];
            // start index to begin the search for the itemId corresponding to this NPC
            // this will help with: 
            //    a) skip unnecessary iterations in the search loop
            //    b) avoid finding the wrong itemId whenever tickets of different spawn the same npc!  
            int startindex = 0;

            while (rs.next())
            {
                npcId = rs.getInt("npcId");
                x = rs.getInt("x");
                y = rs.getInt("y");
                z = rs.getInt("z");
                Castle castle = CastleManager.getInstance().getCastle(x, y, z);
                if (castle != null)
                {
                    if (mercPlaced[castle.getCastleId()-1] >= _mercsMaxPerCastle[castle.getCastleId()-1])
                        continue;
                    startindex = 10*(castle.getCastleId()-1);
                    mercPlaced[castle.getCastleId()-1] += 1;
                }

                // find the FIRST ticket itemId with spawns the saved NPC in the saved location 
                for (int i = startindex; i < _npcIds.length; i++)
                {
                    if (_npcIds[i] == npcId) // Find the index of the item used
                    {
                        // only handle tickets if a siege is not ongoing in this npc's castle
                        if (castle != null && !(castle.getSiege().getIsInProgress()))
                        {
                            itemId = _itemIds[i];
                            // create the ticket in the game world
                            L2ItemInstance dropticket = new L2ItemInstance(IdFactory.getInstance().getNextId(), itemId);
                            dropticket.dropMe(null, x, y, z);
                            dropticket.setDropTime(0); //avoids it from being removed by the auto item destroyer
                            L2World.getInstance().storeObject(dropticket);
                            _droppedTickets.add(dropticket);
                        }
                        break;
                    }
                }
            }

            _log.info("Loaded: " + _droppedTickets.size() + " Mercenary Tickets");
        }
        catch (Exception e)
        {
            _log.warning("Exception: loadMercenaryData(): " + e.getMessage());
        }
    }

    // =========================================================
    // Property - Public
    /**
     * Checks if the passed item has reached the limit of number of dropped
     * tickets that this SPECIFIC item may have in its castle
     * @param itemId 
     * @return 
     */
    public boolean isAtTypeLimit(int itemId)
    {
    	int limit = -1; 
    	// find the max value for this item 
    	for (int i = 0; i < _itemIds.length; i++)
        {
            if (_itemIds[i] == itemId) // Find the index of the item used
            {	
            	limit = _maxMercPerType[i];
            	break;
            }
        }

    	if (limit <= 0)
            return true;

        int count = 0;
        for (L2ItemInstance item : _droppedTickets)
    	{
            if (item != null && item.getItemId() == itemId)
                count++;
    	}

        if (count >= limit)
            return true;

        return false;
    }

    /**
     * Checks if the passed item belongs to a castle which has reached its limit 
     * of number of dropped tickets.
     * @param itemId 
     * @return 
     */
    public boolean isAtCastleLimit(int itemId)
    {
    	int castleId = getTicketCastleId(itemId);
    	if (castleId <= 0)
            return true;
    	int limit = _mercsMaxPerCastle[castleId-1];
        if (limit <= 0)
            return true;

        int count = 0;
        for (L2ItemInstance item : _droppedTickets)
        {
            if (item != null && getTicketCastleId(item.getItemId()) == castleId)
                count++;    		
        }

        if (count >= limit)
            return true;

    	return false;
    }

    public boolean isTooCloseToAnotherTicket(int x, int y, int z)
    {
        for (L2ItemInstance item : _droppedTickets)
        {
            double dx = x - item.getX();
            double dy = y - item.getY();
            double dz = z - item.getZ();

            if ((dx*dx + dy*dy + dz*dz) < 25*25)
                return true;
        }
        return false;
    }

    /**
     * addTicket actions 
     * 1) find the npc that needs to be saved in the mercenary spawns, given this item
     * 2) Use the passed character's location info to add the spawn
     * 3) create a copy of the item to drop in the world
     * returns the id of the mercenary npc that was added to the spawn
     * returns -1 if this fails.
     * @param itemId 
     * @param activeChar 
     * @param messages 
     * @return 
     */ 
    public int addTicket(int itemId, L2PcInstance activeChar, String[] messages)
    {
    	int x = activeChar.getX();
    	int y = activeChar.getY();
    	int z = activeChar.getZ();
    	int heading = activeChar.getHeading();

        Castle castle = CastleManager.getInstance().getCastle(activeChar);
        if (castle == null)		// This should never happen at this point
            return -1;

        //check if this item can be added here
        for (int i = 0; i < _itemIds.length; i++)
        {
            if (_itemIds[i] == itemId) // Find the index of the item used
            {
                spawnMercenary(_npcIds[i], x, y, z, 3000, messages, 0);

                // Hire merc for this castle.  NpcId is at the same index as the item used.
                castle.getSiege().getSiegeGuardManager().hireMerc(x, y, z, heading, _npcIds[i]);

                // create the ticket in the game world
                L2ItemInstance dropticket = new L2ItemInstance(IdFactory.getInstance().getNextId(), itemId);
                dropticket.dropMe(null, x, y, z);
                dropticket.setDropTime(0); //avoids it from being removed by the auto item destroyer
                L2World.getInstance().storeObject(dropticket);	//add to the world
                // and keep track of this ticket in the list
                _droppedTickets.add(dropticket);

                return _npcIds[i];
            }
        }

        return -1;
    }

    private void spawnMercenary(int npcId, int x, int y, int z, int despawnDelay, String[] messages, int chatDelay)
    {
        L2NpcTemplate template = NpcTable.getInstance().getTemplate(npcId);
        if (template != null)
        {
            final L2NpcInstance npc = new L2NpcInstance(IdFactory.getInstance().getNextId(), template);
            npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp());
            npc.spawnMe(x, y, (z+20));

            if (messages != null && messages.length > 0)
            {
                AutoChatHandler.getInstance().registerChat(npc, messages, chatDelay);
            }

            if (despawnDelay > 0)
            {
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                    @Override
					public void run()
                    {
                        npc.deleteMe();
                    }
                }, despawnDelay);
            }
        }
    }

    /**
     * Delete all tickets from a castle; 
     * remove the items from the world and remove references to them from this class  
     * @param castleId 
     */
    public void deleteTickets(int castleId)
    {
    	for (L2ItemInstance item : _droppedTickets)
    	{
            if (item != null && getTicketCastleId(item.getItemId()) == castleId)
            {
                item.decayMe();
                L2World.getInstance().removeObject(item);

                // Remove from the list
                _droppedTickets.remove(item);
            }
        }
    }

    /**
     * Remove a single ticket and its associated spawn from the world
     * (used when the castle lord picks up a ticket, for example).
     * @param item 
     */
    public void removeTicket(L2ItemInstance item)
    {
    	int itemId = item.getItemId();
    	int npcId = -1;

    	// Find the first ticket itemId with spawns the saved NPC in the saved location 
    	for (int i = 0; i < _itemIds.length; i++)
        {
            if (_itemIds[i] == itemId) // Find the index of the item used
            {	
            	npcId = _npcIds[i];
            	break;
            }
        }

    	// Find the castle where this item is
    	Castle castle = CastleManager.getInstance().getCastleById(getTicketCastleId(itemId));
    	if (npcId > 0 && castle != null)
    	{
    		castle.getSiege().getSiegeGuardManager().removeMerc(npcId, item.getX(), item.getY(), item.getZ());
    	}

    	getDroppedTickets().remove(item);
    }

    public int[] getItemIds()
    {
        return _itemIds;
    }

    public final List<L2ItemInstance> getDroppedTickets()
    {
        return _droppedTickets;
    }
    
    private static class SingletonHolder
	{
		protected static final MercTicketManager _instance = new MercTicketManager();
	}
}