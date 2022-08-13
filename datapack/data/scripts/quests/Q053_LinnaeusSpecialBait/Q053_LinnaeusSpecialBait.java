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
package quests.Q053_LinnaeusSpecialBait;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q053_LinnaeusSpecialBait extends Quest
{
	// NPCs
	private static final int LINNAEUS = 8577;

	private static final int CRIMSON_DRAKE = 670;

	// Item
	private static final int CRIMSON_DRAKE_HEART = 7624;
	
	// Reward
	private static final int FLAMING_FISHING_LURE = 7613;

	public static void main(String[] args)
	{
		new Q053_LinnaeusSpecialBait();
	}
	
	public Q053_LinnaeusSpecialBait()
	{
		super(53, Q053_LinnaeusSpecialBait.class.getSimpleName(), "Linnaues' Special Bait");
		
		setItemsIds(CRIMSON_DRAKE_HEART);
		
		addStartNpc(LINNAEUS);
		addTalkId(LINNAEUS);
		
		addKillId(CRIMSON_DRAKE);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8577-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8577-07.htm"))
		{
			htmltext = "8577-06.htm";
			st.takeItems(CRIMSON_DRAKE_HEART, -1);
			st.rewardItems(FLAMING_FISHING_LURE, 4);
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
				htmltext = (player.getLevel() < 60) ? "8577-02.htm" : "8577-01.htm";
				break;
			
			case State.STARTED:
				htmltext = (st.getQuestItemsCount(CRIMSON_DRAKE_HEART) == 100) ? "8577-04.htm" : "8577-05.htm";
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
		
		if (st.dropItems(CRIMSON_DRAKE_HEART, 1, 100, 500000))
			st.set("cond", "2");
		
		return null;
	}
}