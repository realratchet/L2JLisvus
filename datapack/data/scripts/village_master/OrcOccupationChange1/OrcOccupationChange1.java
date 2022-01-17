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
package village_master.OrcOccupationChange1;

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
public class OrcOccupationChange1 extends Quest
{
	// OSBORN, DRIKUS, CASTOR
	private static final int[] NPC_LIST = new int[]
	{
		7500,
		7505,
		7508
	};
	
	// Quest items
	private static final int MARK_OF_RAIDER = 1592;
	private static final int KHAVATARI_TOTEM = 1615;
	private static final int MASK_OF_MEDIUM = 1631;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("OR", new ClassChangeData(ClassId.orcRaider, ClassId.orcFighter, Race.ORC, new String[]
		{
			"09",
			"10",
			"11",
			"12"
		}, new int[]
		{
			MARK_OF_RAIDER
		}));
		CLASSES.put("OM", new ClassChangeData(ClassId.orcMonk, ClassId.orcFighter, Race.ORC, new String[]
		{
			"13",
			"14",
			"15",
			"16"
		}, new int[]
		{
			KHAVATARI_TOTEM
		}));
		CLASSES.put("OS", new ClassChangeData(ClassId.orcShaman, ClassId.orcMage, Race.ORC, new String[]
		{
			"17",
			"18",
			"19",
			"20"
		}, new int[]
		{
			MASK_OF_MEDIUM
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new OrcOccupationChange1();
	}
	
	public OrcOccupationChange1()
	{
		super(-1, OrcOccupationChange1.class.getSimpleName(), "village_master");
		
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
		if (player.getRace() == Race.ORC)
		{
			if (player.getClassId().level() == 1)
			{
				htmlText = npcId + "-21.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = npcId + "-22.htm";
			}
			else if (player.getClassId() == ClassId.orcFighter)
			{
				htmlText = npcId + "-01.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.orcMage)
			{
				htmlText = npcId + "-06.htm";
				return htmlText;
			}
			else
			{
				htmlText = npcId + "-23.htm";
			}
		}
		else
		{
			htmlText = npcId + "-23.htm"; // Other races
		}
		st.exitQuest(true);
		return htmlText;
	}
}