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
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.entity.ClanHall;

public class ClanHallManager
{
	private final static Logger _log = Logger.getLogger(ClanHallManager.class.getName());
    
    public static final ClanHallManager getInstance()
    {
        return SingletonHolder._instance;
    }
    
    // =========================================================
    // Data Field
    private List<ClanHall> _clanHalls;
    
    // =========================================================
    // Constructor
    public ClanHallManager()
    {
    	_log.info("Initializing ClanHallManager");
        load();
    }
    // =========================================================
    // Method - Private
    private final void load()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM clanhall ORDER BY id");
            ResultSet rs = statement.executeQuery())
        {
            while (rs.next())
            {
                if (rs.getInt("ownerId") != 0)
                {
                    // just in case clan is deleted manually from db
                    if (ClanTable.getInstance().getClan(rs.getInt("ownerId")) == null)
                        AuctionManager.initNPC(rs.getInt("id"));
                }
            	getClanHalls().add(new ClanHall(rs.getInt("id"), rs.getString("name"), rs.getInt("ownerId"), rs.getInt("lease"), rs.getString("desc"), rs.getString("location"), rs.getLong("paidUntil"), rs.getInt("Grade"), rs.getBoolean("paid")));
            }

            _log.info("Loaded: " + getClanHalls().size() + " clan halls");
        }
        catch (Exception e)
        {
            _log.warning("Exception: ClanHallManager.load(): " + e.getMessage());
        }
    }

    // =========================================================
    // Property - Public
    public final ClanHall getClanHallById(int clanHallId)
    {
        for (ClanHall clanHall : getClanHalls())
        {
            if (clanHall.getId() == clanHallId)
                return clanHall;
        }
        return null;
    }

    public final ClanHall getNearbyClanHall(int x, int y, int maxDist)
    {
        for (ClanHall ch : getClanHalls())
        {
            if (ch.getZone() != null && ch.getZone().getDistanceToZone(x, y) < maxDist)
                return ch;
        }
        return null;
    }

    public final ClanHall getClanHallByOwner(L2Clan clan)
    {
        for (ClanHall clanHall : getClanHalls())
        {
            if (clan.getClanId() == clanHall.getOwnerId())

                return clanHall;
        }
        return null;
    }

    public final List<ClanHall> getClanHalls()
    {
        if (_clanHalls == null) _clanHalls = new ArrayList<>();
        return _clanHalls;
    }
    
    private static class SingletonHolder
	{
		protected static final ClanHallManager _instance = new ClanHallManager();
	}
}