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
package quests.Q027_ChestCaughtWithABaitOfWind;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q027_ChestCaughtWithABaitOfWind extends Quest
{
	// NPCs
	private static final int LANOSCO = 8570;
	private static final int SHALING = 8434;
	
	// Items
	private static final int LARGE_BLUE_TREASURE_CHEST = 6500;
	private static final int STRANGE_BLUEPRINT = 7625;
	private static final int BLACK_PEARL_RING = 880;

	public static void main(String[] args)
	{
		new Q027_ChestCaughtWithABaitOfWind();
	}
	
	public Q027_ChestCaughtWithABaitOfWind()
	{
		super(27, Q027_ChestCaughtWithABaitOfWind.class.getSimpleName(), "Chest caught with a bait of wind");
		
		setItemsIds(STRANGE_BLUEPRINT);
		
		addStartNpc(LANOSCO);
		addTalkId(LANOSCO, SHALING);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8570-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8570-07.htm"))
		{
			if (st.hasQuestItems(LARGE_BLUE_TREASURE_CHEST))
			{
				st.set("cond", "2");
				st.takeItems(LARGE_BLUE_TREASURE_CHEST, 1);
				st.giveItems(STRANGE_BLUEPRINT, 1);
			}
			else
				htmltext = "8570-08.htm";
		}
		else if (event.equalsIgnoreCase("8434-02.htm"))
		{
			if (st.hasQuestItems(STRANGE_BLUEPRINT))
			{
				htmltext = "8434-02.htm";
				st.takeItems(STRANGE_BLUEPRINT, 1);
				st.giveItems(BLACK_PEARL_RING, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "8434-03.htm";
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
				if (player.getLevel() < 27)
					htmltext = "8570-02.htm";
				else
				{
					QuestState st2 = player.getQuestState("Q050_LanoscosSpecialBait");
					if (st2 != null && st2.isCompleted())
						htmltext = "8570-01.htm";
					else
						htmltext = "8570-03.htm";
				}
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case LANOSCO:
						if (cond == 1)
							htmltext = (!st.hasQuestItems(LARGE_BLUE_TREASURE_CHEST)) ? "8570-06.htm" : "8570-05.htm";
						else if (cond == 2)
							htmltext = "8570-09.htm";
						break;
					
					case SHALING:
						if (cond == 2)
							htmltext = "8434-01.htm";
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