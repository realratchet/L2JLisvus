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
package teleports.TeleportWithCharm;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * @author DraX
 *
 */
public class TeleportWithCharm extends Quest
{
	private static final int WHIRPY = 7540;
	private static final int TAMIL = 7576;
	
	private static final int ORC_GATEKEEPER_CHARM = 1658;
	private static final int DWARF_GATEKEEPER_TOKEN = 1659;
	
	public static void main(String[] args)
    {
        // Quest class
        new TeleportWithCharm();
    }
	
	public TeleportWithCharm()
	{
		super(-1, TeleportWithCharm.class.getSimpleName(), "teleports");
		
		addStartNpc(WHIRPY);
		addStartNpc(TAMIL);
		addTalkId(WHIRPY);
		addTalkId(TAMIL);
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "";
		L2PcInstance player = st.getPlayer();
		
		int npcId = npc.getNpcId();
		if (npcId == WHIRPY)
		{
			if (st.getQuestItemsCount(DWARF_GATEKEEPER_TOKEN) >= 1)
			{
				st.takeItems(DWARF_GATEKEEPER_TOKEN, 1);
				player.teleToLocation(-80826, 149775, -3043);
			}
			else
			{
				htmltext = "7540-01.htm";
			}
		}
		else if (npcId == TAMIL)
		{
			if (st.getQuestItemsCount(ORC_GATEKEEPER_CHARM) >= 1)
			{
				st.takeItems(ORC_GATEKEEPER_CHARM, 1);
				player.teleToLocation(-80826, 149775, -3043);
			}
			else
			{
				htmltext = "7576-01.htm";
			}
		}
		
		st.exitQuest(true);
		return htmltext;
	}
}
