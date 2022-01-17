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
 * For L2JLisvus.
 */
public class RoadScavenger extends Quest
{
	private static final int ROAD_SCAVENGER = 551;
	
	public static void main(String[] args)
    {
        // Quest class
        new RoadScavenger();
    }
	
	public RoadScavenger()
	{
		super(-1, "RoadScavenger", "ai/individual");
		registerNPC(ROAD_SCAVENGER);
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
	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{   
		if (npc.getScriptValue() == 0 && Rnd.get(100) <= 2)
		{
			npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Watch your back!"));
			npc.setScriptValue(1);
		}

		return super.onAttack(npc, attacker, damage, isPet);
	}

	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (skill.isOffensive() && Util.contains(targets, npc))
		{
			if (npc.getScriptValue() == 0 && Rnd.get(100) <= 2)
			{
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Watch your back!"));
				npc.setScriptValue(1);
			}
		}

		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
}