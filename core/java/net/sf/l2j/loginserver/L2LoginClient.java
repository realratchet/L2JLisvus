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
package net.sf.l2j.loginserver;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.interfaces.RSAPrivateKey;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.loginserver.LoginController.ScrambledKeyPair;
import net.sf.l2j.loginserver.crypt.LoginCrypt;
import net.sf.l2j.loginserver.serverpackets.Init;
import net.sf.l2j.loginserver.serverpackets.L2LoginServerPacket;
import net.sf.l2j.loginserver.serverpackets.LoginFail;
import net.sf.l2j.loginserver.serverpackets.LoginFail.LoginFailReason;
import net.sf.l2j.loginserver.serverpackets.PlayFail;
import net.sf.l2j.loginserver.serverpackets.PlayFail.PlayFailReason;
import net.sf.l2j.mmocore.MMOClient;
import net.sf.l2j.mmocore.MMOConnection;
import net.sf.l2j.mmocore.SendablePacket;
import net.sf.l2j.util.Rnd;
import net.sf.l2j.util.Util;

/**
 * This class ...
 * @version $Revision: 1.15.2.5.2.5 $ $Date: 2005/04/06 16:13:46 $
 */
public class L2LoginClient extends MMOClient<MMOConnection<L2LoginClient>>
{
	private static Logger _log = Logger.getLogger(L2LoginClient.class.getName());

	public static enum LoginClientState
	{
		CONNECTED,
		AUTHED_GG,
		AUTHED_LOGIN,
		AGREED,
		JOINED_GS
	}

	private final byte[] _cryptKey =
	{
		(byte)0x5f, (byte)0x3b, (byte)0x35, (byte)0x2e, 
		(byte)0x5d, (byte)0x39, (byte)0x34, (byte)0x2d, 
		(byte)0x33, (byte)0x31, (byte)0x3d, (byte)0x3d, 
		(byte)0x2d, (byte)0x25, (byte)0x78, (byte)0x54, 
		(byte)0x21, (byte)0x5e, (byte)0x5b, (byte)0x24, (byte)0x00
	};
	
	private LoginClientState _state;

	// Crypt
	private LoginCrypt _loginCrypt;
	private ScrambledKeyPair _scrambledPair;
	private byte[] _blowfishKey;

	private String _account;
	private int _accessLevel;
	private int _lastServer;
	private boolean _usesInternalIP;
	private SessionKey _sessionKey;
	private int _sessionId;

	private boolean _hasAttemptedDecryption = false;
	
	private long _connectionStartTime;

	/**
	 * @param con
	 */
	public L2LoginClient(MMOConnection<L2LoginClient> con)
	{
		super(con);
		_state = LoginClientState.CONNECTED;

		String ip = getConnection().getInetAddress().getHostAddress();
		if (Util.isInternalIP(ip))
		{
			_usesInternalIP = true;
		}

		_scrambledPair = LoginController.getInstance().getScrambledRSAKeyPair();
		_blowfishKey = LoginController.getInstance().getBlowfishKey();
		_sessionId = Rnd.nextInt();
		_connectionStartTime = System.currentTimeMillis();
		_loginCrypt = new LoginCrypt();
		_loginCrypt.setKey(_cryptKey);
	}
	
	/**
	 * @see net.sf.l2j.mmocore.MMOClient#decrypt(java.nio.ByteBuffer, int)
	 */
	@Override
	public boolean decrypt(ByteBuffer buf, int size)
	{
		boolean ret = false;
		try
		{
			ret = _loginCrypt.decrypt(buf.array(), buf.position(), size);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			super.getConnection().close((SendablePacket<L2LoginClient>) null);
			return false;
		}

		if (!_hasAttemptedDecryption)
		{
			_hasAttemptedDecryption = true;
			// Chaotic throne client connection attempt
			if (Config.ACCEPT_CHAOTIC_THRONE_CLIENTS && !ret)
			{
				_loginCrypt.setKey(_blowfishKey);
				// Send init packet to trigger account login
				sendPacket(new Init(this));
				return ret;
			}
		}
		
		if (!ret)
		{
			byte[] dump = new byte[size];
			System.arraycopy(buf.array(), buf.position(), dump, 0, size);
			_log.warning("Wrong checksum from client: " + toString());
			super.getConnection().close((SendablePacket<L2LoginClient>) null);
		}
		
		return ret;
	}

	/**
	 * @see net.sf.l2j.mmocore.MMOClient#encrypt(java.nio.ByteBuffer, int)
	 */
	@Override
	public boolean encrypt(ByteBuffer buf, int size)
	{
		final int offset = buf.position();
		try
		{
			size = _loginCrypt.encrypt(buf.array(), offset, size);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		buf.position(offset + size);
		return true;
	}

	public LoginClientState getState()
	{
		return _state;
	}

	public void setState(LoginClientState state)
	{
		_state = state;
	}

	public boolean usesInternalIP()
	{
		return _usesInternalIP;
	}

	public byte[] getBlowfishKey()
	{
		return _blowfishKey;
	}
	
	public byte[] getScrambledModulus()
	{
		return _scrambledPair._scrambledModulus;
	}

	public RSAPrivateKey getRSAPrivateKey()
	{
		return (RSAPrivateKey) _scrambledPair._pair.getPrivate();
	}

	public String getAccount()
	{
		return _account;
	}

	public void setAccount(String account)
	{
		_account = account;
	}

	public void setAccessLevel(int accessLevel)
	{
		_accessLevel = accessLevel;
	}

	public int getAccessLevel()
	{
		return _accessLevel;
	}

	public void setLastServer(int lastServer)
	{
		_lastServer = lastServer;
	}

	public int getLastServer()
	{
		return _lastServer;
	}

	public int getSessionId()
	{
		return _sessionId;
	}

	public void setSessionKey(SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}

	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}

	public long getConnectionStartTime()
	{
		return _connectionStartTime;
	}

	public void sendPacket(L2LoginServerPacket lsp)
	{
		getConnection().sendPacket(lsp);
	}

	public void close(LoginFailReason reason)
	{
		getConnection().close(new LoginFail(reason));
	}

	public void close(PlayFailReason reason)
	{
		getConnection().close(new PlayFail(reason));
	}

	public void close(L2LoginServerPacket lsp)
	{
		getConnection().close(lsp);
	}

	@Override
	public void onDisconnection()
	{
		if (Config.DEBUG)
		{
			_log.info("DISCONNECTED: "+toString());
		}

		if (_state != LoginClientState.JOINED_GS || (getConnectionStartTime() + LoginController.LOGIN_TIMEOUT) < System.currentTimeMillis())
		{
			LoginController.getInstance().removeAuthedLoginClient(getAccount());
		}
	}

	@Override
	public String toString()
	{
		InetAddress address = getConnection().getInetAddress();
		switch (_state)
		{
			case CONNECTED:
			case AUTHED_GG:
				return "[IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
			default:
				return "[Account: " + getAccount() + " - IP: " + (address == null ? "disconnected" : address.getHostAddress())+"]";
		}
	}
	
	@Override
	protected void onForcedDisconnection()
	{
		// Empty
	}
}