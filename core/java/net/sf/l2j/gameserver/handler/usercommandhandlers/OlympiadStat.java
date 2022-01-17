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
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Support for /olympiadstat command
 * Added by kamy
 */
public class OlympiadStat implements IUserCommandHandler
{
    private static final int[] COMMAND_IDS = { 109 }; 
	
    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#useUserCommand(int, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    @Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
    {
        if (COMMAND_IDS[0] != id)
            return false;
        SystemMessage sm = new SystemMessage(SystemMessage.THE_PRESENT_RECORD_DURING_THE_CURRENT_OLYMPIAD_SESSION_IS_S1_WINS_S2_DEFEATS_YOU_HAVE_EARNED_S3_OLYMPIAD_POINTS);
        sm.addNumber(Olympiad.getInstance().getCompetitionWon(activeChar.getObjectId()));
        sm.addNumber(Olympiad.getInstance().getCompetitionLost(activeChar.getObjectId()));
        sm.addNumber(Olympiad.getInstance().getNoblePoints(activeChar.getObjectId()));
    	activeChar.sendPacket(sm);
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