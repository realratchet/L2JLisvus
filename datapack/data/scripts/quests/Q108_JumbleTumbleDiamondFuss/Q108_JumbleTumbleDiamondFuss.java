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
package quests.Q108_JumbleTumbleDiamondFuss;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.util.Rnd;

public class Q108_JumbleTumbleDiamondFuss extends Quest
{
	// NPCs
	private static final int GOUPH = 7523;
	private static final int REEP = 7516;
	private static final int MURDOC = 7521;
	private static final int AIRY = 7522;
	private static final int BRUNON = 7526;
	private static final int MARON = 7529;
	private static final int TOROCCO = 7555;
	
	// Items
	private static final int GOUPH_CONTRACT = 1559;
	private static final int REEP_CONTRACT = 1560;
	private static final int ELVEN_WINE = 1561;
	private static final int BRUNON_DICE = 1562;
	private static final int BRUNON_CONTRACT = 1563;
	private static final int AQUAMARINE = 1564;
	private static final int CHRYSOBERYL = 1565;
	private static final int GEM_BOX = 1566;
	private static final int COAL_PIECE = 1567;
	private static final int BRUNON_LETTER = 1568;
	private static final int BERRY_TART = 1569;
	private static final int BAT_DIAGRAM = 1570;
	private static final int STAR_DIAMOND = 1571;
	
	// Monsters
	private static final int GOBLIN_BRIGAND_LEADER = 323;
	private static final int GOBLIN_BRIGAND_LIEUTENANT = 324;
	private static final int BLADE_BAT = 480;
	
	// Rewards
	private static final int SILVERSMITH_HAMMER = 1511;
	private static final int SPIRITSHOT_FOR_BEGINNERS = 5790;
	private static final int SOULSHOT_FOR_BEGINNERS = 5789;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	private static final int LESSER_HEALING_POTION = 1060;
	
	private static final int[][] LEADER_DROPLIST =
	{
		{
			AQUAMARINE,
			1,
			10,
			800000
		},
		{
			CHRYSOBERYL,
			1,
			10,
			800000
		}
	};
	
	private static final int[][] LIEUTENANT_DROPLIST =
	{
		{
			AQUAMARINE,
			1,
			10,
			600000
		},
		{
			CHRYSOBERYL,
			1,
			10,
			600000
		}
	};
	
	public static void main(String[] args)
	{
		new Q108_JumbleTumbleDiamondFuss();
	}
	
