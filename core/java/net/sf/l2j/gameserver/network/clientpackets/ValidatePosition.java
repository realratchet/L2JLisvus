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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.geoengine.geoeditorcon.GeoEditorListener;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.type.L2WaterZone;
import net.sf.l2j.gameserver.network.serverpackets.GetOnVehicle;
import net.sf.l2j.gameserver.network.serverpackets.PartyMemberPosition;
import net.sf.l2j.gameserver.network.serverpackets.StopRotation;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 * This class ...
 * @version $Revision: 1.13.4.7 $ $Date: 2005/03/27 15:29:30 $
 */
public class ValidatePosition extends L2GameClientPacket
{
	private static Logger _log = Logger.getLogger(ValidatePosition.class.getName());
	private static final String _C__48_VALIDATEPOSITION = "[C] 48 ValidatePosition";
	
	// Max distance between client Z and geodata floor for client Z to be accepted
	private static final int GEO_Z_TOLERANCE = 100;
	
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	private int _boatId;
	
	@Override
	protected void readImpl()
	{
		_x = readD();
		_y = readD();
		_z = readD();
		_heading = readD();
		_boatId = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if ((activeChar == null) || activeChar.isTeleporting() || activeChar.inObserverMode())
		{
			return;
		}
		
		int realX = activeChar.getX();
		int realY = activeChar.getY();
		int realZ = activeChar.getZ();
		
		if (Config.DEVELOPER)
		{
			_log.fine("client pos: " + _x + " " + _y + " " + _z + " head " + _heading);
			_log.fine("server pos: " + realX + " " + realY + " " + realZ + " head " + activeChar.getHeading());
		}

		// This is one way to stop infinite rotation glitch when finish rotation packet gets lost or interrupted due to player spamming arrow keys
		if (activeChar.getRotationSide() != 0)
		{
			activeChar.setRotationSide(0);
			activeChar.broadcastPacket(new StopRotation(activeChar.getObjectId(), _heading, 0));
		}
		
		if ((_x == 0) && (_y == 0))
		{
			if (realX != 0)
			{
				return;
			}
		}
		
		// Position may legitimately lag behind while casting
		if (activeChar.isCastingNow())
		{
			return;
		}
		
		int dx, dy, dz;
		double diffSq;
		
		if (activeChar.isInBoat())
		{
			if (Config.COORD_SYNCHRONIZE == 2)
			{
				dx = _x - activeChar.getInBoatPosition().getX();
				dy = _y - activeChar.getInBoatPosition().getY();
				dz = _z - activeChar.getInBoatPosition().getZ();
				diffSq = ((dx * dx) + (dy * dy));
				if (diffSq > 250000)
				{
					sendPacket(new GetOnVehicle(activeChar.getObjectId(), _boatId, activeChar.getInBoatPosition()));
				}
			}
			return;
		}
		
		if (activeChar.isFalling(_z))
		{
			// This is a small hack to fix position of players when they fall through world textures, based on pablo966's concept
			final int height = GeoData.getInstance().getHeight(_x, _y, _z);
			if (_z < height && Math.abs(height - _z) > 100)
			{
				activeChar.sendPacket(new ValidateLocation(activeChar.getObjectId(), _x, _y, height, _heading));
			}
			return;
		}
		
		// Abnormal Z read from client, move back to server position
		if ((_z < -20000) || (_z > 20000))
		{
			activeChar.teleToLocation(realX, realY, realZ, false);
			return;
		}
		
		dx = _x - realX;
		dy = _y - realY;
		dz = _z - realZ;
		diffSq = ((dx * dx) + (dy * dy));
		final long elapsed = Math.min(activeChar.updateValidatePositionTime(), 1000);

		// Client Z changes are only accepted within sane bounds
		boolean validZ = (Math.abs(dz) <= 200) || ((Math.abs(dz) < 1500) && (Math.abs(_z - activeChar.getClientZ()) < 800));
		
		// With geodata, client Z must match the floor the server knows the player is on
		if (validZ && !activeChar.isFlying() && GeoData.getInstance().hasGeo(_x, _y))
		{
			final int floorZ = GeoData.getInstance().getHeight(_x, _y, realZ);
			if (activeChar.isInsideZone(L2Character.ZONE_WATER))
			{
				// Swimming: claimed point must be inside water and not below the bottom, or on the floor (leaving water)
				// A floor below the swimmer also counts, for shafts and tunnels under another layer
				final boolean inWater = ZoneManager.getInstance().getZone(realX, realY, _z, L2WaterZone.class) != null;
				final boolean aboveFloor = (_z >= (GeoData.getInstance().getHeight(_x, _y, _z) - GEO_Z_TOLERANCE)) || (GeoData.getInstance().getNextLowerZ(GeoData.getInstance().getGeoX(_x), GeoData.getInstance().getGeoY(_y), _z) < _z);
				validZ = (inWater && aboveFloor) || (Math.abs(_z - floorZ) <= GEO_Z_TOLERANCE);
			}
			else
			{
				validZ = Math.abs(_z - floorZ) <= GEO_Z_TOLERANCE;
			}
		}
		
		if ((activeChar.getParty() != null) && (activeChar.getLastPartyPositionDistance(_x, _y, _z) > 150))
		{
			activeChar.setLastPartyPosition(_x, _y, _z);
			activeChar.getParty().broadcastToPartyMembers(activeChar, new PartyMemberPosition(activeChar));
		}
		
		if (Config.ACCEPT_GEOEDITOR_CONN)
		{
			if ((GeoEditorListener.getInstance().getThread() != null) && GeoEditorListener.getInstance().getThread().isWorking() && GeoEditorListener.getInstance().getThread().isSend(activeChar))
			{
				GeoEditorListener.getInstance().getThread().sendGmPosition(_x, _y, (short) _z);
			}
		}
		
		if (activeChar.isFlying() || activeChar.isInsideZone(L2Character.ZONE_WATER))
		{
			if (validZ)
			{
				activeChar.setXYZ(realX, realY, _z);
			}
			if ((!validZ && (Math.abs(dz) > 200)) || (diffSq > 90000))
			{
				activeChar.sendPacket(new ValidateLocation(activeChar));
			}
		}
		else if (diffSq < 360000) // if too large, messes observation
		{
			if (Config.COORD_SYNCHRONIZE == -1) // Only Z coordinate synchronized to server, mainly used when no geodata
			{
				if (validZ)
				{
					activeChar.setXYZ(realX, realY, _z);
					realZ = _z;
				}
				else if (Math.abs(dz) > 200)
				{
					activeChar.sendPacket(new ValidateLocation(activeChar));
				}
			}
			else if (Config.COORD_SYNCHRONIZE == 1) // Trusting also client x,y coordinates (should not be used with geodata)
			{
				if (!validZ)
				{
					if (Math.abs(dz) > 200)
					{
						activeChar.sendPacket(new ValidateLocation(activeChar));
					}
					return;
				}
				
				if (!activeChar.isMoving() || !activeChar.validateMovementHeading(_heading)) // Heading changed on client = possible obstacle
				{
					// character is not moving, take coordinates from client
					if (diffSq < 2500)
					{
						activeChar.setXYZ(realX, realY, _z);
					}
					else
					{
						activeChar.setXYZ(_x, _y, _z);
					}
				}
				else
				{
					activeChar.setXYZ(realX, realY, _z);
				}
				
				activeChar.setHeading(_heading);
				return;
			}
			
			// Without player pathfinding the client handles collisions: while the server has the player
			// stopped, follow client positions that geodata allows and the move speed could reach
			else if (!Config.PLAYER_PATHFINDING && !activeChar.isMoving() && (diffSq > 0))
			{
				final double maxDist = ((activeChar.getMoveSpeed() * elapsed * 1.2) / 1000) + 16;
				if (validZ && !activeChar.isMovementDisabled() && (diffSq <= (maxDist * maxDist)) && GeoData.getInstance().canMove(realX, realY, realZ, _x, _y, _z))
				{
					activeChar.setXYZ(_x, _y, _z);
					realX = _x;
					realY = _y;
					realZ = _z;
					if (diffSq > 256)
					{
						Broadcast.toKnownPlayers(activeChar, new ValidateLocation(activeChar));
					}
				}
				else if (diffSq > 1024)
				{
					activeChar.sendPacket(new ValidateLocation(activeChar));
				}
			}

			// Sync 2 (or other),
			// intended for geodata. Sends a validation packet to client
			// when too far from server calculated true coordinate.
			// Due to geodata "holes", some Z axis checks are made.
			else if ((diffSq > 250000) || (Math.abs(dz) > 200))
			{
				if (validZ && (Math.abs(dz) > 200) && (Math.abs(dz) < 1500) && (Math.abs(_z - activeChar.getClientZ()) < 800))
				{
					activeChar.setXYZ(realX, realY, _z);
					realZ = _z;
				}
				else
				{
					if (Config.DEVELOPER)
					{
						_log.info(activeChar.getName() + ": Synchronizing position Server --> Client");
					}
					
					activeChar.sendPacket(new ValidateLocation(activeChar));
				}
			}
		}
		
		activeChar.setClientX(_x);
		activeChar.setClientY(_y);
		activeChar.setClientZ(_z);
		activeChar.setClientHeading(_heading); // No real need to validate heading.
		activeChar.setLastServerPosition(realX, realY, realZ);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__48_VALIDATEPOSITION;
	}
}