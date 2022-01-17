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
package net.sf.l2j.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.PartyMatchRoom;
import net.sf.l2j.gameserver.model.PartyMatchRoomList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * @version $Revision: 1.1.2.1.2.4 $ $Date: 2005/03/27 15:29:57 $
 */
public class PartyMatchList extends L2GameServerPacket
{
	private static final String _S__AF_PARTYMATCHLIST = "[S] 96 PartyMatchList";
	
	private final L2PcInstance _cha;
	private final int _loc;
	private final int _lim;
	private final List<PartyMatchRoom> _rooms;
	
	/**
	 * @param player 
	 * @param auto 
	 * @param location 
	 * @param limit 
	 */
	public PartyMatchList(L2PcInstance player, int auto, int location, int limit)
	{
		_cha = player;
		_loc = location;
		_lim = limit;
		_rooms = new ArrayList<>();
	}
	
	@Override
	protected final void writeImpl()
	{
		for (PartyMatchRoom room : PartyMatchRoomList.getInstance().getRooms())
		{
			if (room.getMembers() < 1 || room.getOwner() == null || !room.getOwner().isOnline() || room.getOwner().getPartyRoom() != room.getId())
			{
				PartyMatchRoomList.getInstance().deleteRoom(room.getId());
				continue;
			}
			
			if ((_loc > 0) && (_loc != room.getLocation()))
			{
				continue;
			}
			
			if ((_lim == 0) && ((_cha.getLevel() < room.getMinLvl()) || (_cha.getLevel() > room.getMaxLvl())))
			{
				continue;
			}
			
			_rooms.add(room);
		}
		
		int size = _rooms.size();
		
		writeC(0x96);
		
		if (size > 0)
		{
			writeD(1);
		}
		else
		{
			writeD(0);
		}
		
		writeD(size);
		
		for (PartyMatchRoom room : _rooms)
		{
			writeD(room.getId());
			writeS(room.getTitle());
			writeD(room.getLocation());
			writeD(room.getMinLvl());
			writeD(room.getMaxLvl());
			writeD(room.getMembers());
			writeD(room.getMaxMembers());
			writeS(room.getOwner().getName());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__AF_PARTYMATCHLIST;
	}
}