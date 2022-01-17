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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.Earthquake;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;

/**
 * Antharas AI
 * @author Emperorc
 */
public class Antharas extends Quest
{
	private static final int ANTHARAS = 12211;
	
	// Antharas Status Tracking :
	private static final byte DORMANT = 0; // Antharas is spawned and no one has entered yet. Entry is unlocked
	private static final byte WAITING = 1; // Antharas is spawned and someone has entered, triggering a 30 minute window for additional people to enter before he unleashes his attack. Entry is unlocked
	private static final byte FIGHTING = 2; // Antharas is engaged in battle, annihilating his foes. Entry is locked
	private static final byte DEAD = 3; // Antharas has been killed. Entry is locked
	
	private long _lastAction = 0;
	private final L2BossZone _zone;
	
	public static void main(String[] args)
    {
        // Quest class
        new Antharas();
    }
	
	// Boss: Antharas
	public Antharas()
	{
		super(-1, "antharas", "ai");
		registerNPC(ANTHARAS);
		
		_zone = GrandBossManager.getInstance().getZone(179700, 113800, -7709);
		StatsSet info = GrandBossManager.getInstance().getStatsSet(ANTHARAS);
		int status = GrandBossManager.getInstance().getBossStatus(ANTHARAS);
		if (status == DEAD)
		{
			// load the unlock date and time for antharas from DB
			// if antharas is locked until a certain time, mark it so and start the unlock timer
			// the unlock time has not yet expired. Mark Antharas as currently locked. Setup a timer
			// to fire at the correct time (calculate the time between now and the unlock time,
			// setup a timer to fire after that many msec)
			long temp = (info.getLong("respawn_time") - System.currentTimeMillis());
			if (temp > 0)
			{
				startQuestTimer("antharas_unlock", temp, null, null);
			}
			else
			{
				// the time has already expired while the server was offline. Immediately spawn antharas in his cave.
				// also, the status needs to be changed to DORMANT
				L2GrandBossInstance antharas = (L2GrandBossInstance) addSpawn(ANTHARAS, 185708, 114298, -8221, 32768, false, 0);
				GrandBossManager.getInstance().setBossStatus(ANTHARAS, DORMANT);
				antharas.broadcastPacket(new Earthquake(185708, 114298, -8221, 20, 10));
				GrandBossManager.getInstance().addBoss(antharas);
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
			L2GrandBossInstance antharas = (L2GrandBossInstance) addSpawn(ANTHARAS, loc_x, loc_y, loc_z, heading, false, 0);
			GrandBossManager.getInstance().addBoss(antharas);
			antharas.setCurrentHpMp(hp, mp);
			if (status == WAITING)
			{
				// Start timer to lock entry after a certain amount of minutes
				startQuestTimer("waiting", (Config.ANTHARAS_WAIT_TIME * 60000L), antharas, null);
			}
			else if (status == FIGHTING)
			{
				_lastAction = System.currentTimeMillis();
				// Start repeating timer to check for inactivity
				startQuestTimer("antharas_despawn", 60000, antharas, null, true);
			}
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		if (npc != null)
		{
			long temp = 0;
			if (event.equalsIgnoreCase("waiting"))
			{
				npc.teleToLocation(185452, 114835, -8221);
				
				npc.disableCoreAI(true);
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(181911, 114835, -7678));
				startQuestTimer("antharas_has_arrived", 2000, npc, null, true);
				npc.broadcastPacket(new PlaySound(1, "BS02_A", 1, npc.getObjectId(), 185452, 114835, -8221));
				GrandBossManager.getInstance().setBossStatus(ANTHARAS, FIGHTING);
			}
			else if (event.equalsIgnoreCase("camera_1"))
			{
				startQuestTimer("camera_2", 3000, npc, null);
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 700, 13, -19, 0, 20000, 0, 0, 1, 0));
			}
			else if (event.equalsIgnoreCase("camera_2"))
			{
				startQuestTimer("camera_3", 10000, npc, null);
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 700, 13, 0, 6000, 20000, 0, 0, 1, 0));
			}
			else if (event.equalsIgnoreCase("camera_3"))
			{
				startQuestTimer("camera_4", 200, npc, null);
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 3700, 0, -3, 0, 10000, 0, 0, 1, 0));
			}
			else if (event.equalsIgnoreCase("camera_4"))
			{
				startQuestTimer("camera_5", 10800, npc, null);
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 0, -3, 22000, 30000, 0, 0, 1, 0));
			}
			else if (event.equalsIgnoreCase("camera_5"))
			{
				startQuestTimer("antharas_leave_cave", 7500, npc, null);
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 0, -3, 300, 7000, 0, 0, 1, 0));
			}
			else if (event.equalsIgnoreCase("antharas_leave_cave"))
			{
				npc.disableCoreAI(false);
				npc.setRunning();
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(179011, 114871, -7704));
				_lastAction = System.currentTimeMillis();
				startQuestTimer("antharas_despawn", 60000, npc, null, true);
			}
			else if (event.equalsIgnoreCase("antharas_despawn"))
			{
				temp = (System.currentTimeMillis() - _lastAction);
				if (temp > 1800000)
				{
					npc.teleToLocation(185708, 114298, -8221);
					GrandBossManager.getInstance().setBossStatus(ANTHARAS, DORMANT);
					npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp());
					_zone.oustAllPlayers();
					cancelQuestTimer("antharas_despawn", npc, null);
				}
			}
			else if (event.equalsIgnoreCase("antharas_has_arrived"))
			{
				int dx = Math.abs(npc.getX() - 181911);
				int dy = Math.abs(npc.getY() - 114835);
				if ((dx <= 50) && (dy <= 50))
				{
					startQuestTimer("camera_1", 2000, npc, null);
					npc.getSpawn().setLocX(181911);
					npc.getSpawn().setLocY(114835);
					npc.getSpawn().setLocZ(-7678);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					cancelQuestTimer("antharas_has_arrived", npc, null);
				}
				else
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(181911, 114835, -7678));
				}
			}
			else if (event.equalsIgnoreCase("spawn_cubes"))
			{
				addSpawn(12324, 177615, 114941, -7709, 0, false, 900000);
				int radius = 1500;
				for (int i = 0; i < 20; i++)
				{
					int x = (int) (radius * Math.cos(i * .331)); // .331~2pi/19
					int y = (int) (radius * Math.sin(i * .331));
					addSpawn(31859, 177615 + x, 114941 + y, -7709, 0, false, 900000);
				}
				cancelQuestTimer("antharas_despawn", npc, null);
				startQuestTimer("remove_players", 900000, null, null);
			}
		}
		else
		{
			if (event.equalsIgnoreCase("antharas_unlock"))
			{
				L2GrandBossInstance antharas = (L2GrandBossInstance) addSpawn(ANTHARAS, 185708, 114298, -8221, 32768, false, 0);
				GrandBossManager.getInstance().addBoss(antharas);
				GrandBossManager.getInstance().setBossStatus(ANTHARAS, DORMANT);
				antharas.broadcastPacket(new Earthquake(185708, 114298, -8221, 20, 10));
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
		_lastAction = System.currentTimeMillis();
		if (GrandBossManager.getInstance().getBossStatus(ANTHARAS) != FIGHTING)
		{
			attacker.teleToLocation(82480, 149087, -3350, true);
		}
		
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1200, 20, -10, 0, 13000, 0, 0, 1, 0));
		npc.broadcastPacket(new PlaySound(1, "BS01_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
		startQuestTimer("spawn_cubes", 10000, npc, null);
		GrandBossManager.getInstance().setBossStatus(ANTHARAS, DEAD);
		long respawnTime = ((long)Config.ANTHARAS_SPAWN_INTERVAL + Rnd.get(-Config.ANTHARAS_SPAWN_RANDOM_INTERVAL, Config.ANTHARAS_SPAWN_RANDOM_INTERVAL)) * 3600000L;
		startQuestTimer("antharas_unlock", respawnTime, null, null);
		// also save the respawn time so that the info is maintained past reboots
		StatsSet info = GrandBossManager.getInstance().getStatsSet(ANTHARAS);
		info.set("respawn_time", (System.currentTimeMillis() + respawnTime));
		GrandBossManager.getInstance().setStatsSet(ANTHARAS, info);
		return super.onKill(npc, killer, isPet);
	}
}