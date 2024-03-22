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

import net.sf.l2j.gameserver.model.itemcontainer.Inventory;

/**
 * This class is dedicated to the management of EtcItem.
 * 
 * @version $Revision: 1.2.2.1.2.3 $ $Date: 2005/03/27 15:30:10 $
 */
public final class L2EtcItem  extends L2Item
{
	private L2EtcItemType _type;
	private final String _handler;

	/**
	 * Constructor for EtcItem.
	 * 
	 * @see L2Item constructor
	 * @param set : StatsSet designating the set of couples (key,value) for description of the Etc
	 */
	public L2EtcItem(StatsSet set)
	{
		super(set);

		_type = set.getEnum("etcitem_type", L2EtcItemType.class, L2EtcItemType.OTHER);

		_type1 = L2Item.TYPE1_ITEM_QUESTITEM_ADENA;
		if (getItemType() == L2EtcItemType.QUEST)
		{
			_type2 = L2Item.TYPE2_QUEST;
		}
		else if (getItemId() == Inventory.ADENA_ID || getItemId() == Inventory.ANCIENT_ADENA_ID)
		{
			_type2 = L2Item.TYPE2_MONEY;
		}
		else
		{
			_type2 = L2Item.TYPE2_OTHER; // default is other
		}

		_handler = set.getString("handler", null);
	}
	
	/**
	 * Returns the type of Etc Item
	 * @return L2EtcItemType
	 */
	@Override
	public L2EtcItemType getItemType()
	{
		return _type;
	}

    /**
     * Returns if the item is consumable
     * @return boolean
     */
    @Override
	public final boolean isConsumable()
    {
        return ((getItemType() == L2EtcItemType.ARROW) || (getItemType() == L2EtcItemType.SHOT) || (getItemType() == L2EtcItemType.POTION));
    }
    
    /**
     * Returns if the item is a mercenary posting ticket
     * @return boolean
     */
    @Override
	public boolean isMercenaryTicket()
    {
        return getItemType() == L2EtcItemType.CASTLE_GUARD;
    }

	/**
	 * Returns the ID of the Etc item after applying the mask.
	 * @return int : ID of the EtcItem
	 */
	@Override
	public int getItemMask()
	{
		return getItemType().mask();
	}

	public String getHandlerName()
	{
		return _handler;
	}
}