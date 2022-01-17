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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SummonItemsData;
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.instancemanager.ItemsOnGroundManager;
import net.sf.l2j.gameserver.instancemanager.MercTicketManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.knownlist.NullKnownList;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.DropItem;
import net.sf.l2j.gameserver.network.serverpackets.GetItem;
import net.sf.l2j.gameserver.network.serverpackets.SpawnItem;
import net.sf.l2j.gameserver.skills.funcs.Func;
import net.sf.l2j.gameserver.templates.L2Armor;
import net.sf.l2j.gameserver.templates.L2EtcItem;
import net.sf.l2j.gameserver.templates.L2EtcItemType;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * This class manages items.
 * 
 * @version $Revision: 1.4.2.1.2.11 $ $Date: 2005/03/31 16:07:50 $
 */
public final class L2ItemInstance extends L2Object
{
	private static final Logger _log = Logger.getLogger(L2ItemInstance.class.getName());
	private static final Logger _logItems = Logger.getLogger("item");

	/** Enumeration of locations for item */
	public static enum ItemLocation
    {
		VOID,
		INVENTORY,
		PAPERDOLL,
		WAREHOUSE,
		CLANWH,
		PET,
		PET_EQUIP,
		LEASE,
		FREIGHT,
		NPC
	}
	
	/** ID of the owner */
	private int _ownerId;
	
	/** ID of who dropped the item last, used for knownlist */
	private int _dropperObjectId = 0;
	
	/** Quantity of the item */
	private int _count;

    /** Initial Quantity of the item */
    private int _initCount;

    /** Time after restore Item count (in Hours) */
    private int _time;

    /** Quantity of the item can decrease */
    private boolean _decrease = false;

	/** ID of the item */
	private final int _itemId;
	
	/** Object L2Item associated to the item */
	private final L2Item _item;
	
	/** Location of the item : Inventory, PaperDoll, WareHouse */
	private ItemLocation _loc;
	
	/** Slot where item is stored */
	private int _locData;
	
	/** Level of enchantment of the item */
	private int _enchantLevel;
	
	/** Price of the item for selling */
	private int _priceSell;
	
	/** Price of the item for buying */
	private int _priceBuy;
	
	/** Wear Item */
	private boolean _wear;

	/** Custom item types (used loto, race tickets) */
	private int _type1;
	private int _type2;

	private long _dropTime;
	
	public static final int CHARGED_NONE				=	0;
	public static final int CHARGED_SOULSHOT			=	1;
	public static final int CHARGED_SPIRITSHOT			=	1;
	public static final int CHARGED_BLESSED_SOULSHOT	=	2; // Does it really exist?
	public static final int CHARGED_BLESSED_SPIRITSHOT	=	2;
	
	/** Item charged with SoulShot (type of SoulShot) */
	private int _chargedSoulShot = CHARGED_NONE;
	/** Item charged with SpiritShot (type of SpiritShot) */
	private int _chargedSpiritShot = CHARGED_NONE;  
	
	private boolean _chargedFishShot = false;

    private boolean _protected;

	public static final int UNCHANGED = 0;
	public static final int ADDED = 1;
	public static final int REMOVED = 3;
	public static final int MODIFIED = 2;
	private int _lastChange = 2;	// 1 ??, 2 modified, 3 removed
	private boolean _existsInDb; // if a record exists in DB.
	private boolean _storedInDb; // if DB data is up-to-date.

    private final ReentrantLock _dbLock = new ReentrantLock();

    private ScheduledFuture<?> itemLootSchedule = null;

    private final DropProtection _dropProtection = new DropProtection();

	/**
	 * Constructor of the L2ItemInstance from the objectId and the itemId.
	 * @param objectId : int designating the ID of the object in the world
	 * @param itemId : int designating the ID of the item
	 */
	public L2ItemInstance(int objectId, int itemId)
	{
		super(objectId);
		super.setKnownList(new NullKnownList(this));
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		if (_itemId == 0 || _item == null)
			throw new IllegalArgumentException();
		_count = 1;
		_loc = ItemLocation.VOID;
		_type1 = 0;
		_type2 = 0;
		_dropTime = 0;
	}
	
