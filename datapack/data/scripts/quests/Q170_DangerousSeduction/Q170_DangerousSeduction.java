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
package quests.Q170_DangerousSeduction;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q170_DangerousSeduction extends Quest
{
	// NPC
	private static final int VELLIOR = 7305;

	// Item
	private static final int NIGHTMARE_CRYSTAL = 1046;

	public static void main(String[] args)
	{
		new Q170_DangerousSeduction();
	}
	
	public Q170_DangerousSeduction()
	{
		super(170, Q170_DangerousSeduction.class.getSimpleName(), "Dangerous Seduction");
		
		setItemsIds(NIGHTMARE_CRYSTAL);
		
		addStartNpc(VELLIOR);
		addTalkId(VELLIOR);
		
		addKillId(5022); // Merkenis
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7305-04.htm"))
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
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				if (player.getRace() != Race.DARK_ELF)
					htmltext = "7305-00.htm";
				else if (player.getLevel() < 21)
					htmltext = "7305-02.htm";
				else
					htmltext = "7305-03.htm";
				break;
			
			case State.STARTED:
				if (st.hasQuestItems(NIGHTMARE_CRYSTAL))
				{
					htmltext = "7305-06.htm";
					st.takeItems(NIGHTMARE_CRYSTAL, -1);
					st.rewardItems(57, 102680);
					st.playSound(QuestState.SOUND_FINISH);
					st.exitQuest(false);
				}
				else
					htmltext = "7305-05.htm";
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
		
		st.set("cond", "2");
		st.playSound(QuestState.SOUND_MIDDLE);
		st.giveItems(NIGHTMARE_CRYSTAL, 1);
		
		return null;
	}
}