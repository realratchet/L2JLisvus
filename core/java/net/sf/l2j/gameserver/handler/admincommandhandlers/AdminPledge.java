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

import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Pledge Manipulation.
 */
public class AdminPledge implements IAdminCommandHandler
{
    private static String[] _adminCommands =
    {
    	"admin_pledge",
        "admin_pledge_create",
        "admin_pledge_delete",
        "admin_pledge_level"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
    	GMAudit.auditGMAction(activeChar.getName(), command, (activeChar.getTarget() != null) ? activeChar.getTarget().getName() : "no-target", "");
    	
        if (command.startsWith("admin_pledge_create"))
        {
        	L2PcInstance target = getTargetPlayer(activeChar);
        	if (target == null)
        	{
        		showMainPage(activeChar);
        		return false;
        	}
        	
        	if (target.getClan() != null)
            {
            	activeChar.sendMessage("Target already belongs to a clan.");
            	showMainPage(activeChar);
            	return false;
            }
            
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            
            String name = st.hasMoreTokens() ? st.nextToken() : "";
            if (name.isEmpty())
            {
            	activeChar.sendMessage("Usage: //pledge_create <name>");
            	showMainPage(activeChar);
            	return false;
            }
            
            long time = target.getClanCreateExpiryTime();
            target.setClanCreateExpiryTime(0);
            L2Clan clan = ClanTable.getInstance().createClan(target, name);
            if (clan != null)
                activeChar.sendMessage("Clan " + name + " created! Leader: "+target.getName());
            else
            {
                target.setClanCreateExpiryTime(time);
                activeChar.sendMessage("There was a problem while creating the clan.");
            }
        }
        else if (command.startsWith("admin_pledge_delete"))
        {
        	L2PcInstance target = getTargetPlayer(activeChar);
        	if (target == null)
        	{
        		showMainPage(activeChar);
        		return false;
        	}
        	
        	if (target.getClan() == null)
            {
            	activeChar.sendMessage("Target is not associated with any clan.");
            	showMainPage(activeChar);
            	return false;
            }
            
            ClanTable.getInstance().destroyClan(target.getClanId());
            if (target.getClan() != null)
            {
                activeChar.sendMessage("There was a problem while destroying the clan.");
            }
        }
        else if (command.startsWith("admin_pledge_level"))
        {
        	L2PcInstance target = getTargetPlayer(activeChar);
        	if (target == null)
        	{
        		showMainPage(activeChar);
        		return false;
        	}
        	
        	if (target.getClan() == null)
            {
            	activeChar.sendMessage("Target is not associated with any clan.");
            	showMainPage(activeChar);
            	return false;
            }
            
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            
            try
            {
            	int level = Integer.parseInt(st.nextToken());
                if (level >= 0 && level < 6)
                {
                    target.getClan().changeLevel(level);
                    activeChar.sendMessage("Clan " + target.getClan().getName() + " has been upgraded to level " + level + ".");
                }
                else
                    activeChar.sendMessage("Incorrect level.");
            }
            catch (Exception e)
            {
            	activeChar.sendMessage("Usage: //pledge_level <level>");
            }
        }
        
        showMainPage(activeChar);
        return true;
    }

    public void showMainPage(L2PcInstance activeChar)
    {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        adminReply.setFile("data/html/admin/pledge.htm");
        activeChar.sendPacket(adminReply);
    }
    
    private L2PcInstance getTargetPlayer(L2PcInstance activeChar)
    {
    	if (activeChar.getTarget() == null)
        {
    		activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return null;
        }

        if (!(activeChar.getTarget() instanceof L2PcInstance))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
            return null;
        }
    	
        L2PcInstance target = (L2PcInstance)activeChar.getTarget();
        return target;
    }

    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}