	public Q108_JumbleTumbleDiamondFuss()
	{
		super(108, Q108_JumbleTumbleDiamondFuss.class.getSimpleName(), "Jumble, Tumble, Diamond Fuss");
		
		setItemsIds(GOUPH_CONTRACT, REEP_CONTRACT, ELVEN_WINE, BRUNON_DICE, BRUNON_CONTRACT, AQUAMARINE, CHRYSOBERYL, GEM_BOX, COAL_PIECE, BRUNON_LETTER, BERRY_TART, BAT_DIAGRAM, STAR_DIAMOND);
		
		addStartNpc(GOUPH);
		addTalkId(GOUPH, REEP, MURDOC, AIRY, BRUNON, MARON, TOROCCO);
		
		addKillId(GOBLIN_BRIGAND_LEADER, GOBLIN_BRIGAND_LIEUTENANT, BLADE_BAT);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		String htmltext = event;
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7523-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(GOUPH_CONTRACT, 1);
		}
		else if (event.equalsIgnoreCase("7555-02.htm"))
		{
			st.set("cond", "3");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(REEP_CONTRACT, 1);
			st.giveItems(ELVEN_WINE, 1);
		}
		else if (event.equalsIgnoreCase("7526-02.htm"))
		{
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(BRUNON_DICE, 1);
			st.giveItems(BRUNON_CONTRACT, 1);
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
				if (player.getRace() != Race.DWARF)
					htmltext = "7523-00.htm";
				else if (player.getLevel() < 10)
					htmltext = "7523-01.htm";
				else
					htmltext = "7523-02.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case GOUPH:
						if (cond == 1)
							htmltext = "7523-04.htm";
						else if (cond > 1 && cond < 7)
							htmltext = "7523-05.htm";
						else if (cond == 7)
						{
							htmltext = "7523-06.htm";
							st.set("cond", "8");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(GEM_BOX, 1);
							st.giveItems(COAL_PIECE, 1);
						}
						else if (cond > 7 && cond < 12)
							htmltext = "7523-07.htm";
						else if (cond == 12)
						{
							htmltext = "7523-08.htm";
							st.takeItems(STAR_DIAMOND, -1);
							st.giveItems(SILVERSMITH_HAMMER, 1);
							st.giveItems(LESSER_HEALING_POTION, 100);
							
							if (player.isNewbie())
							{
								st.showQuestionMark(26);
								if (player.isMageClass())
								{
									st.playTutorialVoice("tutorial_voice_027");
									st.giveItems(SPIRITSHOT_FOR_BEGINNERS, 3000);
								}
								else
								{
									st.playTutorialVoice("tutorial_voice_026");
									st.giveItems(SOULSHOT_FOR_BEGINNERS, 6000);
								}
							}
							
							st.giveItems(ECHO_BATTLE, 10);
							st.giveItems(ECHO_LOVE, 10);
							st.giveItems(ECHO_SOLITUDE, 10);
							st.giveItems(ECHO_FEAST, 10);
							st.giveItems(ECHO_CELEBRATION, 10);
							player.broadcastPacket(new SocialAction(player.getObjectId(), 3));
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case REEP:
						if (cond == 1)
						{
							htmltext = "7516-01.htm";
							st.set("cond", "2");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(GOUPH_CONTRACT, 1);
							st.giveItems(REEP_CONTRACT, 1);
						}
						else if (cond > 1)
							htmltext = "7516-02.htm";
						break;
					
					case TOROCCO:
						if (cond == 2)
							htmltext = "7555-01.htm";
						else if (cond == 3)
							htmltext = "7555-03.htm";
						else if (cond == 7)
							htmltext = "7555-04.htm";
						else if (cond > 7)
							htmltext = "7555-05.htm";
						break;
					
					case MARON:
						if (cond == 3)
						{
							htmltext = "7529-01.htm";
							st.set("cond", "4");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(ELVEN_WINE, 1);
							st.giveItems(BRUNON_DICE, 1);
						}
						else if (cond == 4)
							htmltext = "7529-02.htm";
						else if (cond > 4)
							htmltext = "7529-03.htm";
						break;
					
					case BRUNON:
						if (cond == 4)
							htmltext = "7526-01.htm";
						else if (cond == 5)
							htmltext = "7526-03.htm";
						else if (cond == 6)
						{
							htmltext = "7526-04.htm";
							st.set("cond", "7");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(BRUNON_CONTRACT, 1);
							st.takeItems(AQUAMARINE, -1);
							st.takeItems(CHRYSOBERYL, -1);
							st.giveItems(GEM_BOX, 1);
						}
						else if (cond == 7)
							htmltext = "7526-05.htm";
						else if (cond == 8)
						{
							htmltext = "7526-06.htm";
							st.set("cond", "9");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(COAL_PIECE, 1);
							st.giveItems(BRUNON_LETTER, 1);
						}
						else if (cond == 9)
							htmltext = "7526-07.htm";
						else if (cond > 9)
							htmltext = "7526-08.htm";
						break;
					
					case MURDOC:
						if (cond == 9)
						{
							htmltext = "7521-01.htm";
							st.set("cond", "10");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(BRUNON_LETTER, 1);
							st.giveItems(BERRY_TART, 1);
						}
						else if (cond == 10)
							htmltext = "7521-02.htm";
						else if (cond > 10)
							htmltext = "7521-03.htm";
						break;
					
					case AIRY:
						if (cond == 10)
						{
							htmltext = "7522-01.htm";
							st.set("cond", "11");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(BERRY_TART, 1);
							st.giveItems(BAT_DIAGRAM, 1);
						}
						else if (cond == 11)
							htmltext = (Rnd.nextBoolean()) ? "7522-02.htm" : "7522-04.htm";
						else if (cond == 12)
							htmltext = "7522-03.htm";
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
		
		switch (npc.getNpcId())
		{
			case GOBLIN_BRIGAND_LEADER:
				if (st.getInt("cond") == 5 && st.dropMultipleItems(LEADER_DROPLIST))
					st.set("cond", "6");
				break;
			
			case GOBLIN_BRIGAND_LIEUTENANT:
				if (st.getInt("cond") == 5 && st.dropMultipleItems(LIEUTENANT_DROPLIST))
					st.set("cond", "6");
				break;
			
			case BLADE_BAT:
				if (st.getInt("cond") == 11 && st.dropItems(STAR_DIAMOND, 1, 1, 200000))
				{
					st.takeItems(BAT_DIAGRAM, 1);
					st.set("cond", "12");
				}
				break;
		}
		return null;
	}
}
