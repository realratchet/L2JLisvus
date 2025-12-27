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
package custom.TvTEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.quest.L2EventScript;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.zone.type.L2ArenaZone;
import net.sf.l2j.gameserver.network.serverpackets.ChangeWaitType;

/**
 * @author DnR
 */
public class TvTEvent extends L2EventScript
{
    private static final long EVENT_NEXT_INTERVAL = 18000 * 1000;
    private static final long EVENT_DURATION = 1800 * 1000;
    private static final long EVENT_INITIAL_DELAY = 900 * 1000;
    private static final long EVENT_REGISTRATION_TIME = 1200 * 1000;
    private static final long PLAYER_RESPAWN_DELAY = 20 * 1000;
    private static final int NPC_MANAGER_ID = 100002;
    private static final byte PLAYER_MIN_LEVEL = 60;
    private static byte PLAYER_MAX_LEVEL = 0;
    private static final int MIN_PARTICIPANTS = 6;
    private static final int MAX_PARTICIPANTS = 40;
    private static final boolean PLAYER_CLEAR_BUFFS_AFTER_RESPAWN = true;
    private static final boolean PLAYER_REWARDS_ON_TIE = false;
    private static final boolean ALLOW_PLAYERS_SAME_IP = true;
    private static final int[] ARENA_DOOR_IDS = new int[]
    {
        24190002,
        24190003
    };
    private static final int[][] REWARD_DATA =
    {
        {
            57,
            10000000
        },
        {
            6393,
            50
        },
        {
            5592,
            20
        }
    };
    
    private static final String[] VOICED_COMMANDS =
    {
        "tvt",
        "tvtjoin",
        "tvtleave"
    };
    
    private final List<L2PcInstance> _registered = new CopyOnWriteArrayList<>();
    private final List<L2PcInstance> _blueTeam = new CopyOnWriteArrayList<>();
    private final List<L2PcInstance> _redTeam = new CopyOnWriteArrayList<>();
    
    private final L2NpcInstance _npcManager;
    private final L2ArenaZone _arenaZone;
    private final L2ItemInstance[] _rewards;
    private final L2DoorInstance[] _doors;
    
    private final Location _npcLoc = new Location(151808, 46864, -3408, 32768);
    private final Location _blueTeamLoc = new Location(148476, 46061, -3411);
    private final Location _redTeamLoc = new Location(150480, 47444, -3411);
    
    private final TvTCommand _voicedCommand = new TvTCommand();
    
    // Team kills
    private int _blueTeamKills = 0;
    private int _redTeamKills = 0;
    
    public static void main(String[] args)
    {
        // Quest class
        new TvTEvent();
    }
    
    public TvTEvent()
    {
        super(-1, TvTEvent.class.getSimpleName(), "custom");
        
        _npcManager = new L2NpcInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(NPC_MANAGER_ID));
        _npcManager.setTitle("TvT Registration");
        _npcManager.setHeading(_npcLoc.getHeading());
        _arenaZone = ZoneManager.getInstance().getZoneById(11012, L2ArenaZone.class);
        
        List<L2DoorInstance> doors = new ArrayList<>();
        for (int doorId : ARENA_DOOR_IDS)
        {
            L2DoorInstance door = DoorTable.getInstance().getDoor(doorId);
            if (door != null)
            {
                doors.add(door);
            }
        }
        _doors = doors.toArray(new L2DoorInstance[doors.size()]);
        
        List<L2ItemInstance> rewards = new ArrayList<>();
        for (int[] rewardInfo : REWARD_DATA)
        {
            L2ItemInstance reward = ItemTable.getInstance().createDummyItem(Integer.valueOf(rewardInfo[0]));
            if (reward != null)
            {
                reward.setCount(Integer.valueOf(rewardInfo[1]));
                rewards.add(reward);
            }
        }
        _rewards = rewards.toArray(new L2ItemInstance[rewards.size()]);
        
        if (_voicedCommand != null)
        {
            VoicedCommandHandler.getInstance().registerVoicedCommandHandler(_voicedCommand);
        }
        
