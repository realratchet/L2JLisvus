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
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.util.Util;

/**
 * Fallen Orc Shaman AI.
 * By Karakan for L2jLisvus
 */
public class FallenOrcShaman extends Quest
{
	private static final int FALLEN_ORC_SHAMAN = 1258;
    private static final int SHARP_TALON_TIGER = 1259;
   
    public static void main(String[] args)
    {
        // Quest class
        new FallenOrcShaman();
    }
    
    public FallenOrcShaman()
    {
    	super(-1, "fallenorcshaman", "ai/individual");
    	registerNPC(FALLEN_ORC_SHAMAN);
    }

    @Override
    public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
    {
    	if (skill.isOffensive() && Util.contains(targets, npc))
    	{
    		boolean isTargetingNpc = caster.getTarget() == npc;
    		
    		npc.onDecay();
    		L2Attackable newNpc = (L2Attackable) addSpawn(SHARP_TALON_TIGER, npc.getX(), npc.getY(), npc.getZ()+10, npc.getHeading(), false, 0);

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

    	return super.onSkillSee(npc, caster, skill, targets, isPet);
    }
   
   	@Override
    public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
    {
   		boolean isTargetingNpc = attacker.getTarget() == npc;
   		
   		npc.onDecay();
   		L2Attackable newNpc = (L2Attackable) addSpawn(SHARP_TALON_TIGER, npc.getX(), npc.getY(), npc.getZ()+10, npc.getHeading(), false, 0);

   		if (isTargetingNpc)
   		{
   			newNpc.onAction(attacker);
   		}

   		newNpc.setRunning();
   		
   		L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
   		if (originalAttacker != null)
   		{
   			newNpc.addDamageHate(originalAttacker, 0, 100);
   			newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalAttacker);
   		}

      	return super.onAttack(npc, attacker, damage, isPet);
    }
}