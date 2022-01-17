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
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.7.4.4 $ $Date: 2005/03/27 18:46:19 $
 */
public class Action extends L2GameClientPacket
{
	private static final String ACTION__C__04 = "[C] 04 Action";
	private static Logger _log = Logger.getLogger(Action.class.getName());

	private boolean _removeSpawnProtection = false;

	// cddddc
	private int _objectId;
	@SuppressWarnings("unused")
	private int _originX;
	@SuppressWarnings("unused")
	private int _originY;
	@SuppressWarnings("unused")
	private int _originZ;
	private int _actionId;

	@Override
	protected void readImpl()
	{
		_objectId = readD(); // Target object Identifier
		_originX = readD();
		_originY = readD();
		_originZ = readD();
		_actionId = readC(); // Action identifier : 0-Simple click, 1-Shift click
	}

	@Override
	public void runImpl()
	{
		if (Config.DEBUG)
		{
			_log.fine("Action:" + _actionId);
		}
		if (Config.DEBUG)
		{
			_log.fine("oid:" + _objectId);
		}

		// Get the current L2PcInstance of the player
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		if (activeChar.inObserverMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.OBSERVERS_CANNOT_PARTICIPATE));
			activeChar.sendPacket(new ActionFailed());
			return;
		}

		L2Object obj;
		if (activeChar.getTargetId() == _objectId)
		{
			obj = activeChar.getTarget();
			_removeSpawnProtection = true;
		}
		else
		{
			obj = L2World.getInstance().findObject(_objectId);
		}

		if (obj == null)
		{
			// Pressing e.g. pickup many times quickly would get you here
			sendPacket(new ActionFailed());
			return;
		}

		if (obj instanceof L2ItemInstance)
		{
			_removeSpawnProtection = true;
		}

		// Check if the target is valid, if the player haven't a shop or isn't the requester of a transaction (ex : FriendInvite, JoinAlly, JoinParty...)
		if (!activeChar.isInStoreMode() && (activeChar.getActiveRequester() == null))
		{
			switch (_actionId)
			{
				case 0:
					obj.onAction(activeChar);
					break;
				case 1:
					if ((obj instanceof L2Character) && ((L2Character) obj).isAlikeDead())
					{
						obj.onAction(activeChar);
					}
					else
					{
						obj.onActionShift(getClient());
					}
					break;
				default:
					// Invalid action detected (probably client cheating), log this
					_log.warning("Character: " + activeChar.getName() + " requested invalid action: " + _actionId);
					sendPacket(new ActionFailed());
					break;
			}
		}
		else
		{
			// Actions prohibited when in trade
			activeChar.sendPacket(new ActionFailed());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return ACTION__C__04;
	}

	@Override
	protected boolean triggersOnActionRequest()
	{
		return _removeSpawnProtection;
	}
}