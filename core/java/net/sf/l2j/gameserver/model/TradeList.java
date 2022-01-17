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
package net.sf.l2j.gameserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.L2ItemInstance.ItemLocation;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.itemcontainer.PcInventory;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2EtcItemType;
import net.sf.l2j.gameserver.templates.L2Item;

/**
 * @author Advi
 */
public class TradeList
{
	public class TradeItem
	{
		private int _objectId;
		private final L2Item _item;
		private int _enchant;
		private final int _type2;
		private int _count;
		private int _price;
		
		public TradeItem(L2ItemInstance item, int count, int price)
		{
			_objectId = item.getObjectId();
			_item = item.getItem();
			_enchant = item.getEnchantLevel();
			_type2 = item.getCustomType2();
			_count = count;
			_price = price;
		}
		
		public TradeItem(L2Item item, int count, int price)
		{
			_objectId = 0;
			_item = item;
			_enchant = 0;
			_type2 = 0;
			_count = count;
			_price = price;
		}
		
		public TradeItem(TradeItem item, int count, int price)
		{
			_objectId = item.getObjectId();
			_item = item.getItem();
			_enchant = item.getEnchant();
			_type2 = 0;
			_count = count;
			_price = price;
		}
		
		public void setObjectId(int objectId)
		{
			_objectId = objectId;
		}
		
		public int getObjectId()
		{
			return _objectId;
		}
		
		public L2Item getItem()
		{
			return _item;
		}
		
		public void setEnchant(int enchant)
		{
			_enchant = enchant;
		}
		
		public int getEnchant()
		{
			return _enchant;
		}
		
		public int getCustomType2()
		{
			return _type2;
		}
		
		public void setCount(int count)
		{
			_count = count;
		}
		
		public int getCount()
		{
			return _count;
		}
		
		public void setPrice(int price)
		{
			_price = price;
		}
		
		public int getPrice()
		{
			return _price;
		}
	}
	
	private static Logger _log = Logger.getLogger(TradeList.class.getName());
	
	private final L2PcInstance _owner;
	private L2PcInstance _partner;
	private final List<TradeItem> _items;
	private String _title;
	private boolean _packaged;
	
	private boolean _confirmed = false;
	private boolean _locked = false;
	
	public TradeList(L2PcInstance owner)
	{
		_items = new CopyOnWriteArrayList<>();
		_owner = owner;
	}
	
	public L2PcInstance getOwner()
	{
		return _owner;
	}
	
	public void setPartner(L2PcInstance partner)
	{
		_partner = partner;
	}
	
	public L2PcInstance getPartner()
	{
		return _partner;
	}
	
