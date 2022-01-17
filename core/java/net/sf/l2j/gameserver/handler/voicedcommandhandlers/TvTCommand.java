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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.eventgame.TvTEvent;

public class TvTCommand implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS =
    {
    	"tvt",
        "tvtjoin",
        "tvtleave"
    };
   
    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
        if (!TvTEvent.getInstance().isEnabled())
            return false;

        if (command.equalsIgnoreCase("tvt"))
        {
        	activeChar.sendMessage("==================");
        	activeChar.sendMessage("TvT Event Commands");
        	activeChar.sendMessage("==================");
        	activeChar.sendMessage(".tvtjoin = Register to event");
        	activeChar.sendMessage(".tvtleave = Cancel event registration");
        }
        else if (command.equalsIgnoreCase("tvtjoin"))
        {
        	TvTEvent.getInstance().registerPlayer(activeChar);
        }
        else if (command.equalsIgnoreCase("tvtleave"))
        {
            if (TvTEvent.getInstance().isRegistered(activeChar))
            	TvTEvent.getInstance().removePlayer(activeChar);
            else
                activeChar.sendMessage("You are not registered for the TvT Event.");
        }

        return true;
    }

    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}