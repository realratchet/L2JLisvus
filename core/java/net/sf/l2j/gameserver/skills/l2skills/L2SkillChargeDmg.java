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
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.BaseStats;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Log;

public class L2SkillChargeDmg extends L2Skill
{
	final int num_charges;
	
	public L2SkillChargeDmg(StatsSet set)
	{
		super(set);
		num_charges = set.getInteger("num_charges", getLevel());
	}
	
	@Override
	public boolean checkCondition(L2Character activeChar, L2Object target, boolean itemOrWeapon)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return false;
		}
		
		L2PcInstance player = (L2PcInstance) activeChar;
		
		if (player.getCharges() < num_charges)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1_CANNOT_BE_USED);
			sm.addSkillName(this);
			activeChar.sendPacket(sm);
			return false;
		}
		return super.checkCondition(activeChar, target, itemOrWeapon);
	}
	
	/**
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Skill#isCritical(L2Character, L2Character)
	 */
	@Override
	public boolean isCritical(L2Character activeChar, L2Character target)
	{
		return Formulas.getInstance().calcCrit(getBaseCritRate() * 10 * BaseStats.STR.calcBonus(activeChar));
	}
	
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean isFirstCritical)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance player = (L2PcInstance) activeChar;
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		// Formula tested by L2Guru
		double modifier = 0;
		modifier = 0.8 + (0.201 * player.getCharges());
		
		// Consume charges
		player.decreaseCharges(num_charges);
		
		L2ItemInstance weapon = activeChar.getActiveWeaponInstance();
		boolean soul = (weapon != null && weapon.getChargedSoulShot() == L2ItemInstance.CHARGED_SOULSHOT);
		
		for (int i = 0; i < targets.length; i++)
		{
			L2Object trg = targets[i];
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			L2Character target = (L2Character) trg;
			
			boolean shld = Formulas.getInstance().calcShldUse(activeChar, target);
			boolean crit = false;
			if (getBaseCritRate() > 0)
			{
				crit = i == 0 ? isFirstCritical : isCritical(activeChar, target);
			}
			
			double damage = Formulas.getInstance().calcPhysDam(activeChar, target, this, shld, false, false, soul);
			
			if (!crit && (getCondition() & COND_CRIT) != 0)
			{
				damage = 0;
			}
			
			if (crit)
			{
				damage *= 2;
			}
			
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
					Log.addGame(activeChar.getName() + "(" + activeChar.getObjectId() + ") " + activeChar.getLevel() + " lvl did damage " 
						+ damage + " with skill " + getName() + "(" + getId() + ") to " + name, "damage_pdam");
				}
				
				damage = damage * modifier;
				activeChar.sendDamageMessage(target, (int) damage, false, false, false);
				target.reduceCurrentHp(damage, activeChar);
			}
		}
	}
}
