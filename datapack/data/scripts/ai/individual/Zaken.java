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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.eventhandling.EventHandler;
import net.sf.l2j.gameserver.eventhandling.events.GameTimeEvent;
import net.sf.l2j.gameserver.instancemanager.EventHandleManager;
import net.sf.l2j.gameserver.instancemanager.EventHandleManager.EventType;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;

/**
 * Zaken AI
 */
public class Zaken extends Quest
{
	private static final Logger _log = Logger.getLogger(Zaken.class.getName());
	
	private static final int ZAKEN = 12374;
	private static final int DOLL_BLADER_B = 12376;
	private static final int VALE_MASTER_B = 12377;
	private static final int PIRATES_ZOMBIE_CAPTAIN_B = 12544;
	private static final int PIRATES_ZOMBIE_B = 12545;
	
	private static final Location[] LOCS =
	{
		new Location(53950, 219860, -3488),
		new Location(55980, 219820, -3488),
		new Location(54950, 218790, -3488),
		new Location(55970, 217770, -3488),
		new Location(53930, 217760, -3488),
		
		new Location(55970, 217770, -3216),
		new Location(55980, 219920, -3216),
		new Location(54960, 218790, -3216),
		new Location(53950, 219860, -3216),
		new Location(53930, 217760, -3216),
		
		new Location(55970, 217770, -2944),
		new Location(55980, 219920, -2944),
		new Location(54960, 218790, -2944),
		new Location(53950, 219860, -2944),
		new Location(53930, 217760, -2944)
	};
	
	// ZAKEN status tracking
	private static final byte ALIVE = 0; // Zaken is spawned
	private static final byte DEAD = 1; // Zaken has been killed
	
	private int _minionStatus;
	private int _hate;
	
	private boolean _hasTeleported;
	private L2Character _mostHated;
	
	private final Location _zakenLocation = new Location(0, 0, 0);
	private final Set<L2PcInstance> _victims = ConcurrentHashMap.newKeySet();
	
	private int _teleportCheck = 0; // used for zaken HP check for teleport
	
	// Zaken door handling
	private final EventHandler<GameTimeEvent> callback = (e) -> {
		if (e.getHour() == 0)
		{
			L2DoorInstance door = DoorTable.getInstance().getDoor(21240006);
			if (door != null && !door.isOpen())
			{
				door.openMe();
			}
		}
	};
	
	private final L2BossZone _zone;
	
	public static void main(String[] args)
	{
		// Quest class
		new Zaken();
	}
	
	public Zaken()
	{
		super(-1, "zaken", "ai");
		registerNPC(ZAKEN);
		registerNPC(DOLL_BLADER_B);
		registerNPC(VALE_MASTER_B);
		registerNPC(PIRATES_ZOMBIE_CAPTAIN_B);
		registerNPC(PIRATES_ZOMBIE_B);
		
		// Register this event for features based on game time change (e.g. door state)
		EventHandleManager.getInstance().addEventHandler(EventType.HOUR_CHANGED, callback);
		
		_zone = GrandBossManager.getInstance().getZone(55312, 219168, -3223);
		StatsSet info = GrandBossManager.getInstance().getStatsSet(ZAKEN);
		int status = GrandBossManager.getInstance().getBossStatus(ZAKEN);
		if (status == DEAD)
		{
			// load the unlock date and time for zaken from DB
			long temp = info.getLong("respawn_time") - System.currentTimeMillis();
			// if zaken is locked until a certain time, mark it so and start the unlock timer
			// the unlock time has not yet expired.
			if (temp > 0)
			{
				startQuestTimer("zaken_unlock", temp, null, null);
			}
			else
			{
				// the time has already expired while the server was offline. Immediately spawn zaken.
				L2GrandBossInstance zaken = (L2GrandBossInstance) addSpawn(ZAKEN, 55275, 218880, -3217, 0, false, 0);
				GrandBossManager.getInstance().setBossStatus(ZAKEN, ALIVE);
				spawnBoss(zaken);
			}
		}
		else
		{
			int loc_x = info.getInteger("loc_x");
			int loc_y = info.getInteger("loc_y");
			int loc_z = info.getInteger("loc_z");
			int heading = info.getInteger("heading");
			int hp = info.getInteger("currentHP");
			int mp = info.getInteger("currentMP");
			L2GrandBossInstance zaken = (L2GrandBossInstance) addSpawn(ZAKEN, loc_x, loc_y, loc_z, heading, false, 0);
			zaken.setCurrentHpMp(hp, mp);
			spawnBoss(zaken);
		}
	}
	
