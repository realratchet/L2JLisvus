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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.ai.L2CharacterAI;
import net.sf.l2j.gameserver.ai.L2DoorAI;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.knownlist.DoorKnownList;
import net.sf.l2j.gameserver.model.actor.stat.DoorStat;
import net.sf.l2j.gameserver.model.actor.status.DoorStatus;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import net.sf.l2j.gameserver.network.serverpackets.DoorInfo;
import net.sf.l2j.gameserver.network.serverpackets.DoorStatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import net.sf.l2j.gameserver.templates.L2DoorTemplate;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.util.StringUtil;

/**
 * This class ...
 * 
 * @version $Revision: 1.3.2.2.2.5 $ $Date: 2005/03/27 15:29:32 $
 */
public class L2DoorInstance extends L2Character
{
    /** The castle index in the array of L2Castle this L2NpcInstance belongs to */
    private int _castleIndex = -2;
    private int _mapRegion = -1;

    // when door is closed, the dimensions are
    private int _rangeXMin = 0;
    private int _rangeYMin = 0;
    private int _rangeZMin = 0;
    private int _rangeXMax = 0;
    private int _rangeYMax = 0;
    private int _rangeZMax = 0;
    
    private long _autoOpenDelay = 0;
    private long _autoCloseDelay = 0;

    // these variables assist in see-through calculation only
    private int _a = 0;
    private int _b = 0;
    private int _c = 0;
    private int _d = 0;

    private boolean _isOpen;
    
    private int[] _connectedDoorIds = new int[0];

    private ClanHall _clanhall;
    
    private Future<?> _doorTask;

    /** This class may be created only by L2Character and only for AI */
    public class AIAccessor extends L2Character.AIAccessor
    {
        protected AIAccessor() {}
        @Override
		public L2DoorInstance getActor() { return L2DoorInstance.this; }
        @Override
		@SuppressWarnings("unused")
        public void moveTo(int x, int y, int z, int offset) {}
        @Override
		@SuppressWarnings("unused")
        public void moveTo(int x, int y, int z) {}
        @Override
		@SuppressWarnings("unused")
        public void stopMove(Location loc) {}
        @Override
		@SuppressWarnings("unused")
        public void doAttack(L2Character target) {}
        @Override
		@SuppressWarnings("unused")
        public void doCast(L2Skill skill) {}
    }
    
    @Override
	public L2CharacterAI getAI()
    {
        if (_ai == null)
        {
            synchronized(this)
            {
                if (_ai == null)
                {
                    _ai = new L2DoorAI(new AIAccessor());
                }
            }
        }
        return _ai;
    }
    
    /**
     * Manages the automatic opening and closing of a door. 
     */
    class AutoOpenClose implements Runnable
    {
    	private final boolean _pendingOpen;
    	
    	private AutoOpenClose(boolean pendingOpen)
    	{
    		_pendingOpen = pendingOpen;
    	}
    	
        @Override
		public void run()
        {
        	_doorTask = null;
        	
        	if (_pendingOpen)
        	{
        		if (!isOpen())
        		{
        			openMe();
        		}
        	}
            else
            {
            	 if (isOpen())
            	 {
            		 closeMe();
            	 }
            }
        }
    }

    /**
     * @param objectId 
     * @param template 
     */
    public L2DoorInstance(int objectId, L2DoorTemplate template)
    {
        super(objectId, template);
        getKnownList();
        getStat();
        getStatus();

        // Set the name of the L2Character
        setName(template.name);
    }

    @Override
	public final DoorKnownList getKnownList()
    {
        if (super.getKnownList() == null || !(super.getKnownList() instanceof DoorKnownList))
            setKnownList(new DoorKnownList(this));
        return (DoorKnownList)super.getKnownList();
    }

    @Override
	public final DoorStat getStat()
    {
        if (super.getStat() == null || !(super.getStat() instanceof DoorStat))
            setStat(new DoorStat(this));
        return (DoorStat)super.getStat();
    }

    @Override
	public final DoorStatus getStatus()
    {
        if (super.getStatus() == null || !(super.getStatus() instanceof DoorStatus))
            setStatus(new DoorStatus(this));
        return (DoorStatus)super.getStatus();
    }
    
