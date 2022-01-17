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

public class ChooseInventoryItem extends L2GameServerPacket
{
	private static final String _S__6F_CHOOSEINVENTORYITEM = "[S] 6f ChooseInventoryItem";
	
	private final int ItemID;
	
	public ChooseInventoryItem(int Item)
	{
		ItemID = Item;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x6f);
		
		writeD(ItemID);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__6F_CHOOSEINVENTORYITEM;
	}
}