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
package net.sf.l2j.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.model.L2ManufactureItem;
import net.sf.l2j.gameserver.model.L2ManufactureList;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.TradeList.TradeItem;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.L2GameClient.GameClientState;

public class OfflineTradersTable
{
	private static Logger _log = Logger.getLogger(OfflineTradersTable.class.getName());

	// SQL DEFINITIONS
	private static final String SAVE_OFFLINE_STATUS = "INSERT INTO character_offline_trade (`char_id`,`time`,`type`,`title`) VALUES (?,?,?,?)";
	private static final String SAVE_ITEMS = "INSERT INTO character_offline_trade_items (`char_id`,`item`,`enchant_level`,`count`,`price`) VALUES (?,?,?,?,?)";
	private static final String CLEAR_OFFLINE_TABLE = "DELETE FROM character_offline_trade";
	private static final String CLEAR_OFFLINE_TABLE_ITEMS = "DELETE FROM character_offline_trade_items";
	private static final String LOAD_OFFLINE_STATUS = "SELECT * FROM character_offline_trade";
	private static final String LOAD_OFFLINE_ITEMS = "SELECT * FROM character_offline_trade_items WHERE char_id = ?";

	public static void storeOffliners()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement stm1 = con.prepareStatement(CLEAR_OFFLINE_TABLE);
			PreparedStatement stm2 = con.prepareStatement(CLEAR_OFFLINE_TABLE_ITEMS);
			PreparedStatement stm3 = con.prepareStatement(SAVE_OFFLINE_STATUS);
			PreparedStatement stmItems = con.prepareStatement(SAVE_ITEMS))
		{
			stm1.execute();
			stm2.execute();
			con.setAutoCommit(false); // Avoid halfway done

			Collection<L2PcInstance> players = L2World.getInstance().getAllPlayers();
			for (L2PcInstance pc : players)
			{
				try
				{
					if (pc.inOfflineMode())
					{
						stm3.setInt(1, pc.getObjectId()); // Char Id
						stm3.setLong(2, pc.getOfflineStartTime());
						stm3.setInt(3, pc.getPrivateStoreType().getId()); // store type
						String title = null;

						switch (pc.getPrivateStoreType())
						{
							case BUY:
								if (!Config.OFFLINE_TRADE_ENABLE)
								{
									continue;
								}
								title = pc.getBuyList().getTitle();
								TradeItem[] items = pc.getBuyList().getItems();
								for (int i = 0; i < items.length; i++)
								{
									TradeItem item = items[i];
									
									stmItems.setInt(1, pc.getObjectId());
									stmItems.setInt(2, Integer.parseInt(item.getItem().getItemId() + String.valueOf(i)));
									stmItems.setInt(3, item.getEnchant());
									stmItems.setInt(4, item.getCount());
									stmItems.setInt(5, item.getPrice());
									stmItems.executeUpdate();
									stmItems.clearParameters();
								}
								break;
							case SELL:
							case PACKAGE_SELL:
								if (!Config.OFFLINE_TRADE_ENABLE)
								{
									continue;
								}
								title = pc.getSellList().getTitle();
								for (TradeItem i : pc.getSellList().getItems())
								{
									stmItems.setInt(1, pc.getObjectId());
									stmItems.setInt(2, i.getObjectId());
									stmItems.setInt(3, 0);
									stmItems.setInt(4, i.getCount());
									stmItems.setInt(5, i.getPrice());
									stmItems.executeUpdate();
									stmItems.clearParameters();
								}
								break;
							case DWARVEN_MANUFACTURE:
							case GENERAL_MANUFACTURE:
								if (!Config.OFFLINE_CRAFT_ENABLE)
								{
									continue;
								}
								title = pc.getCreateList().getStoreName();
								for (L2ManufactureItem i : pc.getCreateList().getList())
								{
									stmItems.setInt(1, pc.getObjectId());
									stmItems.setInt(2, i.getRecipeId());
									stmItems.setInt(3, 0);
									stmItems.setInt(4, 0);
									stmItems.setInt(5, i.getCost());
									stmItems.executeUpdate();
									stmItems.clearParameters();
								}
						}

						stm3.setString(4, title);
						stm3.executeUpdate();
						stm3.clearParameters();
						con.commit(); // flush
					}
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "OfflineTradersTable[storeTradeItems()]: Error while saving offline trader: " + pc.getObjectId() + " " + e, e);
				}
			}
			_log.info("Offline traders stored.");
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "OfflineTradersTable[storeTradeItems()]: Error while saving offline traders: " + e, e);
		}
	}

	public static void restoreOfflineTraders()
	{
		_log.info("Loading offline traders...");
		int nTraders = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement stm = con.prepareStatement(LOAD_OFFLINE_STATUS);
			ResultSet rs = stm.executeQuery())
		{
			while (rs.next())
			{
				long time = rs.getLong("time");
				if (Config.OFFLINE_MAX_DAYS > 0)
				{
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(time);
					cal.add(Calendar.DAY_OF_YEAR, Config.OFFLINE_MAX_DAYS);
					if (cal.getTimeInMillis() <= System.currentTimeMillis())
					{
						continue;
					}
				}

				PrivateStoreType type = PrivateStoreType.findById(rs.getInt("type"));
				if (type == null || type == PrivateStoreType.NONE)
				{
					continue;
				}

				L2PcInstance player = null;
				try
				{
					L2GameClient client = new L2GameClient(null);
					client.setDetached(true);
					player = L2PcInstance.load(rs.getInt("char_id"));
					client.setActiveChar(player);
					client.setAccountName(player.getAccountName());
					client.setState(GameClientState.IN_GAME);
					player.setClient(client);
					player.setOfflineStartTime(time);
					player.spawnMe(player.getX(), player.getY(), player.getZ());
					LoginServerThread.getInstance().addGameServerLogin(player.getAccountName(), client);

					try (PreparedStatement stmItems = con.prepareStatement(LOAD_OFFLINE_ITEMS))
					{
						stmItems.setInt(1, player.getObjectId());
						try (ResultSet items = stmItems.executeQuery())
						{
							switch (type)
							{
								case BUY:
									while (items.next())
									{
										String itemId = items.getString(2);
										if (player.getBuyList().addUniqueItem(Integer.parseInt(itemId.substring(0, itemId.length() - 1)), items.getInt(3), items.getInt(4), items.getInt(5)) == null)
										{
											throw new NullPointerException();
										}
									}
									player.getBuyList().setTitle(rs.getString("title"));
									break;
								case SELL:
								case PACKAGE_SELL:
									while (items.next())
									{
										if (player.getSellList().addItem(items.getInt(2), items.getInt(4), items.getInt(5)) == null)
										{
											throw new NullPointerException();
										}
									}
									player.getSellList().setTitle(rs.getString("title"));
									player.getSellList().setPackaged(type == PrivateStoreType.PACKAGE_SELL);
									break;
								case DWARVEN_MANUFACTURE:
								case GENERAL_MANUFACTURE:
									L2ManufactureList createList = new L2ManufactureList();
									while (items.next())
									{
										createList.add(new L2ManufactureItem(items.getInt(2), items.getInt(5)));
									}
									player.setCreateList(createList);
									player.getCreateList().setStoreName(rs.getString("title"));
									break;
							}
						}
					}

					player.sitDown();
					player.setInOfflineMode();
					if (Config.OFFLINE_SET_NAME_COLOR)
					{
						player.getAppearance().setNameColor(Config.OFFLINE_NAME_COLOR);
					}
					player.setPrivateStoreType(type);
					player.setOnlineStatus(true);
					player.restoreEffects();
					player.broadcastUserInfo();
					nTraders++;
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "OfflineTradersTable[loadOffliners()]: Error loading trader: " + player, e);
					if (player != null)
					{
						player.logout();
					}
				}
			}

			try (Statement stm1 = con.createStatement())
            {
                stm1.execute(CLEAR_OFFLINE_TABLE);
                stm1.execute(CLEAR_OFFLINE_TABLE_ITEMS);
            }
			
			_log.info("Loaded: " + nTraders + " offline trader(s)");
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "OfflineTradersTable[loadOffliners()]: Error while loading offline traders: ", e);
		}
	}
}