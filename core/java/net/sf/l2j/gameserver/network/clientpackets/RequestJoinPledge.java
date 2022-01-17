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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.AskJoinPledge;

/**
 * This class ...
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestJoinPledge extends L2GameClientPacket
{
	private static final String _C__24_REQUESTJOINPLEDGE = "[C] 24 RequestJoinPledge";
	static Logger _log = Logger.getLogger(RequestJoinPledge.class.getName());
	
	private int _target;
	
	@Override
	protected void readImpl()
	{
		_target = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		L2PcInstance newMember = L2World.getInstance().getPlayer(_target);
		if (newMember == null)
		{
			return;
		}
		
		if (!activeChar.getClan().CheckClanJoinCondition(activeChar, newMember))
		{
			return;
		}
		
		if (!activeChar.getRequest().setRequest(newMember, this))
		{
			return;
		}
		
		newMember.sendPacket(new AskJoinPledge(activeChar.getObjectId(), activeChar.getClan().getName()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__24_REQUESTJOINPLEDGE;
	}
}