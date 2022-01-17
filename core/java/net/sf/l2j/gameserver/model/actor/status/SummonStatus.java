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
package net.sf.l2j.gameserver.model.actor.status;

import net.sf.l2j.gameserver.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class SummonStatus extends PlayableStatus
{
	// =========================================================
	// Data Field
	
	// =========================================================
	// Constructor
	public SummonStatus(L2Summon activeChar)
	{
		super(activeChar);
	}
	
	// =========================================================
	// Method - Public
	@Override
	public final void reduceHp(double value, L2Character attacker)
	{
		reduceHp(value, attacker, true, false);
	}
	
	@Override
	public final void reduceHp(double value, L2Character attacker, boolean awake, boolean isDot)
	{
		super.reduceHp(value, attacker, awake, isDot);
		
		if ((attacker != null) && (attacker != getActiveChar()))
		{
			if (!isDot && (getActiveChar().getOwner() != null))
			{
				SystemMessage sm;
				if (getActiveChar() instanceof L2PetInstance)
				{
					sm = new SystemMessage(SystemMessage.PET_RECEIVED_DAMAGE_OF_S2_BY_S1);
				}
				else
				{
					sm = new SystemMessage(SystemMessage.SUMMON_RECEIVED_DAMAGE_OF_S2_BY_S1);
				}
				if (attacker instanceof L2NpcInstance)
				{
					sm.addNpcName(((L2NpcInstance) attacker).getNpcId());
				}
				else
				{
					sm.addString(attacker.getName());
				}
				sm.addNumber((int) value);
				getActiveChar().getOwner().sendPacket(sm);
				
				getActiveChar().getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, attacker);
			}
		}
	}
	
	// =========================================================
	// Method - Private
	
	// =========================================================
	// Property - Public
	@Override
	public L2Summon getActiveChar()
	{
		return (L2Summon) super.getActiveChar();
	}
}