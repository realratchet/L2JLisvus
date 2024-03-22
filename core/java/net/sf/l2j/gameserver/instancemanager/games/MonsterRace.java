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
package net.sf.l2j.gameserver.instancemanager.games;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RaceManagerInstance;
import net.sf.l2j.gameserver.network.serverpackets.DeleteObject;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.MonRaceInfo;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.util.Broadcast;
import net.sf.l2j.util.Rnd;

public class MonsterRace
{
    public static final int LANES = 8;
    public static final int WINDOW_START = 0;
    
    private static final int[][] CODES =
    {
        {
            -1,
            0
        },
        {
            0,
            15322
        },
        {
            13765,
            -1
        }
    };
    
    private static final Integer[] AVAILABLE_MONSTER_IDS =
    {
        8003,
        8004,
        8005,
        8006,
        8007,
        8008,
        8009,
        8010,
        8011,
        8012,
        8013,
        8014,
        8015,
        8016,
        8017,
        8018,
        8019,
        8020,
        8021,
        8022,
        8023,
        8024,
        8025,
        8026
    };
    
    // States
    private static final byte ACCEPTING_BETS = 0;
    private static final byte WAITING = 1;
    private static final byte STARTING_RACE = 2;
    private static final byte RACE_END = 3;
    
    // Time Constants
    private static final int SECOND_IN_MILLIS = 1000;
    private static final int MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;

    private static final int BASE_RACE_NUMBER = 4;
    
    private final L2NpcInstance[] _monsters;
    
    private final Set<L2RaceManagerInstance> _managers;
    private final int[][] _speeds;
    private final int[] _first, _second;
    
    private int _raceNumber = BASE_RACE_NUMBER;
    private int _minutesLeft = 5;
    private byte _state = RACE_END;
    
    public static MonsterRace getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private MonsterRace()
    {
        _monsters = new L2NpcInstance[8];
        _managers = ConcurrentHashMap.newKeySet();
        _speeds = new int[8][20];
        _first = new int[2];
        _second = new int[2];
    }
    
