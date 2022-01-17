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

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class handles commands for GMs to respond to petitions.
 * 
 * @author Tempy
 * 
 */
public class AdminPetition implements IAdminCommandHandler
{
	private static String[] _adminCommands =
	{
		"admin_view_petitions",
		"admin_view_petition",
		"admin_accept_petition",
		"admin_reject_petition",
		"admin_reset_petitions",
		"admin_force_peti"
	};

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		int petitionId = -1;
		
		try
		{
			petitionId = Integer.parseInt(command.split(" ")[1]);
		}
		catch (Exception e) {}
		
		if (command.equals("admin_view_petitions"))
		{
			PetitionManager.getInstance().sendPendingPetitionList(activeChar);
		}
		else if (command.startsWith("admin_view_petition"))
		{
			PetitionManager.getInstance().viewPetition(activeChar, petitionId);
		}
		else if (command.startsWith("admin_accept_petition"))
		{
			if (PetitionManager.getInstance().isPlayerInConsultation(activeChar))
			{
				activeChar.sendMessage("You may only deal with one active petition at a time.");
				return true;
			}
			
			if (PetitionManager.getInstance().isPetitionInProcess(petitionId))
			{
				activeChar.sendMessage("The petition has already answered.");
				return true;
			}
			
			if (!PetitionManager.getInstance().acceptPetition(activeChar, petitionId, false))
				activeChar.sendMessage("Invalid petition specified or error occurred.");
		}
		else if (command.startsWith("admin_reject_petition"))
		{
			if (!PetitionManager.getInstance().rejectPetition(activeChar, petitionId))
				activeChar.sendMessage("Invalid petition specified or error occurred.");
		}
		else if (command.equals("admin_reset_petitions"))
		{
			if (PetitionManager.getInstance().isPetitionInProcess())
			{
				activeChar.sendMessage("You cannot clear the petition queue when a petition is being dealt with.");
				return false;
			}
			
			PetitionManager.getInstance().clearPendingPetitions();
		}
		else if (command.startsWith("admin_force_peti"))
		{
			try
			{
				StringTokenizer st = new StringTokenizer(command, " ");
				st.nextToken();
				
				L2PcInstance target = L2World.getInstance().getPlayer(st.nextToken());
				if (target == null)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_NOT_FOUND_IN_THE_GAME));
					return false;
				}
				
				if (target.getObjectId() == activeChar.getObjectId())
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
					return false;
				}
				
				if (PetitionManager.getInstance().isPlayerPetitionPending(target))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.ALREADY_APPLIED_FOR_PETITION));
					return false;
				}
				
				String val = command.substring(15);
				petitionId = PetitionManager.getInstance().submitPetition(target, val, 9);
				PetitionManager.getInstance().acceptPetition(activeChar, petitionId, true);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Usage: //force_peti text");
				return false;
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