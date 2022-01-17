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
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ExPartyRoomMember;
import net.sf.l2j.gameserver.network.serverpackets.PartyMatchDetail;
import net.sf.l2j.gameserver.network.serverpackets.PartyMatchList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.1.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestPartyMatchConfig extends L2GameClientPacket
{
	private static final String _C__6F_REQUESTPARTYMATCHCONFIG = "[C] 6F RequestPartyMatchConfig";
	
	private int _auto, _loc, _lvl;
	
	@Override
	protected void readImpl()
	{
		_auto = readD();
		_loc = readD(); // Location
		_lvl = readD(); // my level
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!activeChar.isInPartyMatchRoom() && (activeChar.getParty() != null) && (activeChar.getParty().getPartyMembers().get(0) != activeChar))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_VIEW_PARTY_ROOMS));
			activeChar.sendPacket(new ActionFailed());
			return;
		}
		
		if (activeChar.isInPartyMatchRoom())
		{
			// If Player is in Room show him room, not list
			PartyMatchRoomList _list = PartyMatchRoomList.getInstance();
			if (_list == null)
			{
				return;
			}
			
			PartyMatchRoom _room = _list.getPlayerRoom(activeChar);
			if (_room == null)
			{
				return;
			}
			
			activeChar.sendPacket(new PartyMatchDetail(_room));
			
			if (activeChar == _room.getOwner())
			{
				activeChar.sendPacket(new ExPartyRoomMember(activeChar, _room, 1));
			}
			else
			{
				activeChar.sendPacket(new ExPartyRoomMember(activeChar, _room, 2));
			}
			
			activeChar.setPartyRoom(_room.getId());
			activeChar.broadcastUserInfo();
		}
		else
		{
			// Send Room list
			PartyMatchList matchList = new PartyMatchList(activeChar, _auto, _loc, _lvl);
			
			activeChar.sendPacket(matchList);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__6F_REQUESTPARTYMATCHCONFIG;
	}
}