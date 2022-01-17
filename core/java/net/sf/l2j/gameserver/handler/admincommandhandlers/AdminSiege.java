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
import net.sf.l2j.gameserver.instancemanager.AuctionManager;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.zone.type.L2ClanHallZone;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.StringUtil;

/**
 * This class handles all siege commands:
 * Todo: change the class name, and neaten it up
 * 
 *
 */
public class AdminSiege implements IAdminCommandHandler
{
    private static String[] _adminCommands =
    {
        "admin_siege", "admin_add_attacker",
        "admin_add_defender", "admin_add_guard",
        "admin_list_siege_clans", "admin_clear_siege_list",
        "admin_move_defenders", "admin_spawn_doors",
        "admin_endsiege", "admin_startsiege",
        "admin_setcastle", "admin_castledel", 
        "admin_clanhall", "admin_clanhallset", 
        "admin_clanhalldel", "admin_clanhallopendoors", 
        "admin_clanhallclosedoors", "admin_clanhallteleportself"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        StringTokenizer st = new StringTokenizer(command, " ");
        command = st.nextToken(); // Get actual command

        // Get castle
        Castle castle = null;
        ClanHall clanhall = null;
        if (command.startsWith("admin_clanhall"))
            clanhall = ClanHallManager.getInstance().getClanHallById(Integer.parseInt(st.nextToken()));
        else if (st.hasMoreTokens())
            castle = CastleManager.getInstance().getCastle(st.nextToken());
        // Get castle
        String val = "";
        if (st.hasMoreTokens())
        {
        	val = st.nextToken();
        }
        if ((castle == null  || castle.getCastleId() < 0) && clanhall == null)
            // No castle specified
            showCastleSelectPage(activeChar);
        else
        {
        	try
        	{
	            L2Object target = activeChar.getTarget();
	            L2PcInstance player = null;
	            if (target instanceof L2PcInstance)
	                player = (L2PcInstance)target;
	            if (command.equalsIgnoreCase("admin_add_attacker"))
	            {
	                if (player == null)
	                {
	                    activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
	                }
	                else if (castle != null)
	                {
	                    if (!SiegeManager.getInstance().checkIsRegistered(player.getClan(), castle.getCastleId()))
	                        castle.getSiege().registerAttacker(player, true);
	                }
	            }
	            else if (command.equalsIgnoreCase("admin_add_defender"))
	            {
	                if (player == null)
	                {
	                    activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
	                }
	                else if (castle != null)
	                {
	                    if (!SiegeManager.getInstance().checkIsRegistered(player.getClan(), castle.getCastleId()))
	                        castle.getSiege().registerDefender(player, true);
	                }
	            }
	            else if (command.equalsIgnoreCase("admin_add_guard"))
	            {
	                if (!val.isEmpty())
	                {
	                    try
	                    {
	                        int npcId = Integer.parseInt(val);
	                        if (castle != null)
	                        {
	                        	castle.getSiege().getSiegeGuardManager().addSiegeGuard(activeChar, npcId);
	                        }
	                    }
	                    catch (Exception e)
	                    {
	                        activeChar.sendMessage("Value entered for Npc Id wasn't an integer");
	                    }
	                }
	                else
	                    activeChar.sendMessage("Missing Npc Id");
	            }
	            else if (command.equalsIgnoreCase("admin_clear_siege_list"))
	            {
	            	if (castle != null)
                    {
                    	castle.getSiege().clearSiegeClans();
                    }
	            }
	            else if (command.equalsIgnoreCase("admin_endsiege"))
	            {
	            	if (castle != null)
	            	{
	            		if (castle.getSiege().getIsInProgress())
	            		{
	            			castle.getSiege().endSiege();
	            		}
	            		else
		            	{
		            		activeChar.sendMessage("There is currently no siege in progress.");
		            	}
	            	}
	            }
	            else if (command.equalsIgnoreCase("admin_list_siege_clans"))
	            {
	            	if (castle != null)
                    {
                    	castle.getSiege().listRegisterClan(activeChar);
                    }
	                return true;
	            }
	            else if (command.equalsIgnoreCase("admin_move_defenders"))
	            {
	                activeChar.sendPacket(SystemMessage.sendString("Not implemented yet."));
	            }
	            else if (command.equalsIgnoreCase("admin_setcastle"))
	            {
	                if (player == null || player.getClan() == null)
	                {
	                    activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
	                }
	                else if (castle != null)
	                {
	                    if (player.getClan().getHasCastle() == 0)
	                        castle.setOwner(player.getClan());
	                }
	            }
	            else if (command.equalsIgnoreCase("admin_castledel"))
	            {
	                 if (castle != null && castle.getOwnerId() > 0)
	                 {
	                     castle.setOwner(null);
	                     if (player != null && player.getClan() != null)
	                     {
	                    	 player.getClan().broadcastToOnlineMembers(new PledgeShowInfoUpdate(player.getClan()));
	                     }
	                 }
	            }
	            else if (command.equalsIgnoreCase("admin_clanhallset"))
	            {
	                if (player == null || player.getClan() == null)
	                {
	                    activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
	                }
	                else if (clanhall != null)
	                {
	                    if (player.getClan().getHasHideout() == 0)
	                    {
	                        if (clanhall.getOwnerId() == 0)
	                        {
	                            clanhall.setOwner(player.getClan());
	                            if (AuctionManager.getInstance().getAuction(player.getClan().getAuctionBiddedAt()) != null)
	                                AuctionManager.getInstance().getAuction(player.getClan().getAuctionBiddedAt()).cancelBid(player.getClan().getClanId());
	                            if (AuctionManager.getInstance().getAuction(clanhall.getId()) != null)
	                            {
	                                if (!AuctionManager.getInstance().getAuction(clanhall.getId()).getBidders().isEmpty())
	                                    AuctionManager.getInstance().getAuction(clanhall.getId()).removeBids();
	                                AuctionManager.getInstance().getAuction(clanhall.getId()).deleteAuctionFromDB();
	                            }
	                        }
	                    }
	                }
	            }
	            else if (command.equalsIgnoreCase("admin_clanhalldel"))
	            {
	                 if (clanhall != null && clanhall.getOwnerId() > 0)
	                 {
	                     clanhall.setOwner(null);
	                     if (player != null && player.getClan() != null)
	                     {
	                    	 player.getClan().broadcastToOnlineMembers(new PledgeShowInfoUpdate(player.getClan()));
	                     }
	                 }
	            }
	            else if (command.equalsIgnoreCase("admin_clanhallopendoors"))
	            {
	            	if (clanhall != null)
	                {
	            		clanhall.openCloseDoors(true);
	                }
	            }
	            else if (command.equalsIgnoreCase("admin_clanhallclosedoors"))
	            {
	            	if (clanhall != null)
	                {
	            		clanhall.openCloseDoors(false);
	                }
	            }
	            else if (command.equalsIgnoreCase("admin_clanhallteleportself"))
	            {
	                if (clanhall != null)
	                {
	                    L2ClanHallZone zone = clanhall.getZone();
	                    if (zone != null)
	                        activeChar.teleToLocation(zone.getSpawnLoc(), true);
	                }
	            }
	            else if (command.equalsIgnoreCase("admin_spawn_doors"))
                {
	            	if (castle != null)
	            	{
	            		castle.spawnDoors();
	            	}
                }
	            else if (command.equalsIgnoreCase("admin_startsiege"))
	            {
	            	if (castle != null)
	            	{
	            		if (!castle.getSiege().getIsInProgress())
	            		{
	            			castle.getSiege().startSiege();
	            		}
	            		else
		            	{
		            		activeChar.sendMessage("A siege is already in progress.");
		            	}
	            	}
	            }
	            if (clanhall != null)
	                showClanHallPage(activeChar, clanhall);
	            else 
	                showSiegePage(activeChar, castle != null ? castle.getName() : null);
	        }
        	catch (NullPointerException e)
            {
            	activeChar.sendMessage("An error has occured.");
            	return false;
            }
        }
        return true;
    }

