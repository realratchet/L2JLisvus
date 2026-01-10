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
package custom.HeroCirclet;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * @author DnR
 * 
 * Original made by DrLecter.
 */
public class HeroCirclet extends Quest
{
	private static final int[] MONUMENTS = {8690, 8769, 8770, 8771, 8772};
	private static final int CIRCLET = 6842;
	
	public static void main(String[] args)
    {
        new HeroCirclet();
    }
	
	public HeroCirclet()
	{
		super(-1, HeroCirclet.class.getSimpleName(), "custom");
		
		for (int id : MONUMENTS)
		{
			addStartNpc(id);
			addTalkId(id);
		}
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = null;

		if (!player.isHero())
		{
			htmltext = "no_hero.htm";
		}
		else
		{
			QuestState st = player.getQuestState(getName());
			if (st == null)
			{
				htmltext = getNoQuestMsg();
			}
			else if (st.hasQuestItems(CIRCLET))
			{
				htmltext = "already_have_circlet.htm";
			}
			else
			{
				st.giveItems(6842, 1);
			}
		}
		return htmltext;
	}
}
