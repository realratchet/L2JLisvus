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
package quests.Q151_CureForFeverDisease;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q151_CureForFeverDisease extends Quest
{
	// Items
	private static final int POISON_SAC = 703;
	private static final int FEVER_MEDICINE = 704;
	
	// NPCs
	private static final int ELIAS = 7050;
	private static final int YOHANES = 7032;

	public static void main(String[] args)
	{
		new Q151_CureForFeverDisease();
	}
	
	public Q151_CureForFeverDisease()
	{
		super(151, Q151_CureForFeverDisease.class.getSimpleName(), "Cure for Fever Disease");
		
		setItemsIds(FEVER_MEDICINE, POISON_SAC);
		
		addStartNpc(ELIAS);
		addTalkId(ELIAS, YOHANES);
		
		addKillId(103, 106, 108);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7050-03.htm"))
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
				htmltext = (player.getLevel() < 15) ? "7050-01.htm" : "7050-02.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case ELIAS:
						if (cond == 1)
							htmltext = "7050-04.htm";
						else if (cond == 2)
							htmltext = "7050-05.htm";
						else if (cond == 3)
						{
							htmltext = "7050-06.htm";
							st.takeItems(FEVER_MEDICINE, 1);
							st.giveItems(102, 1);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case YOHANES:
						if (cond == 2)
						{
							htmltext = "7032-01.htm";
							st.set("cond", "3");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(POISON_SAC, 1);
							st.giveItems(FEVER_MEDICINE, 1);
						}
						else if (cond == 3)
							htmltext = "7032-02.htm";
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
		
		if (st.dropItems(POISON_SAC, 1, 1, 200000))
			st.set("cond", "2");
		
		return null;
	}
}