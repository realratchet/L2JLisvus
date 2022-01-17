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

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 * 
 * For L2JLisvus.
 */
public class HoneyBear extends Quest
{
	private static final int HONEY_BEAR = 5058;

	public static void main(String[] args)
    {
        // Quest class
        new HoneyBear();
    }
	
	public HoneyBear()
	{
		super(-1, "honeybear", "ai/individual");
		registerNPC(HONEY_BEAR);
	}

	@Override
	public String onSpawn(L2NpcInstance npc)
	{
		// Reset script value
    	if (npc.getScriptValue() > 0)
    	{
    		npc.setScriptValue(0);
    	}      

    	if (Rnd.get(100) < 50)
    	{
    		npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "What does honey of this place taste like?!"));
    	}
    	else
    	{
    		npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Give me some sweet, delicious golden honey!"));
    	}   
    	return super.onSpawn(npc);
	}

	@Override
	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{   
    	if (npc.getScriptValue() == 0)
    	{
    		npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "If you give me some honey, I'll at least spare your life..."));
    		npc.setScriptValue(1);
    	}

    	return super.onAttack(npc, attacker, damage, isPet);
	}

	@Override
	public String onKill (L2NpcInstance npc, L2PcInstance killer, boolean isPet) 
	{
		npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Only for lack of honey did I lose to the likes of you."));
		return super.onKill(npc,killer,isPet);
	}
}