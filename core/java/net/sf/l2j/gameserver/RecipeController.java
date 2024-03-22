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
package net.sf.l2j.gameserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ManufactureItem;
import net.sf.l2j.gameserver.model.L2RecipeInstance;
import net.sf.l2j.gameserver.model.L2RecipeList;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.RecipeBookItemList;
import net.sf.l2j.gameserver.network.serverpackets.RecipeItemMakeInfo;
import net.sf.l2j.gameserver.network.serverpackets.RecipeShopItemInfo;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

public class RecipeController
{
	protected static final Logger _log = Logger.getLogger(RecipeController.class.getName());
	
	private Map<Integer, L2RecipeList> _lists;
	protected final Map<L2PcInstance, RecipeItemMaker> _activeMakers = Collections.synchronizedMap(new WeakHashMap<>());
	
	public static RecipeController getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public RecipeController()
	{
		_lists = new HashMap<>();
		String line = null;

		File recipesData = new File(Config.DATAPACK_ROOT, "data/recipes.csv");
		try (FileReader fr = new FileReader(recipesData);
            BufferedReader br = new BufferedReader(fr);
            LineNumberReader lnr = new LineNumberReader(br))
		{
			while ((line = lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
					continue;
				
				parseList(line);
			}
			_log.config("RecipeController: Loaded " + _lists.size() + " Recipes.");
		}
		catch (Exception e)
		{
            _log.warning("Error loading recipes.");
		}
	}
	
	public int getRecipesCount()
	{
		return _lists.size();
	}
	
	public L2RecipeList getRecipeList(int listId)
	{
		return _lists.get(listId);
	}
	
	public L2RecipeList getRecipeByItemId(int itemId)
	{
		for (L2RecipeList list : _lists.values())
		{
			if (list.getRecipeId() == itemId)
			{
				return list;
			}
		}
		return null;
	}
    
    public L2RecipeList getRecipeById(int recId)
    {
        for (L2RecipeList list : _lists.values())
        {
            if (list.getId() == recId)
            {
                return list;
            }
        }
        return null;
    }
	
	public synchronized void requestBookOpen(L2PcInstance player, boolean isDwarvenCraft)
	{
		RecipeItemMaker maker = null;
		if (Config.ALT_GAME_CREATION)
            maker = _activeMakers.get(player);
		
		if (maker == null)
		{
			RecipeBookItemList response = new RecipeBookItemList(isDwarvenCraft, player.getMaxMp());
			response.addRecipes(isDwarvenCraft ? player.getDwarvenRecipeBook() : player.getCommonRecipeBook());
			player.sendPacket(response);
			return;
		}
		
		SystemMessage sm = new SystemMessage(SystemMessage.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING);
		player.sendPacket(sm);
		return;
	}
	
	public synchronized void requestMakeItemAbort(L2PcInstance player)
	{
		_activeMakers.remove(player);
	}
	
	public synchronized void requestManufactureItem(L2PcInstance manufacturer, int recipeListId, L2PcInstance player)
	{
		L2RecipeList recipeList = getValidRecipeList(player, recipeListId);
		
		if (recipeList == null) return;

        List<L2RecipeList> dwarfRecipes = Arrays.asList(manufacturer.getDwarvenRecipeBook());
        List<L2RecipeList> commonRecipes = Arrays.asList(manufacturer.getCommonRecipeBook());

        if (!dwarfRecipes.contains(recipeList) && !commonRecipes.contains(recipeList))
        {
            Util.handleIllegalPlayerAction(player,"Warning!! Character "+player.getName()+" of account "+player.getAccountName()+" sent a false recipe id.",Config.DEFAULT_PUNISH);
            return;
        }

		RecipeItemMaker maker = _activeMakers.get(manufacturer);
		if (Config.ALT_GAME_CREATION && maker != null) // check if busy
		{
			player.sendMessage("Manufacturer is busy, please try later.");
			return;
		}
		
		maker = new RecipeItemMaker(manufacturer, recipeList, player);
		if (maker._isValid)
		{
			if (Config.ALT_GAME_CREATION)
			{
				_activeMakers.put(manufacturer, maker);
				ThreadPoolManager.getInstance().scheduleGeneral(maker, 100);
			}
			else
				maker.run();
		}
	}
	
	public synchronized void requestMakeItem(L2PcInstance player, int recipeListId)
	{
		L2RecipeList recipeList = getValidRecipeList(player, recipeListId);
		
		if (recipeList == null)	return;

        List<L2RecipeList> dwarfRecipes = Arrays.asList(player.getDwarvenRecipeBook());
        List<L2RecipeList> commonRecipes = Arrays.asList(player.getCommonRecipeBook());

        if (!dwarfRecipes.contains(recipeList) && !commonRecipes.contains(recipeList))
        {
            Util.handleIllegalPlayerAction(player,"Warning!! Character "+player.getName()+" of account "+player.getAccountName()+" sent a false recipe id.",Config.DEFAULT_PUNISH);
            return;
        }

		RecipeItemMaker maker = _activeMakers.get(player);

		// Check if already busy (possible in alt mode only)
		if (Config.ALT_GAME_CREATION && maker != null) 
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1_S2);
			sm.addItemName(recipeList.getItemId());
			sm.addString("You are busy creating");
			player.sendPacket(sm);
			return;
		}
		
