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
package village_master.Alliance;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * Created by DraX on 2005.08.12
 * Minor fixes by DrLecter 2005.09.10
 * Updated by DnR 2020.02.10
 */
public class Alliance extends Quest
{
	private static final int[] NPC_LIST = new int[]
	{
		7026,
		7031,
		7037,
		7066,
		7070,
		7109,
		7115,
		7120,
		7154,
		7174,
		7175,
		7176,
		7187,
		7191,
		7195,
		7288,
		7289,
		7290,
		7297,
		7358,
		7373,
		7462,
		7474,
		7498,
		7499,
		7500,
		7503,
		7504,
		7505,
		7508,
		7511,
		7512,
		7513,
		7520,
		7525,
		7565,
		7594,
		7595,
		7676,
		7677,
		7681,
		7685,
		7687,
		7689,
		7694,
		7699,
		7704,
		7845,
		7847,
		7849,
		7854,
		7857,
		7862,
		7865,
		7894,
		7897,
		7900,
		7905,
		7910,
		7913,
		8269,
		8272,
		8276,
		8279,
		8285,
		8288,
		8314,
		8317,
		8321,
		8324,
		8326,
		8328,
		8331,
		8334,
		8755
	};
	
	public static void main(String[] args)
	{
		// Quest class
		new Alliance();
	}
	
	public Alliance()
	{
		super(-1, Alliance.class.getSimpleName(), "village_master");
		
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
		
		if (event.equalsIgnoreCase("9001-01.htm"))
		{
			htmlText = event;
		}
		else if (player.getClanId() == 0)
		{
			htmlText = "<html><body>You must belong to a clan.</body></html>";
			st.exitQuest(true);
		}
		else // Only if player belongs to a clan
		{
			htmlText = event;
		}
		
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
		
		htmlText = "9001-01.htm";
		return htmlText;
	}
}