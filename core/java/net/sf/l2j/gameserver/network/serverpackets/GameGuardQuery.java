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
package net.sf.l2j.gameserver.network.serverpackets;

/**
 * @author zabbix Lets drink to code!
 */
public class GameGuardQuery extends L2GameServerPacket
{
	private static final String _S__F9_GAMEGUARDQUERY = "[S] F9 GameGuardQuery";
	
	public GameGuardQuery()
	{
		// Lets make user as gg-unauthorized
		// We will set him as gg-ok after reply from client
		// or kick
		if (getClient() != null)
		{
			getClient().setGameGuardOk(false);
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xf9);
	}
	
	@Override
	public String getType()
	{
		return _S__F9_GAMEGUARDQUERY;
	}
}