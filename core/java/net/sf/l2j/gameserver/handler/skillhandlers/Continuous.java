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

import net.sf.l2j.gameserver.ai.CtrlEvent;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeSummonInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 * This class ...
 * @version $Revision: 1.1.2.2.2.9 $ $Date: 2005/04/03 15:55:04 $
 */
public class Continuous implements ISkillHandler
{
	private static SkillType[] _skillIds =
	{
		L2Skill.SkillType.BUFF,
		L2Skill.SkillType.DEBUFF,
		L2Skill.SkillType.DOT,
		L2Skill.SkillType.MDOT,
		L2Skill.SkillType.POISON,
		L2Skill.SkillType.BLEED,
		L2Skill.SkillType.HOT,
		L2Skill.SkillType.CPHOT,
		L2Skill.SkillType.MPHOT,
		L2Skill.SkillType.FEAR,
		L2Skill.SkillType.CONT,
		L2Skill.SkillType.WEAKNESS,
		L2Skill.SkillType.REFLECT,
		L2Skill.SkillType.UNDEAD_DEFENSE,
		L2Skill.SkillType.AGGDEBUFF,
		L2Skill.SkillType.NEGATE
	};
	
	/**
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#useSkill(L2Character, L2Skill, L2Object[], boolean)
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		boolean ss = false;
		boolean sps = false;
		boolean bss = false;

		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		if (weaponInst != null)
		{
			if (skill.useSpiritShot())
			{
				if (weaponInst.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
				{
					bss = true;
				}
				else if (weaponInst.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
				{
					sps = true;
				}
			}
			else if (skill.useSoulShot())
			{
				if (weaponInst.getChargedSoulShot() == L2ItemInstance.CHARGED_SOULSHOT)
				{
					ss = true;
				}
			}
		}
		// If there is no weapon equipped, check for an active summon.
		else if (activeChar instanceof L2Summon)
		{
			L2Summon activeSummon = (L2Summon) activeChar;
			if (skill.useSpiritShot())
			{
				if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
				{
					bss = true;
				}
				else if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
				{
					sps = true;
				}
			}
			else if (skill.useSoulShot())
			{
				if (activeSummon.getChargedSoulShot() == L2ItemInstance.CHARGED_SOULSHOT)
				{
					ss = true;
				}
			}
		}
		else if (activeChar instanceof L2NpcInstance)
		{
			bss = ((L2NpcInstance) activeChar).isUsingShot(false);
			ss = ((L2NpcInstance) activeChar).isUsingShot(true);
		}
		
		for (L2Object trg : targets)
		{
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;
			
			if (skill.isOffensive())
			{
				// Decay targets in the case of corpse mob skills
				if (target == targets[0] && (skill.getTargetType() == SkillTargetType.TARGET_CORPSE_MOB || skill.getTargetType() == SkillTargetType.TARGET_AREA_CORPSE_MOB))
				{
					target.endDecayTask();
				}
				
				boolean acted = Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss);
				if (!acted)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.ATTACK_FAILED));
					continue;
				}
				
				if (skill.hasEffects())
				{
					if (Formulas.getInstance().calculateSkillReflect(skill, activeChar, target))
					{
						target = activeChar;
					}
				}
			}
			
			if (skill.isToggle() || skill.isLevelStackable())
			{
				L2Effect[] effects = target.getAllEffects();
				if (effects != null)
				{
					for (L2Effect e : effects)
					{
						if (e == null)
						{
							continue;
						}
						
						if (e.getSkill().getId() == skill.getId())
						{
							if (skill.isToggle())
							{
								e.exit();
								return;
							}
							
							// Level-stackable effects level increment (e.g. Hot Springs Diseases)
							if (e.getSkill().getLevel() < skill.getMaxStackableLevel())
							{
								L2Skill temp = SkillTable.getInstance().getInfo(skill.getId(), e.getSkill().getLevel() + 1);
								if (temp != null)
								{
									skill = temp;
									e.exit();
								}
							}
							break;
						}
					}
				}
			}

			// Negate effects
			skill.doNegate(target);
			
			// Do the most important check before sending the message
			if (skill.getEffects(activeChar, target).length > 0)
			{
				if (!skill.isOffensive())
				{
					SystemMessage smsg = new SystemMessage(SystemMessage.YOU_FEEL_S1_EFFECT);
					smsg.addString(skill.getName());
					target.sendPacket(smsg);
				}
			}
			
			if (skill.getSkillType() == L2Skill.SkillType.AGGDEBUFF)
			{
				if (target instanceof L2MonsterInstance)
				{
					target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, (int) skill.getPower());
				}
				// Aggression for playable characters
				else if ((target instanceof L2PlayableInstance) && !(target instanceof L2SiegeSummonInstance))
				{
					if (activeChar != target)
					{
						// If target hasn't targeted caster yet, target him now
						if (target.getTarget() != activeChar)
						{
							target.setTarget(activeChar);
						}
						// Else attack caster
						else
						{
							target.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, activeChar);
						}
					}
				}
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}