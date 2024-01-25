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
package net.sf.l2j.gameserver.model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.zone.type.L2ClanHallZone;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class ClanHall
{
	private static final Logger _log = Logger.getLogger(ClanHall.class.getName());

	private static final String DEFAULT_SELLER_CLAN_NAME = "NPC Clan";
	private static final String DEFAULT_SELLER_NAME = "NPC";
	
	// Clan hall functions
	public static final int FUNC_TELEPORT = 1;
	public static final int FUNC_ITEM_CREATE = 2;
	public static final int FUNC_RESTORE_HP = 3;
	public static final int FUNC_RESTORE_MP = 4;
	public static final int FUNC_RESTORE_EXP = 5;
	public static final int FUNC_SUPPORT = 6;
	public static final int FUNC_DECO_FRONTPLATEFORM = 7;
	public static final int FUNC_DECO_CURTAINS = 8;
	
	private static final long PAYMENT_TIME = 604800000;
	
	private final int _clanHallId;
	private final String _name;
	private final int _lease;
	private final int _defaultBid;
	private final String _desc;
	private final String _location;
	private final int _grade;
	
	private final Set<L2DoorInstance> _doors = ConcurrentHashMap.newKeySet();
	private final Map<Integer, ClanHallFunction> _functions = new ConcurrentHashMap<>();
	
	private int _ownerId = 0;
	private long _paidUntil;
	private L2ClanHallZone _zone;
	private boolean _paid;
	private Auction _auction;
	
	public ClanHall(int clanHallId, String name, int ownerId, int lease, int defaultBid, String desc, String location, long paidUntil, int grade, boolean paid)
	{
		_clanHallId = clanHallId;
		_name = name;
		_ownerId = ownerId;
		_lease = lease;
		_defaultBid = defaultBid;
		_desc = desc;
		_location = location;
		_paidUntil = paidUntil;
		_grade = grade;
		_paid = paid;
		
		if (_ownerId == 0)
		{
			return;
		}
		
		L2Clan clan = ClanTable.getInstance().getClan(_ownerId);
		if (clan != null)
		{
			clan.setHasHideout(_clanHallId);
		}
		
		loadFunctions();
		
		if (_defaultBid > 0)
		{
			loadAuction();
		}

		startRentTask(false);
	}
	
	public void openCloseDoor(int doorId, boolean open)
	{
		openCloseDoor(getDoor(doorId), open);
	}
	
	public void openCloseDoor(L2DoorInstance door, boolean open)
	{
		if (door != null)
		{
			if (open)
			{
				door.openMe();
			}
			else
			{
				door.closeMe();
			}
		}
	}
	
	public void openCloseDoor(L2PcInstance activeChar, int doorId, boolean open)
	{
		if ((activeChar != null) && (activeChar.getClanId() == getOwnerId()))
		{
			openCloseDoor(doorId, open);
		}
	}
	
	public void openCloseDoors(boolean open)
	{
		for (L2DoorInstance door : getDoors())
		{
			if (door != null)
			{
				if (open)
				{
					door.openMe();
				}
				else
				{
					door.closeMe();
				}
			}
		}
	}
	
	public void openCloseDoors(L2PcInstance activeChar, boolean open)
	{
		if (activeChar != null && activeChar.getClanId() == getOwnerId())
		{
			openCloseDoors(open);
		}
	}
	
	public void setOwner(L2Clan clan)
	{
		// Remove old owner
		if (getOwnerId() > 0 && (clan == null || clan.getClanId() != getOwnerId()))
		{
			// Try to find clan instance
			L2Clan oldOwner = ClanTable.getInstance().getClan(getOwnerId());
			if (oldOwner != null)
			{
				oldOwner.setHasHideout(0); // Unset hasHideout flag for old owner
			}
		}
		
		updateOwnership(clan); // Update in database
	}
	
	private void updateOwnership(L2Clan clan)
	{
		if (clan != null)
		{
			// Update owner id property
			_ownerId = clan.getClanId();
			
			// Announce to clan members
			clan.setHasHideout(getId()); // Set has hideout flag for new owner
			clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));
			
			_paidUntil = System.currentTimeMillis();
			
			if (_auction != null)
			{
				// Remove existing bids
				_auction.removeBids(clan);
				_auction.deleteMe();
				_auction = null;
			}
			
			// start rent task
			startRentTask(true);
		}
		else
		{
			// Removals
			_paidUntil = 0;
			_ownerId = 0;
			_paid = false;
			
			// Reset functions
			for (Map.Entry<Integer, ClanHallFunction> fc : _functions.entrySet())
			{
				removeFunction(fc.getKey());
			}
			_functions.clear();
			
			openCloseDoors(false);
			
			if (_auction != null)
			{
				// Remove existing bids
				_auction.removeBids(null);
				_auction.deleteMe();

				// Initialize a new auction instance and launch the auction task
				initializeNPCAuction();
				_auction.startAutoTask();
			}
		}
		
		updateDb();
	}
	
	private void loadFunctions()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM clanhall_functions WHERE hall_id = ?"))
		{
			statement.setInt(1, getId());
			try (ResultSet rs = statement.executeQuery())
			{
				while (rs.next())
				{
					_functions.put(rs.getInt("type"), new ClanHallFunction(rs.getInt("type"), rs.getInt("lvl"), rs.getInt("lease"), rs.getLong("rate"), rs.getLong("endTime")));
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Exception: ClanHall.loadFunctions(): " + e.getMessage(), e);
		}
	}
	
	private void loadAuction()
	{
		boolean needsDefaultAuction = false;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM auction WHERE hall_id = ?"))
		{
			statement.setInt(1, getId());
			try (ResultSet rs = statement.executeQuery())
			{
				if (rs.next())
				{
					_auction = new Auction(getId(), rs.getInt("sellerId"), rs.getString("sellerName"), rs.getString("sellerClanName"), rs.getInt("bid"));

					_auction.setEndDate(rs.getLong("endDate"));
					_auction.loadBids();
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Exception: ClanHall.loadAuction(): " + e.getMessage(), e);
		}

		if (_auction == null)
		{
			needsDefaultAuction = true;
		}
		else
		{
			// Clan was forcefully deleted from database
			if (_ownerId > 0 && ClanTable.getInstance().getClan(_ownerId) == null)
			{
				_auction.removeBids(null);
				_auction.deleteMe();
				needsDefaultAuction = true;
			}
		}

		if (needsDefaultAuction)
		{
			initializeNPCAuction();
		}
		_auction.startAutoTask();
	}

	private void initializeNPCAuction()
	{
		_auction = new Auction(getId(), 0, DEFAULT_SELLER_NAME, DEFAULT_SELLER_CLAN_NAME, getDefaultBid());
		_auction.save();
	}
	
	protected void updateDb()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE clanhall SET ownerId=?, paidUntil=?, paid=? WHERE id=?"))
		{
			statement.setInt(1, _ownerId);
			statement.setLong(2, _paidUntil);
			statement.setInt(3, _paid ? 1 : 0);
			statement.setInt(4, _clanHallId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("Exception: updateDb: " + e.getMessage());
		}
	}
	
	public final int getId()
	{
		return _clanHallId;
	}
	
	public final L2DoorInstance getDoor(int doorId)
	{
		if (doorId <= 0)
		{
			return null;
		}
		
		for (L2DoorInstance door : _doors)
		{
			if (door.getDoorId() == doorId)
			{
				return door;
			}
		}
		return null;
	}
	
	public final Set<L2DoorInstance> getDoors()
	{
		return _doors;
	}
	
	public Auction getAuction()
	{
		return _auction;
	}
	
	public void setAuction(Auction auction)
	{
		_auction = auction;
	}
	
	public final String getName()
	{
		return _name;
	}
	
	public final int getOwnerId()
	{
		return _ownerId;
	}
	
	public final int getLease()
	{
		return _lease;
	}
	
	public final int getDefaultBid()
	{
		return _defaultBid;
	}
	
	public final String getDesc()
	{
		return _desc;
	}
	
	public final String getLocation()
	{
		return _location;
	}
	
	public final long getPaidUntil()
	{
		return _paidUntil;
	}
	
	public final int getGrade()
	{
		return _grade;
	}
	
	public final boolean getPaid()
	{
		return _paid;
	}
	
	public void setZone(L2ClanHallZone zone)
	{
		_zone = zone;
	}
	
	public L2ClanHallZone getZone()
	{
		return _zone;
	}
	
	public void banishForeigners()
	{
		_zone.banishForeigners(getOwnerId());
	}
	
	public ClanHallFunction getFunction(int type)
	{
		if (_functions.containsKey(type))
		{
			return _functions.get(type);
		}
		return null;
	}
	
	public void removeFunction(int functionType)
	{
		ClanHallFunction function = _functions.remove(functionType);
		if (function != null && function.getFunctionTask() != null)
		{
			function.getFunctionTask().cancel(false);
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM clanhall_functions WHERE hall_id=? AND type=?"))
		{
			statement.setInt(1, getId());
			statement.setInt(2, functionType);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Exception: ClanHall.removeFunction(int functionType): " + e.getMessage(), e);
		}
	}
	
	public boolean updateFunctions(int type, int lvl, int lease, long rate, long time, boolean addNew)
	{
		if (Config.DEBUG)
		{
			_log.warning("Called ClanHall.updateFunctions(int type, int lvl, int lease, long rate, long time, boolean addNew)");
		}
		
		L2Clan clan = ClanTable.getInstance().getClan(getOwnerId());
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			if (addNew)
			{
				if (clan == null || clan.getWarehouse().getAdena() < lease)
				{
					return false;
				}
				
				clan.getWarehouse().destroyItemByItemId("CH_function_fee", Inventory.ADENA_ID, lease, null, null);
				
				try (PreparedStatement statement = con.prepareStatement("INSERT INTO clanhall_functions (hall_id, type, lvl, lease, rate, endTime) VALUES (?,?,?,?,?,?)"))
				{
					statement.setInt(1, getId());
					statement.setInt(2, type);
					statement.setInt(3, lvl);
					statement.setInt(4, lease);
					statement.setLong(5, rate);
					statement.setLong(6, time);
					statement.execute();
				}
				
				_functions.put(type, new ClanHallFunction(type, lvl, lease, rate, time));
				
				if (Config.DEBUG)
				{
					_log.warning("INSERT INTO clanhall_functions (hall_id, type, lvl, lease, rate, endTime) VALUES (?,?,?,?,?,?)");
				}
			}
			else if (getFunction(type) != null)
			{
				if (lvl == 0 && lease == 0)
				{
					removeFunction(type);
					return true;
				}
				
				ClanHallFunction function = getFunction(type);
				
				if (clan == null || clan.getWarehouse().getAdena() < (lease - function.getLease()))
				{
					return false;
				}
				
				if ((lease - function.getLease()) > 0)
				{
					clan.getWarehouse().destroyItemByItemId("CH_function_fee", Inventory.ADENA_ID, lease - function.getLease(), null, null);
				}
				
				try (PreparedStatement statement = con.prepareStatement("UPDATE clanhall_functions SET lvl=?, lease=? WHERE hall_id=? AND type=?"))
				{
					statement.setInt(1, lvl);
					statement.setInt(2, lease);
					statement.setInt(3, getId());
					statement.setInt(4, type);
					statement.execute();
				}
				function.setLvl(lvl);
				function.setLease(lease);
				
				if (Config.DEBUG)
				{
					_log.warning("UPDATE clanhall_functions WHERE hall_id=? AND id=? SET lvl, lease");
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Exception: ClanHall.updateFunctions(int type, int lvl, int lease, long rate, long time, boolean addNew): " + e.getMessage(), e);
		}
		return true;
	}
	
	public class ClanHallFunction
	{
		private final int _type;
		private int _lvl;
		protected int _fee;
		private final long _rate;
		protected long _endTime;
		protected Future<?> _functionTask;
		
		public ClanHallFunction(int type, int lvl, int lease, long rate, long time)
		{
			_type = type;
			_lvl = lvl;
			_fee = lease;
			_rate = rate;
			_endTime = time;
			_functionTask = ThreadPoolManager.getInstance().scheduleGeneral(new FunctionTask(), 1000);
		}
		
		public int getType()
		{
			return _type;
		}
		
		public int getLvl()
		{
			return _lvl;
		}
		
		public int getLease()
		{
			return _fee;
		}
		
		public long getRate()
		{
			return _rate;
		}
		
		public void setLvl(int lvl)
		{
			_lvl = lvl;
		}
		
		public void setLease(int lease)
		{
			_fee = lease;
		}
		
		public long getEndTime()
		{
			return _endTime;
		}
		
		public Future<?> getFunctionTask()
		{
			return _functionTask;
		}
		
		public void updateFunctionRent()
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("UPDATE clanhall_functions SET endTime=? WHERE type=? AND hall_id=?"))
			{
				statement.setLong(1, _endTime);
				statement.setInt(2, getType());
				statement.setInt(3, getId());
				statement.execute();
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "Exception: ClanHall.ClanHallFunction.updateFunctionRent(int functionType): " + e.getMessage(), e);
			}
		}
		
		private class FunctionTask implements Runnable
		{
			@Override
			public void run()
			{
				try
				{
					L2Clan clan = ClanTable.getInstance().getClan(getOwnerId());
					if (clan != null && clan.getWarehouse().getAdena() >= _fee && clan.getWarehouse().getAdena() >= (_fee * 2)) // if player didn't pay before add extra fee
					{
						if ((_endTime - System.currentTimeMillis()) <= 0)
						{
							clan.getWarehouse().destroyItemByItemId("CH_function_fee", Inventory.ADENA_ID, _fee, null, null);
							
							if (Config.DEBUG)
							{
								_log.warning("deducted " + _fee + " adena from " + getName() + " owner's cwh for functions");
							}
							
							_endTime = System.currentTimeMillis() + getRate();
							
							updateFunctionRent();
						}
						
						_functionTask = ThreadPoolManager.getInstance().scheduleGeneral(new FunctionTask(), _endTime - System.currentTimeMillis());
					}
					else
					{
						removeFunction(getType());
					}
				}
				catch (Throwable t)
				{
				}
			}
		}
	}
	
	private class RentTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				if (_ownerId == 0)
				{
					return;
				}
				
				L2Clan clan = ClanTable.getInstance().getClan(getOwnerId());
				if (clan == null)
				{
					setOwner(null);
					return;
				}
				
				if (clan.getWarehouse().getAdena() >= getLease())
				{
					if (_paidUntil != 0)
					{
						while (_paidUntil <= System.currentTimeMillis())
						{
							_paidUntil += PAYMENT_TIME;
						}
					}
					else
					{
						_paidUntil = System.currentTimeMillis() + PAYMENT_TIME;
					}
					
					clan.getWarehouse().destroyItemByItemId("CH_rental_fee", Inventory.ADENA_ID, getLease(), null, null);
					
					if (Config.DEBUG)
					{
						_log.warning("deducted " + getLease() + " adena from " + getName() + " owner's cwh for ClanHall _paidUntil" + _paidUntil);
					}
					
					ThreadPoolManager.getInstance().scheduleGeneral(new RentTask(), _paidUntil - System.currentTimeMillis());
					_paid = true;
					updateDb();
				}
				else
				{
					_paid = false;
					if (System.currentTimeMillis() > (_paidUntil + PAYMENT_TIME))
					{
						setOwner(null);
						clan.broadcastToOnlineMembers(new SystemMessage(SystemMessage.THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED));
					}
					else
					{
						updateDb();
						clan.broadcastToOnlineMembers(new SystemMessage(SystemMessage.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW));
						
						if ((System.currentTimeMillis() + (1000 * 60 * 60 * 24)) <= (_paidUntil + PAYMENT_TIME))
						{
							ThreadPoolManager.getInstance().scheduleGeneral(new RentTask(), (1000 * 60 * 60 * 24)); // 1 day
						}
						else
						{
							ThreadPoolManager.getInstance().scheduleGeneral(new RentTask(), (_paidUntil + PAYMENT_TIME) - System.currentTimeMillis());
						}
					}
				}
			}
			catch (Throwable t)
			{
			}
		}
	}
	
	private void startRentTask(boolean forced)
	{
		long currentTime = System.currentTimeMillis();
		if (_paidUntil > currentTime)
		{
			ThreadPoolManager.getInstance().scheduleGeneral(new RentTask(), _paidUntil - currentTime);
		}
		else if (!_paid && !forced)
		{
			if ((System.currentTimeMillis() + (1000 * 60 * 60 * 24)) <= (_paidUntil + PAYMENT_TIME))
			{
				ThreadPoolManager.getInstance().scheduleGeneral(new RentTask(), (1000 * 60 * 60 * 24)); // 1 day
			}
			else
			{
				ThreadPoolManager.getInstance().scheduleGeneral(new RentTask(), (_paidUntil + PAYMENT_TIME) - System.currentTimeMillis());
			}
		}
		else
		{
			new RentTask().run();
		}
	}
}