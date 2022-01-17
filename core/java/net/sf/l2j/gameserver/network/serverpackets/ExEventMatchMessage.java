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

public class ExEventMatchMessage extends L2GameServerPacket
{
	public static final byte STRING = 0;
	public static final byte STATIC_FINISH = 1;
	public static final byte STATIC_START = 2;
	public static final byte STATIC_GAME_OVER = 3;
	public static final byte STATIC_1 = 4;
	public static final byte STATIC_2 = 5;
	public static final byte STATIC_3 = 6;
	public static final byte STATIC_4 = 7;
	public static final byte STATIC_5 = 8;
	
	private static final String _S__FE_04_EXEVENTMATCHMESSAGE = "[S] FE:04 ExEventMatchMessage";
	
	private final int _type;
	private final String _message;
	
	public ExEventMatchMessage(int type)
	{
		this(type, "");
	}
	
	public ExEventMatchMessage(String message)
	{
		this(STRING, message);
	}
	
	public ExEventMatchMessage(int type, String message)
	{
		_type = type;
		_message = message;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x04);
		writeC(_type);
		
		if (_type == STRING)
		{
			writeS(_message);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_04_EXEVENTMATCHMESSAGE;
	}
}