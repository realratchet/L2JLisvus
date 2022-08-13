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
package quests.Q033_MakeAPairOfDressShoes;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q033_MakeAPairOfDressShoes extends Quest
{
	// NPCs
	private static final int WOODLEY = 7838;
	private static final int IAN = 7164;
	private static final int LEIKAR = 8520;
	
	// Items
	private static final int LEATHER = 1882;
	private static final int THREAD = 1868;
	private static final int ADENA = 57;
	
	// Rewards
	public static int DRESS_SHOES_BOX = 7113;
	
	public static void main(String[] args)
	{
		new Q033_MakeAPairOfDressShoes();
	}
	
	public Q033_MakeAPairOfDressShoes()
	{
		super(33, Q033_MakeAPairOfDressShoes.class.getSimpleName(), "Make a Pair of Dress Shoes");
		
		addStartNpc(WOODLEY);
		addTalkId(WOODLEY, IAN, LEIKAR);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7838-1.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8520-1.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("7838-3.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("7838-5.htm"))
		{
			if (st.getQuestItemsCount(LEATHER) >= 200 && st.getQuestItemsCount(THREAD) >= 600 && st.getQuestItemsCount(ADENA) >= 200000)
			{
				st.set("cond", "4");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(ADENA, 200000);
				st.takeItems(LEATHER, 200);
				st.takeItems(THREAD, 600);
			}
			else
				htmltext = "7838-4a.htm";
		}
		else if (event.equalsIgnoreCase("7164-1.htm"))
		{
			if (st.getQuestItemsCount(ADENA) >= 300000)
			{
				st.set("cond", "5");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(ADENA, 300000);
			}
			else
				htmltext = "7164-1a.htm";
		}
		else if (event.equalsIgnoreCase("7838-7.htm"))
		{
			st.giveItems(DRESS_SHOES_BOX, 1);
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
				if (player.getLevel() >= 60)
				{
					QuestState fwear = player.getQuestState("Q037_MakeFormalWear");
					if (fwear != null && fwear.getInt("cond") == 7)
						htmltext = "7838-0.htm";
					else
						htmltext = "7838-0a.htm";
				}
				else
					htmltext = "7838-0b.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case WOODLEY:
						if (cond == 1)
							htmltext = "7838-1.htm";
						else if (cond == 2)
							htmltext = "7838-2.htm";
						else if (cond == 3)
						{
							if (st.getQuestItemsCount(LEATHER) >= 200 && st.getQuestItemsCount(THREAD) >= 600 && st.getQuestItemsCount(ADENA) >= 200000)
								htmltext = "7838-4.htm";
							else
								htmltext = "7838-4a.htm";
						}
						else if (cond == 4)
							htmltext = "7838-5a.htm";
						else if (cond == 5)
							htmltext = "7838-6.htm";
						break;
					
					case LEIKAR:
						if (cond == 1)
							htmltext = "8520-0.htm";
						else if (cond > 1)
							htmltext = "8520-1a.htm";
						break;
					
					case IAN:
						if (cond == 4)
							htmltext = "7164-0.htm";
						else if (cond == 5)
							htmltext = "7164-2.htm";
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