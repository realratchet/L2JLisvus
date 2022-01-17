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
package net.sf.l2j.gameserver.geoengine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.geoengine.geodriver.Cell;
import net.sf.l2j.gameserver.geoengine.geodriver.GeoDriver;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.util.GeoUtils;
import net.sf.l2j.gameserver.util.LinePointIterator;
import net.sf.l2j.gameserver.util.LinePointIterator3D;

/**
 * @author -Nemesiss-, HorridoJoho
 */
public class GeoData
{
	private static final Logger LOGGER = Logger.getLogger(GeoData.class.getName());
	private static final String FILE_NAME_FORMAT = "%d_%d.l2j";
	private static final int ELEVATED_SEE_OVER_DISTANCE = 2;
	private static final int MAX_SEE_OVER_HEIGHT = 48;
	private static final int SPAWN_Z_DELTA_LIMIT = 100;
	
	private final GeoDriver _driver = new GeoDriver();

    public static GeoData getInstance()
    {
        return SingletonHolder._instance;
    }
	
	protected GeoData()
	{
		int loadedRegions = 0;
		try
		{
			for (int regionX = L2World.TILE_X_MIN; regionX <= L2World.TILE_X_MAX; regionX++)
			{
				for (int regionY = L2World.TILE_Y_MIN; regionY <= L2World.TILE_Y_MAX; regionY++)
				{
					final Path geoFilePath = Config.GEODATA_PATH.resolve(String.format(FILE_NAME_FORMAT, regionX, regionY));
					final Boolean loadFile = Config.GEODATA_REGIONS.get(regionX + "_" + regionY);
					if (loadFile != null)
					{
						if (loadFile)
						{
							LOGGER.info(getClass().getSimpleName() + ": Loading " + geoFilePath.getFileName() + "...");
							_driver.loadRegion(geoFilePath, regionX, regionY);
							loadedRegions++;
						}
					}
					else if (Config.TRY_LOAD_UNSPECIFIED_REGIONS && Files.exists(geoFilePath))
					{
						try
						{
							LOGGER.info(getClass().getSimpleName() + ": Loading " + geoFilePath.getFileName() + "...");
							_driver.loadRegion(geoFilePath, regionX, regionY);
							loadedRegions++;
						}
						catch (Exception e)
						{
							LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Failed to load " + geoFilePath.getFileName() + "!", e);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.SEVERE, getClass().getSimpleName() + ": Failed to load geodata!", e);
			System.exit(1);
		}
		
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + loadedRegions + " regions.");
	}
	
	public boolean hasGeoPos(int geoX, int geoY)
	{
		return _driver.hasGeoPos(geoX, geoY);
	}
	
	public boolean checkNearestNswe(int geoX, int geoY, int worldZ, int nswe)
	{
		return _driver.checkNearestNswe(geoX, geoY, worldZ, nswe);
	}
	
	public boolean checkNearestNsweAntiCornerCut(int geoX, int geoY, int worldZ, int nswe)
	{
		boolean can = true;
		if ((nswe & Cell.NSWE_NORTH_EAST) == Cell.NSWE_NORTH_EAST)
		{
			// can = canEnterNeighbors(prevX, prevY - 1, prevGeoZ, Direction.EAST) && canEnterNeighbors(prevX + 1, prevY, prevGeoZ, Direction.NORTH);
			can = checkNearestNswe(geoX, geoY - 1, worldZ, Cell.NSWE_EAST) && checkNearestNswe(geoX + 1, geoY, worldZ, Cell.NSWE_NORTH);
		}
		
		if (can && ((nswe & Cell.NSWE_NORTH_WEST) == Cell.NSWE_NORTH_WEST))
		{
			// can = canEnterNeighbors(prevX, prevY - 1, prevGeoZ, Direction.WEST) && canEnterNeighbors(prevX - 1, prevY, prevGeoZ, Direction.NORTH);
			can = checkNearestNswe(geoX, geoY - 1, worldZ, Cell.NSWE_WEST) && checkNearestNswe(geoX, geoY - 1, worldZ, Cell.NSWE_NORTH);
		}
		
		if (can && ((nswe & Cell.NSWE_SOUTH_EAST) == Cell.NSWE_SOUTH_EAST))
		{
			// can = canEnterNeighbors(prevX, prevY + 1, prevGeoZ, Direction.EAST) && canEnterNeighbors(prevX + 1, prevY, prevGeoZ, Direction.SOUTH);
			can = checkNearestNswe(geoX, geoY + 1, worldZ, Cell.NSWE_EAST) && checkNearestNswe(geoX + 1, geoY, worldZ, Cell.NSWE_SOUTH);
		}
		
		if (can && ((nswe & Cell.NSWE_SOUTH_WEST) == Cell.NSWE_SOUTH_WEST))
		{
			// can = canEnterNeighbors(prevX, prevY + 1, prevGeoZ, Direction.WEST) && canEnterNeighbors(prevX - 1, prevY, prevGeoZ, Direction.SOUTH);
			can = checkNearestNswe(geoX, geoY + 1, worldZ, Cell.NSWE_WEST) && checkNearestNswe(geoX - 1, geoY, worldZ, Cell.NSWE_SOUTH);
		}
		
		return can && checkNearestNswe(geoX, geoY, worldZ, nswe);
	}
	
	public int getNearestZ(int geoX, int geoY, int worldZ)
	{
		return _driver.getNearestZ(geoX, geoY, worldZ);
	}
	
	public int getNextLowerZ(int geoX, int geoY, int worldZ)
	{
		return _driver.getNextLowerZ(geoX, geoY, worldZ);
	}
	
	public int getNextHigherZ(int geoX, int geoY, int worldZ)
	{
		return _driver.getNextHigherZ(geoX, geoY, worldZ);
	}
	
	public int getGeoX(int worldX)
	{
		return _driver.getGeoX(worldX);
	}
	
	public int getGeoY(int worldY)
	{
		return _driver.getGeoY(worldY);
	}
	
	public int getWorldX(int geoX)
	{
		return _driver.getWorldX(geoX);
	}
	
	public int getWorldY(int geoY)
	{
		return _driver.getWorldY(geoY);
	}
	
	// ///////////////////
	// L2J METHODS
	/**
	 * Gets the height.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return the height
	 */
	public int getHeight(int x, int y, int z)
	{
		return getNearestZ(getGeoX(x), getGeoY(y), z);
	}
	
	/**
	 * Gets the spawn height.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the the z coordinate
	 * @return the spawn height
	 */
	public int getSpawnHeight(int x, int y, int z)
	{
		final int geoX = getGeoX(x);
		final int geoY = getGeoY(y);

		if (!hasGeoPos(geoX, geoY))
		{
			return z;
		}

		int nextLowerZ = getNextLowerZ(geoX, geoY, z + 20);
		return Math.abs(nextLowerZ - z) <= SPAWN_Z_DELTA_LIMIT ? nextLowerZ : z;
	}
	
	/**
	 * Gets the spawn height.
	 * @param location the location
	 * @return the spawn height
	 */
	public int getSpawnHeight(Location location)
	{
		return getSpawnHeight(location.getX(), location.getY(), location.getZ());
	}
	
	/**
	 * Can see target. Doors as target always return true. Checks doors between.
	 * @param cha the character
	 * @param target the target
	 * @return {@code true} if the character can see the target (LOS), {@code false} otherwise
	 */
	public boolean canSeeTarget(L2Object cha, L2Object target)
	{
		if (target == null)
		{
			return false;
		}
		
		if (target instanceof L2DoorInstance)
		{
			// can always see doors :o
			return true;
		}
		
		if (DoorTable.getInstance().checkIfDoorsBetween(cha.getX(), cha.getY(), cha.getZ(), target.getX(), target.getY(), target.getZ()))
		{
			return false;
		}
		
		return canSeeTarget(cha.getX(), cha.getY(), cha.getZ(), target.getX(), target.getY(), target.getZ());
	}
	
	private int getLosGeoZ(int prevX, int prevY, int prevGeoZ, int curX, int curY, int nswe)
	{
		if ((((nswe & Cell.NSWE_NORTH) != 0) && ((nswe & Cell.NSWE_SOUTH) != 0)) || (((nswe & Cell.NSWE_WEST) != 0) && ((nswe & Cell.NSWE_EAST) != 0)))
		{
			throw new RuntimeException("Multiple directions!");
		}
		
		if (checkNearestNsweAntiCornerCut(prevX, prevY, prevGeoZ, nswe))
		{
			return getNearestZ(curX, curY, prevGeoZ);
		}
		return getNextHigherZ(curX, curY, prevGeoZ);
	}
	
	/**
	 * Can see target. Does not check doors between.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param tx the target's x coordinate
	 * @param ty the target's y coordinate
	 * @param tz the target's z coordinate
	 * @return {@code true} if there is line of sight between the given coordinate sets, {@code false} otherwise
	 */
	public boolean canSeeTarget(int x, int y, int z, int tx, int ty, int tz)
	{
		int geoX = getGeoX(x);
		int geoY = getGeoY(y);
		int tGeoX = getGeoX(tx);
		int tGeoY = getGeoY(ty);
		
		z = getNearestZ(geoX, geoY, z);
		tz = getNearestZ(tGeoX, tGeoY, tz);
		
		// Fastpath
		if ((geoX == tGeoX) && (geoY == tGeoY))
		{
			if (hasGeoPos(tGeoX, tGeoY))
			{
				return z == tz;
			}
			
			return true;
		}
		
		if (tz > z)
		{
			int tmp = tx;
			tx = x;
			x = tmp;
			
			tmp = ty;
			ty = y;
			y = tmp;
			
			tmp = tz;
			tz = z;
			z = tmp;
			
			tmp = tGeoX;
			tGeoX = geoX;
			geoX = tmp;
			
			tmp = tGeoY;
			tGeoY = geoY;
			geoY = tmp;
		}
		
		LinePointIterator3D pointIter = new LinePointIterator3D(geoX, geoY, z, tGeoX, tGeoY, tz);
		// first point is guaranteed to be available, skip it, we can always see our own position
		pointIter.next();
		int prevX = pointIter.x();
		int prevY = pointIter.y();
		int prevZ = pointIter.z();
		int prevGeoZ = prevZ;
		int ptIndex = 0;

		while (pointIter.next())
		{
			int curX = pointIter.x();
			int curY = pointIter.y();
			
			if ((curX == prevX) && (curY == prevY))
			{
				continue;
			}
			
			int beeCurZ = pointIter.z();
			int curGeoZ = prevGeoZ;
			
			// check if the position has geodata
			if (hasGeoPos(curX, curY))
			{
				int nswe = GeoUtils.computeNswe(prevX, prevY, curX, curY);
				curGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, curX, curY, nswe);
				int maxHeight;
				if (ptIndex < ELEVATED_SEE_OVER_DISTANCE)
				{
					maxHeight = z + MAX_SEE_OVER_HEIGHT;
				}
				else
				{
					maxHeight = beeCurZ + MAX_SEE_OVER_HEIGHT;
				}
				
				boolean canSeeThrough = false;
				if (curGeoZ <= maxHeight)
				{
					if ((nswe & Cell.NSWE_NORTH_EAST) == Cell.NSWE_NORTH_EAST)
					{
						int northGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, prevX, prevY - 1, Cell.NSWE_EAST);
						int eastGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, prevX + 1, prevY, Cell.NSWE_NORTH);
						canSeeThrough = (northGeoZ <= maxHeight) && (eastGeoZ <= maxHeight) && (northGeoZ <= getNearestZ(prevX, prevY - 1, beeCurZ)) && (eastGeoZ <= getNearestZ(prevX + 1, prevY, beeCurZ));
					}
					else if ((nswe & Cell.NSWE_NORTH_WEST) == Cell.NSWE_NORTH_WEST)
					{
						int northGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, prevX, prevY - 1, Cell.NSWE_WEST);
						int westGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, prevX - 1, prevY, Cell.NSWE_NORTH);
						canSeeThrough = (northGeoZ <= maxHeight) && (westGeoZ <= maxHeight) && (northGeoZ <= getNearestZ(prevX, prevY - 1, beeCurZ)) && (westGeoZ <= getNearestZ(prevX - 1, prevY, beeCurZ));
					}
					else if ((nswe & Cell.NSWE_SOUTH_EAST) == Cell.NSWE_SOUTH_EAST)
					{
						int southGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, prevX, prevY + 1, Cell.NSWE_EAST);
						int eastGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, prevX + 1, prevY, Cell.NSWE_SOUTH);
						canSeeThrough = (southGeoZ <= maxHeight) && (eastGeoZ <= maxHeight) && (southGeoZ <= getNearestZ(prevX, prevY + 1, beeCurZ)) && (eastGeoZ <= getNearestZ(prevX + 1, prevY, beeCurZ));
					}
					else if ((nswe & Cell.NSWE_SOUTH_WEST) == Cell.NSWE_SOUTH_WEST)
					{
						int southGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, prevX, prevY + 1, Cell.NSWE_WEST);
						int westGeoZ = getLosGeoZ(prevX, prevY, prevGeoZ, prevX - 1, prevY, Cell.NSWE_SOUTH);
						canSeeThrough = (southGeoZ <= maxHeight) && (westGeoZ <= maxHeight) && (southGeoZ <= getNearestZ(prevX, prevY + 1, beeCurZ)) && (westGeoZ <= getNearestZ(prevX - 1, prevY, beeCurZ));
					}
					else
					{
						canSeeThrough = true;
					}
				}
				
				if (!canSeeThrough)
				{
					return false;
				}
			}
			
			prevX = curX;
			prevY = curY;
			prevGeoZ = curGeoZ;
			++ptIndex;
		}
		
		return true;
	}
	
	/**
	 * Move check.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param tx the target's x coordinate
	 * @param ty the target's y coordinate
	 * @param tz the target's z coordinate
	 * @return the last Location (x,y,z) where player can walk - just before wall
	 */
	public Location moveCheck(int x, int y, int z, int tx, int ty, int tz)
	{
		int geoX = getGeoX(x);
		int geoY = getGeoY(y);
		z = getNearestZ(geoX, geoY, z);
		int tGeoX = getGeoX(tx);
		int tGeoY = getGeoY(ty);
		tz = getNearestZ(tGeoX, tGeoY, tz);
		
		if (DoorTable.getInstance().checkIfDoorsBetween(x, y, z, tx, ty, tz))
		{
			return new Location(x, y, getHeight(x, y, z));
		}
		
		LinePointIterator pointIter = new LinePointIterator(geoX, geoY, tGeoX, tGeoY);
		// first point is guaranteed to be available
		pointIter.next();
		int prevX = pointIter.x();
		int prevY = pointIter.y();
		int prevZ = z;
		
		while (pointIter.next())
		{
			int curX = pointIter.x();
			int curY = pointIter.y();
			int curZ = getNearestZ(curX, curY, prevZ);
			
			if (hasGeoPos(prevX, prevY))
			{
				int nswe = GeoUtils.computeNswe(prevX, prevY, curX, curY);
				if (!checkNearestNsweAntiCornerCut(prevX, prevY, prevZ, nswe))
				{
					// can't move, return previous location
					return new Location(getWorldX(prevX), getWorldY(prevY), prevZ);
				}
			}
			
			prevX = curX;
			prevY = curY;
			prevZ = curZ;
		}
		
		if (hasGeoPos(prevX, prevY) && (prevZ != tz))
		{
			// different floors, return start location
			return new Location(x, y, z);
		}
		
		return new Location(tx, ty, tz);
	}
	
	/**
	 * Checks if its possible to move from one location to another.
	 * @param fromX the X coordinate to start checking from
	 * @param fromY the Y coordinate to start checking from
	 * @param fromZ the Z coordinate to start checking from
	 * @param toX the X coordinate to end checking at
	 * @param toY the Y coordinate to end checking at
	 * @param toZ the Z coordinate to end checking at
	 * @return {@code true} if the character at start coordinates can move to end coordinates, {@code false} otherwise
	 */
	public boolean canMove(int fromX, int fromY, int fromZ, int toX, int toY, int toZ)
	{
		int geoX = getGeoX(fromX);
		int geoY = getGeoY(fromY);
		fromZ = getNearestZ(geoX, geoY, fromZ);
		int tGeoX = getGeoX(toX);
		int tGeoY = getGeoY(toY);
		toZ = getNearestZ(tGeoX, tGeoY, toZ);
		
		if (DoorTable.getInstance().checkIfDoorsBetween(fromX, fromY, fromZ, toX, toY, toZ))
		{
			return false;
		}
		
		LinePointIterator pointIter = new LinePointIterator(geoX, geoY, tGeoX, tGeoY);
		// first point is guaranteed to be available
		pointIter.next();
		int prevX = pointIter.x();
		int prevY = pointIter.y();
		int prevZ = fromZ;
		
		while (pointIter.next())
		{
			int curX = pointIter.x();
			int curY = pointIter.y();
			int curZ = getNearestZ(curX, curY, prevZ);
			
			if (hasGeoPos(prevX, prevY))
			{
				int nswe = GeoUtils.computeNswe(prevX, prevY, curX, curY);
				if (!checkNearestNsweAntiCornerCut(prevX, prevY, prevZ, nswe))
				{
					return false;
				}
			}
			
			prevX = curX;
			prevY = curY;
			prevZ = curZ;
		}
		
		if (hasGeoPos(prevX, prevY) && (prevZ != toZ))
		{
			// different floors
			return false;
		}
		
		return true;
	}
	
	public int traceTerrainZ(int x, int y, int z, int tx, int ty)
	{
		int geoX = getGeoX(x);
		int geoY = getGeoY(y);
		z = getNearestZ(geoX, geoY, z);
		int tGeoX = getGeoX(tx);
		int tGeoY = getGeoY(ty);
		
		LinePointIterator pointIter = new LinePointIterator(geoX, geoY, tGeoX, tGeoY);
		// first point is guaranteed to be available
		pointIter.next();
		int prevZ = z;
		
		while (pointIter.next())
		{
			int curX = pointIter.x();
			int curY = pointIter.y();
			int curZ = getNearestZ(curX, curY, prevZ);
			
			prevZ = curZ;
		}
		
		return prevZ;
	}
	
	/**
	 * Checks the specified position for available geodata.
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @return {@code true} if there is geodata for the given coordinates, {@code false} otherwise
	 */
	public boolean hasGeo(int x, int y)
	{
		return hasGeoPos(getGeoX(x), getGeoY(y));
	}
	
	private static class SingletonHolder
	{
		protected static final GeoData _instance = new GeoData();
	}
}