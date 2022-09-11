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
package net.sf.l2j.gameserver.network;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.LoginServerThread.SessionKey;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.CharSelectInfoPackage;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.ServerClose;
import net.sf.l2j.gameserver.util.FloodProtectors;
import net.sf.l2j.mmocore.MMOClient;
import net.sf.l2j.mmocore.MMOConnection;

/**
 * Represents a client connected on Game Server
 * @author KenM
 */
public final class L2GameClient extends MMOClient<MMOConnection<L2GameClient>>
{
	protected static final Logger _log = Logger.getLogger(L2GameClient.class.getName());

	/**
	 * CONNECTED - client has just connected.
	 * AUTHED - Client has authed but doesn't has character attached to it yet.
	 * JOINING - Client has selected a character, but it hasn't joined the server yet.
	 * IN_GAME - Client has selected a char and is in game.
	 * 
	 * @author KenM
	 */
	public static enum GameClientState
	{
		CONNECTED,
		AUTHED,
		JOINING,
		IN_GAME
	}
	
	private final byte[] _cryptKey =
	{
		(byte) 0x94,
		(byte) 0x35,
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0xa1,
		(byte) 0x6c,
		(byte) 0x54,
		(byte) 0x87 // the last 4 bytes are fixed
	};

	public GameClientState state;

	private boolean _isDetached = false;
	private boolean _protocol;

	private String _accountName;
	private L2PcInstance _activeChar;
	private SessionKey _sessionId;

	private final ReentrantLock _activeCharLock = new ReentrantLock();
	
	// Task
	protected final ScheduledFuture<?> _autoSaveInDB;
	protected ScheduledFuture<?> _cleanupTask = null;
	
	// Crypt
	private final GameCrypt _crypt;
	
	private final ClientStat _stat;
	
	private boolean _gameGuardOk = false;

	// Flood protectors
	private final FloodProtectors _floodProtectors = new FloodProtectors(this);

	private final List<Integer> _charSlotMapping = new ArrayList<>();

