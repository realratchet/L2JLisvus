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
package quests.Q164_BloodFiend;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q164_BloodFiend extends Quest
{
	// NPC
	private static final int CREAMEES = 7149;

	// Item
	private static final int KIRUNAK_SKULL = 1044;

	public static void main(String[] args)
	{
		new Q164_BloodFiend();
	}
	
	public Q164_BloodFiend()
	{
		super(164, Q164_BloodFiend.class.getSimpleName(), "Blood Fiend");
		
		setItemsIds(KIRUNAK_SKULL);
		
		addStartNpc(CREAMEES);
		addTalkId(CREAMEES);
		
		addKillId(5021);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7149-04.htm"))
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
				if (player.getRace() == Race.DARK_ELF)
					htmltext = "7149-00.htm";
				else if (player.getLevel() < 21)
					htmltext = "7149-02.htm";
				else
					htmltext = "7149-03.htm";
				break;
			
			case State.STARTED:
				if (st.hasQuestItems(KIRUNAK_SKULL))
				{
					htmltext = "7149-06.htm";
					st.takeItems(KIRUNAK_SKULL, 1);
					st.rewardItems(57, 42130);
					st.playSound(QuestState.SOUND_FINISH);
					st.exitQuest(false);
				}
				else
					htmltext = "7149-05.htm";
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
		st.giveItems(KIRUNAK_SKULL, 1);
		
		return null;
	}
}