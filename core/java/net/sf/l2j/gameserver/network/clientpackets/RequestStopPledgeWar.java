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

import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.taskmanager.AttackStanceTaskManager;

public class RequestStopPledgeWar extends L2GameClientPacket
{
	private static final String _C__4F_REQUESTSTOPPLEDGEWAR = "[C] 4F RequestStopPledgeWar";
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(RequestStopPledgeWar.class.getName());
	
	private String _pledgeName;
	
	@Override
	protected void readImpl()
	{
		_pledgeName = readS();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		L2Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}

		if ((player.getClanPrivileges() & L2Clan.CP_CL_CLAN_WAR) != L2Clan.CP_CL_CLAN_WAR)
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
			return;
		}
		
		L2Clan requestedClan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (requestedClan == null)
		{
			return;
		}
		
		if (!clan.isAtWarWith(requestedClan.getClanId()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.NOT_INVOLVED_IN_WAR));
			return;
		}

		for (L2PcInstance member : clan.getOnlineMembers(0))
		{
			if (AttackStanceTaskManager.getInstance().getAttackStanceTask(member))
			{
				player.sendPacket(new SystemMessage(SystemMessage.CANT_STOP_CLAN_WAR_WHILE_IN_COMBAT));
				return;
			}
		}
		
		ClanTable.getInstance().deleteClanWars(clan.getClanId(), requestedClan.getClanId());

		for (L2PcInstance member : clan.getOnlineMembers(0)) {
			member.broadcastUserInfo();
		}
		for (L2PcInstance member : requestedClan.getOnlineMembers(0)) {
			member.broadcastUserInfo();
		}
	}
	
	@Override
	public String getType()
	{
		return _C__4F_REQUESTSTOPPLEDGEWAR;
	}
}