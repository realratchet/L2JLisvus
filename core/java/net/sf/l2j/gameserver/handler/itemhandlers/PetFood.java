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

import net.sf.l2j.gameserver.datatables.PetDataTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Kerberos
 */
public class PetFood implements IItemHandler
{
	/**
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	@Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		if (item.getItem().getSkills() == null)
		{
			return;
		}
		
		SkillHolder holder = item.getItem().getSkills()[0];
		if (playable instanceof L2PetInstance)
		{
			L2PetInstance pet = ((L2PetInstance) playable);
			if (pet.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				pet.broadcastPacket(new MagicSkillUse(playable, playable, holder.getId(), holder.getLevel(), 0, 0));
				pet.setCurrentFed(pet.getCurrentFed() + holder.getSkill().getFeed());
				pet.broadcastStatusUpdate();
				if (pet.getCurrentFed() < (0.55 * pet.getPetData().getPetMaxFeed()))
				{
					pet.getOwner().sendPacket(new SystemMessage(SystemMessage.YOUR_PET_ATE_A_LITTLE_BUT_IS_STILL_HUNGRY));
				}
			}
		}
		else if (playable instanceof L2PcInstance)
		{
			L2PcInstance player = ((L2PcInstance) playable);
			if (!player.isMounted())
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_CANNOT_BE_USED);
				sm.addItemName(item.getItemId());
				player.sendPacket(sm);
				return;
			}
			
			if (!canEatFood(player, item))
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_CANNOT_BE_USED);
				sm.addItemName(item.getItemId());
				player.sendPacket(sm);
				return;
			}
			
			if (player.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				player.broadcastPacket(new MagicSkillUse(playable, playable, holder.getId(), holder.getLevel(), 0, 0));
				player.setCurrentFeed(player.getCurrentFeed() + holder.getSkill().getFeed());
			}
		}
	}
	
	private boolean canEatFood(L2PcInstance player, L2ItemInstance item)
	{
		boolean canUse = false;
		int itemId = item.getItemId();
		int petId = player.getMountNpcId();
		
		if (PetDataTable.isWolf(petId) && PetDataTable.isWolfFood(itemId))
		{
			canUse = true;
		}
		else if (PetDataTable.isSinEater(petId) && PetDataTable.isSinEaterFood(itemId))
		{
			canUse = true;
		}
		else if (PetDataTable.isHatchling(petId) && PetDataTable.isHatchlingFood(itemId))
		{
			canUse = true;
		}
		else if (PetDataTable.isStrider(petId) && PetDataTable.isStriderFood(itemId))
		{
			canUse = true;
		}
		else if (PetDataTable.isWyvern(petId) && PetDataTable.isWyvernFood(itemId))
		{
			canUse = true;
		}
		else if (PetDataTable.isBaby(petId) && PetDataTable.isBabyFood(itemId))
		{
			canUse = true;
		}
		
		return canUse;
	}
}