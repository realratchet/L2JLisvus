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

import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.GMViewCharacterInfo;
import net.sf.l2j.gameserver.network.serverpackets.GMViewHennaInfo;
import net.sf.l2j.gameserver.network.serverpackets.GMViewItemList;
import net.sf.l2j.gameserver.network.serverpackets.GMViewPledgeInfo;
import net.sf.l2j.gameserver.network.serverpackets.GMViewQuestList;
import net.sf.l2j.gameserver.network.serverpackets.GMViewWarehouseWithdrawList;

/**
 * This class ...
 * @version $Revision: 1.1.2.2.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestGMCommand extends L2GameClientPacket
{
	private static final String _C__6E_REQUESTGMCOMMAND = "[C] 6e RequestGMCommand";
	private static Logger _log = Logger.getLogger(RequestGMCommand.class.getName());
	
	private String _targetName;
	private int _command;
	
	@Override
	protected void readImpl()
	{
		_targetName = readS();
		_command = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!activeChar.isGM())
		{
			return;
		}
		
		L2PcInstance target = L2World.getInstance().getPlayer(_targetName);
		if (target == null)
		{
			return;
		}
		
		switch (_command)
		{
			case 1: // player status
			{
				sendPacket(new GMViewCharacterInfo(target));
				sendPacket(new GMViewHennaInfo(target));
				break;
			}
			case 2: // player clan
			{
				if (target.getClan() != null)
				{
					sendPacket(new GMViewPledgeInfo(target.getClan(), target));
				}
				
				break;
			}
			case 3: // player skills
			{
				activeChar.sendSkillList(target);
				break;
			}
			case 4: // player quests
			{
				sendPacket(new GMViewQuestList(target));
				break;
			}
			case 5: // player inventory
			{
				sendPacket(new GMViewItemList(target));
				sendPacket(new GMViewHennaInfo(target));
				break;
			}
			case 6: // player warehouse
			{
				// defective packet
				sendPacket(new GMViewWarehouseWithdrawList(target));
				break;
			}
			default:
				_log.warning(getClass().getSimpleName() +": Unknown command request from character " + activeChar.getName());
				break;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__6E_REQUESTGMCOMMAND;
	}
}