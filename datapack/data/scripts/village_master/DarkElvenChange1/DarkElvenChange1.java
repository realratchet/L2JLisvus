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
package village_master.DarkElvenChange1;

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
public class DarkElvenChange1 extends Quest
{
	// XENOS, TOBIAS, TRONIX, DEVON
	private static final int[] NPC_LIST = new int[]
	{
		7290,
		7297,
		7462
	};
	
	// Quest items
	private static final int GAZE_OF_ABYSS = 1244;
	private static final int IRON_HEART = 1252;
	private static final int JEWEL_OF_DARKNESS = 1261;
	private static final int ORB_OF_ABYSS = 1270;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("PK", new ClassChangeData(ClassId.palusKnight, ClassId.darkFighter, Race.DARK_ELF, new String[]
		{
			"15",
			"16",
			"17",
			"18"
		}, new int[]
		{
			GAZE_OF_ABYSS
		}));
		CLASSES.put("AS", new ClassChangeData(ClassId.assassin, ClassId.darkFighter, Race.DARK_ELF, new String[]
		{
			"19",
			"20",
			"21",
			"22"
		}, new int[]
		{
			IRON_HEART
		}));
		CLASSES.put("DW", new ClassChangeData(ClassId.darkWizard, ClassId.darkMage, Race.DARK_ELF, new String[]
		{
			"23",
			"24",
			"25",
			"26"
		}, new int[]
		{
			JEWEL_OF_DARKNESS
		}));
		CLASSES.put("SO", new ClassChangeData(ClassId.shillienOracle, ClassId.darkMage, Race.DARK_ELF, new String[]
		{
			"27",
			"28",
			"29",
			"30"
		}, new int[]
		{
			ORB_OF_ABYSS
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new DarkElvenChange1();
	}
	
	public DarkElvenChange1()
	{
		super(-1, DarkElvenChange1.class.getSimpleName(), "village_master");
		
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
		if (player.getRace() == Race.DARK_ELF)
		{
			if (player.getClassId().level() == 1)
			{
				htmlText = npcId + "-32.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = npcId + "-31.htm";
			}
			else if (player.getClassId() == ClassId.darkFighter)
			{
				htmlText = npcId + "-01.htm";
				return htmlText; // Avoid exiting quest
			}
			else if (player.getClassId() == ClassId.darkMage)
			{
				htmlText = npcId + "-08.htm";
				return htmlText; // Avoid exiting quest
			}
			else
			{
				htmlText = npcId + "-33.htm";
			}
		}
		else
		{
			htmlText = npcId + "-33.htm"; // Other races
		}
		st.exitQuest(true);
		return htmlText;
	}
}