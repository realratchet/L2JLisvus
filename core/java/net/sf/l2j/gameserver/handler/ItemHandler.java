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
package net.sf.l2j.gameserver.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.handler.itemhandlers.BeastSoulShot;
import net.sf.l2j.gameserver.handler.itemhandlers.BeastSpice;
import net.sf.l2j.gameserver.handler.itemhandlers.BeastSpiritShot;
import net.sf.l2j.gameserver.handler.itemhandlers.BlessedSpiritShot;
import net.sf.l2j.gameserver.handler.itemhandlers.Book;
import net.sf.l2j.gameserver.handler.itemhandlers.CharChangePotions;
import net.sf.l2j.gameserver.handler.itemhandlers.ChestKey;
import net.sf.l2j.gameserver.handler.itemhandlers.CrystalCarol;
import net.sf.l2j.gameserver.handler.itemhandlers.EnchantScrolls;
import net.sf.l2j.gameserver.handler.itemhandlers.EnergyStone;
import net.sf.l2j.gameserver.handler.itemhandlers.ExtractableItems;
import net.sf.l2j.gameserver.handler.itemhandlers.Firework;
import net.sf.l2j.gameserver.handler.itemhandlers.FishShots;
import net.sf.l2j.gameserver.handler.itemhandlers.Harvester;
import net.sf.l2j.gameserver.handler.itemhandlers.MercTicket;
import net.sf.l2j.gameserver.handler.itemhandlers.PetFood;
import net.sf.l2j.gameserver.handler.itemhandlers.Potions;
import net.sf.l2j.gameserver.handler.itemhandlers.Recipes;
import net.sf.l2j.gameserver.handler.itemhandlers.Remedy;
import net.sf.l2j.gameserver.handler.itemhandlers.RollingDice;
import net.sf.l2j.gameserver.handler.itemhandlers.ScrollOfEscape;
import net.sf.l2j.gameserver.handler.itemhandlers.ScrollOfResurrection;
import net.sf.l2j.gameserver.handler.itemhandlers.Scrolls;
import net.sf.l2j.gameserver.handler.itemhandlers.Seed;
import net.sf.l2j.gameserver.handler.itemhandlers.SevenSignsRecord;
import net.sf.l2j.gameserver.handler.itemhandlers.SoulCrystals;
import net.sf.l2j.gameserver.handler.itemhandlers.SoulShots;
import net.sf.l2j.gameserver.handler.itemhandlers.SpecialXMas;
import net.sf.l2j.gameserver.handler.itemhandlers.SpiritShot;
import net.sf.l2j.gameserver.handler.itemhandlers.SummonItems;
import net.sf.l2j.gameserver.handler.itemhandlers.WorldMap;

/**
 * This class manages handlers of items
 *
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:30:09 $
 */
public class ItemHandler
{
	private static final Logger _log = Logger.getLogger(ItemHandler.class.getName());
	
	private final Map<Integer, IItemHandler> _dataTable = new HashMap<>();
	
	/**
	 * Create ItemHandler if doesn't exist and returns ItemHandler
	 * @return ItemHandler
	 */
	public static ItemHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void load()
	{
		registerItemHandler(new ScrollOfEscape());
		registerItemHandler(new ScrollOfResurrection());
		registerItemHandler(new SoulShots());
		registerItemHandler(new SpecialXMas());
		registerItemHandler(new SpiritShot());
		registerItemHandler(new BlessedSpiritShot());
		registerItemHandler(new BeastSoulShot());
		registerItemHandler(new BeastSpiritShot());
		registerItemHandler(new ChestKey());
		registerItemHandler(new WorldMap());
		registerItemHandler(new PetFood());
		registerItemHandler(new Potions());
		registerItemHandler(new Recipes());
		registerItemHandler(new RollingDice());
		registerItemHandler(new EnchantScrolls());
		registerItemHandler(new EnergyStone());
		registerItemHandler(new Book());
		registerItemHandler(new Remedy());
		registerItemHandler(new Scrolls());
		registerItemHandler(new CrystalCarol());
		registerItemHandler(new SoulCrystals());
		registerItemHandler(new SevenSignsRecord());
		registerItemHandler(new CharChangePotions());
		registerItemHandler(new Firework());
		registerItemHandler(new Seed());
		registerItemHandler(new Harvester());
		registerItemHandler(new MercTicket());
		registerItemHandler(new FishShots());
		registerItemHandler(new ExtractableItems());
		registerItemHandler(new SummonItems());
		registerItemHandler(new BeastSpice());
		
		_log.config("ItemHandler: Loaded " + _dataTable.size() + " handlers.");
	}
	
	/**
	 * Adds handler of item type in <I>data table</I>.<BR><BR>
	 * <B><I>Concept :</I></U><BR>
	 * This handler is put in <I>data table</I> Map &lt;Integer ; IItemHandler &gt; for each ID corresponding to an item type 
	 * (existing in classes of package item handlers) sets as key of the Map. 
	 * @param handler (IItemHandler)
	 */
	public void registerItemHandler(IItemHandler handler)
	{
		// Get all ID corresponding to the item type of the handler
		int[] ids = handler.getItemIds();
		// Add handler for each ID found
		for (int i = 0; i < ids.length; i++)
		{
			_dataTable.put(Integer.valueOf(ids[i]), handler);
		}
	}
	
	/**
	 * Returns the handler of the item
	 * @param itemId : int designating the itemID
	 * @return IItemHandler
	 */
	public IItemHandler getItemHandler(int itemId)
	{
		return _dataTable.get(Integer.valueOf(itemId));
	}
	
	private static class SingletonHolder
	{
		protected static final ItemHandler _instance = new ItemHandler();
	}
}