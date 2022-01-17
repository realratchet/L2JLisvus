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
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 *
 */
public class ValleyOfSaints extends Quest
{
	private static final Map<Integer,Integer[]> CLONEABLE_SPAWNS = new HashMap<>();
	private static final Map<Integer,Integer[]> POLYMORPHING_SPAWNS = new HashMap<>();
	
	static
    {
		// Cloneable spawns
		CLONEABLE_SPAWNS.put(1524, new Integer[]{1525, 30}); // Blade of Splendor
		CLONEABLE_SPAWNS.put(1531, new Integer[]{1658, 30}); // Punishment of Splendor
		CLONEABLE_SPAWNS.put(1539, new Integer[]{1540, 30}); // Wailing of Splendor
		
		// Polymorphing spawns
		POLYMORPHING_SPAWNS.put(1521, new Integer[]{1522, 30}); // Claws of Splendor
		POLYMORPHING_SPAWNS.put(1527, new Integer[]{1528, 30}); // Anger of Splendor
		POLYMORPHING_SPAWNS.put(1533, new Integer[]{1534, 30}); // Alliance of Splendor
		POLYMORPHING_SPAWNS.put(1537, new Integer[]{1538, 30}); // Fang of Splendor
    }
	
	public static void main(String[] args)
    {
        // Quest class
        new ValleyOfSaints();
    }
	
	public ValleyOfSaints()
	{
		super(-1, "valleyofsaints", "ai/group_template");
		// Register NPCs
		for (int id : CLONEABLE_SPAWNS.keySet())
		{
			registerNPC(id);
		}
		for (int id : POLYMORPHING_SPAWNS.keySet())
		{
			registerNPC(id);
		}
	}

	@Override
    public String onSpawn(L2NpcInstance npc)
    {
		// Reset script value
        if (npc.getScriptValue() > 0)
        {
        	npc.setScriptValue(0);
        }
        return super.onSpawn(npc);
    }
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (skill.isOffensive() && Util.contains(targets, npc))
		{
			if (CLONEABLE_SPAWNS.containsKey(npc.getNpcId()))
			{
				Integer[] spawnData = CLONEABLE_SPAWNS.get(npc.getNpcId());
				if (npc.getScriptValue() == 0 && Rnd.get(100) < spawnData[1])
				{
					L2Attackable newNpc = (L2Attackable) addSpawn (spawnData[0], npc.getX()+10, npc.getY()+10, npc.getZ()+10, npc.getHeading(), false, 0);
					npc.setScriptValue(1);
					newNpc.addDamageHate(caster, 0, 100);
					newNpc.setRunning();
					newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, caster);
				}
			}
			else if (POLYMORPHING_SPAWNS.containsKey(npc.getNpcId()))
			{
				Integer[] spawnData = POLYMORPHING_SPAWNS.get(npc.getNpcId());
				if (Rnd.get(100) < spawnData[1])
				{
					boolean isTargetingNpc = caster.getTarget() == npc;
					
					npc.onDecay();
					L2Attackable newNpc = (L2Attackable) addSpawn (1522, npc.getX()+10, npc.getY()+10, npc.getZ()+10, npc.getHeading(), false, 0);
					
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
	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (CLONEABLE_SPAWNS.containsKey(npc.getNpcId()))
		{
			Integer[] spawnData = CLONEABLE_SPAWNS.get(npc.getNpcId());
			if (npc.getScriptValue() == 0 && Rnd.get(100) < spawnData[1])
			{
				L2Attackable newNpc = (L2Attackable) addSpawn (spawnData[0], npc.getX()+10, npc.getY()+10, npc.getZ()+10, npc.getHeading(), false, 0);
				npc.setScriptValue(1);
				newNpc.addDamageHate(attacker, 0, 100);
				newNpc.setRunning();
				newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
			}
		}
		else if (POLYMORPHING_SPAWNS.containsKey(npc.getNpcId()))
		{
			Integer[] spawnData = POLYMORPHING_SPAWNS.get(npc.getNpcId());
			if (Rnd.get(100) < spawnData[1])
			{
				boolean isTargetingNpc = attacker.getTarget() == npc;
				
				npc.onDecay();
				L2Attackable newNpc = (L2Attackable) addSpawn (1522, npc.getX()+10, npc.getY()+10, npc.getZ()+10, npc.getHeading(), false, 0);
				
				if (isTargetingNpc)
				{
					newNpc.onAction(attacker);
				}
				
				newNpc.setRunning();
				
				L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
		   		if (originalAttacker != null)
		   		{
					newNpc.addDamageHate(attacker, 0, 100);
					newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
		   		}
			}
		}

		return super.onAttack(npc, attacker, damage, isPet);
	}
}