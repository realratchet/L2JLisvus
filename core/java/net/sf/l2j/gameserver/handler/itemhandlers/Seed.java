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

import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Manor;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2ChestInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Seed implements IItemHandler
{
    @Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
        if (CastleManorManager.getInstance().isDisabled())
            return;

        if (!(playable instanceof L2PcInstance))
            return;

        L2PcInstance activeChar = (L2PcInstance)playable;
        L2Object target = activeChar.getTarget();

        if (target == null || !(target instanceof L2MonsterInstance) || (target instanceof L2ChestInstance)
            || (target instanceof L2GrandBossInstance) || (target instanceof L2RaidBossInstance))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING));
            activeChar.sendPacket(new ActionFailed());
            return;
        }

        L2MonsterInstance mob = (L2MonsterInstance) target;

        if (mob.isDead())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
            activeChar.sendPacket(new ActionFailed());
            return;
        }

        if (mob.isSeeded())
        {
            activeChar.sendPacket(new ActionFailed());
            return;
        }

        int seedId = item.getItemId();
        if (L2Manor.getInstance().getCastleIdForSeed(seedId) != MapRegionTable.getInstance().getAreaCastle(activeChar))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_SEED_MAY_NOT_BE_SOWN_HERE));
            activeChar.sendPacket(new ActionFailed());
            return;
        }

        mob.setSeeded(seedId, activeChar);
        if (item.getItem().getSkills() != null)
		{
			SkillHolder holder = item.getItem().getSkills()[0];
			activeChar.useMagic(holder.getSkill(), false, false);
		}
    }
}