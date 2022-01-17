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
package net.sf.l2j.gameserver.model.eventgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2TvTManagerInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.zone.type.L2ArenaZone;
import net.sf.l2j.gameserver.network.serverpackets.ChangeWaitType;
import net.sf.l2j.util.Rnd;

/**
 * @author DnR
 */
public class TvTEvent extends L2Event
{
	protected static Logger _log = Logger.getLogger(TvTEvent.class.getName());

	// TvT related lists
	private List<L2PcInstance> _registered = new CopyOnWriteArrayList<>();
	private List<L2PcInstance> _blueTeam = new CopyOnWriteArrayList<>();
	private List<L2PcInstance> _redTeam = new CopyOnWriteArrayList<>();
	
	private L2ItemInstance[] _rewards;
	private L2DoorInstance[] _doors;
	
	// Team kills
	private int _blueTeamKills = 0;
	private int _redTeamKills = 0;
	
	private boolean _canGiveRewardsOnTie;

	protected int _blueX, _blueY, _blueZ;
	protected int _redX, _redY, _redZ;
	
	private ScheduledFuture<?> _registrationTask = null;
	
	public static TvTEvent getInstance()
	{
		return SingletonHolder._instance;
	}
	
	/**
	 * Loads all configuration settings and starts event if needed.
	 * 
	 * @param settings
	 */
	@Override
	public void load(Properties settings)
	{
		// TvT Event settings
		if (settings == null)
		{
			return;
		}

		// Event has already started, so do not reload anything
		if (_state == EventState.STARTED)
		{
			return;
		}
		
		// Clean up
		_registered.clear();
		_redTeam.clear();
		_blueTeam.clear();
		_rewards = null;
		_doors = null;
		
		_isEnabled = Boolean.valueOf(settings.getProperty("TvTEventEnable", "False"));
		_maxParticipants = Integer.parseInt(settings.getProperty("TvTMaxParticipants", "40"));
		_minParticipants = Integer.parseInt(settings.getProperty("TvTMinParticipants", "6"));
		_minLevel = Byte.parseByte(settings.getProperty("TvTEventMinLevel", "60"));
		if ((_minLevel < 1) || (_minLevel > 78))
		{
			_minLevel = 60;
		}
		_maxLevel = Byte.parseByte(settings.getProperty("TvTEventMaxLevel", "78"));
		if ((_maxLevel < 1) || (_maxLevel > 78))
		{
			_maxLevel = 78;
		}
		_eventDelay = Integer.parseInt(settings.getProperty("TvTEventDelay", "18000"));
		
		int npcId = Integer.parseInt(settings.getProperty("TvTNpcManager", "12371"));
		_npcManager = new L2TvTManagerInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(npcId));
		_npcX = Integer.parseInt(settings.getProperty("TvTNpcX", "151808"));
		_npcY = Integer.parseInt(settings.getProperty("TvTNpcY", "46864"));
		_npcZ = Integer.parseInt(settings.getProperty("TvTNpcZ", "-3408"));
		_arena = ZoneManager.getInstance().getZoneById(Integer.parseInt(settings.getProperty("TvTArenaId", "11012")), L2ArenaZone.class);
		_participationTime = Integer.parseInt(settings.getProperty("TvTEventParticipationTime", "1200"));
		_eventDuration = Integer.parseInt(settings.getProperty("TvTEventDuration", "1800"));
		
		// Blue team coords
		_blueX = Integer.parseInt(settings.getProperty("TvTBlueTeamX", "148476"));
		_blueY = Integer.parseInt(settings.getProperty("TvTBlueTeamY", "46061"));
		_blueZ = Integer.parseInt(settings.getProperty("TvTBlueTeamZ", "-3411"));
		
		// Red team coords
		_redX = Integer.parseInt(settings.getProperty("TvTRedTeamX", "150480"));
		_redY = Integer.parseInt(settings.getProperty("TvTRedTeamY", "47444"));
		_redZ = Integer.parseInt(settings.getProperty("TvTRedTeamZ", "-3411"));
		
		// Player respawn delay upon death
		_playerRespawnDelay = Integer.parseInt(settings.getProperty("TvTPlayerRespawnDelay", "20"));
		
		// Dual box protection
		_isDualBoxAllowed = Boolean.valueOf(settings.getProperty("TvTAllowDualBoxing", "True"));
		
