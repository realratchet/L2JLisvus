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
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.ai.L2BoatAI;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.knownlist.BoatKnownList;
import net.sf.l2j.gameserver.model.actor.stat.BoatStat;
import net.sf.l2j.gameserver.model.actor.vehicle.L2BoatCaptain;
import net.sf.l2j.gameserver.model.actor.vehicle.L2BoatTrajet;
import net.sf.l2j.gameserver.model.actor.vehicle.L2VehiclePathPoint;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.VehicleDeparture;
import net.sf.l2j.gameserver.network.serverpackets.VehicleInfo;
import net.sf.l2j.gameserver.network.serverpackets.VehicleStarted;
import net.sf.l2j.gameserver.templates.L2CharTemplate;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.util.Util;

/**
 * @author Maktakien
 */
public class L2BoatInstance extends L2Character
{
    private final Set<L2PcInstance> _passengers = ConcurrentHashMap.newKeySet();
    private L2VehiclePathPoint[] _currentPath;
    
    private byte _runState = 0;
    
    private L2BoatTrajet[] _trajets;
    
    // default
    private int _cycle = 1;
    
    public class BoatAIAccessor extends L2Character.AIAccessor
    {
        @Override
        public void detachAI()
        {
        }
    }
    
    public L2BoatInstance(int objectId, L2CharTemplate template)
    {
        super(objectId, template);
        getKnownList();
        getStat();
        setAI(initAI());
    }
    
    @Override
    public L2BoatAI initAI()
    {
        return new L2BoatAI(new BoatAIAccessor());
    }
    
    @Override
    public final BoatKnownList getKnownList()
    {
        if (super.getKnownList() == null || !(super.getKnownList() instanceof BoatKnownList))
            setKnownList(new BoatKnownList(this));
        return (BoatKnownList) super.getKnownList();
    }
    
    @Override
    public final BoatStat getStat()
    {
        if (super.getStat() == null || !(super.getStat() instanceof BoatStat))
            setStat(new BoatStat(this));
        return (BoatStat) super.getStat();
    }
    
    public L2BoatTrajet getCurrentTrajet()
    {
        return _trajets[_cycle == 1 ? 0 : 1];
    }
    
