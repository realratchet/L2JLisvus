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
package quests.Q171_ActsOfEvil;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.util.Rnd;

public class Q171_ActsOfEvil extends Quest
{
	// Items
	private static final int BLADE_MOLD = 4239;
	private static final int TYRA_BILL = 4240;
	private static final int RANGER_REPORT_1 = 4241;
	private static final int RANGER_REPORT_2 = 4242;
	private static final int RANGER_REPORT_3 = 4243;
	private static final int RANGER_REPORT_4 = 4244;
	private static final int WEAPON_TRADE_CONTRACT = 4245;
	private static final int ATTACK_DIRECTIVES = 4246;
	private static final int CERTIFICATE = 4247;
	private static final int CARGO_BOX = 4248;
	private static final int OL_MAHUM_HEAD = 4249;
	
	// NPCs
	private static final int ALVAH = 7381;
	private static final int ARODIN = 7207;
	private static final int TYRA = 7420;
	private static final int ROLENTO = 7437;
	private static final int NETI = 7425;
	private static final int BURAI = 7617;
	
	// Turek Orcs drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(496, 530000);
		CHANCES.put(497, 550000);
		CHANCES.put(498, 510000);
		CHANCES.put(499, 500000);
	}

	public static void main(String[] args)
	{
		new Q171_ActsOfEvil();
	}
	
	public Q171_ActsOfEvil()
	{
		super(171, Q171_ActsOfEvil.class.getSimpleName(), "Acts of Evil");
		
		setItemsIds(BLADE_MOLD, TYRA_BILL, RANGER_REPORT_1, RANGER_REPORT_2, RANGER_REPORT_3, RANGER_REPORT_4, WEAPON_TRADE_CONTRACT, ATTACK_DIRECTIVES, CERTIFICATE, CARGO_BOX, OL_MAHUM_HEAD);
		
		addStartNpc(ALVAH);
		addTalkId(ALVAH, ARODIN, TYRA, ROLENTO, NETI, BURAI);
		
		addKillId(496, 497, 498, 499, 62, 64, 66, 438);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7381-02.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("7207-02.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("7381-04.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("7381-07.htm"))
		{
			st.set("cond", "7");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(WEAPON_TRADE_CONTRACT, 1);
		}
		else if (event.equalsIgnoreCase("7437-03.htm"))
		{
			st.set("cond", "9");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(CARGO_BOX, 1);
			st.giveItems(CERTIFICATE, 1);
		}
		else if (event.equalsIgnoreCase("7617-04.htm"))
		{
			st.set("cond", "10");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(ATTACK_DIRECTIVES, 1);
			st.takeItems(CARGO_BOX, 1);
			st.takeItems(CERTIFICATE, 1);
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
				htmltext = (player.getLevel() < 27) ? "7381-01a.htm" : "7381-01.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case ALVAH:
						if (cond < 4)
							htmltext = "7381-02a.htm";
						else if (cond == 4)
							htmltext = "7381-03.htm";
						else if (cond == 5)
						{
							if (st.hasQuestItems(RANGER_REPORT_1, RANGER_REPORT_2, RANGER_REPORT_3, RANGER_REPORT_4))
							{
								htmltext = "7381-05.htm";
								st.set("cond", "6");
								st.playSound(QuestState.SOUND_MIDDLE);
								st.takeItems(RANGER_REPORT_1, 1);
								st.takeItems(RANGER_REPORT_2, 1);
								st.takeItems(RANGER_REPORT_3, 1);
								st.takeItems(RANGER_REPORT_4, 1);
							}
							else
								htmltext = "7381-04a.htm";
						}
						else if (cond == 6)
						{
							if (st.hasQuestItems(WEAPON_TRADE_CONTRACT, ATTACK_DIRECTIVES))
								htmltext = "7381-06.htm";
							else
								htmltext = "7381-05a.htm";
						}
						else if (cond > 6 && cond < 11)
							htmltext = "7381-07a.htm";
						else if (cond == 11)
						{
							htmltext = "7381-08.htm";
							st.rewardItems(57, 90000);
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ARODIN:
						if (cond == 1)
							htmltext = "7207-01.htm";
						else if (cond == 2)
							htmltext = "7207-01a.htm";
						else if (cond == 3)
						{
							if (st.hasQuestItems(TYRA_BILL))
							{
								htmltext = "7207-03.htm";
								st.set("cond", "4");
								st.playSound(QuestState.SOUND_MIDDLE);
								st.takeItems(TYRA_BILL, 1);
							}
							else
								htmltext = "7207-01a.htm";
						}
						else if (cond > 3)
							htmltext = "7207-03a.htm";
						break;
					
					case TYRA:
						if (cond == 2)
						{
							if (st.getQuestItemsCount(BLADE_MOLD) >= 20)
							{
								htmltext = "7420-01.htm";
								st.set("cond", "3");
								st.playSound(QuestState.SOUND_MIDDLE);
								st.takeItems(BLADE_MOLD, -1);
								st.giveItems(TYRA_BILL, 1);
							}
							else
								htmltext = "7420-01b.htm";
						}
						else if (cond == 3)
							htmltext = "7420-01a.htm";
						else if (cond > 3)
							htmltext = "7420-02.htm";
						break;
					
					case NETI:
						if (cond == 7)
						{
							htmltext = "7425-01.htm";
							st.set("cond", "8");
							st.playSound(QuestState.SOUND_MIDDLE);
						}
						else if (cond > 7)
							htmltext = "7425-02.htm";
						break;
					
					case ROLENTO:
						if (cond == 8)
							htmltext = "7437-01.htm";
						else if (cond > 8)
							htmltext = "7437-03a.htm";
						break;
					
					case BURAI:
						if (cond == 9 && st.hasQuestItems(CERTIFICATE, CARGO_BOX, ATTACK_DIRECTIVES))
							htmltext = "7617-01.htm";
						else if (cond == 10)
						{
							if (st.getQuestItemsCount(OL_MAHUM_HEAD) >= 30)
							{
								htmltext = "7617-05.htm";
								st.set("cond", "11");
								st.playSound(QuestState.SOUND_MIDDLE);
								st.takeItems(OL_MAHUM_HEAD, -1);
								st.rewardItems(57, 8000);
							}
							else
								htmltext = "7617-04a.htm";
						}
						break;
				}
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
		final QuestState st = checkPlayerState(killer, npc, State.STARTED);
		if (st == null)
			return null;
		
		final int npcId = npc.getNpcId();
		
		switch (npcId)
		{
			case 496:
			case 497:
			case 498:
			case 499:
				if (st.getInt("cond") == 2 && !st.dropItems(BLADE_MOLD, 1, 20, CHANCES.get(npcId)))
				{
					final int count = st.getQuestItemsCount(BLADE_MOLD);
					if (count == 5 || (count >= 10 && Rnd.get(100) < 25))
						addSpawn(5190, killer);
				}
				break;
			
			case 62:
			case 64:
				if (st.getInt("cond") == 5)
				{
					if (!st.hasQuestItems(RANGER_REPORT_1))
					{
						st.giveItems(RANGER_REPORT_1, 1);
						st.playSound(QuestState.SOUND_ITEMGET);
					}
					else if (Rnd.get(100) < 20)
					{
						if (!st.hasQuestItems(RANGER_REPORT_2))
						{
							st.giveItems(RANGER_REPORT_2, 1);
							st.playSound(QuestState.SOUND_ITEMGET);
						}
						else if (!st.hasQuestItems(RANGER_REPORT_3))
						{
							st.giveItems(RANGER_REPORT_3, 1);
							st.playSound(QuestState.SOUND_ITEMGET);
						}
						else if (!st.hasQuestItems(RANGER_REPORT_4))
						{
							st.giveItems(RANGER_REPORT_4, 1);
							st.playSound(QuestState.SOUND_ITEMGET);
						}
					}
				}
				break;
			
			case 438:
				if (st.getInt("cond") == 6 && Rnd.get(100) < 10 && !st.hasQuestItems(WEAPON_TRADE_CONTRACT, ATTACK_DIRECTIVES))
				{
					st.playSound(QuestState.SOUND_ITEMGET);
					st.giveItems(WEAPON_TRADE_CONTRACT, 1);
					st.giveItems(ATTACK_DIRECTIVES, 1);
				}
				break;
			
			case 66:
				if (st.getInt("cond") == 10)
					st.dropItems(OL_MAHUM_HEAD, 1, 30, 500000);
				break;
		}
		
		return null;
	}
}