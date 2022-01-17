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
import net.sf.l2j.gameserver.network.serverpackets.MoveToLocationInVehicle;
import net.sf.l2j.gameserver.templates.L2WeaponType;
import net.sf.l2j.util.Point3D;

public class RequestMoveToLocationInVehicle extends L2GameClientPacket
{
	private int _BoatId;
	private Point3D _pos;
	private Point3D _origin_pos;

	@Override
	protected void readImpl()
	{
		int _x, _y, _z;
		_BoatId = readD(); // objectId of boat
		_x = readD();
		_y = readD();
		_z = readD();
		_pos = new Point3D(_x, _y, _z);
		_x = readD();
		_y = readD();
		_z = readD();
		_origin_pos = new Point3D(_x, _y, _z);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		if (activeChar.isSitting() || (activeChar.isAttackingNow() && (activeChar.getActiveWeaponItem() != null) && (activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW)))
		{
			activeChar.sendPacket(new ActionFailed());
			return;
		}

		final L2BoatInstance boat;
		if (activeChar.isInBoat())
		{
			boat = activeChar.getBoat();
			if (boat.getObjectId() != _BoatId)
			{
				activeChar.sendPacket(new ActionFailed());
				return;
			}
		}
		else
		{
			boat = BoatManager.getInstance().getBoat(_BoatId);
			if (boat == null)
			{
				activeChar.sendPacket(new ActionFailed());
				return;
			}
			activeChar.setBoat(boat);
		}

		if (activeChar.getAI().moveInBoat())
		{
			activeChar.setInBoatPosition(_pos);
			activeChar.broadcastPacket(new MoveToLocationInVehicle(activeChar, _pos, _origin_pos));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		// TODO Auto-generated method stub
		return "[C] 5C RequestMoveToLocationInVehicle";
	}
}