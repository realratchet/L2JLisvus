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
package village_master.KakaiOccupationChange;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * Created by DraX on 2005.08.08 modified by Ariakas on 2005.09.19
 * Updated by DnR on 2020.02.10
 */
public class KakaiOccupationChange extends Quest
{
	private static final int KAKAI_LORD_OF_FLAME = 7565;
	
	public static void main(String[] args)
	{
		// Quest class
		new KakaiOccupationChange();
	}
	
	public KakaiOccupationChange()
	{
		super(-1, KakaiOccupationChange.class.getSimpleName(), "village_master");
		
		addStartNpc(KAKAI_LORD_OF_FLAME);
		addTalkId(KAKAI_LORD_OF_FLAME);
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmlText = getNoQuestMsg();
		for (int i = 1; i <= 8; i++)
		{
			if (event.equalsIgnoreCase("7565-0" + i + ".htm"))
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
		// Orcs got accepted
		if (npcId == KAKAI_LORD_OF_FLAME)
		{
			if (player.getRace() == Race.ORC)
			{
				switch (player.getClassId())
				{
					case orcFighter:
						htmlText = "7565-01.htm";
						break;
					case orcMonk:
					case orcRaider:
					case orcShaman:
						st.exitQuest(true);
						htmlText = "7565-09.htm";
						break;
					case destroyer:
					case overlord:
					case tyrant:
					case warcryer:
						st.exitQuest(true);
						htmlText = "7565-10.htm";
						break;
					case orcMage:
						st.exitQuest(true);
						htmlText = "7565-06.htm";
						break;
				}
			}
			else // All other races must be out
			{
				st.exitQuest(true);
				htmlText = "7565-11.htm";
			}
		}
		return htmlText;
	}
}