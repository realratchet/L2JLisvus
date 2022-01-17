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
package quests.Q001_LettersOfLove;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * @author Zoey76
 * 
 * Letters of Love. Rework and adaptation by DnR.
 */
public class Q001_LettersOfLove extends Quest
{
	// NPCs
	private static final int DARIN = 7048;
	private static final int ROXXY = 7006;
	private static final int BAULRO = 7033;
	
	// Items
	private static final int DARINS_LETTER = 687;
	private static final int ROXXYS_KERCHIEF = 688;
	private static final int DARINS_RECEIPT = 1079;
	private static final int BAULROS_POTION = 1080;
	private static final int NECKLACE_OF_KNOWLEDGE = 906;
	
	// Misc
	private static final int MIN_LEVEL = 2;
	
	public static void main(String[] args)
    {
        // Quest class
        new Q001_LettersOfLove();
    }
	
	public Q001_LettersOfLove()
	{
		super(1, Q001_LettersOfLove.class.getSimpleName(), "Letters of Love");

		this.questItemIds = new int[] {DARINS_LETTER, ROXXYS_KERCHIEF, DARINS_RECEIPT, BAULROS_POTION};
		
		addStartNpc(DARIN);
		addTalkId(DARIN);
		addTalkId(ROXXY);
		addTalkId(BAULRO);
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmlText = event;
		if (event.equalsIgnoreCase("7048-06.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(DARINS_LETTER, 1);
		}
		return htmlText;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = getNoQuestMsg();
		L2PcInstance player = st.getPlayer();
		switch (st.getState())
		{
			case State.CREATED:
				if (npc.getNpcId() == DARIN)
				{
					htmltext = (player.getLevel() < MIN_LEVEL) ? "7048-01.htm" : "7048-02.htm";
				}
				break;
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case DARIN:
						if (cond == 1)
						{
							htmltext = "7048-07.htm";
						}
						else if (cond == 2)
						{
							htmltext = "7048-08.htm";
							st.set("cond", "3");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(ROXXYS_KERCHIEF, 1);
							st.giveItems(DARINS_RECEIPT, 1);
						}
						else if (cond == 3)
						{
							htmltext = "7048-09.htm";
						}
						else if (cond == 4)
						{
							htmltext = "7048-10.htm";
							st.takeItems(BAULROS_POTION, 1);
							st.giveItems(NECKLACE_OF_KNOWLEDGE, 1);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					case ROXXY:
						if (cond == 1)
						{
							htmltext = "7006-01.htm";
							st.set("cond", "2");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(DARINS_LETTER, 1);
							st.giveItems(ROXXYS_KERCHIEF, 1);
						}
						else if (cond == 2)
						{
							htmltext = "7006-02.htm";
						}
						else if (cond > 2)
						{
							htmltext = "7006-03.htm";
						}
						break;
					
					case BAULRO:
						if (cond == 3)
						{
							htmltext = "7033-01.htm";
							st.set("cond", "4");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(DARINS_RECEIPT, 1);
							st.giveItems(BAULROS_POTION, 1);
						}
						else if (cond == 4)
						{
							htmltext = "7033-02.htm";
						}
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