    public void showCastleSelectPage(L2PcInstance activeChar)
    {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        
        StringBuilder replyMSG = new StringBuilder(500);
        StringUtil.append(replyMSG, "<html><body>");
        StringUtil.append(replyMSG, "<table width=260><tr>");
        StringUtil.append(replyMSG, "<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        StringUtil.append(replyMSG, "<td width=180><center>Siege CS/CH Menu</center></td>");
        StringUtil.append(replyMSG, "<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        StringUtil.append(replyMSG, "</tr></table>");
        StringUtil.append(replyMSG, "<center>");
        StringUtil.append(replyMSG, "<br>Please select<br1>");
        StringUtil.append(replyMSG, "<table width=320><tr>");
        StringUtil.append(replyMSG, "<td>Castles:<br></td><td>ClanHalls:<br></td><td></td></tr><tr>");
        StringUtil.append(replyMSG, "<td>");
	
        for (Castle castle: CastleManager.getInstance().getCastles())
        {
            if (castle != null)
            	StringUtil.append(replyMSG, "<a action=\"bypass -h admin_siege " + castle.getName() + "\">" + castle.getName() + "</a><br1>");
        }
        StringUtil.append(replyMSG, "</td><td>");
        int id = 0;
        for (ClanHall clanhall: ClanHallManager.getInstance().getClanHalls())
        {
        	id++;
        	if (id>15)
        	{
        		StringUtil.append(replyMSG, "</td><td>");
        		id = 0;
        	}
        	if (clanhall != null)
        		StringUtil.append(replyMSG, "<a action=\"bypass -h admin_clanhall " + clanhall.getId() + "\">" + clanhall.getName() + "</a><br1>");
        }
        StringUtil.append(replyMSG, "</td></tr></table>");
        StringUtil.append(replyMSG, "</center>");
        StringUtil.append(replyMSG, "</body></html>");
        
        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }
    
    public void showSiegePage(L2PcInstance activeChar, String castleName)
    {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        
        String replyMSG = StringUtil.concat(
        	"<html><body>",
			"<table width=260><tr>",
			"<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
			"<td width=180><center>Siege Menu</center></td>",
			"<td width=40><button value=\"Back\" action=\"bypass -h admin_siege\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
			"</tr></table>",
        	"<center>",
        	"<br><br><br>Castle: " + castleName + "<br><br>",
        	"<table>",
        	"<tr><td><button value=\"Add Attacker\" action=\"bypass -h admin_add_attacker " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"Add Defender\" action=\"bypass -h admin_add_defender " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"</table>",
        	"<br>",
        	"<table>",
        	"<tr><td><button value=\"List Clans\" action=\"bypass -h admin_list_siege_clans " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"Clear List\" action=\"bypass -h admin_clear_siege_list " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"</table>",
        	"<br>",
        	"<table>",
        	"<tr><td><button value=\"Move Defenders\" action=\"bypass -h admin_move_defenders " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"Spawn Doors\" action=\"bypass -h admin_spawn_doors " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"</table>",
        	"<br>",
        	"<table>",
        	"<tr><td><button value=\"Start Siege\" action=\"bypass -h admin_startsiege " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"End Siege\" action=\"bypass -h admin_endsiege " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"</table>",
        	"<br>",
        	"<table>",
        	"<tr><td><button value=\"Give Castle\" action=\"bypass -h admin_setcastle " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"Take Castle\" action=\"bypass -h admin_castledel " + castleName + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"</table>",
        	"<br>",
        	"<table>",
        	"<tr><td>NpcId: <edit var=\"value\" width=40>",
        	"<td><button value=\"Add Guard\" action=\"bypass -h admin_add_guard " + castleName + " $value\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"</table>",
        	"</center>",
        	"</body></html>");
        
        adminReply.setHtml(replyMSG);
        activeChar.sendPacket(adminReply);
    }

    public void showClanHallPage(L2PcInstance activeChar, ClanHall clanhall)
    {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        
        StringBuilder replyMSG = new StringBuilder(1000);
        StringUtil.append(replyMSG, "<html><body>");
        StringUtil.append(replyMSG, "<table width=260><tr>");
        StringUtil.append(replyMSG, "<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        StringUtil.append(replyMSG, "<td width=180><center>Siege Menu</center></td>");
        StringUtil.append(replyMSG, "<td width=40><button value=\"Back\" action=\"bypass -h admin_siege\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        StringUtil.append(replyMSG, "</tr></table>");
        StringUtil.append(replyMSG, "<center>");
        StringUtil.append(replyMSG, "<br><br><br>ClanHall: " + clanhall.getName() + "<br>");

        L2Clan owner = ClanTable.getInstance().getClan(clanhall.getOwnerId()); 
        if (owner == null)
        	StringUtil.append(replyMSG, "ClanHall Owner: none<br><br>");
        else
        	StringUtil.append(replyMSG, "ClanHall Owner: " + owner.getName() + "<br><br>");

        StringUtil.append(replyMSG, "<br>");
        StringUtil.append(replyMSG, "<br>");
        StringUtil.append(replyMSG, "<table>");
        StringUtil.append(replyMSG, "<tr><td><button value=\"Open Doors\" action=\"bypass -h admin_clanhallopendoors " + clanhall.getId() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        StringUtil.append(replyMSG, "<td><button value=\"Close Doors\" action=\"bypass -h admin_clanhallclosedoors " + clanhall.getId() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
        StringUtil.append(replyMSG, "</table>");
        StringUtil.append(replyMSG, "<br>");
        StringUtil.append(replyMSG, "<table>");
        StringUtil.append(replyMSG, "<tr><td><button value=\"Give ClanHall\" action=\"bypass -h admin_clanhallset " + clanhall.getId() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        StringUtil.append(replyMSG, "<td><button value=\"Take ClanHall\" action=\"bypass -h admin_clanhalldel " + clanhall.getId() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
        StringUtil.append(replyMSG, "</table>");
        StringUtil.append(replyMSG, "<br>");
        StringUtil.append(replyMSG, "<table><tr>");
        StringUtil.append(replyMSG, "<td><button value=\"Teleport self\" action=\"bypass -h admin_clanhallteleportself " + clanhall.getId() + " \" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
        StringUtil.append(replyMSG, "</table>");
        StringUtil.append(replyMSG, "</center>");
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