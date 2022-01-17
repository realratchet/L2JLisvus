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
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListDelete;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestOustPledgeMember extends L2GameClientPacket
{
	private static final String _C__27_REQUESTOUSTPLEDGEMEMBER = "[C] 27 RequestOustPledgeMember";
	static Logger _log = Logger.getLogger(RequestOustPledgeMember.class.getName());
	
	private String _target;

	@Override
	protected void readImpl()
	{
		_target = readS();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();

		// is player leader of the clan ?
		if ((activeChar == null) || !activeChar.isClanLeader())
		{
			return;
		}
		
		L2Clan clan = activeChar.getClan();
		if (clan == null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_A_CLAN_MEMBER));
			return;
		}
		
		L2ClanMember member = clan.getClanMember(_target);
		if ((member == null) || (member.getObjectId() == activeChar.getObjectId()))
		{
			return;
		}
		
		if (member.isOnline())
		{
			
			L2PcInstance player = member.getPlayerInstance();
			if (player.isInCombat())
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_MEMBER_CANNOT_BE_DISMISSED_DURING_COMBAT));
				return;
			}
			
			player.sendPacket(new SystemMessage(SystemMessage.CLAN_MEMBERSHIP_TERMINATED));
		}
		
		// this also updates the database
		clan.removeClanMember(member.getObjectId(), System.currentTimeMillis() + (Config.ALT_CLAN_JOIN_DAYS * 86400000)); // 24*60*60*1000 = 86400000
		clan.setCharPenaltyExpiryTime(System.currentTimeMillis() + (Config.ALT_CLAN_JOIN_DAYS * 86400000)); // 24*60*60*1000 = 86400000
		clan.updateClanInDB();
		
		SystemMessage msg = new SystemMessage(SystemMessage.CLAN_MEMBER_S1_EXPELLED);
		msg.addString(member.getName());
		clan.broadcastToOnlineMembers(msg);
		msg = null;
		activeChar.sendPacket(new SystemMessage(309));
		
		clan.broadcastToOnlineMembers(new PledgeShowMemberListDelete(_target));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__27_REQUESTOUSTPLEDGEMEMBER;
	}
}
