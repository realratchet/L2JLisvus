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
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class handles following admin commands:
 * - add_exp_sp_to_character = show menu
 * - add_exp_sp = adds exp & sp to target
 * - remove_exp_sp = removes exp & sp from target
 * 
 * @version $Revision: 1.2.4.6 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminExpSp implements IAdminCommandHandler
{
    private static Logger _log = Logger.getLogger(AdminExpSp.class.getName());

    private static String[] _adminCommands =
    {
        "admin_add_exp_sp_to_character",
        "admin_add_exp_sp",
        "admin_remove_exp_sp"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
    	String val = "no-target";
        if (command.startsWith("admin_add_exp_sp "))
		{
			try
			{
				val = command.substring(16);
				if (!adminAddExpSp(activeChar, val))
				{
					activeChar.sendMessage("Usage: //add_exp_sp exp sp");
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				// Case of empty character name
				activeChar.sendMessage("Usage: //add_exp_sp exp sp");
			}
		}
        else if (command.startsWith("admin_remove_exp_sp "))
        {
            try
            {
                val = command.substring(19);
                if (!adminRemoveExpSP(activeChar, val))
                {
					activeChar.sendMessage("Usage: //remove_exp_sp exp sp");
                }
            }
            catch (StringIndexOutOfBoundsException e)
            {
            	// Case of empty character name
            	activeChar.sendMessage("Usage: //remove_exp_sp exp sp");
            }
        }

        addExpSp(activeChar);
        GMAudit.auditGMAction(activeChar.getName(), command,  val, "");
        return true;
    }
	
	private void addExpSp(L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
		L2PcInstance player = null;
		if (target instanceof L2PcInstance)
		{
			player = (L2PcInstance)target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
			return;
		}
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setFile("data/html/admin/expsp.htm");
		adminReply.replace("%name%", player.getName());
		adminReply.replace("%level%", String.valueOf(player.getLevel()));
		adminReply.replace("%xp%", String.valueOf(player.getExp()));
		adminReply.replace("%sp%", String.valueOf(player.getSp()));
		adminReply.replace("%class%", player.getTemplate().className);
		activeChar.sendPacket(adminReply);
	}

	private boolean adminAddExpSp(L2PcInstance activeChar, String ExpSp)
	{
		L2Object target = activeChar.getTarget();
		L2PcInstance player = null;
		if (target instanceof L2PcInstance)
        {
			player = (L2PcInstance)target;
		}
        else
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
			return false;
		}

		StringTokenizer st = new StringTokenizer(ExpSp);
		if (st.countTokens() != 2)
		{
			return false;
		}
		
		String exp = st.nextToken();
		String sp = st.nextToken();
        long expVal = 0;
        int spVal = 0;
        try
        {
    		expVal = Long.parseLong(exp);
    		spVal = Integer.parseInt(sp);
        }
        catch (NumberFormatException e)
        {
            //Wrong number (maybe it's too big?)
            activeChar.sendMessage("Wrong Number Format");
        }
        if (expVal != 0 || spVal != 0)
        {
    		// Common character information
    		player.sendMessage("Admin is adding you " + expVal + " xp and " + spVal + " sp.");
    		
    		player.addExpAndSp(expVal, spVal);
    
    		// Admin information	
    		activeChar.sendMessage("Added " + expVal + " xp and " + spVal + " sp to "+player.getName()+".");
    		if (Config.DEBUG)
            {
    			_log.fine("GM: " + activeChar.getName() + "(" + activeChar.getObjectId() + ") added " + expVal + " xp and " + spVal + " sp to " + player.getObjectId() + ".");
            }
		}

        return true;
	}
    
    private boolean adminRemoveExpSP(L2PcInstance activeChar, String expSp)
    {
        L2Object target = activeChar.getTarget();
        L2PcInstance player = null;
        if (target instanceof L2PcInstance)
        {
            player = (L2PcInstance)target;
        }
        else
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
            return false;
        }

        StringTokenizer st = new StringTokenizer(expSp);
        if (st.countTokens() != 2)
        {
            return false;
        }
        
        String exp = st.nextToken();
        String sp = st.nextToken();
        long expVal = 0;
        int spVal = 0;
        try
        {
            expVal = Long.parseLong(exp);
            spVal = Integer.parseInt(sp);
        }
        catch (Exception e)
        {
            return false;
        }
        
        if (expVal != 0 || spVal != 0)
        {
            // Common character information
            player.sendMessage("Admin is removing you " + expVal + " xp and " + spVal + " sp.");
            
            player.removeExpAndSp(expVal, spVal);
            
            StatusUpdate su = new StatusUpdate(player.getObjectId());
            su.addAttribute(StatusUpdate.EXP, Experience.getVisualExp(player.getLevel(), player.getExp()));
    		su.addAttribute(StatusUpdate.SP, player.getSp());
    		player.sendPacket(su);
    
            // Admin information 
            activeChar.sendMessage("Removed " + expVal + " xp and " + spVal + " sp from " + player.getName() + ".");
            if (Config.DEBUG)
            {
                _log.fine("GM: "+activeChar.getName() + "("+activeChar.getObjectId()+") added " + expVal + " xp and "+spVal+" sp to " + player.getObjectId() + ".");
            }
        }
        
        return true;
    }
    
    @Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}