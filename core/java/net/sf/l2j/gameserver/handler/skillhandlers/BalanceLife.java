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
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;

/**
 * @author earendil
 * @version $Revision: 1.1.2.2.2.4 $ $Date: 2005/04/06 16:13:48 $
 */

public class BalanceLife implements ISkillHandler
{
	private static SkillType[] SKILL_TYPES =
	{
		SkillType.BALANCE_LIFE
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean critOnFirstTarget)
	{
		// Check for other effects
		try
		{
			ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(SkillType.BUFF);
			if (handler != null)
			{
				handler.useSkill(activeChar, skill, targets, critOnFirstTarget);
			}
		}
		catch (Exception e)
		{
		}
		
		double fullHP = 0;
		double currentHPs = 0;
		
		for (L2Object trg : targets)
		{
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;
			
			// We should not heal if char is dead
			if (target.isDead())
			{
				continue;
			}
			
			fullHP += target.getMaxHp();
			currentHPs += target.getCurrentHp();
		}
		
		double percentHP = currentHPs / fullHP;
		
		for (L2Object target2 : targets)
		{
			L2Character target = (L2Character) target2;
			target.setCurrentHp(target.getMaxHp() * percentHP);
			
			StatusUpdate su = new StatusUpdate(target.getObjectId());
			su.addAttribute(StatusUpdate.CUR_HP, (int) target.getCurrentHp());
			target.sendPacket(su);
			
			target.sendMessage("HP of the party has been balanced.");
		}
	}
	
	@Override
	public SkillType[] getSkillTypes()
	{
		return SKILL_TYPES;
	}
}