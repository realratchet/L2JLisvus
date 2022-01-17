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

import net.sf.l2j.gameserver.model.L2Character;

/**
 * Format (ch)ddddd
 * @author -Wooden-
 */
public class ExFishingStart extends L2GameServerPacket
{
	private static final String _S__FE_13_EXFISHINGSTART = "[S] FE:13 ExFishingStart";
	private final L2Character _character;
	private final int _x, _y, _z, _fishType;
	
	public ExFishingStart(L2Character character, int fishType, int x, int y, int z)
	{
		_character = character;
		_fishType = fishType;
		_x = x;
		_y = y;
		_z = z;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#writeImpl()
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x13);
		writeD(_character.getObjectId());
		writeD(_fishType); // fish type
		writeD(_x); // x poison
		writeD(_y); // y poison
		writeD(_z); // z poison
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_13_EXFISHINGSTART;
	}
}