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
package net.sf.l2j.gameserver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;

/**
 * @author Nyaran, DnR
 */
public class L2MapRegion
{
    private final String _name;
    private final String _restartPointGroup;
    private final int _locId;
    private final int _castleId;
    private final int _bbs;
    private List<int[]> _maps = null;
    
    private List<Location> _spawnLocs = null;
    private List<Location> _otherSpawnLocs = null;
    private List<Location> _chaoticSpawnLocs = null;
    private List<Location> _banishSpawnLocs = null;
    
    private final Map<Race, String> _bannedRaces = new HashMap<>();
    
    public L2MapRegion(StatsSet set)
    {
        _name = set.getString("name");
        _restartPointGroup = set.getString("restartPointGroup");
        _locId = set.getInteger("locId");
        _castleId = set.getInteger("castleId");
        _bbs = set.getInteger("bbs");
    }
    
    public final String getName()
    {
        return _name;
    }
    
    public final String getRestartPointGroup()
    {
        return _restartPointGroup;
    }
    
    public final int getLocId()
    {
        return _locId;
    }
    
    public final int getCastleId()
    {
        return _castleId;
    }
    
    public final int getBbs()
    {
        return _bbs;
    }
    
    public final void addMap(int x, int y)
    {
        if (_maps == null)
        {
            _maps = new ArrayList<>();
        }
        
        _maps.add(new int[] { x, y });
    }
    
    public final List<int[]> getMaps()
    {
        return _maps;
    }
    
    public final boolean isZoneInRegion(int x, int y)
    {
        if (_maps == null)
        {
            return false;
        }
        
        for (int[] map : _maps)
        {
            if ((map[0] == x) && (map[1] == y))
            {
                return true;
            }
        }
        return false;
    }
    
    // Respawn
    public final void addSpawn(int x, int y, int z)
    {
        if (_spawnLocs == null)
        {
            _spawnLocs = new ArrayList<>();
        }
        
        _spawnLocs.add(new Location(x, y, z));
    }
    
    public final void addOtherSpawn(int x, int y, int z)
    {
        if (_otherSpawnLocs == null)
        {
            _otherSpawnLocs = new ArrayList<>();
        }
        
        _otherSpawnLocs.add(new Location(x, y, z));
    }
    
    public final void addChaoticSpawn(int x, int y, int z)
    {
        if (_chaoticSpawnLocs == null)
        {
            _chaoticSpawnLocs = new ArrayList<>();
        }
        
        _chaoticSpawnLocs.add(new Location(x, y, z));
    }
    
    public final void addBanishSpawn(int x, int y, int z)
    {
        if (_banishSpawnLocs == null)
        {
            _banishSpawnLocs = new ArrayList<>();
        }
        
        _banishSpawnLocs.add(new Location(x, y, z));
    }
    
    public final List<Location> getSpawns()
    {
        return _spawnLocs;
    }
    
    public final Location getSpawnLoc()
    {
        return _spawnLocs.get(Rnd.get(_spawnLocs.size()));
    }
    
    public final Location getOtherSpawnLoc()
    {
        return _otherSpawnLocs != null && !_otherSpawnLocs.isEmpty() ? _otherSpawnLocs.get(Rnd.get(_otherSpawnLocs.size())) : getSpawnLoc();
    }
    
    public final Location getChaoticSpawnLoc()
    {
        return _chaoticSpawnLocs != null && !_chaoticSpawnLocs.isEmpty() ? _chaoticSpawnLocs.get(Rnd.get(_chaoticSpawnLocs.size())) : getSpawnLoc();
    }
    
    public final Location getBanishSpawnLoc()
    {
        return _banishSpawnLocs != null && !_banishSpawnLocs.isEmpty() ? _banishSpawnLocs.get(Rnd.get(_banishSpawnLocs.size())) : getSpawnLoc();
    }
    
    public final void addBannedRace(String race, String point)
    {
        _bannedRaces.put(Race.valueOf(race), point);
    }
    
    public final Map<Race, String> getBannedRaces()
    {
        return _bannedRaces;
    }
}
