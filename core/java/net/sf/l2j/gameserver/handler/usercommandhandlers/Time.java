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

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Time implements IUserCommandHandler
{
    private static final int[] COMMAND_IDS = { 77 };

    /* (non-Javadoc)
     * 
     */
    @Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
    {
        if (COMMAND_IDS[0] != id) return false;
        int t = GameTimeController.getInstance().getGameTime();
        String h = "" + (t/60)%24;
        String m = "" + t%60;
        if (t % 60 < 10)
            m = "0" + t%60;

        if (Config.ENABLE_REAL_TIME)
        {
            String RealTime = (new SimpleDateFormat("H:mm")).format(new Date());
            activeChar.sendMessage("It's "+RealTime+" in the real world.");
        }

        SystemMessage sm;
        if (GameTimeController.getInstance().isNight())
            sm = new SystemMessage(SystemMessage.TIME_S1_S2_IN_THE_NIGHT);
        else
            sm = new SystemMessage(SystemMessage.TIME_S1_S2_IN_THE_DAY);

        sm.addString(h);
        sm.addString(m);
        activeChar.sendPacket(sm);

        return true;
    }

    @Override
	public int[] getUserCommandList()
    {
        return COMMAND_IDS;
    }
}