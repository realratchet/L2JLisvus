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
 * Format : (h) d [dS] h sub id d: number of manors [ d: id S: manor name ]
 * @author l3x
 */
public class ExSendManorList extends L2GameServerPacket
{
	private static final String _S__FE_1B_EXSENDMANORLIST = "[S] FE:1B ExSendManorList";

	private static final String[] _manorList =
	{
		"gludio",
		"dion",
		"giran",
		"oren",
		"aden",
		"innadril",
		"goddard"
	};

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#writeImpl()
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1B);
		writeD(_manorList.length);
		for (int i = 0; i < _manorList.length; i++)
		{
			writeD(i + 1);
			writeS(_manorList[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_1B_EXSENDMANORLIST;
	}
}