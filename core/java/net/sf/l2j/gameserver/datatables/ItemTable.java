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
package net.sf.l2j.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.document.DocumentEngine;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ItemInstance.ItemLocation;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.templates.L2Armor;
import net.sf.l2j.gameserver.templates.L2EtcItem;
import net.sf.l2j.gameserver.templates.L2EtcItemType;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;

/**
 * This class ...
 * 
 * @version $Revision: 1.9.2.6.2.9 $ $Date: 2005/04/02 15:57:34 $
 */
public class ItemTable
{
	private static Logger _log = Logger.getLogger(ItemTable.class.getName());
	private static Logger _logItems = Logger.getLogger("item");
	
	private L2Item[] _allTemplates;
	private final Map<Integer, L2EtcItem> _etcItems = new ConcurrentHashMap<>();
	private final Map<Integer, L2Armor>   _armors = new ConcurrentHashMap<>();
	private final Map<Integer, L2Weapon>  _weapons = new ConcurrentHashMap<>();
	
    public static ItemTable getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private ItemTable()
    {
    	load();
    }
    
    public void load()
    {
        int highestId = 0;

    	_etcItems.clear();
        _armors.clear();
        _weapons.clear();
        
        for (L2Item item : DocumentEngine.getInstance().loadItems()) {
			if (highestId < item.getItemId()) {
				highestId = item.getItemId();
			}
			if (item instanceof L2EtcItem) {
				_etcItems.put(item.getItemId(), (L2EtcItem) item);
			} else if (item instanceof L2Armor) {
				_armors.put(item.getItemId(), (L2Armor) item);
			} else {
				_weapons.put(item.getItemId(), (L2Weapon) item);
			}
		}

        buildFastLookupTable(highestId);

        _log.info(getClass().getSimpleName() + ": Loaded " + _etcItems.size() + " Etc items.");
		_log.info(getClass().getSimpleName() + ": Loaded " + _armors.size() + " Armor items.");
		_log.info(getClass().getSimpleName() + ": Loaded " + _weapons.size() + " Weapon items.");
		_log.info(getClass().getSimpleName() + ": Loaded " + (_etcItems.size() + _armors.size() + _weapons.size()) + " items in total.");
    }

    /**
     * Builds a variable in which all items are putting in in function of their ID.
     * 
     * @param size
     */
    private void buildFastLookupTable(int size)
    {
		_log.fine("Highest item ID used: " + size);
		_allTemplates = new L2Item[size + 1];
		
		// Insert armor item in Fast Look Up Table
		for (L2Armor item : _armors.values()) {
			_allTemplates[item.getItemId()] = item;
		}
		
		// Insert weapon item in Fast Look Up Table
		for (L2Weapon item : _weapons.values()) {
			_allTemplates[item.getItemId()] = item;
		}
		
		// Insert etcItem item in Fast Look Up Table
		for (L2EtcItem item : _etcItems.values()) {
			_allTemplates[item.getItemId()] = item;
		}
    }

	/**
	 * Returns the item corresponding to the item ID
	 * @param id : int designating the item
	 * @return L2Item
	 */
	public L2Item getTemplate(int id)
	{
		if (id >= _allTemplates.length || id < 0)
			return null;
		return _allTemplates[id];
	}

