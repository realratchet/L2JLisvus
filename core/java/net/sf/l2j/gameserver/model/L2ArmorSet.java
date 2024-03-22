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

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author Luno
 */
public final class L2ArmorSet
{
	private static final int[] PAPERDOLL_SET_INDICES =
	{
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_FEET
	};

	private final String _name;
	
	private final int[] _set = new int[5];
	
	private final int _skillId;
	private final int _shield;
	private final int _shieldSkillId;
	
	public L2ArmorSet(StatsSet set)
	{
		_name = set.getString("name", "");
		_set[0] = set.getInteger("chest", 0);
		_set[1] = set.getInteger("legs", 0);
		_set[2] = set.getInteger("head", 0);
		_set[3] = set.getInteger("gloves", 0);
		_set[4] = set.getInteger("feet", 0);
		_skillId = set.getInteger("skillId", 0);
		_shield = set.getInteger("shield", 0);
		_shieldSkillId = set.getInteger("shieldSkillId", 0);
	}
	
	/**
	 * Checks if player have equipped all items from set (not checking shield).
	 * 
	 * @param player whose inventory is being checked
	 * @return True if player equips whole set
	 */
	public boolean containsAll(L2PcInstance player)
	{
		final Inventory inv = player.getInventory();
		
		// Exclude chest
		for (int i = 1; i < _set.length; i++)
		{
			int setItemId = _set[i];
			L2ItemInstance item = inv.getPaperdollItem(PAPERDOLL_SET_INDICES[i]);
			int itemId = item != null ? item.getItemId() : 0;
			if (setItemId != 0 && setItemId != itemId)
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean containsItem(int slot, int itemId)
	{
		switch (slot)
		{
			case Inventory.PAPERDOLL_CHEST:
				return _set[0] == itemId;
			case Inventory.PAPERDOLL_LEGS:
				return _set[1] == itemId;
			case Inventory.PAPERDOLL_HEAD:
				return _set[2] == itemId;
			case Inventory.PAPERDOLL_GLOVES:
				return _set[3] == itemId;
			case Inventory.PAPERDOLL_FEET:
				return _set[4] == itemId;
			default:
				return false;
		}
	}
	
	public boolean containsShield(L2PcInstance player)
	{
		Inventory inv = player.getInventory();
		
		L2ItemInstance shieldItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if (shieldItem != null && shieldItem.getItemId() == _shield)
			return true;
		
		return false;
	}
	
	public boolean containsShield(int shield_id)
	{
		if (_shield == 0)
			return false;
		
		return _shield == shield_id;
	}
	
	public int getSkillId()
	{
		return _skillId;
	}

	public int getShieldSkillId()
	{
		return _shieldSkillId;
	}
	
	@Override
	public String toString()
	{
		return _name;
	}
}