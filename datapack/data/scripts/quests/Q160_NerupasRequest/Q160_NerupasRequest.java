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
package quests.Q160_NerupasRequest;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q160_NerupasRequest extends Quest
{
	// Items
	private static final int SILVERY_SPIDERSILK = 1026;
	private static final int UNOREN_RECEIPT = 1027;
	private static final int CREAMEES_TICKET = 1028;
	private static final int NIGHTSHADE_LEAF = 1029;
	
	// Reward
	private static final int LESSER_HEALING_POTION = 1060;
	
	// NPCs
	private static final int NERUPA = 7370;
	private static final int UNOREN = 7147;
	private static final int CREAMEES = 7149;
	private static final int JULIA = 7152;

	public static void main(String[] args)
	{
		new Q160_NerupasRequest();
	}
	
	public Q160_NerupasRequest()
	{
		super(160, Q160_NerupasRequest.class.getSimpleName(), "Nerupa's Request");
		
		setItemsIds(SILVERY_SPIDERSILK, UNOREN_RECEIPT, CREAMEES_TICKET, NIGHTSHADE_LEAF);
		
		addStartNpc(NERUPA);
		addTalkId(NERUPA, UNOREN, CREAMEES, JULIA);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7370-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(SILVERY_SPIDERSILK, 1);
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
				if (player.getRace() != Race.ELF)
					htmltext = "7370-00.htm";
				else if (player.getLevel() < 3)
					htmltext = "7370-02.htm";
				else
					htmltext = "7370-03.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case NERUPA:
						if (cond < 4)
							htmltext = "7370-05.htm";
						else if (cond == 4)
						{
							htmltext = "7370-06.htm";
							st.takeItems(NIGHTSHADE_LEAF, 1);
							st.rewardItems(LESSER_HEALING_POTION, 5);
							st.addExpAndSp(1000, 0);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case UNOREN:
						if (cond == 1)
						{
							htmltext = "7147-01.htm";
							st.set("cond", "2");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(SILVERY_SPIDERSILK, 1);
							st.giveItems(UNOREN_RECEIPT, 1);
						}
						else if (cond == 2)
							htmltext = "7147-02.htm";
						else if (cond == 4)
							htmltext = "7147-03.htm";
						break;
					
					case CREAMEES:
						if (cond == 2)
						{
							htmltext = "7149-01.htm";
							st.set("cond", "3");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(UNOREN_RECEIPT, 1);
							st.giveItems(CREAMEES_TICKET, 1);
						}
						else if (cond == 3)
							htmltext = "7149-02.htm";
						else if (cond == 4)
							htmltext = "7149-03.htm";
						break;
					
					case JULIA:
						if (cond == 3)
						{
							htmltext = "7152-01.htm";
							st.set("cond", "4");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(CREAMEES_TICKET, 1);
							st.giveItems(NIGHTSHADE_LEAF, 1);
						}
						else if (cond == 4)
							htmltext = "7152-02.htm";
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