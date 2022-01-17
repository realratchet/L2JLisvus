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
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2EtcItemType;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.5 $ $Date: 2005/03/29 23:15:33 $
 */
public class RequestGiveItemToPet extends L2GameClientPacket
{
	private static final String REQUESTCIVEITEMTOPET__C__8B = "[C] 8B RequestGiveItemToPet";
	private static Logger _log = Logger.getLogger(RequestGetItemFromPet.class.getName());
	
	private int _objectId;
	private int _amount;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_amount = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if ((player == null) || (player.getPet() == null) || !(player.getPet() instanceof L2PetInstance))
		{
			return;
		}
		
		if (player.getActiveEnchantItem() != null)
		{
			return;
		}
		
		// Alt game - Karma punishment
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_TRADE && player.getKarma() > 0)
		{
			return;
		}
		
		if (player.isInStoreMode())
		{
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			return;
		}
		
		if (player.isMounted())
		{
			return;
		}
		
		if (player.getAccessLevel() > 0 && player.getAccessLevel() < Config.GM_TRANSACTION)
		{
			player.sendMessage("Transactions are disabled for your Access Level.");
			return;
		}
		
		L2ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		if ((item == null) || (_amount == 0))
		{
			return;
		}
		
		if (item.getItemType() == L2EtcItemType.QUEST)
		{
			return;
		}
		
		if (item.isEquipped())
		{
			return;
		}
		
		if (!item.isDropable() || !item.isDestroyable() || !item.isTradeable())
		{
			return;
		}

		if (item.isHeroItem())
		{
			return;
		}
		
		int itemId = item.getItemId();
		if ((itemId >= 6834) && (itemId <= 6841))
		{
			return;
		}
		
		L2PetInstance pet = (L2PetInstance) player.getPet();
		if (pet.isDead())
		{
			sendPacket(new SystemMessage(SystemMessage.CANNOT_GIVE_ITEMS_TO_DEAD_PET));
			return;
		}
		
		if (_amount < 0)
		{
			return;
		}
		
		if (!pet.getInventory().validateCapacity(item))
		{
			pet.getOwner().sendMessage("Your pet cannot carry any more items.");
			return;
		}
		
		if (!pet.getInventory().validateWeight(item, _amount))
		{
			pet.getOwner().sendMessage("Your pet is overweight and cannot carry any more items.");
			return;
		}
		
		L2ItemInstance oldItem = pet.getInventory().getItemByItemId(item.getItemId());
		if (oldItem != null && ((long)oldItem.getCount() + _amount) > L2ItemInstance.getMaxItemCount(oldItem.getItemId()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_THE_LIMIT));
			return;
		}
		
		if (player.transferItem("Transfer", _objectId, _amount, pet.getInventory(), pet) == null)
		{
			_log.warning("Invalid Item transfer request: " + pet.getName() + "(pet) --> " + player.getName());
		}
	}
	
	@Override
	public String getType()
	{
		return REQUESTCIVEITEMTOPET__C__8B;
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return true;
	}
}