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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.util.Point3D;

public final class L2World
{
	private static final Logger _log = Logger.getLogger(L2World.class.getName());

	/**
	 * Shift by, defines number of regions note, shifting by 15 will result in regions 
	 * corresponding to map tiles shifting by 12 divides one tile to 8x8 regions.
	 */
	public static final int SHIFT_BY = 12;

	public static final int TILE_SIZE = 32768;
	
	/** Map dimensions */
	public static final int TILE_X_MIN = 16;
	public static final int TILE_Y_MIN = 10;
	public static final int TILE_X_MAX = 26;
	public static final int TILE_Y_MAX = 25;
	public static final int TILE_ZERO_COORD_X = 20;
	public static final int TILE_ZERO_COORD_Y = 18;
	public static final int MAP_MIN_X = (TILE_X_MIN - TILE_ZERO_COORD_X) * TILE_SIZE;
	public static final int MAP_MIN_Y = (TILE_Y_MIN - TILE_ZERO_COORD_Y) * TILE_SIZE;
	
	public static final int MAP_MAX_X = ((TILE_X_MAX - TILE_ZERO_COORD_X) + 1) * TILE_SIZE;
	public static final int MAP_MAX_Y = ((TILE_Y_MAX - TILE_ZERO_COORD_Y) + 1) * TILE_SIZE;
	
	/** Calculated offset used so top left region is 0,0 */
	public static final int OFFSET_X = Math.abs(MAP_MIN_X >> SHIFT_BY);
	public static final int OFFSET_Y = Math.abs(MAP_MIN_Y >> SHIFT_BY);
	
	/** Number of regions */
	private static final int REGIONS_X = (MAP_MAX_X >> SHIFT_BY) + OFFSET_X;
	private static final int REGIONS_Y = (MAP_MAX_Y >> SHIFT_BY) + OFFSET_Y;

	/** A map containing all the players in game */
	private final Map<Integer, L2PcInstance> _allPlayers;

	/** A map containing all visible objects */
	private final Map<Integer, L2Object> _allObjects;

	/** A map with the pets instances and their owner id */
	private final Map<Integer, L2PetInstance> _allPets;

	private L2WorldRegion[][] _worldRegions;

	/**
	 * Constructor of L2World.<BR>
	 * <BR>
	 */
	private L2World()
	{
		_allPlayers = new ConcurrentHashMap<>();
		_allPets = new ConcurrentHashMap<>();
		_allObjects = new ConcurrentHashMap<>();

		initRegions();
	}

	/**
	 * Return the current instance of L2World.<BR>
	 * <BR>
	 * @return 
	 */
	public static L2World getInstance()
	{
		return SingletonHolder._instance;
	}

	/**
	 * Add L2Object object in _allObjects.<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Withdraw an item from the warehouse, create an item</li>
	 * <li>Spawn a L2Character (PC, NPC, Pet)</li><BR>
	 * @param object 
	 */
	public void storeObject(L2Object object)
	{
		if (_allObjects.containsKey(object.getObjectId()))
		{
			if (Config.DEBUG)
			{
				_log.warning("[L2World] objectId " + object.getObjectId() + " already exist in OID map!");
			}
			return;
		}

		_allObjects.put(object.getObjectId(), object);
	}

	/**
	 * Remove L2Object object from _allObjects of L2World.<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Delete item from inventory, transfer Item from inventory to warehouse</li>
	 * <li>Crystallize item</li>
	 * <li>Remove NPC/PC/Pet from the world</li><BR>
	 * @param object L2Object to remove from _allObjects of L2World
	 */
	public void removeObject(L2Object object)
	{
		_allObjects.remove(object.getObjectId());
	}

	/**
	 * Return the L2Object object that belongs to an ID or null if no object found.<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Client packets : Action, AttackRequest, RequestJoinParty, RequestJoinPledge...</li><BR>
	 * @param objectId Identifier of the L2Object
	 * @return 
	 */
	public L2Object findObject(int objectId)
	{
		return _allObjects.get(objectId);
	}

