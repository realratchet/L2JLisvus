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

import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Experience;

/**
 * This class ...
 * @version $Revision: 1.4.2.5.2.6 $ $Date: 2005/03/27 15:29:39 $
 */
public class CharSelected extends L2GameServerPacket
{
	// SdSddddddddddffddddddddddddddddddddddddddddddddddddddddd d
	private static final String _S__21_CHARSELECTED = "[S] 15 CharSelected";
	private final L2PcInstance _cha;
	private final int _sessionId;
	
	/**
	 * @param cha 
	 * @param sessionId 
	 */
	public CharSelected(L2PcInstance cha, int sessionId)
	{
		_cha = cha;
		_sessionId = sessionId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x15);
		
		writeS(_cha.getName());
		writeD(_cha.getCharId()); // ??
		writeS(_cha.getTitle());
		writeD(_sessionId);
		writeD(_cha.getClanId());
		writeD(0x00); // ??
		writeD(_cha.getAppearance().getSex() ? 1 : 0);
		writeD(_cha.getRace().ordinal());
		writeD(_cha.getClassId().getId());
		writeD(0x01); // active ??
		writeD(_cha.getX());
		writeD(_cha.getY());
		writeD(_cha.getZ());
		
		writeF(_cha.getCurrentHp());
		writeF(_cha.getCurrentMp());
		writeD(_cha.getSp());
		writeD(Experience.getVisualExp(_cha.getLevel(), _cha.getExp()));
		writeD(_cha.getLevel());
		writeD(_cha.getKarma()); // thx evill33t
		writeD(0x0); // ?
		writeD(_cha.getINT());
		writeD(_cha.getSTR());
		writeD(_cha.getCON());
		writeD(_cha.getMEN());
		writeD(_cha.getDEX());
		writeD(_cha.getWIT());
		
		for (int i = 0; i < 30; i++)
		{
			writeD(0x00);
		}
		
		// writeD(0); //c3
		// writeD(0); //c3
		// writeD(0); //c3
		
		writeD(0x00); // c3 work
		writeD(0x00); // c3 work
		
		// extra info
		writeD(GameTimeController.getInstance().getGameTime()); // in-game time
		
		writeD(0x00); //
		
		writeD(0x00); // c3
		
		writeD(0x00); // c3 InspectorBin
		writeD(0x00); // c3
		writeD(0x00); // c3
		writeD(0x00); // c3
		
		writeD(0x00); // c3 InspectorBin for 528 client
		writeD(0x00); // c3
		writeD(0x00); // c3
		
		writeD(0x00); // c3
		writeD(0x00); // c3
		writeD(0x00); // c3
		writeD(0x00); // c3
		writeD(0x00); // c3
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__21_CHARSELECTED;
	}
}