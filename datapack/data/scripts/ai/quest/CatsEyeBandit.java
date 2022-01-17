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
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.Location;
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
public class CatsEyeBandit extends Quest
{
	public static void main(String[] args)
    {
        // Quest class
        new CatsEyeBandit();
    }
	
	public CatsEyeBandit()
	{
		super(-1, "catseyebandit", "ai/individual");
		registerNPC(5038); // Cats Eye Bandit
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
		final int rndX = npc.getX() + Rnd.get(-1000, +1000);
      	final int rndY = npc.getY() + Rnd.get(-1000, +1000);

      	if (!npc.isMoving() && GeoData.getInstance().canMove(npc.getX(), npc.getY(), npc.getZ(), rndX, rndY, npc.getZ()))   
      	{
      		npc.disableCoreAI(true);
      		npc.setRunning();
      		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(rndX, rndY, npc.getZ()));
      		if (npc.getScriptValue() == 0)
      		{
      			npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "You childish fool, do you think you can catch me?"));
      			npc.setScriptValue(1);
      		}
      	}

      	return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		final int rndX = npc.getX() + Rnd.get(-1000, +1000);
		final int rndY = npc.getY() + Rnd.get(-1000, +1000);

		if (skill.isOffensive() && Util.contains(targets, npc))
		{
			if (!npc.isMoving() && GeoData.getInstance().canMove(npc.getX(), npc.getY(), npc.getZ(), rndX, rndY, npc.getZ()))   
			{
				npc.disableCoreAI(true);
				npc.setRunning();
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(rndX, rndY, npc.getZ()));
				if (npc.getScriptValue() == 0)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "You childish fool, do you think you can catch me?"));
					npc.setScriptValue(1);
				}
			}
		}

		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public String onKill (L2NpcInstance npc, L2PcInstance killer, boolean isPet) 
	{
		if (Rnd.get(100) < 50)
		{
			npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "I must do something about this shameful incident..."));
		}

		return super.onKill(npc,killer,isPet);
	}
}