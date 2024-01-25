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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.entity.Auction;
import net.sf.l2j.gameserver.model.entity.ClanHall;

public class ClanHallManager
{
    private final static Logger _log = Logger.getLogger(ClanHallManager.class.getName());
    
    public static final ClanHallManager getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private final List<ClanHall> _clanHalls = new ArrayList<>();
    
    public ClanHallManager()
    {
        _log.info("Initializing ClanHallManager");
        load();
    }
    
    private final void load()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM clanhall ORDER BY id");
            ResultSet rs = statement.executeQuery())
        {
            while (rs.next())
            {
                _clanHalls.add(new ClanHall(rs.getInt("id"), rs.getString("name"), rs.getInt("ownerId"), rs.getInt("lease"), rs.getInt("defaultBid"), rs.getString("desc"), rs.getString("location"), rs.getLong("paidUntil"), rs.getInt("Grade"), rs.getBoolean("paid")));
            }
            
            _log.info("Loaded: " + _clanHalls.size() + " clan halls");
        }
        catch (Exception e)
        {
            _log.warning("Exception: ClanHallManager.load(): " + e.getMessage());
        }
    }
    
    public final ClanHall getClanHallById(int clanHallId)
    {
        for (ClanHall clanHall : _clanHalls)
        {
            if (clanHall.getId() == clanHallId)
                return clanHall;
        }
        return null;
    }
    
    public final ClanHall getNearbyClanHall(int x, int y, int maxDist)
    {
        for (ClanHall ch : _clanHalls)
        {
            if (ch.getZone() != null && ch.getZone().getDistanceToZone(x, y) < maxDist)
                return ch;
        }
        return null;
    }
    
    public final ClanHall getClanHallByOwner(L2Clan clan)
    {
        for (ClanHall clanHall : _clanHalls)
        {
            if (clan.getClanId() == clanHall.getOwnerId())
                
                return clanHall;
        }
        return null;
    }
    
    public final List<ClanHall> getRentableClanHalls()
    {
        final List<ClanHall> list = new ArrayList<>();
        for (ClanHall ch : _clanHalls)
        {
            Auction auction = ch.getAuction();
            if (auction == null)
            {
                continue;
            }
            
            list.add(ch);
        }
        return list;
    }
    
    public final List<ClanHall> getClanHalls()
    {
        return _clanHalls;
    }
    
    private static class SingletonHolder
    {
        protected static final ClanHallManager _instance = new ClanHallManager();
    }
}