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
import net.sf.l2j.gameserver.ai.CtrlEvent;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.util.Log;

/**
 * @author Steuf
 */
public class Blow implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.BLOW
	};
	
	public final static byte FRONT = 50;
	public final static byte SIDE = 60;
	public final static byte BEHIND = 70;
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		for (int i = 0; i < targets.length; i++)
		{
			L2Object trg = targets[i];
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;
			
			if (target.isAlikeDead())
			{
				continue;
			}

			// For the first target, it's already calculated
			boolean success = i == 0 ? isFirstCritical : isCriticalHit(activeChar, skill, target);
			if (success)
			{
				L2ItemInstance weapon = activeChar.getActiveWeaponInstance();
				boolean soul = (weapon != null && weapon.getChargedSoulShot() == L2ItemInstance.CHARGED_SOULSHOT);
				boolean shld = Formulas.getInstance().calcShldUse(activeChar, target);
				
				double damage = (int) Formulas.getInstance().calcBlowDamage(activeChar, target, skill, shld, soul);
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
						Log.addGame(activeChar.getName() + "(" + activeChar.getObjectId() + ") " + activeChar.getLevel() + " lvl did damage " + damage + " with skill " + skill.getName() + "(" + skill.getId() + ") to " + name, "damage_pdam");
					}
					
					// Manage attack or cast break of the target (calculating rate, sending message...)
					if (Formulas.getInstance().calcAtkBreak(target, damage))
					{
						target.breakAttack();
						target.breakCast();
					}
				}
				
				L2PcInstance player = activeChar.getActingPlayer();
				if (player != null)
				{
					if (player.isInOlympiadMode() && target instanceof L2PcInstance)
					{
						player.dmgDealt += damage;
					}
					
					player.sendPacket(new SystemMessage(SystemMessage.CRITICAL_HIT));
					SystemMessage sm = new SystemMessage(SystemMessage.YOU_DID_S1_DMG);
					sm.addNumber((int) damage);
					player.sendPacket(sm);
				}
				target.reduceCurrentHp(damage, activeChar);
			}
			
			if (skill.getId() == 344)
			{
				Formulas.getInstance().calcLethalStrike(activeChar, target, skill.getMagicLevel());
			}
			
			// Self Effect
			L2Effect effect = activeChar.getFirstEffect(skill.getId());
			if ((effect != null) && effect.isSelfEffect())
			{
				effect.exit();
			}
			
			skill.getEffectsSelf(activeChar);
			
			// notify the AI that it is attacked
			target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#isCriticalHit(L2Character, L2Skill, L2Character)
	 */
	@Override
	public boolean isCriticalHit(L2Character activeChar, L2Skill skill, L2Character target)
	{
		boolean success = true;
		
		byte _successChance = SIDE;
		
		if (activeChar.isBehindTarget())
		{
			_successChance = BEHIND;
		}
		else if (activeChar.isInFrontOfTarget())
		{
			_successChance = FRONT;
		}
		
		// If skill requires critical or skill requires behind,
		// calculate chance based on DEX, Position and on self BUFF
		if ((skill.getCondition() & L2Skill.COND_BEHIND) != 0)
		{
			success = (_successChance == BEHIND);
		}
		
		if ((skill.getCondition() & L2Skill.COND_CRIT) != 0)
		{
			success = (success && Formulas.getInstance().calcBlow(activeChar, target, _successChance));
		}
		
		return success;
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}