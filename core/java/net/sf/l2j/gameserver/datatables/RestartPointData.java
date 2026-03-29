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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.util.IXmlReader;
import net.sf.l2j.util.Rnd;

public class RestartPointData implements IXmlReader
{
    public static RestartPointData getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private static Logger _log = Logger.getLogger(RestartPointData.class.getName());
    
    private final Map<String, List<Location>> _restartPoints = new HashMap<>();
    private final Map<String, List<Location>> _chaoticRestartPoints = new HashMap<>();
    private int _count = 0;
    
    private RestartPointData()
    {
        parseDatapackFile("data/restartPoints.xml");
        _log.info(getClass().getSimpleName() + ": Loaded " + _count + " restart points.");
    }
    
    @Override
    public void parseDocument(Document doc, File f)
    {
        for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if ("list".equalsIgnoreCase(n.getNodeName()))
            {
                for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
                {
                    if ("restartPointGroup".equalsIgnoreCase(d.getNodeName()))
                    {
                        List<Location> spawnLocs = null;
                        List<Location> chaoticSpawnLocs = null;
                        
                        NamedNodeMap attrs;
                        Node attribute;
                        
                        attrs = d.getAttributes();
                        
                        attribute = attrs.getNamedItem("id");
                        String id = attribute != null ? attribute.getNodeValue() : null;

                        if (id == null)
                        {
                            _log.warning("The restart point has no identifier: " + d);
                            continue;
                        }

                        if (_restartPoints.containsKey(id) || _chaoticRestartPoints.containsKey(id))
                        {
                            _log.warning("Duplicate restart point ID '" + id + "'. This restart point will be ignored!");
                            continue;
                        }
                        
                        for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
                        {
                            attrs = c.getAttributes();
                            
                            if ("spawn".equalsIgnoreCase(c.getNodeName()))
                            {
                                int x, y, z;
                                boolean isChaotic;
                                
                                attribute = attrs.getNamedItem("X");
                                x = attribute != null ? Integer.parseInt(attribute.getNodeValue()) : 0;
                                attribute = attrs.getNamedItem("Y");
                                y = attribute != null ? Integer.parseInt(attribute.getNodeValue()) : 0;
                                attribute = attrs.getNamedItem("Z");
                                z = attribute != null ? Integer.parseInt(attribute.getNodeValue()) : 0;
                                attribute = attrs.getNamedItem("isChaotic");
                                isChaotic = attribute != null ? Boolean.parseBoolean(attribute.getNodeValue()) : false;
                                
                                if (isChaotic)
                                {
                                    if (chaoticSpawnLocs == null)
                                    {
                                        chaoticSpawnLocs = new ArrayList<>();
                                    }
                                    chaoticSpawnLocs.add(new Location(x, y, z));
                                }
                                else
                                {
                                    if (spawnLocs == null)
                                    {
                                        spawnLocs = new ArrayList<>();
                                    }
                                    spawnLocs.add(new Location(x, y, z));
                                }
                                
                                _count++;
                            }
                        }
                        
                        if (spawnLocs != null)
                        {
                            _restartPoints.put(id, spawnLocs);
                        }
                        if (chaoticSpawnLocs != null)
                        {
                            _chaoticRestartPoints.put(id, chaoticSpawnLocs);
                        }
                    }
                }
            }
        }
    }
    
    public List<Location> getSpawnLocsById(String id)
    {
        return _restartPoints.get(id);
    }
    
    public List<Location> getChaoticSpawnLocsById(String id)
    {
        return _chaoticRestartPoints.get(id);
    }
    
    public Location getSpawnLocById(String id)
    {
        List<Location> spawnLocs = _restartPoints.get(id);
        Location loc;
        
        if (spawnLocs != null && !spawnLocs.isEmpty())
        {
            loc = spawnLocs.get(Rnd.get(spawnLocs.size()));
        }
        else
        {
            loc = null;
            _log.warning(getClass().getSimpleName() + ": No restart points found for group ID " + id);
        }
        return loc;
    }
    
    public Location getChaoticSpawnLocById(String id)
    {
        List<Location> spawnLocs = _chaoticRestartPoints.get(id);
        Location loc;
        
        if (spawnLocs != null && !spawnLocs.isEmpty())
        {
            loc = spawnLocs.get(Rnd.get(spawnLocs.size()));
        }
        else
        {
            loc = null;
            _log.warning(getClass().getSimpleName() + ": No chaotic restart points found for group ID " + id);
        }
        return loc;
    }
    
    private static class SingletonHolder
    {
        protected static final RestartPointData _instance = new RestartPointData();
    }
}