	/**
	 * Constructor of the L2ItemInstance from the objetId and the description of the item given by the L2Item.
	 * @param objectId : int designating the ID of the object in the world
	 * @param item : L2Item containing informations of the item
	 */
	public L2ItemInstance(int objectId, L2Item item)
	{
		super(objectId);
		super.setKnownList(new NullKnownList(this));
		_itemId = item.getItemId();
		_item = item;
		if (_itemId == 0 || _item == null)
			throw new IllegalArgumentException();
		_count = 1;
		_loc = ItemLocation.VOID;
	}

    /**
     * Remove a L2ItemInstance from the world and send server->client GetItem packets.<BR><BR>
     *
     * <B><U> Actions</U> :</B><BR><BR>
     * <li>Send a Server->Client Packet GetItem to player that pick up and its _knowPlayers member </li>
     * <li>Remove the L2Object from the world</li><BR><BR>
     * 
     * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T REMOVE the object from _allObjects of L2World </B></FONT><BR><BR>
     * 
     * <B><U> Assert </U> :</B><BR><BR>
     * <li> this instanceof L2ItemInstance</li>
     * <li> _worldRegion != null <I>(L2Object is visible at the beginning)</I></li><BR><BR>
     *  
     * <B><U> Example of use </U> :</B><BR><BR>
     * <li> Do Pickup Item : PCInstance and Pet</li><BR><BR>
     * 
     * @param player Player that pick up the item
     * 
     */
    public final void pickupMe(L2Character player)
    {
        if (Config.ASSERT) assert getPosition().getWorldRegion() != null;

        L2WorldRegion oldregion = getPosition().getWorldRegion();
    
        // Create a server->client GetItem packet to pick up the L2ItemInstance
        GetItem gi = new GetItem(this, player.getObjectId());
        player.broadcastPacket(gi);

        synchronized (this)
        {
            getPosition().setWorldRegion(null);
        }

        // if this item is a mercenary ticket, remove the spawns!
    	int itemId = getItemId();

        if (MercTicketManager.getInstance().getTicketCastleId(itemId) > 0)
        {
            MercTicketManager.getInstance().removeTicket(this);
            ItemsOnGroundManager.getInstance().removeObject(this);
        }

        if (itemId == Inventory.ADENA_ID || itemId == 6353)
        {
            L2PcInstance actor = player.getActingPlayer();
	        if (actor != null)
            {
                QuestState qs = actor.getQuestState("255_Tutorial");
                if (qs != null)
                    qs.getQuest().notifyEvent("CE"+itemId+"", null, actor);
            }
        }

        // outside of synchronized, to avoid deadlocks
        // Remove the L2ItemInstance from the world
        L2World.getInstance().removeVisibleObject(this, oldregion);
    }

	/**
	 * Sets the ownerID of the item
	 * @param process : String Identifier of process triggering this action
	 * @param ownerId : int designating the ID of the owner
	 * @param creator : L2PcInstance Player requesting the item creation
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 */
	public void setOwnerId(String process, int ownerId, L2PcInstance creator, L2Object reference)
	{
        setOwnerId(ownerId);

		if (Config.LOG_ITEMS) 
		{
			LogRecord record = new LogRecord(Level.INFO, "CHANGE:" + process);
			record.setLoggerName("item");
			record.setParameters(new Object[]{this, creator, reference});
			_logItems.log(record);
		}
	}

	/**
	 * Sets the ownerID of the item
	 * @param ownerId : int designating the ID of the owner
	 */
	public void setOwnerId(int ownerId)
	{
		if (ownerId == _ownerId)
            return;

		_ownerId = ownerId;
		_storedInDb = false;
	}

	/**
	 * Returns the ownerID of the item
	 * @return int : ownerID of the item
	 */
	public int getOwnerId()
	{
		return _ownerId;
	}

