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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.cache.CrestCache;
import net.sf.l2j.gameserver.cache.CrestCache.CrestType;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Format : chdb c (id) 0xD0 h (subid) 0x11 d data size b raw data (picture i think ;) )
 * @author -Wooden-
 */
public class RequestExSetPledgeCrestLarge extends L2GameClientPacket
{
	private static final String _C__D0_11_REQUESTEXSETPLEDGECRESTLARGE = "[C] D0:11 RequestExSetPledgeCrestLarge";
	private static Logger _log = Logger.getLogger(RequestExSetPledgeCrestLarge.class.getName());

	private int _size;
	private byte[] _data;

	@Override
	protected void readImpl()
	{
		_size = readD();
		if (_size > 2176)
		{
			return;
		}
		
		_data = new byte[_size];
		readB(_data);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (_size < 0)
		{
			activeChar.sendMessage("File transfer error.");
			return;
		}
		
		if (_size > 2176)
		{
			activeChar.sendMessage("The insignia file size is greater than 2176 bytes.");
			return;
		}
		
		L2Clan clan = activeChar.getClan();
		if (clan == null)
		{
			return;
		}
		
		if ((activeChar.getClanPrivileges() & L2Clan.CP_CL_MANAGE_CREST) != L2Clan.CP_CL_MANAGE_CREST)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
			return;
		}
		
		if (clan.getDissolvingExpiryTime() > System.currentTimeMillis())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CREST_MANAGEMENT_NOT_ALLOWED_DURING_CLAN_DISSOLUTION));
			return;
		}
		
		int newId = 0;
		if (_data != null && _data.length > 0)
		{
			newId = IdFactory.getInstance().getNextId();
			if (!CrestCache.getInstance().saveCrest(CrestType.PLEDGE_LARGE, newId, _data))
			{
				_log.log(Level.INFO, "Error loading large crest for clan: " + clan.getName());
				return;
			}
		}
		else
		{
			if (!clan.hasCrestLarge())
			{
				return;
			}
		}

		if (clan.hasCrestLarge())
		{
			CrestCache.getInstance().removeCrest(CrestType.PLEDGE_LARGE, clan.getCrestLargeId());
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET crest_large_id = ? WHERE clan_id = ?"))
		{
			statement.setInt(1, newId);
			statement.setInt(2, clan.getClanId());
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			_log.warning("Could not update the large crest id: " + e.getMessage());
		}

		clan.setCrestLargeId(newId);
		if (newId > 0)
		{
			clan.setHasCrestLarge(true);
			activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_EMBLEM_WAS_SUCCESSFULLY_REGISTERED));
		}
		else
		{
			clan.setHasCrestLarge(false);
			activeChar.sendMessage("The clan emblem has been removed.");
		}

		for (L2PcInstance member : clan.getOnlineMembers(0))
		{
			member.broadcastUserInfo();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_11_REQUESTEXSETPLEDGECRESTLARGE;
	}
}