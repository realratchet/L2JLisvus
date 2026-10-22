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
package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;

public class RequestBuyProcure extends L2GameClientPacket
{
	private static final String _C__C3_REQUESTBUYPROCURE = "[C] C3 RequestBuyProcure";
	@SuppressWarnings("unused")
	private int _listId;
	private int _count;
	private int[] _items;

	@Override
	protected void readImpl()
	{
		_listId = readD();
		_count = readD();
		if ((_count < 1) || (_count > 500)) // protect server
		{
			_count = 0;
			return;
		}

		_items = new int[_count * 2];
		for (int i = 0; i < _count; i++)
		{
			readD(); // service
			int itemId = readD();
			_items[(i * 2) + 0] = itemId;
			int count = readD();
			if (count < 1)
			{
				_count = 0;
				_items = null;
				return;
			}
			_items[(i * 2) + 1] = count;
		}
	}

	@Override
	public void runImpl()
	{
		// Unused by the C4 client flow (SellListProcure is never sent); crop sales go through RequestProcureCropList.
		// The old handler had no range, procure amount or price checks, so it is intentionally a no-op.
		sendPacket(ActionFailed.STATIC_PACKET);
	}

	@Override
	public String getType()
	{
		return _C__C3_REQUESTBUYPROCURE;
	}
}