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
package net.sf.l2j.gameserver.instancemanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2BoatInstance;
import net.sf.l2j.gameserver.model.actor.vehicle.L2BoatTrajet;
import net.sf.l2j.gameserver.model.actor.vehicle.L2VehiclePathPoint;
import net.sf.l2j.gameserver.templates.L2CharTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;

public class BoatManager
{
    private final static Logger _log = Logger.getLogger(BoatManager.class.getName());
    
    private final Map<Integer, L2BoatInstance> _boats = new HashMap<>();
    private final Map<Long, L2BoatInstance> _docks = new ConcurrentHashMap<>();
    
    public static final BoatManager getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private BoatManager()
    {
        if (Config.ALLOW_BOAT)
        {
            _log.info("Initializing BoatManager");
            load();
        }
    }
    
    private final void load()
    {
        Map<Integer, L2VehiclePathPoint[]> boatRoutes = loadBoatRoutes();

        File boatData = new File(Config.DATAPACK_ROOT, "data/boat.csv");
        try (FileReader fr = new FileReader(boatData);
            BufferedReader br = new BufferedReader(fr);
            LineNumberReader lnr = new LineNumberReader(br))
        {
            String line = null;
            while ((line = lnr.readLine()) != null)
            {
                if (line.trim().length() == 0 || line.startsWith("#"))
                {
                    continue;
                }
                
                StringTokenizer st = new StringTokenizer(line, ";");
                
                L2BoatInstance boat = createBoat(st);
                L2BoatTrajet[] trajets = new L2BoatTrajet[2];

                for (int i = 0; i < trajets.length; i++)
                {
                    int idWaypoint = Integer.parseInt(st.nextToken());
                    int idWTicket = Integer.parseInt(st.nextToken());
                    int ntx = Integer.parseInt(st.nextToken());
                    int nty = Integer.parseInt(st.nextToken());
                    int ntz = Integer.parseInt(st.nextToken());
                    String npc = st.nextToken();
                    String mess10 = st.nextToken();
                    String mess5 = st.nextToken();
                    String mess1 = st.nextToken();
                    String mess0 = st.nextToken();
                    String messb = st.nextToken();
                    L2VehiclePathPoint[] path = boatRoutes.get(idWaypoint);

                    if (path == null)
                    {
                        _log.warning(getClass().getSimpleName() + ": No path found for boat '" + boat.getName() + "' and route " + idWaypoint + "!");
                    }

                    trajets[i] = new L2BoatTrajet(idWaypoint, idWTicket, ntx, nty, ntz, npc, mess10, mess5, mess1, mess0, messb, path);
                }

                boat.setTrajets(trajets);
                dockShip(boat, boat.getX(), boat.getY());
                boat.beginCycle();
                
                _boats.put(boat.getObjectId(), boat);
                
                if (Config.DEBUG)
                    _log.info("Boat ID : " + boat.getObjectId());
            }
        }
        catch (FileNotFoundException e)
        {
            _log.warning(getClass().getSimpleName() + ": boat.csv is missing in data folder!");
        }
        catch (Exception e)
        {
            _log.warning(getClass().getSimpleName() + ": Error while creating boat table " + e);
            e.printStackTrace();
        }
    }
    
    private final Map<Integer, L2VehiclePathPoint[]> loadBoatRoutes()
    {
        Map<Integer, L2VehiclePathPoint[]> routes = new HashMap<>();

        File boatData = new File(Config.DATAPACK_ROOT, "data/boatpath.csv");
        try (FileReader fr = new FileReader(boatData);
            BufferedReader br = new BufferedReader(fr);
            LineNumberReader lnr = new LineNumberReader(br))
        {
            String line = null;
            while ((line = lnr.readLine()) != null)
            {
                if (line.trim().length() == 0 || line.startsWith("#"))
                    continue;
                
                StringTokenizer st = new StringTokenizer(line, ";");
                int routeId = Integer.parseInt(st.nextToken());
                int max = Integer.parseInt(st.nextToken());
                L2VehiclePathPoint[] path = new L2VehiclePathPoint[max];
                
                for (int i = 0; i < max; i++)
                {
                    int speed1 = Integer.parseInt(st.nextToken());
                    int speed2 = Integer.parseInt(st.nextToken());
                    int x = Integer.parseInt(st.nextToken());
                    int y = Integer.parseInt(st.nextToken());
                    int z = Integer.parseInt(st.nextToken());
                    
                    path[i] = new L2VehiclePathPoint(speed1, speed2, x, y, z);
                }
                
                routes.put(routeId, path);
            }
        }
        catch (FileNotFoundException e)
        {
            _log.warning(getClass().getSimpleName() + ": boatpath.csv is missing in data folder!");
        }
        catch (Exception e)
        {
            _log.warning(getClass().getSimpleName() + ": Error while reading boat route data " + e);
        }

        return routes;
    }
    
