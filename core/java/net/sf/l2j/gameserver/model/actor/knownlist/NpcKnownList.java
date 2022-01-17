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

import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2CabaleBufferInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FestivalGuideInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.quest.Quest;

public class NpcKnownList extends CharKnownList
{
	// =========================================================
	// Data Field
	
	// =========================================================
	// Constructor
	public NpcKnownList(L2NpcInstance activeChar)
	{
		super(activeChar);
	}
	
	// =========================================================
	// Method - Public
	
	// =========================================================
	// Method - Private
	
	// =========================================================
	// Property - Public
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
		
		L2PcInstance player = object.getActingPlayer();
		if (player != null)
		{
			if (getActiveChar().getTemplate().getEventQuests(Quest.QuestEventType.ON_CREATURE_SEE) != null)
			{
				for (Quest quest : getActiveChar().getTemplate().getEventQuests(Quest.QuestEventType.ON_CREATURE_SEE))
				{
					quest.notifyCreatureSee(getActiveChar(), player, object instanceof L2Summon);
				}
			}
		}
		
		return true;
	}
	
	@Override
	public L2NpcInstance getActiveChar()
	{
		return (L2NpcInstance) super.getActiveChar();
	}
	
	@Override
	public int getDistanceToForgetObject(L2Object object)
	{
		return 2 * getDistanceToWatchObject(object);
	}
	
	@Override
	public int getDistanceToWatchObject(L2Object object)
	{
		if (object instanceof L2FestivalGuideInstance)
		{
			return 4000;
		}
		
		if ((object instanceof L2FolkInstance) || !(object instanceof L2Character))
		{
			return 0;
		}
		
		if (object instanceof L2CabaleBufferInstance)
		{
			return 900;
		}
		
		if (object instanceof L2PlayableInstance)
		{
			return 1500;
		}
		
		return 500;
	}
}