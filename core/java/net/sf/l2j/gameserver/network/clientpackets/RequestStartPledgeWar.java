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
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class RequestStartPledgeWar extends L2GameClientPacket
{
	private static final String _C__4D_REQUESTSTARTPLEDGEWAR = "[C] 4D RequestStartPledgewar";
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(RequestStartPledgeWar.class.getName());
	
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
		
		L2Clan _clan = getClient().getActiveChar().getClan();
		if (_clan == null)
		{
			return;
		}
		
		if ((_clan.getLevel() < 3) || (_clan.getMembersCount() < Config.ALT_CLAN_MEMBERS_FOR_WAR))
		{
			SystemMessage sm = new SystemMessage(1564);
			player.sendPacket(sm);
			player.sendPacket(new ActionFailed());
			sm = null;
			return;
		}
		
		if ((player.getClanPrivileges() & L2Clan.CP_CL_CLAN_WAR) != L2Clan.CP_CL_CLAN_WAR)
		{
			player.sendMessage("You are not authorized to manage clan wars.");
			player.sendPacket(new ActionFailed());
			return;
		}
		
		L2Clan clan = ClanTable.getInstance().getClanByName(_pledgeName);
		if ((clan == null) || (clan == _clan))
		{
			player.sendMessage("Invalid Clan.");
			player.sendPacket(new ActionFailed());
			return;
		}
		
		if ((_clan.getAllyId() == clan.getAllyId()) && (_clan.getAllyId() != 0))
		{
			SystemMessage sm = new SystemMessage(1569);
			player.sendPacket(sm);
			player.sendPacket(new ActionFailed());
			sm = null;
			return;
		}
		
		if ((clan.getLevel() < 3) || (clan.getMembersCount() < Config.ALT_CLAN_MEMBERS_FOR_WAR))
		{
			SystemMessage sm = new SystemMessage(1564);
			player.sendPacket(sm);
			player.sendPacket(new ActionFailed());
			sm = null;
			return;
		}
		
		ClanTable.getInstance().storeClanWars(player.getClanId(), clan.getClanId());
		for (L2PcInstance cha : L2World.getInstance().getAllPlayers())
		{
			if ((cha.getClan() == player.getClan()) || (cha.getClan() == clan))
			{
				cha.broadcastUserInfo();
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__4D_REQUESTSTARTPLEDGEWAR;
	}
}