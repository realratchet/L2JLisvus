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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.pathfinding.AbstractNodeLoc;
import net.sf.l2j.gameserver.templates.L2DoorTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;

public class DoorTable 
{
    private static Logger _log = Logger.getLogger(DoorTable.class.getName());

    private final Map<Integer, L2DoorInstance> _doors = new HashMap<>();

    public static DoorTable getInstance()
    {
        return SingletonHolder._instance;
    }

    private DoorTable() 
    {
    	load();
    }
    
    private void load()
    {
    	try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(new File(Config.DATAPACK_ROOT + "/data/doors.xml"));
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if ("list".equalsIgnoreCase(n.getNodeName()))
                {
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if (d.getNodeName().equalsIgnoreCase("door"))
						{
							NamedNodeMap attrs = d.getAttributes();
							final int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
							final String name = attrs.getNamedItem("name").getNodeValue();
							
							int rangeXMin = 0, rangeYMin = 0, rangeZMin = 0, rangeXMax = 0, rangeYMax = 0, rangeZMax = 0;
							// Door IDs of doors connected to it
					        int[] connectedDoorIds = null;
							
							StatsSet set = new StatsSet();
							set.set("id", id);
							set.set("name", name);
							
							// Default values
							set.set("baseHpReg", 3.e-3f);
					        set.set("baseMpReg", 3.e-3f);
					        set.set("sex", "male");

					        for (Node prop = d.getFirstChild(); prop != null; prop = prop.getNextSibling())
							{
								attrs = prop.getAttributes();
								
								if (prop.getNodeName().equalsIgnoreCase("set"))
								{
									final String key = attrs.getNamedItem("name").getNodeValue();
									final String val = attrs.getNamedItem("val").getNodeValue();
									set.set(key, val);
								}
								else if (prop.getNodeName().equalsIgnoreCase("connectedDoors"))
								{
									final Node content = prop.getFirstChild();
									if (content != null)
									{
										final StringTokenizer st = new StringTokenizer(content.getNodeValue());
	
										connectedDoorIds = new int[st.countTokens()];
										for (int i = 0; i < connectedDoorIds.length; i++)
										{
											connectedDoorIds[i] = Integer.parseInt(st.nextToken().trim());
										}
									}
								}
								else if (prop.getNodeName().equalsIgnoreCase("dimensions"))
								{
									rangeXMin = Integer.parseInt(attrs.getNamedItem("minX").getNodeValue());
									rangeYMin = Integer.parseInt(attrs.getNamedItem("minY").getNodeValue());
									rangeZMin = Integer.parseInt(attrs.getNamedItem("minZ").getNodeValue());
									rangeXMax = Integer.parseInt(attrs.getNamedItem("maxX").getNodeValue());
									rangeYMax = Integer.parseInt(attrs.getNamedItem("maxY").getNodeValue());
									rangeZMax = Integer.parseInt(attrs.getNamedItem("maxZ").getNodeValue());

									if (rangeXMin > rangeXMax)
									{
							            _log.severe(getClass().getSimpleName() + ": Error in door data, XMin > XMax, ID:"+id);
									}
							        if (rangeYMin > rangeYMax)
							        {
							            _log.severe(getClass().getSimpleName() + ": Error in door data, YMin > YMax, ID:"+id);
							        }
							        if (rangeZMin > rangeZMax)
							        {
							            _log.severe(getClass().getSimpleName() + ": Error in door data, ZMin > ZMax, ID:"+id);
							        }
								}
							}
							
							final int x = set.getInteger("x");
							final int y = set.getInteger("y");
							final int z = set.getInteger("z");
							final int collisionRadius = ((rangeXMax - rangeXMin) > (rangeYMax - rangeYMin)) ? (rangeYMax - rangeYMin) : (rangeXMax - rangeXMin);
							final boolean isOpenByDefault = set.getBool("isOpenByDefault", false);
							final int castleId = set.getInteger("castleId", 0);
							final int clanHallId = set.getInteger("clanHallId", 0);

							set.set("collisionRadius", collisionRadius);
					        set.set("collisionHeight", rangeZMax - rangeZMin);

					        // Unused properties (XML-set values will be ignored)
					        set.set("baseSTR", 0);
					        set.set("baseCON", 0);
					        set.set("baseDEX", 0);
					        set.set("baseINT", 0);
					        set.set("baseWIT", 0);
					        set.set("baseMEN", 0);
					        set.set("baseShldDef", 0);
					        set.set("baseShldRate", 0);
					        set.set("baseCritRate",  38);
					        set.set("baseAtkRange", 0);
					        set.set("baseMpMax", 0);
					        set.set("baseCpMax", 0);
					        set.set("basePAtk", 0);
					        set.set("baseMAtk", 0);
					        set.set("basePAtkSpd", 0);
					        set.set("baseMAtkSpd", 0);
					        set.set("baseWalkSpd", 0);
					        set.set("baseRunSpd", 0);

					        L2DoorTemplate template = new L2DoorTemplate(set);
					        L2DoorInstance door = new L2DoorInstance(IdFactory.getInstance().getNextId(), template);
					        door.setAutoOpenDelay(set.getLong("autoOpenDelay", 0));
					        door.setAutoCloseDelay(set.getLong("autoCloseDelay", 0));
					        door.setRange(rangeXMin, rangeYMin, rangeZMin, rangeXMax, rangeYMax, rangeZMax);
					        
					        if (connectedDoorIds != null)
					        {
					        	door.setConnectedDoorIds(connectedDoorIds);
					        }
					        
					        try
					        {
					            door.setMapRegion(MapRegionTable.getInstance().getMapRegion(x, y));
					        }
					        catch (Exception e)
					        {
					            _log.severe(getClass().getSimpleName() + ": Error in door data, ID: "+id);
					        }

					        door.setCurrentHpMp(door.getMaxHp(), door.getMaxMp());
					        door.setIsOpen(isOpenByDefault);
					        door.startAutoOpenCloseTask();
					        door.setXYZInvisible(x, y, z);

					        door.setMapRegion(MapRegionTable.getInstance().getMapRegion(x, y));
					        door.spawnMe(door.getX(), door.getY(), door.getZ());

					        _doors.put(id, door);
					        
					        if (castleId > 0)
					        {
					        	attachDoorToCastle(castleId, door);
					        }
					        else if (clanHallId > 0)
					        {
					        	attachDoorToClanHall(clanHallId, door);
					        }
						}
					}
                }
            }
			_log.info(getClass().getSimpleName() + ": Loaded " + _doors.size() + " doors.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			_log.severe(getClass().getSimpleName() + ": Error reading doors.xml file: " + e);
		}
    }

    public L2DoorInstance getDoor(Integer id) 
    {
        return _doors.get(id);
    }

    public L2DoorInstance[] getDoors() 
    {
        L2DoorInstance[] _allTemplates = _doors.values().toArray(new L2DoorInstance[_doors.size()]);
        return _allTemplates;
    }

    public boolean checkIfDoorsBetween(AbstractNodeLoc start, AbstractNodeLoc end)
    {
        return checkIfDoorsBetween(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
    }

    public boolean checkIfDoorsBetween(int x, int y, int z, int tx, int ty, int tz)
    {
        int region;
        try
        {
            region = MapRegionTable.getInstance().getMapRegion(x,y);
        }
        catch (Exception e)
        {
            return false;
        }

        // There are quite many doors, maybe they should be split
        for (L2DoorInstance doorInst : getDoors())
        {
            if (doorInst.getMapRegion() != region)
                continue;
            if (doorInst.getXMax() == 0)
                continue;

            // line segment goes through box
            // first basic checks to stop most calculations short
            // phase 1, x
            if ((x <= doorInst.getXMax() && tx >= doorInst.getXMin()) || (tx <= doorInst.getXMax() && x >= doorInst.getXMin()))
            {
                //phase 2, y
                if ((y <= doorInst.getYMax() && ty >= doorInst.getYMin()) || (ty <= doorInst.getYMax() && y >= doorInst.getYMin()))
                {
                    // phase 3, basically only z remains but now we calculate it with another formula (by range)
                    // in some cases the direct line check (only) in the beginning isn't sufficient,
                    // when char z changes a lot along the path
                    if (doorInst.getCurrentHp() > 0 && !doorInst.isOpen())
                    {
                        int px1 = doorInst.getXMin(); 
                        int py1 = doorInst.getYMin();
                        int pz1 = doorInst.getZMin();
                        int px2 = doorInst.getXMax();
                        int py2 = doorInst.getYMax();
                        int pz2 = doorInst.getZMax();

                        int l = tx - x;
                        int m = ty - y;
                        int n = tz - z;

                        int dk;

                        if ((dk = (doorInst.getA() * l + doorInst.getB() * m + doorInst.getC() * n)) == 0)
                            continue; // Parallel

                        float p = (float)(doorInst.getA() * x + doorInst.getB() * y + doorInst.getC() * z + doorInst.getD()) / (float)dk;

                        int fx = (int)(x - l * p);
                        int fy = (int)(y - m * p);
                        int fz = (int)(z - n * p);

                        if ((Math.min(x,tx) <= fx && fx <= Math.max(x,tx)) && (Math.min(y,ty) <= fy && fy <= Math.max(y,ty)) && (Math.min(z,tz) <= fz && fz <= Math.max(z,tz)))
                        {
                            if (((fx >= px1 && fx <= px2) || (fx >= px2 && fx <= px1))
                                     && ((fy >= py1 && fy <= py2) || (fy >= py2 && fy <= py1))
                                     && ((fz >= pz1 && fz <= pz2) || (fz >= pz2 && fz <= pz1)))
                                return true; // Door between
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void attachDoorToCastle(int castleId, L2DoorInstance door)
    {
    	Castle castle = CastleManager.getInstance().getCastleById(castleId);
        if (castle != null)
        {
        	castle.getDoors().add(door);

            if (Config.DEBUG)
            {
                _log.warning(getClass().getSimpleName() + ": Door " + door.getName() + " has been attached to " + castle.getName());
            }
        }
    }
    
    private void attachDoorToClanHall(int clanHallId, L2DoorInstance door)
    {
    	ClanHall clanHall = ClanHallManager.getInstance().getClanHallById(clanHallId);
        if (clanHall != null)
        {
        	clanHall.getDoors().add(door);
            door.setClanHall(clanHall);

            if (Config.DEBUG)
            {
            	 _log.warning(getClass().getSimpleName() + ": Door " + door.getName() + " has been attached to " + clanHall.getName());
            }
        }
    }
    
    private static class SingletonHolder
	{
		protected static final DoorTable _instance = new DoorTable();
	}
}