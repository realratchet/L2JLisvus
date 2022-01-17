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
package net.sf.l2j.gameserver.instancemanager;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.eventhandling.EventHandler;
import net.sf.l2j.gameserver.eventhandling.EventObject;
import net.sf.l2j.gameserver.eventhandling.events.GameTimeEvent;

/**
 * @author DnR
 *
 */
public class EventHandleManager extends Manager
{
	private final static Logger _log = Logger.getLogger(EventHandleManager.class.getName());
	
	public static enum EventType
	{
		HOUR_CHANGED(GameTimeEvent.class),
		DAY_NIGHT_CHANGED(GameTimeEvent.class);

		private final Class<? extends EventObject> _class;

		private EventType()
		{
			_class = EventObject.class;
		}

		private EventType(Class<? extends EventObject> classType)
		{
			_class = classType;
		}
		
		public Class<? extends EventObject> getEventHandlerClass()
		{
			return _class;
		}
	}
	
	public static EventHandleManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private final Map<EventType, Set<EventHandler<?>>> _handlerTypes = new ConcurrentHashMap<>();
	
	private EventHandleManager()
	{
		_log.info("Initialized Event Handle Manager");
	}
	
	public <T extends EventObject> void addEventHandler(EventType type, EventHandler<?> handler)
	{
		Set<EventHandler<?>> handlers;
		if (_handlerTypes.containsKey(type))
		{
			handlers = _handlerTypes.get(type);
		}
		else
		{
			handlers = ConcurrentHashMap.newKeySet();
			_handlerTypes.put(type, handlers);
		}
		
		handlers.add(handler);
	}
	
	public <T extends EventObject> void removeEventHandler(EventType type, EventHandler<T> handler)
	{
		if (!_handlerTypes.containsKey(type))
		{
			_log.warning(getClass().getSimpleName() + ": Attempt to remove event handler failed! Event type " + type.name() + " not found!");
			return;
		}
		
		Set<EventHandler<?>> handlers = _handlerTypes.get(type);
		if (!handlers.contains(handler))
		{
			_log.warning(getClass().getSimpleName() + ": Attempt to remove event handler failed! Handler is not associated with event type " + type.name() + "!");
			return;
		}
		
		handlers.remove(handler);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EventObject> void dispatchEvent(EventType type, T event)
	{
		// There is no registered event for the given type
		if (!_handlerTypes.containsKey(type))
		{
			return;
		}
		
		if (!type.getEventHandlerClass().isInstance(event))
		{
			_log.severe(getClass().getSimpleName() + ": Event type and object do not match. Dispatch " + type.name() + " has failed!");
			return;
		}
		
		Set<EventHandler<?>> handlers = _handlerTypes.get(type);
		for (EventHandler<?> handler : handlers)
		{
			((EventHandler<T>)handler).onStateChanged(event);
		}
	}
	
	private static class SingletonHolder
	{
		protected static final EventHandleManager _instance = new EventHandleManager();
	}
}
