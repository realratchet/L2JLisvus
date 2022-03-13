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
package quests.Q030_ChestCaughtWithABaitOfFire;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q030_ChestCaughtWithABaitOfFire extends Quest
{
	// NPCs
	private static final int LINNAEUS = 8577;
	private static final int RUKAL = 7629;
	
	// Items
	private static final int RED_TREASURE_BOX = 6511;
	private static final int MUSICAL_SCORE = 7628;
	private static final int NECKLACE_OF_PROTECTION = 916;

	public static void main(String[] args)
	{
		new Q030_ChestCaughtWithABaitOfFire();
	}
	
	public Q030_ChestCaughtWithABaitOfFire()
	{
		super(30, Q030_ChestCaughtWithABaitOfFire.class.getSimpleName(), "Chest caught with a bait of fire");
		
		setItemsIds(MUSICAL_SCORE);
		
		addStartNpc(LINNAEUS);
		addTalkId(LINNAEUS, RUKAL);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8577-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8577-07.htm"))
		{
			if (st.hasQuestItems(RED_TREASURE_BOX))
			{
				st.set("cond", "2");
				st.takeItems(RED_TREASURE_BOX, 1);
				st.giveItems(MUSICAL_SCORE, 1);
			}
			else
				htmltext = "8577-08.htm";
		}
		else if (event.equalsIgnoreCase("7629-02.htm"))
		{
			if (st.hasQuestItems(MUSICAL_SCORE))
			{
				htmltext = "7629-02.htm";
				st.takeItems(MUSICAL_SCORE, 1);
				st.giveItems(NECKLACE_OF_PROTECTION, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "7629-03.htm";
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
				if (player.getLevel() < 60)
					htmltext = "8577-02.htm";
				else
				{
					QuestState st2 = player.getQuestState("Q053_LinnaeusSpecialBait");
					if (st2 != null && st2.isCompleted())
						htmltext = "8577-01.htm";
					else
						htmltext = "8577-03.htm";
				}
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case LINNAEUS:
						if (cond == 1)
							htmltext = (!st.hasQuestItems(RED_TREASURE_BOX)) ? "8577-06.htm" : "8577-05.htm";
						else if (cond == 2)
							htmltext = "8577-09.htm";
						break;
					
					case RUKAL:
						if (cond == 2)
							htmltext = "7629-01.htm";
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