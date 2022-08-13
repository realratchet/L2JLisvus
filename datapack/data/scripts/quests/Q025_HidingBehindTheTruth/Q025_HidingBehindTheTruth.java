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
package quests.Q025_HidingBehindTheTruth;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;

public class Q025_HidingBehindTheTruth extends Quest
{
	// Items
	private static final int FOREST_OF_DEADMAN_MAP = 7063;
	private static final int CONTRACT = 7066;
	private static final int LIDIA_DRESS = 7155;
	private static final int SUSPICIOUS_TOTEM_DOLL_2 = 7156;
	private static final int GEMSTONE_KEY = 7157;
	private static final int SUSPICIOUS_TOTEM_DOLL_3 = 7158;
	
	// Rewards
	private static final int EARRING_OF_BLESSING = 874;
	private static final int RING_OF_BLESSING = 905;
	private static final int NECKLACE_OF_BLESSING = 936;
	
	// NPCs
	private static final int AGRIPEL = 8348;
	private static final int BENEDICT = 8349;
	private static final int MYSTERIOUS_WIZARD = 8522;
	private static final int TOMBSTONE = 8531;
	private static final int MAID_OF_LIDIA = 8532;
	private static final int BROKEN_BOOKSHELF_1 = 8533;
	private static final int BROKEN_BOOKSHELF_2 = 8534;
	private static final int BROKEN_BOOKSHELF_3 = 8535;
	private static final int COFFIN = 8536;
	
	// Monsters
	private static final int TRIOL_PAWN = 5218;
	
	// Spawns
	private static final Map<Integer, Location> TRIOL_SPAWNS = new HashMap<>(3);
	{
		TRIOL_SPAWNS.put(BROKEN_BOOKSHELF_1, new Location(47142, -35941, -1623, 0));
		TRIOL_SPAWNS.put(BROKEN_BOOKSHELF_2, new Location(50055, -47020, -3396, 0));
		TRIOL_SPAWNS.put(BROKEN_BOOKSHELF_3, new Location(59712, -47568, -2720, 0));
	}
	
	// Sound
	private static final String SOUND_HORROR_1 = "SkillSound5.horror_01";
	private static final String SOUND_HORROR_2 = "AmdSound.dd_horror_02";
	private static final String SOUND_CRY = "ChrSound.FDElf_Cry";
	
	private final Map<L2NpcInstance, L2Attackable> _triolPawns = new ConcurrentHashMap<>(3);
	private L2NpcInstance _coffin;

	public static void main(String[] args)
	{
		new Q025_HidingBehindTheTruth();
	}
	
