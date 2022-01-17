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
package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Recall implements ISkillHandler
{
	// private static Logger _log = Logger.getLogger(Recall.class.getName());
	protected SkillType[] _skillIds =
	{
		SkillType.RECALL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		try
		{
			for (L2Object trg : targets)
			{
				if (!(trg instanceof L2PcInstance))
				{
					continue;
				}
				
				L2PcInstance target = (L2PcInstance) trg;
				
				// If Alternate rule Karma punishment is set to true, forbid skill Recall to player with Karma
				if (!Config.ALT_GAME_KARMA_PLAYER_CAN_TELEPORT && (target.getKarma() > 0))
				{
					continue;
				}
				
				if (target.isInOlympiadMode())
				{
					target.sendPacket(new SystemMessage(SystemMessage.THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
					continue;
				}
				
				if (target.isInStoreMode())
				{
					continue;
				}
				
				if ((target.isInParty() && target.getParty().isInDimensionalRift()) || DimensionalRiftManager.getInstance().checkIfInRiftZone(target.getX(), target.getY(), target.getZ(), true))
				{
					target.sendMessage("Once a party is ported in another dimension, its members cannot get out of it.");
					return;
				}
				
				// Check to see if the current player target is in a festival.
				if (target.isFestivalParticipant())
				{
					target.sendMessage("You may not use an escape skill in a festival.");
					continue;
				}
				
				// Check to see if player is in jail
				if (target.isInJail())
				{
					target.sendMessage("You cannot escape from jail.");
					continue;
				}
				
				L2Event event = target.getEvent();
				if (event != null && event.isStarted())
				{
					activeChar.sendMessage("You may not use an escape skill in events.");
					continue;
				}
				
				target.teleToLocation(MapRegionTable.TeleportWhereType.Town);
			}
		}
		catch (Throwable e)
		{
			if (Config.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}