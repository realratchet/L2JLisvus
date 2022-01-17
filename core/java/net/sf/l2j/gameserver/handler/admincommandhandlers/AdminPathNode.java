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

import java.util.List;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.pathfinding.AbstractNodeLoc;
import net.sf.l2j.gameserver.pathfinding.PathFinding;

public class AdminPathNode implements IAdminCommandHandler
{
    private static String[] _adminCommands =
    {
        "admin_pn_info",
        "admin_show_path",
        "admin_path_debug",
        "admin_show_pn",
        "admin_find_path"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.equals("admin_pn_info"))
        {
            final String[] info = PathFinding.getInstance().getStat();
            if (info == null)
            {
                activeChar.sendMessage("Not supported.");
            }
            else
            {
                for (String msg : info)
                {
                    activeChar.sendMessage(msg);
                }
            }
        }
        else if (command.equals("admin_show_path"))
        {

        }
        else if (command.equals("admin_path_debug"))
        {

        }
        else if (command.equals("admin_show_pn"))
        {

        }
        else if (command.equals("admin_find_path"))
        {
            if (Config.PATHFINDING == 0)
            {
                activeChar.sendMessage("PathFinding is disabled.");
                return true;
            }

            if (activeChar.getTarget() != null)
            {
                List<AbstractNodeLoc> path = PathFinding.getInstance().findPath(activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar.getTarget().getX(), activeChar.getTarget().getY(), activeChar.getTarget().getZ(), true);
                if (path == null)
                {
                	activeChar.sendMessage("No Route!");
                	return true;
                }

                for (AbstractNodeLoc a : path)
                {
                    activeChar.sendMessage("x:"+a.getX()+" y:"+a.getY()+" z:"+a.getZ());
                }
            }
            else
                activeChar.sendMessage("No Target!");
        }
        return true;
    }
    
    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    } 
}