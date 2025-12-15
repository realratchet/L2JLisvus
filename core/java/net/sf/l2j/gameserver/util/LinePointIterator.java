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

public abstract class LinePointIterator
{
    public static final int MAX_ITERATIONS = 2000; // Anti-freeze limit
    
    /**
     * Checks if there are more points to iterate.
     * @return true if the iterator has not reached the destination, false otherwise
     */
    public abstract boolean hasNext();
    
    /**
     * Advances to the next point in the line. Returns true if a valid point is available.
     * Includes an iteration limit to prevent freezes in large maps.
     * @return true if there is a next point, false if at destination or iteration limit reached
     */
    public abstract boolean next();
    
    /**
     * Returns the current X coordinate.
     * @return the current X coordinate
     */
    public abstract int x();
    
    /**
     * Returns the current Y coordinate.
     * @return the current Y coordinate
     */
    public abstract int y();
}
