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

import java.util.Vector;

import net.sf.l2j.gameserver.model.L2Skill;

/**
 * @author Dezmond_snz Format: cdddsdd
 */
public class ConfirmDlg extends L2GameServerPacket
{
	private static final String _S__ED_CONFIRMDLG = "[S] ed ConfirmDlg";

	private static final int TYPE_SKILL_NAME = 4;
	private static final int TYPE_ITEM_NAME = 3;
	private static final int TYPE_NPC_NAME = 2;
	private static final int TYPE_NUMBER = 1;
	private static final int TYPE_TEXT = 0;
	
	private int _messageId;
	private int _skillLevel = 1;
	
	private final Vector<CnfDlgData> _info = new Vector<>();
	
	protected class CnfDlgData
	{
		protected final int _type;
		protected final Object _value;
		
		protected CnfDlgData(int type, Object value)
		{
			_type = type;
			_value = value;
		}
	}
	
	public ConfirmDlg(int messageId)
	{
		_messageId = messageId;
	}

	public ConfirmDlg addString(String text)
	{
		_info.add(new CnfDlgData(TYPE_TEXT, text));
		return this;
	}

	public ConfirmDlg addNumber(int number)
	{
		_info.add(new CnfDlgData(TYPE_NUMBER, number));
		return this;
	}

	public ConfirmDlg addSkillName(L2Skill skill)
	{
		if (skill.getId() != skill.getDisplayId()) // Custom skill
		{
			return addString(skill.getName());
		}
		return addSkillName(skill.getId(), skill.getLevel());
	}

	public ConfirmDlg addSkillName(int id)
	{
		return addSkillName(id, 1);
	}

	public ConfirmDlg addSkillName(int id, int lvl)
	{
		_info.add(new CnfDlgData(TYPE_SKILL_NAME, Integer.valueOf(id)));
		_skillLevel = lvl;
		return this;
	}

	public ConfirmDlg addNpcName(int id)
	{
		_info.add(new CnfDlgData(TYPE_NPC_NAME, Integer.valueOf(1000000 + id)));
		return this;
	}

	public ConfirmDlg addItemName(int id)
	{
		_info.add(new CnfDlgData(TYPE_ITEM_NAME, Integer.valueOf(id)));
		return this;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xed);
		writeD(_messageId);
		
		writeD(_info.size());
		
		for (CnfDlgData data : _info)
		{
			writeD(data._type);
			
			switch (data._type)
			{
				case TYPE_TEXT:
					writeS((String)data._value);
					break;
				case TYPE_NUMBER:
				case TYPE_NPC_NAME:
				case TYPE_ITEM_NAME:
					writeD((Integer)data._value);
					break;
				case TYPE_SKILL_NAME:
					writeD((Integer)data._value); // Skill Id
					writeD(_skillLevel); // Skill lvl
					break;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__ED_CONFIRMDLG;
	}
}