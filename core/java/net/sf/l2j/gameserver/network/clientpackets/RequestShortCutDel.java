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

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestShortCutDel extends L2GameClientPacket
{
	private static final String _C__35_REQUESTSHORTCUTDEL = "[C] 35 RequestShortCutDel";

	private int _slot;
	private int _page;

	@Override
	protected void readImpl()
	{
		int slot = readD();
		_slot = slot % 12;
		_page = slot / 12;
	}

	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		// client needs no confirmation.
		// This packet is just to inform the server
		activeChar.deleteShortCut(_slot, _page);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__35_REQUESTSHORTCUTDEL;
	}
}