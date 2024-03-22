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
package ai.individual;

import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_CAST;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_FOLLOW;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestTimer;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * Valakas AI
 * @author Kerberos
 */
public class Valakas extends Quest
{
	private int i_ai0 = 0;
	private int i_ai1 = 0;
	private int i_ai2 = 0;
	private int i_ai3 = 0;
	private int i_ai4 = 0;
	private int i_quest0 = 0;
	private long i_quest1 = 0; // time to tracking valakas when was last time attacked
	private int i_quest2 = 0; // hate value for 1st player
	private int i_quest3 = 0; // hate value for 2nd player
	private int i_quest4 = 0; // hate value for 3rd player
	private L2Character c_quest2 = null; // 1st most hated target
	private L2Character c_quest3 = null; // 2nd most hated target
	private L2Character c_quest4 = null; // 3rd most hated target
	
	private static final int VALAKAS = 12899;
	
	// Valakas Status Tracking :
	private static final byte DORMANT = 0; // Valakas is spawned and no one has entered yet. Entry is unlocked
	private static final byte WAITING = 1; // Valakas is spawned and someone has entered, triggering a 30 minute window for additional people to enter
	// before he unleashes his attack. Entry is unlocked
	private static final byte FIGHTING = 2; // Valakas is engaged in battle, annihilating his foes. Entry is locked
	private static final byte DEAD = 3; // Valakas has been killed. Entry is locked
	
	private final L2BossZone _zone;
	private final Location _spawnLoc = new Location(212852, -114842, -1632);
	
	public static void main(String[] args)
	{
		// Quest class
		new Valakas();
	}
	
