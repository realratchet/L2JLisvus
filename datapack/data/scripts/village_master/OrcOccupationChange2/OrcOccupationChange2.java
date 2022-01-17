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
package village_master.OrcOccupationChange2;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import village_master.ClassChangeData;

/**
 * Created by DrLecter, based on DraX' scripts.
 * Updated by DnR.
 */
public class OrcOccupationChange2 extends Quest
{
	// PENATUS, KARIA, GARVARENTZ, LADANZA, TUSHKU, AKLAN, LAMBAC
	private static final int[] NPC_LIST = new int[]
	{
		7513,
		7681,
		7704,
		7865,
		7913,
		8288,
		8326
	};
	
	// Quest items
	private static final int MARK_OF_CHALLENGER = 2627;
	private static final int MARK_OF_PILGRIM = 2721;
	private static final int MARK_OF_DUELIST = 2762;
	private static final int MARK_OF_WARSPIRIT = 2879;
	private static final int MARK_OF_GLORY = 3203;
	private static final int MARK_OF_CHAMPION = 3276;
	private static final int MARK_OF_LORD = 3390;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("TY", new ClassChangeData(ClassId.tyrant, ClassId.orcMonk, Race.ORC, new String[]
		{
			"16",
			"17",
			"18",
			"19"
		}, new int[]
		{
			MARK_OF_CHALLENGER,
			MARK_OF_GLORY,
			MARK_OF_DUELIST
		}));
		CLASSES.put("DE", new ClassChangeData(ClassId.destroyer, ClassId.orcRaider, Race.ORC, new String[]
		{
			"20",
			"21",
			"22",
			"23"
		}, new int[]
		{
			MARK_OF_CHALLENGER,
			MARK_OF_GLORY,
			MARK_OF_CHAMPION
		}));
		CLASSES.put("OL", new ClassChangeData(ClassId.overlord, ClassId.orcShaman, Race.ORC, new String[]
		{
			"24",
			"25",
			"26",
			"27"
		}, new int[]
		{
			MARK_OF_PILGRIM,
			MARK_OF_GLORY,
			MARK_OF_LORD
		}));
		CLASSES.put("WC", new ClassChangeData(ClassId.warcryer, ClassId.orcShaman, Race.ORC, new String[]
		{
			"28",
			"29",
			"30",
			"31"
		}, new int[]
		{
			MARK_OF_PILGRIM,
			MARK_OF_GLORY,
			MARK_OF_WARSPIRIT
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new OrcOccupationChange2();
	}
	
	public OrcOccupationChange2()
	{
		super(-1, OrcOccupationChange2.class.getSimpleName(), "village_master");
		
		for (int id : NPC_LIST)
		{
			addStartNpc(id);
			addTalkId(id);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmlText;
		}
		
		if (!CLASSES.containsKey(event))
		{
			return event;
		}
		
		ClassChangeData ccd = CLASSES.get(event);
		String[] states = ccd.getStates();
		
		if (player.getRace() == ccd.getRequiredRace() && player.getClassId() == ccd.getRequiredClass())
		{
			String suffix = "-";
			
			// Check if player has all required items
			boolean hasRequiredItems = true;
			for (int itemId : ccd.getRequiredItems())
			{
				if (st.getQuestItemsCount(itemId) <= 0)
				{
					hasRequiredItems = false;
					break;
				}
			}
			
			// Player level is too low
			if (player.getLevel() < 40)
			{
				if (hasRequiredItems)
				{
					suffix += states[1];
				}
				else
				{
					suffix += states[0];
				}
			}
			else
			{
				// Everything is in place, proceed to changing class
				if (hasRequiredItems)
				{
					suffix += states[3];
					ccd.changeClass(st, player);
				}
				else
				{
					suffix += states[2];
				}
			}
			htmlText = "orc" + suffix + ".htm";
		}
		st.exitQuest(true);
		return htmlText;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmlText;
		}
		
		if (player.isSubClassActive())
		{
			st.exitQuest(true);
			return htmlText;
		}
		
		if (player.getRace() == Race.ORC)
		{
			if (player.getClassId().level() == 0)
			{
				htmlText = "orc-33.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = "orc-32.htm";
			}
			else if (player.getClassId() == ClassId.orcMonk)
			{
				htmlText = "orc-01.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.orcRaider)
			{
				htmlText = "orc-05.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.orcShaman)
			{
				htmlText = "orc-09.htm";
				return htmlText;
			}
			else
			{
				htmlText = "orc-34.htm";
			}
		}
		else
		{
			htmlText = "orc-34.htm"; // Other races
		}
		st.exitQuest(true);
		return htmlText;
	}
}