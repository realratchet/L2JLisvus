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
package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.L2Multisell.MultiSellEntry;
import net.sf.l2j.gameserver.model.L2Multisell.MultiSellIngredient;
import net.sf.l2j.gameserver.model.L2Multisell.MultiSellListContainer;
import net.sf.l2j.gameserver.templates.L2Item;

public class MultiSellList extends L2GameServerPacket
{
	private static final String _S__D0_MULTISELLLIST = "[S] D0 MultiSellList";
	
	protected int _page, _finished;
	protected MultiSellListContainer _list;
	
	public MultiSellList(MultiSellListContainer list, int page, int finished)
	{
		_list = list;
		_page = page;
		_finished = finished;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xd0);
		writeD(_list.getListId()); // List id
		writeD(_page); // Page
		writeD(_finished); // Finished
		writeD(0x28); // Size of pages
		writeD(_list == null ? 0 : _list.getEntries().size()); // List length
		
		if (_list != null)
		{
			for (MultiSellEntry ent : _list.getEntries())
			{
				writeD(ent.getEntryId());
				writeC(1);
				writeH(ent.getProducts().size());
				writeH(ent.getIngredients().size());
				
				for (MultiSellIngredient i : ent.getProducts())
				{
					writeH(i.getItemId());
					
					L2Item item = ItemTable.getInstance().getTemplate(i.getItemId());
					if (item != null)
					{
						writeD(item.getBodyPart());
						writeH(item.getType2());
					}
					else
					{
						writeD(0);
						writeH(65535);
					}
					
					writeD(i.getItemCount());
					writeH(i.getEnchantmentLevel()); // Enchant level
				}
				
				for (MultiSellIngredient i : ent.getIngredients())
				{
					writeH(i.getItemId()); // ID
					
					L2Item item = ItemTable.getInstance().getTemplate(i.getItemId());
					writeH(item != null ? item.getType2() : 65535);
					
					writeD(i.getItemCount()); // Count
					writeH(i.getEnchantmentLevel()); // Enchant Level
				}
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _S__D0_MULTISELLLIST;
	}
}