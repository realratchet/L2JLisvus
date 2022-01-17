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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.knownlist.MonsterKnownList;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
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
}