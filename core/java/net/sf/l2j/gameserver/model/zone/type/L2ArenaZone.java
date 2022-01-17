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
package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.model.zone.L2ZoneSpawn;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * An arena
 *
 * @author  durgus
 */
public class L2ArenaZone extends L2ZoneSpawn
{
    private String _arenaName;

    private L2Event _event;
    
    public L2ArenaZone(int id)
    {
        super(id);
    }

    @Override
    protected void onEnter(L2Character character)
    {
        character.setInsideZone(L2Character.ZONE_PVP, true);

        if (character instanceof L2PcInstance)
        {
            L2PcInstance player = (L2PcInstance)character;
            
            // Check if arena is currently hosting an event and player belongs to this event
            L2Event event = getEvent();
            if (event != null && event.isStarted() && player.getEvent() != event)
            {
            	// Teleport foreigner to town
            	player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
            }
            
            player.sendPacket(new SystemMessage(SystemMessage.ENTERED_COMBAT_ZONE));
        }
    }

    @Override
    protected void onExit(L2Character character)
    {
        character.setInsideZone(L2Character.ZONE_PVP, false);

        if (character instanceof L2PcInstance)
            ((L2PcInstance)character).sendPacket(new SystemMessage(SystemMessage.LEFT_COMBAT_ZONE));
    }

	/**
	 * @return the arena name
	 */
	public String getArenaName()
	{
		return _arenaName;
	}

	/**
	 * @param arenaName the arena name to set
	 */
	public void setArenaName(String arenaName)
	{
		_arenaName = arenaName;
	}
	
	public L2Event getEvent()
	{
		return _event;
	}
	
	public void setEvent(L2Event event)
	{
		_event = event;
	}
}