	public void setTitle(String title)
	{
		_title = title;
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public boolean isLocked()
	{
		return _locked;
	}
	
	public boolean isConfirmed()
	{
		return _confirmed;
	}
	
	public boolean isPackaged()
	{
		return _packaged;
	}
	
	public void setPackaged(boolean value)
	{
		_packaged = value;
	}
	
	/**
	 * Retrieves items from TradeList
	 * @return 
	 */
	public TradeItem[] getItems()
	{
		return _items.toArray(new TradeItem[_items.size()]);
	}
	
	/**
	 * Returns the list of items in inventory available for transaction
	 * @param inventory 
	 * @return L2ItemInstance : items in inventory
	 */
	public TradeList.TradeItem[] getAvailableItems(PcInventory inventory)
	{
		List<TradeList.TradeItem> list = new ArrayList<>();
		for (TradeList.TradeItem item : _items)
		{
			item = new TradeItem(item, item.getCount(), item.getPrice());
			inventory.adjustAvailableItem(item, list);
			list.add(item);
		}
		
		return list.toArray(new TradeList.TradeItem[list.size()]);
	}
	
	/**
	 * Returns Item List size
	 * @return 
	 */
	public int getItemCount()
	{
		return _items.size();
	}
	
	/**
	 * Adjust available item from Inventory by the one in this list
	 * @param item : L2ItemInstance to be adjusted
	 * @return TradeItem representing adjusted item
	 */
	public TradeItem adjustAvailableItem(L2ItemInstance item)
	{
		for (TradeItem exclItem : _items)
		{
			// Keep item id check for stackable items to avoid packet visual exploits (e.g. two slots with spirit ores)
			boolean equals  = item.isStackable() ? exclItem.getItem().getItemId() == item.getItemId() : exclItem.getObjectId() == item.getObjectId();
			if (equals)
			{
				if (item.getCount() <= exclItem.getCount())
				{
					return null;
				}
				return new TradeItem(item, item.getCount() - exclItem.getCount(), item.getReferencePrice());
			}
		}
		return new TradeItem(item, item.getCount(), item.getReferencePrice());
	}
	
	/**
	 * Adjust ItemRequest by corresponding item in this list using its <b>ObjectId</b>
	 * @param item : ItemRequest to be adjusted
	 */
	public void adjustItemRequest(ItemRequest item)
	{
		for (TradeItem filtItem : _items)
		{
			if (filtItem.getObjectId() == item.getObjectId())
			{
				if (filtItem.getCount() < item.getCount())
				{
					item.setCount(filtItem.getCount());
				}
				return;
			}
		}
		item.setCount(0);
	}
	
	/**
	 * Add simplified item to TradeList
	 * @param objectId : int
	 * @param count : int
	 * @return
	 */
	public synchronized TradeItem addItem(int objectId, int count)
	{
		return addItem(objectId, count, 0);
	}
	
	/**
	 * Add item to TradeList
	 * @param objectId : int
	 * @param count : int
	 * @param price : int
	 * @return
	 */
	public synchronized TradeItem addItem(int objectId, int count, int price)
	{
		if (isLocked())
		{
			_log.warning(_owner.getName() + ": Attempt to modify locked TradeList!");
			return null;
		}
		
		L2Object o = L2World.getInstance().findObject(objectId);
		if ((o == null) || !(o instanceof L2ItemInstance))
		{
			_log.warning(_owner.getName() + ": Attempt to add invalid item to TradeList!");
			return null;
		}
		
		L2ItemInstance item = (L2ItemInstance) o;
		
		// Item is no longer inside this player's inventory or it's equipped
		if (item.getOwnerId() != _owner.getObjectId() || item.getLocation() != ItemLocation.INVENTORY)
		{
			return null;
		}
		
		// Player should not sell one's own pet, if pet is already summoned
		if (_owner.getPet() != null && _owner.getPet().getControlItemObjectId() == objectId || _owner.getMountObjectID() == objectId)
		{
			return null;
		}
		
		if (!item.isTradeable())
		{
			return null;
		}
		
		if (item.getItemType() == L2EtcItemType.QUEST)
		{
			return null;
		}
		
		// Prevent item from exceeding count limit
		if (count > L2ItemInstance.getMaxItemCount(item.getItemId()))
		{
			count = L2ItemInstance.getMaxItemCount(item.getItemId());
		}
		
		if (count <= 0)
		{
			return null;
		}
		
		if (count > item.getCount())
		{
			return null;
		}
		
		if (!item.isStackable() && count > 1)
		{
			_log.warning(_owner.getName() + ": Attempt to add non-stackable item to TradeList with count > 1!");
			return null;
		}
		
		for (TradeItem checkItem : _items)
		{
			if (checkItem.getObjectId() == objectId)
			{
				return null;
			}
		}
		
		TradeItem titem = new TradeItem(item, count, price);
		_items.add(titem);
		
		// If Player has already confirmed this trade, invalidate the confirmation
		invalidateConfirmation();
		return titem;
	}
	
	/**
	 * Add item to TradeList
	 * @param itemId : int
	 * @param enchantLevel : int
	 * @param count : int
	 * @param price : int
	 * @return
	 */
	public synchronized TradeItem addUniqueItem(int itemId, int enchantLevel, int count, int price)
	{
		if (isLocked())
		{
			_log.warning(_owner.getName() + ": Attempt to modify locked TradeList!");
			return null;
		}
		
		// Check if owner has this item and it's not equipped
		L2ItemInstance item = null;
		for (L2ItemInstance i : _owner.getInventory().getItems())
		{
			if (i != null && i.getItemId() == itemId && i.getEnchantLevel() == enchantLevel)
			{
				item = i;
				break;
			}
		}

		if (item == null)
		{
			_log.warning(_owner.getName() + ": Attempt to add invalid item to TradeList!");
			return null;
		}
		
		if (!item.isTradeable())
		{
			return null;
		}
		
		if (item.getItemType() == L2EtcItemType.QUEST)
		{
			return null;
		}
		
		if (item.isStackable())
		{
			if (((long)item.getCount() + count) > L2ItemInstance.getMaxItemCount(item.getItemId()))
			{
				_owner.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_THE_LIMIT));
				return null;
			}
		}
		else
		{
			if (count > 1)
			{
				_log.warning(_owner.getName() + ": Attempt to add non-stackable item to TradeList with count > 1!");
				return null;
			}
		}
		
