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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.ai.L2BoatAI;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.knownlist.BoatKnownList;
import net.sf.l2j.gameserver.model.actor.stat.BoatStat;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.VehicleDeparture;
import net.sf.l2j.gameserver.network.serverpackets.VehicleInfo;
import net.sf.l2j.gameserver.network.serverpackets.VehicleStarted;
import net.sf.l2j.gameserver.templates.L2CharTemplate;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.util.Util;

/**
 * @author Maktakien
 *
 */
public class L2BoatInstance extends L2Character
{
    private static final Logger _logBoat = Logger.getLogger(L2BoatInstance.class.getName());

    protected final ArrayList<L2PcInstance> _passengers = new ArrayList<>();
    protected ArrayList<L2BoatPoint> _currentPath;

    protected byte _runState  = 0;

    protected L2BoatTrajet _t1;
    protected L2BoatTrajet _t2;

    // default
    protected int _cycle = 1;

    public class AIAccessor extends L2Character.AIAccessor
    {
        @Override
        public void detachAI() {}
    }
    
    public L2BoatInstance(int objectId, L2CharTemplate template)
    {
        super(objectId, template);
        getKnownList();
        getStat();
        setAI(new L2BoatAI(new AIAccessor()));
    }

    @Override
	public final BoatKnownList getKnownList()
    {
        if (super.getKnownList() == null || !(super.getKnownList() instanceof BoatKnownList))
            setKnownList(new BoatKnownList(this));
        return (BoatKnownList)super.getKnownList();
    }

    @Override
	public final BoatStat getStat()
    {
        if (super.getStat() == null || !(super.getStat() instanceof BoatStat))
            setStat(new BoatStat(this));
        return (BoatStat)super.getStat();
    }

