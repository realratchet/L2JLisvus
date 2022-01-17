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
package net.sf.l2j.gameserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2Territory;

/**
 * coded by Balancer
 * ported to L2JRU by Mr 
 * balancer@balancer.ru
 * http://balancer.ru
 *
 * Revised by DnR
 */
public class Territory
{
	private static Logger _log = Logger.getLogger(TradeController.class.getName());
	
	private Map<Integer,L2Territory> _territory;
	
	public static Territory getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private Territory()	
	{	
		// Load all data at server start
		reload();
	}

	public int[] getRandomPoint(int terr)
	{
		return _territory.get(terr).getRandomPoint();
	}

	public int getProcMax(int terr)
	{
		return _territory.get(terr).getProcMax();
	}

	public void reload()
	{
		_territory = new HashMap<>();

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT loc_id, loc_x, loc_y, loc_zmin, loc_zmax, proc FROM locations WHERE loc_id > ?"))
		{
			statement.setInt(1, 0);
			try (ResultSet rset = statement.executeQuery())
			{
				while(rset.next())
				{
					int locationId = rset.getInt(1);
					
					L2Territory terr = _territory.get(locationId);
					if (terr == null)
					{
						_territory.put(locationId, new L2Territory(locationId));
					}
					
					_territory.get(locationId).add(rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getInt(5), rset.getInt(6));
				}
			}
		}
		catch (SQLException e)
		{
			_log.severe("Territory: An error occured while loading territory location data: " + e);
		}
	}
	
	private static class SingletonHolder
	{
		private static final Territory _instance = new Territory();
	}
}