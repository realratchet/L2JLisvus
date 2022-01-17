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
import net.sf.l2j.util.Rnd;

/**
 * @author l3x
 */
public class Harvest implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.HARVEST
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance player = (L2PcInstance) activeChar;
		
		L2Object trg = targets[0];
		if (!(trg instanceof L2MonsterInstance))
		{
			return;
		}
		
		L2MonsterInstance target = (L2MonsterInstance) trg;
		if (player != target.getSeeder())
		{
			SystemMessage sm = new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED_TO_HARVEST);
			player.sendPacket(sm);
			return;
		}
		
		boolean send = false;
		int total = 0;
		int cropId = 0;
		
		// TODO: check items and amount of items player harvest
		if (target.isSeeded())
		{
			if (calcSuccess(player.getLevel(), target.getLevel()))
			{
				L2Attackable.RewardItem[] items = target.takeHarvest();
				if (items != null && items.length > 0)
				{
					for (L2Attackable.RewardItem item : items)
					{
						cropId = item.getItemId(); // Always got 1 type of crop as reward
						if (player.isInParty())
						{
							player.getParty().distributeItem(player, item, true, target);
						}
						else
						{
							player.addItem("Manor", item.getItemId(), item.getCount(), target, false);
							send = true;
							total += item.getCount();
						}
					}
					
					if (send)
					{
						SystemMessage smsg = new SystemMessage(SystemMessage.YOU_PICKED_UP_S1_S2);
						smsg.addNumber(total);
						smsg.addItemName(cropId);
						player.sendPacket(smsg);
						
						if (player.getParty() != null)
						{
							smsg = new SystemMessage(SystemMessage.S1_HARVESTED_S3_S2S);
							smsg.addString(player.getName());
							smsg.addNumber(total);
							smsg.addItemName(cropId);
							player.getParty().broadcastToPartyMembers(player, smsg);
						}
					}
				}
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage.THE_HARVEST_HAS_FAILED));
			}
		}
		else
		{
			player.sendPacket(new SystemMessage(SystemMessage.THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN));
		}
	}
	
	private boolean calcSuccess(int playerLevel, int targetLevel)
	{
		int basicSuccess = 100;
		
		int diff = (playerLevel - targetLevel);
		if (diff < 0)
		{
			diff = -diff;
		}
		
		// apply penalty, target <=> player levels
		// 5% penalty for each level
		if (diff > 5)
		{
			basicSuccess -= (diff - 5) * 5;
		}
		
		// success rate cannot be less than 1%
		if (basicSuccess < 1)
		{
			basicSuccess = 1;
		}
		
		int rate = Rnd.nextInt(99);
		if (rate < basicSuccess)
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}