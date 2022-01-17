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

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * Sdh(h dddhh [dhhh] d) Sdh ddddd ddddd ddddd ddddd
 * @version $Revision: 1.1.2.1.2.4 $ $Date: 2005/03/29 23:15:10 $
 */
public class GMViewWarehouseWithdrawList extends L2GameServerPacket
{
	
	private static final String _S__95_GMViewWarehouseWithdrawList = "[S] 95 GMViewWarehouseWithdrawList";
	private final L2ItemInstance[] _items;
	private final L2PcInstance _character;
	private final int _money;

	public GMViewWarehouseWithdrawList(L2PcInstance cha)
	{
		
		_character = cha;
		_items = _character.getWarehouse().getItems();
		_money = cha.getAdena();

	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x95);
		writeS(_character.getName());
		writeD(_money);
		writeH(_items.length);

		for (L2ItemInstance item : _items)
		{
			writeH(item.getItem().getType1());
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(item.getCount());
			writeH(item.getItem().getType2());
			writeH(item.getCustomType1());

			if (item.isEquipable())
			{
				writeD(item.getItem().getBodyPart());
				writeH(item.getEnchantLevel());
				writeH(0x00);
				writeH(0x00);
			}

			writeD(item.getObjectId());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__95_GMViewWarehouseWithdrawList;
	}
}