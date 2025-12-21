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
package net.sf.l2j.gameserver.model.actor.vehicle;

import net.sf.l2j.gameserver.model.Location;

public class L2VehiclePathPoint extends Location
{
    private final int _speed1;
    private final int _speed2;

    public L2VehiclePathPoint(int speed1, int speed2, int x, int y, int z)
    {
        super(x, y, z);
        _speed1 = speed1;
        _speed2 = speed2;
    }
    
    public int getMoveSpeed()
    {
        return _speed1;
    }
    
    public int getRotationSpeed()
    {
        return _speed2;
    }
}