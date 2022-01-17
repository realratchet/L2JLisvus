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

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.loginserver.GameServerTable.GameServerInfo;
import net.sf.l2j.loginserver.gameserverpackets.ServerStatus;
import net.sf.l2j.loginserver.serverpackets.LoginFail.LoginFailReason;
import net.sf.l2j.util.Log;
import net.sf.l2j.util.Rnd;

/**
 * This class ...
 * @version $Revision: 1.7.4.3 $ $Date: 2005/03/27 15:30:09 $
 */
public class LoginController
{
	protected static Logger _log = Logger.getLogger(LoginController.class.getName());

	/** Time before kicking the client if he did not logged yet */
	public final static int LOGIN_TIMEOUT = 60 * 1000;
	
	/** This map contains the connections of the players that are in the login server */
	private final Map<String, L2LoginClient> _loginServerClients = new ConcurrentHashMap<>();
	private final Map<String, BanInfo> _bannedIps = new ConcurrentHashMap<>();
	
	private final Map<InetAddress, FailedLoginAttempt> _hackProtection = new ConcurrentHashMap<>();
	protected KeyPairGenerator _keyGen;
	protected ScrambledKeyPair[] _keyPairs;
	
	protected byte[][] _blowfishKeys;
	private static final int BLOWFISH_KEYS = 20;
	
	public static enum AuthLoginResult
	{
		INVALID_PASSWORD,
		ACCOUNT_BANNED,
		ALREADY_ON_LS,
		ALREADY_ON_GS,
		AUTH_SUCCESS
	}
	
	public static LoginController getInstance()
	{
		return SingletonHolder._instance;
	}

