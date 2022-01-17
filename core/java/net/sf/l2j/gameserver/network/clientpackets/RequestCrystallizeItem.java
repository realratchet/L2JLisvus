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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.PcInventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.util.IllegalPlayerAction;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.2.2.3.2.5 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestCrystallizeItem extends L2GameClientPacket
{
	private static final String _C__72_REQUESTDCRYSTALLIZEITEM = "[C] 72 RequestCrystallizeItem";
	private static Logger _log = Logger.getLogger(RequestCrystallizeItem.class.getName());
	
	private int _objectId;
	private int _count;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_count = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		if (_count <= 0)
		{
			Util.handleIllegalPlayerAction(activeChar, "[RequestCrystallizeItem] count <= 0! ban! oid: " + _objectId + " owner: " + activeChar.getName(), IllegalPlayerAction.PUNISH_KICK);
			return;
		}
		
		if (activeChar.isInStoreMode() || activeChar.isInCrystallize())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			return;
		}
		
		// Cannot crystallize while fishing
		if (activeChar.isFishing())
		{
			return;
		}
		
		int skillLevel = activeChar.getSkillLevel(L2Skill.SKILL_CRYSTALLIZE);
		if (skillLevel <= 0)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.CRYSTALLIZE_LEVEL_TOO_LOW);
			activeChar.sendPacket(sm);
			sm = null;
			ActionFailed af = new ActionFailed();
			activeChar.sendPacket(af);
			return;
		}
		
		PcInventory inventory = activeChar.getInventory();
		if (inventory != null)
		{
			L2ItemInstance item = inventory.getItemByObjectId(_objectId);
			if ((item == null) || item.isWear())
			{
				activeChar.sendPacket(new ActionFailed());
				return;
			}
			
			if (!item.isDestroyable())
			{
				return;
			}
			
			if (_count > item.getCount())
			{
				_count = activeChar.getInventory().getItemByObjectId(_objectId).getCount();
			}
		}
		
		L2ItemInstance itemToRemove = activeChar.getInventory().getItemByObjectId(_objectId);
		if ((itemToRemove == null) || itemToRemove.isWear())
		{
			return;
		}
		
		if (!itemToRemove.getItem().isCrystallizable() || (itemToRemove.getItem().getCrystalCount() <= 0) || (itemToRemove.getItem().getCrystalType() == L2Item.CRYSTAL_NONE))
		{
			_log.warning(activeChar.getName() + " (" + activeChar.getObjectId() + ") tried to crystallize " + itemToRemove.getItem().getItemId());
			return;
		}
		
		// Check if the char can crystallize C items and return if false
		if ((itemToRemove.getItem().getCrystalType() == L2Item.CRYSTAL_C) && (skillLevel <= 1))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CRYSTALLIZE_LEVEL_TOO_LOW));
			activeChar.sendPacket(new ActionFailed());
			return;
		}
		
		// Check if the user can crystallize B items and return if false
		if ((itemToRemove.getItem().getCrystalType() == L2Item.CRYSTAL_B) && (skillLevel <= 2))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CRYSTALLIZE_LEVEL_TOO_LOW));
			activeChar.sendPacket(new ActionFailed());
			return;
		}
		
		// Check if the user can crystallize A items and return if false
		if ((itemToRemove.getItem().getCrystalType() == L2Item.CRYSTAL_A) && (skillLevel <= 3))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CRYSTALLIZE_LEVEL_TOO_LOW));
			activeChar.sendPacket(new ActionFailed());
			return;
		}
		
		// Check if the user can crystallize S items and return if false;
		if ((itemToRemove.getItem().getCrystalType() == L2Item.CRYSTAL_S) && (skillLevel <= 4))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CRYSTALLIZE_LEVEL_TOO_LOW));
			activeChar.sendPacket(new ActionFailed());
			return;
		}
		
		activeChar.setInCrystallize(true);
		
		// Unequip if needed
		if (itemToRemove.isEquipped())
		{
			L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInSlotAndRecord(itemToRemove.getEquipSlot());
			InventoryUpdate iu = new InventoryUpdate();
			for (L2ItemInstance element : unequiped)
			{
				iu.addModifiedItem(element);
			}
			activeChar.sendPacket(iu);
		}
		
		// Remove from inventory
		L2ItemInstance removedItem = activeChar.getInventory().destroyItem("Crystalize", _objectId, _count, activeChar, null);
		
		// Add crystals
		int crystalId = itemToRemove.getItem().getCrystalItemId();
		int crystalAmount = itemToRemove.getCrystalCount();
		L2ItemInstance createditem = activeChar.getInventory().addItem("Crystalize", crystalId, crystalAmount, activeChar, itemToRemove);
		
		SystemMessage sm = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
		sm.addItemName(crystalId);
		sm.addNumber(crystalAmount);
		activeChar.sendPacket(sm);
		sm = null;
		
		// Send inventory update
		if (!Config.FORCE_INVENTORY_UPDATE)
		{
			InventoryUpdate iu = new InventoryUpdate();
			if (removedItem.getCount() == 0)
			{
				iu.addRemovedItem(removedItem);
			}
			else
			{
				iu.addModifiedItem(removedItem);
			}
			
			if (createditem.getCount() != crystalAmount)
			{
				iu.addModifiedItem(createditem);
			}
			else
			{
				iu.addNewItem(createditem);
			}
			activeChar.sendPacket(iu);
		}
		else
		{
			activeChar.sendPacket(new ItemList(activeChar, false));
		}
		
		// Status & user info
		StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);
		
		activeChar.broadcastUserInfo();
		
		L2World.getInstance().removeObject(removedItem);
		
		activeChar.setInCrystallize(false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__72_REQUESTDCRYSTALLIZEITEM;
	}
}