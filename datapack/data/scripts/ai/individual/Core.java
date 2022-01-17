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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;

/**
 * Core AI
 * @author DrLecter Revised By Emperorc
 */
public class Core extends Quest
{
	private static final int CORE = 12052;
	private static final int DEATH_KNIGHT = 12054;
	private static final int DEATH_WRAITH = 12055;
	private static final int SUSCEPTOR = 12058;
	
	// CORE Status Tracking :
	private static final byte ALIVE = 0; // Core is spawned.
	private static final byte DEAD = 1; // Core has been killed.
	
	private final List<L2Attackable> _minions = new ArrayList<>();
	
	private boolean _firstAttacked;
	
	public static void main(String[] args)
    {
        // Quest class
        new Core();
    }
	
	public Core()
	{
		super(-1, "core", "ai");
		registerNPC(CORE);
		registerNPC(DEATH_KNIGHT);
		registerNPC(DEATH_WRAITH);
		registerNPC(SUSCEPTOR);
		
		StatsSet info = GrandBossManager.getInstance().getStatsSet(CORE);
		int status = GrandBossManager.getInstance().getBossStatus(CORE);
		if (status == DEAD)
		{
			// load the unlock date and time for Core from DB
			long temp = (info.getLong("respawn_time") - System.currentTimeMillis());
			// if Core is locked until a certain time, mark it so and start the unlock timer
			// the unlock time has not yet expired.
			if (temp > 0)
			{
				startQuestTimer("core_unlock", temp, null, null);
			}
			else
			{
				// the time has already expired while the server was offline. Immediately spawn Core.
				L2GrandBossInstance core = (L2GrandBossInstance) addSpawn(CORE, 17726, 108915, -6490, 0, false, 0);
				GrandBossManager.getInstance().setBossStatus(CORE, ALIVE);
				spawnBoss(core);
			}
		}
		else
		{
			String test = loadGlobalQuestVar("Core_Attacked");
            if (test.equalsIgnoreCase("true"))
            {
                _firstAttacked = true;
            }
			int loc_x = info.getInteger("loc_x");
			int loc_y = info.getInteger("loc_y");
			int loc_z = info.getInteger("loc_z");
			int heading = info.getInteger("heading");
			int hp = info.getInteger("currentHP");
			int mp = info.getInteger("currentMP");
			L2GrandBossInstance core = (L2GrandBossInstance) addSpawn(CORE, loc_x, loc_y, loc_z, heading, false, 0);
			core.setCurrentHpMp(hp, mp);
			spawnBoss(core);
		}
	}
	
	@Override
	public void saveGlobalData()
	{
		saveGlobalQuestVar("Core_Attacked", "" + _firstAttacked);
	}
	
	public void spawnBoss(L2GrandBossInstance npc)
	{
		GrandBossManager.getInstance().addBoss(npc);
		npc.broadcastPacket(new PlaySound(1, "BS01_A", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
		
		// Spawn minions
		L2NpcInstance mob;
		for (int i = 0; i < 5; i++)
		{
			int x = 16800 + (i * 360);
			mob = addSpawn(DEATH_KNIGHT, x, 110000, npc.getZ(), 280 + Rnd.get(40), false, 0);
			_minions.add((L2Attackable) mob);
			mob = addSpawn(DEATH_KNIGHT, x, 109000, npc.getZ(), 280 + Rnd.get(40), false, 0);
			_minions.add((L2Attackable) mob);
			int x2 = 16800 + (i * 600);
			mob = addSpawn(DEATH_WRAITH, x2, 109300, npc.getZ(), 280 + Rnd.get(40), false, 0);
			_minions.add((L2Attackable) mob);
		}
		
		for (int i = 0; i < 4; i++)
		{
			int x = 16800 + (i * 450);
			mob = addSpawn(SUSCEPTOR, x, 110300, npc.getZ(), 280 + Rnd.get(40), false, 0);
			_minions.add((L2Attackable) mob);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("core_unlock"))
		{
			L2GrandBossInstance core = (L2GrandBossInstance) addSpawn(CORE, 17726, 108915, -6490, 0, false, 0);
			GrandBossManager.getInstance().setBossStatus(CORE, ALIVE);
			spawnBoss(core);
		}
		else if (event.equalsIgnoreCase("spawn_minion"))
		{
			_minions.add((L2Attackable) addSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 0));
		}
		else if (event.equalsIgnoreCase("despawn_minions"))
		{
			for (int i = 0; i < _minions.size(); i++)
			{
				L2Attackable mob = _minions.get(i);
				if (mob != null)
				{
					mob.decayMe();
				}
			}
			_minions.clear();
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (npc.getNpcId() == CORE)
		{
			if (_firstAttacked)
			{
				if (Rnd.get(100) == 0)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Removing intruders."));
				}
			}
			else
			{
				_firstAttacked = true;
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "A non-permitted target has been discovered."));
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Starting intruder removal system."));
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		if (npc.getNpcId() == CORE)
		{
			int objId = npc.getObjectId();
			npc.broadcastPacket(new PlaySound(1, "BS02_D", 1, objId, npc.getX(), npc.getY(), npc.getZ()));
			npc.broadcastPacket(new NpcSay(objId, 0, npc.getNpcId(), "A fatal error has occurred."));
			npc.broadcastPacket(new NpcSay(objId, 0, npc.getNpcId(), "System is being shut down..."));
			npc.broadcastPacket(new NpcSay(objId, 0, npc.getNpcId(), "......"));
			_firstAttacked = false;
			addSpawn(12053, 16502, 110165, -6394, 0, false, 900000);
			addSpawn(12053, 18948, 110166, -6397, 0, false, 900000);
			GrandBossManager.getInstance().setBossStatus(CORE, DEAD);
			// Time is 60hour +/- 23hour
			long respawnTime = ((long)Config.CORE_SPAWN_INTERVAL + Rnd.get(-Config.CORE_SPAWN_RANDOM_INTERVAL, Config.CORE_SPAWN_RANDOM_INTERVAL)) * 3600000L;
			startQuestTimer("core_unlock", respawnTime, null, null);
			// also save the respawn time so that the info is maintained past reboots
			StatsSet info = GrandBossManager.getInstance().getStatsSet(CORE);
			info.set("respawn_time", (System.currentTimeMillis() + respawnTime));
			GrandBossManager.getInstance().setStatsSet(CORE, info);
			startQuestTimer("despawn_minions", 20000, null, null);
			cancelQuestTimers("spawn_minion");
		}
		else if ((GrandBossManager.getInstance().getBossStatus(CORE) == ALIVE) && (_minions != null) && _minions.contains(npc))
		{
			_minions.remove(npc);
			startQuestTimer("spawn_minion", 60000, npc, null);
		}
		return super.onKill(npc, killer, isPet);
	}
}