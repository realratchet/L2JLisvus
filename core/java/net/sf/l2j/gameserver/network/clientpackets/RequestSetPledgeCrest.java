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
 * This class ...
 * @version $Revision: 1.2.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestSetPledgeCrest extends L2GameClientPacket
{
	private static final String _C__53_REQUESTSETPLEDGECREST = "[C] 53 RequestSetPledgeCrest";
	static Logger _log = Logger.getLogger(RequestSetPledgeCrest.class.getName());

	private int _length;
	private byte[] _data;

	@Override
	protected void readImpl()
	{
		_length = readD();
		if (_length > 256)
		{
			return;
		}
		
		_data = new byte[_length];
		readB(_data);
	}

	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (_length < 0)
		{
			activeChar.sendMessage("File Transfer Error.");
			return;
		}

		if (_length > 256)
		{
			activeChar.sendMessage("The clan crest file size is greater than 256 bytes.");
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

		CrestCache crestCache = CrestCache.getInstance();

		if ((_length == 0) || (_data.length == 0))
		{
			crestCache.removeCrest(CrestType.PLEDGE, clan.getCrestId());

			clan.setHasCrest(false);
			activeChar.sendMessage("The clan crest has been deleted.");

			for (L2PcInstance member : clan.getOnlineMembers(0))
			{
				member.broadcastUserInfo();
			}

			return;
		}

		if (clan.getLevel() < 3)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_LVL_3_NEEDED_TO_SET_CREST));

			return;
		}

		int newId = IdFactory.getInstance().getNextId();

		if (clan.hasCrest())
		{
			crestCache.removeCrest(CrestType.PLEDGE, newId);
		}

		if (!crestCache.saveCrest(CrestType.PLEDGE, newId, _data))
		{
			_log.log(Level.INFO, "Error loading crest of clan:" + clan.getName());
			return;
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET crest_id = ? WHERE clan_id = ?"))
		{
			statement.setInt(1, newId);
			statement.setInt(2, clan.getClanId());
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			_log.warning("could not update the crest id:" + e.getMessage());
		}

		clan.setCrestId(newId);
		clan.setHasCrest(true);

		for (L2PcInstance member : clan.getOnlineMembers(0))
		{
			member.broadcastUserInfo();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__53_REQUESTSETPLEDGECREST;
	}
}