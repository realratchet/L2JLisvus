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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.itemcontainer.ItemContainer;
import net.sf.l2j.gameserver.model.itemcontainer.PcFreight;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.IllegalPlayerAction;
import net.sf.l2j.gameserver.util.Util;

/**
 * @author -Wooden-
 */
public class RequestPackageSend extends L2GameClientPacket
{
	private static final String _C_9F_REQUESTPACKAGESEND = "[C] 9F RequestPackageSend";
	private static Logger _log = Logger.getLogger(RequestPackageSend.class.getName());
	private final List<Item> _items = new ArrayList<>();
	private int _objectID;
	private int _count;

	private class Item
	{
		public int id;
		public int count;

		public Item(int i, int c)
		{
			id = i;
			count = c;
		}
	}
	
	@Override
	protected void readImpl()
	{
		_objectID = readD();
		_count = readD();

		if (_count < 0 || _count > 500)
		{
			_count = -1;
			return;
		}

		for (int i = 0; i < _count; i++)
		{
			int id = readD(); // this is some id sent in PackageSendableList
			int count = readD();
			_items.add(new Item(id, count));
		}
	}

	/**
	 * @see net.sf.l2j.gameserver.network.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	protected void runImpl()
	{
		if (_count == -1)
		{
			return;
		}

		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}

		// Why would somebody do such a thing?
		if (player.getObjectId() == _objectID)
		{
			return;
		}

		if (!player.getAccountChars().containsKey(_objectID))
		{
			return;
		}

		ItemContainer warehouse = player.getActiveWarehouse();
		if (warehouse == null)
		{
			return;
		}

		if (player.getActiveEnchantItem() != null)
		{
			Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to use enchant Exploit!", IllegalPlayerAction.PUNISH_KICKBAN);
			player.setActiveEnchantItem(null);
			return;
		}
		
		if (player.isInStoreMode())
		{
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			return;
		}

		PcFreight freight = null;
		if (warehouse instanceof PcFreight)
		{
			freight = (PcFreight) warehouse;
		}

		if (freight == null)
		{
			return;
		}

		// Restore player items from database
		freight.doQuickRestore(_objectID);

		L2FolkInstance manager = player.getLastFolkNPC();
		if (manager == null || !manager.isWarehouse() || !manager.canInteract(player))
		{
			freight.deleteMe(false);
			return;
		}

		if (player.getAccessLevel() > 0 && player.getAccessLevel() < Config.GM_TRANSACTION)
		{
			player.sendMessage("Transactions are disabled for your Access Level.");
			freight.deleteMe(false);
			return;
		}

		// Alt game - Karma punishment
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_USE_WAREHOUSE && (player.getKarma() > 0))
		{
			freight.deleteMe(false);
			return;
		}

		// Freight price from config or normal price per item slot (30)
		int fee = _count * Config.ALT_GAME_FREIGHT_PRICE;
		int currentAdena = player.getAdena();
		int slots = 0;

		for (Item i : _items)
		{
			int objectId = i.id;
			int count = i.count;

			// Check validity of requested item
			L2ItemInstance item = player.checkItemManipulation(objectId, count, "deposit");
			if (item == null)
			{
				_log.warning("Error depositing a warehouse object for char " + player.getName() + " (validity check)");
				i.id = 0;
				i.count = 0;
				continue;
			}

			// Calculate needed adena and slots
			if (item.getItemId() == Inventory.ADENA_ID)
			{
				currentAdena -= count;
			}

			if (!item.isStackable())
			{
				slots += count;
			}
			else
			{
				L2ItemInstance oldItem = warehouse.getItemByItemId(item.getItemId());
				if (oldItem != null)
				{
					if (((long)oldItem.getCount() + count) > L2ItemInstance.getMaxItemCount(oldItem.getItemId()))
					{
						player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_THE_LIMIT));
						freight.deleteMe(false);
						return;
					}
				}
				else
				{
					slots++;
				}
			}
		}

		// Item Max Limit Check
		if (!warehouse.validateCapacity(slots))
		{
			sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_QUANTITY_THAT_CAN_BE_INPUTTED));
			freight.deleteMe(false);
			return;
		}

		// Check if enough adena and charge the fee
		if ((currentAdena < fee) || !player.reduceAdena("Warehouse", fee, manager, false))
		{
			sendPacket(new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA));
			freight.deleteMe(false);
			return;
		}

		// Set freight location
		freight.setActiveLocation(Config.ALT_GAME_FREIGHTS ? 0 : manager.getNpcId());

		// Proceed to the transfer
		InventoryUpdate playerIU = Config.FORCE_INVENTORY_UPDATE ? null : new InventoryUpdate();
		for (Item i : _items)
		{
			int objectId = i.id;
			int count = i.count;

			// check for an invalid item
			if ((objectId == 0) && (count == 0))
			{
				continue;
			}

			L2ItemInstance oldItem = player.getInventory().getItemByObjectId(objectId);
			if (oldItem == null)
			{
				_log.warning("Error depositing a warehouse object for char " + player.getName() + " (olditem == null)");
				continue;
			}

			if (!oldItem.isAvailable(player, true))
			{
				continue;
			}

			L2ItemInstance newItem = player.getInventory().transferItem("Warehouse", objectId, count, warehouse, player, manager);
			if (newItem == null)
			{
				_log.warning("Error depositing a warehouse object for char " + player.getName() + " (newitem == null)");
				continue;
			}

			if (playerIU != null)
			{
				if ((oldItem.getCount() > 0) && (oldItem != newItem))
				{
					playerIU.addModifiedItem(oldItem);
				}
				else
				{
					playerIU.addRemovedItem(oldItem);
				}
			}
		}
		
		// Cleanup data from quick restore
		freight.deleteMe(false);

		// Send updated item list to the player
		if (playerIU != null)
		{
			player.sendPacket(playerIU);
		}
		else
		{
			player.sendPacket(new ItemList(player, false));
		}

		// Update current load status on player
		StatusUpdate su = new StatusUpdate(player.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
	}

	@Override
	public String getType()
	{
		return _C_9F_REQUESTPACKAGESEND;
	}
}