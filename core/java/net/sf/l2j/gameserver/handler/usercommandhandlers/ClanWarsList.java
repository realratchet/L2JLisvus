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

import java.util.HashSet;
import java.util.Set;

import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Support for /clanwarlist command  
 * @author Tempy
 */
public class ClanWarsList implements IUserCommandHandler
{
    private static final int[] COMMAND_IDS = { 88, 89, 90 }; 

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#useUserCommand(int, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    @Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
    {
        L2Clan clan = activeChar.getClan();
        if (clan == null)
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_A_CLAN_MEMBER));
            return false;	
        }

        Set<Integer> list;

        if (id == 88)
        {
            // Attack List
            list = clan.getWarList();
            activeChar.sendPacket(new SystemMessage(SystemMessage.CLANS_YOU_DECLARED_WAR_ON));
        }
        else if (id == 89)
        {
            // Under Attack List
            list = clan.getAttackerList();
            activeChar.sendPacket(new SystemMessage(SystemMessage.CLANS_THAT_HAVE_DECLARED_WAR_ON_YOU));
        } 
        else // ID = 90
        {
            // War List
            list = new HashSet<>();
            list.addAll(clan.getWarList());
            list.addAll(clan.getAttackerList());
            activeChar.sendPacket(new SystemMessage(SystemMessage.WAR_LIST));
        }

        if (list.isEmpty())
        {
            if (id == 88)
            {
                activeChar.sendPacket(new SystemMessage(SystemMessage.NO_WARS_AGAINST_CLANS));
            }
            else if (id == 89)
            {
                activeChar.sendPacket(new SystemMessage(SystemMessage.NO_WARS_AGAINST_YOU));
            }
            else
            {
                activeChar.sendPacket(new SystemMessage(SystemMessage.NO_CLAN_ON_WAR_LIST));
            }
        }
        else
        {
            for (int clanId : list)
            {
                L2Clan requestedClan = ClanTable.getInstance().getClan(clanId);
                if (requestedClan != null)
                {
                    SystemMessage sm;

                    // Target with Ally
                    if (requestedClan.getAllyId() > 0)
                    {
                        sm = new SystemMessage(SystemMessage.S1_S2_ALLIANCE);
                        sm.addString(requestedClan.getName());
                        sm.addString(requestedClan.getAllyName());
                    }
                    else
                    {
                        sm = new SystemMessage(SystemMessage.S1_NO_ALLIANCE_EXISTS);
                        sm.addString(requestedClan.getName());
                    }
                    activeChar.sendPacket(sm);
                }
            }
        }

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