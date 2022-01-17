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

/**
 * @author 
 *
 */
public class AdminRideWyvern implements IAdminCommandHandler
{
    private static String[] _adminCommands =
    {
        "admin_ride_wyvern",
        "admin_ride_strider",
        "admin_unride_wyvern",
        "admin_unride_strider",
        "admin_unride",
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_ride"))
        {
            if (activeChar.isMounted() || activeChar.getPet() != null)
            {
            	activeChar.sendMessage("Already Have a Pet or Mounted.");
                return false;
            }
            
            int petRideId;
            if (command.startsWith("admin_ride_wyvern"))
            	petRideId = 12621;
            else if (command.startsWith("admin_ride_strider"))
            	petRideId = 12526;
            else
            {
            	activeChar.sendMessage("Command '"+command+"' not recognized.");
                return false;
            }
            activeChar.mount(petRideId, 0, false);
            return false;
        }
        else if (command.startsWith("admin_unride"))
        {
            activeChar.dismount();
        }
        
        return true;
    }
    
    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }  
}