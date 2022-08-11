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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2WorldRegion;
import net.sf.l2j.gameserver.model.zone.L2ZoneSpawn;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.form.ZoneCuboid;
import net.sf.l2j.gameserver.model.zone.form.ZoneCylinder;
import net.sf.l2j.gameserver.model.zone.form.ZoneNPoly;
import net.sf.l2j.gameserver.model.zone.type.L2ArenaZone;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.model.zone.type.L2OlympiadStadiumZone;

/**
 * This class manages all zone data.
 * 
 * @author durgus
 */
public class ZoneManager
{
	private final static Logger _log = Logger.getLogger(ZoneManager.class.getName());
	
	private final Map<Class<? extends L2ZoneType>, Map<Integer, ? extends L2ZoneType>> _classZones = new HashMap<>();
	private int _lastDynamicId = 300000;
	private List<L2ItemInstance> _debugItems;
	
	public static final ZoneManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	// =========================================================
	// Data Field
	
	// =========================================================
	// Constructor
	public ZoneManager()
	{
		load();
	}
	
	/**
	 * Reloads zones.
	 */
	public void reload()
	{
		// Get the world regions
		int count = 0;
		final L2WorldRegion[][] worldRegions = L2World.getInstance().getAllWorldRegions();
		
		// Clear zones
		for (L2WorldRegion[] worldRegion : worldRegions)
		{
			for (L2WorldRegion element : worldRegion)
			{
				element.getZones().clear();
				count++;
			}
		}
		
		_log.info(getClass().getSimpleName() + ": Removed zones in " + count + " regions.");
		
		// Cleanup of boss zones
		GrandBossManager.getInstance().getZones().clear();
		
		// Clean debug items since zones are being reloaded
		clearDebugItems();
		
		// Load the zones
		load();
		
		// Re-validate all characters in zones
		for (L2Object obj : L2World.getInstance().getAllVisibleObjects())
		{
			if (obj instanceof L2Character && !((L2Character) obj).isTeleporting())
			{
				((L2Character) obj).revalidateZone(true);
			}
		}
	}
	
