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
package quests.Q032_AnObviousLie;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q032_AnObviousLie extends Quest
{
	// Items
	private static final int SUEDE = 1866;
	private static final int THREAD = 1868;
	private static final int SPIRIT_ORE = 3031;
	private static final int MAP = 7165;
	private static final int MEDICINAL_HERB = 7166;
	
	// Rewards
	private static final int CAT_EARS = 6843;
	private static final int RACOON_EARS = 7680;
	private static final int RABBIT_EARS = 7683;
	
	// NPCs
	private static final int GENTLER = 7094;
	private static final int MAXIMILIAN = 7120;
	private static final int MIKI_THE_CAT = 8706;
	private static final int ALLIGATOR = 135;

	public static void main(String[] args)
	{
		new Q032_AnObviousLie();
	}
	
	public Q032_AnObviousLie()
	{
		super(32, Q032_AnObviousLie.class.getSimpleName(), "An Obvious Lie");
		
		setItemsIds(MAP, MEDICINAL_HERB);
		
		addStartNpc(MAXIMILIAN);
		addTalkId(MAXIMILIAN, GENTLER, MIKI_THE_CAT);
		
		addKillId(ALLIGATOR);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7120-1.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7094-1.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(MAP, 1);
		}
		else if (event.equalsIgnoreCase("8706-1.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(MAP, 1);
		}
		else if (event.equalsIgnoreCase("7094-4.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(MEDICINAL_HERB, 20);
		}
		else if (event.equalsIgnoreCase("7094-7.htm"))
		{
			if (st.getQuestItemsCount(SPIRIT_ORE) < 500)
				htmltext = "7094-5.htm";
			else
			{
				st.set("cond", "6");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(SPIRIT_ORE, 500);
			}
		}
		else if (event.equalsIgnoreCase("8706-4.htm"))
		{
			st.set("cond", "7");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("7094-10.htm"))
		{
			st.set("cond", "8");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("7094-13.htm"))
			st.playSound(QuestState.SOUND_MIDDLE);
		else if (event.equalsIgnoreCase("cat"))
		{
			if (st.getQuestItemsCount(THREAD) < 1000 || st.getQuestItemsCount(SUEDE) < 500)
				htmltext = "7094-11.htm";
			else
			{
				htmltext = "7094-14.htm";
				st.takeItems(SUEDE, 500);
				st.takeItems(THREAD, 1000);
				st.giveItems(CAT_EARS, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
		}
		else if (event.equalsIgnoreCase("racoon"))
		{
			if (st.getQuestItemsCount(THREAD) < 1000 || st.getQuestItemsCount(SUEDE) < 500)
				htmltext = "7094-11.htm";
			else
			{
				htmltext = "7094-14.htm";
				st.takeItems(SUEDE, 500);
				st.takeItems(THREAD, 1000);
				st.giveItems(RACOON_EARS, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
		}
		else if (event.equalsIgnoreCase("rabbit"))
		{
			if (st.getQuestItemsCount(THREAD) < 1000 || st.getQuestItemsCount(SUEDE) < 500)
				htmltext = "7094-11.htm";
			else
			{
				htmltext = "7094-14.htm";
				st.takeItems(SUEDE, 500);
				st.takeItems(THREAD, 1000);
				st.giveItems(RABBIT_EARS, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
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
				htmltext = (player.getLevel() < 45) ? "7120-0a.htm" : "7120-0.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case MAXIMILIAN:
						htmltext = "7120-2.htm";
						break;
					
					case GENTLER:
						if (cond == 1)
							htmltext = "7094-0.htm";
						else if (cond == 2 || cond == 3)
							htmltext = "7094-2.htm";
						else if (cond == 4)
							htmltext = "7094-3.htm";
						else if (cond == 5)
							htmltext = (st.getQuestItemsCount(SPIRIT_ORE) < 500) ? "7094-5.htm" : "7094-6.htm";
						else if (cond == 6)
							htmltext = "7094-8.htm";
						else if (cond == 7)
							htmltext = "7094-9.htm";
						else if (cond == 8)
							htmltext = (st.getQuestItemsCount(THREAD) < 1000 || st.getQuestItemsCount(SUEDE) < 500) ? "7094-11.htm" : "7094-12.htm";
						break;
					
					case MIKI_THE_CAT:
						if (cond == 2)
							htmltext = "8706-0.htm";
						else if (cond > 2 && cond < 6)
							htmltext = "8706-2.htm";
						else if (cond == 6)
							htmltext = "8706-3.htm";
						else if (cond > 6)
							htmltext = "8706-5.htm";
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
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "3");
		if (st == null)
			return null;
		
		if (st.dropItemsAlways(MEDICINAL_HERB, 1, 20))
		{
			st.set("cond", "4");
		}
		
		return null;
	}
}