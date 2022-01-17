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
package village_master.ElvenHumanMystics2;

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
public class ElvenHumanMystics2 extends Quest
{
	// JUREK, ARKENIAS, VALLERIA, SCRAIDE, DRIKIYAN
	private static final int[] NPC_LIST = new int[]
	{
		7115,
		7174,
		7176,
		7694,
		7854
	};
	
	// Quest items
	private static final int MARK_OF_SCHOLAR = 2674;
	private static final int MARK_OF_TRUST = 2734;
	private static final int MARK_OF_MAGUS = 2840;
	private static final int MARK_OF_LIFE = 3140;
	private static final int MARK_OF_WITCHCRAFT = 3307;
	private static final int MARK_OF_SUMMONER = 3336;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("EW", new ClassChangeData(ClassId.spellsinger, ClassId.elvenWizard, Race.ELF, new String[]
		{
			"18",
			"19",
			"20",
			"21"
		}, new int[]
		{
			MARK_OF_SCHOLAR,
			MARK_OF_LIFE,
			MARK_OF_MAGUS
		}));
		CLASSES.put("ES", new ClassChangeData(ClassId.elementalSummoner, ClassId.elvenWizard, Race.ELF, new String[]
		{
			"22",
			"23",
			"24",
			"25"
		}, new int[]
		{
			MARK_OF_SCHOLAR,
			MARK_OF_LIFE,
			MARK_OF_SUMMONER
		}));
		CLASSES.put("HS", new ClassChangeData(ClassId.sorceror, ClassId.wizard, Race.HUMAN, new String[]
		{
			"26",
			"27",
			"28",
			"29"
		}, new int[]
		{
			MARK_OF_SCHOLAR,
			MARK_OF_TRUST,
			MARK_OF_MAGUS
		}));
		CLASSES.put("HN", new ClassChangeData(ClassId.necromancer, ClassId.wizard, Race.HUMAN, new String[]
		{
			"30",
			"31",
			"32",
			"33"
		}, new int[]
		{
			MARK_OF_SCHOLAR,
			MARK_OF_TRUST,
			MARK_OF_WITCHCRAFT
		}));
		CLASSES.put("HW", new ClassChangeData(ClassId.warlock, ClassId.wizard, Race.HUMAN, new String[]
		{
			"34",
			"35",
			"36",
			"37"
		}, new int[]
		{
			MARK_OF_SCHOLAR,
			MARK_OF_TRUST,
			MARK_OF_SUMMONER
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new ElvenHumanMystics2();
	}
	
	public ElvenHumanMystics2()
	{
		super(-1, ElvenHumanMystics2.class.getSimpleName(), "village_master");
		
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
			htmlText = "mystic" + suffix + ".htm";
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
			if (!player.getClassId().isMage())
			{
				htmlText = "mystic-40.htm";
			}
			else if (player.getClassId().level() == 0)
			{
				htmlText = "mystic-38.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = "mystic-39.htm";
			}
			else if (player.getClassId() == ClassId.elvenWizard)
			{
				htmlText = "mystic-01.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.wizard)
			{
				htmlText = "mystic-08.htm";
				return htmlText;
			}
			else
			{
				htmlText = "mystic-40.htm";
			}
		}
		else
		{
			htmlText = "mystic-40.htm"; // Other races
		}
		st.exitQuest(true);
		return htmlText;
	}
}