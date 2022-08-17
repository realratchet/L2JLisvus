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
package quests.Q167_DwarvenKinship;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q167_DwarvenKinship extends Quest
{
	// Items
	private static final int CARLON_LETTER = 1076;
	private static final int NORMAN_LETTER = 1106;
	
	// NPCs
	private static final int CARLON = 7350;
	private static final int NORMAN = 7210;
	private static final int HAPROCK = 7255;

	public static void main(String[] args)
	{
		new Q167_DwarvenKinship();
	}
	
	public Q167_DwarvenKinship()
	{
		super(167, Q167_DwarvenKinship.class.getSimpleName(), "Dwarven Kinship");
		
		setItemsIds(CARLON_LETTER, NORMAN_LETTER);
		
		addStartNpc(CARLON);
		addTalkId(CARLON, HAPROCK, NORMAN);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7350-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(CARLON_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("7255-03.htm"))
		{
			st.set("cond", "2");
			st.takeItems(CARLON_LETTER, 1);
			st.giveItems(NORMAN_LETTER, 1);
			st.rewardItems(57, 2000);
		}
		else if (event.equalsIgnoreCase("7255-04.htm"))
		{
			st.takeItems(CARLON_LETTER, 1);
			st.rewardItems(57, 3000);
			st.playSound(QuestState.SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("7210-02.htm"))
		{
			st.takeItems(NORMAN_LETTER, 1);
			st.rewardItems(57, 20000);
			st.playSound(QuestState.SOUND_FINISH);
			st.exitQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				htmltext = (player.getLevel() < 15) ? "7350-02.htm" : "7350-03.htm";
				break;
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case CARLON:
						if (cond == 1)
							htmltext = "7350-05.htm";
						break;
					case HAPROCK:
						if (cond == 1)
							htmltext = "7255-01.htm";
						else if (cond == 2)
							htmltext = "7255-05.htm";
						break;
					case NORMAN:
						if (cond == 2)
							htmltext = "7210-01.htm";
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