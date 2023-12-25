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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExAutoSoulShot;
import net.sf.l2j.gameserver.templates.L2EtcItemType;

/**
 * This class ...
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:33 $
 */
public class ShortCuts
{
	private static Logger _log = Logger.getLogger(ShortCuts.class.getName());
	
	private final L2PcInstance _owner;
	private final Map<Integer, L2ShortCut> _shortCuts = new TreeMap<>();

	// In C4 and older clients, shortcut register and delete client packets run concurrently during shortcut swap
	// Using synchronized methods adds a small delay to method calls resulting in execution order loss
	// ReentrantLock seems to do the job really well and prevents concurrent execution
	private final ReentrantLock _lock = new ReentrantLock();
	
	public ShortCuts(L2PcInstance owner)
	{
		_owner = owner;
	}
	
	public L2ShortCut[] getAllShortCuts()
	{
		return _shortCuts.values().toArray(new L2ShortCut[_shortCuts.values().size()]);
	}
	
	public L2ShortCut getShortCut(int slot, int page)
	{
		L2ShortCut sc = _shortCuts.get(slot + (page * 12));
		// Verify shortcut
		if (sc != null && sc.getType() == L2ShortCut.TYPE_ITEM)
		{
			if (_owner.getInventory().getItemByObjectId(sc.getId()) == null)
			{
				deleteShortCut(sc.getSlot(), sc.getPage());
				sc = null;
			}
			
		}
		return sc;
	}
	
	public void registerShortCut(L2ShortCut shortCut)
	{
		_lock.lock();
		
		try
		{
			_shortCuts.put(shortCut.getSlot() + (12 * shortCut.getPage()), shortCut);
			registerShortCutIntoDb(shortCut);
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	private void registerShortCutIntoDb(L2ShortCut shortCut)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("REPLACE INTO character_shortcuts (char_obj_id,slot,page,type,shortcut_id,level,class_index) values(?,?,?,?,?,?,?)"))
		{
			statement.setInt(1, _owner.getObjectId());
			statement.setInt(2, shortCut.getSlot());
			statement.setInt(3, shortCut.getPage());
			statement.setInt(4, shortCut.getType());
			statement.setInt(5, shortCut.getId());
			statement.setInt(6, shortCut.getLevel());
			statement.setInt(7, _owner.getClassIndex());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("Could not store character shortcut: " + e);
		}
	}
	
	/**
	 * @param slot
	 * @param page
	 */
	public void deleteShortCut(int slot, int page)
	{
		_lock.lock();
		
		try
		{
			L2ShortCut old = _shortCuts.remove(slot + (page * 12));
			if (old == null || _owner == null)
			{
				return;
			}
			
			deleteShortCutFromDb(old);
			
			if (old.getType() == L2ShortCut.TYPE_ITEM)
			{
				L2ItemInstance item = _owner.getInventory().getItemByObjectId(old.getId());
				if (item != null && item.getItemType() == L2EtcItemType.SHOT)
				{
					_owner.removeAutoSoulShot(item.getItemId());
					_owner.sendPacket(new ExAutoSoulShot(item.getItemId(), 0));
				}
			}
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public int deleteShortCutsByItem(L2ItemInstance item)
	{
		_lock.lock();
		
		int count = 0;
		
		try
		{
			for (L2ShortCut shortCut : getAllShortCuts())
			{
				if (shortCut == null)
				{
					continue;
				}
				
				if (shortCut.getType() == L2ShortCut.TYPE_ITEM && shortCut.getId() == item.getObjectId())
				{
					L2ShortCut toRemove = _shortCuts.remove(shortCut.getSlot() + (shortCut.getPage() * 12));
					if (toRemove != null)
					{
						count++;
					}
				}
			}
			
			// Now, remove them all from database at once.
			if (_owner != null)
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE char_obj_id=? AND shortcut_id=? AND class_index=?"))
				{
					statement.setInt(1, _owner.getObjectId());
					statement.setInt(2, item.getObjectId());
					statement.setInt(3, _owner.getClassIndex());
					statement.execute();
				}
				catch (Exception e)
				{
					_log.warning("Could not delete character shortcuts: " + e);
				}
				
				if (item.getItemType() == L2EtcItemType.SHOT)
				{
					_owner.removeAutoSoulShot(item.getItemId());
					_owner.sendPacket(new ExAutoSoulShot(item.getItemId(), 0));
				}
			}
		}
		finally
		{
			_lock.unlock();
		}
		
		return count;
	}
	
	/**
	 * @param shortCut
	 */
	private void deleteShortCutFromDb(L2ShortCut shortCut)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE char_obj_id=? AND slot=? AND page=? AND type=? AND shortcut_id=? AND level=? AND class_index=?"))
		{
			statement.setInt(1, _owner.getObjectId());
			statement.setInt(2, shortCut.getSlot());
			statement.setInt(3, shortCut.getPage());
			statement.setInt(4, shortCut.getType());
			statement.setInt(5, shortCut.getId());
			statement.setInt(6, shortCut.getLevel());
			statement.setInt(7, _owner.getClassIndex());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("Could not delete character shortcut: " + e);
		}
	}
	
	public void restore()
	{
		_shortCuts.clear();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT char_obj_id, slot, page, type, shortcut_id, level FROM character_shortcuts WHERE char_obj_id=? AND class_index=?"))
		{
			statement.setInt(1, _owner.getObjectId());
			statement.setInt(2, _owner.getClassIndex());
			
			try (ResultSet rset = statement.executeQuery())
			
			{
				while (rset.next())
				{
					int slot = rset.getInt("slot");
					int page = rset.getInt("page");
					int type = rset.getInt("type");
					int id = rset.getInt("shortcut_id");
					int level = rset.getInt("level");
					
					if (level > -1)
					{
						// Get current skill level if already loaded
						final int temp = _owner.getSkillLevel(id);
						if (temp > -1)
						{
							level = temp;
						}
					}
					
					L2ShortCut sc = new L2ShortCut(slot, page, type, id, level, 1);
					_shortCuts.put(slot + (page * 12), sc);
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Could not restore character shortcuts: " + e);
		}
		
		// Verify shortcuts
		for (L2ShortCut sc : getAllShortCuts())
		{
			if (sc == null)
			{
				continue;
			}
			
			if (sc.getType() == L2ShortCut.TYPE_ITEM)
			{
				if (_owner.getInventory().getItemByObjectId(sc.getId()) == null)
				{
					deleteShortCut(sc.getSlot(), sc.getPage());
				}
			}
		}
	}
}