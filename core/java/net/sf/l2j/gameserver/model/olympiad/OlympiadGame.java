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
package net.sf.l2j.gameserver.model.olympiad;

import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2CubicInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad.COMP_TYPE;
import net.sf.l2j.gameserver.model.zone.type.L2OlympiadStadiumZone;
import net.sf.l2j.gameserver.network.serverpackets.ExOlympiadMatchEnd;
import net.sf.l2j.gameserver.network.serverpackets.ExOlympiadMode;
import net.sf.l2j.gameserver.network.serverpackets.ExOlympiadUserInfo;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author DnR
 *
 */
class OlympiadGame
{
	protected static final Logger _log = Logger.getLogger(OlympiadGame.class.getName());
	
	private final L2OlympiadStadiumZone _stadium;
	
    protected COMP_TYPE _type;
    protected boolean _aborted;
    protected boolean _playerOneDisconnected;
    protected boolean _playerTwoDisconnected;
    protected String _playerOneName;
    protected String _playerTwoName;
    protected int _playerOneID = 0;
    protected int _playerTwoID = 0;

    protected static boolean _battleStarted;
    private static final String POINTS = "olympiad_points";
	private static final String COMP_DONE = "competitions_done";
	private static final String COMP_WON = "competitions_won";
	private static final String COMP_LOST = "competitions_lost";
	
	// Game state
	private byte _state;
	protected static byte INITIAL = 0;
    protected static byte STANDBY = 1;
    protected static byte PLAYING = 2;
    
    private final L2PcInstance[] _players;
    private Location _playerOneLoc;
    private Location _playerTwoLoc;

	protected OlympiadGame(L2OlympiadStadiumZone stadium, COMP_TYPE type, L2PcInstance[] players)
	{
		_aborted = false;
		_stadium = stadium;
        _state = INITIAL;
        _playerOneDisconnected = false;
        _playerTwoDisconnected = false;
        _type = type;

        _players = players;
        if (players != null)
        {
            final L2PcInstance playerOne = _players[0];
            final L2PcInstance playerTwo = _players[1];

            if (playerOne != null && playerTwo != null)
            {
                _playerOneName = playerOne.getName();
                _playerTwoName = playerTwo.getName();
                playerOne.setOlympiadGameId(stadium.getStadiumId());
                playerTwo.setOlympiadGameId(stadium.getStadiumId());
                _playerOneID = playerOne.getObjectId();
                _playerTwoID = playerTwo.getObjectId();

                if (Config.DEBUG)
                    _log.info("Olympiad System: Game - " + stadium.getStadiumId() + ": " + _playerOneName + " Vs " + _playerTwoName);
            }
            else
            {
                _aborted = true;
                clearPlayers();
            }
        }
        else
        {
            _aborted = true;
            clearPlayers();
        }
    }

    protected void clearPlayers()
    {
        _players[0] = null;
        _players[1] = null;
        _playerOneName = "";
        _playerTwoName = "";
        _playerOneID = 0;
        _playerTwoID = 0;
    }

    protected void handleDisconnect(L2PcInstance player)
    {
        Location loc = null;
        if (player == _players[0])
        {
            loc = _playerOneLoc;
            _playerOneDisconnected = true;
        }
        else if (player == _players[1])
        {
            loc = _playerTwoLoc;
            _playerTwoDisconnected = true;
        }
        
        // Also, reset player position if he is already inside arena
        if (player.isInOlympiadMode() && loc != null)
        {
            player.setXYZ(loc.getX(), loc.getY(), loc.getZ());
        }
    }

    protected void healPlayer(L2PcInstance player)
    {
        player.setCurrentCp(player.getMaxCp());
        player.setCurrentHp(player.getMaxHp());
        player.setCurrentMp(player.getMaxMp());
    }

