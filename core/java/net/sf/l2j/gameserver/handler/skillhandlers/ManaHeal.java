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
package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Stats;

/**
 * This class ...
 * @version $Revision: 1.1.2.2.2.1 $ $Date: 2005/03/02 15:38:36 $
 */
public class ManaHeal implements ISkillHandler
{
	private static SkillType[] _skillIds =
	{
		SkillType.MANAHEAL,
		SkillType.MANARECHARGE
	};
	
	/**
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#useSkill(net.sf.l2j.gameserver.model.L2Character, net.sf.l2j.gameserver.model.L2Skill, net.sf.l2j.gameserver.model.L2Object[], boolean)
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		for (L2Object trg : targets)
		{
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;
			
			double mp;
			if (skill.getSkillType() == SkillType.MANARECHARGE)
			{
				mp = target.calcStat(Stats.RECHARGE_MP_RATE, skill.getPower(), null, null);
				
				// Mana recharge formula based on caster and target level difference
				int levelDiff = target.getLevel() - activeChar.getLevel();
				if (levelDiff > 5 && mp > 0)
				{
					mp -= levelDiff * (mp / 20);
					if (mp < 0)
					{
						mp = 0;
					}
				}
			}
			else
			{
				mp = skill.getPower();
			}
			
			target.setCurrentMp(mp + target.getCurrentMp());
			StatusUpdate sump = new StatusUpdate(target.getObjectId());
			sump.addAttribute(StatusUpdate.CUR_MP, (int) target.getCurrentMp());
			target.sendPacket(sump);
			
			if ((activeChar instanceof L2PcInstance) && (activeChar != target))
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S2_MP_RESTORED_BY_S1);
				sm.addString(activeChar.getName());
				sm.addNumber((int) mp);
				target.sendPacket(sm);
			}
			else
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_MP_RESTORED);
				sm.addNumber((int) mp);
				target.sendPacket(sm);
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}