		TradeItem titem = new TradeItem(item, count, price);
		_items.add(titem);
		
		// If player has already confirmed this trade, invalidate the confirmation
		invalidateConfirmation();
		return titem;
	}
	
	/**
	 * Remove item from TradeList
	 * @param objectId : int
	 * @param itemId : int
	 * @param enchantLevel : int
	 * @param count : int
	 * @return
	 */
	public synchronized TradeItem removeItem(int objectId, int itemId, int enchantLevel, int count)
	{
		if (isLocked())
		{
			_log.warning(_owner.getName() + ": Attempt to modify locked TradeList!");
			return null;
		}
		
		for (TradeItem titem : _items)
		{
			if (titem.getObjectId() == objectId || titem.getItem().getItemId() == itemId && titem.getEnchant() == enchantLevel)
			{
				// If Partner has already confirmed this trade, invalidate the confirmation
				if (_partner != null)
				{
					TradeList partnerList = _partner.getActiveTradeList();
					if (partnerList == null)
					{
						_log.warning(_partner.getName() + ": Trading partner (" + _partner.getName() + ") is invalid in this trade!");
						return null;
					}
					partnerList.invalidateConfirmation();
				}
				
				// Reduce item count or complete item
				if ((count != -1) && (titem.getCount() > count))
				{
					titem.setCount(titem.getCount() - count);
				}
				else
				{
					_items.remove(titem);
				}
				
				return titem;
			}
		}
		return null;
	}
	
	/**
	 * Update items in TradeList according their quantity in owner inventory
	 */
	public synchronized void updateItems()
	{
		for (TradeItem titem : _items)
		{
			L2ItemInstance item = _owner.getInventory().getItemByObjectId(titem.getObjectId());
			if (item == null || titem.getCount() < 1)
			{
				removeItem(titem.getObjectId(), -1, titem.getEnchant(), -1);
			}
			else if (item.getCount() < titem.getCount())
			{
				titem.setCount(item.getCount());
			}
		}
	}
	
	/**
	 * Locks TradeList, no further changes are allowed
	 */
	public void lock()
	{
		_locked = true;
	}
	
	/**
	 * Clears item list
	 */
	public void clear()
	{
		_items.clear();
		_locked = false;
	}
	
	/**
	 * Confirms TradeList
	 * @return : boolean
	 */
	public boolean confirm()
	{
		if (_confirmed)
		{
			return true; // Already confirmed
		}
		
		// If Partner has already confirmed this trade, proceed exchange
		if (_partner != null)
		{
			TradeList partnerList = _partner.getActiveTradeList();
			if (partnerList == null)
			{
				_log.warning(_partner.getName() + ": Trading partner (" + _partner.getName() + ") is invalid in this trade!");
				return false;
			}
			
			// Synchronization order to avoid deadlock
			TradeList sync1, sync2;
			if (getOwner().getObjectId() > partnerList.getOwner().getObjectId())
			{
				sync1 = partnerList;
				sync2 = this;
			}
			else
			{
				sync1 = this;
				sync2 = partnerList;
			}
			
			synchronized (sync1)
			{
				synchronized (sync2)
				{
					_confirmed = true;
					if (partnerList.isConfirmed())
					{
						partnerList.lock();
						this.lock();
						if (!partnerList.validate())
						{
							return false;
						}
						
						if (!this.validate())
						{
							return false;
						}
						
						doExchange(partnerList);
					}
					else
					{
						_partner.onTradeConfirm(_owner);
					}
				}
			}
			
		}
		else
		{
			_confirmed = true;
		}
		
		return _confirmed;
	}
	
	/**
	 * Cancels TradeList confirmation
	 */
	public void invalidateConfirmation()
	{
		_confirmed = false;
	}
	
	/**
	 * Validates TradeList with owner inventory
	 * @return 
	 */
	private boolean validate()
	{
		// Check for Owner validity
		if ((_owner == null) || (L2World.getInstance().findObject(_owner.getObjectId()) == null))
		{
			_log.warning("Invalid owner of TradeList");
			return false;
		}
		
		// Check for Item validity
		for (TradeItem titem : _items)
		{
			L2ItemInstance item = _owner.checkItemManipulation(titem.getObjectId(), titem.getCount(), "transfer");
			if ((item == null) || (titem.getCount() < 1))
			{
				_log.warning(_owner.getName() + ": Invalid Item in TradeList");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Transfers all TradeItems from inventory to partner
	 * @param partner 
	 * @param ownerIU 
	 * @param partnerIU 
	 * @return 
	 */
	private boolean transferItems(L2PcInstance partner, InventoryUpdate ownerIU, InventoryUpdate partnerIU)
	{
		for (TradeItem titem : _items)
		{
			L2ItemInstance oldItem = _owner.getInventory().getItemByObjectId(titem.getObjectId());
			if (oldItem == null)
			{
				return false;
			}
			
			L2ItemInstance partnerItem = partner.getInventory().getItemByItemId(oldItem.getItemId());
			if (partnerItem != null && ((long)partnerItem.getCount() + titem.getCount()) > L2ItemInstance.getMaxItemCount(partnerItem.getItemId()))
			{
				partner.sendPacket(new SystemMessage(oldItem.getItemId() == Inventory.ADENA_ID ? 
					SystemMessage.YOU_HAVE_EXCEEDED_POCKET_MONEY_LIMIT : SystemMessage.YOU_HAVE_EXCEEDED_THE_LIMIT));
				return false;
			}
			
			L2ItemInstance newItem = _owner.getInventory().transferItem("Trade", titem.getObjectId(), titem.getCount(), partner.getInventory(), _owner, _partner);
			if (newItem == null)
			{
				return false;
			}
			
			// Add changes to inventory update packets
			if (ownerIU != null)
			{
				if ((oldItem.getCount() > 0) && (oldItem != newItem))
				{
					ownerIU.addModifiedItem(oldItem);
				}
				else
				{
					ownerIU.addRemovedItem(oldItem);
				}
			}
			
			if (partnerIU != null)
			{
				if (newItem.getCount() > titem.getCount())
				{
					partnerIU.addModifiedItem(newItem);
				}
				else
				{
					partnerIU.addNewItem(newItem);
				}
			}
		}
		return true;
	}
	
	/**
	 * Count items slots
	 * @param partner 
	 * @return 
	 */
	public int countItemsSlots(L2PcInstance partner)
	{
		int slots = 0;
		
		for (TradeItem item : _items)
		{
			if (item == null)
			{
				continue;
			}
			L2Item template = ItemTable.getInstance().getTemplate(item.getItem().getItemId());
			if (template == null)
			{
				continue;
			}
			if (!template.isStackable())
			{
				slots += item.getCount();
			}
			else if (partner.getInventory().getItemByItemId(item.getItem().getItemId()) == null)
			{
				slots++;
			}
		}
		
		return slots;
	}
	
	/**
	 * Calculate weight of items in tradeList
	 * @return 
	 */
	public int calcItemsWeight()
	{
		int weight = 0;
		
		for (TradeItem item : _items)
		{
			if (item == null)
			{
				continue;
			}
			L2Item template = ItemTable.getInstance().getTemplate(item.getItem().getItemId());
			if (template == null)
			{
				continue;
			}
			weight += item.getCount() * template.getWeight();
		}
		
		return weight;
	}
	
	/**
	 * Proceeds with trade.
	 * 
	 * @param partnerList 
	 */
	private void doExchange(TradeList partnerList)
	{
		boolean success = false;
		// check weight and slots
		if ((!this.getOwner().getInventory().validateWeight(partnerList.calcItemsWeight())) || !(partnerList.getOwner().getInventory().validateWeight(this.calcItemsWeight())))
		{
			partnerList.getOwner().sendPacket(new SystemMessage(SystemMessage.WEIGHT_LIMIT_EXCEEDED));
			this.getOwner().sendPacket(new SystemMessage(SystemMessage.WEIGHT_LIMIT_EXCEEDED));
		}
		else if ((!this.getOwner().getInventory().validateCapacity(partnerList.countItemsSlots(this.getOwner()))) || (!partnerList.getOwner().getInventory().validateCapacity(this.countItemsSlots(partnerList.getOwner()))))
		{
			partnerList.getOwner().sendPacket(new SystemMessage(SystemMessage.SLOTS_FULL));
			this.getOwner().sendPacket(new SystemMessage(SystemMessage.SLOTS_FULL));
		}
		else
		{
			// Prepare inventory update packet
			InventoryUpdate ownerIU = Config.FORCE_INVENTORY_UPDATE ? null : new InventoryUpdate();
			InventoryUpdate partnerIU = Config.FORCE_INVENTORY_UPDATE ? null : new InventoryUpdate();
			
			// Transfer items
			boolean partnerTransfer = partnerList.transferItems(this.getOwner(), partnerIU, ownerIU);
			boolean ownerTransfer = transferItems(partnerList.getOwner(), ownerIU, partnerIU);
			if (!partnerTransfer || !ownerTransfer)
			{
				// Finish the trade
				partnerList.getOwner().onTradeFinish(false);
				this.getOwner().onTradeFinish(false);
				return;
			}
			
			// Send inventory update packet
			if (ownerIU != null)
			{
				_owner.sendPacket(ownerIU);
			}
			else
			{
				_owner.sendPacket(new ItemList(_owner, false));
			}
			
			if (partnerIU != null)
			{
				_partner.sendPacket(partnerIU);
			}
			else
			{
				_partner.sendPacket(new ItemList(_partner, false));
			}
			
			// Update current load as well
			StatusUpdate playerSU = new StatusUpdate(_owner.getObjectId());
			playerSU.addAttribute(StatusUpdate.CUR_LOAD, _owner.getCurrentLoad());
			_owner.sendPacket(playerSU);
			playerSU = new StatusUpdate(_partner.getObjectId());
			playerSU.addAttribute(StatusUpdate.CUR_LOAD, _partner.getCurrentLoad());
			_partner.sendPacket(playerSU);
			
			success = true;
		}
		// Finish the trade
		partnerList.getOwner().onTradeFinish(success);
		this.getOwner().onTradeFinish(success);
	}
	
	/**
	 * Buy items from this PrivateStore list
	 * @param player 
	 * @param items 
	 * @return : boolean true if success
	 */
	public synchronized boolean privateStoreBuy(L2PcInstance player, ItemRequest[] items)
	{
		if (_locked)
		{
			return false;
		}
		
		if (!validate())
		{
			lock();
			return false;
		}
		
		long slots = 0;
		long weight = 0;
		long totalPrice = 0;
		
		final PcInventory ownerInventory = _owner.getInventory();
		final PcInventory playerInventory = player.getInventory();
		
		for (ItemRequest item : items)
		{
			boolean found = false;

			for (TradeItem ti : _items)
			{
				if (ti.getObjectId() == item.getObjectId())
				{
					if (ti.getPrice() == item.getPrice())
					{
						if (ti.getCount() < item.getCount())
							item.setCount(ti.getCount());
						found = true;
					}
					break;
				}
			}
			
			// Item with this objectId and price not found in trade list
			if (!found)
			{
				item.setCount(0);
				continue;
			}

			// Check if requested item is available for manipulation
			L2ItemInstance oldItem = _owner.checkItemManipulation(item.getObjectId(), item.getCount(), "sell");
			if (oldItem == null || !oldItem.isTradeable())
			{
				// Private store sell invalid item - disable it
				lock();
				return false;
			}
			
			// Check for overflow in the single item
			if ((L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID) / item.getCount()) < item.getPrice())
			{
				// Private store attempting to overflow - disable it
				lock();
				return false;
			}

			totalPrice += item.getCount() * item.getPrice();
			// Check for overflow of the total price
			if (totalPrice > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID) || totalPrice < 0)
			{
				// Private store attempting to overflow - disable it
				lock();
				return false;
			}
			
			weight += item.getCount() * oldItem.getItem().getWeight();
			if (!oldItem.isStackable())
			{
				slots += item.getCount();
			}
			else
			{
				L2ItemInstance invItem = player.getInventory().getItemByItemId(oldItem.getItemId());
				if (invItem != null)
				{
					if (((long)invItem.getCount() + item.getCount()) > L2ItemInstance.getMaxItemCount(invItem.getItemId()))
					{
						player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_THE_LIMIT));
						return false;
					}
				}
				else
				{
					slots++;
				}
			}
		}
		
		if (totalPrice > playerInventory.getAdena())
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA));
			return false;
		}
		
		if (weight > Integer.MAX_VALUE || !player.getInventory().validateWeight((int) weight))
		{
			player.sendPacket(new SystemMessage(SystemMessage.WEIGHT_LIMIT_EXCEEDED));
			return false;
		}
		
		if (slots > Integer.MAX_VALUE || !player.getInventory().validateCapacity((int) slots))
		{
			player.sendPacket(new SystemMessage(SystemMessage.SLOTS_FULL));
			return false;
		}
		
		// Prepare inventory update packets
		final InventoryUpdate ownerIU = new InventoryUpdate();
		final InventoryUpdate playerIU = new InventoryUpdate();
		
		L2ItemInstance adenaItem = playerInventory.getAdenaInstance();
		playerInventory.reduceAdena("PrivateStore", (int) totalPrice, player, _owner);
		playerIU.addItem(adenaItem);
		ownerInventory.addAdena("PrivateStore", (int) totalPrice, _owner, player);
		ownerIU.addItem(ownerInventory.getAdenaInstance());
		
		boolean ok = true;
		
		// Transfer items
		for (ItemRequest item : items)
		{
			if (item.getCount() == 0)
			{
				continue;
			}
			
			// Check if requested item is available for manipulation
			L2ItemInstance oldItem = _owner.checkItemManipulation(item.getObjectId(), item.getCount(), "sell");
			if (oldItem == null)
			{
				lock();
				ok = false;
				break;
			}
			
			// Proceed with item transfer
			L2ItemInstance newItem = ownerInventory.transferItem("PrivateStore", item.getObjectId(), item.getCount(), playerInventory, _owner, player);
			if (newItem == null)
			{
				ok = false;
				break;
			}
			removeItem(item.getObjectId(), -1, item.getEnchantLevel(), item.getCount());
			
			// Add changes to inventory update packets
			if (oldItem.getCount() > 0 && oldItem != newItem)
			{
				ownerIU.addModifiedItem(oldItem);
			}
			else
			{
				ownerIU.addRemovedItem(oldItem);
			}
			
			if (newItem.getCount() > item.getCount())
			{
				playerIU.addModifiedItem(newItem);
			}
			else
			{
				playerIU.addNewItem(newItem);
			}
			
			// Send messages about the transaction to both players
			if (newItem.isStackable())
			{
				SystemMessage msg = new SystemMessage(SystemMessage.S1_PURCHASED_S3_S2_s);
				msg.addString(player.getName());
				msg.addItemName(newItem.getItemId());
				msg.addNumber(item.getCount());
				_owner.sendPacket(msg);
				
				msg = new SystemMessage(SystemMessage.PURCHASED_S3_S2_s_FROM_S1);
				msg.addString(_owner.getName());
				msg.addItemName(newItem.getItemId());
				msg.addNumber(item.getCount());
				player.sendPacket(msg);
			}
			else
			{
				SystemMessage msg = new SystemMessage(SystemMessage.S1_PURCHASED_S2);
				msg.addString(player.getName());
				msg.addItemName(newItem.getItemId());
				_owner.sendPacket(msg);
				
				msg = new SystemMessage(SystemMessage.PURCHASED_S2_FROM_S1);
				msg.addString(_owner.getName());
				msg.addItemName(newItem.getItemId());
				player.sendPacket(msg);
			}
		}
		
		// Send inventory update packet
		_owner.sendPacket(ownerIU);
		player.sendPacket(playerIU);
		return ok;
	}
	
	/**
	 * Sell items to this PrivateStore list
	 * @param player 
	 * @param items 
	 * @return : boolean true if success
	 */
	public synchronized boolean privateStoreSell(L2PcInstance player, ItemRequest[] items)
	{
		if (_locked)
		{
			return false;
		}
		
		boolean ok = false;
		
		final PcInventory ownerInventory = _owner.getInventory();
		final PcInventory playerInventory = player.getInventory();
		
		// Prepare inventory update packet
		final InventoryUpdate ownerIU = new InventoryUpdate();
		final InventoryUpdate playerIU = new InventoryUpdate();
		
		int totalPrice = 0;
		for (ItemRequest item : items)
		{
			// Searching item in tradelist using itemId
			boolean found = false;

			for (TradeItem ti : _items)
			{
				if (ti.getItem().getItemId() == item.getItemId() && ti.getEnchant() == item.getEnchantLevel())
				{
					// Price should be the same
					if (ti.getPrice() == item.getPrice())
					{
						// If requesting more than available - decrease count
						if (ti.getCount() < item.getCount())
						{
							item.setCount(ti.getCount());
						}
						found = item.getCount() > 0;
					}
					break;
				}
			}
			
			// Not found any item in the trade list with same itemId and price
			// maybe another player already sold this item ?
			if (!found)
			{
				continue;
			}

			// Check for overflow in the single item
			if ((L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID) / item.getCount()) < item.getPrice())
			{
				lock();
				break;
			}

			long tempPrice = totalPrice + item.getCount() * item.getPrice();
			// Check for overflow of the total price
			if (tempPrice > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID) || tempPrice < 0)
			{
				lock();
				break;
			}

			if ((tempPrice + playerInventory.getAdena()) > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID))
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_POCKET_MONEY_LIMIT));
				continue;
			}
			
			if (ownerInventory.getAdena() < tempPrice)
			{
				continue;
			}

			// Check if requested item is available for manipulation
			int objectId = item.getObjectId();
			L2ItemInstance oldItem = player.checkItemManipulation(objectId, item.getCount(), "sell");
			// private store - buy use same objectId for buying several non-stackable items
			if (oldItem == null || oldItem.isEquipped() || oldItem.getEnchantLevel() != item.getEnchantLevel())
			{
				// Searching other items using same itemId
				oldItem = playerInventory.getItemByItemId(item.getItemId());
				if (oldItem == null || oldItem.isEquipped() || oldItem.getEnchantLevel() != item.getEnchantLevel())
				{
					continue;
				}
				objectId = oldItem.getObjectId();
				oldItem = player.checkItemManipulation(objectId, item.getCount(), "sell");
				if (oldItem == null || oldItem.isEquipped() || oldItem.getEnchantLevel() != item.getEnchantLevel())
				{
					continue;
				}
			}
			
			if (!oldItem.isTradeable())
			{
				continue;
			}

			// Proceed with item transfer
			L2ItemInstance newItem = playerInventory.transferItem("PrivateStore", objectId, item.getCount(), ownerInventory, player, _owner);
			if (newItem == null)
			{
				continue;
			}

			removeItem(-1, item.getItemId(), item.getEnchantLevel(), item.getCount());
			ok = true;

			// increase total price only after successful transaction
			totalPrice = (int) tempPrice;

			// Add changes to inventory update packets
			if (oldItem.getCount() > 0 && oldItem != newItem)
			{
				playerIU.addModifiedItem(oldItem);
			}
			else
			{
				playerIU.addRemovedItem(oldItem);
			}
			
			if (newItem.getCount() > item.getCount())
			{
				ownerIU.addModifiedItem(newItem);
			}
			else
			{
				ownerIU.addNewItem(newItem);
			}

			// Send messages about the transaction to both players
			if (newItem.isStackable())
			{
				SystemMessage msg = new SystemMessage(SystemMessage.PURCHASED_S3_S2_s_FROM_S1);
				msg.addString(player.getName());
				msg.addItemName(newItem.getItemId());
				msg.addNumber(item.getCount());
				_owner.sendPacket(msg);
				
				msg = new SystemMessage(SystemMessage.S1_PURCHASED_S3_S2_s);
				msg.addString(_owner.getName());
				msg.addItemName(newItem.getItemId());
				msg.addNumber(item.getCount());
				player.sendPacket(msg);
			}
			else
			{
				SystemMessage msg = new SystemMessage(SystemMessage.PURCHASED_S2_FROM_S1);
				msg.addString(player.getName());
				msg.addItemName(newItem.getItemId());
				_owner.sendPacket(msg);
				
				msg = new SystemMessage(SystemMessage.S1_PURCHASED_S2);
				msg.addString(_owner.getName());
				msg.addItemName(newItem.getItemId());
				player.sendPacket(msg);
			}
		}
		
		if (totalPrice > 0)
		{
			// Transfer adena
			if (totalPrice > ownerInventory.getAdena())
			{
				return false;
			}
			
			final L2ItemInstance adenaItem = ownerInventory.getAdenaInstance();
			ownerInventory.reduceAdena("PrivateStore", totalPrice, _owner, player);
			ownerIU.addItem(adenaItem);
			playerInventory.addAdena("PrivateStore", totalPrice, player, _owner);
			playerIU.addItem(playerInventory.getAdenaInstance());
		}
		
		if (ok)
		{
			// Send inventory update packet
			_owner.sendPacket(ownerIU);
			player.sendPacket(playerIU);
		}
		return ok;
	}
	
	/**
	 * @param objectId
	 * @return
	 */
	public TradeItem getItem(int objectId)
	{
		for (TradeItem item : _items)
		{
			if (item.getObjectId() == objectId)
			{
				return item;
			}
		}
		return null;
	}
}