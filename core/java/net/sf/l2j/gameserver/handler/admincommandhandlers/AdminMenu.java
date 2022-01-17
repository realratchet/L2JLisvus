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

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class handles following admin commands:
 * - handles ever admin menu command
 * 
 * @version $Revision: 1.3.2.6.2.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminMenu implements IAdminCommandHandler 
{
    private static String[] _adminCommands =
    {
		"admin_char_manage",
		"admin_teleport_character_to_menu",
		"admin_recall_char_menu",
		"admin_goto_char_menu",
		"admin_kick_menu",
		"admin_kill_menu",
		"admin_ban_menu",
		"admin_unban_menu"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		String target = (activeChar.getTarget() != null?activeChar.getTarget().getName():"no-target");
        GMAudit.auditGMAction(activeChar.getName(), command, target, "");

        if (command.equals("admin_char_manage"))
		{
			showHelpPage(activeChar, "charmanage.htm");
		}
		else if (command.startsWith("admin_teleport_character_to_menu"))
        {
			String[] data = command.split(" ");
            if(data.length==5)
            {
				String playerName=data[1];
                int x=Integer.parseInt(data[2]);
                int y=Integer.parseInt(data[3]);
                int z=Integer.parseInt(data[4]);
                L2PcInstance player = L2World.getInstance().getPlayer(playerName);
                if(player!=null)
                {
					teleportCharacter(player,x,y,z,activeChar);
                }
			}
			showHelpPage(activeChar, "charmanage.htm");
		}
		else if (command.startsWith("admin_recall_char_menu"))
		{
            try
            {
    		    String targetName = command.substring(23);
    		    L2PcInstance player = L2World.getInstance().getPlayer(targetName);
    		    int x = activeChar.getX();
    			int y = activeChar.getY();
    			int z = activeChar.getZ();
    		    teleportCharacter(player,x,y,z,activeChar);
            }
            catch (StringIndexOutOfBoundsException e)
            { }
		}
		else if (command.startsWith("admin_goto_char_menu"))
		{
            try
            {
    		    String targetName = command.substring(21);
    		    L2PcInstance player = L2World.getInstance().getPlayer(targetName);
    	        teleportToCharacter(activeChar, player);
            }
            catch (StringIndexOutOfBoundsException e)
            { }
		}
		else if (command.equals("admin_kill_menu"))
		{
			handleKill(activeChar);
		}
		else if (command.startsWith("admin_kick_menu"))
        {
            StringTokenizer st = new StringTokenizer(command);
            if (st.countTokens() > 1)
            {
                st.nextToken();
                String player = st.nextToken();
                
                L2PcInstance plyr = L2World.getInstance().getPlayer(player);
				if (plyr != null)
				{
					plyr.logout();
					activeChar.sendMessage("You kicked " + plyr.getName() + " from the game.");
				}
				else
				{
					activeChar.sendMessage("Player " + player + " was not found in the game.");
				}
            }
			showHelpPage(activeChar, "charmanage.htm");
        }
        else if (command.startsWith("admin_ban_menu"))
        {
            StringTokenizer st = new StringTokenizer(command);
            if (st.countTokens() > 1)
            {
                st.nextToken();
                String player = st.nextToken();
                L2PcInstance plyr = L2World.getInstance().getPlayer(player);
                if (plyr != null)
                {
                    plyr.logout();
                    LoginServerThread.getInstance().sendAccessLevel(plyr.getAccountName(), -100);
                    activeChar.sendMessage("A ban request has been sent for account "+plyr.getAccountName()+".");
                }
                else
                    activeChar.sendMessage("Target is not online.");

            }
            showHelpPage(activeChar, "charmanage.htm");
        }
        else if (command.startsWith("admin_unban_menu"))
        {
            StringTokenizer st = new StringTokenizer(command);
            if (st.countTokens() > 1)
            {
                st.nextToken();
                String player = st.nextToken();
                LoginServerThread.getInstance().sendAccessLevel(player, 0);
                activeChar.sendMessage("An unban request has been sent for account "+player+".");
            }
            showHelpPage(activeChar, "charmanage.htm");
        }
        return true;
    }

    private void handleKill(L2PcInstance activeChar)
    {
        handleKill(activeChar, null);
    }

	private void handleKill(L2PcInstance activeChar, String player)
    {
		L2Object obj = activeChar.getTarget();
        if (player != null)
        {
            L2PcInstance plyr = L2World.getInstance().getPlayer(player);
            if (plyr != null)
            {
                obj = plyr;
            }
        }
        
		if (obj != null && obj instanceof L2Character)
		{
			L2Character target = (L2Character)obj;
			target.reduceCurrentHp(target.getMaxHp()+1, activeChar);
			
			activeChar.sendMessage("You killed " + obj.getName() + ".");
		}
		else
		{
			activeChar.sendMessage("Incorrect target.");
		}
		showHelpPage(activeChar, "charmanage.htm");
	}

    private void teleportCharacter(L2PcInstance player, int x, int y, int z, L2PcInstance activeChar)
    {
        if (player != null)
        {
        	// Information
            if (activeChar != null)
            {
                activeChar.sendMessage("You have recalled " + player.getName());
            }
        	player.sendMessage("Admin is teleporting you.");
            player.teleToLocation(x, y, z, true);
        }
        showHelpPage(activeChar, "charmanage.htm");
    }

	private void teleportToCharacter(L2PcInstance activeChar, L2Object target)
	{
	    L2PcInstance player = null;
		if (target != null && target instanceof L2PcInstance) 
		{
			player = (L2PcInstance)target;
		} 
		else 
		{
			activeChar.sendMessage("Incorrect target.");
			return;
		}
		
		if (player.getObjectId() == activeChar.getObjectId())
		{	
			activeChar.sendMessage("You cannot self teleport.");
		}
		else
		{
			int x = player.getX();
			int y = player.getY();
			int z = player.getZ();
			
			activeChar.teleToLocation(x, y, z, true);
		
			activeChar.sendMessage("You have teleported to character " + player.getName() + ".");
		}
		showHelpPage(activeChar, "charmanage.htm");
	}
	
	@Override
	public String[] getAdminCommandList() 
    {
        return _adminCommands;
    }
}