    /** Return the L2DoorTemplate of the L2DoorInstance. */
	@Override
	public final L2DoorTemplate getTemplate()
	{
		return (L2DoorTemplate) super.getTemplate();
	}

    public final boolean isUnlockable() 
    {
        return getTemplate().isUnlockable;
    }
    
    @Override
	public final int getLevel() 
    {
        return 1;
    }
    
    /**
     * @return Returns the doorId.
     */
    public int getDoorId()
    {
        return getTemplate().doorId;
    }
    
    public boolean isOpenByDefault()
    {
    	return getTemplate().isOpenByDefault;
    }
    
    /**
     * @return Returns true if door is set as open.
     */
    public boolean isOpen()
    {
        return _isOpen;
    }
    
    public void setIsOpen(boolean val)
    {
    	_isOpen = val;
    }
    
    public int getDamage() 
    {
        int dmg = 6 - (int)Math.ceil(getCurrentHp() / getMaxHp() * 6);
        if (dmg > 6)
            return 6;
        if (dmg < 0)
            return 0;
        return dmg;
    }

    public final Castle getCastle()
    {
        if (_castleIndex < 0)
        	_castleIndex = CastleManager.getInstance().getCastleIndex(this);
        if (_castleIndex < 0)
        	return null;
        return CastleManager.getInstance().getCastles().get(_castleIndex);
    }
    
    public void setClanHall(ClanHall clanhall)
    {
    	_clanhall = clanhall;
    }
    
    public ClanHall getClanHall()
    {
    	return _clanhall;
    }

    public boolean isEnemyOf(@SuppressWarnings("unused") L2Character cha) 
    {
        return true;
    }
    
    @Override
	public boolean isAutoAttackable(L2Character attacker)
    {
        if (attacker == null || !(attacker instanceof L2PlayableInstance))
            return false;

        if (getClanHall() != null)
            return false;

        // Attackable during siege by attacker only
        return (getCastle() != null
                && getCastle().getCastleId() > 0
                && getCastle().getSiege().getIsInProgress()
                && getCastle().getSiege().checkIsAttacker(((L2PcInstance)attacker).getClan()));
    }

    public boolean isAttackable(L2Character attacker)
    {
        return isAutoAttackable(attacker);
    }
    
    @Override
	public void updateAbnormalEffect() {}
    
    public int getDistanceToWatchObject(L2Object object)
    {
        if (!(object instanceof L2PcInstance))
            return 0;
        return 2000;
    }

    /**
     * Return the distance after which the object must be remove from _knownObject according to the type of the object.<BR><BR>
     *   
     * <B><U> Values </U> :</B><BR><BR>
     * <li> object is a L2PcInstance : 4000</li>
     * <li> object is not a L2PcInstance : 0 </li><BR><BR>
     * @param object 
     * @return 
     * 
     */
    public int getDistanceToForgetObject(L2Object object) 
    {
        if (!(object instanceof L2PcInstance))
            return 0;
        
        return 4000;
    }

    /**
     * Return null.<BR><BR>
     */ 
    @Override
	public L2ItemInstance getActiveWeaponInstance() 
    {
        return null;
    }
    
    @Override
	public L2Weapon getActiveWeaponItem() 
    {
        return null;
    }

    @Override
	public L2ItemInstance getSecondaryWeaponInstance() 
    {
        return null;
    }

    @Override
	public L2Weapon getSecondaryWeaponItem() 
    {
        return null;
    }