    /**
     * @param st
     * @return
     */
    private L2BoatInstance createBoat(StringTokenizer st)
    {
        L2BoatInstance boat;
        
        String name = st.nextToken();
        int id = Integer.parseInt(st.nextToken());
        int xspawn = Integer.parseInt(st.nextToken());
        int yspawn = Integer.parseInt(st.nextToken());
        int zspawn = Integer.parseInt(st.nextToken());
        int heading = Integer.parseInt(st.nextToken());
        
        StatsSet npcDat = new StatsSet();
        npcDat.set("npcId", id);
        npcDat.set("level", 0);
        npcDat.set("jClass", "boat");
        
        npcDat.set("baseSTR", 0);
        npcDat.set("baseCON", 0);
        npcDat.set("baseDEX", 0);
        npcDat.set("baseINT", 0);
        npcDat.set("baseWIT", 0);
        npcDat.set("baseMEN", 0);
        
        npcDat.set("baseShldDef", 0);
        npcDat.set("baseShldRate", 0);
        npcDat.set("baseAccCombat", 38);
        npcDat.set("baseEvasRate", 38);
        npcDat.set("baseCritRate", 38);
        
        npcDat.set("collisionRadius", 0);
        npcDat.set("collisionHeight", 0);
        npcDat.set("sex", "male");
        npcDat.set("type", "");
        npcDat.set("baseAtkRange", 0);
        npcDat.set("baseMpMax", 0);
        npcDat.set("baseCpMax", 0);
        npcDat.set("rewardExp", 0);
        npcDat.set("rewardSp", 0);
        npcDat.set("basePAtk", 0);
        npcDat.set("baseMAtk", 0);
        npcDat.set("basePAtkSpd", 0);
        npcDat.set("aggroRange", 0);
        npcDat.set("baseMAtkSpd", 0);
        npcDat.set("rhand", 0);
        npcDat.set("lhand", 0);
        npcDat.set("armor", 0);
        npcDat.set("baseWalkSpd", 0);
        npcDat.set("baseRunSpd", 0);
        npcDat.set("name", name);
        npcDat.set("baseHpMax", 50000);
        npcDat.set("baseHpReg", 3.e-3f);
        npcDat.set("baseMpReg", 3.e-3f);
        npcDat.set("basePDef", 100);
        npcDat.set("baseMDef", 100);
        
        L2CharTemplate template = new L2CharTemplate(npcDat);
        boat = new L2BoatInstance(IdFactory.getInstance().getNextId(), template);
        boat.setName(name);
        boat.setHeading(heading);
        boat.setXYZ(xspawn, yspawn, zspawn);
        
        return boat;
    }
    
    /**
     * @param boatId
     * @return
     */
    public L2BoatInstance getBoat(int boatId)
    {
        return _boats.get(boatId);
    }
    
    /**
     * Reserves the dock at given location for a boat.
     * @param boat
     * @param x
     * @param y
     * @return true if dock is free or already held by this boat
     */
    public boolean dockShip(L2BoatInstance boat, int x, int y)
    {
        final L2BoatInstance holder = _docks.putIfAbsent(((long) x << 32) | (y & 0xFFFFFFFFL), boat);
        return holder == null || holder == boat;
    }
    
    /**
     * Releases any dock held by a boat.
     * @param boat
     */
    public void undockShip(L2BoatInstance boat)
    {
        _docks.values().removeIf(b -> b == boat);
    }
    
    private static class SingletonHolder
    {
        protected static final BoatManager _instance = new BoatManager();
    }
}