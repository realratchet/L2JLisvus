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
package quests.Q031_SecretBuriedInTheSwamp;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q031_SecretBuriedInTheSwamp extends Quest
{
	// Item
	private static final int KRORIN_JOURNAL = 7252;
	
	// NPCs
	private static final int ABERCROMBIE = 8555;
	private static final int FORGOTTEN_MONUMENT_1 = 8661;
	private static final int FORGOTTEN_MONUMENT_2 = 8662;
	private static final int FORGOTTEN_MONUMENT_3 = 8663;
	private static final int FORGOTTEN_MONUMENT_4 = 8664;
	private static final int CORPSE_OF_DWARF = 8665;

	public static void main(String[] args)
	{
		new Q031_SecretBuriedInTheSwamp();
	}
	
	public Q031_SecretBuriedInTheSwamp()
	{
		super(31, Q031_SecretBuriedInTheSwamp.class.getSimpleName(), "Secret Buried in the Swamp");
		
		setItemsIds(KRORIN_JOURNAL);
		
		addStartNpc(ABERCROMBIE);
		addTalkId(ABERCROMBIE, CORPSE_OF_DWARF, FORGOTTEN_MONUMENT_1, FORGOTTEN_MONUMENT_2, FORGOTTEN_MONUMENT_3, FORGOTTEN_MONUMENT_4);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8555-01.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8665-01.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(KRORIN_JOURNAL, 1);
		}
		else if (event.equalsIgnoreCase("8555-04.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8661-01.htm"))
		{
			st.set("cond", "4");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8662-01.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8663-01.htm"))
		{
			st.set("cond", "6");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8664-01.htm"))
		{
			st.set("cond", "7");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8555-07.htm"))
		{
			st.takeItems(KRORIN_JOURNAL, 1);
			st.rewardItems(57, 40000);
			st.addExpAndSp(130000, 0);
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
				htmltext = (player.getLevel() < 66) ? "8555-00a.htm" : "8555-00.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case ABERCROMBIE:
						if (cond == 1)
							htmltext = "8555-02.htm";
						else if (cond == 2)
							htmltext = "8555-03.htm";
						else if (cond > 2 && cond < 7)
							htmltext = "8555-05.htm";
						else if (cond == 7)
							htmltext = "8555-06.htm";
						break;
					
					case CORPSE_OF_DWARF:
						if (cond == 1)
							htmltext = "8665-00.htm";
						else if (cond > 1)
							htmltext = "8665-02.htm";
						break;
					
					case FORGOTTEN_MONUMENT_1:
						if (cond == 3)
							htmltext = "8661-00.htm";
						else if (cond > 3)
							htmltext = "8661-02.htm";
						break;
					
					case FORGOTTEN_MONUMENT_2:
						if (cond == 4)
							htmltext = "8662-00.htm";
						else if (cond > 4)
							htmltext = "8662-02.htm";
						break;
					
					case FORGOTTEN_MONUMENT_3:
						if (cond == 5)
							htmltext = "8663-00.htm";
						else if (cond > 5)
							htmltext = "8663-02.htm";
						break;
					
					case FORGOTTEN_MONUMENT_4:
						if (cond == 6)
							htmltext = "8664-00.htm";
						else if (cond > 6)
							htmltext = "8664-02.htm";
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