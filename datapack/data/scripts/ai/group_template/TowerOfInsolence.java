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
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;

/**
 * @author Karakan
 * 
 * Tower of Insolence Angels AI.
 */
public class TowerOfInsolence extends Quest
{
	private static final Map<Integer,Integer[]> ANGEL_SPAWNS = new HashMap<>();
	
	static
	{
		ANGEL_SPAWNS.put(830, new Integer[]{859, 100}); 	// Guardian Angel
		ANGEL_SPAWNS.put(831, new Integer[]{860, 100}); 	// Seal Angel
		ANGEL_SPAWNS.put(1062, new Integer[]{1063, 100}); 	// Messenger Angel
		ANGEL_SPAWNS.put(1067, new Integer[]{1068, 100}); 	// Guardian Archangel
		ANGEL_SPAWNS.put(1070, new Integer[]{1071, 100}); 	// Seal Archangel
	}
	
	public static void main(String[] args)
    {
        // Quest class
        new TowerOfInsolence();
    }
	
	public TowerOfInsolence()
	{
		super(-1, "towerofinsolence", "ai/group_template");
		// Register NPCs
		for (int id : ANGEL_SPAWNS.keySet())
		{
			registerNPC(id);
		}
	}

	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (ANGEL_SPAWNS.containsKey(npcId))
		{
			Integer[] spawnData = ANGEL_SPAWNS.get(npc.getNpcId());
			
			// Spawn new npc
			L2Attackable newNpc = (L2Attackable) addSpawn (spawnData[0], npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 0);
			
			// If player had previously targeted victim, set target to that of new npc
			if (killer.getTarget() == npc)
			{
				newNpc.onAction(killer);
			}
			
			newNpc.setRunning();
			
			// Force new npc to attack original killer
			L2Character originalKiller = isPet ? killer.getPet() : killer;
			if (originalKiller != null)
			{
				newNpc.addDamageHate(originalKiller, 0, 100);
				newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalKiller);
			}
		}
		
		return super.onKill(npc, killer, isPet);
	}
}