    @Override
	public void onAction(L2PcInstance player) 
    {
        if (player == null)
            return;

        // Check if the L2PcInstance already target the L2NpcInstance
        if (this != player.getTarget())
        {
            // Set the target of the L2PcInstance player
            player.setTarget(this);

            // Send a Server->Client packet MyTargetSelected to the L2PcInstance player
            MyTargetSelected my = new MyTargetSelected(getObjectId(), 0);
            player.sendPacket(my);

            DoorStatusUpdate su = new DoorStatusUpdate(this);
            player.sendPacket(su);

            // Send a Server->Client packet ValidateLocation to correct the L2NpcInstance position and heading on the client
            player.sendPacket(new ValidateLocation(this));
        }
        else
        {
        	final ClanHall ch = getClanHall();
            if (isAutoAttackable(player))
            {
                if (Math.abs(player.getZ() - getZ()) < 400) // this max height difference might need some tweaking                    player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, this);
            }
            else if (player.getClan() != null && ch != null && player.getClanId() == ch.getOwnerId()
            	&& (player.getClanPrivileges() & L2Clan.CP_CH_OPEN_DOOR) == L2Clan.CP_CH_OPEN_DOOR)
            {
                if (!isInsideRadius(player, L2NpcInstance.INTERACTION_DISTANCE, false, false))
                    player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
                else
                {
                    player.gatesRequest(this);
                    if (isOpen())
                        player.sendPacket(new ConfirmDlg(1141));
                    else
                        player.sendPacket(new ConfirmDlg(1140));
                }
            }
        }
        // Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
        player.sendPacket(new ActionFailed());
    }

    @Override
	public void onActionShift(L2GameClient client) 
    {
        L2PcInstance player = client.getActiveChar();
        if (player == null)
        	return;

        if (player.isGM())
        {
            player.setTarget(this);
            MyTargetSelected my = new MyTargetSelected(getObjectId(), player.getLevel());
            player.sendPacket(my);

            if (isAutoAttackable(player))
            {
                DoorStatusUpdate su = new DoorStatusUpdate(this);
                player.sendPacket(su);
            }

            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            String sb = StringUtil.concat(
            	"<html><body><table border=0>",
            	"<tr><td>Door Information</td></tr>",
            	"<tr><td><br></td></tr>",
            	"<tr><td>Current HP  "+getCurrentHp()+ "</td></tr>",
            	"<tr><td>Max HP      "+getMaxHp()+"</td></tr>",
            	"<tr><td>Max X       "+getXMax()+"</td></tr>",
            	"<tr><td>Max Y       "+getYMax()+"</td></tr>",
            	"<tr><td>Max Z       "+getZMax()+"</td></tr>",
            	"<tr><td>Min X       "+getXMin()+"</td></tr>",
            	"<tr><td>Min Y       "+getYMin()+"</td></tr>",
            	"<tr><td>Min Z       "+getZMin()+"</td></tr>",
            	"<tr><td>Object ID:  " + getObjectId() + "</td></tr>",
            	"<tr><td>Door ID:<br>"+getDoorId()+"</td></tr>",
            	"<tr><td><br></td></tr>",
            	"<tr><td>Class: " + getClass().getName() + "</td></tr>",
            	"<tr><td><br></td></tr>",
            	"</table>",
            	"<table><tr>",
            	"<td><button value=\"Open\" action=\"bypass -h admin_open "+getDoorId()+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
            	"<td><button value=\"Close\" action=\"bypass -h admin_close "+getDoorId()+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
            	"<td><button value=\"Kill\" action=\"bypass -h admin_kill\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
            	"<td><button value=\"Delete\" action=\"bypass -h admin_delete\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
            	"</tr></table></body></html>");

            html.setHtml(sb);
            player.sendPacket(html);
        }

        player.sendPacket(new ActionFailed());
    }

    @Override
	public void broadcastStatusUpdate()
    {
        Collection<L2PcInstance> knownPlayers = getKnownList().getKnownPlayers().values(); 
        if (knownPlayers == null || knownPlayers.isEmpty())
            return;

        DoorStatusUpdate su = new DoorStatusUpdate(this);
        for (L2PcInstance player : knownPlayers)
            player.sendPacket(su);
    }
    
    public final void startAutoOpenCloseTask()
    {
    	final boolean openRequest = !isOpen();
    	final long delay = openRequest ? getAutoOpenDelay() : getAutoCloseDelay();
    	if (delay <= 0)
    	{
    		return;
    	}
    	
        if (_doorTask != null)
        {
        	_doorTask.cancel(false);
        	_doorTask = null;
        }
        
        _doorTask = ThreadPoolManager.getInstance().scheduleGeneral(new AutoOpenClose(openRequest), delay);
    }

