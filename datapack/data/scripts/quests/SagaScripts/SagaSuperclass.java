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
package quests.SagaScripts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;

/**
 * @author Emperorc
 */
public abstract class SagaSuperclass extends Quest
{
	private Map<L2NpcInstance, Integer> _spawnList = new ConcurrentHashMap<>();
	
	// Cryolite, Divine Stone of Wisdom
	private int[] _generalItems = new int[] {7080, 7081};
	
	protected int _classId = -1;
	protected int _prevClassId = -1;
	
	protected int[] _npcs = new int[12];
	protected int[] _items = new int[10];
	protected int[] _mobs = new int[3];
	
	protected Location[] _spawnLocs = new Location[3];
	protected String[] _texts = new String[18];
	
	/**
	 * Scripts are usually compiled one by one.
	 * We do this so that saga scripts extend the same class from memory, 
	 * instead of compiling SagaSuperclass again and again.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Quests
		new quests.Q070_SagaOfThePhoenixKnight.Q070_SagaOfThePhoenixKnight();
		new quests.Q071_SagaOfEvasTemplar.Q071_SagaOfEvasTemplar();
		new quests.Q072_SagaOfTheSwordMuse.Q072_SagaOfTheSwordMuse();
		new quests.Q073_SagaOfTheDuelist.Q073_SagaOfTheDuelist();
		new quests.Q074_SagaOfTheDreadnought.Q074_SagaOfTheDreadnought();
		new quests.Q075_SagaOfTheTitan.Q075_SagaOfTheTitan();
		new quests.Q076_SagaOfTheGrandKhavatari.Q076_SagaOfTheGrandKhavatari();
		new quests.Q077_SagaOfTheDominator.Q077_SagaOfTheDominator();
		new quests.Q078_SagaOfTheDoomcryer.Q078_SagaOfTheDoomcryer();
		new quests.Q079_SagaOfTheAdventurer.Q079_SagaOfTheAdventurer();
		new quests.Q080_SagaOfTheWindRider.Q080_SagaOfTheWindRider();
		new quests.Q081_SagaOfTheGhostHunter.Q081_SagaOfTheGhostHunter();
		new quests.Q082_SagaOfTheSagittarius.Q082_SagaOfTheSagittarius();
		new quests.Q083_SagaOfTheMoonlightSentinel.Q083_SagaOfTheMoonlightSentinel();
		new quests.Q084_SagaOfTheGhostSentinel.Q084_SagaOfTheGhostSentinel();
		new quests.Q085_SagaOfTheCardinal.Q085_SagaOfTheCardinal();
		new quests.Q086_SagaOfTheHierophant.Q086_SagaOfTheHierophant();
		new quests.Q087_SagaOfEvasSaint.Q087_SagaOfEvasSaint();
		new quests.Q088_SagaOfTheArchmage.Q088_SagaOfTheArchmage();
		new quests.Q089_SagaOfTheMysticMuse.Q089_SagaOfTheMysticMuse();
		new quests.Q090_SagaOfTheStormScreamer.Q090_SagaOfTheStormScreamer();
		new quests.Q091_SagaOfTheArcanaLord.Q091_SagaOfTheArcanaLord();
		new quests.Q092_SagaOfTheElementalMaster.Q092_SagaOfTheElementalMaster();
		new quests.Q093_SagaOfTheSpectralMaster.Q093_SagaOfTheSpectralMaster();
		new quests.Q094_SagaOfTheSoultaker.Q094_SagaOfTheSoultaker();
		new quests.Q095_SagaOfTheHellKnight.Q095_SagaOfTheHellKnight();
		new quests.Q096_SagaOfTheSpectralDancer.Q096_SagaOfTheSpectralDancer();
		new quests.Q097_SagaOfTheShillienTemplar.Q097_SagaOfTheShillienTemplar();
		new quests.Q098_SagaOfTheShillienSaint.Q098_SagaOfTheShillienSaint();
		new quests.Q099_SagaOfTheFortuneSeeker.Q099_SagaOfTheFortuneSeeker();
		new quests.Q100_SagaOfTheMaestro.Q100_SagaOfTheMaestro();
		
		// Misc
		new SagaOnKill();
	}
	
	public SagaSuperclass(int questId, String name, String descr)
	{
		super(questId, name, descr);
	}
	
	/**
	 * This function is called by subclasses in order to add their own NPCs.
	 */
	protected void registerNPCs()
	{
		addStartNpc(_npcs[0]);
		addAttackId(_mobs[2]);
		addFirstTalkId(_npcs[4]);
		
		for (int id : _npcs)
		{
			addTalkId(id);
		}
		
		for (int id : _mobs)
		{
			addKillId(id);
		}
		
		this.questItemIds = _items;
	}
	
