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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.eventgame.TvTEvent;
import net.sf.l2j.gameserver.model.eventgame.L2Event.EventState;
/**
 * This class handles following admin commands:
 * - admin = shows menu
 * 
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminEventEngine implements IAdminCommandHandler
{
    private static String[] _adminCommands = 
    {
	   	"admin_tvt_abort"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_tvt_abort"))
        {
            if (TvTEvent.getInstance().getEventState() == EventState.STARTED || TvTEvent.getInstance().getEventState() == EventState.REGISTER)
            {
                TvTEvent.getInstance().setEventState(EventState.INITIAL);
                activeChar.sendMessage("TvT Event was successfully aborted.");
            }
            else
                activeChar.sendMessage("TvT Event is not currently in progress.");
        }

        return true;
    }

	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}