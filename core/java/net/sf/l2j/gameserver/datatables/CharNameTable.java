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
package net.sf.l2j.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.3.2.2.2.1 $ $Date: 2005/03/27 15:29:18 $
 */
public class CharNameTable
{
	private static Logger _log = Logger.getLogger(CharNameTable.class.getName());
	
	private final Map<Integer, String> _charNames = new ConcurrentHashMap<>();
	
	public static CharNameTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected CharNameTable()
	{
		if (Config.CACHE_CHAR_NAMES)
		{
			loadAll();
		}
	}
	
	private void loadAll()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT obj_Id, char_name FROM characters");
			ResultSet rs = statement.executeQuery())
		{
			while (rs.next())
			{
				final int id = rs.getInt(1);
				_charNames.put(id, rs.getString(2));
			}
		}
		catch (SQLException e)
		{
			_log.warning(getClass().getSimpleName() + ": Could not load character name: " + e.getMessage() + e);
		}
		_log.info(getClass().getSimpleName() + ": Loaded " + _charNames.size() + " character names.");
	}
	
	public final void addName(L2PcInstance player)
	{
		if (player != null)
		{
			addName(player.getObjectId(), player.getName());
		}
	}
	
	private final void addName(int objectId, String name)
	{
		if (name != null)
		{
			if (!name.equals(_charNames.get(objectId)))
			{
				_charNames.put(objectId, name);
			}
		}
	}
	
	public final void removeName(int objId)
	{
		_charNames.remove(objId);
	}
	
	public final int getIdByName(String name)
	{
		if ((name == null) || name.isEmpty())
		{
			return -1;
		}
		
		for (Entry<Integer, String> entry : _charNames.entrySet())
		{
			if (entry.getValue().equalsIgnoreCase(name))
			{
				return entry.getKey();
			}
		}
		
		if (Config.CACHE_CHAR_NAMES)
		{
			return -1;
		}
		
		int id = -1;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT obj_Id FROM characters WHERE char_name=?"))
		{
			statement.setString(1, name);
			try (ResultSet rs = statement.executeQuery())
			{
				while (rs.next())
				{
					id = rs.getInt(1);
				}
			}
		}
		catch (SQLException e)
		{
			_log.warning(getClass().getSimpleName() + ": Could not check existing character name: " + e.getMessage() + e);
		}
		
		if (id > 0)
		{
			_charNames.put(id, name);
			return id;
		}
		
		return -1; // Not found
	}
	
	public final String getNameById(int id)
	{
		if (id <= 0)
		{
			return null;
		}
		
		String name = _charNames.get(id);
		if (name != null)
		{
			return name;
		}
		
		if (Config.CACHE_CHAR_NAMES)
		{
			return null;
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT char_name FROM characters WHERE obj_Id=?"))
		{
			statement.setInt(1, id);
			try (ResultSet rset = statement.executeQuery())
			{
				if (rset.next())
				{
					name = rset.getString(1);
					_charNames.put(id, name);
					return name;
				}
			}
		}
		catch (SQLException e)
		{
			_log.warning(getClass().getSimpleName() + ": Could not check existing character id: " + e.getMessage() + e);
		}
		
		return null; // Not found
	}
	
	public boolean doesCharNameExist(String name)
	{
		boolean result = true;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?"))
		{
            statement.setString(1, name);
	        try (ResultSet rset = statement.executeQuery())
	        {
	        	result = rset.next();
            }
		}
		catch (SQLException e)
		{
			_log.warning("could not check existing character name:"+e.getMessage());
		}
		return result;
	}
    
    public int getAccountCharacterCount(String account)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT COUNT(char_name) FROM characters WHERE account_name=?"))
        {
            statement.setString(1, account);
            try (ResultSet rset = statement.executeQuery())
            {
                while(rset.next())
                {
                    return rset.getInt(1);
                }
            }
        }
        catch (SQLException e)
        {
            _log.warning("could not check existing character number:"+e.getMessage());
        }

        return 0;
    }
    
    private static class SingletonHolder
	{
		protected static final CharNameTable _instance = new CharNameTable();
	}
}