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
 * Format (ch)dddcc
 * @author -Wooden-
 */
public class ExFishingStartCombat extends L2GameServerPacket
{
	private static final String _S__FE_15_EXFISHINGSTARTCOMBAT = "[S] FE:15 ExFishingStartCombat";
	private final L2Character _character;
	private final int _time, _hp;
	private final int _lureType, _mode;
	
	public ExFishingStartCombat(L2Character character, int time, int hp, int mode, int lureType)
	{
		_character = character;
		_time = time;
		_hp = hp;
		_mode = mode;
		_lureType = lureType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#writeImpl()
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x15);
		
		writeD(_character.getObjectId());
		writeD(_time);
		writeD(_hp);
		
		writeC(_mode); // mode: 0 = resting, 1 = fighting
		writeC(_lureType); // 0 = newbie lure, 1 = normal lure
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_15_EXFISHINGSTARTCOMBAT;
	}
}