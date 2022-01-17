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

/**
 * @author zabbix Lets drink to code! Unknown Packet:ca 0000: 45 00 01 00 1e 37 a2 f5 00 00 00 00 00 00 00 00 E....7..........
 */

public class GameGuardReply extends L2GameClientPacket
{
	private static final String _C__CA_GAMEGUARDREPLY = "[C] CA GameGuardReply";
	private static Logger _log = Logger.getLogger(GameGuardReply.class.getName());

	@Override
	protected void readImpl()
	{
	}

	@Override
	public void runImpl()
	{
		_log.warning("GameGuard reply by account " + getClient().getAccountName());
		getClient().setGameGuardOk(true);
	}

	@Override
	public String getType()
	{
		return _C__CA_GAMEGUARDREPLY;
	}
}