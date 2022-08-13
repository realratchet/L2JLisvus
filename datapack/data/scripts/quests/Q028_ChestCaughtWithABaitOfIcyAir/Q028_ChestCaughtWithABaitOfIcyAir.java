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
package quests.Q028_ChestCaughtWithABaitOfIcyAir;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q028_ChestCaughtWithABaitOfIcyAir extends Quest
{
	// NPCs
	private static final int OFULLE = 8572;
	private static final int KIKI = 8442;
	
	// Items
	private static final int BIG_YELLOW_TREASURE_CHEST = 6503;
	private static final int KIKI_LETTER = 7626;
	private static final int ELVEN_RING = 881;

	public static void main(String[] args)
	{
		new Q028_ChestCaughtWithABaitOfIcyAir();
	}
	
	public Q028_ChestCaughtWithABaitOfIcyAir()
	{
		super(28, Q028_ChestCaughtWithABaitOfIcyAir.class.getSimpleName(), "Chest caught with a bait of icy air");
		
		setItemsIds(KIKI_LETTER);
		
		addStartNpc(OFULLE);
		addTalkId(OFULLE, KIKI);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8572-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8572-07.htm"))
		{
			if (st.hasQuestItems(BIG_YELLOW_TREASURE_CHEST))
			{
				st.set("cond", "2");
				st.takeItems(BIG_YELLOW_TREASURE_CHEST, 1);
				st.giveItems(KIKI_LETTER, 1);
			}
			else
				htmltext = "8572-08.htm";
		}
		else if (event.equalsIgnoreCase("8442-02.htm"))
		{
			if (st.hasQuestItems(KIKI_LETTER))
			{
				htmltext = "8442-02.htm";
				st.takeItems(KIKI_LETTER, 1);
				st.giveItems(ELVEN_RING, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "8442-03.htm";
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
				if (player.getLevel() < 36)
					htmltext = "8572-02.htm";
				else
				{
					QuestState st2 = player.getQuestState("Q051_OFullesSpecialBait");
					if (st2 != null && st2.isCompleted())
						htmltext = "8572-01.htm";
					else
						htmltext = "8572-03.htm";
				}
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case OFULLE:
						if (cond == 1)
							htmltext = (!st.hasQuestItems(BIG_YELLOW_TREASURE_CHEST)) ? "8572-06.htm" : "8572-05.htm";
						else if (cond == 2)
							htmltext = "8572-09.htm";
						break;
					
					case KIKI:
						if (cond == 2)
							htmltext = "8442-01.htm";
						break;
				}
				break;
			
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}