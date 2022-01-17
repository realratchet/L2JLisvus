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

/**
 * 
 *
 * @author  Luno
 */
public final class L2ArmorSet
{
	private final int _chest;
	private final int _legs;
	private final int _head;
	private final int _gloves;
	private final int _feet;
	private final int _skill_id;
	
	private final int _shield;
	private final int _shield_skill_id;
	
	public L2ArmorSet(int chest, int legs, int head, int gloves, int feet, int skill_id, int shield, int shield_skill_id)
	{
		_chest = chest;
		_legs  = legs;
		_head  = head;
		_gloves = gloves;
		_feet  = feet;
		_skill_id = skill_id;

		_shield = shield;
		_shield_skill_id = shield_skill_id;
	}
	/**
	 * Checks if player have equiped all items from set (not checking shield)
	 * @param player whose inventory is being checked
	 * @return True if player equips whole set
	 */
	public boolean containAll(L2PcInstance player)
	{
		Inventory inv = player.getInventory();
		
		L2ItemInstance legsItem   = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		L2ItemInstance headItem   = inv.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
		L2ItemInstance glovesItem = inv.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		L2ItemInstance feetItem   = inv.getPaperdollItem(Inventory.PAPERDOLL_FEET);
		
		int legs = 0;
		int head = 0;
		int gloves = 0;
		int feet = 0;
		
		if(legsItem != null)   legs = legsItem.getItemId();
		if(headItem != null)   head = headItem.getItemId();
		if(glovesItem != null) gloves = glovesItem.getItemId();
		if(feetItem != null)   feet = feetItem.getItemId();
		
		return containAll(_chest,legs,head,gloves,feet);
		
	}
	public boolean containAll(int chest, int legs, int head, int gloves, int feet)
	{
		if(_chest != 0 && _chest != chest)
			return false;
		if(_legs != 0 && _legs != legs)
			return false;
		if(_head != 0 && _head != head)
			return false;
		if(_gloves != 0 && _gloves != gloves)
			return false;
		if(_feet != 0 && _feet != feet)
			return false;
	
		return true;
	}
	public boolean containItem(int slot, int itemId)
	{
		switch(slot)
		{
		case Inventory.PAPERDOLL_CHEST:
			return _chest == itemId;
		case Inventory.PAPERDOLL_LEGS:
			return _legs == itemId;
		case Inventory.PAPERDOLL_HEAD:
			return _head == itemId;
		case Inventory.PAPERDOLL_GLOVES:
			return _gloves == itemId;
		case Inventory.PAPERDOLL_FEET:
			return _feet == itemId;
		default:
			return false;
		}
	}
	public int getSkillId()
	{
		return _skill_id;
	}
	public boolean containShield(L2PcInstance player)
	{
		Inventory inv = player.getInventory();
		
		L2ItemInstance shieldItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if(shieldItem!= null && shieldItem.getItemId() == _shield)
			return true;
	
		return false;
	}
	public boolean containShield(int shield_id)
	{
		if (_shield == 0)
			return false;
		
		return _shield == shield_id;
	}
	public int getShieldSkillId()
	{
		return _shield_skill_id;
	}
}