    public final void closeMe()
    {
    	if (!_isOpen)
    	{
    		return;
    	}
    	
        _isOpen = false;
        broadcastStatusUpdate();
        
        // Execute a task to close the door if needed
        startAutoOpenCloseTask();
        
        // Handle connected doors
        for (int id : _connectedDoorIds)
		{
			L2DoorInstance door = DoorTable.getInstance().getDoor(id);
			if (door != null && door != this)
			{
				door.setIsOpen(false);
				door.broadcastStatusUpdate();
        		
        		// Execute a task to close the door if needed
                door.startAutoOpenCloseTask();
			}
		}
    }

    public final void openMe()
    {
    	if (_isOpen)
    	{
    		return;
    	}
    	
        _isOpen = true;
        broadcastStatusUpdate();
        
        // Execute a task to close the door if needed
        startAutoOpenCloseTask();
        
        // Handle connected doors
        for (int id : _connectedDoorIds)
        {
        	L2DoorInstance door = DoorTable.getInstance().getDoor(id);
        	if (door != null && door != this)
        	{
        		door.setIsOpen(true);
        		door.broadcastStatusUpdate();
        		
        		// Execute a task to close the door if needed
        		door.startAutoOpenCloseTask();
        	}
        }
    }

    @Override
	public String toString()
    {
        return "door " + getDoorId();
    }

    public int getXMin()
    {
        return _rangeXMin;
    }

    public int getYMin()
    {
        return _rangeYMin;
    }

    public int getZMin()
    {
        return _rangeZMin;
    }

    public int getXMax()
    {
        return _rangeXMax;
    }

    public int getYMax()
    {
        return _rangeYMax;
    }

    public int getZMax()
    {
        return _rangeZMax;
    }

    public int getA()
    {
        return _a;
    }

    public int getB()
    {
        return _b;
    }

    public int getC()
    {
        return _c;
    }

    public int getD()
    {
        return _d;
    }
    
    public void setRange(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax)
    {
        _rangeXMin = xMin;
        _rangeYMin = yMin;
        _rangeZMin = zMin;

        _rangeXMax = xMax;
        _rangeYMax = yMax;
        _rangeZMax = zMax;

        _a = _rangeYMax *(_rangeZMax -_rangeZMin)+_rangeYMin *(_rangeZMin -_rangeZMax);
        _b = _rangeZMin *(_rangeXMax -_rangeXMin)+_rangeZMax *(_rangeXMin -_rangeXMax);
        _c = _rangeXMin *(_rangeYMax -_rangeYMin)+_rangeXMin *(_rangeYMin -_rangeYMax);
        _d = -1*(_rangeXMin *(_rangeYMax *_rangeZMax -_rangeYMin *_rangeZMax)+_rangeXMax *(_rangeYMin *_rangeZMin -_rangeYMin *_rangeZMax)+_rangeXMin *(_rangeYMin *_rangeZMax -_rangeYMax *_rangeZMin));
    }

    public int getMapRegion()
    {
        return _mapRegion;
    }

    public void setMapRegion(int region)
    {
        _mapRegion = region;
    }
    
    public long getAutoOpenDelay()
    {
    	return _autoOpenDelay;
    }
    
    public void setAutoOpenDelay(long val)
    {
    	_autoOpenDelay = val;
    }
    
    public long getAutoCloseDelay()
    {
    	return _autoCloseDelay;
    }
    
    public void setAutoCloseDelay(long val)
    {
    	_autoCloseDelay = val;
    }

    public int[] getConnectedDoorIds()
    {
    	return _connectedDoorIds;
    }
    
    public void setConnectedDoorIds(int[] val)
    {
    	_connectedDoorIds = val;
    }
    
    public Collection<L2SiegeGuardInstance> getKnownSiegeGuards()
    {
        List<L2SiegeGuardInstance> result = new ArrayList<>();
        for (L2Object obj : getKnownList().getKnownObjects().values())  
        {  
            if (obj instanceof L2SiegeGuardInstance) result.add((L2SiegeGuardInstance) obj);
        }
        
        return result;
    }
    
    @Override
    public void sendInfo(L2PcInstance activeChar)
    {
    	activeChar.sendPacket(new DoorInfo(this));
		activeChar.sendPacket(new DoorStatusUpdate(this));
    }
}