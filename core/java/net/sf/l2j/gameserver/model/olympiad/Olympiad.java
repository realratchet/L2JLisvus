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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.zone.type.L2OlympiadStadiumZone;
import net.sf.l2j.gameserver.network.serverpackets.ExOlympiadMatchEnd;
import net.sf.l2j.gameserver.network.serverpackets.ExOlympiadSpelledInfo;
import net.sf.l2j.gameserver.network.serverpackets.ExOlympiadUserInfo;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author godson
 */
public class Olympiad
{
    protected static final Logger _log = Logger.getLogger(Olympiad.class.getName());

    private static Map<Integer, StatsSet> _nobles = new ConcurrentHashMap<>();
    private static Map<Integer, Integer> _noblesRank = new HashMap<>();

    protected static List<L2PcInstance> _nonClassBasedRegisters;
    protected static Map<Integer, List<L2PcInstance>> _classBasedRegisters;

    public static final String OLYMPIAD_HTML_PATH = "data/html/olympiad/";
    private static final String OLYMPIAD_LOAD_DATA = "SELECT current_cycle, period, olympiad_end, validation_end, "
		+ "next_weekly_change FROM olympiad_data WHERE id = 0";
    private static final String OLYMPIAD_SAVE_DATA = "INSERT INTO olympiad_data (id, current_cycle, "
		+ "period, olympiad_end, validation_end, next_weekly_change) VALUES (0,?,?,?,?,?) "
		+ "ON DUPLICATE KEY UPDATE current_cycle=?, period=?, olympiad_end=?, "
		+ "validation_end=?, next_weekly_change=?";
    private static final String OLYMPIAD_LOAD_NOBLES = "SELECT olympiad_nobles.char_id, olympiad_nobles.class_id, "
	    + "characters.char_name, olympiad_nobles.olympiad_points, olympiad_nobles.competitions_done, "
	    + "olympiad_nobles.competitions_won, olympiad_nobles.competitions_lost "
	    + "FROM olympiad_nobles, characters WHERE characters.obj_Id = olympiad_nobles.char_id";
    private static final String OLYMPIAD_SAVE_NOBLES = "INSERT INTO olympiad_nobles values (?,?,?,?,?,?)";
    private static final String OLYMPIAD_UPDATE_NOBLES = "UPDATE olympiad_nobles set " +
            "olympiad_points = ?, competitions_done = ?, competitions_won = ?, competitions_lost = ? where char_id = ?";
    private static final String OLYMPIAD_GET_HEROES = "SELECT olympiad_nobles.char_id, characters.char_name "
	    + "FROM olympiad_nobles, characters WHERE characters.obj_Id = olympiad_nobles.char_id "
	    + "AND olympiad_nobles.class_id = ? AND olympiad_nobles.competitions_done >= 5 AND olympiad_nobles.competitions_won > 0 "
	    + "ORDER BY olympiad_nobles.olympiad_points DESC, olympiad_nobles.competitions_done DESC";
    private static final String GET_ALL_CLASSIFIED_NOBLESS = "SELECT char_id from olympiad_nobles_eom "
		+ "WHERE competitions_done >= 5 AND competitions_won > 0 ORDER BY olympiad_points DESC, competitions_done DESC";
    private static final String GET_EACH_CLASS_LEADER = "SELECT characters.char_name from olympiad_nobles_eom, characters "
	    + "WHERE characters.obj_Id = olympiad_nobles_eom.char_id AND olympiad_nobles_eom.class_id = ? "
	    + "AND olympiad_nobles_eom.competitions_done >= 5 AND olympiad_nobles_eom.competitions_won > 0 "
	    + "ORDER BY olympiad_nobles_eom.olympiad_points DESC, olympiad_nobles_eom.competitions_done DESC LIMIT 10";
    private static final String GET_EACH_CLASS_LEADER_CURRENT = "SELECT characters.char_name from olympiad_nobles, characters "
		+ "WHERE characters.obj_Id = olympiad_nobles.char_id AND olympiad_nobles.class_id = ? "
		+ "AND olympiad_nobles.competitions_done >= 5 AND olympiad_nobles.competitions_won > 0 "
		+ "ORDER BY olympiad_nobles.olympiad_points DESC, olympiad_nobles.competitions_done DESC LIMIT 10";
    private static final String OLYMPIAD_DELETE_ALL = "TRUNCATE olympiad_nobles";
	private static final String OLYMPIAD_MONTH_CLEAR = "TRUNCATE olympiad_nobles_eom";
	private static final String OLYMPIAD_MONTH_CREATE = "INSERT INTO olympiad_nobles_eom SELECT * FROM olympiad_nobles";
    private static final int[] HERO_IDS = {88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,
        106,107,108,109,110,111,112,113,114,115,116,117,118};

