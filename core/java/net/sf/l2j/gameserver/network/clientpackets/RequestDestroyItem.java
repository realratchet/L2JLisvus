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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.PetDataTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.7.2.4.2.6 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestDestroyItem extends L2GameClientPacket
{
	private static final String _C__59_REQUESTDESTROYITEM = "[C] 59 RequestDestroyItem";
	private static Logger _log = Logger.getLogger(RequestDestroyItem.class.getName());

	private int _objectId;
	private int _count;

	@Override
	protected void readImpl()
	{
		_objectId = 0;
		_count = 0;

		try
		{
			_objectId = readD();
			_count = readD();
		}
		catch (Exception e)
		{
		}
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
			if (_count < 0)
			{
				Util.handleIllegalPlayerAction(activeChar, "[RequestDestroyItem] count < 0! ban! oid: " + _objectId + " owner: " + activeChar.getName(), Config.DEFAULT_PUNISH);
			}
			return;
		}

		int count = _count;

		if (activeChar.isProcessingTransaction() || activeChar.isInStoreMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			return;
		}
		
		// Cannot destroy while fishing
		if (activeChar.isFishing())
		{
			return;
		}

		L2ItemInstance itemToRemove = activeChar.getInventory().getItemByObjectId(_objectId);
		// if we cannot find requested item, its actually a cheat!
		if (itemToRemove == null)
		{
			return;
		}

		// Cannot discard item that the skill is consuming
		if (activeChar.isCastingNow())
		{
			if ((activeChar.getCurrentSkill() != null) && (activeChar.getCurrentSkill().getSkill().getItemConsumeId() == itemToRemove.getItemId()))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISCARD_THIS_ITEM));
				return;
			}
		}
		
		if (itemToRemove.isWear())
		{
			return;
		}

		if (!activeChar.isGM() && !itemToRemove.isDestroyable())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISCARD_THIS_ITEM));
			return;
		}

		if (!itemToRemove.isStackable() && (count > 1))
		{
			Util.handleIllegalPlayerAction(activeChar, "[RequestDestroyItem] count > 1 but item is not stackable! oid: " + _objectId + " owner: " + activeChar.getName(), Config.DEFAULT_PUNISH);
			return;
		}
		
		if (_count > itemToRemove.getCount())
		{
			count = itemToRemove.getCount();
		}

		if (itemToRemove.isEquipped())
		{
			L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInSlotAndRecord(itemToRemove.getEquipSlot());
			InventoryUpdate iu = new InventoryUpdate();
			for (L2ItemInstance element : unequiped)
			{
				iu.addModifiedItem(element);
			}
			activeChar.sendPacket(iu);
			activeChar.broadcastUserInfo();
		}

		int itemId = itemToRemove.getItemId();
		if (PetDataTable.isPetItem(itemId))
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?"))
			{
				if ((activeChar.getPet() != null) && (activeChar.getPet().getControlItemObjectId() == _objectId))
				{
					activeChar.getPet().unSummon(activeChar);
				}

				// if it's a pet control item, delete the pet
				statement.setInt(1, _objectId);
				statement.execute();
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "could not delete pet objectid: ", e);
			}
		}

		L2ItemInstance removedItem = activeChar.getInventory().destroyItem("Destroy", _objectId, count, activeChar, null);
		if (removedItem == null)
		{
			return;
		}
		
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

			activeChar.sendPacket(iu);

		}
		else
		{
			sendPacket(new ItemList(activeChar, true));
		}

		StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);

		L2World.getInstance().removeObject(removedItem);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__59_REQUESTDESTROYITEM;
	}
}