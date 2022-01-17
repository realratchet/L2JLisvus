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
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.JoinPledge;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListAdd;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListAll;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestAnswerJoinPledge extends L2GameClientPacket
{
	private static final String _C__25_REQUESTANSWERJOINPLEDGE = "[C] 25 RequestAnswerJoinPledge";
	
	private int _answer;
	
	@Override
	protected void readImpl()
	{
		_answer = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		L2PcInstance requestor = activeChar.getRequest().getPartner();
		if (requestor == null)
		{
			return;
		}
		
		if (_answer == 0)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.YOU_DID_NOT_RESPOND_TO_S1_CLAN_INVITATION);
			sm.addString(requestor.getName());
			activeChar.sendPacket(sm);
			sm = null;
			sm = new SystemMessage(SystemMessage.S1_DID_NOT_RESPOND_TO_CLAN_INVITATION);
			sm.addString(activeChar.getName());
			requestor.sendPacket(sm);
			sm = null;
		}
		else
		{
			if (!(requestor.getRequest().getRequestPacket() instanceof RequestJoinPledge))
			{
				return;
			}
			
			L2Clan clan = requestor.getClan();
			if (!clan.CheckClanJoinCondition(requestor, activeChar))
			{
				return;
			}
			
			activeChar.sendPacket(new JoinPledge(requestor.getClanId()));
			
			// this also updates the database
			clan.addClanMember(activeChar);
			
			activeChar.setClan(clan);
			activeChar.setClanPrivileges(0);
			// Set siege status if any
	        activeChar.setSiegeState((byte) requestor.getSiegeState());
	        activeChar.setSiegeSide(requestor.getSiegeSide());
			
			SystemMessage sm = new SystemMessage(SystemMessage.ENTERED_THE_CLAN);
			activeChar.sendPacket(sm);
			
			sm = new SystemMessage(SystemMessage.S1_HAS_JOINED_CLAN);
			sm.addString(activeChar.getName());
			
			clan.broadcastToOnlineMembers(sm);
			
			sm = null;
			
			clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListAdd(activeChar), activeChar);
			
			clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));
			
			// this activates the clan tab for the new member
			activeChar.sendPacket(new PledgeShowMemberListAll(clan, activeChar));
			activeChar.setClanJoinExpiryTime(0);
			
			activeChar.broadcastUserInfo();
			
		}
		
		activeChar.getRequest().onRequestResponse();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__25_REQUESTANSWERJOINPLEDGE;
	}
}