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
package net.sf.l2j.gameserver.model;

/**
 * This class ...
 * 
 * @version $Revision: 1.1.4.1 $ $Date: 2005/03/27 15:29:33 $
 */
public final class Location implements IPositionable
{
	private int _x;
	private int _y;
	private int _z;
	private int _heading;

	public Location(int x, int y, int z)
	{
		_x = x;
		_y = y;
		_z = z;
	}
	
	public Location(int x, int y, int z, int heading)
	{
		_x = x;
		_y = y;
		_z = z;
		_heading = heading;
	}
	
	@Override
	public int getX()
	{
		return _x;
	}

	@Override
    public final void setX(int x)
    {
        _x = x;
    }
	
	@Override
	public int getY()
	{
		return _y;
	}

	@Override
    public final void setY(int y)
    {
        _y = y;
    }
	
	@Override
	public int getZ()
	{
		return _z;
	}

	@Override
    public final void setZ(int z)
    {
        _z = z;
    }
	
	@Override
	public int getHeading()
	{
		return _heading;
	}

	@Override
	public final void setHeading(int heading)
	{
		_heading = heading;
	}

	@Override
	public void setXYZ(int x, int y, int z)
	{
		_x = x;
		_y = y;
		_z = z;
	}

	@Override
	public String toString()
	{
		return "[" + getClass().getSimpleName() + "] X: " + _x + " Y: " + _y + " Z: " + _z + " Heading: " + _heading;
	}
}