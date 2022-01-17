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

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;

/**
 * This class manages all Raid Bosses. In a group mob, there are one master called RaidBoss and several slaves called Minions.
 * @version $Revision: 1.20.4.6 $ $Date: 2005/04/06 16:13:39 $
 */
public final class L2RaidBossInstance extends L2MonsterInstance
{
	private static final int RAIDBOSS_MAINTENANCE_INTERVAL = 30000; // 30 sec

	private RaidBossSpawnManager.StatusEnum _raidStatus;

	/**
	 * Constructor of L2RaidBossInstance (use L2Character and L2NpcInstance constructor).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Call the L2Character constructor to set the _template of the L2RaidBossInstance (copy skills from template to object and link _calculators to NPC_STD_CALCULATOR)</li>
	 * <li>Set the name of the L2RaidBossInstance</li>
	 * <li>Create a RandomAnimation Task that will be launched after the calculated delay if the server allow it</li><BR>
	 * <BR>
	 * @param objectId Identifier of the object to initialized
	 * @param template Template to apply to the NPC
	 */
	public L2RaidBossInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
		// Set this spawn as Raid Boss
		setIsRaid(true);
	}
	
	@Override
	protected int getMaintenanceInterval()
	{
		return RAIDBOSS_MAINTENANCE_INTERVAL;
	}

	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}

		if (killer instanceof L2PlayableInstance)
		{
			broadcastPacket(new SystemMessage(1209));
		}

		RaidBossSpawnManager.getInstance().updateStatus(this, true);

		return true;

	}

	/**
	 * Spawn all minions at a regular interval Also if boss is too far from home location at the time of this check, teleport it home
	 */
	@Override
	protected void startMaintenanceTask()
	{
		if (_minionList != null)
		{
			_minionList.spawnMinions();
		}

		_maintainTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				// Teleport raid boss home if it's too far from home location
				L2Spawn bossSpawn = getSpawn();
				if (!isInsideRadius(bossSpawn.getLocX(), bossSpawn.getLocY(), bossSpawn.getLocZ(), 5000, true, false))
				{
					teleToLocation(bossSpawn.getLocX(), bossSpawn.getLocY(), bossSpawn.getLocZ(), true);
					healFull(); // prevents minor exploiting with it
				}

				if (_minionList != null)
				{
					_minionList.maintainMinions();
				}
			}
		}, 60000, getMaintenanceInterval() + Rnd.get(5000));
	}

	public void setRaidStatus(RaidBossSpawnManager.StatusEnum status)
	{
		_raidStatus = status;
	}

	public RaidBossSpawnManager.StatusEnum getRaidStatus()
	{
		return _raidStatus;
	}

	/**
	 * Reduce the current HP of the L2Attackable, update its _aggroList and launch the doDie Task if necessary.<BR>
	 * <BR>
	 * @param damage
	 * @param attacker
	 * @param awake
	 * @param isDot
	 */
	@Override
	public void reduceCurrentHp(double damage, L2Character attacker, boolean awake, boolean isDot)
	{
		super.reduceCurrentHp(damage, attacker, awake, isDot);
	}

	public void healFull()
	{
		super.setCurrentHp(super.getMaxHp());
		super.setCurrentMp(super.getMaxMp());
	}
}