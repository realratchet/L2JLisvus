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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.util.Util;

public class PcStatus extends PlayableStatus
{
	// =========================================================
	// Data Field
	
	// =========================================================
	// Constructor
	public PcStatus(L2PcInstance activeChar)
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
		if (getActiveChar().isInvul())
		{
			if (attacker != getActiveChar())
			{
				return;
			}
			else if (!isDot)
			{
				return;
			}
		}
		
		if (getActiveChar().isDead())
		{
			return;
		}
		
		if ((attacker != null) && (attacker != getActiveChar()))
		{
			int fullValue = (int) value;
			int tDmg = 0;
			
			// Check and calculate transfered damage
			L2Summon summon = getActiveChar().getPet();
			if ((summon != null) && (summon instanceof L2SummonInstance))
			{
				// C4 transfer pain has no distance limit between master and servitor, so use master's forget range (4000)
				int distanceToForget = 4000;
				if (Util.checkIfInRange(distanceToForget, getActiveChar(), summon, true))
				{
					tDmg = ((int) value * (int) getActiveChar().getStat().calcStat(Stats.TRANSFER_DAMAGE_PERCENT, 0, null, null)) / 100;
					
					// Only transfer dmg up to current HP, it should not be killed
					if (summon.getCurrentHp() < tDmg)
					{
						tDmg = (int) summon.getCurrentHp() - 1;
					}
					if (tDmg > 0)
					{
						summon.reduceCurrentHp(tDmg, attacker);
						value -= tDmg;
						fullValue = (int) value; // reduce the announced value here as player will get a message about summon damage
					}
				}
			}
			
			if (attacker instanceof L2PlayableInstance)
			{
				if (getCurrentCp() >= value)
				{
					setCurrentCp(getCurrentCp() - value); // Set Cp to diff of Cp vs value
					value = 0; // No need to subtract anything from Hp
				}
				else
				{
					value -= getCurrentCp(); // Get diff from value vs Cp; will apply diff to Hp
					setCurrentCp(0); // Set Cp to 0
				}
			}

			if ((fullValue > 0) && !isDot)
			{
				// Send a System Message to the target L2PcInstance
				SystemMessage smsg = new SystemMessage(SystemMessage.S1_GAVE_YOU_S2_DMG);
				if (attacker instanceof L2NpcInstance)
				{
					smsg.addNpcName(((L2NpcInstance) attacker).getNpcId());
					if (Config.AUTO_TARGET_NPC)
					{
						if (getActiveChar().getTarget() == null)
						{
							((L2NpcInstance) attacker).onAction(getActiveChar());
						}
					}
				}
				else if (attacker instanceof L2Summon)
				{
					smsg.addNpcName(((L2Summon) attacker).getNpcId());
				}
				else
				{
					smsg.addString(attacker.getName());
				}
				
				smsg.addNumber(fullValue);
				getActiveChar().sendPacket(smsg);
				
				// Send a System Message to the target L2PcInstance
				// if there is the case of transfer damage.
				if (tDmg > 0)
				{
					final L2PcInstance attackingPlayer = attacker.getActingPlayer();
					if (attackingPlayer != null)
					{
						smsg = new SystemMessage(SystemMessage.GIVEN_S1_DAMAGE_TO_YOUR_TARGET_AND_S2_DAMAGE_TO_SERVITOR);
						smsg.addNumber(fullValue);
						smsg.addNumber(tDmg);
						attackingPlayer.sendPacket(smsg);
					}
				}
			}
		}
		
		if (!getActiveChar().isDead() && getActiveChar().isSitting() && awake)
		{
			getActiveChar().standUp();
		}
		
		if (getActiveChar().isFakeDeath() && awake)
		{
			getActiveChar().stopFakeDeath(true);
		}

		super.reduceHp(value, attacker, awake, isDot);
	}
	
	// =========================================================
	// Method - Private
	
	// =========================================================
	// Property - Public
	@Override
	public L2PcInstance getActiveChar()
	{
		return (L2PcInstance) super.getActiveChar();
	}
}