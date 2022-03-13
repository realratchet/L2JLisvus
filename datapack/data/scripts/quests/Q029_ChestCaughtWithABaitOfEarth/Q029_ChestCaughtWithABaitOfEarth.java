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
package quests.Q029_ChestCaughtWithABaitOfEarth;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q029_ChestCaughtWithABaitOfEarth extends Quest
{
	// NPCs
	private static final int WILLIE = 8574;
	private static final int ANABEL = 7909;
	
	// Items
	private static final int SMALL_PURPLE_TREASURE_CHEST = 6507;
	private static final int SMALL_GLASS_BOX = 7627;
	private static final int PLATED_LEATHER_GLOVES = 2455;

	public static void main(String[] args)
	{
		new Q029_ChestCaughtWithABaitOfEarth();
	}
	
	public Q029_ChestCaughtWithABaitOfEarth()
	{
		super(29, Q029_ChestCaughtWithABaitOfEarth.class.getSimpleName(), "Chest caught with a bait of earth");
		
		setItemsIds(SMALL_GLASS_BOX);
		
		addStartNpc(WILLIE);
		addTalkId(WILLIE, ANABEL);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8574-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8574-07.htm"))
		{
			if (st.hasQuestItems(SMALL_PURPLE_TREASURE_CHEST))
			{
				st.set("cond", "2");
				st.takeItems(SMALL_PURPLE_TREASURE_CHEST, 1);
				st.giveItems(SMALL_GLASS_BOX, 1);
			}
			else
				htmltext = "8574-08.htm";
		}
		else if (event.equalsIgnoreCase("7909-02.htm"))
		{
			if (st.hasQuestItems(SMALL_GLASS_BOX))
			{
				htmltext = "7909-02.htm";
				st.takeItems(SMALL_GLASS_BOX, 1);
				st.giveItems(PLATED_LEATHER_GLOVES, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "7909-03.htm";
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
				if (player.getLevel() < 48)
					htmltext = "8574-02.htm";
				else
				{
					QuestState st2 = player.getQuestState("Q052_WilliesSpecialBait");
					if (st2 != null && st2.isCompleted())
						htmltext = "8574-01.htm";
					else
						htmltext = "8574-03.htm";
				}
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case WILLIE:
						if (cond == 1)
							htmltext = (!st.hasQuestItems(SMALL_PURPLE_TREASURE_CHEST)) ? "8574-06.htm" : "8574-05.htm";
						else if (cond == 2)
							htmltext = "8574-09.htm";
						break;
					
					case ANABEL:
						if (cond == 2)
							htmltext = "7909-01.htm";
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