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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.TradeController;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2MercManagerInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MerchantInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ShopPreviewInfo;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.12.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestPreviewItem extends L2GameClientPacket
{
	private static final String _C__C6_REQUESTPREVIEWITEM = "[C] C6 RequestPreviewItem";
	protected static Logger _log = Logger.getLogger(RequestPreviewItem.class.getName());

	@SuppressWarnings("unused")
	private int _unknown;

	/** List of ItemID to Wear */
	private int _listId;

	/** Number of Item to Wear */
	private int _count;

	/** Table of ItemId containing all Item to Wear */
	private int[] _items;

	@Override
	protected void readImpl()
	{
		_unknown = readD();
		_listId = readD(); // List of ItemID to Wear
		_count = readD(); // Number of Item to Wear

		if (_count < 0)
		{
			_count = 0;
		}
		else if (_count > 100)
		{
			return; // prevent too long lists
		}

		// Create _items table that will contain all ItemID to Wear
		_items = new int[_count];

		// Fill _items table with all ItemID to Wear
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
		}
	}

	/**
	 * Launch Wear action.<BR>
	 * <BR>
	 */
	@Override
	public void runImpl()
	{
		if (_count < 1 || _listId >= 1000000)
		{
			sendPacket(new ActionFailed());
			return;
		}

		// Get the current player and return if null
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}

		if (player.isInStoreMode())
		{
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			player.sendPacket(new ActionFailed());
			return;
		}

		if (player.isWearingFormalWear())
		{
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_USE_ITEMS_SKILLS_WITH_FORMALWEAR));
			return;
		}

		if (player.isMounted())
		{
			return;
		}

		if (player.isAllActionsDisabled() || player.isAttackingNow() || player.isCastingNow() || player.isAlikeDead())
		{
			return;
		}

		// If Alternate rule Karma punishment is set to true, forbid Wear to player with Karma
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (player.getKarma() > 0))
		{
			return;
		}

		// Check current target of the player and the INTERACTION_DISTANCE
		L2Object target = player.getTarget();
		if (!player.isGM() && ((target == null // No target (ie GM Shop)
		) || !((target instanceof L2MerchantInstance) || (target instanceof L2MercManagerInstance)) // Target not a merchant and not mercmanager
			|| !player.isInsideRadius(target, L2NpcInstance.INTERACTION_DISTANCE, false, false)))
		{
			return;
		}

		// Get the current merchant targeted by the player
		L2MerchantInstance merchant = ((target != null) && (target instanceof L2MerchantInstance)) ? (L2MerchantInstance) target : null;
		if (merchant == null)
		{
			return;
		}

		List<L2TradeList> lists = TradeController.getInstance().getBuyListByNpcId(merchant.getNpcId());
		if (lists == null)
		{
			Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id.", Config.DEFAULT_PUNISH);
			return;
		}

		L2TradeList list = null;
		for (L2TradeList tradeList : lists)
		{
			if (tradeList.getListId() == _listId)
			{
				list = tradeList;
				break;
			}
		}

		if (list == null)
		{
			Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id.", Config.DEFAULT_PUNISH);
			return;
		}

		// Check for buylist validity and calculates summary values
		long totalPrice = 0;

		Map<Integer, Integer> itemList = new HashMap<>();
		for (int i = 0; i < _count; i++)
		{
			int itemId = _items[i];
			if (!list.containsItemId(itemId))
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id.", Config.DEFAULT_PUNISH);
				return;
			}

			L2Item template = ItemTable.getInstance().getTemplate(itemId);
			int slot = Inventory.getPaperdollIndex(template.getBodyPart());
			if (slot < 0) {
				continue;
			}

			if (itemList.containsKey(slot) || slot == Inventory.PAPERDOLL_RHAND && itemList.containsKey(slot + Inventory.PAPERDOLL_LHAND))
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_CAN_NOT_TRY_THOSE_ITEMS_ON_AT_THE_SAME_TIME));
				return;
			}

			totalPrice += Config.WEAR_PRICE;
			if (totalPrice > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID))
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to "
					+ "purchase over " + L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID) + " adena worth of goods.", Config.DEFAULT_PUNISH);
				return;
			}

			// lrhand slot for item preview
			if (template.getBodyPart() == L2Item.SLOT_LR_HAND)
			{
				itemList.put(slot + Inventory.PAPERDOLL_LHAND, itemId);
			}
			else
			{
				itemList.put(slot, itemId);
			}
		}

		// Charge buyer and add tax to castle treasury if not owned by npc clan because a Try On is not Free
		if ((totalPrice < 0) || !player.reduceAdena("Wear", (int) totalPrice, player.getLastFolkNPC(), false))
		{
			sendPacket(new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA));
			return;
		}

		if (!itemList.isEmpty())
		{
			player.sendPacket(new ShopPreviewInfo(itemList));
			// Schedule task
			ThreadPoolManager.getInstance().scheduleGeneral(() ->
			{
				player.sendPacket(new SystemMessage(SystemMessage.NO_LONGER_TRYING_ON));
				player.sendPacket(new UserInfo(player));
			}, Config.WEAR_DELAY * 1000L);

		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__C6_REQUESTPREVIEWITEM;
	}
}