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
package ai.group_template;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 * 
 * * Outlaw Forest - Ol Mahum Transcender AI.
 */
public class OutlawForest extends Quest
{
	private static final Map<Integer, Integer[]> POLYMORPHING_OLMAHUM = new HashMap<>();
	
	static
	{
		POLYMORPHING_OLMAHUM.put(1261, new Integer[]
		{
			1262,
			15
		}); // Ol Mahum Transcender Lv50 -> Lv53
		POLYMORPHING_OLMAHUM.put(1262, new Integer[]
		{
			1263,
			10
		}); // Ol Mahum Transcender Lv53 -> Lv55
		POLYMORPHING_OLMAHUM.put(1263, new Integer[]
		{
			1264,
			5
		}); // Ol Mahum Transcender Lv55 -> Lv58
	}
	
	private static final String[] NPC_STRING =
	{
		"Now the battle begins!",
		"You have more skills than i thought!",
		"Prepare to die!"
	};
	
	public static void main(String[] args)
    {
        // Quest class
        new OutlawForest();
    }
	
	public OutlawForest()
	{
		super(-1, "outlawforest", "ai/group_template");
		for (int id : POLYMORPHING_OLMAHUM.keySet())
		{
			registerNPC(id);
		}
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (skill.isOffensive() && Util.contains(targets, npc))
		{
			if (POLYMORPHING_OLMAHUM.containsKey(npc.getNpcId()))
			{
				Integer[] spawnData = POLYMORPHING_OLMAHUM.get(npc.getNpcId());
				if (Rnd.get(100) <= spawnData[1])
				{
					boolean isTargetingNpc = caster.getTarget() == npc;
					
					npc.onDecay();
					L2Attackable newNpc = (L2Attackable) addSpawn(spawnData[0], npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 0);
					
					newNpc.broadcastPacket(new NpcSay(newNpc.getObjectId(), 0, newNpc.getNpcId(), (NPC_STRING[Rnd.get(3)])));
					
					if (isTargetingNpc)
					{
						newNpc.onAction(caster);
					}
					
					newNpc.setRunning();
					
					L2Character originalCaster = isPet ? caster.getPet() : caster;
					if (originalCaster != null)
					{
						newNpc.addDamageHate(originalCaster, 0, 100);
						newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalCaster);
					}
				}
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (POLYMORPHING_OLMAHUM.containsKey(npc.getNpcId()))
		{
			Integer[] spawnData = POLYMORPHING_OLMAHUM.get(npc.getNpcId());
			if (Rnd.get(100) <= spawnData[1])
			{
				boolean isTargetingNpc = attacker.getTarget() == npc;
				
				npc.onDecay();
				L2Attackable newNpc = (L2Attackable) addSpawn(spawnData[0], npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 0);
				
				newNpc.broadcastPacket(new NpcSay(newNpc.getObjectId(), 0, newNpc.getNpcId(), (NPC_STRING[Rnd.get(3)])));
				
				if (isTargetingNpc)
				{
					newNpc.onAction(attacker);
				}
				
				newNpc.setRunning();
				
				L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
				if (originalAttacker != null)
				{
					newNpc.addDamageHate(originalAttacker, 0, 100);
					newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalAttacker);
				}
			}
		}
		
		return super.onAttack(npc, attacker, damage, isPet);
	}
}
