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
package net.sf.l2j.gameserver.network.serverpackets;

public class Ride extends L2GameServerPacket
{
	private static final String _S__86_Ride = "[S] 86 Ride";
	public static final int ACTION_MOUNT = 1;
	public static final int ACTION_DISMOUNT = 0;
	
	private final int _id;
	private final int _bRide;
	private final int _rideType;
	private final int _rideClassID;
	
	/**
	 * 0x86 UnknownPackets dddd
	 * @param id 
	 * @param mount 
	 * @param rideClassId 
	 */
	public Ride(int id, boolean mount, int rideClassId)
	{
		_id = id; // charobjectID
		_bRide = mount ? 1 : 0;
		_rideClassID = rideClassId + 1000000; // npcID
		
		if (rideClassId == 0)
		{
			_rideType = 0;
		}
		else if ((rideClassId == 12526) || // wind strider
			(rideClassId == 12527) || // star strider
			(rideClassId == 12528))
		{
			_rideType = 1; // 1 for Strider ; 2 for wyvern
		}
		else if (rideClassId == 12621)
		{
			_rideType = 2; // 1 for Strider ; 2 for wyvern
		}
		else
		{
			throw new IllegalArgumentException("Unsupported mount NpcId: " + rideClassId);
		}
		
	}
	
	public int getMountType()
	{
		return _rideType;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x86);
		writeD(_id);
		writeD(_bRide);
		writeD(_rideType);
		writeD(_rideClassID);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__86_Ride;
	}
}