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
 * sample
 * <p>
 * 7d c1 b2 e0 4a 00 00 00 00
 * <p>
 * format cdd
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class AskJoinAlly extends L2GameServerPacket
{
	private static final String _S__a8_ASKJOINALLY_0Xa8 = "[S] a8 AskJoinAlly 0xa8";
	// private static Logger _log = Logger.getLogger(AskJoinAlly.class.getName());

	private final int _requestorId;
	private final String _requestorName;
	private final String _requestorAllyName;

	/**
	 * @param requestorId 
	 * @param requestorName 
	 * @param requestorAllyName 
	 *
	 */
	public AskJoinAlly(int requestorId, String requestorName, String requestorAllyName)
	{
		_requestorId = requestorId;
		_requestorName = requestorName;
		_requestorAllyName = requestorAllyName;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xa8);
		writeD(_requestorId);
		writeS(_requestorName);
		writeS("");
		writeS(_requestorAllyName);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__a8_ASKJOINALLY_0Xa8;
	}
}