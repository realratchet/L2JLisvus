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
package quests.Q043_HelpTheSister;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q043_HelpTheSister extends Quest
{
	// NPCs
	private static final int COOPER = 7829;
	private static final int GALLADUCCI = 7097;
	
	// Items
	private static final int CRAFTED_DAGGER = 220;
	private static final int MAP_PIECE = 7550;
	private static final int MAP = 7551;
	private static final int PET_TICKET = 7584;
	
	// Monsters
	private static final int SPECTER = 171;
	private static final int SORROW_MAIDEN = 197;

	public static void main(String[] args)
	{
		new Q043_HelpTheSister();
	}
	
	public Q043_HelpTheSister()
	{
		super(43, Q043_HelpTheSister.class.getSimpleName(), "Help the Sister!");
		
		setItemsIds(MAP_PIECE, MAP);
		
		addStartNpc(COOPER);
		addTalkId(COOPER, GALLADUCCI);
		
		addKillId(SPECTER, SORROW_MAIDEN);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7829-01.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7829-03.htm") && st.hasQuestItems(CRAFTED_DAGGER))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(CRAFTED_DAGGER, 1);
		}
		else if (event.equalsIgnoreCase("7829-05.htm"))
		{
			st.set("cond", "4");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(MAP_PIECE, 30);
			st.giveItems(MAP, 1);
		}
		else if (event.equalsIgnoreCase("7097-06.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(MAP, 1);
		}
		else if (event.equalsIgnoreCase("7829-07.htm"))
		{
			st.giveItems(PET_TICKET, 1);
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
				htmltext = (player.getLevel() < 26) ? "7829-00a.htm" : "7829-00.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case COOPER:
						if (cond == 1)
							htmltext = (!st.hasQuestItems(CRAFTED_DAGGER)) ? "7829-01a.htm" : "7829-02.htm";
						else if (cond == 2)
							htmltext = "7829-03a.htm";
						else if (cond == 3)
							htmltext = "7829-04.htm";
						else if (cond == 4)
							htmltext = "7829-05a.htm";
						else if (cond == 5)
							htmltext = "7829-06.htm";
						break;
					
					case GALLADUCCI:
						if (cond == 4)
							htmltext = "7097-05.htm";
						else if (cond == 5)
							htmltext = "7097-06a.htm";
						break;
				}
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
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "2");
		if (st == null)
			return null;
		
		if (st.dropItemsAlways(MAP_PIECE, 1, 30))
			st.set("cond", "3");
		
		return null;
	}
}