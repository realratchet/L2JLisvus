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
package quests.Q404_PathToWizard;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Quest 404 Path to Wizard
 * Converted to Java
 * @author Karakan
 */
public class Q404_PathToWizard extends Quest
{
	private static final int MAP_OF_LUSTER_ID = 1280;
	private static final int KEY_OF_FLAME_ID = 1281;
	private static final int FLAME_EARING_ID = 1282;
	private static final int BROKEN_BRONZE_MIRROR_ID = 1283;
	private static final int WIND_FEATHER_ID = 1284;
	private static final int WIND_BANGEL_ID = 1285;
	private static final int RAMAS_DIARY_ID = 1286;
	private static final int SPARKLE_PEBBLE_ID = 1287;
	private static final int WATER_NECKLACE_ID = 1288;
	private static final int RUST_GOLD_COIN_ID = 1289;
	private static final int RED_SOIL_ID = 1290;
	private static final int EARTH_RING_ID = 1291;
	private static final int BEAD_OF_SEASON_ID = 1292;
	
	public static void main(String[] args)
	{
		new Q404_PathToWizard();
	}
	
	public Q404_PathToWizard()
	{
		super(404, Q404_PathToWizard.class.getSimpleName(), "Path to Wizard");
		
		setItemsIds(MAP_OF_LUSTER_ID, KEY_OF_FLAME_ID, FLAME_EARING_ID, BROKEN_BRONZE_MIRROR_ID, WIND_FEATHER_ID, WIND_BANGEL_ID, RAMAS_DIARY_ID, SPARKLE_PEBBLE_ID, WATER_NECKLACE_ID, RUST_GOLD_COIN_ID, RED_SOIL_ID, EARTH_RING_ID);
		
		addStartNpc(7391);
		addTalkId(7391, 7409, 7410, 7411, 7412, 7413);
		
		addKillId(21, 359, 5030);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		String htmltext = event;
		if (st == null)
			return htmltext;
		
		switch (event)
		{
			case "1":
				if (st.getPlayer().getClassId().getId() == 0x0a)
				{
					if (st.getPlayer().getLevel() >= 19)
					{
						if (st.getQuestItemsCount(BEAD_OF_SEASON_ID) > 0)
						{
							htmltext = "7391-03.htm";
						}
						else
						{
							htmltext = "7391-08.htm";
							st.set("cond", "1");
							st.setState(State.STARTED);
							st.playSound(QuestState.SOUND_ACCEPT);
						}
					}
					else
					{
						htmltext = "7391-02.htm";
					}
				}
				else if (st.getPlayer().getClassId().getId() == 0x0b)
				{
					htmltext = "7391-02a.htm";
				}
				else
				{
					htmltext = "7391-01.htm";
				}
				break;
			
			case "7410_1":
			{
				if (st.getQuestItemsCount(WIND_FEATHER_ID) == 0)
				{
					htmltext = "7410-03.htm";
					st.giveItems(WIND_FEATHER_ID, 1);
					st.set("cond", "6");
				}
			}
				break;
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
		
		int id = st.getState();
		int npcId = npc.getNpcId();
		switch (npcId)
		{
			case 7391:
				if (id == State.COMPLETED)
				{
					htmltext = getAlreadyCompletedMsg();
				}
				else if (st.getInt("cond") == 0)
				{
					htmltext = "7391-04.htm";
				}
				else if (st.getInt("cond") != 0 && (st.getQuestItemsCount(FLAME_EARING_ID) == 0 || st.getQuestItemsCount(WIND_BANGEL_ID) == 0 || st.getQuestItemsCount(WATER_NECKLACE_ID) == 0 || st.getQuestItemsCount(EARTH_RING_ID) == 0))
				{
					htmltext = "7391-05.htm";
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(FLAME_EARING_ID) != 0 && st.getQuestItemsCount(WIND_BANGEL_ID) != 0 && st.getQuestItemsCount(WATER_NECKLACE_ID) != 0 && st.getQuestItemsCount(EARTH_RING_ID) != 0)
				{
					st.takeItems(FLAME_EARING_ID, st.getQuestItemsCount(FLAME_EARING_ID));
					st.takeItems(WIND_BANGEL_ID, st.getQuestItemsCount(WIND_BANGEL_ID));
					st.takeItems(WATER_NECKLACE_ID, st.getQuestItemsCount(WATER_NECKLACE_ID));
					st.takeItems(EARTH_RING_ID, st.getQuestItemsCount(EARTH_RING_ID));
					st.set("cond", "0");
					st.setState(State.COMPLETED);
					st.playSound(QuestState.SOUND_FINISH);
					if (st.getQuestItemsCount(BEAD_OF_SEASON_ID) == 0)
					{
						st.giveItems(BEAD_OF_SEASON_ID, 1);
					}
					htmltext = "7391-06.htm";
				}
				break;
			
			case 7411:
				if (st.getInt("cond") != 0 && st.getQuestItemsCount(MAP_OF_LUSTER_ID) == 0 && st.getQuestItemsCount(FLAME_EARING_ID) == 0)
				{
					if (st.getQuestItemsCount(MAP_OF_LUSTER_ID) == 0)
					{
						st.giveItems(MAP_OF_LUSTER_ID, 1);
					}
					htmltext = "7411-01.htm";
					st.set("cond", "2");
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(MAP_OF_LUSTER_ID) != 0 && st.getQuestItemsCount(KEY_OF_FLAME_ID) == 0)
				{
					htmltext = "7411-02.htm";
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(MAP_OF_LUSTER_ID) != 0 && st.getQuestItemsCount(KEY_OF_FLAME_ID) != 0)
				{
					st.takeItems(KEY_OF_FLAME_ID, st.getQuestItemsCount(KEY_OF_FLAME_ID));
					st.takeItems(MAP_OF_LUSTER_ID, st.getQuestItemsCount(MAP_OF_LUSTER_ID));
					if (st.getQuestItemsCount(FLAME_EARING_ID) == 0)
					{
						st.giveItems(FLAME_EARING_ID, 1);
					}
					htmltext = "7411-03.htm";
					st.set("cond", "4");
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(FLAME_EARING_ID) != 0)
				{
					htmltext = "7411-04.htm";
				}
				break;
			
			case 7412:
				if (st.getInt("cond") != 0 && st.getQuestItemsCount(FLAME_EARING_ID) != 0 && st.getQuestItemsCount(BROKEN_BRONZE_MIRROR_ID) == 0 && st.getQuestItemsCount(WIND_BANGEL_ID) == 0)
				{
					if (st.getQuestItemsCount(BROKEN_BRONZE_MIRROR_ID) == 0)
					{
						st.giveItems(BROKEN_BRONZE_MIRROR_ID, 1);
					}
					htmltext = "7412-01.htm";
					st.set("cond", "5");
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(BROKEN_BRONZE_MIRROR_ID) != 0 && st.getQuestItemsCount(WIND_FEATHER_ID) == 0)
				{
					htmltext = "7412-02.htm";
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(BROKEN_BRONZE_MIRROR_ID) != 0 && st.getQuestItemsCount(WIND_FEATHER_ID) != 0)
				{
					st.takeItems(WIND_FEATHER_ID, st.getQuestItemsCount(WIND_FEATHER_ID));
					st.takeItems(BROKEN_BRONZE_MIRROR_ID, st.getQuestItemsCount(BROKEN_BRONZE_MIRROR_ID));
					if (st.getQuestItemsCount(WIND_BANGEL_ID) == 0)
					{
						st.giveItems(WIND_BANGEL_ID, 1);
					}
					htmltext = "7412-03.htm";
					st.set("cond", "7");
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(WIND_BANGEL_ID) != 0)
				{
					htmltext = "7412-04.htm";
				}
				break;
			
			case 7410:
				if (st.getInt("cond") != 0 && st.getQuestItemsCount(BROKEN_BRONZE_MIRROR_ID) != 0 && st.getQuestItemsCount(WIND_FEATHER_ID) == 0)
				{
					htmltext = "7410-01.htm";
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(BROKEN_BRONZE_MIRROR_ID) != 0 && st.getQuestItemsCount(WIND_FEATHER_ID) != 0)
				{
					htmltext = "7410-04.htm";
				}
				break;
			
			case 7413:
				if (st.getInt("cond") != 0 && st.getQuestItemsCount(WIND_BANGEL_ID) != 0 && st.getQuestItemsCount(RAMAS_DIARY_ID) == 0 && st.getQuestItemsCount(WATER_NECKLACE_ID) == 0)
				{
					if (st.getQuestItemsCount(RAMAS_DIARY_ID) == 0)
					{
						st.giveItems(RAMAS_DIARY_ID, 1);
					}
					htmltext = "7413-01.htm";
					st.set("cond", "8");
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(RAMAS_DIARY_ID) != 0 && st.getQuestItemsCount(SPARKLE_PEBBLE_ID) < 2)
				{
					htmltext = "7413-02.htm";
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(RAMAS_DIARY_ID) != 0 && st.getQuestItemsCount(SPARKLE_PEBBLE_ID) >= 2)
				{
					st.takeItems(SPARKLE_PEBBLE_ID, st.getQuestItemsCount(SPARKLE_PEBBLE_ID));
					st.takeItems(RAMAS_DIARY_ID, st.getQuestItemsCount(RAMAS_DIARY_ID));
					if (st.getQuestItemsCount(WATER_NECKLACE_ID) == 0)
					{
						st.giveItems(WATER_NECKLACE_ID, 1);
					}
					htmltext = "7413-03.htm";
					st.set("cond", "10");
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(WATER_NECKLACE_ID) != 0)
				{
					htmltext = "7413-04.htm";
				}
				break;
			
			case 7409:
				if (st.getInt("cond") != 0 && st.getQuestItemsCount(WATER_NECKLACE_ID) != 0 && st.getQuestItemsCount(RUST_GOLD_COIN_ID) == 0 && st.getQuestItemsCount(EARTH_RING_ID) == 0)
				{
					if (st.getQuestItemsCount(RUST_GOLD_COIN_ID) == 0)
					{
						st.giveItems(RUST_GOLD_COIN_ID, 1);
					}
					htmltext = "7409-01.htm";
					st.set("cond", "11");
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(RUST_GOLD_COIN_ID) != 0 && st.getQuestItemsCount(RED_SOIL_ID) == 0)
				{
					htmltext = "7409-02.htm";
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(RUST_GOLD_COIN_ID) != 0 && st.getQuestItemsCount(RED_SOIL_ID) != 0)
				{
					st.takeItems(RED_SOIL_ID, st.getQuestItemsCount(RED_SOIL_ID));
					st.takeItems(RUST_GOLD_COIN_ID, st.getQuestItemsCount(RUST_GOLD_COIN_ID));
					if (st.getQuestItemsCount(EARTH_RING_ID) == 0)
					{
						st.giveItems(EARTH_RING_ID, 1);
					}
					htmltext = "7409-03.htm";
					st.set("cond", "13");
				}
				else if (st.getInt("cond") != 0 && st.getQuestItemsCount(EARTH_RING_ID) != 0)
				{
					htmltext = "7409-03.htm";
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		final QuestState st = checkPlayerState(killer, npc, State.STARTED);
		if (st != null && st.getState() == State.STARTED)
			
			switch (npc.getNpcId())
			{
				case 359: // Ratman Warrior
					if (st.getInt("cond") == 2)
					{
						st.giveItems(KEY_OF_FLAME_ID, 1);
						st.playSound(QuestState.SOUND_MIDDLE);
						st.set("cond", "3");
					}
					break;
				
				case 5030: // Water Seer
					if (st.getInt("cond") == 8 && st.getQuestItemsCount(SPARKLE_PEBBLE_ID) < 2)
					{
						st.giveItems(SPARKLE_PEBBLE_ID, 1);
						if (st.getQuestItemsCount(SPARKLE_PEBBLE_ID) == 2)
						{
							st.playSound(QuestState.SOUND_MIDDLE);
							st.set("cond", "9");
						}
						else
						{
							st.playSound(QuestState.SOUND_ITEMGET);
						}
					}
					break;
				
				case 21: // Red Bear
					if (st.getInt("cond") == 11)
					{
						st.giveItems(RED_SOIL_ID, 1);
						st.playSound(QuestState.SOUND_MIDDLE);
						st.set("cond", "12");
					}
			}
		return null;
	}
}