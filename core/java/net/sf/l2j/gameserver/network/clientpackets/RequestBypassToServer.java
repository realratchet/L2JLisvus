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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.communitybbs.CommunityBoard;
import net.sf.l2j.gameserver.datatables.AdminCommandRightsData;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2CastleChamberlainInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2ManorManagerInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * This class ...
 * @version $Revision: 1.12.4.5 $ $Date: 2005/04/11 10:06:11 $
 */
public class RequestBypassToServer extends L2GameClientPacket
{
	private static final String _C__21_REQUESTBYPASSTOSERVER = "[C] 21 RequestBypassToServer";
	private static Logger _log = Logger.getLogger(RequestBypassToServer.class.getName());
	
	// S
	private String _command;
	
	@Override
	protected void readImpl()
	{
		_command = readS();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!getClient().getFloodProtectors().getServerBypass().tryPerformAction(_command))
		{
			return;
		}
		
		try
		{
			if (_command.startsWith("admin_"))
			{
				String command = _command.split(" ")[0];
				
				IAdminCommandHandler ach = AdminCommandHandler.getInstance().getAdminCommandHandler(command);
				if (ach == null)
				{
					if (activeChar.isGM())
					{
						activeChar.sendMessage("The command '" + command + "' does not exist!");
					}

					_log.warning("No handler registered for admin command '" + command + "'");
					return;
				}
				
				if (!AdminCommandRightsData.getInstance().checkAccess(activeChar, _command))
				{
					activeChar.sendMessage("You don't have the access rights to use this command!");
					_log.warning("Character " + activeChar.getName() + " tried to use admin command '" + command + "', but has no access to it!");
					return;
				}

				ach.useAdminCommand(_command, activeChar);
			}
			else if (_command.startsWith("voice "))
			{
				String text = _command.substring(5);
				StringTokenizer st = new StringTokenizer(text);
				
				String target = "";
				
				String command = st.nextToken().substring(1);
				if (st.countTokens() > 1)
				{
					target = text.substring(command.length() + 2);
				}
				
				IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(command);
				if (vch == null)
				{
					_log.warning("No handler registered for voiced command '" + command + "'");
					return;
				}
					
				vch.useVoicedCommand(command, activeChar, target);
			}
			else if (_command.equals("come_here") && activeChar.isGM())
			{
				comeHere(activeChar);
			}
			else if (_command.startsWith("player_help "))
			{
				playerHelp(activeChar, _command.substring(12));
			}
			else if (_command.startsWith("npc_"))
			{
				if (!activeChar.validateBypass(_command))
				{
					return;
				}
				
				int endOfId = _command.indexOf('_', 5);
				String id;
				if (endOfId > 0)
				{
					id = _command.substring(4, endOfId);
				}
				else
				{
					id = _command.substring(4);
				}
				
				try
				{
					L2Object object = L2World.getInstance().findObject(Integer.parseInt(id));
					if (object != null && object instanceof L2NpcInstance && endOfId > 0 && ((L2NpcInstance)object).canInteract(activeChar))
					{
						((L2NpcInstance) object).onBypassFeedback(activeChar, _command.substring(endOfId + 1));
					}
					
					activeChar.sendPacket(new ActionFailed());
				}
				catch (NumberFormatException nfe)
				{
				}
			}
			else if (_command.equals("menu_select?ask=-16&reply=1")) // Draw a Symbol
			{
				L2Object object = activeChar.getTarget();
				if (object instanceof L2NpcInstance)
				{
					((L2NpcInstance) object).onBypassFeedback(activeChar, _command);
				}
				
			}
			else if (_command.equals("menu_select?ask=-16&reply=2"))
			{
				L2Object object = activeChar.getTarget();
				if (object instanceof L2NpcInstance)
				{
					((L2NpcInstance) object).onBypassFeedback(activeChar, _command);
				}
				
			}
			else if (_command.startsWith("manor_menu_select?"))
			{
				L2NpcInstance target = activeChar.getLastFolkNPC();
				if ((target instanceof L2ManorManagerInstance || target instanceof L2CastleChamberlainInstance) && target.canInteract(activeChar))
				{
					target.onBypassFeedback(activeChar, _command);
				}
			}
			else if (_command.startsWith("bbs_"))
			{
				CommunityBoard.getInstance().handleCommands(getClient(), _command);
			}
			else if (_command.startsWith("_bbs"))
			{
				CommunityBoard.getInstance().handleCommands(getClient(), _command);
			}
			else if (_command.startsWith("Quest "))
			{
				if (!activeChar.validateBypass(_command))
				{
					return;
				}
				
				L2PcInstance player = getClient().getActiveChar();
				String p = _command.substring(6).trim();
				int idx = p.indexOf(' ');
				if (idx < 0)
				{
					player.processQuestEvent(p, "");
				}
				else
				{
					player.processQuestEvent(p.substring(0, idx), p.substring(idx).trim());
				}
			}
			else if (_command.startsWith("OlympiadArenaChange"))
			{
				Olympiad.getInstance().bypassChangeArena(_command, activeChar);
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Bad RequestBypassToServer: ", e);
		}
	}
	
	/**
	 * @param activeChar
	 */
	private void comeHere(L2PcInstance activeChar)
	{
		L2Object obj = activeChar.getTarget();
		if (obj instanceof L2NpcInstance)
		{
			L2NpcInstance temp = (L2NpcInstance) obj;
			temp.setTarget(activeChar);
			temp.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(activeChar.getX(), activeChar.getY(), activeChar.getZ()));
		}
	}
	
	private void playerHelp(L2PcInstance activeChar, String path)
	{
		if (path.indexOf("..") != -1)
		{
			return;
		}
		
		String filename = "data/html/help/" + path;
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		html.setFile(filename);
		activeChar.sendPacket(html);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__21_REQUESTBYPASSTOSERVER;
	}
}