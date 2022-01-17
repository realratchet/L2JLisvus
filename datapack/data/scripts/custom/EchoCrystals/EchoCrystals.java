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
package custom.EchoCrystals;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.util.StringUtil;

/**
 * @authors Elektra, Plim (java)
 */
public class EchoCrystals extends Quest
{
	private static final int[] NPCS =
	{
		8042,
		8043
	};
	
	private static final int[][] SCORES = 
	{
		{4411, 4410},
		{4412, 4409},
		{4413, 4408},
		{4414, 4420},
		{4415, 4421},
		{4416, 4418},
		{4417, 4419}
	};
	
	private static final int ADENA = 57;
	private static final int COST = 200;
	
	public static void main(String[] args)
    {
        // Quest class
        new EchoCrystals();
    }
	
	public EchoCrystals()
	{
		super(-1, EchoCrystals.class.getSimpleName(), "custom");
		
		for (int id : NPCS)
		{
			addStartNpc(id);
			addTalkId(id);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = "1.htm";
		if (event.equals("0"))
		{
			return htmlText;
		}
		
		QuestState st = player.getQuestState(getName());
		if (st != null && StringUtil.isDigit(event))
		{
			int crystal = Integer.parseInt(event);
			for (int[] scoreData : SCORES)
			{
				if (crystal == scoreData[0])
				{
					int score = scoreData[1];
					if (st.getQuestItemsCount(score) == 0)
						htmlText = "You do not have enough items.";
					else if (st.getQuestItemsCount(ADENA) < COST)
						htmlText = "Not enough adena.";
					else
					{
						st.takeItems(ADENA, COST);
						st.giveItems(crystal, 1);
						htmlText = "Echo Crystal has been created.";
					}
					break;
				}
			}
		}
		
		return htmlText;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, QuestState qs)
	{
		return "1.htm";
	}
}
