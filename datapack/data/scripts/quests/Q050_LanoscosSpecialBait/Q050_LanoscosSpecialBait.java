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
package quests.Q050_LanoscosSpecialBait;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q050_LanoscosSpecialBait extends Quest
{
	// NPCs
	private static final int LANOSCO = 8570;

	private static final int SINGING_WIND = 1026;

	// Item
	private static final int ESSENCE_OF_WIND = 7621;
	
	// Reward
	private static final int WIND_FISHING_LURE = 7610;

	public static void main(String[] args)
	{
		new Q050_LanoscosSpecialBait();
	}
	
	public Q050_LanoscosSpecialBait()
	{
		super(50, Q050_LanoscosSpecialBait.class.getSimpleName(), "Lanosco's Special Bait");
		
		setItemsIds(ESSENCE_OF_WIND);
		
		addStartNpc(LANOSCO);
		addTalkId(LANOSCO);
		
		addKillId(SINGING_WIND);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8570-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8570-07.htm"))
		{
			htmltext = "8570-06.htm";
			st.takeItems(ESSENCE_OF_WIND, -1);
			st.rewardItems(WIND_FISHING_LURE, 4);
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
				htmltext = (player.getLevel() < 27) ? "8570-02.htm" : "8570-01.htm";
				break;
			
			case State.STARTED:
				htmltext = (st.getQuestItemsCount(ESSENCE_OF_WIND) == 100) ? "8570-04.htm" : "8570-05.htm";
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
		
		if (st.dropItems(ESSENCE_OF_WIND, 1, 100, 500000))
			st.set("cond", "2");
		
		return null;
	}
}