    public void begin()
    {
        // firstly, check passengers
        checkPassengers();

        _runState = 0;
        _currentPath = null;

        if (_cycle == 1)
            _currentPath = _t1._path;
        else
            _currentPath = _t2._path;

        if (_currentPath == null)
        {
            getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
            return;
        }

        L2BoatPoint point = _currentPath.get(0);
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
            for (L2PcInstance player : getKnownList().getKnownPlayersInRadius(1000))
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
        _move = null;
        _runState++;

        if (_runState < _currentPath.size())
        {
            final L2BoatPoint point = _currentPath.get(_runState);
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
                _move = m;

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
        ThreadPoolManager.getInstance().scheduleGeneral(new BoatCaptain(1, this), 300000);
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
        _move = null;
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
        final int itemId;
        if (_cycle == 1)
            itemId = _t1._IdWTicket1;
        else
            itemId = _t2._IdWTicket1;

        if (itemId != 0)
        {
            final L2ItemInstance it = player.getInventory().getItemByItemId(itemId);
            if ((it != null) && (it.getCount() >= 1))
            {
                player.getInventory().destroyItem("Boat", it, 1, player, this);
                InventoryUpdate iu = new InventoryUpdate();
                iu.addModifiedItem(it);
                player.sendPacket(iu);
            }
            else
            {
                oustPlayer(player);
                return;
            }
        }
        _passengers.add(player);
    }

    public void oustPlayer(L2PcInstance player)
    {
        final int x, y, z;
        if (_cycle == 1)
        {
            x = _t1._ntx1;
            y = _t1._nty1;
            z = _t1._ntz1;
        }
        else
        {
            x = _t2._ntx1;
            y = _t2._nty1;
            z = _t2._ntz1;
        }

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

        CreatureSay sm;
        PlaySound ps;
        switch(i)
        {     	   
            case 10:
                if (_cycle == 1)

                    sm = new CreatureSay(0, Say2.SHOUT,_t1._npc1, _t1._sysmess10_1);
                else
                    sm = new CreatureSay(0, Say2.SHOUT,_t2._npc1, _t2._sysmess10_1);

                ps = new PlaySound(0,"itemsound.ship_arrival_departure",1,getObjectId(),this.getX(),this.getY(),this.getZ());

                for (L2PcInstance player : knownPlayers)
                {
                    player.sendPacket(sm);
                    player.sendPacket(ps);
                }
                break;
            case 5:
                if (_cycle == 1)
                    sm = new CreatureSay(0, Say2.SHOUT,_t1._npc1, _t1._sysmess5_1);
                else
                    sm = new CreatureSay(0, Say2.SHOUT,_t2._npc1, _t2._sysmess5_1);

                ps = new PlaySound(0,"itemsound.ship_5min",1,this.getObjectId(),this.getX(),this.getY(),this.getZ());

                for (L2PcInstance player : knownPlayers)
                {
                    player.sendPacket(sm);
                    player.sendPacket(ps);
                }
                break;
            case 1:
                if (_cycle == 1)
                    sm = new CreatureSay(0, Say2.SHOUT,_t1._npc1, _t1._sysmess1_1);
                else
                    sm = new CreatureSay(0, Say2.SHOUT,_t2._npc1, _t2._sysmess1_1);

                ps = new PlaySound(0,"itemsound.ship_1min",1,this.getObjectId(),this.getX(),this.getY(),this.getZ());

                for (L2PcInstance player : knownPlayers)
                {
                    player.sendPacket(sm);
                    player.sendPacket(ps);
                }
                break;
            case 0:
                if (_cycle == 1)
                    sm = new CreatureSay(0, Say2.SHOUT,_t1._npc1, _t1._sysmess0_1);
                else
                    sm = new CreatureSay(0, Say2.SHOUT,_t2._npc1, _t2._sysmess0_1);

                for (L2PcInstance player : knownPlayers)
                    player.sendPacket(sm);
                break;
            case -1:
                if (_cycle == 1)
                    sm = new CreatureSay(0, Say2.SHOUT,_t1._npc1, _t1._sysmessb_1);
                else
                    sm = new CreatureSay(0, Say2.SHOUT,_t2._npc1, _t2._sysmessb_1);

                ps = new PlaySound(0,"itemsound.ship_arrival_departure",1,this.getObjectId(),this.getX(),this.getY(),this.getZ());

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
        ThreadPoolManager.getInstance().scheduleGeneral(new BoatCaptain(1, this), 300000);
    }

    private class BoatCaptain implements Runnable
    {
        private int _state;
        private L2BoatInstance _boat;

        /**
         * @param i
         * @param instance 
         */
        public BoatCaptain(int i, L2BoatInstance instance)
        {
            _state = i;
            _boat = instance;
        }

        @Override
		public void run()
        {
            switch(_state)
            {
                case 1:
                    _boat.say(5);
                    ThreadPoolManager.getInstance().scheduleGeneral(new BoatCaptain(2, _boat), 240000);  
                    break;
                case 2:
                    _boat.say(1);
                    ThreadPoolManager.getInstance().scheduleGeneral(new BoatCaptain(3, _boat), 40000);  
                    break;
                case 3:
                    _boat.say(0);
                    ThreadPoolManager.getInstance().scheduleGeneral(new BoatCaptain(4, _boat), 20000);  
                    break;
                case 4:
                    _boat.say(-1);
                    _boat.begin();
                    break;
            }
        }
    }

    /**
     * @param idWaypoint1
     * @param idWTicket1
     * @param ntx1
     * @param nty1
     * @param ntz1
     * @param idnpc1
     * @param sysmess10_1
     * @param sysmess5_1
     * @param sysmess1_1
     * @param sysmess0_1 
     * @param sysmessb_1
     */
    public void SetTrajet1(int idWaypoint1, int idWTicket1, int ntx1, int nty1, int ntz1, String idnpc1, String sysmess10_1, String sysmess5_1, String sysmess1_1, String sysmess0_1, String sysmessb_1)
    {		
        _t1 = new L2BoatTrajet(idWaypoint1,idWTicket1,ntx1,nty1,ntz1,idnpc1,sysmess10_1,sysmess5_1,sysmess1_1,sysmess0_1,sysmessb_1);
    }

    public void SetTrajet2(int idWaypoint1, int idWTicket1, int ntx1, int nty1, int ntz1, String idnpc1, String sysmess10_1, String sysmess5_1, String sysmess1_1, String sysmess0_1, String sysmessb_1)
    {		
        _t2 = new L2BoatTrajet(idWaypoint1,idWTicket1,ntx1,nty1,ntz1,idnpc1,sysmess10_1,sysmess5_1,sysmess1_1,sysmess0_1,sysmessb_1);
    }

    private class L2BoatPoint
    {
        protected int speed1;
        protected int speed2;
        protected int x;
        protected int y;
        protected int z;

        /**
		 * 
		 */
		public L2BoatPoint()
		{
			// TODO Auto-generated constructor stub
		}

		public int getMoveSpeed()
        {
            return speed1;
        }

        public int getRotationSpeed()
        {
            return speed2;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public int getZ()
        {
            return z;
        }
    }

    private class L2BoatTrajet
    {
        protected ArrayList<L2BoatPoint> _path;

        public int _IdWaypoint1;
        public int _IdWTicket1;
        public int _ntx1;
        public int _nty1;
        public int _ntz1;
        public String _npc1;
        public String _sysmess10_1;
        public String _sysmess5_1;
        public String _sysmess1_1;
        public String _sysmessb_1;
        public String _sysmess0_1;


        /**
         * @param idWaypoint1
         * @param idWTicket1
         * @param ntx1
         * @param nty1
         * @param ntz1
         * @param npc1 
         * @param sysmess10_1
         * @param sysmess5_1
         * @param sysmess1_1
         * @param sysmess0_1 
         * @param sysmessb_1
         */
        public L2BoatTrajet(int idWaypoint1, int idWTicket1, int ntx1, int nty1, int ntz1, String npc1, String sysmess10_1, String sysmess5_1, String sysmess1_1,String sysmess0_1, String sysmessb_1)
        {
            _IdWaypoint1 = idWaypoint1;
            _IdWTicket1 = idWTicket1;
            _ntx1 = ntx1;
            _nty1 = nty1;
            _ntz1 = ntz1;
            _npc1 = npc1;
            _sysmess10_1 = sysmess10_1;
            _sysmess5_1 = sysmess5_1;
            _sysmess1_1 = sysmess1_1;
            _sysmessb_1 = sysmessb_1;
            _sysmess0_1 = sysmess0_1;
            loadBoatPath();
        }

        /**
         * @param line
         */
        public void parseLine(String line)
        {
            _path = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(line, ";");
            Integer.parseInt(st.nextToken());

            int max = Integer.parseInt(st.nextToken());
            for (int i = 0; i < max; i++)
            {
                L2BoatPoint bp = new L2BoatPoint();
                bp.speed1 = Integer.parseInt(st.nextToken());
                bp.speed2 = Integer.parseInt(st.nextToken());
                bp.x = Integer.parseInt(st.nextToken());
                bp.y = Integer.parseInt(st.nextToken());
                bp.z = Integer.parseInt(st.nextToken());
                _path.add(bp);
            }
        }

        /**
         * 
         */
        private void loadBoatPath()
        {
            File boatData = new File(Config.DATAPACK_ROOT, "data/boatpath.csv");
            try (FileReader fr = new FileReader(boatData);
                BufferedReader br = new BufferedReader(fr);
                LineNumberReader lnr = new LineNumberReader(br))
            {
                String line = null;
                while ((line = lnr.readLine()) != null) 
                {
                    if (line.trim().length() == 0 || !line.startsWith(_IdWaypoint1+";")) 
                        continue;
                    parseLine(line);	
                    return;
                }
                _logBoat.warning("No path for boat "+getName()+" !!!");
            } 
            catch (FileNotFoundException e) 
            {		
                _logBoat.warning("boatpath.csv is missing in data folder");
            } 
            catch (Exception e) 
            {
                _logBoat.warning("error while creating boat table " + e);
            }
        }
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.model.L2Character#updateAbnormalEffect()
     */
    @Override
    public void updateAbnormalEffect()
    {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.model.L2Character#getActiveWeaponInstance()
     */
    @Override
    public L2ItemInstance getActiveWeaponInstance()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.model.L2Character#getActiveWeaponItem()
     */
    @Override
    public L2Weapon getActiveWeaponItem()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.model.L2Character#getSecondaryWeaponInstance()
     */
    @Override
    public L2ItemInstance getSecondaryWeaponInstance()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.model.L2Character#getSecondaryWeaponItem()
     */
    @Override
    public L2Weapon getSecondaryWeaponItem()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.model.L2Character#getLevel()
     */
    @Override
    public int getLevel()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.model.L2Object#isAutoAttackable(net.sf.l2j.gameserver.model.L2Character)
     */
    @Override
    public boolean isAutoAttackable(L2Character attacker)
    {
        // TODO Auto-generated method stub
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