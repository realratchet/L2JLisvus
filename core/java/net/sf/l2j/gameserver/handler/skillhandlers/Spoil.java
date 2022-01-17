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
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 * @author _drunk_
 */
public class Spoil implements ISkillHandler
{
	// private static Logger _log = Logger.getLogger(Spoil.class.getName());
	protected SkillType[] _skillIds =
	{
		SkillType.SPOIL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		if (targets == null)
		{
			return;
		}
		
		for (L2Object trg : targets)
		{
			if (!(trg instanceof L2MonsterInstance))
			{
				continue;
			}
			
			L2MonsterInstance target = (L2MonsterInstance) trg;
			
			if (target.isSpoil())
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.ALREADY_SPOILED));
				continue;
			}
			
			// SPOIL SYSTEM by Lbaldi
			boolean spoil = false;
			if (target.isDead() == false)
			{
				spoil = Formulas.getInstance().calcMagicSuccess(activeChar, target, skill);
				
				if (spoil)
				{
					target.setSpoil(true);
					target.setIsSpoiledBy(activeChar.getObjectId());
					activeChar.sendPacket(new SystemMessage(SystemMessage.SPOIL_SUCCESS));
				}
				else
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
					sm.addString(target.getName());
					sm.addSkillName(skill);
					activeChar.sendPacket(sm);
				}
				target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}
