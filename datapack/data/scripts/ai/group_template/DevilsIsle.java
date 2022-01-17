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
 * @author DnR
 */
public class DevilsIsle extends Quest
{
	private static final Map<Integer, Integer[]> POLYMORPHING_SPAWNS = new HashMap<>();
	
	static
	{
		// C4 - confirmed polymorphs
		POLYMORPHING_SPAWNS.put(833, new Integer[]{1605, 10}); // Zaken's Archer -> Zaken's Archer
		POLYMORPHING_SPAWNS.put(1605, new Integer[]{1606, 5}); // Zaken's Archer -> Zaken's Archer
		POLYMORPHING_SPAWNS.put(835, new Integer[]{1608, 10}); // Zaken's Seer -> Zaken's Watchman
		POLYMORPHING_SPAWNS.put(1608, new Integer[]{1609, 5}); // Zaken's Watchman -> Zaken's Watchman
		POLYMORPHING_SPAWNS.put(839, new Integer[]{1611, 10}); // Unpleasant Humming -> Unpleasant Humming
		POLYMORPHING_SPAWNS.put(1611, new Integer[]{1612, 5}); // Unpleasant Humming -> Unpleasant Humming
		POLYMORPHING_SPAWNS.put(840, new Integer[]{1614, 10}); // Death Flyer -> Death Flyer
		POLYMORPHING_SPAWNS.put(1614, new Integer[]{1615, 5}); // Death Flyer -> Death Flyer
		POLYMORPHING_SPAWNS.put(832, new Integer[]{1602, 10}); // Zaken's Pikeman -> Zaken's Pikeman
		POLYMORPHING_SPAWNS.put(1602, new Integer[]{1603, 5}); // Zaken's Pikeman -> Zaken's Pikeman
		POLYMORPHING_SPAWNS.put(842, new Integer[]{1620, 10}); // Musveren ->Musveren
		POLYMORPHING_SPAWNS.put(1620, new Integer[]{1621, 5}); // Musveren ->Musveren
		POLYMORPHING_SPAWNS.put(841, new Integer[]{1617, 10}); // Fiend Archer -> Fiend Archer
		POLYMORPHING_SPAWNS.put(1617, new Integer[]{1618, 5}); // Fiend Archer -> Fiend Archer
		POLYMORPHING_SPAWNS.put(844, new Integer[]{1626, 10}); // Kaim Vanul -> Kaim Vanul
		POLYMORPHING_SPAWNS.put(1626, new Integer[]{1627, 5}); // Kaim Vanul -> Kaim Vanul
		POLYMORPHING_SPAWNS.put(845, new Integer[]{1629, 10}); // Pirate's Zombie Captain -> Pirate's Zombie Captain
		POLYMORPHING_SPAWNS.put(1629, new Integer[]{1630, 5}); // Pirate's Zombie Captain -> Pirate's Zombie Captain
		POLYMORPHING_SPAWNS.put(846, new Integer[]{1632, 10}); // Doll Blader -> Doll Blader
		POLYMORPHING_SPAWNS.put(1632, new Integer[]{1633, 5}); // Doll Blader -> Doll Blader
		POLYMORPHING_SPAWNS.put(847, new Integer[]{1635, 10}); // Vale Master -> Vale Master
		POLYMORPHING_SPAWNS.put(1635, new Integer[]{1636, 5}); // Vale Master -> Vale Master
		// Need to check : 1623 Zaken's Guard - 1624 Zaken's Guard - 1625 Zaken's Elite Guard
	}
	
	private static final String[] NPC_STRING =
	{
		"Now the battle begins!",
		"You have more skills than i thought!",
		"You idiot i have just been toying with you.",
		"Witness my true power!"
	};
	
	public static void main(String[] args)
    {
        // Quest class
        new DevilsIsle();
    }
	
	public DevilsIsle()
	{
		super(-1, "devilsisle", "ai/group_template");
		// Register NPCs
		for (int id : POLYMORPHING_SPAWNS.keySet())
		{
			registerNPC(id);
		}
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (skill.isOffensive() && Util.contains(targets, npc))
		{
			if (POLYMORPHING_SPAWNS.containsKey(npc.getNpcId()))
			{
				Integer[] spawnData = POLYMORPHING_SPAWNS.get(npc.getNpcId());
				if (Rnd.get(100) <= spawnData[1] && npc.getCurrentHp() / npc.getMaxHp() < 0.5)
				{
					boolean isTargetingNpc = caster.getTarget() == npc;
					
					npc.onDecay();
					L2Attackable newNpc = (L2Attackable) addSpawn(spawnData[0], npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 0);
					
					newNpc.broadcastPacket(new NpcSay(newNpc.getObjectId(), 0, newNpc.getNpcId(), (NPC_STRING[Rnd.get(4)])));
					
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
		if (POLYMORPHING_SPAWNS.containsKey(npc.getNpcId()))
		{
			Integer[] spawnData = POLYMORPHING_SPAWNS.get(npc.getNpcId());
			if (Rnd.get(100) <= spawnData[1] && npc.getCurrentHp() / npc.getMaxHp() < 0.5)
			{
				boolean isTargetingNpc = attacker.getTarget() == npc;
				
				npc.onDecay();
				L2Attackable newNpc = (L2Attackable) addSpawn(spawnData[0], npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 0);
				
				newNpc.broadcastPacket(new NpcSay(newNpc.getObjectId(), 0, newNpc.getNpcId(), (NPC_STRING[Rnd.get(4)])));
				
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
