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
package net.sf.l2j.gameserver.model.quest;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author DnR
 */
public abstract class L2EventScript extends Quest
{
	/**
	 * Event States.
	 */
	public static enum EventState
	{
		INITIAL,
		REGISTER,
		STARTED
	}
	
	// Event default state
	private EventState _state = EventState.INITIAL;

	public L2EventScript(int questId, String name, String descr)
	{
		super(questId, name, descr);
	}
	
	public boolean isStarted()
	{
		return _state == EventState.STARTED;
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
	 * @param player
	 * @return
	 */
	public abstract boolean isRegistered(L2PcInstance player);
	
	/**
	 * @param player
	 */
	public abstract void onDie(L2PcInstance player);
	
	/**
	 * @param player
	 * @param target
	 */
	public abstract void onKill(L2PcInstance player, L2PcInstance target);
	
	/**
	 * @param player
	 */
	public abstract void onRevive(L2PcInstance player);
	
	public abstract boolean isRemovingBuffsOnRespawn();
	
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
}
