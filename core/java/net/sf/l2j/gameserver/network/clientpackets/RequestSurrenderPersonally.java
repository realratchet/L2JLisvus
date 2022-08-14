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
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class RequestSurrenderPersonally extends L2GameClientPacket
{
	private static final String _C__69_REQUESTSURRENDERPERSONALLY = "[C] 69 RequestSurrenderPersonally";
	private static Logger _log = Logger.getLogger(RequestSurrenderPledgeWar.class.getName());
	
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
		
		if (Config.DEBUG)
		{
			_log.info("RequestSurrenderPersonally by " + getClient().getActiveChar().getName() + " with " + _pledgeName);
		}
		
		L2Clan _clan = getClient().getActiveChar().getClan();
		L2Clan clan = ClanTable.getInstance().getClanByName(_pledgeName);
		
		if (_clan == null)
		{
			return;
		}
		
		if (clan == null)
		{
			player.sendMessage("No such clan.");
			player.sendPacket(new ActionFailed());
			return;
		}
		
		if (!_clan.isAtWarWith(clan.getClanId()) || (player.getWantsPeace() == 1))
		{
			player.sendMessage("You aren't at war with this clan.");
			player.sendPacket(new ActionFailed());
			return;
		}
		
		player.setWantsPeace(1);
		player.deathPenalty(false, false, false);
		SystemMessage sm = new SystemMessage(SystemMessage.YOU_HAVE_PERSONALLY_SURRENDERED_TO_THE_S1_CLAN);
		sm.addString(_pledgeName);
		player.sendPacket(sm);
		sm = null;
		ClanTable.getInstance().checkSurrender(_clan, clan);
	}
	
	@Override
	public String getType()
	{
		return _C__69_REQUESTSURRENDERPERSONALLY;
	}
}