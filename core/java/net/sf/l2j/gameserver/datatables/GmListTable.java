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
package net.sf.l2j.gameserver.datatables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class stores references to all online game masters. (access level > 100)
 * 
 * @version $Revision: 1.2.2.1.2.7 $ $Date: 2005/04/05 19:41:24 $
 */
public class GmListTable
{
    private static Logger _log = Logger.getLogger(GmListTable.class.getName());

    private Map<L2PcInstance, Boolean> _gmList;

    public static GmListTable getInstance()
    {
        return SingletonHolder._instance;
    }

    public List<L2PcInstance> getAllGms(boolean includeHidden)
    {
        List<L2PcInstance> tmpGmList = new ArrayList<>();

        for (Map.Entry<L2PcInstance, Boolean> entry : _gmList.entrySet())
        {
            if (includeHidden || !entry.getValue())
                tmpGmList.add(entry.getKey());
        }
        return tmpGmList;
    }

    public List<String> getAllGmNames(boolean includeHidden)
    {
        List<String> tmpGmList = new ArrayList<>();

        for (Map.Entry<L2PcInstance, Boolean> entry : _gmList.entrySet())
        {
            if (!entry.getValue())
                tmpGmList.add(entry.getKey().getName());
            else if (includeHidden)
                tmpGmList.add(entry.getKey().getName()+" (invis)");
        }
        return tmpGmList;
    }

    private GmListTable()
    {
        _gmList = new ConcurrentHashMap<>();
    }

    /**
     * Add a L2PcInstance player to the Set _gmList
     * @param player 
     * @param hidden 
     */
    public void addGm(L2PcInstance player, boolean hidden)
    {
        if (Config.DEBUG)
            _log.fine("added gm: "+player.getName());
        _gmList.put(player,hidden);
    }

    public void deleteGm(L2PcInstance player)
    {
        if (Config.DEBUG)
            _log.fine("deleted gm: "+player.getName());
        _gmList.remove(player);
    }

    /**
     * GM will be displayed on clients gm list
     * @param player
     */
    public void showGm(L2PcInstance player)
    {
    	if (_gmList.containsKey(player))
			_gmList.put(player, false);
    }

    /**
     * GM will no longer be displayed on clients gmlist
     * @param player
     */
    public void hideGm(L2PcInstance player)
    {
    	if (_gmList.containsKey(player))
			_gmList.put(player, true);
    }

    public boolean isGmOnline(boolean includeHidden)
    {
    	for (Map.Entry<L2PcInstance, Boolean> entry : _gmList.entrySet())
        {
            if (includeHidden || !entry.getValue())
                return true;
        }
        return false;
    }

    public void sendListToPlayer(L2PcInstance player)
    {
        if (!isGmOnline(player.isGM()))
            player.sendPacket(new SystemMessage(SystemMessage.NO_GM_PROVIDING_SERVICE_NOW)); //There are not any GMs that are providing customer service currently.
        else
        {
            SystemMessage sm = new SystemMessage(SystemMessage.GM_LIST);
            player.sendPacket(sm);

            for (String name : getAllGmNames(player.isGM()))
            {
                sm = new SystemMessage(SystemMessage.GM_S1);
                sm.addString(name);
                player.sendPacket(sm);
            }
        }
    }

    public static void broadcastToGMs(L2GameServerPacket packet)
    {
        for (L2PcInstance gm : getInstance().getAllGms(true))
        {
            gm.sendPacket(packet);
        }
    }

    public static void broadcastMessageToGMs(String message)
    {
        for (L2PcInstance gm : getInstance().getAllGms(true))
        {
            gm.sendPacket(SystemMessage.sendString(message));
        }
    }
    
    private static class SingletonHolder
	{
		protected static final GmListTable _instance = new GmListTable();
	}
}