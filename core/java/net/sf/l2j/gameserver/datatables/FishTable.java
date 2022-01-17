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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.FishData;

/**
 * @author -Nemesiss-
 */
public class FishTable
{
	private static Logger _log = Logger.getLogger(FishTable.class.getName());
	
	private static List<FishData> _fishes;
	private static List<FishData> _newbieFishes;
	
	public static FishTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private FishTable()
	{
		// Create table that contains all fish data
		int count = 0;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT id, level, name, hp, hpregen, fish_type, fish_group, fish_guts, guts_check_time, wait_time, combat_time FROM fish ORDER BY id");
			ResultSet Fishes = statement.executeQuery())
		{
			_fishes = new ArrayList<>();
			_newbieFishes = new ArrayList<>();
			
			FishData fish;
			
			while (Fishes.next())
			{
				int id = Fishes.getInt("id");
				int lvl = Fishes.getInt("level");
				String name = Fishes.getString("name");
				int hp = Fishes.getInt("hp");
				int hpreg = Fishes.getInt("hpregen");
				int type = Fishes.getInt("fish_type");
				int group = Fishes.getInt("fish_group");
				int fish_guts = Fishes.getInt("fish_guts");
				int guts_check_time = Fishes.getInt("guts_check_time");
				int wait_time = Fishes.getInt("wait_time");
				int combat_time = Fishes.getInt("combat_time");
				fish = new FishData(id, lvl, name, hp, hpreg, type, group, fish_guts, guts_check_time, wait_time, combat_time);
				if (fish.getGroup() == 0)
					_newbieFishes.add(fish);
				else
					_fishes.add(fish);
			}
			
			count = _newbieFishes.size() + _fishes.size();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "error while creating fishes table" + e);
		}
		
		_log.config("FishTable: Loaded " + count + " Fishes.");
	}
	
	/**
	 * @param lvl
	 * @param type
	 * @param group
	 * @return List of Fish that can be fished
	 */
	public List<FishData> getFish(int lvl, int type, int group)
	{
		List<FishData> result = new ArrayList<>();
		List<FishData> _Fishing = null;
		if (group == 0)
			_Fishing = _newbieFishes;
		else
			_Fishing = _fishes;
		
		if (_Fishing == null || _Fishing.isEmpty())
		{
			// the fish list is empty
			_log.warning("Fish are not defined!");
			return null;
		}
		
		for (FishData f : _Fishing)
		{
			if (f.getLevel() != lvl)
				continue;
			if (f.getType() != type)
				continue;
			
			result.add(f);
		}
		
		if (result.size() == 0)
			_log.warning("Cant Find Any Fish!? - Lvl: " + lvl + " Type: " + type);
		
		return result;
	}
	
	private static class SingletonHolder
	{
		protected static final FishTable _instance = new FishTable();
	}
}