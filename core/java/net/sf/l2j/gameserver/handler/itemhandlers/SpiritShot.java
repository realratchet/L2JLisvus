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
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 * This class ...
 * @version $Revision: 1.1.2.1.2.5 $ $Date: 2005/03/27 15:30:07 $
 */
public class SpiritShot implements IItemHandler
{
	// All the item IDs that this handler knows.
	private static int[] _itemIds =
	{
		5790,
		2509,
		2510,
		2511,
		2512,
		2513,
		2514
	};
	private static int[] _skillIds =
	{
		2061,
		2155,
		2156,
		2157,
		2158,
		2159
	};

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
		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		L2Weapon weaponItem = activeChar.getActiveWeaponItem();
		int itemId = item.getItemId();

		// Check if Spiritshot can be used
		if (weaponInst == null || weaponItem.getSpiritShotCount() == 0)
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_USE_SPIRITSHOTS));
			}
			return;
		}

		// Check if Spiritshot is already active
		if (weaponInst.getChargedSpiritShot() != L2ItemInstance.CHARGED_NONE)
		{
			return;
		}

		// Check for correct grade
		int weaponGrade = weaponItem.getCrystalType();
		if (((weaponGrade == L2Item.CRYSTAL_NONE) && (itemId != 5790) && (itemId != 2509)) || ((weaponGrade == L2Item.CRYSTAL_D) && (itemId != 2510)) || ((weaponGrade == L2Item.CRYSTAL_C) && (itemId != 2511)) || ((weaponGrade == L2Item.CRYSTAL_B) && (itemId != 2512)) || ((weaponGrade == L2Item.CRYSTAL_A) && (itemId != 2513)) || ((weaponGrade == L2Item.CRYSTAL_S) && (itemId != 2514)))
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.SPIRITSHOTS_GRADE_MISMATCH));
			}
			return;
		}

		// Consume Spiritshot if player has enough of them
		if (!activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), weaponItem.getSpiritShotCount(), null, false))
		{
			if (!activeChar.disableAutoShot(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_SPIRITSHOTS));
			}
			return;
		}

		// Charge Spiritshot
		weaponInst.setChargedSpiritShot(L2ItemInstance.CHARGED_SPIRITSHOT);

		// Send message to client
		activeChar.sendPacket(new SystemMessage(SystemMessage.ENABLED_SPIRITSHOT));
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, new MagicSkillUse(activeChar, activeChar, _skillIds[weaponGrade], 1, 0, 0), 360000);
	}

	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}