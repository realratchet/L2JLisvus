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
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.instancemanager.CoupleManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.entity.Couple;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author evill33t & squeezed
 */
public class L2WeddingManagerInstance extends L2FolkInstance
{
	/**
	 * @param objectId
	 * @param template
	 */
	public L2WeddingManagerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void showChatWindow(L2PcInstance player)
	{
		if (player == null)
			return;

		String fileName;
		String condition = null;
		if (Config.ALLOW_WEDDING)
		{
			condition = "For this to work, the requester has to pay the cost of <font color=\"LEVEL\">" + String.valueOf(Config.WEDDING_PRICE) + " adena</font>";
			// Add formal wear details only if needed
			if (Config.WEDDING_FORMAL_WEAR)
			{
				condition += " as well as both of them to be wearing a <font color=\"LEVEL\">Formal Wear</font>";
			}
			condition += ".";
			
			fileName = "data/html/mods/wedding/start.htm";
		}
		else
		{
			fileName = "data/html/npcdefault.htm";
		}
		
		sendHtmlMessage(player, fileName, condition);
		player.sendPacket(new ActionFailed());
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (!Config.ALLOW_WEDDING)
		{
			return;
		}
		
		// Standard message
		String filename = "data/html/npcdefault.htm";
		
		// If player has no partner
		if (player.getPartnerId() == 0)
		{
			filename = "data/html/mods/wedding/nopartner.htm";
			sendHtmlMessage(player, filename, null);
			return;
		}

		L2PcInstance pTarget = L2World.getInstance().getPlayer(player.getPartnerId());
		// Partner offline
		if (pTarget == null || !pTarget.isOnline())
		{
			filename = "data/html/mods/wedding/notfound.htm";
			sendHtmlMessage(player, filename, null);
			return;
		}

		// Already married
		if (player.isMarried())
		{
			filename = "data/html/mods/wedding/already.htm";
			sendHtmlMessage(player, filename, null);
			return;
		}
		else if (player.isMarryAccepted())
		{
			filename = "data/html/mods/wedding/waitforpartner.htm";
			sendHtmlMessage(player, filename, null);
			return;
		}
		else if (command.startsWith("AcceptWedding"))
		{
			// Accept the wedding request
			player.setMarryAccepted(true);
			Couple couple = CoupleManager.getInstance().getCouple(player.getCoupleId());
			couple.marry();
			
			// Messages to the couple
			player.sendMessage("Congratulations! You are married!");
			player.setIsMarried(true);
			player.setMarryRequest(false);
			player.setMarryAccepted(false);
			pTarget.sendMessage("Congratulations! You are married!");
			pTarget.setIsMarried(true);
			pTarget.setMarryRequest(false);
			pTarget.setMarryAccepted(false);
			
			// Wedding march
			MagicSkillUse msu = new MagicSkillUse(player, player, 2230, 1, 1, 0);
			player.broadcastPacket(msu);
			msu = new MagicSkillUse(pTarget, pTarget, 2230, 1, 1, 0);
			pTarget.broadcastPacket(msu);
			
			// Fireworks
			msu = new MagicSkillUse(player, player, 2025, 1, 1, 0);
			player.sendPacket(msu);
			player.broadcastPacket(msu);
			
			msu = new MagicSkillUse(pTarget, pTarget, 2025, 1, 1, 0);
			pTarget.sendPacket(msu);
			pTarget.broadcastPacket(msu);
			
			Announcements.getInstance().announceToAll("Congratulations to " + pTarget.getName() + " and " + player.getName() + "! They have been married.");
			
			filename = "data/html/mods/wedding/accepted.htm";
			sendHtmlMessage(pTarget, filename, pTarget.getName());
			return;
		}
		else if (command.startsWith("DeclineWedding"))
		{
			player.setMarryRequest(false);
			pTarget.setMarryRequest(false);
			player.setMarryAccepted(false);
			pTarget.setMarryAccepted(false);
			player.sendMessage("You declined.");
			pTarget.sendMessage("Your partner declined.");
			filename = "data/html/mods/wedding/declined.htm";
			sendHtmlMessage(pTarget, filename, pTarget.getName());
			return;
		}
		else if (player.isMarryRequested())
		{
			// Check for formal wear
			if (Config.WEDDING_FORMAL_WEAR)
			{
				Inventory inv = player.getInventory();
				L2ItemInstance item = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
				if (item == null)
				{
					player.setIsWearingFormalWear(false);
				}
				else
				{
					if (item.getItemId() == 6408)
					{
						player.setIsWearingFormalWear(true);
					}
					else
					{
						player.setIsWearingFormalWear(false);
					}
				}
			}
			if (Config.WEDDING_FORMAL_WEAR && !player.isWearingFormalWear())
			{
				filename = "data/html/mods/wedding/noformal.htm";
				sendHtmlMessage(player, filename, null);
				return;
			}
			filename = "data/html/mods/wedding/ask.htm";
			player.setMarryRequest(false);
			pTarget.setMarryRequest(false);
			sendHtmlMessage(player, filename, pTarget.getName());
			return;
		}
		else if (command.startsWith("AskWedding"))
		{
			// Check for formal wear
			if (Config.WEDDING_FORMAL_WEAR)
			{
				Inventory inv = player.getInventory();
				L2ItemInstance item = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
				
				if (item == null)
				{
					player.setIsWearingFormalWear(false);
				}
				else
				{
					if (item.getItemId() == 6408)
					{
						player.setIsWearingFormalWear(true);
					}
					else
					{
						player.setIsWearingFormalWear(false);
					}
				}
			}
			if (Config.WEDDING_FORMAL_WEAR && !player.isWearingFormalWear())
			{
				filename = "data/html/mods/wedding/noformal.htm";
				sendHtmlMessage(player, filename, null);
				return;
			}

			if (!player.reduceAdena("Wedding Request", Config.WEDDING_PRICE, player.getLastFolkNPC(), true))
			{
				filename = "data/html/mods/wedding/adena.htm";
				sendHtmlMessage(player, filename, String.valueOf(Config.WEDDING_PRICE));
				return;
			}
			
			player.setMarryAccepted(true);
			pTarget.setMarryRequest(true);
			filename = "data/html/mods/wedding/requested.htm";
			sendHtmlMessage(player, filename, pTarget.getName());
			return;
		}
		sendHtmlMessage(player, filename, null);
	}
	
	private void sendHtmlMessage(L2PcInstance player, String filename, String replace)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		html.setFile(filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		if (replace != null)
		{
			html.replace("%replace%", replace);
		}
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
}
