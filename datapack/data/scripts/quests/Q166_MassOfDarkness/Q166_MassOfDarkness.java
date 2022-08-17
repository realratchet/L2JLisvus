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
package quests.Q166_MassOfDarkness;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q166_MassOfDarkness extends Quest
{
	// NPCs
	private static final int UNDRIAS = 7130;
	private static final int IRIA = 7135;
	private static final int DORANKUS = 7139;
	private static final int TRUDY = 7143;
	
	// Items
	private static final int UNDRIAS_LETTER = 1088;
	private static final int CEREMONIAL_DAGGER = 1089;
	private static final int DREVIANT_WINE = 1090;
	private static final int GARMIEL_SCRIPTURE = 1091;

	public static void main(String[] args)
	{
		new Q166_MassOfDarkness();
	}
	
	public Q166_MassOfDarkness()
	{
		super(166, Q166_MassOfDarkness.class.getSimpleName(), "Mass of Darkness");
		
		setItemsIds(UNDRIAS_LETTER, CEREMONIAL_DAGGER, DREVIANT_WINE, GARMIEL_SCRIPTURE);
		
		addStartNpc(UNDRIAS);
		addTalkId(UNDRIAS, IRIA, DORANKUS, TRUDY);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7130-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(UNDRIAS_LETTER, 1);
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
				if (player.getRace() != Race.DARK_ELF)
					htmltext = "7130-00.htm";
				else if (player.getLevel() < 2)
					htmltext = "7130-02.htm";
				else
					htmltext = "7130-03.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case UNDRIAS:
						if (cond == 1)
							htmltext = "7130-05.htm";
						else if (cond == 2)
						{
							htmltext = "7130-06.htm";
							st.takeItems(CEREMONIAL_DAGGER, 1);
							st.takeItems(DREVIANT_WINE, 1);
							st.takeItems(GARMIEL_SCRIPTURE, 1);
							st.takeItems(UNDRIAS_LETTER, 1);
							st.rewardItems(57, 500);
							st.addExpAndSp(500, 0);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case IRIA:
						if (cond == 1 && !st.hasQuestItems(CEREMONIAL_DAGGER))
						{
							htmltext = "7135-01.htm";
							st.giveItems(CEREMONIAL_DAGGER, 1);
							
							if (st.hasQuestItems(DREVIANT_WINE, GARMIEL_SCRIPTURE))
							{
								st.set("cond", "2");
								st.playSound(QuestState.SOUND_MIDDLE);
							}
							else
								st.playSound(QuestState.SOUND_ITEMGET);
						}
						else if (cond == 2)
							htmltext = "7135-02.htm";
						break;
					
					case DORANKUS:
						if (cond == 1 && !st.hasQuestItems(DREVIANT_WINE))
						{
							htmltext = "7139-01.htm";
							st.giveItems(DREVIANT_WINE, 1);
							
							if (st.hasQuestItems(CEREMONIAL_DAGGER, GARMIEL_SCRIPTURE))
							{
								st.set("cond", "2");
								st.playSound(QuestState.SOUND_MIDDLE);
							}
							else
								st.playSound(QuestState.SOUND_ITEMGET);
						}
						else if (cond == 2)
							htmltext = "7139-02.htm";
						break;
					
					case TRUDY:
						if (cond == 1 && !st.hasQuestItems(GARMIEL_SCRIPTURE))
						{
							htmltext = "7143-01.htm";
							st.giveItems(GARMIEL_SCRIPTURE, 1);
							
							if (st.hasQuestItems(CEREMONIAL_DAGGER, DREVIANT_WINE))
							{
								st.set("cond", "2");
								st.playSound(QuestState.SOUND_MIDDLE);
							}
							else
								st.playSound(QuestState.SOUND_ITEMGET);
						}
						else if (cond == 2)
							htmltext = "7143-02.htm";
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