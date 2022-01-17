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

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * NpcUltimateEvasion behavior on low health.
 * 
 * @author Karakan
 */
public class NpcUltimateEvasion extends Quest
{
	public static void main(String[] args)
	{
		// Quest class
		new NpcUltimateEvasion();
	}
	
	public NpcUltimateEvasion()
	{
		super(-1, "npcultimateevasion", "ai/group_template");
		int[] mobs =
		{
			808, // Nos Lad
			936, // Tanor Silenos
			1524, // Blade of Splendor
			1525, // Blade of Splendor
			1531, // Punishment of Splendor
			1539, // Wailing of Splendor
			1540, // Wailing of Splendor
			1658 // Punishment of Splendor
		};
		
		for (int id : mobs)
		{
			registerNPC(id);
		}
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
		if (npc.getScriptValue() == 0)
		{
			if (npc.getCurrentHp() / npc.getMaxHp() < 0.2 && Rnd.get(100) < 30) // 20% Hp - 30% Chance
			{
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
				npc.setTarget(npc);
				npc.doCast(SkillTable.getInstance().getInfo(4103, 2));
				npc.setScriptValue(1);
			}
		}
		
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (npc.getScriptValue() == 0)
		{
			if (skill.isOffensive() && Util.contains(targets, npc))
			{
				if (npc.getCurrentHp() / npc.getMaxHp() < 0.2 && Rnd.get(100) < 30) // 20% Hp - 30% Chance
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					npc.setTarget(npc);
					npc.doCast(SkillTable.getInstance().getInfo(4103, 1));
					npc.setScriptValue(1);
				}
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
}