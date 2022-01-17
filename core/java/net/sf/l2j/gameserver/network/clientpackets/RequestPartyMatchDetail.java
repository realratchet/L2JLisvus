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
import net.sf.l2j.gameserver.network.serverpackets.ExManagePartyRoomMember;
import net.sf.l2j.gameserver.network.serverpackets.ExPartyRoomMember;
import net.sf.l2j.gameserver.network.serverpackets.PartyMatchDetail;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @athor Gnacik
 */
public class RequestPartyMatchDetail extends L2GameClientPacket
{
	private static final String _C__71_REQUESTPARTYMATCHDETAIL = "[C] 71 RequestPartyMatchDetail";
	
	private int _roomId;
	@SuppressWarnings("unused")
	private int _unk1;
	
	@Override
	protected void readImpl()
	{
		_roomId = readD();
		_unk1 = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		PartyMatchRoom _room = PartyMatchRoomList.getInstance().getRoom(_roomId);
		if (_room == null)
		{
			return;
		}
		
		if ((activeChar.getParty() == null) && (activeChar.getLevel() >= _room.getMinLvl()) && (activeChar.getLevel() <= _room.getMaxLvl()) && (_room.getPartyMembers().size() < _room.getMaxMembers()))
		{
			_room.addMember(activeChar);
			activeChar.setPartyRoom(_roomId);
			
			activeChar.sendPacket(new PartyMatchDetail(_room));
			activeChar.sendPacket(new ExPartyRoomMember(activeChar, _room, 0));
			
			for (L2PcInstance _member : _room.getPartyMembers())
			{
				if ((_member == null) || (_member == activeChar))
				{
					continue;
				}
				
				_member.sendPacket(new ExManagePartyRoomMember(activeChar, _room, 0));
			}
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_ENTER_PARTY_ROOM));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__71_REQUESTPARTYMATCHDETAIL;
	}
}