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
package teleports.ToIVortex;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.util.Rnd;

public class ToIVortex extends Quest
{
	private static final int GREEN_STONE = 4401;
	private static final int BLUE_STONE = 4402;
	private static final int RED_STONE = 4403;
	
	private static final int[] NPCS = {7952, 7953, 7954, 12078};
	
	private static final int[][] EXIT_LOCATIONS = {{108784, 16000, -4928}, {113824, 10448, -5164}, {115488, 22096, -5168}};
	
	public static void main(String[] args)
    {
        // Quest class
        new ToIVortex();
    }
	
	public ToIVortex()
	{
		super(-1, ToIVortex.class.getSimpleName(), "teleports");
		
		for (int id : NPCS)
		{
			addStartNpc(id);
			addTalkId(id);
			addFirstTalkId(id);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		
		if (npc.getNpcId() == 12078)
		{
			if (event.equalsIgnoreCase("exit"))
			{
				int[] coords = EXIT_LOCATIONS[Rnd.get(3)];
			    player.teleToLocation(coords[0] + Rnd.get(100), coords[1] + Rnd.get(100), coords[2]);
			}
		}
		else if (event.equalsIgnoreCase("blue"))
		{
			if (st.getQuestItemsCount(BLUE_STONE) > 0)
			{
				st.takeItems(BLUE_STONE, 1);
				player.teleToLocation(114097, 19935, 935);
			}
			else
				htmltext = "no-items.htm";
		}
		else if (event.equalsIgnoreCase("green"))
		{
			if (st.getQuestItemsCount(GREEN_STONE) > 0)
			{
				st.takeItems(GREEN_STONE, 1);
				player.teleToLocation(110930, 15963, -4378);
			}
			else
				htmltext = "no-items.htm";
		}
		else if (event.equalsIgnoreCase("red"))
		{
			if (st.getQuestItemsCount(RED_STONE) > 0)
			{
				st.takeItems(RED_STONE, 1);
				player.teleToLocation(118558, 16659, 5987);
			}
			else
				htmltext = "no-items.htm";
		}
		st.exitQuest(true);
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2NpcInstance npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		
		return npc.getNpcId() + ".htm";
	}
}