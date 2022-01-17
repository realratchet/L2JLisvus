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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * <p>
 * Format: (c) Sd
 * <ul>
 * <li>S: content</li>
 * <li>d: type</li>
 * </ul>
 * </p>
 * @author -Wooden-, TempyIncursion
 */
public class RequestPetition extends L2GameClientPacket
{
	private static final String _C__7F_RequestPetition = "[C] 7F RequestPetition";
	// private static Logger _log = Logger.getLogger(RequestPetition.class.getName());
	
	private String _content;
	private int _type; // 1 = on : 0 = off;
	
	@Override
	protected void readImpl()
	{
		_content = readS();
		_type = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!GmListTable.getInstance().isGmOnline(false))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.NO_GM_PROVIDING_SERVICE_NOW));
			return;
		}
		
		if (!PetitionManager.getInstance().isPetitioningAllowed())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_CONNECT_TO_PETITION_SERVER));
			return;
		}
		
		if (PetitionManager.getInstance().isPlayerPetitionPending(activeChar))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.ALREADY_APPLIED_FOR_PETITION));
			return;
		}
		
		if (PetitionManager.getInstance().getPendingPetitionCount() == Config.MAX_PETITIONS_PENDING)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.PETITION_SYSTEM_UNAVAILABLE));
			return;
		}
		
		int totalPetitions = PetitionManager.getInstance().getPlayerTotalPetitionCount(activeChar) + 1;
		if (totalPetitions > Config.MAX_PETITIONS_PER_PLAYER)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.CANNOT_SUBMIT_MORE_THAN_S1_PETITIONS);
			sm.addNumber(totalPetitions);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}
		
		if (_content.length() > 255)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.PETITIONS_CANNOT_EXCEED_255_CHARACTERS));
			return;
		}
		
		int petitionId = PetitionManager.getInstance().submitPetition(activeChar, _content, _type);
		
		SystemMessage sm = new SystemMessage(SystemMessage.PETITION_ACCEPTED_RECEIPT_S1);
		sm.addNumber(petitionId);
		activeChar.sendPacket(sm);
		
		sm = new SystemMessage(SystemMessage.YOU_HAVE_SUBMITTED_S1_PETITIONS_S2_REMAINING);
		sm.addNumber(totalPetitions);
		sm.addNumber(Config.MAX_PETITIONS_PER_PLAYER - totalPetitions);
		activeChar.sendPacket(sm);
		
		sm = new SystemMessage(SystemMessage.THERE_ARE_S1_PENDING_PETITIONS);
		sm.addNumber(PetitionManager.getInstance().getPendingPetitionCount());
		activeChar.sendPacket(sm);
		sm = null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__7F_RequestPetition;
	}
}