        addFirstTalkId(NPC_MANAGER_ID);
        addTalkId(NPC_MANAGER_ID);
        startQuestTimer("tvt_schedule_registration", EVENT_INITIAL_DELAY, null, null);
    }
    
    private void beginRegistration()
    {
        // Set state
        setEventState(EventState.REGISTER);
        
        // Lock arena
        if (_arenaZone != null)
        {
            _arenaZone.setEvent(this);
        }
        
        // Spawn TvT manager NPC
        _npcManager.spawnMe(_npcLoc.getX(), _npcLoc.getY(), _npcLoc.getZ());
        
        Announcements.getInstance().announceToAll("TvT Event: Registration opened for " + (EVENT_REGISTRATION_TIME / 60 / 1000) + " minute(s).");
        // If zone has name, display event location
        if (_arenaZone != null && _arenaZone.getName() != null)
        {
            Announcements.getInstance().announceToAll("TvT Event: Event will take place in " + _arenaZone.getName() + ".");
        }
        
        startQuestTimer("tvt_registration_minute_15", EVENT_REGISTRATION_TIME - 900000, null, null);
    }
    
    private void startEvent()
    {
        int registeredCount = _registered.size();
        
        if (registeredCount >= 2 && registeredCount >= MIN_PARTICIPANTS && getEventState() != EventState.INITIAL)
        {
            // Prepare arena for event
            prepareArena();
            
            // Port players and start event
            Announcements.getInstance().announceToAll("TvT Event: Event has started!");
            portTeamsToArena();
            
            startQuestTimer("tvt_remaining_minute_15", EVENT_DURATION - 900000, _npcManager, null);
        }
        else
        {
            if (getEventState() == EventState.INITIAL)
            {
                Announcements.getInstance().announceToAll("TvT Event: Event was cancelled.");
            }
            else
            {
                Announcements.getInstance().announceToAll("TvT Event: Event was cancelled due to lack of participation.");
            }
            
            _registered.clear();
            resetEvent();
            startQuestTimer("tvt_schedule_registration", EVENT_NEXT_INTERVAL, null, null);
        }
    }
    
    private void finishEvent()
    {
        if (getEventState() == EventState.INITIAL)
        {
            Announcements.getInstance().announceToAll("TvT Event: Event was cancelled.");
        }
        else
        {
            Announcements.getInstance().announceToAll("TvT Event: Blue Team kills: " + _blueTeamKills + " , Red Team kills: " + _redTeamKills + ".");
        }
        
        // Shutting down event
        cleanUpTeams();
        resetEvent();
        startQuestTimer("tvt_schedule_registration", EVENT_NEXT_INTERVAL, null, null);
    }
    
    private void resetEvent()
    {
        // Unlock arena
        if (_arenaZone != null)
        {
            _arenaZone.setEvent(null);
        }
        
        // Open doors
        toggleArenaDoors(true);
        
        // Delete registration NPC if spawned
        if (_npcManager != null)
        {
            if (_npcManager.isVisible())
            {
                _npcManager.deleteMe();
            }
        }
        
        setEventState(EventState.INITIAL);
    }
    
    /**
     * Handles arena doors open state.
     * @param open
     */
    private void toggleArenaDoors(boolean open)
    {
        if (_doors == null)
        {
            return;
        }
        
        for (L2DoorInstance door : _doors)
        {
            // Open door
            if (open)
            {
                door.openMe();
            }
            // Close door
            else
            {
                door.closeMe();
            }
        }
    }
    
    /**
     * Cleans up arena for event to start.
     */
    private void prepareArena()
    {
        // Banish players
        if (_arenaZone != null)
        {
            for (L2Character activeChar : _arenaZone.getCharacterList())
            {
                L2PcInstance player = activeChar.getActingPlayer();
                if (player == null)
                {
                    continue;
                }
                
                player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
            }
        }
        
        // Close doors
        toggleArenaDoors(false);
    }
    
    /**
     * Ports teams to arena.
     */
    private void portTeamsToArena()
    {
        Collections.shuffle(_registered);
        
        for (L2PcInstance player : _registered)
        {
            // First create 2 event teams
            if (_blueTeam.size() > _redTeam.size())
            {
                _redTeam.add(player);
                player.setEventTeam(2);
            }
            else
            {
                _blueTeam.add(player);
                player.setEventTeam(1);
            }
            
            // Abort casting if player casting
            if (player.isCastingNow())
            {
                player.abortCast();
            }
            
            player.getAppearance().setVisible();
            
            if (player.isDead())
            {
                player.doRevive();
            }
            else
            {
                player.setCurrentCp(player.getMaxCp());
                player.setCurrentHp(player.getMaxHp());
                player.setCurrentMp(player.getMaxMp());
            }
            
            // Remove Buffs if needed
            if (PLAYER_CLEAR_BUFFS_AFTER_RESPAWN)
            {
                player.stopAllEffects();
                player.clearCharges();
            }
            
            // Remove Summon's Buffs
            L2Summon summon = player.getPet();
            if (summon != null)
            {
                if (summon.isCastingNow())
                {
                    summon.abortCast();
                }
                
                if (summon instanceof L2PetInstance)
                {
                    summon.unSummon(player);
                }
                else if (PLAYER_CLEAR_BUFFS_AFTER_RESPAWN)
                {
                    summon.stopAllEffects();
                }
            }
            
            // Remove player from his party
            if (player.getParty() != null)
            {
                L2Party party = player.getParty();
                party.removePartyMember(player, true);
            }
            
            _registered.remove(player);
        }
        
        setEventState(EventState.STARTED);
        
        // Port teams
        for (L2PcInstance blue : _blueTeam)
        {
            if (blue == null)
            {
                continue;
            }
            
            blue.teleToLocation(_blueTeamLoc.getX(), _blueTeamLoc.getY(), _blueTeamLoc.getZ(), true);
        }
        
        for (L2PcInstance red : _redTeam)
        {
            if (red == null)
            {
                continue;
            }
            
            red.teleToLocation(_redTeamLoc.getX(), _redTeamLoc.getY(), _redTeamLoc.getZ(), true);
        }
    }
    
    /**
     * Increases player team kills.
     * @param player
     * @param target
     */
    private void increaseTeamKills(L2PcInstance player, L2PcInstance target)
    {
        // Increase kills only if victim belonged to enemy team
        if (player.getEventTeam() == 1 && target.getEventTeam() == 2)
        {
            _blueTeamKills++;
        }
        else if (player.getEventTeam() == 2 && target.getEventTeam() == 1)
        {
            _redTeamKills++;
        }
    }
    
    /**
     * Handles teams rewards and teleport.
     */
    private void cleanUpTeams()
    {
        boolean isRewardingOnTie = _blueTeamKills == _redTeamKills && PLAYER_REWARDS_ON_TIE;
        
        // Blue team
        for (L2PcInstance player : _blueTeam)
        {
            if (player == null)
            {
                continue;
            }
            
            // Give rewards
            if (getEventState() != EventState.INITIAL && (_blueTeamKills > _redTeamKills || isRewardingOnTie))
            {
                if (_rewards != null)
                {
                    for (L2ItemInstance reward : _rewards)
                    {
                        player.addItem("TvTReward", reward.getItemId(), reward.getCount(), null, true);
                    }
                }
            }
            
            if (player.isDead())
            {
                player.doRevive();
            }
            
            player.setEventTeam(0);
            cancelQuestTimer("player_revive", null, player);
            player.teleToLocation(_npcLoc.getX(), _npcLoc.getY(), _npcLoc.getZ(), true);
        }
        
        // Red team
        for (L2PcInstance player : _redTeam)
        {
            if (player == null)
            {
                continue;
            }
            
            // Give rewards
            if (getEventState() != EventState.INITIAL && (_blueTeamKills < _redTeamKills || isRewardingOnTie))
            {
                if (_rewards != null)
                {
                    for (L2ItemInstance reward : _rewards)
                    {
                        player.addItem("TvTReward", reward.getItemId(), reward.getCount(), null, true);
                    }
                }
            }
            
            if (player.isDead())
            {
                player.doRevive();
            }
            
            player.setEventTeam(0);
            player.teleToLocation(_npcLoc.getX(), _npcLoc.getY(), _npcLoc.getZ(), true);
        }
        
        // Event ended in a tie and no rewards will be given
        if (getEventState() != EventState.INITIAL && _blueTeamKills == _redTeamKills && !PLAYER_REWARDS_ON_TIE)
        {
            Announcements.getInstance().announceToAll("TvT Event: Event ended in a Tie. No rewards will be given!");
        }
        
        _blueTeam.clear();
        _redTeam.clear();
        _blueTeamKills = 0;
        _redTeamKills = 0;
    }
    
    @Override
    public void unload(boolean removeFromList)
    {
        super.unload(removeFromList);
        
        if (getEventState() == EventState.REGISTER)
        {
            _registered.clear();
            Announcements.getInstance().announceToAll("TvT Event: Event was cancelled.");
        }
        else if (getEventState() == EventState.STARTED)
        {
            setEventState(EventState.INITIAL);
            cleanUpTeams();
            Announcements.getInstance().announceToAll("TvT Event: Event was cancelled.");
        }
        
        resetEvent();
        
        if (_voicedCommand != null)
        {
            VoicedCommandHandler.getInstance().removeVoicedCommandHandler(_voicedCommand);
        }
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        if (event.equalsIgnoreCase("player_revive"))
        {
            if (player.isDead() && !player.isPendingRevive())
            {
                player.setIsPendingRevive(true);
                
                if (player.getEventTeam() == 1)
                {
                    player.teleToLocation(_blueTeamLoc.getX(), _blueTeamLoc.getY(), _blueTeamLoc.getZ(), true);
                }
                else if (player.getEventTeam() == 2)
                {
                    player.teleToLocation(_redTeamLoc.getX(), _redTeamLoc.getY(), _redTeamLoc.getZ(), true);
                }
                else
                {
                    // Player has probably left event
                    player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
                }
            }
        }
        else if (event.equalsIgnoreCase("tvt_event_participation"))
        {
            registerPlayer(player);
        }
        else if (event.equalsIgnoreCase("tvt_event_remove_participation"))
        {
            removePlayer(player);
        }
        else if (event.equalsIgnoreCase("tvt_schedule_registration"))
        {
            beginRegistration();
        }
        else if (event.equalsIgnoreCase("tvt_registration_minute_15"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 15 minutes until registration is closed!");
            startQuestTimer("tvt_registration_minute_10", 300000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_registration_minute_10"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 10 minutes until registration is closed!");
            startQuestTimer("tvt_registration_minute_5", 300000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_registration_minute_5"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 5 minutes until registration is closed!");
            startQuestTimer("tvt_registration_minute_1", 240000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_registration_minute_1"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 1 minute until registration is closed!");
            startQuestTimer("tvt_registration_second_30", 30000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_registration_second_30"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 30 seconds until registration is closed!");
            startQuestTimer("tvt_registration_second_5", 25000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_registration_second_5"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 5 seconds until registration is closed!");
            startQuestTimer("tvt_started", 5000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_started"))
        {
            startEvent();
        }
        else if (event.equalsIgnoreCase("tvt_remaining_minute_15"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 15 minutes until event is finished!");
            startQuestTimer("tvt_remaining_minute_10", 300000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_remaining_minute_10"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 10 minutes until event is finished!");
            startQuestTimer("tvt_remaining_minute_5", 300000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_remaining_minute_5"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 5 minutes until event is finished!");
            startQuestTimer("tvt_remaining_minute_1", 240000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_remaining_minute_1"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 1 minute until event is finished!");
            startQuestTimer("tvt_remaining_second_30", 30000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_remaining_second_30"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 30 seconds until event is finished!");
            startQuestTimer("tvt_remaining_second_5", 25000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_remaining_second_5"))
        {
            Announcements.getInstance().announceToAll("TvT Event: 5 seconds until event is finished!");
            startQuestTimer("tvt_ended", 5000, null, null);
        }
        else if (event.equalsIgnoreCase("tvt_ended"))
        {
            finishEvent();
        }
        return super.onAdvEvent(event, npc, player);
    }
    
    @Override
    public String onFirstTalk(L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = getNoQuestMsg();
        
        QuestState st = player.getQuestState(getName());
        if (st == null)
        {
            st = newQuestState(player);
        }
        
        if (getEventState() == EventState.REGISTER)
        {
            String fileContent = loadHtmlFile(player, _registered.contains(player) ? "TvTEventRemoveParticipation.htm" : "TvTEventParticipation.htm");
            if (fileContent != null)
            {
                fileContent = fileContent.replaceAll("%registeredcount%", String.valueOf(_registered.size()));
                fileContent = fileContent.replaceAll("%minimumplayers%", String.valueOf(MIN_PARTICIPANTS));
                fileContent = fileContent.replaceAll("%maximumplayers%", String.valueOf(MAX_PARTICIPANTS));
                fileContent = fileContent.replaceAll("%minimumlevel%", String.valueOf(PLAYER_MIN_LEVEL));
                fileContent = fileContent.replaceAll("%maximumlevel%", PLAYER_MAX_LEVEL > 0 ? String.valueOf(PLAYER_MAX_LEVEL) : "Any");
            }
            
            htmltext = fileContent;
        }
        else
        {
            String fileContent = loadHtmlFile(player, "TvTEventStatus.htm");
            if (fileContent != null)
            {
                fileContent = fileContent.replaceAll("%team1playercount%", String.valueOf(_blueTeam.size()));
                fileContent = fileContent.replaceAll("%team1points%", String.valueOf(_blueTeamKills));
                fileContent = fileContent.replaceAll("%team2playercount%", String.valueOf(_redTeam.size()));
                fileContent = fileContent.replaceAll("%team2points%", String.valueOf(_redTeamKills));
            }
            
            htmltext = fileContent;
        }
        return htmltext;
    }
    
    /**
     * Registers player to event.
     * @param player
     */
    @Override
    public void registerPlayer(L2PcInstance player)
    {
        if (getEventState() != EventState.REGISTER)
        {
            player.sendMessage("TvT Registration is not in progress.");
            return;
        }
        
        if (player.isFestivalParticipant())
        {
            player.sendMessage("Festival participants cannot register to the event.");
            return;
        }
        
        if (player.isInJail())
        {
            player.sendMessage("Jailed players cannot register to the event.");
            return;
        }
        
        if (player.isAIOBuffer())
        {
            player.sendMessage("AIO Buffers cannot register to the event.");
            return;
        }
        
        if (player.isDead())
        {
            player.sendMessage("Dead players cannot register to the event.");
            return;
        }
        
        if (Olympiad.getInstance().isRegisteredInComp(player))
        {
            player.sendMessage("Grand Olympiad participants cannot register to the event.");
            return;
        }
        
        if (player.getLevel() < PLAYER_MIN_LEVEL || (PLAYER_MAX_LEVEL > 0 && player.getLevel() > PLAYER_MAX_LEVEL))
        {
            player.sendMessage("You have not reached the appropriate level to join the event.");
            return;
        }
        
        if (_registered.size() == MAX_PARTICIPANTS)
        {
            player.sendMessage("There is no more room for you to register to the event.");
            return;
        }
        
        for (L2PcInstance registered : _registered)
        {
            if (registered == null)
            {
                continue;
            }
            
            if (registered.getObjectId() == player.getObjectId())
            {
                player.sendMessage("You are already registered in the TvT event.");
                return;
            }
            
            // Check if dual boxing is not allowed
            if (!ALLOW_PLAYERS_SAME_IP)
            {
                if ((registered.getClient() == null) || (player.getClient() == null))
                {
                    continue;
                }
                
                String ip1 = player.getClient().getConnection().getInetAddress().getHostAddress();
                String ip2 = registered.getClient().getConnection().getInetAddress().getHostAddress();
                if ((ip1 != null) && (ip2 != null) && ip1.equals(ip2))
                {
                    player.sendMessage("Your IP is already registered in the TvT event.");
                    return;
                }
            }
        }
        
        _registered.add(player);
        
        player.sendMessage("You have registered to participate in the TvT Event.");
        
        super.registerPlayer(player);
    }
    
    /**
     * Removes player from event.
     * @param player
     */
    @Override
    public void removePlayer(L2PcInstance player)
    {
        if (_registered.contains(player))
        {
            _registered.remove(player);
            player.sendMessage("You have been removed from the TvT Event registration list.");
        }
        else
        {
            List<L2PcInstance> team;
            List<L2PcInstance> enemyTeam;
            
            if (player.getEventTeam() == 1)
            {
                team = _blueTeam;
                enemyTeam = _redTeam;
            }
            else if (player.getEventTeam() == 2)
            {
                team = _redTeam;
                enemyTeam = _blueTeam;
            }
            else
            {
                team = null;
                enemyTeam = null;
            }
            
            if (team != null && enemyTeam != null)
            {
                team.remove(player);
                cancelQuestTimer("player_revive", null, player);

                if (team.isEmpty())
                {
                    // The entire team gave up so unset kill count
                    if (team == _blueTeam)
                    {
                        _blueTeamKills = 0;
                    }
                    else
                    {
                        _redTeamKills = 0;
                    }
                    
                    // If no participants left in both teams, do not attempt to announce rewards
                    if (enemyTeam.isEmpty())
                    {
                        setEventState(EventState.INITIAL);
                    }

                    finishEvent();
                }
            }
            
            // Now, remove team status
            player.setEventTeam(0);
        }
        
        super.removePlayer(player);
    }
    
    @Override
    public boolean isRegistered(L2PcInstance player)
    {
        return _registered.contains(player);
    }
    
    @Override
    public void onDie(L2PcInstance player)
    {
        if (player == null)
        {
            return;
        }
        
        player.broadcastPacket(new ChangeWaitType(player, ChangeWaitType.WT_START_FAKEDEATH));
        
        player.sendMessage("You will be respawned in " + (PLAYER_RESPAWN_DELAY / 1000) + " seconds.");
        startQuestTimer("player_revive", PLAYER_RESPAWN_DELAY, null, player);
    }
    
    @Override
    public void onKill(L2PcInstance player, L2PcInstance target)
    {
        if (player == null || target == null)
        {
            return;
        }
        
        increaseTeamKills(player, target);
    }
    
    @Override
    public void onRevive(L2PcInstance player)
    {
        if (player == null)
        {
            return;
        }
        
        // Heal player fully
        player.setCurrentCp(player.getMaxCp());
        player.setCurrentHp(player.getMaxHp());
        player.setCurrentMp(player.getMaxMp());
        
        ChangeWaitType revive = new ChangeWaitType(player, ChangeWaitType.WT_STOP_FAKEDEATH);
        player.broadcastPacket(revive);
    }
    
    @Override
    public boolean isRemovingBuffsOnRespawn()
    {
        return PLAYER_CLEAR_BUFFS_AFTER_RESPAWN;
    }
    
    private class TvTCommand implements IVoicedCommandHandler
    {
        @Override
        public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
        {
            if (command.equalsIgnoreCase("tvt"))
            {
                activeChar.sendMessage("==================");
                activeChar.sendMessage("TvT Event Commands");
                activeChar.sendMessage("==================");
                activeChar.sendMessage(".tvtjoin = Register to event");
                activeChar.sendMessage(".tvtleave = Cancel event registration");
            }
            else if (command.equalsIgnoreCase("tvtjoin"))
            {
                registerPlayer(activeChar);
            }
            else if (command.equalsIgnoreCase("tvtleave"))
            {
                if (isRegistered(activeChar))
                {
                    removePlayer(activeChar);
                }
                else
                {
                    activeChar.sendMessage("You are not registered for the TvT Event.");
                }
            }
            
            return true;
        }
        
        @Override
        public String[] getVoicedCommandList()
        {
            return VOICED_COMMANDS;
        }
    }
}
