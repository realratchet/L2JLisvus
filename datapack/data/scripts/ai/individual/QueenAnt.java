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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;

/**
 * Queen Ant AI
 * @author Emperorc
 */
public class QueenAnt extends Quest
{
	private static final int QUEEN = 12001;
	private static final int LARVA = 12002;
	private static final int NURSE = 12003;
	private static final int GUARD = 12004;
	private static final int ROYAL = 12005;
	
	// QUEEN Status Tracking :
	private static final byte ALIVE = 0; // Queen Ant is spawned.
	private static final byte DEAD = 1; // Queen Ant has been killed.
	
	private final L2BossZone _zone;
	private final Location _spawnLoc = new Location(-21610, 181594, -5734);
	
	private final List<L2Attackable> _minions = new CopyOnWriteArrayList<>();
	private final List<L2Attackable> _nurses = new CopyOnWriteArrayList<>();
	
	public static void main(String[] args)
    {
        // Quest class
        new QueenAnt();
    }
	
	public QueenAnt()
	{
		super(-1, "queen_ant", "ai");
		registerNPC(QUEEN);
		registerNPC(LARVA);
		registerNPC(NURSE);
		registerNPC(GUARD);
		registerNPC(ROYAL);
		
		_zone = GrandBossManager.getInstance().getZone(_spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ());
		StatsSet info = GrandBossManager.getInstance().getStatsSet(QUEEN);
		int status = GrandBossManager.getInstance().getBossStatus(QUEEN);
		if (status == DEAD)
		{
			// load the unlock date and time for queen ant from DB
			long temp = info.getLong("respawn_time") - System.currentTimeMillis();
			// if queen ant is locked until a certain time, mark it so and start the unlock timer
			// the unlock time has not yet expired.
			if (temp > 0)
			{
				startQuestTimer("queen_unlock", temp, null, null);
			}
			else
			{
				// the time has already expired while the server was offline. Immediately spawn queen ant.
				L2GrandBossInstance queen = (L2GrandBossInstance) addSpawn(QUEEN, _spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ(), 0, false, 0);
				GrandBossManager.getInstance().setBossStatus(QUEEN, ALIVE);
				spawnBoss(queen);
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
			L2GrandBossInstance queen = (L2GrandBossInstance) addSpawn(QUEEN, loc_x, loc_y, loc_z, heading, false, 0);
			queen.setCurrentHpMp(hp, mp);
			spawnBoss(queen);
		}
	}
	
	public void spawnBoss(L2GrandBossInstance npc)
	{
		GrandBossManager.getInstance().addBoss(npc);
		if (_zone != null)
		{
			if (Rnd.get(100) < 33)
			{
				_zone.movePlayersTo(-19480, 187344, -5600);
			}
			else if (Rnd.get(100) < 50)
			{
				_zone.movePlayersTo(-17928, 180912, -5520);
			}
			else
			{
				_zone.movePlayersTo(-23808, 182368, -5600);
			}
		}
		GrandBossManager.getInstance().addBoss(npc);
		startQuestTimer("action", 10000, npc, null, true);
		npc.broadcastPacket(new PlaySound(1, "BS02_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
		// Spawn minions
		addSpawn(LARVA, -21600, 179482, -5846, Rnd.get(360), false, 0);
		_nurses.add((L2Attackable) addSpawn(NURSE, -22000, 179482, -5846, 0, false, 0));
		_nurses.add((L2Attackable) addSpawn(NURSE, -21200, 179482, -5846, 0, false, 0));
		
		int radius = 400;
		
		for (int i = 0; i < 6; i++)
		{
			int x = (int) (radius * Math.cos(i * 1.407)); // 1.407~2pi/6
			int y = (int) (radius * Math.sin(i * 1.407));
			_nurses.add((L2Attackable) addSpawn(NURSE, npc.getX() + x, npc.getY() + y, npc.getZ(), 0, false, 0));
		}
		
		for (int i = 0; i < 8; i++)
		{
			int x = (int) (radius * Math.cos(i * .7854)); // .7854~2pi/8
			int y = (int) (radius * Math.sin(i * .7854));
			_minions.add((L2Attackable) addSpawn(ROYAL, npc.getX() + x, npc.getY() + y, npc.getZ(), 0, false, 0));
		}
		
		startQuestTimer("check_location", 120000, npc, null, true);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("action") && (npc != null))
		{
			if (Rnd.get(3) == 0)
			{
				if (Rnd.get(2) == 0)
				{
					npc.broadcastPacket(new SocialAction(npc.getObjectId(), 3));
				}
				else
				{
					npc.broadcastPacket(new SocialAction(npc.getObjectId(), 4));
				}
			}
		}
		else if (event.equalsIgnoreCase("queen_unlock"))
		{
			L2GrandBossInstance queen = (L2GrandBossInstance) addSpawn(QUEEN, _spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ(), 0, false, 0);
			GrandBossManager.getInstance().setBossStatus(QUEEN, ALIVE);
			spawnBoss(queen);
		}
		else if (event.equalsIgnoreCase("check_location") && npc != null)
		{
			// If boss has been lured too far away, teleport it back to zone
			if (!npc.isInsideRadius(_spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ(), 10000, true, false))
			{
				npc.teleToLocation(_spawnLoc.getX(), _spawnLoc.getY(), _spawnLoc.getZ());
			}
			
			for (L2Attackable mob : _minions)
			{
				if (mob != null && !mob.isInsideRadius(npc.getX(), npc.getY(), npc.getZ(), 10000, true, false))
				{
					mob.teleToLocation(npc.getX(), npc.getY(), npc.getZ());
				}
			}
		}
		else if (event.equalsIgnoreCase("despawn_royals"))
		{
			for (L2Attackable mob : _minions)
			{
				if (mob != null)
				{
					mob.decayMe();
				}
			}
			_minions.clear();
		}
		else if (event.equalsIgnoreCase("spawn_royal"))
		{
			_minions.add((L2Attackable) addSpawn(ROYAL, npc.getX(), npc.getY(), npc.getZ(), 0, false, 0));
		}
		else if (event.equalsIgnoreCase("spawn_nurse"))
		{
			_nurses.add((L2Attackable) addSpawn(NURSE, npc.getX(), npc.getY(), npc.getZ(), 0, false, 0));
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == QUEEN || npcId == LARVA)
		{
			if (damage > 0 && Rnd.get(100) < 5)
			{
				List<L2Attackable> candidates = new ArrayList<>();
				for (L2Attackable minion : _nurses)
				{
					if (!minion.isAlikeDead() && !minion.isAllSkillsDisabled())
					{
						candidates.add(minion);
					}
				}
				
				if (!candidates.isEmpty())
				{
					// Nurse will heal this attacked NPC
					L2Attackable candidate = candidates.get(Rnd.get(candidates.size()));
					if (candidate != null)
					{
						candidate.setTarget(npc);
						candidate.doCast(SkillTable.getInstance().getInfo(npcId == LARVA ? 4024 : 4020, 1));
					}
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == QUEEN)
		{
			npc.broadcastPacket(new PlaySound(1, "BS02_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
			GrandBossManager.getInstance().setBossStatus(QUEEN, DEAD);
			
			// Time is 36hour +/- 17hour
			long respawnTime = ((long)Config.QUEEN_ANT_SPAWN_INTERVAL + Rnd.get(-Config.QUEEN_ANT_SPAWN_RANDOM_INTERVAL, Config.QUEEN_ANT_SPAWN_RANDOM_INTERVAL)) * 3600000L;
			startQuestTimer("queen_unlock", respawnTime, null, null);
			cancelQuestTimer("action", npc, null);
			cancelQuestTimers("check_location");
			
			// also save the respawn time so that the info is maintained past reboots
			StatsSet info = GrandBossManager.getInstance().getStatsSet(QUEEN);
			info.set("respawn_time", System.currentTimeMillis() + respawnTime);
			GrandBossManager.getInstance().setStatsSet(QUEEN, info);
			_nurses.clear();
			
			startQuestTimer("despawn_royals", 20000, null, null);
		}
		else if (GrandBossManager.getInstance().getBossStatus(QUEEN) == ALIVE)
		{
			if (npcId == ROYAL)
			{
				_minions.remove(npc);
				startQuestTimer("spawn_royal", (280 + Rnd.get(40)) * 1000, npc, null);
			}
			else if (npcId == NURSE)
			{
				_nurses.remove(npc);
				startQuestTimer("spawn_nurse", 10000, npc, null);
			}
		}
		return super.onKill(npc, killer, isPet);
	}
}