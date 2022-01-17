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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2Skill;

public class SkillSpellbookTable
{
	private static Logger _log = Logger.getLogger(SkillSpellbookTable.class.getName());
	
	private static Map<Integer, Integer> _skillSpellbooks;
	
	public static SkillSpellbookTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private SkillSpellbookTable()
	{
		_skillSpellbooks = new HashMap<>();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT skill_id, item_id FROM skill_spellbooks");
			ResultSet spbooks = statement.executeQuery())
		{
			while (spbooks.next())
				_skillSpellbooks.put(spbooks.getInt("skill_id"), spbooks.getInt("item_id"));
			
			_log.config("SkillSpellbookTable: Loaded " + _skillSpellbooks.size() + " Spellbooks.");
		}
		catch (Exception e)
		{
			_log.warning("Error while loading spellbook data: " + e);
		}
	}
	
	public int getBookForSkill(int skillId)
	{
		if (!_skillSpellbooks.containsKey(skillId))
			return -1;
		
		return _skillSpellbooks.get(skillId);
	}
	
	public int getBookForSkill(L2Skill skill)
	{
		return getBookForSkill(skill.getId());
	}
	
	private static class SingletonHolder
	{
		protected static final SkillSpellbookTable _instance = new SkillSpellbookTable();
	}
}