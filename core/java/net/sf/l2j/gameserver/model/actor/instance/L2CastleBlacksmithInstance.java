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

import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author  l3x
 */
public class L2CastleBlacksmithInstance extends L2FolkInstance
{
    public L2CastleBlacksmithInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
	public void onBypassFeedback(L2PcInstance player, String command)
    {
        if (CastleManorManager.getInstance().isDisabled())
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            html.setFile("data/html/npcdefault.htm");
            html.replace("%objectId%", String.valueOf(getObjectId()));
            html.replace("%npcname%", getName());
            player.sendPacket(html);
            return;
        }

        int condition = validateCondition(player);
        if (condition <= COND_ALL_FALSE)
            return;

        if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
            return;

        else if (condition == COND_CASTLE_OWNER)
        {
            if (command.startsWith("Chat"))
            {
                int val = 0;
                try
                {
                    val = Integer.parseInt(command.substring(5));
                }
                catch (IndexOutOfBoundsException ioobe) {}
                catch (NumberFormatException nfe) {}

                showChatWindow(player, val);
            }
            else
                super.onBypassFeedback(player, command);
        }
    }

    @Override
	public void showChatWindow(L2PcInstance player, int val)
    {
    	NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
    	if (CastleManorManager.getInstance().isDisabled())
        {
            html.setFile("data/html/npcdefault.htm");
            html.replace("%objectId%", String.valueOf(getObjectId()));
            html.replace("%npcname%", getName());
        }
    	else
    	{
	        String filename = "data/html/castleblacksmith/castleblacksmith-no.htm";
	
	        int condition = validateCondition(player);
	        if (condition > COND_ALL_FALSE)
	        {
	            if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
	                filename = "data/html/castleblacksmith/castleblacksmith-busy.htm";			// Busy because of siege
	            else if (condition == COND_CASTLE_OWNER) // Clan owns castle
	            {
	            	if (val == 0) 
	                    filename = "data/html/castleblacksmith/castleblacksmith.htm";				
	            	else
	                    filename = "data/html/castleblacksmith/castleblacksmith-" + val + ".htm";
	            }
	        }
	
	        html.setFile(filename);
	        html.replace("%objectId%", String.valueOf(getObjectId()));
	        html.replace("%npcname%", getName());
	        html.replace("%castleid%", Integer.toString(getCastle().getCastleId()));
    	}
    	player.sendPacket(html);
    	
    	player.sendPacket(new ActionFailed());
    }

    @Override
	protected int validateCondition(L2PcInstance player)
    {
        if (player.isGM())
            return COND_CASTLE_OWNER;

        if (getCastle() != null && getCastle().getCastleId() > 0)
        {
            if (player.getClan() != null)
            {
                if (getCastle().getSiege().getIsInProgress())
                    return COND_BUSY_BECAUSE_OF_SIEGE;                                      // Busy because of siege
                else if (getCastle().getOwnerId() == player.getClanId()                     // Clan owns castle
                        && player.isClanLeader())                                           // Leader of clan
                    return COND_CASTLE_OWNER;  // Owner
            }
        }
        return super.validateCondition(player);
    }
}