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
package village_master.DarkElvenChange2;

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
public class DarkElvenChange2 extends Quest
{
	// INNOCENTIN, BRECSON, MEDOWN, ANGUS, ANDROMEDA, OLTLIN, XAIRAKIN, SAMAEL, VALDIS, TIFAREN
	private static final int[] NPC_LIST = new int[]
	{
		7195,
		7699,
		7474,
		7862,
		7910,
		8285,
		8324,
		8328,
		8331,
		8334
	};
	
	// Quest items
	private static final int MARK_OF_CHALLENGER = 2627;
	private static final int MARK_OF_DUTY = 2633;
	private static final int MARK_OF_SEEKER = 2673;
	private static final int MARK_OF_SCHOLAR = 2674;
	private static final int MARK_OF_PILGRIM = 2721;
	private static final int MARK_OF_DUELIST = 2762;
	private static final int MARK_OF_SEARCHER = 2809;
	private static final int MARK_OF_REFORMER = 2821;
	private static final int MARK_OF_MAGUS = 2840;
	private static final int MARK_OF_FATE = 3172;
	private static final int MARK_OF_SAGITTARIUS = 3293;
	private static final int MARK_OF_WITCHCRAFT = 3307;
	private static final int MARK_OF_SUMMONER = 3336;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("SK", new ClassChangeData(ClassId.shillienKnight, ClassId.palusKnight, Race.DARK_ELF, new String[]
		{
			"26",
			"27",
			"28",
			"29"
		}, new int[]
		{
			MARK_OF_DUTY,
			MARK_OF_FATE,
			MARK_OF_WITCHCRAFT
		}));
		CLASSES.put("BD", new ClassChangeData(ClassId.bladedancer, ClassId.palusKnight, Race.DARK_ELF, new String[]
		{
			"30",
			"31",
			"32",
			"33"
		}, new int[]
		{
			MARK_OF_CHALLENGER,
			MARK_OF_FATE,
			MARK_OF_DUELIST
		}));
		CLASSES.put("SE", new ClassChangeData(ClassId.shillienElder, ClassId.shillienOracle, Race.DARK_ELF, new String[]
		{
			"34",
			"35",
			"36",
			"37"
		}, new int[]
		{
			MARK_OF_PILGRIM,
			MARK_OF_FATE,
			MARK_OF_REFORMER
		}));
		CLASSES.put("AW", new ClassChangeData(ClassId.abyssWalker, ClassId.assassin, Race.DARK_ELF, new String[]
		{
			"38",
			"39",
			"40",
			"41"
		}, new int[]
		{
			MARK_OF_SEEKER,
			MARK_OF_FATE,
			MARK_OF_SEARCHER
		}));
		CLASSES.put("PR", new ClassChangeData(ClassId.phantomRanger, ClassId.assassin, Race.DARK_ELF, new String[]
		{
			"42",
			"43",
			"44",
			"45"
		}, new int[]
		{
			MARK_OF_SEEKER,
			MARK_OF_FATE,
			MARK_OF_SAGITTARIUS
		}));
		CLASSES.put("SH", new ClassChangeData(ClassId.spellhowler, ClassId.darkWizard, Race.DARK_ELF, new String[]
		{
			"46",
			"47",
			"48",
			"49"
		}, new int[]
		{
			MARK_OF_SCHOLAR,
			MARK_OF_FATE,
			MARK_OF_MAGUS
		}));
		CLASSES.put("PS", new ClassChangeData(ClassId.phantomSummoner, ClassId.darkWizard, Race.DARK_ELF, new String[]
		{
			"50",
			"51",
			"52",
			"53"
		}, new int[]
		{
			MARK_OF_SCHOLAR,
			MARK_OF_FATE,
			MARK_OF_SUMMONER
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new DarkElvenChange2();
	}
	
	public DarkElvenChange2()
	{
		super(-1, DarkElvenChange2.class.getSimpleName(), "village_master");
		
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
			
			htmlText = "de" + suffix + ".htm";
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
		
		if (player.getRace() == Race.DARK_ELF)
		{
			if (player.getClassId().level() == 0)
			{
				htmlText = "de-55.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = "de-54.htm";
			}
			else if (player.getClassId() == ClassId.palusKnight)
			{
				htmlText = "de-01.htm";
				return htmlText; // Avoid exiting quest
			}
			else if (player.getClassId() == ClassId.shillienOracle)
			{
				htmlText = "de-08.htm";
				return htmlText; // Avoid exiting quest
			}
			else if (player.getClassId() == ClassId.assassin)
			{
				htmlText = "de-12.htm";
				return htmlText; // Avoid exiting quest
			}
			else if (player.getClassId() == ClassId.darkWizard)
			{
				htmlText = "de-19.htm";
				return htmlText; // Avoid exiting quest
			}
			else
			{
				htmlText = "de-56.htm";
			}
		}
		else
		{
			htmlText = "de-56.htm"; // Other races
		}
		st.exitQuest(true);
		return htmlText;
	}
}