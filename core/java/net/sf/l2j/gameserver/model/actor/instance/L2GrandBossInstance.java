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

import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * This class manages all Grand Bosses.
 * @version $Revision: 1.0.0.0 $ $Date: 2006/06/16 $
 */
public final class L2GrandBossInstance extends L2MonsterInstance
{
	private static final int BOSS_MAINTENANCE_INTERVAL = 10000;

	/**
	 * Constructor for L2GrandBossInstance. This represents all grand bosses:
	 * <ul>
	 * <li>12001 Queen Ant</li>
	 * <li>12052 Core</li>
	 * <li>12169 Orfen</li>
	 * <li>12211 Antharas</li>
	 * <li>12372 Baium</li>
	 * <li>12374 Zaken</li>
	 * <li>12899 Valakas</li>
	 * </ul>
	 * <br>
	 * @param objectId ID of the instance
	 * @param template L2NpcTemplate of the instance
	 */
	public L2GrandBossInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);

		// Set Raid Boss state
		setIsRaid(true);
	}

	@Override
	protected int getMaintenanceInterval()
	{
		return BOSS_MAINTENANCE_INTERVAL;
	}

	@Override
	public void onSpawn()
	{
		super.onSpawn();
	}
}