		maker = new RecipeItemMaker(player, recipeList, player);
		if (maker._isValid)
		{
			if (Config.ALT_GAME_CREATION)
			{
				_activeMakers.put(player, maker);
				ThreadPoolManager.getInstance().scheduleGeneral(maker, 100);
			}
			else
			{
				maker.run();
			}
		}
	}
	
	private void parseList(String line)
	{
		try
		{
			StringTokenizer st = new StringTokenizer(line, ";");
			List<L2RecipeInstance> recipePartList = new ArrayList<>();
			
			//we use common/dwarf for easy reading of the recipes.csv file 
			String recipeTypeString = st.nextToken();
			
			// now parse the string into a boolean 
			boolean isDwarvenRecipe;
			
			if (recipeTypeString.equalsIgnoreCase("dwarven"))
				isDwarvenRecipe = true;
			else if (recipeTypeString.equalsIgnoreCase("common"))
				isDwarvenRecipe = false;
			else
			{
				//prints a helpful message 
				_log.warning("Error parsing recipes.csv, unknown recipe type " + recipeTypeString);
				return;
			}
			
			String recipeName = st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			int recipeId = Integer.parseInt(st.nextToken());
			int level = Integer.parseInt(st.nextToken());
			
			// material
			StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
			while (st2.hasMoreTokens())
			{
				StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
				int rpItemId = Integer.parseInt(st3.nextToken());
				int quantity = Integer.parseInt(st3.nextToken());
				L2RecipeInstance rp = new L2RecipeInstance(rpItemId, quantity);
				recipePartList.add(rp);
			}
			
			int itemId = Integer.parseInt(st.nextToken());
			int count = Integer.parseInt(st.nextToken());
			
			// NPC fee (can be used for future custom mod)
			st.nextToken();
			
			int mpCost = Integer.parseInt(st.nextToken());
			int successRate = Integer.parseInt(st.nextToken());
			
			L2RecipeList recipeList = new L2RecipeList(id, level, recipeId, recipeName, successRate, mpCost, itemId, count, isDwarvenRecipe);
			for (L2RecipeInstance recipePart : recipePartList)
			{
				recipeList.addRecipe(recipePart);
			}
			_lists.put(Integer.valueOf(_lists.size()), recipeList);
		}
		catch (Exception e)
		{
			_log.severe("Exception in RecipeController.parseList() - " + e);
		}
	}
	
	private class RecipeItemMaker implements Runnable
	{
		protected boolean _isValid;
		protected List<TempItem> _items = null;
		protected final L2RecipeList _recipeList;
		protected final L2PcInstance _player; // "crafter"
		protected final L2PcInstance _target; // "customer"		
		protected final L2Skill _skill;
		protected final int _skillId;
		protected final int _skillLevel;
		protected double _creationPasses;
		protected double _manaRequired;
		protected int _price;
		protected int _totalItems;
		protected int _delay;
		
		public RecipeItemMaker(L2PcInstance pPlayer, L2RecipeList pRecipeList, L2PcInstance pTarget)
		{
			_player = pPlayer;
			_target = pTarget;
			_recipeList = pRecipeList;
			
			_isValid = false;
			_skillId = _recipeList.isDwarvenRecipe() ? L2Skill.SKILL_CREATE_DWARVEN : L2Skill.SKILL_CREATE_COMMON;
			_skillLevel = _player.getSkillLevel(_skillId);
			_skill = _player.getKnownSkill(_skillId);
			
			_player.isInCraftMode(true);
			
			if (_player.isAlikeDead())
			{
				_player.sendPacket(new ActionFailed());
				abort();
				return;
			}
			
			if (_target.isAlikeDead())
			{
				_target.sendPacket(new ActionFailed());
				abort();
				return;
			}
			
			if (_target.isProcessingTransaction())
			{
				_target.sendMessage("You are busy.");
				_target.sendPacket(new ActionFailed());
				abort();
				return;
			}

			if (_player.isProcessingTransaction())
			{
				if (_player != _target)
				{
					_target.sendMessage("Manufacturer " + _player.getName() + " is busy.");	
				}
				_player.sendPacket(new ActionFailed());
				abort();
				return;
			}
			
			// validate recipe list
			if (_recipeList == null || _recipeList.getRecipes().length == 0)
			{
				_player.sendMessage("No such recipe.");
				_player.sendPacket(new ActionFailed());
				abort();
				return;
			}
			
			_manaRequired = _recipeList.getMpCost();
			
			// validate skill level
			if (_recipeList.getLevel() > _skillLevel)
			{
				_player.sendMessage("Need skill level " + _recipeList.getLevel());
				_player.sendPacket(new ActionFailed());
				abort();
				return;
			}
			
			// check that customer can afford to pay for creation services
			if (_player != _target)
			{
				for (L2ManufactureItem temp : _player.getCreateList().getList())
				{
					if (temp.getRecipeId() == _recipeList.getId()) // find recipe for item we want manufactured
					{
						_price = temp.getCost();
						if (_target.getAdena() < _price) // check price
						{
							_target.sendPacket(new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA));
							abort();
							return;
						}
						break;
					}
				}
			}
			
			// Make temporary items
			if ((_items = listItems(false)) == null)
			{
				abort();
				return;
			}
			
			// calculate reference price
			for (TempItem i : _items)
			{
				_totalItems += i.getQuantity();
			}
			// initial mana check requires MP as written on recipe
			if (_player.getCurrentMp() < _manaRequired)
			{
				_target.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_MP));
				abort();
				return;
			}
			
			// determine number of creation passes needed
			// can "equip"  skillLevel items each pass
			_creationPasses = (_totalItems / _skillLevel) + ((_totalItems % _skillLevel)!=0 ? 1 : 0);
			
			if (Config.ALT_GAME_CREATION && _creationPasses != 0) // update mana required to "per pass"
				_manaRequired /= _creationPasses; // checks to validateMp() will only need portion of mp for one pass
	
			updateMakeInfo(1);
			updateCurMp();
			updateCurLoad();
			
			_player.isInCraftMode(false);
			_isValid = true;
		}
		
		@Override
		public void run()
		{	
			if (!Config.IS_CRAFTING_ENABLED)
			{
				_target.sendMessage("Item creation is currently disabled.");
				abort();
				return;
			}

			if (_player == null || _target == null)
			{
				_log.warning("player or target == null (disconnected?), aborting" + _target + _player);
				abort();
				return;
			}

			if (!_player.isOnline() || !_target.isOnline())
			{
				_log.warning("player or target is not online, aborting " + _target + _player);
				abort();
				return;
			}

			if (Config.ALT_GAME_CREATION && !_activeMakers.containsKey(_player))
			{			
				if (_target != _player) 
				{
					_target.sendMessage("Manufacture aborted.");
					_player.sendMessage("Manufacture aborted.");
				} 
				else
				{
					_player.sendMessage("Item creation aborted.");		
				}
						
				abort();
				return;
			}		
					
			if (Config.ALT_GAME_CREATION && !_items.isEmpty())
			{
				if (!validateMp())
				{
					return;	// check mana
				}
				_player.reduceCurrentMp(_manaRequired); 	// use some mp
				updateCurMp();								// update craft window mp bar
				
				grabSomeItems(); // grab (equip) some more items with a nice msg to player
				
				// if still not empty, schedule another pass
				if (!_items.isEmpty())
				{
					// Divided by RATE_CONSUMABLES_COST to remove craft time increase on higher consumables rates 
					_delay = (int) (Config.ALT_GAME_CREATION_SPEED * _player.getMReuseRate(_skill)
							* GameTimeController.TICKS_PER_SECOND / Config.RATE_CONSUMABLE_COST)
							* GameTimeController.MILLIS_IN_TICK;
					
					// Start animation
					MagicSkillUse msk = new MagicSkillUse(_player, _skillId, _skillLevel, _delay, 0);
					_player.broadcastPacket(msk);
					
					_player.sendPacket(new SetupGauge(SetupGauge.BLUE, _delay));
					ThreadPoolManager.getInstance().scheduleGeneral(this, 100 + _delay);
				} 
				else 
				{
					// For alt mode, sleep delay millisec before finishing
					_player.sendPacket(new SetupGauge(SetupGauge.BLUE, _delay));
					
					try
					{ 
						Thread.sleep(_delay); 
					}
					catch (InterruptedException e)
					{
					}
					finally
					{
						finishCrafting();
					}
				}
			}
			else // For old craft mode just finish
			{
				finishCrafting();
			}
		}
	
		private void finishCrafting()
		{
			// First take adena for manufacture
			if (_target != _player && _price > 0) // customer must pay for services
			{
				// attempt to pay for item
				L2ItemInstance adenaTransfer = _target.transferItem("PayManufacture", _target.getInventory().getAdenaInstance().getObjectId(), _price, _player.getInventory(), _player);
				if (adenaTransfer == null)
				{
					_target.sendPacket(new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA));
					abort();
					return;	
				}
			}

			byte status;
			if ((_items = listItems(true)) == null) // This line actually takes materials from inventory
			{
				// Handle possible cheaters here 
				// (they click craft then try to get rid of items in order to get free craft)
				status = -1;
			}
			else if (Rnd.get(100) < _recipeList.getSuccessRate())
			{
				rewardPlayer(); // and immediately puts created item in its place
				status = 1;
			}
			else
			{
				_player.sendMessage("Item(s) failed to create.");
                if (_target != _player)
                {
                    _target.sendMessage("Item(s) failed to create.");
                }
				status = 0;
			}
			
			_activeMakers.remove(_player);
			_player.isInCraftMode(false);
			
			if (status > -1)
			{
				if (!Config.ALT_GAME_CREATION)
				{
					_player.reduceCurrentMp(_manaRequired);
				}
				
				// Recipe item info window retrieves item data from client, so update everything before opening
				updateCurMp();
				updateCurLoad();
				_target.sendPacket(new ItemList(_target, false));
				updateMakeInfo(status);
			}
		}
		
		private void updateMakeInfo(int status)
		{
			if (_target == _player)
				_target.sendPacket(new RecipeItemMakeInfo(_recipeList.getId(), _target, status));
			else
				_target.sendPacket(new RecipeShopItemInfo(_recipeList.getId(), _player, status));
		}
		
		private void updateCurLoad()
		{
			StatusUpdate su = new StatusUpdate(_target.getObjectId());
			su.addAttribute(StatusUpdate.CUR_LOAD, _target.getCurrentLoad());
			_target.sendPacket(su);
		}
		
		private void updateCurMp()
		{
			StatusUpdate su = new StatusUpdate(_target.getObjectId());
			su.addAttribute(StatusUpdate.CUR_MP, (int) _target.getCurrentMp());
			_target.sendPacket(su);
		}
		
		private void grabSomeItems()
		{
			int numItems = _skillLevel;
			
			while (numItems > 0 && !_items.isEmpty())
			{
				TempItem item = _items.get(0);
				
				int count = item.getQuantity();
				if (count >= numItems) count = numItems;
				
				item.setQuantity(item.getQuantity() - count);
				if (item.getQuantity() <= 0) _items.remove(0);
				else _items.set(0, item);
				
				numItems -= count;
				
				if (_target == _player)
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_S2_EQUIPPED);
					sm.addNumber(count);
					sm.addItemName(item.getItemId());
					_player.sendPacket(sm);
				} 
				else
					_target.sendMessage("Manufacturer " + _player.getName() + " used " + count + " " + item.getItemName());
			}
		}
		
		private boolean validateMp()
		{
			if (_player.getCurrentMp() < _manaRequired)
			{
				// rest (wait for MP)
				if (Config.ALT_GAME_CREATION)
				{
					_player.sendPacket(new SetupGauge(SetupGauge.BLUE, _delay));
					ThreadPoolManager.getInstance().scheduleGeneral(this, 100 + _delay);
				}
				else // no rest - report no mana
				{
					_target.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_MP));
					abort(); 
				}
				return false;
			}
			return true;
		}
		
		private List<TempItem> listItems(boolean remove)
		{
			L2Item recipeListItem = ItemTable.getInstance().getTemplate(_recipeList.getItemId());
			if (recipeListItem == null)
			{
				_log.warning(getClass().getSimpleName() + ": Missing template for recipe list item ID " + _recipeList.getItemId());
				return null;
			}

			L2RecipeInstance[] recipes = _recipeList.getRecipes();
			Inventory inv = _target.getInventory();
			List<TempItem> materials = new ArrayList<>();
			
			boolean aborted = false;
			for (L2RecipeInstance recipe : recipes)
			{
				int quantity = recipeListItem.isConsumable() ? (int) (recipe.getQuantity() * Config.RATE_CONSUMABLE_COST) : recipe.getQuantity();
				if (quantity > 0)
				{
					L2ItemInstance item = inv.getItemByItemId(recipe.getItemId());
					int itemQuantityAmount = item == null ? 0 : item.getCount();
					
					// Check materials
					if (itemQuantityAmount < quantity)
					{
						SystemMessage sm = new SystemMessage(SystemMessage.MISSING_S2_S1_TO_CREATE);
						sm.addItemName(recipe.getItemId());
						sm.addNumber(quantity - itemQuantityAmount);
						_target.sendPacket(sm);
						
						// Do not proceed to manufacture
						aborted = true;
					}
					
					if (!aborted)
					{
						// Make new temporary object, just for counting purposes
						materials.add(new TempItem(item, quantity));
					}
				}
			}
			
			// Abort transaction
			if (aborted)
			{
				abort();
				return null;
			}

			if (remove)
			{
				for(TempItem tmp : materials)
				{
					inv.destroyItemByItemId("Manufacture", tmp.getItemId(), tmp.getQuantity(), _target, _player);
				}
			}			
			return materials;
		}
		
		private void abort()
		{
			updateMakeInfo(0);
			_player.isInCraftMode(false);
			_activeMakers.remove(_player);
		}
		
		private void rewardPlayer()
		{
			int itemId = _recipeList.getItemId();
			int itemCount = _recipeList.getCount();
			
			L2ItemInstance createdItem = _target.getInventory().addItem("Manufacture", itemId, itemCount, _target, _player);
			if (createdItem == null)
			{
				return;
			}
			
			// inform customer of earned item
            SystemMessage sm = null;
            if (itemCount > 1)
            {
    			sm = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
    			sm.addItemName(itemId);
                sm.addNumber(itemCount);
    			_target.sendPacket(sm);
            }
            else
            {
                sm = new SystemMessage(SystemMessage.EARNED_ITEM);
                sm.addItemName(itemId);
                _target.sendPacket(sm);
            }
			
			if (_target != _player)
			{
				// inform manufacturer of earned profit
				sm = new SystemMessage(SystemMessage.EARNED_ADENA);
				sm.addNumber(_price);
				_player.sendPacket(sm);
			}
			
			if (Config.ALT_GAME_CREATION)
			{
				int recipeLevel = _recipeList.getLevel();
				long exp = createdItem.getReferencePrice() * itemCount;
				// one variation
				// exp -= materialsRefPrice;   // mat. ref. price is not accurate so other method is better
				
				if (exp < 0) exp = 0;
				
				// another variation
				exp /= recipeLevel;
				for (int i = _skillLevel; i > recipeLevel; i--)
					exp /= 4;
				
				long sp = exp / 10;
				 
				// Added multiplication of Creation speed with XP/SP gain
				// slower crafting -> more XP,  faster crafting -> less XP 
				// you can use ALT_GAME_CREATION_XP_RATE/SP to
				// modify XP/SP gained (default = 1)
				_player.addExpAndSp((long)_player.calcStat(Stats.EXPSP_RATE, exp * Config.ALT_GAME_CREATION_XP_RATE  
				                                         * Config.ALT_GAME_CREATION_SPEED, null, null)
				                  ,(int) _player.calcStat(Stats.EXPSP_RATE, sp * Config.ALT_GAME_CREATION_SP_RATE   
				                                         * Config.ALT_GAME_CREATION_SPEED, null, null));
			}
			updateMakeInfo(1); // success
		}
		
		/**
		 * Class explanation:
		 * For item counting or checking purposes. When you don't want to modify inventory 
		 * class contains itemId, quantity, ownerId, referencePrice, but not objectId 
		 */
		private class TempItem
		{
            // no object id stored, this will be only "list" of items with it's owner
			private int _itemId;
			private int _quantity;
			private String _itemName;
			
			/**
			 * @param item
			 * @param quantity of that item
			 */
			public TempItem(L2ItemInstance item, int quantity)
			{
				super();
				_itemId = item.getItemId();
				_quantity = quantity;
				_itemName = item.getItem().getName();
			}
			
			/**
			 * @return Returns the quantity.
			 */
			public int getQuantity()
			{
				return _quantity;
			}
			
			/**
			 * @param quantity The quantity to set.
			 */
			public void setQuantity(int quantity)
			{
				_quantity = quantity;
			}
			
			/**
			 * @return Returns the itemId.
			 */
			public int getItemId()
			{
				return _itemId;
			}
			
			/**
			 * @return Returns the itemName.
			 */
			public String getItemName()
			{
				return _itemName;
			}
		}
	}
	
	private L2RecipeList getValidRecipeList(L2PcInstance player, int id)
	{
		L2RecipeList recipeList = getRecipeList(id - 1);
		if ((recipeList == null) || (recipeList.getRecipes().length == 0))
		{
			player.sendMessage("No recipe for: " + id);
			player.isInCraftMode(false);
			return null;
		}
		return recipeList;
	}
	
	private static class SingletonHolder
	{
		protected static final RecipeController _instance = new RecipeController();
	}
}