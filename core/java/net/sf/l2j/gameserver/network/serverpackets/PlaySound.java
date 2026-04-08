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

/**
 * This class ...
 * @version $Revision: 1.1.6.2 $ $Date: 2005/03/27 15:29:39 $
 */
public class PlaySound extends L2GameServerPacket
{
	private static final String _S__98_PlaySound = "[S] 98 PlaySound";

	// 0 Sound, 1 Music, 2 Voice
	private final int _type;
	private final String _soundFile;
	private final int _bindToObject;
	private final int _objectId; // used for ships
	private final int _locX;
	private final int _locY;
	private final int _locZ;
	// Only for Music and Voice
	private final int _delay;
	
	public PlaySound(String soundFile)
	{
		_type = 0;
		_soundFile = soundFile;
		_bindToObject = 0;
		_objectId = 0;
		_locX = 0;
		_locY = 0;
		_locZ = 0;
		_delay = 0;
	}

	public PlaySound(int type, String soundFile, int bindToObject, int objectId, int locX, int locY, int locZ)
	{
		this(type, soundFile, bindToObject, objectId, locX, locY, locZ, 0);
	}
	
	public PlaySound(int type, String soundFile, int bindToObject, int objectId, int locX, int locY, int locZ, int delay)
	{
		_type = type;
		_soundFile = soundFile;
		_bindToObject = bindToObject;
		_objectId = objectId;
		_locX = locX;
		_locY = locY;
		_locZ = locZ;
		_delay = delay;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x98);
		writeD(_type);
		writeS(_soundFile);
		writeD(_bindToObject);
		writeD(_objectId);
		writeD(_locX);
		writeD(_locY);
		writeD(_locZ);
		writeD(_delay);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__98_PlaySound;
	}
}