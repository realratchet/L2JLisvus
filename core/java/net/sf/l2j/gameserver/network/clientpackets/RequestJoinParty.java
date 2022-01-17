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

import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.AskJoinParty;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * sample 29 42 00 00 10 01 00 00 00 format cdd
 * @version $Revision: 1.7.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestJoinParty extends L2GameClientPacket
{
	private static final String _C__29_REQUESTJOINPARTY = "[C] 29 RequestJoinParty";
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(RequestJoinParty.class.getName());
	
	private String _name;
	private int _itemDistribution;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
		_itemDistribution = readD();
		if (_itemDistribution < 0)
		{
			_itemDistribution = 0;
		}
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance requestor = getClient().getActiveChar();
		L2PcInstance target = L2World.getInstance().getPlayer(_name);
		
		if (requestor == null)
		{
			return;
		}
		
		if (target == null)
		{
			requestor.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
			return;
		}
		
		if (target.getAppearance().getInvisible())
		{
			requestor.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
			return;
		}
		
		SystemMessage msg;
		
		if (target.isInParty())
		{
			msg = new SystemMessage(SystemMessage.S1_IS_ALREADY_IN_PARTY);
			msg.addString(target.getName());
			requestor.sendPacket(msg);
			msg = null;
			return;
		}
		
		if (target == requestor)
		{
			requestor.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
			return;
		}
		
		L2Event targetEvent = target.getEvent();
		L2Event requestorEvent = requestor.getEvent();
		
		if (targetEvent != null && targetEvent.isStarted() && target.getEventTeam() == 0 || target.getEventTeam() > 0 && target.getEventTeam() != requestor.getEventTeam())
		{
			return;
		}
		
		if (requestorEvent != null && requestorEvent.isStarted() && requestor.getEventTeam() == 0 || requestor.getEventTeam() > 0 && requestor.getEventTeam() != target.getEventTeam())
		{
			requestor.sendMessage("Player in Event.");
			return;
		}
		
		if ((requestor.getLootInvitation() >= 0) || (requestor.getActiveRequester() != null))
		{
			requestor.sendPacket(new SystemMessage(SystemMessage.WAITING_FOR_REPLY));
			return;
		}
		
		if (target.getLootInvitation() >= 0)
		{
			msg = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
			msg.addString(target.getName());
			requestor.sendPacket(msg);
			msg = null;
			return;
		}
		
		if (target.isInJail() || requestor.isInJail())
		{
			requestor.sendMessage("Player is jailed.");
			return;
		}
		
		if (target.inOfflineMode())
		{
			requestor.sendMessage("Player is in Offline mode.");
			return;
		}
		
		if (target.inObserverMode() || requestor.inObserverMode())
		{
			return;
		}
		
		if (target.isInOlympiadMode() || requestor.isInOlympiadMode())
		{
			return;
		}
		
		if (!requestor.isInParty())
		{
			// asker has no party
			createNewParty(target, requestor);
		}
		else
		{
			// asker has a party
			addTargetToParty(target, requestor);
		}
	}
	
	/**
	 * @param target
	 * @param requestor
	 */
	private void addTargetToParty(L2PcInstance target, L2PcInstance requestor)
	{
		SystemMessage msg;
		
		// summary of ppl already in party and ppl that get invitation
		if (!requestor.getParty().isLeader(requestor))
		{
			requestor.sendPacket(new SystemMessage(SystemMessage.ONLY_LEADER_CAN_INVITE));
			return;
		}
		
		if (requestor.getParty().isInDimensionalRift())
		{
			requestor.sendMessage("You can't invite a player when in Dimensional Rift.");
			return;
		}
		
		if (requestor.getParty().getMemberCount() >= 9)
		{
			requestor.sendPacket(new SystemMessage(SystemMessage.PARTY_FULL));
			return;
		}
		
		if (!target.isProcessingRequest())
		{
			requestor.setLootInvitation(requestor.getParty().getLootDistribution());
			requestor.onTransactionRequest(target);
			target.sendPacket(new AskJoinParty(requestor));
			
			msg = new SystemMessage(SystemMessage.YOU_INVITED_S1_TO_PARTY);
			msg.addString(target.getName());
			requestor.sendPacket(msg);
		}
		else
		{
			msg = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
			msg.addString(target.getName());
			requestor.sendPacket(msg);
		}
		msg = null;
	}
	
	/**
	 * @param target
	 * @param requestor
	 */
	private void createNewParty(L2PcInstance target, L2PcInstance requestor)
	{
		SystemMessage msg;
		
		if (!target.isProcessingRequest())
		{
			if (requestor.getLootInvitation() < 0)
			{
				requestor.setLootInvitation(_itemDistribution);
			}
			requestor.onTransactionRequest(target);
			target.sendPacket(new AskJoinParty(requestor));
			
			msg = new SystemMessage(SystemMessage.YOU_INVITED_S1_TO_PARTY);
			msg.addString(target.getName());
			requestor.sendPacket(msg);
		}
		else
		{
			msg = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
			msg.addString(target.getName());
			requestor.sendPacket(msg);
		}
		msg = null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__29_REQUESTJOINPARTY;
	}
}