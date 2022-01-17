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
import net.sf.l2j.gameserver.network.serverpackets.KeyPacket;

/**
 * This class ...
 * @version $Revision: 1.5.2.8.2.8 $ $Date: 2005/04/02 10:43:04 $
 */
public class ProtocolVersion extends L2GameClientPacket
{
	private static final String _C__00_PROTOCOLVERSION = "[C] 00 ProtocolVersion";
	private static Logger _log = Logger.getLogger(ProtocolVersion.class.getName());

	private int _version;
	
	@Override
	protected void readImpl()
	{
		// Avoid possible incomplete versions of this packet that could cause errors
		if (_buf.remaining() < 4)
		{
			getClient().closeNow();
			return;
		}
		_version = readD();
	}

	@Override
	public void runImpl()
	{
		// This packet is never encrypted
		if (_version == -2)
		{
			if (Config.DEBUG)
			{
				_log.info("Ping received");
			}

			// This is just a ping attempt
			getClient().closeNow();
			return;
		}

		if ((_version < Config.MIN_PROTOCOL_REVISION) || (_version > Config.MAX_PROTOCOL_REVISION))
		{
			if (Config.DEBUG)
			{
				_log.info("Client: " + getClient().toString() + " -> Protocol Revision: " + _version + " is invalid. Supported Revisions: Min = " + Config.MIN_PROTOCOL_REVISION + ", Max = " + Config.MAX_PROTOCOL_REVISION);
				_log.warning("Wrong Protocol Version " + _version);
			}
			
			getClient().setProtocolOk(false);
			getClient().sendPacket(new KeyPacket(getClient().enableCrypt(), (byte) 0));
		}
		else
		{
			if (Config.DEBUG)
			{
				_log.fine("Client Protocol Revision is ok:" + _version);
			}
			
			getClient().setProtocolOk(true);
			getClient().sendPacket(new KeyPacket(getClient().enableCrypt(), (byte) 1));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__00_PROTOCOLVERSION;
	}
}