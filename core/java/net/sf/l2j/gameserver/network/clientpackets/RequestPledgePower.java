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
import net.sf.l2j.gameserver.network.serverpackets.ManagePledgePower;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;

public class RequestPledgePower extends L2GameClientPacket
{
	static Logger _log = Logger.getLogger(ManagePledgePower.class.getName());
	private static final String _C__C0_REQUESTPLEDGEPOWER = "[C] C0 RequestPledgePower";
	
	private int _clanMemberId;
	private int _action;
	private int _privs;
	
	@Override
	protected void readImpl()
	{
		_clanMemberId = readD();
		_action = readD();
		
		if (_action == 3)
		{
			_privs = readD();
		}
		else
		{
			_privs = 0;
		}
	}
	
	@Override
	public void runImpl()
	{
		final L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		final L2Clan clan = player.getClan();
		if (clan != null)
		{
			L2PcInstance target = null;
			L2ClanMember member = clan.getClanMember(_clanMemberId);
			if (member != null)
			{
				target = member.getPlayerInstance();
			}
			
			switch (_action)
			{
				case 1:
				{
					player.sendPacket(new ManagePledgePower(player.getClanPrivileges()));
					break;
				}
				case 2:
				{
					if (target != null)
					{
						player.sendPacket(new ManagePledgePower(target.getClanPrivileges()));
					}
					break;
				}
				case 3:
				{
					if (player.getObjectId() == clan.getLeaderId())
					{
						if (target != null && target != player)
						{
							target.setClanPrivileges(_privs);
							target.sendPacket(new UserInfo(target));
							target.sendPacket(new PledgeShowInfoUpdate(clan));
						}
					}
					break;
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__C0_REQUESTPLEDGEPOWER;
	}
}