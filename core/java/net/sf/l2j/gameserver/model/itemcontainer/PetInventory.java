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

import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ItemInstance.ItemLocation;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.templates.L2Armor;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;

public class PetInventory extends Inventory 
{
    private final L2PetInstance _owner;

    public PetInventory(L2PetInstance owner) 
    {
        _owner = owner;
    }

    @Override
	public L2PetInstance getOwner()
    { 
        return _owner; 
    }

    @Override
	public int getOwnerId()
    {
        // gets the L2PcInstance-owner's ID
        int id;
        try
        {
            id = _owner.getOwner().getObjectId();
        }
        catch (NullPointerException e)
        {
            return 0;
        }
        return id;
    }

    /**
     * Refresh the weight of equipment loaded
     */
    @Override
    public void refreshWeight()
    {
        super.refreshWeight();
        getOwner().updateAndBroadcastStatus(1);
    }


    public boolean validateCapacity(L2ItemInstance item)
    {
        int slots = 0;

        if (!(item.isStackable() && getItemByItemId(item.getItemId()) != null))
        	slots++;

        return validateCapacity(slots);
    }

    @Override
	public boolean validateCapacity(int slots)
    {
        return (_items.size() + slots <= _owner.getInventoryLimit());
    }

    public boolean validateWeight(L2ItemInstance item, int count)
    {
        int weight = 0;
        L2Item template = ItemTable.getInstance().getTemplate(item.getItemId());
        if (template == null)
            return false;
        weight += count * template.getWeight();
        return validateWeight(weight);
    }

    @Override
    public boolean validateWeight(int weight)
    {
        return (_totalWeight + weight <= _owner.getMaxLoad());
    }

    /**
	 * Equips item in slot of pet paperdoll.
	 * @param item : L2ItemInstance designating the item and slot used.
	 */
	public void equipPetItem(L2ItemInstance item)
	{
		L2Item template = item.getItem();
		if (template.isPetItem())
		{
			if (template instanceof L2Weapon)
			{
				setPaperdollItem(PAPERDOLL_RHAND, item);
				_owner.setWeapon(item.getItemId());
			}
			else if (template instanceof L2Armor)
			{
				setPaperdollItem(PAPERDOLL_CHEST, item);
				_owner.setArmor(item.getItemId());
			}
			else
			{
				_log.warning("Unknown pet item type");
			}
		}
		else
		{
			_log.warning("Attempted to equip a non-pet item to pet. Item: " + item.getItemId());
		}
	}

    @Override
	protected ItemLocation getBaseLocation() 
    {
        return ItemLocation.PET; 
    }

    @Override
	protected ItemLocation getEquipLocation() 
    { 
        return ItemLocation.PET_EQUIP; 
    }
}