	public Collection<L2Object> getAllVisibleObjects()
	{
		return _allObjects.values();
	}

	/**
	 * Get the count of all visible objects in world.<br>
	 * <br>
	 * @return count off all L2World objects
	 */
	public final int getAllVisibleObjectsCount()
	{
		return _allObjects.size();
	}

	/**
	 * Return a table containing all GMs.<BR>
	 * <BR>
	 * @return 
	 */
	public List<L2PcInstance> getAllGMs()
	{
		return GmListTable.getInstance().getAllGms(true);
	}

	/**
	 * Return a collection containing all players in game.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : Read-only, please! </B></FONT><BR>
	 * <BR>
	 * @return 
	 */
	public Collection<L2PcInstance> getAllPlayers()
	{
		return _allPlayers.values();
	}

	/**
	 * Return how many players are online.<BR>
	 * <BR>
	 * @return number of online players.
	 */
	public int getAllPlayersCount()
	{
		return _allPlayers.size();
	}

	/**
	 * Return the player instance corresponding to the given name.<BR>
	 * <BR>
	 * @param name Name of the player to get Instance
	 * @return 
	 */
	public L2PcInstance getPlayer(String name)
	{
		return getPlayer(CharNameTable.getInstance().getIdByName(name));
	}
	
	/**
	 * Return the player instance corresponding to the given object ID.<BR>
	 * <BR>
	 * @param objectId Object ID of the player to get Instance
	 * @return 
	 */
	public L2PcInstance getPlayer(int objectId)
	{
		return _allPlayers.get(objectId);
	}

	/**
	 * Add the L2PcInstance to _allPlayers of L2World.<BR>
	 * <BR>
	 * @param cha 
	 */
	public void addToAllPlayers(L2PcInstance cha)
	{
		_allPlayers.put(cha.getObjectId(), cha);
	}

	/**
	 * Remove the L2PcInstance from _allPlayers of L2World.<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Remove a player from the visible objects</li><BR>
	 * @param cha 
	 */
	public void removeFromAllPlayers(L2PcInstance cha)
	{
		_allPlayers.remove(cha.getObjectId());
	}
	
	/**
	 * Return a collection containing all pets in game.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : Read-only, please! </B></FONT><BR>
	 * <BR>
	 * @return 
	 */
	public Collection<L2PetInstance> getAllPets()
	{
		return _allPets.values();
	}

	/**
	 * Return the pet instance from the given ownerId.<BR>
	 * <BR>
	 * @param ownerId ID of the owner
	 * @return 
	 */
	public L2PetInstance getPet(int ownerId)
	{
		return _allPets.get(ownerId);
	}

	/**
	 * Add the given pet instance from the given ownerId.<BR>
	 * <BR>
	 * @param ownerId ID of the owner
	 * @param pet L2PetInstance of the pet
	 * @return 
	 */
	public L2PetInstance addPet(int ownerId, L2PetInstance pet)
	{
		return _allPets.put(ownerId, pet);
	}

	/**
	 * Remove the given pet instance.<BR>
	 * <BR>
	 * @param ownerId ID of the owner
	 */
	public void removePet(int ownerId)
	{
		_allPets.remove(ownerId);
	}

	/**
	 * Remove the given pet instance.<BR>
	 * <BR>
	 * @param pet the pet to remove
	 */
	public void removePet(L2PetInstance pet)
	{
		_allPets.remove(pet.getObjectId());
	}

