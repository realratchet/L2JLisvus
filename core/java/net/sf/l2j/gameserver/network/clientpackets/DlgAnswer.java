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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Dezmond_snz Format: cddd
 */
public class DlgAnswer extends L2GameClientPacket
{
	private static final String _C__C5_DLGANSWER = "[C] C5 DlgAnswer";
	private static Logger _log = Logger.getLogger(DlgAnswer.class.getName());
	
	private int _messageId;
	private int _answer;
	
	@Override
	protected void readImpl()
	{
		_messageId = readD();
		_answer = readD();
	}

	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (Config.DEBUG)
		{
			_log.fine(getType() + ": Answer accepted. Message ID " + _messageId + ", asnwer " + _answer);
		}

		switch (_messageId)
		{
			case SystemMessage.RESSURECTION_REQUEST:
				activeChar.reviveAnswer(_answer);
				break;
			case 1140:
				activeChar.gatesAnswer(_answer, 1);
				break;
			case 1141:
				activeChar.gatesAnswer(_answer, 0);
				break;
			case SystemMessage.S1_S2:
				// Engage request
				if (activeChar.isEngageRequested())
				{
					activeChar.engageAnswer(_answer);
				}
				break;
		}
	}

	@Override
	public String getType()
	{
		return _C__C5_DLGANSWER;
	}
}
