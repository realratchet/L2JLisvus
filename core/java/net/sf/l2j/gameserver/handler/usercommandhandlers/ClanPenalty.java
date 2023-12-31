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
package net.sf.l2j.gameserver.handler.usercommandhandlers;

import java.text.SimpleDateFormat;

import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.util.StringUtil;

/**
 * Support for clan penalty user command.  
 * @author Tempy
 */
public class ClanPenalty implements IUserCommandHandler
{
    private static final int[] COMMAND_IDS = { 100 }; 

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#useUserCommand(int, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    @Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
    {
        boolean penalty = false;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        StringBuilder htmlContent = new StringBuilder();
        
        StringUtil.append(htmlContent, "<html><body>");
        StringUtil.append(htmlContent, "<center><table width=270 border=0 bgcolor=111111>");
        StringUtil.append(htmlContent, "<tr><td width=170>Penalty</td>");
        StringUtil.append(htmlContent, "<td width=100 align=center>Expiration Date</td></tr>");
        StringUtil.append(htmlContent, "</table><table width=270 border=0><tr>");

        if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis())
        {
        	StringUtil.append(htmlContent, "<tr><td width=170>Unable to join a clan.</td>");
        	StringUtil.append(htmlContent, "<td width=100 align=center>"+format.format(activeChar.getClanJoinExpiryTime())+"</td></tr>");
            penalty = true;
        }

        if (activeChar.getClanCreateExpiryTime() > System.currentTimeMillis())
        {
        	StringUtil.append(htmlContent, "<tr><td width=170>Unable to create a clan.</td>");
        	StringUtil.append(htmlContent, "<td width=100 align=center>"+format.format(activeChar.getClanCreateExpiryTime())+"</td></tr>");
            penalty = true;
        }

        L2Clan clan = activeChar.getClan();
        if (clan != null)
        {
            if (clan.getCharPenaltyExpiryTime() > System.currentTimeMillis())
            {
            	StringUtil.append(htmlContent, "<tr><td width=170>Unable to invite players to clan.</td>");
            	StringUtil.append(htmlContent, "<td width=100 align=center>"+format.format(clan.getCharPenaltyExpiryTime())+"</td></tr>");
                penalty = true;
            }

            if (clan.getRecoverPenaltyExpiryTime() > System.currentTimeMillis())
            {
            	StringUtil.append(htmlContent, "<tr><td width=170>Unable to dissolve clan.</td>");
            	StringUtil.append(htmlContent, "<td width=100 align=center>"+format.format(clan.getRecoverPenaltyExpiryTime())+"</td></tr>");
                penalty = true;
            }
        }

        if (!penalty)
        {
        	StringUtil.append(htmlContent, "<td width=170>No penalties currently in effect.</td>");
        	StringUtil.append(htmlContent, "<td width=100 align=center> </td>");
        }
		
        StringUtil.append(htmlContent, "</tr></table><img src=\"L2UI.SquareWhite\" width=270 height=1>");
        StringUtil.append(htmlContent, "</center></body></html>");
        
        NpcHtmlMessage penaltyHtml = new NpcHtmlMessage(0);
        penaltyHtml.setHtml(htmlContent.toString());
        activeChar.sendPacket(penaltyHtml);
        return true;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#getUserCommandList()
     */
    @Override
	public int[] getUserCommandList()
    {
        return COMMAND_IDS;
    }
}