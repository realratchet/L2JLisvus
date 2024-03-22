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
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Harvester implements IItemHandler
{
    @Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
        if (!(playable instanceof L2PcInstance))
            return;

        if (CastleManorManager.getInstance().isDisabled())
            return;

        final L2PcInstance activeChar = (L2PcInstance)playable;

        if (activeChar.getTarget() == null || !(activeChar.getTarget() instanceof L2MonsterInstance))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
            activeChar.sendPacket(new ActionFailed());
            return;
        }

        final L2MonsterInstance target = (L2MonsterInstance)activeChar.getTarget();
        
        if (target == null || !target.isDead())
        {
            activeChar.sendPacket(new ActionFailed());
            return;
        }

        if (item.getItem().getSkills() != null)
        {
            SkillHolder holder = item.getItem().getSkills()[0];
            activeChar.useMagic(holder.getSkill(), false, false, item.getObjectId());
        }
    }
}