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
package quests.Q052_WilliesSpecialBait;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q052_WilliesSpecialBait extends Quest
{
	// NPCs
	private static final int WILLIE = 8574;

	private static final int TARLK_BASILISK = 573;

	// Item
	private static final int TARLK_EYE = 7623;
	
	// Reward
	private static final int EARTH_FISHING_LURE = 7612;

	public static void main(String[] args)
	{
		new Q052_WilliesSpecialBait();
	}
	
	public Q052_WilliesSpecialBait()
	{
		super(52, Q052_WilliesSpecialBait.class.getSimpleName(), "Willie's Special Bait");
		
		setItemsIds(TARLK_EYE);
		
		addStartNpc(WILLIE);
		addTalkId(WILLIE);
		
		addKillId(TARLK_BASILISK);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8574-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8574-07.htm"))
		{
			htmltext = "8574-06.htm";
			st.takeItems(TARLK_EYE, -1);
			st.rewardItems(EARTH_FISHING_LURE, 4);
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
				htmltext = (player.getLevel() < 48) ? "8574-02.htm" : "8574-01.htm";
				break;
			
			case State.STARTED:
				htmltext = (st.getQuestItemsCount(TARLK_EYE) == 100) ? "8574-04.htm" : "8574-05.htm";
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
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "1");
		if (st == null)
			return null;
		
		if (st.dropItems(TARLK_EYE, 1, 100, 500000))
			st.set("cond", "2");
		
		return null;
	}
}