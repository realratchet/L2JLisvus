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
package net.sf.l2j.loginserver.gameserverpackets;

import java.util.logging.Logger;

/**
 * Format: cccddb c desired ID c accept alternative ID c reserve Host s ExternalHostName s InternalHostName d max players d hexid size b hexid
 * @author -Wooden-
 */
public class GameServerAuth extends GameServerBasePacket
{
	protected static Logger _log = Logger.getLogger(GameServerAuth.class.getName());
	private final byte[] _hexID;
	private final int _desiredID;
	private final boolean _hostReserved;
	private final boolean _acceptAlternativeID;
	private final int _maxPlayers;
	private final int _port;
	private final String _externalHost;
	private final String _internalHost;
	
	/**
	 * @param decrypt
	 */
	public GameServerAuth(byte[] decrypt)
	{
		super(decrypt);
		_desiredID = readC();
		_acceptAlternativeID = (readC() == 0 ? false : true);
		_hostReserved = (readC() == 0 ? false : true);
		_externalHost = readS();
		// Some forks have removed internal host functionality from their game servers
		_internalHost = decrypt.length > 40 ? readS() : "*";
		_port = readH();
		_maxPlayers = readD();
		int size = readD();
		_hexID = readB(size);
	}

	/**
	 * @return
	 */
	public byte[] getHexID()
	{
		return _hexID;
	}
	
	public boolean getHostReserved()
	{
		return _hostReserved;
	}
	
	public int getDesiredID()
	{
		return _desiredID;
	}
	
	public boolean acceptAlternateID()
	{
		return _acceptAlternativeID;
	}

	/**
	 * @return Returns the max_palyers.
	 */
	public int getMaxPlayers()
	{
		return _maxPlayers;
	}

	/**
	 * @return Returns the externalHost.
	 */
	public String getExternalHost()
	{
		return _externalHost;
	}

	/**
	 * @return Returns the internalHost.
	 */
	public String getInternalHost()
	{
		return _internalHost;
	}
	
	/**
	 * @return Returns the port.
	 */
	public int getPort()
	{
		return _port;
	}
}