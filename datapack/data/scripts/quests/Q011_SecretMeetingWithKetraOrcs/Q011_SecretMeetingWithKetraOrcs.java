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
package quests.Q011_SecretMeetingWithKetraOrcs;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q011_SecretMeetingWithKetraOrcs extends Quest
{
	// Npcs
	private static final int CADMON = 8296;
	private static final int LEON = 8256;
	private static final int WAHKAN = 8371;
	
	// Items
	private static final int MUNITIONS_BOX = 7231;
	
	public static void main(String[] args)
	{
		new Q011_SecretMeetingWithKetraOrcs();
	}
	
	public Q011_SecretMeetingWithKetraOrcs()
	{
		super(11, Q011_SecretMeetingWithKetraOrcs.class.getSimpleName(), "Secret Meeting With Ketra Orcs");
		
		setItemsIds(MUNITIONS_BOX);
		
		addStartNpc(CADMON);
		addTalkId(CADMON, LEON, WAHKAN);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8296-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8256-02.htm"))
		{
			st.giveItems(MUNITIONS_BOX, 1);
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8371-02.htm"))
		{
			st.takeItems(MUNITIONS_BOX, 1);
			st.addExpAndSp(79787, 0);
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
				htmltext = (player.getLevel() < 74) ? "8296-02.htm" : "8296-01.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case CADMON:
						if (cond == 1)
							htmltext = "8296-04.htm";
						break;
					
					case LEON:
						if (cond == 1)
							htmltext = "8256-01.htm";
						else if (cond == 2)
							htmltext = "8256-03.htm";
						break;
					
					case WAHKAN:
						if (cond == 2)
							htmltext = "8371-01.htm";
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
