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
package net.sf.l2j.gameserver.model.eventgame;

import java.util.Properties;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.type.L2ArenaZone;

/**
 * @author DnR
 *
 */
public abstract class L2Event
{
	/**
	 *  Event States.
	 */
	public static enum EventState
	{
		INITIAL,
		REGISTER,
		SCHEDULED_NEXT,
		STARTED
	}

	protected boolean _isEnabled = false;
	
	// Event default state
	protected EventState _state = EventState.INITIAL;
	
	// Event zone
	protected L2ArenaZone _arena;
	
	// Event global parameters
	protected int _minParticipants;
	protected int _maxParticipants;
	protected byte _minLevel;
	protected byte _maxLevel;

	protected L2NpcInstance _npcManager;
	
	protected int _eventDelay;
	protected int _participationTime;
	protected int _eventDuration;
	protected int _playerRespawnDelay;
	
	protected boolean _isDualBoxAllowed;
	protected boolean _isRemovingBuffsOnRespawn;
	
	protected int _npcX, _npcY, _npcZ;
	
	/**
	 * Checks whether event is enabled.
	 * 
	 * @return
	 */
	public boolean isEnabled()
	{
		return _isEnabled;
	}
	
	public boolean isStarted()
	{
		return _state == EventState.STARTED;
	}
	
	public boolean isRemovingBuffsOnRespawn()
	{
		return _isRemovingBuffsOnRespawn;
	}
	
	public EventState getEventState()
	{
		return _state;
	}
	
	public void setEventState(EventState state)
	{
		_state = state;
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public abstract boolean isRegistered(L2PcInstance player);
	
	/**
	 * @param eventSettings
	 */
	public abstract void load(Properties eventSettings);
	
	/**
	 * 
	 * @param player
	 */
	public abstract  void onDie(L2PcInstance player);
	
	/**
	 * 
	 * @param player
	 * @param target 
	 */
	public abstract  void onKill(L2PcInstance player, L2PcInstance target);
	
	/**
	 * 
	 * @param player
	 */
	public abstract  void onRevive(L2PcInstance player);
	
	/**
	 * @param player
	 */
	public void registerPlayer(L2PcInstance player)
	{
		player.setEvent(this);
	}

	/**
	 * @param player
	 */
	public void removePlayer(L2PcInstance player)
	{
		player.setEvent(null);
	}
	
	/**
	 * 
	 * @return
	 */
	public L2ArenaZone getArena()
	{
		return _arena;
	}
}
