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

import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SummonItemsData;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2SummonItem;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author FBIagent
 */
public class SummonItems implements IItemHandler
{
    @Override
    public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
        L2SummonItem summonItem = SummonItemsData.getInstance().getSummonItem(item.getItemId());
        if (summonItem == null)
        {
            return;
        }
        
        if (!(playable instanceof L2PcInstance))
            return;
        
        L2PcInstance activeChar = (L2PcInstance) playable;
        
        if (!activeChar.getFloodProtectors().getItemPetSummon().tryPerformAction("summon items"))
            return;
        
        if (activeChar.isSitting())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_MOVE_SITTING));
            return;
        }
        
        if (activeChar.inObserverMode())
            return;
        
        if (activeChar.isInOlympiadMode())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
            return;
        }
        
        L2Event event = activeChar.getEvent();
        if (event != null && event.isStarted())
        {
            activeChar.sendMessage("You may not summon a pet in events.");
            return;
        }
        
        if (activeChar.isAllSkillsDisabled())
            return;
        
        if (activeChar.isAttackingNow() || activeChar.isRooted())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_SUMMON_IN_COMBAT));
            return;
        }
        
        int npcID = summonItem.getNpcId();
        if (npcID == 0)
            return;
        
        L2NpcTemplate npcTemplate = NpcTable.getInstance().getTemplate(npcID);
        if (npcTemplate == null)
            return;
        
        switch (summonItem.getType())
        {
            case 0: // Static summons (like Christmas tree)
                try
                {
                    activeChar.stopMove(null);
                    
                    L2Spawn spawn = new L2Spawn(npcTemplate);
                    spawn.setId(IdFactory.getInstance().getNextId());
                    spawn.setLocX(activeChar.getX());
                    spawn.setLocY(activeChar.getY());
                    spawn.setLocZ(activeChar.getZ());
                    L2World.getInstance().storeObject(spawn.spawnOne());
                    activeChar.destroyItem("Summon", item.getObjectId(), 1, null, false);
                    activeChar.sendMessage("Created " + npcTemplate.name + " at x: " + spawn.getLocX() + " y: " + spawn.getLocY() + " z: " + spawn.getLocZ());
                }
                catch (Exception e)
                {
                    activeChar.sendMessage("Target is not in game.");
                }
                break;
            case 1: // Pet summons
                if (item.getItem().getSkills() != null)
                {
                    SkillHolder holder = item.getItem().getSkills()[0];
                    L2Skill skill = holder.getSkill();
                    if (skill != null)
                    {
                        activeChar.useMagic(holder.getSkill(), false, false, item.getObjectId());
                    }
                }
                break;
            case 2: // Wyvern
                activeChar.stopMove(null);
                activeChar.mount(summonItem.getNpcId(), item.getObjectId(), true);
                break;
        }
    }
}