    protected void removals()
	{
        if (_aborted)
            return;

        if (_players[0] == null || _players[1] == null)
            return;

        if (_playerOneDisconnected || _playerTwoDisconnected)
            return;

        for (L2PcInstance player : _players)
        {
            // Abort casting if player casting  
            if (player.isCastingNow())
            {
                player.abortCast();
            }

            player.getAppearance().setVisible();

            // Heal player fully
            healPlayer(player);

            // Remove buffs
            player.stopAllEffects();
            player.clearCharges();
            
            // Remove player from his party
            L2Party party = player.getParty();
            if (party != null)
            {
                party.removePartyMember(player, true);
            }
            
            // Remove forbidden items
            player.checkItemRestriction();
            
            // Remove shot automation
            player.disableAutoShotsAll();

            // Discharge any active shots
            player.setChargedSoulShot(L2ItemInstance.CHARGED_NONE);
            player.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);
            
            // Cubic cleanup
            List<L2CubicInstance> cubics = player.getCubics();
            if (cubics != null)
            {
                boolean removed = false;
                for (L2CubicInstance cubic : cubics)
                {
                    if (cubic.givenByOther())
                    {
                        cubic.stopAction();
                        cubic.cancelDisappear();
                        player.getCubics().remove(cubic);
                        removed = true;
                    }
                }

                if (removed)
                {
                    player.broadcastUserInfo();
                }
            }
            
            // Summon cleanup
            L2Summon summon = player.getPet();
            if (summon != null)
            {
                if (summon instanceof L2PetInstance)
                {
                    summon.unSummon(player);
                }
                else
                {
                	summon.stopAllEffects();

                    if (summon.isCastingNow())
                    {
                        summon.abortCast();
                    }
                    
                    summon.setChargedSoulShot(L2ItemInstance.CHARGED_NONE);
                	summon.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);
                }
            }
        }
    }

    protected boolean portPlayersToArena()
    {
        final L2PcInstance playerOne = _players[0];
        final L2PcInstance playerTwo = _players[1];
        final boolean _playerOneCrash = (playerOne == null || _playerOneDisconnected);
        final boolean _playerTwoCrash = (playerTwo == null || _playerTwoDisconnected);

        if (_playerOneCrash || _playerTwoCrash || _aborted)
        {
            _players[0] = null;
            _players[1] = null;
            _aborted = true;
            return false;
        }

        _playerOneLoc = new Location(playerOne.getX(), playerOne.getY(), playerOne.getZ());
        _playerTwoLoc = new Location(playerTwo.getX(), playerTwo.getY(), playerTwo.getZ());

        if (playerOne.isSitting())
            playerOne.standUp();

        if (playerTwo.isSitting())
            playerTwo.standUp();

        playerOne.setTarget(null);
        playerTwo.setTarget(null);

        playerOne.teleToLocation(_stadium.getSpawnLocs().get(0), false);
        playerTwo.teleToLocation(_stadium.getSpawnLocs().get(1), false);

        playerOne.setIsInOlympiadMode(true);
        playerOne.setIsOlympiadStart(false);
        playerOne.setOlympiadSide(1);

        playerTwo.setIsInOlympiadMode(true);
        playerTwo.setIsOlympiadStart(false);
        playerTwo.setOlympiadSide(2);

        playerOne.sendPacket(new ExOlympiadMode(1));
        playerTwo.sendPacket(new ExOlympiadMode(2));

        _state = STANDBY;

        return true;
    }

    protected void sendMessageToPlayers(boolean toBattleBegin, int nsecond)
    {
    	SystemMessage sm;
        if (!toBattleBegin)
            sm = new SystemMessage(SystemMessage.YOU_WILL_ENTER_THE_OLYMPIAD_STADIUM_IN_S1_SECOND_S);
        else
            sm = new SystemMessage(SystemMessage.THE_GAME_WILL_START_IN_S1_SECOND_S);

        sm.addNumber(nsecond);

        for (L2PcInstance player : _players)
        {
            if (player != null)
                player.sendPacket(sm);
        }
    }

    protected void broadcastMessage(SystemMessage sm, boolean toAll)
    {
        for (L2PcInstance player : _players)
        {
            if (player != null)
                player.sendPacket(sm);
        }

        List<L2PcInstance> spectators = _stadium.getSpectators();
        if (toAll)
        {
            for (L2PcInstance spec : spectators)
            {
                if (spec == null)
                    continue;

                spec.sendPacket(sm);
            }
        }
    }

    protected void portPlayersBack()
    {
        final L2PcInstance playerOne = _players[0];
        final L2PcInstance playerTwo = _players[1];
        if (playerOne != null && _playerOneLoc != null)
        {
            playerOne.sendPacket(new ExOlympiadMatchEnd());
            playerOne.teleToLocation(_playerOneLoc, false);
        }

        if (playerTwo != null && _playerTwoLoc != null)
        {
            playerTwo.sendPacket(new ExOlympiadMatchEnd());
            playerTwo.teleToLocation(_playerTwoLoc, false);
        }
    }

    protected void playersStatusBack()
    {
        for (L2PcInstance player : _players)
        {
            if (player == null) {
                continue;
            }
            
            if (player.isDead())
            {
                player.setIsDead(false);
            }

            player.getStatus().startHpMpRegeneration();

             // Heal player fully
            healPlayer(player);

            player.setIsInOlympiadMode(false);
            player.setIsOlympiadStart(false);
            player.setOlympiadSide(-1);
            player.setOlympiadGameId(-1);
            player.sendPacket(new ExOlympiadMode(0));
        }
    }
    
    protected boolean checkBattleStatus()
    {
        final boolean _pOneCrash = (_players[0] == null || _playerOneDisconnected);
        final boolean _pTwoCrash = (_players[1] == null || _playerTwoDisconnected);

        int div;
        switch (_type)
		{
			case NON_CLASSED:
				div = 5;
				break;
			default:
				div = 3;
				break;
		}
        
        if (_pOneCrash || _pTwoCrash || _aborted)
        {
            StatsSet playerOneStat = Olympiad.getNobleStats(_playerOneID);
            StatsSet playerTwoStat = Olympiad.getNobleStats(_playerTwoID);

            int playerOnePlayed = playerOneStat.getInteger(COMP_DONE);
            int playerTwoPlayed = playerTwoStat.getInteger(COMP_DONE);
            int playerOneWon = playerOneStat.getInteger(COMP_WON);
            int playerTwoWon = playerTwoStat.getInteger(COMP_WON);
            int playerOneLost = playerOneStat.getInteger(COMP_LOST);
            int playerTwoLost = playerTwoStat.getInteger(COMP_LOST);
            
            int playerOnePoints = playerOneStat.getInteger(POINTS);
            int playerTwoPoints = playerTwoStat.getInteger(POINTS);
            
            int pointDiff = Math.min(playerOnePoints, playerTwoPoints) / div;
            if (pointDiff <= 0)
            {
    			pointDiff = 1;
            }
    		else if (pointDiff > Config.ALT_OLY_MAX_POINTS)
    		{
    			pointDiff = Config.ALT_OLY_MAX_POINTS;
    		}
            
            SystemMessage sm;

            if (_pOneCrash && !_pTwoCrash)
            {
                try
                {
                	playerOneStat.set(POINTS, playerOnePoints - pointDiff);
                    playerOneStat.set(COMP_LOST, playerOneLost + 1);

                    if (Config.DEBUG)
                        _log.info("Olympia Result: "+_playerOneName+" vs "+_playerTwoName+" ... "+_playerOneName+" lost " + pointDiff + " points for crash");						

                    playerTwoStat.set(POINTS, playerTwoPoints + pointDiff);
                    playerTwoStat.set(COMP_WON, playerTwoWon + 1);

                    if (Config.DEBUG)
                        _log.info("Olympia Result: "+_playerOneName+" vs "+_playerTwoName+" ... "+_playerTwoName+" Win " + pointDiff + " points");

                    sm = new SystemMessage(SystemMessage.S1_HAS_WON_THE_GAME);
                    sm.addString(_playerTwoName);
                    broadcastMessage(sm, true);
                    
                    sm = new SystemMessage(SystemMessage.S1_HAS_GAINED_S2_OLYMPIAD_POINTS);
                    sm.addString(_playerTwoName);
                    sm.addNumber(pointDiff);
                    broadcastMessage(sm, false);
                    
                    sm = new SystemMessage(SystemMessage.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
                    sm.addString(_playerOneName);
                    sm.addNumber(pointDiff);
                    broadcastMessage(sm, false);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (_pTwoCrash && !_pOneCrash)
            {
                try
                {
                	playerTwoStat.set(POINTS, playerTwoPoints - pointDiff);
                    playerTwoStat.set(COMP_LOST, playerTwoLost + 1);

                    if (Config.DEBUG)
                        _log.info("Olympia Result: "+_playerTwoName+" vs "+_playerOneName+" ... "+_playerTwoName+" lost " + pointDiff + " points for crash");

                    playerOneStat.set(POINTS, playerOnePoints + pointDiff);
                    playerOneStat.set(COMP_WON, playerOneWon + 1);

                    if (Config.DEBUG)
                        _log.info("Olympia Result: "+_playerTwoName+" vs "+_playerOneName+" ... "+_playerOneName+" Win " + pointDiff + " points");

                    sm = new SystemMessage(SystemMessage.S1_HAS_WON_THE_GAME);
                    sm.addString(_playerOneName);
                    broadcastMessage(sm, true);
                    
                    sm = new SystemMessage(SystemMessage.S1_HAS_GAINED_S2_OLYMPIAD_POINTS);
                    sm.addString(_playerOneName);
                    sm.addNumber(pointDiff);
                    broadcastMessage(sm, false);
                    
                    sm = new SystemMessage(SystemMessage.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
                    sm.addString(_playerTwoName);
                    sm.addNumber(pointDiff);
                    broadcastMessage(sm, false);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (_pOneCrash && _pTwoCrash)
            {
                try
                {                    
                    playerOneStat.set(POINTS, playerOnePoints - pointDiff);
                    playerTwoStat.set(POINTS, playerTwoPoints - pointDiff);

                    if (Config.DEBUG)
                        _log.info("Olympia Result: "+_playerOneName+" vs "+_playerTwoName+" ... " + " both lost " + pointDiff + " points for crash");

                    sm = new SystemMessage(SystemMessage.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
                    sm.addString(_playerOneName);
                    sm.addNumber(pointDiff);
                    broadcastMessage(sm, false);
                    
                    sm = new SystemMessage(SystemMessage.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
                    sm.addString(_playerTwoName);
                    sm.addNumber(pointDiff);
                    broadcastMessage(sm, false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            playerOneStat.set(COMP_DONE, playerOnePlayed + 1);
            playerTwoStat.set(COMP_DONE, playerTwoPlayed + 1);

            return false;
        }

        return true;
    }

    protected boolean hasWinner()
    {
        final L2PcInstance playerOne = _players[0];
        final L2PcInstance playerTwo = _players[1];

        if (_aborted || playerOne == null || playerTwo == null)
            return true;

        double playerOneHp = playerOne.getOlympiadGameId() != -1 ? playerOne.getCurrentHp() : 0;
        double playerTwoHp = playerTwo.getOlympiadGameId() != -1 ? playerTwo.getCurrentHp() : 0;

        return playerTwoHp == 0 || playerOneHp == 0;
    }

    protected void validateWinner()
    {
        L2PcInstance playerOne = _players[0];
        L2PcInstance playerTwo = _players[1];

        if (_aborted || playerOne == null || playerTwo == null || _playerOneDisconnected || _playerTwoDisconnected)
            return;

        StatsSet playerOneStat = Olympiad.getNobleStats(_playerOneID);
        StatsSet playerTwoStat = Olympiad.getNobleStats(_playerTwoID);

        int div;
        int gpReward;

        int playerOnePlayed = playerOneStat.getInteger(COMP_DONE);
        int playerTwoPlayed = playerTwoStat.getInteger(COMP_DONE);
        int playerOneWon = playerOneStat.getInteger(COMP_WON);
        int playerTwoWon = playerTwoStat.getInteger(COMP_WON);
        int playerOneLost = playerOneStat.getInteger(COMP_LOST);
        int playerTwoLost = playerTwoStat.getInteger(COMP_LOST);

        int playerOnePoints = playerOneStat.getInteger(POINTS);
        int playerTwoPoints = playerTwoStat.getInteger(POINTS);

        final double playerOneHp = playerOne.isDead() ? 0 : playerOne.getCurrentHp() + playerOne.getCurrentCp();
        final double playerTwoHp = playerTwo.isDead() ? 0 : playerTwo.getCurrentHp() + playerTwo.getCurrentCp();

        String result = "";

        // If players crashed, check if they have relogged
        _players[0] = L2World.getInstance().getPlayer(_playerOneID);
        _players[1] = L2World.getInstance().getPlayer(_playerTwoID);
        playerOne = _players[0];
        playerTwo = _players[1];

        switch (_type)
        {
            case NON_CLASSED:
                div = 5;
                gpReward = Config.ALT_OLY_NONCLASSED_RITEM_C;
                break;
            default:
                div = 3;
                gpReward = Config.ALT_OLY_CLASSED_RITEM_C;
                break;
        }
        
        int pointDiff = Math.min(playerOnePoints, playerTwoPoints) / div;
		if (pointDiff <= 0)
		{
			pointDiff = 1;
		}
		else if (pointDiff > Config.ALT_OLY_MAX_POINTS)
		{
			pointDiff = Config.ALT_OLY_MAX_POINTS;
		}

        SystemMessage sm;
        
        if (!playerOne.isOnline() && !playerTwo.isOnline())
        {
            result = " tie";
            sm = new SystemMessage(SystemMessage.THE_GAME_ENDED_IN_A_TIE);
            broadcastMessage(sm, true);
        }
        else if (!playerTwo.isOnline() || (playerTwoHp == 0 && playerOneHp != 0) 
        	|| (playerOne.dmgDealt > playerTwo.dmgDealt && playerTwoHp != 0 && playerOneHp != 0))
        {
            playerOneStat.set(POINTS, playerOnePoints + pointDiff);
            playerOneStat.set(COMP_WON, playerOneWon + 1);
            playerTwoStat.set(POINTS, playerTwoPoints - pointDiff);
            playerTwoStat.set(COMP_LOST, playerTwoLost + 1);
            
            sm = new SystemMessage(SystemMessage.S1_HAS_WON_THE_GAME);
            sm.addString(_playerOneName);
            broadcastMessage(sm, true);
            
            sm = new SystemMessage(SystemMessage.S1_HAS_GAINED_S2_OLYMPIAD_POINTS);
            sm.addString(_playerOneName);
            sm.addNumber(pointDiff);
            broadcastMessage(sm, false);
            
            sm = new SystemMessage(SystemMessage.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
            sm.addString(_playerTwoName);
            sm.addNumber(pointDiff);
            broadcastMessage(sm, false);

            try
            {
                result = " ("+playerOneHp+"hp vs "+playerTwoHp+"hp - "+playerOne.dmgDealt+"dmg vs "+playerTwo.dmgDealt+"dmg) "+_playerOneName+" win "+pointDiff+" points";
                L2ItemInstance item = playerOne.getInventory().addItem("Olympiad", Config.ALT_OLY_BATTLE_REWARD_ITEM, gpReward, playerOne, null);
                InventoryUpdate iu = new InventoryUpdate();
                iu.addModifiedItem(item);
                playerOne.sendPacket(iu);

                sm = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
                sm.addItemName(item.getItemId());
                sm.addNumber(gpReward);
                playerOne.sendPacket(sm);
            }
            catch (Exception e) {}
        }
        else if (!playerOne.isOnline() || (playerOneHp == 0 && playerTwoHp != 0) 
        	|| (playerTwo.dmgDealt > playerOne.dmgDealt && playerOneHp != 0 && playerTwoHp != 0))
        {
            playerTwoStat.set(POINTS, playerTwoPoints + pointDiff);
            playerTwoStat.set(COMP_WON, playerTwoWon + 1);
            playerOneStat.set(POINTS, playerOnePoints - pointDiff);
            playerOneStat.set(COMP_LOST, playerOneLost + 1);
            
            sm = new SystemMessage(SystemMessage.S1_HAS_WON_THE_GAME);
            sm.addString(_playerTwoName);
            broadcastMessage(sm, true);
            
            sm = new SystemMessage(SystemMessage.S1_HAS_GAINED_S2_OLYMPIAD_POINTS);
            sm.addString(_playerTwoName);
            sm.addNumber(pointDiff);
            broadcastMessage(sm, false);
            
            sm = new SystemMessage(SystemMessage.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
            sm.addString(_playerOneName);
            sm.addNumber(pointDiff);
            broadcastMessage(sm, false);

            try
            {
                result = " ("+playerOneHp+"hp vs "+playerTwoHp+"hp - "+playerOne.dmgDealt+"dmg vs "+playerTwo.dmgDealt+"dmg) "+_playerTwoName+" win "+pointDiff+" points";
                L2ItemInstance item = playerTwo.getInventory().addItem("Olympiad", Config.ALT_OLY_BATTLE_REWARD_ITEM, gpReward, playerTwo, null);
                InventoryUpdate iu = new InventoryUpdate();
                iu.addModifiedItem(item);
                playerTwo.sendPacket(iu);

                sm = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
                sm.addItemName(item.getItemId());
                sm.addNumber(gpReward);
                playerTwo.sendPacket(sm);
            }
            catch (Exception e) {}
        }
        else
        {
            result = " tie";
            sm = new SystemMessage(SystemMessage.THE_GAME_ENDED_IN_A_TIE);
            broadcastMessage(sm, true);
            
            if (Config.ALT_OLY_LOSE_POINTS_ON_TIE)
            {	
            	final int pointOneDiff = Math.min(playerOnePoints / div, Config.ALT_OLY_MAX_POINTS);
                final int pointTwoDiff = Math.min(playerTwoPoints / div, Config.ALT_OLY_MAX_POINTS);
                
	            playerOneStat.set(POINTS, playerOnePoints - pointOneDiff);
	            playerOneStat.set(COMP_LOST, playerOneLost + 1);
	
	            playerTwoStat.set(POINTS, playerTwoPoints - pointTwoDiff);
	            playerTwoStat.set(COMP_LOST, playerTwoLost + 1);
	            
	            sm = new SystemMessage(SystemMessage.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
	            sm.addString(_playerOneName);
	            sm.addNumber(pointOneDiff);
	            broadcastMessage(sm, false);
	            
	            sm = new SystemMessage(SystemMessage.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
	            sm.addString(_playerTwoName);
	            sm.addNumber(pointTwoDiff);
	            broadcastMessage(sm, false);
            }
        }

        if (Config.DEBUG)
            _log.info("Olympia Result: "+_playerOneName+" vs "+_playerTwoName+" ... "+result);

        playerOneStat.set(COMP_DONE, playerOnePlayed + 1);
        playerTwoStat.set(COMP_DONE, playerTwoPlayed + 1);
        
        for (int i = 40; i > 10; i -= 10)
        {
            sm = new SystemMessage(SystemMessage.YOU_WILL_GO_BACK_TO_THE_VILLAGE_IN_S1_SECOND_S);
            sm.addNumber(i);
            broadcastMessage(sm, false);
            
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e) {}

            if (i == 20)
            {
                sm = new SystemMessage(SystemMessage.YOU_WILL_GO_BACK_TO_THE_VILLAGE_IN_S1_SECOND_S);
                sm.addNumber(10);
                broadcastMessage(sm, false);
                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e) {}
            }
        }

        for (int i = 5; i > 0; i--)
        {
            sm = new SystemMessage(SystemMessage.YOU_WILL_GO_BACK_TO_THE_VILLAGE_IN_S1_SECOND_S);
            sm.addNumber(i);
            broadcastMessage(sm, false);
	
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {}
        }
        
        sm = null;
    }

    protected void additions()
    {
    	L2Skill skill;
        SystemMessage sm;
        
        for (L2PcInstance player : _players)
        {
             // Heal player fully
            healPlayer(player);

	        // Wind Walk Buff for any class
	        skill = SkillTable.getInstance().getInfo(1204, 2);
	        if (skill != null)
	        {
	        	skill.getEffects(player, player);
	        	sm = new SystemMessage(SystemMessage.YOU_FEEL_S1_EFFECT);
		        sm.addSkillName(skill);
		        player.sendPacket(sm);
	        }
	        
	        // Acumen to mystics and Haste to fighters
	        skill = SkillTable.getInstance().getInfo(player.isMageClass() ? 1085 : 1086, 1);
	        if (skill != null)
	        {
	        	skill.getEffects(player, player);
	        	sm = new SystemMessage(SystemMessage.YOU_FEEL_S1_EFFECT);
		        sm.addSkillName(skill);
		        player.sendPacket(sm);
	        }
	        
	        player.dmgDealt = 0;
        }
    }

    protected boolean makeCompetitionStart()
    {
        if (_aborted)
            return false;

        SystemMessage sm = new SystemMessage(SystemMessage.STARTS_THE_GAME);
        broadcastMessage(sm, true);

		_state = PLAYING;

        final L2PcInstance playerOne = _players[0];
        final L2PcInstance playerTwo = _players[1];
        if (playerOne != null && playerTwo != null) {
            playerOne.setIsOlympiadStart(true);
            playerOne.sendPacket(new ExOlympiadUserInfo(playerTwo));

            playerTwo.setIsOlympiadStart(true);
            playerTwo.sendPacket(new ExOlympiadUserInfo(playerOne));
        }

		// Broadcast users info to spectators
		for (L2PcInstance spectator : _stadium.getSpectators())
		{
			if (spectator == null)
			{
				continue;
			}
			Olympiad.broadcastUsersInfo(spectator);
		}
        return true;
    }

    /**
     * 
     * @param player
     * @return 
     */
    protected L2PcInstance getOpponentOf(L2PcInstance player)
    {
        L2PcInstance opponent;
        if (player == _players[0])
        {
            opponent = _players[1];
        }
        else if (player == _players[1])
        {
            opponent = _players[0];
        }
        else
        {
            opponent = null;
        }
        return opponent;
    }

    protected L2PcInstance[] getPlayers()
    {
        return _players;
    }
    
    protected L2OlympiadStadiumZone getStadium()
    {
    	return _stadium;
    }
    
    protected byte getState()
    {
    	return _state;
    }
    
    protected void setState(byte value)
    {
    	_state = value;
    }
    
    protected String getTitle()
	{
		return _playerOneName + " / " + _playerTwoName;
	}
}

/**
 *
 * @author ascharot
 * 
 */
class OlympiadGameTask implements Runnable
{
	protected static final Logger _log = Logger.getLogger(OlympiadGameTask.class.getName());

	protected OlympiadGame _game = null;
	
	protected static final long BATTLE_PERIOD = Config.ALT_OLY_BATTLE; // Usually several minutes

   	private boolean _terminated = false;
   	private boolean _started = false;

   	public OlympiadGameTask(OlympiadGame game)
   	{
       	_game = game;
   	}

   	protected void cleanGame()
   	{
       	_started = false;
       	_terminated = true;

       	if (_game.getState() > OlympiadGame.INITIAL)
       	{
    	   	_game.playersStatusBack();
           	_game.portPlayersBack();
       	}

       	_game.setState(OlympiadGame.INITIAL);

       	// Notify spectators that match ended
       	List<L2PcInstance> spectators = _game.getStadium().getSpectators();
       	for (L2PcInstance spec : spectators)
		{
			if (spec != null)
			{
				spec.sendPacket(new ExOlympiadMatchEnd());
			}
		}
       	
       	_game.clearPlayers();
       	OlympiadManager.getInstance().removeGame(_game.getStadium().getStadiumId());
       	_game = null;
   	}
   	
   	public boolean isTerminated()
   	{
	   	return _terminated || _game._aborted;
   	}

   	public boolean isStarted()
   	{
   		return _started;
   	}

   	@Override
   	public void run()
   	{
   		_started = true;
   		if (_game != null)
   		{
            final L2PcInstance[] players = _game.getPlayers();
   			if (players[0] != null && players[1] != null)
   			{
   				// Waiting to teleport to arena
   				for (int i = 120; i > 10; i -= 5)
   				{
   					switch(i)
   					{
   						case 120:
   						case 60:
   						case 30:
   						case 15: 
   							_game.sendMessageToPlayers(false, i);
   							break;
   					}

   					try
   					{
   						Thread.sleep(5000);
   					}
   					catch (InterruptedException e){}
   				}

   				for (int i = 5; i > 0; i--)
   				{
   					_game.sendMessageToPlayers(false,i);		    			
   					try
   					{
   						Thread.sleep(1000);
   					}
   					catch (InterruptedException e){}
   				}

   				// Check if players are qualified to fight
                for (int i = 0; i < players.length; i++)
                {
                    if (players[i] != null && !players[i].checkOlympiadConditions())
                    {
                        players[i] = null;
                    }
                }

   				// Checking for opponents and teleporting to arena
   				if (!_game.checkBattleStatus())
   				{
   					cleanGame();
   					return;
   				}

   				_game.portPlayersToArena();
   				_game.removals();

   				try
   				{
   					Thread.sleep(5000);
   				}
   				catch (InterruptedException e){}

   				synchronized(this)
   				{
   					if (!OlympiadGame._battleStarted)
   						OlympiadGame._battleStarted = true;
   				}

   				for (int i = 60; i >= 10; i -= 10)
   				{
   					_game.sendMessageToPlayers(true, i);
                    if (i == 20)
                    {
                        _game.additions();
                    }

   					try
                  	{
   						Thread.sleep(i == 10 ? 5000 : 10000);
                  	}
   					catch (InterruptedException e){}
   				}

   				for (int i = 5; i > 0; i--)
   				{
   					_game.sendMessageToPlayers(true, i);
   					try
   					{
   						Thread.sleep(1000);
   					}
   					catch (InterruptedException e){}
   				}

   				if (!_game.checkBattleStatus())
   				{
   					cleanGame();
   					return;
   				}

   				_game.makeCompetitionStart();

   				// Wait several minutes (Battle)
   				for (int i = 0; i < BATTLE_PERIOD; i += 10000)
   				{
   					try
   					{
   						Thread.sleep(10000);
   						// If the game has Winner then stop waiting battle_period and validate winner
   						if (_game.hasWinner())
   							break;

   						if (!_game.checkBattleStatus())
   						{
   							cleanGame();
   							return;
   						}
   					}
   					catch (InterruptedException e){}
   				}

   				_game.validateWinner();
   				cleanGame();
            }
        }
    }
}