	@Override
	public void unload(boolean removeFromList)
	{
		// Cleanup
		EventHandleManager.getInstance().removeEventHandler(EventType.HOUR_CHANGED, callback);
		super.unload(removeFromList);
	}
	
	public void spawnBoss(L2GrandBossInstance npc)
	{
		if (npc == null)
		{
			_log.warning("Zaken AI failed to load, missing Zaken in grandboss_data.sql");
			return;
		}
		
		GrandBossManager.getInstance().addBoss(npc);
		npc.broadcastPacket(new PlaySound(1, "BS01_A", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
		
		_hasTeleported = false;
		_hate = 0;
		_teleportCheck = 3;
		_mostHated = null;

		_zakenLocation.setXYZ(npc.getX(), npc.getY(), npc.getZ());
		_victims.clear();
		
		if (_zone == null)
		{
			_log.warning("Zaken AI failed to load, missing zone for Zaken");
			return;
		}
		
		if (_zone.isInsideZone(npc))
		{
			_minionStatus = 1;
			startQuestTimer("1003", 1700, null, null, true);
		}
		
		startQuestTimer("1001", 1000, npc, null, false); // Buffs, random teleports, etc
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		int status = GrandBossManager.getInstance().getBossStatus(ZAKEN);
		if ((status == DEAD) && !event.equalsIgnoreCase("zaken_unlock"))
		{
			return super.onAdvEvent(event, npc, player);
		}
		
		if (event.equalsIgnoreCase("1001"))
		{
			boolean isInNightMode = (npc.getAbnormalEffect() & L2Character.ABNORMAL_EFFECT_TEXTURE_CHANGE) == L2Character.ABNORMAL_EFFECT_TEXTURE_CHANGE;
			if (!isInNightMode)
			{
				isInNightMode = true;
			}
			
			int sk_4227 = 0;
			L2Effect[] effects = npc.getAllEffects();
			if (effects.length != 0)
			{
				for (L2Effect e : effects)
				{
					if (e.getSkill().getId() == 4227)
					{
						sk_4227 = 1;
						break;
					}
				}
			}
			
			if (GameTimeController.getInstance().isNight())
			{
				final L2Character mostHated = ((L2Attackable) npc).getMostHated();
				
				// Use night face if zaken have day face
				if ((npc.getAbnormalEffect() & L2Character.ABNORMAL_EFFECT_TEXTURE_CHANGE) != L2Character.ABNORMAL_EFFECT_TEXTURE_CHANGE)
				{
					npc.startAbnormalEffect(L2Character.ABNORMAL_EFFECT_TEXTURE_CHANGE);
					_zakenLocation.setXYZ(npc.getX(), npc.getY(), npc.getZ());
				}
				
				if (sk_4227 == 0) // use zaken regeneration
				{
					L2Skill skill = SkillTable.getInstance().getInfo(4227, 1);
					skill.getEffects(npc, npc);
				}
				
				if ((npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ATTACK) && !_hasTeleported)
				{
					boolean willTeleport = true;
					
					// Check most hated distance. If distance is low, Zaken doesn't teleport.
					if (mostHated != null && mostHated.isInsideRadius(_zakenLocation.getX(), _zakenLocation.getY(), _zakenLocation.getZ(), 1500, true, false))
						willTeleport = false;
					
					// We're still under willTeleport possibility. Now we check each victim distance. If at least one is near Zaken, we cancel the teleport possibility.
					if (willTeleport)
					{
						for (L2PcInstance victim : _victims)
						{
							if (victim.isInsideRadius(_zakenLocation.getX(), _zakenLocation.getY(), _zakenLocation.getZ(), 1500, true, false))
							{
								willTeleport = false;
								continue;
							}
						}
					}
					
					// All targets are far, clear victims list and Zaken teleport
					if (willTeleport)
					{
						_victims.clear();
						
						npc.setTarget(npc);
						npc.doCast(SkillTable.getInstance().getInfo(4222, 1));
					}
				}
				
				if (!_hasTeleported && (Rnd.get(20) < 1))
				{
					_zakenLocation.setXYZ(npc.getX(), npc.getY(), npc.getZ());
				}
				
				// Process to cleanup hate from most hated upon 5 straight AI loops.
				if (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ATTACK && mostHated != null)
				{
					if (_hate == 0)
					{
						_mostHated = mostHated;
						_hate = 1;
					}
					else
					{
						if (_mostHated == mostHated)
							_hate++;
						else
						{
							_hate = 1;
							_mostHated = mostHated;
						}
					}
				}
				
				// Cleanup hate towards idle state
				if (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE)
					_hate = 0;
				
				// We built enough hate ; release the current most hated target, reset the hate counter
				if (_hate > 5)
				{
					((L2Attackable) npc).stopHating(_mostHated);
					L2Character nextTarget = ((L2Attackable) npc).getMostHated();
					if (nextTarget != null)
					{
						npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, nextTarget);
					}
					
					_hate = 0;
				}
			}
			// Use day face in day time
			else if (isInNightMode)
			{
				if ((npc.getAbnormalEffect() & L2Character.ABNORMAL_EFFECT_TEXTURE_CHANGE) == L2Character.ABNORMAL_EFFECT_TEXTURE_CHANGE)
				{
					npc.stopAbnormalEffect(L2Character.ABNORMAL_EFFECT_TEXTURE_CHANGE);
				}
				_teleportCheck = 3;
			}
			
			if (sk_4227 == 1) // when switching to day time, cancel zaken night regen
			{
				npc.setTarget(npc);
				npc.doCast(SkillTable.getInstance().getInfo(4242, 1));
				npc.stopSkillEffects(4227);
			}
			
			if (Rnd.get(40) < 1)
			{
				npc.setTarget(npc);
				npc.doCast(SkillTable.getInstance().getInfo(4222, 1));
			}

			startQuestTimer("1001", 30000, npc, null, false);
		}
		else if (event.equalsIgnoreCase("1002"))
		{
			npc.doCast(SkillTable.getInstance().getInfo(4222, 1));
			_hasTeleported = false;
		}
		else if (event.equalsIgnoreCase("1003"))
		{
			if (_minionStatus == 1)
			{
				Location loc = LOCS[Rnd.get(LOCS.length)];
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, loc.getX() + Rnd.get(650), loc.getY() + Rnd.get(650), loc.getZ(), Rnd.get(65536), false, 0);
				_minionStatus = 2;
			}
			else if (_minionStatus == 2)
			{
				Location loc = LOCS[Rnd.get(LOCS.length)];
				addSpawn(DOLL_BLADER_B, loc.getX() + Rnd.get(650), loc.getY() + Rnd.get(650), loc.getZ(), Rnd.get(65536), false, 0);
				_minionStatus = 3;
			}
			else if (_minionStatus == 3)
			{
				for (int i = 0; i < 2; i++)
				{
					addSpawn(VALE_MASTER_B, LOCS[Rnd.get(LOCS.length)].getX() + Rnd.get(650), LOCS[Rnd.get(LOCS.length)].getY() + Rnd.get(650), LOCS[Rnd.get(LOCS.length)].getZ(), Rnd.get(65536), false, 0);
				}
				_minionStatus = 4;
			}
			else if (_minionStatus == 4)
			{
				for (int i = 0; i < 5; i++)
				{
					addSpawn(PIRATES_ZOMBIE_B, LOCS[Rnd.get(LOCS.length)].getX() + Rnd.get(650), LOCS[Rnd.get(LOCS.length)].getY() + Rnd.get(650), LOCS[Rnd.get(LOCS.length)].getZ(), Rnd.get(65536), false, 0);
				}
				_minionStatus = 5;
			}
			else if (_minionStatus == 5)
			{
				addSpawn(DOLL_BLADER_B, 52675, 219371, -3290, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 52687, 219596, -3368, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 52672, 219740, -3418, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 52857, 219992, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 52959, 219997, -3488, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 53381, 220151, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 54236, 220948, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54885, 220144, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55264, 219860, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 55399, 220263, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55679, 220129, -3488, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 56276, 220783, -3488, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 57173, 220234, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 56267, 218826, -3488, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 56294, 219482, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 56094, 219113, -3488, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 56364, 218967, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 57113, 218079, -3488, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 56186, 217153, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55440, 218081, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 55202, 217940, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55225, 218236, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54973, 218075, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 53412, 218077, -3488, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 54226, 218797, -3488, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 54394, 219067, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54139, 219253, -3488, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 54262, 219480, -3488, Rnd.get(65536), false, 0);
				_minionStatus = 6;
			}
			else if (_minionStatus == 6)
			{
				addSpawn(PIRATES_ZOMBIE_B, 53412, 218077, -3488, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 54413, 217132, -3488, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 54841, 217132, -3488, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 55372, 217128, -3343, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 55893, 217122, -3488, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 56282, 217237, -3216, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 56963, 218080, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 56267, 218826, -3216, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 56294, 219482, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 56094, 219113, -3216, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 56364, 218967, -3216, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 56276, 220783, -3216, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 57173, 220234, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54885, 220144, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55264, 219860, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 55399, 220263, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55679, 220129, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 54236, 220948, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 54464, 219095, -3216, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 54226, 218797, -3216, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 54394, 219067, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54139, 219253, -3216, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 54262, 219480, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 53412, 218077, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55440, 218081, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 55202, 217940, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55225, 218236, -3216, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54973, 218075, -3216, Rnd.get(65536), false, 0);
				_minionStatus = 7;
			}
			else if (_minionStatus == 7)
			{
				addSpawn(PIRATES_ZOMBIE_B, 54228, 217504, -3216, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 54181, 217168, -3216, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 54714, 217123, -3168, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 55298, 217127, -3073, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 55787, 217130, -2993, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 56284, 217216, -2944, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 56963, 218080, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 56267, 218826, -2944, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 56294, 219482, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 56094, 219113, -2944, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 56364, 218967, -2944, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 56276, 220783, -2944, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 57173, 220234, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54885, 220144, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55264, 219860, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 55399, 220263, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55679, 220129, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 54236, 220948, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 54464, 219095, -2944, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 54226, 218797, -2944, Rnd.get(65536), false, 0);
				addSpawn(VALE_MASTER_B, 54394, 219067, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54139, 219253, -2944, Rnd.get(65536), false, 0);
				addSpawn(DOLL_BLADER_B, 54262, 219480, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 53412, 218077, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 54280, 217200, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55440, 218081, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_CAPTAIN_B, 55202, 217940, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 55225, 218236, -2944, Rnd.get(65536), false, 0);
				addSpawn(PIRATES_ZOMBIE_B, 54973, 218075, -2944, Rnd.get(65536), false, 0);
				_minionStatus = 8;
				cancelQuestTimer("1003", null, null);
			}
		}
		else if (event.equalsIgnoreCase("zaken_unlock"))
		{
			L2GrandBossInstance zaken = (L2GrandBossInstance) addSpawn(ZAKEN, 55275, 218880, -3217, 0, false, 0);
			GrandBossManager.getInstance().setBossStatus(ZAKEN, ALIVE);
			spawnBoss(zaken);
		}
		else if (event.equalsIgnoreCase("CreateOnePrivateEx"))
		{
			addSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), 0, false, 0);
		}
		
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFactionCall(L2NpcInstance npc, L2NpcInstance caller, L2PcInstance attacker, boolean isPet)
	{
		if ((caller == null) || (npc == null))
		{
			return super.onFactionCall(npc, caller, attacker, isPet);
		}
		
		if (caller.getNpcId() == ZAKEN && GameTimeController.getInstance().isNight())
		{
			if ((npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE) && !_hasTeleported && caller.getCurrentHp() < (0.9 * caller.getMaxHp()) && Rnd.get(450) < 1)
			{
				_hasTeleported = true;
				_zakenLocation.setXYZ(npc.getX(), npc.getY(), npc.getZ());
				startQuestTimer("1002", 300, caller, null);
			}
		}
		return super.onFactionCall(npc, caller, attacker, isPet);
	}
	
	@Override
	public String onSpellFinished(L2NpcInstance npc, L2PcInstance player, L2Skill skill)
	{
		if (npc.getNpcId() == ZAKEN)
		{
			int skillId = skill.getId();
			if (skillId == 4222)
			{
				npc.teleToLocation(_zakenLocation.getX(), _zakenLocation.getY(), _zakenLocation.getZ());
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
			}
			else if (skillId == 4216)
			{
				Location loc = LOCS[Rnd.get(LOCS.length)];
				player.teleToLocation(loc.getX(), loc.getY(), loc.getZ());
				((L2Attackable) npc).stopHating(player);
				
				L2Character nextTarget = ((L2Attackable) npc).getMostHated();
				if (nextTarget != null)
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, nextTarget);
				}
			}
			else if (skillId == 4217)
			{
				Location loc;
				
				for (L2PcInstance victim : _victims)
				{
					if (victim.isInsideRadius(player, 250, true, false))
					{
						loc = LOCS[Rnd.get(LOCS.length)];
						
						victim.teleToLocation(loc.getX(), loc.getY(), loc.getZ());
						((L2Attackable) npc).stopHating(victim);
					}
				}
				
				loc = LOCS[Rnd.get(LOCS.length)];
				player.teleToLocation(loc.getX(), loc.getY(), loc.getZ());
				((L2Attackable) npc).stopHating(player);

				L2Character nextTarget = ((L2Attackable) npc).getMostHated();
				if (nextTarget != null)
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, nextTarget);
				}
			}
		}
		return super.onSpellFinished(npc, player, skill);
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == ZAKEN)
		{
			if (attacker.getMountType() == 1)
			{
				int sk_4258 = 0;
				L2Effect[] effects = attacker.getAllEffects();
				if (effects.length != 0)
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
			
			L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
			int hate = (int) (((damage / npc.getMaxHp()) / 0.05) * 20000);
			((L2Attackable) npc).addDamageHate(originalAttacker, 0, hate);
			if (Rnd.get(10) < 1)
			{
				int i0 = Rnd.get(LOCS.length * LOCS.length);
				if (i0 < 1)
				{
					npc.setTarget(attacker);
					npc.doCast(SkillTable.getInstance().getInfo(4216, 1));
				}
				else if (i0 < 2)
				{
					npc.setTarget(attacker);
					npc.doCast(SkillTable.getInstance().getInfo(4217, 1));
				}
				else if (i0 < 4)
				{
					npc.setTarget(attacker);
					npc.doCast(SkillTable.getInstance().getInfo(4219, 1));
				}
				else if (i0 < 8)
				{
					npc.setTarget(attacker);
					npc.doCast(SkillTable.getInstance().getInfo(4218, 1));
				}
				else if (i0 < 15)
				{
					for (L2Character character : npc.getKnownList().getKnownCharactersInRadius(100))
					{
						if (character != attacker)
						{
							continue;
						}
						
						if (attacker != ((L2Attackable) npc).getMostHated())
						{
							npc.setTarget(attacker);
							npc.doCast(SkillTable.getInstance().getInfo(4221, 1));
						}
					}
				}
				
				if (Rnd.get(2) < 1)
				{
					if (attacker == ((L2Attackable) npc).getMostHated())
					{
						npc.setTarget(attacker);
						npc.doCast(SkillTable.getInstance().getInfo(4220, 1));
					}
				}
			}
			
			if (!GameTimeController.getInstance().isNight() && (npc.getCurrentHp() < ((npc.getMaxHp() * _teleportCheck) / 4)))
			{
				_teleportCheck -= 1;
				
				npc.setTarget(npc);
				npc.doCast(SkillTable.getInstance().getInfo(4222, 1));
			}
		}
		
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == ZAKEN)
		{
			npc.broadcastPacket(new PlaySound(1, "BS02_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
			GrandBossManager.getInstance().setBossStatus(ZAKEN, DEAD);
			// Time is 60hour +/- 20hour
			long respawnTime = ((long) Config.ZAKEN_SPAWN_INTERVAL + Rnd.get(-Config.ZAKEN_SPAWN_RANDOM_INTERVAL, Config.ZAKEN_SPAWN_RANDOM_INTERVAL)) * 3600000L;
			startQuestTimer("zaken_unlock", respawnTime, null, null);
			cancelQuestTimer("1001", npc, null);
			cancelQuestTimer("1003", npc, null);
			// also save the respawn time so that the info is maintained past reboots
			StatsSet info = GrandBossManager.getInstance().getStatsSet(ZAKEN);
			info.set("respawn_time", System.currentTimeMillis() + respawnTime);
			GrandBossManager.getInstance().setStatsSet(ZAKEN, info);
		}
		else if (GrandBossManager.getInstance().getBossStatus(ZAKEN) == ALIVE)
		{
			if (npcId != ZAKEN)
			{
				startQuestTimer("CreateOnePrivateEx", ((30 + Rnd.get(60)) * 1000), npc, null);
			}
		}
		return super.onKill(npc, killer, isPet);
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == ZAKEN)
		{
			if (Rnd.get(12) < 1)
			{
				int i0 = Rnd.get(LOCS.length * LOCS.length);
				if (i0 < 1)
				{
					npc.setTarget(caster);
					npc.doCast(SkillTable.getInstance().getInfo(4216, 1));
				}
				else if (i0 < 2)
				{
					npc.setTarget(caster);
					npc.doCast(SkillTable.getInstance().getInfo(4217, 1));
				}
				else if (i0 < 4)
				{
					npc.setTarget(caster);
					npc.doCast(SkillTable.getInstance().getInfo(4219, 1));
				}
				else if (i0 < 8)
				{
					npc.setTarget(caster);
					npc.doCast(SkillTable.getInstance().getInfo(4218, 1));
				}
				else if (i0 < 15)
				{
					for (L2Character character : npc.getKnownList().getKnownCharactersInRadius(100))
					{
						if (character != caster)
						{
							continue;
						}
						
						if (caster != ((L2Attackable) npc).getMostHated())
						{
							npc.setTarget(caster);
							npc.doCast(SkillTable.getInstance().getInfo(4221, 1));
						}
					}
				}
				
				if (Rnd.get(2) < 1)
				{
					if (caster == ((L2Attackable) npc).getMostHated())
					{
						npc.setTarget(caster);
						npc.doCast(SkillTable.getInstance().getInfo(4220, 1));
					}
				}
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public String onAggroRangeEnter(L2NpcInstance npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == ZAKEN)
		{
			if (_zone.isInsideZone(npc))
			{
				L2Character target = isPet ? player.getPet() : player;
				((L2Attackable) npc).addDamageHate(target, 1, 200);
			}
			
			if ((player.getZ() > (npc.getZ() - 100)) && (player.getZ() < (npc.getZ() + 100)))
			{
				if (Rnd.get(3) < 1 && _victims.size() < 5)
				{
					_victims.add(player);
				}
				
				if (Rnd.get(LOCS.length) < 1)
				{
					int chance = Rnd.get(LOCS.length * LOCS.length);
					if (chance < 1)
					{
						npc.setTarget(player);
						npc.doCast(SkillTable.getInstance().getInfo(4216, 1));
					}
					else if (chance < 2)
					{
						npc.setTarget(player);
						npc.doCast(SkillTable.getInstance().getInfo(4217, 1));
					}
					else if (chance < 4)
					{
						npc.setTarget(player);
						npc.doCast(SkillTable.getInstance().getInfo(4219, 1));
					}
					else if (chance < 8)
					{
						npc.setTarget(player);
						npc.doCast(SkillTable.getInstance().getInfo(4218, 1));
					}
					else if (chance < 15)
					{
						for (L2Character character : npc.getKnownList().getKnownCharactersInRadius(100))
						{
							if (character != player)
							{
								continue;
							}
							
							if (player != ((L2Attackable) npc).getMostHated())
							{
								npc.setTarget(player);
								npc.doCast(SkillTable.getInstance().getInfo(4221, 1));
							}
						}
					}
					
					if (Rnd.get(2) < 1)
					{
						if (player == ((L2Attackable) npc).getMostHated())
						{
							npc.setTarget(player);
							npc.doCast(SkillTable.getInstance().getInfo(4220, 1));
						}
					}
				}
			}
		}
		
		return super.onAggroRangeEnter(npc, player, isPet);
	}
}