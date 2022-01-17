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
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeSummonInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.util.Rnd;

/**
 * This Handles Disabler skills
 * @author _drunk_
 */
public class Disablers implements ISkillHandler
{
	protected SkillType[] _skillIds =
	{
		L2Skill.SkillType.STUN,
		L2Skill.SkillType.ROOT,
		L2Skill.SkillType.SLEEP,
		L2Skill.SkillType.CONFUSION,
		L2Skill.SkillType.AGGDAMAGE,
		L2Skill.SkillType.AGGREDUCE,
		L2Skill.SkillType.AGGREDUCE_CHAR,
		L2Skill.SkillType.AGGREMOVE,
		L2Skill.SkillType.MUTE,
		L2Skill.SkillType.FAKE_DEATH,
		L2Skill.SkillType.CONFUSE_MOB_ONLY,
		L2Skill.SkillType.MAGE_BANE,
		L2Skill.SkillType.WARRIOR_BANE,
		L2Skill.SkillType.CANCEL,
		L2Skill.SkillType.PARALYZE
	};

	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		SkillType type = skill.getSkillType();

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
			if (target.isDead() || target.isInvul())
			{
				continue;
			}

			switch (type)
			{
				case FAKE_DEATH:
				{
					// stun/fakedeath is not mdef dependent, it depends on lvl difference, target CON and power of stun
					skill.getEffects(activeChar, target);
					break;
				}

				case STUN:
				{
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
					{
						if (Formulas.getInstance().calculateSkillReflect(skill, activeChar, target))
						{
							target = activeChar;
						}

						skill.getEffects(activeChar, target);
					}

					else
					{
						if (activeChar instanceof L2PcInstance)
						{
							SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
							sm.addString(target.getName());
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}
					}
					break;
				}
				case ROOT:
				case SLEEP:
				case PARALYZE:
				{
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
					{
						if (Formulas.getInstance().calculateSkillReflect(skill, activeChar, target))
						{
							target = activeChar;
						}

						skill.getEffects(activeChar, target);
					}

					else
					{
						if (activeChar instanceof L2PcInstance)
						{
							SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
							sm.addString(target.getName());
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}
					}

					break;
				}
				case CONFUSION:
				case MUTE:
				{
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
					{
						if (Formulas.getInstance().calculateSkillReflect(skill, activeChar, target))
						{
							target = activeChar;
						}

						// stop same type effect if available
						L2Effect[] effects = target.getAllEffects();
						for (L2Effect e : effects)
						{

							if (e.getSkill().getSkillType() == type)
							{
								e.exit();
							}

						}

						skill.getEffects(activeChar, target);

					}
					else
					{
						if (activeChar instanceof L2PcInstance)
						{
							SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
							sm.addString(target.getName());
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}
					}
					break;
				}
				case CONFUSE_MOB_ONLY:
				{
					// do nothing if not on mob
					if ((target instanceof L2MonsterInstance) && Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
					{
						if (Formulas.getInstance().calculateSkillReflect(skill, activeChar, target))
						{
							target = activeChar;
						}

						// stop same type effect if available
						L2Effect[] effects = target.getAllEffects();
						for (L2Effect e : effects)
						{

							if (e.getSkill().getSkillType() == type)
							{
								e.exit();
							}

						}

						skill.getEffects(activeChar, target);

					}
					else
					{
						if (activeChar instanceof L2PcInstance)
						{
							SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
							sm.addString(target.getName());
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}
					}

					break;
				}
				case AGGDAMAGE:
				{
					if (target instanceof L2MonsterInstance)
					{
						target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, (int) ((150 * skill.getPower()) / (target.getLevel() + 7)));
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

					skill.getEffects(activeChar, target);

					L2Effect effect = activeChar.getFirstEffect(skill.getId());
					if ((effect != null) && effect.isSelfEffect())
					{
						effect.exit();
					}

					skill.getEffectsSelf(activeChar);
					break;
				}
				case AGGREDUCE:
				{
					// these skills needs to be rechecked
					if (target instanceof L2MonsterInstance)
					{
						skill.getEffects(activeChar, target);

						double aggdiff = ((L2MonsterInstance) target).getHating(activeChar) - target.calcStat(Stats.AGGRESSION, ((L2MonsterInstance) target).getHating(activeChar), target, skill);

						if (skill.getPower() > 0)
						{
							target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, null, -(int) skill.getPower());
						}
						else if (aggdiff > 0)
						{
							target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, null, -(int) aggdiff);
						}
					}
					break;
				}
				case AGGREDUCE_CHAR:
				{
					// these skills needs to be rechecked
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
					{
						if (target instanceof L2MonsterInstance)
						{
							target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, -((L2MonsterInstance) target).getHating(activeChar));
						}

						skill.getEffects(activeChar, target);
					}
					else
					{
						if (activeChar instanceof L2PcInstance)
						{
							SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
							sm.addString(target.getName());
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}
					}
					break;
				}
				case AGGREMOVE:
				{
					// these skills needs to be rechecked
					if (target instanceof L2MonsterInstance)
					{
						if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
						{
							if (skill.getTargetType() == L2Skill.SkillTargetType.TARGET_UNDEAD)
							{
								if (target.isUndead())
								{
									target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, null, -((L2MonsterInstance) target).getHating(((L2MonsterInstance) target).getMostHated()));
								}
							}
							else
							{
								target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, null, -((L2MonsterInstance) target).getHating(((L2MonsterInstance) target).getMostHated()));
							}
						}
						else
						{
							if (activeChar instanceof L2PcInstance)
							{
								SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
								sm.addString(target.getName());
								sm.addSkillName(skill);
								activeChar.sendPacket(sm);
							}
						}
					}

					break;
				}
				case MAGE_BANE:
				{
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
					{
						L2Effect[] effects = target.getAllEffects();
						for (L2Effect e : effects)
						{
							if (e.getStackType() != null && (e.getStackType().equals("mAtk") || e.getStackType().equals("mAtkSpeedUp")))
							{
								double rate = 180.0 / (1.0 + e.getSkill().getLevel());
								if (rate > 95.0)
								{
									rate = 95.0;
								}
								else if (rate < 40.0)
								{
									rate = 40.0;
								}

								if (Rnd.get(100) < rate)
								{
									// Remove effect
									e.exit();
								}
							}
						}
					}
					else
					{
						if (activeChar instanceof L2PcInstance)
						{
							SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
							sm.addString(target.getName());
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}
					}
					break;
				}
				case WARRIOR_BANE:
				{
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
					{
						L2Effect[] effects = target.getAllEffects();
						for (L2Effect e : effects)
						{
							if (e.getStackType() != null && (e.getStackType().equals("SpeedUp") || e.getStackType().equals("pAtkSpeedUp")))
							{
								double rate = 180.0 / (1.0 + e.getSkill().getLevel());
								if (rate > 95.0)
								{
									rate = 95.0;
								}
								else if (rate < 40.0)
								{
									rate = 40.0;
								}

								if (Rnd.get(100) < rate)
								{
									// Remove effect
									e.exit();
								}
							}
						}
					}
					else
					{

						if (activeChar instanceof L2PcInstance)
						{
							SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
							sm.addString(target.getName());
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}

					}
					break;
				}
				case CANCEL:
				{
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss))
					{
						// Get all skills effects on the L2Character
						L2Effect[] effects = target.getAllEffects();
						int maxToRemove = skill.getMaxNegatedEffects();

						for (L2Effect e : effects)
						{
							switch (e.getSkill().getId())
							{
								case 1323:
								case 1325:
								case 4082:
								case 4215:
								case 4515:
									continue;
							}

							double rate = 150.0 / (1.0 + e.getSkill().getLevel());
							if (rate > 75.0)
							{
								rate = 75.0;
							}
							else if (rate < 25.0)
							{
								rate = 25.0;
							}

							if (Rnd.get(100) < rate)
							{
								// Remove effect
								e.exit();
								
								/*-
								 * By DnR: Check if counter reaches 1 (before it gets reduced by 1) and stop cancelling effects.
								 * We don't check for 0 value because skills with zero max negated effects remove all buffs.
								 * It's basically the same thing allowing us to use a value for removing all buffs.
								 */
								if (maxToRemove == 1)
								{
									break;
								}
								maxToRemove--;
							}
						}
					}
					else
					{
						if (activeChar instanceof L2PcInstance)
						{
							SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
							sm.addString(target.getName());
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}
					}

					break;

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
