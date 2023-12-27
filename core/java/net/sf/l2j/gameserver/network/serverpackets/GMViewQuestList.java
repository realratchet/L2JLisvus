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
 * Sh (dd) h (dddd)
 * @author Tempy
 */
public class GMViewQuestList extends L2GameServerPacket
{
	private static final String _S__AC_GMVIEWQUESTLIST = "[S] ac GMViewQuestList";
	
	private final L2PcInstance _activeChar;
	private QuestState[] _questStates;
	
	public GMViewQuestList(L2PcInstance cha)
	{
		_activeChar = cha;
		_questStates = _activeChar.getAllActiveQuestStates();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x93);
		writeS(_activeChar.getName());
		
		writeH(_questStates.length); // quest count
		
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
		return _S__AC_GMVIEWQUESTLIST;
	}
}