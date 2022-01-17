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

import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager.CropProcure;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager.SeedProduction;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.util.StringUtil;

/**
 * Admin command handler for Manor System
 * This class handles following admin commands:
 * - manor_info = shows info about current manor state 
 * - manor_approve = approves settings for the next manor period 
 * - manor_setnext = changes manor settings to the next day's
 * - manor_reset castle = resets all manor data for specified castle (or all)
 * - manor_setmaintenance = sets manor system under maintenance mode
 * - manor_save = saves all manor data into database
 * - manor_disable = disables manor system
 * 
 * @author l3x
 */
public class AdminManor implements IAdminCommandHandler
{
    private static final String[] _adminCommands =
    {
        "admin_manor", "admin_manor_approve", 
        "admin_manor_setnext", "admin_manor_reset",
        "admin_manor_setmaintenance", "admin_manor_save",
        "admin_manor_disable"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        StringTokenizer st = new StringTokenizer(command);
        command = st.nextToken();

        if (command.equals("admin_manor"))
        {
            showMainPage(activeChar);
        }
        else if (command.equals("admin_manor_setnext"))
        {
            CastleManorManager.getInstance().setNextPeriod();
            CastleManorManager.getInstance().setNewManorRefresh();
            CastleManorManager.getInstance().updateManorRefresh();
            activeChar.sendMessage("Manor System: set to next period");
            showMainPage(activeChar);
        }
        else if (command.equals("admin_manor_approve"))
        {
            CastleManorManager.getInstance().approveNextPeriod();
            CastleManorManager.getInstance().setNewPeriodApprove();
            CastleManorManager.getInstance().updatePeriodApprove();
            activeChar.sendMessage("Manor System: next period approved");
            showMainPage(activeChar);
        }
        else if (command.equals("admin_manor_reset"))
        {
            int castleId = 0;
            try
            {
                castleId = Integer.parseInt(st.nextToken());
            }
            catch (Exception e) {}

            if (castleId > 0)
            {
                Castle castle = CastleManager.getInstance().getCastleById(castleId);
                castle.setCropProcure(new ArrayList<CropProcure>(), CastleManorManager.PERIOD_CURRENT);
                castle.setCropProcure(new ArrayList<CropProcure>(), CastleManorManager.PERIOD_NEXT);
                castle.setSeedProduction(new ArrayList<SeedProduction>(), CastleManorManager.PERIOD_CURRENT);
                castle.setSeedProduction(new ArrayList<SeedProduction>(), CastleManorManager.PERIOD_NEXT);

                if (Config.ALT_MANOR_SAVE_ALL_ACTIONS)
                {
                    castle.saveCropData();
                    castle.saveSeedData();
                }
                activeChar.sendMessage("Manor data for " + castle.getName() + " was nulled");
            }
            else
            {
                for (Castle castle : CastleManager.getInstance().getCastles())
                {
                    castle.setCropProcure(new ArrayList<CropProcure>(), CastleManorManager.PERIOD_CURRENT);
                    castle.setCropProcure(new ArrayList<CropProcure>(), CastleManorManager.PERIOD_NEXT);
                    castle.setSeedProduction(new ArrayList<SeedProduction>(), CastleManorManager.PERIOD_CURRENT);
                    castle.setSeedProduction(new ArrayList<SeedProduction>(), CastleManorManager.PERIOD_NEXT);

                    if (Config.ALT_MANOR_SAVE_ALL_ACTIONS)
                    {
                        castle.saveCropData();
                        castle.saveSeedData();
                    }
                }
                activeChar.sendMessage("Manor data was nulled");
            }
            showMainPage(activeChar);
        }
        else if (command.equals("admin_manor_setmaintenance"))
        {
            boolean mode = CastleManorManager.getInstance().isUnderMaintenance();
            CastleManorManager.getInstance().setUnderMaintenance(!mode);
            if (mode)
                activeChar.sendMessage("Manor System: not under maintenance");
            else
                activeChar.sendMessage("Manor System: under maintenance");
            showMainPage(activeChar);
        }
        else if (command.equals("admin_manor_save"))
        {
            CastleManorManager.getInstance().save();
            activeChar.sendMessage("Manor System: all data saved");
            showMainPage(activeChar);
        }
        else if (command.equals("admin_manor_disable"))
        {
            boolean mode = CastleManorManager.getInstance().isDisabled();
            CastleManorManager.getInstance().setDisabled(!mode);
            if (mode)
                activeChar.sendMessage("Manor System: enabled");
            else
                activeChar.sendMessage("Manor System: disabled");
            showMainPage(activeChar);
        }

        return true;
    }

