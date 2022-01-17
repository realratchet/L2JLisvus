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
package village_master.ElvenHumanMystics1;

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
public class ElvenHumanMystics1 extends Quest
{
	// SYLVAIN, RAYMOND, LEVIAN
	private static final int[] NPC_LIST = new int[]
	{
		7070,
		7289,
		7037
	};
	
	// Quest items
	private static final int MARK_OF_FAITH = 1201;
	private static final int ETERNITY_DIAMOND = 1230;
	private static final int LEAF_OF_ORACLE = 1235;
	private static final int BEAD_OF_SEASON = 1292;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("EW", new ClassChangeData(ClassId.elvenWizard, ClassId.elvenMage, Race.ELF, new String[]
		{
			"15",
			"16",
			"17",
			"18"
		}, new int[]
		{
			ETERNITY_DIAMOND
		}));
		CLASSES.put("EO", new ClassChangeData(ClassId.oracle, ClassId.elvenMage, Race.ELF, new String[]
		{
			"19",
			"20",
			"21",
			"22"
		}, new int[]
		{
			LEAF_OF_ORACLE
		}));
		CLASSES.put("HW", new ClassChangeData(ClassId.wizard, ClassId.mage, Race.HUMAN, new String[]
		{
			"23",
			"24",
			"25",
			"26"
		}, new int[]
		{
			BEAD_OF_SEASON
		}));
		CLASSES.put("HC", new ClassChangeData(ClassId.cleric, ClassId.mage, Race.HUMAN, new String[]
		{
			"27",
			"28",
			"29",
			"30"
		}, new int[]
		{
			MARK_OF_FAITH
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new ElvenHumanMystics1();
	}
	
	public ElvenHumanMystics1()
	{
		super(-1, ElvenHumanMystics1.class.getSimpleName(), "village_master");
		
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
			if (!player.getClassId().isMage())
			{
				htmlText = npcId + "-33.htm";
			}
			else if (player.getClassId().level() == 1)
			{
				htmlText = npcId + "-31.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = npcId + "-32.htm";
			}
			else if (player.getClassId() == ClassId.elvenMage)
			{
				htmlText = npcId + "-01.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.mage)
			{
				htmlText = npcId + "-08.htm";
				return htmlText;
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