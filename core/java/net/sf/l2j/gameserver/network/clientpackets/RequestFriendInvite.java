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

import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.AskJoinFriend;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestFriendInvite extends L2GameClientPacket
{
	private static final String _C__5E_REQUESTFRIENDINVITE = "[C] 5E RequestFriendInvite";
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(RequestFriendInvite.class.getName());
	
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		SystemMessage sm;
		
		L2PcInstance friend = L2World.getInstance().getPlayer(_name);
		
		if (friend == null || !friend.isOnline() || friend.getAppearance().getInvisible())
		{
			// Target is not found in the game.
			activeChar.sendPacket(new SystemMessage(SystemMessage.THE_USER_YOU_REQUESTED_IS_NOT_IN_GAME));
			return;
		}
		
		if (friend == activeChar)
		{
			// You cannot add yourself to your own friend list.
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_ADD_YOURSELF_TO_OWN_FRIEND_LIST));
			return;
		}
		
		if (BlockList.isBlocked(activeChar, friend))
		{
			activeChar.sendMessage("You have blocked " + _name + ".");
			return;
		}
		
		if (BlockList.isBlocked(friend, activeChar))
		{
			activeChar.sendMessage("You are in target's block list.");
			return;
		}
		
		if (activeChar.getFriendList().contains(friend.getObjectId()))
		{
			// Player already is in your friends list
			sm = new SystemMessage(SystemMessage.S1_ALREADY_IN_FRIENDS_LIST);
			sm.addString(_name);
			activeChar.sendPacket(sm);
			return;
		}
		
		if (!friend.isProcessingRequest())
		{
			// request to become friend
			activeChar.onTransactionRequest(friend);
			sm = new SystemMessage(SystemMessage.S1_REQUESTED_TO_BECOME_FRIENDS);
			sm.addString(activeChar.getName());
			friend.sendPacket(sm);
			friend.sendPacket(new AskJoinFriend(activeChar.getName()));
		}
		else
		{
			sm = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
			sm.addString(_name);
			activeChar.sendPacket(sm);
		}
	}
	
	@Override
	public String getType()
	{
		return _C__5E_REQUESTFRIENDINVITE;
	}
}