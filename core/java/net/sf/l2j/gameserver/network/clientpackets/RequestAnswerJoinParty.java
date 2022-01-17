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
import net.sf.l2j.gameserver.model.PartyMatchRoom;
import net.sf.l2j.gameserver.model.PartyMatchRoomList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.ExClosePartyRoom;
import net.sf.l2j.gameserver.network.serverpackets.ExManagePartyRoomMember;
import net.sf.l2j.gameserver.network.serverpackets.ExPartyRoomMember;
import net.sf.l2j.gameserver.network.serverpackets.JoinParty;
import net.sf.l2j.gameserver.network.serverpackets.PartyMatchDetail;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * sample 2a 01 00 00 00 format cdd
 * @version $Revision: 1.7.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestAnswerJoinParty extends L2GameClientPacket
{
	private static final String _C__2A_REQUESTANSWERPARTY = "[C] 2A RequestAnswerJoinParty";
	// private static Logger _log = Logger.getLogger(RequestAnswerJoinParty.class.getName());
	
	private int _response;
	
	@Override
	protected void readImpl()
	{
		_response = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		L2PcInstance requestor = player.getActiveRequester();
		if (requestor == null)
		{
			return;
		}
		
		if (_response == 1)
		{
			L2Event playerEvent = player.getEvent();
			L2Event requestorEvent = requestor.getEvent();

			// Summary of people already in party and people that get invitation
			if (requestor.isInParty() && !requestor.getParty().isLeader(requestor))
			{
				requestor.sendPacket(new SystemMessage(SystemMessage.ONLY_LEADER_CAN_INVITE));
			}
			else if (requestor.isInParty() && (requestor.getParty().getMemberCount() >= 9))
			{
				requestor.sendPacket(new SystemMessage(SystemMessage.PARTY_FULL));
				player.sendPacket(new SystemMessage(SystemMessage.PARTY_FULL));
			}
			else if (requestor.isInParty() && requestor.getParty().isInDimensionalRift())
			{
				requestor.sendMessage("You cannot invite characters from another dimension.");
			}
			else if (player.isInJail() || requestor.isInJail())
			{
				requestor.sendMessage("Cannot request or accept party invitations while in jail.");
			}
			else if (requestor.inOfflineMode())
			{
				player.sendMessage("Requestor is in Offline mode.");
			}
			else if (player.inObserverMode() || requestor.inObserverMode())
			{
				player.sendMessage("A Party request cannot be done while one of the partners is in Observer mode.");
			}
			else if (player.isInOlympiadMode() || requestor.isInOlympiadMode())
			{
				player.sendMessage("A Party request cannot be done while one of the partners is in Olympiad mode.");
			}
			else if (playerEvent != null && playerEvent.isStarted() && player.getEventTeam() == 0 
				|| player.getEventTeam() > 0 && player.getEventTeam() != requestor.getEventTeam())
			{
				player.sendMessage("Cannot request or accept party invitations while in event.");
			}
			else if (requestorEvent != null && requestorEvent.isStarted() && requestor.getEventTeam() == 0 
				|| requestor.getEventTeam() > 0 && requestor.getEventTeam() != player.getEventTeam())
			{
				requestor.sendMessage("Cannot request or accept party invitations while in event.");
			}
			else if (player.isInParty())
			{
				SystemMessage msg = new SystemMessage(SystemMessage.S1_IS_ALREADY_IN_PARTY);
				msg.addString(player.getName());
				requestor.sendPacket(msg);
				msg = null;
			}
			else
			{
				if (!requestor.isInParty())
				{
					requestor.setParty(new L2Party(requestor));
				}
				
				player.joinParty(requestor.getParty());
				
				// Check everything in detail
				checkPartyMatchingConditions(requestor, player);
			}
		}
		else
		{
			requestor.sendPacket(new SystemMessage(SystemMessage.PLAYER_DECLINED));
		}
		
		requestor.sendPacket(new JoinParty(_response));
		
		requestor.setLootInvitation(-1);
		// just in case somebody manages to invite a requestor
		player.setLootInvitation(-1);
		
		player.setActiveRequester(null);
		requestor.onTransactionResponse();
	}
	
	private void checkPartyMatchingConditions(L2PcInstance requestor, L2PcInstance player)
	{
		if (requestor.isInPartyMatchRoom())
		{
			PartyMatchRoomList list = PartyMatchRoomList.getInstance();
			if (list != null)
			{
				PartyMatchRoom room = list.getPlayerRoom(requestor);
				PartyMatchRoom targetRoom = list.getPlayerRoom(player);
				if (player.isInPartyMatchRoom())
				{
					if (room.getId() != targetRoom.getId())
					{
						requestor.sendPacket(new ExClosePartyRoom());
						room.deleteMember(requestor);
						requestor.setPartyRoom(0);
						requestor.broadcastUserInfo();
						
						player.sendPacket(new ExClosePartyRoom());
						targetRoom.deleteMember(player);
						player.setPartyRoom(0);
					}
					else if (requestor != room.getOwner())
					{
						requestor.sendPacket(new ExClosePartyRoom());
						room.deleteMember(requestor);
						requestor.setPartyRoom(0);
						requestor.broadcastUserInfo();
						
						player.sendPacket(new ExClosePartyRoom());
						room.deleteMember(player);
						player.setPartyRoom(0);
					}
					else
					{
						for (L2PcInstance member : room.getPartyMembers())
						{
							member.sendPacket(new ExManagePartyRoomMember(player, room, 1));
						}
					}
					player.broadcastUserInfo();
				}
				else
				{
					if (requestor != room.getOwner())
					{
						requestor.sendPacket(new ExClosePartyRoom());
						room.deleteMember(requestor);
						requestor.setPartyRoom(0);
						requestor.broadcastUserInfo();
					}
					else
					{
						room.addMember(player);
						player.setPartyRoom(room.getId());
						
						player.sendPacket(new PartyMatchDetail(room));
						player.sendPacket(new ExPartyRoomMember(player, room, 0));
						
						player.broadcastUserInfo();
						
						for (L2PcInstance member : room.getPartyMembers())
						{
							member.sendPacket(new ExManagePartyRoomMember(player, room, 0));
						}
					}
				}
			}
		}
		else
		{
			PartyMatchRoom _room = PartyMatchRoomList.getInstance().getPlayerRoom(player);
			if (_room != null)
			{
				player.sendPacket(new ExClosePartyRoom());
				_room.deleteMember(player);
				player.setPartyRoom(0);
				player.broadcastUserInfo();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__2A_REQUESTANSWERPARTY;
	}
}