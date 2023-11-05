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
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 * Beast SoulShot Handler
 * @author Tempy
 */
public class BeastSoulShot implements IItemHandler
{
	@Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		if (playable == null)
		{
			return;
		}

		if (playable instanceof L2Summon)
		{
			((L2Summon) playable).getOwner().sendPacket(new SystemMessage(SystemMessage.PET_CANNOT_USE_ITEM));
			return;
		}
		
		if (!(playable instanceof L2PcInstance))
		{
			return;
		}

		L2PcInstance activeOwner = (L2PcInstance) playable;
		L2Summon activePet = activeOwner.getPet();

		if (activePet == null)
		{
			activeOwner.sendPacket(new SystemMessage(SystemMessage.NO_PETS_AVAILABLE));
			return;
		}

		if (activePet.isDead())
		{
			activeOwner.sendPacket(new SystemMessage(SystemMessage.SOULSHOTS_AND_SPIRITSHOTS_ARE_NOT_AVAILABLE_FOR_A_DEAD_PET));
			return;
		}
		
		if (activePet.getChargedSoulShot() != L2ItemInstance.CHARGED_NONE)
		{
			return;
		}

		// If the player doesn't have enough beast soulshot remaining, remove any auto soulshot task.
		if (!activeOwner.destroyItemWithoutTrace("Consume", item.getObjectId(), activePet.getSoulShotsPerHit(), null, false))
		{
			if (!activeOwner.disableAutoShot(item.getItemId()))
			{
				activeOwner.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_SOULSHOTS_FOR_PET));
			}
			return;
		}

		activePet.setChargedSoulShot(L2ItemInstance.CHARGED_SOULSHOT);
		activeOwner.sendPacket(new SystemMessage(SystemMessage.PET_USES_SHOTS));

		if (item.getItem().getSkills() != null)
		{
			SkillHolder holder = item.getItem().getSkills()[0];
			Broadcast.toSelfAndKnownPlayersInRadius(activeOwner, new MagicSkillUse(activePet, activePet, holder.getId(), holder.getLevel(), 0, 0), 360000);
		}
	}
}
