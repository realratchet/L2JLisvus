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

import java.util.List;
import java.util.StringTokenizer;

import net.sf.l2j.gameserver.TradeController;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager.SeedProduction;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.BuyList;
import net.sf.l2j.gameserver.network.serverpackets.BuyListSeed;
import net.sf.l2j.gameserver.network.serverpackets.ExShowCropInfo;
import net.sf.l2j.gameserver.network.serverpackets.ExShowManorDefaultInfo;
import net.sf.l2j.gameserver.network.serverpackets.ExShowProcureCropDetail;
import net.sf.l2j.gameserver.network.serverpackets.ExShowSeedInfo;
import net.sf.l2j.gameserver.network.serverpackets.ExShowSellCropList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class L2ManorManagerInstance extends L2MerchantInstance
{
	public L2ManorManagerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	private void showBuyWindow(L2PcInstance player, String val)
	{
		double taxRate = 0;
		player.tempInventoryDisable();
		
		L2TradeList list = TradeController.getInstance().getBuyList(Integer.parseInt(val));
		
		if (list != null)
		{
			list.getItems().get(0).setCount(1);
			BuyList bl = new BuyList(list, player.getAdena(), taxRate);
			player.sendPacket(bl);
		}
		else
		{
			_log.info("possible client hacker: " + player.getName() + " attempting to buy from GM shop! < Ban him!");
			_log.info("buylist id:" + val);
		}
		
		player.sendPacket(new ActionFailed());
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if ((player.getLastFolkNPC() == null) || (player.getLastFolkNPC().getObjectId() != getObjectId()))
		{
			return;
		}
		
		if (command.startsWith("manor_menu_select"))
		{
			// input string format:
			// manor_menu_select?ask=X&state=Y&time=X
			if (CastleManorManager.getInstance().isUnderMaintenance())
			{
				player.sendPacket(new SystemMessage(SystemMessage.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE));
				player.sendPacket(new ActionFailed());
				return;
			}
			
			String params = command.substring(command.indexOf("?") + 1);
			StringTokenizer st = new StringTokenizer(params, "&");
			int ask = Integer.parseInt(st.nextToken().split("=")[1]);
			int state = Integer.parseInt(st.nextToken().split("=")[1]);
			int time = Integer.parseInt(st.nextToken().split("=")[1]);
			
			int castleId;
			if (state == -1)
			{
				castleId = getCastle().getCastleId();
			}
			else
			{
				castleId = state;
			}
			
			switch (ask)
			{
				// Main action
				case 1: // Seed purchase
					if (castleId != getCastle().getCastleId())
					{
						player.sendPacket(new SystemMessage(SystemMessage.HERE_YOU_CAN_BUY_ONLY_SEEDS_OF_S1_MANOR));
					}
					else
					{
						L2TradeList tradeList = new L2TradeList(0);
						List<SeedProduction> seeds = getCastle().getSeedProduction(CastleManorManager.PERIOD_CURRENT);
						
						for (SeedProduction s : seeds)
						{
							L2ItemInstance item = ItemTable.getInstance().createDummyItem(s.getId());
							item.setPriceToSell(s.getPrice());
							item.setCount(s.getCanProduce());
							if ((item.getCount() > 0) && (item.getPriceToSell() > 0))
							{
								tradeList.addItem(item);
							}
						}
						BuyListSeed bl = new BuyListSeed(tradeList, castleId, player.getAdena());
						player.sendPacket(bl);
					}
					break;
				case 2: // Crop sales
					player.sendPacket(new ExShowSellCropList(player, castleId, getCastle().getCropProcure(CastleManorManager.PERIOD_CURRENT)));
					break;
				case 3: // Current seeds (Manor info)
					if ((time == 1) && !CastleManager.getInstance().getCastleById(castleId).isNextPeriodApproved())
					{
						player.sendPacket(new ExShowSeedInfo(castleId, null));
					}
					else
					{
						player.sendPacket(new ExShowSeedInfo(castleId, CastleManager.getInstance().getCastleById(castleId).getSeedProduction(time)));
					}
					break;
				case 4: // Current crops (Manor info)
					if ((time == 1) && !CastleManager.getInstance().getCastleById(castleId).isNextPeriodApproved())
					{
						player.sendPacket(new ExShowCropInfo(castleId, null));
					}
					else
					{
						player.sendPacket(new ExShowCropInfo(castleId, CastleManager.getInstance().getCastleById(castleId).getCropProcure(time)));
					}
					break;
				case 5: // Basic info (Manor info)
					player.sendPacket(new ExShowManorDefaultInfo());
					break;
				case 6: // Buy harvester
					showBuyWindow(player, "1" + getNpcId());
					break;
				case 9: // Edit sales (Crop sales)
					player.sendPacket(new ExShowProcureCropDetail(state));
					break;
			}
		}
		else if (command.startsWith("help"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken(); // discard first
			String filename = "manor_client_help00" + st.nextToken() + ".htm";
			showChatWindow(player, filename);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	public String getHtmlPath()
	{
		return "data/html/manormanager/";
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		return "data/html/manormanager/manager.htm"; // Used only in parent method to return from "Territory status" to initial screen.
	}
	
	@Override
	public void showChatWindow(L2PcInstance player)
	{
		// If player is a lord of this manor, alternative message from NPC
		if (CastleManorManager.getInstance().isDisabled())
		{
			NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/npcdefault.htm");
			html.replace("%objectId%", String.valueOf(getObjectId()));
			html.replace("%npcname%", getName());
			player.sendPacket(html);
			player.sendPacket(new ActionFailed());
		}
		else if (!player.isGM() // Player is not GM
			&& (getCastle() != null) && (getCastle().getCastleId() > 0 // Verification of castle
			) && (player.getClan() != null // Player have clan
			) && (getCastle().getOwnerId() == player.getClanId() // Player's clan owning the castle
			) && player.isClanLeader())
		{
			showChatWindow(player, "manager-lord.htm");
		}
		else
		{
			showChatWindow(player, "manager.htm");
		}
	}
	
	@Override
	public void showChatWindow(L2PcInstance player, String filename)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(getHtmlPath() + filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcId%", String.valueOf(getNpcId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
		
		player.sendPacket(new ActionFailed());
	}
}