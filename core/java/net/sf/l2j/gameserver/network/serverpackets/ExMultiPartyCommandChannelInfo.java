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

import net.sf.l2j.gameserver.model.L2CommandChannel;
import net.sf.l2j.gameserver.model.L2Party;

/**
 * @author chris_00
 */
public class ExMultiPartyCommandChannelInfo extends L2GameServerPacket
{
	private static final String _S__FE_30_EXPARTYCHANNELINFO = "[S] FE:30 ExMultiPartyCommandChannelInfo";
	
	private final L2CommandChannel _channel;
	
	public ExMultiPartyCommandChannelInfo(L2CommandChannel channel)
	{
		_channel = channel;
	}
	
	@Override
	protected final void writeImpl()
	{
		if (_channel == null)
		{
			return;
		}
		
		writeC(0xfe);
		writeH(0x30);
		
		writeS(_channel.getChannelLeader().getName());
		writeD(_channel.getMemberCount());
		
		writeD(_channel.getParties().size());
		for (L2Party p : _channel.getParties())
		{
			writeS(p.getPartyMembers().get(0).getName());
			writeD(p.getMemberCount());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_30_EXPARTYCHANNELINFO;
	}
}