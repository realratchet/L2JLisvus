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
import java.util.Calendar;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author evill33t
 *
 */
public class Couple
{
	private static final Logger _log = Logger.getLogger(Couple.class.getName());
	
	// =========================================================
	// Data Field
	private int _id = 0;
	private int _player1Id = 0;
	private int _player2Id = 0;
	private boolean _isMarried = false;
	private Calendar _affianceDate;
	private Calendar _weddingDate;
	
	// =========================================================
	// Constructor
	public Couple(StatsSet set)
	{
		_id = set.getInteger("id");
		_player1Id = set.getInteger("player1_id");
		_player2Id = set.getInteger("player2_id");
		_isMarried = set.getBool("married");
		_affianceDate = Calendar.getInstance();
		_affianceDate.setTimeInMillis(set.getLong("affiance_date"));
		
		_weddingDate = Calendar.getInstance();
		if (set.getLong("wedding_date") > 0)
		{
			_weddingDate.setTimeInMillis(set.getLong("wedding_date"));
		}
	}
	
	public Couple(L2PcInstance player1, L2PcInstance player2)
	{
		int _tempPlayer1Id = player1.getObjectId();
		int _tempPlayer2Id = player2.getObjectId();
		
		_player1Id = _tempPlayer1Id;
		_player2Id = _tempPlayer2Id;
		
		_affianceDate = Calendar.getInstance();
		_affianceDate.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
		
		_weddingDate = Calendar.getInstance();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO weddings (id, player1_id, player2_id, married, affiance_date, wedding_date) VALUES (?, ?, ?, ?, ?, ?)"))
		{
			_id = IdFactory.getInstance().getNextId();
			statement.setInt(1, _id);
			statement.setInt(2, _player1Id);
			statement.setInt(3, _player2Id);
			statement.setBoolean(4, false);
			statement.setLong(5, _affianceDate.getTimeInMillis());
			statement.setLong(6, 0);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.severe(e.toString());
		}
	}
	
	public void marry()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE weddings SET married = ?, wedding_date = ? WHERE id = ?"))
		{
			statement.setBoolean(1, true);
			_weddingDate = Calendar.getInstance();
			statement.setLong(2, _weddingDate.getTimeInMillis());
			statement.setInt(3, _id);
			statement.execute();
			_isMarried = true;
		}
		catch (Exception e)
		{
			_log.severe(e.toString());
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
	
	public final Calendar getAffianceDate()
	{
		return _affianceDate;
	}
	
	public final Calendar getWeddingDate()
	{
		return _weddingDate;
	}
}