	private void animateCast(L2NpcInstance npc, L2Character target, int skillId, int skillLevel)
	{
		target.broadcastPacket(new MagicSkillUse(target, target, skillId, skillLevel, 6000, 1));
		target.broadcastPacket(new MagicSkillUse(npc, npc, skillId, skillLevel, 6000, 1));
	}
	
	private void addSpawn(QuestState st, L2NpcInstance npc)
	{
		_spawnList.put(npc, st.getPlayer().getObjectId());
	}
	
	private void deleteSpawn(QuestState st, L2NpcInstance npc)
	{
		if (_spawnList.containsKey(npc))
		{
			_spawnList.remove(npc);
			npc.deleteMe();
		}
	}
	
	private QuestState findRightState(L2NpcInstance npc)
	{
		QuestState st = null;
		if (_spawnList.containsKey(npc))
		{
			L2PcInstance player = L2World.getInstance().getPlayer(_spawnList.get(npc));
			if (player != null && player.getClassId().getId() == _prevClassId)
			{
				st = player.getQuestState(getName());
			}
		}
		return st;
	}
	
	protected void autoChat(L2NpcInstance npc, String text)
	{
		npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), text));
	}
	
	protected void giveHallishaMark(QuestState st)
	{
		if (st.getInt("spawned") == 0)
		{
			st.giveItems(_items[1], 1);
			
			if (st.getQuestItemsCount(_items[1]) >= 700)
			{
				st.takeItems(_items[1], 20);
				int x = st.getPlayer().getX();
				int y = st.getPlayer().getY();
				int z = st.getPlayer().getZ();
				L2NpcInstance archon = st.addSpawn(_mobs[1], x, y, z);
				addSpawn(st, archon);
				st.set("spawned", "1");
				st.startQuestTimer("Archon Hellisha has despawned", 600000, archon);
				autoChat(archon, _texts[13].replace("PLAYERNAME", st.getPlayer().getName()));
				((L2Attackable) archon).addDamageHate(st.getPlayer(), 0, 99999);
				archon.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, st.getPlayer(), null);
			}
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		
		if (player.getClassId().getId() != _prevClassId)
		{
			return null;
		}
		
		String htmlText = "";
		
		if (event.equalsIgnoreCase("accept"))
		{
			st.set("cond", "1");
			st.setState(State.STARTED);
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(_items[8], 1);
			htmlText = "0-03.htm";
		}
		else if (event.equalsIgnoreCase("0-1"))
		{
			if (player.getLevel() < 76)
			{
				htmlText = "0-02.htm";
				st.exitQuest(true);
			}
			else
			{
				htmlText = "0-05.htm";
			}
		}
		else if (event.equalsIgnoreCase("0-2"))
		{
			if (player.getLevel() >= 76)
			{
				htmlText = "0-07.htm";
				st.takeItems(_items[8], -1);
				st.addExpAndSp(2299404, 0);
				st.giveItems(57, 5000000);
				st.giveItems(6622, 1);
				player.setClassId(_classId);
				if (!player.isSubClassActive() && player.getBaseClass() == _prevClassId)
				{
					player.setBaseClass(_classId);
				}
				player.broadcastUserInfo();
				animateCast(npc, player, 4339, 1);
				st.exitQuest(false);
			}
			else
			{
				st.takeItems(_items[8], -1);
				st.playSound(QuestState.SOUND_MIDDLE);
				st.set("cond", "20");
				htmlText = "0-08.htm";
			}
		}
		else if (event.equalsIgnoreCase("1-3"))
		{
			st.set("cond", "3");
			htmlText = "1-05.htm";
		}
		else if (event.equalsIgnoreCase("1-4"))
		{
			if (st.getQuestItemsCount(_generalItems[0]) > 0 && (_items[9] == 0 || st.getQuestItemsCount(_items[9]) > 0))
			{
				st.set("cond", "4");
				st.takeItems(_generalItems[0], 1);
				if (_items[9] != 0)
				{
					st.takeItems(_items[9], 1);
				}
				st.giveItems(_items[0], 1);
				htmlText = "1-06.htm";
			}
			else
			{
				htmlText = "1-02.htm";
			}
		}
		else if (event.equalsIgnoreCase("2-1"))
		{
			st.set("cond", "2");
			htmlText = "2-05.htm";
		}
		else if (event.equalsIgnoreCase("2-2"))
		{
			st.set("cond", "5");
			st.takeItems(_items[0], 1);
			st.giveItems(_items[2], 1);
			htmlText = "2-06.htm";
		}
		else if (event.equalsIgnoreCase("3-5"))
		{
			htmlText = "3-07.htm";
		}
		else if (event.equalsIgnoreCase("3-6"))
		{
			st.set("cond", "11");
			htmlText = "3-02.htm";
		}
		else if (event.equalsIgnoreCase("3-7"))
		{
			st.set("cond", "12");
			htmlText = "3-03.htm";
		}
		else if (event.equalsIgnoreCase("3-8"))
		{
			if (st.getQuestItemsCount(_generalItems[1]) > 0)
			{
				st.set("cond", "13");
				st.takeItems(_generalItems[1], 1);
				st.giveItems(_items[5], 1);
				htmlText = "3-08.htm";
			}
			else
			{
				htmlText = "3-04.htm";
			}
		}
		else if (event.equalsIgnoreCase("4-1"))
		{
			htmlText = "4-10.htm";
		}
		else if (event.equalsIgnoreCase("4-2"))
		{
			st.giveItems(_items[7], 1);
			st.set("cond", "18");
			st.playSound(QuestState.SOUND_MIDDLE);
			htmlText = "4-11.htm";
		}
		else if (event.equalsIgnoreCase("4-3"))
		{
			st.giveItems(_items[7], 1);
			st.set("cond", "18");
			autoChat(npc, _texts[13].replace("PLAYERNAME", player.getName()));
			st.set("Quest0", "0");
			cancelQuestTimer("Mob_2 has despawned", npc, player);
			st.playSound(QuestState.SOUND_MIDDLE);
			deleteSpawn(st, npc);
			return null;
		}
		else if (event.equalsIgnoreCase("5-1"))
		{
			st.set("cond", "6");
			st.takeItems(_items[2], 1);
			animateCast(npc, player, 4546, 1);
			st.playSound(QuestState.SOUND_MIDDLE);
			htmlText = "5-02.htm";
		}
		else if (event.equalsIgnoreCase("6-1"))
		{
			st.set("cond", "8");
			st.takeItems(_items[3], 1);
			animateCast(npc, player, 4546, 1);
			st.playSound(QuestState.SOUND_MIDDLE);
			htmlText = "6-03.htm";
		}
		else if (event.equalsIgnoreCase("7-1"))
		{
			if (st.getInt("spawned") == 1)
			{
				htmlText = "7-03.htm";
			}
			else if (st.getInt("spawned") == 0)
			{
				Location loc = _spawnLocs[0];
				L2NpcInstance mob = st.addSpawn(_mobs[0], loc.getX(), loc.getY(), loc.getZ());
				st.set("Mob_1", String.valueOf(mob.getObjectId()));
				st.set("spawned", "1");
				st.startQuestTimer("Mob_1 Timer 1", 500, mob);
				st.startQuestTimer("Mob_1 has despawned", 300000, mob);
				addSpawn(st, mob);
				htmlText = "7-02.htm";
			}
			else
			{
				htmlText = "7-04.htm";
			}
		}
		else if (event.equalsIgnoreCase("7-2"))
		{
			st.set("cond", "10");
			st.takeItems(_items[4], 1);
			animateCast(npc, player, 4546, 1);
			st.playSound(QuestState.SOUND_MIDDLE);
			htmlText = "7-06.htm";
		}
		else if (event.equalsIgnoreCase("8-1"))
		{
			st.set("cond", "14");
			st.takeItems(_items[5], 1);
			animateCast(npc, player, 4546, 1);
			st.playSound(QuestState.SOUND_MIDDLE);
			htmlText = "8-02.htm";
		}
		else if (event.equalsIgnoreCase("9-1"))
		{
			st.set("cond", "17");
			st.takeItems(_items[6], 1);
			animateCast(npc, player, 4546, 1);
			st.playSound(QuestState.SOUND_MIDDLE);
			htmlText = "9-03.htm";
		}
		else if (event.equalsIgnoreCase("10-1"))
		{
			if (st.getInt("Quest0") == 0)
			{
				Location loc = _spawnLocs[1];
				L2NpcInstance mob1 = st.addSpawn(_mobs[2], loc.getX(), loc.getY(), loc.getZ());
				loc = _spawnLocs[2];
				L2NpcInstance mob2 = st.addSpawn(_npcs[4], loc.getX(), loc.getY(), loc.getZ());
				
				addSpawn(st, mob2);
				addSpawn(st, mob1);
				
				st.set("Mob_3", String.valueOf(mob2.getObjectId()));
				st.set("Mob_2", String.valueOf(mob1.getObjectId()));
				st.set("Quest0", "1");
				st.set("Quest", "45");
				st.startQuestTimer("Mob_3 Timer 1", 500, mob2);
				st.startQuestTimer("Mob_3 has despawned", 59000, mob2);
				st.startQuestTimer("Mob_2 Timer 1", 500, mob1);
				st.startQuestTimer("Mob_2 has despawned", 60000, mob1);
				htmlText = "10-02.htm";
			}
			else if (st.getInt("Quest") == 45)
			{
				htmlText = "10-03.htm";
			}
			else
			{
				htmlText = "10-04.htm";
			}
		}
		else if (event.equalsIgnoreCase("10-2"))
		{
			st.set("cond", "19");
			st.takeItems(_items[7], 1);
			animateCast(npc, player, 4546, 1);
			st.playSound(QuestState.SOUND_MIDDLE);
			htmlText = "10-06.htm";
		}
		else if (event.equalsIgnoreCase("11-9"))
		{
			st.set("cond", "15");
			htmlText = "11-03.htm";
		}
		else if (event.equalsIgnoreCase("Mob_1 Timer 1"))
		{
			autoChat(npc, _texts[0].replace("PLAYERNAME", player.getName()));
			return null;
		}
		else if (event.equalsIgnoreCase("Mob_1 has despawned"))
		{
			autoChat(npc, _texts[1].replace("PLAYERNAME", player.getName()));
			deleteSpawn(st, npc);
			st.set("spawned", "0");
			return null;
		}
		else if (event.equalsIgnoreCase("Archon of Hellisha has despawned"))
		{
			autoChat(npc, _texts[6].replace("PLAYERNAME", player.getName()));
			deleteSpawn(st, npc);
			st.set("spawned", "0");
			return null;
		}
		else if (event.equalsIgnoreCase("Mob_3 Timer 1"))
		{
			final int objectId = st.getInt("Mob_2");
			if (npc.getKnownList().getKnownObjects().containsKey(objectId))
			{
				L2Object obj = npc.getKnownList().getKnownObjects().get(objectId);
				if (!(obj instanceof L2NpcInstance))
				{
					return null;
				}
				
				L2NpcInstance mob = (L2NpcInstance) obj;
				
				if (npc instanceof L2Attackable)
				{
					((L2Attackable) npc).addDamageHate(mob, 0, 99999);
				}
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, mob, null);
				mob.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, npc, null);
				autoChat(npc, _texts[14].replace("PLAYERNAME", player.getName()));
			}
			else
			{
				st.startQuestTimer("Mob_3 Timer 1", 500, npc);
			}
			return null;
		}
		else if (event.equalsIgnoreCase("Mob_3 has despawned"))
		{
			autoChat(npc, _texts[15].replace("PLAYERNAME", player.getName()));
			st.set("Quest0", "2");
			npc.doDie(npc);
			deleteSpawn(st, npc);
			return null;
		}
		else if (event.equalsIgnoreCase("Mob_2 Timer 1"))
		{
			autoChat(npc, _texts[7].replace("PLAYERNAME", player.getName()));
			st.startQuestTimer("Mob_2 Timer 2", 1500, npc);
			if (st.getInt("Quest") == 45)
			{
				st.set("Quest", "0");
			}
			return null;
		}
		else if (event.equalsIgnoreCase("Mob_2 Timer 2"))
		{
			autoChat(npc, _texts[8].replace("PLAYERNAME", player.getName()));
			st.startQuestTimer("Mob_2 Timer 3", 10000, npc);
			return null;
		}
		else if (event.equalsIgnoreCase("Mob_2 Timer 3"))
		{
			if (st.getInt("Quest0") == 0)
			{
				st.startQuestTimer("Mob_2 Timer 3", 13000, npc);
				if (st.getRandom(2) == 0)
				{
					autoChat(npc, _texts[9].replace("PLAYERNAME", player.getName()));
				}
				else
				{
					autoChat(npc, _texts[10].replace("PLAYERNAME", player.getName()));
				}
			}
			return null;
		}
		else if (event.equalsIgnoreCase("Mob_2 has despawned"))
		{
			st.set("Quest", String.valueOf(st.getInt("Quest") + 1));
			if (st.getInt("Quest0") == 1 || st.getInt("Quest0") == 2 || st.getInt("Quest") > 3)
			{
				st.set("Quest0", "0");
				if (st.getInt("Quest0") == 1)
				{
					autoChat(npc, _texts[11].replace("PLAYERNAME", player.getName()));
				}
				else
				{
					autoChat(npc, _texts[12].replace("PLAYERNAME", player.getName()));
				}
				npc.doDie(npc);
				deleteSpawn(st, npc);
			}
			else
			{
				st.startQuestTimer("Mob_2 has despawned", 1000, npc);
			}
			return null;
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
		
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		
		if (st.getState() == State.COMPLETED && npcId == _npcs[0])
		{
			htmlText = getAlreadyCompletedMsg();
			return htmlText;
		}
		
		if (player.getClassId().getId() != _prevClassId)
		{
			return htmlText;
		}
		
		if (cond == 0)
		{
			if (npcId == _npcs[0])
			{
				htmlText = "0-01.htm";
			}
		}
		else if (cond == 1)
		{
			if (npcId == _npcs[0])
			{
				htmlText = "0-04.htm";
			}
			else if (npcId == _npcs[2])
			{
				htmlText = "2-01.htm";
			}
		}
		else if (cond == 2)
		{
			if (npcId == _npcs[2])
			{
				htmlText = "2-02.htm";
			}
			else if (npcId == _npcs[1])
			{
				htmlText = "1-01.htm";
			}
		}
		else if (cond == 3)
		{
			if (npcId == _npcs[1])
			{
				if (st.getQuestItemsCount(_generalItems[0]) > 0)
				{
					if (_items[9] == 0)
					{
						htmlText = "1-03.htm";
					}
					else if (st.getQuestItemsCount(_items[9]) > 0)
					{
						htmlText = "1-03.htm";
					}
					else
					{
						htmlText = "1-02.htm";
					}
				}
				else
				{
					htmlText = "1-02.htm";
				}
			}
		}
		else if (cond == 4)
		{
			if (npcId == _npcs[1])
			{
				htmlText = "1-04.htm";
			}
			else if (npcId == _npcs[2])
			{
				htmlText = "2-03.htm";
			}
		}
		else if (cond == 5)
		{
			if (npcId == _npcs[2])
			{
				htmlText = "2-04.htm";
			}
			else if (npcId == _npcs[5])
			{
				htmlText = "5-01.htm";
			}
		}
		else if (cond == 6)
		{
			if (npcId == _npcs[5])
			{
				htmlText = "5-03.htm";
			}
			else if (npcId == _npcs[6])
			{
				htmlText = "6-01.htm";
			}
		}
		else if (cond == 7)
		{
			if (npcId == _npcs[6])
			{
				htmlText = "6-02.htm";
			}
		}
		else if (cond == 8)
		{
			if (npcId == _npcs[6])
			{
				htmlText = "6-04.htm";
			}
			else if (npcId == _npcs[7])
			{
				htmlText = "7-01.htm";
			}
		}
		else if (cond == 9)
		{
			if (npcId == _npcs[7])
			{
				htmlText = "7-05.htm";
			}
		}
		else if (cond == 10)
		{
			if (npcId == _npcs[7])
			{
				htmlText = "7-07.htm";
			}
			else if (npcId == _npcs[3])
			{
				htmlText = "3-01.htm";
			}
		}
		else if (cond == 11 || cond == 12)
		{
			if (npcId == _npcs[3])
			{
				if (st.getQuestItemsCount(_generalItems[1]) > 0)
				{
					htmlText = "3-05.htm";
				}
				else
				{
					htmlText = "3-04.htm";
				}
			}
		}
		else if (cond == 13)
		{
			if (npcId == _npcs[3])
			{
				htmlText = "3-06.htm";
			}
			else if (npcId == _npcs[8])
			{
				htmlText = "8-01.htm";
			}
		}
		else if (cond == 14)
		{
			if (npcId == _npcs[8])
			{
				htmlText = "8-03.htm";
			}
			else if (npcId == _npcs[11])
			{
				htmlText = "11-01.htm";
			}
		}
		else if (cond == 15)
		{
			if (npcId == _npcs[11])
			{
				htmlText = "11-02.htm";
			}
			else if (npcId == _npcs[9])
			{
				htmlText = "9-01.htm";
			}
		}
		else if (cond == 16)
		{
			if (npcId == _npcs[9])
			{
				htmlText = "9-02.htm";
			}
		}
		else if (cond == 17)
		{
			if (npcId == _npcs[9])
			{
				htmlText = "9-04.htm";
			}
			else if (npcId == _npcs[10])
			{
				htmlText = "10-01.htm";
			}
		}
		else if (cond == 18)
		{
			if (npcId == _npcs[10])
			{
				htmlText = "10-05.htm";
			}
		}
		else if (cond == 19)
		{
			if (npcId == _npcs[10])
			{
				htmlText = "10-07.htm";
			}
			else if (npcId == _npcs[0])
			{
				htmlText = "0-06.htm";
			}
		}
		else if (cond == 20)
		{
			if (npcId == _npcs[0])
			{
				if (player.getLevel() >= 76)
				{
					htmlText = "0-07.htm";
					st.addExpAndSp(2299404, 0);
					st.giveItems(57, 5000000);
					st.giveItems(6622, 1);
					player.setClassId(_classId);
					if (!player.isSubClassActive() && player.getBaseClass() == _prevClassId)
					{
						player.setBaseClass(_classId);
					}
					player.broadcastUserInfo();
					animateCast(npc, player, 4339, 1);
					st.exitQuest(false);
				}
				else
				{
					htmlText = "0-10.htm";
				}
			}
		}
		return htmlText;
	}
	
	@Override
	public String onFirstTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = "";
		QuestState st = player.getQuestState(getName());
		int npcId = npc.getNpcId();
		if (st != null)
		{
			int cond = st.getInt("cond");
			if (npcId == _npcs[4])
			{
				if (cond == 17)
				{
					QuestState st2 = findRightState(npc);
					if (st2 != null)
					{
						player.setLastQuestNpcObject(npc.getObjectId());
						if (st == st2)
						{
							if (st.getInt("Tab") == 1)
							{
								if (st.getInt("Quest0") == 0)
								{
									htmlText = "4-04.htm";
								}
								else if (st.getInt("Quest0") == 1)
								{
									htmlText = "4-06.htm";
								}
							}
							else
							{
								if (st.getInt("Quest0") == 0)
								{
									htmlText = "4-01.htm";
								}
								else if (st.getInt("Quest0") == 1)
								{
									htmlText = "4-03.htm";
								}
							}
						}
						else
						{
							if (st.getInt("Tab") == 1)
							{
								if (st.getInt("Quest0") == 0)
								{
									htmlText = "4-05.htm";
								}
								else if (st.getInt("Quest0") == 1)
								{
									htmlText = "4-07.htm";
								}
							}
							else
							{
								if (st.getInt("Quest0") == 0)
								{
									htmlText = "4-02.htm";
								}
							}
						}
					}
				}
				else if (cond == 18)
				{
					htmlText = "4-08.htm";
				}
			}
		}
		if (htmlText.isEmpty())
		{
			npc.showChatWindow(player);
		}
		return htmlText;
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance player, int damage, boolean isPet)
	{
		QuestState st = findRightState(npc);
		if (st == null)
		{
			return super.onAttack(npc, player, damage, isPet);
		}
		
		final int cond = st.getInt("cond");
		final int npcId = npc.getNpcId();
		
		if (npcId == _mobs[2] && player == st.getPlayer() && cond == 17)
		{
			st.set("Quest0", String.valueOf(st.getInt("Quest0") + 1));
			if (st.getInt("Quest0") == 1)
			{
				autoChat(npc, _texts[16].replace("PLAYERNAME", player.getName()));
			}
			
			if (st.getInt("Quest0") > 15)
			{
				st.set("Quest0", "1");
				autoChat(npc, _texts[17].replace("PLAYERNAME", player.getName()));
				cancelQuestTimer("Mob_3 has despawned", npc, st.getPlayer());
				st.set("Tab", "1");
				deleteSpawn(st, npc);
			}
		}
		return super.onAttack(npc, player, damage, isPet);
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
	{
		QuestState st = findRightState(npc);
		if (st == null)
		{
			return super.onKill(npc, player, isPet);
		}
		
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		
		if (npcId == _mobs[0] && cond == 8)
		{
			if (player == st.getPlayer())
			{
				autoChat(npc, _texts[12].replace("PLAYERNAME", player.getName()));
				st.giveItems(_items[4], 1);
				st.set("cond", "9");
				st.playSound(QuestState.SOUND_MIDDLE);
			}
			cancelQuestTimer("Mob_1 has despawned", npc, st.getPlayer());
			st.set("spawned", "0");
			deleteSpawn(st, npc);
		}
		else if (npcId == _mobs[1] && cond == 15)
		{
			if (player == st.getPlayer())
			{
				autoChat(npc, _texts[4].replace("PLAYERNAME", player.getName()));
				st.giveItems(_items[6], 1);
				st.takeItems(_items[1], -1);
				st.set("cond", "16");
				st.playSound(QuestState.SOUND_MIDDLE);
			}
			else
			{
				autoChat(npc, _texts[5].replace("PLAYERNAME", player.getName()));
			}
			cancelQuestTimer("Archon Hellisha has despawned", npc, st.getPlayer());
			st.set("spawned", "0");
			deleteSpawn(st, npc);
		}
		else if (npcId == _mobs[2] && cond == 17)
		{
			if (player == st.getPlayer())
			{
				autoChat(npc, _texts[17].replace("PLAYERNAME", player.getName()));
				st.set("Tab", "1");
			}
			cancelQuestTimer("Mob_3 has despawned", npc, st.getPlayer());
			st.set("Quest0", "1");
			deleteSpawn(st, npc);
		}
		return super.onKill(npc, player, isPet);
	}
}