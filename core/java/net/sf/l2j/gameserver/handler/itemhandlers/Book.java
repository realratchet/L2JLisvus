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
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class Book implements IItemHandler
{
    @Override
    public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
        if (!(playable instanceof L2PcInstance))
            return;

        L2PcInstance activeChar = (L2PcInstance) playable;
        final int itemId = item.getItemId();

        String filename = "data/html/help/" + itemId + ".htm";
        String content = HtmCache.getInstance().getHtm(filename);

        if (content == null)
        {
            NpcHtmlMessage html = new NpcHtmlMessage(1);
            html.setHtml("<html><body>My Text is missing:<br>"+filename+"</body></html>");
            activeChar.sendPacket(html);
        }
        else
        {
            NpcHtmlMessage itemReply = new NpcHtmlMessage(5);
            itemReply.setHtml(content);
            activeChar.sendPacket(itemReply);
        }

        activeChar.sendPacket(new ActionFailed());
    }
}