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
package quests.Q037_MakeFormalWear;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q037_MakeFormalWear extends Quest
{
	// NPCs
	private static final int ALEXIS = 7842;
	private static final int LEIKAR = 8520;
	private static final int JEREMY = 8521;
	private static final int MIST = 8627;
	
	// Items
	private static final int MYSTERIOUS_CLOTH = 7076;
	private static final int JEWEL_BOX = 7077;
	private static final int SEWING_KIT = 7078;
	private static final int DRESS_SHOES_BOX = 7113;
	private static final int SIGNET_RING = 7164;
	private static final int ICE_WINE = 7160;
	private static final int BOX_OF_COOKIES = 7159;
	
	// Reward
	private static final int FORMAL_WEAR = 6408;
	
	public static void main(String[] args)
	{
		new Q037_MakeFormalWear();
	}

	public Q037_MakeFormalWear()
	{
		super(37, Q037_MakeFormalWear.class.getSimpleName(), "Make Formal Wear");
		
		setItemsIds(SIGNET_RING, ICE_WINE, BOX_OF_COOKIES);
		
		addStartNpc(ALEXIS);
		addTalkId(ALEXIS, LEIKAR, JEREMY, MIST);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7842-1.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8520-1.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(SIGNET_RING, 1);
		}
		else if (event.equalsIgnoreCase("8521-1.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(SIGNET_RING, 1);
			st.giveItems(ICE_WINE, 1);
		}
		else if (event.equalsIgnoreCase("8627-1.htm"))
		{
			st.set("cond", "4");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(ICE_WINE, 1);
		}
		else if (event.equalsIgnoreCase("8521-3.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(BOX_OF_COOKIES, 1);
		}
		else if (event.equalsIgnoreCase("8520-3.htm"))
		{
			st.set("cond", "6");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(BOX_OF_COOKIES, 1);
		}
		else if (event.equalsIgnoreCase("8520-5.htm"))
		{
			st.set("cond", "7");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(JEWEL_BOX, 1);
			st.takeItems(MYSTERIOUS_CLOTH, 1);
			st.takeItems(SEWING_KIT, 1);
		}
		else if (event.equalsIgnoreCase("8520-7.htm"))
		{
			st.takeItems(DRESS_SHOES_BOX, 1);
			st.giveItems(FORMAL_WEAR, 1);
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
				htmltext = (player.getLevel() < 60) ? "7842-0a.htm" : "7842-0.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case ALEXIS:
						if (cond == 1)
							htmltext = "7842-2.htm";
						break;
					
					case LEIKAR:
						if (cond == 1)
							htmltext = "8520-0.htm";
						else if (cond == 2)
							htmltext = "8520-1a.htm";
						else if (cond == 5 || cond == 6)
						{
							if (st.hasQuestItems(MYSTERIOUS_CLOTH, JEWEL_BOX, SEWING_KIT))
								htmltext = "8520-4.htm";
							else if (st.hasQuestItems(BOX_OF_COOKIES))
								htmltext = "8520-2.htm";
							else
								htmltext = "8520-3a.htm";
						}
						else if (cond == 7)
							htmltext = (st.hasQuestItems(DRESS_SHOES_BOX)) ? "8520-6.htm" : "8520-5a.htm";
						break;
					
					case JEREMY:
						if (st.hasQuestItems(SIGNET_RING))
							htmltext = "8521-0.htm";
						else if (cond == 3)
							htmltext = "8521-1a.htm";
						else if (cond == 4)
							htmltext = "8521-2.htm";
						else if (cond > 4)
							htmltext = "8521-3a.htm";
						break;
					
					case MIST:
						if (cond == 3)
							htmltext = "8627-0.htm";
						else if (cond > 3)
							htmltext = "8627-2.htm";
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