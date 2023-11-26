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
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.MercTicketManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class MercTicket implements IItemHandler
{
    private static String[] _messages =
    {
    	"To arms!.",
    	"I am ready to serve you my lord when the time comes.",
    	"You summon me."
    };
    
    /**
     * Handler for using mercenary tickets.  Things to do:
     * 1) Check constraints:
     * 1.a) Tickets may only be used in a castle
     * 1.b) Only specific tickets may be used in each castle (different tickets for each castle)
     * 1.c) only the owner of that castle may use them
     * 1.d) tickets cannot be used during siege
     * 1.e) Check if max number of tickets has been reached
     * 1.f) Check if max number of tickets from this ticket's TYPE has been reached
     * 2) If allowed, call the MercTicketManager to add the item and spawn in the world
     * 3) Remove the item from the person's inventory  
     */
    @Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
    	int itemId = item.getItemId();
    	L2PcInstance activeChar = (L2PcInstance)playable;
    	Castle castle = CastleManager.getInstance().getCastle(activeChar);
        if (castle == null)
        {
            activeChar.sendMessage("Mercenary Tickets can only be used in castles.");
            return;
        }

        int castleId = castle.getCastleId();
        int ticketCastleId = MercTicketManager.getInstance().getTicketCastleId(itemId);
        
    	if (ticketCastleId != castleId)
    	{
            activeChar.sendPacket(new SystemMessage(SystemMessage.MERCENARIES_CANNOT_BE_POSITIONED_HERE));
            return;
    	}

        if (!activeChar.isCastleLord(castleId))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_AUTHORITY_TO_POSITION_MERCENARIES));
            return;
        }

        if (castle.getSiege().getIsInProgress())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE));
            return;
        }

        if (MercTicketManager.getInstance().isAtCastleLimit(item.getItemId()))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE));
            return;
        }

        if (MercTicketManager.getInstance().isAtTypeLimit(item.getItemId()))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE));
            return;
        }

        if (MercTicketManager.getInstance().isTooCloseToAnotherTicket(activeChar.getX(), activeChar.getY(), activeChar.getZ()))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.POSITIONING_CANNOT_BE_DONE_BECAUSE_DISTANCE_BETWEEN_MERCENARIES_TOO_SHORT));
            return;
        }

        // Remove item from char's inventory
        if (!activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false))
    	{
        	return;
    	}

        int npcId = MercTicketManager.getInstance().addTicket(item.getItemId(), activeChar, _messages);
        activeChar.sendMessage("Hired mercenary ("+itemId+","+npcId+") at coords:" + activeChar.getX() + "," + activeChar.getY() + "," + activeChar.getZ() + " heading:" + activeChar.getHeading());
    }
}