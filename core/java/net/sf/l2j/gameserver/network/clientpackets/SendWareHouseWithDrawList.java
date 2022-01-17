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
import net.sf.l2j.gameserver.model.ClanWarehouse;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.ItemContainer;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ... 32 SendWareHouseWithDrawList cd (dd) WootenGil rox :P
 * @version $Revision: 1.2.2.1.2.4 $ $Date: 2005/03/29 23:15:16 $
 */
public class SendWareHouseWithDrawList extends L2GameClientPacket
{
	private static final String _C__32_SENDWAREHOUSEWITHDRAWLIST = "[C] 32 SendWareHouseWithDrawList";
	private static Logger _log = Logger.getLogger(SendWareHouseWithDrawList.class.getName());

	private int _count;
	private int[] _items;
	
	@Override
	protected void readImpl()
	{
		_count = readD();
		if ((_count < 0) || ((_count * 8) > _buf.remaining()) || (_count > Config.MAX_ITEM_IN_PACKET))
		{
			_count = 0;
			_items = null;
			return;
		}
		_items = new int[_count * 2];
		for (int i = 0; i < _count; i++)
		{
			int objectId = readD();
			_items[(i * 2) + 0] = objectId;
			int count = readD();
			if (count < 0)
			{
				_count = 0;
				_items = null;
				return;
			}
			_items[(i * 2) + 1] = count;
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
		ItemContainer warehouse = player.getActiveWarehouse();
		if (warehouse == null)
		{
			return;
		}
		
		L2FolkInstance manager = player.getLastFolkNPC();
		if (manager == null || !manager.isWarehouse() || !manager.canInteract(player))
		{
			return;
		}

		if (warehouse instanceof ClanWarehouse && player.getAccessLevel() > 0 && player.getAccessLevel() < Config.GM_TRANSACTION)
		{
			player.sendMessage("Transactions are disabled for your Access Level.");
			return;
		}

		// Alt game - Karma punishment
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_USE_WAREHOUSE && (player.getKarma() > 0))
		{
			return;
		}

		if ((warehouse instanceof ClanWarehouse) && !player.isClanLeader())
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
			return;
		}

		int weight = 0;
		int slots = 0;

		for (int i = 0; i < _count; i++)
		{
			int objectId = _items[(i * 2) + 0];
			int count = _items[(i * 2) + 1];

			// Calculate needed slots
			L2ItemInstance item = warehouse.getItemByObjectId(objectId);
			if (item == null)
			{
				continue;
			}
			
			weight += weight * item.getItem().getWeight();
			if (!item.isStackable())
			{
				slots += count;
			}
			else
			{
				L2ItemInstance oldItem = player.getInventory().getItemByItemId(item.getItemId());
				if (oldItem != null)
				{
					if (((long)oldItem.getCount() + count) > L2ItemInstance.getMaxItemCount(oldItem.getItemId()))
					{
						player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_THE_LIMIT));
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
		if (!player.getInventory().validateCapacity(slots))
		{
			sendPacket(new SystemMessage(SystemMessage.SLOTS_FULL));
			return;
		}

		// Weight limit Check
		if (!player.getInventory().validateWeight(weight))
		{
			sendPacket(new SystemMessage(SystemMessage.WEIGHT_LIMIT_EXCEEDED));
			return;
		}

		// Proceed to the transfer
		InventoryUpdate playerIU = Config.FORCE_INVENTORY_UPDATE ? null : new InventoryUpdate();
		for (int i = 0; i < _count; i++)
		{
			int objectId = _items[(i * 2) + 0];
			int count = _items[(i * 2) + 1];
			
			L2ItemInstance oldItem = warehouse.getItemByObjectId(objectId);
			if ((oldItem == null) || (oldItem.getCount() < count))
			{
				player.sendMessage("Cannot withdraw requested item(s).");
			}
			L2ItemInstance newItem = warehouse.transferItem("Warehouse", objectId, count, player.getInventory(), player, player.getLastFolkNPC());
			if (newItem == null)
			{
				_log.warning("Error withdrawing a warehouse object for char " + player.getName());
				continue;
			}

			if (playerIU != null)
			{
				if (newItem.getCount() > count)
				{
					playerIU.addModifiedItem(newItem);
				}
				else
				{
					playerIU.addNewItem(newItem);
				}
			}
		}

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

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__32_SENDWAREHOUSEWITHDRAWLIST;
	}
}