	/**
	 * Sets the location of the item
	 * @param loc : ItemLocation (enumeration)
	 */
	public void setLocation(ItemLocation loc)
	{
		setLocation(loc, 0);
	}

	/**
	 * Sets the location of the item.<BR><BR>
	 * <U><I>Remark :</I></U> If loc and locData different from database, say data not up-to-date
	 * @param loc : ItemLocation (enumeration)
	 * @param locData : int designating the slot where the item is stored or the village for freights
	 */
	public void setLocation(ItemLocation loc, int locData)
	{
		if (loc == _loc && locData == _locData)
			return;
		_loc = loc;
		_locData = locData;
		_storedInDb = false;
	}

	public ItemLocation getLocation()
	{
		return _loc;
	}

	/**
	 * Returns the quantity of item
	 * @return int
	 */
	public int getCount()
	{
		return _count;
	}
	
	public static int getMaxItemCount(int itemId)
	{
		switch (itemId)
		{
			case Inventory.ADENA_ID:	// Adena
			case Inventory.ANCIENT_ADENA_ID:	// Ancient Adena
			case 6360:	// Blue Seal Stone
			case 6361:	// Green Seal Stone
			case 6362:	// Red Seal Stone
				return 2000000000; // 2 billions
		}
		
		return 1000000; // 1 million
	}

	/**
	 * Sets the quantity of the item.
	 * 
	 * @param process : String Identifier of process triggering this action
	 * @param count : int
	 * @param creator : L2PcInstance Player requesting the item creation
	 * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
	 */
	public void changeCount(String process, int count, L2PcInstance creator, L2Object reference)
	{
        if (count == 0)
            return;

        if (count > 0 && _count > getMaxItemCount(_itemId) - count)
            _count = getMaxItemCount(_itemId);
        else
            _count += count;

        if (_count < 0)
            _count = 0;

        _storedInDb = false;
        
        if (Config.LOG_ITEMS && process != null) 
        {
            LogRecord record = new LogRecord(Level.INFO, "CHANGE:" + process);
            record.setLoggerName("item");
            record.setParameters(new Object[]{this, creator, reference});
            _logItems.log(record);
        }
	}

	/**
	 * Sets the quantity of the item.
	 * 
	 * @param count : int
	 */
	public void setCount(int count)
	{
		if (_count == count) return;

		_count = count >= -1 ? count : 0;
		_storedInDb = false;
	}

	/**
	 * Returns if item is equippable 
	 * @return boolean
	 */
	public boolean isEquipable()
	{
		return !(_item.getBodyPart() == 0 || _item instanceof L2EtcItem);
	}

	/**
	 * Returns if item is equipped
	 * @return boolean
	 */
	public boolean isEquipped()
	{
		return _loc == ItemLocation.PAPERDOLL || _loc == ItemLocation.PET_EQUIP;
	}

	/**
	 * Returns the slot where the item is stored
	 * @return int
	 */
	public int getEquipSlot()
	{
		if (Config.ASSERT) assert _loc == ItemLocation.PAPERDOLL || _loc == ItemLocation.PET_EQUIP || _loc == ItemLocation.FREIGHT;
		return _locData;
	}
	
	/**
	 * Returns the characteristics of the item
	 * @return L2Item
	 */
	public L2Item getItem()
	{
		return _item;
	}

	public int getCustomType1()
	{
		return _type1;
	}
	
	public int getCustomType2()
	{
		return _type2;
	}
	
	public void setCustomType1(int newtype)
	{
		_type1 = newtype;
	}
	
	public void setCustomType2(int newtype)
	{
		_type2 = newtype;
	}
	
	public void setDropTime(long time)
	{
		_dropTime = time;
	}
	
	public long getDropTime()
	{
		return _dropTime;
	}
	
	public boolean isWear()
	{
		return _wear;
	}

	public void setWear(boolean newwear)
	{
		_wear=  newwear;
	}

