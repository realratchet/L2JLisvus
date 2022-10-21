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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;

public class BufferTable
{
	private static Logger _log = Logger.getLogger(BufferTable.class.getName());

	private Map<Integer, BuffInfo> _aioBuffs = new ConcurrentHashMap<>();
    private Map<Integer, BuffInfo> _npcBuffs = new ConcurrentHashMap<>();
    
    public static BufferTable getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private BufferTable()
    {
    	load();
    }

    public final void load()
    {
    	// AIO buffs
        _aioBuffs.clear();

        // Load AIO buffs if enabled
        if (Config.AIO_BUFFER_ENABLED)
        {
	        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
	            PreparedStatement statement = con.prepareStatement("SELECT skill_id, skill_level, duration FROM aio_buffer");
	            ResultSet rset = statement.executeQuery())
	        {
	            while (rset.next())
	            {
	
	                int skillId = rset.getInt("skill_id");
	                int skillLevel = rset.getInt("skill_level");
	                int duration = rset.getInt("duration");

	                _aioBuffs.put(skillId, new BuffInfo(skillId, skillLevel, duration, 0, 0));
	            }
	            
	            _log.info("BufferTable: Loaded " + _aioBuffs.size() + " AIO buffs.");
	        }
	        catch (Exception e)
	        {
	            _log.warning("BufferTable: Error reading aio_buffer table: " + e);
	        }
        }

        // Reset NPC buffs
        _npcBuffs.clear();

        // Load NPC buffs if enabled
        if (Config.NPC_BUFFER_ENABLED)
        {
	        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
	            PreparedStatement statement = con.prepareStatement("SELECT skill_id, skill_level, duration, skill_fee_id, skill_fee_amount FROM npc_buffer");
	            ResultSet rset = statement.executeQuery())
	        {
	            while (rset.next())
	            {
	
	                int skillId = rset.getInt("skill_id");
	                int skillLevel = rset.getInt("skill_level");
	                int duration = rset.getInt("duration");
	                int skillFeeId = rset.getInt("skill_fee_id");
	                int skillFeeAmount = rset.getInt("skill_fee_amount");

	                _npcBuffs.put(skillId, new BuffInfo(skillId, skillLevel, duration, skillFeeId, skillFeeAmount));
	            }
	            
	            _log.info("BufferTable: Loaded " + _npcBuffs.size() + " NPC buffs.");
	        }
	        catch (Exception e)
	        {
	            _log.warning("BufferTable: Error reading npc_buffer table: " + e);
	        }
        }
    }
    
    public Map<Integer, BuffInfo> getAIOBuffs()
    {
    	return _aioBuffs;
    }
    
    public Map<Integer, BuffInfo> getNPCBuffs()
    {
    	return _npcBuffs;
    }
    
    public class BuffInfo
    {
    	private final int _skillId, _skillLevel, _duration, _skillFeeId, _skillFeeAmount;

    	BuffInfo(int skillId, int skillLevel, int duration, int skillFeeId, int skillFeeAmount)
    	{
    		_skillId = skillId;
    		_skillLevel = skillLevel;
    		_duration = duration;
    		_skillFeeId = skillFeeId;
    		_skillFeeAmount = skillFeeAmount;
    	}
    	
    	public int getSkillId()
    	{
    		return _skillId;
    	}
    	
    	public int getSkillLevel()
    	{
    		return _skillLevel;
    	}
    	
    	public int getDuration()
    	{
    		return _duration;
    	}
    	
    	public int getSkillFeeId()
    	{
    		return _skillFeeId;
    	}
    	
    	public int getSkillFeeAmount()
    	{
    		return _skillFeeAmount;
    	}
    }
    
    private static class SingletonHolder
	{
		protected static final BufferTable _instance = new BufferTable();
	}
}