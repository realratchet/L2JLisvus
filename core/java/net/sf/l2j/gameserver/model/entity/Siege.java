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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable.TeleportWhereType;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.MercTicketManager;
import net.sf.l2j.gameserver.instancemanager.SiegeGuardManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager.SiegeSpawn;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2SiegeClan;
import net.sf.l2j.gameserver.model.L2SiegeClan.SiegeClanType;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2ControlTowerInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.RelationChanged;
import net.sf.l2j.gameserver.network.serverpackets.SiegeInfo;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class Siege
{
	private static final Logger _log = Logger.getLogger(Siege.class.getName());
	
	// ==========================================================================================
	// Message to add/check
	// id=17 msg=[Castle siege has begun.] c3_attr1=[SystemMsg_k.17]
	// id=18 msg=[Castle siege is over.] c3_attr1=[SystemMsg_k.18]
	// id=288 msg=[The castle gate has been broken down.]
	// id=291 msg=[Clan $s1 is victorious over $s2's castle siege!]
	// id=292 msg=[$s1 has announced the castle siege time.]
	// - id=293 msg=[The registration term for $s1 has ended.]
	// - id=358 msg=[$s1 hour(s) until castle siege conclusion.]
	// - id=359 msg=[$s1 minute(s) until castle siege conclusion.]
	// - id=360 msg=[Castle siege $s1 second(s) left!]
	// id=640 msg=[You have failed to refuse castle defense aid.]
	// id=641 msg=[You have failed to approve castle defense aid.]
	// id=644 msg=[You are not yet registered for the castle siege.]
	// - id=645 msg=[Only clans with Level 4 and higher may register for a castle siege.]
	// id=646 msg=[You do not have the authority to modify the castle defender list.]
	// - id=688 msg=[The clan that owns the castle is automatically registered on the defending side.]
	// id=689 msg=[A clan that owns a castle cannot participate in another siege.]
	// id=690 msg=[You cannot register on the attacking side because you are part of an alliance with the clan that owns the castle.]
	// id=718 msg=[The castle gates cannot be opened and closed during a siege.]
	// - id=295 msg=[$s1's siege was canceled because there were no clans that participated.]
	// id=659 msg=[This is not the time for siege registration and so registrations cannot be accepted or rejected.]
	// - id=660 msg=[This is not the time for siege registration and so registration and cancellation cannot be done.]
	// id=663 msg=[The siege time has been declared for $s. It is not possible to change the time after a siege time has been declared. Do you want to continue?]
	// id=667 msg=[You are registering on the attacking side of the $s1 siege. Do you want to continue?]
	// id=668 msg=[You are registering on the defending side of the $s1 siege. Do you want to continue?]
	// id=669 msg=[You are canceling your application to participate in the $s1 siege battle. Do you want to continue?]
	// id=707 msg=[You cannot teleport to a village that is in a siege.]
	// - id=711 msg=[The siege of $s1 has started.]
	// - id=712 msg=[The siege of $s1 has finished.]
	// id=844 msg=[The siege to conquer $s1 has begun.]
	// - id=845 msg=[The deadline to register for the siege of $s1 has passed.]
	// - id=846 msg=[The siege of $s1 has been canceled due to lack of interest.]
	// - id=856 msg=[The siege of $s1 has ended in a draw.]
	// id=285 msg=[Clan $s1 has succeeded in engraving the ruler!]
	// - id=287 msg=[The opponent clan has begun to engrave the ruler.]
	
	public static enum TeleportWhoType
	{
		All,
		Attacker,
		DefenderNotOwner,
		Owner,
		Spectator
	}
	
	private int _controlTowerCount;

	// ===============================================================
	// Schedule task
	public class ScheduleEndSiegeTask implements Runnable
	{
		private final Castle castle;
		
		public ScheduleEndSiegeTask(Castle pCastle)
		{
			castle = pCastle;
		}
		
		@Override
		public void run()
		{
			if (!getIsInProgress())
			{
				return;
			}
			
			try
			{
				long timeRemaining = _siegeEndDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
				if (timeRemaining > 3600000)
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_HOURS_UNTIL_SIEGE_CONCLUSION);
					sm.addNumber(SiegeManager.getInstance().getSiegeLength());
					announceToPlayer(sm, true);
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleEndSiegeTask(castle), timeRemaining - 3600000); // Prepare task for 1 hr left.
				}
				else if ((timeRemaining <= 3600000) && (timeRemaining > 600000))
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_MINUTES_UNTIL_SIEGE_CONCLUSION);
					sm.addNumber(Math.round(timeRemaining / 60000));
					announceToPlayer(sm, true);
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleEndSiegeTask(castle), timeRemaining - 600000); // Prepare task for 10 minute left.
				}
				else if ((timeRemaining <= 600000) && (timeRemaining > 300000))
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_MINUTES_UNTIL_SIEGE_CONCLUSION);
					sm.addNumber(Math.round(timeRemaining / 60000));
					announceToPlayer(sm, true);
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleEndSiegeTask(castle), timeRemaining - 300000); // Prepare task for 5 minute left.
				}
				else if ((timeRemaining <= 300000) && (timeRemaining > 10000))
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_MINUTES_UNTIL_SIEGE_CONCLUSION);
					sm.addNumber(Math.round(timeRemaining / 60000));
					announceToPlayer(sm, true);
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleEndSiegeTask(castle), timeRemaining - 10000); // Prepare task for 10 seconds count down
				}
				else if ((timeRemaining <= 10000) && (timeRemaining > 0))
				{
					SystemMessage sm = new SystemMessage(SystemMessage.CASTLE_SIEGE_S1_SECONDS_LEFT);
					sm.addNumber(Math.round(timeRemaining / 1000));
					announceToPlayer(sm, true);
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleEndSiegeTask(castle), timeRemaining); // Prepare task for second count down
				}
				else
				{
					castle.getSiege().endSiege();
				}
			}
			catch (Throwable t)
			{	
			}
		}
	}
	
	public class ScheduleStartSiegeTask implements Runnable
	{
		private final Castle castle;
		
		public ScheduleStartSiegeTask(Castle pCastle)
		{
			castle = pCastle;
		}
		
		@Override
		public void run()
		{
			if (getIsInProgress())
			{
				return;
			}
			
			try
			{
				long timeRemaining = getSiegeDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
				if (timeRemaining > 86400000)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStartSiegeTask(castle), timeRemaining - 86400000); // Prepare task for 24 before siege start to end registration
				}
				else if ((timeRemaining <= 86400000) && (timeRemaining > 13600000))
				{
					SystemMessage sm = new SystemMessage(SystemMessage.REGISTRATION_TERM_FOR_S1_ENDED);
					sm.addString(getCastle().getName());
					Announcements.getInstance().announceToAll(sm);
					_isRegistrationOver = true;
					clearSiegeWaitingClans();
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStartSiegeTask(castle), timeRemaining - 13600000); // Prepare task for 1 hr left before siege start.
				}
				else if ((timeRemaining <= 13600000) && (timeRemaining > 600000))
				{
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStartSiegeTask(castle), timeRemaining - 600000); // Prepare task for 10 minute left.
				}
				else if ((timeRemaining <= 600000) && (timeRemaining > 300000))
				{
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStartSiegeTask(castle), timeRemaining - 300000); // Prepare task for 5 minute left.
				}
				else if ((timeRemaining <= 300000) && (timeRemaining > 10000))
				{
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStartSiegeTask(castle), timeRemaining - 10000); // Prepare task for 10 seconds count down
				}
				else if ((timeRemaining <= 10000) && (timeRemaining > 0))
				{
					ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStartSiegeTask(castle), timeRemaining); // Prepare task for second count down
				}
				else
				{
					castle.getSiege().startSiege();
				}
			}
			catch (Throwable t)
			{
			}
		}
	}
	
	// =========================================================
	// Data Field
	
	// Attacker and Defender
	private final List<L2SiegeClan> _attackerClans = new CopyOnWriteArrayList<>();
	private final List<L2SiegeClan> _defenderClans = new CopyOnWriteArrayList<>();
	private final List<L2SiegeClan> _defenderWaitingClans = new CopyOnWriteArrayList<>();
	
	// Castle setting
	private final List<L2ControlTowerInstance> _controlTowers = new ArrayList<>();
	
	private final Castle _castle;
	private final SiegeGuardManager _siegeGuardManager;
	private final Calendar _siegeEndDate;
	private final Calendar _siegeRegistrationEndDate;
	
	private boolean _isInProgress = false;
	private boolean _isRegistrationOver = false;
	
	// =========================================================
	// Constructor
	public Siege(Castle castle)
	{
		_castle = castle;
		_siegeGuardManager = new SiegeGuardManager(getCastle());
		
		// Set siege end date
		_siegeEndDate = Calendar.getInstance();
		_siegeEndDate.setTimeInMillis(getCastle().getSiegeDate().getTimeInMillis());
		_siegeEndDate.add(Calendar.HOUR_OF_DAY, SiegeManager.getInstance().getSiegeLength());
		
		// Set registration end date
		_siegeRegistrationEndDate = Calendar.getInstance();
		_siegeRegistrationEndDate.setTimeInMillis(getCastle().getSiegeDate().getTimeInMillis());
		_siegeRegistrationEndDate.add(Calendar.DAY_OF_MONTH, -1);
		
		startAutoTask();
	}
	
	/**
	 * Starts the auto tasks.
	 * <BR><BR>
	 */
	private void startAutoTask()
	{
		correctSiegeDateTime();
		loadSiegeClans();
		
		// Schedule siege auto start
		ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStartSiegeTask(getCastle()), 1000);
		
		_log.info("Siege of " + getCastle().getName() + ": " + getCastle().getSiegeDate().getTime());
	}
	
	/**
	 * When siege starts.<BR>
	 * <BR>
	 */
	public void startSiege()
	{
		if (!getIsInProgress())
		{
			if (getAttackerClans().size() <= 0)
			{
				SystemMessage sm;
				if (getCastle().getOwnerId() <= 0)
				{
					sm = new SystemMessage(SystemMessage.SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST);
				}
				else
				{
					sm = new SystemMessage(SystemMessage.S1_SIEGE_WAS_CANCELED_BECAUSE_NO_CLANS_PARTICIPATED);
				}
				sm.addString(getCastle().getName());
				Announcements.getInstance().announceToAll(sm);
				return;
			}
			
			_isInProgress = true; // Flag so that same siege instance cannot be started again
			
			loadSiegeClans(); // Load siege clan from db
			updatePlayerSiegeStateFlags(false);
			teleportPlayer(Siege.TeleportWhoType.Attacker, MapRegionTable.TeleportWhereType.Town); // Teleport to the closest town
			_controlTowerCount = 0;
			spawnControlTower(getCastle().getCastleId()); // Spawn control tower
			getCastle().spawnDoors(); // Spawn door
			spawnSiegeGuard(); // Spawn siege guard
			MercTicketManager.getInstance().deleteTickets(getCastle().getCastleId()); // remove the tickets from the ground
			getCastle().getZone().updateZoneStatusForCharactersInside();
			
			// Schedule a task to prepare auto siege end
			ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleEndSiegeTask(getCastle()), 1000);
			
			SystemMessage sm = new SystemMessage(SystemMessage.SIEGE_OF_S1_HAS_STARTED);
			sm.addString(getCastle().getName());
			Announcements.getInstance().announceToAll(sm);
		}
	}
	
	/**
	 * When control of castle changed during siege<BR>
	 * <BR>
	 */
	public void midVictory()
	{
		if (getIsInProgress()) // Siege still in progress
		{
			if (getCastle().getOwnerId() > 0)
			{
				_siegeGuardManager.removeMercs(); // Remove all mercenary entries from db
			}
			
			if ((getDefenderClans().isEmpty()) && (getAttackerClans().size() == 1)) // If defender doesn't exist (Pc vs Npc)
			{
				L2SiegeClan newOwner = getAttackerClan(getCastle().getOwnerId());
				removeAttacker(newOwner);
				addDefender(newOwner, SiegeClanType.OWNER);
				endSiege();
				return;
			}
			
			if (getCastle().getOwnerId() > 0)
			{
				int allyId = ClanTable.getInstance().getClan(getCastle().getOwnerId()).getAllyId();
				if (getDefenderClans().isEmpty()) // If defender doesn't exist (Pc vs Npc) and only an alliance attacks
				{
					// The player's clan is in an alliance
					if (allyId != 0)
					{
						boolean allInSameAlly = true;
						for (L2SiegeClan sc : getAttackerClans())
						{
							if (sc != null)
							{
								if (ClanTable.getInstance().getClan(sc.getClanId()).getAllyId() != allyId)
								{
									allInSameAlly = false;
									break;
								}
							}
						}
						
						if (allInSameAlly)
						{
							L2SiegeClan newOwner = getAttackerClan(getCastle().getOwnerId());
							removeAttacker(newOwner);
							addDefender(newOwner, SiegeClanType.OWNER);
							endSiege();
							return;
						}
					}
				}
				
				for (L2SiegeClan sc : getDefenderClans())
				{
					if (sc != null)
					{
						final boolean isOwner = sc.getType() == SiegeClanType.OWNER;
						removeDefender(sc);
						addAttacker(sc);
						// Insert or update siege clan type into database
						saveSiegeClan(sc.getClanId(), SiegeClanType.ATTACKER, !isOwner);
					}
				}
				
				L2SiegeClan newOwner = getAttackerClan(getCastle().getOwnerId());
				removeAttacker(newOwner);
				addDefender(newOwner, SiegeClanType.OWNER);
				// Owners are not kept into database as siege clans
				removeSiegeClan(getCastle().getOwnerId(), false);
				
				// The player's clan is in an alliance
				if (allyId != 0)
				{
					L2Clan[] clanList = ClanTable.getInstance().getClans();
					for (L2Clan clan : clanList)
					{
						if (clan.getAllyId() == allyId)
						{
							L2SiegeClan sc = getAttackerClan(clan.getClanId());
							if (sc != null)
							{
								removeAttacker(sc);
								addDefender(sc, SiegeClanType.DEFENDER);
								// Update siege clan type into database
								saveSiegeClan(sc.getClanId(), SiegeClanType.DEFENDER, true);
							}
						}
					}
				}
				teleportPlayer(Siege.TeleportWhoType.Attacker, MapRegionTable.TeleportWhereType.SiegeFlag); // Teleport to siege flag or second closest town
				teleportPlayer(Siege.TeleportWhoType.Spectator, MapRegionTable.TeleportWhereType.Town); // Teleport to the second closest town
				
				removeDefenderFlags(); // Removes defenders' flags
				getCastle().removeDoorUpgrade(); // Remove all castle door upgrades
				getCastle().spawnDoors(true); // Respawn door to castle but make them weaker (50% hp)
				removeControlTowers(); // Remove all control tower from this castle
				_controlTowerCount = 0;// Each new siege midvictory CT are completely respawned.
				spawnControlTower(getCastle().getCastleId());
				updatePlayerSiegeStateFlags(false);
			}
		}
	}
	
	/**
	 * When siege ends.<BR>
	 * <BR>
	 */
	public void endSiege()
	{
		if (getIsInProgress())
		{
			SystemMessage sm = new SystemMessage(SystemMessage.SIEGE_OF_S1_HAS_FINISHED);
			sm.addString(getCastle().getName());
			Announcements.getInstance().announceToAll(sm);
			
			if (getCastle().getOwnerId() > 0)
			{
				L2Clan clan = ClanTable.getInstance().getClan(getCastle().getOwnerId());
				sm = new SystemMessage(SystemMessage.CLAN_S1_VICTORIOUS_OVER_S2_SIEGE);
				sm.addString(clan.getName());
				sm.addString(getCastle().getName());
				Announcements.getInstance().announceToAll(sm);
			}
			else
			{
				sm = new SystemMessage(SystemMessage.SIEGE_S1_DRAW);
				sm.addString(getCastle().getName());
				Announcements.getInstance().announceToAll(sm);
			}
			
			removeFlags(); // Removes all flags. Note: Remove flag before teleporting players
			teleportPlayer(Siege.TeleportWhoType.Attacker, MapRegionTable.TeleportWhereType.Town); // Teleport to the second closest town
			teleportPlayer(Siege.TeleportWhoType.DefenderNotOwner, MapRegionTable.TeleportWhereType.Town); // Teleport to the second closest town
			teleportPlayer(Siege.TeleportWhoType.Spectator, MapRegionTable.TeleportWhereType.Town); // Teleport to the second closest town
			_isInProgress = false; // Flag so that siege instance can be started
			updatePlayerSiegeStateFlags(true);
			saveCastleSiege(); // Save castle specific data
			removeControlTowers(); // Remove all control tower from this castle
			_siegeGuardManager.unspawnSiegeGuard(); // Remove all spawned siege guard from this castle
			if (getCastle().getOwnerId() > 0)
			{
				_siegeGuardManager.removeMercs();
			}
			getCastle().spawnDoors(); // Respawn door to castle
			getCastle().getZone().updateZoneStatusForCharactersInside();
			
		}
	}
	
	/**
	 * Teleports players depending on their status.
	 * 
	 * @param teleportWho 
	 * @param teleportWhere 
	 */
	private void teleportPlayer(TeleportWhoType teleportWho, TeleportWhereType teleportWhere)
	{
		List<L2PcInstance> players;
		switch (teleportWho)
		{
			case Owner:
				players = getOwnersInZone();
				break;
			case Attacker:
				players = getAttackersInZone();
				break;
			case DefenderNotOwner:
				players = getDefendersButNotOwnersInZone();
				break;
			case Spectator:
				players = getSpectatorsInZone();
				break;
			default:
				players = getPlayersInZone();
		}
		
		for (L2PcInstance player : players)
		{
			if (player.isGM() || player.isInJail())
			{
				continue;
			}
			player.teleToLocation(teleportWhere);
		}
	}
	
	// =========================================================
	// Method - Public
	/**
	 * Announce to player.<BR>
	 * <BR>
	 * @param message The SystemMessage to send to player
	 * @param bothSides True - broadcast to both attackers and defenders. False - only to defenders.
	 */
	public void announceToPlayer(SystemMessage message, boolean bothSides)
	{
		for (L2SiegeClan sc : getDefenderClans())
		{
			L2Clan clan = ClanTable.getInstance().getClan(sc.getClanId());
			for (L2PcInstance member : clan.getOnlineMembers(0))
			{
				if (member != null)
				{
					member.sendPacket(message);
				}
			}
		}
		
		if (bothSides)
		{
			for (L2SiegeClan sc : getAttackerClans())
			{
				L2Clan clan = ClanTable.getInstance().getClan(sc.getClanId());
				for (L2PcInstance member : clan.getOnlineMembers(0))
				{
					if (member != null)
					{
						member.sendPacket(message);
					}
				}
			}
		}
	}
	
	public void updatePlayerSiegeStateFlags(boolean clear)
	{
		L2Clan clan;
		for (L2SiegeClan siegeclan : getAttackerClans())
		{
			clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
			for (L2PcInstance member : clan.getOnlineMembers(0))
			{
				if (clear)
				{
					member.setSiegeState((byte) 0);
					member.setSiegeSide(0);
				}
				else
				{
					member.setSiegeState((byte) 1);
					member.setSiegeSide(getCastle().getCastleId());
				}
				member.sendPacket(new UserInfo(member));
				for (L2PcInstance player : member.getKnownList().getKnownPlayers().values())
				{
					player.sendPacket(new RelationChanged(member, member.getRelation(player), member.isAutoAttackable(player)));
					if (member.getPet() != null)
					{
						player.sendPacket(new RelationChanged(member.getPet(), member.getRelation(player), member.isAutoAttackable(player)));
					}
					
				}
			}
		}
		
		for (L2SiegeClan siegeclan : getDefenderClans())
		{
			clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
			for (L2PcInstance member : clan.getOnlineMembers(0))
			{
				if (clear)
				{
					member.setSiegeState((byte) 0);
					member.setSiegeSide(0);
				}
				else
				{
					member.setSiegeState((byte) 2);
					member.setSiegeSide(getCastle().getCastleId());
				}
				member.sendPacket(new UserInfo(member));
				for (L2PcInstance player : member.getKnownList().getKnownPlayers().values())
				{
					player.sendPacket(new RelationChanged(member, member.getRelation(player), member.isAutoAttackable(player)));
					if (member.getPet() != null)
					{
						player.sendPacket(new RelationChanged(member.getPet(), member.getRelation(player), member.isAutoAttackable(player)));
					}
				}
			}
		}
	}
	
	/**
	 * Approve clan as defender for siege<BR>
	 * <BR>
	 * @param clanId The int of player's clan id
	 */
	public void approveSiegeDefenderClan(int clanId)
	{
		if (clanId <= 0)
		{
			return;
		}
		
		// Get clan from waiting clans
		L2SiegeClan toRemove = null;
		for (L2SiegeClan sc : getDefenderWaitingClans())
		{
			if (sc.getClanId() == clanId)
			{
				toRemove = sc;
				break;
			}
		}
		
		// Remove clan from defender waiting clans
		if (toRemove != null)
		{
			getDefenderWaitingClans().remove(toRemove);
		}
		
		// Add clan to defenders
		saveSiegeClan(ClanTable.getInstance().getClan(clanId), SiegeClanType.DEFENDER, true);
	}
	
	/** 
	 * Return true if object is inside the zone 
	 * @param object 
	 * @return 
	 */
	public boolean checkIfInZone(L2Object object)
	{
		return checkIfInZone(object.getX(), object.getY(), object.getZ());
	}
	
	/** 
	 * Return true if object is inside the zone 
	 * @param x 
	 * @param y 
	 * @param z 
	 * @return 
	 */
	public boolean checkIfInZone(int x, int y, int z)
	{
		return (getIsInProgress() && (getCastle().checkIfInZone(x, y, z))); // Castle zone during siege
	}
	
	/**
	 * Return true if clan is attacker<BR>
	 * <BR>
	 * @param clan The L2Clan of the player
	 * @return 
	 */
	public boolean checkIsAttacker(L2Clan clan)
	{
		return (getAttackerClan(clan) != null);
	}
	
	/**
	 * Return true if clan is defender<BR>
	 * <BR>
	 * @param clan The L2Clan of the player
	 * @return 
	 */
	public boolean checkIsDefender(L2Clan clan)
	{
		return (getDefenderClan(clan) != null);
	}
	
	/**
	 * Return true if clan is defender waiting approval<BR>
	 * <BR>
	 * @param clan The L2Clan of the player
	 * @return 
	 */
	public boolean checkIsDefenderWaiting(L2Clan clan)
	{
		return (getDefenderWaitingClan(clan) != null);
	}
	
	/**
	 * Cleans all registered siege clans from database for castle.
	 */
	public void clearSiegeClans()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement("DELETE FROM siege_clans WHERE castle_id=?"))
			{
				statement.setInt(1, getCastle().getCastleId());
				statement.execute();
			}
			
			if (getCastle().getOwnerId() > 0)
			{
				try (PreparedStatement statement2 = con.prepareStatement("DELETE FROM siege_clans WHERE clan_id=?"))
				{
					statement2.setInt(1, getCastle().getOwnerId());
					statement2.execute();
				}
			}
			
			getAttackerClans().clear();
			getDefenderClans().clear();
			getDefenderWaitingClans().clear();
		}
		catch (Exception e)
		{
			_log.warning("Exception: clearSiegeClans(): " + e.getMessage());
		}
	}
	
	/**
	 * Cleans all siege clans waiting for approval from database for castle.
	 */
	public void clearSiegeWaitingClans()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM siege_clans WHERE castle_id=? and type = 2"))
		{
			statement.setInt(1, getCastle().getCastleId());
			statement.execute();
			
			getDefenderWaitingClans().clear();
		}
		catch (Exception e)
		{
			_log.warning("Exception: clearSiegeWaitingClans(): " + e.getMessage());
		}
	}
	
	/** 
	 * Return list of L2PcInstance registered as attacker in the zone. 
	 * @return 
	 */
	public List<L2PcInstance> getAttackersInZone()
	{
		List<L2PcInstance> players = new ArrayList<>();
		L2Clan clan;
		for (L2SiegeClan siegeclan : getAttackerClans())
		{
			clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
			for (L2PcInstance player : clan.getOnlineMembers(0))
			{
				if (checkIfInZone(player.getX(), player.getY(), player.getZ()))
				{
					players.add(player);
				}
			}
		}
		return players;
	}
	
	/** 
	 * Return list of L2PcInstance registered as defender but not owner in the zone. 
	 * @return 
	 */
	public List<L2PcInstance> getDefendersButNotOwnersInZone()
	{
		List<L2PcInstance> players = new ArrayList<>();
		L2Clan clan;
		for (L2SiegeClan siegeclan : getDefenderClans())
		{
			clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
			if (clan.getClanId() == getCastle().getOwnerId())
			{
				continue;
			}
			
			for (L2PcInstance player : clan.getOnlineMembers(0))
			{
				if (checkIfInZone(player.getX(), player.getY(), player.getZ()))
				{
					players.add(player);
				}
			}
		}
		return players;
	}
	
	/** 
	 * Return list of L2PcInstance in the zone. 
	 * @return 
	 */
	public List<L2PcInstance> getPlayersInZone()
	{
		return getCastle().getZone().getAllPlayers();
	}
	
	/** 
	 * Return list of L2PcInstance owning the castle in the zone. 
	 * @return 
	 */
	public List<L2PcInstance> getOwnersInZone()
	{
		List<L2PcInstance> players = new ArrayList<>();
		
		L2Clan clan;
		for (L2SiegeClan siegeclan : getDefenderClans())
		{
			clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
			if (clan.getClanId() != getCastle().getOwnerId())
			{
				continue;
			}
			for (L2PcInstance player : clan.getOnlineMembers(0))
			{
				if (checkIfInZone(player.getX(), player.getY(), player.getZ()))
				{
					players.add(player);
				}
			}
		}
		return players;
	}
	
	/** 
	 * Return list of L2PcInstance not registered as attacker or defender in the zone. 
	 * @return 
	 */
	public List<L2PcInstance> getSpectatorsInZone()
	{
		List<L2PcInstance> players = new ArrayList<>();
		
		for (L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
			if (!player.isInsideZone(L2Character.ZONE_SIEGE) || player.getSiegeState() != 0)
			{
				continue;
			}
			
			if (checkIfInZone(player.getX(), player.getY(), player.getZ()))
			{
				players.add(player);
			}
		}
		
		return players;
	}
	
	/** 
	 * Reduces control tower count.
	 */
	public void reduceControlTowerCount()
	{
		_controlTowerCount--;
		if (_controlTowerCount < 0)
		{
			_controlTowerCount = 0;
		}
	}
	
	/** 
	 * Display list of registered clans 
	 * @param player 
	 */
	public void listRegisterClan(L2PcInstance player)
	{
		player.sendPacket(new SiegeInfo(player, getCastle()));
	}
	
	/**
	 * Register clan as attacker<BR>
	 * <BR>
	 * @param player The L2PcInstance of the player trying to register
	 */
	public void registerAttacker(L2PcInstance player)
	{
		registerAttacker(player, false);
	}
	
	public void registerAttacker(L2PcInstance player, boolean force)
	{
		if (player.getClan() == null)
		{
			return;
		}
		int allyId = 0;
		if (getCastle().getOwnerId() != 0)
		{
			allyId = ClanTable.getInstance().getClan(getCastle().getOwnerId()).getAllyId();
		}
		if (allyId != 0)
		{
			if ((player.getClan().getAllyId() == allyId) && !force)
			{
				player.sendMessage("You cannot register as an attacker as your alliance owns the castle.");
				return;
			}
		}
		if (force || checkIfCanRegister(player))
		{
			saveSiegeClan(player.getClan(), SiegeClanType.ATTACKER, false); // Save to database
		}
	}
	
	/**
	 * Register clan as defender<BR>
	 * <BR>
	 * @param player The L2PcInstance of the player trying to register
	 */
	public void registerDefender(L2PcInstance player)
	{
		registerDefender(player, false);
	}
	
	public void registerDefender(L2PcInstance player, boolean force)
	{
		if (getCastle().getOwnerId() <= 0)
		{
			player.sendMessage("You cannot register as a defender because " + getCastle().getName() + " is owned by NPC.");
		}
		else if (force || checkIfCanRegister(player))
		{
			saveSiegeClan(player.getClan(), SiegeClanType.DEFENDER_PENDING, false); // Save to database
		}
	}
	
	/**
	 * Saves registration to database.<BR>
	 * <BR>
	 * @param clanId 
	 * @param type
	 * @param isUpdateRegistration 
	 * @return 
	 */
	private boolean saveSiegeClan(int clanId, SiegeClanType type, boolean isUpdateRegistration)
	{
		boolean completed = false;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			if (!isUpdateRegistration)
			{
				try (PreparedStatement statement = con.prepareStatement("INSERT INTO siege_clans (clan_id,castle_id,type) VALUES (?,?,?)"))
				{
					statement.setInt(1, clanId);
					statement.setInt(2, getCastle().getCastleId());
					statement.setInt(3, type.getId());
					statement.execute();
					completed = true;
				}
			}
			else
			{
				try (PreparedStatement statement = con.prepareStatement("UPDATE siege_clans SET type = ? WHERE castle_id = ? and clan_id = ?"))
				{
					statement.setInt(1, type.getId());
					statement.setInt(2, getCastle().getCastleId());
					statement.setInt(3, clanId);
					statement.execute();
					completed = true;
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Exception: saveSiegeClan(int clanId, SiegeClanType type, boolean isUpdateRegistration): " + e.getMessage());
		}
		return completed;
	}
	
	/**
	 * Saves registration to database.<BR>
	 * <BR>
	 * @param clan The L2Clan of player
	 * @param type
	 * @param isUpdateRegistration 
	 */
	private void saveSiegeClan(L2Clan clan, SiegeClanType type, boolean isUpdateRegistration)
	{
		if (clan.getHasCastle() > 0)
		{
			return;
		}
		
		if (type == SiegeClanType.OWNER || type == SiegeClanType.DEFENDER || type == SiegeClanType.DEFENDER_PENDING)
		{
			if ((getDefenderClans().size() + getDefenderWaitingClans().size()) >= SiegeManager.getInstance().getDefenderMaxClans())
			{
				return;
			}
		}
		else
		{
			if (getAttackerClans().size() >= SiegeManager.getInstance().getAttackerMaxClans())
			{
				return;
			}
		}
		
		if (saveSiegeClan(clan.getClanId(), type, isUpdateRegistration))
		{
			if (type == SiegeClanType.OWNER || type == SiegeClanType.DEFENDER)
			{
				addDefender(clan.getClanId(), type);
			}
			else if (type == SiegeClanType.ATTACKER)
			{
				addAttacker(clan.getClanId());
			}
			else if (type == SiegeClanType.DEFENDER_PENDING)
			{
				addDefenderWaiting(clan.getClanId());
			}
		}
	}
	
	/**
	 * Removes clan from siege.<BR>
	 * <BR>
	 * @param clanId The int of player's clan id
	 * @param reload 
	 */
	public void removeSiegeClan(int clanId, boolean reload)
	{
		if (clanId <= 0)
		{
			return;
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM siege_clans WHERE castle_id=? and clan_id=?"))
		{
			statement.setInt(1, getCastle().getCastleId());
			statement.setInt(2, clanId);
			statement.execute();
			
			if (reload)
			{
				loadSiegeClans();
			}
		}
		catch (Exception e)
		{
			_log.warning("Exception: removeSiegeClan(): " + e.getMessage());
		}
	}
	
	/**
	 * Removes clan from siege.<BR>
	 * <BR>
	 * @param player The L2PcInstance of player/clan being removed
	 */
	public void removeSiegeClan(L2PcInstance player)
	{
		if ((player.getClan() == null) || (player.getClan().getHasCastle() == getCastle().getCastleId()) || !SiegeManager.getInstance().checkIsRegistered(player.getClan(), getCastle().getCastleId()))
		{
			return;
		}
		
		if (getIsInProgress())
		{
			player.sendMessage("This is not the time for siege registration and so registration and cancellation cannot be done.");
			return;
		}
		removeSiegeClan(player.getClan().getClanId(), true);
	}
	
	// =========================================================
	// Method - Private
	/**
	 * Add clan as attacker<BR>
	 * <BR>
	 * @param clanId The int of clan's id
	 */
	private void addAttacker(int clanId)
	{
		getAttackerClans().add(new L2SiegeClan(clanId, SiegeClanType.ATTACKER)); // Add registered attacker to attacker list
	}
	
	/**
	 * Add clan as defender<BR>
	 * <BR>
	 * @param clanId The int of clan's id
	 */
	private void addDefender(int clanId)
	{
		getDefenderClans().add(new L2SiegeClan(clanId, SiegeClanType.DEFENDER)); // Add registered defender to defender list
	}
	
	/**
	 * <p>
	 * Add clan as defender with the specified type
	 * </p>
	 * @param clanId The int of clan's id
	 * @param type the type of the clan
	 */
	private void addDefender(int clanId, SiegeClanType type)
	{
		getDefenderClans().add(new L2SiegeClan(clanId, type));
	}
	
	/**
	 * Add clan as defender waiting approval<BR>
	 * <BR>
	 * @param clanId The int of clan's id
	 */
	private void addDefenderWaiting(int clanId)
	{
		getDefenderWaitingClans().add(new L2SiegeClan(clanId, SiegeClanType.DEFENDER_PENDING)); // Add registered defender to defender list
	}
	
	private void removeDefender(L2SiegeClan sc)
	{
		if (sc != null)
		{
			getDefenderClans().remove(sc);
		}
	}
	
	private void removeAttacker(L2SiegeClan sc)
	{
		if (sc != null)
		{
			getAttackerClans().remove(sc);
		}
	}
	
	private void addDefender(L2SiegeClan sc, SiegeClanType type)
	{
		if (sc == null)
		{
			return;
		}
		sc.setType(type);
		getDefenderClans().add(sc);
	}
	
	private void addAttacker(L2SiegeClan sc)
	{
		if (sc == null)
		{
			return;
		}
		sc.setType(SiegeClanType.ATTACKER);
		getAttackerClans().add(sc);
	}
	
	/**
	 * Return true if the player can register.<BR>
	 * <BR>
	 * @param player The L2PcInstance of the player trying to register
	 * @return 
	 */
	private boolean checkIfCanRegister(L2PcInstance player)
	{
		if (getIsRegistrationOver())
		{
			player.sendMessage("The deadline to register for the siege of " + getCastle().getName() + " has passed.");
		}
		else if (getIsInProgress())
		{
			player.sendMessage("This is not the time for siege registration and so registration and cancellation cannot be done.");
		}
		else if ((player.getClan() == null) || (player.getClan().getLevel() < SiegeManager.getInstance().getSiegeClanMinLevel()))
		{
			player.sendMessage("Only clans with Level " + SiegeManager.getInstance().getSiegeClanMinLevel() + " and higher may register for a castle siege.");
		}
		else if (player.getClan().getHasCastle() > 0)
		{
			player.sendMessage("You cannot register because your clan already owns a castle.");
		}
		else if (player.getClan().getClanId() == getCastle().getOwnerId())
		{
			player.sendPacket(new SystemMessage(SystemMessage.CLAN_THAT_OWNS_CASTLE_IS_AUTOMATICALLY_REGISTERED_DEFENDING));
		}
		else if (SiegeManager.getInstance().checkIsRegistered(player.getClan(), getCastle().getCastleId()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.ALREADY_REQUESTED_SIEGE_BATTLE));
		}
		else if (checkIfAlreadyRegisteredForSameDay(player.getClan()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.ALREADY_SUBMITTED_A_REQUEST_FOR_ANOTHER_SIEGE_BATTLE));
		}
		else
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Return true if the clan has already registered to a siege for the same day.<BR>
	 * <BR>
	 * @param clan The L2Clan of the player trying to register
	 * @return 
	 */
	public boolean checkIfAlreadyRegisteredForSameDay(L2Clan clan)
	{
		for (Siege siege : SiegeManager.getInstance().getSieges())
		{
			if (siege == this)
			{
				continue;
			}
			if (siege.getSiegeDate().get(Calendar.DAY_OF_WEEK) == this.getSiegeDate().get(Calendar.DAY_OF_WEEK))
			{
				if (siege.checkIsAttacker(clan))
				{
					return true;
				}
				if (siege.checkIsDefender(clan))
				{
					return true;
				}
				if (siege.checkIsDefenderWaiting(clan))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Return the correct siege date as Calendar.<BR>
	 * <BR>
	 */
	private void correctSiegeDateTime()
	{
		boolean corrected = false;
		
		// There might still be time before siege ends
		if (_siegeEndDate.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis())
		{
			// Since siege has past reschedule it to the next one (14 days)
			// This is usually caused by server being down
			corrected = true;
			setNextSiegeDate();
		}
		
		if (getCastle().getSiegeDate().get(Calendar.DAY_OF_WEEK) != getCastle().getSiegeDayOfWeek())
		{
			corrected = true;
			getCastle().getSiegeDate().set(Calendar.DAY_OF_WEEK, getCastle().getSiegeDayOfWeek());
		}
		
		if (getCastle().getSiegeDate().get(Calendar.HOUR_OF_DAY) != getCastle().getSiegeHourOfDay())
		{
			corrected = true;
			getCastle().getSiegeDate().set(Calendar.HOUR_OF_DAY, getCastle().getSiegeHourOfDay());
		}
		
		getCastle().getSiegeDate().set(Calendar.MINUTE, 0);
		
		if (corrected)
		{
			saveSiegeDate();
		}
	}
	
	/**
	 * Loads siege clans from database.
	 */
	private void loadSiegeClans()
	{
		getAttackerClans().clear();
		getDefenderClans().clear();
		getDefenderWaitingClans().clear();
		
		// Add castle owner as defender (add owner first so that they are on the top of the defender list)
		if (getCastle().getOwnerId() > 0)
		{
			addDefender(getCastle().getOwnerId(), SiegeClanType.OWNER);
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT clan_id, type FROM siege_clans where castle_id=?"))
		{
			statement.setInt(1, getCastle().getCastleId());
			try (ResultSet rs = statement.executeQuery())
			{
				while (rs.next())
				{
					final int clanId = rs.getInt("clan_id");
					// Avoid the case of owner being stored as a siege clan due to a possible abnormality
					if (clanId == getCastle().getOwnerId())
					{
						continue;
					}
					
					final int typeId = rs.getInt("type");
					if (typeId == SiegeClanType.DEFENDER.getId())
					{
						addDefender(clanId);
					}
					else if (typeId == SiegeClanType.ATTACKER.getId())
					{
						addAttacker(clanId);
					}
					else if (typeId == SiegeClanType.DEFENDER_PENDING.getId())
					{
						addDefenderWaiting(clanId);
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Exception: loadSiegeClans(): " + e.getMessage());
		}
	}
	
	/**
	 * Removes all spawned control towers.
	 */
	private void removeControlTowers()
	{
		// Remove all instances of control towers for this castle
		for (L2ControlTowerInstance ct : _controlTowers)
		{
			if (ct != null)
			{
				ct.decayMe();
			}
		}
		_controlTowers.clear();
	}
	
	/**
	 * Removes all flags.
	 */
	private void removeFlags()
	{
		for (L2SiegeClan sc : getAttackerClans())
		{
			if (sc != null)
			{
				sc.removeFlags();
			}
		}
		for (L2SiegeClan sc : getDefenderClans())
		{
			if (sc != null)
			{
				sc.removeFlags();
			}
		}
	}
	
	/**
	 * Removes flags from defenders.
	 */
	private void removeDefenderFlags()
	{
		for (L2SiegeClan sc : getDefenderClans())
		{
			if (sc != null)
			{
				sc.removeFlags();
			}
		}
	}
	
	/**
	 * Saves castle siege data to database.
	 */
	private void saveCastleSiege()
	{
		setNextSiegeDate(); // Set the next set date for 2 weeks from now
		saveSiegeDate(); // Save the new date
		startAutoTask(); // Prepare auto start siege and end registration
	}
	
	/**
	 * Save siege date to database.
	 */
	private void saveSiegeDate()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE castle SET siegeDate = ? WHERE id = ?"))
		{
			statement.setLong(1, getSiegeDate().getTimeInMillis());
			statement.setInt(2, getCastle().getCastleId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("Exception: saveSiegeDate(): " + e.getMessage());
		}
	}
	
	/**
	 * Sets the date for the next siege.
	 */
	private void setNextSiegeDate()
	{
		while (getCastle().getSiegeDate().getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
		{
			// Set next siege date if siege has passed
			getCastle().getSiegeDate().add(Calendar.DAY_OF_MONTH, 14); // Schedule to happen in 14 days
		}
		_isRegistrationOver = false; // Allow registration for next siege
		
		clearSiegeClans(); // Cleanup old siege clans from db
	}
	
	/** 
	 * Spawn control tower. 
	 * @param id 
	 */
	private void spawnControlTower(int id)
	{
		for (SiegeSpawn _sp : SiegeManager.getInstance().getControlTowerSpawnList(id))
		{
			L2ControlTowerInstance ct;
			L2NpcTemplate template = NpcTable.getInstance().getTemplate(_sp.getNpcId());
			
			ct = new L2ControlTowerInstance(IdFactory.getInstance().getNextId(), template);
			
			ct.setMaxSiegeHp(_sp.getHp());
			ct.setCurrentHpMp(ct.getMaxHp(), ct.getMaxMp());
			ct.spawnMe(_sp.getLocation().getX(), _sp.getLocation().getY(), _sp.getLocation().getZ() + 20);
			
			_controlTowerCount++;
			_controlTowers.add(ct);
		}
	}
	
	/**
	 * Spawn siege guard.<BR>
	 * <BR>
	 */
	private void spawnSiegeGuard()
	{
		getSiegeGuardManager().spawnSiegeGuard();
		
		// Register guard to the closest Control Tower
		// When CT dies, so do all the guards that it controls
		if (!getSiegeGuardManager().getSiegeGuardSpawn().isEmpty() && !_controlTowers.isEmpty())
		{
			L2ControlTowerInstance closestCt;
			double distance, x, y, z;
			double distanceClosest = 0;
			for (L2Spawn spawn : getSiegeGuardManager().getSiegeGuardSpawn())
			{
				if (spawn == null)
				{
					continue;
				}
				closestCt = null;
				distanceClosest = 0;
				for (L2ControlTowerInstance ct : _controlTowers)
				{
					if (ct == null)
					{
						continue;
					}
					x = (spawn.getLocX() - ct.getX());
					y = (spawn.getLocY() - ct.getY());
					z = (spawn.getLocZ() - ct.getZ());
					
					distance = (x * x) + (y * y) + (z * z);
					
					if ((closestCt == null) || (distance < distanceClosest))
					{
						closestCt = ct;
						distanceClosest = distance;
					}
				}
				
				if (closestCt != null)
				{
					closestCt.registerGuard(spawn);
				}
			}
		}
	}
	
	public final L2SiegeClan getAttackerClan(L2Clan clan)
	{
		if (clan == null)
		{
			return null;
		}
		return getAttackerClan(clan.getClanId());
	}
	
	public final L2SiegeClan getAttackerClan(int clanId)
	{
		for (L2SiegeClan sc : getAttackerClans())
		{
			if ((sc != null) && (sc.getClanId() == clanId))
			{
				return sc;
			}
		}
		return null;
	}
	
	public final List<L2SiegeClan> getAttackerClans()
	{
		return _attackerClans;
		
	}
	
	public final int getAttackerRespawnDelay()
	{
		return (SiegeManager.getInstance().getAttackerRespawnDelay());
	}
	
	public final Castle getCastle()
	{
		if (_castle == null)
		{
			return null;
		}
		return _castle;
	}
	
	public final L2SiegeClan getDefenderClan(L2Clan clan)
	{
		if (clan == null)
		{
			return null;
		}
		return getDefenderClan(clan.getClanId());
	}
	
	public final L2SiegeClan getDefenderClan(int clanId)
	{
		for (L2SiegeClan sc : getDefenderClans())
		{
			if ((sc != null) && (sc.getClanId() == clanId))
			{
				return sc;
			}
		}
		return null;
	}
	
	public final List<L2SiegeClan> getDefenderClans()
	{
		return _defenderClans;
		
	}
	
	public final L2SiegeClan getDefenderWaitingClan(L2Clan clan)
	{
		if (clan == null)
		{
			return null;
		}
		return getDefenderWaitingClan(clan.getClanId());
	}
	
	public final L2SiegeClan getDefenderWaitingClan(int clanId)
	{
		for (L2SiegeClan sc : getDefenderWaitingClans())
		{
			if ((sc != null) && (sc.getClanId() == clanId))
			{
				return sc;
			}
		}
		return null;
	}
	
	public final List<L2SiegeClan> getDefenderWaitingClans()
	{
		return _defenderWaitingClans;
	}
	
	public final boolean getIsInProgress()
	{
		return _isInProgress;
	}
	
	public final boolean getIsRegistrationOver()
	{
		return _isRegistrationOver;
	}
	
	public final Calendar getSiegeDate()
	{
		return getCastle().getSiegeDate();
	}
	
	public final SiegeGuardManager getSiegeGuardManager()
	{
		return _siegeGuardManager;
	}
	
	public int getControlTowerCount()
	{
		return _controlTowerCount;
	}
}