    private static final int COMP_START = Config.ALT_OLY_START_TIME; // 8PM
    private static final int COMP_MIN = Config.ALT_OLY_MIN; // 00 mins
    private static final long COMP_PERIOD = Config.ALT_OLY_CPERIOD; // 4 hours
    protected static final long BATTLE_WAIT = Config.ALT_OLY_BWAIT; // 10mins
    protected static final long INITIAL_WAIT = Config.ALT_OLY_IWAIT;  // 5mins
    protected static final long WEEKLY_PERIOD = Config.ALT_OLY_WPERIOD; // 1 week
    protected static final long VALIDATION_PERIOD = Config.ALT_OLY_VPERIOD; // 24 hours

    private static final int DEFAULT_POINTS = 18;
    protected static final int WEEKLY_POINTS = 3;

    public static final String CHAR_ID = "char_id";
    public static final String CLASS_ID = "class_id";
    public static final String CHAR_NAME = "char_name";
    public static final String POINTS = "olympiad_points";
    public static final String COMP_DONE = "competitions_done";
    public static final String COMP_WON = "competitions_won";
    public static final String COMP_LOST = "competitions_lost";

    // Default values
    protected int _currentCycle = 1;
    protected int _period = 0;
    protected long _olympiadEnd = 0;
    protected long _validationEnd = 0;
    protected long _nextWeeklyChange = 0;
    
    private long _compEnd;
    private Calendar _compStart;
    
    public static final byte NONE = 0;
    public static final byte REGISTER = 1;
    public static final byte LAST_FIGHT = 2;

    protected byte _compPeriodState = NONE;

    protected ScheduledFuture<?> _scheduledOlympiadEnd;
    protected ScheduledFuture<?> _scheduledWeeklyTask;
    protected ScheduledFuture<?> _scheduledValidationTask;

    protected static enum COMP_TYPE
    {
        CLASSED,
        NON_CLASSED
    }

    public static Olympiad getInstance()
    {
        return SingletonHolder._instance;
    }

    private Olympiad()
    {
    	load();

        if (_period == 0)
            init();
    }
    
    private void load()
    {
        _nobles.clear();

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(OLYMPIAD_LOAD_DATA);
			ResultSet rset = statement.executeQuery())
		{
			while (rset.next())
			{
				_currentCycle = rset.getInt("current_cycle");
				_period = rset.getInt("period");
				_olympiadEnd = rset.getLong("olympiad_end");
				_validationEnd = rset.getLong("validation_end");
				_nextWeeklyChange = rset.getLong("next_weekly_change");
			}
		}
		catch (Exception e)
		{
			_log.warning("Olympiad System: Error loading olympiad data from database: " + e);
		}

