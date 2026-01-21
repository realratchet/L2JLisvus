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
package net.sf.l2j.gameserver.instancemanager;

import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.type.L2TownZone;

public class TownManager
{
	public static final boolean townHasCastleInSiege(int x, int y)
	{
		int locId = MapRegionManager.getInstance().getMapRegionLocId(x, y);
		int castleId;
		
		switch (locId)
		{
			case 912:
				castleId = 1;
				break;
			case 916:
				castleId = 2;
				break;
			case 918:
				castleId = 3;
				break;
			case 922:
				castleId = 4;
				break;
			case 924:
				castleId = 5;
				break;
			case 926:
				castleId = 6;
				break;
			case 1538:
				castleId = 7;
				break;
			case 1537:
				castleId = 8;
				break;
			case 1714:
				castleId = 9;
				break;
			default:
				castleId = 0;
				break;
		}

		Castle castle = castleId > 0 ? CastleManager.getInstance().getCastleById(castleId) : null;
		return castle != null ? castle.getSiege().getIsInProgress() : false;
	}
	
	public static final L2TownZone getTown(int townId)
	{
		for (L2TownZone temp : ZoneManager.getInstance().getAllZones(L2TownZone.class))
		{
			if (temp.getTownId() == townId)
			{
				return temp;
			}
		}
		return null;
	}
	
	public static final L2TownZone getTown(int x, int y, int z)
	{
		for (L2ZoneType temp : ZoneManager.getInstance().getZones(x, y, z))
		{
			if (temp instanceof L2TownZone)
			{
				return (L2TownZone) temp;
			}
		}
		return null;
	}
}