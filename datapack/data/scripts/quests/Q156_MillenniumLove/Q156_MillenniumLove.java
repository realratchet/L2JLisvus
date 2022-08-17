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
package quests.Q156_MillenniumLove;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q156_MillenniumLove extends Quest
{
	// Items
	private static final int LILITH_LETTER = 1022;
	private static final int THEON_DIARY = 1023;
	
	// NPCs
	private static final int LILITH = 7368;
	private static final int BAENEDES = 7369;

	public static void main(String[] args)
	{
		new Q156_MillenniumLove();
	}
	
	public Q156_MillenniumLove()
	{
		super(156, Q156_MillenniumLove.class.getSimpleName(), "Millennium Love");
		
		setItemsIds(LILITH_LETTER, THEON_DIARY);
		
		addStartNpc(LILITH);
		addTalkId(LILITH, BAENEDES);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7368-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(LILITH_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("7369-02.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(LILITH_LETTER, 1);
			st.giveItems(THEON_DIARY, 1);
		}
		else if (event.equalsIgnoreCase("7369-03.htm"))
		{
			st.takeItems(LILITH_LETTER, 1);
			st.addExpAndSp(3000, 0);
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
				htmltext = (player.getLevel() < 15) ? "7368-00.htm" : "7368-01.htm";
				break;
			
			case State.STARTED:
				switch (npc.getNpcId())
				{
					case LILITH:
						if (st.hasQuestItems(LILITH_LETTER))
							htmltext = "7368-05.htm";
						else if (st.hasQuestItems(THEON_DIARY))
						{
							htmltext = "7368-06.htm";
							st.takeItems(THEON_DIARY, 1);
							st.giveItems(5250, 1);
							st.addExpAndSp(3000, 0);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case BAENEDES:
						if (st.hasQuestItems(LILITH_LETTER))
							htmltext = "7369-01.htm";
						else if (st.hasQuestItems(THEON_DIARY))
							htmltext = "7369-04.htm";
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