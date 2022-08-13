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
package quests.Q018_MeetingWithTheGoldenRam;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q018_MeetingWithTheGoldenRam extends Quest
{
	// Items
	private static final int SUPPLY_BOX = 7245;
	
	// NPCs
	private static final int DONAL = 8314;
	private static final int DAISY = 8315;
	private static final int ABERCROMBIE = 8555;

	public static void main(String[] args)
	{
		new Q018_MeetingWithTheGoldenRam();
	}
	
	public Q018_MeetingWithTheGoldenRam()
	{
		super(18, Q018_MeetingWithTheGoldenRam.class.getSimpleName(), "Meeting with the Golden Ram");
		
		setItemsIds(SUPPLY_BOX);
		
		addStartNpc(DONAL);
		addTalkId(DONAL, DAISY, ABERCROMBIE);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8314-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8315-02.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(SUPPLY_BOX, 1);
		}
		else if (event.equalsIgnoreCase("8555-02.htm"))
		{
			st.takeItems(SUPPLY_BOX, 1);
			st.rewardItems(57, 15000);
			st.addExpAndSp(50000, 0);
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
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				htmltext = (player.getLevel() < 66) ? "8314-02.htm" : "8314-01.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case DONAL:
						htmltext = "8314-04.htm";
						break;
					
					case DAISY:
						if (cond == 1)
							htmltext = "8315-01.htm";
						else if (cond == 2)
							htmltext = "8315-03.htm";
						break;
					
					case ABERCROMBIE:
						if (cond == 2)
							htmltext = "8555-01.htm";
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