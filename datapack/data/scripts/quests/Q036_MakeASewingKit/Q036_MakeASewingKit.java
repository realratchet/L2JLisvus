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
package quests.Q036_MakeASewingKit;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q036_MakeASewingKit extends Quest
{
	private static final int FERRIS = 7847;
	private static final int IRON_GOLEM = 566;

	// Items
	private static final int REINFORCED_STEEL = 7163;
	private static final int ARTISANS_FRAME = 1891;
	private static final int ORIHARUKON = 1893;
	
	// Reward
	private static final int SEWING_KIT = 7078;

	public static void main(String[] args)
	{
		new Q036_MakeASewingKit();
	}
	
	public Q036_MakeASewingKit()
	{
		super(36, Q036_MakeASewingKit.class.getSimpleName(), "Make a Sewing Kit");
		
		setItemsIds(REINFORCED_STEEL);
		
		addStartNpc(FERRIS); // Ferris
		addTalkId(FERRIS);
		
		addKillId(IRON_GOLEM); // Iron Golem
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7847-1.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7847-3.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(REINFORCED_STEEL, 5);
		}
		else if (event.equalsIgnoreCase("7847-5.htm"))
		{
			if (st.getQuestItemsCount(ORIHARUKON) >= 10 && st.getQuestItemsCount(ARTISANS_FRAME) >= 10)
			{
				st.takeItems(ARTISANS_FRAME, 10);
				st.takeItems(ORIHARUKON, 10);
				st.giveItems(SEWING_KIT, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "7847-4a.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				if (player.getLevel() >= 60)
				{
					QuestState fwear = player.getQuestState("Q037_MakeFormalWear");
					if (fwear != null && fwear.getInt("cond") == 6)
						htmltext = "7847-0.htm";
					else
						htmltext = "7847-0a.htm";
				}
				else
					htmltext = "7847-0b.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				if (cond == 1)
					htmltext = "7847-1a.htm";
				else if (cond == 2)
					htmltext = "7847-2.htm";
				else if (cond == 3)
					htmltext = (st.getQuestItemsCount(ORIHARUKON) < 10 || st.getQuestItemsCount(ARTISANS_FRAME) < 10) ? "7847-4a.htm" : "7847-4.htm";
				break;
			
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "1");
		if (st == null)
			return null;
		
		if (st.dropItems(REINFORCED_STEEL, 1, 5, 500000))
			st.set("cond", "2");
		
		return null;
	}
}