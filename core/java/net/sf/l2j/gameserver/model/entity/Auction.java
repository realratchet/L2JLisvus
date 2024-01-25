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
package net.sf.l2j.gameserver.model.entity;

import static net.sf.l2j.gameserver.model.itemcontainer.Inventory.ADENA_ID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.model.Bidder;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Auction
{
	private static final Logger _log = Logger.getLogger(Auction.class.getName());
	
	private long _endDate;
	
	private Bidder _highestBidder;
	
	private Future<?> _endTask;
	
	private final int _clanHallId;
	private final int _sellerId;
	private final String _sellerClanName;
	private final String _sellerName;
	private final int _sellerBid;
	private final Map<Integer, Bidder> _bidders = new HashMap<>();
	
	public Auction(int clanHallId, int sellerId, String sellerName, String sellerClanName, int sellerBid)
	{
		_clanHallId = clanHallId;
		_sellerId = sellerId;
		_sellerName = sellerName;
		_sellerClanName = sellerClanName;
		_sellerBid = sellerBid;
	}
	
	public void loadBids()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT bidderId, bidderName, maxBid, clan_name, time_bid FROM auction_bid WHERE hall_id = ? ORDER BY maxBid DESC"))
		{
			statement.setInt(1, _clanHallId);
			try (ResultSet rs = statement.executeQuery())
			{
				while (rs.next())
				{
					int bidderId = rs.getInt("bidderId");
					Bidder bidder = new Bidder(rs.getInt("bidderId"), rs.getString("bidderName"), rs.getString("clan_name"), rs.getInt("maxBid"), rs.getLong("time_bid"));
					
					if (rs.isFirst())
					{
						_highestBidder = bidder;
					}
					
					_bidders.put(bidderId, bidder);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Failed to load bids from database", e);
		}
	}
	
	public void startAutoTask()
	{
		long currentTime = System.currentTimeMillis();
		long taskDelay = 0;
		if (_endDate <= currentTime)
		{
			_endDate = currentTime + (7 * 24 * 60 * 60 * 1000);
			updateAuctionDate();
		}
		else
		{
			taskDelay = _endDate - currentTime;
		}
		
		_endTask = ThreadPoolManager.getInstance().scheduleGeneral(this::endAuction, taskDelay);
	}
	
	public Bidder findHighestBidder()
	{
		Bidder highestBidder = null;
		
		for (Bidder bidder : _bidders.values())
		{
			if (highestBidder == null || bidder.getBid() > highestBidder.getBid())
			{
				highestBidder = bidder;
			}
		}
		
		return highestBidder;
	}
	
	private void updateAuctionDate()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE auction SET endDate=? WHERE id=?"))
		{
			statement.setLong(1, _endDate);
			statement.setInt(2, _clanHallId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Failed to update auction date into database", e);
		}
	}
	
	public synchronized void setBid(L2PcInstance player, int bid)
	{
		L2Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		
		if (bid <= _sellerBid)
		{
			player.sendPacket(new SystemMessage(SystemMessage.BID_PRICE_MUST_BE_HIGHER));
			return;
		}
		
		int requiredAdena = bid;
		
		Bidder bidder = null;
		boolean isNewBidder = !_bidders.containsKey(player.getClanId());
		
		// Bidder object exists, we retrieve the bid.
		if (!isNewBidder)
		{
			bidder = _bidders.get(player.getClanId());
			
			// Check if requested bid is higher than current bid
			if (bid <= bidder.getBid())
			{
				player.sendPacket(new SystemMessage(SystemMessage.BID_PRICE_MUST_BE_HIGHER));
				return;
			}
			
			// We calculate the difference, which will be taken from clan warehouse
			requiredAdena -= bidder.getBid();
		}
		
		// Failed to retrieve adena from warehouse
		if (!takeItem(player, requiredAdena))
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA));
			return;
		}
		
		final long time = System.currentTimeMillis();
		
		// Update bidder details
		if (bidder != null)
		{
			bidder.setBid(bid);
			bidder.setBidTime(time);
		}
		else
		{
			int clanId = player.getClanId();
			bidder = new Bidder(clanId, clan.getLeaderName(), clan.getName(), bid, time);
			_bidders.put(clanId, bidder);
		}
		
		// Set new highest bidder
		_highestBidder = findHighestBidder();
		
		player.sendPacket(new SystemMessage(SystemMessage.BID_IN_CLANHALL_AUCTION));
		clan.setAuctionBiddedAt(_clanHallId, true);
		updateBidInDB(player, bid, time, isNewBidder);
	}
	
	private void returnItem(L2Clan clan, int quantity, boolean penalty)
	{
		if (clan == null)
		{
			return;
		}
		
		if (penalty)
		{
			quantity *= 0.9; // take 10% tax fee if needed
		}
		
		clan.getWarehouse().addItem("Outbidded", ADENA_ID, quantity, null, null);
	}
	
	public boolean takeItem(L2PcInstance bidder, int quantity)
	{
		L2Clan clan = bidder.getClan();
		if (clan == null)
		{
			return false;
		}
		
		if (clan.getWarehouse().getAdena() < quantity)
		{
			return false;
		}
		
		// Take item from clan warehouse
		bidder.getClan().getWarehouse().destroyItemByItemId("Buy", ADENA_ID, quantity, bidder, bidder);
		return true;
	}
	
	private void updateBidInDB(L2PcInstance bidder, int bid, long time, boolean isNew)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			if (isNew)
			{
				try (PreparedStatement statement = con.prepareStatement("INSERT INTO auction_bid (hall_id, bidderId, bidderName, maxBid, clan_name, time_bid) VALUES (?, ?, ?, ?, ?, ?)"))
				{
					statement.setInt(1, _clanHallId);
					statement.setInt(2, bidder.getClanId());
					statement.setString(3, bidder.getName());
					statement.setInt(4, bid);
					statement.setString(5, bidder.getClan().getName());
					statement.setLong(6, time);
					statement.execute();
				}
			}
			else
			{
				try (PreparedStatement statement = con.prepareStatement("UPDATE auction_bid SET bidderId=?, bidderName=?, maxBid=?, time_bid=? WHERE hall_id=? AND bidderId=?"))
				{
					statement.setInt(1, bidder.getClanId());
					statement.setString(2, bidder.getClan().getLeaderName());
					statement.setInt(3, bid);
					statement.setLong(4, time);
					statement.setInt(5, _clanHallId);
					statement.setInt(6, bidder.getClanId());
					statement.execute();
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Failed to update bid data in database for clan " + bidder.getClanId(), e);
		}
	}
	
	public void removeBids(L2Clan winningClan)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM auction_bid WHERE hall_id=?"))
		{
			statement.setInt(1, _clanHallId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Cannot remove bids from database for clan hall ID " + _clanHallId, e);
		}
		
		for (Bidder bidder : _bidders.values())
		{
			L2Clan clan = ClanTable.getInstance().getClanByName(bidder.getClanName());
			if (clan != null)
			{
				clan.setAuctionBiddedAt(0, true);
				
				if (clan != winningClan)
				{
					returnItem(clan, bidder.getBid(), true); // 10 % tax
				}
				else
				{
					SystemMessage sm = new SystemMessage(SystemMessage.CLANHALL_AWARDED_TO_CLAN_S1);
					sm.addString(winningClan.getName());
					clan.broadcastToOnlineMembers(sm);
				}
				
			}
		}
		_bidders.clear();
	}
	
	public void endAuction()
	{
		if (_endTask != null)
		{
			_endTask.cancel(false);
			_endTask = null;
		}

		ClanHall ch = ClanHallManager.getInstance().getClanHallById(_clanHallId);
		if (ch == null)
		{
			_log.warning("Failed to retrieve clan hall for auction " + _clanHallId);
			return;
		}
		
		if (_highestBidder == null)
		{
			// It's an NPC auction so it's simply being restarted
			if (_sellerId == 0)
			{
				startAutoTask();
			}
			else
			{
				L2Clan sellerClan = ClanTable.getInstance().getClanByName(_sellerClanName);
				if (sellerClan != null)
				{
					sellerClan.broadcastToOnlineMembers(new SystemMessage(SystemMessage.CLANHALL_NOT_SOLD));
				}
				deleteMe();
				ch.setAuction(null);
			}
			return;
		}
		
		if (_sellerId > 0)
		{
			L2Clan sellerClan = ClanTable.getInstance().getClanByName(_sellerClanName);
			returnItem(sellerClan, _highestBidder.getBid(), true);
			returnItem(sellerClan, ch.getLease(), false);
		}

		ch.setOwner(ClanTable.getInstance().getClanByName(_highestBidder.getClanName()));
	}
	
	public synchronized void cancelBid(L2Clan clan)
	{
		Bidder bidder = _bidders.remove(clan.getClanId());
		if (bidder == null)
		{
			return;
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM auction_bid WHERE hall_id=? AND bidderId=?"))
		{
			statement.setInt(1, _clanHallId);
			statement.setInt(2, clan.getClanId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Cannot delete bid from database for bidding clan + " + clan.getClanId(), e);
		}
		
		returnItem(clan, bidder.getBid(), true);
		clan.setAuctionBiddedAt(0, true);
		
		if (bidder == _highestBidder)
		{
			_highestBidder = findHighestBidder();
		}
	}

	public void deleteMe()
	{
		_highestBidder = null;
		_endDate = 0;
		
		if (_endTask != null)
		{
			_endTask.cancel(false);
			_endTask = null;
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM auction WHERE hall_id=?"))
		{
			statement.setInt(1, _clanHallId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Failed to delete auction for clan hall " + _clanHallId, e);
		}
	}
	
	public void cancelAuction()
	{
		deleteMe();
		removeBids(ClanTable.getInstance().getClanByName(_sellerClanName));
	}
	
	public void save()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO auction (hall_id, sellerId, sellerName, sellerClanName, bid, endDate) VALUES (?,?,?,?,?,?)"))
		{
			statement.setInt(1, _clanHallId);
			statement.setInt(2, _sellerId);
			statement.setString(3, _sellerName);
			statement.setString(4, _sellerClanName);
			statement.setInt(5, _sellerBid);
			statement.setLong(6, _endDate);
			
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Failed to store auction into database", e);
		}
	}
	
	public final long getEndDate()
	{
		return _endDate;
	}

	public final void setEndDate(long endDate)
	{
		_endDate = endDate;
	}
	
	public String getSellerName()
	{
		return _sellerName;
	}

	public String getSellerClanName()
	{
		return _sellerClanName;
	}

	public int getSellerBid()
	{
		return _sellerBid;
	}
	
	public final Map<Integer, Bidder> getBidders()
	{
		return _bidders;
	}
}