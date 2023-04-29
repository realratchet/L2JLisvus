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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PunishmentLevel;

/**
 * This class handles following admin commands:
 * - kill = kills target L2Character
 * 
 * @version $Revision: 1.1.6.3 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminBan implements IAdminCommandHandler
{
    private static String[] _adminCommands =
    {
        "admin_ban", "admin_unban","admin_jail","admin_unjail"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        StringTokenizer st = new StringTokenizer(command);
        st.nextToken();

        String targetName = "";

        if (command.startsWith("admin_ban"))
        {   
            try
            {
                targetName = st.nextToken();
                final L2PcInstance targetPlayer = L2World.getInstance().getPlayer(targetName);
                if (targetPlayer != null)
                {
                    if (targetPlayer == activeChar)
                    {
                        activeChar.sendMessage("You cannot use it on yourself.");
                    }
                    else
                    {
                        targetPlayer.setAccountAccesslevel(-100);
                        activeChar.sendMessage("Account of " + targetPlayer.getName() + " was banned.");
                        targetPlayer.logout();
                    }
                }
                else
                {
                    LoginServerThread.getInstance().sendAccessLevel(targetName, -100);
                    activeChar.sendMessage("This player is not ingame, so an account ban request was sent for " + targetName + " in case this name belongs to an account.");
                }
            }
            catch(Exception e)
            {
                activeChar.sendMessage("Usage: //ban <name>");
                if (Config.DEBUG)
                    e.printStackTrace();
            }
        }
        else if (command.startsWith("admin_unban"))
        {
            try
            {
            	targetName = st.nextToken();
            	LoginServerThread.getInstance().sendAccessLevel(targetName, 0);
                activeChar.sendMessage("Unban request was sent for account " + targetName + ".");
            }
            catch(Exception e)
            {
                activeChar.sendMessage("Usage: //unban <account_name>");
                if (Config.DEBUG)
                    e.printStackTrace();
            }
        }
        else if (command.startsWith("admin_jail"))
        {
            try
            {
                targetName = st.nextToken();
                int delay = 0;

                try
                {
                    delay = Integer.parseInt(st.nextToken());
                }
                catch (NumberFormatException nfe)
                {
                    activeChar.sendMessage("Usage: //jail <player_name> [penalty_minutes]");
                }
                catch (NoSuchElementException nsee) {}

                final L2PcInstance targetPlayer = L2World.getInstance().getPlayer(targetName);
                if (targetPlayer != null)
                {
                    if (PunishmentLevel.JAIL.getSeverity() < targetPlayer.getPunishmentLevel().getSeverity())
                    {
                        activeChar.sendMessage("Character " + targetPlayer.getName() + " is currently undergoing a more severe punishment: " + targetPlayer.getPunishmentLevel().getDescription());
                    }
                    else
                    {
                        targetPlayer.setPunishment(PunishmentLevel.JAIL, delay);
                        activeChar.sendMessage("Character " + targetName + " has been jailed for " + (delay > 0 ? delay + " minute(s)." : "ever!"));
                    }
                }
                else
                {
                    jailOfflinePlayer(activeChar, targetName, delay);
                }
            }
            catch (NoSuchElementException nsee) 
            {
                activeChar.sendMessage("Usage: //jail <player_name> [penalty_minutes]");
            }
            catch(Exception e)
            {
                if (Config.DEBUG)
                    e.printStackTrace();
            }            
        }
        else if (command.startsWith("admin_unjail"))
        {
            try
            {
                targetName = st.nextToken();
                final L2PcInstance targetPlayer = L2World.getInstance().getPlayer(targetName);
                if (targetPlayer != null)
                {
                    if (targetPlayer.isInJail())
                    {
                        targetPlayer.setPunishment(PunishmentLevel.NONE, 0);
                    }
                    else
                    {
                        activeChar.sendMessage("Character " + targetPlayer.getName() + " is not jailed.");
                    }
                }
                else
                {
                    unjailOfflinePlayer(activeChar, targetName);
                }
            }
            catch (NoSuchElementException nsee) 
            {
                activeChar.sendMessage("Specify a character name.");
            }
            catch(Exception e)
            {
                if (Config.DEBUG)
                    e.printStackTrace();
            }            
        }

        GMAudit.auditGMAction(activeChar.getName(), command, targetName, "");
        return true;
    }

    private void jailOfflinePlayer(L2PcInstance activeChar, String name, int delay)
    {
        final int playerId = CharNameTable.getInstance().getIdByName(name);
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=?, punish_level=?, punish_timer=? WHERE obj_Id=? AND punish_level<=?"))
        {
            statement.setInt(1, -114356);
            statement.setInt(2, -249645);
            statement.setInt(3, -2984);
            statement.setInt(4, PunishmentLevel.JAIL.ordinal());
            statement.setLong(5, delay * 60000);
            statement.setInt(6, playerId);
            statement.setInt(7, PunishmentLevel.JAIL.ordinal());

            statement.execute();
            int count = statement.getUpdateCount();

            if (count == 0)
                activeChar.sendMessage("Failed to apply jail punishment to character " + name + ". Please make sure that character exists and is not undergoing a more severe punishment.");
            else
                activeChar.sendMessage("Character "+name+" was jailed for "+(delay>0 ? delay+" minute(s)." : "ever!"));
    	}
        catch (SQLException se)
    	{
            activeChar.sendMessage("SQLException while jailing player");
            if (Config.DEBUG)
                se.printStackTrace();
    	}
    }
    
    private void unjailOfflinePlayer(L2PcInstance activeChar, String name)
    {
        final int playerId = CharNameTable.getInstance().getIdByName(name);
    	try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=?, punish_level=?, punish_timer=? WHERE obj_Id=? AND punish_level=?"))
    	{
            statement.setInt(1, 17836);
            statement.setInt(2, 170178);
            statement.setInt(3, -3507);
            statement.setInt(4, PunishmentLevel.NONE.ordinal());
            statement.setLong(5, 0);
            statement.setInt(6, playerId);
            statement.setInt(7, PunishmentLevel.JAIL.ordinal());

            statement.execute();
            int count = statement.getUpdateCount();

            if (count == 0)
                activeChar.sendMessage("Failed to remove character " + name + " from jail. Please make sure that character exists and is in jail.");
            else
                activeChar.sendMessage("Character "+name+" was removed from jail.");
    	}
        catch (SQLException se)
    	{
            activeChar.sendMessage("SQLException while jailing player.");
            if (Config.DEBUG)
                se.printStackTrace();
    	}
    }

    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}