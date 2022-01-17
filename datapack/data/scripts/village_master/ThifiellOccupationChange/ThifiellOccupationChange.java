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
package village_master.ThifiellOccupationChange;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * Created by DraX on 2005.08.08
 * Updated by DnR on 2020.02.08
 */
public class ThifiellOccupationChange extends Quest
{
	private static final int TETRARCH_THIFIELL = 7358;
	
	public static void main(String[] args)
	{
		// Quest class
		new ThifiellOccupationChange();
	}
	
	public ThifiellOccupationChange()
	{
		super(-1, ThifiellOccupationChange.class.getSimpleName(), "village_master");
		
		addStartNpc(TETRARCH_THIFIELL);
		addTalkId(TETRARCH_THIFIELL);
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmlText = getNoQuestMsg();
		for (int i = 1; i <= 10; i++)
		{
			if (event.equalsIgnoreCase("7358-0" + i + ".htm"))
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
		
		// Dark Elves got accepted
		if (npcId == TETRARCH_THIFIELL)
		{
			if (player.getRace() == Race.DARK_ELF)
			{
				switch (player.getClassId())
				{
					case darkFighter:
						htmlText = "7358-01.htm";
						break;
					case darkMage:
						htmlText = "7358-02.htm";
						break;
					case darkWizard:
					case shillienOracle:
					case palusKnight:
					case assassin:
						st.exitQuest(true);
						htmlText = "7358-12.htm";
						break;
					default: // All other classes must be out
						st.exitQuest(true);
						htmlText = "7358-13.htm";
						break;
				}
			}
			else // All other races must be out
			{
				st.exitQuest(true);
				htmlText = "7358-11.htm";
			}
		}
		return htmlText;
	}
}