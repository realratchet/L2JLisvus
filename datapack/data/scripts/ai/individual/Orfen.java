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
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;

/**
 * Orfen AI
 * @author Emperorc
 */
public class Orfen extends Quest
{
	private static final int[][] POS =
	{
		{
			43728,
			17220,
			-4342
		},
		{
			55024,
			17368,
			-5412
		},
		{
			53504,
			21248,
			-5486
		},
		{
			53248,
			24576,
			-5262
		}
	};
	
	private static final String[] TEXTS =
	{
		"$s1. Stop kidding yourself about your own powerlessness!",
		"$s1. I'll make you feel what true fear is!",
		"You're really stupid to have challenged me. $s1! Get ready!",
		"$s1. Do you think that's going to work?!"
	};
	
	private static final int ORFEN = 12169;
	private static final int RAIKEL_LEOS = 12171;
	private static final int RIBA_IREN = 12173;
	
	private static final byte ALIVE = 0;
	private static final byte DEAD = 1;
	
	private boolean _isTeleported;
	private final List<L2Attackable> _minions = new ArrayList<>();
	private final L2BossZone _zone;
	
	public static void main(String[] args)
    {
        // Quest class
        new Orfen();
    }
	
	public Orfen()
	{
		super(-1, "orfen", "ai");
		registerNPC(ORFEN);
		registerNPC(RAIKEL_LEOS);
		registerNPC(RIBA_IREN);
		
		_isTeleported = false;
		_zone = GrandBossManager.getInstance().getZone(POS[0][0], POS[0][1], POS[0][2]);
		StatsSet info = GrandBossManager.getInstance().getStatsSet(ORFEN);
		int status = GrandBossManager.getInstance().getBossStatus(ORFEN);
		if (status == DEAD)
		{
			// Load the unlock date and time for Orfen from DB
			long temp = info.getLong("respawn_time") - System.currentTimeMillis();
			// If Orfen is locked until a certain time, mark it so and start the unlock timer
			// the unlock time has not yet expired.
			if (temp > 0)
			{
				startQuestTimer("orfen_unlock", temp, null, null);
			}
			else
			{
				// the time has already expired while the server was offline. Immediately spawn Orfen.
				int i = Rnd.get(10);
				int x = 0;
				int y = 0;
				int z = 0;
				if (i < 4)
				{
					x = POS[1][0];
					y = POS[1][1];
					z = POS[1][2];
				}
				else if (i < 7)
				{
					x = POS[2][0];
					y = POS[2][1];
					z = POS[2][2];
				}
				else
				{
					x = POS[3][0];
					y = POS[3][1];
					z = POS[3][2];
				}
				
				L2GrandBossInstance orfen = (L2GrandBossInstance) addSpawn(ORFEN, x, y, z, 0, false, 0);
				GrandBossManager.getInstance().setBossStatus(ORFEN, ALIVE);
				spawnBoss(orfen);
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
			L2GrandBossInstance orfen = (L2GrandBossInstance) addSpawn(ORFEN, loc_x, loc_y, loc_z, heading, false, 0);
			orfen.setCurrentHpMp(hp, mp);
			spawnBoss(orfen);
		}
	}
	
	public void setSpawnPoint(L2NpcInstance npc, int index)
	{
		((L2Attackable) npc).clearAggroList();
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
		L2Spawn spawn = npc.getSpawn();
		spawn.setLocX(POS[index][0]);
		spawn.setLocY(POS[index][1]);
		spawn.setLocZ(POS[index][2]);
		npc.teleToLocation(POS[index][0], POS[index][1], POS[index][2]);
	}
	
	public void spawnBoss(L2GrandBossInstance npc)
	{
		GrandBossManager.getInstance().addBoss(npc);
		npc.broadcastPacket(new PlaySound(1, "BS01_A", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
		startQuestTimer("check_orfen_pos", 10000, npc, null, true);
		
		// Spawn minions
		int x = npc.getX();
		int y = npc.getY();
		L2NpcInstance mob;
		mob = addSpawn(RAIKEL_LEOS, x + 100, y + 100, npc.getZ(), 0, false, 0);
		_minions.add((L2Attackable) mob);
		mob = addSpawn(RAIKEL_LEOS, x + 100, y - 100, npc.getZ(), 0, false, 0);
		_minions.add((L2Attackable) mob);
		mob = addSpawn(RAIKEL_LEOS, x - 100, y + 100, npc.getZ(), 0, false, 0);
		_minions.add((L2Attackable) mob);
		mob = addSpawn(RAIKEL_LEOS, x - 100, y - 100, npc.getZ(), 0, false, 0);
		_minions.add((L2Attackable) mob);
		startQuestTimer("check_minion_loc", 10000, npc, null, true);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("orfen_unlock"))
		{
			int i = Rnd.get(10);
			int x = 0;
			int y = 0;
			int z = 0;
			if (i < 4)
			{
				x = POS[1][0];
				y = POS[1][1];
				z = POS[1][2];
			}
			else if (i < 7)
			{
				x = POS[2][0];
				y = POS[2][1];
				z = POS[2][2];
			}
			else
			{
				x = POS[3][0];
				y = POS[3][1];
				z = POS[3][2];
			}
			
			L2GrandBossInstance orfen = (L2GrandBossInstance) addSpawn(ORFEN, x, y, z, 0, false, 0);
			GrandBossManager.getInstance().setBossStatus(ORFEN, ALIVE);
			spawnBoss(orfen);
		}
		else if (event.equalsIgnoreCase("check_orfen_pos"))
		{
			if ((_isTeleported && (npc.getCurrentHp() > (npc.getMaxHp() * 0.95))) || (!_zone.isInsideZone(npc) && !_isTeleported))
			{
				setSpawnPoint(npc, Rnd.get(3) + 1);
				_isTeleported = false;
			}
			else if (_isTeleported && !_zone.isInsideZone(npc))
			{
				setSpawnPoint(npc, 0);
			}
		}
		else if (event.equalsIgnoreCase("check_minion_loc"))
		{
			for (int i = 0; i < _minions.size(); i++)
			{
				L2Attackable mob = _minions.get(i);
				if (!npc.isInsideRadius(mob, 3000, false, false))
				{
					mob.teleToLocation(npc.getX(), npc.getY(), npc.getZ());
					((L2Attackable) npc).clearAggroList();
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
				}
			}
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
		else if (event.equalsIgnoreCase("spawn_minion"))
		{
			_minions.add((L2Attackable) addSpawn(RAIKEL_LEOS, npc.getX(), npc.getY(), npc.getZ(), 0, false, 0));
		}
		
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (npc.getNpcId() == ORFEN)
		{
			L2Character originalCaster = isPet ? caster.getPet() : caster;
			if (skill.getAggroPoints() > 0 && (Rnd.get(5) == 0) && npc.isInsideRadius(originalCaster, 1000, false, false))
			{
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), TEXTS[Rnd.get(4)], caster.getName()));
				originalCaster.teleToLocation(npc.getX(), npc.getY(), npc.getZ());
				npc.setTarget(originalCaster);
				npc.doCast(SkillTable.getInstance().getInfo(4064, 1));
			}
		}
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public String onFactionCall(L2NpcInstance npc, L2NpcInstance caller, L2PcInstance attacker, boolean isPet)
	{
		if ((caller == null) || (npc == null))
		{
			return super.onFactionCall(npc, caller, attacker, isPet);
		}
		
		int npcId = npc.getNpcId();
		int callerId = caller.getNpcId();
		if ((npcId == RAIKEL_LEOS) && (Rnd.get(20) == 0))
		{
			npc.setTarget(attacker);
			npc.doCast(SkillTable.getInstance().getInfo(4067, 4));
		}
		else if (npcId == RIBA_IREN)
		{
			int chance = 1;
			if (callerId == ORFEN)
			{
				chance = 9;
			}
			if ((callerId != RIBA_IREN) && (caller.getCurrentHp() < (caller.getMaxHp() / 2)) && (Rnd.get(10) < chance))
			{
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
				npc.setTarget(caller);
				npc.doCast(SkillTable.getInstance().getInfo(4516, 1));
			}
		}
		return super.onFactionCall(npc, caller, attacker, isPet);
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == ORFEN)
		{
			if (((npc.getCurrentHp() - damage) < (npc.getMaxHp() / 2)) && !_isTeleported)
			{
				setSpawnPoint(npc, 0);
				_isTeleported = true;
			}
			else if (npc.isInsideRadius(attacker, 1000, false, false) && !npc.isInsideRadius(attacker, 300, false, false) && (Rnd.get(10) == 0))
			{
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), TEXTS[Rnd.get(3)], attacker.getName()));
				attacker.teleToLocation(npc.getX(), npc.getY(), npc.getZ());
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4064, 1));
			}
		}
		else if (npcId == RIBA_IREN)
		{
			if ((npc.getCurrentHp() - damage) < (npc.getMaxHp() / 2))
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4516, 1));
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		if (npc.getNpcId() == ORFEN)
		{
			npc.broadcastPacket(new PlaySound(1, "BS02_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
			GrandBossManager.getInstance().setBossStatus(ORFEN, DEAD);
			// Time is 48hour +/- 20hour
			long respawnTime = ((long)Config.ORFEN_SPAWN_INTERVAL + Rnd.get(-Config.ORFEN_SPAWN_RANDOM_INTERVAL, Config.ORFEN_SPAWN_RANDOM_INTERVAL)) * 3600000L;
			startQuestTimer("orfen_unlock", respawnTime, null, null);
			// also save the respawn time so that the info is maintained past reboots
			StatsSet info = GrandBossManager.getInstance().getStatsSet(ORFEN);
			info.set("respawn_time", System.currentTimeMillis() + respawnTime);
			GrandBossManager.getInstance().setStatsSet(ORFEN, info);
			cancelQuestTimer("check_minion_loc", npc, null);
			cancelQuestTimer("check_orfen_pos", npc, null);
			startQuestTimer("despawn_minions", 20000, null, null);
			cancelQuestTimers("spawn_minion");
		}
		else if ((GrandBossManager.getInstance().getBossStatus(ORFEN) == ALIVE) && (npc.getNpcId() == RAIKEL_LEOS))
		{
			_minions.remove(npc);
			startQuestTimer("spawn_minion", 360000, npc, null);
		}
		return super.onKill(npc, killer, isPet);
	}
}