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

import javax.crypto.Cipher;

import net.sf.l2j.Config;
import net.sf.l2j.loginserver.GameServerTable.GameServerInfo;
import net.sf.l2j.loginserver.L2LoginClient;
import net.sf.l2j.loginserver.L2LoginClient.LoginClientState;
import net.sf.l2j.loginserver.LoginController;
import net.sf.l2j.loginserver.LoginController.AuthLoginResult;
import net.sf.l2j.loginserver.serverpackets.AccountKicked;
import net.sf.l2j.loginserver.serverpackets.AccountKicked.AccountKickedReason;
import net.sf.l2j.loginserver.serverpackets.LoginFail.LoginFailReason;
import net.sf.l2j.loginserver.serverpackets.LoginOk;
import net.sf.l2j.loginserver.serverpackets.ServerList;

public class RequestAuthLogin extends L2LoginClientPacket
{
	private final byte[] _raw = new byte[128];
	
	private String _account;
	private String _password;
	
	/**
	 * Returns offset for credentials.
	 * This is a good method since it supports all clients.
	 * 
	 * @param decrypted
	 * @return
	 */
	private int getOffset(byte[] decrypted)
	{
		int lastIndex = decrypted.length - 1;
		// First 80 are definitely zeroes
		for (int i = 80; i < decrypted.length; i++)
		{
			byte b = decrypted[i];
			if (b != 0 && (b + i) == lastIndex)
			{
				return i + 3;
			}
		}
		return 0;
	}
	
	@Override
	public boolean readImpl()
	{
		if (super._buf.remaining() >= _raw.length)
		{
			readB(_raw);
			return true;
		}
		return false;
	}
	
	@Override
	public void run()
	{
		final L2LoginClient client = getClient();
		try
		{
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.DECRYPT_MODE, client.getRSAPrivateKey());
			byte[] decrypted = rsaCipher.doFinal(_raw, 0x00, 0x80);
			
			int offset = getOffset(decrypted);
			_account = new String(decrypted, offset, 14).trim().toLowerCase();
			_password = new String(decrypted, (offset + 14), 16).trim();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		LoginController lc = LoginController.getInstance();
		AuthLoginResult result = lc.tryAuthLogin(_account, _password, client);
		
		switch (result)
		{
			case AUTH_SUCCESS:
				client.setAccount(_account);
				client.setState(LoginClientState.AUTHED_LOGIN);
				lc.assignKeyToLogin(client);
				if (Config.SHOW_LICENCE)
				{
					client.sendPacket(new LoginOk(client.getSessionKey()));
				}
				else
				{
					client.sendPacket(new ServerList(client));
				}
				break;
			case INVALID_PASSWORD:
				client.close(LoginFailReason.REASON_USER_OR_PASS_WRONG);
				break;
			case ACCOUNT_BANNED:
				client.close(new AccountKicked(AccountKickedReason.REASON_ILLEGAL_USE));
				break;
			case ALREADY_ON_LS:
				L2LoginClient oldClient;
				if ((oldClient = lc.getAuthedClient(_account)) != null)
				{
					// kick the other client
					oldClient.close(LoginFailReason.REASON_ACCOUNT_IN_USE);
					lc.removeAuthedLoginClient(_account);
				}
				// kick also current client
				client.close(LoginFailReason.REASON_ACCOUNT_IN_USE);
				break;
			case ALREADY_ON_GS:
				GameServerInfo gsi;
				if ((gsi = lc.getAccountOnGameServer(_account)) != null)
				{
					client.close(LoginFailReason.REASON_ACCOUNT_IN_USE);
					
					// kick from there
					if (gsi.isAuthed())
					{
						gsi.getGameServerThread().kickPlayer(_account);
					}
				}
				break;
		}
	}
}