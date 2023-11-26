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
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class ItemSkills implements IItemHandler
{
	@Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		L2PcInstance player = null;
		
		if (playable instanceof L2PcInstance)
		{
			player = (L2PcInstance) playable;
		}
		else if (playable instanceof L2PetInstance)
		{
			player = ((L2PetInstance) playable).getOwner();
		}
		
		if (player == null)
		{
			return;
		}
		
		if (!player.destroyItem("Consume", item.getObjectId(), 1, null, false))
		{
			return;
		}
		
		SkillHolder[] holders = item.getItem().getSkills();
		for (SkillHolder holder : holders)
		{
			L2Skill skill = holder.getSkill();
			if (skill == null)
			{
				continue;
			}

			if (!skill.checkCondition(playable, playable))
			{
				return;
			}
			
			if (playable.isSkillDisabled(skill.getId(), false))
			{
				return;
			}
			
			if (skill.isPotion())
			{
				castInstantly(playable, item, skill);
			}
			else
			{
				playable.doCast(skill, true);
			}
		}
	}
	
	private void castInstantly(L2PlayableInstance activeChar, L2ItemInstance item, L2Skill skill)
	{
		if (activeChar instanceof L2PcInstance)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.USE_S1);
			sm.addItemName(item.getItemId());
			activeChar.sendPacket(sm);
		}
		
		if (skill.getReuseDelay() > 10)
		{
			activeChar.disableSkill(skill.getId(), skill.getReuseDelay());
		}
		
		activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, skill.getDisplayId(), skill.getLevel(), skill.getHitTime(), 0));
		
		L2PlayableInstance[] targets = new L2PlayableInstance[]
		{
			activeChar
		};
		
		// Apply effects
		try
		{
			ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());
			if (handler != null)
			{
				handler.useSkill(activeChar, skill, targets);
			}
			else
			{
				skill.useSkill(activeChar, targets);
			}
		}
		catch (Exception e)
		{
		}
	}
}