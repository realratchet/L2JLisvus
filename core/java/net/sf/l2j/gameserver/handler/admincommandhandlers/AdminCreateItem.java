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

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.util.StringUtil;

/**
 * This class handles following admin commands:
 * - itemcreate = show menu
 * - create_item id [num] = creates num items with respective id, if num is not specified num = 1
 * - giveitem name id [num] = gives num items to specified player name with respective id, if num is not specified num = 1
 * 
 * @version $Revision: 1.2.2.2.2.3 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminCreateItem implements IAdminCommandHandler
{
    private static String[] _adminCommands =
    {
        "admin_itemcreate",
        "admin_create_item",
        "admin_giveitem"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.equals("admin_itemcreate"))
        {
            showHelpPage(activeChar, "itemcreation.htm");
        }
        else if (command.startsWith("admin_create_item"))
        {
            try
            {
                String val = command.substring(17);
                StringTokenizer st = new StringTokenizer(val);
                if (st.countTokens() == 2)
                {
                    int id = Integer.parseInt(st.nextToken());
                    long count = Long.parseLong(st.nextToken());
                    createItem(activeChar, activeChar, id, count);
                }
                else if (st.countTokens() == 1)
                {
                    int id = Integer.parseInt(st.nextToken());
                    createItem(activeChar, activeChar, id, 1);
                }
                else
                {
                    showHelpPage(activeChar, "itemcreation.htm");
                }
            }
            catch (StringIndexOutOfBoundsException e)
            {
                activeChar.sendMessage("Error while creating item.");
            }
            catch (NumberFormatException nfe)
            {
                activeChar.sendMessage("Wrong value entered.");
            }

            GMAudit.auditGMAction(activeChar.getName(), command, "no-target", "");
        }
        else if (command.startsWith("admin_giveitem"))
        {
            try
            {
                String val = command.substring(14);
                StringTokenizer st = new StringTokenizer(val);
                if (st.countTokens() > 1)
                {
                    L2PcInstance target = L2World.getInstance().getPlayer(st.nextToken());
                    if (target == null)
                    {
                        activeChar.sendMessage("Target is not online.");
                        return false;
                    }

                    int id = Integer.parseInt(st.nextToken());
                    long count = st.hasMoreTokens() ? Long.parseLong(st.nextToken()) : 1;
                    createItem(activeChar, target, id, count);

                    GMAudit.auditGMAction(activeChar.getName(), command, target.getName(), "");
                }
                else
                {
                	activeChar.sendMessage("Usage: //giveitem <playername> <itemid>");
                	activeChar.sendMessage("Usage: //giveitem <playername> <itemid> <itemcount>");
                }
            }
            catch (Exception e)
            {
            	activeChar.sendMessage("Usage: //giveitem <playername> <itemid>");
            	activeChar.sendMessage("Usage: //giveitem <playername> <itemid> <itemcount>");
            }
        }

        return true;
    }

    private void createItem(L2PcInstance activeChar, L2PcInstance target, int id, long count)
    {
    	if (count > L2ItemInstance.getMaxItemCount(id))
    	{
    		count = L2ItemInstance.getMaxItemCount(id);
    	}
    	
        if (count > 20)
        {
            L2Item template = ItemTable.getInstance().getTemplate(id);
            if (!template.isStackable())
            {
                activeChar.sendMessage("This item does not stack - Creation aborted.");
                return;
            }
        }
        
        target.getInventory().addItem("Admin", id, (int)count, target, null);
        ItemList il = new ItemList(target, true);
        target.sendPacket(il);

        activeChar.sendMessage("You have spawned " + count + " item(s) number " + id + " in " + target.getName() + "'s inventory.");

        if (activeChar != target)
            target.sendMessage("An Admin has spawned " + count + " item(s) number " + id + " in your inventory.");

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);		
        String replyMSG = StringUtil.concat(
        	"<html><body>",
        	"<table width=260><tr>",
        	"<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td width=180><center>Item Creation Menu</center></td>",
        	"<td width=40><button value=\"Back\" action=\"bypass -h admin_help itemcreation.htm\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"</tr></table>",
        	"<br><br>",
        	"<table width=270><tr><td>Item Creation Complete.<br></td></tr></table>",
        	"<table width=270><tr><td>You have spawned " + count + " item(s) number in " + target.getName() + "'s inventory.</td></tr></table>",
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