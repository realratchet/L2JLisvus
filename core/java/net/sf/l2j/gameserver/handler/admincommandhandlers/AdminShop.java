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
import net.sf.l2j.gameserver.TradeController;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.BuyList;
import net.sf.l2j.gameserver.network.serverpackets.SellList;

/**
 * This class handles following admin commands:
 * - gmshop = shows menu
 * - buy id = shows shop with respective id
 * - sell = sells items
 * @version $Revision: 1.2.4.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminShop implements IAdminCommandHandler
{
    private static Logger _log = Logger.getLogger(AdminShop.class.getName());
	
    private static String[] _adminCommands =
    {
        "admin_buy",
        "admin_sell",
        "admin_gmshop"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_buy"))
        {
            try
            {
                handleBuyRequest(activeChar, command.substring(10));
            }
            catch (IndexOutOfBoundsException e)
            {
                activeChar.sendMessage("Please specify a buylist.");
            }
        }
        else if (command.startsWith("admin_sell"))
        {
            activeChar.sendPacket(new SellList(activeChar));

            if (Config.DEBUG)
                _log.fine("GM: "+activeChar.getName()+"("+activeChar.getObjectId()+") opened GM Shop sell list");

            activeChar.sendPacket(new ActionFailed());
        }
        else if (command.equals("admin_gmshop"))
        {
            showHelpPage(activeChar, "gmshops.htm");
        }
		
        String target = (activeChar.getTarget() != null?activeChar.getTarget().getName():"no-target");
        GMAudit.auditGMAction(activeChar.getName(), command, target, "");

        return true;
    }

    private void handleBuyRequest(L2PcInstance activeChar, String command)
    {	
        int val = -1;

        try
        {
            val = Integer.parseInt(command);
        }
        catch (Exception e)
        {
            _log.warning("admin buylist failed:"+command);
        }

        L2TradeList list = TradeController.getInstance().getBuyList(val);
        if (list != null)
        {	
            BuyList bl  = new BuyList(list, activeChar.getAdena());
            activeChar.sendPacket(bl);

            if (Config.DEBUG)
                _log.fine("GM: "+activeChar.getName()+"("+activeChar.getObjectId()+") opened GM shop id "+val);
        }
        else
            _log.warning("no buylist with id:" +val);

        activeChar.sendPacket(new ActionFailed());
    }
    
    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}