	public L2GameClient(MMOConnection<L2GameClient> con)
	{
		super(con);
		state = GameClientState.CONNECTED;
		_crypt = new GameCrypt();
		_stat = new ClientStat(this);
		_autoSaveInDB = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoSaveTask(), 300000L, 900000L);
	}

	public byte[] enableCrypt()
	{
		_crypt.setKey(_cryptKey);
		return _cryptKey;
	}

	@Override
	public boolean decrypt(ByteBuffer buf, int size)
	{
		_crypt.decrypt(buf.array(), buf.position(), size);
		return true;
	}

	@Override
	public boolean encrypt(final ByteBuffer buf, final int size)
	{
		_crypt.encrypt(buf.array(), buf.position(), size);
		buf.position(buf.position() + size);
		return true;
	}
	
	/**
	 * Save the L2PcInstance to the database.
	 * @param cha
	 * @param storeItems
	 */
	public static void saveCharToDisk(L2PcInstance cha, boolean storeItems)
	{
		try
		{
			cha.store();
			if (Config.UPDATE_ITEMS_ON_CHAR_STORE && storeItems)
			{
				cha.getInventory().updateDatabase();
				cha.getWarehouse().updateDatabase();
			}
		}
		catch (Exception e)
		{
			_log.warning("Error saving player character: " + e);
		}
	}

	public void markRestoredChar(int charslot)
	{
		// have to make sure active character must be nulled
		int objid = getObjectIdForSlot(charslot);
		if (objid < 0)
		{
			return;
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET deletetime=0 WHERE obj_Id=?"))
		{
			statement.setInt(1, objid);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("Data error on restoring char: " + e);
		}
	}

	/**
	 * Method to handle character deletion
	 * @param charslot
	 * @return a byte:
	 *         <li>-1: Error: No char was found for such charslot, caught exception, etc...
	 *         <li>0: character is not member of any clan, proceed with deletion
	 *         <li>1: character is member of a clan, but not clan leader
	 *         <li>2: character is clan leader
	 */
	public byte markToDeleteChar(int charslot)
	{
		int objid = getObjectIdForSlot(charslot);
		if (objid < 0)
		{
			return -1;
		}

		byte answer = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			int clanId = 0;

			try (PreparedStatement statement = con.prepareStatement("SELECT clanId from characters WHERE obj_Id=?"))
			{
				statement.setInt(1, objid);

				try (ResultSet rs = statement.executeQuery())
				{
					rs.next();
					clanId = rs.getInt(1);
				}
			}

			if (clanId != 0)
			{
				L2Clan clan = ClanTable.getInstance().getClan(clanId);
				if (clan != null)
				{
					if (clan.getLeaderId() == objid)
					{
						answer = 2;
					}
					else
					{
						answer = 1;
					}
				}

			}

			// Setting delete time
			if (answer == 0)
			{
				if (Config.DELETE_DAYS == 0)
				{
					deleteCharByObjId(objid);
				}
				else
				{
					try (PreparedStatement statement = con.prepareStatement("UPDATE characters SET deletetime=? WHERE obj_Id=?"))
					{
						statement.setLong(1, System.currentTimeMillis() + (Config.DELETE_DAYS * 86400000)); // 24*60*60*1000 = 86400000
						statement.setInt(2, objid);
						statement.execute();
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Data error on update delete time of char: " + e);

			return -1;
		}

		return answer;

	}

	public static void deleteCharByObjId(int objid)
	{
		if (objid < 0)
		{
			return;
		}

		CharNameTable.getInstance().removeName(objid);
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_friends WHERE char_id=? OR friend_id=?"))
			{
				statement.setInt(1, objid);
				statement.setInt(2, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_hennas WHERE char_obj_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_macroses WHERE char_obj_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_quests WHERE char_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_recipebook WHERE char_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE char_obj_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_skills_save WHERE char_obj_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_subclasses WHERE char_obj_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM heroes WHERE char_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM olympiad_nobles WHERE char_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM seven_signs WHERE char_obj_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id IN (SELECT object_id FROM items WHERE items.owner_id=?)"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM items WHERE owner_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM merchant_lease WHERE player_id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_recommends WHERE char_id=? OR target_id=?"))
			{
				statement.setInt(1, objid);
				statement.setInt(2, objid);
				statement.execute();
			}

			try (PreparedStatement statement = con.prepareStatement("DELETE FROM characters WHERE obj_Id=?"))
			{
				statement.setInt(1, objid);
				statement.execute();
			}
		}
		catch (Exception e)
		{
			_log.warning("Data error on deleting char: " + e);
		}
	}

	public L2PcInstance loadCharFromDisk(int charslot)
	{
		L2PcInstance character = L2PcInstance.load(getObjectIdForSlot(charslot));
		if (character != null)
		{
			// pre-init some values for each login
			character.setRunning(); // running is default
			character.standUp(); // standing is default

			character.refreshOverloaded();
			character.refreshExpertisePenalty();
			character.setOnlineStatus(true);
		}
		else
		{
			_log.warning("could not restore in slot:" + charslot);
		}

		return character;
	}

	/**
	 * @param charslot
	 * @return
	 */
	private int getObjectIdForSlot(int charslot)
	{
		if ((charslot < 0) || (charslot >= _charSlotMapping.size()))
		{
			_log.warning(toString() + " tried to delete Character in slot " + charslot + " but no characters exits at that slot.");
			return -1;
		}
		Integer objectId = _charSlotMapping.get(charslot);
		return objectId.intValue();
	}

	/**
	 * @return
	 */
	public L2PcInstance getActiveChar()
	{
		return _activeChar;
	}

	/**
	 * @return Returns the sessionId.
	 */
	public SessionKey getSessionId()
	{
		return _sessionId;
	}

	public String getAccountName()
	{
		return _accountName;
	}

	public void setAccountName(String accountName)
	{
		_accountName = accountName;
	}

	/**
	 * @param cha
	 */
	public void setActiveChar(L2PcInstance cha)
	{
		_activeChar = cha;
		if (cha != null)
		{
			L2World.getInstance().storeObject(getActiveChar());
		}
	}

	public void sendPacket(L2GameServerPacket gsp)
	{
		if (_isDetached)
		{
			return;
		}
		
		getConnection().sendPacket(gsp);
		gsp.runImpl();
	}
	
	public boolean isDetached()
	{
		return _isDetached;
	}
	
	public void setDetached(boolean b)
	{
		_isDetached = b;
	}

	public ReentrantLock getActiveCharLock()
	{
		
		return _activeCharLock;
	}

	public FloodProtectors getFloodProtectors()
	{
		return _floodProtectors;
	}

	/**
	 * @param key
	 */
	public void setSessionId(SessionKey key)
	{
		_sessionId = key;
	}

	/**
	 * @param chars
	 */
	public void setCharSelection(CharSelectInfoPackage[] chars)
	{
		_charSlotMapping.clear();

		for (CharSelectInfoPackage c : chars)
		{
			int objectId = c.getObjectId();
			_charSlotMapping.add(Integer.valueOf(objectId));
		}
	}

	public void close(L2GameServerPacket gsp)
	{
		getConnection().close(gsp);
	}
	
	public void setGameGuardOk(boolean gameGuardOk)
	{
		_gameGuardOk = gameGuardOk;
	}

	public boolean isGameGuardOk()
	{
		return _gameGuardOk;
	}

	public boolean isProtocolOk()
	{
		return _protocol;
	}

	public void setProtocolOk(boolean b)
	{
		_protocol = b;
	}

	public GameClientState getState()
	{
		return state;
	}

	public void setState(GameClientState pState)
	{
		state = pState;
	}
	
	public ClientStat getStat()
	{
		return _stat;
	}

	@Override
	protected void onForcedDisconnection()
	{
		_log.info("Client " + toString() + " disconnected abnormally.");
	}

	@Override
	protected void onDisconnection()
	{
		// no long running tasks here, do it async
		try
		{
			ThreadPoolManager.getInstance().executeTask(new DisconnectTask());
		}
		catch (RejectedExecutionException e)
		{
			// server is closing
		}
	}

	public void closeNow()
	{
		super.getConnection().close(new ServerClose());
		cleanMe(true);
	}

	/**
	 * Produces the best possible string representation of this client.
	 */
	@Override
	public String toString()
	{
		try
		{
			InetAddress address = getConnection().getInetAddress();
			switch (getState())
			{
				case CONNECTED:
					return "[IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
				case AUTHED:
					return "[Account: " + getAccountName() + " - IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
				case JOINING:
				case IN_GAME:
					return "[Character: " + (getActiveChar() == null ? "disconnected" : getActiveChar().getName() + "[" + getActiveChar().getObjectId() + "]") + " - Account: " + getAccountName() + " - IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
				default:
					throw new IllegalStateException("Missing state on switch");
			}
		}
		catch (NullPointerException e)
		{
			return "[Character read failed due to disconnect]";
		}
	}

	class DisconnectTask implements Runnable
	{
		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			boolean fast = true;

			try
			{
				final L2PcInstance player = L2GameClient.this.getActiveChar();
				if ((player != null) && !isDetached())
				{
					setDetached(true);
					if (!Olympiad.getInstance().isRegisteredInComp(player) && !player.isFestivalParticipant() && !player.isInJail() 
						&& player.getEvent() == null && !player.isInBoat())
					{
						if (Config.OFFLINE_TRADE_ENABLE && (player.getPrivateStoreType() == PrivateStoreType.SELL 
							|| player.getPrivateStoreType() == PrivateStoreType.PACKAGE_SELL 
							|| player.getPrivateStoreType() == PrivateStoreType.BUY) 
							|| Config.OFFLINE_CRAFT_ENABLE && (player.isInCraftMode() 
							|| player.getPrivateStoreType() == PrivateStoreType.DWARVEN_MANUFACTURE
							|| player.getPrivateStoreType() == PrivateStoreType.GENERAL_MANUFACTURE))
						{
							player.setInOfflineMode();
							if (Config.OFFLINE_SET_NAME_COLOR)
							{
								player.getAppearance().setNameColor(Config.OFFLINE_NAME_COLOR);
								player.broadcastUserInfo();
							}
							if (player.getOfflineStartTime() == 0)
							{
								player.setOfflineStartTime(System.currentTimeMillis());
							}
							
							return;
						}
					}
					if (player.isInCombat() || player.isLocked())
					{
						fast = false;
					}
				}
				cleanMe(fast);
			}
			catch (Exception e1)
			{
				_log.log(Level.WARNING, "Error while disconnecting client.", e1);
			}
		}
	}
	
	public void cleanMe(boolean fast)
	{
		try
		{
			synchronized (this)
			{
				if (_cleanupTask == null)
				{
					_cleanupTask = ThreadPoolManager.getInstance().scheduleGeneral(new CleanupTask(), fast ? 5 : 15000L);
				}
			}
		}
		catch (Exception e1)
		{
			_log.log(Level.WARNING, "Error during cleanup.", e1);
		}
	}

	class CleanupTask implements Runnable
	{
		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			try
			{
				// We are going to manually save the char below thus we can force the cancel
				if (_autoSaveInDB != null)
				{
					_autoSaveInDB.cancel(true);
				}

				L2PcInstance player = L2GameClient.this.getActiveChar();
				if (player != null) // this should only happen on connection loss
				{
					if (player.isLocked())
					{
						_log.log(Level.WARNING, "Player " + player.getName() + " still performing subclass actions during disconnect.");
					}

					// to prevent call cleanMe() again
					setDetached(false);
					// prevent closing again
					player.setClient(null);
					
					if (player.isOnline())
					{
						player.logout();
					}
				}
				L2GameClient.this.setActiveChar(null);
			}
			catch (Exception e1)
			{
				_log.log(Level.WARNING, "Error while cleanup client.", e1);
			}
			finally
			{
				LoginServerThread.getInstance().sendLogout(L2GameClient.this.getAccountName());
			}
		}
	}

	class AutoSaveTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				L2PcInstance player = getActiveChar();
				if (player != null && player.isOnline())
				{
					saveCharToDisk(player, true);
					if (player.getPet() != null)
					{
						player.getPet().store();
					}
				}
			}
			catch (Exception e)
			{
				_log.severe(e.toString());
			}
		}
	}
	
	public boolean cancelCleanup()
	{
		Future<?> task = _cleanupTask;
		if (task != null)
		{
			return task.cancel(true);
		}
		return false;
	}
}