	private LoginController()
	{
		_log.info("Initializing LoginController");
		
		_keyPairs = new ScrambledKeyPair[10];
		
		try
		{
			_keyGen = KeyPairGenerator.getInstance("RSA");
			RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
			_keyGen.initialize(spec);
			
			// Generate the initial set of keys
			for (int i = 0; i < 10; i++)
			{
				_keyPairs[i] = new ScrambledKeyPair(_keyGen.generateKeyPair());
			}
			_log.info("Cached 10 KeyPairs for RSA communication");
			testCipher((RSAPrivateKey) _keyPairs[0]._pair.getPrivate());
			
			if (Config.ACCEPT_CHAOTIC_THRONE_CLIENTS)
			{
				// Store keys for blowfish communication
				generateBlowFishKeys();
			}
		}
		catch (GeneralSecurityException e)
		{
			_log.log(Level.SEVERE, "FATAL: Failed initializing LoginController. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		
		Thread purge = new PurgeThread();
		purge.setDaemon(true);
		purge.start();
	}
	
	/**
	 * This is mostly to force the initialization of the Crypto Implementation, avoiding it being done on runtime when its first needed.<BR>
	 * In short it avoids the worst-case execution time on runtime by doing it on loading.
	 * @param key Any private RSA Key just for testing purposes.
	 * @throws GeneralSecurityException if a underlying exception was thrown by the Cipher
	 */
	private void testCipher(RSAPrivateKey key) throws GeneralSecurityException
	{
		// avoid worst-case execution, KenM
		Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
		rsaCipher.init(Cipher.DECRYPT_MODE, key);
	}
	
	private void generateBlowFishKeys()
	{
		_blowfishKeys = new byte[BLOWFISH_KEYS][16];
		
		for (int i = 0; i < BLOWFISH_KEYS; i++)
		{
			for (int j = 0; j < _blowfishKeys[i].length; j++)
				_blowfishKeys[i][j] = (byte) (Rnd.get(255) + 1);
		}
		_log.info("Stored " + _blowfishKeys.length + " keys for Blowfish communication.");
	}
	
	/**
	 * @return Returns a random key
	 */
	public byte[] getBlowfishKey()
	{
		if (_blowfishKeys == null)
		{
			return null;
		}
		return _blowfishKeys[(int) (Math.random() * BLOWFISH_KEYS)];
	}
	
	public void assignKeyToLogin(L2LoginClient client)
	{
		client.setSessionKey(new SessionKey(Rnd.nextInt(), Rnd.nextInt(), Rnd.nextInt(), Rnd.nextInt()));
		_loginServerClients.put(client.getAccount(), client);
	}
	
	public boolean isAccountInAnyGameServer(String account)
	{
		Collection<GameServerInfo> serverList = GameServerTable.getInstance().getRegisteredGameServers().values();
		for (GameServerInfo gsi : serverList)
		{
			GameServerThread gst = gsi.getGameServerThread();
			if (gst != null && gst.hasAccountOnGameServer(account))
			{
				return true;
			}
		}
		return false;
	}
	
	public GameServerInfo getAccountOnGameServer(String account)
	{
		Collection<GameServerInfo> serverList = GameServerTable.getInstance().getRegisteredGameServers().values();
		for (GameServerInfo gsi : serverList)
		{
			GameServerThread gst = gsi.getGameServerThread();
			if (gst != null && gst.hasAccountOnGameServer(account))
			{
				return gsi;
			}
		}
		return null;
	}
	
	public SessionKey getKeyForAccount(String account)
	{
		L2LoginClient client = _loginServerClients.get(account);
		if (client != null)
		{
			return client.getSessionKey();
		}
		return null;
	}
	
	public void removeAuthedLoginClient(String account)
	{
		if (account == null)
		{
			return;
		}
		_loginServerClients.remove(account);
	}

	public L2LoginClient getAuthedClient(String account)
	{
		if (account == null)
		{
			return null;
		}
		
		return _loginServerClients.get(account);
	}

	public AuthLoginResult tryAuthLogin(String account, String password, L2LoginClient client)
	{
		AuthLoginResult ret = AuthLoginResult.INVALID_PASSWORD;
		// Check authentication
		if (isLoginValid(account, password, client))
		{
			// Login was successful, verify presence on Gameservers
			ret = AuthLoginResult.ALREADY_ON_GS;
			if (!isAccountInAnyGameServer(account))
			{
				// Account is not on any GS so verify LS itself
				ret = AuthLoginResult.ALREADY_ON_LS;

				if (!_loginServerClients.containsKey(account))
				{
					_loginServerClients.put(account, client);
					ret = AuthLoginResult.AUTH_SUCCESS;
				}
			}
		}
		else
		{
			if (client.getAccessLevel() < 0)
			{
				ret = AuthLoginResult.ACCOUNT_BANNED;
			}
		}
		return ret;
	}

	/**
	 *
	 * @param client 
	 * @param serverId 
	 * @return
	 */
	public boolean isLoginPossible(L2LoginClient client, int serverId)
	{
		GameServerInfo gsi = GameServerTable.getInstance().getRegisteredGameServerById(serverId);
		int access = client.getAccessLevel();
		if (gsi != null && gsi.isAuthed())
		{
			boolean loginOk = (gsi.getCurrentPlayerCount() < gsi.getMaxPlayers() && gsi.getStatus() != ServerStatus.STATUS_GM_ONLY) || access > 0;
			
			if (loginOk && client.getLastServer() != serverId)
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement statement = con.prepareStatement("UPDATE accounts SET last_server = ? WHERE login = ?"))
				{
					statement.setInt(1, serverId);
					statement.setString(2, client.getAccount());
					statement.executeUpdate();
				}
				catch (Exception e)
				{
					_log.warning("Could not set last server ID: " + e);
				}
			}
			return loginOk;
		}
		return false;
	}
	