	/**
	 * Add a L2Object in the world.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * L2Object (including L2PcInstance) are identified in <B>_visibleObjects</B> of his current L2WorldRegion and in <B>_knownObjects</B> of other surrounding L2Characters <BR>
	 * L2PcInstance are identified in <B>_allPlayers</B> of L2World, in <B>_allPlayers</B> of his current L2WorldRegion and in <B>_knownPlayer</B> of other surrounding L2Characters <BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Add the L2Object object in _allPlayers* of L2World</li>
	 * <li>Add the L2Object object in _gmList** of GmListTable</li>
	 * <li>Add object in _knownObjects and _knownPlayer* of all surrounding L2WorldRegion L2Characters</li><BR>
	 * <li>If object is a L2Character, add all surrounding L2Object in its _knownObjects and all surrounding L2PcInstance in its _knownPlayer</li><BR>
	 * <I>* only if object is a L2PcInstance</I><BR>
	 * <I>** only if object is a GM L2PcInstance</I><BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T ADD the object in _visibleObjects and _allPlayers* of L2WorldRegion (need synchronization)</B></FONT><BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T ADD the object to _allObjects and _allPlayers* of L2World (need synchronization)</B></FONT><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Drop an Item</li>
	 * <li>Spawn a L2Character</li>
	 * <li>Apply Death Penalty of a L2PcInstance</li><BR>
	 * <BR>
	 * @param object L2object to add in the world
	 * @param newRegion L2WorldRegion in which the object will be add (not used)
	 */
	public void addVisibleObject(L2Object object, L2WorldRegion newRegion)
	{
		// If selected L2Object is a L2PcIntance, add it in L2ObjectHashSet(L2PcInstance) _allPlayers of L2World
		// XXX TODO: this code should be obsoleted by protection in putObject func...
		if (object instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) object;

			if (!player.isTeleporting())
			{
				L2PcInstance tmp = _allPlayers.get(Integer.valueOf(player.getObjectId()));
				if (tmp != null)
				{
					_log.warning("Duplicate character!? Closing both characters (" + player.getName() + ")");
					player.logout();
					tmp.logout();
					return;
				}
				_allPlayers.put(player.getObjectId(), player);
			}
		}

		if (!newRegion.isActive())
		{
			return;
		}

		// Get all visible objects contained in the _visibleObjects of L2WorldRegions
		// in a circular area of 2000 units
		List<L2Object> visibles = getVisibleObjects(object, 2000);
		if (Config.DEBUG)
		{
			_log.finest("objects in range:" + visibles.size());
		}

