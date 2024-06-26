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

import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

/**
 * This class handles following admin commands:
 * - gmchat text = sends text to all online GM's
 * 
 * @version $Revision: 1.2.4.3 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminGmChat implements IAdminCommandHandler
{
	private static String[] _adminCommands = {"admin_gmchat", "admin_snoop"};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_gmchat"))
			handleGmChat(command, activeChar);
		else if(command.startsWith("admin_snoop"))
			snoop(command, activeChar);
		return true;
	}
	
	/**
	 * @param command
	 * @param activeChar
	 */
	private void snoop(String command, L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
		if(target == null)
		{
			activeChar.sendMessage("You must select a target.");
			return;
		}
		if(!(target instanceof L2PcInstance))
		{
			activeChar.sendMessage("Target must be a player.");
			return;
		}
		L2PcInstance player = (L2PcInstance)target;
		player.addSnooper(activeChar);
		activeChar.addSnooped(player);
		
	}
	
	private void handleGmChat(String command, L2PcInstance activeChar)
	{
		try
		{
			String text = command.substring(13);
			CreatureSay cs = new CreatureSay(0, Say2.ALLIANCE, activeChar.getName(), text);
			GmListTable.getInstance().broadcastToGMs(cs);
		}
		catch (StringIndexOutOfBoundsException e)
		{
			// empty message.. ignore
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}