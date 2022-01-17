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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.TradeController;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2DropCategory;
import net.sf.l2j.gameserver.model.L2DropData;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.StringUtil;

/**
 * @author terry
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AdminEditNpc implements IAdminCommandHandler
{
    private static final Logger _log = Logger.getLogger(AdminEditNpc.class.getName());
    
    private final static int PAGE_LIMIT = 20;

    private static String[] _adminCommands =
    {
        "admin_edit_npc",
        "admin_save_npc",
        "admin_show_droplist",
        "admin_edit_drop",
        "admin_add_drop",
        "admin_del_drop",
        "admin_showShop",
        "admin_showShopList",
        "admin_addShopItem",
        "admin_delShopItem",
        "admin_editShopItem",
        "admin_close_window"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_showShop "))
        {
            String[] args = command.split(" ");
            if (args.length > 1)
                showShop(activeChar, Integer.parseInt(command.split(" ")[1]));
        }
        else if(command.startsWith("admin_showShopList "))
        {
            String[] args = command.split(" ");
            if (args.length > 2)
                showShopList(activeChar, Integer.parseInt(command.split(" ")[1]), Integer.parseInt(command.split(" ")[2]));
        }
        else if (command.startsWith("admin_edit_npc "))
        {
            try
            {
                String[] commandSplit = command.split(" ");
                int npcId = Integer.valueOf(commandSplit[1]);
                L2NpcTemplate npc = NpcTable.getInstance().getTemplate(npcId);
                showNpcProperty(activeChar, npc);
            }
            catch(Exception e)
            {
                activeChar.sendMessage("Wrong usage: //edit_npc <npcId>");
            }
        }
        else if (command.startsWith("admin_show_droplist "))
        {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            try
            {
                int npcId = Integer.parseInt(st.nextToken());
                int page = 1;
                if (st.hasMoreTokens())
                    page = Integer.parseInt(st.nextToken());
                showNpcDropList(activeChar, npcId, page);
            }
            catch(Exception e)
            {
                activeChar.sendMessage("Usage: //show_droplist <npc_id>");
            }
        }
        else if (command.startsWith("admin_addShopItem "))
        {
            String[] args = command.split(" ");
            if (args.length > 1)
                addShopItem(activeChar, args);
        }
        else if (command.startsWith("admin_delShopItem "))
        {
            String[] args = command.split(" ");
            if (args.length > 2)
                delShopItem(activeChar, args);
        }
        else if (command.startsWith("admin_editShopItem "))
        {
            String[] args = command.split(" ");
            if (args.length > 2)
                editShopItem(activeChar, args);
        }
        else if (command.startsWith("admin_save_npc "))
        {
            try
            {
                saveNpcProperty(activeChar, command);
            }
            catch (StringIndexOutOfBoundsException e) {}
        }
        else if (command.startsWith("admin_edit_drop "))
        {
            int npcId = -1, itemId = 0, category = -1000;
            try
            {
                StringTokenizer st = new StringTokenizer(command.substring(16).trim());
                if (st.countTokens() == 3)
                {            
    	            try
    	            {
                        npcId = Integer.parseInt(st.nextToken());
                        itemId = Integer.parseInt(st.nextToken());
                        category = Integer.parseInt(st.nextToken());
                        showEditDropData(activeChar, npcId, itemId, category);
    	            }
    	            catch(Exception e) {}
                }
                else if (st.countTokens() == 6)
                {
                    try
                    {
                        npcId = Integer.parseInt(st.nextToken());
                        itemId = Integer.parseInt(st.nextToken());
                        category = Integer.parseInt(st.nextToken());
                        int min = Integer.parseInt(st.nextToken());
                        int max = Integer.parseInt(st.nextToken());
                        int chance = Integer.parseInt(st.nextToken());

                        updateDropData(activeChar, npcId, itemId, min, max, category, chance);
                    }
                    catch(Exception e)
                    {
                        _log.fine("admin_edit_drop parements error: " + command);
                    }
                }
                else
                    activeChar.sendMessage("//Usage: edit_drop <npc_id> <item_id> <category> [<min> <max> <chance>]");
            }
            catch (StringIndexOutOfBoundsException e)
            {
                activeChar.sendMessage("//Usage: edit_drop <npc_id> <item_id> <category> [<min> <max> <chance>]");
            }
        }
        else if (command.startsWith("admin_add_drop "))
        {
            int npcId = -1;
            try
            {
                StringTokenizer st = new StringTokenizer(command.substring(15).trim());
                if (st.countTokens() == 1)
                {            
    	            try
    	            {
                        String[] input = command.substring(15).split(" ");
                        if (input.length < 1)
                            return true;
                        npcId = Integer.parseInt(input[0]);
    	            }
    	            catch(Exception e){}
    	            
    	            if(npcId > 0)
    	            {
    	                L2NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
    	                showAddDropData(activeChar, npcData);
    	            }
                }
                else if(st.countTokens() == 6)
                {
                    try
                    {
                        npcId = Integer.parseInt(st.nextToken());
                        int itemId = Integer.parseInt(st.nextToken());
                        int category = Integer.parseInt(st.nextToken());
                        int min = Integer.parseInt(st.nextToken());
                        int max = Integer.parseInt(st.nextToken());
                        int chance = Integer.parseInt(st.nextToken());
                        
                        addDropData(activeChar, npcId, itemId, min, max, category, chance);
                    }
                    catch(Exception e)
                    {
                        _log.fine("admin_add_drop parements error: " + command);
                    }
                }
                else
                    activeChar.sendMessage("//Usage: add_drop <npc_id> [<item_id> <category> <min> <max> <chance>]");
            }
            catch (StringIndexOutOfBoundsException e)
            {
                activeChar.sendMessage("//Usage: add_drop <npc_id> [<item_id> <category> <min> <max> <chance>]");
            }
        }
        else if (command.startsWith("admin_del_drop "))
        {
            int npcId = -1, itemId = -1, category = -1000;
            try
            {
                String[] input = command.substring(15).split(" ");
                if (input.length >= 3)
                {
                    npcId = Integer.parseInt(input[0]);
                    itemId = Integer.parseInt(input[1]);
                    category = Integer.parseInt(input[2]);
                }
            }
            catch(Exception e){}
            
            if (npcId > 0)
                deleteDropData(activeChar, npcId, itemId, category);
            else
                activeChar.sendMessage("//Usage: del_drop <npc_id> <item_id> <category>");
        }

        return true;
    }

    private void editShopItem(L2PcInstance admin, String[] args)
    {
        int tradeListID = Integer.parseInt(args[1]);
        int itemID = Integer.parseInt(args[2]);
        L2TradeList tradeList = TradeController.getInstance().getBuyList(tradeListID);
        
        L2Item item = ItemTable.getInstance().getTemplate(itemID);
        if (tradeList.getPriceForItemId(itemID) < 0)
        {
            return;
        }
        
        if (args.length > 3)
        {
            int price = Integer.parseInt(args[3]);
            int order = findOrderTradeList(itemID, tradeList.getPriceForItemId(itemID), tradeListID);
            
            tradeList.replaceItem(itemID, Integer.parseInt(args[3]));
            updateTradeList(itemID, price, tradeListID, order);
            
            admin.sendMessage("Updated price for "+item.getName()+" in Trade List "+tradeListID);
            showShopList(admin, tradeListID, 1);
            return;
        }
        
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        
        String replyMSG = StringUtil.concat(
        	"<html><title>Merchant Shop Item Edit</title>",
        	"<body>",
        	"<br>Edit an entry in merchantList.",
        	"<br>Editing Item: "+item.getName(),
        	"<table>",
        	"<tr><td width=100>Property</td><td width=100>Edit Field</td><td width=100>Old Value</td></tr>",
        	"<tr><td><br></td><td></td></tr>",
        	"<tr><td>Price</td><td><edit var=\"price\" width=80></td><td>"+tradeList.getPriceForItemId(itemID)+"</td></tr>",
        	"</table>",
        	"<center><br><br><br>",
        	"<button value=\"Save\" action=\"bypass -h admin_editShopItem " + tradeListID + " " + itemID + " $price\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"<br><button value=\"Back\" action=\"bypass -h admin_showShopList " + tradeListID +" 1\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</center>",
        	"</body></html>");

        adminReply.setHtml(replyMSG);        
        admin.sendPacket(adminReply);
    }

    private void delShopItem(L2PcInstance admin, String[] args)
    {
        int tradeListID = Integer.parseInt(args[1]);
        int itemID = Integer.parseInt(args[2]);
        L2TradeList tradeList = TradeController.getInstance().getBuyList(tradeListID);
        
        if (tradeList.getPriceForItemId(itemID) < 0)
        {
            return;
        }

        if (args.length > 3)
        {
            int order = findOrderTradeList(itemID, tradeList.getPriceForItemId(itemID), tradeListID);
        	
            tradeList.removeItem(itemID);
            deleteTradeList(tradeListID, order);
            
            admin.sendMessage("Deleted "+ItemTable.getInstance().getTemplate(itemID).getName()+" from Trade List "+tradeListID);
            showShopList(admin, tradeListID, 1);
            return;
        }
        
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        
        String replyMSG = StringUtil.concat(
        	"<html><title>Merchant Shop Item Delete</title>",
        	"<body>",
        	"<br>Delete entry in merchantList.",
        	"<br>Item to Delete: "+ItemTable.getInstance().getTemplate(itemID).getName(),
        	"<table>",
        	"<tr><td width=100>Property</td><td width=100>Value</td></tr>",
        	"<tr><td><br></td><td></td></tr>",
        	"<tr><td>Price</td><td>"+tradeList.getPriceForItemId(itemID)+"</td></tr>",
        	"</table>",
        	"<center><br><br><br>",
        	"<button value=\"Confirm\" action=\"bypass -h admin_delShopItem " + tradeListID + " " + itemID + " 1\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"<br><button value=\"Back\" action=\"bypass -h admin_showShopList " + tradeListID +" 1\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</center>",
        	"</body></html>");

        adminReply.setHtml(replyMSG);
        admin.sendPacket(adminReply);
    }

    private void addShopItem(L2PcInstance admin, String[] args)
    {
        int tradeListID = Integer.parseInt(args[1]);
        
        L2TradeList tradeList = TradeController.getInstance().getBuyList(tradeListID);
        if (tradeList == null)
        {
            admin.sendMessage("TradeList not found!");
            return;
        }
        
        if (args.length > 3)
        {
        	int order = tradeList.getItems().size() + 1; // last item order + 1
            int itemID = Integer.parseInt(args[2]);
            int price = Integer.parseInt(args[3]);
            
            L2ItemInstance newItem = ItemTable.getInstance().createDummyItem(itemID);
            newItem.setPriceToSell(price);
            newItem.setCount(-1);
            tradeList.addItem(newItem);
            storeTradeList(itemID, price, tradeListID, order);
            
            admin.sendMessage("Added "+newItem.getItem().getName()+" to Trade List "+tradeList.getListId());
            showShopList(admin, tradeListID, 1);
            return;
        }
        
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        
        String replyMSG = StringUtil.concat(
        	"<html><title>Merchant Shop Item Add</title>",
        	"<body>",
        	"<br>Add a new entry in merchantList.",
        	"<table>",
        	"<tr><td width=100>Property</td><td>Edit Field</td></tr>",
        	"<tr><td><br></td><td></td></tr>",
        	"<tr><td>ItemID</td><td><edit var=\"itemID\" width=80></td></tr>",
        	"<tr><td>Price</td><td><edit var=\"price\" width=80></td></tr>",
        	"</table>",
        	"<center><br><br><br>",
        	"<button value=\"Save\" action=\"bypass -h admin_addShopItem " + tradeListID + " $itemID $price\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"<br><button value=\"Back\" action=\"bypass -h admin_showShopList " + tradeListID +" 1\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</center>",
        	"</body></html>");
        
        adminReply.setHtml(replyMSG);
        admin.sendPacket(adminReply);
    }
    
    private void showShopList(L2PcInstance admin, int tradeListID, int page)
    {
        L2TradeList tradeList = TradeController.getInstance().getBuyList(tradeListID);
        if (page > tradeList.getItems().size() / PAGE_LIMIT + 1 || page < 1)
            return;
        
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        StringBuilder html = itemListHtml(tradeList, page);
        
        adminReply.setHtml(html.toString());        
        admin.sendPacket(adminReply);
    }
    
    private StringBuilder itemListHtml(L2TradeList tradeList, int page)
    {
        StringBuilder replyMSG = new StringBuilder(2000);
        StringUtil.append(replyMSG, "<html><title>Merchant Shop List Page: "+page+"</title>");
        StringUtil.append(replyMSG, "<body>");
        StringUtil.append(replyMSG, "<br>Edit, add or delete entries in a merchantList.");
        StringUtil.append(replyMSG, "<table>");
        StringUtil.append(replyMSG, "<tr><td width=150>Item Name</td><td width=60>Price</td><td width=40>Delete</td></tr>");
        int start = ((page-1) * PAGE_LIMIT);
        int end = Math.min(((page-1) * PAGE_LIMIT) + (PAGE_LIMIT-1), tradeList.getItems().size() - 1);
        for (L2ItemInstance item : tradeList.getItems(start, end+1))
        {
        	StringUtil.append(replyMSG, "<tr><td><a action=\"bypass -h admin_editShopItem "+tradeList.getListId()+" "+item.getItemId()+"\">"+item.getItem().getName()+"</a></td>");
        	StringUtil.append(replyMSG, "<td>"+item.getPriceToSell()+"</td>");
        	StringUtil.append(replyMSG, "<td><button value=\"Del\" action=\"bypass -h admin_delShopItem "+tradeList.getListId()+" "+item.getItemId()+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        	StringUtil.append(replyMSG, "</tr>");
        }
        StringUtil.append(replyMSG, "<tr>");
        int min = 1;
        int max = tradeList.getItems().size() / PAGE_LIMIT + 1;
        if (page > 1)
        {
        	StringUtil.append(replyMSG, "<td><button value=\"Page"+(page - 1)+"\" action=\"bypass -h admin_showShopList "+tradeList.getListId()+" "+(page - 1)+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        }
        if (page < max)
        {
            if (page <= min)
            	StringUtil.append(replyMSG, "<td></td>");
            StringUtil.append(replyMSG, "<td><button value=\"Page"+(page + 1)+"\" action=\"bypass -h admin_showShopList "+tradeList.getListId()+" "+(page + 1)+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        }
        StringUtil.append(replyMSG, "</tr><tr><td>.</td></tr>");
        StringUtil.append(replyMSG, "</table>");
        StringUtil.append(replyMSG, "<center>");
        StringUtil.append(replyMSG, "<button value=\"Add\" action=\"bypass -h admin_addShopItem "+tradeList.getListId()+"\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        StringUtil.append(replyMSG, "<button value=\"Close\" action=\"bypass -h admin_close_window\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        StringUtil.append(replyMSG, "</center></body></html>");
        
        return replyMSG;
    }

    private void showShop(L2PcInstance admin, int merchantID)
    {
        List<L2TradeList> tradeLists = getTradeLists(merchantID);
        if (tradeLists == null)
        {
            admin.sendMessage("Unknown npc template ID" + merchantID);
            return;
        }
        
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        
        StringBuilder replyMSG = new StringBuilder(1000);
        StringUtil.append(replyMSG, "<html><title>Merchant Shop Lists</title>");
        StringUtil.append(replyMSG, "<body>");
        StringUtil.append(replyMSG, "<br>Select a list to view");
        StringUtil.append(replyMSG, "<table>");
        StringUtil.append(replyMSG, "<tr><td>Mecrchant List ID</td></tr>");
        
        for (L2TradeList tradeList : tradeLists)
        {
            if (tradeList != null)
            	StringUtil.append(replyMSG, "<tr><td><a action=\"bypass -h admin_showShopList "+tradeList.getListId()+" 1\">Trade List "+tradeList.getListId()+"</a></td></tr>");
        }
        
        StringUtil.append(replyMSG, "</table>");
        StringUtil.append(replyMSG, "<center>");
        StringUtil.append(replyMSG, "<button value=\"Close\" action=\"bypass -h admin_close_window\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        StringUtil.append(replyMSG, "</center></body></html>");
        
        adminReply.setHtml(replyMSG.toString());        
        admin.sendPacket(adminReply);
    }
    
    private void storeTradeList(int itemID, int price, int tradeListID, int order)
    {
        String table = "merchant_buylists";
        if (Config.CUSTOM_MERCHANT_TABLES)
            table = "custom_merchant_buylists";

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO `" + table + "` (`item_id`,`price`,`shop_id`,`item_order`) values ("+itemID+","+price+","+tradeListID+","+order+")"))
        {
	    stmt.execute();
        }
        catch (Exception e)
        {
            _log.warning("Could not store trade list (" + itemID + ", " + price + ", " + tradeListID + ", " + order + "): " + e);
        }
    }
    
    private void updateTradeList(int itemID, int price, int tradeListID, int order)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            int updated = 0;
            if (Config.CUSTOM_MERCHANT_TABLES)
            {
                try (PreparedStatement stmt = con.prepareStatement("UPDATE custom_merchant_buylists SET `price`='"+price+"' WHERE `shop_id`='"+tradeListID+"' AND `item_order`='"+order+"'"))
                {
                	updated = stmt.executeUpdate();
                }
            }

            if (updated == 0)
            {
                try (PreparedStatement stmt = con.prepareStatement("UPDATE merchant_buylists SET `price`='"+price+"' WHERE `shop_id`='"+tradeListID+"' AND `item_order`='"+order+"'"))
                {
                	stmt.executeUpdate();
                }
            }
        }
        catch (Exception e)
        {
            _log.warning("Could not update trade list (" + itemID + ", " + price + ", " + tradeListID + ", " + order + "): " + e);
        }
    }
    
    private void deleteTradeList(int tradeListID, int order)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            int updated = 0;
            if (Config.CUSTOM_MERCHANT_TABLES)
            {
            	try (PreparedStatement stmt = con.prepareStatement("DELETE FROM custom_merchant_buylists WHERE `shop_id`='"+tradeListID+"' AND `item_order`='"+order+"'"))
                {
                    updated = stmt.executeUpdate();
                }
            }

            if (updated == 0)
            {
            	try (PreparedStatement stmt = con.prepareStatement("DELETE FROM merchant_buylists WHERE `shop_id`='"+tradeListID+"' AND `item_order`='"+order+"'"))
                {
                    stmt.executeUpdate();
                }
            }
        }
        catch (Exception e)
        {
            _log.warning("Could not delete trade list (" + tradeListID + ", " + order + "): " + e);
        }
    }
    
    private int findOrderTradeList(int itemID, int price, int tradeListID)
    {
       	int order = -1;

        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM merchant_buylists WHERE `shop_id`='"+tradeListID+"' AND `item_id` ='"+itemID+"' AND `price` = '"+price+"'");
    	        ResultSet rs = stmt.executeQuery())
            {
    	        if (rs.first())
    	            order = rs.getInt("order");
            }

            if (order < 0 && Config.CUSTOM_MERCHANT_TABLES)
            {
                try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM custom_merchant_buylists WHERE `shop_id`='"+tradeListID+"' AND `item_id` ='"+itemID+"' AND `price` = '"+price+"'");
    	            ResultSet rs = stmt.executeQuery())
                {
    	            if (rs.first())
    	                order = rs.getInt("order");
                }
            }
        }
        catch (Exception e)
        {
            _log.warning("Could not get order for (" + itemID + ", " + price + ", " + tradeListID + "): " + e);
        }
        return order;
    }

    private List<L2TradeList> getTradeLists(int merchantID)
    {
        String target = "npc_%objectId%_Buy";
        
        String content = HtmCache.getInstance().getHtm("data/html/merchant/"+merchantID+".htm");

        if (content == null)
        {
            content = HtmCache.getInstance().getHtm("data/html/merchant/7001.htm");
            
            if (content == null)
                return null;
        }

        List<L2TradeList> tradeLists = new ArrayList<>();
        
        String[] lines = content.split("\n");
        int pos = 0;
        
        for (String line : lines)
        {
            pos = line.indexOf(target); 
            if (pos >= 0)
            {
                int tradeListID = Integer.decode((line.substring(pos+target.length()+1)).split("\"")[0]);
                tradeLists.add(TradeController.getInstance().getBuyList(tradeListID));
            }
        }
        
        return tradeLists;
    }

    private void showNpcProperty(L2PcInstance adminPlayer, L2NpcTemplate npc)
    {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        String content = HtmCache.getInstance().getHtm("data/html/admin/editnpc.htm");
	    
        if (content != null)
        {
            adminReply.setHtml(content);
            adminReply.replace("%npcId%", String.valueOf(npc.npcId));
            adminReply.replace("%templateId%", String.valueOf(npc.idTemplate));
		    adminReply.replace("%name%", npc.name);
		    adminReply.replace("%serverSideName%", npc.serverSideName == true ? "1" : "0");
		    adminReply.replace("%title%", npc.title);
		    adminReply.replace("%serverSideTitle%", npc.serverSideTitle == true ? "1" : "0");
		    adminReply.replace("%collisionRadius%", String.valueOf(npc.collisionRadius));
		    adminReply.replace("%collisionHeight%", String.valueOf(npc.collisionHeight));
		    adminReply.replace("%level%", String.valueOf(npc.level));
		    adminReply.replace("%sex%", String.valueOf(npc.sex));	        
		    adminReply.replace("%type%", String.valueOf(npc.type));
		    adminReply.replace("%attackRange%", String.valueOf(npc.baseAtkRange));
		    adminReply.replace("%hp%", String.valueOf(npc.baseHpMax));
		    adminReply.replace("%mp%", String.valueOf(npc.baseMpMax));
		    adminReply.replace("%hpRegen%", String.valueOf(npc.baseHpReg));
		    adminReply.replace("%mpRegen%", String.valueOf(npc.baseMpReg));
		    adminReply.replace("%str%", String.valueOf(npc.baseSTR));
		    adminReply.replace("%con%", String.valueOf(npc.baseCON));
		    adminReply.replace("%dex%", String.valueOf(npc.baseDEX));
		    adminReply.replace("%int%", String.valueOf(npc.baseINT));
		    adminReply.replace("%wit%", String.valueOf(npc.baseWIT));
		    adminReply.replace("%men%", String.valueOf(npc.baseMEN));
		    adminReply.replace("%exp%", String.valueOf(npc.rewardExp));
		    adminReply.replace("%sp%", String.valueOf(npc.rewardSp));
		    adminReply.replace("%pAtk%", String.valueOf(npc.basePAtk));
		    adminReply.replace("%pDef%", String.valueOf(npc.basePDef));
		    adminReply.replace("%mAtk%", String.valueOf(npc.baseMAtk));
		    adminReply.replace("%mDef%", String.valueOf(npc.baseMDef));
		    adminReply.replace("%pAtkSpd%", String.valueOf(npc.basePAtkSpd));
		    adminReply.replace("%aggro%", String.valueOf(npc.aggroRange));
		    adminReply.replace("%mAtkSpd%", String.valueOf(npc.baseMAtkSpd));
		    adminReply.replace("%rHand%", String.valueOf(npc.rhand));
		    adminReply.replace("%lHand%", String.valueOf(npc.lhand));
		    adminReply.replace("%armor%", String.valueOf(npc.armor));
		    adminReply.replace("%walkSpd%", String.valueOf(npc.baseWalkSpd));
		    adminReply.replace("%runSpd%", String.valueOf(npc.baseRunSpd));
		    adminReply.replace("%factionId%", npc.factionId == null ? "" : npc.factionId);
		    adminReply.replace("%factionRange%", String.valueOf(npc.factionRange));
		    adminReply.replace("%isUndead%", npc.isUndead ? "1" : "0");
		    adminReply.replace("%absorbLevel%", String.valueOf(npc.absorbLevel));
            adminReply.replace("%ss%", String.valueOf(npc.ss));
            adminReply.replace("%bss%", String.valueOf(npc.bss));
            adminReply.replace("%ssRate%", String.valueOf(npc.ssRate));
            adminReply.replace("%AI%", npc.AI.name());
        }
        else
            adminReply.setHtml("<html><body>File not found: data/html/admin/editnpc.htm</body></html>");

        adminPlayer.sendPacket(adminReply);
    }

    private void saveNpcProperty(L2PcInstance adminPlayer, String command)
    {
        String[] commandSplit = command.split(" ");

        if (commandSplit.length < 4)
            return;

        StatsSet newNpcData = new StatsSet();

        try
        {
            newNpcData.set("npcId", commandSplit[1]);

            String statToSet = commandSplit[2];
            String value = commandSplit[3];

            if (commandSplit.length > 4)
            {
                for (int i=0;i<commandSplit.length-3;i++)
                    value += " " + commandSplit[i+4];
            }

            if (statToSet.equals("templateId"))
                newNpcData.set("idTemplate", Integer.valueOf(value));
            else if (statToSet.equals("name"))
                newNpcData.set("name", value);
            else if (statToSet.equals("serverSideName"))
                newNpcData.set("serverSideName", Integer.valueOf(value));
            else if (statToSet.equals("title"))
                newNpcData.set("title", value);
            else if (statToSet.equals("serverSideTitle"))
                newNpcData.set("serverSideTitle", Integer.valueOf(value) == 1 ? 1 : 0);
            else if (statToSet.equals("collisionRadius"))
                newNpcData.set("collision_radius", Integer.valueOf(value));
            else if (statToSet.equals("collisionHeight"))
                newNpcData.set("collision_height", Integer.valueOf(value));
            else if (statToSet.equals("level"))
                newNpcData.set("level", Integer.valueOf(value));
            else if (statToSet.equals("sex"))
            {
                int intValue = Integer.valueOf(value);
                newNpcData.set("sex", intValue == 0 ? "male" : intValue == 1 ? "female" : "etc");
            }
            else if (statToSet.equals("type"))
            {
                Class.forName("net.sf.l2j.gameserver.model.actor.instance." + value + "Instance");
                newNpcData.set("type", value);
            }
			else if (statToSet.equals("attackRange"))
				newNpcData.set("attackrange", Integer.valueOf(value));
			else if (statToSet.equals("hp"))
				newNpcData.set("hp", Integer.valueOf(value));
			else if (statToSet.equals("mp"))
				newNpcData.set("mp", Integer.valueOf(value));
			else if (statToSet.equals("hpRegen"))
				newNpcData.set("hpreg", Integer.valueOf(value));
			else if (statToSet.equals("mpRegen"))
				newNpcData.set("mpreg", Integer.valueOf(value));
			else if (statToSet.equals("str"))
				newNpcData.set("str", Integer.valueOf(value));
			else if (statToSet.equals("con"))
				newNpcData.set("con", Integer.valueOf(value));
			else if (statToSet.equals("dex"))
				newNpcData.set("dex", Integer.valueOf(value));
			else if (statToSet.equals("int"))
				newNpcData.set("int", Integer.valueOf(value));
			else if (statToSet.equals("wit"))
				newNpcData.set("wit", Integer.valueOf(value));
			else if (statToSet.equals("men"))
				newNpcData.set("men", Integer.valueOf(value));
			else if (statToSet.equals("exp"))
				newNpcData.set("exp", Integer.valueOf(value));
			else if (statToSet.equals("sp"))
				newNpcData.set("sp", Integer.valueOf(value));
			else if (statToSet.equals("pAtk"))
				newNpcData.set("patk", Integer.valueOf(value));
			else if (statToSet.equals("pDef"))
				newNpcData.set("pdef", Integer.valueOf(value));
			else if (statToSet.equals("mAtk"))
				newNpcData.set("matk", Integer.valueOf(value));
			else if (statToSet.equals("mDef"))
				newNpcData.set("mdef", Integer.valueOf(value));
			else if (statToSet.equals("pAtkSpd"))
				newNpcData.set("atkspd", Integer.valueOf(value));
			else if (statToSet.equals("aggro"))
				newNpcData.set("aggro", Integer.valueOf(value));
			else if (statToSet.equals("mAtkSpd"))
				newNpcData.set("matkspd", Integer.valueOf(value));
			else if (statToSet.equals("rHand"))
				newNpcData.set("rhand", Integer.valueOf(value));
			else if (statToSet.equals("lHand"))
				newNpcData.set("lhand", Integer.valueOf(value));
			else if (statToSet.equals("armor"))
				newNpcData.set("armor", Integer.valueOf(value));
			else if (statToSet.equals("walkSpd"))
				newNpcData.set("walkSpd", Integer.valueOf(value));
			else if (statToSet.equals("runSpd"))
				newNpcData.set("runspd", Integer.valueOf(value));
			else if (statToSet.equals("factionId"))
				newNpcData.set("faction_id", value);
			else if (statToSet.equals("factionRange"))
				newNpcData.set("faction_range", Integer.valueOf(value));
			else if (statToSet.equals("isUndead"))
				newNpcData.set("isUndead", Integer.valueOf(value) == 1 ? 1 : 0);
			else if (statToSet.equals("absorbLevel"))
				newNpcData.set("absorb_level", Integer.valueOf(value));
			else if (statToSet.equals("ss"))
				newNpcData.set("ss", Integer.valueOf(value));
			else if (statToSet.equals("bss"))
				newNpcData.set("bss", Integer.valueOf(value));
			else if (statToSet.equals("ssRate"))
				newNpcData.set("ss_rate", Integer.valueOf(value));
			else if (statToSet.equals("AI"))
				newNpcData.set("AI", value);
        }
        catch (Exception e)
        {
            _log.warning("Error saving new npc value: " + e);
        }

        NpcTable.getInstance().saveNpc(newNpcData);

        int npcId = newNpcData.getInteger("npcId");

        NpcTable.getInstance().reloadNpc(npcId);
        showNpcProperty(adminPlayer, NpcTable.getInstance().getTemplate(npcId));
    }

	private void showNpcDropList(L2PcInstance admin, int npcId, int page)
	{
	    L2NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
	    if(npcData == null)
	    {
	        admin.sendMessage("unknown npc template id" + npcId);
	        return;
	    }
	    
	    NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
	    
	    StringBuilder replyMSG = new StringBuilder(2000);
	    StringUtil.append(replyMSG, "<html><title>NPC: "+ npcData.name + "("+npcData.npcId+") 's drop manage</title>",
	    	"<body>",
	    	"<br>Notes: Click [drop_id] to show the detail of drop data, click [del] to delete the drop data!",
	    	"<table>",
	    	"<tr><td>Npc/Item/Category</td><td>Item[id]</td><td>type</td><td>del</td></tr>");

        int myPage = 1;
        int i = 0;
        int shown = 0;
        boolean hasMore = false;
        if (npcData.getDropData() != null)
        {
        	for (L2DropCategory cat : npcData.getDropData())
            {
                if (shown == PAGE_LIMIT)
                {
                    hasMore = true;
                    break;
                }

			    for (L2DropData drop : cat.getAllDrops())
			    {
                    if (myPage != page)
                    {
                        i++;
                        if (i == PAGE_LIMIT)
                        {
                            myPage++;
                            i = 0;
                        }
                        continue;
                    }

                    if (shown == PAGE_LIMIT)
                    {
                        hasMore = true;
                        break;
                    }

                    StringUtil.append(replyMSG, "<tr><td><a action=\"bypass -h admin_edit_drop " + npcData.npcId + " " + drop.getItemId() + " " + cat.getCategoryType() + "\">"
                    	+ npcData.npcId + " " + drop.getItemId() + " " + cat.getCategoryType() + "</a></td>" +
                    	"<td>" + ItemTable.getInstance().getTemplate(drop.getItemId()).getName() + "[" + drop.getItemId() + "]" + "</td><td>" + (drop.isQuestDrop()?"Q":(cat.isSweep()?"S":"D")) + "</td><td>" +
                    	"<a action=\"bypass -h admin_del_drop " + npcData.npcId + " " + drop.getItemId() +" "+ cat.getCategoryType() +"\">del</a></td></tr>");
                    shown++;
			    }
            }
        }

        StringUtil.append(replyMSG, "</table><table width=300 bgcolor=666666 border=0><tr>");

        if (page > 1)
        {
        	StringUtil.append(replyMSG, "<td width=120><a action=\"bypass -h admin_show_droplist ");
        	StringUtil.append(replyMSG, String.valueOf(npcId));
        	StringUtil.append(replyMSG, " ");
        	StringUtil.append(replyMSG, String.valueOf(page - 1));
        	StringUtil.append(replyMSG, "\">Prev Page</a></td>");
            if (!hasMore)
            {
            	StringUtil.append(replyMSG, "<td width=100>Page ");
            	StringUtil.append(replyMSG, String.valueOf(page));
            	StringUtil.append(replyMSG, "</td><td width=70></td></tr>");
            }
        }

        if (hasMore)
        {
            if (page <= 1)
            	StringUtil.append(replyMSG, "<td width=120></td>");
            StringUtil.append(replyMSG, "<td width=100>Page ");
            StringUtil.append(replyMSG, String.valueOf(page));
            StringUtil.append(replyMSG, "</td><td width=70><a action=\"bypass -h admin_show_droplist ");
            StringUtil.append(replyMSG, String.valueOf(npcId));
            StringUtil.append(replyMSG, " ");
            StringUtil.append(replyMSG, String.valueOf(page + 1));
            StringUtil.append(replyMSG, "\">Next Page</a></td></tr>");
        }

        StringUtil.append(replyMSG, "</table>");
        StringUtil.append(replyMSG, "<center>");
        StringUtil.append(replyMSG, "<button value=\"Add Drop Data\" action=\"bypass -h admin_add_drop "+ npcId + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        StringUtil.append(replyMSG, "<button value=\"Close\" action=\"bypass -h admin_close_window\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        StringUtil.append(replyMSG, "</center></body></html>");
	    
	    adminReply.setHtml(replyMSG.toString());	    
	    admin.sendPacket(adminReply);
	    
	}
	
	private void showEditDropData(L2PcInstance admin, int npcId, int itemId, int category)
	{
        L2NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
        if (npcData == null)
        {
            admin.sendMessage("Unknown npc template id " + npcId);
            return;
        }

        L2Item itemData = ItemTable.getInstance().getTemplate(itemId);
        if (itemData == null)
        {
            admin.sendMessage("Unknown item template id " + itemId);
            return;
        }

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        StringBuilder replyMSG = new StringBuilder(500);
        StringUtil.append(replyMSG, "<html><title>Details of Drop Data: (" + npcId + " " + itemId + " " + category + ")</title>", "<body>");

        List<L2DropData> dropDatas = null;
        if (npcData.getDropData() != null)
        {
            for (L2DropCategory dropCat : npcData.getDropData())
            {
                if (dropCat.getCategoryType() == category)
                {
                    dropDatas = dropCat.getAllDrops();
                    break;
                }
            }
        }

        L2DropData dropData = null;
        if (dropDatas != null)
        {
            for (L2DropData drop : dropDatas)
            {
                if (drop.getItemId() == itemId)
                {
                    dropData = drop;
                    break;
                }
            }
        }

        if (dropData != null)
        {
        	StringUtil.append(replyMSG, "<table>");
        	StringUtil.append(replyMSG, "<tr><td>NPC</td><td>"+ npcData.name + "</td></tr>");
        	StringUtil.append(replyMSG, "<tr><td>ItemName</td><td>"+ itemData.getName() + "(" + itemId + ")</td></tr>");
        	StringUtil.append(replyMSG, "<tr><td>Category</td><td>"+ ((category==-1)?"sweep":Integer.toString(category)) + "</td></tr>");
        	StringUtil.append(replyMSG, "<tr><td>Min(" + dropData.getMinDrop() + ")</td><td><edit var=\"min_drop\" width=80></td></tr>");
        	StringUtil.append(replyMSG, "<tr><td>Max(" + dropData.getMaxDrop() + ")</td><td><edit var=\"max_drop\" width=80></td></tr>");
        	StringUtil.append(replyMSG, "<tr><td>Chance(" + dropData.getChance() + ")</td><td><edit var=\"chance\" width=80></td></tr>");
        	StringUtil.append(replyMSG, "</table>");
            
        	StringUtil.append(replyMSG, "<center>");
        	StringUtil.append(replyMSG, "<button value=\"Save Changes\" action=\"bypass -h admin_edit_drop " + npcId + " " + itemId + " " + category +" $min_drop $max_drop $chance\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        	StringUtil.append(replyMSG, "<br><button value=\"DropList\" action=\"bypass -h admin_show_droplist " + npcId +"\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        	StringUtil.append(replyMSG, "</center>");
        }

        StringUtil.append(replyMSG, "</body></html>");
        adminReply.setHtml(replyMSG.toString());

        admin.sendPacket(adminReply);
	}

	private void showAddDropData(L2PcInstance admin, L2NpcTemplate npcData)
	{	    
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        
        String replyMSG = StringUtil.concat(
        	"<html><title>Add dropdata to " + npcData.name + "(" + npcData.npcId + ")</title>",
        	"<body>",
        	"<table>",
        	"<tr><td>Item-Id</td><td><edit var=\"itemId\" width=80></td></tr>",
        	"<tr><td>Min</td><td><edit var=\"min_drop\" width=80></td></tr>",
        	"<tr><td>Max</td><td><edit var=\"max_drop\" width=80></td></tr>",
        	"<tr><td>Category(sweep=-1)</td><td><edit var=\"category\" width=80></td></tr>",
        	"<tr><td>Chance(0-1000000)</td><td><edit var=\"chance\" width=80></td></tr>",
        	"</table>",
        	"<center>",
        	"<button value=\"Save\" action=\"bypass -h admin_add_drop " + npcData.npcId + " $itemId $category $min_drop $max_drop $chance\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"<br><button value=\"DropList\" action=\"bypass -h admin_show_droplist " + npcData.npcId +"\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</center>",
        	"</body></html>");
	    adminReply.setHtml(replyMSG);
	    
	    admin.sendPacket(adminReply);
	}
	
	private void updateDropData(L2PcInstance admin, int npcId, int itemId, int min, int max, int category, int chance)
	{
	    try (Connection con = L2DatabaseFactory.getInstance().getConnection())
	    {
            int updated = 0;
            if (Config.CUSTOM_DROPLIST_TABLE)
            {
                try (PreparedStatement statement = con.prepareStatement("UPDATE custom_droplist SET min_drop=?, max_drop=?, chance=? WHERE mobId=? AND itemId=? AND category=?"))
                {
	                statement.setInt(1, min);
	                statement.setInt(2, max);
	                statement.setInt(3, chance);
                    statement.setInt(4, npcId);
                    statement.setInt(5, itemId);
                    statement.setInt(6, category);

                    updated = statement.executeUpdate();
                }
            }

            if (updated == 0)
            {
	            try (PreparedStatement statement = con.prepareStatement("UPDATE droplist SET min_drop=?, max_drop=?, chance=? WHERE mobId=? AND itemId=? AND category=?"))
                {
	                statement.setInt(1, min);
	                statement.setInt(2, max);
	                statement.setInt(3, chance);
                    statement.setInt(4, npcId);
                    statement.setInt(5, itemId);
                    statement.setInt(6, category);

	                statement.executeUpdate();
	            }
            }

	        reLoadNpcDropList(npcId);

            showNpcDropList(admin, npcId, 1);
            admin.sendMessage("Updated drop data for npc id " + npcId + " and item id " + itemId + " in category " + category + ".");
	    }
	    catch (Exception e)
        {
            admin.sendMessage("Could not update drop data!");
            _log.warning("Error while updating drop data (" + npcId + ", " + itemId + ", " + min + ", " + max + ", " + category + ", " + chance + "): " + e);
        }
	}

	private void addDropData(L2PcInstance admin, int npcId, int itemId, int min, int max, int category, int chance)
	{
	    String table = "droplist";
        if (Config.CUSTOM_DROPLIST_TABLE)
            table = "custom_droplist";

	    try (Connection con = L2DatabaseFactory.getInstance().getConnection();
	        PreparedStatement statement = con.prepareStatement("INSERT INTO `" + table + "` (mobId, itemId, min_drop, max_drop, category, chance) values(?,?,?,?,?,?)"))
	    {
	        statement.setInt(1, npcId);
	        statement.setInt(2, itemId);
	        statement.setInt(3, min);
	        statement.setInt(4, max);
	        statement.setInt(5, category);
	        statement.setInt(6, chance);
	        statement.execute();
	        
	        reLoadNpcDropList(npcId);
    
	        showNpcDropList(admin, npcId, 1);
            admin.sendMessage("Added drop data for npc id " + npcId + " with item id " + itemId + " in category " + category + ".");
	    }
	    catch(Exception e)
        {
            admin.sendMessage("Could not add drop data!");
            _log.warning("Error while adding drop data (" + npcId + ", " + itemId + ", " + min + ", " + max + ", " + category + ", " + chance + "): " + e);
        }
	}
	
	private void deleteDropData(L2PcInstance admin, int npcId, int itemId, int category)
	{
        if (npcId <= 0)
            return;

	    try (Connection con = L2DatabaseFactory.getInstance().getConnection())
	    {
            int updated = 0;
            if (Config.CUSTOM_DROPLIST_TABLE)
            {
                try (PreparedStatement statement2 = con.prepareStatement("DELETE FROM custom_droplist WHERE mobId=? AND itemId=? AND category=?"))
                {
                    statement2.setInt(1, npcId);
                    statement2.setInt(2, itemId);
                    statement2.setInt(3, category);
                    updated = statement2.executeUpdate();
                }
            }

            if (updated == 0)
            {
            	try (PreparedStatement statement2 = con.prepareStatement("DELETE FROM droplist WHERE mobId=? AND itemId=? AND category=?"))
                {
                    statement2.setInt(1, npcId);
                    statement2.setInt(2, itemId);
                    statement2.setInt(3, category);
                    statement2.executeUpdate();
                }
            }

            reLoadNpcDropList(npcId);
            showNpcDropList(admin, npcId, 1);
            admin.sendMessage("Deleted drop data for npc id " + npcId + " and item id " + itemId + " in category " + category + ".");
	    }
	    catch(Exception e)
        {
            admin.sendMessage("Could not delete drop data!");
            _log.warning("Error while deleting drop data (" + npcId + ", " + itemId + ", " + category + "): " + e);
        }
	}

    private void reLoadNpcDropList(int npcId)
    {
        L2NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
        if (npcData == null)
            return;

        // reset the drop lists
        npcData.clearAllDropData();

        // get the drops
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            L2DropData dropData = null;

            npcData.clearAllDropData();

            try (PreparedStatement statement = con.prepareStatement("SELECT mobId, itemId, min_drop, max_drop, category, chance FROM droplist WHERE mobId=?"))
            {
                statement.setInt(1, npcId);
                try (ResultSet dropDataList = statement.executeQuery())
                {
                    while (dropDataList.next())
                    {
                        dropData = new L2DropData();

                        dropData.setItemId(dropDataList.getInt("itemId"));
                        dropData.setMinDrop(dropDataList.getInt("min_drop"));
                        dropData.setMaxDrop(dropDataList.getInt("max_drop"));
                        dropData.setChance(dropDataList.getInt("chance"));

                        int category = dropDataList.getInt("category");
                        npcData.addDropData(dropData, category);
                    }
                }
            }

            if (Config.CUSTOM_DROPLIST_TABLE)
            {
                try (PreparedStatement statement = con.prepareStatement("SELECT mobId, itemId, min_drop, max_drop, category, chance FROM custom_droplist WHERE mobId=?"))
                {
                    statement.setInt(1, npcId);
                    try (ResultSet dropDataList2 = statement.executeQuery())
                    {
                        while (dropDataList2.next())
                        {
                            dropData = new L2DropData();

                            dropData.setItemId(dropDataList2.getInt("itemId"));
                            dropData.setMinDrop(dropDataList2.getInt("min_drop"));
                            dropData.setMaxDrop(dropDataList2.getInt("max_drop"));
                            dropData.setChance(dropDataList2.getInt("chance"));

                            int category = dropDataList2.getInt("category");
                            npcData.addDropData(dropData, category);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            _log.warning("Error while reloading npc droplist (" + npcId + "): " + e);
        }
    }
    
    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}