	public Q025_HidingBehindTheTruth()
	{
		super(25, Q025_HidingBehindTheTruth.class.getSimpleName(), "Hiding Behind the Truth");
		
		// Note: FOREST_OF_DEADMAN_MAP and SUSPICIOUS_TOTEM_DOLL_2 are items from previous quests, should not be added.
		setItemsIds(CONTRACT, LIDIA_DRESS, GEMSTONE_KEY, SUSPICIOUS_TOTEM_DOLL_3);
		
		addStartNpc(BENEDICT);
		addTalkId(AGRIPEL, BENEDICT, MYSTERIOUS_WIZARD, TOMBSTONE, MAID_OF_LIDIA, BROKEN_BOOKSHELF_1, BROKEN_BOOKSHELF_2, BROKEN_BOOKSHELF_3, COFFIN);
		addFirstTalkId(MAID_OF_LIDIA);
		
		addKillId(TRIOL_PAWN);
		addDecayId(TRIOL_PAWN, COFFIN);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		// Benedict
		if (event.equalsIgnoreCase("8349-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("state", "1");
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("8349-04.htm"))
		{
			// Suspicious Totem is lost, redirect to Mysterious Wizard to obtain it again
			if (!st.hasQuestItems(SUSPICIOUS_TOTEM_DOLL_2))
			{
				htmltext = "8349-05.htm";
				if (st.getInt("cond") == 1)
				{
					st.set("cond", "2");
					st.playSound(QuestState.SOUND_MIDDLE);
				}
			}
		}
		else if (event.equalsIgnoreCase("8349-10.htm"))
		{
			st.set("state", "2");
			st.set("cond", "4");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		// Agripel
		else if (event.equalsIgnoreCase("8348-02.htm"))
		{
			st.set("state", "3");
			st.takeItems(SUSPICIOUS_TOTEM_DOLL_2, -1);
		}
		else if (event.equalsIgnoreCase("8348-08.htm"))
		{
			st.set("state", "6");
			st.set("cond", "5");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(GEMSTONE_KEY, 1);
		}
		else if (event.equalsIgnoreCase("8348-10.htm"))
		{
			st.set("state", "21");
			st.takeItems(SUSPICIOUS_TOTEM_DOLL_3, -1);
		}
		else if (event.equalsIgnoreCase("8348-13.htm"))
		{
			st.set("state", "22");
		}
		else if (event.equalsIgnoreCase("8348-16.htm"))
		{
			st.set("state", "23");
			st.set("cond", "17");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8348-17.htm"))
		{
			st.set("state", "24");
			st.set("cond", "18");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		// Mysterious Wizard
		else if (event.equalsIgnoreCase("8522-04.htm"))
		{
			st.set("state", "7");
			st.set("cond", "6");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8522-10.htm"))
		{
			st.set("state", "19");
		}
		else if (event.equalsIgnoreCase("8522-13.htm"))
		{
			st.set("state", "20");
			st.set("cond", "16");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8522-16.htm"))
		{
			st.takeItems(FOREST_OF_DEADMAN_MAP, -1);
			st.giveItems(EARRING_OF_BLESSING, 1);
			st.giveItems(NECKLACE_OF_BLESSING, 1);
			st.addExpAndSp(1607062, 0);
			st.playSound(QuestState.SOUND_FINISH);
			st.exitQuest(false);
		}
		// Broken Bookshelf 1, 2 and 3
		else if (event.equalsIgnoreCase("853x-05.htm"))
		{
			// Get bookshelf flags.
			final String npcId = String.valueOf(npc.getNpcId());
			st.set(npcId, "1");
			
			// Check bookshelves.
			if (st.getInt("8533") + st.getInt("8534") + st.getInt("8535") == 3)
			{
				// All are open, clear bookshelf identifier, mark gem box location.
				st.unset("8533");
				st.unset("8534");
				st.unset("8535");
				st.set("bookshelf", npcId);
				st.set("state", "8");
				st.playSound(SOUND_HORROR_2);
			}
			else
			{
				// Not all bookshelves are opened yet.
				htmltext = "853x-03.htm";
			}
		}
		else if (event.equalsIgnoreCase("853x-07.htm"))
		{
			if (!st.hasQuestItems(SUSPICIOUS_TOTEM_DOLL_3))
			{
				L2Attackable triolPawn = _triolPawns.get(npc);
				if (triolPawn == null)
				{
					final Location triolLoc = TRIOL_SPAWNS.get(npc.getNpcId());
					triolPawn = (L2Attackable) addSpawn(TRIOL_PAWN, triolLoc.getX(), triolLoc.getY(), triolLoc.getZ(), triolLoc.getHeading(), false, 120000);
					triolPawn.broadcastPacket(new NpcSay(triolPawn.getObjectId(), 0, triolPawn.getNpcId(), "Did you call me, " + player.getName() + "?"));
					triolPawn.setScriptValue(player.getObjectId());
					triolPawn.addDamageHate(player, 0, 999);
					triolPawn.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
					
					_triolPawns.put(npc, triolPawn);
					
					st.set("cond", "7");
					st.playSound(QuestState.SOUND_MIDDLE);
				}
				else if (triolPawn.getScriptValue() == player.getObjectId())
				{
					htmltext = "853x-08.htm";
				}
				else
				{
					htmltext = "853x-09.htm";
				}
			}
			else
			{
				htmltext = "853x-10.htm";
			}
		}
		else if (event.equalsIgnoreCase("853x-11.htm"))
		{
			st.unset("bookshelf");
			st.set("state", "9");
			st.set("cond", "9");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(GEMSTONE_KEY, -1);
			st.giveItems(CONTRACT, 1);
		}
		// Maid of Lidia
		else if (event.equalsIgnoreCase("8532-02.htm"))
		{
			st.set("state", "10");
			st.takeItems(CONTRACT, -1);
		}
		else if (event.equalsIgnoreCase("8532-07.htm"))
		{
			st.set("state", "11");
			st.set("cond", "11");
			st.playSound(SOUND_HORROR_1);
		}
		else if (event.equalsIgnoreCase("8532-12.htm"))
		{
			final int sorrow = st.getInt("sorrow");
			if (sorrow > 0)
			{
				htmltext = "8532-11.htm";
				st.set("sorrow", String.valueOf(sorrow - 1));
				st.playSound(SOUND_CRY);
			}
			else
			{
				st.unset("sorrow");
				st.set("state", "14");
			}
		}
		else if (event.equalsIgnoreCase("8532-17.htm"))
		{
			st.set("state", "15");
		}
		else if (event.equalsIgnoreCase("8532-21.htm"))
		{
			st.set("state", "16");
			st.set("cond", "15");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8532-25.htm"))
		{
			st.takeItems(FOREST_OF_DEADMAN_MAP, -1);
			st.giveItems(EARRING_OF_BLESSING, 1);
			st.giveItems(RING_OF_BLESSING, 2);
			st.addExpAndSp(1607062, 0);
			st.playSound(QuestState.SOUND_FINISH);
			st.exitQuest(false);
		}
		// Tombstone
		else if (event.equalsIgnoreCase("8531-02.htm"))
		{
			st.set("cond", "12");
			st.playSound(QuestState.SOUND_MIDDLE);
			if (_coffin == null)
			{
				_coffin = addSpawn(COFFIN, 60104, -35820, -681, 0, false, 20000);
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onDecay(L2NpcInstance npc)
	{
		if (_coffin == npc)
		{
			_coffin = null;
		}
		else
			_triolPawns.values().remove(npc);
		
		return null;
	}
	
	@Override
	public String onFirstTalk(L2NpcInstance npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if (st != null && st.getInt("state") == 11)
		{
			st.playSound(SOUND_HORROR_1);
			return "8532-08.htm";
		}
		npc.showChatWindow(player);
		return null;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				QuestState st2 = player.getQuestState("Q024_InhabitantsOfTheForestOfTheDead");
				htmltext = (st2 != null && st2.isCompleted() && player.getLevel() >= 66) ? "8349-01.htm" : "8349-02.htm";
				break;
			case State.STARTED:
				final int state = st.getInt("state");
				switch (npc.getNpcId())
				{
					case BENEDICT:
						if (state == 1)
							htmltext = "8349-03a.htm";
						else if (state > 1)
							htmltext = "8349-10.htm";
						break;
					case AGRIPEL:
						if (state == 2)
							htmltext = "8348-01.htm";
						else if (state > 2 && state < 6)
							htmltext = "8348-02.htm";
						else if (state > 5 && state < 20)
							htmltext = "8348-08a.htm";
						else if (state == 20)
							htmltext = "8348-09.htm";
						else if (state == 21)
							htmltext = "8348-10a.htm";
						else if (state == 22)
							htmltext = "8348-15.htm";
						else if (state == 23)
							htmltext = "8348-18.htm";
						else if (state == 24)
							htmltext = "8348-19.htm";
						break;
					case MYSTERIOUS_WIZARD:
						if (state == 1)
						{
							if (!st.hasQuestItems(SUSPICIOUS_TOTEM_DOLL_2))
							{
								htmltext = "8522-01.htm";
								st.set("cond", "3");
								st.playSound(QuestState.SOUND_MIDDLE);
								st.giveItems(SUSPICIOUS_TOTEM_DOLL_2, 1);
							}
							else
							{
								htmltext = "8522-02.htm";
							}
						}
						else if (state > 1 && state < 6)
							htmltext = "8522-02.htm";
						else if (state == 6)
							htmltext = "8522-03.htm";
						else if (state > 6 && state < 9)
							htmltext = "8522-04.htm";
						else if (state == 9)
						{
							htmltext = "8522-06.htm";
							
							if (st.getInt("cond") != 10 && st.hasQuestItems(CONTRACT))
							{
								st.set("cond", "10");
								st.playSound(QuestState.SOUND_MIDDLE);
							}
						}
						else if (state > 9 && state < 16)
							htmltext = "8522-06.htm";
						else if (state == 16)
							htmltext = "8522-06a.htm";
						else if (state == 19)
							htmltext = "8522-11.htm";
						else if (state > 19 && state < 23)
							htmltext = "8522-14.htm";
						else if (state == 23)
							htmltext = "8522-15a.htm";
						else if (state == 24)
							htmltext = "8522-15.htm";
						break;
					case BROKEN_BOOKSHELF_1:
					case BROKEN_BOOKSHELF_2:
					case BROKEN_BOOKSHELF_3:
						if (state == 7)
						{
							// Investigating bookshelves, check if current one has been opened.
							if (st.getInt(String.valueOf(npc.getNpcId())) == 0)
							{
								htmltext = "853x-01.htm";
							}
							else
							{
								htmltext = "853x-03.htm";
							}
						}
						else if (state == 8)
						{
							// Gem box has been found. Check if in this bookshelf.
							if (st.getInt("bookshelf") == npc.getNpcId())
							{
								htmltext = "853x-05.htm";
							}
							else
							{
								htmltext = "853x-03.htm";
							}
						}
						else if (state > 8)
						{
							htmltext = "853x-02.htm";
						}
						break;
					case MAID_OF_LIDIA:
						if (state == 9)
							htmltext = "8532-01.htm";
						else if (state > 9 && state < 12)
							htmltext = "8532-03.htm";
						else if (state == 12)
						{
							htmltext = "8532-09.htm";
							st.set("sorrow", "4");
							st.set("state", "13");
							st.set("cond", "14");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.takeItems(LIDIA_DRESS, -1);
						}
						else if (state == 13)
						{
							htmltext = "8532-10.htm";
							st.playSound(SOUND_CRY);
						}
						else if (state == 14)
							htmltext = "8532-12.htm";
						else if (state == 15)
							htmltext = "8532-17.htm";
						else if (state > 15 && state < 23)
							htmltext = "8532-21.htm";
						else if (state == 23)
							htmltext = "8532-23.htm";
						else if (state == 24)
							htmltext = "8532-24.htm";
						break;
					
					case TOMBSTONE:
						if (state == 11)
							htmltext = (_coffin == null) ? "8531-01.htm" : "8531-02.htm";
						else if (state > 11)
							htmltext = "8531-03.htm";
						break;
					
					case COFFIN:
						if (state == 11)
						{
							htmltext = "8536-01.htm";
							st.set("state", "12");
							st.set("cond", "13");
							st.playSound(QuestState.SOUND_MIDDLE);
							st.giveItems(LIDIA_DRESS, 1);
							
							_coffin.deleteMe();
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
		final QuestState st = checkPlayerCondition(killer, npc, "cond", "7");
		if (st == null)
			return null;
		
		if (killer.getObjectId() != npc.getScriptValue())
			return null;
		
		if (st.dropItemsAlways(SUSPICIOUS_TOTEM_DOLL_3, 1, 1))
		{
			st.set("cond", "8");
			npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "I'm confused! Maybe it's time to go back."));
		}
		
		return null;
	}
}