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

import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.TradeController;
import net.sf.l2j.gameserver.model.L2Multisell;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SellList;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.network.serverpackets.ShopPreviewList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.StringUtil;

/**
 * This class ...
 * @version $Revision: 1.10.4.9 $ $Date: 2005/04/11 10:06:08 $
 */
public class L2MerchantInstance extends L2FolkInstance
{
	private static int PET_RENT_DATA[][] =
	{
		{
			1800,
			30
		},
		{
			7200,
			60
		},
		{
			720000,
			600
		},
		{
			6480000,
			1800
		}
	};
	
	/**
	 * @param objectId
	 * @param template
	 */
	public L2MerchantInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		
		return "data/html/merchant/" + pom + ".htm";
	}
	
	private void showWearWindow(L2PcInstance player, int val)
	{
		player.tempInventoryDisable();
		
		if (Config.DEBUG)
		{
			_log.fine("Showing wearlist");
		}
		
		L2TradeList list = TradeController.getInstance().getBuyList(val);
		
		if (list != null)
		{
			ShopPreviewList bl = new ShopPreviewList(list, player.getAdena(), player.getExpertiseIndex());
			player.sendPacket(bl);
		}
		else
		{
			_log.warning("no buylist with id:" + val);
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
	
	private void showSellWindow(L2PcInstance player)
	{
		if (Config.DEBUG)
		{
			_log.fine("Showing sell list");
		}
		
		player.sendPacket(new SellList(player));
		
		if (Config.DEBUG)
		{
			_log.fine("Showing sell window");
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		if (actualCommand.equalsIgnoreCase("Buy"))
		{
			if (!st.hasMoreTokens())
			{
				return;
			}
			
			int val = Integer.parseInt(st.nextToken());
			showBuyWindow(player, val);
		}
		else if (actualCommand.equalsIgnoreCase("Sell"))
		{
			showSellWindow(player);
		}
		else if (actualCommand.equalsIgnoreCase("RentPet"))
		{
			if (Config.ALLOW_PET_RENT && Config.LIST_PET_RENT_NPC.contains(getNpcId()))
			{
				if (!st.hasMoreTokens())
				{
					showRentPetWindow(player);
				}
				else
				{
					int val = Integer.parseInt(st.nextToken());
					tryRentPet(player, val);
				}
			}
		}
		else if (actualCommand.equalsIgnoreCase("Wear") && Config.ALLOW_WEAR)
		{
			if (!st.hasMoreTokens())
			{
				return;
			}
			
			int val = Integer.parseInt(st.nextToken());
			showWearWindow(player, val);
		}
		else if (actualCommand.equalsIgnoreCase("Multisell"))
		{
			if (!st.hasMoreTokens())
			{
				return;
			}
			
			int val = Integer.parseInt(st.nextToken());
			L2Multisell.getInstance().createMultiSell(val, player, false, this);
		}
		else if (actualCommand.equalsIgnoreCase("Exc_Multisell"))
		{
			if (!st.hasMoreTokens())
			{
				return;
			}
			
			int val = Integer.parseInt(st.nextToken());
			L2Multisell.getInstance().createMultiSell(val, player, true, this);
		}
		else
		{
			// this class dont know any other commands, let forward
			// the command to the parent class
			super.onBypassFeedback(player, command);
		}
	}
	
	public void showRentPetWindow(L2PcInstance player)
	{
		StringBuilder sb = new StringBuilder(230 * PET_RENT_DATA.length);
		
		sb.append("<html><body>" + getTemplate().title + " " + getTemplate().name + ":<br>");
		sb.append("You can rent a wyvern or strider for adena.<br>My prices:<br1>");
		sb.append("<table border=0><tr><td>Ride</td></tr>");
		sb.append("<tr><td>Wyvern</td><td>Strider</td></tr>");

		for (int i = 0; i < PET_RENT_DATA.length; i++)
		{
			int[] rentData = PET_RENT_DATA[i];
			int cost = rentData[0];
			int duration = rentData[1];
			int val = i + 1;

			sb.append("<tr><td><a action=\"bypass -h npc_%objectId%_RentPet " + val + "\">" +  duration + " sec/" + cost + " adena</a></td><td><a action=\"bypass -h npc_%objectId%_RentPet " + (val + 10) + "\">" +  duration + " sec/" + ( cost / 2) + " adena</a></td></tr>");
		}

		sb.append("</table>");
		sb.append("</body></html>");
		
		insertObjectIdAndShowChatWindow(player, sb.toString());
	}
	
	public void tryRentPet(L2PcInstance player, int val)
	{
		if (player == null)
		{
			return;
		}

		if (player.getPet() != null)
		{
			player.sendMessage("Please unsummon your current pet in order to rent one.");
			return;
		}

		if (player.isMounted() || player.isRentedPet())
		{
			player.sendMessage("Cannot rent a pet while mounted.");
			return;
		}
		
		if (!player.disarmWeapons())
		{
			return;
		}
		
		int petId;
		double costRate;
		
		if (val > 10)
		{
			petId = 12526;
			val -= 10;
			costRate = 0.5;
		}
		else
		{
			petId = 12621;
			costRate = 1;
		}
		
		if (val < 1 || val > PET_RENT_DATA.length)
		{
			return;
		}

		int[] rentData = PET_RENT_DATA[val - 1];
		int price = (int) (costRate * rentData[0]);
		int time = rentData[1];
		
		if (!player.reduceAdena("Rent", price, player.getLastFolkNPC(), true))
		{
			return;
		}
		
		player.abortCast();
		player.mount(petId, 0, false);
		player.sendPacket(new SetupGauge(SetupGauge.GREEN, time * 1000));
		player.startRentPet(time);
	}
	
	@Override
	public void onActionShift(L2GameClient client)
	{
		L2PcInstance player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		if (player.isGM())
		{
			player.setTarget(this);
			
			MyTargetSelected my = new MyTargetSelected(getObjectId(), player.getLevel() - getLevel());
			player.sendPacket(my);
			
			if (isAutoAttackable(player))
			{
				StatusUpdate su = new StatusUpdate(getObjectId());
				su.addAttribute(StatusUpdate.CUR_HP, (int) getCurrentHp());
				su.addAttribute(StatusUpdate.MAX_HP, getMaxHp());
				player.sendPacket(su);
			}
			
			NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			StringBuilder sb = new StringBuilder(500);
			StringUtil.append(sb, "<html><body><table border=0>");
			StringUtil.append(sb, "<tr><td>Current Target:</td></tr>");
			StringUtil.append(sb, "<tr><td><br></td></tr>");
			
			StringUtil.append(sb, "<tr><td>Object ID: " + getObjectId() + "</td></tr>");
			StringUtil.append(sb, "<tr><td>Template ID: " + getNpcId() + "</td></tr>");
			StringUtil.append(sb, "<tr><td><br></td></tr>");
			
			StringUtil.append(sb, "<tr><td>HP: " + getCurrentHp() + "</td></tr>");
			StringUtil.append(sb, "<tr><td>MP: " + getCurrentMp() + "</td></tr>");
			StringUtil.append(sb, "<tr><td>Level: " + getLevel() + "</td></tr>");
			StringUtil.append(sb, "<tr><td><br></td></tr>");
			
			StringUtil.append(sb, "<tr><td>Class: " + getClass().getName() + "</td></tr>");
			StringUtil.append(sb, "<tr><td><br></td></tr>");
			
			// changed by terry 2005-02-22 21:45
			StringUtil.append(sb, "</table><table><tr><td><button value=\"Edit NPC\" action=\"bypass -h admin_edit_npc " + getNpcId() + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			StringUtil.append(sb, "<td><button value=\"Kill\" action=\"bypass -h admin_kill\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			StringUtil.append(sb, "<tr><td><button value=\"Show DropList\" action=\"bypass -h admin_show_droplist " + getNpcId() + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			StringUtil.append(sb, "<td><button value=\"Delete\" action=\"bypass -h admin_delete\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			StringUtil.append(sb, "</table>");
			
			if (player.isGM())
			{
				StringUtil.append(sb, "<button value=\"View Shop\" action=\"bypass -h admin_showShop " + getNpcId() + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></br>");
				StringUtil.append(sb, "<button value=\"Lease next week\" action=\"bypass -h npc_" + getObjectId() + "_Lease\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
				StringUtil.append(sb, "<button value=\"Abort current leasing\" action=\"bypass -h npc_" + getObjectId() + "_Lease next\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
				StringUtil.append(sb, "<button value=\"Manage items\" action=\"bypass -h npc_" + getObjectId() + "_Lease manage\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			}
			
			StringUtil.append(sb, "</body></html>");
			
			html.setHtml(sb.toString());
			player.sendPacket(html);
		}
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}