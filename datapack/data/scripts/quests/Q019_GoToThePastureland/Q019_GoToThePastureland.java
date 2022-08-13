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
package quests.Q019_GoToThePastureland;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q019_GoToThePastureland extends Quest
{
	// Items
	private static final int YOUNG_WILD_BEAST_MEAT = 7547;
	
	// NPCs
	private static final int VLADIMIR = 8302;
	private static final int TUNATUN = 8537;

	public static void main(String[] args)
	{
		new Q019_GoToThePastureland();
	}
	
	public Q019_GoToThePastureland()
	{
		super(19, Q019_GoToThePastureland.class.getSimpleName(), "Go to the Pastureland!");
		
		setItemsIds(YOUNG_WILD_BEAST_MEAT);
		
		addStartNpc(VLADIMIR);
		addTalkId(VLADIMIR, TUNATUN);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8302-01.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(YOUNG_WILD_BEAST_MEAT, 1);
		}
		else if (event.equalsIgnoreCase("019_finish"))
		{
			if (st.hasQuestItems(YOUNG_WILD_BEAST_MEAT))
			{
				htmltext = "8537-01.htm";
				st.takeItems(YOUNG_WILD_BEAST_MEAT, 1);
				st.rewardItems(57, 30000);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "8537-02.htm";
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
				htmltext = (player.getLevel() < 63) ? "8302-03.htm" : "8302-00.htm";
				break;
			
			case State.STARTED:
				switch (npc.getNpcId())
				{
					case VLADIMIR:
						htmltext = "8302-02.htm";
						break;
					
					case TUNATUN:
						htmltext = "8537-00.htm";
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