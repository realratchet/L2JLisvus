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

import net.sf.l2j.gameserver.model.PartyMatchRoom;
import net.sf.l2j.gameserver.model.PartyMatchRoomList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExClosePartyRoom;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Gnacik
 */
public class RequestWithdrawPartyRoom extends L2GameClientPacket
{
	private static final String _C__D0_02_REQUESTWITHDRAWPARTYROOM = "[C] D0:02 RequestWithdrawPartyRoom";
	
	private int _roomid;
	@SuppressWarnings("unused")
	private int _unk1;
	
	@Override
	protected void readImpl()
	{
		_roomid = readD();
		_unk1 = readD();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	public void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		PartyMatchRoom _room = PartyMatchRoomList.getInstance().getRoom(_roomid);
		if (_room == null)
		{
			return;
		}
		
		if ((activeChar.isInParty() && _room.getOwner().isInParty()) && (activeChar.getParty().getPartyLeaderOID() == _room.getOwner().getParty().getPartyLeaderOID()))
		{
			return;
		}
		
		_room.deleteMember(activeChar);
		activeChar.setPartyRoom(0);
		
		activeChar.sendPacket(new ExClosePartyRoom());
		activeChar.sendPacket(new SystemMessage(SystemMessage.PARTY_ROOM_EXITED));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_02_REQUESTWITHDRAWPARTYROOM;
	}
}