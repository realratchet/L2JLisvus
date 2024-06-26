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
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class DrainSoul implements ISkillHandler
{
	private static final SkillType[] SKILL_TYPES =
	{
		SkillType.DRAIN_SOUL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean critOnFirstTarget)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return;
		}

        L2PcInstance player = (L2PcInstance) activeChar;
		L2Object[] targetList = skill.getTargetList(activeChar);

		if (targetList == null)
		{
			return;
		}

        for (L2Object trg : targets)
		{
			if (!(trg instanceof L2MonsterInstance))
			{
				continue;
			}
			
			L2MonsterInstance target = (L2MonsterInstance) trg;
            target.addAbsorber(player);
        }
	}
	
	@Override
	public SkillType[] getSkillTypes()
	{
		return SKILL_TYPES;
	}
}