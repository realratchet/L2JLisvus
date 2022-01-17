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
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.EnchantResult;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2WeaponType;
import net.sf.l2j.gameserver.util.IllegalPlayerAction;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

public class RequestEnchantItem extends L2GameClientPacket
{
	protected static final Logger _log = Logger.getLogger(Inventory.class.getName());
	private static final String _C__58_REQUESTENCHANTITEM = "[C] 58 RequestEnchantItem";
	private static final int[] crystalScrolls =
	{
		731,
		732,
		949,
		950,
		953,
		954,
		957,
		958,
		961,
		962
	};
	
	private int _objectId;
	
	@Override
	protected void readImpl()
	{
		_objectId = 0;
		try
		{
			_objectId = readD();
		}
		catch (Exception e)
		{
		}
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_objectId == 0))
		{
			return;
		}
		
		if (activeChar.isProcessingTransaction() || activeChar.isInStoreMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_ENCHANT_WHILE_STORE));
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2));
			return;
		}
		
		L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		L2ItemInstance scroll = activeChar.getActiveEnchantItem();
		if (item == null || scroll == null)
		{
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2));
			return;
		}
		
		// Can't enchant rods and hero weapons
		if (item.getItem().getItemType() == L2WeaponType.FISHINGROD || (!Config.ALLOW_HERO_ENCHANT && item.isHeroItem()))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INAPPROPRIATE_ENCHANT_CONDITION));
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2));
			return;
		}
		
		// Can't enchant traveler's weapons
		if (item.getItemId() >= 7816 && item.getItemId() <= 7831)
        {
			activeChar.sendPacket(new SystemMessage(SystemMessage.INAPPROPRIATE_ENCHANT_CONDITION));
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2));
            return;
        }
		
		if (item.isWear())
		{
			Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to enchant a weared Item", IllegalPlayerAction.PUNISH_KICK);
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2));
			return;
		}
		
		switch (item.getLocation())
		{
			case INVENTORY:
			case PAPERDOLL:
			{
				if (item.getOwnerId() != activeChar.getObjectId())
				{
					activeChar.setActiveEnchantItem(null);
					activeChar.sendPacket(new EnchantResult(2));
					return;
				}
				break;
			}
			default:
			{
				Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to use enchant Exploit!", IllegalPlayerAction.PUNISH_KICKBAN);
				activeChar.setActiveEnchantItem(null);
				activeChar.sendPacket(new EnchantResult(2));
				return;
			}
		}
		
		if (activeChar.getActiveWarehouse() != null)
		{
			activeChar.setActiveWarehouse(null);
		}
		
		int itemType2 = item.getItem().getType2();
		
		boolean blessedScroll = false;
		boolean enchantItem = false;
		int crystalId = 0;
		
		/** pretty code ;D */
		switch (item.getItem().getCrystalType())
		{
			case L2Item.CRYSTAL_S:
				crystalId = 1462;
				switch (scroll.getItemId())
				{
					case 959:
					case 961:
					case 6577:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 960:
					case 962:
					case 6578:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
			case L2Item.CRYSTAL_A:
				crystalId = 1461;
				switch (scroll.getItemId())
				{
					case 729:
					case 731:
					case 6569:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 730:
					case 732:
					case 6570:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						
						break;
				}
				break;
			case L2Item.CRYSTAL_B:
				crystalId = 1460;
				switch (scroll.getItemId())
				{
					case 947:
					case 949:
					case 6571:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 948:
					case 950:
					case 6572:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
			case L2Item.CRYSTAL_C:
				crystalId = 1459;
				switch (scroll.getItemId())
				{
					case 951:
					case 953:
					case 6573:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 952:
					case 954:
					case 6574:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
			case L2Item.CRYSTAL_D:
				crystalId = 1458;
				switch (scroll.getItemId())
				{
					case 955:
					case 957:
					case 6575:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 956:
					case 958:
					case 6576:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
			
		}
		
		if (!enchantItem)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INAPPROPRIATE_ENCHANT_CONDITION));
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2));
			return;
		}
		
		// Get the scroll type - Yesod
		if ((scroll.getItemId() >= 6569) && (scroll.getItemId() <= 6578))
		{
			blessedScroll = true;
		}
		else
		{
			for (int crystalscroll : crystalScrolls)
			{
				if (scroll.getItemId() == crystalscroll)
				{
					blessedScroll = true;
				}
				
				break;
			}
		}
		
		int chance = 0;
		int maxEnchantLevel = 0;
		
		if (item.getItem().getType2() == L2Item.TYPE2_WEAPON)
		{
			if (blessedScroll)
			{
				chance = Config.BLESSED_ENCHANT_CHANCE_WEAPON;
			}
			else
			{
				chance = Config.ENCHANT_CHANCE_WEAPON;
			}
			maxEnchantLevel = Config.ENCHANT_MAX_WEAPON;
		}
		else if (item.getItem().getType2() == L2Item.TYPE2_SHIELD_ARMOR)
		{
			if (blessedScroll)
			{
				chance = Config.BLESSED_ENCHANT_CHANCE_ARMOR;
			}
			else
			{
				chance = Config.ENCHANT_CHANCE_ARMOR;
			}
			maxEnchantLevel = Config.ENCHANT_MAX_ARMOR;
		}
		else if (item.getItem().getType2() == L2Item.TYPE2_ACCESSORY)
		{
			if (blessedScroll)
			{
				chance = Config.BLESSED_ENCHANT_CHANCE_JEWELRY;
			}
			else
			{
				chance = Config.ENCHANT_CHANCE_JEWELRY;
			}
			maxEnchantLevel = Config.ENCHANT_MAX_JEWELRY;
		}
		
		if ((item.getEnchantLevel() >= maxEnchantLevel) && (maxEnchantLevel != 0))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INAPPROPRIATE_ENCHANT_CONDITION));
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2));
			return;
		}
		
		scroll = activeChar.getInventory().destroyItem("Enchant", scroll, 1, activeChar, item);
		if (scroll == null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_ITEMS));
			Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to enchant with a scroll he doesnt have", Config.DEFAULT_PUNISH);
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2));
			return;
		}
		
		if ((item.getEnchantLevel() < Config.ENCHANT_SAFE_MAX) || ((item.getItem().getBodyPart() == L2Item.SLOT_FULL_ARMOR) && (item.getEnchantLevel() < Config.ENCHANT_SAFE_MAX_FULL)))
		{
			chance = 100;
		}
		
		SystemMessage sm;
		final InventoryUpdate iu = !Config.FORCE_INVENTORY_UPDATE ? new InventoryUpdate() : null;
		// Scroll will be removed from inventory regardless of result
		if (iu != null)
		{
			if (scroll.getCount() > 0)
			{
				iu.addModifiedItem(scroll);
			}
			else
			{
				iu.addRemovedItem(scroll);
			}
		}
		
		if (Rnd.get(100) < chance)
		{
			synchronized (item)
			{
				if (item.getOwnerId() != activeChar.getObjectId())
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.INAPPROPRIATE_ENCHANT_CONDITION));
					activeChar.setActiveEnchantItem(null);
					activeChar.sendPacket(new EnchantResult(2));
					return;
				}
				
				if ((item.getLocation() != L2ItemInstance.ItemLocation.INVENTORY) && (item.getLocation() != L2ItemInstance.ItemLocation.PAPERDOLL))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.INAPPROPRIATE_ENCHANT_CONDITION));
					activeChar.setActiveEnchantItem(null);
					activeChar.sendPacket(new EnchantResult(2));
					return;
				}
				
				if (item.getEnchantLevel() == 0)
				{
					sm = new SystemMessage(SystemMessage.S1_SUCCESSFULLY_ENCHANTED);
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				else
				{
					sm = new SystemMessage(SystemMessage.S1_S2_SUCCESSFULLY_ENCHANTED);
					sm.addNumber(item.getEnchantLevel());
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				
				item.setEnchantLevel(item.getEnchantLevel() + 1);
				item.updateDatabase();
			}
			
			if (iu != null)
			{
				iu.addModifiedItem(item);
			}
			activeChar.sendPacket(new EnchantResult(0));
		}
		else
		{
			if (!blessedScroll)
			{
				if (item.getEnchantLevel() > 0)
				{
					sm = new SystemMessage(SystemMessage.ENCHANTMENT_FAILED_S1_S2_EVAPORATED);
					sm.addNumber(item.getEnchantLevel());
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				else
				{
					sm = new SystemMessage(SystemMessage.ENCHANTMENT_FAILED_S1_EVAPORATED);
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				
				if (item.isEquipped())
				{
					if (item.getEnchantLevel() > 0)
					{
						sm = new SystemMessage(SystemMessage.EQUIPMENT_S1_S2_REMOVED);
						sm.addNumber(item.getEnchantLevel());
						sm.addItemName(item.getItemId());
						activeChar.sendPacket(sm);
					}
					else
					{
						sm = new SystemMessage(SystemMessage.S1_DISARMED);
						sm.addItemName(item.getItemId());
						activeChar.sendPacket(sm);
					}
					
					L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInSlotAndRecord(item.getEquipSlot());
					if (iu != null)
					{
						for (L2ItemInstance element : unequiped)
						{
							iu.addModifiedItem(element);
						}
					}
				}
				
				int count = item.getCrystalCount() - ((item.getItem().getCrystalCount() + 1) / 2);
				if (count < 1)
				{
					count = 1;
				}
				
				L2ItemInstance destroyItem = activeChar.getInventory().destroyItem("Enchant", item, activeChar, null);
				if (destroyItem != null)
				{
					L2ItemInstance crystals = activeChar.getInventory().addItem("Enchant", crystalId, count, activeChar, destroyItem);
					
					sm = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
					sm.addItemName(crystals.getItemId());
					sm.addNumber(count);
					activeChar.sendPacket(sm);
					
					if (iu != null)
					{
						if (destroyItem.getCount() == 0)
						{
							iu.addRemovedItem(destroyItem);
						}
						else
						{
							iu.addModifiedItem(destroyItem);
						}
						iu.addItem(crystals);
					}
					
					L2World.getInstance().removeObject(destroyItem);
					activeChar.sendPacket(new EnchantResult(1));
				}
				else
				{
					activeChar.sendPacket(new EnchantResult(2));
				}
			}
			else
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.BLESSED_ENCHANT_FAILED));
				
				item.setEnchantLevel(0);
				item.updateDatabase();
				
				if (iu != null)
				{
					iu.addModifiedItem(item);
				}
				
				activeChar.sendPacket(new EnchantResult(2));
			}
		}
		sm = null;
		
		StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);
		
		if (iu != null)
		{
			activeChar.sendPacket(iu);
		}
		else
		{
			activeChar.sendPacket(new ItemList(activeChar, false));
		}
		activeChar.broadcastUserInfo();
		activeChar.setActiveEnchantItem(null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__58_REQUESTENCHANTITEM;
	}
}