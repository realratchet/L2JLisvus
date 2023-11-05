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
package net.sf.l2j.gameserver.skills.l2skills;

import net.sf.l2j.gameserver.datatables.MapRegionTable.TeleportWhereType;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author Nemesiss
 */
public class L2SkillTeleport extends L2Skill
{
	private final TeleportWhereType _teleportWhereType;
	private final int _locX, _locY, _locZ;
	
	public L2SkillTeleport(StatsSet set)
	{
		super(set);
		
		_teleportWhereType = set.getEnum("teleportWhereType", TeleportWhereType.class, TeleportWhereType.Town);
		_locX = set.getInteger("locX", 0);
		_locY = set.getInteger("locY", 0);
		_locZ = set.getInteger("locZ", 0);
	}
	
	/**
	 * @see net.sf.l2j.gameserver.model.L2Skill#useSkill(net.sf.l2j.gameserver.model.L2Character, net.sf.l2j.gameserver.model.L2Object[])
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		for (L2Object obj : targets)
		{
			if (!(obj instanceof L2PcInstance))
			{
				continue;
			}
			
			L2PcInstance target = (L2PcInstance) obj;
			
			if (_locX != 0 && _locY != 0 && _locZ != 0)
			{
				target.teleToLocation(_locX, _locY, _locZ, true);
			}
			else
			{
				target.teleToLocation(_teleportWhereType);
			}
		}
	}
}