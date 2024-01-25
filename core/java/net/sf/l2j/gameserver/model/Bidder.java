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

public class Bidder
{
    private final int _id;
    private final String _name;
    private final String _clanName;
    private int _bid;
    private long _bidTime;
    
    public Bidder(int id, String name, String clanName, int bid, long bidTime)
    {
        _id = id;
        _name = name;
        _clanName = clanName;
        _bid = bid;
        _bidTime = bidTime;
    }
    
    public int getId()
    {
        return _id;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public String getClanName()
    {
        return _clanName;
    }
    
    public int getBid()
    {
        return _bid;
    }
    
    public long getBidTime()
    {
        return _bidTime;
    }
    
    public void setBidTime(long bidTime)
    {
        _bidTime = bidTime;
    }
    
    public void setBid(int bid)
    {
        _bid = bid;
    }
}