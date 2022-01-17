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

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author NightMarez
 * @version $Revision: 1.3.2.2.2.5 $ $Date: 2005/03/27 15:29:32 $
 *
 */
public final class L2CastleTeleporterInstance extends L2FolkInstance
{
    private boolean _currentTask = false;

    /**
     * @param objectId 
     * @param template
     */
    public L2CastleTeleporterInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
	public void onBypassFeedback(L2PcInstance player, String command)
    {
        StringTokenizer st = new StringTokenizer(command, " ");
        String actualCommand = st.nextToken(); // Get actual command
        if (actualCommand.equalsIgnoreCase("tele"))
        {
            int delay;
            if (!getTask())
            {
                if (getCastle().getSiege().getIsInProgress() && getCastle().getSiege().getControlTowerCount() == 0)
                    delay = 480000;
                else
                    delay = 30000;

                setTask(true);
                ThreadPoolManager.getInstance().scheduleGeneral(new oustAllPlayers(), delay);
            }

            String filename = "data/html/teleporter/MassGK-1.htm";
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            html.setFile(filename);
            player.sendPacket(html);
            return;
        }
		super.onBypassFeedback(player, command);
    }

    @Override
	public void showChatWindow(L2PcInstance player)
    {
        String filename;
        if (!getTask())
        {
            if (getCastle().getSiege().getIsInProgress() && getCastle().getSiege().getControlTowerCount() == 0)
                filename = "data/html/teleporter/MassGK-2.htm";
            else
                filename = "data/html/teleporter/MassGK.htm";
        }
        else
            filename = "data/html/teleporter/MassGK-1.htm";

        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
        
        player.sendPacket(new ActionFailed());
    }

    public void oustAllPlayers()
    {
        getCastle().oustAllPlayers();
    }

    class oustAllPlayers implements Runnable
    {
        @Override
		public void run()
        {
            try
            {
                if (getCastle().getSiege().getIsInProgress())
                {
                    NpcSay cs = new NpcSay(getObjectId(), 1, getNpcId(), "The defenders of "+ getCastle().getName()+" castle will be teleported to the inner castle.");
                    int region = MapRegionTable.getInstance().getMapRegion(getX(), getY());
                    for (L2PcInstance player : L2World.getInstance().getAllPlayers())
                    {
                        if (region == MapRegionTable.getInstance().getMapRegion(player.getX(),player.getY()))
                            player.sendPacket(cs);
                    }
                }
                oustAllPlayers();
                setTask(false);
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
    }

    public boolean getTask()
    {
        return _currentTask;
    }

    public void setTask(boolean state)
    {
        _currentTask = state;
    }
}