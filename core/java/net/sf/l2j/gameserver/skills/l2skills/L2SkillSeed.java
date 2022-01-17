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
package net.sf.l2j.gameserver.skills.l2skills;

import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.skills.effects.EffectSeed;
import net.sf.l2j.gameserver.templates.StatsSet;

public class L2SkillSeed extends L2Skill
{
	public L2SkillSeed(StatsSet set)
	{
		super(set);
	}
	
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		// Update Seeds Effects
		for (L2Object obj : targets)
		{
			L2Character target = (L2Character) obj;
			if (target.isAlikeDead() && (getTargetType() != SkillTargetType.TARGET_CORPSE_MOB))
			{
				continue;
			}
			
			boolean effectExists = false;
			L2Effect[] effects = target.getAllEffects();
			for (L2Effect seed : effects)
			{
				if (seed == null)
				{
					continue;
				}
				
				if (seed.getSkill().getId() == getId())
				{
					effectExists = true;
					if (seed instanceof EffectSeed)
					{
						((EffectSeed) seed).increasePower();
					}
				}
				
				if (seed.getEffectType() == L2Effect.EffectType.SEED)
				{
					seed.rescheduleEffect();
				}
			}
			
			if (!effectExists)
			{
				getEffects(activeChar, target);
			}
		}
	}
}