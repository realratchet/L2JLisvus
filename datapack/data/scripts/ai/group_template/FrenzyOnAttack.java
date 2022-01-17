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
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 * 
 * Frenzy (Ultimate Buff) behavior on low health.
 */
public class FrenzyOnAttack extends Quest
{
	private static final String[] ORC_WORDS =
	{
		"Dear ultimate power!!!",
		"The battle has just begun!",
		"I never thought I'd use this against a novice!",
		"You won't take me down easily."
	};
	
	public static void main(String[] args)
    {
        // Quest class
        new FrenzyOnAttack();
    }
	
	public FrenzyOnAttack()
	{
		super(-1, "frenzyonattack", "ai/group_template");
		int[] mobs = 
		{
			270,		// Breka Orc Overlord
			495,		// Turek Orc Overlord
			588,		// Timak Orc Overlord
			778,		// Ragna Orc Overlord
			1116,		// Hames Orc Overlord
			12955,		// Halisha's Officer
			12958,		// Halisha's Officer 2
			12961,		// Halisha's Officer 3
			12964,		// Halisha's Officer 4
			12993,		// Executioner of Halisha
			12994,		// Executioner of Halisha 2
			12995,		// Executioner of Halisha 3
			12996,		// Executioner of Halisha 4
			12997,		// Executioner of Halisha 5
			12998,		// Executioner of Halisha 6
			12999,		// Executioner of Halisha 7
			13000		// Executioner of Halisha 8
		};
		
		for (int id : mobs)
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
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (npc.getScriptValue() == 0)
		{
			if (npc.getCurrentHp() / npc.getMaxHp() < 0.25 && Rnd.get(100) < 10)
			{
				if (npc.getNpcId() == 270 ||  npc.getNpcId() == 495 || npc.getNpcId() == 588 || npc.getNpcId() == 778 || npc.getNpcId() == 1116)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), (ORC_WORDS[Rnd.get(ORC_WORDS.length)])));
				}

				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
				npc.setTarget(npc);
				npc.doCast(SkillTable.getInstance().getInfo(4318, 1));
				npc.setScriptValue(1);
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (skill.isOffensive() && Util.contains(targets, npc))
		{
			if (npc.getScriptValue() == 0)
			{
				if (npc.getCurrentHp() / npc.getMaxHp() < 0.25 && Rnd.get(100) < 10)
				{
					if (npc.getNpcId() == 270 ||  npc.getNpcId() == 495 || npc.getNpcId() == 588 || npc.getNpcId() == 778 || npc.getNpcId() == 1116)
					{
						npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), (ORC_WORDS[Rnd.get(ORC_WORDS.length)])));
					}

					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					npc.setTarget(npc);
					npc.doCast(SkillTable.getInstance().getInfo(4318, 1));
					npc.setScriptValue(1);
				}
			}
		}
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
}