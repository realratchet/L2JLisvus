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
package quests.Q154_SacrificeToTheSea;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q154_SacrificeToTheSea extends Quest
{
	// NPCs
	private static final int ROCKSWELL = 7312;
	private static final int CRISTEL = 7051;
	private static final int ROLFE = 7055;
	
	// Items
	private static final int FOX_FUR = 1032;
	private static final int FOX_FUR_YARN = 1033;
	private static final int MAIDEN_DOLL = 1034;
	
	// Reward
	private static final int EARING = 113;

	public static void main(String[] args)
	{
		new Q154_SacrificeToTheSea();
	}
	
	public Q154_SacrificeToTheSea()
	{
		super(154, Q154_SacrificeToTheSea.class.getSimpleName(), "Sacrifice to the Sea");
		
		setItemsIds(FOX_FUR, FOX_FUR_YARN, MAIDEN_DOLL);
		
		addStartNpc(ROCKSWELL);
		addTalkId(ROCKSWELL, CRISTEL, ROLFE);
		
		addKillId(481, 544, 545); // Following Keltirs can be found near Talking Island
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7312-04.htm"))
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
				htmltext = (player.getLevel() < 2) ? "7312-02.htm" : "7312-03.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case ROCKSWELL:
						if (cond == 1)
							htmltext = "7312-05.htm";
						else if (cond == 2)
							htmltext = "7312-08.htm";
						else if (cond == 3)
							htmltext = "7312-06.htm";
						else if (cond == 4)
						{
							htmltext = "7312-07.htm";
							st.takeItems(MAIDEN_DOLL, -1);
							st.giveItems(EARING, 1);
							st.addExpAndSp(100, 0);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case CRISTEL:
						if (cond == 1)
							htmltext = (st.hasQuestItems(FOX_FUR)) ? "7051-01.htm" : "7051-01a.htm";
						else if (cond == 2)
						{
							htmltext = "7051-02.htm";
							st.set("cond", "3");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(FOX_FUR, -1);
							st.giveItems(FOX_FUR_YARN, 1);
						}
						else if (cond == 3)
							htmltext = "7051-03.htm";
						else if (cond == 4)
							htmltext = "7051-04.htm";
						break;
					
					case ROLFE:
						if (cond < 3)
							htmltext = "7055-03.htm";
						else if (cond == 3)
						{
							htmltext = "7055-01.htm";
							st.set("cond", "4");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(FOX_FUR_YARN, 1);
							st.giveItems(MAIDEN_DOLL, 1);
						}
						else if (cond == 4)
							htmltext = "7055-02.htm";
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
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "1");
		if (st == null)
			return null;
		
		if (st.dropItems(FOX_FUR, 1, 10, 400000))
			st.set("cond", "2");
		
		return null;
	}
}