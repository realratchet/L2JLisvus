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
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author _drunk_
 */
public class Sweep implements ISkillHandler
{
	// private static Logger _log = Logger.getLogger(Sweep.class.getName());
	protected SkillType[] _skillIds =
	{
		SkillType.SWEEP
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance player = (L2PcInstance) activeChar;
		
		for (L2Object trg : targets)
		{
			if (!(trg instanceof L2MonsterInstance))
			{
				continue;
			}
			
			L2MonsterInstance target = (L2MonsterInstance) trg;
			L2Attackable.RewardItem[] items = null;
			boolean isSweeping = false;
			
			synchronized (target)
			{
				if (target.isSweepActive())
				{
					items = target.takeSweep();
					isSweeping = true;
				}
			}
			
			if (isSweeping)
			{
				if (items == null || items.length == 0)
				{
					continue;
				}
				
				for (L2Attackable.RewardItem item : items)
				{
					if (player.isInParty())
					{
						player.getParty().distributeItem(player, item, true, target);
					}
					else
					{
						player.addItem("Sweep", item.getItemId(), item.getCount(), target, false);
						
						SystemMessage sm;
						if (item.getCount() > 1)
						{
							sm = new SystemMessage(SystemMessage.EARNED_S2_S1_s); // earned $s2$s1
							sm.addItemName(item.getItemId());
							sm.addNumber(item.getCount());
						}
						else
						{
							sm = new SystemMessage(SystemMessage.EARNED_ITEM); // earned $s1
							sm.addItemName(item.getItemId());
						}
						player.sendPacket(sm);
					}
				}
			}
			
			target.setSpoil(false);
			target.endDecayTask();
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}