		// Remove buffs on respawn
		_isRemovingBuffsOnRespawn = Boolean.valueOf(settings.getProperty("TvTRemoveBuffsOnRespawn", "True"));
		
		// Give rewards on tie
		_canGiveRewardsOnTie = Boolean.valueOf(settings.getProperty("TvTGiveRewardsOnTie", "False"));
		
		// Load doors
		String[] propertySplit = settings.getProperty("TvTArenaDoors", "").split(";");
		List<L2DoorInstance> doors = new ArrayList<>();
		for (String id : propertySplit)
		{
			L2DoorInstance door = DoorTable.getInstance().getDoor(Integer.parseInt(id));
			if (door != null)
			{
				doors.add(door);
			}
		}
		_doors = doors.toArray(new L2DoorInstance[doors.size()]);
		
		// Load rewards
		propertySplit = settings.getProperty("TvTEventRewardList", "").split(";");
		List<L2ItemInstance> rewards = new ArrayList<>();
		for (String item : propertySplit)
		{
			String[] itemSplit = item.split(",");
			if (itemSplit.length != 2)
			{
				_log.info("[TvTEventRewardList]: invalid config property -> TvTEventRewardList \"" + item + "\"");
			}
			else
			{
				try
				{
					L2ItemInstance reward = ItemTable.getInstance().createDummyItem(Integer.valueOf(itemSplit[0]));
					if (reward == null)
					{
						_log.info("[TvTEventRewardList]: Invalid item " + itemSplit[0]);
						continue;
					}
					
					reward.setCount(Integer.valueOf(itemSplit[1]));
					rewards.add(reward);
				}
				catch (NumberFormatException nfe)
				{
					if (!item.isEmpty())
					{
						_log.info("[TvTEventRewardList]: invalid config property -> ItemList \"" + itemSplit[0] + "\"" + itemSplit[1]);
					}
				}
			}
		}
		_rewards = rewards.toArray(new L2ItemInstance[rewards.size()]);
		
		_log.info("TvT Event: Initialized Event");
		
