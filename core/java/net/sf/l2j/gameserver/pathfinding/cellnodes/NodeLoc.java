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
package net.sf.l2j.gameserver.pathfinding.cellnodes;

import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.geoengine.geodriver.Cell;
import net.sf.l2j.gameserver.pathfinding.AbstractNodeLoc;

/**
 *
 * @author -Nemesiss-, FBIagent
 */
public class NodeLoc extends AbstractNodeLoc
{
	private int _x;
	private int _y;
	private boolean _goNorth;
    private boolean _goEast;
    private boolean _goSouth;
    private boolean _goWest;
    private int _geoHeight;

	public NodeLoc(int x, int y, int z)
	{
		set(x, y, z);
	}

    public void set(int x, int y, int z)
    {
        _x = x;
        _y = y;
        _goNorth = GeoData.getInstance().checkNearestNswe(x, y, z, Cell.NSWE_NORTH);
        _goEast = GeoData.getInstance().checkNearestNswe(x, y, z, Cell.NSWE_EAST);
        _goSouth = GeoData.getInstance().checkNearestNswe(x, y, z, Cell.NSWE_SOUTH);
        _goWest = GeoData.getInstance().checkNearestNswe(x, y, z, Cell.NSWE_WEST);
        _geoHeight = GeoData.getInstance().getNearestZ(x, y, z);
    }

    public boolean canGoNorth()
    {
        return _goNorth;
    }

    public boolean canGoEast()
    {
        return _goEast;
    }

    public boolean canGoSouth()
    {
        return _goSouth;
    }

    public boolean canGoWest()
    {
        return _goWest;
    }

    public boolean canGoNone()
    {
        return !canGoNorth() && !canGoEast() && !canGoSouth() && !canGoWest();
    }

    public boolean canGoAll()
    {
        return canGoNorth() && canGoEast() && canGoSouth() && canGoWest();
    }

	/**
	 * @see net.sf.l2j.gameserver.pathfinding.AbstractNodeLoc#getX()
	 */
	@Override
	public int getX()
	{
		return GeoData.getInstance().getWorldX(_x);
	}

	/**
	 * @see net.sf.l2j.gameserver.pathfinding.AbstractNodeLoc#getY()
	 */
	@Override
	public int getY()
	{
		return GeoData.getInstance().getWorldY(_y);
	}

	/**
	 * @see net.sf.l2j.gameserver.pathfinding.AbstractNodeLoc#getZ()
	 */
	@Override
	public int getZ()
	{
		return _geoHeight;
	}

	@Override
	public void setZ(short z)
	{
		// do nothing here
	}
	
	/**
	 * @see net.sf.l2j.gameserver.pathfinding.AbstractNodeLoc#getNodeX()
	 */
	@Override
	public int getNodeX()
	{
		return _x;
	}

	/**
	 * @see net.sf.l2j.gameserver.pathfinding.AbstractNodeLoc#getNodeY()
	 */
	@Override
	public int getNodeY()
	{
		return _y;
	}

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + _x;
        result = prime * result + _y;

        int nswe = 0;
        if (canGoNorth())
            nswe |= Cell.NSWE_NORTH;
        if (canGoEast())
            nswe |= Cell.NSWE_EAST;
        if (canGoSouth())
            nswe |= Cell.NSWE_SOUTH;
        if (canGoEast())
            nswe |= Cell.NSWE_EAST;

        result = (prime * result) + (((_geoHeight & 0xFFFF) << 1) | nswe);
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
	        return false;
        if (!(obj instanceof NodeLoc))
	        return false;
        final NodeLoc other = (NodeLoc) obj;
        if (_x != other._x)
	        return false;
        if (_y != other._y)
	        return false;
        if (_goNorth != other._goNorth)
            return false;
        if (_goEast != other._goEast)
            return false;
        if (_goSouth != other._goSouth)
            return false;
        if (_goWest != other._goWest)
            return false;
        if (_geoHeight != other._geoHeight)
	        return false;
        return true;
    }
}