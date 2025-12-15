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

/**
 * A 2D Line Point Iterator implementing Bresenham's algorithm for geodata grid traversal.
 * Iterates from source (srcX, srcY) to destination (dstX, dstY) in a straight line.
 * Used for movement and line-of-sight checks in the game world.
 * @author FBIagent, Kallone
 */
public final class LinePointIterator2D extends LinePointIterator
{
    private int _srcX;
    private int _srcY;
    private final int _dstX;
    private final int _dstY;
    
    private final long _dx;
    private final long _dy;
    private final long _sx;
    private final long _sy;
    private long _error;
    
    private boolean _first;
    private int _iterations;
    
    /**
     * Constructs a LinePointIterator from source to destination coordinates.
     * @param srcX the source X coordinate
     * @param srcY the source Y coordinate
     * @param dstX the destination X coordinate
     * @param dstY the destination Y coordinate
     */
    public LinePointIterator2D(int srcX, int srcY, int dstX, int dstY)
    {
        _srcX = srcX;
        _srcY = srcY;
        _dstX = dstX;
        _dstY = dstY;
        _dx = Math.abs((long) dstX - srcX);
        _dy = Math.abs((long) dstY - srcY);
        _sx = srcX < dstX ? 1 : -1;
        _sy = srcY < dstY ? 1 : -1;
        
        if (_dx >= _dy)
            _error = _dx / 2;
        else
            _error = _dy / 2;
        
        _first = true;
        _iterations = 0;
    }
    
    @Override
    public boolean hasNext()
    {
        if (_iterations >= MAX_ITERATIONS)
            return false;
        return _first || _srcX != _dstX || _srcY != _dstY;
    }
    
    @Override
    public boolean next()
    {
        if (_iterations >= MAX_ITERATIONS)
        {
            return false;
        }
        
        _iterations++;
        
        if (_first)
        {
            _first = false;
            return true;
        }
        else if (_dx >= _dy)
        {
            if (_srcX != _dstX)
            {
                _srcX += _sx;
                
                _error += _dy;
                if (_error >= _dx)
                {
                    _srcY += _sy;
                    _error -= _dx;
                }
                
                return true;
            }
        }
        else
        {
            if (_srcY != _dstY)
            {
                _srcY += _sy;
                
                _error += _dx;
                if (_error >= _dy)
                {
                    _srcX += _sx;
                    _error -= _dy;
                }
                
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int x()
    {
        return _srcX;
    }
    
    @Override
    public int y()
    {
        return _srcY;
    }
}