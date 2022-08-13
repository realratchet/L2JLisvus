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
package quests.Q039_RedEyedInvaders;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q039_RedEyedInvaders extends Quest
{
	// NPCs
	private static final int BABENCO = 7334;
	private static final int BATHIS = 7332;
	
	// Mobs
	private static final int MAILLE_LIZARDMAN = 919;
	private static final int MAILLE_LIZARDMAN_SCOUT = 920;
	private static final int MAILLE_LIZARDMAN_GUARD = 921;
	private static final int ARANEID = 925;
	
	// Items
	private static final int BLACK_BONE_NECKLACE = 7178;
	private static final int RED_BONE_NECKLACE = 7179;
	private static final int INCENSE_POUCH = 7180;
	private static final int GEM_OF_MAILLE = 7181;
	
	// First droplist
	private static final Map<Integer, int[]> FIRST_DP = new HashMap<>();
	{
		FIRST_DP.put(MAILLE_LIZARDMAN_GUARD, new int[]
		{
			RED_BONE_NECKLACE,
			BLACK_BONE_NECKLACE
		});
		FIRST_DP.put(MAILLE_LIZARDMAN, new int[]
		{
			BLACK_BONE_NECKLACE,
			RED_BONE_NECKLACE
		});
		FIRST_DP.put(MAILLE_LIZARDMAN_SCOUT, new int[]
		{
			BLACK_BONE_NECKLACE,
			RED_BONE_NECKLACE
		});
	}
	
	// Second droplist
	private static final Map<Integer, int[]> SECOND_DP = new HashMap<>();
	{
		SECOND_DP.put(ARANEID, new int[]
		{
			GEM_OF_MAILLE,
			INCENSE_POUCH,
			500000
		});
		SECOND_DP.put(MAILLE_LIZARDMAN_GUARD, new int[]
		{
			INCENSE_POUCH,
			GEM_OF_MAILLE,
			300000
		});
		SECOND_DP.put(MAILLE_LIZARDMAN_SCOUT, new int[]
		{
			INCENSE_POUCH,
			GEM_OF_MAILLE,
			250000
		});
	}
	
	// Rewards
	private static final int GREEN_COLORED_LURE_HG = 6521;
	private static final int BABY_DUCK_RODE = 6529;
	private static final int FISHING_SHOT_NG = 6535;
	
	public static void main(String[] args)
	{
		new Q039_RedEyedInvaders();
	}

	public Q039_RedEyedInvaders()
	{
		super(39, Q039_RedEyedInvaders.class.getSimpleName(), "Red-Eyed Invaders");
		
		setItemsIds(BLACK_BONE_NECKLACE, RED_BONE_NECKLACE, INCENSE_POUCH, GEM_OF_MAILLE);
		
		addStartNpc(BABENCO);
		addTalkId(BABENCO, BATHIS);
		
		addKillId(MAILLE_LIZARDMAN, MAILLE_LIZARDMAN_SCOUT, MAILLE_LIZARDMAN_GUARD, ARANEID);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7334-1.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7332-1.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("7332-3.htm"))
		{
			st.set("cond", "4");
			st.takeItems(BLACK_BONE_NECKLACE, -1);
			st.takeItems(RED_BONE_NECKLACE, -1);
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("7332-5.htm"))
		{
			st.takeItems(INCENSE_POUCH, -1);
			st.takeItems(GEM_OF_MAILLE, -1);
			st.giveItems(GREEN_COLORED_LURE_HG, 60);
			st.giveItems(BABY_DUCK_RODE, 1);
			st.giveItems(FISHING_SHOT_NG, 500);
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
				htmltext = (player.getLevel() < 20) ? "7334-2.htm" : "7334-0.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case BABENCO:
						htmltext = "7334-3.htm";
						break;
					
					case BATHIS:
						if (cond == 1)
							htmltext = "7332-0.htm";
						else if (cond == 2)
							htmltext = "7332-2a.htm";
						else if (cond == 3)
							htmltext = "7332-2.htm";
						else if (cond == 4)
							htmltext = "7332-3a.htm";
						else if (cond == 5)
							htmltext = "7332-4.htm";
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
		final int npcId = npc.getNpcId();
		
		L2PcInstance member = getRandomPartyMember(killer, npc, "2");
		if (member != null && npcId != ARANEID)
		{
			final QuestState st = member.getQuestState(getName());
			final int[] list = FIRST_DP.get(npcId);
			
			if (st.dropItems(list[0], 1, 100, 500000) && st.getQuestItemsCount(list[1]) == 100)
				st.set("cond", "3");
		}
		else
		{
			member = getRandomPartyMember(killer, npc, "4");
			if (member != null && npcId != MAILLE_LIZARDMAN)
			{
				final QuestState st = member.getQuestState(getName());
				final int[] list = SECOND_DP.get(npcId);
				
				if (st.dropItems(list[0], 1, 30, list[2]) && st.getQuestItemsCount(list[1]) == 30)
					st.set("cond", "5");
			}
		}
		
		return null;
	}
}