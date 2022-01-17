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
package net.sf.l2j.gameserver.model.actor.knownlist;

import net.sf.l2j.gameserver.ai.CtrlEvent;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.ai.L2CharacterAI;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class MonsterKnownList extends AttackableKnownList
{
	// =========================================================
	// Data Field
	
	// =========================================================
	// Constructor
	public MonsterKnownList(L2MonsterInstance activeChar)
	{
		super(activeChar);
	}
	
	// =========================================================
	// Method - Public
	@Override
	public boolean addKnownObject(L2Object object)
	{
		return addKnownObject(object, null);
	}
	
	@Override
	public boolean addKnownObject(L2Object object, L2Character dropper)
	{
		if (!super.addKnownObject(object, dropper))
		{
			return false;
		}
		
		final L2CharacterAI ai = getActiveChar().getAI(); // force AI creation

		// Set the L2MonsterInstance Intention to AI_INTENTION_ACTIVE if the state was AI_INTENTION_IDLE
		if ((object instanceof L2PcInstance) && (ai != null) && (ai.getIntention() == CtrlIntention.AI_INTENTION_IDLE))
		{
			ai.setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null);
		}
		return true;
	}
	
	@Override
	public boolean removeKnownObject(L2Object object)
	{
		if (!super.removeKnownObject(object))
		{
			return false;
		}
		
		if (!(object instanceof L2Character))
		{
			return true;
		}
		
		if (getActiveChar().hasAI())
		{
			// Notify the L2MonsterInstance AI with EVT_FORGET_OBJECT
			getActiveChar().getAI().notifyEvent(CtrlEvent.EVT_FORGET_OBJECT, object);
		}
		return true;
	}
	
	// =========================================================
	// Method - Private
	
	// =========================================================
	// Property - Public
	@Override
	public final L2MonsterInstance getActiveChar()
	{
		return (L2MonsterInstance) super.getActiveChar();
	}
}