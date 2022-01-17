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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;

public class L2SiegeClan
{
	// ==========================================================================================
	// Instance
	// ===============================================================
	// Data Field
	private int _clanId = 0;
	private final Set<L2NpcInstance> _flags = ConcurrentHashMap.newKeySet(1);
	private SiegeClanType _type;
	
	public enum SiegeClanType
	{
		OWNER(-1),
		DEFENDER(0),
		ATTACKER(1),
		DEFENDER_PENDING(2);
		
		private final int _id;
		
		private SiegeClanType(int id)
		{
			_id = id;
		}
		
		public int getId()
		{
			return _id;
		}
	}
	
	// =========================================================
	// Constructor
	
	public L2SiegeClan(int clanId, SiegeClanType type)
	{
		_clanId = clanId;
		_type = type;
	}
	
	// =========================================================
	// Method - Public
	public int getNumFlags()
	{
		return _flags.size();
	}
	
	public void addFlag(L2NpcInstance flag)
	{
		_flags.add(flag);
	}
	
	public boolean removeFlag(L2NpcInstance flag)
	{
		if (flag == null)
			return false;
		
		boolean ret = _flags.remove(flag);
		flag.deleteMe();
		return ret;
	}
	
	public void removeFlags()
	{
		for (L2NpcInstance flag : getFlags())
		{
			removeFlag(flag);
		}
	}
	
	// =========================================================
	// Property
	public final int getClanId()
	{
		return _clanId;
	}
	
	public final Set<L2NpcInstance> getFlags()
	{
		return _flags;
	}
	
	public SiegeClanType getType()
	{
		return _type;
	}
	
	public void setType(SiegeClanType setType)
	{
		_type = setType;
	}
}