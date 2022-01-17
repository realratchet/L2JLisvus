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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneSpawn;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Olympiad stadium zone.
 *
 * @author  durgus
 */
public class L2OlympiadStadiumZone extends L2ZoneSpawn
{
    private int _stadiumId;
    
    private boolean _freeToUse = true;
	private final List<L2PcInstance> _spectators = new CopyOnWriteArrayList<>();

    public L2OlympiadStadiumZone(int id)
    {
        super(id);
    }
    
    @Override
    public void setParameter(String name, String value)
    {
        if (name.equals("stadiumId"))
            _stadiumId = Integer.parseInt(value);
        else
            super.setParameter(name, value);
    }
    
    @Override
    protected void onEnter(L2Character character)
    {
        character.setInsideZone(L2Character.ZONE_PVP, true);
        character.setInsideZone(L2Character.ZONE_NO_LANDING, true);

        L2PcInstance player = null;
        if (character instanceof L2PcInstance)
        {
            player = (L2PcInstance)character;
            player.sendPacket(new SystemMessage(SystemMessage.ENTERED_COMBAT_ZONE));
        }
        else if (character instanceof L2Summon)
            player = ((L2Summon)character).getOwner();

        if (player != null)
        {
            if (!player.isGM() && !player.isInOlympiadMode() && !player.inObserverMode())

                player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
            else
            {
                if (player.isMounted())
                    player.dismount();
            }
        }
    }

    @Override
    protected void onExit(L2Character character)
    {
        character.setInsideZone(L2Character.ZONE_PVP, false);
        character.setInsideZone(L2Character.ZONE_NO_LANDING, false);

        if (character instanceof L2PcInstance)
            ((L2PcInstance)character).sendPacket(new SystemMessage(SystemMessage.LEFT_COMBAT_ZONE));
    }

    public int getStadiumId()
    {
        return _stadiumId;
    }
    
    public boolean isFreeToUse()
	{
        return _freeToUse;
	}

	public void setStadiaBusy()
	{
        _freeToUse = false;
	}

	public void setStadiaFree()
	{
        _freeToUse = true;
	}
	public void addSpectator(L2PcInstance spec, boolean storeCoords)
	{
		Location loc = getAlternateSpawnLoc();
		if (loc == null)
		{
			return;
		}
		
		spec.enterOlympiadObserverMode(loc.getX(), loc.getY(), loc.getZ(), getStadiumId(), storeCoords);
		_spectators.add(spec);
	}

	public List<L2PcInstance> getSpectators()
	{
		return _spectators;
	}
	
	public void removeSpectator(L2PcInstance spec)
	{
		if (_spectators != null && _spectators.contains(spec))
			_spectators.remove(spec);
	}
}