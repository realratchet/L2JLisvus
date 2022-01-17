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

import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public final class L2MercManagerInstance extends L2FolkInstance
{
    public L2MercManagerInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
	public void onBypassFeedback(L2PcInstance player, String command)
    {
        int condition = validateCondition(player);
        if (condition <= COND_ALL_FALSE) return;

        if (condition == COND_BUSY_BECAUSE_OF_SIEGE) return;
        else if (condition == COND_CASTLE_OWNER)
        {
            StringTokenizer st = new StringTokenizer(command, " ");
            String actualCommand = st.nextToken(); // Get actual command

            String val = "";
            if (st.countTokens() >= 1)
            {
                val = st.nextToken();
            }

            if (actualCommand.equalsIgnoreCase("hire"))
            {
                if (val.isEmpty()) return;

                showBuyWindow(player, Integer.parseInt(val));
                return;
            }
        }

        super.onBypassFeedback(player, command);
    }

    @Override
	public void showChatWindow(L2PcInstance player)
    {
        String filename = "data/html/mercmanager/mercmanager-no.htm";

        int condition = validateCondition(player);
        if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
        	filename = "data/html/mercmanager/mercmanager-busy.htm"; // Busy because of siege
        else if (condition == COND_CASTLE_OWNER) // Clan owns castle
            filename = "data/html/mercmanager/mercmanager.htm"; // Owner message window

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
            if (player.getClan() != null)
            {
                if (getCastle().getSiege().getIsInProgress())
                	return COND_BUSY_BECAUSE_OF_SIEGE; // Busy because of siege
                else if (getCastle().getOwnerId() == player.getClanId() // Clan owns castle
                    && player.isClanLeader()) // Leader of clan
                    return COND_CASTLE_OWNER; // Owner
            }
        }

        return super.validateCondition(player);
    }
}