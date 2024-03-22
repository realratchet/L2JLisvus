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
import java.util.logging.Logger;

/**
 * This class ...
 * @version $Revision: 1.4.2.1.2.5 $ $Date: 2005/03/27 15:29:33 $
 */
public class L2TradeList
{
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L2TradeList.class.getName());
	
	private List<L2ItemInstance> _items;
	private int _listId;
	private boolean _confirmed;
	private String _buyStoreName, _sellStoreName;
	
	private String _npcId;
	
	public L2TradeList(int listId)
	{
		_items = new ArrayList<>();
		_listId = listId;
		_confirmed = false;
	}
	
	public void setNpcId(String id)
	{
		_npcId = id;
	}
	
	public String getNpcId()
	{
		return _npcId;
	}
	
	public void addItem(L2ItemInstance item)
	{
		_items.add(item);
	}
	
	public void replaceItem(int itemID, int price)
	{
		for (L2ItemInstance item : _items)
		{
			if (item.getItemId() == itemID)
			{
				item.setPriceToSell(price);
			}
		}
	}
	
	public boolean decreaseCount(int itemID, int count)
	{
		for (L2ItemInstance item : _items)
		{
			if (item.getItemId() == itemID)
			{
				if (item.getCount() >= count)
				{
					item.setCount(item.getCount() - count);
					return true;
				}
			}
		}
		return false;
	}
	
	public void removeItem(int itemID)
	{
		_items.removeIf(item -> item.getItemId() == itemID);
	}
	
	/**
	 * @return Returns the listId.
	 */
	public int getListId()
	{
		return _listId;
	}
	
	public void setSellStoreName(String name)
	{
		_sellStoreName = name;
	}
	
	public String getSellStoreName()
	{
		return _sellStoreName;
	}
	
	public void setBuyStoreName(String name)
	{
		_buyStoreName = name;
	}
	
	public String getBuyStoreName()
	{
		return _buyStoreName;
	}
	
	/**
	 * @return Returns the items.
	 */
	public List<L2ItemInstance> getItems()
	{
		return _items;
	}
	
	public List<L2ItemInstance> getItems(int start, int end)
	{
		return _items.subList(start, end);
	}
	
	public int getPriceForItemId(int itemId)
	{
		for (L2ItemInstance item : _items)
		{
			if (item.getItemId() == itemId)
			{
				return item.getPriceToSell();
			}
		}
		return -1;
	}
	
	public boolean countDecrease(int itemId)
	{
		for (L2ItemInstance item : _items)
		{
			if (item.getItemId() == itemId)
				return item.getCountDecrease();
		}
		return false;
	}
	
	public boolean containsItemId(int itemId)
	{
		for (L2ItemInstance item : _items)
		{
			if (item.getItemId() == itemId)
				return true;
		}
		return false;
	}
	
	public L2ItemInstance getItem(int ObjectId)
	{
		for (L2ItemInstance item : _items)
		{
			if (item.getObjectId() == ObjectId)
			{
				return item;
			}
		}
		return null;
	}
	
	public synchronized void setConfirmedTrade(boolean x)
	{
		_confirmed = x;
	}
	
	public synchronized boolean hasConfirmed()
	{
		return _confirmed;
	}
}