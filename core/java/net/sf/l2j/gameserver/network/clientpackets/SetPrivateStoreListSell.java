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
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.PrivateStoreManageListSell;
import net.sf.l2j.gameserver.network.serverpackets.PrivateStoreMsgSell;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.2.2.1.2.5 $ $Date: 2005/03/27 15:29:30 $
 */
public class SetPrivateStoreListSell extends L2GameClientPacket
{
	private static final String _C__74_SETPRIVATESTORELISTSELL = "[C] 74 SetPrivateStoreListSell";
	// private static Logger _log = Logger.getLogger(SetPrivateStoreListSell.class.getName());

	private int _count;
	private boolean _packageSale;
	private int[] _items; // count * 3

	@Override
	protected void readImpl()
	{
		_packageSale = (readD() == 1);
		_count = readD();
		if ((_count <= 0) || ((_count * 12) > _buf.remaining()) || (_count > Config.MAX_ITEM_IN_PACKET))
		{
			_count = 0;
			_items = null;
			return;
		}

		_items = new int[_count * 3];
		for (int x = 0; x < _count; x++)
		{
			_items[(x * 3) + 0] = readD(); // objectId
			int count = readD();
			if (count < 0)
			{
				_count = 0;
				_items = null;
				return;
			}
			_items[(x * 3) + 1] = count;
			_items[(x * 3) + 2] = readD(); // price
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
			player.sendPacket(new PrivateStoreManageListSell(player));
			player.sendPacket(new ActionFailed());
			return;
		}

		if (player.getAccessLevel() > 0 && player.getAccessLevel() < Config.GM_TRANSACTION)
		{
			player.sendPacket(new PrivateStoreManageListSell(player));
			player.sendMessage("Transactions are disabled for your Access Level.");
			player.sendPacket(new ActionFailed());
			return;
		}

		if (player.isInsideZone(L2Character.ZONE_NO_STORE))
		{
			player.sendPacket(new PrivateStoreManageListSell(player));
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

		TradeList tradeList = player.getSellList();
		tradeList.clear();
		tradeList.setPackaged(_packageSale);

		long totalCost = 0;
		for (int i = 0; i < _count; i++)
		{
			int objectId = _items[(i * 3) + 0];
			int count = _items[(i * 3) + 1];
			int price = _items[(i * 3) + 2];

			if (tradeList.addItem(objectId, count, price) == null)
			{
				player.sendPacket(new PrivateStoreManageListSell(player));
				return;
			}
			
			totalCost += (long)count * price;
		}

		// Check maximum number of allowed slots for private stores
		if (_count > player.getPrivateSellStoreLimit())
		{
			player.sendPacket(new PrivateStoreManageListSell(player));
			player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_QUANTITY_THAT_CAN_BE_INPUTTED));
			return;
		}
		
		// Check if player can carry more adena
		if ((totalCost + player.getAdena()) > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID))
		{
			player.sendPacket(new PrivateStoreManageListSell(player));
			player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_POCKET_MONEY_LIMIT));
			return;
		}

		player.sitDown();

		if (_packageSale)
		{
			player.setPrivateStoreType(PrivateStoreType.PACKAGE_SELL);
		}
		else
		{
			player.setPrivateStoreType(PrivateStoreType.SELL);
		}

		player.broadcastUserInfo();
		player.broadcastPacket(new PrivateStoreMsgSell(player));
	}

	@Override
	public String getType()
	{
		return _C__74_SETPRIVATESTORELISTSELL;
	}
}