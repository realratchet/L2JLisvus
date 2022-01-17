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
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 * This class ...
 * @version $Revision: 1.2.4.4 $ $Date: 2005/03/27 15:30:07 $
 */
public class SoulShots implements IItemHandler
{
	// All the item IDs that this handler knows.
	private static int[] _itemIds =
	{
		5789,
		1835,
		1463,
		1464,
		1465,
		1466,
		1467
	};
	private static int[] _skillIds =
	{
		2039,
		2150,
		2151,
		2152,
		2153,
		2154
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

		// Check if Soulshot can be used
		if (weaponInst == null || weaponItem.getSoulShotCount() == 0)
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_USE_SOULSHOTS));
			}
			return;
		}

		// Check if Soulshot is already active
		if (weaponInst.getChargedSoulShot() != L2ItemInstance.CHARGED_NONE)
		{
			return;
		}

		// Check for correct grade
		int weaponGrade = weaponItem.getCrystalType();
		if (((weaponGrade == L2Item.CRYSTAL_NONE) && (itemId != 5789) && (itemId != 1835)) || ((weaponGrade == L2Item.CRYSTAL_D) && (itemId != 1463)) || ((weaponGrade == L2Item.CRYSTAL_C) && (itemId != 1464)) || ((weaponGrade == L2Item.CRYSTAL_B) && (itemId != 1465)) || ((weaponGrade == L2Item.CRYSTAL_A) && (itemId != 1466)) || ((weaponGrade == L2Item.CRYSTAL_S) && (itemId != 1467)))
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.SOULSHOTS_GRADE_MISMATCH));
			}
			return;
		}

		// Consume Soulshots if player has enough of them
		int saSSCount = (int) activeChar.getStat().calcStat(Stats.SOULSHOT_COUNT, 0, null, null);
		int SSCount = saSSCount == 0 ? weaponItem.getSoulShotCount() : saSSCount;

		if (!activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), SSCount, null, false))
		{
			if (!activeChar.disableAutoShot(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_SOULSHOTS));
			}
			return;
		}

		// Charge soulshot
		weaponInst.setChargedSoulShot(L2ItemInstance.CHARGED_SOULSHOT);

		// Send message to client
		activeChar.sendPacket(new SystemMessage(SystemMessage.ENABLED_SOULSHOT));
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, new MagicSkillUse(activeChar, activeChar, _skillIds[weaponGrade], 1, 0, 0), 360000);
	}

	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}