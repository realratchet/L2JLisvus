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
package net.sf.l2j.gameserver.model.itemcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ItemInstance.ItemLocation;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.templates.L2Item;

/**
 * @author Advi
 */
public abstract class ItemContainer
{
	protected static final Logger _log = Logger.getLogger(ItemContainer.class.getName());

	protected final List<L2ItemInstance> _items;

	protected ItemContainer()
	{
		_items = new CopyOnWriteArrayList<>();
	}

	protected abstract L2Character getOwner();

	protected abstract ItemLocation getBaseLocation();

	/**
	 * Returns the ownerID of the inventory
	 * @return int
	 */
	public int getOwnerId()
	{
		return getOwner() == null ? 0 : getOwner().getObjectId();
	}
	
	/**
	 * Returns the quantity of items in the inventory
	 * @return int
	 */
	public int getSize()
	{
		return _items.size();
	}
	
	/**
	 * Returns the list of items in inventory
	 * @return L2ItemInstance : items in inventory
	 */
	public L2ItemInstance[] getItems()
	{
		return _items.toArray(new L2ItemInstance[_items.size()]);
	}
	
	/**
	 * Returns the item from inventory by using its <B>itemId</B><BR>
	 * <BR>
	 * @param itemId : int designating the ID of the item
	 * @return L2ItemInstance designating the item or null if not found in inventory
	 */
	public L2ItemInstance getItemByItemId(int itemId)
	{
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getItemId() == itemId))
			{
				return item;
			}
		}

		return null;
	}

	/**
	 * Returns the item from inventory by using its <B>itemId</B><BR>
	 * <BR>
	 * @param itemId : int designating the ID of the item
	 * @param itemToIgnore : used during a loop, to avoid returning the same item
	 * @return L2ItemInstance designating the item or null if not found in inventory
	 */
	public L2ItemInstance getItemByItemId(int itemId, L2ItemInstance itemToIgnore)
	{
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getItemId() == itemId) && !item.equals(itemToIgnore))
			{
				return item;
			}
		}

		return null;
	}

	/**
	 * Returns item from inventory by using its <B>objectId</B>
	 * @param objectId : int designating the ID of the object
	 * @return L2ItemInstance designating the item or null if not found in inventory
	 */
	public L2ItemInstance getItemByObjectId(int objectId)
	{
		for (L2ItemInstance item : _items)
		{
			if (item == null)
			{
				continue;
			}

			if (item.getObjectId() == objectId)
			{
				return item;
			}
		}

		return null;
	}

	public int getInventoryItemCount(int itemId, int enchantLevel)
	{
		int count = 0;

		for (L2ItemInstance item : _items)
		{
			if (item == null)
			{
				continue;
			}

			if ((item.getItemId() == itemId) && ((item.getEnchantLevel() == enchantLevel) || (enchantLevel < 0)))

			{
				if (item.isStackable())
				{
					count = item.getCount();
				}
				else
				{
					count++;
				}
			}
		}

		return count;
	}

	public L2ItemInstance addItem(String process, L2ItemInstance item, L2PcInstance actor, L2Object reference)
	{
		return addItem(process, item, getItemByItemId(item.getItemId()), actor, reference);
	}
	
	/**
	 * Adds item to inventory
	 * @param process : String Identifier of process triggering this action
	 * @param item : L2ItemInstance to be added
	 * @param oldItem : L2ItemInstance Item inside inventory for itemId
	 * @param actor : L2PcInstance Player requesting the item add
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the new item or the updated item in inventory
	 */
	public L2ItemInstance addItem(String process, L2ItemInstance item, L2ItemInstance oldItem, L2PcInstance actor, L2Object reference)
	{
		// If stackable item is found in inventory just add to current quantity
		if (oldItem != null && oldItem.isStackable())
		{
			oldItem.changeCount(process, item.getCount(), actor, reference);
			oldItem.setLastChange(L2ItemInstance.MODIFIED);

			// And destroys the item
			ItemTable.getInstance().destroyItem(process, item, actor, reference);
			item.updateDatabase();
			item = oldItem;

			// Updates database
			if ((item.getItemId() == Inventory.ADENA_ID) && (item.getCount() < (10000 * Config.RATE_DROP_ADENA)))
			{
				// Small adena changes won't be saved to database all the time
				if ((GameTimeController.getInstance().getGameTicks() % 5) == 0)
				{
					item.updateDatabase();
				}
			}
			else
			{
				item.updateDatabase();
			}
		}
		// If item hasn't be found in inventory, create new one
		else
		{
			item.setOwnerId(process, getOwnerId(), actor, reference);
			item.setLocation(getBaseLocation());
			item.setLastChange((L2ItemInstance.ADDED));

			// Add item in inventory
			addItem(item);

			// Updates database
			item.updateDatabase();
		}

		refreshWeight();
		return item;
	}
	
	public L2ItemInstance addItem(String process, int itemId, int count, L2PcInstance actor, L2Object reference)
	{
		return addItem(process, itemId, count, getItemByItemId(itemId), actor, reference);
	}
	
	/**
	 * Adds item to inventory
	 * @param process : String Identifier of process triggering this action
	 * @param itemId : int Item Identifier of the item to be added
	 * @param count : int Quantity of items to be added
	 * @param oldItem L2ItemInstance Item inside inventory for itemId
	 * @param actor : L2PcInstance Player requesting the item add
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the new item or the updated item in inventory
	 */
	public L2ItemInstance addItem(String process, int itemId, int count, L2ItemInstance oldItem, L2PcInstance actor, L2Object reference)
	{
		// If stackable item is found in inventory just add to current quantity
		if (oldItem != null && oldItem.isStackable())
		{
			oldItem.changeCount(process, count, actor, reference);
			oldItem.setLastChange(L2ItemInstance.MODIFIED);
			// Updates database
			if ((itemId == Inventory.ADENA_ID) && (count < (10000 * Config.RATE_DROP_ADENA)))
			{
				// Small adena changes won't be saved to database all the time
				if ((GameTimeController.getInstance().getGameTicks() % 5) == 0)
				{
					oldItem.updateDatabase();
				}
			}
			else
			{
				oldItem.updateDatabase();
			}
		}
		// If item hasn't be found in inventory, create new one
		else
		{
			for (int i = 0; i < count; i++)
			{
				L2Item template = ItemTable.getInstance().getTemplate(itemId);
				if (template == null)
				{
					_log.log(Level.WARNING, (actor != null ? "[" + actor.getName() + "] " : "") + "Invalid ItemId requested: ", itemId);
					return null;
				}

				oldItem = ItemTable.getInstance().createItem(process, itemId, template.isStackable() ? count : 1, actor, reference);
				oldItem.setOwnerId(getOwnerId());
				oldItem.setLocation(getBaseLocation());
				oldItem.setLastChange(L2ItemInstance.ADDED);

				// Add item in inventory
				addItem(oldItem);
				// Updates database
				oldItem.updateDatabase();

				// If stackable, end loop as entire count is included in 1 instance of item
				if (template.isStackable() || !Config.MULTIPLE_ITEM_DROP)
				{
					break;
				}
			}
		}
		
		refreshWeight();
		return oldItem;
	}

	/**
	 * Adds Wear/Try On item to inventory<BR>
	 * <BR>
	 * @param process : String Identifier of process triggering this action
	 * @param itemId : int Item Identifier of the item to be added
	 * @param actor : L2PcInstance Player requesting the item add
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the new worn item
	 */
	public L2ItemInstance addWearItem(String process, int itemId, L2PcInstance actor, L2Object reference)
	{
		// Search the item in the inventory of the player
		L2ItemInstance item = getItemByItemId(itemId);
		
		// There is such item already in inventory
		if (item != null)
		{
			return item;
		}

		// Create and Init the L2ItemInstance corresponding to the Item Identifier and quantity
		// Add the L2ItemInstance object to _allObjects of L2world
		item = ItemTable.getInstance().createItem(process, itemId, 1, actor, reference);

		// Set Item Properties
		item.setWear(true); // "Try On" Item -> Don't save it in database
		item.setOwnerId(getOwnerId());
		item.setLocation(getBaseLocation());
		item.setLastChange((L2ItemInstance.ADDED));

		// Add item in inventory and equip it if necessary (item location defined)
		addItem(item);

		// Calculate the weight loaded by player
		refreshWeight();

		return item;
	}

	/**
	 * Transfers item to another inventory
	 * @param process : String Identifier of process triggering this action
	 * @param objectId 
	 * @param count : int Quantity of items to be transfered
	 * @param target 
	 * @param actor : L2PcInstance Player requesting the item transfer
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the new item or the updated item in inventory
	 */
	public L2ItemInstance transferItem(String process, int objectId, int count, ItemContainer target, L2PcInstance actor, L2Object reference)
	{
		if (target == null)
		{
			return null;
		}

		L2ItemInstance sourceItem = getItemByObjectId(objectId);
		if (sourceItem == null)
		{
			return null;
		}
		L2ItemInstance targetItem = sourceItem.isStackable() ? target.getItemByItemId(sourceItem.getItemId()) : null;

		synchronized (sourceItem)
		{
			if (getItemByObjectId(objectId) != sourceItem)
			{
				return null;
			}

			// Check if requested quantity is available
			if (count > sourceItem.getCount())
			{
				count = sourceItem.getCount();
			}

			// If possible, move entire item object
			if ((sourceItem.getCount() == count) && (targetItem == null))
			{
				removeItem(sourceItem);
				target.addItem(process, sourceItem, actor, reference);
				targetItem = sourceItem;
			}
			else
			{
				if (sourceItem.getCount() > count)
				{
					sourceItem.changeCount(process, -count, actor, reference);
				}
				else // Otherwise destroy old item
				{
					removeItem(sourceItem);
					ItemTable.getInstance().destroyItem(process, sourceItem, actor, reference);
				}

				if (targetItem != null)
				{
					targetItem.changeCount(process, count, actor, reference);
				}
				else
				{
					targetItem = target.addItem(process, sourceItem.getItemId(), count, actor, reference);
				}
			}

			// Updates database
			sourceItem.updateDatabase(true);
			if ((targetItem != sourceItem) && (targetItem != null))
			{
				targetItem.updateDatabase();
			}
			refreshWeight();
		}
		return targetItem;
	}

	/**
	 * Destroy item from inventory and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param item : L2ItemInstance to be destroyed
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public L2ItemInstance destroyItem(String process, L2ItemInstance item, L2PcInstance actor, L2Object reference)
	{
		return destroyItem(process, item, item.getCount(), actor, reference);
	}

	/**
	 * Destroy item from inventory and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param item : L2ItemInstance to be destroyed
	 * @param count 
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public L2ItemInstance destroyItem(String process, L2ItemInstance item, int count, L2PcInstance actor, L2Object reference)
	{
		synchronized (item)
		{
			// Adjust item quantity
			if (item.getCount() > count)
			{
				item.changeCount(process, -count, actor, reference);
				item.setLastChange(L2ItemInstance.MODIFIED);

				// Don't update often for untraced items
				if ((process != null) || ((GameTimeController.getInstance().getGameTicks() % 10) == 0))
				{
					item.updateDatabase();
				}

				refreshWeight();

				return item;
			}

			if (item.getCount() < count)
			{
				return null;
			}

			boolean removed = removeItem(item);
			if (!removed)
			{
				return null;
			}

			ItemTable.getInstance().destroyItem(process, item, actor, reference);

			item.updateDatabase();
			refreshWeight();
		}

		return item;
	}

	/**
	 * Destroy item from inventory by using its <B>objectID</B> and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param objectId : int Item Instance identifier of the item to be destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public L2ItemInstance destroyItem(String process, int objectId, int count, L2PcInstance actor, L2Object reference)
	{
		L2ItemInstance item = getItemByObjectId(objectId);
		if (item == null)
		{
			return null;
		}

		return destroyItem(process, item, count, actor, reference);
	}

	/**
	 * Destroy item from inventory by using its <B>itemId</B> and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param itemId : int Item identifier of the item to be destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public L2ItemInstance destroyItemByItemId(String process, int itemId, int count, L2PcInstance actor, L2Object reference)
	{
		L2ItemInstance item = getItemByItemId(itemId);
		if (item == null)
		{
			return null;
		}

		return destroyItem(process, item, count, actor, reference);
	}

	/**
	 * Destroy all items from inventory and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 */
	public synchronized void destroyAllItems(String process, L2PcInstance actor, L2Object reference)
	{
		for (L2ItemInstance item : _items)
		{
			destroyItem(process, item, actor, reference);
		}
	}

	/**
	 * Get warehouse adena
	 * @return 
	 */
	public int getAdena()
	{
		int count = 0;

		for (L2ItemInstance item : _items)
		{
			if (item.getItemId() == Inventory.ADENA_ID)
			{
				count = item.getCount();
				return count;
			}
		}

		return count;
	}

	/**
	 * Adds item to inventory for further adjustments.
	 * @param item : L2ItemInstance to be added from inventory
	 */
	protected void addItem(L2ItemInstance item)
	{
		_items.add(0, item);
	}

	/**
	 * Removes item from inventory for further adjustments.
	 * @param item : L2ItemInstance to be removed from inventory
	 * @return 
	 */
	protected boolean removeItem(L2ItemInstance item)
	{
		return _items.remove(item);
	}

	/**
	 * Refresh the weight of equipment loaded
	 */
	protected void refreshWeight()
	{
	}

	/**
	 * Delete item object from world.
	 */
	public void deleteMe()
	{
		deleteMe(true);
	}
	
	/**
	 * Delete item object from world.
	 * 
	 * @param update Updates items in database.
	 */
	public void deleteMe(boolean update)
	{
		if (getOwner() != null)
		{
			for (L2ItemInstance item : _items)
			{
				if (update)
				{
					item.updateDatabase(true);
				}
				L2World.getInstance().removeObject(item);
			}
		}
		_items.clear();
	}

	/**
	 * Update database with items in inventory
	 */
	public void updateDatabase()
	{
		if (getOwner() != null)
		{
			for (L2ItemInstance item : _items)
			{
				if (item != null)
				{
					item.updateDatabase(true);
				}
			}
		}
	}

	/**
	 * Get back items in container from database
	 */
	public void restore()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT object_id FROM items WHERE owner_id=? AND (loc=?) ORDER BY object_id DESC"))
		{
			statement.setInt(1, getOwnerId());
			statement.setString(2, getBaseLocation().name());
			try (ResultSet inv = statement.executeQuery())
			{
				L2ItemInstance item;
				while (inv.next())
				{
					int objectId = inv.getInt(1);
					item = L2ItemInstance.restoreFromDb(objectId);
					if (item == null)
					{
						continue;
					}

					L2World.getInstance().storeObject(item);

					// If stackable item is found in inventory just add to current quantity
					L2ItemInstance invItem = getItemByItemId(item.getItemId());
					if (item.isStackable() && invItem != null)
					{
						addItem("Restore", item, invItem, null, getOwner());
					}
					else
					{
						addItem(item);
					}
				}
			}
			refreshWeight();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "could not restore container:", e);
		}
	}

	public boolean validateCapacity(int slots)
	{
		return true;
	}

	public boolean validateWeight(int weight)
	{
		return true;
	}
}