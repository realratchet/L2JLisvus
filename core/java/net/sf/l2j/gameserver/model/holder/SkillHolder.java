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
package net.sf.l2j.gameserver.model.holder;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;

/**
 * Simple class for storing skill id/level.
 * @author BiggBoss
 */
public class SkillHolder implements IIdentifiable
{
    private final int _id;
    private final int _level;
    
    public SkillHolder(int id)
    {
        _id = id;
        _level = 1;
    }
    
    public SkillHolder(int skillId, int skillLvl)
    {
        _id = skillId;
        _level = skillLvl;
    }
    
    public SkillHolder(L2Skill skill)
    {
        _id = skill.getId();
        _level = skill.getLevel();
    }
    
    @Override
    public final int getId()
    {
        return _id;
    }
    
    public final int getLevel()
    {
        return _level;
    }
    
    public final L2Skill getSkill()
    {
        return SkillTable.getInstance().getInfo(_id, Math.max(_level, 1));
    }
    
    public final L2Skill getSkill(int levelOverride)
    {
        return SkillTable.getInstance().getInfo(_id, Math.max(levelOverride, 1));
    }
    
    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + "] ID: " + _id + ", level: " + _level;
    }
}