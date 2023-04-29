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

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PunishmentLevel;

/**
 * This class handles following admin commands:
 * - admin_banchat = Imposes a chat ban on the specified player/target.
 * - admin_unbanchat = Removes any chat ban on the specified player/target.
 * 
 * Uses:
 * admin_banchat [<player_name>] [<ban_duration>]
 * admin_unbanchat [<player_name>]
 * 
 * If <player_name> is not specified, the current target player is used.
 * 
 * @version $Revision: 1.1.6.3 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminBanChat implements IAdminCommandHandler
{
	//private static Logger _log = Logger.getLogger(AdminBan.class.getName());
	private static String[] _adminCommands = {"admin_banchat", "admin_unbanchat"};

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		String[] cmdParams = command.split(" ");
		int delay = 0;
		
		L2Object targetObject = null;
		L2PcInstance targetPlayer = null;
		
		if (cmdParams.length > 1) 
        {
			targetPlayer = L2World.getInstance().getPlayer(cmdParams[1]);
            
            if (cmdParams.length > 2) 
            {
                try
                {
                    delay = Integer.parseInt(cmdParams[2]) * 60000;
                } catch (NumberFormatException nfe) {}
            }
		}
		else 
		{
			if (activeChar.getTarget() != null)
			{
				targetObject = activeChar.getTarget();
		
				if (targetObject != null && targetObject instanceof L2PcInstance)
					targetPlayer = (L2PcInstance)targetObject;
			}
		}

		if (targetPlayer == null)
		{
			activeChar.sendMessage("Incorrect parameter or target.");
			return false;
		}

		if (command.startsWith("admin_banchat"))
		{
			if (PunishmentLevel.CHAT.getSeverity() < targetPlayer.getPunishmentLevel().getSeverity())
			{
				activeChar.sendMessage("Character " + targetPlayer.getName() + " is currently undergoing a more severe punishment: " + targetPlayer.getPunishmentLevel().getDescription());
			}
			else
			{
				targetPlayer.setPunishment(PunishmentLevel.CHAT, delay);
				activeChar.sendMessage("Character " + targetPlayer.getName() + " was chat-banned for " + (delay > 0 ? delay + " minute(s)." : "ever!"));
			}
		}
		else if (command.startsWith("admin_unbanchat"))
		{
			if (targetPlayer.isChatBanned())
			{
				targetPlayer.setPunishment(PunishmentLevel.NONE, 0);
				activeChar.sendMessage("Chat-ban for player " + targetPlayer.getName() + " has now been lifted.");
			}
			else
			{
				activeChar.sendMessage("Character " + targetPlayer.getName() + " is not chat-banned.");
			}
		}
		
		GMAudit.auditGMAction(activeChar.getName(), command,  targetPlayer.getName(), "");
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}