	// Boss: Valakas
	public Valakas()
	{
		super(-1, "valakas", "ai");
		registerNPC(VALAKAS);
		
		i_ai0 = 0;
		i_ai1 = 0;
		i_ai2 = 0;
		i_ai3 = 0;
		i_ai4 = 0;
		i_quest0 = 0;
		i_quest1 = System.currentTimeMillis();
		_zone = GrandBossManager.getInstance().getZone(_spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ());
		StatsSet info = GrandBossManager.getInstance().getStatsSet(VALAKAS);
		int status = GrandBossManager.getInstance().getBossStatus(VALAKAS);
		if (status == DEAD)
		{
			// load the unlock date and time for valakas from DB
			long temp = (info.getLong("respawn_time") - System.currentTimeMillis());
			// if valakas is locked until a certain time, mark it so and start the unlock timer
			// the unlock time has not yet expired. Mark valakas as currently locked. Setup a timer
			// to fire at the correct time (calculate the time between now and the unlock time,
			// setup a timer to fire after that many msec)
			if (temp > 0)
			{
				startQuestTimer("valakas_unlock", temp, null, null);
			}
			else
			{
				// the time has already expired while the server was offline. Immediately spawn valakas in his cave.
				// also, the status needs to be changed to DORMANT
				L2GrandBossInstance valakas = (L2GrandBossInstance) addSpawn(VALAKAS, _spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ(), 0, false, 0);
				GrandBossManager.getInstance().setBossStatus(VALAKAS, DORMANT);
				GrandBossManager.getInstance().addBoss(valakas);
				
				ThreadPoolManager.getInstance().scheduleGeneral(() -> {
					valakas.setIsInvul(true);
					valakas.decayMe();
				}, 100L);
				
				startQuestTimer("1003", 60000, valakas, null, true);
			}
		}
		else
		{
			int locX = info.getInteger("loc_x");
			int locY = info.getInteger("loc_y");
			int locZ = info.getInteger("loc_z");
			int heading = info.getInteger("heading");
			final int hp = info.getInteger("currentHP");
			final int mp = info.getInteger("currentMP");
			L2GrandBossInstance valakas = (L2GrandBossInstance) addSpawn(VALAKAS, locX, locY, locZ, heading, false, 0);
			valakas.setRunning();
			GrandBossManager.getInstance().addBoss(valakas);
			
			ThreadPoolManager.getInstance().scheduleGeneral(() -> {
				valakas.setCurrentHpMp(hp, mp);
				if (status != FIGHTING)
				{
					valakas.setXYZ(_spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ());
					valakas.setHeading(0);
					valakas.setIsInvul(true);
					valakas.decayMe();
				}
			}, 100L);
			
			startQuestTimer("1003", 60000, valakas, null, true);
			if (status == WAITING)
			{
				// Start timer to lock entry after a certain amount of minutes
				startQuestTimer("1001", (Config.VALAKAS_WAIT_TIME * 60000L), valakas, null);
			}
			else if (status == FIGHTING)
			{
				// Start repeating timer to check for inactivity
				startQuestTimer("1002", 60000, valakas, null, true);
			}
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		if (npc != null)
		{
			long temp = 0;
			if (event.equalsIgnoreCase("1001"))
			{
				npc.setShowSummonAnimation(true);
				npc.setRunning();
				npc.spawnMe();
				
				// Launch the cinematic, and tasks (regen + skill)
				startQuestTimer("spawn_1", 2000, npc, null, false); // 2000
				startQuestTimer("spawn_2", 3500, npc, null, false); // 1500
				startQuestTimer("spawn_3", 6800, npc, null, false); // 3300
				startQuestTimer("spawn_4", 9700, npc, null, false); // 2900
				startQuestTimer("spawn_5", 12400, npc, null, false); // 2700
				startQuestTimer("spawn_6", 12401, npc, null, false); // 1
				startQuestTimer("spawn_7", 15601, npc, null, false); // 3200
				startQuestTimer("spawn_8", 17001, npc, null, false); // 1400
				startQuestTimer("spawn_9", 23701, npc, null, false); // 6700 - end of cinematic
				startQuestTimer("spawn_10", 29401, npc, null, false); // 5700 - AI + unlock
			}
			else if (event.equalsIgnoreCase("1002"))
			{
				int lvl = 0;
				int sk_4691 = 0;
				L2Effect[] effects = npc.getAllEffects();
				if ((effects != null) && (effects.length != 0))
				{
					for (L2Effect e : effects)
					{
						if (e.getSkill().getId() == 4629)
						{
							sk_4691 = 1;
							lvl = e.getSkill().getLevel();
							break;
						}
					}
				}
				
				if (GrandBossManager.getInstance().getBossStatus(VALAKAS) == FIGHTING)
				{
					temp = (System.currentTimeMillis() - i_quest1);
					if (temp > 1800000)
					{
						npc.getAI().setIntention(AI_INTENTION_IDLE);
						npc.setXYZ(_spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ());
						npc.setHeading(0);
						npc.setIsInvul(true);
						npc.decayMe();
						GrandBossManager.getInstance().setBossStatus(VALAKAS, DORMANT);
						npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp());
						_zone.oustAllPlayers();
						cancelQuestTimer("1002", npc, null);
						i_quest2 = 0;
						i_quest3 = 0;
						i_quest4 = 0;
					}
					else if (!_zone.isInsideZone(212852, -114842, npc.getZ()))
					{
						npc.teleToLocation(_spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ());
					}
				}
				else if (npc.getCurrentHp() > ((npc.getMaxHp() * 1) / 4))
				{
					if ((sk_4691 == 0) || ((sk_4691 == 1) && (lvl != 4)))
					{
						npc.setTarget(npc);
						npc.doCast(SkillTable.getInstance().getInfo(4691, 4));
					}
				}
				else if (npc.getCurrentHp() > ((npc.getMaxHp() * 2) / 4.0))
				{
					if ((sk_4691 == 0) || ((sk_4691 == 1) && (lvl != 3)))
					{
						npc.setTarget(npc);
						npc.doCast(SkillTable.getInstance().getInfo(4691, 3));
					}
				}
				else if (npc.getCurrentHp() > ((npc.getMaxHp() * 3) / 4.0))
				{
					if ((sk_4691 == 0) || ((sk_4691 == 1) && (lvl != 2)))
					{
						npc.setTarget(npc);
						npc.doCast(SkillTable.getInstance().getInfo(4691, 2));
					}
				}
				else if ((sk_4691 == 0) || ((sk_4691 == 1) && (lvl != 1)))
				{
					npc.setTarget(npc);
					npc.doCast(SkillTable.getInstance().getInfo(4691, 1));
				}
			}
			else if (event.equalsIgnoreCase("1003"))
			{
				if (!npc.isInvul())
				{
					getRandomSkill(npc);
				}
				else
				{
					npc.getAI().setIntention(AI_INTENTION_IDLE);
				}
			}
			else if (event.equalsIgnoreCase("spawn_1"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1800, 180, -1, 1500, 10000, 0, 0, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_2"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1300, 180, -5, 3000, 10000, 0, -5, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_3"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 500, 180, -8, 600, 10000, 0, 60, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_4"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 800, 180, -8, 2700, 10000, 0, 30, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_5"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 200, 250, 70, 0, 10000, 30, 80, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_6"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 250, 70, 2500, 10000, 30, 80, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_7"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 700, 150, 30, 0, 10000, -10, 60, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_8"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1200, 150, 20, 2900, 10000, -10, 30, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_9"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 750, 170, -10, 3400, 4000, 10, -15, 1, 0));
			}
			else if (event.equalsIgnoreCase("spawn_10"))
			{
				GrandBossManager.getInstance().setBossStatus(VALAKAS, FIGHTING);
				i_quest1 = System.currentTimeMillis();
				startQuestTimer("1002", 60000, npc, null, true);
				npc.setIsInvul(false);
				getRandomSkill(npc);
			}
			else if (event.equalsIgnoreCase("die_1"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2000, 130, -1, 0, 10000, 0, 0, 1, 1));
			}
			else if (event.equalsIgnoreCase("die_2"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 210, -5, 3000, 10000, -13, 0, 1, 1));
			}
			else if (event.equalsIgnoreCase("die_3"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1300, 200, -8, 3000, 10000, 0, 15, 1, 1));
			}
			else if (event.equalsIgnoreCase("die_4"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1000, 190, 0, 500, 10000, 0, 10, 1, 1));
			}
			else if (event.equalsIgnoreCase("die_5"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1700, 120, 0, 2500, 10000, 12, 40, 1, 1));
			}
			else if (event.equalsIgnoreCase("die_6"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1700, 20, 0, 700, 10000, 10, 10, 1, 1));
			}
			else if (event.equalsIgnoreCase("die_7"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1700, 10, 0, 1000, 10000, 20, 70, 1, 1));
			}
			else if (event.equalsIgnoreCase("die_8"))
			{
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1700, 10, 0, 300, 250, 20, -20, 1, 1));
				
				addSpawn(8759, _spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ(), 0, false, 900000);
				int radius = 1500;
				for (int i = 0; i < 20; i++)
				{
					int x = (int) (radius * Math.cos(i * .331)); // .331~2pi/19
					int y = (int) (radius * Math.sin(i * .331));
					addSpawn(8759, 212852 + x, -114842 + y, -1632, 0, false, 900000);
				}
				cancelQuestTimer("1002", npc, null);
				startQuestTimer("remove_players", 900000, null, null);
			}
		}
		else
		{
			if (event.equalsIgnoreCase("valakas_unlock"))
			{
				L2GrandBossInstance valakas = (L2GrandBossInstance) addSpawn(VALAKAS, -105200, -253104, -15264, 32768, false, 0);
				GrandBossManager.getInstance().addBoss(valakas);
				GrandBossManager.getInstance().setBossStatus(VALAKAS, DORMANT);
			}
			else if (event.equalsIgnoreCase("remove_players"))
			{
				_zone.oustAllPlayers();
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (npc.isInvul())
		{
			return null;
		}
		
		i_quest1 = System.currentTimeMillis();
		
		if (GrandBossManager.getInstance().getBossStatus(VALAKAS) != FIGHTING && !Config.VALAKAS_ALLOW_UNPROVOKED)
		{
			attacker.teleToLocation(150037, -57255, -2976, true);
			return null;
		}
		
		if (attacker.getMountType() == 1)
		{
			int sk_4258 = 0;
			L2Effect[] effects = attacker.getAllEffects();
			if ((effects != null) && (effects.length != 0))
			{
				for (L2Effect e : effects)
				{
					if (e.getSkill().getId() == 4258)
					{
						sk_4258 = 1;
						break;
					}
				}
			}
			
			if (sk_4258 == 0)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4258, 1));
			}
		}
		
		if (attacker.getZ() < (npc.getZ() + 200))
		{
			if (i_ai2 == 0)
			{
				i_ai1 = (i_ai1 + damage);
			}
			if (i_quest0 == 0)
			{
				i_ai4 = (i_ai4 + damage);
			}
			if (i_quest0 == 0)
			{
				i_ai3 = (i_ai3 + damage);
			}
			else if (i_ai2 == 0)
			{
				i_ai0 = (i_ai0 + damage);
			}
			
			if (i_quest0 == 0)
			{
				if ((((i_ai4 / npc.getMaxHp()) * 100)) > 1)
				{
					if (i_ai3 > (i_ai4 - i_ai3))
					{
						i_ai3 = 0;
						i_ai4 = 0;
						npc.setTarget(npc);
						npc.doCast(SkillTable.getInstance().getInfo(4687, 1));
						i_quest0 = 1;
					}
				}
			}
		}
		
		int i1 = 0;
		if (attacker.getAI().getIntention() != AI_INTENTION_CAST)
		{
			if (attacker == c_quest2)
			{
				if (((damage * 1000) + 1000) > i_quest2)
				{
					i_quest2 = ((damage * 1000) + Rnd.get(3000));
				}
			}
			else if (attacker == c_quest3)
			{
				if (((damage * 1000) + 1000) > i_quest3)
				{
					i_quest3 = ((damage * 1000) + Rnd.get(3000));
				}
			}
			else if (attacker == c_quest4)
			{
				if (((damage * 1000) + 1000) > i_quest4)
				{
					i_quest4 = ((damage * 1000) + Rnd.get(3000));
				}
			}
			else if (i_quest2 > i_quest3)
			{
				i1 = 3;
			}
			else if (i_quest2 == i_quest3)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 2;
				}
				else
				{
					i1 = 3;
				}
			}
			else if (i_quest2 < i_quest3)
			{
				i1 = 2;
			}
			
			if (i1 == 2)
			{
				if (i_quest2 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest2 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 2;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest2 < i_quest4)
				{
					i1 = 2;
				}
			}
			else if (i1 == 3)
			{
				if (i_quest3 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest3 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 3;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest3 < i_quest4)
				{
					i1 = 3;
				}
			}
			
			if (i1 == 2)
			{
				i_quest2 = (damage * 1000) + Rnd.get(3000);
				c_quest2 = attacker;
			}
			else if (i1 == 3)
			{
				i_quest3 = (damage * 1000) + Rnd.get(3000);
				c_quest3 = attacker;
			}
			else if (i1 == 4)
			{
				i_quest4 = (damage * 1000) + Rnd.get(3000);
				c_quest4 = attacker;
			}
		}
		else if (npc.getCurrentHp() > ((npc.getMaxHp() * 1) / 4))
		{
			if (attacker == c_quest2)
			{
				if ((((damage / 30) * 1000) + 1000) > i_quest2)
				{
					i_quest2 = (((damage / 30) * 1000) + Rnd.get(3000));
				}
			}
			else if (attacker == c_quest3)
			{
				if ((((damage / 30) * 1000) + 1000) > i_quest3)
				{
					i_quest3 = (((damage / 30) * 1000) + Rnd.get(3000));
				}
			}
			else if (attacker == c_quest4)
			{
				if ((((damage / 30) * 1000) + 1000) > i_quest4)
				{
					i_quest4 = (((damage / 30) * 1000) + Rnd.get(3000));
				}
			}
			else if (i_quest2 > i_quest3)
			{
				i1 = 3;
			}
			else if (i_quest2 == i_quest3)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 2;
				}
				else
				{
					i1 = 3;
				}
			}
			else if (i_quest2 < i_quest3)
			{
				i1 = 2;
			}
			
			if (i1 == 2)
			{
				if (i_quest2 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest2 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 2;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest2 < i_quest4)
				{
					i1 = 2;
				}
			}
			else if (i1 == 3)
			{
				if (i_quest3 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest3 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 3;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest3 < i_quest4)
				{
					i1 = 3;
				}
			}
			
			if (i1 == 2)
			{
				i_quest2 = (((damage / 30) * 1000) + Rnd.get(3000));
				c_quest2 = attacker;
			}
			else if (i1 == 3)
			{
				i_quest3 = (((damage / 30) * 1000) + Rnd.get(3000));
				c_quest3 = attacker;
			}
			else if (i1 == 4)
			{
				i_quest4 = (((damage / 30) * 1000) + Rnd.get(3000));
				c_quest4 = attacker;
			}
		}
		else if (npc.getCurrentHp() > ((npc.getMaxHp() * 2) / 4))
		{
			if (attacker == c_quest2)
			{
				if ((((damage / 50) * 1000) + 1000) > i_quest2)
				{
					i_quest2 = (((damage / 50) * 1000) + Rnd.get(3000));
				}
			}
			else if (attacker == c_quest3)
			{
				if ((((damage / 50) * 1000) + 1000) > i_quest3)
				{
					i_quest3 = (((damage / 50) * 1000) + Rnd.get(3000));
				}
			}
			else if (attacker == c_quest4)
			{
				if ((((damage / 50) * 1000) + 1000) > i_quest4)
				{
					i_quest4 = (((damage / 50) * 1000) + Rnd.get(3000));
				}
			}
			else if (i_quest2 > i_quest3)
			{
				i1 = 3;
			}
			else if (i_quest2 == i_quest3)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 2;
				}
				else
				{
					i1 = 3;
				}
			}
			else if (i_quest2 < i_quest3)
			{
				i1 = 2;
			}
			
			if (i1 == 2)
			{
				if (i_quest2 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest2 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 2;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest2 < i_quest4)
				{
					i1 = 2;
				}
			}
			else if (i1 == 3)
			{
				if (i_quest3 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest3 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 3;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest3 < i_quest4)
				{
					i1 = 3;
				}
			}
			
			if (i1 == 2)
			{
				i_quest2 = (((damage / 50) * 1000) + Rnd.get(3000));
				c_quest2 = attacker;
			}
			else if (i1 == 3)
			{
				i_quest3 = (((damage / 50) * 1000) + Rnd.get(3000));
				c_quest3 = attacker;
			}
			else if (i1 == 4)
			{
				i_quest4 = (((damage / 50) * 1000) + Rnd.get(3000));
				c_quest4 = attacker;
			}
		}
		else if (npc.getCurrentHp() > ((npc.getMaxHp() * 3) / 4.0))
		{
			if (attacker == c_quest2)
			{
				if ((((damage / 100) * 1000) + 1000) > i_quest2)
				{
					i_quest2 = (((damage / 100) * 1000) + Rnd.get(3000));
				}
			}
			else if (attacker == c_quest3)
			{
				if ((((damage / 100) * 1000) + 1000) > i_quest3)
				{
					i_quest3 = (((damage / 100) * 1000) + Rnd.get(3000));
				}
			}
			else if (attacker == c_quest4)
			{
				if ((((damage / 100) * 1000) + 1000) > i_quest4)
				{
					i_quest4 = (((damage / 100) * 1000) + Rnd.get(3000));
				}
			}
			else if (i_quest2 > i_quest3)
			{
				i1 = 3;
			}
			else if (i_quest2 == i_quest3)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 2;
				}
				else
				{
					i1 = 3;
				}
			}
			else if (i_quest2 < i_quest3)
			{
				i1 = 2;
			}
			
			if (i1 == 2)
			{
				if (i_quest2 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest2 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 2;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest2 < i_quest4)
				{
					i1 = 2;
				}
			}
			else if (i1 == 3)
			{
				if (i_quest3 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest3 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 3;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest3 < i_quest4)
				{
					i1 = 3;
				}
				
				if (i1 == 2)
				{
					i_quest2 = (((damage / 100) * 1000) + Rnd.get(3000));
					c_quest2 = attacker;
				}
				else if (i1 == 3)
				{
					i_quest3 = (((damage / 100) * 1000) + Rnd.get(3000));
					c_quest3 = attacker;
				}
				else if (i1 == 4)
				{
					i_quest4 = (((damage / 100) * 1000) + Rnd.get(3000));
					c_quest4 = attacker;
				}
			}
		}
		else if (attacker == c_quest2)
		{
			if ((((damage / 150) * 1000) + 1000) > i_quest2)
			{
				i_quest2 = (((damage / 150) * 1000) + Rnd.get(3000));
			}
		}
		else if (attacker == c_quest3)
		{
			if ((((damage / 150) * 1000) + 1000) > i_quest3)
			{
				i_quest3 = (((damage / 150) * 1000) + Rnd.get(3000));
			}
		}
		else if (attacker == c_quest4)
		{
			if ((((damage / 150) * 1000) + 1000) > i_quest4)
			{
				i_quest4 = (((damage / 150) * 1000) + Rnd.get(3000));
			}
		}
		else if (i_quest2 > i_quest3)
		{
			i1 = 3;
		}
		else if (i_quest2 == i_quest3)
		{
			if (Rnd.get(100) < 50)
			{
				i1 = 2;
			}
			else
			{
				i1 = 3;
			}
		}
		else if (i_quest2 < i_quest3)
		{
			i1 = 2;
		}
		
		if (i1 == 2)
		{
			if (i_quest2 > i_quest4)
			{
				i1 = 4;
			}
			else if (i_quest2 == i_quest4)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 2;
				}
				else
				{
					i1 = 4;
				}
			}
			else if (i_quest2 < i_quest4)
			{
				i1 = 2;
			}
		}
		else if (i1 == 3)
		{
			if (i_quest3 > i_quest4)
			{
				i1 = 4;
			}
			else if (i_quest3 == i_quest4)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 3;
				}
				else
				{
					i1 = 4;
				}
			}
			else if (i_quest3 < i_quest4)
			{
				i1 = 3;
			}
		}
		
		if (i1 == 2)
		{
			i_quest2 = (((damage / 150) * 1000) + Rnd.get(3000));
			c_quest2 = attacker;
		}
		else if (i1 == 3)
		{
			i_quest3 = (((damage / 150) * 1000) + Rnd.get(3000));
			c_quest3 = attacker;
		}
		else if (i1 == 4)
		{
			i_quest4 = (((damage / 150) * 1000) + Rnd.get(3000));
			c_quest4 = attacker;
		}
		
		getRandomSkill(npc);
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		npc.broadcastPacket(new PlaySound(1, "B03_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
		
		startQuestTimer("die_1", 300, npc, null, false); // 300
		startQuestTimer("die_2", 600, npc, null, false); // 300
		startQuestTimer("die_3", 3800, npc, null, false); // 3200
		startQuestTimer("die_4", 8200, npc, null, false); // 4400
		startQuestTimer("die_5", 8700, npc, null, false); // 500
		startQuestTimer("die_6", 13300, npc, null, false); // 4600
		startQuestTimer("die_7", 14000, npc, null, false); // 700
		startQuestTimer("die_8", 16500, npc, null, false); // 2500
		
		GrandBossManager.getInstance().setBossStatus(VALAKAS, DEAD);
		
		long respawnTime = ((long) Config.VALAKAS_SPAWN_INTERVAL + Rnd.get(-Config.VALAKAS_SPAWN_RANDOM_INTERVAL, Config.VALAKAS_SPAWN_RANDOM_INTERVAL)) * 3600000L;
		startQuestTimer("valakas_unlock", respawnTime, null, null);
		
		// Also save the respawn time so that the info is maintained past reboots
		StatsSet info = GrandBossManager.getInstance().getStatsSet(VALAKAS);
		info.set("respawn_time", (System.currentTimeMillis() + respawnTime));
		GrandBossManager.getInstance().setStatsSet(VALAKAS, info);
		return super.onKill(npc, killer, isPet);
	}
	
	public void getRandomSkill(L2NpcInstance npc)
	{
		if (npc.isInvul() || npc.isCastingNow())
		{
			return;
		}
		
		L2Skill skill = null;
		int i0 = 0;
		int i1 = 0;
		int i2 = 0;
		L2Character c2 = null;
		
		if (c_quest2 == null)
		{
			i_quest2 = 0;
		}
		else if (!Util.checkIfInRange(5000, npc, c_quest2, true) || c_quest2.isDead())
		{
			i_quest2 = 0;
		}
		
		if (c_quest3 == null)
		{
			i_quest3 = 0;
		}
		else if (!Util.checkIfInRange(5000, npc, c_quest3, true) || c_quest3.isDead())
		{
			i_quest3 = 0;
		}
		
		if (c_quest4 == null)
		{
			i_quest4 = 0;
		}
		else if (!Util.checkIfInRange(5000, npc, c_quest4, true) || c_quest4.isDead())
		{
			i_quest4 = 0;
		}
		
		if (i_quest2 > i_quest3)
		{
			i1 = 2;
			i2 = i_quest2;
			c2 = c_quest2;
		}
		else
		{
			i1 = 3;
			i2 = i_quest3;
			c2 = c_quest3;
		}
		
		if (i_quest4 > i2)
		{
			i1 = 4;
			i2 = i_quest4;
			c2 = c_quest4;
		}
		
		if (i2 == 0)
		{
			c2 = getRandomTarget(npc);
		}
		
		if (i2 > 0)
		{
			if (Rnd.get(100) < 70)
			{
				if (i1 == 2)
				{
					i_quest2 = 500;
				}
				else if (i1 == 3)
				{
					i_quest3 = 500;
				}
				else if (i1 == 4)
				{
					i_quest4 = 500;
				}
			}
			
			if (npc.getCurrentHp() > ((npc.getMaxHp() * 1) / 4))
			{
				i0 = 0;
				i1 = 0;
				if (Util.checkIfInRange(1423, npc, c2, true))
				{
					i0 = 1;
					i1 = 1;
				}
				
				if (c2.getZ() < (npc.getZ() + 200))
				{
					if (Rnd.get(100) < 20)
					{
						skill = SkillTable.getInstance().getInfo(4690, 1);
					}
					else if (Rnd.get(100) < 15)
					{
						skill = SkillTable.getInstance().getInfo(4689, 1);
					}
					else if ((Rnd.get(100) < 15) && (i0 == 1) && (i_quest0 == 1))
					{
						skill = SkillTable.getInstance().getInfo(4685, 1);
						i_quest0 = 0;
					}
					else if ((Rnd.get(100) < 10) && (i1 == 1))
					{
						skill = SkillTable.getInstance().getInfo(4688, 1);
					}
					else if (Rnd.get(100) < 35)
					{
						skill = SkillTable.getInstance().getInfo(4683, 1);
					}
					else
					{
						if (Rnd.get(2) == 0)
						{
							skill = SkillTable.getInstance().getInfo(4681, 1); // left hand
						}
						else
						{
							skill = SkillTable.getInstance().getInfo(4682, 1); // right hand
						}
					}
				}
				else if (Rnd.get(100) < 20)
				{
					skill = SkillTable.getInstance().getInfo(4690, 1);
				}
				else if (Rnd.get(100) < 15)
				{
					skill = SkillTable.getInstance().getInfo(4689, 1);
				}
				else
				{
					skill = SkillTable.getInstance().getInfo(4684, 1);
				}
			}
			else if (npc.getCurrentHp() > ((npc.getMaxHp() * 2) / 4))
			{
				i0 = 0;
				i1 = 0;
				if (Util.checkIfInRange(1423, npc, c2, true))
				{
					i0 = 1;
					i1 = 1;
				}
				
				if (c2.getZ() < (npc.getZ() + 200))
				{
					if (Rnd.get(100) < 5)
					{
						skill = SkillTable.getInstance().getInfo(4690, 1);
					}
					else if (Rnd.get(100) < 10)
					{
						skill = SkillTable.getInstance().getInfo(4689, 1);
					}
					else if ((Rnd.get(100) < 10) && (i0 == 1) && (i_quest0 == 1))
					{
						skill = SkillTable.getInstance().getInfo(4685, 1);
						i_quest0 = 0;
					}
					else if ((Rnd.get(100) < 10) && (i1 == 1))
					{
						skill = SkillTable.getInstance().getInfo(4688, 1);
					}
					else if (Rnd.get(100) < 20)
					{
						skill = SkillTable.getInstance().getInfo(4683, 1);
					}
					else
					{
						if (Rnd.get(2) == 0)
						{
							skill = SkillTable.getInstance().getInfo(4681, 1); // left hand
						}
						else
						{
							skill = SkillTable.getInstance().getInfo(4682, 1); // right hand
						}
					}
				}
				else if (Rnd.get(100) < 5)
				{
					skill = SkillTable.getInstance().getInfo(4690, 1);
				}
				else if (Rnd.get(100) < 10)
				{
					skill = SkillTable.getInstance().getInfo(4689, 1);
				}
				else
				{
					skill = SkillTable.getInstance().getInfo(4684, 1);
				}
			}
			else if (npc.getCurrentHp() > ((npc.getMaxHp() * 3) / 4.0))
			{
				i0 = 0;
				i1 = 0;
				if (Util.checkIfInRange(1423, npc, c2, true))
				{
					i0 = 1;
					i1 = 1;
				}
				
				if (c2.getZ() < (npc.getZ() + 200))
				{
					if (Rnd.get(100) < 0)
					{
						skill = SkillTable.getInstance().getInfo(4690, 1);
					}
					else if (Rnd.get(100) < 5)
					{
						skill = SkillTable.getInstance().getInfo(4689, 1);
					}
					else if ((Rnd.get(100) < 5) && (i0 == 1) && (i_quest0 == 1))
					{
						skill = SkillTable.getInstance().getInfo(4685, 1);
						i_quest0 = 0;
					}
					else if ((Rnd.get(100) < 10) && (i1 == 1))
					{
						skill = SkillTable.getInstance().getInfo(4688, 1);
					}
					else if (Rnd.get(100) < 15)
					{
						skill = SkillTable.getInstance().getInfo(4683, 1);
					}
					else
					{
						if (Rnd.get(2) == 0)
						{
							skill = SkillTable.getInstance().getInfo(4681, 1); // left hand
						}
						else
						{
							skill = SkillTable.getInstance().getInfo(4682, 1); // right hand
						}
					}
				}
				else if (Rnd.get(100) < 0)
				{
					skill = SkillTable.getInstance().getInfo(4690, 1);
				}
				else if (Rnd.get(100) < 5)
				{
					skill = SkillTable.getInstance().getInfo(4689, 1);
				}
				else
				{
					skill = SkillTable.getInstance().getInfo(4684, 1);
				}
			}
			else
			{
				i0 = 0;
				i1 = 0;
				if (Util.checkIfInRange(1423, npc, c2, true))
				{
					i0 = 1;
					i1 = 1;
				}
				
				if (c2.getZ() < (npc.getZ() + 200))
				{
					if (Rnd.get(100) < 0)
					{
						skill = SkillTable.getInstance().getInfo(4690, 1);
					}
					else if (Rnd.get(100) < 10)
					{
						skill = SkillTable.getInstance().getInfo(4689, 1);
					}
					else if ((Rnd.get(100) < 5) && (i0 == 1) && (i_quest0 == 1))
					{
						skill = SkillTable.getInstance().getInfo(4685, 1);
						i_quest0 = 0;
					}
					else if ((Rnd.get(100) < 10) && (i1 == 1))
					{
						skill = SkillTable.getInstance().getInfo(4688, 1);
					}
					else if (Rnd.get(100) < 15)
					{
						skill = SkillTable.getInstance().getInfo(4683, 1);
					}
					else
					{
						if (Rnd.get(2) == 0)
						{
							skill = SkillTable.getInstance().getInfo(4681, 1); // left hand
						}
						else
						{
							skill = SkillTable.getInstance().getInfo(4682, 1); // right hand
						}
					}
				}
				else if (Rnd.get(100) < 0)
				{
					skill = SkillTable.getInstance().getInfo(4690, 1);
				}
				else if (Rnd.get(100) < 10)
				{
					skill = SkillTable.getInstance().getInfo(4689, 1);
				}
				else
				{
					skill = SkillTable.getInstance().getInfo(4684, 1);
				}
			}
		}
		
		if (skill != null)
		{
			callSkillAI(npc, c2, skill);
		}
	}
	
	public void callSkillAI(L2NpcInstance npc, L2Character c2, L2Skill skill)
	{
		QuestTimer timer = getQuestTimer("1003", npc, null);
		
		if (npc == null)
		{
			if (timer != null)
			{
				timer.cancel();
			}
			return;
		}
		
		if (npc.isInvul())
		{
			return;
		}
		
		if ((c2 == null) || c2.isDead() || (timer == null))
		{
			c2 = getRandomTarget(npc); // just in case if hate AI fail
			if (timer == null)
			{
				startQuestTimer("1003", 500, npc, null, true);
				return;
			}
		}
		
		L2Character target = c2;
		if ((target == null) || target.isDead())
		{
			return;
		}
		
		if (Util.checkIfInRange(skill.getCastRange(), npc, target, true))
		{
			timer.cancel();
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			npc.setTarget(target);
			npc.doCast(skill);
		}
		else
		{
			npc.getAI().setIntention(AI_INTENTION_FOLLOW, target);
		}
	}
	
	public L2Character getRandomTarget(L2NpcInstance npc)
	{
		List<L2Character> result = new ArrayList<>();
		Collection<L2Object> objs = npc.getKnownList().getKnownObjects().values();
		{
			for (L2Object obj : objs)
			{
				if (obj instanceof L2PlayableInstance)
				{
					if (Util.checkIfInRange(5000, npc, obj, true) && !((L2Character) obj).isDead() && !((L2Character) obj).isGM())
					{
						result.add((L2Character) obj);
					}
				}
			}
		}
		
		if (!result.isEmpty() && (result.size() != 0))
		{
			Object[] characters = result.toArray();
			return (L2Character) characters[Rnd.get(characters.length)];
		}
		return null;
	}
	
	@Override
	public String onSpellFinished(L2NpcInstance npc, L2PcInstance player, L2Skill skill)
	{
		if (npc.isInvul())
		{
			return null;
		}
		
		if ((npc.getNpcId() == VALAKAS) && !npc.isInvul())
		{
			getRandomSkill(npc);
		}
		
		return super.onSpellFinished(npc, player, skill);
	}
	
	@Override
	public String onAggroRangeEnter(L2NpcInstance npc, L2PcInstance player, boolean isPet)
	{
		int i1 = 0;
		if (GrandBossManager.getInstance().getBossStatus(VALAKAS) == FIGHTING)
		{
			if (npc.getCurrentHp() > ((npc.getMaxHp() * 1) / 4))
			{
				if (player == c_quest2)
				{
					if (((10 * 1000) + 1000) > i_quest2)
					{
						i_quest2 = ((10 * 1000) + Rnd.get(3000));
					}
				}
				else if (player == c_quest3)
				{
					if (((10 * 1000) + 1000) > i_quest3)
					{
						i_quest3 = ((10 * 1000) + Rnd.get(3000));
					}
				}
				else if (player == c_quest4)
				{
					if (((10 * 1000) + 1000) > i_quest4)
					{
						i_quest4 = ((10 * 1000) + Rnd.get(3000));
					}
				}
				else if (i_quest2 > i_quest3)
				{
					i1 = 3;
				}
				else if (i_quest2 == i_quest3)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 2;
					}
					else
					{
						i1 = 3;
					}
				}
				else if (i_quest2 < i_quest3)
				{
					i1 = 2;
				}
				
				if (i1 == 2)
				{
					if (i_quest2 > i_quest4)
					{
						i1 = 4;
					}
					else if (i_quest2 == i_quest4)
					{
						if (Rnd.get(100) < 50)
						{
							i1 = 2;
						}
						else
						{
							i1 = 4;
						}
					}
					else if (i_quest2 < i_quest4)
					{
						i1 = 2;
					}
				}
				else if (i1 == 3)
				{
					if (i_quest3 > i_quest4)
					{
						i1 = 4;
					}
					else if (i_quest3 == i_quest4)
					{
						if (Rnd.get(100) < 50)
						{
							i1 = 3;
						}
						else
						{
							i1 = 4;
						}
					}
					else if (i_quest3 < i_quest4)
					{
						i1 = 3;
					}
				}
				
				if (i1 == 2)
				{
					i_quest2 = ((10 * 1000) + Rnd.get(3000));
					c_quest2 = player;
				}
				else if (i1 == 3)
				{
					i_quest3 = ((10 * 1000) + Rnd.get(3000));
					c_quest3 = player;
				}
				else if (i1 == 4)
				{
					i_quest4 = ((10 * 1000) + Rnd.get(3000));
					c_quest4 = player;
				}
			}
			else if (npc.getCurrentHp() > ((npc.getMaxHp() * 2) / 4))
			{
				if (player == c_quest2)
				{
					if (((6 * 1000) + 1000) > i_quest2)
					{
						i_quest2 = ((6 * 1000) + Rnd.get(3000));
					}
				}
				else if (player == c_quest3)
				{
					if (((6 * 1000) + 1000) > i_quest3)
					{
						i_quest3 = ((6 * 1000) + Rnd.get(3000));
					}
				}
				else if (player == c_quest4)
				{
					if (((6 * 1000) + 1000) > i_quest4)
					{
						i_quest4 = ((6 * 1000) + Rnd.get(3000));
					}
				}
				else if (i_quest2 > i_quest3)
				{
					i1 = 3;
				}
				else if (i_quest2 == i_quest3)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 2;
					}
					else
					{
						i1 = 3;
					}
				}
				else if (i_quest2 < i_quest3)
				{
					i1 = 2;
				}
				
				if (i1 == 2)
				{
					if (i_quest2 > i_quest4)
					{
						i1 = 4;
					}
					else if (i_quest2 == i_quest4)
					{
						if (Rnd.get(100) < 50)
						{
							i1 = 2;
						}
						else
						{
							i1 = 4;
						}
					}
					else if (i_quest2 < i_quest4)
					{
						i1 = 2;
					}
				}
				else if (i1 == 3)
				{
					if (i_quest3 > i_quest4)
					{
						i1 = 4;
					}
					else if (i_quest3 == i_quest4)
					{
						if (Rnd.get(100) < 50)
						{
							i1 = 3;
						}
						else
						{
							i1 = 4;
						}
					}
					else if (i_quest3 < i_quest4)
					{
						i1 = 3;
					}
				}
				
				if (i1 == 2)
				{
					i_quest2 = ((6 * 1000) + Rnd.get(3000));
					c_quest2 = player;
				}
				else if (i1 == 3)
				{
					i_quest3 = ((6 * 1000) + Rnd.get(3000));
					c_quest3 = player;
				}
				else if (i1 == 4)
				{
					i_quest4 = ((6 * 1000) + Rnd.get(3000));
					c_quest4 = player;
				}
			}
			else if (npc.getCurrentHp() > ((npc.getMaxHp() * 3) / 4.0))
			{
				if (player == c_quest2)
				{
					if (((3 * 1000) + 1000) > i_quest2)
					{
						i_quest2 = ((3 * 1000) + Rnd.get(3000));
					}
				}
				else if (player == c_quest3)
				{
					if (((3 * 1000) + 1000) > i_quest3)
					{
						i_quest3 = ((3 * 1000) + Rnd.get(3000));
					}
				}
				else if (player == c_quest4)
				{
					if (((3 * 1000) + 1000) > i_quest4)
					{
						i_quest4 = ((3 * 1000) + Rnd.get(3000));
					}
				}
				else if (i_quest2 > i_quest3)
				{
					i1 = 3;
				}
				else if (i_quest2 == i_quest3)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 2;
					}
					else
					{
						i1 = 3;
					}
				}
				else if (i_quest2 < i_quest3)
				{
					i1 = 2;
				}
				
				if (i1 == 2)
				{
					if (i_quest2 > i_quest4)
					{
						i1 = 4;
					}
					else if (i_quest2 == i_quest4)
					{
						if (Rnd.get(100) < 50)
						{
							i1 = 2;
						}
						else
						{
							i1 = 4;
						}
					}
					else if (i_quest2 < i_quest4)
					{
						i1 = 2;
					}
				}
				else if (i1 == 3)
				{
					if (i_quest3 > i_quest4)
					{
						i1 = 4;
					}
					else if (i_quest3 == i_quest4)
					{
						if (Rnd.get(100) < 50)
						{
							i1 = 3;
						}
						else
						{
							i1 = 4;
						}
					}
					else if (i_quest3 < i_quest4)
					{
						i1 = 3;
					}
				}
				
				if (i1 == 2)
				{
					i_quest2 = ((3 * 1000) + Rnd.get(3000));
					c_quest2 = player;
				}
				else if (i1 == 3)
				{
					i_quest3 = ((3 * 1000) + Rnd.get(3000));
					c_quest3 = player;
				}
				else if (i1 == 4)
				{
					i_quest4 = ((3 * 1000) + Rnd.get(3000));
					c_quest4 = player;
				}
			}
			else if (player == c_quest2)
			{
				if (((2 * 1000) + 1000) > i_quest2)
				{
					i_quest2 = ((2 * 1000) + Rnd.get(3000));
				}
			}
			else if (player == c_quest3)
			{
				if (((2 * 1000) + 1000) > i_quest3)
				{
					i_quest3 = ((2 * 1000) + Rnd.get(3000));
				}
			}
			else if (player == c_quest4)
			{
				if (((2 * 1000) + 1000) > i_quest4)
				{
					i_quest4 = ((2 * 1000) + Rnd.get(3000));
				}
			}
			else if (i_quest2 > i_quest3)
			{
				i1 = 3;
			}
			else if (i_quest2 == i_quest3)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 2;
				}
				else
				{
					i1 = 3;
				}
			}
			else if (i_quest2 < i_quest3)
			{
				i1 = 2;
			}
			
			if (i1 == 2)
			{
				if (i_quest2 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest2 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 2;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest2 < i_quest4)
				{
					i1 = 2;
				}
			}
			else if (i1 == 3)
			{
				if (i_quest3 > i_quest4)
				{
					i1 = 4;
				}
				else if (i_quest3 == i_quest4)
				{
					if (Rnd.get(100) < 50)
					{
						i1 = 3;
					}
					else
					{
						i1 = 4;
					}
				}
				else if (i_quest3 < i_quest4)
				{
					i1 = 3;
				}
			}
			
			if (i1 == 2)
			{
				i_quest2 = ((2 * 1000) + Rnd.get(3000));
				c_quest2 = player;
			}
			else if (i1 == 3)
			{
				i_quest3 = ((2 * 1000) + Rnd.get(3000));
				c_quest3 = player;
			}
			else if (i1 == 4)
			{
				i_quest4 = ((2 * 1000) + Rnd.get(3000));
				c_quest4 = player;
			}
		}
		else if (player == c_quest2)
		{
			if (((1 * 1000) + 1000) > i_quest2)
			{
				i_quest2 = ((1 * 1000) + Rnd.get(3000));
			}
		}
		else if (player == c_quest3)
		{
			if (((1 * 1000) + 1000) > i_quest3)
			{
				i_quest3 = ((1 * 1000) + Rnd.get(3000));
			}
		}
		else if (player == c_quest4)
		{
			if (((1 * 1000) + 1000) > i_quest4)
			{
				i_quest4 = ((1 * 1000) + Rnd.get(3000));
			}
		}
		else if (i_quest2 > i_quest3)
		{
			i1 = 3;
		}
		else if (i_quest2 == i_quest3)
		{
			if (Rnd.get(100) < 50)
			{
				i1 = 2;
			}
			else
			{
				i1 = 3;
			}
		}
		else if (i_quest2 < i_quest3)
		{
			i1 = 2;
		}
		
		if (i1 == 2)
		{
			if (i_quest2 > i_quest4)
			{
				i1 = 4;
			}
			else if (i_quest2 == i_quest4)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 2;
				}
				else
				{
					i1 = 4;
				}
			}
			else if (i_quest2 < i_quest4)
			{
				i1 = 2;
			}
		}
		else if (i1 == 3)
		{
			if (i_quest3 > i_quest4)
			{
				i1 = 4;
			}
			else if (i_quest3 == i_quest4)
			{
				if (Rnd.get(100) < 50)
				{
					i1 = 3;
				}
				else
				{
					i1 = 4;
				}
			}
			else if (i_quest3 < i_quest4)
			{
				i1 = 3;
			}
		}
		
		if (i1 == 2)
		{
			i_quest2 = ((1 * 1000) + Rnd.get(3000));
			c_quest2 = player;
		}
		else if (i1 == 3)
		{
			i_quest3 = ((1 * 1000) + Rnd.get(3000));
			c_quest3 = player;
		}
		else if (i1 == 4)
		{
			i_quest4 = ((1 * 1000) + Rnd.get(3000));
			c_quest4 = player;
		}
		
		if ((GrandBossManager.getInstance().getBossStatus(VALAKAS) == FIGHTING) && !npc.isInvul())
		{
			getRandomSkill(npc);
		}
		
		return super.onAggroRangeEnter(npc, player, isPet);
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (npc.isInvul())
		{
			return null;
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
}