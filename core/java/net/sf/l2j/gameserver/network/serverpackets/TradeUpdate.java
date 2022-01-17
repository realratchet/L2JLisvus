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
import net.sf.l2j.gameserver.model.TradeList.TradeItem;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class TradeUpdate extends L2GameServerPacket
{
	private static final String _S__74_TRADEUPDATE = " 74 TradeUpdate";
	
	private final L2PcInstance _activeChar;
	
	public TradeUpdate(final L2PcInstance activeChar)
	{
		_activeChar = activeChar;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x74);
		
		writeH(_activeChar.getActiveTradeList().getItems().length);
		for (final TradeItem _item : _activeChar.getActiveTradeList().getItems())
		{
			int _availableCount = 1;
			boolean _stackable = false;
			
			L2ItemInstance item = _activeChar.getInventory().getItemByObjectId(_item.getObjectId());
			if (item == null)
			{
				continue;
			}
			
			if ((item.getCount() - _item.getCount()) > 0)
			{
				_availableCount = item.getCount() - _item.getCount();
				_stackable = _item.getItem().isStackable();
			}
			
			writeH(_stackable ? 3 : 2);
			writeH(_item.getItem().getType1()); // item type1
			writeD(_item.getObjectId());
			writeD(_item.getItem().getItemId());
			writeD(_availableCount);
			writeH(_item.getItem().getType2()); // item type2
			writeH(0x00); // ?
			writeD(_item.getItem().getBodyPart()); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
			writeH(_item.getEnchant()); // enchant level
			writeH(0x00); // ?
			writeH(0x00);
		}
	}
	
	@Override
	public String getType()
	{
		return _S__74_TRADEUPDATE;
	}
}