	/**
	 * Create the L2ItemInstance corresponding to the Item Identifier and quantitiy add logs the activity.<BR><BR>
	 * 
	 * <B><U> Actions</U> :</B><BR><BR>
	 * <li>Create and Init the L2ItemInstance corresponding to the Item Identifier and quantity </li>
	 * <li>Add the L2ItemInstance object to _allObjects of L2world </li>
	 * <li>Logs Item creation according to log settings</li><BR><BR>
	 * 
	 * @param process : String Identifier of process triggering this action
     * @param itemId : int Item Identifier of the item to be created
     * @param count : int Quantity of items to be created for stackable items
	 * @param actor : L2PcInstance Player requesting the item creation
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
     * @return L2ItemInstance corresponding to the new item
	 */
	public L2ItemInstance createItem(String process, int itemId, int count, L2PcInstance actor, L2Object reference)
	{
		// Create and Init the L2ItemInstance corresponding to the Item Identifier
		L2ItemInstance item = new L2ItemInstance(IdFactory.getInstance().getNextId(), itemId);

        if (process.equalsIgnoreCase("loot"))
        {
            ScheduledFuture<?> itemLootShedule;
            if (reference != null && reference instanceof L2Attackable && ((L2Attackable)reference).isRaid() && !Config.AUTO_LOOT_RAIDS)
            {
                item.setOwnerId(actor.getObjectId());
                itemLootShedule = ThreadPoolManager.getInstance().scheduleGeneral(new ResetOwner(item), 15000);
                item.setItemLootSchedule(itemLootShedule);
            }
            else if (!Config.AUTO_LOOT)
            {
                item.setOwnerId(actor.getObjectId());
                itemLootShedule = ThreadPoolManager.getInstance().scheduleGeneral(new ResetOwner(item), 15000);
                item.setItemLootSchedule(itemLootShedule);
            }
        }

		if (Config.DEBUG)
            _log.fine(getClass().getSimpleName() + ": Item created, object ID: " + item.getObjectId()+ " item ID: " + itemId);
		
		// Add the L2ItemInstance object to _allObjects of L2world
		L2World.getInstance().storeObject(item);

		// Set Item parameters
		if (item.isStackable() && count > 1) item.setCount(count);
		
		if (Config.LOG_ITEMS) 
		{
			LogRecord record = new LogRecord(Level.INFO, "CREATE:" + process);
			record.setLoggerName("item");
			record.setParameters(new Object[]{item, actor, reference});
			_logItems.log(record);
		}
        
		return item; 	
	}

	public L2ItemInstance createItem(String process, int itemId, int count, L2PcInstance actor)
	{
		return createItem(process, itemId, count, actor, null);
	}
	
	/**
	 * Returns a dummy item.<BR><BR>
	 * <U><I>Concept :</I></U><BR>
	 * Dummy item is created by setting the ID of the object in the world at null value 
	 * @param itemId : integer designating the item
	 * @return L2ItemInstance designating the dummy item created
	 */
	public L2ItemInstance createDummyItem(int itemId)
	{
		L2ItemInstance temp = null;

		try
		{
			temp = new L2ItemInstance(0, itemId);
		} 
		catch (Exception e)
		{
			_log.log(Level.SEVERE, getClass().getSimpleName() + ": Item template missing for ID: " + itemId, e);
		}
		
		return temp; 	
	}

	/**
	 * Destroys the L2ItemInstance.<BR><BR>
	 * 
	 * <B><U> Actions</U> :</B><BR><BR>
	 * <li>Sets L2ItemInstance parameters to be unusable </li>
	 * <li>Removes the L2ItemInstance object to _allObjects of L2world </li>
	 * <li>Logs Item deletion according to log settings</li><BR><BR>
	 * 
	 * @param process : String Identifier of process triggering this action
     * @param item : integer The item to be destroyed
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 */
	public void destroyItem(String process, L2ItemInstance item, L2PcInstance actor, L2Object reference)
	{
		synchronized (item)
		{
	    	item.setCount(0);
	        item.setOwnerId(0);
	        item.setLocation(ItemLocation.VOID);
	        item.setLastChange(L2ItemInstance.REMOVED);

	        L2World.getInstance().removeObject(item);
			IdFactory.getInstance().releaseId(item.getObjectId());

			if (Config.LOG_ITEMS) 
			{
				LogRecord record = new LogRecord(Level.INFO, "DELETE: " + process);
				record.setLoggerName("item");
				record.setParameters(new Object[]{item, actor, reference});
				_logItems.log(record);
			}

			// if it's a pet control item, delete the pet as well
			if (item.getItem().getItemType() == L2EtcItemType.PET_COLLAR)
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?"))
				{
					// Delete the pet in db
					statement.setInt(1, item.getObjectId());
					statement.execute();
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "could not delete pet objectid:", e);
				}
			}
		}
	}

    protected class ResetOwner implements Runnable
    {
        L2ItemInstance _item;
        
        public ResetOwner(L2ItemInstance item)
        {
            _item = item;
        }

        @Override
		public void run()
        {
            _item.setOwnerId(0);
            _item.setItemLootSchedule(null);
        }
    }
    
    private static class SingletonHolder
	{
		protected static final ItemTable _instance = new ItemTable();
	}
}