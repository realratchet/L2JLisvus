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

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 * This class ...
 * @version $Revision: 1.1.2.5.2.4 $ $Date: 2005/04/03 15:55:03 $
 */
public class Resurrect implements ISkillHandler
{
	private static SkillType[] _skillIds =
	{
		SkillType.RESURRECT
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		L2PcInstance player = null;
		if (activeChar instanceof L2PcInstance)
		{
			player = (L2PcInstance) activeChar;
		}
		
		if (player == null)
		{
			return;
		}
		
		if (player.isFestivalParticipant())
		{
			player.sendMessage("Resurrection inside festival is prohibited.");
			return;
		}
		
		if (player.isInOlympiadMode())
		{
			player.sendMessage("You cannot use this skill during an Olympiad match.");
			return;
		}
		
		List<L2Character> targetToRes = new ArrayList<>();
		for (L2Object trg : targets)
		{
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;
			
			if (target instanceof L2PcInstance)
			{
				// Check for same party or for same clan, if target is for clan.
				if (skill.getTargetType() == SkillTargetType.TARGET_CORPSE_CLAN)
				{
					if (player.getClanId() != ((L2PcInstance) target).getClanId())
					{
						continue;
					}
				}
			}
			
			if (target.isVisible())
			{
				targetToRes.add(target);
			}
		}
		
		if (targetToRes.isEmpty())
		{
			activeChar.abortCast(true);
			if (activeChar instanceof L2PcInstance)
			{
				((L2PcInstance) activeChar).sendMessage("No valid target to resurrect.");
			}
		}
		
		for (L2Character cha : targetToRes)
		{
			if (activeChar instanceof L2PcInstance)
			{
				if (cha instanceof L2PcInstance)
				{
					((L2PcInstance) cha).reviveRequest((L2PcInstance) activeChar, skill, false);
				}
				else if (cha instanceof L2PetInstance)
				{
					if (((L2PetInstance) cha).getOwner() == activeChar)
					{
						cha.doRevive(Formulas.getInstance().calculateSkillResurrectRestorePercent(skill.getPower(), activeChar));
					}
					else
					{
						((L2PetInstance) cha).getOwner().reviveRequest((L2PcInstance) activeChar, skill, true);
					}
				}
				else
				{
					cha.doRevive(Formulas.getInstance().calculateSkillResurrectRestorePercent(skill.getPower(), activeChar));
				}
			}
			else
			{
				cha.doRevive(Formulas.getInstance().calculateSkillResurrectRestorePercent(skill.getPower(), activeChar));
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}