    public void begin()
    {
        // firstly, check passengers
        checkPassengers();
        
        _runState = 0;
        _currentPath = getCurrentTrajet().getPath();
        
        if (_currentPath == null)
        {
            getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
            return;
        }
        
        L2VehiclePathPoint point = _currentPath[0];
        if (point != null)
        {
            getStat().setMoveSpeed(point.getMoveSpeed());
            getStat().setRotationSpeed(point.getRotationSpeed());
            
            // departure
            getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(point.getX(), point.getY(), point.getZ()));
        }
    }
    
    private void checkPassengers()
    {
        _passengers.clear();
        
        final Collection<L2PcInstance> knownPlayers = getKnownList().getKnownPlayersInRadius(1000);
        if (knownPlayers != null && !knownPlayers.isEmpty())
        {
            for (L2PcInstance player : knownPlayers)
            {
                if (player == null)
                    continue;
                
                if (player.isInBoat() && player.getBoat() == this)
                    addPassenger(player);
            }
        }
    }
    
    @Override
    public boolean moveToNextRoutePoint()
    {
        _runState++;
        
        if (_runState < _currentPath.length)
        {
            final L2VehiclePathPoint point = _currentPath[_runState];
            if (!isMovementDisabled())
            {
                getStat().setMoveSpeed(point.getMoveSpeed());
                getStat().setRotationSpeed(point.getRotationSpeed());
                
                MoveData m = new MoveData();
                m.disregardingGeodata = false;
                m.onGeodataPathIndex = -1;
                m._xDestination = point.getX();
                m._yDestination = point.getY();
                m._zDestination = point.getZ();
                m._heading = 0;
                
                final double dx = point.getX() - getX();
                final double dy = point.getY() - getY();
                final double distance = Math.sqrt((dx * dx) + (dy * dy));
                if (distance > 1)
                    setHeading(Util.calculateHeadingFrom(getX(), getY(), point.getX(), point.getY()));
                
                m._moveStartTime = GameTimeController.getInstance().getGameTicks();
                setMoveData(m);
                
                GameTimeController.getInstance().registerMovingObject(this);
                
                broadcastPacket(new VehicleDeparture(this));
                return true;
            }
        }
        
        if (_cycle == 1)
            _cycle = 2;
        else
            _cycle = 1;
        
        say(10);
        ThreadPoolManager.getInstance().scheduleGeneral(new L2BoatCaptain(1, this), 300000);
        return false;
    }
    
    @Override
    public boolean updatePosition()
    {
        final boolean result = super.updatePosition();
        if (!_passengers.isEmpty())
        {
            for (L2PcInstance player : _passengers)
            {
                if (player != null && player.getBoat() == this)
                {
                    player.setXYZ(getX(), getY(), getZ());
                    player.revalidateZone(false);
                }
            }
        }
        return result;
    }
    
    @Override
    public void stopMove(Location loc)
    {
        setMoveData(null);
        if (loc != null)
        {
            setXYZ(loc.getX(), loc.getY(), loc.getZ());
            setHeading(loc.getHeading());
            revalidateZone(true);
        }
        
        broadcastPacket(new VehicleStarted(getObjectId(), 0));
        broadcastPacket(new VehicleInfo(this));
    }
    
    private void addPassenger(L2PcInstance player)
    {
        final int itemId = getCurrentTrajet().getTicketId();
        if (itemId != 0)
        {
            if (!player.destroyItemByItemId("Boat", itemId, 1, this, false))
            {
                oustPlayer(player);
                return;
            }
        }
        _passengers.add(player);
    }
    
    public void oustPlayer(L2PcInstance player)
    {
        L2BoatTrajet trajet = getCurrentTrajet();
        
        final int x = trajet.getOustX();
        final int y = trajet.getOustY();
        final int z = trajet.getOustZ();
        
        removePassenger(player);
        
        if (player.isOnline())
            player.teleToLocation(x, y, z);
        else
            player.setXYZInvisible(x, y, z); // disconnects handling
    }
    
    public void removePassenger(L2PcInstance player)
    {
        if (!_passengers.isEmpty() && _passengers.contains(player))
            _passengers.remove(player);
    }
    
    /**
     * @param i
     */
    public void say(int i)
    {
        final Collection<L2PcInstance> knownPlayers = getKnownList().getKnownPlayers().values();
        if (knownPlayers == null || knownPlayers.isEmpty())
            return;
        
        L2BoatTrajet trajet = getCurrentTrajet();
        CreatureSay sm;
        PlaySound ps;
        
        switch (i)
        {
            case 10:
                sm = new CreatureSay(0, Say2.SHOUT, trajet.getAnnouncerName(), trajet.getSysMessage10());
                ps = new PlaySound(0, "itemsound.ship_arrival_departure", 1, getObjectId(), this.getX(), this.getY(), this.getZ());
                
                for (L2PcInstance player : knownPlayers)
                {
                    player.sendPacket(sm);
                    player.sendPacket(ps);
                }
                break;
            case 5:
                sm = new CreatureSay(0, Say2.SHOUT, trajet.getAnnouncerName(), trajet.getSysMessage5());
                ps = new PlaySound(0, "itemsound.ship_5min", 1, this.getObjectId(), this.getX(), this.getY(), this.getZ());
                
                for (L2PcInstance player : knownPlayers)
                {
                    player.sendPacket(sm);
                    player.sendPacket(ps);
                }
                break;
            case 1:
                sm = new CreatureSay(0, Say2.SHOUT, trajet.getAnnouncerName(), trajet.getSysMessage1());
                ps = new PlaySound(0, "itemsound.ship_1min", 1, this.getObjectId(), this.getX(), this.getY(), this.getZ());
                
                for (L2PcInstance player : knownPlayers)
                {
                    player.sendPacket(sm);
                    player.sendPacket(ps);
                }
                break;
            case 0:
                sm = new CreatureSay(0, Say2.SHOUT, trajet.getAnnouncerName(), trajet.getSysMessage0());
                
                for (L2PcInstance player : knownPlayers)
                    player.sendPacket(sm);
                break;
            case -1:
                sm = new CreatureSay(0, Say2.SHOUT, trajet.getAnnouncerName(), trajet.getSysMessageBegin());
                ps = new PlaySound(0, "itemsound.ship_arrival_departure", 1, this.getObjectId(), this.getX(), this.getY(), this.getZ());
                
                for (L2PcInstance player : knownPlayers)
                {
                    player.sendPacket(sm);
                    player.sendPacket(ps);
                }
                break;
        }
    }
    
    public void beginCycle()
    {
        say(10);
        ThreadPoolManager.getInstance().scheduleGeneral(new L2BoatCaptain(1, this), 300000);
    }
    
    public void setTrajets(L2BoatTrajet[] trajets)
    {
        _trajets = trajets;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.model.L2Character#updateAbnormalEffect()
     */
    @Override
    public void updateAbnormalEffect()
    {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.model.L2Character#getActiveWeaponInstance()
     */
    @Override
    public L2ItemInstance getActiveWeaponInstance()
    {
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.model.L2Character#getActiveWeaponItem()
     */
    @Override
    public L2Weapon getActiveWeaponItem()
    {
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.model.L2Character#getSecondaryWeaponInstance()
     */
    @Override
    public L2ItemInstance getSecondaryWeaponInstance()
    {
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.model.L2Character#getSecondaryWeaponItem()
     */
    @Override
    public L2Item getSecondaryWeaponItem()
    {
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.model.L2Character#getLevel()
     */
    @Override
    public int getLevel()
    {
        return 0;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.model.L2Object#isAutoAttackable(net.sf.l2j.gameserver.model.L2Character)
     */
    @Override
    public boolean isAutoAttackable(L2Character attacker)
    {
        return false;
    }
    
    @Override
    public void sendInfo(L2PcInstance activeChar)
    {
        activeChar.sendPacket(new VehicleInfo(this));
        if (isMoving())
        {
            activeChar.sendPacket(new VehicleDeparture(this));
        }
    }
}