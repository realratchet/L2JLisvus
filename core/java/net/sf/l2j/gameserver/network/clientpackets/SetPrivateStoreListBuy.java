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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.PrivateStoreManageListBuy;
import net.sf.l2j.gameserver.network.serverpackets.PrivateStoreMsgBuy;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.2.2.1.2.5 $ $Date: 2005/03/27 15:29:30 $
 */
public class SetPrivateStoreListBuy extends L2GameClientPacket
{
	private static final String _C__91_SETPRIVATESTORELISTBUY = "[C] 91 SetPrivateStoreListBuy";

	// private static Logger _log = Logger.getLogger(SetPrivateStoreListBuy.class.getName());

	private int _count;
	private int[] _items; // count * 4

	@Override
	protected void readImpl()
	{
		_count = readD();
		if ((_count <= 0) || ((_count * 12) > _buf.remaining()) || (_count > Config.MAX_ITEM_IN_PACKET))
		{
			_count = 0;
			_items = null;
			return;
		}
		_items = new int[_count * 4];
		for (int x = 0; x < _count; x++)
		{
			_items[(x * 4) + 0] = readD(); // itemId
			_items[(x * 4) + 1] = readH(); // Enchantment
			
			readH();
			
			int count = readD();
			if (count < 0)
			{
				_count = 0;
				_items = null;
				return;
			}
			_items[(x * 4) + 2] = count;
			_items[(x * 4) + 3] = readD(); // price
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

		if (player.isAttackingDisabled() || player.isOutOfControl() || player.isImmobilized() || player.isCastingNow())
		{
			player.sendPacket(new PrivateStoreManageListBuy(player));
			player.sendPacket(new ActionFailed());
			return;
		}

		if (player.getAccessLevel() > 0 && player.getAccessLevel() < Config.GM_TRANSACTION)
		{
			player.sendPacket(new PrivateStoreManageListBuy(player));
			player.sendMessage("Transactions are disabled for your Access Level.");
			player.sendPacket(new ActionFailed());
			return;
		}

		if (player.isInsideZone(L2Character.ZONE_NO_STORE))
		{
			player.sendPacket(new PrivateStoreManageListBuy(player));
			player.sendPacket(new SystemMessage(SystemMessage.NO_PRIVATE_STORE_HERE));
			player.sendPacket(new ActionFailed());
			return;
		}

		if (_count <= 0)
		{
			player.setPrivateStoreType(PrivateStoreType.NONE);
			player.broadcastUserInfo();
			return;
		}

		TradeList tradeList = player.getBuyList();
		tradeList.clear();

		long totalCost = 0;
		for (int i = 0; i < _count; i++)
		{
			int itemId = _items[(i * 4) + 0];
			int enchantLevel = _items[(i * 4) + 1];
			int count = _items[(i * 4) + 2];
			int price = _items[(i * 4) + 3];

			if (tradeList.addUniqueItem(itemId, enchantLevel, count, price) == null)
			{
				player.sendPacket(new PrivateStoreManageListBuy(player));
				return;
			}
			totalCost += (long)count * price;
		}

		// Check maximum number of allowed slots for private stores
		if (_count > player.getPrivateBuyStoreLimit())
		{
			player.sendPacket(new PrivateStoreManageListBuy(player));
			player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_QUANTITY_THAT_CAN_BE_INPUTTED));
			return;
		}

		// Check for available funds
		if (totalCost > player.getAdena() || totalCost <= 0)
		{
			player.sendPacket(new PrivateStoreManageListBuy(player));
			player.sendPacket(new SystemMessage(SystemMessage.THE_PURCHASE_PRICE_IS_HIGHER_THAN_MONEY));
			return;
		}

		player.sitDown();
		player.setPrivateStoreType(PrivateStoreType.BUY);
		player.broadcastUserInfo();
		player.broadcastPacket(new PrivateStoreMsgBuy(player));
	}

	@Override
	public String getType()
	{
		return _C__91_SETPRIVATESTORELISTBUY;
	}
}
