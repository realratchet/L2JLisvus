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
package quests.Q169_OffspringOfNightmares;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q169_OffspringOfNightmares extends Quest
{
	// NPC
	private static final int VLASTY = 7145;

	// Items
	private static final int CRACKED_SKULL = 1030;
	private static final int PERFECT_SKULL = 1031;
	private static final int BONE_GAITERS = 31;

	public static void main(String[] args)
	{
		new Q169_OffspringOfNightmares();
	}
	
	public Q169_OffspringOfNightmares()
	{
		super(169, Q169_OffspringOfNightmares.class.getSimpleName(), "Offspring of Nightmares");
		
		setItemsIds(CRACKED_SKULL, PERFECT_SKULL);
		
		addStartNpc(VLASTY);
		addTalkId(VLASTY);
		
		addKillId(105, 25);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7145-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7145-08.htm"))
		{
			int reward = 17000 + (st.getQuestItemsCount(CRACKED_SKULL) * 20);
			st.takeItems(PERFECT_SKULL, -1);
			st.takeItems(CRACKED_SKULL, -1);
			st.giveItems(BONE_GAITERS, 1);
			st.rewardItems(57, reward);
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
				if (player.getRace() != Race.DARK_ELF)
					htmltext = "7145-00.htm";
				else if (player.getLevel() < 15)
					htmltext = "7145-02.htm";
				else
					htmltext = "7145-03.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				if (cond == 1)
				{
					if (st.hasQuestItems(CRACKED_SKULL))
						htmltext = "7145-06.htm";
					else
						htmltext = "7145-05.htm";
				}
				else if (cond == 2)
					htmltext = "7145-07.htm";
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
		final QuestState st = checkPlayerState(killer, npc, State.STARTED);
		if (st == null)
			return null;
		
		if (st.getInt("cond") == 1 && st.dropItems(PERFECT_SKULL, 1, 1, 200000))
			st.set("cond", "2");
		else
			st.dropItems(CRACKED_SKULL, 1, 0, 500000);
		
		return null;
	}
}