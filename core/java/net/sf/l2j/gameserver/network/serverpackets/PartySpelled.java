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
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */
public class PartySpelled extends L2GameServerPacket
{
	private static final String _S__EE_PartySpelled = "[S] EE PartySpelled";
	private final List<Effect> _effects;
	private final L2Character _char;
	private int _extraSlot = 0;

	class Effect
	{
		int skillId;
		int dat;
		int duration;

		public Effect(int pSkillId, int pDat, int pDuration)
		{
			skillId = pSkillId;
			dat = pDat;
			duration = pDuration;
		}
	}

	public PartySpelled(L2Character cha)
	{
		_effects = new ArrayList<>();
		_char = cha;
	}

	@Override
	protected final void writeImpl()
	{
		if (_char == null)
		{
			return;
		}

		writeC(0xee);
		writeD(_char instanceof L2SummonInstance ? 2 : _char instanceof L2PetInstance ? 1 : 0);
		writeD(_char.getObjectId());
		writeD(_effects.size());

		for (Effect temp : _effects)
		{
			writeD(temp.skillId);
			writeH(temp.dat);
			writeD(temp.duration);
		}
	}

	public void addPartySpelledEffect(int skillId, int dat, int duration)
	{
		/**
		 * Override slots if effects exceed the number of buffs per slot * 3, since
		 * client cannot support more visible effects. :)
		 */
		int size = Config.BUFF_SLOTS_PER_ROW * 3;
		if (_effects.size() >= size)
		{
			_effects.set(_extraSlot++, new Effect(skillId, dat, duration));
		}
		else
		{
			_effects.add(new Effect(skillId, dat, duration));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__EE_PartySpelled;
	}
}