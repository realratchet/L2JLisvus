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
package net.sf.l2j.gameserver.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.network.serverpackets.Earthquake;
import net.sf.l2j.util.Rnd;

/** 
 * Thanks to L2Fortress and balancer.ru - kombat
 */ 
public class DimensionalRift
{
    private byte _type;
    private L2Party _party;
    private byte jumps_current = 0;

    private Timer teleporterTimer;
    private TimerTask teleporterTimerTask;
    private Timer spawnTimer;
    private TimerTask spawnTimerTask;

    private Future<?> earthQuakeTask;

    private byte _choosenRoom = -1;
    private boolean _hasJumped = false;
    private List<Byte> _completedRooms = new ArrayList<>();
    private List<L2PcInstance> _deadPlayers = new CopyOnWriteArrayList<>();
    private boolean isBossRoom = false;

    public DimensionalRift(L2Party party, byte type, byte room)
    {
        DimensionalRiftManager.getInstance().getRoom(type, room).setpartyInside(true);
        _type = type;
        _party = party;
        _choosenRoom = room;
        int[] coords = getRoomCoord(room);
        party.setDimensionalRift(this);
        for (L2PcInstance p : party.getPartyMembers())
        {
            QuestState qs = p.getQuestState("635_InTheDimensionalRift");
            if (qs != null && qs.getInt("cond") != 1)
                qs.set("cond", "1");

            p.teleToLocation(coords[0], coords[1], coords[2]);
        }
        createSpawnTimer(_choosenRoom);
        createTeleporterTimer();
    }

    public byte getType()
    {
        return _type;
    }

    public byte getCurrentRoom()
    {
        return _choosenRoom;
    }

