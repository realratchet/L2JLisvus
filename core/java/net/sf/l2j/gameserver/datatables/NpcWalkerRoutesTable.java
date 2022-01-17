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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2NpcWalkerNode;

/**
 * Main Table to Load Npc Walkers Routes and Chat SQL Table.<br>
 * 
 * @author Rayan RPG for L2Emu Project
 * 
 * @since 927 
 *
 */
public class NpcWalkerRoutesTable
{
    private static Logger _log = Logger.getLogger(NpcWalkerRoutesTable.class.getName());

    private List<L2NpcWalkerNode> _routes = new ArrayList<>();

    public static NpcWalkerRoutesTable getInstance()
    {
        return SingletonHolder._instance;
    }

    private NpcWalkerRoutesTable()
    {
        load();
    }

    public void load()
    {
    	_log.info("Initializing Walker Routes Table.");
    	
        _routes.clear();

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT route_id, npc_id, move_point, chatText, move_x, move_y, move_z, delay, running FROM walker_routes ORDER By move_point ASC");
            ResultSet rset = statement.executeQuery())
        {
            L2NpcWalkerNode route;
            while (rset.next())
            {
                route = new L2NpcWalkerNode();
                route.setRouteId(rset.getInt("route_id"));
                route.setNpcId(rset.getInt("npc_id"));
                route.setMovePoint(rset.getString("move_point"));
                route.setChatText(rset.getString("chatText"));

                route.setMoveX(rset.getInt("move_x"));
                route.setMoveY(rset.getInt("move_y"));
                route.setMoveZ(rset.getInt("move_z"));
                route.setDelay(rset.getInt("delay"));
                route.setRunning(rset.getBoolean("running"));

                _routes.add(route);
            }

            _log.info("WalkerRoutesTable: Loaded "+_routes.size()+" Npc Walker Routes.");
        }
        catch (Exception e) 
        {
            _log.warning("WalkerRoutesTable: Error while loading Npc Walkers Routes: "+e.getMessage());
        }
    }

    public List<L2NpcWalkerNode> getRouteForNpc(int id)
    {
        List<L2NpcWalkerNode> result = new ArrayList<>();
        for (L2NpcWalkerNode node : _routes)
        {
            if (node.getNpcId() == id)
                result.add(node);
        }
        return result;	
    }
    
    private static class SingletonHolder
	{
		protected static final NpcWalkerRoutesTable _instance = new NpcWalkerRoutesTable();
	}
}