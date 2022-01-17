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
package village_master.DwarvenOccupationChange;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.util.Util;
import village_master.ClassChangeData;

/**
 * Created by DrLecter, based on DraX' and Ariakas work.
 * Updated by DnR.
 */
public class DwarvenOccupationChange extends Quest
{
	// GESTO, CROOP, BRAXT, KLUMP, NATOOLS, MONA, DONALD
	private static final int[] BH_NPC_LIST = new int[] {7511, 7676, 7685, 7845, 7894, 8269, 8314};
	
	// KUSTO, FLUTTER, VERGARA, FERRIS, ROMAN, NOEL, LOMBERT
	private static final int[] WS_NPC_LIST = new int[] {7512, 7677, 7687, 7847, 7897, 8272, 8317};
	
	// RIKADIO, RANSPO, MOKE
	private static final int[] SCAV_NPC_LIST = new int[] {7503, 7594, 7498};
	
	// MENDIO, OPIX, TAPOY
	private static final int[] ARTI_NPC_LIST = new int[] {7504, 7595, 7499};
	
	// First class items
	private static final int FINAL_PASS_CERTIFICATE = 1635;
	private static final int RING_OF_RAVEN = 1642;
	// SEARCHER, GUILDSMAN, PROSPERITY
	private static final int[] BH_MARKS = new int[] {2809, 3119, 3238};
	// MAESTRO, GUILDSMAN, PROSPERITY
	private static final int[] WS_MARKS = new int[] {2867, 3119, 3238};
	
	private static final int[] UNIQUE_DIALOGS = new int[] {7594,7595,7498,7499};
	
	private static final String[] DENIAL_STATES = new String[]
	{
		"05",
		"06",
		"07",
		"08"
	};
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("BH", new ClassChangeData(ClassId.bountyHunter, ClassId.scavenger, Race.DWARF, DENIAL_STATES, BH_MARKS));
		CLASSES.put("WS", new ClassChangeData(ClassId.warsmith, ClassId.artisan, Race.DWARF, DENIAL_STATES, WS_MARKS));
		CLASSES.put("SC", new ClassChangeData(ClassId.scavenger, ClassId.dwarvenFighter, Race.DWARF, DENIAL_STATES, new int[]
		{
			RING_OF_RAVEN
		}));
		CLASSES.put("AR", new ClassChangeData(ClassId.artisan, ClassId.dwarvenFighter, Race.DWARF, DENIAL_STATES, new int[]
		{
			FINAL_PASS_CERTIFICATE
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new DwarvenOccupationChange();
	}
	
	public DwarvenOccupationChange()
	{
		super(-1, DwarvenOccupationChange.class.getSimpleName(), "village_master");
		
		for (int id : BH_NPC_LIST)
		{
			addStartNpc(id);
			addTalkId(id);
		}
		for (int id : WS_NPC_LIST)
		{
			addStartNpc(id);
			addTalkId(id);
		}
		for (int id : SCAV_NPC_LIST)
		{
			addStartNpc(id);
			addTalkId(id);
		}
		for (int id : ARTI_NPC_LIST)
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
			String prefix = (Util.contains(UNIQUE_DIALOGS, npcId) ? npcId : event.toLowerCase()) + "-";
			
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
			int minLevel = ccd.getRequiredClass().level() == 0 ? 20 : 40;
			if (player.getLevel() < minLevel)
			{
				if (hasRequiredItems)
				{
					prefix += states[1];
				}
				else
				{
					prefix += states[0];
				}
			}
			else
			{
				// Everything is in place, proceed to changing class
				if (hasRequiredItems)
				{
					prefix += states[3];
					ccd.changeClass(st, player);
				}
				else
				{
					prefix += states[2];
				}
			}
			
			htmlText = prefix + ".htm";
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
		String key = null;
		if (Util.contains(BH_NPC_LIST, npcId))
		{
			key = "BH";
		}
		else if (Util.contains(WS_NPC_LIST, npcId))
		{
			key = "WS";
		}
		else if (Util.contains(SCAV_NPC_LIST, npcId))
		{
			key = "SC";
		}
		else if (Util.contains(ARTI_NPC_LIST, npcId))
		{
			key = "AR";
		}
		
		// This should never happen
		if (key == null)
		{
			st.exitQuest(true);
			return htmlText;
		}
		
		String prefix = (Util.contains(UNIQUE_DIALOGS, npcId) ? String.valueOf(npcId) : key.toLowerCase()) + "-";
		
		ClassChangeData ccd = CLASSES.get(key);
		if (ccd != null && player.getRace() == ccd.getRequiredRace())
		{
			if (player.getClassId() == ccd.getRequiredClass())
			{
				htmlText = prefix + "01.htm";
			}
			else if (ccd.getNewClass().level() == 1)
			{
				st.exitQuest(true);
				if (player.getClassId().level() <= ccd.getNewClass().level())
				{
					htmlText = prefix + "09.htm";
				}
				else
				{
					htmlText = prefix + "10.htm";
				}
			}
			else if (ccd.getNewClass().level() == 2)
			{
				st.exitQuest(true);
				if (player.getClassId().level() < ccd.getNewClass().level())
				{
					htmlText = prefix + "09.htm";
				}
				else
				{
					htmlText = prefix + "10.htm";
				}
			}
			else
			{
				st.exitQuest(true);
				htmlText = prefix + "11.htm";
			}
		}
		else
		{
			st.exitQuest(true);
			htmlText = prefix + "11.htm";
		}
		
		return htmlText;
	}
}