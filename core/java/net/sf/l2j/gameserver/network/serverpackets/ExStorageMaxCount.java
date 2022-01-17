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

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * Format: (ch)ddddddd d: Number of Inventory Slots d: Number of Warehouse Slots d: Number of Freight Slots (unconfirmed) (200 for a low level dwarf) d: Private Sell Store Slots (unconfirmed) (4 for a low level dwarf) d: Private Buy Store Slots (unconfirmed) (5 for a low level dwarf) d: Dwarven
 * Recipe Book Slots d: Normal Recipe Book Slots
 * @author -Wooden- format from KenM
 */
public class ExStorageMaxCount extends L2GameServerPacket
{
	private static final String _S__FE_2E_EXSTORAGEMAXCOUNT = "[S] FE:2E ExStorageMaxCount";
	private final L2PcInstance _character;
	private final int _inventory;
	private final int _warehouse;
	private final int _freight;
	private final int _privateSell;
	private final int _privateBuy;
	private final int _receipeD;
	private final int _recipe;
	
	public ExStorageMaxCount(L2PcInstance character)
	{
		_character = character;
		_inventory = _character.getInventoryLimit();
		_warehouse = _character.getWareHouseLimit();
		_privateSell = _character.getPrivateSellStoreLimit();
		_privateBuy = _character.getPrivateBuyStoreLimit();
		_freight = _character.getFreightLimit();
		_receipeD = _character.getDwarfRecipeLimit();
		_recipe = _character.getCommonRecipeLimit();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#writeImpl()
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x2e);
		
		writeD(_inventory);
		writeD(_warehouse);
		writeD(_freight);
		writeD(_privateSell);
		writeD(_privateBuy);
		writeD(_receipeD);
		writeD(_recipe);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_2E_EXSTORAGEMAXCOUNT;
	}
}