    private String formatTime(long millis)
    {
        String s = "";
        int secs  = (int) millis/1000;
        int mins  = secs/60;
        secs -= mins*60;
        int hours = mins/60;
        mins -= hours*60;

        if (hours > 0)
            s += hours + ":";

        s += mins + ":";
        s += secs;
        return s;
    }

    private void showMainPage(L2PcInstance activeChar)
    {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        StringBuilder replyMSG = new StringBuilder(1000);
        StringUtil.append(replyMSG, "<html><body>");
        StringUtil.append(replyMSG, "<center><table width=260><tr><td width=40>");
        StringUtil.append(replyMSG, "<button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        StringUtil.append(replyMSG, "</td><td width=180>");
        StringUtil.append(replyMSG, "<center><font color=\"LEVEL\"> [Manor System] </font></center>");
        StringUtil.append(replyMSG, "</td><td width=40>");
        StringUtil.append(replyMSG, "</td></tr></table></center><br>");
        StringUtil.append(replyMSG, "<table width=\"100%\"><tr><td>");
        StringUtil.append(replyMSG, "Disabled: " + (CastleManorManager.getInstance().isDisabled()?"yes":"no") + "</td><td>");
        StringUtil.append(replyMSG, "Under Maintenance: " + (CastleManorManager.getInstance().isUnderMaintenance()?"yes":"no") + "</td></tr><tr><td>");
        StringUtil.append(replyMSG, "Time to refresh: " + formatTime(CastleManorManager.getInstance().getMillisToManorRefresh()) + "</td><td>");
        StringUtil.append(replyMSG, "Time to approve: " + formatTime(CastleManorManager.getInstance().getMillisToNextPeriodApprove()) + "</td></tr>");
        StringUtil.append(replyMSG, "</table>");

        StringUtil.append(replyMSG, "<center><table><tr><td>");
        StringUtil.append(replyMSG, "<button value=\"Set Next\" action=\"bypass -h admin_manor_setnext\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
        StringUtil.append(replyMSG, "<button value=\"Approve Next\" action=\"bypass -h admin_manor_approve\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr><tr><td>");
        StringUtil.append(replyMSG, "<button value=\""+(CastleManorManager.getInstance().isUnderMaintenance()?"Set normal":"Set mainteance")+"\" action=\"bypass -h admin_manor_setmaintenance\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
        StringUtil.append(replyMSG, "<button value=\""+(CastleManorManager.getInstance().isDisabled()?"Enable":"Disable")+"\" action=\"bypass -h admin_manor_disable\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr><tr><td>");
        StringUtil.append(replyMSG, "<button value=\"Refresh\" action=\"bypass -h admin_manor\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
        StringUtil.append(replyMSG, "<button value=\"Back\" action=\"bypass -h admin_admin\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
        StringUtil.append(replyMSG, "</table></center>");

        StringUtil.append(replyMSG, "<br><center>Castle Information:<table width=\"100%\">");
        StringUtil.append(replyMSG, "<tr><td></td><td>Current Period</td><td>Next Period</td></tr>");

        for (Castle c : CastleManager.getInstance().getCastles())
        	StringUtil.append(replyMSG, "<tr><td>"+c.getName()+"</td>" + "<td>"+c.getManorCost(CastleManorManager.PERIOD_CURRENT)+"a</td>" + "<td>"+c.getManorCost(CastleManorManager.PERIOD_NEXT)+"a</td>" + "</tr>");

        StringUtil.append(replyMSG, "</table><br></center>");
        StringUtil.append(replyMSG, "</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply); 		
    }

    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}