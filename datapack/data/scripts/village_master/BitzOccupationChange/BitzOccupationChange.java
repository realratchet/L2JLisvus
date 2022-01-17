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
package village_master.BitzOccupationChange;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * Created by DraX on 2005.08.08
 * Updated by ElgarL on 28.09.2005
 * Updated by DnR on 07.02.2020
 */
public class BitzOccupationChange extends Quest
{
	private static final int GRAND_MASTER_BITZ = 7026;
	
	public static void main(String[] args)
	{
		// Quest class
		new BitzOccupationChange();
	}
	
	public BitzOccupationChange()
	{
		super(-1, BitzOccupationChange.class.getSimpleName(), "village_master");
		
		addStartNpc(GRAND_MASTER_BITZ);
		addTalkId(GRAND_MASTER_BITZ);
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmlText = getNoQuestMsg();
		for (int i = 1; i <= 7; i++)
		{
			if (event.equalsIgnoreCase("7026-0" + i + ".htm"))
			{
				return event;
			}
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
		
		int npcId = npc.getNpcId();
		if (npcId == GRAND_MASTER_BITZ)
		{
			if (player.getRace() == Race.HUMAN)
			{
				switch (player.getClassId())
				{
					case fighter:
						htmlText = "7026-01.htm";
						break;
					case warrior:
					case knight:
					case rogue:
						htmlText = "7026-08.htm";
						break;
					case gladiator:
					case warlord:
					case paladin:
					case darkAvenger:
					case treasureHunter:
					case hawkeye:
					case duelist:
					case dreadnought:
					case phoenixKnight:
					case hellKnight:
					case sagittarius:
					case adventurer:
						htmlText = "7026-09.htm";
						break;
					default: // All other classes must be out
						htmlText = "7026-10.htm";
						st.exitQuest(true);
						break;
				}
			}
			else // All other races must be out
			{
				htmlText = "7026-10.htm";
				st.exitQuest(true);
			}
		}
		return htmlText;
	}
}