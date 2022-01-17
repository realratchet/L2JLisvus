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

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.AskJoinAlly;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestJoinAlly extends L2GameClientPacket
{
	private static final String _C__82_REQUESTJOINALLY = "[C] 82 RequestJoinAlly";
	// private static Logger _log = Logger.getLogger(RequestJoinAlly.class.getName());
	
	private int _id;
	
	@Override
	protected void readImpl()
	{
		_id = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		L2PcInstance target = L2World.getInstance().getPlayer(_id);
		if (target == null)
		{
			return;
		}
		
		L2Clan clan = activeChar.getClan();
		if (clan == null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_A_CLAN_MEMBER));
			return;
		}
		
		if (!clan.CheckAllyJoinCondition(activeChar, target))
		{
			return;
		}
		
		if (!activeChar.getRequest().setRequest(target, this))
		{
			return;
		}
		
		SystemMessage sm = new SystemMessage(SystemMessage.S1_ALLIANCE_LEADER_OF_S2_REQUESTED_ALLIANCE);
		sm.addString(activeChar.getName());
		sm.addString(clan.getAllyName());
		target.sendPacket(sm);
		sm = null;
		
		target.sendPacket(new AskJoinAlly(activeChar.getObjectId(), activeChar.getName(), clan.getAllyName()));
	}
	
	@Override
	public String getType()
	{
		return _C__82_REQUESTJOINALLY;
	}
}