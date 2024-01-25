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
package net.sf.l2j.gameserver.model.actor.instance;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.model.Bidder;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.entity.Auction;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.StringUtil;

public final class L2AuctioneerInstance extends L2FolkInstance
{
	private static final Map<Integer, Auction> _pendingAuctions = new ConcurrentHashMap<>();
	
	public L2AuctioneerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		int condition = validateCondition(player);
		if (condition <= COND_ALL_FALSE)
		{
			player.sendMessage("Inappropriate conditions.");
			return;
		}
		
		if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
		{
			NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/auction/auction-busy.htm");
			player.sendPacket(html);
			return;
		}
		
		if (condition == COND_REGULAR)
		{
			List<ClanHall> rentableHalls = ClanHallManager.getInstance().getRentableClanHalls();
			if (rentableHalls.isEmpty())
			{
				player.sendPacket(new SystemMessage(SystemMessage.NO_CLAN_HALLS_UP_FOR_AUCTION));
				return;
			}
			
			StringTokenizer st = new StringTokenizer(command, " ");
			String actualCommand = st.nextToken(); // Get actual command
			String val = st.hasMoreTokens() ? st.nextToken() : "";
			
			if (actualCommand.equalsIgnoreCase("bidding"))
			{
				if (val.isEmpty())
				{
					return;
				}
				
				if (Config.DEBUG)
				{
					_log.warning("bidding show successful");
				}
				
				try
				{
					int hallId = Integer.parseInt(val);
					ClanHall ch = ClanHallManager.getInstance().getClanHallById(hallId);
					if (ch != null)
					{
						Auction auction = ch.getAuction();
						if (auction != null)
						{
							SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
							long remainingTime = auction.getEndDate() - System.currentTimeMillis();
							
							NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
							html.setFile("data/html/auction/AgitAuctionInfo.htm");
							html.replace("%AGIT_NAME%", ch.getName());
							html.replace("%OWNER_PLEDGE_NAME%", auction.getSellerClanName());
							html.replace("%OWNER_PLEDGE_MASTER%", auction.getSellerName());
							html.replace("%AGIT_SIZE%", String.valueOf(ch.getGrade() * 10));
							html.replace("%AGIT_LEASE%", String.valueOf(ch.getLease()));
							html.replace("%AGIT_LOCATION%", ch.getLocation());
							html.replace("%AGIT_AUCTION_END%", String.valueOf(format.format(auction.getEndDate())));
							html.replace("%AGIT_AUCTION_REMAIN%", String.valueOf(remainingTime / 3600000) + " hours " + String.valueOf(((remainingTime / 60000) % 60)) + " minutes");
							html.replace("%AGIT_AUCTION_MINBID%", String.valueOf(auction.getSellerBid()));
							html.replace("%AGIT_AUCTION_COUNT%", String.valueOf(auction.getBidders().size()));
							html.replace("%AGIT_AUCTION_DESC%", ch.getDesc());
							html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_list");
							html.replace("%AGIT_LINK_BIDLIST%", "bypass -h npc_" + getObjectId() + "_bidlist " + ch.getId());
							html.replace("%AGIT_LINK_RE%", "bypass -h npc_" + getObjectId() + "_bid1 " + ch.getId());
							
							player.sendPacket(html);
						}
					}
				}
				catch (Exception e)
				{
					player.sendMessage("Invalid auction!");
				}
				
				return;
			}
			else if (actualCommand.equalsIgnoreCase("list"))
			{
				SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
				
				/** Limit for make new page, prevent client crash **/
				int limit = 15;
				int start;
				int i = 1;
				double npage = Math.ceil((float) rentableHalls.size() / limit);
				
				if (val.isEmpty())
				{
					start = 1;
				}
				else
				{
					start = (limit * (Integer.parseInt(val) - 1)) + 1;
					limit *= Integer.parseInt(val);
				}
				if (Config.DEBUG)
				{
					_log.warning("cmd list: auction test started");
				}
				String items = "";
				items += "<table width=280 border=0><tr>";
				for (int j = 1; j <= npage; j++)
				{
					items += "<td><center><a action=\"bypass -h npc_" + getObjectId() + "_list " + j + "\"> Page " + j + " </a></center></td>";
				}
				items += "</tr></table>" + "<table width=280 border=0>";
				
				for (ClanHall ch : rentableHalls)
				{
					Auction auction = ch.getAuction();
					
					if (i > limit)
					{
						break;
					}
					else if (i < start)
					{
						i++;
						continue;
					}
					else
					{
						i++;
					}
					items += "<tr>" + "<td>" + ch.getLocation() + "</td>" + "<td><a action=\"bypass -h npc_" + getObjectId() + "_bidding " + ch.getId() + "\">" + ch.getName() + "</a></td>" + "<td>" + format.format(auction.getEndDate()) + "</td>" + "<td>" + auction.getSellerBid() + "</td>" + "</tr>";
					
				}
				items += "</table>";
				
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/auction/AgitAuctionList.htm");
				html.replace("%itemsField%", items);
				html.replace("%objectId%", String.valueOf(getObjectId()));
				player.sendPacket(html);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("location"))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/auction/location.htm");
				html.replace("%location%", MapRegionTable.getInstance().getClosestTownName(player));
				html.replace("%LOCATION%", getPictureName(player));
				html.replace("%objectId%", String.valueOf(getObjectId()));
				player.sendPacket(html);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("start"))
			{
				showChatWindow(player);
				return;
			}
			else
			{
				L2Clan clan = player.getClan();
				if (clan == null || !player.isClanLeader())
				{
					player.sendPacket(new SystemMessage(SystemMessage.CANNOT_PARTICIPATE_IN_AUCTION));
					return;
				}
				
				if (actualCommand.equalsIgnoreCase("bid"))
				{
					if (val.isEmpty())
					{
						return;
					}
					
					try
					{
						int hallId = Integer.parseInt(val);
						int bid = st.hasMoreTokens() ? Math.min(Integer.parseInt(st.nextToken()), Integer.MAX_VALUE) : 0;
						ClanHall ch = ClanHallManager.getInstance().getClanHallById(hallId);
						
						if (ch != null)
						{
							Auction auction = ch.getAuction();
							if (auction != null)
							{
								auction.setBid(player, bid);
							}
						}
					}
					catch (Exception e)
					{
						player.sendMessage("Invalid auction!");
					}
					
					return;
				}
				else if (actualCommand.equalsIgnoreCase("auction"))
				{
					if (val.isEmpty())
					{
						return;
					}
					
					try
					{
						int days = Integer.parseInt(val);
						int bid = st.hasMoreTokens() ? Math.max(Math.min(Integer.parseInt(st.nextToken()), Integer.MAX_VALUE), 0) : 0;
						
						ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
						if (ch == null)
						{
							return;
						}
						
						if (clan.getWarehouse().getAdena() < ch.getLease())
						{
							showSelectedItems(player, clan);
							player.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_ADENA_IN_CWH));
							return;
						}
						
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
						long endDate = (days * 86400000L) + System.currentTimeMillis();
						
						Auction auction = new Auction(ch.getId(), clan.getLeaderId(), clan.getLeaderName(), clan.getName(), bid);
						auction.setEndDate(endDate);
						_pendingAuctions.put(ch.getId(), auction);
						
						NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/auction/AgitSale3.htm");
						html.replace("%x%", val);
						html.replace("%AGIT_AUCTION_END%", String.valueOf(format.format(endDate)));
						html.replace("%AGIT_AUCTION_MINBID%", String.valueOf(ch.getDefaultBid()));
						html.replace("%AGIT_AUCTION_MIN%", String.valueOf(bid));
						html.replace("%AGIT_AUCTION_DESC%", ch.getDesc());
						html.replace("%objectId%", String.valueOf(getObjectId()));
						player.sendPacket(html);
					}
					catch (Exception e)
					{
						player.sendMessage("Invalid auction settings!");
					}
					
