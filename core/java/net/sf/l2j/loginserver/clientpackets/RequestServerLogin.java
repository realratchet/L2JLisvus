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
package net.sf.l2j.loginserver.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.loginserver.L2LoginClient;
import net.sf.l2j.loginserver.LoginController;
import net.sf.l2j.loginserver.SessionKey;
import net.sf.l2j.loginserver.L2LoginClient.LoginClientState;
import net.sf.l2j.loginserver.serverpackets.LoginFail;
import net.sf.l2j.loginserver.serverpackets.LoginFail.LoginFailReason;
import net.sf.l2j.loginserver.serverpackets.PlayFail.PlayFailReason;
import net.sf.l2j.loginserver.serverpackets.PlayOk;

/**
 * Format is ddc d: first part of session id d: second part of session id c: server ID (session ID is sent in LoginOk packet and fixed to 0x55555555 0x44444444)
 */
public class RequestServerLogin extends L2LoginClientPacket
{
	private static Logger _log = Logger.getLogger(RequestServerLogin.class.getName());
	
	private int _skey1;
	private int _skey2;
	private int _serverId;
	
	/**
	 * @return
	 */
	public int getSessionKey1()
	{
		return _skey1;
	}
	
	/**
	 * @return
	 */
	public int getSessionKey2()
	{
		return _skey2;
	}
	
	/**
	 * @return
	 */
	public int getServerID()
	{
		return _serverId;
	}
	
	@Override
	public boolean readImpl()
	{
		if (super._buf.remaining() >= 9)
		{
			_skey1 = readD();
			_skey2 = readD();
			_serverId = readC();
			return true;
		}
		return false;
	}
	
	@Override
	public void run()
	{
		final L2LoginClient client = getClient();
		final SessionKey sk = client.getSessionKey();

		// Check if licence is shown and key values are valid
		if (Config.SHOW_LICENCE)
		{
			if (!sk.checkLoginPair(_skey1, _skey2))
			{
				client.close(new LoginFail(LoginFailReason.REASON_ACCESS_FAILED));
				return;
			}

			if (!Config.ALLOW_L2WALKER && client.getState() != LoginClientState.AGREED)
			{
				_log.warning("Account " + client.getAccount() + " tried to log in using a 3rd party program.");
				client.close(new LoginFail(LoginFailReason.REASON_ACCESS_FAILED));
				return;
			}
		}

		if (LoginController.getInstance().isLoginPossible(client, _serverId))
		{
			client.setState(LoginClientState.JOINED_GS);
			client.sendPacket(new PlayOk(sk));
		}
		else
		{
			client.close(PlayFailReason.REASON_TOO_MANY_PLAYERS);
		}
	}
}