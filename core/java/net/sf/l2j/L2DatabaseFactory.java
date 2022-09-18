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
package net.sf.l2j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zaxxer.hikari.HikariDataSource;

/**
 * This class manages the database connections.
 */
public class L2DatabaseFactory
{
    private static Logger _log = Logger.getLogger(L2DatabaseFactory.class.getName());

    private HikariDataSource _dataSource;

    // =========================================================
    // Constructor
    public L2DatabaseFactory()
    {
    	_dataSource = new HikariDataSource();
		_dataSource.setDriverClassName(Config.DATABASE_DRIVER);
		_dataSource.setJdbcUrl(Config.DATABASE_URL);
		_dataSource.setUsername(Config.DATABASE_LOGIN);
		_dataSource.setPassword(Config.DATABASE_PASSWORD);
		_dataSource.setMaximumPoolSize(Config.DATABASE_MAX_CONNECTIONS);
		_dataSource.setIdleTimeout(Config.DATABASE_MAX_IDLE_TIME);
		// A maximum life time of 10 minutes
		_dataSource.setMaxLifetime(600000);
		
        try
        {
    		// Test the connection
    		_dataSource.getConnection().close();

            if (Config.DEBUG)
                _log.fine("Database Connection working");
        }
        catch (Exception e)
        {
            if (Config.DEBUG)
                _log.fine("Database Connection failed");
            
            _log.warning(getClass().getSimpleName() + ": Could not initialize database connection. Reason: " + e.getMessage());
        }
    }

    // =========================================================
    // Method - Public

    public void shutdown()
    {
        try
        {
            _dataSource.close();
        }
        catch (Exception e)
        {
            _log.log(Level.INFO, "", e);
        }
        finally
        {
        	_dataSource = null;
        }
    }

    // =========================================================
    // Property - Public
    public static L2DatabaseFactory getInstance()
    {
       return SingletonHolder._instance;
    }

    public static final String safetyString(String... whatToCheck)
    {
        // NOTE: Use brace as a safety precaution just in case name is a reserved word
        String braceLeft = "`";
        String braceRight = "`";

        String result = "";
        for(String word : whatToCheck)
        {
            if (!result.isEmpty())
                result += ", ";
            result += braceLeft + word + braceRight;
        }
        return result;
    }
    
    /**
     * Gets the connection.
     * @return the connection
     */
    public Connection getConnection()
    {
        Connection con = null;
        while (con == null)
        {
        	try
            {
                con = _dataSource.getConnection();
            }
            catch (SQLException e)
            {
                _log.warning(getClass().getSimpleName() + ": getConnection() failed, trying again " + e);
            }
        }
        return con;
    }
    
    private static class SingletonHolder
	{
		protected static final L2DatabaseFactory _instance = new L2DatabaseFactory();
	}
}