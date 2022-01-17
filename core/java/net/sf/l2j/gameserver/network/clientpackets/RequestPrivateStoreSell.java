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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.ItemRequest;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;

/**
 * This class ...
 * @version $Revision: 1.2.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestPrivateStoreSell extends L2GameClientPacket
{
	private static final String _C__96_REQUESTPRIVATESTORESELL = "[C] 96 RequestPrivateStoreSell";
	private static Logger _log = Logger.getLogger(RequestPrivateStoreSell.class.getName());
	
	private int _storePlayerId;
	private int _count;
	private ItemRequest[] _items;
	
	@Override
	protected void readImpl()
	{
		_storePlayerId = readD();
		_count = readD();
		if (_count < 0 || (_count * 20) > _buf.remaining() || _count > Config.MAX_ITEM_IN_PACKET)
		{
			_count = 0;
		}
		_items = new ItemRequest[_count];
		
		for (int i = 0; i < _count; i++)
		{
			int objectId = readD();
			int itemId = readD();
			int enchantLevel = readH();
			readH();
			int count = readD();
			int price = readD();
			
			if (objectId < 1 || itemId < 1 || count < 0 || price < 0)
			{
				_count = 0;
				_items = null;
				return;
			}
			_items[i] = new ItemRequest(objectId, itemId, enchantLevel, count, price);
		}
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("privatestoresell"))
		{
			player.sendMessage("You are selling items too fast.");
			return;
		}
		
		L2PcInstance storePlayer = L2World.getInstance().getPlayer(_storePlayerId);
		if (storePlayer == null)
		{
			return;
		}
		
		if (storePlayer.getPrivateStoreType() != PrivateStoreType.BUY)
		{
			return;
		}
		
		TradeList storeList = storePlayer.getBuyList();
		if (storeList == null)
		{
			return;
		}
		
		if (player.getAccessLevel() > 0 && player.getAccessLevel() < Config.GM_TRANSACTION)
		{
			player.sendMessage("Transactions are disabled for your Access Level.");
			sendPacket(new ActionFailed());
			return;
		}
		
		if (!storeList.privateStoreSell(player, _items))
		{
			sendPacket(new ActionFailed());
			_log.warning("PrivateStore sell has failed due to invalid list or request. Player: " + player.getName() + ", Private store of: " + storePlayer.getName());
			return;
		}
		
		if (storeList.getItemCount() == 0)
		{
			storePlayer.setPrivateStoreType(PrivateStoreType.NONE);
			storePlayer.broadcastUserInfo();
		}
	}
	
	@Override
	public String getType()
	{
		return _C__96_REQUESTPRIVATESTORESELL;
	}
}