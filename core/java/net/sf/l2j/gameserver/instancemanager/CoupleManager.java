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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Couple;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author evill33t
 *
 */
public class CoupleManager
{
	private static final Logger _log = Logger.getLogger(CoupleManager.class.getName());
	
	private final List<Couple> _couples = new CopyOnWriteArrayList<>();
	
	public static final CoupleManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private CoupleManager()
    {
    	load();
    }
	
	public final void load()
	{
		_couples.clear();
		
		if (Config.ALLOW_WEDDING)
		{
			_log.info("Wedding System: Initializing CoupleManager");
			
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				Statement ps = con.createStatement();
				ResultSet rs = ps.executeQuery("SELECT * FROM weddings ORDER BY id"))
			{
				while (rs.next())
				{
					StatsSet set = new StatsSet();
					set.set("id", rs.getInt("id"));
					set.set("player1_id", rs.getInt("player1_id"));
					set.set("player2_id", rs.getInt("player2_id"));
					set.set("married", rs.getBoolean("married"));
					set.set("affiance_date", rs.getLong("affiance_date"));
					set.set("wedding_date", rs.getLong("wedding_date"));
					getCouples().add(new Couple(set));
				}
				_log.info(getClass().getSimpleName() + ": Loaded: " + getCouples().size() + " couples(s)");
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "Exception: CoupleManager.load(): " + e.getMessage(), e);
			}
		}
	}
	
	public final Couple getCouple(int coupleId)
	{
		int index = getCoupleIndex(coupleId);
		if (index >= 0)
		{
			return getCouples().get(index);
		}
		return null;
	}
	
	public void createCouple(L2PcInstance player1, L2PcInstance player2)
	{
		if (player1 != null && player2 != null)
		{
			if (player1.getPartnerId() == 0 && player2.getPartnerId() == 0)
			{
				int player1Id = player1.getObjectId();
				int player2Id = player2.getObjectId();
				
				Couple couple = new Couple(player1, player2);
				getCouples().add(couple);
				player1.setPartnerId(player2Id);
				player2.setPartnerId(player1Id);
				player1.setCoupleId(couple.getId());
				player2.setCoupleId(couple.getId());
			}
		}
	}
	
	public void deleteCouple(int coupleId)
	{
		int index = getCoupleIndex(coupleId);
		Couple couple = getCouples().get(index);
		if (couple != null)
		{
			L2PcInstance player1 = L2World.getInstance().getPlayer(couple.getPlayer1Id());
			L2PcInstance player2 = L2World.getInstance().getPlayer(couple.getPlayer2Id());
			if (player1 != null)
			{
				player1.setPartnerId(0);
				player1.setIsMarried(false);
				player1.setCoupleId(0);
				
			}
			if (player2 != null)
			{
				player2.setPartnerId(0);
				player2.setIsMarried(false);
				player2.setCoupleId(0);
				
			}
			couple.divorce();
			getCouples().remove(index);
		}
	}
	
	public final int getCoupleIndex(int coupleId)
	{
		for (int i = 0; i < getCouples().size(); i++)
		{
			Couple temp = getCouples().get(i);
			if (temp != null && temp.getId() == coupleId)
			{
				return i;
			}
		}
		return -1;
	}
	
	public final List<Couple> getCouples()
	{
		return _couples;
	}
	
	private static class SingletonHolder
	{
		protected static final CoupleManager _instance = new CoupleManager();
	}
}