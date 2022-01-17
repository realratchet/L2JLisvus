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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.FriendList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestFriendDel extends L2GameClientPacket
{
	
	private static final String _C__61_REQUESTFRIENDDEL = "[C] 61 RequestFriendDel";
	private static Logger _log = Logger.getLogger(RequestFriendDel.class.getName());
	
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	@Override
	public void runImpl()
	{
		SystemMessage sm;
		
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		int id = CharNameTable.getInstance().getIdByName(_name);
		if (id == -1)
		{
			sm = new SystemMessage(SystemMessage.S1_NOT_ON_YOUR_FRIENDS_LIST);
			sm.addString(_name);
			activeChar.sendPacket(sm);
			return;
		}
		
		if (!activeChar.getFriendList().contains(id))
		{
			sm = new SystemMessage(SystemMessage.S1_NOT_ON_YOUR_FRIENDS_LIST);
			sm.addString(_name);
			activeChar.sendPacket(sm);
			return;
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM character_friends WHERE (char_id=? AND friend_id=?) OR (char_id=? AND friend_id=?)"))
		{
			statement.setInt(1, activeChar.getObjectId());
			statement.setInt(2, id);
			statement.setInt(3, id);
			statement.setInt(4, activeChar.getObjectId());
			statement.execute();
			
			// Player deleted from your friends list
			sm = new SystemMessage(SystemMessage.S1_HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST);
			sm.addString(_name);
			activeChar.sendPacket(sm);
			
			activeChar.getFriendList().remove(Integer.valueOf(id));
			activeChar.sendPacket(new FriendList(activeChar));
			
			L2PcInstance player = L2World.getInstance().getPlayer(_name);
			if (player != null)
			{
				player.getFriendList().remove(Integer.valueOf(activeChar.getObjectId()));
				player.sendPacket(new FriendList(player));
			}
		}
		catch (Exception e)
		{
			_log.warning("Could not delete friend object ID: " + e);
		}
	}
	
	@Override
	public String getType()
	{
		return _C__61_REQUESTFRIENDDEL;
	}
}