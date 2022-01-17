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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class AdminLevel implements IAdminCommandHandler
{
    public static final String[] ADMIN_COMMANDS =
    {
        "admin_add_level",
        "admin_set_level"
    };

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IAdminCommandHandler#useAdminCommand(java.lang.String, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (activeChar == null)
        	return false;
        
		L2Object targetChar = activeChar.getTarget();
		String target = (targetChar != null ? targetChar.getName() : "no-target");
        GMAudit.auditGMAction(activeChar.getName(), command, target, "");

        StringTokenizer st = new StringTokenizer(command, " ");
        String actualCommand = st.nextToken(); // Get actual command

        String val = "";
        if (st.countTokens() >= 1) { val = st.nextToken(); }
 
        if (actualCommand.equalsIgnoreCase("admin_add_level"))
        {
            try
            {
                if (targetChar instanceof L2PlayableInstance)
                        ((L2PlayableInstance)targetChar).getStat().addLevel(Byte.parseByte(val));
            }
            catch (NumberFormatException e) { activeChar.sendMessage("Wrong Number Format"); }
        }
        else if(actualCommand.equalsIgnoreCase("admin_set_level"))
        {
            try
            {
            	if (targetChar == null || !(targetChar instanceof L2PcInstance))
                {
                    activeChar.sendPacket(new SystemMessage(144));	// incorrect target!
                    return false;
                }
            	L2PcInstance targetPlayer = (L2PcInstance)targetChar;

                byte lvl = Byte.parseByte(val);
            	if (lvl >= 1 && lvl <= Config.MAX_PLAYER_LEVEL)
            	{
            		long pXp = targetPlayer.getExp();
            		long tXp = Experience.LEVEL[lvl];
            		
            		if (pXp > tXp)
            		{
            			targetPlayer.removeExpAndSp(pXp - tXp, 0);
            			StatusUpdate su = new StatusUpdate(targetPlayer.getObjectId());
            			su.addAttribute(StatusUpdate.EXP, Experience.getVisualExp(targetPlayer.getLevel(), targetPlayer.getExp()));
            			targetPlayer.sendPacket(su);
            		}
            		else if (pXp < tXp)
            		{
            			// Do not share exp with pet
            			L2Summon summon = targetPlayer.getPet();
            			targetPlayer.setPet(null);
            			targetPlayer.addExpAndSp(tXp - pXp, 0);
            			targetPlayer.setPet(summon);
            		}
            	}
            	else
            	{
                    activeChar.sendMessage("You must specify level between 1 and " + Config.MAX_PLAYER_LEVEL + ".");
                    return false;
            	}
            }
            catch (NumberFormatException  e)
            {
                activeChar.sendMessage("You must specify level between 1 and " + Config.MAX_PLAYER_LEVEL + ".");
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IAdminCommandHandler#getAdminCommandList()
     */
    @Override
	public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }
}