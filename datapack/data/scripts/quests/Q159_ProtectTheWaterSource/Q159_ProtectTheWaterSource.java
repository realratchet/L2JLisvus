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
package quests.Q159_ProtectTheWaterSource;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q159_ProtectTheWaterSource extends Quest
{
	// NPCs
	private static final int ASTERIOS = 7154;

	private static final int PLAGUE_ZOMBIE = 5017;

	// Items
	private static final int PLAGUE_DUST = 1035;
	private static final int HYACINTH_CHARM_1 = 1071;
	private static final int HYACINTH_CHARM_2 = 1072;

	public static void main(String[] args)
	{
		new Q159_ProtectTheWaterSource();
	}
	
	public Q159_ProtectTheWaterSource()
	{
		super(159, Q159_ProtectTheWaterSource.class.getSimpleName(), "Protect the Water Source");
		
		setItemsIds(PLAGUE_DUST, HYACINTH_CHARM_1, HYACINTH_CHARM_2);
		
		addStartNpc(ASTERIOS);
		addTalkId(ASTERIOS);
		
		addKillId(PLAGUE_ZOMBIE);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7154-04.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(HYACINTH_CHARM_1, 1);
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
					htmltext = "7154-00.htm";
				else if (player.getLevel() < 12)
					htmltext = "7154-02.htm";
				else
					htmltext = "7154-03.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				if (cond == 1)
					htmltext = "7154-05.htm";
				else if (cond == 2)
				{
					htmltext = "7154-06.htm";
					st.set("cond", "3");
					st.playSound(QuestState.SOUND_MIDDLE);
					st.takeItems(PLAGUE_DUST, -1);
					st.takeItems(HYACINTH_CHARM_1, 1);
					st.giveItems(HYACINTH_CHARM_2, 1);
				}
				else if (cond == 3)
					htmltext = "7154-07.htm";
				else if (cond == 4)
				{
					htmltext = "7154-08.htm";
					st.takeItems(HYACINTH_CHARM_2, 1);
					st.takeItems(PLAGUE_DUST, -1);
					st.rewardItems(57, 18250);
					st.playSound(QuestState.SOUND_FINISH);
					st.exitQuest(false);
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
		final QuestState st = checkPlayerState(killer, npc, State.STARTED);
		if (st == null)
			return null;
		
		if (st.getInt("cond") == 1 && st.dropItems(PLAGUE_DUST, 1, 1, 400000))
			st.set("cond", "2");
		else if (st.getInt("cond") == 3 && st.dropItems(PLAGUE_DUST, 1, 5, 400000))
			st.set("cond", "4");
		
		return null;
	}
}