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
import net.sf.l2j.gameserver.model.TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:40 $
 */
public class PrivateStoreManageListBuy extends L2GameServerPacket
{
	private static final String _S__D0_PRIVATESELLLISTBUY = "[S] b7 PrivateSellListBuy";
	private final L2PcInstance _player;
	private final int _playerAdena;
	private final L2ItemInstance[] _itemList;
	private final TradeList.TradeItem[] _buyList;
	
	public PrivateStoreManageListBuy(L2PcInstance player)
	{
		_player = player;
		_playerAdena = _player.getAdena();
		_itemList = _player.getInventory().getAvailableItems(false, true);
		_buyList = _player.getBuyList().getItems();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb7);
		writeD(_player.getObjectId());
		writeD(_playerAdena);
		
		writeD(_itemList.length); // Inventory items for potential buy
		for (L2ItemInstance item : _itemList)
		{
			writeD(item.getItemId());
			writeH(item.getEnchantLevel());
			writeD(item.getCount());
			writeD(item.getReferencePrice());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeH(item.getItem().getType2());
		}
		
		writeD(_buyList.length); // count for all items already added for buy
		for (TradeList.TradeItem item : _buyList)
		{
			writeD(item.getItem().getItemId());
			writeH(item.getEnchant());
			writeD(item.getCount());
			writeD(item.getItem().getReferencePrice());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeH(item.getItem().getType2());
			writeD(item.getPrice()); // your price
			writeD(item.getItem().getReferencePrice()); // fixed store price
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__D0_PRIVATESELLLISTBUY;
	}
}