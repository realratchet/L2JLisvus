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
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2MercManagerInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MerchantInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestSellItem extends L2GameClientPacket
{
	private static final String _C__1E_REQUESTSELLITEM = "[C] 1E RequestSellItem";
	// private static Logger _log = Logger.getLogger(RequestSellItem.class.getName());

	private int _listId;
	private int _count;
	private int[] _items; // count*3

	@Override
	protected void readImpl()
	{
		_listId = readD();
		_count = readD();
		if ((_count <= 0) || ((_count * 12) > _buf.remaining()) || (_count > Config.MAX_ITEM_IN_PACKET))
		{
			_count = 0;
			_items = null;
			return;
		}
		_items = new int[_count * 3];
		for (int i = 0; i < _count; i++)
		{
			int objectId = readD();
			_items[(i * 3) + 0] = objectId;
			int itemId = readD();
			_items[(i * 3) + 1] = itemId;
			int count = readD();
			if (count <= 0)
			{
				_count = 0;
				_items = null;
				return;
			}
			_items[(i * 3) + 2] = count;
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

		if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("sell"))
		{
			player.sendMessage("You are selling too fast.");
			return;
		}

		if (player.isInStoreMode())
		{
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			return;
		}
		
		// Alt game - Karma punishment
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (player.getKarma() > 0))
		{
			return;
		}

		L2Object target = player.getTarget();
		if (!player.isGM() && ((target == null // No target (ie GM Shop)
		) || !((target instanceof L2MerchantInstance) || (target instanceof L2MercManagerInstance)) // Target not a merchant and not mercmanager
			|| !player.isInsideRadius(target, L2NpcInstance.INTERACTION_DISTANCE, false, false)))
		{
			return;
		}
		
		L2MerchantInstance merchant = ((target != null) && (target instanceof L2MerchantInstance)) ? (L2MerchantInstance) target : null;
		if ((merchant != null) && (_listId > 1000000)) // lease
		{
			if (merchant.getTemplate().npcId != (_listId - 1000000))
			{
				sendPacket(new ActionFailed());
				return;
			}
		}
		
		long totalPrice = 0;
		// Proceed the sell
		for (int i = 0; i < _count; i++)
		{
			int objectId = _items[(i * 3) + 0];
			int itemId = _items[(i * 3) + 1];
			int count = _items[(i * 3) + 2];

			if ((count < 0) || (count > L2ItemInstance.getMaxItemCount(itemId)))
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + L2ItemInstance.getMaxItemCount(itemId) + " items at the same time.", Config.DEFAULT_PUNISH);
				SystemMessage sm = new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_QUANTITY_THAT_CAN_BE_INPUTTED);
				sendPacket(sm);
				sm = null;
				return;
			}

			L2ItemInstance item = player.checkItemManipulation(objectId, count, "sell");
			if ((item == null) || (!item.getItem().isSellable()))
			{
				continue;
			}

			if (item.isEquipped())
			{
				continue;
			}

			int oldAdena = player.getAdena();
			if ((oldAdena + (totalPrice + ((item.getReferencePrice() * count) / 2))) > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID))
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_POCKET_MONEY_LIMIT));
				break;
			}
			
			totalPrice += (item.getReferencePrice() * count) / 2;
			if (totalPrice > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID))
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID) + " adena worth of goods.", Config.DEFAULT_PUNISH);
				return;
			}

			item = player.getInventory().destroyItem("Sell", objectId, count, player, null);
		}
		player.addAdena("Sell", (int) totalPrice, merchant, false);

		if (merchant != null)
		{
			String html = HtmCache.getInstance().getHtm("data/html/merchant/" + merchant.getNpcId() + "-sold.htm");
			if (html != null)
			{
				NpcHtmlMessage soldMsg = new NpcHtmlMessage(0);
				soldMsg.setHtml(html.replaceAll("%objectId%", String.valueOf(merchant.getObjectId())));
				player.sendPacket(soldMsg);
			}
		}

		// Update current load as well
		StatusUpdate su = new StatusUpdate(player.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
		player.sendPacket(new ItemList(player, true));
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__1E_REQUESTSELLITEM;
	}
}
