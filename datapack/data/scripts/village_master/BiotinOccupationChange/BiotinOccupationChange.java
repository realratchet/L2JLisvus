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
package village_master.BiotinOccupationChange;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * Created by DraX on 2005.08.08
 * Updated by DnR on 08.02.2020
 */
public class BiotinOccupationChange extends Quest
{
	private static final int HIGH_PRIEST_BIOTIN = 7031;
	
	public static void main(String[] args)
	{
		// Quest class
		new BiotinOccupationChange();
	}
	
	public BiotinOccupationChange()
	{
		super(-1, BiotinOccupationChange.class.getSimpleName(), "village_master");
		
		addStartNpc(HIGH_PRIEST_BIOTIN);
		addTalkId(HIGH_PRIEST_BIOTIN);
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmlText = getNoQuestMsg();
		for (int i = 1; i <= 5; i++)
		{
			if (event.equalsIgnoreCase("7031-0" + i + ".htm"))
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
		if (npcId == HIGH_PRIEST_BIOTIN)
		{
			if (player.getRace() == Race.HUMAN)
			{
				switch (player.getClassId())
				{
					case mage:
						htmlText = "7031-01.htm";
						break;
					case wizard:
					case cleric:
						htmlText = "7031-06.htm";
						break;
					case sorceror:
					case necromancer:
					case warlock:
					case bishop:
					case prophet:
						htmlText = "7031-07.htm";
						break;
					default: // All other races and classes must be out
						htmlText = "7031-08.htm";
						st.exitQuest(true);
						break;
				}
			}
			else // All other races must be out
			{
				htmlText = "7031-08.htm";
				st.exitQuest(true);
			}
		}
		return htmlText;
	}
}