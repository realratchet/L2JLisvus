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
package net.sf.l2j.gameserver.skills.l2skills;

import net.sf.l2j.gameserver.ai.L2CharacterAI;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SummonItemsData;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2SummonItem;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;

public class L2SkillSummonPet extends L2Skill
{
	public L2SkillSummonPet(StatsSet set)
	{
		super(set);
	}
	
	@Override
	public boolean checkCondition(L2Character activeChar, L2Object target)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return false;
		}
		
		final L2PcInstance player = (L2PcInstance) activeChar;
		if (player.getPet() != null || player.isMounted())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_HAVE_A_PET));
			return false;
		}
		
		return super.checkCondition(activeChar, target);
	}
	
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean critOnFirstTarget)
	{
		L2CharacterAI ai = activeChar.getAI();
		if (ai.getCurrentControlItemObjectId() == 0)
		{
			return;
		}
		
		if (activeChar.isAlikeDead() || !(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance player = (L2PcInstance) activeChar;
		L2ItemInstance item = player.getInventory().getItemByObjectId(ai.getCurrentControlItemObjectId());
		
		if (item == null)
		{
			return;
		}
		
		L2SummonItem summonItem = SummonItemsData.getInstance().getSummonItem(item.getItemId());
		if (summonItem == null)
		{
			return;
		}
		
		int npcId = summonItem.getNpcId();
		
		L2NpcTemplate npcTemplate = NpcTable.getInstance().getTemplate(npcId);
		if (npcTemplate == null)
		{
			return;
		}
		
		L2PetInstance pet = L2PetInstance.spawnPet(npcTemplate, player, item);
		if (pet == null)
		{
			return;
		}
		
		pet.setTitle(player.getName());
		item.setEnchantLevel(pet.getLevel());
		
		if (!pet.isRespawned())
		{
			pet.setCurrentHp(pet.getMaxHp());
			pet.setCurrentMp(pet.getMaxMp());
			pet.getStat().setExp(pet.getExpForThisLevel());
			pet.setCurrentFed(pet.getMaxFed());
			
			pet.store();
		}
		
		pet.setRunning();
		player.setPet(pet);
		
		L2World.getInstance().storeObject(pet);
		pet.spawnMe(pet.getX(), pet.getY(), pet.getZ());
	}
}