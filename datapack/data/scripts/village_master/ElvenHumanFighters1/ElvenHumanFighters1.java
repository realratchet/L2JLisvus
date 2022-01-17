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
package village_master.ElvenHumanFighters1;

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
public class ElvenHumanFighters1 extends Quest
{
	// PABRIS, RAINS, RAMOS
	private static final int[] NPC_LIST = new int[]
	{
		7066,
		7288,
		7373
	};
	
	// Quest items
	private static final int MEDALLION_OF_WARRIOR = 1145;
	private static final int SWORD_OF_RITUAL = 1161;
	private static final int BEZIQUES_RECOMMENDATION = 1190;
	private static final int ELVEN_KNIGHT_BROOCH = 1204;
	private static final int REORIA_RECOMMENDATION = 1217;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("EK", new ClassChangeData(ClassId.elvenKnight, ClassId.elvenFighter, Race.ELF, new String[]
		{
			"18",
			"19",
			"20",
			"21"
		}, new int[]
		{
			ELVEN_KNIGHT_BROOCH
		}));
		CLASSES.put("ES", new ClassChangeData(ClassId.elvenScout, ClassId.elvenFighter, Race.ELF, new String[]
		{
			"22",
			"23",
			"24",
			"25"
		}, new int[]
		{
			REORIA_RECOMMENDATION
		}));
		CLASSES.put("HW", new ClassChangeData(ClassId.warrior, ClassId.fighter, Race.HUMAN, new String[]
		{
			"26",
			"27",
			"28",
			"29"
		}, new int[]
		{
			MEDALLION_OF_WARRIOR
		}));
		CLASSES.put("HK", new ClassChangeData(ClassId.knight, ClassId.fighter, Race.HUMAN, new String[]
		{
			"30",
			"31",
			"32",
			"33"
		}, new int[]
		{
			SWORD_OF_RITUAL
		}));
		CLASSES.put("HR", new ClassChangeData(ClassId.rogue, ClassId.fighter, Race.HUMAN, new String[]
		{
			"34",
			"35",
			"36",
			"37"
		}, new int[]
		{
			BEZIQUES_RECOMMENDATION
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new ElvenHumanFighters1();
	}
	
	public ElvenHumanFighters1()
	{
		super(-1, ElvenHumanFighters1.class.getSimpleName(), "village_master");
		
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
		int npcId = npc.getNpcId();
		
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
			if (player.getLevel() < 20)
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
			htmlText = npcId + suffix + ".htm";
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
		
		int npcId = npc.getNpcId();
		if (player.getRace() == Race.HUMAN || player.getRace() == Race.ELF)
		{
			if (player.getClassId().level() == 1)
			{
				htmlText = npcId + "-38.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = npcId + "-39.htm";
			}
			else if (player.getClassId() == ClassId.elvenFighter)
			{
				htmlText = npcId + "-01.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.fighter)
			{
				htmlText = npcId + "-08.htm";
				return htmlText;
			}
			else
			{
				htmlText = npcId + "-40.htm";
			}
		}
		else
		{
			htmlText = npcId + "-40.htm"; // Other races
		}
		st.exitQuest(true);
		return htmlText;
	}
}