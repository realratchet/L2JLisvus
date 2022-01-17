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

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExAutoSoulShot;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.0.0.0 $ $Date: 2005/07/11 15:29:30 $
 */
public class RequestAutoSoulShot extends L2GameClientPacket
{
	private static final String _C__CF_REQUESTAUTOSOULSHOT = "[C] CF RequestAutoSoulShot";
	
	// format cd
	private int _itemId;
	private int _type; // 1 = on : 0 = off;
	
	@Override
	protected void readImpl()
	{
		_itemId = readD();
		_type = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (activeChar.isInStoreMode() || activeChar.isDead() || activeChar.getActiveRequester() != null)
		{
			return;
		}
		
		L2ItemInstance item = activeChar.getInventory().getItemByItemId(_itemId);
		if (item == null)
		{
			return;
		}
		
		if (_type == 1)
		{
			// Fishing shots are not automatic on retail
			if ((_itemId < 6535) || (_itemId > 6540))
			{
				// Attempt to charge first shot on activation
				if ((_itemId == 6645) || (_itemId == 6646) || (_itemId == 6647))
				{
					L2Summon summon = activeChar.getPet();
					if (summon != null)
					{
						activeChar.addAutoSoulShot(_itemId);
						ExAutoSoulShot atk = new ExAutoSoulShot(_itemId, _type);
						activeChar.sendPacket(atk);
						
						// Start the auto soulshot use
						SystemMessage sm = new SystemMessage(SystemMessage.USE_OF_S1_WILL_BE_AUTO);
						sm.addString(item.getItemName());
						activeChar.sendPacket(sm);
						
						summon.rechargeShots(true, true);
					}
					else
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.NO_PET_TO_AUTOMATE_USE));
					}
				}
				else
				{
					if ((_itemId >= 3947) && (_itemId <= 3952) && activeChar.isInOlympiadMode())
					{
						SystemMessage sm = new SystemMessage(SystemMessage.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
						sm.addString(item.getItemName());
						activeChar.sendPacket(sm);
					}
					else
					{
						activeChar.addAutoSoulShot(_itemId);
						ExAutoSoulShot atk = new ExAutoSoulShot(_itemId, _type);
						activeChar.sendPacket(atk);
						
						// Start the auto soulshot use
						SystemMessage sm = new SystemMessage(SystemMessage.USE_OF_S1_WILL_BE_AUTO);
						sm.addString(item.getItemName());
						activeChar.sendPacket(sm);
						
						activeChar.rechargeShots(true, true);
					}
				}
			}
			
		}
		else if (_type == 0)
		{
			activeChar.removeAutoSoulShot(_itemId);
			ExAutoSoulShot atk = new ExAutoSoulShot(_itemId, _type);
			activeChar.sendPacket(atk);
			
			// Cancel the auto soulshot use
			SystemMessage sm = new SystemMessage(SystemMessage.AUTO_USE_OF_S1_CANCELLED);
			sm.addString(item.getItemName());
			activeChar.sendPacket(sm);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__CF_REQUESTAUTOSOULSHOT;
	}
}