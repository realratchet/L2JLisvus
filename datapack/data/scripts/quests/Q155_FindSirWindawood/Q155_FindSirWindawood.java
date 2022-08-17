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
package quests.Q155_FindSirWindawood;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q155_FindSirWindawood extends Quest
{
	// Items
	private static final int OFFICIAL_LETTER = 1019;
	private static final int HASTE_POTION = 734;
	
	// NPCs
	private static final int ABELLOS = 7042;
	private static final int WINDAWOOD = 7311;

	public static void main(String[] args)
	{
		new Q155_FindSirWindawood();
	}
	
	public Q155_FindSirWindawood()
	{
		super(155, Q155_FindSirWindawood.class.getSimpleName(), "Find Sir Windawood");
		
		setItemsIds(OFFICIAL_LETTER);
		
		addStartNpc(ABELLOS);
		addTalkId(WINDAWOOD, ABELLOS);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7042-02.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(OFFICIAL_LETTER, 1);
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
				htmltext = (player.getLevel() < 3) ? "7042-01a.htm" : "7042-01.htm";
				break;
			
			case State.STARTED:
				switch (npc.getNpcId())
				{
					case ABELLOS:
						htmltext = "7042-03.htm";
						break;
					
					case WINDAWOOD:
						if (st.hasQuestItems(OFFICIAL_LETTER))
						{
							htmltext = "7311-01.htm";
							st.takeItems(OFFICIAL_LETTER, 1);
							st.rewardItems(HASTE_POTION, 1);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
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