		if (_state == EventState.INITIAL || _state == EventState.SCHEDULED_NEXT)
		{
			_state = EventState.INITIAL;
			scheduleRegistration();
		}
	}
	
	/**
	 * Schedules event registration.
	 */
	private void scheduleRegistration()
	{
		// If registration task is currently running, cancel it now
		if (_registrationTask != null)
		{
			_registrationTask.cancel(false);
			_registrationTask = null;
		}
		
		// Delete registration NPC if spawned
		if (_npcManager != null)
		{
			if (_npcManager.isVisible())
			{
				_npcManager.deleteMe();
			}
		}

		// Start task
		if (_isEnabled)
		{
			if (_state != EventState.SCHEDULED_NEXT)
			{
				// Set state
				_state = EventState.SCHEDULED_NEXT;
	
				_log.info("TvT Event: Next event in " + (_eventDelay / 60) + " minute(s)");
				_registrationTask = ThreadPoolManager.getInstance().scheduleGeneral(new RegistrationTask(), _eventDelay * 1000L);
			}
		}
	}
	
	/**
	 * Starts event cycle.
	 */
	protected void scheduleEvent()
	{
		// Set state
		_state = EventState.REGISTER;
		
		// Lock arena
		if (_arena != null)
		{
			_arena.setEvent(this);
		}

		// Spawn TvT manager NPC
		_npcManager.setTitle("TvT Registration");
		_npcManager.spawnMe(_npcX, _npcY, _npcZ);
		
		Announcements.getInstance().announceToAll("TvT Event: Registration opened for " + (_participationTime / 60) + " minute(s).");
		// If zone has name, display event location
		if (_arena != null && _arena.getName() != null)
		{
			Announcements.getInstance().announceToAll("TvT Event: Event will take place in " + _arena.getName() + ".");
		}

		// Start timer
		eventTimer(_participationTime);
		
		if ((_registered.size() >= _minParticipants) && (_state != EventState.INITIAL))
		{
			// Prepare arena for event
			prepareArena();
			
			// Port players and start event
			Announcements.getInstance().announceToAll("TvT Event: Event has started!");
			portTeamsToArena();
			eventTimer(_eventDuration);
			
			if (_state == EventState.INITIAL)
			{
				Announcements.getInstance().announceToAll("TvT Event: Event was cancelled.");
			}
			else
			{
				Announcements.getInstance().announceToAll("TvT Event: Blue Team kills: " + _blueTeamKills + " , Red Team kills: " + _redTeamKills + ".");
			}
			
			// Shutting down event
			eventRemovals();
		}
		else
		{
			if (_state == EventState.INITIAL)
			{
				Announcements.getInstance().announceToAll("TvT Event: Event was cancelled.");
			}
			else
			{
				Announcements.getInstance().announceToAll("TvT Event: Event was cancelled due to lack of participation.");
			}
			_registered.clear();
		}

		// Unlock arena
		if (_arena != null)
		{
			_arena.setEvent(null);
		}
		
		// Open doors
		toggleArenaDoors(true);
		
		_state = EventState.INITIAL;
		
		// Schedule next registration
		scheduleRegistration();
	}

	/**
	 * Cleans up arena for event to start.
	 */
	private void prepareArena()
	{
		// Banish players
		if (_arena != null)
		{
			for (L2Character activeChar : _arena.getCharacterList())
			{
				L2PcInstance player = activeChar.getActingPlayer();
				if (player == null)
				{
					continue;
				}
				
				player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
			}
		}
		
		// Close doors
		toggleArenaDoors(false);
	}
	
	/**
	 * Handles arena doors open state.
	 * 
	 * @param open
	 */
	private void toggleArenaDoors(boolean open)
	{
		if (_doors == null)
		{
			return;
		}
		
		for (L2DoorInstance door : _doors)
		{
			// Open door
			if (open)
			{
				door.openMe();
			}
			// Close door
			else
			{
				door.closeMe();
			}
		}
	}
	
	/**
	 * Cleans up and finishes event.
	 */
	private void eventRemovals()
	{
		// Blue team
		for (L2PcInstance blue : _blueTeam)
		{
			if (blue == null)
			{
				continue;
			}
			
			// Give rewards
			if (_state != EventState.INITIAL && (_blueTeamKills > _redTeamKills || _blueTeamKills == _redTeamKills && _canGiveRewardsOnTie))
			{
				if (_rewards != null)
				{
					for (L2ItemInstance reward : _rewards)
					{
						blue.addItem("TvTReward", reward.getItemId(), reward.getCount(), null, true);
					}
				}
			}
			
			if (blue.isDead())
			{
				blue.doRevive();
			}
			
			removePlayer(blue);
			blue.teleToLocation(_npcX, _npcY, _npcZ, true);
		}
		
		// Red team
		for (L2PcInstance red : _redTeam)
		{
			if (red == null)
			{
				continue;
			}
			
			// Give rewards
			if (_state != EventState.INITIAL && (_blueTeamKills < _redTeamKills || _blueTeamKills == _redTeamKills && _canGiveRewardsOnTie))
			{
				if (_rewards != null)
				{
					for (L2ItemInstance reward : _rewards)
					{
						red.addItem("TvTReward", reward.getItemId(), reward.getCount(), null, true);
					}
				}
			}
			
			if (red.isDead())
			{
				red.doRevive();
			}
			
			removePlayer(red);
			red.teleToLocation(_npcX, _npcY, _npcZ, true);
		}
		
		// Event ended in a tie and no rewards will be given
		if (_blueTeamKills == _redTeamKills && !_canGiveRewardsOnTie)
		{
			Announcements.getInstance().announceToAll("TvT Event: Event ended in a Tie. No rewards will be given!");
		}
		
		_blueTeam.clear();
		_redTeam.clear();
		_blueTeamKills = 0;
		_redTeamKills = 0;
	}
	
	/**
	 * Event timer.
	 * 
	 * @param time
	 */
	private void eventTimer(int time)
	{
		for (int seconds = time; (seconds > 0 && _state != EventState.INITIAL); seconds--)
		{
			switch (seconds)
			{
				case 900:
				case 600:
				case 300:
				case 60:
					if (_state == EventState.STARTED)
					{
						Announcements.getInstance().announceToAll("TvT Event: " + (seconds / 60) + " minute(s) until event is finished!");
					}
					else
					{
						Announcements.getInstance().announceToAll("TvT Event: " + (seconds / 60) + " minute(s) until registration is closed!");
					}
					break;
				case 30:
				case 5:
					if (_state == EventState.STARTED)
					{
						Announcements.getInstance().announceToAll("TvT Event: " + seconds + " second(s) until event is finished!");
					}
					else
					{
						Announcements.getInstance().announceToAll("TvT Event: " + seconds + " second(s) until registration is closed!");
					}
					break;
			}
			
			long oneSecWaitStart = System.currentTimeMillis();
			while ((oneSecWaitStart + 1000L) > System.currentTimeMillis())
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException ie)
				{
				}
			}
		}
	}
	
	/**
	 * Ports teams to arena.
	 */
	private void portTeamsToArena()
	{
		L2PcInstance player;
		while (_registered.size() > 0)
		{
			player = _registered.get(Rnd.get(_registered.size()));
			
			// First create 2 event teams
			if (_blueTeam.size() > _redTeam.size())
			{
				_redTeam.add(player);
				player.setEventTeam(2);
			}
			else
			{
				_blueTeam.add(player);
				player.setEventTeam(1);
			}
			
			// Abort casting if player casting
			if (player.isCastingNow())
			{
				player.abortCast();
			}
			
			player.getAppearance().setVisible();
			
			if (player.isDead())
			{
				player.doRevive();
			}
			else
			{
				player.setCurrentCp(player.getMaxCp());
				player.setCurrentHp(player.getMaxHp());
				player.setCurrentMp(player.getMaxMp());
			}
			
			// Remove Buffs if needed
			if (_isRemovingBuffsOnRespawn)
			{
				player.stopAllEffects();
				player.clearCharges();
			}
			
			// Remove Summon's Buffs
			L2Summon summon = player.getPet();
			if (summon != null)
			{
				if (summon.isCastingNow())
				{
					summon.abortCast();
				}
				
				if (summon instanceof L2PetInstance)
				{
					summon.unSummon(player);
				}
				else if (_isRemovingBuffsOnRespawn)
				{
					summon.stopAllEffects();
				}
			}
			
			// Remove player from his party
			if (player.getParty() != null)
			{
				L2Party party = player.getParty();
				party.removePartyMember(player, true);
			}
			
			_registered.remove(player);
		}
		
		_state = EventState.STARTED;
		
		// Port teams
		for (L2PcInstance blue : _blueTeam)
		{
			if (blue == null)
			{
				continue;
			}
			
			blue.teleToLocation(_blueX, _blueY, _blueZ, true);
		}
		
		for (L2PcInstance red : _redTeam)
		{
			if (red == null)
			{
				continue;
			}
			
			red.teleToLocation(_redX, _redY, _redZ, true);
		}
	}
	
	/**
	 * Registers player to event.
	 * 
	 * @param player
	 */
	@Override
	public void registerPlayer(L2PcInstance player)
	{
		if (_state != EventState.REGISTER)
		{
			player.sendMessage("TvT Registration is not in progress.");
			return;
		}
		
		if (player.isFestivalParticipant())
		{
			player.sendMessage("Festival participants cannot register to the event.");
			return;
		}
		
		if (player.isInJail())
		{
			player.sendMessage("Jailed players cannot register to the event.");
			return;
		}
		
		if (player.isAIOBuffer())
		{
			player.sendMessage("AIO Buffers cannot register to the event.");
			return;
		}
		
		if (player.isDead())
		{
			player.sendMessage("Dead players cannot register to the event.");
			return;
		}
		
		if (Olympiad.getInstance().isRegisteredInComp(player))
		{
			player.sendMessage("Grand Olympiad participants cannot register to the event.");
			return;
		}
		
		if ((player.getLevel() < _minLevel) || (player.getLevel() > _maxLevel))
		{
			player.sendMessage("You have not reached the appropriate level to join the event.");
			return;
		}
		
		if (_registered.size() == _maxParticipants)
		{
			player.sendMessage("There is no more room for you to register to the event.");
			return;
		}
		
		for (L2PcInstance registered : _registered)
		{
			if (registered == null)
			{
				continue;
			}
			
			if (registered.getObjectId() == player.getObjectId())
			{
				player.sendMessage("You are already registered in the TvT event.");
				return;
			}
			
			// Check if dual boxing is not allowed
			if (!_isDualBoxAllowed)
			{
				if ((registered.getClient() == null) || (player.getClient() == null))
				{
					continue;
				}
				
				String ip1 = player.getClient().getConnection().getInetAddress().getHostAddress();
				String ip2 = registered.getClient().getConnection().getInetAddress().getHostAddress();
				if ((ip1 != null) && (ip2 != null) && ip1.equals(ip2))
				{
					player.sendMessage("Your IP is already registered in the TvT event.");
					return;
				}
			}
		}
		
		_registered.add(player);
		
		player.sendMessage("You have registered to participate in the TvT Event.");
		
		super.registerPlayer(player);
	}
	
	/**
	 * Removes player from event.
	 * 
	 * @param player
	 */
	@Override
	public void removePlayer(L2PcInstance player)
	{
		if (_registered.contains(player))
		{
			_registered.remove(player);
			player.sendMessage("You have been removed from the TvT Event registration list.");
		}
		else if (player.getEventTeam() == 1)
		{
			_blueTeam.remove(player);
		}
		else if (player.getEventTeam() == 2)
		{
			_redTeam.remove(player);
		}
		
		// If no participants left, abort event
		if ((player.getEventTeam() > 0) && (_blueTeam.size() == 0) && (_redTeam.size() == 0))
		{
			_state = EventState.INITIAL;
		}
		
		// Now, remove team status
		player.setEventTeam(0);
		
		super.removePlayer(player);
	}
	
	@Override
	public boolean isRegistered(L2PcInstance player)
	{
		return _registered.contains(player);
	}
	
	public List<L2PcInstance> getBlueTeam()
	{
		return _blueTeam;
	}
	
	public List<L2PcInstance> getRedTeam()
	{
		return _redTeam;
	}
	
	public List<L2PcInstance> getRegistered()
	{
		return _registered;
	}
	
	public int getBlueTeamKills()
	{
		return _blueTeamKills;
	}
	
	public int getRedTeamKills()
	{
		return _redTeamKills;
	}
	
	/**
	 * Increases player team kills.
	 * 
	 * @param player
	 * @param target 
	 */
	public void increaseTeamKills(L2PcInstance player, L2PcInstance target)
	{
		// Increase kills only if victim belonged to enemy team
		if (player.getEventTeam() == 1 && target.getEventTeam() == 2)
		{
			_blueTeamKills++;
		}
		else if (player.getEventTeam() == 2 && target.getEventTeam() == 1)
		{
			_redTeamKills++;
		}
	}
	
	public int getMinParticipants()
	{
		return _minParticipants;
	}
	
	public int getMaxParticipants()
	{
		return _maxParticipants;
	}
	
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	public boolean isDualBoxAllowed()
	{
		return _isDualBoxAllowed;
	}
	
	public int getPlayerRespawnDelay()
	{
		return _playerRespawnDelay;
	}
	
	class RegistrationTask implements Runnable
	{
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			if (_isEnabled)
			{
				scheduleEvent();
			}
			else
			{
				_state = EventState.INITIAL; // Default state
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.entity.L2Event#onDie(net.sf.l2j.gameserver.model.actor.instance.L2PcInstance)
	 */
	@Override
	public void onDie(L2PcInstance player)
	{
		if (player == null)
		{
			return;
		}

		player.broadcastPacket(new ChangeWaitType(player, ChangeWaitType.WT_START_FAKEDEATH));
		
		player.sendMessage("You will be respawned in " + _playerRespawnDelay + " seconds.");
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				if (!player.isDead() || player.isPendingRevive())
				{
					return;
				}
				
				player.setIsPendingRevive(true);
				
				if (player.getEventTeam() == 1)
				{
					player.teleToLocation(_blueX, _blueY, _blueZ, true);
				}
				else if (player.getEventTeam() == 2)
				{
					player.teleToLocation(_redX, _redY, _redZ, true);
				}
				// Player has probably left event for some reason
				else
				{
					player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
				}
			}
		}, _playerRespawnDelay * 1000L);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.entity.L2Event#onKill(net.sf.l2j.gameserver.model.actor.instance.L2PcInstance)
	 */
	@Override
	public void onKill(L2PcInstance player, L2PcInstance target)
	{
		if (player == null || target == null)
		{
			return;
		}

		increaseTeamKills(player, target);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.entity.L2Event#onRevive(net.sf.l2j.gameserver.model.actor.instance.L2PcInstance)
	 */
	@Override
	public void onRevive(L2PcInstance player)
	{
		if (player == null)
		{
			return;
		}

		// Heal Player fully
		player.setCurrentCp(player.getMaxCp());
		player.setCurrentHp(player.getMaxHp());
		player.setCurrentMp(player.getMaxMp());

		ChangeWaitType revive = new ChangeWaitType(player, ChangeWaitType.WT_STOP_FAKEDEATH);
		player.broadcastPacket(revive);
	}
	
	private static class SingletonHolder
	{
		protected static final TvTEvent _instance = new TvTEvent();
	}
}