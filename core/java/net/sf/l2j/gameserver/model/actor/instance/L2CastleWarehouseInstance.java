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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.WareHouseDepositList;
import net.sf.l2j.gameserver.network.serverpackets.WareHouseWithdrawalList;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author l3x
 */
public class L2CastleWarehouseInstance extends L2FolkInstance
{
	/**
	 * @param objectId
	 * @param template
	 */
	public L2CastleWarehouseInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	private void showRetrieveWindow(L2PcInstance player)
	{
		player.sendPacket(new ActionFailed());
		player.setActiveWarehouse(player.getWarehouse());
		
		if (player.getActiveWarehouse().getSize() == 0)
		{
			player.sendPacket(new SystemMessage(SystemMessage.NOTHING_IN_WAREHOUSE));
			return;
		}
		
		if (Config.DEBUG)
		{
			_log.fine("Showing stored items");
		}
		
		player.sendPacket(new WareHouseWithdrawalList(player, WareHouseWithdrawalList.Private));
	}
	
	private void showDepositWindow(L2PcInstance player)
	{
		player.sendPacket(new ActionFailed());
		player.setActiveWarehouse(player.getWarehouse());
		player.tempInventoryDisable();
		
		if (Config.DEBUG)
		{
			_log.fine("Showing items to deposit");
		}
		
		player.sendPacket(new WareHouseDepositList(player, WareHouseDepositList.Private));
	}
	
	private void showDepositWindowClan(L2PcInstance player)
	{
		player.sendPacket(new ActionFailed());
		if (player.getClan() != null)
		{
			if (player.getClan().getLevel() == 0)
			{
				
				player.sendPacket(new SystemMessage(SystemMessage.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE));
				
				return;
			}
			
			player.setActiveWarehouse(player.getClan().getWarehouse());
			player.tempInventoryDisable();
			
			if (Config.DEBUG)
			{
				_log.fine("Showing items to deposit - clan");
			}
			
			WareHouseDepositList dl = new WareHouseDepositList(player, WareHouseDepositList.Clan);
			player.sendPacket(dl);
			
		}
	}
	
	private void showWithdrawWindowClan(L2PcInstance player)
	{
		player.sendPacket(new ActionFailed());
		if ((player.getClanPrivileges() & L2Clan.CP_CL_VIEW_WAREHOUSE) != L2Clan.CP_CL_VIEW_WAREHOUSE)
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_CLAN_WAREHOUSE));
			return;
		}

		if (player.getClan().getLevel() == 0)
		{
			
			player.sendPacket(new SystemMessage(SystemMessage.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE));
			return;
		}
		
		player.setActiveWarehouse(player.getClan().getWarehouse());
		
		if (Config.DEBUG)
		{
			_log.fine("Showing items to withdraw - clan");
		}
		
		player.sendPacket(new WareHouseWithdrawalList(player, WareHouseWithdrawalList.Clan));
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		// little check to prevent enchant exploit
		if (player.getActiveEnchantItem() != null)
		{
			_log.info("Player " + player.getName() + " trying to use enchant exploit, ban this player!");
			player.logout();
			return;
		}
		
		if (command.startsWith("WithdrawP"))
		{
			showRetrieveWindow(player);
		}
		else if (command.equals("DepositP"))
		{
			showDepositWindow(player);
		}
		else if (command.equals("WithdrawC"))
		{
			showWithdrawWindowClan(player);
		}
		else if (command.equals("DepositC"))
		{
			showDepositWindowClan(player);
		}
		else if (command.startsWith("Chat"))
		{
			int val = 0;
			try
			{
				val = Integer.parseInt(command.substring(5));
			}
			catch (IndexOutOfBoundsException ioobe)
			{
			}
			catch (NumberFormatException nfe)
			{
			}
			showChatWindow(player, val);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public void showChatWindow(L2PcInstance player, int val)
	{
		player.sendPacket(new ActionFailed());
		String filename = "data/html/castlewarehouse/castlewarehouse-no.htm";
		
		int condition = validateCondition(player);
		if (condition > COND_ALL_FALSE)
		{
			if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
			{
				filename = "data/html/castlewarehouse/castlewarehouse-busy.htm"; // Busy because of siege
			}
			else if (condition == COND_CASTLE_OWNER) // Clan owns castle
			{
				if (val == 0)
				{
					filename = "data/html/castlewarehouse/castlewarehouse.htm";
				}
				else
				{
					filename = "data/html/castlewarehouse/castlewarehouse-" + val + ".htm";
				}
			}
		}
		
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
	
	@Override
	protected int validateCondition(L2PcInstance player)
	{
		if (player.isGM())
		{
			return COND_CASTLE_OWNER;
		}
		
		if ((getCastle() != null) && (getCastle().getCastleId() > 0))
		{
			if (player.getClan() != null)
			{
				if (getCastle().getSiege().getIsInProgress())
				{
					return COND_BUSY_BECAUSE_OF_SIEGE; // Busy because of siege
				}
				else if (getCastle().getOwnerId() == player.getClanId())
				{
					return COND_CASTLE_OWNER;
				}
			}
		}
		
		return super.validateCondition(player);
	}
	
	@Override
	public final boolean isWarehouse()
	{
		return true;
	}
}