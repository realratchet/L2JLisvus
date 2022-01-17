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
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListDelete;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestWithdrawalPledge extends L2GameClientPacket
{
	private static final String _C__26_REQUESTWITHDRAWALPLEDGE = "[C] 26 RequestWithdrawalPledge";
	static Logger _log = Logger.getLogger(RequestWithdrawalPledge.class.getName());
	
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		// is player in a clan?
		if (activeChar == null)
		{
			return;
		}
		
		L2Clan clan = activeChar.getClan();
		if (clan == null)
		
		{
			
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_A_CLAN_MEMBER));
			
			return;
			
		}
		
		if (activeChar.isClanLeader())
		{
			return;
		}
		
		if (activeChar.isInCombat())
		{
			activeChar.sendPacket(new SystemMessage(1116));
			return;
		}
		
		// this also updates the database
		clan.removeClanMember(activeChar.getObjectId(), System.currentTimeMillis() + (Config.ALT_CLAN_JOIN_DAYS * 86400000L)); // 24*60*60*1000 = 86400000
		SystemMessage sm = new SystemMessage(223);
		sm.addString(activeChar.getName());
		clan.broadcastToOnlineMembers(sm);
		sm = null;
		// Remove player from memberlist
		clan.broadcastToOnlineMembers(new PledgeShowMemberListDelete(activeChar.getName()));
		
		activeChar.sendPacket(new SystemMessage(197));
		activeChar.sendPacket(new SystemMessage(232));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__26_REQUESTWITHDRAWALPLEDGE;
	}
}