        switch(_period)
        {
            case 0:
                if (_olympiadEnd == 0 || _olympiadEnd < Calendar.getInstance().getTimeInMillis())
                    setNewOlympiadEnd();
                else
                    scheduleWeeklyChange();
                break;
            case 1:
                if (_validationEnd > Calendar.getInstance().getTimeInMillis())
                {
                	loadNoblesRank();
                    _scheduledValidationTask = ThreadPoolManager.getInstance().scheduleGeneral(new ValidationEndTask(), getMillisToValidationEnd());
                }
                else
                {
                    _currentCycle++;
                    _period = 0;
                    deleteNobles();
                    setNewOlympiadEnd();
                }
                break;
            default:
                _log.warning("Olympiad System: Something went wrong while loading!! Period = " + _period);
                return;
        }

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(OLYMPIAD_LOAD_NOBLES);
            ResultSet rset = statement.executeQuery())
        {
            while(rset.next())
            {
                StatsSet statDat = new StatsSet();
                int charId = rset.getInt(CHAR_ID);
                statDat.set(CLASS_ID, rset.getInt(CLASS_ID));
                statDat.set(CHAR_NAME, rset.getString(CHAR_NAME));
                statDat.set(POINTS, rset.getInt(POINTS));
                statDat.set(COMP_DONE, rset.getInt(COMP_DONE));
                statDat.set(COMP_WON, rset.getInt(COMP_WON));
                statDat.set(COMP_LOST, rset.getInt(COMP_LOST));
                statDat.set("to_save", false);

                _nobles.put(charId, statDat);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        synchronized(this)
        {
            _log.info("Olympiad System: Loading Olympiad System....");
            if (_period == 0)
                _log.info("Olympiad System: Currently in Olympiad Period");
            else
                _log.info("Olympiad System: Currently in Validation Period");

            _log.info("Olympiad System: Period Ends....");

            long milliToEnd;
            if (_period == 0)
                milliToEnd = getMillisToOlympiadEnd();
            else
                milliToEnd = getMillisToValidationEnd();

            double numSecs = (milliToEnd / 1000) % 60;
            double countDown = ((milliToEnd / 1000) - numSecs) / 60;
            int numMins = (int) Math.floor(countDown % 60);
            countDown = (countDown - numMins) / 60;
            int numHours = (int) Math.floor(countDown % 24);
            int numDays = (int) Math.floor((countDown - numHours) / 24);

            _log.info("Olympiad System: In " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");

            if (_period == 0)
            {
                _log.info("Olympiad System: Next Weekly Change is in....");

                milliToEnd = getMillisToWeekChange();

                double numSecs2 = (milliToEnd / 1000) % 60;
                double countDown2 = ((milliToEnd / 1000) - numSecs2) / 60;
                int numMins2 = (int) Math.floor(countDown2 % 60);
                countDown2 = (countDown2 - numMins2) / 60;
                int numHours2 = (int) Math.floor(countDown2 % 24);
                int numDays2 = (int) Math.floor((countDown2 - numHours2) / 24);

                _log.info("Olympiad System: " + numDays2 + " days, " + numHours2 + " hours and " + numMins2 + " mins.");
            }
        }

        _log.info("Olympiad System: Loaded " + _nobles.size() + " nobles");
    }

    protected void loadNoblesRank()
	{
		_noblesRank.clear();
		Map<Integer, Integer> tmpPlace = new HashMap<>();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(GET_ALL_CLASSIFIED_NOBLESS);
			ResultSet rset = statement.executeQuery())
		{
			int place = 1;
			while (rset.next())
			{
				tmpPlace.put(rset.getInt(CHAR_ID), place++);
			}
		}
		catch (Exception e)
		{
			_log.warning("Olympiad System: Error loading noblesse data from database for Ranking: " + e);
		}

		int rank1 = (int) Math.round(tmpPlace.size() * 0.01);
		int rank2 = (int) Math.round(tmpPlace.size() * 0.10);
		int rank3 = (int) Math.round(tmpPlace.size() * 0.25);
		int rank4 = (int) Math.round(tmpPlace.size() * 0.50);
		if (rank1 == 0)
		{
			rank1 = 1;
			rank2++;
			rank3++;
			rank4++;
		}
		
		for (int charId : tmpPlace.keySet())
		{
			if (tmpPlace.get(charId) <= rank1)
				_noblesRank.put(charId, 1);
			else if (tmpPlace.get(charId) <= rank2)
				_noblesRank.put(charId, 2);
			else if (tmpPlace.get(charId) <= rank3)
				_noblesRank.put(charId, 3);
			else if (tmpPlace.get(charId) <= rank4)
				_noblesRank.put(charId, 4);
			else
				_noblesRank.put(charId, 5);
		}
	}
    
    protected void init()
    {
        if (_period == 1)
            return;

    	_nonClassBasedRegisters = new CopyOnWriteArrayList<>();
        _classBasedRegisters = new ConcurrentHashMap<>();

        _compStart = Calendar.getInstance();
        _compStart.set(Calendar.HOUR_OF_DAY, COMP_START);
        _compStart.set(Calendar.MINUTE, COMP_MIN);
        _compEnd = _compStart.getTimeInMillis() + COMP_PERIOD;

        if (_scheduledOlympiadEnd != null)
        {
            _scheduledOlympiadEnd.cancel(true);
        }

    	_scheduledOlympiadEnd = ThreadPoolManager.getInstance().scheduleGeneral(new OlympiadEndTask(), getMillisToOlympiadEnd());

        updateCompStatus();
    }

    protected class OlympiadEndTask implements Runnable
    {
        @Override
		public void run()
        {
            SystemMessage sm = new SystemMessage(SystemMessage.OLYMPIAD_PERIOD_S1_HAS_ENDED);
            sm.addNumber(_currentCycle);

            Announcements.getInstance().announceToAll(sm);
            Announcements.getInstance().announceToAll("Olympiad Validation Period has began.");

            if (_scheduledWeeklyTask != null)
            {
                _scheduledWeeklyTask.cancel(true);
                _scheduledWeeklyTask = null;
            }

            saveNobleData();

            _period = 1;
            Hero.getInstance().computeNewHeroes(getHeroesToBe());

            // Save Olympiad status data
            saveOlympiadStatus();
            
            updateMonthlyData();

            Calendar validationEnd = Calendar.getInstance();
            _validationEnd = validationEnd.getTimeInMillis() + VALIDATION_PERIOD;

            loadNoblesRank();

            if (_scheduledValidationTask != null)
            {
                _scheduledValidationTask.cancel(false);
            }
            _scheduledValidationTask  = ThreadPoolManager.getInstance().scheduleGeneral(new ValidationEndTask(), getMillisToValidationEnd());
        }
    }

    protected class ValidationEndTask implements Runnable
    {
        @Override
		public void run()
        {
            Announcements.getInstance().announceToAll("Olympiad Validation Period has ended.");
            _period = 0;
            _currentCycle++;
            deleteNobles();
            setNewOlympiadEnd();
            init();
        }
    }

    public boolean registerNoble(L2PcInstance noble, boolean classBased)
    {
        if (_compPeriodState != REGISTER)
        {
            noble.sendPacket(new SystemMessage(SystemMessage.THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS));
            return false;
        }

        // Checked in L2PcInstance
        if (!noble.checkOlympiadConditions())
            return false;

        if (_classBasedRegisters.containsKey(noble.getClassId().getId()))
        {
            List<L2PcInstance> classed = _classBasedRegisters.get(noble.getClassId().getId());
            for (L2PcInstance participant : classed)
            {
                if (participant.getObjectId() == noble.getObjectId())
                {

                    noble.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_ALREADY_ON_THE_WAITING_LIST_TO_PARTICIPATE_IN_THE_GAME_FOR_YOUR_CLASS));
                    return false;
                }
            }
        }

        if (isRegisteredInComp(noble))
        {
            noble.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_ALREADY_ON_THE_WAITING_LIST_FOR_ALL_CLASSES_WAITING_TO_PARTICIPATE_IN_THE_GAME));

            return false;
        }

        if (!_nobles.containsKey(noble.getObjectId()))
        {
            StatsSet statDat = new StatsSet();
            statDat.set(CLASS_ID, noble.getClassId().getId());
            statDat.set(CHAR_NAME, noble.getName());
            statDat.set(POINTS, DEFAULT_POINTS);
            statDat.set(COMP_DONE, 0);
            statDat.set(COMP_WON, 0);
            statDat.set(COMP_LOST, 0);
            statDat.set("to_save", true);

            _nobles.put(noble.getObjectId(), statDat);
        }


        if (classBased && getNoblePoints(noble.getObjectId()) < 3)
        {
            noble.sendMessage("Cannot register when you have less than 3 points.");
            return false;
        }

        if (!classBased && getNoblePoints(noble.getObjectId()) < 5)
        {
            noble.sendMessage("Cannot register when you have less than 5 points.");
            return false;
        }

        if (classBased)
        {
            if (_classBasedRegisters.containsKey(noble.getClassId().getId()))
            {
                List<L2PcInstance> classed = _classBasedRegisters.get(noble.getClassId().getId());
                classed.add(noble);

                _classBasedRegisters.put(noble.getClassId().getId(), classed);
            }
            else
            {
                List<L2PcInstance> classed = new CopyOnWriteArrayList<>();
                classed.add(noble);

                _classBasedRegisters.put(noble.getClassId().getId(), classed);
            }

            noble.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_CLASSIFIED_GAMES));
        }
        else
        {
            _nonClassBasedRegisters.add(noble);
            noble.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_NO_CLASS_GAMES));
        }

        return true;
    }

    protected static int getNobleCount()
	{
		return _nobles.size();
	}
    
    protected static StatsSet getNobleStats(int playerId)
	{
		return _nobles.get(playerId);
	}
    
    protected static List<L2PcInstance> getRegisteredNonClassBased()
	{
		return _nonClassBasedRegisters;
	}
	
	protected static Map<Integer, List<L2PcInstance>> getRegisteredClassBased()
	{
		return _classBasedRegisters;
	}
	
	protected static List<Integer> hasEnoughRegisteredClassed()
	{
		List<Integer> result = new ArrayList<>();
		for (Integer classList : getRegisteredClassBased().keySet())
		{
			if (getRegisteredClassBased().get(classList).size() >= Config.ALT_OLY_CLASSED)
			{
				result.add(classList);
			}
		}
		
		if (!result.isEmpty())
		{
			return result;
		}
		return null;
	}
	
	protected static boolean hasEnoughRegisteredNonClassed()
	{
		return Olympiad.getRegisteredNonClassBased().size() >= Config.ALT_OLY_NONCLASSED;
	}
	
	protected static void clearRegistered()
	{
		_nonClassBasedRegisters.clear();
		_classBasedRegisters.clear();
	}
    
    public boolean isRegistered(L2PcInstance noble)
    {
        boolean result = false;

        if (_nonClassBasedRegisters != null && _nonClassBasedRegisters.contains(noble))
            result = true;
        else if (_classBasedRegisters != null && _classBasedRegisters.containsKey(noble.getClassId().getId()))
        {
            List<L2PcInstance> classed = _classBasedRegisters.get(noble.getClassId().getId());
            if (classed != null && classed.contains(noble))
                result = true;
        }

        return result;
    }

    public boolean unRegisterNoble(L2PcInstance noble)
    {
        if (_compPeriodState == NONE)
        {
            noble.sendPacket(new SystemMessage(SystemMessage.THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS));
            return false;
        }

    	if (!noble.isNoble())
        {
            noble.sendPacket(new SystemMessage(SystemMessage.ONLY_NOBLESS_CAN_PARTICIPATE_IN_THE_OLYMPIAD));
            return false;
        }

    	if (!isRegistered(noble))
    	{
            noble.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_NOT_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_A_GAME));
            return false;
        }

        if (_nonClassBasedRegisters.contains(noble))
            _nonClassBasedRegisters.remove(noble);
        else
        {
            List<L2PcInstance> classed = _classBasedRegisters.get(noble.getClassId().getId());
            classed.remove(noble);

            _classBasedRegisters.put(noble.getClassId().getId(), classed);
        }

        for (OlympiadGame game : OlympiadManager.getInstance().getOlympiadGames().values())
        {
            if (game == null)
                continue;

            if (game._playerOneID == noble.getObjectId() || game._playerTwoID == noble.getObjectId())
            {
                noble.sendMessage("Cannot cancel registration while you are already selected for a game.");
                return false;
            }
        }

        noble.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_BEEN_DELETED_FROM_THE_WAITING_LIST_OF_A_GAME));

    	return true;
    }

    public void removeDisconnectedCompetitor(L2PcInstance player)
    {
    	if (OlympiadManager.getInstance().getOlympiadGame(player.getOlympiadGameId()) != null)
			OlympiadManager.getInstance().getOlympiadGame(player.getOlympiadGameId()).handleDisconnect(player);

        List<L2PcInstance> classed = _classBasedRegisters.get(player.getClassId().getId());

        if (_nonClassBasedRegisters.contains(player))
            _nonClassBasedRegisters.remove(player);
        else if (classed != null && classed.contains(player))
        {
            classed.remove(player);
            _classBasedRegisters.put(player.getClassId().getId(), classed);
        }
    }

    private void updateCompStatus()
    {
        synchronized(this)
        {
            long milliToStart = getMillisToCompBegin();

            double numSecs = (milliToStart / 1000) % 60;
            double countDown = ((milliToStart / 1000) - numSecs) / 60;
            int numMins = (int) Math.floor(countDown % 60);
            countDown = (countDown - numMins) / 60;
            int numHours = (int) Math.floor(countDown % 24);
            int numDays = (int) Math.floor((countDown - numHours) / 24);

            _log.info("Olympiad System: Competition Period Starts in " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");
            _log.info("Olympiad System: Event starts/started : " + _compStart.getTime());
        }

        ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
        {
            @Override
			public void run()
            {
                if (isOlympiadEnd())
                    return;

                _compPeriodState = REGISTER;
                OlympiadManager om = OlympiadManager.getInstance();

                Announcements.getInstance().announceToAll(new SystemMessage(SystemMessage.THE_OLYMPIAD_GAME_HAS_STARTED));

                _log.info("Olympiad System: Olympiad Game Started");

                Thread olyCycle = new Thread(om);

                olyCycle.start();

                long regEnd = getMillisToCompEnd() - 600000;
                if (regEnd > 0)
                {
                    ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                    {
                        @Override
						public void run()
                        {
                            if (isOlympiadEnd())
                                return;

                            _compPeriodState = LAST_FIGHT;
                            Announcements.getInstance().announceToAll("Olympiad Registration Period has ended.");
                        }
                    }, regEnd);
                }

                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                    @Override
					public void run()
                    {
                        if (isOlympiadEnd())
                            return;

                        _compPeriodState = NONE;
                        Announcements.getInstance().announceToAll(new SystemMessage(SystemMessage.THE_OLYMPIAD_GAME_HAS_ENDED));
                        _log.info("Olympiad System: Olympiad Game Ended");

                        while (OlympiadGame._battleStarted)
                        {
                        	try
                            {
                        		// Wait 1 minute for pending games to end
                                Thread.sleep(60000);
                            }
                            catch (InterruptedException e){}
                        }
                        save();
                        init();
                    }
                }, getMillisToCompEnd());
            }
        }, getMillisToCompBegin());
    }

    private long getMillisToOlympiadEnd()
    {
        return (_olympiadEnd - Calendar.getInstance().getTimeInMillis());
    }

    public void manualSelectHeroes()
    {
        if (_scheduledOlympiadEnd != null)
            _scheduledOlympiadEnd.cancel(true);

        _scheduledOlympiadEnd = ThreadPoolManager.getInstance().scheduleGeneral(new OlympiadEndTask(), 0);
    }

    protected long getMillisToValidationEnd()
    {
        if (_validationEnd > Calendar.getInstance().getTimeInMillis())
            return (_validationEnd - Calendar.getInstance().getTimeInMillis());
        return 10L;
    }

    public boolean isOlympiadEnd()
    {
    	return (_period != 0);
    }

    protected void setNewOlympiadEnd()
    {
        SystemMessage sm = new SystemMessage(SystemMessage.OLYMPIAD_PERIOD_S1_HAS_STARTED);
        sm.addNumber(_currentCycle);

        Announcements.getInstance().announceToAll(sm);

        Calendar currentTime = Calendar.getInstance();
        currentTime.add(Calendar.MONTH, 1);
        currentTime.set(Calendar.DAY_OF_MONTH, 1);
        currentTime.set(Calendar.AM_PM, Calendar.AM);
        currentTime.set(Calendar.HOUR, 12);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        _olympiadEnd = currentTime.getTimeInMillis();

        Calendar nextChange = Calendar.getInstance();
        _nextWeeklyChange = nextChange.getTimeInMillis() + WEEKLY_PERIOD;
        scheduleWeeklyChange();
    }

    public byte getCompPeriodState()
    {
        return _compPeriodState;
    }

    private long getMillisToCompBegin()
    {
        if (_compStart.getTimeInMillis() < Calendar.getInstance().getTimeInMillis() &&
                _compEnd > Calendar.getInstance().getTimeInMillis())
            return 10L;

        if (_compStart.getTimeInMillis() > Calendar.getInstance().getTimeInMillis())
            return (_compStart.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());

        return setNewCompBegin();
    }

    private long setNewCompBegin()
    {
        _compStart = Calendar.getInstance();
        _compStart.set(Calendar.HOUR_OF_DAY, COMP_START);
        _compStart.set(Calendar.MINUTE, COMP_MIN);
        _compStart.add(Calendar.HOUR_OF_DAY, 24);
        _compEnd = _compStart.getTimeInMillis() + COMP_PERIOD;

        _log.info("Olympiad System: New Schedule @ " + _compStart.getTime());

        return (_compStart.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
    }

    protected long getMillisToCompEnd()
    {
        return (_compEnd - Calendar.getInstance().getTimeInMillis());
    }

    private long getMillisToWeekChange()
    {
        if (_nextWeeklyChange > Calendar.getInstance().getTimeInMillis())
            return (_nextWeeklyChange - Calendar.getInstance().getTimeInMillis());
        return 10L;
    }

    private void scheduleWeeklyChange()
    {
        _scheduledWeeklyTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable()
        {
            @Override
			public void run()
            {
                addWeeklyPoints();
                _log.info("Olympiad System: Added weekly points to nobles");

                Calendar nextChange = Calendar.getInstance();
                _nextWeeklyChange = nextChange.getTimeInMillis() + WEEKLY_PERIOD;
            }
        }, getMillisToWeekChange(), WEEKLY_PERIOD);
    }

    protected synchronized void addWeeklyPoints()
    {
        if (_period == 1)
            return;

        for (Integer nobleId : _nobles.keySet())
        {
            StatsSet nobleInfo = _nobles.get(nobleId);
            int currentPoints = nobleInfo.getInteger(POINTS);
            currentPoints += WEEKLY_POINTS;
            nobleInfo.set(POINTS, currentPoints);
        }
    }

    public Map<Integer, String> getMatchList()
    {
    	return OlympiadManager.getInstance().getAllTitles();
    }

    public int getCurrentCycle()
    {
        return _currentCycle;
    }

    public void addSpectator(int id, L2PcInstance spectator, boolean storeCoords)
    {
    	if (isRegisteredInComp(spectator))
    	{
            spectator.sendPacket(new SystemMessage(SystemMessage.WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME));
            return;
    	}

        if (spectator.getEvent() != null)
            return;

        L2OlympiadStadiumZone stadium = ZoneManager.getInstance().getOlympiadStadium(id);
        if (stadium != null)
        {
        	stadium.addSpectator(spectator, storeCoords);
        }
    }
    
    public static void removeSpectator(int id, L2PcInstance spectator)
    {
    	L2OlympiadStadiumZone stadium = ZoneManager.getInstance().getOlympiadStadium(id);
        if (stadium != null)
        {
        	stadium.removeSpectator(spectator);
        }
    }

    public List<L2PcInstance> getSpectators(int id)
    {
    	OlympiadGame game = OlympiadManager.getInstance().getOlympiadGame(id);
    	if (game == null)
    	{
			return null;
    	}
    	
    	return game.getStadium().getSpectators();
    }

    public Map<Integer, OlympiadGame> getOlympiadGames()
    {
    	return OlympiadManager.getInstance().getOlympiadGames();
    }

    public boolean playerInStadium(L2PcInstance player)
    {
        return ZoneManager.getInstance().getOlympiadStadium(player) != null;
    }

    public int[] getWaitingList()
    {
        int[] array = new int[2];

        if (_compPeriodState == NONE)
            return null;

        int classCount = 0;

        if (_classBasedRegisters.size() != 0)
        {
            for (List<L2PcInstance> classed : _classBasedRegisters.values())
                classCount += classed.size();
        }

        array[0] = classCount;
        array[1] = _nonClassBasedRegisters.size();

        return array;
    }

    protected void saveNobleData()
    {
        if (_nobles == null || _nobles.isEmpty())
            return;

        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
        	for (Map.Entry<Integer, StatsSet> nobleEntry : _nobles.entrySet())
            {
        		final StatsSet nobleInfo = nobleEntry.getValue();
                if (nobleInfo == null)
                    continue;

                int charId = nobleEntry.getKey();
                int classId = nobleInfo.getInteger(CLASS_ID);

                int points = nobleInfo.getInteger(POINTS);
                int compDone = nobleInfo.getInteger(COMP_DONE);
                int compWon = nobleInfo.getInteger(COMP_WON);
                int compLost = nobleInfo.getInteger(COMP_LOST);
                boolean toSave = nobleInfo.getBool("to_save");

                if (toSave)
                {
                    try (PreparedStatement statement = con.prepareStatement(OLYMPIAD_SAVE_NOBLES))
                    {
                        statement.setInt(1, charId);
                        statement.setInt(2, classId);

                        statement.setInt(3, points);
                        statement.setInt(4, compDone);
                        statement.setInt(5, compWon);
                        statement.setInt(6, compLost);
                        statement.execute();
                    }

                    nobleInfo.set("to_save", false);
                }
                else
                {
                    try (PreparedStatement statement = con.prepareStatement(OLYMPIAD_UPDATE_NOBLES))
                    {
                        statement.setInt(1, points);
                        statement.setInt(2, compDone);
                        statement.setInt(3, compWon);
                        statement.setInt(4, compLost);
                        statement.setInt(5, charId);
                        statement.execute();
                    }
                }
            }
        }
        catch(SQLException e)
        {
            _log.warning("Olympiad System: Couldnt save nobles info in db");
        }
    }

    protected void updateMonthlyData()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement(OLYMPIAD_MONTH_CLEAR))
			{
				statement.execute();
			}
			
			try (PreparedStatement statement = con.prepareStatement(OLYMPIAD_MONTH_CREATE))
			{
				statement.execute();
			}
		}
		catch (SQLException e)
		{
			_log.severe("Olympiad System: Failed to update monthly noblesse data: " + e);
		}
	}
    
    protected List<StatsSet> getHeroesToBe()
    {
        final List<StatsSet> heroesToBe = new ArrayList<>();
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            StatsSet hero;

            for (int i = 0; i < HERO_IDS.length; i++)
            {
                try (PreparedStatement statement = con.prepareStatement(OLYMPIAD_GET_HEROES))
                {
                    statement.setInt(1, HERO_IDS[i]);
                    try (ResultSet rset = statement.executeQuery())
                    {
                        if (rset.next())
                        {
                            hero = new StatsSet();
                            hero.set(CLASS_ID, HERO_IDS[i]);
                            hero.set(CHAR_ID, rset.getInt(CHAR_ID));
                            hero.set(CHAR_NAME, rset.getString(CHAR_NAME));

                            heroesToBe.add(hero);
                        }
                    }
                }
            }
        }
        catch(SQLException e)
        {
            _log.warning("Olympiad System: Could not load heroes from db");
        }
        return heroesToBe;
    }

    public List<String> getClassLeaderBoard(int classId)
    {
    	List<String> names = new ArrayList<>();

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(Config.ALT_OLY_SHOW_MONTHLY_WINNERS ? GET_EACH_CLASS_LEADER : GET_EACH_CLASS_LEADER_CURRENT))
        {
            statement.setInt(1, classId);
            try (ResultSet rset = statement.executeQuery())
            {
                while (rset.next())
                {
                    names.add(rset.getString(CHAR_NAME));
                }
            }
        }
        catch(SQLException e)
        {
            _log.warning("Olympiad System: Could not load heroes from db");
        }

        return names;
    }

    public int getNoblessePasses(L2PcInstance player, boolean clear)
    {
        if (_period != 1 || _nobles.isEmpty() || _noblesRank.isEmpty())
            return 0;

        int objId = player.getObjectId();
		if (!_noblesRank.containsKey(objId))
			return 0;
		
		StatsSet noble = _nobles.get(objId);
		if (noble == null || noble.getInteger(POINTS) == 0)
			return 0;
		
		int rank = _noblesRank.get(objId);
		int points = (player.isHero() ? Config.ALT_OLY_HERO_POINTS : 0);
		switch (rank)
		{
			case 1:
				points += Config.ALT_OLY_RANK1_POINTS;
				break;
			case 2:
				points += Config.ALT_OLY_RANK2_POINTS;
				break;
			case 3:
				points += Config.ALT_OLY_RANK3_POINTS;
				break;
			case 4:
				points += Config.ALT_OLY_RANK4_POINTS;
				break;
			default:
				points += Config.ALT_OLY_RANK5_POINTS;
		}

		if (clear)
		{
			noble.set(POINTS, 0);
		}
		
		points *= Config.ALT_OLY_GP_PER_POINT;
		
		return points;
    }

    public boolean isRegisteredInComp(L2PcInstance player)
    {
        boolean result = isRegistered(player);

        if (_compPeriodState != NONE)
        {
            for (OlympiadGame game : OlympiadManager.getInstance().getOlympiadGames().values())
            {
                if (game == null)
                    continue;

                if (game._playerOneID == player.getObjectId() || game._playerTwoID == player.getObjectId())
                {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public int getNoblePoints(int objId)
    {
        if (_nobles.isEmpty())
            return 0;

        StatsSet noble = _nobles.get(objId);
        if (noble == null)
            return 0;
        int points = noble.getInteger(POINTS);

        return points;
    }

    public int getLastNobleOlympiadPoints(int objId)
	{
		int result = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT olympiad_points FROM olympiad_nobles_eom WHERE char_id = ?"))
		{
			statement.setInt(1, objId);
			try (ResultSet rs = statement.executeQuery())
			{
				if (rs.first())
					result = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			_log.warning("Olympiad System: Could not load last olympiad points:" + e);
		}
		return result;
	}
    
    public int getCompetitionDone(int objId)
    {
        if (_nobles.isEmpty())
            return 0;

        StatsSet noble = _nobles.get(objId);
        if (noble == null)
            return 0;
        int points = noble.getInteger(COMP_DONE);

        return points;
    }

    public int getCompetitionWon(int objId)
    {
        if (_nobles.isEmpty())
            return 0;

        StatsSet noble = _nobles.get(objId);
        if (noble == null)
            return 0;
        int points = noble.getInteger(COMP_WON);

        return points;
    }

    public int getCompetitionLost(int objId)
    {
        if (_nobles.isEmpty())
            return 0;

        StatsSet noble = _nobles.get(objId);
        if (noble == null)
            return 0;
        int points = noble.getInteger(COMP_LOST);

        return points;
    }

    protected void deleteNobles()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(OLYMPIAD_DELETE_ALL))
        {
            statement.execute();
        }
        catch(SQLException e)
        {
            _log.warning("Olympiad System: Couldnt delete nobles from db");
        }
        _nobles.clear();
    }

    /**
     * Saves all Olympiad data.
     */
    public void save()
    {
        saveNobleData();
        saveOlympiadStatus();
    }
    
    public void saveOlympiadStatus()
    {
    	try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(OLYMPIAD_SAVE_DATA))
		{
			statement.setInt(1, _currentCycle);
			statement.setInt(2, _period);
			statement.setLong(3, _olympiadEnd);
			statement.setLong(4, _validationEnd);
			statement.setLong(5, _nextWeeklyChange);
			statement.setInt(6, _currentCycle);
			statement.setInt(7, _period);
			statement.setLong(8, _olympiadEnd);
			statement.setLong(9, _validationEnd);
			statement.setLong(10, _nextWeeklyChange);
			statement.execute();
		}
		catch (SQLException e)
		{
			_log.severe("Olympiad System: Failed to save olympiad data to database: " + e);
		}
    }

    public void sendMatchList(L2PcInstance player)
    {
    	NpcHtmlMessage message = new NpcHtmlMessage(0);
		message.setFile(Olympiad.OLYMPIAD_HTML_PATH + "olympiad_observe2.htm");

		Map<Integer, String> matches = getMatchList();
		Collection<L2OlympiadStadiumZone> stadiums = ZoneManager.getInstance().getAllZones(L2OlympiadStadiumZone.class);
		for (L2OlympiadStadiumZone stadium : stadiums)
		{
			String state = "Initial State";
			String players = "&nbsp;";
			if (matches.containsKey(stadium.getStadiumId()))
			{
				OlympiadGame game = OlympiadManager.getInstance().getOlympiadGame(stadium.getStadiumId());
				if (game != null && game.getState() == OlympiadGame.PLAYING)
					state = "Playing";
				else
					state = "Standby";
				players = matches.get(stadium.getStadiumId());
			}
			message.replace("%state" + stadium.getStadiumId() + "%", state);
			message.replace("%players" + stadium.getStadiumId() + "%", players);
        }

        player.sendPacket(message);
    }

    public void bypassChangeArena(String command, L2PcInstance player)
    {
        if (!player.inObserverMode() || player.isTeleporting())
            return;

        String[] commands = command.split(" ");
        int id = Integer.parseInt(commands[1]);

        int arenaId = player.getOlympiadGameId();
        if (arenaId < 0 || arenaId == id)
            return;

        removeSpectator(arenaId, player);
        // Reset interface
        player.sendPacket(new ExOlympiadMatchEnd());

        addSpectator(id, player, false);
    }
    
    public static void sendUserInfo(L2PcInstance player)
    {
    	// Get the game this spectator observes
    	OlympiadGame game = OlympiadManager.getInstance().getOlympiadGame(player.getOlympiadGameId());
    	if (game == null)
    	{
    		return;
    	}

        // Send player status to opponent
        L2PcInstance opponent = game.getOpponentOf(player);
        if (opponent != null)
        {
            opponent.sendPacket(new ExOlympiadUserInfo(player));
        }
        else
        {
            _log.warning("Olympiad System: Failed to find opponent for player " + player.getName() + " - " + player.getObjectId());
        }

        // Notify spectators
        for (L2PcInstance spectator : game.getStadium().getSpectators())
	    {
	        if (spectator == null)
	            continue;
	
	        spectator.sendPacket(new ExOlympiadUserInfo(player));
	    }
    }
    
    public static void broadcastUsersInfo(L2PcInstance spectator)
    {
    	// Get the game this spectator observes
    	final OlympiadGame game = OlympiadManager.getInstance().getOlympiadGame(spectator.getOlympiadGameId());
    	if (game == null)
    	{
    		return;
    	}
    	
    	// Check if game has started
    	if (game.getState() < OlympiadGame.PLAYING)
    	{
    		return;
    	}
    	
    	// Broadcast user spelled packets to spectator
        for (L2PcInstance player : game.getPlayers())
    	{
            if (player == null)
            {
                continue;
            }

    		spectator.sendPacket(new ExOlympiadUserInfo(player));
   
    		ExOlympiadSpelledInfo os = new ExOlympiadSpelledInfo(player);

    		// Get all player effects
    		L2Effect[] effects = player.getAllEffects();
    		for (L2Effect e : effects)
    		{
    			if (e == null || !e.getShowIcon())
					continue;
				
				if (e.getInUse())
				{
					e.addOlympiadSpelledIcon(os);
				}
    		}
    		spectator.sendPacket(os);
    	}
    }
    
    private static class SingletonHolder
	{
		protected static final Olympiad _instance = new Olympiad();
	}
}