		// tell the player about the surroundings
		// Go through the visible objects contained in the circular area
		for (L2Object visible : visibles)
		{
			if (visible == null)
			{
				continue;
			}

			// Add the object in L2ObjectHashSet(L2Object) _knownObjects of the visible L2Character according to conditions :
			// - L2Character is visible
			// - object is not already known
			// - object is in the watch distance
			// If L2Object is a L2PcInstance, add L2Object in L2ObjectHashSet(L2PcInstance) _knownPlayer of the visible L2Character
			visible.getKnownList().addKnownObject(object);

			// Add the visible L2Object in L2ObjectHashSet(L2Object) _knownObjects of the object according to conditions
			// If visible L2Object is a L2PcInstance, add visible L2Object in L2ObjectHashSet(L2PcInstance) _knownPlayer of the object
			object.getKnownList().addKnownObject(visible);
		}
	}

	/**
	 * Remove a L2Object from the world.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * L2Object (including L2PcInstance) are identified in <B>_visibleObjects</B> of his current L2WorldRegion and in <B>_knownObjects</B> of other surrounding L2Characters <BR>
	 * L2PcInstance are identified in <B>_allPlayers</B> of L2World, in <B>_allPlayers</B> of his current L2WorldRegion and in <B>_knownPlayer</B> of other surrounding L2Characters <BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Remove the L2Object object from _allPlayers* of L2World</li>
	 * <li>Remove the L2Object object from _visibleObjects and _allPlayers* of L2WorldRegion</li>
	 * <li>Remove the L2Object object from _gmList** of GmListTable</li>
	 * <li>Remove object from _knownObjects and _knownPlayer* of all surrounding L2WorldRegion L2Characters</li><BR>
	 * <li>If object is a L2Character, remove all L2Object from its _knownObjects and all L2PcInstance from its _knownPlayer</li><BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T REMOVE the object from _allObjects of L2World</B></FONT><BR>
	 * <BR>
	 * <I>* only if object is a L2PcInstance</I><BR>
	 * <I>** only if object is a GM L2PcInstance</I><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Pickup an Item</li>
	 * <li>Decay a L2Character</li><BR>
	 * <BR>
	 * @param object L2object to remove from the world
	 * @param oldRegion L2WorldRegion in which the object was before removing
	 */
	public void removeVisibleObject(L2Object object, L2WorldRegion oldRegion)
	{
		if (object == null)
		{
			return;
		}

		if (oldRegion != null)
		{
			// Remove the object from the L2ObjectHashSet(L2Object) _visibleObjects of L2WorldRegion
			// If object is a L2PcInstance, remove it from the L2ObjectHashSet(L2PcInstance) _allPlayers of this L2WorldRegion
			oldRegion.removeVisibleObject(object);

			// Go through all surrounding L2WorldRegion L2Characters
			for (L2WorldRegion reg : oldRegion.getSurroundingRegions())
			{
				final Collection<L2Object> visibleObjects = reg.getVisibleObjects().values();
				for (L2Object obj : visibleObjects)
				{
					// Remove the L2Object from the L2ObjectHashSet(L2Object) _knownObjects of the surrounding L2WorldRegion L2Characters
					// If object is a L2PcInstance, remove the L2Object from the L2ObjectHashSet(L2PcInstance) _knownPlayer of the surrounding L2WorldRegion L2Characters
					// If object is targeted by one of the surrounding L2WorldRegion L2Characters, cancel ATTACK and cast
					if (obj.getKnownList() != null)
					{
						obj.getKnownList().removeKnownObject(object);
					}

					// Remove surrounding L2WorldRegion L2Characters from the L2ObjectHashSet(L2Object) _KnownObjects of object
					// If surrounding L2WorldRegion L2Characters is a L2PcInstance, remove it from the L2ObjectHashSet(L2PcInstance) _knownPlayer of object
					// TODO Delete this line if all the stuff is done by the next line object.removeAllKnownObjects()
					if (object.getKnownList() != null)
					{
						object.getKnownList().removeKnownObject(obj);
					}
				}
			}

			// If object is a L2Character :
			// Remove all L2Object from L2ObjectHashSet(L2Object) containing all L2Object detected by the L2Character
			// Remove all L2PcInstance from L2ObjectHashSet(L2PcInstance) containing all player ingame detected by the L2Character
			object.getKnownList().removeAllKnownObjects();

			// If selected L2Object is a L2PcIntance, remove it from L2ObjectHashSet(L2PcInstance) _allPlayers of L2World
			if (object instanceof L2PcInstance)
			{
				if (!((L2PcInstance) object).isTeleporting())
				{
					removeFromAllPlayers((L2PcInstance) object);
				}
			}
		}
	}

	/**
	 * Return all visible objects of the L2WorldRegion object's and of its surrounding L2WorldRegion.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All visible object are identified in <B>_visibleObjects</B> of their current L2WorldRegion <BR>
	 * All surrounding L2WorldRegion are identified in <B>_surroundingRegions</B> of the selected L2WorldRegion in order to scan a large area around a L2Object<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Find Close Objects for L2Character</li><BR>
	 * @param object L2object that determine the current L2WorldRegion
	 * @return 
	 */
	public List<L2Object> getVisibleObjects(L2Object object)
	{
		L2WorldRegion reg = object.getWorldRegion();
		if (reg == null)
		{
			return null;
		}

		// Create a List in order to contain all visible L2Object
		List<L2Object> result = new LinkedList<>();

		// Go through the List of region
		for (L2WorldRegion regi : reg.getSurroundingRegions())
		{
			// Go through visible objects of the selected region
			for (L2Object obj : regi.getVisibleObjects().values())
			{
				if (obj == null || obj.equals(object))
				{
					continue; // skip our own character
				}
				if (!obj.isVisible())
				{
					continue; // skip dying objects
				}

				result.add(obj);
			}
		}

		return result;
	}

	/**
	 * Return all visible objects of the L2WorldRegions in the circular area (radius) centered on the object.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All visible object are identified in <B>_visibleObjects</B> of their current L2WorldRegion <BR>
	 * All surrounding L2WorldRegion are identified in <B>_surroundingRegions</B> of the selected L2WorldRegion in order to scan a large area around a L2Object<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Define the aggro-list of monster</li>
	 * <li>Define visible objects of a L2Object</li>
	 * <li>Skill : Confusion...</li><BR>
	 * @param object L2object that determine the center of the circular area
	 * @param radius Radius of the circular area
	 * @return 
	 */
	public List<L2Object> getVisibleObjects(L2Object object, int radius)
	{
		if ((object == null) || !object.isVisible())
		{
			return new LinkedList<>();
		}

		int x = object.getX();
		int y = object.getY();
		int sqRadius = radius * radius;

		// Create a List in order to contain all visible L2Object
		List<L2Object> result = new LinkedList<>();

		// Go through the List of region
		for (L2WorldRegion regi : object.getWorldRegion().getSurroundingRegions())
		{
			// Go through visible objects of the selected region
			for (L2Object obj : regi.getVisibleObjects().values())
			{
				if (obj == null || obj.equals(object))
				{
					continue; // skip null or our own character
				}

				int x1 = obj.getX();
				int y1 = obj.getY();

				double dx = x1 - x;
				// if (dx > radius || -dx > radius)
				// continue;
				double dy = y1 - y;
				// if (dy > radius || -dy > radius)
				// continue;

				// If the visible object is inside the circular area
				// add the object to the List result
				if (((dx * dx) + (dy * dy)) < sqRadius)
				{
					result.add(obj);
				}
			}
		}

		return result;
	}

	/**
	 * Return all visible objects of the L2WorldRegions in the spherical area (radius) centered on the object.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All visible object are identified in <B>_visibleObjects</B> of their current L2WorldRegion <BR>
	 * All surrounding L2WorldRegion are identified in <B>_surroundingRegions</B> of the selected L2WorldRegion in order to scan a large area around a L2Object<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Define the target list of a skill</li>
	 * <li>Define the target list of a pole arm attack</li><BR>
	 * <BR>
	 * @param object L2object that determine the center of the circular area
	 * @param radius Radius of the spherical area
	 * @return 
	 */
	public List<L2Object> getVisibleObjects3D(L2Object object, int radius)
	{
		if (object == null || !object.isVisible())
		{
			return new LinkedList<>();
		}

		int x = object.getX();
		int y = object.getY();
		int z = object.getZ();
		int sqRadius = radius * radius;

		// Create a List in order to contain all visible L2Object
		List<L2Object> result = new LinkedList<>();

		// Go through visible object of the selected region
		for (L2WorldRegion regi : object.getWorldRegion().getSurroundingRegions())
		{
			for (L2Object obj : regi.getVisibleObjects().values())
			{
				if (obj.equals(object))
				{
					continue; // skip our own character
				}

				int x1 = obj.getX();
				int y1 = obj.getY();
				int z1 = obj.getZ();

				long dx = x1 - x;
				// if (dx > radius || -dx > radius)
				// continue;
				long dy = y1 - y;
				// if (dy > radius || -dy > radius)
				// continue;
				long dz = z1 - z;

				if (((dx * dx) + (dy * dy) + (dz * dz)) < sqRadius)
				{
					result.add(obj);
				}
			}
		}

		return result;
	}

	/**
	 * Return all visible players of the L2WorldRegion object's and of its surrounding L2WorldRegion.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All visible object are identified in <B>_visibleObjects</B> of their current L2WorldRegion <BR>
	 * All surrounding L2WorldRegion are identified in <B>_surroundingRegions</B> of the selected L2WorldRegion in order to scan a large area around a L2Object<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Find Close Objects for L2Character</li><BR>
	 * @param object L2object that determine the current L2WorldRegion
	 * @return 
	 */
	public List<L2PlayableInstance> getVisiblePlayable(L2Object object)
	{
		L2WorldRegion reg = object.getWorldRegion();
		if (reg == null)
		{
			return null;
		}

		// Create a List in order to contain all visible L2Object
		List<L2PlayableInstance> result = new LinkedList<>();

		// Go through the List of region
		for (L2WorldRegion regi : reg.getSurroundingRegions())
		{
			// Go through visible playables of the selected region
			for (L2PlayableInstance obj : regi.getVisiblePlayable().values())
			{
				if (obj == null)
				{
					continue;
				}

				if (obj.equals(object))
				{
					continue; // skip our own character
				}

				if (!obj.isVisible())
				{
					continue; // skip dying objects
				}

				result.add(obj);
			}
		}

		return result;
	}

	/**
	 * Calculate the current L2WorldRegions of the object according to its position (x,y).<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Set position of a new L2Object (drop, spawn...)</li>
	 * <li>Update position of a L2Object after a movement</li><BR>
	 * @param point point position of the object
	 * @return 
	 */
	public L2WorldRegion getRegion(Point3D point)
	{
		return _worldRegions[(point.getX() >> SHIFT_BY) + OFFSET_X][(point.getY() >> SHIFT_BY) + OFFSET_Y];
	}

	public L2WorldRegion getRegion(int x, int y)
	{
		return _worldRegions[(x >> SHIFT_BY) + OFFSET_X][(y >> SHIFT_BY) + OFFSET_Y];
	}

	public L2WorldRegion[][] getAllWorldRegions()
	{
		return _worldRegions;
	}

	/**
	 * Check if the current L2WorldRegions of the object is valid according to its position (x,y).<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Init L2WorldRegions</li><BR>
	 * @param x X position of the object
	 * @param y Y position of the object
	 * @return True if the L2WorldRegion is valid
	 */
	private boolean validRegion(int x, int y)
	{
		return ((x >= 0) && (x <= REGIONS_X) && (y >= 0) && (y <= REGIONS_Y));
	}

	/**
	 * Init each L2WorldRegion and their surrounding table.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All surrounding L2WorldRegion are identified in <B>_surroundingRegions</B> of the selected L2WorldRegion in order to scan a large area around a L2Object<BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Constructor of L2World</li><BR>
	 */
	private void initRegions()
	{
		_log.config("L2World: Setting up World Regions");

		_worldRegions = new L2WorldRegion[REGIONS_X + 1][REGIONS_Y + 1];

		for (int i = 0; i <= REGIONS_X; i++)
		{
			for (int j = 0; j <= REGIONS_Y; j++)
			{
				_worldRegions[i][j] = new L2WorldRegion(i, j);
			}
		}

		for (int x = 0; x <= REGIONS_X; x++)
		{
			for (int y = 0; y <= REGIONS_Y; y++)
			{
				for (int a = -1; a <= 1; a++)
				{
					for (int b = -1; b <= 1; b++)
					{
						if (validRegion(x + a, y + b))
						{
							_worldRegions[x + a][y + b].addSurroundingRegion(_worldRegions[x][y]);
						}
					}
				}
			}
		}

		_log.config("L2World: (" + REGIONS_X + " by " + REGIONS_Y + ") World Region Grid set up.");

	}

	/**
	 * Deleted all spawns in the world.
	 */
	public void deleteVisibleNpcSpawns()
	{
		_log.info("Deleting all visible NPC's.");
		for (int i = 0; i <= REGIONS_X; i++)
		{
			for (int j = 0; j <= REGIONS_Y; j++)
			{
				_worldRegions[i][j].deleteVisibleNpcSpawns();
			}
		}
		_log.info("All visible NPC's deleted.");
	}
	
	private static class SingletonHolder
	{
		protected static final L2World _instance = new L2World();
	}
}