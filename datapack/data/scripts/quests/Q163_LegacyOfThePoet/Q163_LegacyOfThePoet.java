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
package quests.Q163_LegacyOfThePoet;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q163_LegacyOfThePoet extends Quest
{
	// NPC
	private static final int STARDEN = 7220;
	
	// Items
	private static final int[] RUMIELS_POEMS =
	{
		1038,
		1039,
		1040,
		1041
	};
	
	// Droplist
	private static final int[][] DROPLIST =
	{
		{
			RUMIELS_POEMS[0],
			1,
			1,
			100000
		},
		{
			RUMIELS_POEMS[1],
			1,
			1,
			200000
		},
		{
			RUMIELS_POEMS[2],
			1,
			1,
			200000
		},
		{
			RUMIELS_POEMS[3],
			1,
			1,
			400000
		}
	};

	public static void main(String[] args)
	{
		new Q163_LegacyOfThePoet();
	}
	
	public Q163_LegacyOfThePoet()
	{
		super(163, Q163_LegacyOfThePoet.class.getSimpleName(), "Legacy of the Poet");
		
		setItemsIds(RUMIELS_POEMS);
		
		addStartNpc(STARDEN);
		addTalkId(STARDEN);
		
		addKillId(372, 373);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7220-07.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				if (player.getRace() == Race.DARK_ELF)
					htmltext = "7220-00.htm";
				else if (player.getLevel() < 11)
					htmltext = "7220-02.htm";
				else
					htmltext = "7220-03.htm";
				break;
			
			case State.STARTED:
				if (st.getInt("cond") == 2)
				{
					htmltext = "7220-09.htm";
					
					for (int poem : RUMIELS_POEMS)
						st.takeItems(poem, -1);
					
					st.rewardItems(57, 13890);
					st.playSound(QuestState.SOUND_FINISH);
					st.exitQuest(false);
				}
				else
					htmltext = "7220-08.htm";
				break;
			
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "1");
		if (st == null)
			return null;
		
		if (st.dropMultipleItems(DROPLIST))
			st.set("cond", "2");
		
		return null;
	}
}