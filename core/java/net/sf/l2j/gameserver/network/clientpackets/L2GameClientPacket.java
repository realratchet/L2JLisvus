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
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.mmocore.ReceivablePacket;

/**
 * Packets received by the game server from clients
 * @author KenM
 */
public abstract class L2GameClientPacket extends ReceivablePacket<L2GameClient>
{
	private static Logger _log = Logger.getLogger(L2GameClientPacket.class.getName());

	@Override
	protected boolean read()
	{
		try
		{
			readImpl();
			return true;
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Client: " + getClient().toString() + " - Failed reading: " + getType() + " - L2JLisvus tag: " + Config.PROJECT_TAG + " ; " + e.getMessage(), e);
			
			if (e instanceof BufferUnderflowException)
			{
				// Count underflow reads
				getClient().getStat().countUnderflowReads();
			}
		}
		return false;
	}

	protected abstract void readImpl();

	@Override
	public final void run()
	{
		try
		{
			runImpl();

			/*
			 * Removes onspawn protection - player has faster computer than average
			 */
			if (triggersOnActionRequest() && (getClient().getActiveChar() != null))
			{
				getClient().getActiveChar().onActionRequest();
			}

			cleanUp();
		}
		catch (Throwable t)
		{
			_log.log(Level.SEVERE, "Client: " + getClient().toString() + " - Failed running: " + getType() + " - L2JLisvus tag: " + Config.PROJECT_TAG + " ; " + t.getMessage(), t);
			// in case of EnterWorld error kick player from game
			if (this instanceof EnterWorld)
			{
				getClient().closeNow();
			}
		}
	}

	protected abstract void runImpl();

	protected final void sendPacket(L2GameServerPacket gsp)
	{
		getClient().sendPacket(gsp);
	}
	
	/**
	 * @return A String with this packet name for debugging purposes
	 */
	public abstract String getType();

	/**
	 * Overridden with true value on some packets that should disable spawn protection
	 * @return
	 */
	protected boolean triggersOnActionRequest()
	{
		return false;
	}

	protected void cleanUp()
	{
	}
}