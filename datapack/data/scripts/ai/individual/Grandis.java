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
package ai.individual;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 *
 */
public class Grandis extends Quest
{
	private final L2Skill BearStun = SkillTable.getInstance().getInfo(4089, 1);
	private final L2Skill WolfStun = SkillTable.getInstance().getInfo(4090, 1);
	private final L2Skill OgreStun = SkillTable.getInstance().getInfo(4091, 1);
	private final L2Skill PumaStun = SkillTable.getInstance().getInfo(4092, 1);
	
	public static void main(String[] args)
    {
        // Quest class
        new Grandis();
    }
	
	public Grandis()
	{
		super(-1, "grandis", "ai/individual");
		int[] mobs =
		{
			554
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
		if ((npc.getCurrentHp() <= (npc.getStat().getMaxHp() * 50) / 100.0) && npc.getScriptValue() == 0)
		{
			if (Rnd.get(100) <= 75 && Rnd.get(100) >= 50)
			{
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
				npc.setTarget(npc);
				npc.doCast(OgreStun);
				npc.setScriptValue(1);
			}
			else if (Rnd.get(100) <= 50 && Rnd.get(100) >= 25)
			{
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
				npc.setTarget(npc);
				npc.doCast(BearStun);
				npc.setScriptValue(1);
			}
			else if (Rnd.get(100) <= 25 && Rnd.get(100) > 10)
			{
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
				npc.setTarget(npc);
				npc.doCast(WolfStun);
				npc.setScriptValue(1);
			}
			else if (Rnd.get(100) <= 10)
			{
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
				npc.setTarget(npc);
				npc.doCast(PumaStun);
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
			if ((npc.getCurrentHp() <= (npc.getStat().getMaxHp() * 50) / 100.0) && npc.getScriptValue() == 0)
			{
				if (Rnd.get(100) <= 75 && Rnd.get(100) >= 50)
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					npc.setTarget(npc);
					npc.doCast(OgreStun);
					npc.setScriptValue(1);
				}
				else if (Rnd.get(100) <= 50 && Rnd.get(100) >= 25)
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					npc.setTarget(npc);
					npc.doCast(BearStun);
					npc.setScriptValue(1);
				}
				else if (Rnd.get(100) <= 25 && Rnd.get(100) > 10)
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					npc.setTarget(npc);
					npc.doCast(WolfStun);
					npc.setScriptValue(1);
				}
				else if (Rnd.get(100) <= 10)
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					npc.setTarget(npc);
					npc.doCast(PumaStun);
					npc.setScriptValue(1);
				}
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
}