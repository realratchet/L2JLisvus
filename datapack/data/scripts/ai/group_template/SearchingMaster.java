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
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;

/**
 * @author Karakan
 *
 */
public class SearchingMaster extends Quest
{
	public static void main(String[] args)
    {
        // Quest class
        new SearchingMaster();
    }
	
	public SearchingMaster()
	{
		super(-1, "SearchingMaster", "ai/group_template");
		int[] mobs = { 960, 961, 962, 963, 964, 965, 966, 967, 968, 969, 970, 971, 972, 973 };
		for (int id : mobs)
		{
			registerNPC(id);
		}
	}

	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance player, int damage, boolean isPet)
	{
		if (player == null)
		{
			return null;
		}

		npc.setIsRunning(true);
		((L2Attackable) npc).addDamageHate(player, 0, 100);
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);

		return super.onAttack(npc, player, damage, isPet);
	}
}