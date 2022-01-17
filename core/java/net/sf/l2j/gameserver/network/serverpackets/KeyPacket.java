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
 * This class ...
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class KeyPacket extends L2GameServerPacket
{
	private static final String _S__01_KEYPACKET = "[S] 01 KeyPacket";

	private final byte[] _key;
	private final byte _protocolOk;

	public KeyPacket(byte key[], byte protocolOk)
	{
		_key = key;
		_protocolOk = protocolOk;
	}

	@Override
	public void writeImpl()
	{
		writeC(0x00);
		writeC(_protocolOk);
		writeB(_key);
		writeD(0x01);
		writeD(0x01);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__01_KEYPACKET;
	}
}