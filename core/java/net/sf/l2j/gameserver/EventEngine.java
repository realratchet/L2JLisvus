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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.model.eventgame.TvTEvent;
import net.sf.l2j.gameserver.model.eventgame.L2Event.EventState;

public class EventEngine
{
	private static final Logger _log = Logger.getLogger(EventEngine.class.getName());
	
	private final List<L2Event> _events = new CopyOnWriteArrayList<>();
	
	public static EventEngine getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private EventEngine()
	{
		// Registered events
		_events.add(TvTEvent.getInstance());
		
		load();
	}

	/**
	 * Loads settings for all registered events.
	 */
    public void load()
    {
        // Load all Events and their settings
        Properties eventSettings = new Properties();
        try (InputStream is = new FileInputStream(new File(Config.EVENTS_FILE)))
        {
            eventSettings.load(is);
        }
        catch (Exception e)
        {
            _log.warning("EventEngine: Error while loading events settings");
            e.printStackTrace();
        }

        // Initialize settings for all events
        for (L2Event event : _events)
        {
        	// Reload settings if event is not underway
        	if (event.getEventState() != EventState.STARTED)
        	{
        		event.load(eventSettings);
        	}
        }
        
        _log.info("EventEngine: Loaded event settings");
    }
    
    private static class SingletonHolder
	{
		protected static final EventEngine _instance = new EventEngine();
	}
}