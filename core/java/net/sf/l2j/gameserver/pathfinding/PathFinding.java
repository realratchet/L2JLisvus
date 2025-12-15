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
package net.sf.l2j.gameserver.pathfinding;

import java.util.List;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.pathfinding.cellnodes.CellPathFinding;
import net.sf.l2j.gameserver.pathfinding.geonodes.GeoPathFinding;

/**
 * @author -Nemesiss-
 */
public abstract class PathFinding
{
	public static PathFinding getInstance()
	{
		if (Config.PATHFINDING == 1)
		{
			// Higher Memory Usage, Smaller Cpu Usage
			return GeoPathFinding.getInstance();
		}
		return CellPathFinding.getInstance();
	}
	
	public abstract boolean pathNodesExist(short regionoffset);
	
	public abstract List<AbstractNodeLoc> findPath(int x, int y, int z, int tx, int ty, int tz, boolean playable);
	
	/**
	 * Convert geodata position to pathnode position
	 * @param geoPos
	 * @return pathnode position
	 */
	public short getNodePos(int geoPos)
	{
		return (short) (geoPos >> 3); // OK?
	}
	
	/**
	 * Convert node position to pathnode block position
	 * @param nodePos
	 * @return pathnode block position (0...255)
	 */
	public short getNodeBlock(int nodePos)
	{
		return (short) (nodePos % 256);
	}
	
	public byte getRegionX(int nodePos)
	{
		return (byte) ((nodePos >> 8) + 16);
	}
	
	public byte getRegionY(int nodePos)
	{
		return (byte) ((nodePos >> 8) + 10);
	}
	
	public short getRegionOffset(byte rx, byte ry)
	{
		return (short) ((rx << 5) + ry);
	}
	
	/**
	 * Convert pathnode x to World x position
	 * @param nodeX
	 * @return
	 */
	public int calculateWorldX(short nodeX)
	{
		return L2World.MAP_MIN_X + (nodeX * 128) + 48;
	}
	
	/**
	 * Convert pathnode y to World y position
	 * @param nodeY
	 * @return
	 */
	public int calculateWorldY(short nodeY)
	{
		return L2World.MAP_MIN_Y + (nodeY * 128) + 48;
	}
	
	public String[] getStat()
	{
		return null;
	}
}