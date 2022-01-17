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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.PcFreight;
import net.sf.l2j.gameserver.network.serverpackets.PackageSendableList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Format: (c)d d: char object id (?)
 * @author -Wooden-
 */
public class RequestPackageSendableItemList extends L2GameClientPacket
{
	private static final String _C_9E_REQUESTPACKAGESENDABLEITEMLIST = "[C] 9E RequestPackageSendableItemList";
	private int _objectID;
	
	@Override
	protected void readImpl()
	{
		_objectID = readD();
	}
	
	/**
	 * @see net.sf.l2j.gameserver.network.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	public void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		// Possible exploit
		if (activeChar.getObjectId() == _objectID)
		{
			return;
		}
		
		L2ItemInstance[] items = activeChar.getInventory().getAvailableItems(true);
		activeChar.setActiveWarehouse(new PcFreight(null));
		
		// Build list
		activeChar.sendPacket(new PackageSendableList(items, _objectID));
		
		if (!Config.ALT_GAME_FREIGHTS)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.PACKAGES_CAN_ONLY_BE_RETRIEVED_HERE));
		}
	}

	@Override
	public String getType()
	{
		return _C_9E_REQUESTPACKAGESENDABLEITEMLIST;
	}
}