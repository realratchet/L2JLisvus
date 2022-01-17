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

import net.sf.l2j.gameserver.instancemanager.BoatManager;
import net.sf.l2j.gameserver.model.actor.instance.L2BoatInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.GetOnVehicle;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.Point3D;

/**
 * This class ...
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestGetOnVehicle extends L2GameClientPacket
{
	private static final String _C__5C_GETONVEHICLE = "[C] 5C GetOnVehicle";
	
	private int _boatId;
	private Point3D _pos;
	
	@Override
	protected void readImpl()
	{
		int x, y, z;
		_boatId = readD();
		x = readD();
		y = readD();
		z = readD();
		_pos = new Point3D(x, y, z);
	}
	
	@Override
	public void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (activeChar.isFlying())
		{
			sendPacket(new ActionFailed());
			return;
		}
		
		L2BoatInstance boat;
		if (activeChar.isInBoat())
		{
			boat = activeChar.getBoat();
			if (boat.getObjectId() != _boatId)
			{
				sendPacket(new ActionFailed());
				return;
			}
		}
		else
		{
			boat = BoatManager.getInstance().getBoat(_boatId);
			if ((boat == null) || boat.isMoving() || !activeChar.isInsideRadius(boat, 1000, true, false))
			{
				sendPacket(new ActionFailed());
				return;
			}
		}
		
		if (activeChar.getPet() != null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.RELEASE_PET_ON_BOAT));
		}
		
		activeChar.setInBoatPosition(_pos);
		activeChar.setBoat(boat);
		activeChar.broadcastPacket(new GetOnVehicle(activeChar.getObjectId(), boat.getObjectId(), _pos));
		activeChar.setXYZ(boat.getX(), boat.getY(), boat.getZ());
		activeChar.revalidateZone(true);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__5C_GETONVEHICLE;
	}
}