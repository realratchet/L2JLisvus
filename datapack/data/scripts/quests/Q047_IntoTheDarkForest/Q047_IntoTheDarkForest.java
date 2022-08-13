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
package quests.Q047_IntoTheDarkForest;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q047_IntoTheDarkForest extends Quest
{
	// NPCs
	private static final int GALLADUCCI = 7097;
	private static final int GENTLER = 7094;
	private static final int SANDRA = 7090;
	private static final int DUSTIN = 7116;
	
	// Items
	private static final int ORDER_DOCUMENT_1 = 7563;
	private static final int ORDER_DOCUMENT_2 = 7564;
	private static final int ORDER_DOCUMENT_3 = 7565;
	private static final int MAGIC_SWORD_HILT = 7568;
	private static final int GEMSTONE_POWDER = 7567;
	private static final int PURIFIED_MAGIC_NECKLACE = 7566;
	private static final int MARK_OF_TRAVELER = 7570;
	private static final int SCROLL_OF_ESCAPE_SPECIAL = 7556;

	public static void main(String[] args)
	{
		new Q047_IntoTheDarkForest();
	}
	
	public Q047_IntoTheDarkForest()
	{
		super(47, Q047_IntoTheDarkForest.class.getSimpleName(), "Into the Dark Forest");
		
		setItemsIds(ORDER_DOCUMENT_1, ORDER_DOCUMENT_2, ORDER_DOCUMENT_3, MAGIC_SWORD_HILT, GEMSTONE_POWDER, PURIFIED_MAGIC_NECKLACE);
		
		addStartNpc(GALLADUCCI);
		addTalkId(GALLADUCCI, SANDRA, DUSTIN, GENTLER);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7097-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(ORDER_DOCUMENT_1, 1);
		}
		else if (event.equalsIgnoreCase("7094-02.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(ORDER_DOCUMENT_1, 1);
			st.giveItems(MAGIC_SWORD_HILT, 1);
		}
		else if (event.equalsIgnoreCase("7097-06.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(MAGIC_SWORD_HILT, 1);
			st.giveItems(ORDER_DOCUMENT_2, 1);
		}
		else if (event.equalsIgnoreCase("7090-02.htm"))
		{
			st.set("cond", "4");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(ORDER_DOCUMENT_2, 1);
			st.giveItems(GEMSTONE_POWDER, 1);
		}
		else if (event.equalsIgnoreCase("7097-09.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(GEMSTONE_POWDER, 1);
			st.giveItems(ORDER_DOCUMENT_3, 1);
		}
		else if (event.equalsIgnoreCase("7116-02.htm"))
		{
			st.set("cond", "6");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(ORDER_DOCUMENT_3, 1);
			st.giveItems(PURIFIED_MAGIC_NECKLACE, 1);
		}
		else if (event.equalsIgnoreCase("7097-12.htm"))
		{
			st.takeItems(MARK_OF_TRAVELER, -1);
			st.takeItems(PURIFIED_MAGIC_NECKLACE, 1);
			st.rewardItems(SCROLL_OF_ESCAPE_SPECIAL, 1);
			st.playSound(QuestState.SOUND_FINISH);
			st.exitQuest(false);
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
				if (player.getRace() == Race.DARK_ELF && player.getLevel() >= 3)
				{
					if (st.hasQuestItems(MARK_OF_TRAVELER))
						htmltext = "7097-02.htm";
					else
						htmltext = "7097-01.htm";
				}
				else
					htmltext = "7097-01a.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case GALLADUCCI:
						if (cond == 1)
							htmltext = "7097-04.htm";
						else if (cond == 2)
							htmltext = "7097-05.htm";
						else if (cond == 3)
							htmltext = "7097-07.htm";
						else if (cond == 4)
							htmltext = "7097-08.htm";
						else if (cond == 5)
							htmltext = "7097-10.htm";
						else if (cond == 6)
							htmltext = "7097-11.htm";
						break;
					
					case GENTLER:
						if (cond == 1)
							htmltext = "7094-01.htm";
						else if (cond > 1)
							htmltext = "7094-03.htm";
						break;
					
					case SANDRA:
						if (cond == 3)
							htmltext = "7090-01.htm";
						else if (cond > 3)
							htmltext = "7090-03.htm";
						break;
					
					case DUSTIN:
						if (cond == 5)
							htmltext = "7116-01.htm";
						else if (cond == 6)
							htmltext = "7116-03.htm";
						break;
				}
				break;
			
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}