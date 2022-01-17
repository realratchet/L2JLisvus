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
package net.sf.l2j.gameserver;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.eventhandling.events.GameTimeEvent;
import net.sf.l2j.gameserver.instancemanager.EventHandleManager;
import net.sf.l2j.gameserver.instancemanager.EventHandleManager.EventType;
import net.sf.l2j.gameserver.model.L2Character;

/**
 * This class ...
 * 
 * @version $Revision: 1.1.4.8 $ $Date: 2005/04/06 16:13:24 $
 */
public class GameTimeController extends Thread
{
	private static final Logger _log = Logger.getLogger(GameTimeController.class.getName());

	public static final int TICKS_PER_SECOND = 10; // not able to change this without checking through code
	public static final int MILLIS_IN_TICK = 1000 / TICKS_PER_SECOND;
	public static final int IG_DAYS_PER_DAY = 6;
	public static final int MILLIS_PER_IG_DAY = (3600000 * 24) / IG_DAYS_PER_DAY;
	public static final int SECONDS_PER_IG_DAY = MILLIS_PER_IG_DAY / 1000;
	public static final int MINUTES_PER_IG_DAY = SECONDS_PER_IG_DAY / 60;
	public static final int TICKS_PER_IG_DAY = SECONDS_PER_IG_DAY * TICKS_PER_SECOND;
	public static final int TICKS_SUN_STATE_CHANGE = TICKS_PER_IG_DAY / 4;
	
	private final Set<L2Character> _movingObjects = ConcurrentHashMap.newKeySet();

	private final long _referenceTime;
	
	private boolean _isActive = false;

	public static GameTimeController getInstance()
	{
		return SingletonHolder._instance;
	}

	private GameTimeController()
	{
		super("GameTimeController");
		super.setDaemon(true);
		super.setPriority(MAX_PRIORITY);
		
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		_referenceTime = c.getTimeInMillis();

		_isActive = true;
	}

	public final int getGameTime()
	{
		return (getGameTicks() % TICKS_PER_IG_DAY) / MILLIS_IN_TICK;
	}
	
	public final int getGameHour()
	{
		return getGameTime() / 60;
	}
	
	public final int getGameMinute()
	{
		return getGameTime() % 60;
	}
	
	public final boolean isNight()
	{
		return getGameHour() < 6;
	}
	
	/**
	 * The true GameTime tick. Directly taken from current time. This represents the tick of the time.
	 * @return
	 */
	public final int getGameTicks()
	{
		return (int) ((System.currentTimeMillis() - _referenceTime) / MILLIS_IN_TICK);
	}

	/**
	 * Add a L2Character to movingObjects of GameTimeController.<BR><BR>
	 * 
	 * <B><U> Concept</U> :</B><BR><BR>
	 * All L2Character in movement are identified in <B>movingObjects</B> of GameTimeController.<BR><BR>
	 * 
	 * @param cha The L2Character to add to movingObjects of GameTimeController
	 * 
	 */
	public void registerMovingObject(L2Character cha)
	{
        if (cha == null)
            return;
        _movingObjects.add(cha);
	}

    /**
     * Move all L2Characters contained in movingObjects of GameTimeController.<BR><BR>
     * 
     * <B><U> Concept</U> :</B><BR><BR>
     * All L2Character in movement are identified in <B>movingObjects</B> of GameTimeController.<BR><BR>
     * 
     * <B><U> Actions</U> :</B><BR><BR>
     * <li>Update the position of each L2Character </li>
     * <li>If movement is finished, the L2Character is removed from movingObjects </li>
     * <li>Create a task to update the _knownObject and _knowPlayers of each L2Character that finished its movement and of their already known L2Object then notify AI with EVT_ARRIVED </li><BR><BR>
     * 
     */
    protected void moveObjects()
    {
    	_movingObjects.removeIf(L2Character::updatePosition);
    }

	public void stopTimer()
	{
		_isActive = false;
		super.interrupt();
		_log.info("Stopping " + getClass().getSimpleName());
	}

	@Override
	public final void run()
	{
		_log.info("Started " + getClass().getSimpleName());
		
		long nextTickTime, sleepTime;
		int gameHour = getGameHour();
		boolean isNight = gameHour < 6;
		
		while (_isActive)
		{
			nextTickTime = ((System.currentTimeMillis() / MILLIS_IN_TICK) * MILLIS_IN_TICK) + 100;
			
			try
			{
				moveObjects();
			}
			catch (final Throwable e)
			{
				_log.log(Level.WARNING, "Unable to move objects!", e);
			}
			
			sleepTime = nextTickTime - System.currentTimeMillis();
			if (sleepTime > 0)
			{
				try
				{
					Thread.sleep(sleepTime);
				}
				catch (final InterruptedException e)
				{
				}
			}
			
			final int gameHourTemp = getGameHour();
			final boolean isNightTemp = gameHourTemp < 6;
			
			if (gameHourTemp != gameHour)
			{
				gameHour = gameHourTemp;

				ThreadPoolManager.getInstance().executeAi(() -> 
					EventHandleManager.getInstance().dispatchEvent(EventType.HOUR_CHANGED, new GameTimeEvent(gameHourTemp, isNightTemp)));
			}
			
			if (isNightTemp != isNight)
			{
				isNight = !isNight;

				ThreadPoolManager.getInstance().executeAi(() -> 
					EventHandleManager.getInstance().dispatchEvent(EventType.DAY_NIGHT_CHANGED, new GameTimeEvent(gameHourTemp, isNightTemp)));
			}
		}
	}
	
	private static class SingletonHolder
	{
		protected static final GameTimeController _instance = new GameTimeController();
	}
}