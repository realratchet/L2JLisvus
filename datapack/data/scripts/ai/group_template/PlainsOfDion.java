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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 * 
 * Plains Of Dion AI.
 */
public class PlainsOfDion extends Quest
{
	private static final int DELU_LIZARDMEN[] =
	{
		1104, // Delu Lizardman Supplier
		1105, // Delu Lizardman Special Agent
	};
	
	private static final String[] NPC_STRING =
	{
		"$s1! How dare you interrupt our fight ? Hey guys help!",
		"$s1! Hey, we are having a duel here!",
		"The duel is over, attack!!",
		"Foul, kill the coward!",
		"How dare you interrupt a sacred duel? You must be taught a lesson!"
	};
	
	private static final String[] NPC_STRING_ON_ASSIST =
	{
		"Die, you coward!",
		"Kill the coward!",
		"What are you looking at?!"
	};
	
	public static void main(String[] args)
    {
        // Quest class
        new PlainsOfDion();
    }
	
	public PlainsOfDion()
	{
		super(-1, "plainsofdion", "ai/group_template");
		registerNPC(1107); // Delu Lizardman Commander
	}
	
	@Override
	public String onSpawn(L2NpcInstance npc)
	{
		if (npc.getScriptValue() > 0)
		{
			npc.setScriptValue(0);
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
		if (originalAttacker == null)
		{
			return null;
		}
		
		if (npc.getScriptValue() == 0)
		{
			if (Rnd.get(100) <= 10)
			{
				List<L2MonsterInstance> minions = new ArrayList<>();
				Collection<L2Character> characters = npc.getKnownList().getKnownCharactersInRadius(300);
				for (L2Character obj : characters)
				{
					if (obj == null || !(obj instanceof L2MonsterInstance))
					{
						continue;
					}
					
					minions.add((L2MonsterInstance) obj);
				}
				
				// There are monsters who can help nearby, so call for assistance
				if (!minions.isEmpty())
				{
					npc.setScriptValue(1);
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), NPC_STRING[Rnd.get(5)], originalAttacker.getName()));
					
					for (L2MonsterInstance minion : minions)
					{
						if (Util.contains(DELU_LIZARDMEN, minion.getNpcId()))
						{
							if (!minion.isAttackingNow() && !minion.isDead() && GeoData.getInstance().canSeeTarget(minion, attacker))
							{
								minion.setTarget(attacker);
								minion.setRunning();
								
								minion.broadcastPacket(new NpcSay(minion.getObjectId(), 0, minion.getNpcId(), (NPC_STRING_ON_ASSIST[Rnd.get(3)])));
								
								minion.addDamageHate(originalAttacker, 0, 100);
								minion.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalAttacker);
							}
						}
					}
					minions.clear();
				}
			}
		}
		
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (skill.isOffensive() && Util.contains(targets, npc))
		{
			L2Character originalAttacker = isPet ? caster.getPet() : caster;
			if (originalAttacker == null)
			{
				return null;
			}
			
			if (npc.getScriptValue() == 0)
			{
				if (Rnd.get(100) <= 10)
				{
					List<L2MonsterInstance> minions = new ArrayList<>();
					Collection<L2Character> characters = npc.getKnownList().getKnownCharactersInRadius(300);
					for (L2Character obj : characters)
					{
						if (obj == null || !(obj instanceof L2MonsterInstance))
						{
							continue;
						}
						
						minions.add((L2MonsterInstance) obj);
					}
					
					// There are monsters who can help nearby, so call for assistance
					if (!minions.isEmpty())
					{
						npc.setScriptValue(1);
						npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), NPC_STRING[Rnd.get(5)], originalAttacker.getName()));
						
						for (L2MonsterInstance minion : minions)
						{
							if (Util.contains(DELU_LIZARDMEN, minion.getNpcId()))
							{
								if (!minion.isAttackingNow() && !minion.isDead() && GeoData.getInstance().canSeeTarget(minion, caster))
								{
									minion.setTarget(caster);
									minion.setRunning();
									
									minion.broadcastPacket(new NpcSay(minion.getObjectId(), 0, minion.getNpcId(), (NPC_STRING_ON_ASSIST[Rnd.get(3)])));
									
									minion.addDamageHate(originalAttacker, 0, 100);
									minion.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalAttacker);
								}
							}
						}
						minions.clear();
					}
				}
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
}
