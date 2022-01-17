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

import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class handles following admin commands:
 * - open = open selected door
 * - close = close selected door
 * - openall = open all doors
 * - closeall = close all doors
 *
 */
public class AdminDoorControl implements IAdminCommandHandler
{
    private static String[] _adminCommands  = 
    {
        "admin_open",
        "admin_close",
        "admin_openall",
        "admin_closeall"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        try
        {
            if (command.startsWith("admin_open "))
            {
            	L2DoorInstance door;
            	int doorId = Integer.parseInt(command.substring(11));
            	door = DoorTable.getInstance().getDoor(doorId);
            	if (door != null)
            	{
            	    door.openMe();
            	}
            }
            else if (command.startsWith("admin_close "))
            {
            	L2DoorInstance door;
            	int doorId = Integer.parseInt(command.substring(12));
            	door = DoorTable.getInstance().getDoor(doorId);
            	if (door != null)
            	{
            	    door.closeMe();
            	}
            }
            else if (command.equals("admin_closeall"))
            {
                for(L2DoorInstance door : DoorTable.getInstance().getDoors())
                {
                    door.closeMe();
                }
            }
            else if (command.equals("admin_openall"))
            {
                for(L2DoorInstance door : DoorTable.getInstance().getDoors())
                {
                    door.openMe();
                }
            }
            else if (command.equals("admin_open"))
            {
                L2Object target = activeChar.getTarget();
                if (target instanceof L2DoorInstance)
                {
                    ((L2DoorInstance)target).openMe();
                }
                else
                {
                    activeChar.sendMessage("Incorrect target.");
                }
            }
            else if (command.equals("admin_close"))
            {
                L2Object target = activeChar.getTarget();
                if (target instanceof L2DoorInstance)
                {
                    ((L2DoorInstance)target).closeMe();
                }
                else
                {
                    activeChar.sendMessage("Incorrect target.");
                }
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String target = (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target");
        GMAudit.auditGMAction(activeChar.getName(), command, target, "");
        return true;
    }

    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}