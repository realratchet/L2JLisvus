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
package net.sf.l2j.gameserver.instancemanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.model.PartyMatchRoom;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Gnacik
 */
public class PartyMatchRoomManager
{
	private int _maxId;
	private final Map<Integer, PartyMatchRoom> _rooms = new ConcurrentHashMap<>();
	
	public static PartyMatchRoomManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void addPartyMatchRoom(PartyMatchRoom room)
	{
		_rooms.put(room.getId(), room);
	}
	
	public void deleteRoom(int id)
	{
		final PartyMatchRoom room = _rooms.remove(id);
		if (room != null)
		{
			room.disband();
		}
	}
	
	public PartyMatchRoom getRoom(int id)
	{
		return _rooms.get(id);
	}
	
	public PartyMatchRoom[] getRooms()
	{
		return _rooms.values().toArray(new PartyMatchRoom[_rooms.size()]);
	}
	
	public int getPartyMatchRoomCount()
	{
		return _rooms.size();
	}
	
	public int getAutoIncrementId()
	{
		// reset all ids as free
		// if room list is empty
		if (_rooms.isEmpty())
			_maxId = 0;
		
		return ++_maxId;
	}
	
	public PartyMatchRoom getPlayerRoom(L2PcInstance player)
	{
		for (PartyMatchRoom _room : _rooms.values())
		{
			for (L2PcInstance member : _room.getPartyMembers())
			{
				if (member.equals(player))
					return _room;
			}
		}
		return null;
	}
	
	public int getPlayerRoomId(L2PcInstance player)
	{
		for (PartyMatchRoom _room : _rooms.values())
		{
			for (L2PcInstance member : _room.getPartyMembers())
			{
				if (member.equals(player))
					return _room.getId();
			}
		}
		return -1;
	}
	
	private static class SingletonHolder
	{
		protected static final PartyMatchRoomManager _instance = new PartyMatchRoomManager();
	}
}