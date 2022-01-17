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
package village_master.BronkOccupationChange;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * Created by DraX on 2005.08.08 modified by Ariakas on 2005.09.19
 * Updated by DnR on 2020.02.10
 */
public class BronkOccupationChange extends Quest
{
	private static final int HEAD_BLACKSMITH_BRONK = 7525;
	
	public static void main(String[] args)
	{
		// Quest class
		new BronkOccupationChange();
	}
	
	public BronkOccupationChange()
	{
		super(-1, BronkOccupationChange.class.getSimpleName(), "village_master");
		
		addStartNpc(HEAD_BLACKSMITH_BRONK);
		addTalkId(HEAD_BLACKSMITH_BRONK);
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmlText = getNoQuestMsg();
		for (int i = 1; i <= 4; i++)
		{
			if (event.equalsIgnoreCase("7525-0" + i + ".htm"))
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
		// Dwarves got accepted
		if (npcId == HEAD_BLACKSMITH_BRONK)
		{
			if (player.getRace() == Race.DWARF)
			{
				switch (player.getClassId())
				{
					case dwarvenFighter:
						htmlText = "7525-01.htm";
						break;
					case artisan:
						st.exitQuest(true);
						htmlText = "7525-05.htm";
						break;
					case warsmith:
						st.exitQuest(true);
						htmlText = "7525-06.htm";
						break;
					case scavenger:
					case bountyHunter:
						st.exitQuest(true);
						htmlText = "7525-07.htm";
						break;
				}
			}
			else // All other races must be out
			{
				st.exitQuest(true);
				htmlText = "7525-07.htm";
			}
		}
		return htmlText;
	}
}