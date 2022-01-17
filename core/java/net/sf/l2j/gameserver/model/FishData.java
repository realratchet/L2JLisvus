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

public class FishData
{
	private final int _id;
	private final int _level;
	private final String _name;
	private final int _hP;
	private final int _hpRegen;
	private final int _type;
    private final int _group;
    private final int _fish_guts;
    private final int _guts_check_time;
    private final int _wait_time;
    private final int _combat_time;

	public FishData(int id, int lvl, String name, int hP, int hpRegen, int type, int group, int fish_guts, int guts_check_time, int wait_time, int combat_time)
	{
		_id = id;
		_level = lvl;
		_name = name.intern();
		_hP = hP;
		_hpRegen = hpRegen;
		_type = type;
        _group = group;
        _fish_guts = fish_guts;
        _guts_check_time = guts_check_time;
        _wait_time = wait_time;
        _combat_time = combat_time;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId()
	{
	        return _id;
	}

	/**
	 * @return Returns the level.
	 */
	public int getLevel()
	{
		return _level;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return _name;
	}

	public int getHP()
	{
		return _hP;
	}

	public int getHpRegen()
	{
		return _hpRegen;
	}

	public int getType()
	{
		return _type;
	}

	public int getGroup()
	{
		return _group;
	}

	public int getFishGuts()
	{
		return _fish_guts;
	}

	public int getGutsCheckTime()
	{
		return _guts_check_time;
	}

	public int getWaitTime()
	{
		return _wait_time;
	}

	public int getCombatTime()
	{
		return _combat_time;
	}
}