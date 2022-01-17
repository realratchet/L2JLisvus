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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.Shutdown;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.util.StringUtil;


/**
 * This class handles following admin commands:
 * - server_shutdown [sec] = shows menu or shuts down server in sec seconds
 * 
 * @version $Revision: 1.5.2.1.2.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminShutdown implements IAdminCommandHandler
{
	private static String[] _adminCommands = {"admin_server_shutdown", "admin_server_restart", "admin_server_abort"};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_server_shutdown"))
		{	
			try
			{
				int val = Integer.parseInt(command.substring(22)); 
				serverShutdown(activeChar, val, false);
			}
			catch (Exception e)
			{				
				// Do nothing
			}
		}
		else if (command.startsWith("admin_server_restart"))
		{	
			try
			{
				int val = Integer.parseInt(command.substring(21)); 
				serverShutdown(activeChar, val, true);
			}
			catch (Exception e)
			{				
				// Do nothing
			}
		}
		else if (command.startsWith("admin_server_abort"))
		{	
			serverAbort(activeChar);
		}
		sendHtmlForm(activeChar);

		return true;
	}
	
	private void sendHtmlForm(L2PcInstance activeChar)
    {
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		
		int t = GameTimeController.getInstance().getGameTime();
		int h = t/60;
		int m = t%60;
		SimpleDateFormat format = new SimpleDateFormat("h:mm a");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, m);
		
		String replyMSG = StringUtil.concat(
			"<html><body>",
			"<table width=260><tr>",
			"<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
			"<td width=180><center>Server Management Menu</center></td>",
			"<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
			"</tr></table>",
			"<br><br>",
			"<table>",
			"<tr><td>Players Online: " + L2World.getInstance().getAllPlayersCount() + "</td></tr>",				
			"<tr><td>Used Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes</td></tr>",
			"<tr><td>Server Rates: " + Config.RATE_XP + "x, " + Config.RATE_SP + "x, " + Config.RATE_DROP_ADENA + "x, " + Config.RATE_DROP_ITEMS + "x, " + Config.RATE_BOSS_DROP_ITEMS + "x</td></tr>",
			"<tr><td>Game Time: " + format.format(cal.getTime()) + "</td></tr>",
			"</table><br>",		
			"<table width=270>",
			"<tr><td>Enter in seconds the time till the server shutdowns bellow:</td></tr>",
			"<br>",
			"<tr><td><center>Seconds till: <edit var=\"shutdown_time\" width=60></center></td></tr>",
			"</table><br>",
			"<center><table><tr><td>",
			"<button value=\"Shutdown\" action=\"bypass -h admin_server_shutdown $shutdown_time\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",			
			"<button value=\"Restart\" action=\"bypass -h admin_server_restart $shutdown_time\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",			
			"<button value=\"Abort\" action=\"bypass -h admin_server_abort\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
			"</td></tr></table></center>",
			"</body></html>");

		adminReply.setHtml(replyMSG);
		activeChar.sendPacket(adminReply);
	}
	
	private void serverShutdown(L2PcInstance activeChar, int seconds, boolean restart)
	{
		Shutdown.getInstance().startShutdown(activeChar, seconds, restart);
	}
	
	private void serverAbort(L2PcInstance activeChar)
	{
		Shutdown.getInstance().abort(activeChar);
	}
	
	@Override
	public String[] getAdminCommandList()
    {
		return _adminCommands;
	}
}