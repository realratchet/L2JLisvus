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
package quests.Q051_OFullesSpecialBait;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q051_OFullesSpecialBait extends Quest
{
	// NPCs
	private static final int OFULLE = 8572;

	private static final int FETTERED_SOUL = 552;

	// Item
	private static final int LOST_BAIT = 7622;
	
	// Reward
	private static final int ICY_AIR_LURE = 7611;

	public static void main(String[] args)
	{
		new Q051_OFullesSpecialBait();
	}
	
	public Q051_OFullesSpecialBait()
	{
		super(51, Q051_OFullesSpecialBait.class.getSimpleName(), "O'Fulle's Special Bait");
		
		setItemsIds(LOST_BAIT);
		
		addStartNpc(OFULLE);
		addTalkId(OFULLE);
		
		addKillId(FETTERED_SOUL);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8572-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8572-07.htm"))
		{
			htmltext = "8572-06.htm";
			st.takeItems(LOST_BAIT, -1);
			st.rewardItems(ICY_AIR_LURE, 4);
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
				htmltext = (player.getLevel() < 36) ? "8572-02.htm" : "8572-01.htm";
				break;
			
			case State.STARTED:
				htmltext = (st.getQuestItemsCount(LOST_BAIT) == 100) ? "8572-04.htm" : "8572-05.htm";
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
		
		if (st.dropItemsAlways(LOST_BAIT, 1, 100))
			st.set("cond", "2");
		
		return null;
	}
}