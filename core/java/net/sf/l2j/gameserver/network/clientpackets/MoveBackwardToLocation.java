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

import java.nio.BufferUnderflowException;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.PartyMemberPosition;
import net.sf.l2j.gameserver.network.serverpackets.StopMove;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.11.2.4.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class MoveBackwardToLocation extends L2GameClientPacket
{
	// private static Logger _log = Logger.getLogger(MoveBackwardToLocation.class.getName());

	// cdddddd
	private int _targetX;
	private int _targetY;
	private int _targetZ;
	@SuppressWarnings("unused")
	private int _originX;
	@SuppressWarnings("unused")
	private int _originY;
	@SuppressWarnings("unused")
	private int _originZ;
	@SuppressWarnings("unused")
	private int _moveMovement;

	private static final String _C__01_MOVEBACKWARDTOLOC = "[C] 01 MoveBackwardToLoc";

	@Override
	protected void readImpl()
	{
		_targetX = readD();
		_targetY = readD();
		_targetZ = readD();
		_originX = readD();
		_originY = readD();
		_originZ = readD();
		try
		{
			_moveMovement = readD(); // is 0 if cursor keys are used 1 if mouse is used
		}
		catch (BufferUnderflowException e)
		{
			if (!Config.ALLOW_L2WALKER)
			{
				getClient().getActiveChar().sendPacket(new SystemMessage(769));
				Util.handleIllegalPlayerAction(getClient().getActiveChar(), "Player " + getClient().getActiveChar().getName() + " is trying to use L2Walker and got kicked.", Config.DEFAULT_PUNISH);
			}
		}
	}

	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		if (Config.PLAYER_MOVEMENT_BLOCK_TIME > 0 && !activeChar.isGM() && activeChar.getNotMoveUntil() > System.currentTimeMillis())
		{
			activeChar.sendPacket(new ActionFailed());
			return;
		}
		
		if ((_targetX == _originX) && (_targetY == _originY) && (_targetZ == _originZ))
 		{
 			activeChar.sendPacket(new StopMove(activeChar));
 			return;
 		}
		
		// Correcting targetZ from floor level to head level (?)
 		// Client is giving floor level as targetZ but that floor level doesn't
 		// match our current geodata and teleport coords as good as head level!
 		// L2J uses floor, not head level as char coordinates. This is some
 		// sort of incompatibility fix.
 		// Validate position packets sends head level.
 		_targetZ += activeChar.getTemplate().collisionHeight;

		if (activeChar.getTeleMode() > 0)
		{
			if (activeChar.getTeleMode() == 1)
			{
				activeChar.setTeleMode(0);
			}
			activeChar.sendPacket(new ActionFailed());
			activeChar.teleToLocation(_targetX, _targetY, _targetZ, false);
			return;
		}

		double dx = _targetX - activeChar.getX();
		double dy = _targetY - activeChar.getY();
		// Can't move if character is confused, or trying to move a huge distance
		if (activeChar.isOutOfControl() || (((dx * dx) + (dy * dy)) > 98010000)) // 9900*9900
		{
			activeChar.sendPacket(new ActionFailed());
			return;
		}
		
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(_targetX, _targetY, _targetZ));
		
		if (activeChar.getParty() != null)
		{
			activeChar.getParty().broadcastToPartyMembers(activeChar, new PartyMemberPosition(activeChar));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__01_MOVEBACKWARDTOLOC;
	}

	@Override
	protected boolean triggersOnActionRequest()
	{
		return true;
	}
}