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
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/29 23:15:33 $
 */
public class RequestGetItemFromPet extends L2GameClientPacket
{
	private static final String REQUESTGETITEMFROMPET__C__8C = "[C] 8C RequestGetItemFromPet";
	private static Logger _log = Logger.getLogger(RequestGetItemFromPet.class.getName());
	
	private int _objectId;
	private int _amount;
	@SuppressWarnings("unused")
	private int _unknown;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_amount = readD();
		_unknown = readD();// = 0 for most trades
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if ((player == null) || (player.getPet() == null) || !(player.getPet() instanceof L2PetInstance))
		{
			return;
		}
		
		L2PetInstance pet = (L2PetInstance) player.getPet();
		
		if (_amount < 0)
		{
			Util.handleIllegalPlayerAction(player, "[RequestGetItemFromPet] count < 0! ban! oid: " + _objectId + " owner: " + player.getName(), Config.DEFAULT_PUNISH);
			return;
		}
		
		L2ItemInstance item = pet.getInventory().getItemByObjectId(_objectId);
		if (item == null || _amount == 0)
		{
			return;
		}
		
		L2ItemInstance oldItem = pet.getInventory().getItemByItemId(item.getItemId());
		if (oldItem != null && ((long)oldItem.getCount() + _amount) > L2ItemInstance.getMaxItemCount(oldItem.getItemId()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_THE_LIMIT));
			return;
		}
		
		if (pet.transferItem("Transfer", _objectId, _amount, player.getInventory(), player, pet) == null)
		{
			_log.warning("Invalid Item transfer request: " + pet.getName() + "(pet) --> " + player.getName());
		}
	}
	
	@Override
	public String getType()
	{
		return REQUESTGETITEMFROMPET__C__8C;
	}
}