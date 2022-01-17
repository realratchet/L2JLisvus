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
package net.sf.l2j.gameserver.util;

import net.sf.l2j.gameserver.geoengine.geodriver.Cell;

/**
 * @author FBIagent
 */
public final class GeoUtils
{
	/**
	 * Difference between x values: never above 1.
	 * Difference between y values: never above 1.
	 * 
	 * @param lastX
	 * @param lastY
	 * @param x
	 * @param y
	 * @return
	 */
	public static int computeNswe(int lastX, int lastY, int x, int y)
	{
		if (x > lastX) // east
		{
			if (y > lastY)
			{
				return Cell.NSWE_SOUTH_EAST;
			}
			else if (y < lastY)
			{
				return Cell.NSWE_NORTH_EAST;
			}
			else
			{
				return Cell.NSWE_EAST;
			}
		}
		else if (x < lastX) // west
		{
			if (y > lastY)
			{
				return Cell.NSWE_SOUTH_WEST;
			}
			else if (y < lastY)
			{
				return Cell.NSWE_NORTH_WEST;
			}
			else
			{
				return Cell.NSWE_WEST;
			}
		}
		else
		// unchanged x
		{
			if (y > lastY)
			{
				return Cell.NSWE_SOUTH;
			}
			else if (y < lastY)
			{
				return Cell.NSWE_NORTH;
			}
			else
			{
				throw new RuntimeException();
			}
		}
	}
}