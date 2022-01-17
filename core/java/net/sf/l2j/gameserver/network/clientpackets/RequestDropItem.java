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
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.handler.ItemHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2EtcItemType;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.util.IllegalPlayerAction;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.11.2.1.2.7 $ $Date: 2005/04/02 21:25:21 $
 */
public class RequestDropItem extends L2GameClientPacket
{
	private static final String _C__12_REQUESTDROPITEM = "[C] 12 RequestDropItem";
	private static Logger _log = Logger.getLogger(RequestDropItem.class.getName());

	private int _objectId;
	private int _count;
	private int _x;
	private int _y;
	private int _z;

	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_count = readD();
		_x = readD();
		_y = readD();
		_z = readD();
	}

	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		if (activeChar.isDead())
		{
			return;
		}
		
		if (activeChar.inObserverMode())
		{
			return;
		}

		// Flood protect drop to avoid packet lag
		if (!getClient().getFloodProtectors().getDropItem().tryPerformAction("drop item"))
		{
			return;
		}

		L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		if (item == null || _count == 0)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISCARD_THIS_ITEM));
			return;
		}
		
		// Mercenary Posting Tickets
		if (item.getItem().isMercenaryTicket())
		{
			IItemHandler handler = ItemHandler.getInstance().getItemHandler(item.getItemId());
			if (handler == null)
			{
				if (Config.DEBUG)
				{
					_log.warning("No item handler registered for item ID " + item.getItemId() + ".");
				}
			}
			else
			{
				ThreadPoolManager.getInstance().executeTask(() -> handler.useItem(activeChar, item));
			}
			return;
		}
		
		if (!activeChar.validateItemManipulation(_objectId, "drop") || (!Config.ALLOW_DISCARDITEM && !activeChar.isGM()) || !item.isDropable())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISCARD_THIS_ITEM));
			return;
		}

		if ((item.getItemType() == L2EtcItemType.QUEST) && !activeChar.isGM())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISCARD_THIS_ITEM));
			return;
		}

		if (_count > item.getCount())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.NOTHING_HAPPENED));
			return;
		}

		if (_count < 0)
		{
			Util.handleIllegalPlayerAction(activeChar, "[RequestDropItem] count <= 0! ban! oid: " + _objectId + " owner: " + activeChar.getName(), IllegalPlayerAction.PUNISH_KICK);
			return;
		}

		if (!item.isStackable() && (_count > 1))
		{
			Util.handleIllegalPlayerAction(activeChar, "[RequestDropItem] count > 1 but item is not stackable! ban! oid: " + _objectId + " owner: " + activeChar.getName(), IllegalPlayerAction.PUNISH_KICK);
			return;
		}

		if (activeChar.getAccessLevel() > 0 && activeChar.getAccessLevel() < Config.GM_TRANSACTION)
		{
			activeChar.sendMessage("Transactions are disabled for your Access Level.");
			return;
		}

		if (activeChar.isProcessingTransaction() || activeChar.isInStoreMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			return;
		}

		if (activeChar.isFishing())
		{
			// You can't mount, dismount, break and drop items while fishing
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DO_WHILE_FISHING_2));
			return;
		}

		// Cannot discard an equipped item or an item that the skill is consuming
		if (activeChar.isCastingNow())
		{
			if (item.isEquipped() || ((activeChar.getCurrentSkill() != null) && (activeChar.getCurrentSkill().getSkill().getItemConsumeId() == item.getItemId())))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISCARD_THIS_ITEM));
				return;
			}
		}
		
		if (L2Item.TYPE2_QUEST == item.getItem().getType2() && !activeChar.isGM())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISCARD_EXCHANGE_ITEM));
			return;
		}
		
		if (!activeChar.isInsideRadius(_x, _y, 150, false) || (Math.abs(_z - activeChar.getZ()) > 50))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISCARD_DISTANCE_TOO_FAR));
			return;
		}
		
		if (Config.DEBUG)
		{
			_log.fine("requested drop item " + _objectId + "(" + item.getCount() + ") at " + _x + "/" + _y + "/" + _z);
		}

		InventoryUpdate iu = !Config.FORCE_INVENTORY_UPDATE ? new InventoryUpdate() : null;
		
		if (item.isEquipped())
		{
			L2ItemInstance[] unequipped = activeChar.getInventory().unEquipItemInBodySlotAndRecord(item.getItem().getBodyPart());
			if (unequipped.length > 0)
			{
				if (iu != null)
				{
					for (L2ItemInstance i : unequipped)
					{
						iu.addModifiedItem(i);
					}
				}
				
				activeChar.broadcastUserInfo();
			}
		}

		L2ItemInstance droppedItem = activeChar.dropItem("Drop", item, _count, _x, _y, _z, null, false, false);
		if (droppedItem == null)
		{
			return;
		}
		
		// Send inventory update packet
		if (iu != null)
		{
			iu.addItem(item);
			activeChar.sendPacket(iu);
		}
		else
		{
			activeChar.sendPacket(new ItemList(activeChar, true));
		}

		if (Config.DEBUG)
		{
			_log.fine("dropping " + _objectId + " item(" + _count + ") at: " + _x + " " + _y + " " + _z);
		}

		if (activeChar.isGM())
		{
			String target = (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target");
			GMAudit.auditGMAction(activeChar.getName(), "drop", target, droppedItem.getItemId() + " - " + droppedItem.getItemName() + " - " + droppedItem.getObjectId());
		}

		if (droppedItem.getItemId() == Inventory.ADENA_ID && droppedItem.getCount() >= 1000000)
		{
			String msg = "Character (" + activeChar.getName() + ") has dropped (" + droppedItem.getCount() + ") adena at (" + _x + "," + _y + "," + _z + ")";
			_log.warning(msg);
			GmListTable.broadcastMessageToGMs(msg);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__12_REQUESTDROPITEM;
	}
}