	public void setAccountAccessLevel(String account, int banLevel)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE accounts SET access_level=? WHERE login=?"))
		{
			statement.setInt(1, banLevel);
			statement.setString(2, account);
			statement.executeUpdate();
		}
		catch (Exception e)
		{
			_log.warning("Could not set accessLevel:" + e);
		}
	}
	
	/**
	 * <p>
	 * This method returns one of the 10 {@link ScrambledKeyPair}.
	 * </p>
	 * @return a scrambled keypair
	 */
	public ScrambledKeyPair getScrambledRSAKeyPair()
	{
		return _keyPairs[Rnd.nextInt(10)];
	}
	
	/**
	 * user name is not case sensitive any more
	 * @param user
	 * @param password
	 * @param client
	 * @return
	 */
	public boolean isLoginValid(String user, String password, L2LoginClient client)
	{
		boolean ok = false;
		
		InetAddress address = client.getConnection().getInetAddress();
		if (address == null || user == null)
		{
			return false;
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] raw = password.getBytes("UTF-8");
			byte[] hash = md.digest(raw);
			
			byte[] expected = null;
			int access = 0;
			int lastServer = 1;
			
			try (PreparedStatement statement = con.prepareStatement("SELECT password, access_level, last_server FROM accounts WHERE login=?"))
			{
				statement.setString(1, user);
				try (ResultSet rset = statement.executeQuery())
				{
					if (rset.next())
					{
						expected = Base64.getDecoder().decode(rset.getString("password"));
						access = rset.getInt("access_level");
						lastServer = rset.getInt("last_server");
						if (lastServer <= 0)
						{
							lastServer = 1;
						}
						
						if (Config.DEBUG)
						{
							_log.fine("account exists");
						}
					}
				}
			}
			
			if (expected == null)
			{
				if (Config.AUTO_CREATE_ACCOUNTS)
				{
					if ((user.length() >= 2) && (user.length() <= 14))
					{
						try (PreparedStatement statement = con.prepareStatement("INSERT INTO accounts (login,password,last_active,access_level,last_ip) values(?,?,?,?,?)"))
						{
							statement.setString(1, user);
							statement.setString(2, Base64.getEncoder().encodeToString(hash));
							statement.setLong(3, System.currentTimeMillis());
							statement.setInt(4, 0);
							statement.setString(5, address.getHostAddress());
							statement.execute();
						}
						
						_log.info("Created new account for " + user);
						return true;
						
					}
					_log.warning("Invalid username creation/use attempt: " + user);
					return false;
				}
				_log.warning("[" + address.getHostAddress() + "]: account missing for user " + user);
				return false;
			}
			
			// This account is banned
			if (access < 0)
			{
				client.setAccessLevel(access);
				return false;
			}
			
			ok = true;
			for (int i = 0; i < expected.length; i++)
			{
				if (hash[i] != expected[i])
				{
					ok = false;
					break;
				}
			}

			if (ok)
			{
				client.setAccessLevel(access);
				client.setLastServer(lastServer);
				try (PreparedStatement statement = con.prepareStatement("UPDATE accounts SET last_active=?, last_ip=? WHERE login=?"))
				{
					statement.setLong(1, System.currentTimeMillis());
					statement.setString(2, address.getHostAddress());
					statement.setString(3, user);
					statement.execute();
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Failed to check password: " + e);
			ok = false;
		}
		
		if (!ok)
		{
			if (Config.LOG_LOGIN_ATTEMPTS)
			{
				Log.addLogin("'" + user + "' " + address.getHostAddress(), "login_ip_attempts");
			}
			
			FailedLoginAttempt failedAttempt = _hackProtection.get(address);
			int failedCount;
			if (failedAttempt == null)
			{
				_hackProtection.put(address, new FailedLoginAttempt(password));
				failedCount = 1;
			}
			else
			{
				failedAttempt.increaseCounter(password);
				failedCount = failedAttempt.getCount();
			}
			
			if (failedCount >= Config.LOGIN_TRY_BEFORE_BAN)
			{
				_log.info("Banning '" + address.getHostAddress() + "' for " + Config.LOGIN_BLOCK_AFTER_BAN + " seconds due to "
						+ failedCount + " invalid user/pass attempts");
				addBannedIP(address.getHostAddress(), Config.LOGIN_BLOCK_AFTER_BAN * 1000);
				// IP was banned, so remove it from hack protection list
				_hackProtection.remove(address);
			}
		}
		else
		{
			_hackProtection.remove(address);
			
			if (Config.LOG_LOGIN_ATTEMPTS)
			{
				Log.addLogin("'" + user + "' " + address.getHostAddress(), "login_ip_attempts");
			}
		}
		
		return ok;
	}
	
	public boolean unblockIP(String ip)
	{
		InetAddress address = null;
		try
		{
			address = InetAddress.getByName(ip);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		// Invalid address
		if (address == null)
		{
			return false;
		}
		
		if (_hackProtection.containsKey(address))
		{
			_hackProtection.remove(address);
			_log.warning("Removed host from hacklist! IP: " + address.getHostAddress());
			return true;
		}
		return false;
	}
	
	public void addBannedIP(String address, long expiration)
	{
		_bannedIps.put(address, new BanInfo(System.currentTimeMillis() + expiration));
	}
	
	public boolean isBannedAddress(String address)
	{
		BanInfo ban = _bannedIps.get(address);
		if (ban != null)
		{
			if (!ban.hasExpired())
			{
				return true;
			}
			_bannedIps.remove(address);
		}
		return false;
	}
	
	public Map<String, BanInfo> getBannedIps()
	{
		return _bannedIps;
	}
	
	class FailedLoginAttempt
	{
		private int _count;
		private long _lastAttemptTime;
		private String _lastPassword;
		
		public FailedLoginAttempt(String lastPassword)
		{
			_count = 1;
			_lastAttemptTime = System.currentTimeMillis();
			_lastPassword = lastPassword;
		}
		
		public void increaseCounter(String password)
		{
			if (!_lastPassword.equals(password))
			{
				// Check if there is a long time since last wrong try
				if (System.currentTimeMillis() - _lastAttemptTime < 300 * 1000)
				{
					_count++;
				}
				else
				{
					// restart the status
					_count = 1;
				}
				_lastPassword = password;
				_lastAttemptTime = System.currentTimeMillis();
			}
			else
			// Trying the same password is not brute force
			{
				_lastAttemptTime = System.currentTimeMillis();
			}
		}
		
		public int getCount()
		{
			return _count;
		}
	}
	
	private class BanInfo
	{
		private final long _expiration;
		
		public BanInfo(long expiration)
		{
			_expiration = expiration;
		}
		
		public boolean hasExpired()
		{
			return (System.currentTimeMillis() > _expiration) && (_expiration > 0);
		}
	}
	
	public static class ScrambledKeyPair
	{
		public KeyPair _pair;
		public byte[] _scrambledModulus;
		
		public ScrambledKeyPair(KeyPair pPair)
		{
			_pair = pPair;
			_scrambledModulus = scrambleModulus(((RSAPublicKey) _pair.getPublic()).getModulus());
		}
		
		private byte[] scrambleModulus(BigInteger modulus)
		{
			byte[] scrambledMod = modulus.toByteArray();
			
			if ((scrambledMod.length == 0x81) && (scrambledMod[0] == 0x00))
			{
				byte[] temp = new byte[0x80];
				System.arraycopy(scrambledMod, 1, temp, 0, 0x80);
				scrambledMod = temp;
			}
			// step 1 : 0x4d-0x50 <-> 0x00-0x04
			for (int i = 0; i < 4; i++)
			{
				byte temp = scrambledMod[0x00 + i];
				scrambledMod[0x00 + i] = scrambledMod[0x4d + i];
				scrambledMod[0x4d + i] = temp;
			}
			// step 2 : xor first 0x40 bytes with last 0x40 bytes
			for (int i = 0; i < 0x40; i++)
			{
				scrambledMod[i] = (byte) (scrambledMod[i] ^ scrambledMod[0x40 + i]);
			}
			// step 3 : xor bytes 0x0d-0x10 with bytes 0x34-0x38
			for (int i = 0; i < 4; i++)
			{
				scrambledMod[0x0d + i] = (byte) (scrambledMod[0x0d + i] ^ scrambledMod[0x34 + i]);
			}
			// step 4 : xor last 0x40 bytes with first 0x40 bytes
			for (int i = 0; i < 0x40; i++)
			{
				scrambledMod[0x40 + i] = (byte) (scrambledMod[0x40 + i] ^ scrambledMod[i]);
			}
			_log.fine("Modulus was scrambled");
			
			return scrambledMod;
		}
	}
	
	class PurgeThread extends Thread
	{
		public PurgeThread()
		{
			setName("PurgeThread");
		}
		
		@Override
		public void run()
		{
			while (!isInterrupted())
			{
				for (L2LoginClient client : _loginServerClients.values())
				{
					if (client == null)
					{
						continue;
					}
					if ((client.getConnectionStartTime() + LOGIN_TIMEOUT) < System.currentTimeMillis())
					{
						client.close(LoginFailReason.REASON_ACCESS_FAILED);
					}
				}
				
				try
				{
					Thread.sleep(LOGIN_TIMEOUT / 2);
				}
				catch (InterruptedException e)
				{
					return;
				}
			}
		}
	}
	
	private static class SingletonHolder
	{
		protected static final LoginController _instance = new LoginController();
	}
}