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
package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Support for /partyinfo command  
 * Added by Tempy - 28 Jul 05
 */
public class PartyInfo implements IUserCommandHandler
{
    private static final int[] COMMAND_IDS = { 81 }; 
	
    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#useUserCommand(int, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    @Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
    {
        if (id != COMMAND_IDS[0])
            return false;
    	if (!activeChar.isInParty())
    	{
            activeChar.sendMessage("Party does not exist.");
            return false;	
    	}

        L2Party playerParty = activeChar.getParty();
    	int memberCount = playerParty.getMemberCount();
    	int lootDistribution = playerParty.getLootDistribution();
    	String partyLeader = playerParty.getPartyMembers().get(0).getName();

        activeChar.sendPacket(new SystemMessage(1030));

        switch (lootDistribution)
        {
            case L2Party.ITEM_LOOTER:
                activeChar.sendPacket(new SystemMessage(1031));
                break;
            case L2Party.ITEM_ORDER:
                activeChar.sendPacket(new SystemMessage(1034));
                break;
            case L2Party.ITEM_ORDER_SPOIL:
                activeChar.sendPacket(new SystemMessage(1035));
                break;
            case L2Party.ITEM_RANDOM:
                activeChar.sendPacket(new SystemMessage(1032));
                break;
            case L2Party.ITEM_RANDOM_SPOIL:
                activeChar.sendPacket(new SystemMessage(1033));
                break;
        }

        SystemMessage sm = new SystemMessage(1611);                
    	sm.addString(partyLeader);
    	activeChar.sendPacket(sm);

    	activeChar.sendMessage("Members: " + memberCount + "/9");
    	activeChar.sendPacket(new SystemMessage(1030));
    	return true;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#getUserCommandList()
     */
    @Override
	public int[] getUserCommandList()
    {
        return COMMAND_IDS;
    }
}