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

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Escape implements IUserCommandHandler
{
    private static final int[] COMMAND_IDS =
    {
        52
    };
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#useUserCommand(int, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    @Override
    public boolean useUserCommand(int id, L2PcInstance activeChar)
    {
        if (activeChar.isAlikeDead() || activeChar.isAllSkillsDisabled())
        {
            return false;
        }
        
        if (activeChar.isInOlympiadMode() || activeChar.inObserverMode() || activeChar.isFestivalParticipant() || activeChar.isInJail())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.NO_UNSTUCK_PLEASE_SEND_PETITION));
            return false;
        }
        
        if (activeChar.isInParty() && activeChar.getParty().isInDimensionalRift() || DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getX(), activeChar.getY(), activeChar.getZ(), true))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.NO_UNSTUCK_PLEASE_SEND_PETITION));
            return false;
        }
        
        if (GrandBossManager.getInstance().getZone(activeChar) != null)
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.NO_UNSTUCK_PLEASE_SEND_PETITION));
            return false;
        }
        
        L2Event event = activeChar.getEvent();
        if (event != null && event.isStarted())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.NO_UNSTUCK_PLEASE_SEND_PETITION));
            return false;
        }
        
        L2Skill skill;
        
        if (activeChar.isGM())
        {
            skill = SkillTable.getInstance().getInfo(2100, 1);
        }
        else
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.STUCK_TRANSPORT_IN_FIVE_MINUTES));
            skill = SkillTable.getInstance().getInfo(2099, 1);
        }
        
        activeChar.beginCast(skill);
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#getUserCommandList()
     */
    @Override
    public int[] getUserCommandList()
    {
        return COMMAND_IDS;
    }
}