	/**
	 * Returns the type of item
	 * @return Enum
	 */
	public Enum<?> getItemType()
	{
		return _item.getItemType();
	}

	/**
	 * Returns the ID of the item
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}

    /**
     * Returns the quantity of crystals for crystallization
     * @return int
     */
    public final int getCrystalCount()
    {
        return _item.getCrystalCount(_enchantLevel);
    }

	/**
	 * Returns the reference price of the item
	 * @return int
	 */
	public int getReferencePrice()
	{
		return _item.getReferencePrice();
	}

	/**
	 * Returns the name of the item
	 * @return String
	 */
	public String getItemName()
	{
		return _item.getName();
	}

	/**
	 * Returns the price of the item for selling
	 * @return int
	 */
	public int getPriceToSell()
	{
        return (isConsumable() ? (int)(_priceSell * Config.RATE_CONSUMABLE_COST) : _priceSell);
	}
	
	/**
	 * Sets the price of the item for selling.
	 * 
	 * @param price : int designating the price
	 */
	public void setPriceToSell(int price)
	{
		_priceSell = price;
		_storedInDb = false;
	}
	
	/**
	 * Returns the price of the item for buying
	 * @return int
	 */
	public int getPriceToBuy()
	{
        return (isConsumable() ? (int)(_priceBuy * Config.RATE_CONSUMABLE_COST) : _priceBuy);
	}
	
	/**
	 * Sets the price of the item for buying.
	 * 
	 * @param price : int
	 */
	public void setPriceToBuy(int price)
	{
		_priceBuy = price;
		_storedInDb = false;
	}

	/**
	 * Returns the last change of the item
	 * @return int
	 */
	public int getLastChange()
	{
		return _lastChange;
	}

	/**
	 * Sets the last change of the item
	 * @param lastChange : int
	 */
	public void setLastChange(int lastChange)
	{
		_lastChange = lastChange;
	}

	/**
	 * Returns if item is stackable
	 * @return boolean
	 */
	public boolean isStackable()
	{
		return _item.isStackable();
	}

    /**
     * Returns if item is dropable
     * @return boolean
     */
    public boolean isDropable()
    {
        return _item.isDropable();
    }

    /**
     * Returns if item is destroyable
     * @return boolean
     */
    public boolean isDestroyable()
    {
        return _item.isDestroyable();
    }

    /**
     * Returns if item is tradeable
     * @return boolean
     */
    public boolean isTradeable()
    {
        return _item.isTradeable();
    }
	
    /**
     * Returns if item is a hero item
     * @return boolean
     */
    public boolean isHeroItem()
    {
        return _item.isHeroItem();
    }
    
    /**
     * Returns if item is consumable
     * @return boolean
     */
    public boolean isConsumable()
    {
        return _item.isConsumable();
    }
    
    public boolean isAvailable(L2PcInstance player, boolean allowAdena)
    {
    	return isAvailable(player, allowAdena, false);
    }
    
    /**
     * Returns if item is available for manipulation.
     * 
     * @param player 
     * @param allowAdena 
     * @param allowEquipped 
     * @return boolean
     */
    public boolean isAvailable(L2PcInstance player, boolean allowAdena, boolean allowEquipped)
    {
    	return ((allowEquipped || !isEquipped()) // Not equipped (conditionally)
    		&& (getItem().getType2() != 3) // Not Quest Item
    		&& (getItem().getType2() != 4 || getItem().getType1() != 1) // TODO: what does this mean?
    		&& (player.getPet() == null || getObjectId() != player.getPet().getControlItemObjectId()) // Not Control item of currently summoned pet
    		&& (player.getActiveEnchantItem() != this) // Not momentarily used enchant scroll
    		&& (allowAdena || _itemId != Inventory.ADENA_ID)
    		&& (player.getCurrentSkill() == null || player.getCurrentSkill().getSkill().getItemConsumeId() != _itemId)
    		&& (isTradeable()));
    }

