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

import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * This class ...
 * 
 * @version $Revision$ $Date$
 */
public class L2SiegeNpcInstance extends L2FolkInstance
{
    public L2SiegeNpcInstance(int objectID, L2NpcTemplate template)
    {
        super(objectID, template);
    }
    /**
     * If siege is in progress shows the Busy HTML<BR>
     * else it shows the SiegeInfo window.
     * @param player
     */
    @Override
	public void showChatWindow(L2PcInstance player)
    {
        if (!getCastle().getSiege().getIsInProgress())
        {
            getCastle().getSiege().listRegisterClan(player);
        }
        else
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            html.setFile("data/html/siege/" + getTemplate().npcId + "-busy.htm");
            html.replace("%castlename%", getCastle().getName());
            html.replace("%objectId%", String.valueOf(getObjectId()));
            player.sendPacket(html);
        }
        player.sendPacket(new ActionFailed());
    }
}