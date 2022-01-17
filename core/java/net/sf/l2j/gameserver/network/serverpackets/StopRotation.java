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
 * @version $Revision: 1.2.2.1.2.4 $ $Date: 2005/03/27 15:29:57 $
 */
public class StopRotation extends L2GameServerPacket
{
	private static final String _S__78_STOPROTATION = "[S] 63 StopRotation";
	
	private final int _charId;
	private final int _degree;
	private final int _speed;
	
	public StopRotation(int objectId, int degree, int speed)
	{
		_charId = objectId;
		_degree = degree;
		_speed = speed;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x63);
		writeD(_charId);
		writeD(_degree);
		writeD(_speed);
		writeC(0); // ?
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__78_STOPROTATION;
	}
}