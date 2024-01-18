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
package net.sf.l2j.gameserver.model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author evill33t
 */
public class Couple
{
	private static final Logger _log = Logger.getLogger(Couple.class.getName());
	
	private final int _id;
	private final int _player1Id;
	private final int _player2Id;
	private boolean _isMarried;
	private long _affianceDate;
	private long _weddingDate;
	
	public Couple(StatsSet set)
	{
		_id = set.getInteger("id");
		_player1Id = set.getInteger("player1_id");
		_player2Id = set.getInteger("player2_id");
		_isMarried = set.getBool("married", false);
		_affianceDate = set.getLong("affiance_date", 0);
		_weddingDate = set.getLong("wedding_date", 0);
	}
	
	public void store()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO weddings (id, player1_id, player2_id, married, affiance_date, wedding_date) VALUES (?, ?, ?, ?, ?, ?)"))
		{
			statement.setInt(1, _id);
			statement.setInt(2, _player1Id);
			statement.setInt(3, _player2Id);
			statement.setBoolean(4, _isMarried);
			statement.setLong(5, _affianceDate);
			statement.setLong(6, _weddingDate);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.severe("Failed to store wedding into database: " + e);
		}
	}
	
	public void marry()
	{
		_isMarried = true;
		_weddingDate = System.currentTimeMillis();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE weddings SET married = ?, wedding_date = ? WHERE id = ?"))
		{
			statement.setBoolean(1, _isMarried);
			statement.setLong(2, _weddingDate);
			statement.setInt(3, _id);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.severe("Failed to update couple married state into database: " + e);
		}
	}
	
	public void divorce()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM weddings WHERE id = ?"))
		{
			statement.setInt(1, _id);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.severe("Exception: Couple.divorce(): " + e);
		}
	}
	
	public final int getId()
	{
		return _id;
	}
	
	public final int getPlayer1Id()
	{
		return _player1Id;
	}
	
	public final int getPlayer2Id()
	{
		return _player2Id;
	}
	
	public final boolean isMarried()
	{
		return _isMarried;
	}
	
	public final long getAffianceDate()
	{
		return _affianceDate;
	}
	
	public final long getWeddingDate()
	{
		return _weddingDate;
	}
}