    public void scheduleTimers()
    {
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Announcement(SystemMessage.MONSRACE_TICKETS_AVAILABLE_FOR_S1_RACE), 0, 10 * MINUTE_IN_MILLIS);
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Announcement(SystemMessage.MONSRACE_TICKETS_NOW_AVAILABLE_FOR_S1_RACE), 30 * SECOND_IN_MILLIS, 10 * MINUTE_IN_MILLIS);
        
        for (int i = 2; i <= 6; i++)
        {
            ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Announcement(SystemMessage.MONSRACE_TICKETS_STOP_IN_S1_MINUTES), i * MINUTE_IN_MILLIS, 10 * MINUTE_IN_MILLIS);
        }
        
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> {
            makeAnnouncement(SystemMessage.MONSRACE_TICKET_SALES_CLOSED);
            makeAnnouncement(SystemMessage.MONSRACE_BEGINS_IN_S1_MINUTES);
        }, 7 * MINUTE_IN_MILLIS, 10 * MINUTE_IN_MILLIS);
        
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Announcement(SystemMessage.MONSRACE_BEGINS_IN_S1_MINUTES), 8 * MINUTE_IN_MILLIS, 10 * MINUTE_IN_MILLIS);
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Announcement(SystemMessage.MONSRACE_BEGINS_IN_30_SECONDS), 8 * MINUTE_IN_MILLIS + 30 * SECOND_IN_MILLIS, 10 * MINUTE_IN_MILLIS);
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Announcement(SystemMessage.MONSRACE_COUNTDOWN_IN_FIVE_SECONDS), 8 * MINUTE_IN_MILLIS + 50 * SECOND_IN_MILLIS, 10 * MINUTE_IN_MILLIS);
        
        for (int i = 55; i <= 59; i++)
        {
            ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Announcement(SystemMessage.MONSRACE_BEGINS_IN_S1_SECONDS), 8 * MINUTE_IN_MILLIS + i * SECOND_IN_MILLIS, 10 * MINUTE_IN_MILLIS);
        }
        
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Announcement(SystemMessage.MONSRACE_RACE_START), 9 * MINUTE_IN_MILLIS, 10 * MINUTE_IN_MILLIS);
    }
    
    public void addManager(L2RaceManagerInstance npc)
    {
        _managers.add(npc);
    }

    public void removeManager(L2RaceManagerInstance npc)
    {
        _managers.remove(npc);
    }
    
    public int getRaceNumber()
    {
        return _raceNumber;
    }
    
    public boolean isAcceptingBets()
    {
        return _state == ACCEPTING_BETS;
    }
    
    public void prepareMonsterParticipants()
    {
        List<Integer> availableNpcIds = new ArrayList<>(Arrays.asList(AVAILABLE_MONSTER_IDS));
        
        for (int i = 0; i < 8; i++)
        {
            int index = Rnd.get(availableNpcIds.size());
            int npcId = availableNpcIds.remove(index);
            
            try
            {
                L2NpcTemplate template = NpcTable.getInstance().getTemplate(npcId);
                Constructor<?> _constructor = Class.forName("net.sf.l2j.gameserver.model.actor.instance." + template.type + "Instance").getConstructors()[0];
                int objectId = IdFactory.getInstance().getNextId();
                _monsters[i] = (L2NpcInstance) _constructor.newInstance(objectId, template);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void calculateSpeeds()
    {
        int total = 0;
        _first[1] = 0;
        _second[1] = 0;
        
        for (int i = 0; i < 8; i++)
        {
            total = 0;
            for (int j = 0; j < 20; j++)
            {
                if (j == 19)
                    _speeds[i][j] = 100;
                else
                    _speeds[i][j] = Rnd.get(60) + 65;
                total += _speeds[i][j];
            }
            
            if (total >= _first[1])
            {
                _second[0] = _first[0];
                _second[1] = _first[1];
                _first[0] = 8 - i;
                _first[1] = total;
            }
            else if (total >= _second[1])
            {
                _second[0] = 8 - i;
                _second[1] = total;
            }
        }
    }
    
    private void startRace()
    {
        broadcastToNearbyPlayers(new PlaySound(1, "S_Race", 0, 0, 0, 0, 0));
        broadcastToNearbyPlayers(new PlaySound(0, "ItemSound2.race_start", 1, 121209259, 12125, 182487, -3559));
        broadcastToNearbyPlayers(new MonRaceInfo(CODES[1][0], CODES[1][1], _monsters, _speeds));
        
        ThreadPoolManager.getInstance().scheduleGeneral(() -> {
            broadcastToNearbyPlayers(new MonRaceInfo(CODES[2][0], CODES[2][1], _monsters, _speeds));
            ThreadPoolManager.getInstance().scheduleGeneral(() -> {
                endRace();
            }, 30000);
        }, 5000);
    }
    
    private void endRace()
    {
        makeAnnouncement(SystemMessage.MONSRACE_FIRST_PLACE_S1_SECOND_S2);
        makeAnnouncement(SystemMessage.MONSRACE_RACE_END);

        // Reset race number if it ever reaches maximum
        if (_raceNumber == Integer.MAX_VALUE)
        {
            _raceNumber = BASE_RACE_NUMBER;
        }
        else
        {
            _raceNumber++;
        }
        
        for (L2NpcInstance npc : _monsters)
        {
            broadcastToNearbyPlayers(new DeleteObject(npc));
        }
    }
    
    public void makeAnnouncement(int type)
    {
        SystemMessage sm = new SystemMessage(type);
        switch (type)
        {
            case SystemMessage.MONSRACE_TICKETS_AVAILABLE_FOR_S1_RACE:
            case SystemMessage.MONSRACE_TICKETS_NOW_AVAILABLE_FOR_S1_RACE:
                if (_state != ACCEPTING_BETS)
                {
                    _state = ACCEPTING_BETS;
                    prepareMonsterParticipants();
                    calculateSpeeds();
                    broadcastToNearbyPlayers(new MonRaceInfo(CODES[0][0], CODES[0][1], _monsters, _speeds));
                }
                sm.addNumber(_raceNumber);
                break;
            case SystemMessage.MONSRACE_TICKETS_STOP_IN_S1_MINUTES:
            case SystemMessage.MONSRACE_BEGINS_IN_S1_MINUTES:
            case SystemMessage.MONSRACE_BEGINS_IN_S1_SECONDS:
                sm.addNumber(_minutesLeft);
                if (type == SystemMessage.MONSRACE_BEGINS_IN_S1_MINUTES)
                    sm.addNumber(_raceNumber);
                _minutesLeft--;
                break;
            case SystemMessage.MONSRACE_BEGINS_IN_30_SECONDS:
                sm.addNumber(_raceNumber);
                break;
            case SystemMessage.MONSRACE_TICKET_SALES_CLOSED:
                sm.addNumber(_raceNumber);
                _state = WAITING;
                _minutesLeft = 2;
                break;
            case SystemMessage.MONSRACE_COUNTDOWN_IN_FIVE_SECONDS:
            case SystemMessage.MONSRACE_RACE_END:
                sm.addNumber(_raceNumber);
                _minutesLeft = 5;
                break;
            case SystemMessage.MONSRACE_FIRST_PLACE_S1_SECOND_S2:
                _state = RACE_END;
                sm.addNumber(getFirstPlace());
                sm.addNumber(getSecondPlace());
                break;
        }
        broadcastToNearbyPlayers(sm);
        
        if (type == SystemMessage.MONSRACE_RACE_START)
        {
            _state = STARTING_RACE;
            startRace();
            _minutesLeft = 5;
        }
    }
    
    private void broadcastToNearbyPlayers(L2GameServerPacket pkt)
    {
        for (L2RaceManagerInstance manager : _managers)
        {
            if (!manager.isDead())
                Broadcast.toKnownPlayers(manager, pkt);
        }
    }
    
    /**
     * @return Returns the _monsters.
     */
    public L2NpcInstance[] getMonsters()
    {
        return _monsters;
    }
    
    /**
     * @return Returns the _speeds.
     */
    public int[][] getSpeeds()
    {
        return _speeds;
    }
    
    public int getFirstPlace()
    {
        return _first[0];
    }
    
    public int getSecondPlace()
    {
        return _second[0];
    }
    
    class Announcement implements Runnable
    {
        private int type;
        
        public Announcement(int pType)
        {
            this.type = pType;
        }
        
        @Override
        public void run()
        {
            makeAnnouncement(type);
        }
    }
    
    private static class SingletonHolder
    {
        protected static final MonsterRace _instance = new MonsterRace();
    }
}