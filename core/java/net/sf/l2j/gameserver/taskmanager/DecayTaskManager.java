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
package net.sf.l2j.gameserver.taskmanager;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Character;

/**
 * @author la2 Lets drink to code!
 */
public class DecayTaskManager
{
	protected static final Logger _log = Logger.getLogger(DecayTaskManager.class.getName());
	
	protected Map<L2Character, Long> _decayTasks = new ConcurrentHashMap<>();

	public DecayTaskManager()
	{
		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new DecayScheduler(), 10000, 5000);
	}

	public static DecayTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}

	public void addDecayTask(L2Character actor)
	{
		_decayTasks.put(actor, System.currentTimeMillis());
	}

	public void addDecayTask(L2Character actor, int interval)
	{
		_decayTasks.put(actor, System.currentTimeMillis() + interval);
	}

	public void cancelDecayTask(L2Character actor)
	{
		try
		{
			_decayTasks.remove(actor);
		}
		catch (NoSuchElementException e)
		{
		}
	}

	private class DecayScheduler implements Runnable
	{
		protected DecayScheduler()
		{
			// Do nothing
		}

		@Override
		public void run()
		{
			Long current = System.currentTimeMillis();
			try
			{
				if (_decayTasks != null)
				{
					for (L2Character actor : _decayTasks.keySet())
					{
						if ((current - _decayTasks.get(actor)) > actor.getDecayTime())
						{
							actor.onDecay();
							_decayTasks.remove(actor);
						}
					}
				}
			}
			catch (Throwable e)
			{
				// TODO: Find out the reason for exception. Unless caught here, mob decay would stop.
				_log.warning(e.toString());
			}
		}
	}

	/**
	 * <u><b><font color="FF0000">Read only</font></b></u>
	 * @return
	 */
	public Map<L2Character, Long> getTasks()
	{
		return _decayTasks;
	}

	@Override
	public String toString()
	{
		String ret = "============= DecayTask Manager Report ============\r\n";
		ret += "Tasks count: " + _decayTasks.size() + "\r\n";
		ret += "Tasks dump:\r\n";

		Long current = System.currentTimeMillis();
		for (L2Character actor : _decayTasks.keySet())
		{
			ret += "Class/Name: " + actor.getClass().getSimpleName() + "/" + actor.getName() + " decay timer: " + (current - _decayTasks.get(actor)) + "\r\n";
		}

		return ret;
	}
	
	private static class SingletonHolder
	{
		protected static final DecayTaskManager _instance = new DecayTaskManager();
	}
}