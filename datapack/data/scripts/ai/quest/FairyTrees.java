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
package ai.quest;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 *
 */
public class FairyTrees extends Quest
{
	private static final String[] NPC_STRING =
	{
		"We must protect the fairy tree!",
		"Get out of the sacred tree you scoundrel!",
		"Death to the thieves of the pure water of the world!"
	};
	
	public static void main(String[] args)
    {
        // Quest class
        new FairyTrees();
    }
	
	public FairyTrees()
	{
		super(-1, "fairytrees", "ai/group_template");
		int[] mobs = 
		{ 
			5185, 	// Fairy Tree Of Wind
			5186, 	// Fairy Tree Of Star
			5187, 	// Fairy Tree Of Twilight
			5188 	// Fairy Tree Of Abyss
		};
		
		for (int id : mobs)
		{
			registerNPC(id);
		}
	}

	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		for (int i = 0; i < 20; i++)
		{
			L2Attackable newNpc = (L2Attackable) addSpawn(5189, npc.getX(), npc.getY(), npc.getZ(), 0, false, 30000); // Soul of Tree Guardian
			newNpc.broadcastPacket(new NpcSay(newNpc.getObjectId(), 0, newNpc.getNpcId(), (NPC_STRING[Rnd.get(3)])));
			
			if (killer.getTarget() == npc)
			{
				newNpc.onAction(killer);
			}

			newNpc.setRunning();
			
			L2Character originalKiller = isPet ? killer.getPet() : killer;
			if (originalKiller != null)
			{
				newNpc.addDamageHate(originalKiller, 0, 100);
				newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalKiller);
				
				// There is a chance for guardian to cast Venomous Poison
				if (Rnd.get(1, 2) == 1)
				{
					L2Skill skill = SkillTable.getInstance().getInfo(4243, 1);
					if (skill != null)
					{
						npc.doCast(skill);
					}
				}
			}
		}

		return super.onKill(npc, killer, isPet);
	}
}