					return;
				}
				else if (actualCommand.equalsIgnoreCase("confirmAuction"))
				{
					ClanHall ch = ClanHallManager.getInstance().getClanHallById(clan.getHasHideout());
					if (ch == null)
					{
						return;
					}
					
					if (!_pendingAuctions.containsKey(ch.getId()))
					{
						return;
					}
					
					if (clan.getWarehouse().getAdena() < ch.getLease())
					{
						showSelectedItems(player, clan);
						player.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_ADENA_IN_CWH));
						return;
					}
					
					Auction auction = _pendingAuctions.remove(ch.getId());
					if (auction.takeItem(player, ch.getLease()))
					{
						auction.save();
						auction.startAutoTask();
						
						showSelectedItems(player, clan);
						player.sendPacket(new SystemMessage(SystemMessage.REGISTERED_FOR_CLANHALL));
					}
					
					return;
				}
				else if (actualCommand.equalsIgnoreCase("bid1"))
				{
					if (val.isEmpty())
					{
						return;
					}
					
					if (clan.getLevel() < 2)
					{
						player.sendPacket(new SystemMessage(SystemMessage.AUCTION_ONLY_CLAN_LEVEL_2_HIGHER));
						return;
					}
					
					if (clan.getHasHideout() > 0)
					{
						player.sendPacket(new SystemMessage(SystemMessage.CANNOT_PARTICIPATE_IN_AUCTION));
						return;
					}
					
					try
					{
						int hallId = Integer.parseInt(val);
						
						if (clan.getAuctionBiddedAt() > 0 && clan.getAuctionBiddedAt() != hallId)
						{
							player.sendPacket(new SystemMessage(SystemMessage.ALREADY_SUBMITTED_BID));
							return;
						}
						
						ClanHall ch = ClanHallManager.getInstance().getClanHallById(hallId);
						if (ch == null)
						{
							return;
						}
						
						Auction auction = ch.getAuction();
						if (auction == null)
						{
							return;
						}
						
						NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/auction/AgitBid1.htm");
						html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_bidding " + val);
						html.replace("%PLEDGE_ADENA%", String.valueOf(clan.getWarehouse().getAdena()));
						html.replace("%AGIT_AUCTION_MINBID%", String.valueOf(auction.getSellerBid()));
						html.replace("npc_%objectId%_bid", "npc_" + getObjectId() + "_bid " + val);
						player.sendPacket(html);
					}
					catch (Exception e)
					{
						player.sendMessage("Invalid auction!");
					}
					return;
				}
				else if (actualCommand.equalsIgnoreCase("bidlist"))
				{
					try
					{
						int hallId = val.isEmpty() ? clan.getAuctionBiddedAt() : Integer.parseInt(val);
						if (hallId <= 0)
						{
							return;
						}
						
						ClanHall ch = ClanHallManager.getInstance().getClanHallById(hallId);
						if (ch == null)
						{
							return;
						}
						
						Auction auction = ch.getAuction();
						if (auction == null)
						{
							return;
						}
						
						if (Config.DEBUG)
						{
							_log.warning("bidlist: auction test started");
						}
						
						Collection<Bidder> bidders = auction.getBidders().values();
						SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
						
						StringBuilder sb = new StringBuilder(bidders.size() * 150);
						for (Bidder bidder : bidders)
						{
							StringUtil.append(sb, "<tr><td align=center>", bidder.getClanName(), "</td><td align=center>", bidder.getName(), "</td><td align=center>", format.format(bidder.getBidTime()), "</td></tr>");
						}
						
						NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/auction/AgitBidderList.htm");
						html.replace("%AGIT_LIST%", sb.toString());
						html.replace("%x%", val);
						html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_bidding " + hallId);
						player.sendPacket(html);
					}
					catch (Exception e)
					{
						player.sendMessage("Invalid bid list!");
					}
					return;
				}
				else if (actualCommand.equalsIgnoreCase("selectedItems"))
				{
					showSelectedItems(player, clan);
					return;
				}
				else if (actualCommand.equalsIgnoreCase("cancelBid"))
				{
					ClanHall ch = ClanHallManager.getInstance().getClanHallById(clan.getAuctionBiddedAt());
					if (ch == null)
					{
						return;
					}
					
					Auction auction = ch.getAuction();
					if (auction == null)
					{
						return;
					}
					
					Bidder bidder = auction.getBidders().get(player.getClanId());
					if (bidder == null)
					{
						return;
					}
					
					int bid = bidder.getBid();
					
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/auction/AgitBidCancel.htm");
					html.replace("%AGIT_BID%", String.valueOf(bid));
					html.replace("%AGIT_BID_REMAIN%", String.valueOf((int) (bid * 0.9)));
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					return;
				}
				else if (actualCommand.equalsIgnoreCase("doCancelBid"))
				{
					ClanHall ch = ClanHallManager.getInstance().getClanHallById(clan.getAuctionBiddedAt());
					if (ch == null)
					{
						return;
					}
					
					Auction auction = ch.getAuction();
					if (auction != null)
					{
						auction.cancelBid(clan);
						player.sendPacket(new SystemMessage(SystemMessage.CANCELED_BID));
					}
					
					return;
				}
				else if (actualCommand.equalsIgnoreCase("cancelAuction"))
				{
					ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
					if (ch != null)
					{
						NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/auction/AgitSaleCancel.htm");
						html.replace("%AGIT_DEPOSIT%", String.valueOf(ch.getLease()));
						html.replace("%objectId%", String.valueOf(getObjectId()));
						player.sendPacket(html);
					}
					
					return;
				}
				else if (actualCommand.equalsIgnoreCase("doCancelAuction"))
				{
					ClanHall ch = ClanHallManager.getInstance().getClanHallById(clan.getHasHideout());
					if (ch == null)
					{
						return;
					}
					
					Auction auction = ch.getAuction();
					if (auction != null)
					{
						auction.cancelAuction();
						player.sendPacket(new SystemMessage(SystemMessage.CANCELED_BID));
					}
					
					showChatWindow(player);
					return;
				}
				else if (actualCommand.equalsIgnoreCase("sale2"))
				{
					ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
					if (ch == null)
					{
						return;
					}
					
					if (clan.getWarehouse().getAdena() < ch.getLease())
					{
						showSelectedItems(player, clan);
						player.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_ADENA_IN_CWH));
						return;
					}
					
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/auction/AgitSale2.htm");
					html.replace("%AGIT_LAST_PRICE%", String.valueOf(ch.getLease()));
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					return;
				}
				else if (actualCommand.equalsIgnoreCase("sale"))
				{
					ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
					if (ch != null)
					{
						NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/auction/AgitSale1.htm");
						html.replace("%AGIT_DEPOSIT%", String.valueOf(ch.getLease()));
						html.replace("%AGIT_PLEDGE_ADENA%", String.valueOf(clan.getWarehouse().getAdena()));
						html.replace("%objectId%", String.valueOf(getObjectId()));
						player.sendPacket(html);
					}
					return;
				}
				else if (actualCommand.equalsIgnoreCase("rebid"))
				{
					ClanHall ch = ClanHallManager.getInstance().getClanHallById(clan.getAuctionBiddedAt());
					if (ch == null)
					{
						return;
					}
					
					Auction auction = ch.getAuction();
					if (auction == null)
					{
						return;
					}
					
					Bidder bidder = auction.getBidders().get(player.getClanId());
					if (bidder != null)
					{
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
						
						NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/auction/AgitBid2.htm");
						html.replace("%AGIT_AUCTION_BID%", String.valueOf(bidder.getBid()));
						html.replace("%AGIT_AUCTION_MINBID%", String.valueOf(ch.getDefaultBid()));
						html.replace("%AGIT_AUCTION_END%", String.valueOf(format.format(auction.getEndDate())));
						html.replace("npc_%objectId%_bid1", "npc_" + getObjectId() + "_bid1 " + ch.getId());
						player.sendPacket(html);
					}
					return;
				}
			}
		}
		
		super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(L2PcInstance player)
	{
		String filename;
		
		int condition = validateCondition(player);
		if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
		{
			filename = "data/html/auction/auction-busy.htm"; // Busy because of siege
		}
		else
		{
			filename = "data/html/auction/auction.htm";
		}
		
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcId%", String.valueOf(getNpcId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
		
		player.sendPacket(new ActionFailed());
	}
	
	@Override
	protected int validateCondition(L2PcInstance player)
	{
		if (getCastle() != null && getCastle().getCastleId() > 0)
		{
			if (getCastle().getSiege().getIsInProgress())
			{
				return COND_BUSY_BECAUSE_OF_SIEGE; // Busy because of siege
			}
			return COND_REGULAR;
		}
		
		return super.validateCondition(player);
	}
	
	private void showSelectedItems(L2PcInstance player, L2Clan clan)
	{
		if (clan.getHasHideout() == 0)
		{
			if (clan.getAuctionBiddedAt() > 0)
			{
				ClanHall ch = ClanHallManager.getInstance().getClanHallById(clan.getAuctionBiddedAt());
				if (ch == null)
				{
					return;
				}
				
				Auction auction = ch.getAuction();
				if (auction == null)
				{
					return;
				}
				
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				long remainingTime = auction.getEndDate() - System.currentTimeMillis();
				
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/auction/AgitBidInfo.htm");
				html.replace("%AGIT_NAME%", ch.getName());
				html.replace("%OWNER_PLEDGE_NAME%", auction.getSellerClanName());
				html.replace("%OWNER_PLEDGE_MASTER%", auction.getSellerName());
				html.replace("%AGIT_AUCTION_MINBID%", String.valueOf(auction.getSellerBid()));
				html.replace("%AGIT_SIZE%", String.valueOf(ch.getGrade() * 10));
				html.replace("%AGIT_LEASE%", String.valueOf(ch.getLease()));
				html.replace("%AGIT_LOCATION%", ch.getLocation());
				html.replace("%AGIT_AUCTION_END%", String.valueOf(format.format(auction.getEndDate())));
				html.replace("%AGIT_AUCTION_REMAIN%", String.valueOf(remainingTime / 3600000) + " hours " + String.valueOf(((remainingTime / 60000) % 60)) + " minutes");
				html.replace("%AGIT_AUCTION_MYBID%", auction.getBidders().containsKey(player.getClanId()) ? String.valueOf(auction.getBidders().get(player.getClanId()).getBid()) : "");
				html.replace("%AGIT_AUCTION_DESC%", ch.getDesc());
				html.replace("%objectId%", String.valueOf(getObjectId()));
				player.sendPacket(html);
			}
		}
		else
		{
			ClanHall ch = ClanHallManager.getInstance().getClanHallById(clan.getHasHideout());
			if (ch == null)
			{
				return;
			}
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			Auction auction = ch.getAuction();
			
			if (auction != null)
			{
				long remainingTime = auction.getEndDate() - System.currentTimeMillis();
				
				html.setFile("data/html/auction/AgitSaleInfo.htm");
				html.replace("%AGIT_NAME%", ch.getName());
				html.replace("%AGIT_OWNER_PLEDGE_NAME%", auction.getSellerClanName());
				html.replace("%OWNER_PLEDGE_MASTER%", auction.getSellerName());
				html.replace("%AGIT_SIZE%", String.valueOf(ch.getGrade() * 10));
				html.replace("%AGIT_LEASE%", String.valueOf(ch.getLease()));
				html.replace("%AGIT_LOCATION%", ch.getLocation());
				html.replace("%AGIT_AUCTION_END%", String.valueOf(format.format(auction.getEndDate())));
				html.replace("%AGIT_AUCTION_REMAIN%", String.valueOf(remainingTime / 3600000) + " hours " + String.valueOf(((remainingTime / 60000) % 60)) + " minutes");
				html.replace("%AGIT_AUCTION_MINBID%", String.valueOf(auction.getSellerBid()));
				html.replace("%AGIT_AUCTION_BIDCOUNT%", String.valueOf(auction.getBidders().size()));
				html.replace("%AGIT_AUCTION_DESC%", ch.getDesc());
				html.replace("%id%", String.valueOf(ch.getId()));
				html.replace("%objectId%", String.valueOf(getObjectId()));
			}
			else
			{
				html.setFile("data/html/auction/AgitInfo.htm");
				html.replace("%AGIT_NAME%", ch.getName());
				html.replace("%AGIT_OWNER_PLEDGE_NAME%", clan.getName());
				html.replace("%OWNER_PLEDGE_MASTER%", clan.getLeaderName());
				html.replace("%AGIT_SIZE%", String.valueOf(ch.getGrade() * 10));
				html.replace("%AGIT_LEASE%", String.valueOf(ch.getLease()));
				html.replace("%AGIT_LOCATION%", ch.getLocation());
				html.replace("%objectId%", String.valueOf(getObjectId()));
			}
			player.sendPacket(html);
		}
	}
	
	private String getPictureName(L2PcInstance player)
	{
		int nearestTownId = MapRegionTable.getInstance().getMapRegion(player.getX(), player.getY());
		String nearestTown;
		
		switch (nearestTownId)
		{
			case 5:
				nearestTown = "GLUDIO";
				break;
			case 6:
				nearestTown = "GLUDIN";
				break;
			case 7:
				nearestTown = "DION";
				break;
			case 8:
				nearestTown = "GIRAN";
				break;
			case 15:
				nearestTown = "GODARD";
				break;
			default:
				nearestTown = "ADEN";
				break;
		}
		
		return nearestTown;
	}
}