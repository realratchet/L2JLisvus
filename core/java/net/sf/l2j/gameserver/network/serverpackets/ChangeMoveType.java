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

import net.sf.l2j.gameserver.model.L2Character;

/**
 * sample 0000: 3e 2a 89 00 4c 01 00 00 00 .|... format dd
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:57 $
 */
public class ChangeMoveType extends L2GameServerPacket
{
	private static final String _S__3E_CHANGEMOVETYPE = "[S] 3E ChangeMoveType";
	
	private final int _chaId;
	private final boolean _running;
	
	public ChangeMoveType(L2Character cha)
	{
		_chaId = cha.getObjectId();
		_running = cha.isRunning();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x2e);
		writeD(_chaId);
		writeD(_running ? 1 : 0);
		writeD(0); // c2
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__3E_CHANGEMOVETYPE;
	}
}