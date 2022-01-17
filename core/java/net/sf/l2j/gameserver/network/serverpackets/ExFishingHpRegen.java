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
 * Format (ch)dddcccd d: character oid d: time left d: fish hp c: c: c: 00 if fish gets damage 02 if fish regens d:
 * @author -Wooden-
 */
public class ExFishingHpRegen extends L2GameServerPacket
{
	private static final String _S__FE_16_EXFISHINGHPREGEN = "[S] FE:16 ExFishingHpRegen";
	private final L2Character _character;
	private final int _time, _fishHP, _hpMode, _anim, _goodUse, _penalty;
	
	public ExFishingHpRegen(L2Character character, int time, int fishHP, int HPmode, int GoodUse, int anim, int penalty)
	{
		_character = character;
		_time = time;
		_fishHP = fishHP;
		_hpMode = HPmode;
		_goodUse = GoodUse;
		_anim = anim;
		_penalty = penalty;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#writeImpl()
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x16);
		
		writeD(_character.getObjectId());
		writeD(_time);
		writeD(_fishHP);
		writeC(_hpMode); // HP raise -1 stop - 0
		writeC(_goodUse); // its 1 when skill is correct used
		writeC(_anim); // Anim - 1 realing, 2 - pumping, 0 - none
		writeD(_penalty); // Penalty
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_16_EXFISHINGHPREGEN;
	}
}