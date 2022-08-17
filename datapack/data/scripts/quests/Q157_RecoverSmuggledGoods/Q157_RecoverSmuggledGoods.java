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
package quests.Q157_RecoverSmuggledGoods;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q157_RecoverSmuggledGoods extends Quest
{
	// NPCs
	private static final int WILFORD = 7005;

	private static final int TOAD = 121;

	// Item
	private static final int ADAMANTITE_ORE = 1024;
	
	// Reward
	private static final int BUCKLER = 20;

	public static void main(String[] args)
	{
		new Q157_RecoverSmuggledGoods();
	}
	
	public Q157_RecoverSmuggledGoods()
	{
		super(157, Q157_RecoverSmuggledGoods.class.getSimpleName(), "Recover Smuggled Goods");
		
		setItemsIds(ADAMANTITE_ORE);
		
		addStartNpc(WILFORD);
		addTalkId(WILFORD);
		
		addKillId(TOAD);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7005-05.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
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
				htmltext = (player.getLevel() < 5) ? "7005-02.htm" : "7005-03.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				if (cond == 1)
					htmltext = "7005-06.htm";
				else if (cond == 2)
				{
					htmltext = "7005-07.htm";
					st.takeItems(ADAMANTITE_ORE, -1);
					st.giveItems(BUCKLER, 1);
					st.playSound(QuestState.SOUND_FINISH);
					st.exitQuest(false);
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
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "1");
		if (st == null)
			return null;
		
		if (st.dropItems(ADAMANTITE_ORE, 1, 20, 400000))
			st.set("cond", "2");
		
		return null;
	}
}