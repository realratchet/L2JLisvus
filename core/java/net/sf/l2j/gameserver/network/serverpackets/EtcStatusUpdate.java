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
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/* Packet format: F3 XX000000 YY000000 ZZ000000 */

/**
 * @author Luca Baldi
 */
public class EtcStatusUpdate extends L2GameServerPacket
{
	private static final String _S__F3_ETCSTATUSUPDATE = "[S] F3 EtcStatusUpdate";

	private final L2PcInstance _activeChar;

	public EtcStatusUpdate(L2PcInstance activeChar)
	{
		_activeChar = activeChar;
	}

	/**
	 * @see net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket#writeImpl()
	 */
	@Override
	protected final void writeImpl()
	{

		writeC(0xF3); // several icons to a separate line (0 = disabled)

		writeD(_activeChar.getCharges()); // 1-7 increase force, lvl
		writeD(_activeChar.getWeightPenalty()); // 1-4 weight penalty, lvl (1=50%, 2=66.6%, 3=80%, 4=100%)
		writeD((_activeChar.getMessageRefusal() || _activeChar.isChatBanned()) ? 1 : 0); // 1 = block all chat
		writeD(_activeChar.isInsideZone(L2Character.ZONE_DANGER_AREA) ? 1 : 0); // 1 = danger area
		writeD(Math.min(_activeChar.getExpertisePenalty(), 1)); // 1 = grade penalty
	}

	/**
	 * @see net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__F3_ETCSTATUSUPDATE;
	}
}