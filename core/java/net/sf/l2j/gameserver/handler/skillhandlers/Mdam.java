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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.util.Log;

/**
 * This class ...
 * @version $Revision: 1.1.2.8.2.9 $ $Date: 2005/04/05 19:41:23 $
 */
public class Mdam implements ISkillHandler
{
	// private static Logger _log = Logger.getLogger(Mdam.class.getName());

	private static SkillType[] _skillIds =
	{
		SkillType.MDAM,
		SkillType.DEATHLINK
	};

	/**
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#useSkill(net.sf.l2j.gameserver.model.L2Character, net.sf.l2j.gameserver.model.L2Skill, net.sf.l2j.gameserver.model.L2Object[], boolean)
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}

		boolean sps = false;
		boolean bss = false;

		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		if (weaponInst != null)
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
		// If there is no weapon equipped, check for an active summon.
		else if (activeChar instanceof L2Summon)
		{
			L2Summon activeSummon = (L2Summon) activeChar;

			if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
			{
				bss = true;
			}
			else if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
			{
				sps = true;
			}
		}
		else if (activeChar instanceof L2NpcInstance)
		{
			bss = ((L2NpcInstance) activeChar).isUsingShot(false);
			sps = ((L2NpcInstance) activeChar).isUsingShot(true);
		}

		for (int i = 0; i < targets.length; i++)
		{
			L2Object trg = targets[i];
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;

			if ((activeChar instanceof L2PcInstance) && (target instanceof L2PcInstance) && target.isFakeDeath())
			{
				target.stopFakeDeath(true);
			}
			
			// Decay targets in the case of corpse mob skills
			if (target == targets[0] && (skill.getTargetType() == SkillTargetType.TARGET_CORPSE_MOB || skill.getTargetType() == SkillTargetType.TARGET_AREA_CORPSE_MOB))
			{
				target.endDecayTask();
			}
			
			if (target.isDead())
			{
				continue;
			}

			boolean mCrit = i == 0 ? isFirstCritical : isCriticalHit(activeChar, skill, target);
			int damage = (int) Formulas.getInstance().calcMagicDam(activeChar, target, skill, sps, bss, mCrit);
			
			// Why are we trying to reduce the current target HP here?
			// Why not inside the below "if" condition, after the effects processing as it should be?
			// It doesn't seem to make sense for me. I'm moving this line inside the "if" condition, right after the effects processing...
			// [changed by nexus - 2006-08-15]
			// target.reduceCurrentHp(damage, activeChar);

			if (damage > 0)
			{
				// Logging damage
				if (Config.LOG_GAME_DAMAGE && damage > 5000 && activeChar instanceof L2PcInstance)
				{
					String name = "";
					if (target instanceof L2RaidBossInstance)
					{
						name = "RaidBoss ";
					}
					if (target instanceof L2NpcInstance)
					{
						name += target.getName() + "(" + ((L2NpcInstance) target).getTemplate().npcId + ")";
					}
					if (target instanceof L2PcInstance)
					{
						name = target.getName() + "(" + target.getObjectId() + ") ";
					}
					name += target.getLevel() + " lvl";
					Log.addGame(activeChar.getName() + "(" + activeChar.getObjectId() + ") " + activeChar.getLevel() + " lvl did damage " + damage + " with skill " + skill.getName() + "(" + skill.getId() + ") to " + name, "damage_mdam");
				}
				
				// Manage attack or cast break of the target (calculating rate, sending message...)
				if (Formulas.getInstance().calcAtkBreak(target, damage))
				{
					target.breakAttack();
					target.breakCast();
				}

				activeChar.sendDamageMessage(target, damage, mCrit, false, false);

				if (skill.hasEffects())
				{
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, false, sps, bss))
					{
						if (Formulas.getInstance().calculateSkillReflect(skill, activeChar, target))
						{
							target = activeChar;
						}

						// activate attacked effects, if any
						target.stopEffect(skill.getId());
						if (target.getFirstEffect(skill.getId()) != null)
						{
							target.removeEffect(target.getFirstEffect(skill.getId()));
						}

						skill.getEffects(activeChar, target);
					}
					else
					{
						SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
						sm.addString(target.getName());
						sm.addSkillName(skill);
						activeChar.sendPacket(sm);
					}
				}

				target.reduceCurrentHp(damage, activeChar);
			}
		}

		L2Effect effect = activeChar.getFirstEffect(skill.getId());
		if ((effect != null) && effect.isSelfEffect())
		{
			effect.exit();
		}

		skill.getEffectsSelf(activeChar);

		if (skill.isSuicideAttack())
		{
			activeChar.doDie(activeChar);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#isCriticalHit(L2Character, L2Skill, L2Character)
	 */
	@Override
	public boolean isCriticalHit(L2Character activeChar, L2Skill skill, L2Character target)
	{
		return Formulas.getInstance().calcMCrit(activeChar.getMCriticalHit(target, skill));
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}