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

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Manor;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.Rnd;

/**
 * @author l3x
 */
public class Sow implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.SOW
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
		if (target.isSeeded())
		{
			return;
		}
		
		if (target.isDead())
		{
			return;
		}
		
		if (target.getSeeder() != player)
		{
			return;
		}
		
		int seedId = target.getSeedType();
		if (seedId == 0)
		{
			return;
		}
		
		// Consuming used seed
		if (!player.destroyItemByItemId("Consume", seedId, 1, null, false))
		{
			return;
		}
		
		SystemMessage sm = null;
		if (calcSuccess(seedId, player.getLevel(), target.getLevel()))
		{
			player.sendPacket(new PlaySound("Itemsound.quest_itemget"));
			target.setSeeded();
			sm = new SystemMessage(SystemMessage.THE_SEED_WAS_SUCCESSFULLY_SOWN);
		}
		else
		{
			sm = new SystemMessage(SystemMessage.THE_SEED_WAS_NOT_SOWN);
		}
		
		if (!player.isInParty())
		{
			player.sendPacket(sm);
		}
		else
		{
			player.getParty().broadcastToPartyMembers(sm);
		}
		
		target.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
	}
	
	private boolean calcSuccess(int seedId, int playerLevel, int targetLevel)
	{
		// TODO: check all the chances
		int basicSuccess = (L2Manor.getInstance().isAlternative(seedId) ? 20 : 90);
		int minlevelSeed = 0;
		int maxlevelSeed = 0;
		minlevelSeed = L2Manor.getInstance().getSeedMinLevel(seedId);
		maxlevelSeed = L2Manor.getInstance().getSeedMaxLevel(seedId);
		
		// 5% decrease in chance if player level
		// is more then +/- 5 levels to _seed's_ level
		if (targetLevel < minlevelSeed)
		{
			basicSuccess -= 5;
		}
		if (targetLevel > maxlevelSeed)
		{
			basicSuccess -= 5;
		}
		
		// 5% decrease in chance if player level
		// is more than +/- 5 levels to _target's_ level
		int diff = (playerLevel - targetLevel);
		if (diff < 0)
		{
			diff = -diff;
		}
		
		if (diff > 5)
		{
			basicSuccess -= 5 * (diff - 5);
		}
		
		// Chance can't be less than 1%
		if (basicSuccess < 1)
		{
			basicSuccess = 1;
		}
		
		int rate = Rnd.nextInt(99);
		return (rate < basicSuccess);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}