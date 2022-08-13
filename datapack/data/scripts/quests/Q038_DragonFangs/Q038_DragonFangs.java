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
package quests.Q038_DragonFangs;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.util.Rnd;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q038_DragonFangs extends Quest
{
	// Items
	private static final int FEATHER_ORNAMENT = 7173;
	private static final int TOOTH_OF_TOTEM = 7174;
	private static final int TOOTH_OF_DRAGON = 7175;
	private static final int LETTER_OF_IRIS = 7176;
	private static final int LETTER_OF_ROHMER = 7177;
	
	// NPCs
	private static final int LUIS = 7386;
	private static final int IRIS = 7034;
	private static final int ROHMER = 7344;
	
	// Reward { item, adena }
	private static final int REWARD[][] =
	{
		{
			45,
			5200
		},
		{
			627,
			1500
		},
		{
			1123,
			3200
		},
		{
			605,
			3200
		}
	};
	
	// Droplist
	private static final Map<Integer, int[]> DROPLIST = new HashMap<>();
	{
		DROPLIST.put(1100, new int[]
		{
			1,
			FEATHER_ORNAMENT,
			100,
			1000000
		});
		DROPLIST.put(357, new int[]
		{
			1,
			FEATHER_ORNAMENT,
			100,
			1000000
		});
		DROPLIST.put(1101, new int[]
		{
			6,
			TOOTH_OF_DRAGON,
			50,
			500000
		});
		DROPLIST.put(356, new int[]
		{
			6,
			TOOTH_OF_DRAGON,
			50,
			500000
		});
	}

	public static void main(String[] args)
	{
		new Q038_DragonFangs();
	}
	
	public Q038_DragonFangs()
	{
		super(38, Q038_DragonFangs.class.getSimpleName(), "Dragon Fangs");
		
		setItemsIds(FEATHER_ORNAMENT, TOOTH_OF_TOTEM, TOOTH_OF_DRAGON, LETTER_OF_IRIS, LETTER_OF_ROHMER);
		
		addStartNpc(LUIS);
		addTalkId(LUIS, IRIS, ROHMER);
		
		for (int mob : DROPLIST.keySet())
			addKillId(mob);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7386-02.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7386-04.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(FEATHER_ORNAMENT, 100);
			st.giveItems(TOOTH_OF_TOTEM, 1);
		}
		else if (event.equalsIgnoreCase("7034-02a.htm"))
		{
			if (st.hasQuestItems(TOOTH_OF_TOTEM))
			{
				htmltext = "7034-02.htm";
				st.set("cond", "4");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(TOOTH_OF_TOTEM, 1);
				st.giveItems(LETTER_OF_IRIS, 1);
			}
		}
		else if (event.equalsIgnoreCase("7344-02a.htm"))
		{
			if (st.hasQuestItems(LETTER_OF_IRIS))
			{
				htmltext = "7344-02.htm";
				st.set("cond", "5");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(LETTER_OF_IRIS, 1);
				st.giveItems(LETTER_OF_ROHMER, 1);
			}
		}
		else if (event.equalsIgnoreCase("7034-04a.htm"))
		{
			if (st.hasQuestItems(LETTER_OF_ROHMER))
			{
				htmltext = "7034-04.htm";
				st.set("cond", "6");
				st.playSound(QuestState.SOUND_MIDDLE);
				st.takeItems(LETTER_OF_ROHMER, 1);
			}
		}
		else if (event.equalsIgnoreCase("7034-06a.htm"))
		{
			if (st.getQuestItemsCount(TOOTH_OF_DRAGON) >= 50)
			{
				int position = Rnd.get(REWARD.length);
				
				htmltext = "7034-06.htm";
				st.takeItems(TOOTH_OF_DRAGON, 50);
				st.giveItems(REWARD[position][0], 1);
				st.rewardItems(57, REWARD[position][1]);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
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
				htmltext = (player.getLevel() < 19) ? "7386-01a.htm" : "7386-01.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case LUIS:
						if (cond == 1)
							htmltext = "7386-02a.htm";
						else if (cond == 2)
							htmltext = "7386-03.htm";
						else if (cond > 2)
							htmltext = "7386-03a.htm";
						break;
					
					case IRIS:
						if (cond == 3)
							htmltext = "7034-01.htm";
						else if (cond == 4)
							htmltext = "7034-02b.htm";
						else if (cond == 5)
							htmltext = "7034-03.htm";
						else if (cond == 6)
							htmltext = "7034-05a.htm";
						else if (cond == 7)
							htmltext = "7034-05.htm";
						break;
					
					case ROHMER:
						if (cond == 4)
							htmltext = "7344-01.htm";
						else if (cond > 4)
							htmltext = "7344-03.htm";
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
		final QuestState st = checkPlayerState(killer, npc, State.STARTED);
		if (st == null)
			return null;
		
		final int droplist[] = DROPLIST.get(npc.getNpcId());
		
		if (st.getInt("cond") == droplist[0] && st.dropItems(droplist[1], 1, droplist[2], droplist[3]))
			st.set("cond", String.valueOf(droplist[0] + 1));
		
		return null;
	}
}