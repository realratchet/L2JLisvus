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
	private final String _charName;
	private final String _text;
	private final boolean _snoop;
	
	public CreatureSay(int objectId, int messageType, String charName, String text)
	{
		this(objectId, messageType, charName, text, true);
	}
	
	public CreatureSay(int objectId, int messageType, String charName, String text, boolean snoop)
	{
		this(objectId, messageType, charName, text, null, snoop);
	}
	
	public CreatureSay(int objectId, int messageType, String charName, String text, String targetName)
	{
		this(objectId, messageType, charName, text, targetName, true);
	}
	
	/**
	 * @param objectId 
	 * @param messageType 
	 * @param charName 
	 * @param text 
	 * @param targetName 
	 * @param snoop 
	 */
	public CreatureSay(int objectId, int messageType, String charName, String text, String targetName, boolean snoop)
	{
		_objectId = objectId;
		_textType = messageType;
		_charName = charName;
		_text = targetName != null && text.contains("$s1") ? text.replace("$s1", targetName) : text;
		_snoop = snoop;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4a);
		writeD(_objectId);
		writeD(_textType);
		writeS(_charName);
		writeS(_text);
	}
	
	@Override
	public final void runImpl()
	{
		if (!_snoop)
		{
			return;
		}
		
		L2PcInstance _pci = getClient().getActiveChar();
		if (_pci != null)
		{
			_pci.broadcastSnoop(_textType, _charName, _text);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__4A_CREATURESAY;
	}
}