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
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 * @author _tomciaaa_
 */
public class StrSiegeAssault implements ISkillHandler
{
	protected SkillType[] _skillIds =
	{
		SkillType.STRSIEGEASSAULT
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if ((activeChar == null) || !(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		if (!activeChar.isRiding())
		{
			return;
		}
		
		// Damage calculation
		int damage = 0;
		
		for (int i = 0; i < targets.length; i++)
		{
			L2Object trg = targets[i];
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;
			if (!(target instanceof L2DoorInstance))
			{
				return;
			}
			
			L2ItemInstance weapon = activeChar.getActiveWeaponInstance();
			boolean dual = activeChar.isUsingDualWeapon();
			boolean shld = Formulas.getInstance().calcShldUse(activeChar, target);
			boolean crit = (i == 0 ? isFirstCritical : isCriticalHit(activeChar, skill, target));
			boolean soul = (weapon != null && weapon.getChargedSoulShot() == L2ItemInstance.CHARGED_SOULSHOT);
			
			damage = (int) Formulas.getInstance().calcPhysDam(activeChar, target, skill, shld, crit, dual, soul);
			if (damage > 0)
			{
				activeChar.sendDamageMessage(target, damage, false, false, false);
				target.reduceCurrentHp(damage, activeChar);
			}
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#isCriticalHit(L2Character, L2Skill, L2Character)
	 */
	@Override
	public boolean isCriticalHit(L2Character activeChar, L2Skill skill, L2Character target)
	{
		return Formulas.getInstance().calcCrit(activeChar.getCriticalHit(target, skill));
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}