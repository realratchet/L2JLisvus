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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager.CropProcure;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager.SeedProduction;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Manor;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2ArtefactInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.zone.type.L2CastleTeleportZone;
import net.sf.l2j.gameserver.model.zone.type.L2SiegeZone;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Castle
{
	private static final Logger _log = Logger.getLogger(Castle.class.getName());
	
	// =========================================================
	// Data Field
	private List<CropProcure> _procure = new ArrayList<>();
	private List<SeedProduction> _production = new ArrayList<>();
	private List<CropProcure> _procureNext = new ArrayList<>();
	private List<SeedProduction> _productionNext = new ArrayList<>();
	private boolean _isNextPeriodApproved = false;
	
	private static final String CASTLE_MANOR_DELETE_PRODUCTION = "DELETE FROM castle_manor_production WHERE castle_id=?";
	private static final String CASTLE_MANOR_DELETE_PRODUCTION_PERIOD = "DELETE FROM castle_manor_production WHERE castle_id=? AND period=?";
	private static final String CASTLE_MANOR_DELETE_PROCURE = "DELETE FROM castle_manor_procure WHERE castle_id=?";
	private static final String CASTLE_MANOR_DELETE_PROCURE_PERIOD = "DELETE FROM castle_manor_procure WHERE castle_id=? AND period=?";
	private static final String CASTLE_UPDATE_CROP = "UPDATE castle_manor_procure SET can_buy=? WHERE crop_id=? AND castle_id=? AND period=?";
	private static final String CASTLE_UPDATE_SEED = "UPDATE castle_manor_production SET can_produce=? WHERE seed_id=? AND castle_id=? AND period=?";
	
	// =========================================================
	// Data Field
	private int _castleId = 0;
	private String _name = "";
	private int _ownerId = 0;
	private Siege _siege;
	private Calendar _siegeDate;
	private int _siegeDayOfWeek = 7; // Default to Saturday
    private int _siegeHourOfDay = 20; // Default to 8 pm server time
	private int _taxPercent = 0;
	private double _taxRate = 0;
	private int _treasury = 0;
	
	private L2SiegeZone _zone;
	private L2CastleTeleportZone _teleZone;
	
	private final Set<L2DoorInstance> _doors = ConcurrentHashMap.newKeySet();
	private final List<L2ArtefactInstance> _artefacts = new ArrayList<>(1);
	private final Map<Integer, Integer> _engraves = new HashMap<>();
	private final Map<Integer, CastleFunction> _functions = new ConcurrentHashMap<>();
	
	/** Castle Functions */
	public static final int FUNC_TELEPORT = 1;
	public static final int FUNC_RESTORE_HP = 2;
	public static final int FUNC_RESTORE_MP = 3;
	public static final int FUNC_RESTORE_EXP = 4;
	public static final int FUNC_SUPPORT = 5;
	
	public class CastleFunction
	{
		private final int _type;
		private int _lvl;
		protected int _fee;
		protected int _tempFee;
		private final long _rate;
		private long _endDate;
		protected boolean _inDebt;
		public boolean _cwh;
		Future<?> _functionTask;
		
		public CastleFunction(int type, int lvl, int lease, int tempLease, long rate, long time, boolean cwh)
		{
			_type = type;
			_lvl = lvl;
			_fee = lease;
			_tempFee = tempLease;
			_rate = rate;
			_endDate = time;
			initializeTask(cwh);
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
		
		public long getEndTime()
		{
			return _endDate;
		}
		
		public void setLvl(int lvl)
		{
			_lvl = lvl;
		}
		
		public void setLease(int lease)
		{
			_fee = lease;
		}
		
		public void setEndTime(long time)
		{
			_endDate = time;
		}
		
		public Future<?> getFunctionTask()
		{
			return _functionTask;
		}
		
		private void initializeTask(boolean cwh)
		{
			if (getOwnerId() <= 0)
			{
				return;
			}
			
			long currentTime = System.currentTimeMillis();
			if (_endDate > currentTime)
			{
				_functionTask = ThreadPoolManager.getInstance().scheduleGeneral(new FunctionTask(cwh), _endDate - currentTime);
			}
			else
			{
				_functionTask = ThreadPoolManager.getInstance().scheduleGeneral(new FunctionTask(cwh), 0);
			}
		}
		
		private class FunctionTask implements Runnable
		{
			public FunctionTask(boolean cwh)
			{
				_cwh = cwh;
			}
			
			@Override
			public void run()
			{
				try
				{
					if (getOwnerId() <= 0)
					{
						return;
					}
					
					if ((ClanTable.getInstance().getClan(getOwnerId()).getWarehouse().getAdena() >= _fee) || !_cwh)
					{
						int fee = _fee;
						boolean newfc = true;
						if ((getEndTime() == 0) || (getEndTime() == -1))
						{
							if (getEndTime() == -1)
							{
								newfc = false;
								fee = _tempFee;
							}
						}
						else
						{
							newfc = false;
						}
						
						setEndTime(System.currentTimeMillis() + getRate());
						dbSave(newfc);
						
						if (_cwh)
						{
							ClanTable.getInstance().getClan(getOwnerId()).getWarehouse().destroyItemByItemId("CS_function_fee", Inventory.ADENA_ID, fee, null, null);
							if (Config.DEBUG)
							{
								_log.warning("deducted " + fee + " adena from " + getName() + " owner's cwh for function id : " + getType());
							}
						}
						_functionTask = ThreadPoolManager.getInstance().scheduleGeneral(new FunctionTask(true), getRate());
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
		
		public void dbSave(boolean newFunction)
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection())
			{
				if (newFunction)
				{
					try (PreparedStatement statement = con.prepareStatement("INSERT INTO castle_functions (castle_id, type, lvl, lease, rate, endTime) VALUES (?,?,?,?,?,?)"))
					{
						statement.setInt(1, getCastleId());
						statement.setInt(2, getType());
						statement.setInt(3, getLvl());
						statement.setInt(4, getLease());
						statement.setLong(5, getRate());
						statement.setLong(6, getEndTime());
						statement.execute();
					}
				}
				else
				{
					try (PreparedStatement statement = con.prepareStatement("UPDATE castle_functions SET lvl=?, lease=?, endTime=? WHERE castle_id=? AND type=?"))
					{
						statement.setInt(1, getLvl());
						statement.setInt(2, getLease());
						statement.setLong(3, getEndTime());
						statement.setInt(4, getCastleId());
						statement.setInt(5, getType());
						statement.execute();
					}
				}
			}
			catch (Exception e)
			{
				_log.severe("Exception: Castle.updateFunctions(int type, int lvl, int lease, long rate, long time, boolean addNew): " + e.getMessage());
			}
		}
	}
	
	// =========================================================
	// Constructor
	public Castle(int castleId)
	{
		_castleId = castleId;
		
		load();
		
		if (getOwnerId() != 0)
		{
			loadFunctions();
		}
	}
	
	// =========================================================
	// Method - Public
	
	/**
	 * Return function with id
	 * @param type
	 * @return
	 */
	public CastleFunction getFunction(int type)
	{
		if (_functions.containsKey(type))
		{
			return _functions.get(type);
		}
		return null;
	}
	
	public void engrave(L2Clan clan, L2Object target)
	{
		if (!_artefacts.contains(target))
		{
			return;
		}
		
		_engraves.put(target.getObjectId(), clan.getClanId());
		
		SystemMessage sm = new SystemMessage(SystemMessage.CLAN_S1_ENGRAVED_RULER);
		sm.addString(clan.getName());
		getSiege().announceToPlayer(sm, true);
		
		if (_engraves.size() == _artefacts.size())
		{
			// Check if this clan is the one who engraved all artifacts
			for (int id : _engraves.values())
			{
				if (clan.getClanId() != id)
				{
					return;
				}
			}
			
			_engraves.clear();
			setOwner(clan);
		}
	}
	
	// This method add to the treasury
	/**
	 * Add amount to castle instance's treasury (warehouse).
	 * @param amount
	 */
	public void addToTreasury(int amount)
	{
		if (getOwnerId() <= 0)
		{
			return;
		}
		
		if (!_name.equalsIgnoreCase("aden")) // If current castle instance is not Aden
		{
			Castle aden = CastleManager.getInstance().getCastle("aden");
			if (aden != null)
			{
				int adenTax = (int) (amount * aden.getTaxRate()); // Find out what Aden gets from the current castle instance's income
				if (aden.getOwnerId() > 0)
				{
					aden.addToTreasury(adenTax); // Only bother to really add the tax to the treasury if not npc owned
				}
				
				amount -= adenTax; // Subtract Aden's income from current castle instance's income
			}
		}
		
		addToTreasuryNoTax(amount);
	}
	
	/**
	 * Add amount to castle instance's treasury (warehouse), no tax paying.
	 * @param amount
	 * @return
	 */
	public boolean addToTreasuryNoTax(int amount)
	{
		if (getOwnerId() <= 0)
		{
			return false;
		}
		
		if (amount < 0)
		{
			amount *= -1;
			if (_treasury < amount)
			{
				return false;
			}
			_treasury -= amount;
		}
		else
		{
			if (((long)_treasury + amount) > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID))
			{
				_treasury = L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID);
			}
			else
			{
				_treasury += amount;
			}
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE castle SET treasury = ? WHERE id = ?"))
		{
			statement.setInt(1, getTreasury());
			statement.setInt(2, getCastleId());
			statement.execute();
		}
		catch (Exception e)
		{
		}
		return true;
	}
	
	/**
	 * Move non clan members off castle area and to nearest town.<BR>
	 * <BR>
	 */
	public void banishForeigners()
	{
		_zone.banishForeigners(getOwnerId());
	}
	
	public void closeDoor(L2PcInstance activeChar, int doorId)
	{
		openCloseDoor(activeChar, doorId, false);
	}
	
	public void openDoor(L2PcInstance activeChar, int doorId)
	{
		openCloseDoor(activeChar, doorId, true);
	}
	
	public void openCloseDoor(L2PcInstance activeChar, int doorId, boolean open)
	{
		if (activeChar.getClanId() != getOwnerId())
		{
			return;
		}
		
		L2DoorInstance door = getDoor(doorId);
		
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
	
	/**
	 * This method is used to begin removing all castle upgrades.
	 */
	public void removeUpgrade()
	{
		removeDoorUpgrade();
		for (Map.Entry<Integer, CastleFunction> fc : _functions.entrySet())
		{
			removeFunction(fc.getKey());
		}
		_functions.clear();
	}
	
	/**
	 * This method updates the castle tax rate.
	 * 
	 * @param clan
	 */
	public void setOwner(L2Clan clan)
	{
		// Remove old owner
		if ((getOwnerId() > 0) && ((clan == null) || (clan.getClanId() != getOwnerId())))
		{
			L2Clan oldOwner = ClanTable.getInstance().getClan(getOwnerId()); // Try to find clan instance
			if (oldOwner != null)
			{
				CastleManager.getInstance().removeCirclet(oldOwner, getCastleId());
				oldOwner.setHasCastle(0); // Unset has castle flag for old owner
				
				Announcements.getInstance().announceToAll(oldOwner.getName() + " has lost " + getName() + " castle!");
			}
			
			for (Map.Entry<Integer, CastleFunction> fc : _functions.entrySet())
			{
				removeFunction(fc.getKey());
			}
			_functions.clear();
		}
		
		updateOwnerInDB(clan); // Update in database
		
		if (getSiege().getIsInProgress())
		{
			getSiege().midVictory(); // Mid victory phase of siege
		}
	}
	
	/**
	 * This method updates the castle tax rate.
	 * 
	 * @param activeChar
	 * @param taxPercent
	 */
	public void setTaxPercent(L2PcInstance activeChar, int taxPercent)
	{
		int maxTax;
		switch (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE))
		{
			case SevenSigns.CABAL_DAWN:
				maxTax = 25;
				break;
			case SevenSigns.CABAL_DUSK:
				maxTax = 5;
				break;
			default: // no owner
				maxTax = 15;
		}
		
		if ((taxPercent < 0) || (taxPercent > maxTax))
		{
			activeChar.sendMessage("Tax value must be between 0 and " + maxTax + ".");
			return;
		}
		
		setTaxPercent(taxPercent);
		activeChar.sendMessage(getName() + " castle tax was changed to " + taxPercent + "%.");
	}
	
	public void setTaxPercent(int taxPercent)
	{
		_taxPercent = taxPercent;
		_taxRate = _taxPercent / 100.0;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE castle SET taxPercent = ? WHERE id = ?"))
		{
			statement.setInt(1, taxPercent);
			statement.setInt(2, getCastleId());
			statement.execute();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * Respawn all doors on castle grounds<BR>
	 * <BR>
	 */
	public void spawnDoors()
	{
		spawnDoors(false);
	}
	
	/**
	 * Respawn all doors on castle grounds<BR>
	 * <BR>
	 * @param isDoorWeak
	 */
	public void spawnDoors(boolean isDoorWeak)
	{
		final Siege siege = getSiege();
		final boolean isSiegeInProgress = (siege != null && siege.getIsInProgress());
		
		for (L2DoorInstance door : _doors)
		{
			if (door.isDead())
			{
				door.doRevive();
				door.setCurrentHp((isDoorWeak) ? (door.getMaxHp() / 2) : (door.getMaxHp()));
			}
			
			if (door.isOpen())
			{
				door.closeMe();
			}
			
			// At the end of the siege, open doors that remain open by default
			if (door.isOpenByDefault())
			{
				if (!isSiegeInProgress)
				{
					door.openMe();
				}
			}
		}
	}
	
	// This method upgrade door
	public void upgradeDoor(int doorId, int hp, int pDef, int mDef)
	{
		L2DoorInstance door = getDoor(doorId);
		if (door == null)
		{
			return;
		}
		
		if (door.getDoorId() == doorId)
		{
			door.setCurrentHp(door.getMaxHp() + hp);
			
			saveDoorUpgrade(doorId, hp, pDef, mDef);
			return;
		}
	}
	
	// =========================================================
	// Method - Private
	// This method loads castle
	private void load()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement("SELECT * FROM castle WHERE id = ?"))
			{
				statement.setInt(1, getCastleId());
				try (ResultSet rs = statement.executeQuery())
				{
					while (rs.next())
					{
						_name = rs.getString("name");
						
						_siegeDate = Calendar.getInstance();
						_siegeDate.setTimeInMillis(rs.getLong("siegeDate"));
						
						_siegeDayOfWeek = rs.getInt("siegeDayOfWeek");
						if ((_siegeDayOfWeek < 1) || (_siegeDayOfWeek > 7))
						{
							_siegeDayOfWeek = 7;
						}
						
						_siegeHourOfDay = rs.getInt("siegeHourOfDay");
						if ((_siegeHourOfDay < 0) || (_siegeHourOfDay > 23))
						{
							_siegeHourOfDay = 20;
						}
						
						_taxPercent = rs.getInt("taxPercent");
						_treasury = rs.getInt("treasury");
						
					}
				}
			}
			
			_taxRate = _taxPercent / 100.0;
			
			try (PreparedStatement statement = con.prepareStatement("SELECT clan_id FROM clan_data WHERE hasCastle = ?"))
			{
				statement.setInt(1, getCastleId());
				try (ResultSet rs = statement.executeQuery())
				{
					while (rs.next())
					{
						_ownerId = rs.getInt("clan_id");
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Exception: loadCastleData(): " + e.getMessage());
		}
	}
	
	/** Load All Functions */
	private void loadFunctions()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM castle_functions WHERE castle_id = ?"))
		{
			statement.setInt(1, getCastleId());
			try (ResultSet rs = statement.executeQuery())
			{
				while (rs.next())
				{
					_functions.put(rs.getInt("type"), new CastleFunction(rs.getInt("type"), rs.getInt("lvl"), rs.getInt("lease"), 0, rs.getLong("rate"), rs.getLong("endTime"), true));
				}
			}
		}
		catch (Exception e)
		{
			_log.severe("Exception: loadFunctions(): " + e.getMessage());
		}
	}
	
	/**
	 * Remove function In List and in DB
	 * @param functionType
	 */
	public void removeFunction(int functionType)
	{
		CastleFunction function = _functions.remove(functionType);
		if ((function != null) && (function.getFunctionTask() != null))
		{
			function.getFunctionTask().cancel(false);
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM castle_functions WHERE castle_id=? AND type=?"))
		{
			statement.setInt(1, getCastleId());
			statement.setInt(2, functionType);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.severe("Exception: Castle.removeFunctions(int functionType): " + e.getMessage());
		}
	}
	
	public boolean updateFunctions(L2PcInstance player, int type, int lvl, int lease, long rate, boolean addNew)
	{
		if (Config.DEBUG)
		{
			_log.warning("Called Castle.updateFunctions(int type, int lvl, int lease, long rate, boolean addNew) Owner : " + getOwnerId());
		}
		
		if (lease > 0)
		{
			if (!player.destroyItemByItemId("Consume", Inventory.ADENA_ID, lease, null, true))
			{
				return false;
			}
		}
		
		if (addNew)
		{
			_functions.put(type, new CastleFunction(type, lvl, lease, 0, rate, 0, false));
		}
		else
		{
			if ((lvl == 0) && (lease == 0))
			{
				removeFunction(type);
			}
			else
			{
				CastleFunction func = _functions.get(type);
				int diffLease = lease - func.getLease();
				
				if (Config.DEBUG)
				{
					_log.warning("Called Castle.updateFunctions diffLease : " + diffLease);
				}
				
				if (diffLease > 0)
				{
					_functions.put(type, new CastleFunction(type, lvl, lease, 0, rate, -1, false));
				}
				else
				{
					func.setLease(lease);
					func.setLvl(lvl);
					func.dbSave(false);
				}
			}
		}
		return true;
	}
	
	public void loadDoorUpgrade()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM castle_doorupgrade WHERE castleId = ?"))
		{
			statement.setInt(1, _castleId);
			try (ResultSet rs = statement.executeQuery())
			{
				while (rs.next())
				{
					upgradeDoor(rs.getInt("id"), rs.getInt("hp"), rs.getInt("pDef"), rs.getInt("mDef"));
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Exception: loadDoorUpgrade(): " + e.getMessage());
		}
	}
	
	public void removeDoorUpgrade()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM castle_doorupgrade WHERE castleId = ?"))
		{
			statement.setInt(1, _castleId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("Exception: removeDoorUpgrade(): " + e.getMessage());
		}
	}
	
	private void saveDoorUpgrade(int doorId, int hp, int pDef, int mDef)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO castle_doorupgrade (doorId, castleId, hp, pDef, mDef) values (?,?,?,?,?)"))
		{
			statement.setInt(1, doorId);
			statement.setInt(2, _castleId);
			statement.setInt(3, hp);
			statement.setInt(4, pDef);
			statement.setInt(5, mDef);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("Exception: saveDoorUpgrade(int doorId, int hp, int pDef, int mDef): " + e.getMessage());
		}
	}
	
	private void updateOwnerInDB(L2Clan clan)
	{
		if (clan != null)
		{
			_ownerId = clan.getClanId(); // Update owner id property
		}
		else
		{
			_ownerId = 0; // Remove owner
			resetManor();
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			// NEED TO REMOVE HAS CASTLE FLAG FROM CLAN_DATA
			// SHOULD BE CHECKED FROM CASTLE TABLE
			try (PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET hasCastle=? WHERE hasCastle=?"))
			{
				statement.setInt(1, 0);
				statement.setInt(2, getCastleId());
				statement.execute();
			}
			
			try (PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET hasCastle=? WHERE clan_id=?"))
			{
				statement.setInt(1, getCastleId());
				statement.setInt(2, getOwnerId());
				statement.execute();
			}
			
			// Announce to clan members
			if (clan != null)
			{
				clan.setHasCastle(getCastleId()); // Set has castle flag for new owner
				Announcements.getInstance().announceToAll(clan.getName() + " has taken " + getName() + " castle!");
				
				for (L2ClanMember member : clan.getMembers())
				{
					if (member.isOnline() && (member.getPlayerInstance() != null))
					{
						member.getPlayerInstance().sendPacket(new PledgeShowInfoUpdate(clan));
					}
				}
				
				clan.broadcastToOnlineMembers(new PlaySound(1, "Siege_Victory", 0, 0, 0, 0, 0));
			}
		}
		catch (Exception e)
		{
			_log.warning("Exception: updateOwnerInDB(L2Clan clan): " + e.getMessage());
		}
	}
	
	// =========================================================
	// Property
	public final int getCastleId()
	{
		return _castleId;
	}
	
	/**
	 * Registers artefact to castle.
	 * 
	 * @param artefact
	 */
	public void registerArtefact(L2ArtefactInstance artefact)
	{
		if (Config.DEBUG)
		{
			_log.info("Artefact '" + artefact.getObjectId() + "' has been registered to '" + getName() + "' castle.");
		}
		_artefacts.add(artefact);
	}
	
	public List<L2ArtefactInstance> getArtefacts()
	{
		return _artefacts;
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
	
	public final String getName()
	{
		return _name;
	}
	
	public final int getOwnerId()
	{
		return _ownerId;
	}
	
	public final Siege getSiege()
	{
		if (_siege == null)
		{
			_siege = new Siege(this);
		}
		return _siege;
	}
	
	public final Calendar getSiegeDate()
	{
		return _siegeDate;
	}
	
	public final int getSiegeDayOfWeek()
	{
		return _siegeDayOfWeek;
	}
	
	public final int getSiegeHourOfDay()
	{
		return _siegeHourOfDay;
	}
	
	public final int getTaxPercent()
	{
		return _taxPercent;
	}
	
	public final double getTaxRate()
	{
		return _taxRate;
	}
	
	public final int getTreasury()
	{
		return _treasury;
	}
	
	public boolean checkIfInZone(int x, int y, int z)
	{
		return _zone.isInsideZone(x, y, z);
	}
	
	public void setZone(L2SiegeZone zone)
	{
		_zone = zone;
	}
	
	public L2SiegeZone getZone()
	{
		return _zone;
	}
	
	public void setTeleZone(L2CastleTeleportZone zone)
	{
		_teleZone = zone;
	}
	
	public L2CastleTeleportZone getTeleZone()
	{
		return _teleZone;
	}
	
	public void oustAllPlayers()
	{
		getTeleZone().oustAllPlayers();
	}
	
	public double getDistance(L2Object obj)
	{
		return _zone.getDistanceToZone(obj);
	}
	
	public List<SeedProduction> getSeedProduction(int period)
	{
		return (period == CastleManorManager.PERIOD_CURRENT ? _production : _productionNext);
	}
	
	public List<CropProcure> getCropProcure(int period)
	{
		return (period == CastleManorManager.PERIOD_CURRENT ? _procure : _procureNext);
	}
	
	public void setSeedProduction(List<SeedProduction> seed, int period)
	{
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			_production = seed;
		}
		else
		{
			_productionNext = seed;
		}
	}
	
	public void setCropProcure(List<CropProcure> crop, int period)
	{
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			_procure = crop;
		}
		else
		{
			_procureNext = crop;
		}
	}
	
	public synchronized SeedProduction getSeed(int seedId, int period)
	{
		for (SeedProduction seed : getSeedProduction(period))
		{
			if (seed.getId() == seedId)
			{
				return seed;
			}
		}
		
		return null;
	}
	
	public synchronized CropProcure getCrop(int cropId, int period)
	{
		for (CropProcure crop : getCropProcure(period))
		{
			if (crop.getId() == cropId)
			{
				return crop;
			}
		}
		return null;
	}
	
	public int getManorCost(int period)
	{
		List<CropProcure> procure;
		List<SeedProduction> production;
		
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			procure = _procure;
			production = _production;
		}
		else
		{
			procure = _procureNext;
			production = _productionNext;
		}
		
		int total = 0;
		if (production != null)
		{
			for (SeedProduction seed : production)
			{
				total += L2Manor.getInstance().getSeedBuyPrice(seed.getId()) * seed.getStartProduce();
			}
		}
		
		if (procure != null)
		{
			for (CropProcure crop : procure)
			{
				total += crop.getPrice() * crop.getStartAmount();
			}
		}
		return total;
	}
	
	// Save manor production data
	public void saveSeedData()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement(CASTLE_MANOR_DELETE_PRODUCTION))
			{
				statement.setInt(1, getCastleId());
				statement.execute();
			}
			
			if (_production != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[_production.size()];
				for (SeedProduction s : _production)
				{
					values[count] = "(" + getCastleId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + CastleManorManager.PERIOD_CURRENT + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					try (PreparedStatement statement = con.prepareStatement(query))
					{
						statement.execute();
					}
				}
			}
			
			if (_productionNext != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[_productionNext.size()];
				for (SeedProduction s : _productionNext)
				{
					values[count] = "(" + getCastleId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + CastleManorManager.PERIOD_NEXT + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					try (PreparedStatement statement = con.prepareStatement(query))
					{
						statement.execute();
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.info("Error adding seed production data for castle " + getName() + ": " + e.getMessage());
		}
	}
	
	// Save manor production data for specified period
	public void saveSeedData(int period)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement(CASTLE_MANOR_DELETE_PRODUCTION_PERIOD))
			{
				statement.setInt(1, getCastleId());
				statement.setInt(2, period);
				statement.execute();
			}
			
			List<SeedProduction> prod = null;
			prod = getSeedProduction(period);
			
			if (prod != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[prod.size()];
				for (SeedProduction s : prod)
				{
					values[count] = "(" + getCastleId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + period + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					try (PreparedStatement statement = con.prepareStatement(query))
					{
						statement.execute();
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.info("Error adding seed production data for castle " + getName() + ": " + e.getMessage());
		}
	}
	
	// Save crop procure data
	public void saveCropData()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement(CASTLE_MANOR_DELETE_PROCURE))
			{
				statement.setInt(1, getCastleId());
				statement.execute();
			}
			
			if ((_procure != null) && (_procure.size() > 0))
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[_procure.size()];
				for (CropProcure cp : _procure)
				{
					values[count] = "(" + getCastleId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + CastleManorManager.PERIOD_CURRENT + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					try (PreparedStatement statement = con.prepareStatement(query))
					{
						statement.execute();
					}
				}
			}
			
			if ((_procureNext != null) && (_procureNext.size() > 0))
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[_procureNext.size()];
				for (CropProcure cp : _procureNext)
				{
					values[count] = "(" + getCastleId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + CastleManorManager.PERIOD_NEXT + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					try (PreparedStatement statement = con.prepareStatement(query))
					{
						statement.execute();
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.info("Error adding crop data for castle " + getName() + ": " + e.getMessage());
		}
	}
	
	// Save crop procure data for specified period
	public void saveCropData(int period)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement(CASTLE_MANOR_DELETE_PROCURE_PERIOD))
			{
				statement.setInt(1, getCastleId());
				statement.setInt(2, period);
				statement.execute();
			}
			
			List<CropProcure> proc = null;
			proc = getCropProcure(period);
			
			if ((proc != null) && (proc.size() > 0))
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[proc.size()];
				
				for (CropProcure cp : proc)
				{
					values[count] = "(" + getCastleId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + period + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					try (PreparedStatement statement = con.prepareStatement(query))
					{
						statement.execute();
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.info("Error adding crop data for castle " + getName() + ": " + e.getMessage());
		}
	}
	
	public void updateCrop(int cropId, int amount, int period)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(CASTLE_UPDATE_CROP))
		{
			statement.setInt(1, amount);
			statement.setInt(2, cropId);
			statement.setInt(3, getCastleId());
			statement.setInt(4, period);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("Error adding crop data for castle " + getName() + ": " + e.getMessage());
		}
	}
	
	public void updateSeed(int seedId, int amount, int period)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(CASTLE_UPDATE_SEED))
		{
			statement.setInt(1, amount);
			statement.setInt(2, seedId);
			statement.setInt(3, getCastleId());
			statement.setInt(4, period);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("Error adding seed production data for castle " + getName() + ": " + e.getMessage());
		}
	}
	
	public boolean isNextPeriodApproved()
	{
		return _isNextPeriodApproved;
	}
	
	public void setNextPeriodApproved(boolean val)
	{
		_isNextPeriodApproved = val;
	}
	
	public void resetManor()
	{
		setCropProcure(new ArrayList<CropProcure>(), CastleManorManager.PERIOD_CURRENT);
		setCropProcure(new ArrayList<CropProcure>(), CastleManorManager.PERIOD_NEXT);
		setSeedProduction(new ArrayList<SeedProduction>(), CastleManorManager.PERIOD_CURRENT);
		setSeedProduction(new ArrayList<SeedProduction>(), CastleManorManager.PERIOD_NEXT);
		if (Config.ALT_MANOR_SAVE_ALL_ACTIONS)
		{
			saveCropData();
			saveSeedData();
		}
	}
}