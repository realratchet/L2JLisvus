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

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * sample 5F 01 00 00 00 format cdd
 * @version $Revision: 1.7.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestAnswerJoinAlly extends L2GameClientPacket
{
	private static final String _C__83_REQUESTANSWERJOINALLY = "[C] 83 RequestAnswerJoinAlly";
	// private static Logger _log = Logger.getLogger(RequestAnswerJoinAlly.class.getName());

	private int _response;

	@Override
	protected void readImpl()
	{
		_response = readD();
	}

	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		L2PcInstance requestor = activeChar.getRequest().getPartner();
		if (requestor == null)
		{
			return;
		}

		if (_response == 0)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_DID_NOT_RESPOND_TO_ALLY_INVITATION));
			requestor.sendPacket(new SystemMessage(SystemMessage.NO_RESPONSE_TO_ALLY_INVITATION));
		}
		else
		{
			if (!(requestor.getRequest().getRequestPacket() instanceof RequestJoinAlly))
			{
				return;
			}

			L2Clan clan = requestor.getClan();

			if (!clan.CheckAllyJoinCondition(requestor, activeChar))
			{
				return;
			}

			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ACCEPTED_ALLIANCE));

			activeChar.getClan().setAllyId(clan.getAllyId());
			activeChar.getClan().setAllyName(clan.getAllyName());
			activeChar.getClan().setAllyPenaltyExpiryTime(0, 0);
			activeChar.getClan().setAllyCrestId(clan.getAllyCrestId());
			activeChar.getClan().updateClanInDB();

		}
		activeChar.getRequest().onRequestResponse();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__83_REQUESTANSWERJOINALLY;
	}
}