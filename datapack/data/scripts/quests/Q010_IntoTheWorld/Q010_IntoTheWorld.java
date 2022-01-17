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
package quests.Q010_IntoTheWorld;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * @author CubicVirtuoso
 */
public class Q010_IntoTheWorld extends Quest
{
	private static final int VERY_EXPENSIVE_NECKLACE = 7574;
	private static final int SCROLL_OF_ESCAPE_GIRAN = 7559;
	private static final int MARK_OF_TRAVELER = 7570;
	
	public static void main(String[] args)
	{
		// Quest class
		new Q010_IntoTheWorld();
	}
	
	public Q010_IntoTheWorld()
	{
		super(10, Q010_IntoTheWorld.class.getSimpleName(), "Into The World");
		
		this.questItemIds = new int[]
		{
			VERY_EXPENSIVE_NECKLACE
		};
		
		addStartNpc(7533);
		addTalkId(7533);
		addTalkId(7520);
		addTalkId(7650);
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("7533-03.htm"))
		{
			st.set("cond", "1");
			st.setState(State.STARTED);
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7520-02.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(VERY_EXPENSIVE_NECKLACE, 1);
		}
		else if (event.equalsIgnoreCase("7650-02.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(VERY_EXPENSIVE_NECKLACE, 1);
		}
		else if (event.equalsIgnoreCase("7533-06.htm"))
		{
			st.giveItems(SCROLL_OF_ESCAPE_GIRAN, 1);
			st.giveItems(MARK_OF_TRAVELER, 1);
			st.playSound(QuestState.SOUND_FINISH);
			st.exitQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		int npcId = npc.getNpcId();
		byte id = st.getState();
		if (id == State.CREATED)
		{
			st.set("cond", "0");
			if (player.getRace().ordinal() == 4)
			{
				htmltext = "7533-02.htm";
			}
			else
			{
				htmltext = "7533-01.htm";
				st.exitQuest(true);
			}
		}
		else if (id == State.COMPLETED)
		{
			htmltext = getAlreadyCompletedMsg();
		}
		else if (npcId == 7533)
		{
			if (st.getInt("cond") == 1)
			{
				htmltext = "7533-04.htm";
			}
			else if (st.getInt("cond") == 4)
			{
				htmltext = "7533-05.htm";
			}
		}
		else if (npcId == 7520)
		{
			if (st.getInt("cond") == 3)
			{
				htmltext = "7520-04.htm";
				st.set("cond", "4");
			}
			else if (st.getInt("cond") > 0)
			{
				if (st.getQuestItemsCount(VERY_EXPENSIVE_NECKLACE) == 0)
				{
					htmltext = "7520-01.htm";
				}
				else
				{
					htmltext = "7520-03.htm";
				}
			}
		}
		else if (npcId == 7650)
		{
			if (st.getInt("cond") == 2)
			{
				if (st.getQuestItemsCount(VERY_EXPENSIVE_NECKLACE) > 0)
				{
					htmltext = "7650-01.htm";
				}
			}
		}
		return htmltext;
	}
}