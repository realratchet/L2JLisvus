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
package net.sf.l2j.gameserver.templates;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.Race;

/**
 * @author mkizub
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class L2PcTemplate extends L2CharTemplate
{
	/** The Class object of the L2PcInstance */
	public final ClassId classId;

	public final Race   race;
	public final String className;

	public final int    spawnX;
	public final int    spawnY;
	public final int    spawnZ;

	public final int     classBaseLevel;
	public final float   lvlHpAdd;
	public final float   lvlHpMod;
	public final float   lvlCpAdd;
	public final float   lvlCpMod;
	public final float   lvlMpAdd;
	public final float   lvlMpMod;

    public final double collisionHeightFemale;
    public final double collisionRadiusFemale;

	private List<InitialItem> _items = new ArrayList<>();

	public L2PcTemplate(StatsSet set)
	{
		super(set);
		classId   = ClassId.values()[set.getInteger("classId")];
		race      = Race.values()[set.getInteger("raceId")];
		className = set.getString("className");

		spawnX    = set.getInteger("spawnX");
		spawnY    = set.getInteger("spawnY");
		spawnZ    = set.getInteger("spawnZ");

		classBaseLevel = set.getInteger("classBaseLevel");
		lvlHpAdd  = set.getFloat("lvlHpAdd");
		lvlHpMod  = set.getFloat("lvlHpMod");
        lvlCpAdd  = set.getFloat("lvlCpAdd");
        lvlCpMod  = set.getFloat("lvlCpMod");
		lvlMpAdd  = set.getFloat("lvlMpAdd");
		lvlMpMod  = set.getFloat("lvlMpMod");

        collisionRadiusFemale = set.getDouble("collisionRadiusFemale");
        collisionHeightFemale = set.getDouble("collisionHeightFemale");
	}

	/**
	 * Adds starter equipment.
	 * 
	 * @param itemId
	 * @param count 
	 * @param toEquip 
	 * @param shortcutPage 
	 * @param shortcutSlot 
	 */
	public void addItem(int itemId, int count, boolean toEquip, int shortcutPage, int shortcutSlot)
	{
		_items.add(new InitialItem(itemId, count, toEquip, shortcutPage, shortcutSlot));
	}

	/**
	 *
	 * @return itemIds of all the starter equipment
	 */
	public InitialItem[] getItems()
	{
		return _items.toArray(new InitialItem[_items.size()]);
	}

    public final int getFallHeight()
    {
        return 333;
    }
    
    public class InitialItem
    {
    	private final int _itemId;
    	private final int _count;
    	private final boolean _toEquip;
    	private final int _shortcutPage;
    	private final int _shortcutSlot;
    	
    	private InitialItem(int itemId, int count, boolean toEquip, int shortcutPage, int shortcutSlot)
    	{
    		_itemId = itemId;
    		_count = count;
    		_toEquip = toEquip;
    		_shortcutPage = shortcutPage;
    		_shortcutSlot = shortcutSlot;
    	}
    	
    	public int getItemId()
    	{
    		return _itemId;
    	}
    	
    	public int getCount()
    	{
    		return _count;
    	}
    	
    	public boolean toEquip()
    	{
    		return _toEquip;
    	}
    	
    	public int getShortcutPage()
    	{
    		return _shortcutPage;
    	}
    	
    	public int getShortcutSlot()
    	{
    		return _shortcutSlot;
    	}
    }
}