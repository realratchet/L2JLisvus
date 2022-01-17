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

import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.model.eventgame.TvTEvent;
import net.sf.l2j.gameserver.model.eventgame.L2Event.EventState;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class L2TvTManagerInstance extends L2NpcInstance
{
    public L2TvTManagerInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(L2PcInstance player, String command)
    {
        if (command.equals("tvt_event_participation"))
            TvTEvent.getInstance().registerPlayer(player);
        else if (command.equals("tvt_event_remove_participation"))
        	TvTEvent.getInstance().removePlayer(player);
    }

    @Override
    public void showChatWindow(L2PcInstance player, int val)
    {
        if (player == null)
            return;

        if (!TvTEvent.getInstance().isEnabled())
            return;

        if (TvTEvent.getInstance().getEventState() == EventState.REGISTER)
        {
            String htmFile = "data/html/mods/event/";

            if (!TvTEvent.getInstance().isRegistered(player))
                htmFile += "TvTEventParticipation";
            else
                htmFile += "TvTEventRemoveParticipation";

            htmFile += ".htm";

            String htmContent = HtmCache.getInstance().getHtm(htmFile);
            if (htmContent != null)
            {
                NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());

                npcHtmlMessage.setHtml(htmContent);
                npcHtmlMessage.replace("%objectId%", String.valueOf(getObjectId()));
                npcHtmlMessage.replace("%registeredcount%", String.valueOf(TvTEvent.getInstance().getRegistered().size()));
                npcHtmlMessage.replace("%minimumplayers%", String.valueOf(TvTEvent.getInstance().getMinParticipants()));
                npcHtmlMessage.replace("%maximumplayers%", String.valueOf(TvTEvent.getInstance().getMaxParticipants()));
                npcHtmlMessage.replace("%minimumlevel%", String.valueOf(TvTEvent.getInstance().getMinLevel()));
                npcHtmlMessage.replace("%maximumlevel%", String.valueOf(TvTEvent.getInstance().getMaxLevel()));
                player.sendPacket(npcHtmlMessage);
            }
        }
        else
        {
            String htmFile = "data/html/mods/event/TvTEventStatus.htm";
            String htmContent = HtmCache.getInstance().getHtm(htmFile);

            if (htmContent != null)
            {
                
                NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());

                npcHtmlMessage.setHtml(htmContent);
                npcHtmlMessage.replace("%team1playercount%", String.valueOf(TvTEvent.getInstance().getBlueTeam().size()));
                npcHtmlMessage.replace("%team1points%", String.valueOf(TvTEvent.getInstance().getBlueTeamKills()));
                npcHtmlMessage.replace("%team2playercount%", String.valueOf(TvTEvent.getInstance().getRedTeam().size()));
                npcHtmlMessage.replace("%team2points%", String.valueOf(TvTEvent.getInstance().getRedTeamKills()));
                player.sendPacket(npcHtmlMessage);
            }
        }

        player.sendPacket(new ActionFailed());
    }
}