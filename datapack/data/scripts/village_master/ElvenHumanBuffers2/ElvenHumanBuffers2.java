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
package village_master.ElvenHumanBuffers2;

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
public class ElvenHumanBuffers2 extends Quest
{
	// MAXIMILIAN, HOLLINT, ORVEN, SQUILLARI, BERNHARD, SIEGMUND, GREGORY, HALASTER, RAHORAKI
	private static final int[] NPC_LIST = new int[]
	{
		7120,
		7191,
		7857,
		7905,
		8276,
		8321,
		8279,
		8755,
		8336
	};
	
	// Quest items
	private static final int MARK_OF_PILGRIM = 2721;
	private static final int MARK_OF_TRUST = 2734;
	private static final int MARK_OF_HEALER = 2820;
	private static final int MARK_OF_REFORMER = 2821;
	private static final int MARK_OF_LIFE = 3140;
	
	private static Map<String, ClassChangeData> CLASSES = new HashMap<>();
	
	// New class, Required Class, Required Race, States:[Low Level & Missing Items, Low Level, Level Ok & Missing Items, Level Ok], Required Items:[]
	static
	{
		CLASSES.put("EE", new ClassChangeData(ClassId.elder, ClassId.oracle, Race.ELF, new String[]
		{
			"12",
			"13",
			"14",
			"15"
		}, new int[]
		{
			MARK_OF_PILGRIM,
			MARK_OF_LIFE,
			MARK_OF_HEALER
		}));
		CLASSES.put("BI", new ClassChangeData(ClassId.bishop, ClassId.cleric, Race.HUMAN, new String[]
		{
			"16",
			"17",
			"18",
			"19"
		}, new int[]
		{
			MARK_OF_PILGRIM,
			MARK_OF_TRUST,
			MARK_OF_HEALER
		}));
		CLASSES.put("PH", new ClassChangeData(ClassId.prophet, ClassId.cleric, Race.HUMAN, new String[]
		{
			"20",
			"21",
			"22",
			"23"
		}, new int[]
		{
			MARK_OF_PILGRIM,
			MARK_OF_TRUST,
			MARK_OF_REFORMER
		}));
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new ElvenHumanBuffers2();
	}
	
	public ElvenHumanBuffers2()
	{
		super(-1, ElvenHumanBuffers2.class.getSimpleName(), "village_master");
		
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
			htmlText = "buffer" + suffix + ".htm";
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
			if (player.getClassId() == ClassId.oracle)
			{
				htmlText = "buffer-01.htm";
				return htmlText;
			}
			else if (player.getClassId() == ClassId.cleric)
			{
				htmlText = "buffer-05.htm";
				return htmlText;
			}
			else if (player.getClassId().level() == 0)
			{
				htmlText = "buffer-24.htm";
			}
			else if (player.getClassId().level() >= 2)
			{
				htmlText = "buffer-25.htm";
			}
			else
			{
				htmlText = "buffer-26.htm";
			}
		}
		else
		{
			htmlText = "buffer-26.htm"; // Other races
		}
		st.exitQuest(true);
		return htmlText;
	}
}