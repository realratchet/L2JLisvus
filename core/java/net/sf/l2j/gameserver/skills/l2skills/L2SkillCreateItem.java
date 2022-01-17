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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;

/**
 * @author Nemesiss
 */
public class L2SkillCreateItem extends L2Skill
{
	private final int create_item_id;
	private final int create_item_count;
	private final int random_count;
	
	public L2SkillCreateItem(StatsSet set)
	{
		super(set);
		
		create_item_id = set.getInteger("create_item_id", 0);
		create_item_count = set.getInteger("create_item_count", 0);
		random_count = set.getInteger("random_count", 1);
	}
	
	/**
	 * @see net.sf.l2j.gameserver.model.L2Skill#useSkill(net.sf.l2j.gameserver.model.L2Character, net.sf.l2j.gameserver.model.L2Object[])
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		if (create_item_id == 0 || create_item_count == 0)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1_PREPARED_FOR_REUSE);
			activeChar.sendPacket(sm);
			return;
		}
		L2PcInstance player = (L2PcInstance) activeChar;
		if (activeChar instanceof L2PcInstance)
		{
			int rnd = Rnd.nextInt(random_count) + 1;
			int count = create_item_count * rnd;
			
			giveItems(player, create_item_id, count);
		}
	}
	
	/**
	 * @param activeChar
	 * @param itemId
	 * @param count
	 */
	public void giveItems(L2PcInstance activeChar, int itemId, int count)
	{
		L2ItemInstance item = activeChar.getInventory().addItem("Skill", itemId, count, activeChar, activeChar);
		
		if (count > 1)
		{
			SystemMessage smsg = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
			smsg.addItemName(itemId);
			smsg.addNumber(count);
			activeChar.sendPacket(smsg);
		}
		else
		{
			SystemMessage smsg = new SystemMessage(SystemMessage.EARNED_ITEM);
			smsg.addItemName(itemId);
			activeChar.sendPacket(smsg);
		}
		
		// Send update packet
		if (!Config.FORCE_INVENTORY_UPDATE)
		{
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(item);
			activeChar.sendPacket(iu);
		}
		else
		{
			activeChar.sendPacket(new ItemList(activeChar, false));
		}
	}
}