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

import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.type.L2TownZone;

public class TownManager
{
	public static final L2TownZone getClosestTown(L2Object activeObject)
	{
		switch (MapRegionTable.getInstance().getMapRegion(activeObject.getPosition().getX(), activeObject.getPosition().getY()))
		{
			case 0:
				return getTown(2); // TI
			case 1:
				return getTown(3); // Elven
			case 2:
				return getTown(1); // DE
			case 3:
				return getTown(4); // Orc
			case 4:
				return getTown(6); // Dwarven
			case 5:
				return getTown(7); // Gludio
			case 6:
				return getTown(5); // Gludin
			case 7:
				return getTown(8); // Dion
			case 8:
				return getTown(9); // Giran
			case 9:
				return getTown(10); // Oren
			case 10:
				return getTown(12); // Aden
			case 11:
				return getTown(11); // HV
			case 12:
				return getTown(9); // Giran
			case 13:
				return getTown(15); // Heine
			case 14:
				return getTown(14); // Rune
			case 15:
				return getTown(13); // Goddard
			case 16:
				return getTown(8); // Dion
		}
		
		return getTown(12); // Default to Aden
	}
	
	public static final boolean townHasCastleInSiege(int x, int y)
	{
		int curtown = (MapRegionTable.getInstance().getMapRegion(x, y));
		int[] castleidarray =
		{
			0,
			0,
			0,
			0,
			0,
			1,
			0,
			2,
			3,
			4,
			5,
			0,
			0,
			6,
			0,
			7,
			2
		};
		// find an instance of the castle for this town.
		int castleIndex = castleidarray[curtown];
		if (castleIndex > 0)
		{
			Castle castle = CastleManager.getInstance().getCastles().get(CastleManager.getInstance().getCastleIndex(castleIndex));
			if (castle != null)
			{
				return castle.getSiege().getIsInProgress();
			}
		}
		return false;
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