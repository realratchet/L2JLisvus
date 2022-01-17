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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.StringUtil;

public class AdminEnchant implements IAdminCommandHandler
{
	private static final Logger _log = Logger.getLogger(AdminEnchant.class.getName());
	
	private static String[] _adminCommands =
	{
		"admin_seteh", // 6
		"admin_setec", // 10
		"admin_seteg", // 9
		"admin_setel", // 11
		"admin_seteb", // 12
		"admin_setew", // 7
		"admin_setes", // 8
		"admin_setle", // 1
		"admin_setre", // 2
		"admin_setlf", // 4
		"admin_setrf", // 5
		"admin_seten", // 3
		"admin_setun", // 0
		"admin_setba", // 13
		"admin_enchant"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_enchant"))
		{
			showMainPage(activeChar);
		}
		else
		{
			int armorType = -1;

			if (command.startsWith("admin_seteh"))
			{
				armorType = Inventory.PAPERDOLL_HEAD;
			}
			else if (command.startsWith("admin_setec"))
			{
				armorType = Inventory.PAPERDOLL_CHEST;
			}
			else if (command.startsWith("admin_seteg"))
			{
				armorType = Inventory.PAPERDOLL_GLOVES;
			}
			else if (command.startsWith("admin_seteb"))
			{
				armorType = Inventory.PAPERDOLL_FEET;
			}
			else if (command.startsWith("admin_setel"))
			{
				armorType = Inventory.PAPERDOLL_LEGS;
			}
			else if (command.startsWith("admin_setew"))
			{
				armorType = Inventory.PAPERDOLL_RHAND;
			}
			else if (command.startsWith("admin_setes"))
			{
				armorType = Inventory.PAPERDOLL_LHAND;
			}
			else if (command.startsWith("admin_setle"))
			{
				armorType = Inventory.PAPERDOLL_LEAR;
			}
			else if (command.startsWith("admin_setre"))
			{
				armorType = Inventory.PAPERDOLL_REAR;
			}
			else if (command.startsWith("admin_setlf"))
			{
				armorType = Inventory.PAPERDOLL_LFINGER;
			}
			else if (command.startsWith("admin_setrf"))
			{
				armorType = Inventory.PAPERDOLL_RFINGER;
			}
			else if (command.startsWith("admin_seten"))
			{
				armorType = Inventory.PAPERDOLL_NECK;
			}
			else if (command.startsWith("admin_setun"))
			{
				armorType = Inventory.PAPERDOLL_UNDER;
			}
			else if (command.startsWith("admin_setba"))
			{
				armorType = Inventory.PAPERDOLL_BACK;
			}

			if (armorType != -1)
			{
				try
				{
					int ench = Integer.parseInt(command.substring(12));

					// check value
					if ((ench < 0) || (ench > 65535))
					{
						activeChar.sendMessage("You must set the enchant level to be between 0-65535.");
					}
					else
					{
						setEnchant(activeChar, ench, armorType);
					}
				}
				catch (StringIndexOutOfBoundsException e)
				{
					if (Config.DEVELOPER)
					{
						_log.warning("Set enchant error: " + e);
					}
					activeChar.sendMessage("Please specify a new enchant value.");
				}
				catch (NumberFormatException e)
				{
					if (Config.DEVELOPER)
					{
						_log.warning("Set enchant error: " + e);
					}
					activeChar.sendMessage("Please specify a valid new enchant value.");
				}
			}

			// show the enchant menu after an action
			showMainPage(activeChar);
		}

		return true;
	}

	private void setEnchant(L2PcInstance activeChar, int ench, int slotType)
	{
		// get the target
		L2Object target = activeChar.getTarget();
		if (target == null)
		{
			target = activeChar;
		}
		L2PcInstance player = null;
		if (target instanceof L2PcInstance)
		{
			player = (L2PcInstance) target;
		}
		else
		{
			activeChar.sendMessage("Wrong target type.");
			return;
		}

		// now we need to find the equipped weapon of the targeted character...
		int curEnchant = 0; // display purposes only
		L2ItemInstance itemInstance = null;

		// only attempt to enchant if there is a weapon equipped
		L2ItemInstance equipped = player.getInventory().getPaperdollItem(slotType);
		if ((equipped != null) && (equipped.getEquipSlot() == slotType))
		{
			itemInstance = equipped;
		}

		if (itemInstance != null)
		{
			curEnchant = itemInstance.getEnchantLevel();

			// Set enchant value
			itemInstance.setEnchantLevel(ench);
			
			StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
			su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
			player.sendPacket(su);
			
			if (!Config.FORCE_INVENTORY_UPDATE)
			{
				InventoryUpdate iu = new InventoryUpdate();
				iu.addModifiedItem(itemInstance);
				player.sendPacket(iu);
			}
			else
			{
				player.sendPacket(new ItemList(player, false));
			}
			player.broadcastUserInfo();

			// Information
			activeChar.sendMessage("Changed enchantment of " + player.getName() + "'s " + itemInstance.getItem().getName() + " from " + curEnchant + " to " + ench + ".");
			if (activeChar != player)
			{
				player.sendMessage("Admin has changed the enchantment of your " + itemInstance.getItem().getName() + " from " + curEnchant + " to " + ench + ".");
			}
			
			// These messages have enchant success sound
			if (itemInstance.getEnchantLevel() == 0)
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_SUCCESSFULLY_ENCHANTED);
				sm.addItemName(itemInstance.getItemId());
				player.sendPacket(sm);
			}
			else
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_S2_SUCCESSFULLY_ENCHANTED);
				sm.addNumber(itemInstance.getEnchantLevel());
				sm.addItemName(itemInstance.getItemId());
				player.sendPacket(sm);
			}

			// log
			GMAudit.auditGMAction(activeChar.getName(), "enchant", player.getName(), itemInstance.getItem().getName() + " from " + curEnchant + " to " + ench);
		}
	}

	public void showMainPage(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

		String replyMSG = StringUtil.concat(
			"<html><body>",
			"<center><table width=260><tr><td width=40>",
			"<button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
			"</td><td width=180>",
			"<center>Enchant Equip</center>",
			"</td><td width=40>",
			"</td></tr></table></center><br>",
			"<center><table width=270><tr><td>",
			"<button value=\"Underwear\" action=\"bypass -h admin_setun $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Helmet\" action=\"bypass -h admin_seteh $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Cloak\" action=\"bypass -h admin_setba $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Mask\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Necklace\" action=\"bypass -h admin_seten $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>",
			"</center><center><table width=270><tr><td>",
			"<button value=\"Weapon\" action=\"bypass -h admin_setew $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Chest\" action=\"bypass -h admin_setec $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Shield\" action=\"bypass -h admin_setes $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Earring\" action=\"bypass -h admin_setre $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Earring\" action=\"bypass -h admin_setle $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>",
			"</center><center><table width=270><tr><td>",
			"<button value=\"Gloves\" action=\"bypass -h admin_seteg $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Leggings\" action=\"bypass -h admin_setel $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Boots\" action=\"bypass -h admin_seteb $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Ring\" action=\"bypass -h admin_setrf $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
			"<button value=\"Ring\" action=\"bypass -h admin_setlf $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>",
			"</center><br>",
			"<center>[Enchant 0-65535]</center>",
			"<center><edit var=\"menu_command\" width=100 height=15></center><br>",
			"</body></html>");

		adminReply.setHtml(replyMSG);
		activeChar.sendPacket(adminReply);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}