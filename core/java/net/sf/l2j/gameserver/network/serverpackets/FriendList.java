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

import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * Support for "Chat with Friends" dialog. Format: ch (hdSdh) h: Total Friend Count h: Unknown d: Player Object ID S: Friend Name d: Online/Offline h: Unknown
 * @author Tempy
 */
public class FriendList extends L2GameServerPacket
{
	private static final String _S__FA_FRIENDLIST = "[S] FA FriendList";
	
	private final List<FriendInfo> _info;
	
	private static class FriendInfo
	{
		private int _objectId;
		private String _name;
		private boolean _isOnline;
		
		public FriendInfo(int objectId, String name, boolean isOnline)
		{
			_objectId = objectId;
			_name = name;
			_isOnline = isOnline;
		}
		
		public final int getObject()
		{
			return _objectId;
		}
		
		public final String getName()
		{
			return _name;
		}
		
		public final boolean isOnline()
		{
			return _isOnline;
		}
	}
	
	public FriendList(L2PcInstance player)
	{
		_info = new ArrayList<>(player.getFriendList().size());
		for (int objId : player.getFriendList())
		{
			String name = CharNameTable.getInstance().getNameById(objId);
			L2PcInstance friend = L2World.getInstance().getPlayer(objId);
			_info.add(new FriendInfo(objId, name, friend != null && friend.isOnline()));
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfa);
		
		// Do not write friends size if player has no friends
		int size = _info.size();
		if (size > 0)
		{
			writeH(_info.size());
			
			for (FriendInfo info : _info)
			{
				writeH(0); // ??
				writeD(info.getObject()); // character id
				writeS(info.getName());
				writeD(info.isOnline() ? 0x01 : 0x00); // online
				writeH(0); // ??
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FA_FRIENDLIST;
	}
}