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

import net.sf.l2j.gameserver.model.L2Object;

/**
 * format dddd sample 0000: 3a 69 08 10 48 02 c1 00 00 f7 56 00 00 89 ea ff :i..H.....V..... 0010: ff 0c b2 d8 61 ....a
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */
public class TeleportToLocation extends L2GameServerPacket
{
	private static final String _S__38_TELEPORTTOLOCATION = "[S] 28 TeleportToLocation";
	private final int _targetId;
	private final int _x;
	private final int _y;
	private final int _z;

	/**
	 * @param cha
	 * @param x 
	 * @param y 
	 * @param z 
	 */
	public TeleportToLocation(L2Object cha, int x, int y, int z)
	{
		_targetId = cha.getObjectId();
		_x = x;
		_y = y;
		_z = z;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x28);
		writeD(_targetId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__38_TELEPORTTOLOCATION;
	}
}