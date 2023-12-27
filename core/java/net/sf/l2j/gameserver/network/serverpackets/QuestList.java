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
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * sample for rev 377: 98 05 00 number of quests ff 00 00 00 0a 01 00 00 39 01 00 00 04 01 00 00 a2 00 00 00 04 00 number of quest items 85 45 13 40 item obj id 36 05 00 00 item id 02 00 00 00 count 00 00 ?? bodyslot 23 bd 12 40 86 04 00 00 0a 00 00 00 00 00 1f bd 12 40 5a 04 00 00 09 00 00 00 00 00
 * 1b bd 12 40 5b 04 00 00 39 00 00 00 00 00 . format h (d) h (dddh) rev 377 format h (dd) h (dddd) rev 417
 * @version $Revision: 1.4.2.2.2.2 $ $Date: 2005/02/10 16:44:28 $
 */
public class QuestList extends L2GameServerPacket
{
	private static final String _S__98_QUESTLIST = "[S] 80 QuestList";
	
	private L2PcInstance _activeChar;
	private QuestState[] _questStates;

	public QuestList(L2PcInstance activeChar)
	{
		_activeChar = activeChar;
		_questStates = _activeChar.getAllActiveQuestStates();
	}

	
	@Override
	protected final void writeImpl()
	{
		writeC(0x80);
		writeH(_questStates.length);

		for (QuestState qs : _questStates)
		{
			writeD(qs.getQuest().getQuestIntId());
			writeD(qs.getInt("cond")); // stage of quest progress
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__98_QUESTLIST;
	}
}