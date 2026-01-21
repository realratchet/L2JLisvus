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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.gameserver.datatables.RestartPointData;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2MapRegion;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2SiegeClan;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.zone.L2ZoneSpawn;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.util.IXmlReader;
import net.sf.l2j.gameserver.util.Util;

public class MapRegionManager implements IXmlReader
{
	private static final Logger _log = Logger.getLogger(MapRegionManager.class.getName());
	
	public static final MapRegionManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final MapRegionManager _instance = new MapRegionManager();
	}
	
	public static enum TeleportWhereType
	{
		Castle,
		ClanHall,
		SiegeFlag,
		Town
	}
	
	private final List<L2MapRegion> _regions = new ArrayList<>();
	
	protected MapRegionManager()
	{
		load();
	}
	
	public void load()
	{
		parseDatapackFile("data/mapRegions.xml");
		_log.info("Loaded " + _regions.size() + " map regions.");
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
					if ("region".equalsIgnoreCase(d.getNodeName()))
					{
						L2MapRegion region = new L2MapRegion(new StatsSet(d.getAttributes()));
						
						for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
						{
							NamedNodeMap attrs = c.getAttributes();
							Node attribute;
							
							if ("map".equalsIgnoreCase(c.getNodeName()))
							{
								int mapX, mapY;
								
								attribute = attrs.getNamedItem("X");
								mapX = attribute != null ? Integer.parseInt(attribute.getNodeValue()) : null;
								attribute = attrs.getNamedItem("Y");
								mapY = attribute != null ? Integer.parseInt(attribute.getNodeValue()) : null;
								
								region.addMap(mapX, mapY);
							}
						}
						_regions.add(region);
					}
				}
			}
		}
	}
	
	public L2MapRegion getMapRegion(int locX, int locY)
	{
		for (L2MapRegion region : _regions)
		{
			if (region.isZoneInRegion(getMapRegionX(locX), getMapRegionY(locY)))
			{
				return region;
			}
		}
		return null;
	}
	
	public int getMapRegionLocId(int locX, int locY)
	{
		L2MapRegion region = getMapRegion(locX, locY);
		if (region != null)
		{
			return region.getLocId();
		}
		return 0;
	}
	
	public L2MapRegion getMapRegion(L2Object obj)
	{
		return getMapRegion(obj.getX(), obj.getY());
	}
	
	public int getMapRegionLocId(L2Object obj)
	{
		return getMapRegionLocId(obj.getX(), obj.getY());
	}
	
	public int getMapRegionX(int posX)
	{
		return (posX >> 15) + 9 + 11;
	}
	
	public int getMapRegionY(int posY)
	{
		return (posY >> 15) + 10 + 8;
	}
	
	/**
	 * Get town name by character position
	 * @param activeChar
	 * @return
	 */
	public String getClosestTownName(L2Character activeChar)
	{
		L2MapRegion region = getMapRegion(activeChar);
		
		if (region == null)
		{
			return "Aden Castle Town";
		}
		
		return null;
	}
	
	public int getAreaCastleId(L2Character activeChar)
	{
		L2MapRegion region = getMapRegion(activeChar);
		if (region == null)
		{
			return 0;
		}
		return region.getCastleId();
	}
	
	public Location getTeleToLocation(L2Character activeChar, TeleportWhereType teleportWhere)
	{
		if (activeChar instanceof L2PcInstance)
		{
			L2PcInstance player = ((L2PcInstance) activeChar);
			
			if (player.getClan() != null)
			{
				L2ZoneSpawn spawnZone = null;
				
				// If teleport to clan hall
				if (teleportWhere == TeleportWhereType.ClanHall)
				{
					ClanHall clanhall = ClanHallManager.getInstance().getClanHallByOwner(player.getClan());
					if (clanhall != null)
					{
						spawnZone = clanhall.getZone();
					}
				}
				else if (teleportWhere == TeleportWhereType.Castle)
				{
					Castle castle = CastleManager.getInstance().getCastleByOwner(player.getClan());

					if (castle != null)
					{
						spawnZone = castle.getZone();
					}
					else
					{
						castle = CastleManager.getInstance().getCastle(player);
						if (castle != null && castle.getCastleId() > 0)
						{
							// If teleport to castle
							if (castle.getSiege().getIsInProgress() && castle.getSiege().getDefenderClan(player.getClan()) != null)
							{
								spawnZone = castle.getZone();
							}
						}
					}
				}

				if (spawnZone != null && spawnZone.getRestartPointGroup() > 0)
				{
					Location loc = RestartPointData.getInstance().getSpawnLocById(spawnZone.getRestartPointGroup());
					if (loc != null)
					{
						return loc;
					}
				}

				if (teleportWhere == TeleportWhereType.SiegeFlag)
				{
					Siege siege = SiegeManager.getInstance().getSiege(player);
					if (siege != null)
					{
						// Check if player's clan is attacker
						L2SiegeClan sc = siege.getAttackerClan(player.getClan());
						if (sc != null)
						{
							// Get closest flag
							double closestDistance = 0;
							L2NpcInstance closestFlag = null;
							for (L2NpcInstance flag : sc.getFlags())
							{
								final double distance = Util.calculateDistance(player, flag, true);
								if (closestFlag == null || distance < closestDistance)
								{
									closestFlag = flag;
									closestDistance = distance;
								}
							}
							
							// Spawn to closest flag
							if (closestFlag != null)
							{
								return new Location(closestFlag.getX(), closestFlag.getY(), closestFlag.getZ());
							}
						}
					}
				}
			}
			
			// Karma player land out of city
			if (player.getKarma() > 0)
			{
				L2ZoneSpawn zone = ZoneManager.getInstance().getZone(player, L2ZoneSpawn.class);
				Location loc = null;

				if (zone != null && zone.getRestartPointGroup() > 0)
				{
					loc = RestartPointData.getInstance().getChaoticSpawnLocById(zone.getRestartPointGroup());
				}

				if (loc == null)
				{
					L2MapRegion region = getMapRegion(player);
					loc = region != null ? RestartPointData.getInstance().getChaoticSpawnLocById(region.getRestartPointGroup()) : null;
				}

				if (loc != null)
				{
					return loc;
				}

				return new Location(17817, 170079, -3530);
			}
			
			// Checking if in arena
			L2ZoneSpawn arena = ZoneManager.getInstance().getArena(player);
			if (arena != null)
			{
				Location location = RestartPointData.getInstance().getSpawnLocById(arena.getRestartPointGroup());
				if (location != null)
				{
					return location;
				}
			}
		}

		Location loc = null;
		
		// Get the nearest town
		L2ZoneSpawn zone = ZoneManager.getInstance().getZone(activeChar, L2ZoneSpawn.class);

		if (zone != null && zone.getRestartPointGroup() > 0)
		{
			loc = RestartPointData.getInstance().getSpawnLocById(zone.getRestartPointGroup());
		}

		if (loc == null)
		{
			L2MapRegion region = getMapRegion(activeChar);
			loc = region != null ? RestartPointData.getInstance().getSpawnLocById(region.getRestartPointGroup()) : null;
		}

		if (loc != null)
		{
			return loc;
		}
		
		// Port to the Talking Island if no closest spawn location found
		return new Location(-84176, 243382, -3126);
	}
}
