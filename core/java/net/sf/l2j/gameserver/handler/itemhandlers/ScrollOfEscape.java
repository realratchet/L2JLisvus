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
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class ScrollOfEscape implements IItemHandler
{
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
     */
    @Override
    public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
        if (!(playable instanceof L2PcInstance))
            return;
        
        L2PcInstance activeChar = (L2PcInstance) playable;
        
        if (activeChar.isMovementDisabled() || activeChar.isAlikeDead() || activeChar.isAllSkillsDisabled())
            return;
        
        if (activeChar.isSitting())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_MOVE_SITTING));
            return;
        }
        
        if (activeChar.isInParty() && activeChar.getParty().isInDimensionalRift() || DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getX(), activeChar.getY(), activeChar.getZ(), true))
        {
            activeChar.sendMessage("Once a party is ported in another dimension, its members cannot get out of it.");
            return;
        }
        
        if (activeChar.isInOlympiadMode())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
            return;
        }
        
        L2Event event = activeChar.getEvent();
        if (event != null && event.isStarted())
        {
            activeChar.sendMessage("You may not use an escape skill in events.");
            return;
        }
        
        // Check to see if the player is in a festival.
        if (activeChar.isFestivalParticipant())
        {
            activeChar.sendMessage("You may not use an escape skill in a festival.");
            return;
        }
        
        // Check to see if player is in jail
        if (activeChar.isInJail())
        {
            activeChar.sendMessage("You cannot escape from jail.");
            return;
        }
        
		if (!activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false))
		{
			return;
		}
        
        if (item.getItem().getSkills() != null)
        {
            SkillHolder holder = item.getItem().getSkills()[0];
            activeChar.useMagic(holder.getSkill(), false, false, item.getObjectId());
        }
    }
}