    /* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#onAction(net.sf.l2j.gameserver.model.L2PcInstance)
	 * also check constraints: only soloing castle owners may pick up mercenary tickets of their castle 
	 */
	@Override
	public void onAction(L2PcInstance player)
	{
		// this causes the validate position handler to do the pickup if the location is reached.
		// mercenary tickets can only be picked up by the castle owner.
		int castleId = MercTicketManager.getInstance().getTicketCastleId(_itemId);
        if (castleId > 0 && (!player.isCastleLord(castleId) || player.isInParty()))
        {
            if (player.isInParty())    // Do not allow owner who is in party to pick tickets up
                player.sendMessage("You cannot pickup mercenaries while in party.");
            else
                player.sendMessage("Only the castle lord can pickup mercenaries.");

            player.setTarget(this);
            player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            // Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
            player.sendPacket(new ActionFailed());
		}
		else
			player.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, this);
	}
	/**
	 * Returns the level of enchantment of the item.
	 * 
	 * @return int
	 */
	public int getEnchantLevel()
	{
		// Handling for pet collars that haven't been used yet
		if (_enchantLevel == 0 && _item.getItemType() == L2EtcItemType.PET_COLLAR)
		{
			L2SummonItem summonItem = SummonItemsData.getInstance().getSummonItem(getItemId());
			if (summonItem != null && summonItem.isPetSummon())
			{
				L2NpcTemplate npcTemplate = NpcTable.getInstance().getTemplate(summonItem.getNpcId());
				if (npcTemplate != null)
				{
					_enchantLevel = npcTemplate.level;
				}
			}
		}
		return _enchantLevel;
	}
	
	/**
	 * Sets the level of enchantment of the item
	 * @param enchantLevel 
	 */
	public void setEnchantLevel(int enchantLevel)
	{
		if (_enchantLevel == enchantLevel)
			return;

		_enchantLevel = enchantLevel;
		_storedInDb = false;
	}

	/**
	 * Returns the physical defense of the item
	 * @return int
	 */
	public int getPDef()
	{
		if (_item instanceof L2Armor)
			return ((L2Armor)_item).getPDef();
		return 0;
	}

	/**
	 * Returns false cause item can't be attacked
	 * @return boolean false
	 */
    @Override
	public boolean isAutoAttackable(L2Character attacker)
    {
        return false;
    }
    
	/**
	 * Returns the type of charge with SoulShot of the item. 
	 * @return int (CHARGED_NONE, CHARGED_SOULSHOT)
	 */
	public int getChargedSoulShot()
	{
		return 	_chargedSoulShot;	
	}
	
	/**
	 * Returns the type of charge with SpiritShot of the item
	 * @return int (CHARGED_NONE, CHARGED_SPIRITSHOT, CHARGED_BLESSED_SPIRITSHOT)
	 */
	public int getChargedSpiritShot()
	{
		return _chargedSpiritShot;		
	}
	public boolean getChargedFishShot()
	{
		return _chargedFishShot;		
	}
	
	/**
	 * Sets the type of charge with SoulShot of the item
	 * @param type : int (CHARGED_NONE, CHARGED_SOULSHOT)
	 */
	public void setChargedSoulShot(int type) 
	{
		_chargedSoulShot = type;
	}
	
	/**
	 * Sets the type of charge with SpiritShot of the item
	 * @param type : int (CHARGED_NONE, CHARGED_SPIRITSHOT, CHARGED_BLESSED_SPIRITSHOT)
	 */
	public void setChargedSpiritShot(int type) 
	{
		_chargedSpiritShot = type;
	}
	public void setChargedFishShot(boolean type) 
	{
		_chargedFishShot = type;
	}

    /** 
     * This function basically returns a set of functions from
     * L2Item/L2Armor/L2Weapon, but may add additional
     * functions, if this particular item instance is enhanced
     * for a particular player.
     * @param player : L2Character designating the player
     * @return Func[]
     */
    public Func[] getStatFuncs(L2Character player)
    {
    	return getItem().getStatFuncs(this, player);
    }
	
    /**
     * Updates the database.<BR>
     */
    public void updateDatabase()
    {
        updateDatabase(false);
    }

    /**
     * Updates the database.<BR>
     *
     * @param force if the update should necessarily be done.
     */
    public void updateDatabase(boolean force)
    {
        if (isWear()) // avoid saving wear items
            return;

        _dbLock.lock();
        try
        {
            if (_existsInDb)
            {
                if (_ownerId == 0 || _loc==ItemLocation.VOID || (_count == 0 && _loc != ItemLocation.LEASE))
                    removeFromDb();
                else if (!Config.LAZY_ITEMS_UPDATE || force)
                    updateInDb();
            }
            else
            {
                if (_count == 0 && _loc != ItemLocation.LEASE)
                    return;

                if (_loc == ItemLocation.VOID || _loc == ItemLocation.NPC || _ownerId == 0)
                    return;

                insertIntoDb();
            }
        }
        finally
        {
            _dbLock.unlock();
        }
    }
	
    /**
     * Returns a L2ItemInstance stored in database from its objectID
     * @param objectId : int designating the objectID of the item
     * @return L2ItemInstance
     */
    public static L2ItemInstance restoreFromDb(int objectId)
    {
        L2ItemInstance inst = null;
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT owner_id, object_id, item_id, count, enchant_level, loc, loc_data, price_sell, price_buy, custom_type1, custom_type2 FROM items WHERE object_id = ?"))
        {
            statement.setInt(1, objectId);
            try (ResultSet rs = statement.executeQuery())
            {
				if (rs.next())
	            {
					int ownerId = rs.getInt("owner_id");
					int itemId = rs.getInt("item_id");
					int count = rs.getInt("count");
					ItemLocation loc = ItemLocation.valueOf(rs.getString("loc"));
					int locData = rs.getInt("loc_data");
					int enchantLevel = rs.getInt("enchant_level");
					int customType1 =  rs.getInt("custom_type1");
					int customType2 =  rs.getInt("custom_type2");
					int priceSell = rs.getInt("price_sell");
					int priceBuy = rs.getInt("price_buy");
	
					L2Item item = ItemTable.getInstance().getTemplate(itemId);
	                if (item != null)
	                {
					    inst = new L2ItemInstance(objectId, item);
					    inst._ownerId = ownerId;
					    inst._count = count > getMaxItemCount(itemId) ? getMaxItemCount(itemId) : count;
					    inst._enchantLevel = enchantLevel;
					    inst._type1 = customType1;
					    inst._type2 = customType2;
					    inst._loc = loc;
					    inst._locData = locData;
					    inst._priceSell = priceSell;
					    inst._priceBuy  = priceBuy;
                        inst._existsInDb = true;
					    inst._storedInDb = true;
	                }
	                else
	                    _log.severe("Item item_id="+itemId+" not known, object_id="+objectId);
				}
	            else
	            {
					_log.severe("Item object_id="+objectId+" not found");
				}
            }
        }
        catch (Exception e)
        {
            _log.log(Level.SEVERE, "Could not restore item "+objectId+" from DB:", e);
            return null;
        }
        return inst;
    }

    public void changeCountWithoutTrace(int count, L2PcInstance creator, L2Object reference)
    {
        changeCount(null, count, creator, reference);
    }

    /**
     * Init a dropped L2ItemInstance and add it in the world as a visible object.<BR><BR>
     *
     * <B><U> Actions</U> :</B><BR><BR>
     * <li>Set the x,y,z position of the L2ItemInstance dropped and update its _worldregion </li>
     * <li>Add the L2ItemInstance dropped to _visibleObjects of its L2WorldRegion</li>
     * <li>Add the L2ItemInstance dropped in the world as a <B>visible</B> object</li><BR><BR>
     * 
     * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T ADD the object to _allObjects of L2World </B></FONT><BR><BR>
     * 
     * <B><U> Assert </U> :</B><BR><BR>
     * <li> _worldRegion == null <I>(L2Object is invisible at the beginning)</I></li><BR><BR>
     *  
     * <B><U> Example of use </U> :</B><BR><BR>
     * <li> Drop item</li>
     * <li> Call Pet</li><BR>
     * 
     */
    public class ItemDropTask implements Runnable
	{
		private final L2Character _dropper;
		private final L2ItemInstance _item;
		private int _x,_y,_z;

		public ItemDropTask(L2ItemInstance item, L2Character dropper, int x, int y, int z)
		{
			_item = item;
			_dropper = dropper;
			_x = x;
			_y = y;
			_z = z;
		}

		@Override
		public final void run()
		{
			if (Config.ASSERT)
				assert _item.getPosition().getWorldRegion() == null;

			if (_dropper != null)
			{
				Location dropDest = GeoData.getInstance().moveCheck(_dropper.getX(), _dropper.getY(), _dropper.getZ(), _x, _y, _z);
				_x = dropDest.getX();
				_y = dropDest.getY();
				_z = dropDest.getZ();
			}

			synchronized (_item)
			{
				// Set the x,y,z position of the L2ItemInstance dropped and update its _worldregion
				_item.getPosition().setWorldPosition(_x, _y, _z);
				_item.getPosition().setWorldRegion(L2World.getInstance().getRegion(getPosition().getWorldPosition()));
			}

			// Add the L2ItemInstance dropped to _visibleObjects of its L2WorldRegion
			_item.getPosition().getWorldRegion().addVisibleObject(_item);
			_item.setDropTime(System.currentTimeMillis());
			_item.setDropperObjectId(_dropper != null ? _dropper.getObjectId() : 0); //Set the dropper Id for the knownlist packets in sendInfo

			// This can synchronize on others instances, so it's out of
			// synchronized, to avoid deadlocks
			// Add the L2ItemInstance dropped in the world as a visible object
			L2World.getInstance().addVisibleObject(_item, _item.getPosition().getWorldRegion());
			if (Config.SAVE_DROPPED_ITEM)
			{
				ItemsOnGroundManager.getInstance().save(_item);
			}
			_item.setDropperObjectId(0); //Set the dropper Id back to 0 so it no longer shows the drop packet
		}
	}
    
    public final void dropMe(L2Character dropper, int x, int y, int z)
	{
		ThreadPoolManager.getInstance().executeTask(new ItemDropTask(this, dropper, x, y, z));
	}

    /**
     * Update the database with values of the item
     */
    private void updateInDb()
    {
        if (Config.ASSERT) assert _existsInDb;

        if (_wear)
            return;

        if (_storedInDb)
            return;

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE items SET owner_id=?,count=?,loc=?,loc_data=?,enchant_level=?,price_sell=?,price_buy=?,custom_type1=?,custom_type2=? " + "WHERE object_id = ?"))
        {
            statement.setInt(1, _ownerId);
            statement.setInt(2, getCount());
            statement.setString(3, _loc.name());
            statement.setInt(4, _locData);
            statement.setInt(5, getEnchantLevel());
            statement.setInt(6, _priceSell);
            statement.setInt(7, _priceBuy);
            statement.setInt(8, getCustomType1());
            statement.setInt(9, getCustomType2());
            statement.setInt(10, getObjectId());
            statement.executeUpdate();
            _existsInDb = true;
            _storedInDb = true;
        }
        catch (Exception e)
        {
            _log.log(Level.SEVERE, "Could not update item "+getObjectId()+" in DB: Reason: "+e.getMessage(), e);
        }
    }

	/**
	 * Insert the item in database
	 */
	private void insertIntoDb()
    {
		if (_wear)
			return;

		if (Config.ASSERT) assert !_existsInDb && getObjectId() != 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO items (owner_id,item_id,count,loc,loc_data,enchant_level,price_sell,price_buy,object_id,custom_type1,custom_type2) VALUES (?,?,?,?,?,?,?,?,?,?,?)"))
		{	
			statement.setInt(1, _ownerId);
			statement.setInt(2, _itemId);
			statement.setInt(3, getCount());
			statement.setString(4, _loc.name());
			statement.setInt(5, _locData);
			statement.setInt(6, getEnchantLevel());
			statement.setInt(7, _priceSell);
			statement.setInt(8, _priceBuy);
			statement.setInt(9, getObjectId());
			statement.setInt(10, _type1);
			statement.setInt(11, _type2);
			
			statement.executeUpdate();
			_existsInDb = true;
			_storedInDb = true;
        }
        catch (Exception e)
        {
            _log.log(Level.SEVERE, "Could not insert item "+getObjectId()+" into DB: Reason: "+e.getMessage(), e);
        }
	}

    /**
     * Delete item from database
     */
    private void removeFromDb()
    {
        if (_wear)
            return;

        if (Config.ASSERT) assert _existsInDb;

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM items WHERE object_id=?"))
        {
            statement.setInt(1, getObjectId());
            statement.executeUpdate();
            _existsInDb = false;
            _storedInDb = false;
        }
        catch (Exception e)
        {
            _log.log(Level.SEVERE, "Could not delete item "+getObjectId()+" in DB: "+e.getMessage(), e);
        }
    }
    
	/**
	 * Returns the item in String format.
	 * 
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "" + _item;
	}

    public void resetOwnerTimer()
    {
        if (itemLootSchedule != null)
            itemLootSchedule.cancel(true);
        itemLootSchedule = null;
    }

    public void setItemLootSchedule(ScheduledFuture<?> sf)
    {
        itemLootSchedule = sf;
    }

    public ScheduledFuture<?> getItemLootSchedule()
    {
        return itemLootSchedule;
    }

    public void setProtected(boolean isProtected)
    {
        _protected = isProtected;
    }

    public boolean isProtected()
    {
        return _protected;
    }

    public void setCountDecrease(boolean decrease)
    {
        _decrease = decrease;
    }

    public boolean getCountDecrease()
    {
        return _decrease;
    }

    public void setInitCount(int InitCount)
    {
        _initCount = InitCount;
    }

    public int getInitCount()
    {
        return _initCount;
    }

    public void restoreInitCount()
    {
        if (_decrease)
            _count = _initCount;
    }

    public void setTime(int time)
    {
        if (time > 0)
            _time = time;
        else
            _time = 0;
    }

    public void setRestoreTime(long savedTimer)
    {
        long remainingTime = savedTimer - System.currentTimeMillis();
        if (remainingTime < 0)
            remainingTime = 0;

        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable()
        {
            @Override
			public void run()
            {
                restoreInitCount();
                dataTimerSave();
            }
        }, remainingTime, (long)getTime()*60*60*1000);
    }

    protected void dataTimerSave()
    {
        long timerSave = System.currentTimeMillis()+(long)getTime()*60*60*1000;
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE merchant_buylists SET savetimer =? WHERE time =?"))
        {
            statement.setLong(1, timerSave);
            statement.setInt(2, getTime());
            statement.executeUpdate();
        }
        catch (Exception e)
        {
            _log.log(Level.SEVERE, "TradeController: Could not update Timer save in Buylist" );
        }
    }

    public int getTime()
    {
        return _time;
    }

    public final DropProtection getDropProtection()
    {
        return _dropProtection;
    }
    
    public final boolean isWeapon()
    {
    	return _item.getType2() == L2Item.TYPE2_WEAPON;
    }
    
    public void setDropperObjectId(int id)
    {
    	_dropperObjectId = id;
    }
    
    @Override
    public void sendInfo(L2PcInstance activeChar)
    {
    	if (_dropperObjectId != 0)
            activeChar.sendPacket(new DropItem(this, _dropperObjectId));
        else
            activeChar.sendPacket(new SpawnItem(this));
    }
}