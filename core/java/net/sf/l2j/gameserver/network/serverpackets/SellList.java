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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MerchantInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.4.2.3.2.4 $ $Date: 2005/03/27 15:29:39 $
 */
public class SellList extends L2GameServerPacket
{
	private static final String _S__10_SELLLIST = "[S] 10 SellList";
	private static Logger _log = Logger.getLogger(SellList.class.getName());
	private final L2PcInstance _char;
	private final L2MerchantInstance _lease;
	private final int _money;
	private final List<L2ItemInstance> _list = new ArrayList<>();
	
	public SellList(L2PcInstance player)
	{
		_char = player;
		_lease = null;
		_money = _char.getAdena();
		lease();
	}
	
	public SellList(L2PcInstance player, L2MerchantInstance lease)
	{
		_char = player;
		_lease = lease;
		_money = _char.getAdena();
		lease();
	}
	
	private void lease()
	{
		if (_lease == null)
		{
			for (L2ItemInstance item : _char.getInventory().getItems())
			{
				if (!item.isEquipped() && // Not equipped
					item.getItem().isSellable() && // Item is sellable
					((_char.getPet() == null) || // Pet not summoned or
					(item.getObjectId() != _char.getPet().getControlItemObjectId()))) // Pet is summoned and not the item that summoned the pet
				{
					_list.add(item);
					if (Config.DEBUG)
					{
						_log.fine("item added to selllist: " + item.getItem().getName());
					}
				}
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x10);
		writeD(_money);
		writeD(_lease == null ? 0x00 : 1000000 + _lease.getTemplate().npcId);
		
		writeH(_list.size());
		
		for (L2ItemInstance item : _list)
		{
			writeH(item.getItem().getType1());
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(item.getCount());
			writeH(item.getItem().getType2());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeH(item.getEnchantLevel());
			writeH(0x00);
			writeH(item.getCustomType2());
			
			if (_lease == null)
			{
				writeD(item.getItem().getReferencePrice() / 2);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__10_SELLLIST;
	}
}