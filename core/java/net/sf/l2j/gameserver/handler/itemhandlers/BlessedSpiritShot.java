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
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 * This class ...
 * @version $Revision: 1.1.2.1.2.5 $ $Date: 2005/03/27 15:30:07 $
 */
public class BlessedSpiritShot implements IItemHandler
{
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	@Override
	public synchronized void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		if (!(playable instanceof L2PcInstance))
		{
			return;
		}

		L2PcInstance activeChar = (L2PcInstance) playable;
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
			return;
		}

		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		L2Weapon weaponItem = activeChar.getActiveWeaponItem();
		int itemId = item.getItemId();

		// Check if Blessed Spiritshot can be used
		if (weaponInst == null || weaponItem.getSpiritShotCount() == 0)
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_USE_SPIRITSHOTS));
			}
			return;
		}

		// Check if Blessed Spiritshot is already active (it can be charged over Spiritshot)
		if (weaponInst.getChargedSpiritShot() != L2ItemInstance.CHARGED_NONE)
		{
			return;
		}

		// Check for correct grade
		if (weaponItem.getCrystalType() != item.getItem().getCrystalType())
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.SPIRITSHOTS_GRADE_MISMATCH));
			}
			return;
		}

		// Consume Blessed Spiritshot if player has enough of them
		if (!activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), weaponItem.getSpiritShotCount(), null, false))
		{
			if (!activeChar.disableAutoShot(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_SPIRITSHOTS));
			}
			return;
		}

		// Charge Blessed Spiritshot
		weaponInst.setChargedSpiritShot(L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT);

		// Send message to client
		activeChar.sendPacket(new SystemMessage(SystemMessage.ENABLED_SPIRITSHOT));

		if (item.getItem().getSkills() != null)
		{
			SkillHolder holder = item.getItem().getSkills()[0];
            Broadcast.toSelfAndKnownPlayersInRadius(activeChar, new MagicSkillUse(activeChar, activeChar, holder.getId(), holder.getLevel(), 0, 0), 360000);
        }
	}
}