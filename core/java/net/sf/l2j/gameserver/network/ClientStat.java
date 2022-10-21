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
package net.sf.l2j.gameserver.network;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;

/**
 * @author DnR
 *
 */
public class ClientStat
{
	private static Logger _log = Logger.getLogger(ClientStat.class.getName());

	// Flood protection
	private int _packetsSentInSec = 0;
	private int _packetsSentStartTick = 0;
	
	private int _underflowReadsInMin = 0;
	private int _underflowReadStartTick = 0;
	
	private int _unknownPacketsInMin = 0;
	private int _unknownPacketStartTick = 0;

	private final L2GameClient _client;

	public ClientStat(L2GameClient client)
	{
		_client = client;
	}
	
	/**
	 * Checks whether client is flooded by packets.
	 * @return True if the amount of packets counted once every second is higher than maximum.
	 */
	public boolean isPacketFlooded()
	{
		if ((GameTimeController.getInstance().getGameTicks() - _packetsSentStartTick) > 10)
		{
			_packetsSentStartTick = GameTimeController.getInstance().getGameTicks();
			_packetsSentInSec = 0;
		}
		else
		{
			_packetsSentInSec++;
			if (_packetsSentInSec > 80)
			{
				_client.sendPacket(new ActionFailed());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Counts the amount of BufferUnderflowExceptions once every minute.
	 */
	public void countUnderflowReads()
	{
		if ((GameTimeController.getInstance().getGameTicks() - _underflowReadStartTick) > 600)
		{
			_underflowReadStartTick = GameTimeController.getInstance().getGameTicks();
			_underflowReadsInMin = 0;
		}
		else
		{
			_underflowReadsInMin++;
			if (_underflowReadsInMin > 1)
			{
				_client.closeNow();
				_log.severe("Client " + _client.toString() + " - Disconnected: Too many buffer underflow exceptions");
			}
		}
	}
	
	/**
	 * Counts the amount of unknown packets once every minute.
	 */
	public void countUnknownPackets()
	{
		if ((GameTimeController.getInstance().getGameTicks() - _unknownPacketStartTick) > 600)
		{
			_unknownPacketStartTick = GameTimeController.getInstance().getGameTicks();
			_unknownPacketsInMin = 0;
		}
		else
		{
			_unknownPacketsInMin++;
			if (_unknownPacketsInMin > 5)
			{
				_client.closeNow();
				_log.severe("Client " + _client.toString() + " - Disconnected: Too many unknown packets sent");
			}
		}
	}
}
