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
package quests.Q017_LightAndDarkness;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q017_LightAndDarkness extends Quest
{
	// Items
	private static final int BLOOD_OF_SAINT = 7168;
	
	// NPCs
	private static final int HIERARCH = 8517;
	private static final int SAINT_ALTAR_1 = 8508;
	private static final int SAINT_ALTAR_2 = 8509;
	private static final int SAINT_ALTAR_3 = 8510;
	private static final int SAINT_ALTAR_4 = 8511;

	public static void main(String[] args)
	{
		new Q017_LightAndDarkness();
	}
	
	public Q017_LightAndDarkness()
	{
		super(17, Q017_LightAndDarkness.class.getSimpleName(), "Light and Darkness");
		
		setItemsIds(BLOOD_OF_SAINT);
		
		addStartNpc(HIERARCH);
		addTalkId(HIERARCH, SAINT_ALTAR_1, SAINT_ALTAR_2, SAINT_ALTAR_3, SAINT_ALTAR_4);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8517-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(BLOOD_OF_SAINT, 4);
		}
		else if (event.equalsIgnoreCase("8508-02.htm"))
		{
			if (st.hasQuestItems(BLOOD_OF_SAINT))
			{
				st.set("cond", "2");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(BLOOD_OF_SAINT, 1);
			}
			else
				htmltext = "8508-03.htm";
		}
		else if (event.equalsIgnoreCase("8509-02.htm"))
		{
			if (st.hasQuestItems(BLOOD_OF_SAINT))
			{
				st.set("cond", "3");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(BLOOD_OF_SAINT, 1);
			}
			else
				htmltext = "8509-03.htm";
		}
		else if (event.equalsIgnoreCase("8510-02.htm"))
		{
			if (st.hasQuestItems(BLOOD_OF_SAINT))
			{
				st.set("cond", "4");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(BLOOD_OF_SAINT, 1);
			}
			else
				htmltext = "8510-03.htm";
		}
		else if (event.equalsIgnoreCase("8511-02.htm"))
		{
			if (st.hasQuestItems(BLOOD_OF_SAINT))
			{
				st.set("cond", "5");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(BLOOD_OF_SAINT, 1);
			}
			else
				htmltext = "8511-03.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				htmltext = (player.getLevel() < 61) ? "8517-03.htm" : "8517-01.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case HIERARCH:
						if (cond == 5)
						{
							htmltext = "8517-07.htm";
							st.addExpAndSp(105527, 0);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						else
						{
							if (st.hasQuestItems(BLOOD_OF_SAINT))
								htmltext = "8517-05.htm";
							else
							{
								htmltext = "8517-06.htm";
								st.exitQuest(true);
							}
						}
						break;
					
					case SAINT_ALTAR_1:
						if (cond == 1)
							htmltext = "8508-01.htm";
						else if (cond > 1)
							htmltext = "8508-04.htm";
						break;
					
					case SAINT_ALTAR_2:
						if (cond == 2)
							htmltext = "8509-01.htm";
						else if (cond > 2)
							htmltext = "8509-04.htm";
						break;
					
					case SAINT_ALTAR_3:
						if (cond == 3)
							htmltext = "8510-01.htm";
						else if (cond > 3)
							htmltext = "8510-04.htm";
						break;
					
					case SAINT_ALTAR_4:
						if (cond == 4)
							htmltext = "8511-01.htm";
						else if (cond > 4)
							htmltext = "8511-04.htm";
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