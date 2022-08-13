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
package quests.Q044_HelpTheSon;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q044_HelpTheSon extends Quest
{
	// Npcs
	private static final int LUNDY = 7827;
	private static final int DRIKUS = 7505;
	
	// Items
	private static final int WORK_HAMMER = 168;
	private static final int GEMSTONE_FRAGMENT = 7552;
	private static final int GEMSTONE = 7553;
	private static final int PET_TICKET = 7585;
	
	// Monsters
	private static final int MAILLE = 919;
	private static final int MAILLE_SCOUT = 920;
	private static final int MAILLE_GUARD = 921;

	public static void main(String[] args)
	{
		new Q044_HelpTheSon();
	}
	
	public Q044_HelpTheSon()
	{
		super(44, Q044_HelpTheSon.class.getSimpleName(), "Help the Son!");
		
		setItemsIds(GEMSTONE_FRAGMENT, GEMSTONE);
		
		addStartNpc(LUNDY);
		addTalkId(LUNDY, DRIKUS);
		
		addKillId(MAILLE, MAILLE_SCOUT, MAILLE_GUARD);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7827-01.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7827-03.htm") && st.hasQuestItems(WORK_HAMMER))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(WORK_HAMMER, 1);
		}
		else if (event.equalsIgnoreCase("7827-05.htm"))
		{
			st.set("cond", "4");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(GEMSTONE_FRAGMENT, 30);
			st.giveItems(GEMSTONE, 1);
		}
		else if (event.equalsIgnoreCase("7505-06.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(GEMSTONE, 1);
		}
		else if (event.equalsIgnoreCase("7827-07.htm"))
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
				htmltext = (player.getLevel() < 24) ? "7827-00a.htm" : "7827-00.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case LUNDY:
						if (cond == 1)
							htmltext = (!st.hasQuestItems(WORK_HAMMER)) ? "7827-01a.htm" : "7827-02.htm";
						else if (cond == 2)
							htmltext = "7827-03a.htm";
						else if (cond == 3)
							htmltext = "7827-04.htm";
						else if (cond == 4)
							htmltext = "7827-05a.htm";
						else if (cond == 5)
							htmltext = "7827-06.htm";
						break;
					
					case DRIKUS:
						if (cond == 4)
							htmltext = "7505-05.htm";
						else if (cond == 5)
							htmltext = "7505-06a.htm";
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
		
		if (st.dropItemsAlways(GEMSTONE_FRAGMENT, 1, 30))
			st.set("cond", "3");
		
		return null;
	}
}