	private final void load()
	{
		_log.info("Loading zones...");
		_classZones.clear();
		
		int zoneCount = 0;
		
		// Get the world regions
		L2WorldRegion[][] worldRegions = L2World.getInstance().getAllWorldRegions();
		
		// Load zone xmls
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			
			final File dir = new File(Config.DATAPACK_ROOT + "/data/zones");
			if (!dir.exists())
			{
				_log.warning(getClass().getSimpleName() + ": Directory '" + dir.getAbsolutePath() + "' does not exist!");
				return;
			}
			
			Document doc;
			NamedNodeMap attrs;
			Node attribute;
			String zoneName;
			int zoneId, minZ, maxZ;
			String zoneType, zoneShape;
			
			final File[] files = dir.listFiles();
			for (File f : files)
			{
				if (!f.getName().endsWith(".xml"))
				{
					continue;
				}
				
				doc = factory.newDocumentBuilder().parse(f);
				for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if ("list".equalsIgnoreCase(n.getNodeName()))
					{
						attrs = n.getAttributes();
						attribute = attrs.getNamedItem("enabled");
						if (attribute != null && !Boolean.parseBoolean(attribute.getNodeValue()))
						{
							continue;
						}
						
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							if ("zone".equalsIgnoreCase(d.getNodeName()))
							{
								attrs = d.getAttributes();
								
								attribute = attrs.getNamedItem("id");
								if (attribute != null)
								{
									zoneId = Integer.parseInt(attribute.getNodeValue());
								}
								else
								{
									zoneId = _lastDynamicId++;
								}
								
								attribute = attrs.getNamedItem("name");
								if (attribute != null)
								{
									zoneName = attribute.getNodeValue();
								}
								else
								{
									zoneName = null;
								}
								
								attribute = attrs.getNamedItem("minZ");
								if (attribute != null)
								{
									minZ = Integer.parseInt(attribute.getNodeValue());
								}
								else
								{
									_log.warning(getClass().getSimpleName() + ": Missing minZ for zone: " + zoneId + " in file: " + f.getName());
									continue;
								}
								
								attribute = attrs.getNamedItem("maxZ");
								if (attribute != null)
								{
									maxZ = Integer.parseInt(attribute.getNodeValue());
								}
								else
								{
									_log.warning(getClass().getSimpleName() + ": Missing maxZ for zone: " + zoneId + " in file: " + f.getName());
									continue;
								}
								
								attribute = attrs.getNamedItem("type");
								if (attribute != null)
								{
									zoneType = attribute.getNodeValue();
								}
								else
								{
									_log.warning(getClass().getSimpleName() + ": Missing type for zone: " + zoneId + " in file: " + f.getName());
									continue;
								}
								
								attribute = attrs.getNamedItem("shape");
								if (attribute != null)
								{
									zoneShape = attribute.getNodeValue();
								}
								else
								{
									_log.warning(getClass().getSimpleName() + ": Missing shape for zone: " + zoneId + " in file: " + f.getName());
									continue;
								}
								
								L2ZoneType temp = null;
								try
								{
									Class<?> newZone = Class.forName("net.sf.l2j.gameserver.model.zone.type.L2" + zoneType);
									Constructor<?> zoneConstructor = newZone.getConstructor(int.class);
									temp = (L2ZoneType) zoneConstructor.newInstance(zoneId);
									
									// Check for unknown type
									if (temp == null)
									{
										throw new Exception();
									}
								}
								catch (Exception e)
								{
									_log.log(Level.WARNING, getClass().getSimpleName() + ": No such zone type: " + zoneType + " in file: " + f.getName(), e);
									continue;
								}
								
								// Get the zone shape
								try
								{
									List<int[]> points = new ArrayList<>();
									
									// Loading from XML first
									for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
									{
										if ("node".equalsIgnoreCase(cd.getNodeName()))
										{
											attrs = cd.getAttributes();
											int[] point = new int[2];
											point[0] = Integer.parseInt(attrs.getNamedItem("X").getNodeValue());
											point[1] = Integer.parseInt(attrs.getNamedItem("Y").getNodeValue());
											points.add(point);
										}
									}
									
									int[][] coords = points.toArray(new int[points.size()][]);
									if ((coords == null) || (coords.length == 0))
									{
										_log.warning(getClass().getSimpleName() + ": Missing data for zone: " + zoneId + " in file: " + f.getName());
										continue;
									}
									
									// Create this zone. Parsing for cuboids is a
									// bit different than for other polygons
									// cuboids need exactly 2 points to be defined.
									// Other polygons need at least 3 (one per
									// vertex)
									if (zoneShape.equalsIgnoreCase("Cuboid"))
									{
										if (coords.length == 2)
										{
											temp.setZone(new ZoneCuboid(coords[0][0], coords[1][0], coords[0][1], coords[1][1], minZ, maxZ));
										}
										else
										{
											_log.warning(getClass().getSimpleName() + ": Missing cuboid vertex for zone: " + zoneId + " in file: " + f.getName());
											continue;
										}
									}
									else if (zoneShape.equalsIgnoreCase("NPoly"))
									{
										// nPoly needs to have at least 3 vertices
										if (coords.length > 2)
										{
											final int[] aX = new int[coords.length];
											final int[] aY = new int[coords.length];
											for (int i = 0; i < coords.length; i++)
											{
												aX[i] = coords[i][0];
												aY[i] = coords[i][1];
											}
											temp.setZone(new ZoneNPoly(aX, aY, minZ, maxZ));
										}
										else
										{
											_log.warning(getClass().getSimpleName() + ": Bad data for zone: " + zoneId + " in file: " + f.getName());
											continue;
										}
									}
									else if (zoneShape.equalsIgnoreCase("Cylinder"))
									{
										// A Cylinder zone requires a center point
										// at x,y and a radius
										attrs = d.getAttributes();
										final int zoneRad = Integer.parseInt(attrs.getNamedItem("rad").getNodeValue());
										if ((coords.length == 1) && (zoneRad > 0))
										{
											temp.setZone(new ZoneCylinder(coords[0][0], coords[0][1], minZ, maxZ, zoneRad));
										}
										else
										{
											_log.warning(getClass().getSimpleName() + ": Bad data for zone: " + zoneId + " in file: " + f.getName());
											continue;
										}
									}
									else
									{
										_log.warning(getClass().getSimpleName() + ": Unknown shape: " + zoneShape + " in file: " + f.getName());
										continue;
									}
								}
								catch (Exception e)
								{
									_log.log(Level.WARNING, getClass().getSimpleName() + ": Failed to load zone " + zoneId + " coordinates: " + e.getMessage(), e);
								}
								
								// Check for additional parameters
								for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
								{
									if ("stat".equalsIgnoreCase(cd.getNodeName()))
									{
										attrs = cd.getAttributes();
										String name = attrs.getNamedItem("name").getNodeValue();
										String val = attrs.getNamedItem("val").getNodeValue();
										
										temp.setParameter(name, val);
									}
									else if ("spawn".equalsIgnoreCase(cd.getNodeName()) && temp instanceof L2ZoneSpawn)
									{
										attrs = cd.getAttributes();
										int spawnX = Integer.parseInt(attrs.getNamedItem("X").getNodeValue());
										int spawnY = Integer.parseInt(attrs.getNamedItem("Y").getNodeValue());
										int spawnZ = Integer.parseInt(attrs.getNamedItem("Z").getNodeValue());
										
										Node val = attrs.getNamedItem("isAlternate");
										if (val != null && Boolean.parseBoolean(val.getNodeValue()))
										{
											((L2ZoneSpawn) temp).addAlternateSpawn(spawnX, spawnY, spawnZ);
											continue;
										}
										
										((L2ZoneSpawn) temp).addSpawn(spawnX, spawnY, spawnZ);
									}
								}
								
								if (checkId(zoneId))
								{
									_log.config(getClass().getSimpleName() + ": Caution: Zone (" + zoneId + ") from file: " + f.getName() + " overrides previous definition.");
								}
								
								// Set name if exists
								if (zoneName != null && !zoneName.isEmpty())
								{
									temp.setName(zoneName);
								}
								
								addZone(zoneId, temp);
								
								// Register the zone into any world region it intersects with...
								// currently 11136 test for each zone :>
								int ax, ay, bx, by;
								for (int x = 0; x < worldRegions.length; x++)
								{
									for (int y = 0; y < worldRegions[x].length; y++)
									{
										ax = (x - L2World.OFFSET_X) << L2World.SHIFT_BY;
										bx = ((x + 1) - L2World.OFFSET_X) << L2World.SHIFT_BY;
										ay = (y - L2World.OFFSET_Y) << L2World.SHIFT_BY;
										by = ((y + 1) - L2World.OFFSET_Y) << L2World.SHIFT_BY;
										
										if (temp.getZone().intersectsRectangle(ax, bx, ay, by))
										{
											if (Config.DEBUG)
											{
												_log.info(getClass().getSimpleName() + ": Zone (" + zoneId + ") added to: " + x + " " + y);
											}
											worldRegions[x][y].addZone(temp);
										}
									}
								}
								
								// Add boss zones to grand boss manager
								if (temp instanceof L2BossZone)
								{
									GrandBossManager.getInstance().addZone((L2BossZone) temp);
								}
								
								// Increase the counter
								zoneCount++;
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, getClass().getSimpleName() + ": Error while loading zones.", e);
			return;
		}
		
		_log.info("Loaded " + zoneCount + " zones.");
	}
	
	/**
	 * Adds new zone.
	 * @param <T> the generic type
	 * @param id the id
	 * @param zone the zone
	 */
	@SuppressWarnings("unchecked")
	public <T extends L2ZoneType> void addZone(Integer id, T zone)
	{
		Map<Integer, T> map = (Map<Integer, T>) _classZones.get(zone.getClass());
		if (map == null)
		{
			map = new LinkedHashMap<>();
			map.put(id, zone);
			_classZones.put(zone.getClass(), map);
		}
		else
		{
			map.put(id, zone);
		}
	}
	
	/**
	 * Checks if zone id already exists.
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean checkId(int id)
	{
		for (Map<Integer, ? extends L2ZoneType> map : _classZones.values())
		{
			if (map.containsKey(id))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns all zones registered with the ZoneManager. To minimize iteration processing
	 * retrieve zones from L2WorldRegion for a specific location instead.
	 * @return zones
	 * @see #getAllZones(Class)
	 */
	public Collection<L2ZoneType> getAllZones()
	{
		final List<L2ZoneType> zones = new ArrayList<>();
		for (Map<Integer, ? extends L2ZoneType> map : _classZones.values())
		{
			zones.addAll(map.values());
		}
		return zones;
	}
	
	/**
	 * Returns all zones by class type.
	 * @param <T>
	 * @param zoneType Zone class
	 * @return Collection of zones
	 */
	@SuppressWarnings("unchecked")
	public <T extends L2ZoneType> Collection<T> getAllZones(Class<T> zoneType)
	{
		return (Collection<T>) _classZones.get(zoneType).values();
	}
	
	/**
	 * Get zone by ID.
	 * @param id the id
	 * @return the zone by id
	 * @see #getZoneById(int, Class)
	 */
	public L2ZoneType getZoneById(int id)
	{
		for (Map<Integer, ? extends L2ZoneType> map : _classZones.values())
		{
			if (map.containsKey(id))
			{
				return map.get(id);
			}
		}
		return null;
	}
	
	/**
	 * Get zone by ID and zone class.
	 * @param <T> the generic type
	 * @param id the id
	 * @param zoneType the zone type
	 * @return zone
	 */
	@SuppressWarnings("unchecked")
	public <T extends L2ZoneType> T getZoneById(int id, Class<T> zoneType)
	{
		return (T) _classZones.get(zoneType).get(id);
	}
	
	/**
	 * Returns all zones from where the object is located.
	 * @param object the object
	 * @return zones
	 */
	public List<L2ZoneType> getZones(L2Object object)
	{
		return getZones(object.getX(), object.getY(), object.getZ());
	}
	
	/**
	 * Gets the zone.
	 * @param <T> the generic type
	 * @param object the object
	 * @param type the type
	 * @return zone from where the object is located by type
	 */
	public <T extends L2ZoneType> T getZone(L2Object object, Class<T> type)
	{
		if (object == null)
		{
			return null;
		}
		return getZone(object.getX(), object.getY(), object.getZ(), type);
	}
	
	/**
	 * Returns all zones from given coordinates (plane).
	 * @param x the x
	 * @param y the y
	 * @return zones
	 */
	public List<L2ZoneType> getZones(int x, int y)
	{
		final L2WorldRegion region = L2World.getInstance().getRegion(x, y);
		final List<L2ZoneType> temp = new ArrayList<>();
		for (L2ZoneType zone : region.getZones())
		{
			if (zone.isInsideZone(x, y))
			{
				temp.add(zone);
			}
		}
		return temp;
	}
	
	/**
	 * Returns all zones from given coordinates.
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return zones
	 */
	public List<L2ZoneType> getZones(int x, int y, int z)
	{
		final L2WorldRegion region = L2World.getInstance().getRegion(x, y);
		final List<L2ZoneType> temp = new ArrayList<>();
		for (L2ZoneType zone : region.getZones())
		{
			if (zone.isInsideZone(x, y, z))
			{
				temp.add(zone);
			}
		}
		return temp;
	}
	
	/**
	 * Gets the zone.
	 * @param <T> the generic type
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param type the type
	 * @return zone from given coordinates
	 */
	@SuppressWarnings("unchecked")
	public <T extends L2ZoneType> T getZone(int x, int y, int z, Class<T> type)
	{
		final L2WorldRegion region = L2World.getInstance().getRegion(x, y);
		for (L2ZoneType zone : region.getZones())
		{
			if (zone.isInsideZone(x, y, z) && type.isInstance(zone))
			{
				return (T) zone;
			}
		}
		return null;
	}
	
	/**
	 * Gets the arena.
	 * 
	 * @param character the character
	 * @return the arena
	 */
	public final L2ArenaZone getArena(L2Character character)
	{
		if (character == null)
		{
			return null;
		}
		
		for (L2ZoneType temp : ZoneManager.getInstance().getZones(character.getX(), character.getY(), character.getZ()))
		{
			if ((temp instanceof L2ArenaZone) && temp.isCharacterInZone(character))
			{
				return ((L2ArenaZone) temp);
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the olympiad stadium.
	 * 
	 * @param character the character
	 * @return the olympiad stadium
	 */
	public final L2OlympiadStadiumZone getOlympiadStadium(L2Character character)
	{
		if (character == null)
		{
			return null;
		}
		
		for (L2ZoneType temp : ZoneManager.getInstance().getZones(character.getX(), character.getY(), character.getZ()))
		{
			if ((temp instanceof L2OlympiadStadiumZone) && temp.isCharacterInZone(character))
			{
				return ((L2OlympiadStadiumZone) temp);
			}
		}
		return null;
	}
	
	/**
	 * Gets the olympiad stadium.
	 * 
	 * @param stadiumId
	 * @return the olympiad stadium
	 */
	public final L2OlympiadStadiumZone getOlympiadStadium(int stadiumId)
	{
		for (L2OlympiadStadiumZone temp : ZoneManager.getInstance().getAllZones(L2OlympiadStadiumZone.class))
		{
			if (temp.getStadiumId() == stadiumId)
			{
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * General storage for debug items used for visualizing zones.
	 * @return list of items
	 */
	public List<L2ItemInstance> getDebugItems()
	{
		if (_debugItems == null)
		{
			_debugItems = new ArrayList<>();
		}
		return _debugItems;
	}
	
	/**
	 * Remove all zone debug items from world.
	 */
	public void clearDebugItems()
	{
		if (_debugItems != null && !_debugItems.isEmpty())
		{
			final Iterator<L2ItemInstance> it = _debugItems.iterator();
			while (it.hasNext())
			{
				final L2ItemInstance item = it.next();
				if (item != null)
				{
					item.decayMe();
				}
				it.remove();
			}
		}
	}
	
	private static class SingletonHolder
	{
		protected static final ZoneManager _instance = new ZoneManager();
	}
}