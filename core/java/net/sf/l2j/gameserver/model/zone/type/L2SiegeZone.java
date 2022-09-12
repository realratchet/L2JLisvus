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

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeSummonInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.zone.L2ZoneSpawn;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * A castle zone
 *
 * @author  durgus
 */
public class L2SiegeZone extends L2ZoneSpawn
{
    private int _castleId;
    private Castle _castle;

    public L2SiegeZone(int id)
    {
        super(id);

    }

    @Override
    public void setParameter(String name, String value)
    {
        if (name.equals("castleId"))
        {
            _castleId = Integer.parseInt(value);

            // Register self to the correct castle
            _castle = CastleManager.getInstance().getCastleById(_castleId);
            _castle.setZone(this);
        }

        else
            super.setParameter(name, value);
    }

    @Override
    protected void onEnter(L2Character character)
    {
        if (_castle.getSiege().getIsInProgress())
        {
            character.setInsideZone(L2Character.ZONE_PVP, true);
            character.setInsideZone(L2Character.ZONE_SIEGE, true);

            if (character instanceof L2PcInstance)
            {
                L2PcInstance player = (L2PcInstance)character;
                player.sendPacket(new SystemMessage(SystemMessage.ENTERED_COMBAT_ZONE));

                if (player.isFlying())
                {
                    boolean isCastleLord = player.getClan() != null && player.isClanLeader() && player.getClan().getHasCastle() == _castle.getCastleId();
                    if (!isCastleLord)
                        player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
                }
            }
        }

    }

    @Override
    protected void onExit(L2Character character)
    {
        if (_castle.getSiege().getIsInProgress())
        {
            character.setInsideZone(L2Character.ZONE_PVP, false);
            character.setInsideZone(L2Character.ZONE_SIEGE, false);

            if (character instanceof L2PcInstance)
            {
                L2PcInstance player = (L2PcInstance)character;
                player.sendPacket(new SystemMessage(SystemMessage.LEFT_COMBAT_ZONE));

                // Set pvp flag
                if (player.getPvpFlag() == 0)
                    player.updatePvPStatus();
            }
        }

        if (character instanceof L2SiegeSummonInstance)
            ((L2SiegeSummonInstance)character).unSummon(((L2SiegeSummonInstance)character).getOwner());
    }

    public void updateZoneStatusForCharactersInside()
    {
        if (_castle.getSiege().getIsInProgress())
        {
            for (L2Character character : _characterList.values())
            {
                try	
                { 
                    onEnter(character); 
                } 
                catch (NullPointerException e) {}
            }
        }
        else
        {
            for (L2Character character : _characterList.values())
            {
                try	
                { 
                    character.setInsideZone(L2Character.ZONE_PVP, false);
                    character.setInsideZone(L2Character.ZONE_SIEGE, false);

                    if (character instanceof L2PcInstance)
                        ((L2PcInstance)character).sendPacket(new SystemMessage(SystemMessage.LEFT_COMBAT_ZONE));

                    if (character instanceof L2SiegeSummonInstance)
                        ((L2SiegeSummonInstance)character).unSummon(((L2SiegeSummonInstance)character).getOwner());
                } 
                catch (NullPointerException e) {}
            }
        }
    }

    /**
     * Removes all foreigners from the castle
     * @param ownerId
     */
    public void banishForeigners(int ownerId)
    {
        for (L2Character temp : _characterList.values())
        {
            if (!(temp instanceof L2PcInstance) || ((L2PcInstance)temp).getClanId() == ownerId)
                continue;
            
            ((L2PcInstance)temp).teleToLocation(MapRegionTable.TeleportWhereType.Town); 
        }
    }

    /**
     * Sends a message to all players in this zone
     * @param message
     */
    public void announceToPlayers(String message)
    {
        for (L2Character temp : _characterList.values())
        {
            if (temp instanceof L2PcInstance)
                ((L2PcInstance)temp).sendMessage(message);
        }
    }

    /**
     * Returns all players within this zone
     * @return
     */
    public List<L2PcInstance> getAllPlayers()
    {
        List<L2PcInstance> players = new ArrayList<>();

        for (L2Character temp : _characterList.values())
        {
            if (temp instanceof L2PcInstance)
                players.add((L2PcInstance)temp);
        }

        return players;
    }
}