    protected void createTeleporterTimer()
    {
        if (_party == null)
            return;

        if (teleporterTimerTask != null)
        {
            teleporterTimerTask.cancel();
            teleporterTimerTask = null;
        }

        if (teleporterTimer != null)
        {
            teleporterTimer.cancel();
            teleporterTimer = null;
        }

        if (earthQuakeTask != null)
        {
            earthQuakeTask.cancel(false);
            earthQuakeTask = null;
        }

        teleporterTimer = new Timer();
        teleporterTimerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (_choosenRoom > -1)
                    DimensionalRiftManager.getInstance().getRoom(_type, _choosenRoom).unspawn().setpartyInside(false);

                // Check if party has jumps left, is not dispersed, and
                // there is at least 1 alive
                if (jumps_current < getMaxJumps()
                        && _party.getMemberCount() > 1
                        && _party.getMemberCount() > _deadPlayers.size())
                {
                    jumps_current++;

                    _completedRooms.add(_choosenRoom);
                    _choosenRoom = -1;

                    for (L2PcInstance p : _party.getPartyMembers())
                    {
                        if (_deadPlayers.contains(p))
                            p.setIsPendingRevive(true);
                        teleportToNextRoom(p);
                    }
                    createTeleporterTimer();
                    createSpawnTimer(_choosenRoom);
                }
                else
                {
                    for (L2PcInstance p : _party.getPartyMembers())
                    {
                        if (_deadPlayers.contains(p))
                            p.setIsPendingRevive(true);
                        teleportToWaitingRoom(p);
                    }
                    killRift();
                    cancel();
                }
            }
        };

        long jumpTime = calcTimeToNextJump();
        teleporterTimer.schedule(teleporterTimerTask, jumpTime); //Teleporter task, 8-10 minutes

        earthQuakeTask = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
        {
            @Override
			public void run()
            {
                for (L2PcInstance p : _party.getPartyMembers())
                    p.sendPacket(new Earthquake(p.getX(), p.getY(), p.getZ(), 65, 9));
            }
        }, jumpTime - 7000);
    }

    public void createSpawnTimer(final byte room)
    {
        if (spawnTimerTask != null)
        {
            spawnTimerTask.cancel();
            spawnTimerTask = null;
        }

        if (spawnTimer != null)
        {
            spawnTimer.cancel();
            spawnTimer = null;
        }

        spawnTimer = new Timer();
        spawnTimerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                DimensionalRiftManager.getInstance().getRoom(_type, room).spawn();
            }
        };

        spawnTimer.schedule(spawnTimerTask, Config.RIFT_SPAWN_DELAY);
    }

    public void partyMemberExited(L2PcInstance player, boolean hasLeft)
    {
        if (!hasLeft || _party.getMemberCount() <= 2)
        {
            for (L2PcInstance p : _party.getPartyMembers())
            {
                if (_deadPlayers.contains(p))
                    p.setIsPendingRevive(true);
                teleportToWaitingRoom(p);
            }
            killRift();
        }
        else
        {
            if (_deadPlayers.contains(player))
            {
                _deadPlayers.remove(player);
                player.setIsPendingRevive(true);
            }
            teleportToWaitingRoom(player);
        }
    }

    public void manualTeleport(L2PcInstance player, L2NpcInstance npc)
    {
        if (!player.isInParty() || !player.getParty().isInDimensionalRift())
            return;

        if (player.getObjectId() != player.getParty().getPartyLeaderOID())
        {
            DimensionalRiftManager.getInstance().showHtmlFile(player, "data/html/seven_signs/rift/NotPartyLeader.htm", npc);
            return;
        }

        if (_hasJumped)
        {
            DimensionalRiftManager.getInstance().showHtmlFile(player, "data/html/seven_signs/rift/AlreadyTeleported.htm", npc);
            return;
        }
		_hasJumped = true;

        DimensionalRiftManager.getInstance().getRoom(_type, _choosenRoom).unspawn().setpartyInside(false);
        _completedRooms.add(_choosenRoom);
        _choosenRoom = -1;

        for (L2PcInstance p : _party.getPartyMembers())
            teleportToNextRoom(p);

        DimensionalRiftManager.getInstance().getRoom(_type, _choosenRoom).setpartyInside(true);

        createSpawnTimer(_choosenRoom);
        createTeleporterTimer();
    }

    public void manualExitRift(L2PcInstance player, L2NpcInstance npc)
    {
        if (!player.isInParty() || !player.getParty().isInDimensionalRift())
            return;

        if (player.getObjectId() != player.getParty().getPartyLeaderOID())
        {
            DimensionalRiftManager.getInstance().showHtmlFile(player, "data/html/seven_signs/rift/NotPartyLeader.htm", npc);
            return;
        }

        for (L2PcInstance p : player.getParty().getPartyMembers())
        {
            if (_deadPlayers.contains(p))
                p.setIsPendingRevive(true);
            teleportToWaitingRoom(p);
        }
        killRift();
    }

    protected void teleportToNextRoom(L2PcInstance player)
    {
        if (_choosenRoom == -1)
        {
            List<Byte> emptyRooms;
            do
            {
                emptyRooms = DimensionalRiftManager.getInstance().getFreeRooms(_type);
                // Do not tp in the same room a second time
                emptyRooms.removeAll(_completedRooms);
                // If no room left, find any empty
                if (emptyRooms.isEmpty())
                    emptyRooms = DimensionalRiftManager.getInstance().getFreeRooms(_type);
                _choosenRoom = emptyRooms.get(Rnd.get(1, emptyRooms.size())-1);
            }
            while (DimensionalRiftManager.getInstance().getRoom(_type, _choosenRoom).ispartyInside());
        }

        DimensionalRiftManager.getInstance().getRoom(_type, _choosenRoom).setpartyInside(true);
        checkBossRoom(_choosenRoom);
        int[] coords = getRoomCoord(_choosenRoom);
        player.teleToLocation(coords[0], coords[1], coords[2]);
    }

    protected void teleportToWaitingRoom(L2PcInstance player)
    {
        DimensionalRiftManager.getInstance().teleportToWaitingRoom(player);

        QuestState qs = player.getQuestState("635_InTheDimensionalRift");
        if (qs != null && qs.getInt("cond") == 1)
            qs.set("cond", "0");
    }

    public void killRift()
    {
        _completedRooms = null;

        if (_party != null)
            _party.setDimensionalRift(null);

        _party = null;
        _deadPlayers = null;

        if (earthQuakeTask != null)
        {
            earthQuakeTask.cancel(false);
            earthQuakeTask = null;
        }

        DimensionalRiftManager.getInstance().getRoom(_type, _choosenRoom).unspawn().setpartyInside(false);
        DimensionalRiftManager.getInstance().killRift(this);
    }

    public Timer getTeleportTimer()
    {
        return teleporterTimer;
    }

    public TimerTask getTeleportTimerTask()
    {
        return teleporterTimerTask;
    }

    public Timer getSpawnTimer()
    {
        return spawnTimer;
    }

    public TimerTask getSpawnTimerTask()
    {
        return spawnTimerTask;
    }

    public void setTeleportTimer(Timer t)
    {
        teleporterTimer = t;
    }

    public void setTeleportTimerTask(TimerTask tt)
    {
        teleporterTimerTask = tt;
    }

    public void setSpawnTimer(Timer t)
    {
        spawnTimer = t;
    }

    public void setSpawnTimerTask(TimerTask st)
    {
        spawnTimerTask = st;
    }

    private long calcTimeToNextJump()
    {
        int time = Rnd.get(Config.RIFT_AUTO_JUMPS_TIME_MIN, Config.RIFT_AUTO_JUMPS_TIME_MAX) * 1000;

        if (isBossRoom)
            return (long)(time * Config.RIFT_BOSS_ROOM_TIME_MULTIPLIER);

        return time;
    }

    public void memberDead(L2PcInstance player)
    {
        if (!_deadPlayers.contains(player))
        {
            _deadPlayers.add(player);
            player.sendMessage("Even if party cannot revive you, you will automatically be ressurected at the next room (excluding the use of Chance Card, and the death of all party members).");
        }
    }

    public void memberRessurected(L2PcInstance player)
    {
        if (_deadPlayers.contains(player))
            _deadPlayers.remove(player);
    }

    public void checkBossRoom(byte room)
    {
        isBossRoom = DimensionalRiftManager.getInstance().getRoom(_type, room).isBossRoom();
    }

    public int[] getRoomCoord(byte room)
    {
        return DimensionalRiftManager.getInstance().getRoom(_type, room).getTeleportCoords();
    }

    public byte getMaxJumps()
    {
        if (Config.RIFT_MAX_JUMPS <= 8 && Config.RIFT_MAX_JUMPS >= 1)
            return (byte) Config.RIFT_MAX_JUMPS;
        return 4;
    }
}