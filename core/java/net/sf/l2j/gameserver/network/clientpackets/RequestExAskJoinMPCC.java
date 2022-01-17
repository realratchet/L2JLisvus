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

import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExAskJoinMPCC;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Format: (ch) S
 * @author -Wooden-
 */
public class RequestExAskJoinMPCC extends L2GameClientPacket
{
	private static final String _C__D0_0D_REQUESTEXASKJOINMPCC = "[C] D0:0D RequestExAskJoinMPCC";
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		L2Party activeParty = activeChar.getParty();
		if (activeParty == null)
		{
			return;
		}
		
		L2PcInstance player = L2World.getInstance().getPlayer(_name);
		if (player == null)
		{
			return;
		}
		
		boolean canInvite = false;
		if (activeParty.getPartyMembers().get(0).equals(activeChar))
		{
			if (activeParty.isInCommandChannel())
			{
				if (activeParty.getCommandChannel().getChannelLeader().equals(activeChar) && (activeParty.getCommandChannel().getParties().size() < 50))
				{
					canInvite = true;
				}
			}
			else
			{
				if ((activeChar.getClan() != null) && (activeChar.getClan().getLevel() > 4) && (activeChar.getClan().getLeaderId() == activeChar.getObjectId()))
				{
					canInvite = true;
				}
			}
		}
		
		if (canInvite)
		{
			if (player.isInParty())
			{
				if (activeChar.getParty().equals(player.getParty()))
				{
					return;
				}
				
				if (player.getParty().isInCommandChannel())
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_ALREADY_IN_CHANNEL);
					sm.addString(player.getParty().getPartyMembers().get(0).getName());
					activeChar.sendPacket(sm);
					sm = null;
				}
				else
				{
					// ready to open a new CC
					// send request to targets Party's PartyLeader
					askJoinMPCC(activeChar, player);
				}
			}
			else
			{
				activeChar.sendMessage("Target does not belong to a party.");
			}
		}
		else
		{
			if (activeParty.isInCommandChannel())
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.NO_CHANNEL_INVITE_RIGHT));
			}
			else
			{
				activeChar.sendMessage("Only Party leader, a Clan Leader with a clan of level 5 or more, can open a command channel.");
			}
		}
	}
	
	private void askJoinMPCC(L2PcInstance requestor, L2PcInstance target)
	{
		if (!target.isProcessingRequest())
		{
			requestor.onTransactionRequest(target.getParty().getPartyMembers().get(0));
			target.getParty().getPartyMembers().get(0).sendPacket(new ExAskJoinMPCC(requestor.getName()));
			requestor.sendMessage("You have invited a party to the channel.");
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
			sm.addString(target.getName());
			requestor.sendPacket(sm);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_0D_REQUESTEXASKJOINMPCC;
	}
}