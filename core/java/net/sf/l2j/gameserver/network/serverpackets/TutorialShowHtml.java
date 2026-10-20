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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class TutorialShowHtml extends L2GameServerPacket
{
	private static final String _S__A0_TUTORIALSHOWHTML = "[S] a0 TutorialShowHtml";
	private final String _html;
	
	public TutorialShowHtml(String html)
	{
		_html = html;
	}
	
	@Override
	public void runImpl()
	{
		if (!Config.BYPASS_VALIDATION)
		{
			return;
		}
		
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		// Only links of the tutorial window currently shown may be requested
		activeChar.clearTutorialLinks();
		for (int i = 0; i < _html.length(); i++)
		{
			int start = _html.indexOf("\"link ", i);
			int finish = _html.indexOf("\"", start + 1);
			if ((start < 0) || (finish < 0))
			{
				break;
			}
			
			i = finish;
			activeChar.addTutorialLink(_html.substring(start + 6, finish).trim());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket#writeImpl()
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xa0);
		writeS(_html);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__A0_TUTORIALSHOWHTML;
	}
}