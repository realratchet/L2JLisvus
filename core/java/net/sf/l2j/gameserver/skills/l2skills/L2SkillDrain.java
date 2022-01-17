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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2CubicInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Log;

public class L2SkillDrain extends L2Skill
{
	private final float _absorbPart;
	private final int _absorbAbs;

	public L2SkillDrain(StatsSet set)
	{
		super(set);

		_absorbPart = set.getFloat("absorbPart", 0.f);
		_absorbAbs = set.getInteger("absorbAbs", 0);
	}

	/**
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Skill#isCritical(L2Character, L2Character)
	 */
	@Override
	public boolean isCritical(L2Character activeChar, L2Character target)
	{
		return Formulas.getInstance().calcMCrit(activeChar.getMCriticalHit(target, this));
	}
	
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}

		boolean ss = false;
		boolean sps = false;
		boolean bss = false;

		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		if (weaponInst != null)
		{
			if (useSpiritShot())
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
			else if (useSoulShot())
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
			if (useSpiritShot())
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
			else if (useSoulShot())
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

			boolean mCrit = i == 0 ? isFirstCritical : isCritical(activeChar, target);
			int damage = (int) Formulas.getInstance().calcMagicDam(activeChar, target, this, sps, bss, mCrit);

			// No drain effect on invulnerable chars unless they cast it themselves.
			if (activeChar == target || !target.isInvul())
			{
				int _drain = 0;
				int _cp = (int) target.getCurrentCp();
				int _hp = (int) target.getCurrentHp();

				if (_cp > 0)
				{
					if (damage < _cp)
					{
						_drain = 0;
					}
					else
					{
						_drain = damage - _cp;
					}
				}

				else if (damage > _hp)
				{
					_drain = _hp;
				}
				else
				{
					_drain = damage;
				}

				double hpAdd = _absorbAbs + (_absorbPart * _drain);
				double hp = ((activeChar.getCurrentHp() + hpAdd) > activeChar.getMaxHp() ? activeChar.getMaxHp() : (activeChar.getCurrentHp() + hpAdd));

				activeChar.setCurrentHp(hp);

				StatusUpdate suhp = new StatusUpdate(activeChar.getObjectId());
				suhp.addAttribute(StatusUpdate.CUR_HP, (int) hp);
				activeChar.sendPacket(suhp);
			}

			// Decay targets in the case of corpse mob skills
			if (target == targets[0] && (getTargetType() == SkillTargetType.TARGET_CORPSE_MOB || getTargetType() == SkillTargetType.TARGET_AREA_CORPSE_MOB))
			{
				target.endDecayTask();
			}
			
			// Check to see if we should damage the target
			if (damage > 0 && !target.isDead() && getTargetType() != SkillTargetType.TARGET_CORPSE_MOB)
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
					Log.addGame(activeChar.getName() + "(" + activeChar.getObjectId() + ") " + activeChar.getLevel() + " lvl did damage " 
						+ damage + " with skill " + getName() + "(" + getId() + ") to " + name, "damage_mdam");
				}
				
				activeChar.sendDamageMessage(target, damage, mCrit, false, false);
				target.reduceCurrentHp(damage, activeChar);

				if (Formulas.getInstance().calcAtkBreak(target, damage))
				{
					target.breakAttack();
					target.breakCast();
				}
				
				if (hasEffects())
                {
					if (Formulas.getInstance().calcSkillSuccess(activeChar, target, this, ss, sps, bss))
					{
						if (Formulas.getInstance().calculateSkillReflect(this, activeChar, target))
						{
							target = activeChar;
						}
						
						getEffects(activeChar, target);
					}
					else
					{
						SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
						sm.addString(target.getName());
						sm.addSkillName(this);
						activeChar.sendPacket(sm);
					}
                }
			}
		}
	}
	
	public void useCubicSkill(L2CubicInstance activeCubic, L2Object[] targets)
    {
		final L2PcInstance owner = activeCubic.getOwner();
        for (L2Object obj : targets)
        {
        	if (!(obj instanceof L2Character))
			{
				continue;
			}
        	L2Character target = (L2Character) obj;

			boolean mcrit = Formulas.getInstance().calcMCrit(activeCubic.getOwner().getMCriticalHit(target, this));
			int damage = (int)Formulas.getInstance().calcMagicDam(activeCubic, target, this, mcrit);
			
			double hpAdd = _absorbAbs + _absorbPart * damage;
			double hp = ((owner.getCurrentHp() + hpAdd) > owner.getMaxHp() ? owner.getMaxHp() : (owner.getCurrentHp() + hpAdd));

            owner.setCurrentHp(hp);
            
			StatusUpdate suhp = new StatusUpdate(owner.getObjectId());
			suhp.addAttribute(StatusUpdate.CUR_HP, (int)hp);
			owner.sendPacket(suhp);
			
            // Check to see if we should damage the target
            if (damage > 0 && !target.isDead() && getTargetType() != SkillTargetType.TARGET_CORPSE_MOB)
            {
            	owner.sendDamageMessage(target, damage, mcrit, false, false);
    			target.reduceCurrentHp(damage, activeCubic.getOwner());
                
                if (Formulas.getInstance().calcAtkBreak(target, damage))
                {
                    target.breakAttack();
                    target.breakCast();
                }
            }
		}
	}
}