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

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class CreatureSay extends L2GameServerPacket
{
	private static final String _S__4A_CREATURESAY = "[S] 4A CreatureSay";
	private final int _objectId;
	private final int _textType;
	private final boolean _snoop;
	private final String _charName;
	private final String _text;
	private final int _sysStringId;
	private final int _sysMsgId;
	
	public CreatureSay(int objectId, int messageType, String charName, String text)
	{
		this(objectId, messageType, charName, text, true);
	}
	
	/**
	 * @param objectId
	 * @param messageType
	 * @param charName
	 * @param text
	 * @param snoop
	 */
	public CreatureSay(int objectId, int messageType, String charName, String text, boolean snoop)
	{
		_objectId = objectId;
		_textType = messageType;
		_charName = charName;
		_text = text;
		_sysStringId = 0;
		_sysMsgId = 0;
		_snoop = snoop;
	}
	
	/**
	 * @param objectId
	 * @param messageType
	 * @param sysStringId
	 * @param sysMsgId
	 */
	public CreatureSay(int objectId, int messageType, int sysStringId, int sysMsgId)
	{
		_objectId = objectId;
		_textType = messageType;
		_charName = null;
		_text = null;
		_sysStringId = sysStringId;
		_sysMsgId = sysMsgId;
		_snoop = false;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4a);
		writeD(_objectId);
		writeD(_textType);
		
		if (_text != null)
		{
			writeS(_charName);
			writeS(_text);
		}
		else
		{
			writeD(_sysStringId);
			writeD(_sysMsgId);
		}
	}
	
	@Override
	public final void runImpl()
	{
		if (!_snoop)
		{
			return;
		}
		
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar != null)
		{
			activeChar.broadcastSnoop(_textType, _charName, _text);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__4A_CREATURESAY;
	}
}