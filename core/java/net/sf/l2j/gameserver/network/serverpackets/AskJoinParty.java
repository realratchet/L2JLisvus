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

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * sample
 * <p>
 * 4b c1 b2 e0 4a 00 00 00 00
 * <p>
 * format cdd
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class AskJoinParty extends L2GameServerPacket
{
	private static final String _S__4B_ASKJOINPARTY_0X4B = "[S] 39 AskJoinParty 0x4b";
	// private static Logger _log = Logger.getLogger(AskJoinParty.class.getName());
	
	private final String _requestorName;
	private int _itemDistribution;
	
	/**
	 * @param requestor 
	 */
	public AskJoinParty(L2PcInstance requestor)
	{
		_requestorName = requestor.getName();
		if (requestor.isInParty())
		{
			_itemDistribution = requestor.getParty().getLootDistribution();
		}
		else
		{
			_itemDistribution = requestor.getLootInvitation();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x39);
		writeS(_requestorName);
		writeD(_itemDistribution);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__4B_ASKJOINPARTY_0X4B;
	}
}