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

import java.util.Map;

import net.sf.l2j.gameserver.model.itemcontainer.Inventory;

public class ShopPreviewInfo extends L2GameServerPacket
{
	private static final String _S__F0_SHOPPREVIEWINFO = "[S] F0 ShopPreviewInfo";
	
	private final Map<Integer, Integer> _itemlist;
	
	public ShopPreviewInfo(Map<Integer, Integer> itemlist)
	{
		_itemlist = itemlist;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xf0);
		writeD(Inventory.PAPERDOLL_TOTALSLOTS);
		// Slots
		writeD(getFromList(Inventory.PAPERDOLL_REAR));
		writeD(getFromList(Inventory.PAPERDOLL_LEAR));
		writeD(getFromList(Inventory.PAPERDOLL_NECK));
		writeD(getFromList(Inventory.PAPERDOLL_RFINGER));
		writeD(getFromList(Inventory.PAPERDOLL_LFINGER));
		writeD(getFromList(Inventory.PAPERDOLL_HEAD)); // Correct
		writeD(getFromList(Inventory.PAPERDOLL_RHAND)); // Correct
		writeD(getFromList(Inventory.PAPERDOLL_LHAND)); // Correct
		writeD(getFromList(Inventory.PAPERDOLL_GLOVES)); // Correct
		writeD(getFromList(Inventory.PAPERDOLL_CHEST)); // Correct
		writeD(getFromList(Inventory.PAPERDOLL_LEGS)); // Correct
		writeD(getFromList(Inventory.PAPERDOLL_FEET)); // Correct
		writeD(getFromList(Inventory.PAPERDOLL_UNDER));
		writeD(getFromList(Inventory.PAPERDOLL_LHAND + Inventory.PAPERDOLL_RHAND)); // Correct
		writeD(getFromList(Inventory.PAPERDOLL_HAIR)); // Correct
	}

	private int getFromList(int key)
	{
		return _itemlist.getOrDefault(key, 0);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__F0_SHOPPREVIEWINFO;
	}
}