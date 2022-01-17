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

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestGiveNickName extends L2GameClientPacket
{
	private static final String _C__55_REQUESTGIVENICKNAME = "[C] 55 RequestGiveNickName";
	static Logger _log = Logger.getLogger(RequestGiveNickName.class.getName());

	private String _target;
	private String _title;
	
	@Override
	protected void readImpl()
	{
		_target = readS();
		_title = readS();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		// Noblesse can bestow a title to themselves
		if (activeChar.isNoble() && _target.matches(activeChar.getName()))
		{
			activeChar.setTitle(_title);
			activeChar.sendPacket(new SystemMessage(SystemMessage.TITLE_CHANGED));
			activeChar.broadcastTitleInfo();
		}
		
		// Can the player change/give a title?
		else if ((activeChar.getClanPrivileges() & L2Clan.CP_CL_GIVE_TITLE) == L2Clan.CP_CL_GIVE_TITLE)
		{
			if (activeChar.getClan().getLevel() < 3)
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_LVL_3_NEEDED_TO_ENDOWE_TITLE));
				return;
			}
			
			L2ClanMember member1 = activeChar.getClan().getClanMember(_target);
			if (member1 != null)
			{
				L2PcInstance member = member1.getPlayerInstance();
				if ((member != null) && !member.inOfflineMode())
				{
					
					// is target from the same clan?
					member.setTitle(_title);
					member.sendPacket(new SystemMessage(SystemMessage.TITLE_CHANGED));
					
					member.broadcastTitleInfo();
				}
				else
				{
					activeChar.sendMessage("Target is not online.");
				}
			}
			else
			{
				activeChar.sendMessage("Target does not belong to your clan.");
			}
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__55_REQUESTGIVENICKNAME;
	}
}