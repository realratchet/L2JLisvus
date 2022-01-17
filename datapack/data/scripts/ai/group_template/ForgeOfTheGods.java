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
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Attackable;
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
 * Forge of the Gods AI.
 */
public class ForgeOfTheGods extends Quest
{
	private static final Map<Integer,Integer[]> FOG_SUICIDE_AND_SPAWN = new HashMap<>();
	private static final Map<Integer,Integer[]> FOG_SUICIDE_ONLY = new HashMap<>();
	 
	static
    {
		FOG_SUICIDE_AND_SPAWN.put(1378, new Integer[]{1652, 30}); // Scarlet Stakato Noble
		FOG_SUICIDE_AND_SPAWN.put(1381, new Integer[]{1653, 30}); // Assassin Beetle
		FOG_SUICIDE_AND_SPAWN.put(1384, new Integer[]{1654, 30}); // Necromancer of Destruction
		FOG_SUICIDE_AND_SPAWN.put(1387, new Integer[]{1655, 30}); // Arimanes of Destruction
		FOG_SUICIDE_AND_SPAWN.put(1390, new Integer[]{1656, 30}); // Ashuras of Destruction
		FOG_SUICIDE_AND_SPAWN.put(1393, new Integer[]{1657, 30}); // Magma Drake
 
		FOG_SUICIDE_ONLY.put(1376, new Integer[]{0, 30}); // Scarlet Stakato Walker
		FOG_SUICIDE_ONLY.put(1377, new Integer[]{0, 30}); // Scarlet Stakato Soldier
		FOG_SUICIDE_ONLY.put(1379, new Integer[]{0, 30}); // Tepra Scorpion
		FOG_SUICIDE_ONLY.put(1380, new Integer[]{0, 30}); // Tepra Scarab
		FOG_SUICIDE_ONLY.put(1382, new Integer[]{0, 30}); // Mercenary of Destruction
		FOG_SUICIDE_ONLY.put(1383, new Integer[]{0, 30}); // Knight of Destruction
		FOG_SUICIDE_ONLY.put(1385, new Integer[]{0, 30}); // Lavastone Golem
		FOG_SUICIDE_ONLY.put(1386, new Integer[]{0, 30}); // Magma Golem
		FOG_SUICIDE_ONLY.put(1388, new Integer[]{0, 30}); // Iblis of Destruction
		FOG_SUICIDE_ONLY.put(1389, new Integer[]{0, 30}); // Balrog of Destruction
		FOG_SUICIDE_ONLY.put(1391, new Integer[]{0, 30}); // Lavasilisk
		FOG_SUICIDE_ONLY.put(1392, new Integer[]{0, 30}); // Blazing Ifrit
		FOG_SUICIDE_ONLY.put(1394, new Integer[]{0, 30}); // Lavasaurus
		FOG_SUICIDE_ONLY.put(1395, new Integer[]{0, 30}); // Elder Lavasaurus
    }
	
	public static void main(String[] args)
    {
        // Quest class
        new ForgeOfTheGods();
    }
	
	public ForgeOfTheGods()
	{
		super(-1, "forgeofthegods", "ai/group_template");
		// Register NPCs
		for (int id : FOG_SUICIDE_AND_SPAWN.keySet())
		{
			registerNPC(id);
		}
		for (int id : FOG_SUICIDE_ONLY.keySet())
		{
			registerNPC(id);
		}
	}
	
	@Override
	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		Integer[] suicide = null;
		if (FOG_SUICIDE_AND_SPAWN.containsKey(npc.getNpcId()))
		{
			suicide = FOG_SUICIDE_AND_SPAWN.get(npc.getNpcId());
		}
		else if (FOG_SUICIDE_ONLY.containsKey(npc.getNpcId()))
		{
			suicide = FOG_SUICIDE_ONLY.get(npc.getNpcId());
		}
		
		if (suicide != null)
		{
			if ((npc.getCurrentHp() <= (npc.getStat().getMaxHp() * 20) / 100.0) && Rnd.get(100) < suicide[1]) // at 20% HP , 30% cast chance
			{
				if (Util.checkIfInRange(150, npc, attacker, true)) // Skill Radius
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					npc.setTarget(npc);
					npc.doCast(SkillTable.getInstance().getInfo(4614, 12)); // NPC Death Bomb
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
			Integer[] suicide = null;
			if (FOG_SUICIDE_AND_SPAWN.containsKey(npc.getNpcId()))
			{
				suicide = FOG_SUICIDE_AND_SPAWN.get(npc.getNpcId());
			}
			else if (FOG_SUICIDE_ONLY.containsKey(npc.getNpcId()))
			{
				suicide = FOG_SUICIDE_ONLY.get(npc.getNpcId());
			}
			
			if (suicide != null)
			{
				if ((npc.getCurrentHp() <= (npc.getStat().getMaxHp() * 20) / 100.0) && Rnd.get(100) < suicide[1]) // at 20% HP , 30% cast chance
				{
					if (npc.getCurrentHp() <= ((npc.getStat().getMaxHp() * 20) / 100.0)) //  20% HP
					{
						if (Util.checkIfInRange(150, npc, caster, true)) // Skill Radius
						{
							npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
							npc.setTarget(npc);
							npc.doCast(SkillTable.getInstance().getInfo(4614, 12)); // NPC Death Bomb
						}
					}
				}
			}
		}

		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public String onSpellFinished(L2NpcInstance npc, L2PcInstance player, L2Skill skill)
	{
		if (FOG_SUICIDE_AND_SPAWN.containsKey(npc.getNpcId()) && skill.getId() == 4614)
		{
			Integer[] suicide = FOG_SUICIDE_AND_SPAWN.get(npc.getNpcId());
			if (Rnd.get(100) < suicide[1])
			{
				for (int i = 0; i < 5; i++)
				{
					L2Attackable newNpc = (L2Attackable) addSpawn(suicide[0], npc.getX()+Rnd.get(150), npc.getY()+Rnd.get(150), npc.getZ(), 0, false, 0);
					
					if (player != null)
					{
						newNpc.setRunning();
						newNpc.addDamageHate(player, 0, 100);
						newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
					}
				}
			}
		}

		return super.onSpellFinished(npc, player, skill);
	}
}
