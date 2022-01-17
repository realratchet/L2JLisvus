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
package village_master.ElvenHumanFighters2;

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
public class ElvenHumanFighters2 extends Quest
{
	// HANNAVALT, BLACKBIRD, SIRIA, SEDRICK, MARCUS
	private static final int[] NPC_LIST = new int[]
	{
		7109,
		7187,
		7689,
		7849,
		7900
	};
	
	// Quest items
	private static final int MARK_OF_CHALLENGER = 2627;
	private static final int MARK_OF_DUTY = 2633;
	private static final int MARK_OF_SEEKER = 2673;
	private static final int MARK_OF_TRUST = 2734;
	private static final int MARK_OF_DUELIST = 2762;
	private static final int MARK_OF_SEARCHER = 2809;
	private static final int MARK_OF_HEALER = 2820;
	private static final int MARK_OF_LIFE = 3140;
	private static final int MARK_OF_CHAMPION = 3276;
	private static final int MARK_OF_SAGITTARIUS = 3293;
	private static final int MARK_OF_WITCHCRAFT = 3307;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("TK", new ClassChangeData(ClassId.templeKnight, ClassId.elvenKnight, Race.ELF, new String[]
		{
			"36",
			"37",
			"38",
			"39"
		}, new int[]
		{
			MARK_OF_DUTY,
			MARK_OF_LIFE,
			MARK_OF_HEALER
		}));
		CLASSES.put("SS", new ClassChangeData(ClassId.swordSinger, ClassId.elvenKnight, Race.ELF, new String[]
		{
			"40",
			"41",
			"42",
			"43"
		}, new int[]
		{
			MARK_OF_CHALLENGER,
			MARK_OF_LIFE,
			MARK_OF_DUELIST
		}));
		CLASSES.put("PL", new ClassChangeData(ClassId.paladin, ClassId.knight, Race.HUMAN, new String[]
		{
			"44",
			"45",
			"46",
			"47"
		}, new int[]
		{
			MARK_OF_DUTY,
			MARK_OF_TRUST,
			MARK_OF_HEALER
		}));
		CLASSES.put("DA", new ClassChangeData(ClassId.darkAvenger, ClassId.knight, Race.HUMAN, new String[]
		{
			"48",
			"49",
			"50",
			"51"
		}, new int[]
		{
			MARK_OF_DUTY,
			MARK_OF_TRUST,
			MARK_OF_WITCHCRAFT
		}));
		CLASSES.put("TH", new ClassChangeData(ClassId.treasureHunter, ClassId.rogue, Race.HUMAN, new String[]
		{
			"52",
			"53",
			"54",
			"55"
		}, new int[]
		{
			MARK_OF_SEEKER,
			MARK_OF_TRUST,
			MARK_OF_SEARCHER
		}));
		CLASSES.put("HE", new ClassChangeData(ClassId.hawkeye, ClassId.rogue, Race.HUMAN, new String[]
		{
			"56",
			"57",
			"58",
			"59"
		}, new int[]
		{
			MARK_OF_SEEKER,
			MARK_OF_TRUST,
			MARK_OF_SAGITTARIUS
		}));
		CLASSES.put("PW", new ClassChangeData(ClassId.plainsWalker, ClassId.elvenScout, Race.ELF, new String[]
		{
			"60",
			"61",
			"62",
			"63"
		}, new int[]
		{
			MARK_OF_SEEKER,
			MARK_OF_LIFE,
			MARK_OF_SEARCHER
		}));
		CLASSES.put("SR", new ClassChangeData(ClassId.silverRanger, ClassId.elvenScout, Race.ELF, new String[]
		{
			"64",
			"65",
			"66",
			"67"
		}, new int[]
		{
			MARK_OF_SEEKER,
			MARK_OF_LIFE,
			MARK_OF_SAGITTARIUS
		}));
		CLASSES.put("GL", new ClassChangeData(ClassId.gladiator, ClassId.warrior, Race.HUMAN, new String[]
		{
			"68",
			"69",
			"70",
			"71"
		}, new int[]
		{
			MARK_OF_CHALLENGER,
			MARK_OF_TRUST,
			MARK_OF_DUELIST
		}));
		CLASSES.put("WL", new ClassChangeData(ClassId.warlord, ClassId.warrior, Race.HUMAN, new String[]
		{
			"72",
			"73",
			"74",
			"75"
		}, new int[]
		{
			MARK_OF_CHALLENGER,
			MARK_OF_TRUST,
			MARK_OF_CHAMPION
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new ElvenHumanFighters2();
	}
	
	public ElvenHumanFighters2()
	{
		super(-1, ElvenHumanFighters2.class.getSimpleName(), "village_master");
		
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
			htmlText = "fighter" + suffix + ".htm";
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
		
		if (player.getRace() == Race.HUMAN || player.getRace() == Race.ELF)
		{
			if (player.getClassId().level() == 0)
			{
				htmlText = "fighter-76.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = "fighter-77.htm";
			}
			else if (player.getClassId() == ClassId.elvenKnight)
			{
				htmlText = "fighter-01.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.knight)
			{
				htmlText = "fighter-08.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.rogue)
			{
				htmlText = "fighter-15.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.elvenScout)
			{
				htmlText = "fighter-22.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.warrior)
			{
				htmlText = "fighter-29.htm";
				return htmlText;
			}
			else
			{
				htmlText = "fighter-78.htm";
			}
		}
		else
		{
			htmlText = "fighter-78.htm"; // Other races
		}
		st.exitQuest(true);
		return htmlText;
	}
}