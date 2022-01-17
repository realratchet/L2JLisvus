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

import net.sf.l2j.Config;

/**
 * MagicEffectIcons format h (dhd)
 * @version $Revision: 1.3.2.1.2.6 $ $Date: 2005/04/05 19:41:08 $
 */
public class MagicEffectIcons extends L2GameServerPacket
{
	private static final String _S__97_MAGICEFFECTICONS = "[S] 7f MagicEffectIcons";
	private List<Effect> _effects;
	private int _extraSlot = 0;

	class Effect
	{
		int _skillId;
		int _level;
		int _duration;

		public Effect(int skillId, int level, int duration)
		{
			_skillId = skillId;
			_level = level;
			_duration = duration;
		}
	}

	public MagicEffectIcons()
	{
		_effects = new ArrayList<>();
	}

	public void addEffect(int skillId, int level, int duration)
	{
		/**
		 * Override slots if effects exceed the number of buffs per slot * 3, since
		 * client cannot support more visible effects. :)
		 */
		int size = Config.BUFF_SLOTS_PER_ROW * 3;
		if (_effects.size() >= size)
		{
			_effects.set(_extraSlot++, new Effect(skillId, level, duration));
		}
		else
		{
			_effects.add(new Effect(skillId, level, duration));
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x7f);
		writeH(_effects.size());

		for (Effect temp : _effects)
		{
			writeD(temp._skillId);
			writeH(temp._level);
			writeD(temp._duration);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__97_MAGICEFFECTICONS;
	}
}