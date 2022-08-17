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
package quests.Q158_SeedOfEvil;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q158_SeedOfEvil extends Quest
{
	// NPCs
	private static final int BIOTIN = 7031;

	private static final int NERKAS = 5016;

	// Item
	private static final int CLAY_TABLET = 1025;
	
	// Reward
	private static final int ENCHANT_ARMOR_D = 956;

	public static void main(String[] args)
	{
		new Q158_SeedOfEvil();
	}
	
	public Q158_SeedOfEvil()
	{
		super(158, Q158_SeedOfEvil.class.getSimpleName(), "Seed of Evil");
		
		setItemsIds(CLAY_TABLET);
		
		addStartNpc(BIOTIN);
		addTalkId(BIOTIN);
		
		addKillId(NERKAS);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7031-04.htm"))
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
				htmltext = (player.getLevel() < 21) ? "7031-02.htm" : "7031-03.htm";
				break;
			
			case State.STARTED:
				if (!st.hasQuestItems(CLAY_TABLET))
					htmltext = "7031-05.htm";
				else
				{
					htmltext = "7031-06.htm";
					st.takeItems(CLAY_TABLET, 1);
					st.giveItems(ENCHANT_ARMOR_D, 1);
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
		
		st.set("cond", "2");
		st.playSound(QuestState.SOUND_MIDDLE);
		st.giveItems(CLAY_TABLET, 1);
		
		return null;
	}
}