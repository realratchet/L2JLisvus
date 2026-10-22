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

public class RequestReplySurrenderPledgeWar extends L2GameClientPacket
{
	private static final String _C__52_REQUESTREPLYSURRENDERPLEDGEWAR = "[C] 52 RequestReplySurrenderPledgeWar";
	// private static Logger _log = Logger.getLogger(RequestReplySurrenderPledgeWar.class.getName());
	
	private int _answer;
	
	@Override
	protected void readImpl()
	{
		readS();
		_answer = readD();
	}
	
	@Override
	public void runImpl()
	{
		// War declaration, stop and surrender are applied directly by their request packets;
		// no reply is ever expected, so a client reply must not change any state.
	}
	
	@Override
	public String getType()
	{
		return _C__52_REQUESTREPLYSURRENDERPLEDGEWAR;
	}
}