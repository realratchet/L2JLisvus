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
package village_master.AsteriosOccupationChange;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * Created by DraX on 2005.08.08
 * Updated by DnR on 08.02.2020
 */
public class AsteriosOccupationChange extends Quest
{
	private static final int HIERARCH_ASTERIOS = 7154;
	
	public static void main(String[] args)
	{
		// Quest class
		new AsteriosOccupationChange();
	}
	
	public AsteriosOccupationChange()
	{
		super(-1, AsteriosOccupationChange.class.getSimpleName(), "village_master");
		
		addStartNpc(HIERARCH_ASTERIOS);
		addTalkId(HIERARCH_ASTERIOS);
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmlText = getNoQuestMsg();
		for (int i = 1; i <= 10; i++)
		{
			if (event.equalsIgnoreCase("7154-0" + i + ".htm"))
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
		if (npcId == HIERARCH_ASTERIOS)
		{
			// Elves got accepted
			if (player.getRace() == Race.ELF)
			{
				switch (player.getClassId())
				{
					case elvenFighter:
						htmlText = "7154-01.htm";
						break;
					case elvenMage:
						htmlText = "7154-02.htm";
						break;
					case elvenWizard:
					case oracle:
					case elvenKnight:
					case elvenScout:
						st.exitQuest(true);
						htmlText = "7154-12.htm";
						break;
					default: // All other classes must be out
						st.exitQuest(true);
						htmlText = "7154-13.htm";
						break;
				}
			}
			else // All other races must be out
			{
				st.exitQuest(true);
				htmlText = "7154-11.htm";
			}
		}
		return htmlText;
	}
}