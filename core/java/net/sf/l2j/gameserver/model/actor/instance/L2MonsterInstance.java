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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.SoulCrystalData;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.knownlist.MonsterKnownList;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.L2SoulCrystal;
import net.sf.l2j.gameserver.util.MinionList;
import net.sf.l2j.util.Rnd;

/**
 * This class manages all Monsters. L2MonsterInstance :<BR>
 * <BR>
 * <li>L2MinionInstance</li>
 * <li>L2RaidBossInstance</li>
 * <li>L2GrandBossInstance</li>
 * @version $Revision: 1.20.4.6 $ $Date: 2005/04/06 16:13:39 $
 */
public class L2MonsterInstance extends L2Attackable
{
	protected MinionList _minionList = null;
	
	protected ScheduledFuture<?> _maintainTask = null;
	
	private static final int MONSTER_MAINTENANCE_INTERVAL = 1000;

	/** True if a Soul Crystal was successfully used on the L2Attackable */
	private boolean _absorbed;
	
	/** The table containing all L2PcInstance that successfully absorbed the soul of this L2Attackable */
	private final Map<L2PcInstance, AbsorberInfo> _absorbersList = new ConcurrentHashMap<>();
	
	/**
	 * Constructor of L2MonsterInstance (use L2Character and L2NpcInstance constructor).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Call the L2Character constructor to set the _template of the L2MonsterInstance (copy skills from template to object and link _calculators to NPC_STD_CALCULATOR)</li>
	 * <li>Set the name of the L2MonsterInstance</li>
	 * <li>Create a RandomAnimation Task that will be launched after the calculated delay if the server allow it</li><BR>
	 * <BR>
	 * @param objectId Identifier of the object to initialized
	 * @param template L2NpcTemplate to apply to the NPC
	 */
	public L2MonsterInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
		getKnownList();
		if (getTemplate().getMinionData() != null)
		{
			_minionList = new MinionList(this);
		}
	}
	
	@Override
	public final MonsterKnownList getKnownList()
	{
		if ((super.getKnownList() == null) || !(super.getKnownList() instanceof MonsterKnownList))
		{
			setKnownList(new MonsterKnownList(this));
		}
		return (MonsterKnownList) super.getKnownList();
	}
	
	/**
	 * Return True if the attacker is not another monster.<BR>
	 * <BR>
	 */
	@Override
	public boolean isAutoAttackable(L2Character attacker)
	{
		if (attacker instanceof L2MonsterInstance)
		{
			return false;
		}

		return true;
	}
	
	/**
	 * Return True if the L2MonsterInstance is Aggressive (aggroRange > 0).<BR>
	 * <BR>
	 */
	@Override
	public boolean isAggressive()
	{
		return (getTemplate().aggroRange > 0);
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();

		resetAbsorbList();
		
		if (_minionList != null)
		{
			for (L2MinionInstance minion : getSpawnedMinions())
			{
				if (minion == null)
				{
					continue;
				}
				getSpawnedMinions().remove(minion);
				minion.deleteMe();
			}
			_minionList.clearRespawnList();
		}
		startMaintenanceTask();
	}
	
	protected int getMaintenanceInterval()
	{
		return MONSTER_MAINTENANCE_INTERVAL;
	}
	
	/**
	 * Spawn all minions at a regular interval
	 */
	protected void startMaintenanceTask()
	{
		if (_minionList == null)
		{
			return;
		}
		
		_maintainTask = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				_minionList.spawnMinions();
			}
		}, getMaintenanceInterval());
	}
	
	public void callMinions()
	{
		if (_minionList != null && _minionList.hasMinions())
		{
			for (L2MinionInstance minion : _minionList.getSpawnedMinions())
			{
				// Get actual coords of the minion and check to see if it's too far away from this L2MonsterInstance
				if (!isInsideRadius(minion, 200, false, false))
				{
					// Get the coords of the master to use as a base to move the minion to
					int masterX = getX();
					int masterY = getY();
					int masterZ = getZ();
					
					// Calculate a new random coord for the minion based on the master's coord
					int minionX = masterX + (Rnd.nextInt(401) - 200);
					int minionY = masterY + (Rnd.nextInt(401) - 200);
					int minionZ = masterZ;
					while (((minionX != (masterX + 30)) && (minionX != (masterX - 30))) || ((minionY != (masterY + 30)) && (minionY != (masterY - 30))))
					{
						minionX = masterX + (Rnd.nextInt(401) - 200);
						minionY = masterY + (Rnd.nextInt(401) - 200);
					}
					
					// Move the minion to the new coords
					if (!minion.isInCombat() && !minion.isDead() && !minion.isMovementDisabled())
					{
						minion.moveToLocation(minionX, minionY, minionZ, 0);
					}
					
				}
			}
		}
	}
	
	public void callMinionsToAssist(L2Character attacker)
	{
		if (_minionList != null && _minionList.hasMinions())
		{
			List<L2MinionInstance> spawnedMinions = _minionList.getSpawnedMinions();
			if (spawnedMinions != null && !spawnedMinions.isEmpty())
			{
				Iterator<L2MinionInstance> itr = spawnedMinions.iterator();
				L2MinionInstance minion;
				while (itr.hasNext())
				{
					minion = itr.next();
					// Trigger the aggro condition of the minion
					if ((minion != null) && (attacker != null) && !minion.isDead() && !minion.isInCombat())
					{
						if (isRaid() && !(this instanceof L2MinionInstance))
						{
							minion.addDamageHate(attacker, 0, 100);
						}
						else
						{
							minion.addDamageHate(attacker, 0, 1);
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}

		// Enhance soul crystals of the attacker if this L2MonsterInstance had its soul absorbed
		try
		{
			levelSoulCrystals(killer);
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "", e);
		}
		
		if (_maintainTask != null)
		{
			_maintainTask.cancel(true); // doesn't do it?
		}
		
		if (hasMinions() && isRaid())
		{
			deleteSpawnedMinions();
		}
		return true;
	}
	
	public List<L2MinionInstance> getSpawnedMinions()
	{
		if (_minionList == null)
		{
			return null;
		}
		
		return _minionList.getSpawnedMinions();
	}
	
	public int getTotalSpawnedMinionsInstances()
	{
		if (_minionList == null)
		{
			return 0;
		}
		
		return _minionList.countSpawnedMinions();
	}
	
	public int getTotalSpawnedMinionsGroups()
	{
		if (_minionList == null)
		{
			return 0;
		}
		
		return _minionList.lazyCountSpawnedMinionsGroups();
	}
	
	public void notifyMinionDied(L2MinionInstance minion)
	{
		if (_minionList == null)
		{
			return;
		}
		
		_minionList.moveMinionToRespawnList(minion);
	}
	
	public void notifyMinionSpawned(L2MinionInstance minion)
	{
		if (_minionList == null)
		{
			return;
		}
		
		_minionList.addSpawnedMinion(minion);
	}
	
	public boolean hasMinions()
	{
		if (_minionList == null)
		{
			return false;
		}
		
		return _minionList.hasMinions();
	}
	
	@Override
	public void deleteMe()
	{
		if (hasMinions())
		{
			if (_maintainTask != null)
			{
				_maintainTask.cancel(true);
			}
			
			deleteSpawnedMinions();
		}
		super.deleteMe();
	}
	
	public void deleteSpawnedMinions()
	{
		if (getSpawnedMinions() != null)
		{
			for (L2MinionInstance minion : getSpawnedMinions())
			{
				if (minion == null)
				{
					continue;
				}
				
				minion.abortAttack();
				minion.abortCast();
				minion.deleteMe();
				getSpawnedMinions().remove(minion);
			}
			
			_minionList.clearRespawnList();
		}
	}

	private int getAbsorbLevel()
	{
		return getTemplate().absorbLevel;
	}

	/**
	 * Activate the absorbed soul condition on the L2Attackable.<BR>
	 * <BR>
	 */
	public void absorbSoul()
	{
		_absorbed = true;
	}
	
	/**
	 * Return True if the L2Attackable had his soul absorbed.<BR>
	 * <BR>
	 * @return
	 */
	public boolean isAbsorbed()
	{
		return _absorbed;
	}

	/**
	 * Adds an attacker that successfully absorbed the soul of this L2Attackable into the _absorbersList.<BR>
	 * <BR>
	 * @param attacker - a valid L2PcInstance condition
	 */
	public void addAbsorber(L2PcInstance attacker)
	{
		// The attacker must not be null
		if (attacker == null)
		{
			return;
		}
		
		// This L2Attackable must be of one type in the _absorbingMOBS_levelXX tables.
		// OBS: This is done so to avoid triggering the absorbed conditions for mobs that can't be absorbed.
		if (getAbsorbLevel() == 0)
		{
			return;
		}
		
		// If we have no _absorbersList initiated, do it
		AbsorberInfo ai = _absorbersList.get(attacker);
		
		// If the L2Character attacker isn't already in the _absorbersList of this L2Attackable, add it
		if (ai == null)
		{
			ai = new AbsorberInfo(attacker, getCurrentHp());
			_absorbersList.put(attacker, ai);
		}
		else
		{
			ai.absorber = attacker;
			ai.absorbedHP = getCurrentHp();
		}
		
		// Set this L2Attackable as absorbed
		absorbSoul();
	}
	
	/**
	 * Calculate the leveling chance of Soul Crystals based on the attacker that killed this L2Attackable
	 * @param attacker The player that last killed this L2Attackable $ Rewrite 06.12.06 - Yesod
	 */
	private void levelSoulCrystals(L2Character attacker)
	{
		// Only L2PcInstance can absorb a soul
		if (!(attacker instanceof L2PcInstance) && !(attacker instanceof L2Summon))
		{
			resetAbsorbList();
			return;
		}
		
		int maxAbsorbLevel = getAbsorbLevel();
		int minAbsorbLevel = 0;
		
		// If this is not a valid L2Attackable, clears the _absorbersList and just return
		if (maxAbsorbLevel == 0)
		{
			resetAbsorbList();
			return;
		}
		// All boss mobs with maxAbsorbLevel 13 have minAbsorbLevel of 12 else 10
		if (maxAbsorbLevel > 10)
		{
			minAbsorbLevel = maxAbsorbLevel > 12 ? 12 : 10;
		}
		
		final boolean isBossMob = maxAbsorbLevel > 10 ? true : false;
		
		L2NpcTemplate.AbsorbCrystalType absorbType = getTemplate().absorbType;
		L2PcInstance killer = (attacker instanceof L2Summon) ? ((L2Summon) attacker).getOwner() : (L2PcInstance) attacker;
		
		// If this mob is a boss, then skip some checks
		if (!isBossMob)
		{
			// Fail if this L2Attackable isn't absorbed or there's no one in its _absorbersList
			if (!isAbsorbed())
			{
				resetAbsorbList();
				return;
			}
			
			// Fail if the killer isn't in the _absorbersList of this L2Attackable and mob is not boss
			AbsorberInfo ai = _absorbersList.get(killer);
			if (ai == null || ai.absorber.getObjectId() != killer.getObjectId())
			{
				resetAbsorbList();
				return;
			}
			
			// Check if the soul crystal was used when HP of this L2Attackable wasn't higher than half of it
			if (ai.absorbedHP > (getMaxHp() / 2.0))
			{
				resetAbsorbList();
				return;
			}
		}
		
		final int chance = Rnd.get(100);
		int quantity = 0;
		L2SoulCrystal crystal = null;
		
		// ********
		// Now we have four choices:
		// 1- The Monster level is too low for the crystal. Nothing happens.
		// 2- Everything is correct, but it failed. Nothing happens. (57.5%)
		// 3- Everything is correct, but it failed. The crystal scatters. A sound event is played. (10%)
		// 4- Everything is correct, the crystal level up. A sound event is played. (32.5%)
		List<L2PcInstance> players = new ArrayList<>();
		if (absorbType == L2NpcTemplate.AbsorbCrystalType.FULL_PARTY && killer.isInParty())
		{
			players = killer.getParty().getPartyMembers();
		}
		else if (absorbType == L2NpcTemplate.AbsorbCrystalType.PARTY_ONE_RANDOM && killer.isInParty())
		{
			// This is a naive method for selecting a random member. It gets any random party member and
			// then checks if the member has a valid crystal. It does not select the random party member
			// among those who have crystals, only.	However, this might actually be correct (same as retail).
			players.add(killer.getParty().getPartyMembers().get(Rnd.get(killer.getParty().getMemberCount())));
		}
		else
		{
			players.add(killer);
		}
		
		for (L2PcInstance player : players)
		{
			quantity = 0;
			
			L2ItemInstance[] items = player.getInventory().getItems();
			for (L2ItemInstance item : items)
			{
				int itemId = item.getItemId();
				for (L2SoulCrystal c : SoulCrystalData.getInstance().getSoulCrystals())
				{
					if (c.itemId == itemId)
					{
						if (Config.LEVEL_UP_SOUL_CRYSTAL_WHEN_HAS_MANY)
						{
							if (quantity < 1)
							{
								quantity = 1;
							}
						}
						else
						{
							// Keep count but make sure the player has no more than 1 crystal
							if (++quantity > 1)
							{
								break;
							}
						}
						
						// Check if the crystal level is sufficient
						if (c.level >= minAbsorbLevel && c.level < maxAbsorbLevel)
						{
							crystal = c;
							if (Config.LEVEL_UP_SOUL_CRYSTAL_WHEN_HAS_MANY)
							{
								break;
							}
						}
					}
				}
			}
			
			// Too many crystals in inventory
			if (quantity > 1)
			{
				player.sendPacket(new SystemMessage(SystemMessage.SOUL_CRYSTAL_ABSORBING_FAILED_RESONATION));
				continue;
			}
			
			// Player has no crystals or crystal was rejected
			if (crystal == null)
			{
				if (quantity == 1)
				{
					player.sendPacket(new SystemMessage(SystemMessage.SOUL_CRYSTAL_ABSORBING_REFUSED));
				}
				continue;
			}
			
			/**
			 * TODO: Confirm boss chance for crystal level up and for crystal breaking.
             * It is known that bosses with FULL_PARTY crystal level ups have 100% success rate, but this is not
             * the case for the other bosses (one-random or last-hit).
             * While not confirmed, it is most reasonable that crystals leveled up at bosses will never break.
             * Also, the chance to level up is guessed as around 70% if not higher.
             */
			final int chanceLevelUp = isBossMob ? 70 : SoulCrystalData.LEVEL_CHANCE;
			
			// If succeeds or it is a boss mob, level up the crystal.
			// Ember and Anakazel(78) are not 100% success rate and each individual
			// member of the party has a failure rate on leveling.
			if (absorbType == L2NpcTemplate.AbsorbCrystalType.FULL_PARTY && getNpcId() != 10319 && getNpcId() != 10338 
				|| (chance <= chanceLevelUp))
			{
				// Give staged crystal
				exchangeCrystal(player, crystal.itemId, crystal.leveledItemId, false);
			}
			else if (!isBossMob && (chance >= (100.0 - SoulCrystalData.BREAK_CHANCE)))
			{
				// Remove current crystal and give a broken one
				exchangeCrystal(player, crystal.itemId, crystal.color.getBrokenCrystalId(), true);
				resetAbsorbList();
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage.SOUL_CRYSTAL_ABSORBING_FAILED));
			}
		}
	}
	
	private void exchangeCrystal(L2PcInstance player, int takeId, int giveId, boolean isBroken)
	{
		L2ItemInstance item = player.getInventory().destroyItemByItemId("SoulCrystal", takeId, 1, player, this);
		if (item != null)
		{
			// Prepare inventory update packet
			InventoryUpdate playerIU = new InventoryUpdate();
			playerIU.addRemovedItem(item);
			
			// Add new crystal to the killer's inventory
			item = player.getInventory().addItem("SoulCrystal", giveId, 1, player, this);
			playerIU.addItem(item);
			
			// Send a sound event and text message to the player
			if (isBroken)
			{
				player.sendPacket(new SystemMessage(SystemMessage.SOUL_CRYSTAL_BROKE));
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage.SOUL_CRYSTAL_ABSORBING_SUCCEEDED));
			}
			
			// Send system message
			SystemMessage sms = new SystemMessage(SystemMessage.EARNED_ITEM);
			sms.addItemName(giveId);
			player.sendPacket(sms);
			
			// Send inventory update packet
			player.sendPacket(playerIU);
		}
	}
	
	private void resetAbsorbList()
	{
		_absorbed = false;
		_absorbersList.clear();
	}

	/**
	 * This class contains all AbsorberInfo of the L2Attackable against the absorber L2Character.<BR>
	 * <BR>
	 * <B><U> Data</U> :</B><BR>
	 * <BR>
	 * <li>absorber : The attacker L2Character concerned by this AbsorberInfo of this L2Attackable</li>
	 */
	public final class AbsorberInfo
	{
		/** The attacker L2Character concerned by this AbsorberInfo of this L2Attackable */
		L2PcInstance absorber;
		double absorbedHP;
		
		/**
		 * Constructor of AbsorberInfo.<BR>
		 * <BR>
		 * @param attacker
		 * @param pAbsorbedHP
		 */
		AbsorberInfo(L2PcInstance attacker, double pAbsorbedHP)
		{
			absorber = attacker;
			absorbedHP = pAbsorbedHP;
		}
		
		/**
		 * Verify is object is equal to this AbsorberInfo.<BR>
		 * <BR>
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			
			if (obj instanceof AbsorberInfo)
			{
				return (((AbsorberInfo) obj).absorber == absorber);
			}
			
			return false;
		}
		
		/**
		 * Return the Identifier of the absorber L2Character.<BR>
		 * <BR>
		 */
		@Override
		public int hashCode()
		{
			return absorber.getObjectId();
		}
	}
}