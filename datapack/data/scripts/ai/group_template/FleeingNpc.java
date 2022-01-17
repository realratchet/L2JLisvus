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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 * 
 * Elpies and Four Sepulcher victims AI.<br>
 * For L2JLisvus.
 */
public class FleeingNpc extends Quest
{
	public static void main(String[] args)
    {
        // Quest class
        new FleeingNpc();
    }
	
	public FleeingNpc()
	{
		super(-1, "fleeingnpc", "ai/group_template");
		int[] mobs = 
		{
			432, 	// Elpy
			12985, 	// Victim
			12986, 	// Victim 2
			12987, 	// Victim 3
			12988, 	// Victim 4
			12989, 	// Victim 5
			12990, 	// Victim 6
			12991, 	// Victim 7
			12992  	// Victim 8
		};
		
		for (int id : mobs)
		{
			registerNPC(id);
		}
	}

	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		final int rndX = npc.getX() + Rnd.get(-Config.MAX_DRIFT_RANGE, Config.MAX_DRIFT_RANGE); // Could be increased (+-500 for Dungeons,  +-1000 for Hunting Grounds)
		final int rndY = npc.getY() + Rnd.get(-Config.MAX_DRIFT_RANGE, Config.MAX_DRIFT_RANGE); 

		npc.disableCoreAI(true);
		if (!npc.isMoving() && GeoData.getInstance().canMove(npc.getX(), npc.getY(), npc.getZ(), rndX, rndY, npc.getZ()))
		{
			npc.setRunning();
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(rndX, rndY, npc.getZ()));
		}

		return super.onAttack(npc, attacker, damage, isPet);
	}

	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		final int rndX = npc.getX() + Rnd.get(-Config.MAX_DRIFT_RANGE, Config.MAX_DRIFT_RANGE); // Could be increased (+-500 for Dungeons,  +-1000 for Hunting Grounds)
		final int rndY = npc.getY() + Rnd.get(-Config.MAX_DRIFT_RANGE, Config.MAX_DRIFT_RANGE); 

		npc.disableCoreAI(true);
		if (skill.isOffensive() && Util.contains(targets, npc))
		{
			if (!npc.isMoving() && GeoData.getInstance().canMove(npc.getX(), npc.getY(), npc.getZ(), rndX, rndY, npc.getZ()))   
			{
				npc.setRunning();
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(rndX, rndY, npc.getZ()));
			}
		}

		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
}