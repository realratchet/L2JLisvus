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
package quests.Q165_ShilensHunt;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q165_ShilensHunt extends Quest
{
	// NPC
	private static final int NELSYA = 7348;

	// Monsters
	private static final int ASHEN_WOLF = 456;
	private static final int YOUNG_BROWN_KELTIR = 529;
	private static final int BROWN_KELTIR = 532;
	private static final int ELDER_BROWN_KELTIR = 536;
	
	// Items
	private static final int DARK_BEZOAR = 1160;
	private static final int LESSER_HEALING_POTION = 1060;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(ASHEN_WOLF, 1000000);
		CHANCES.put(YOUNG_BROWN_KELTIR, 333333);
		CHANCES.put(BROWN_KELTIR, 333333);
		CHANCES.put(ELDER_BROWN_KELTIR, 666667);
	}

	public static void main(String[] args)
	{
		new Q165_ShilensHunt();
	}
	
	public Q165_ShilensHunt()
	{
		super(165, Q165_ShilensHunt.class.getSimpleName(), "Shilen's Hunt");
		
		setItemsIds(DARK_BEZOAR);
		
		addStartNpc(NELSYA);
		addTalkId(NELSYA);
		
		addKillId(ASHEN_WOLF, YOUNG_BROWN_KELTIR, BROWN_KELTIR, ELDER_BROWN_KELTIR);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7348-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
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
					htmltext = "7348-00.htm";
				else if (player.getLevel() < 3)
					htmltext = "7348-01.htm";
				else
					htmltext = "7348-02.htm";
				break;
			
			case State.STARTED:
				if (st.getQuestItemsCount(DARK_BEZOAR) >= 13)
				{
					htmltext = "7348-05.htm";
					st.takeItems(DARK_BEZOAR, -1);
					st.rewardItems(LESSER_HEALING_POTION, 5);
					st.addExpAndSp(1000, 0);
					st.playSound(QuestState.SOUND_FINISH);
					st.exitQuest(false);
				}
				else
					htmltext = "7348-04.htm";
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
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "1");
		if (st == null)
			return null;
		
		if (st.dropItems(DARK_BEZOAR, 1, 13, CHANCES.get(npc.getNpcId())))
			st.set("cond", "2");
		
		return null;
	}
}