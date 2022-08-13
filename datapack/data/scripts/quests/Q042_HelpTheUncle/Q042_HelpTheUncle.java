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
package quests.Q042_HelpTheUncle;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q042_HelpTheUncle extends Quest
{
	// NPCs
	private static final int WATERS = 7828;
	private static final int SOPHYA = 7735;
	
	// Items
	private static final int TRIDENT = 291;
	private static final int MAP_PIECE = 7548;
	private static final int MAP = 7549;
	private static final int PET_TICKET = 7583;
	
	// Monsters
	private static final int MONSTER_EYE_DESTROYER = 68;
	private static final int MONSTER_EYE_GAZER = 266;

	public static void main(String[] args)
	{
		new Q042_HelpTheUncle();
	}
	
	public Q042_HelpTheUncle()
	{
		super(42, Q042_HelpTheUncle.class.getSimpleName(), "Help the Uncle!");
		
		setItemsIds(MAP_PIECE, MAP);
		
		addStartNpc(WATERS);
		addTalkId(WATERS, SOPHYA);
		
		addKillId(MONSTER_EYE_DESTROYER, MONSTER_EYE_GAZER);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7828-01.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7828-03.htm") && st.hasQuestItems(TRIDENT))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(TRIDENT, 1);
		}
		else if (event.equalsIgnoreCase("7828-05.htm"))
		{
			st.set("cond", "4");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(MAP_PIECE, 30);
			st.giveItems(MAP, 1);
		}
		else if (event.equalsIgnoreCase("7735-06.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(MAP, 1);
		}
		else if (event.equalsIgnoreCase("7828-07.htm"))
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
				htmltext = (player.getLevel() < 25) ? "7828-00a.htm" : "7828-00.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case WATERS:
						if (cond == 1)
							htmltext = (!st.hasQuestItems(TRIDENT)) ? "7828-01a.htm" : "7828-02.htm";
						else if (cond == 2)
							htmltext = "7828-03a.htm";
						else if (cond == 3)
							htmltext = "7828-04.htm";
						else if (cond == 4)
							htmltext = "7828-05a.htm";
						else if (cond == 5)
							htmltext = "7828-06.htm";
						break;
					
					case SOPHYA:
						if (cond == 4)
							htmltext = "7735-05.htm";
						else if (cond == 5)
							htmltext = "7735-06a.htm";
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