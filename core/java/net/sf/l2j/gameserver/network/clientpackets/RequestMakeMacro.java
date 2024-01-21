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

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Macro;
import net.sf.l2j.gameserver.model.L2Macro.L2MacroCmd;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class RequestMakeMacro extends L2GameClientPacket
{
	private static final Logger _log = Logger.getLogger(RequestMakeMacro.class.getName());
	private static final String _C__C1_REQUESTMAKEMACRO = "[C] C1 RequestMakeMacro";
	
	private int _id;
	private String _name;
	private String _desc;
	private String _acronym;
	private int _icon;
	private int _count;
	private int _commandsLenght = 0;
	private L2MacroCmd[] _commands;

	@Override
	protected void readImpl()
	{
		_id = readD();
		_name = readS();
		_desc = readS();
		_acronym = readS();
		_icon = readC();
		_count = readC();
		
		if (_count > 12)
		{
			_count = 12;
		}

		_commands = new L2MacroCmd[_count];

		if (Config.DEBUG)
		{
			_log.info("Make macro id:" + _id + "\tname:" + _name + "\tdesc:" + _desc + "\tacronym:" + _acronym + "\ticon:" + _icon + "\tcount:" + _count);
		}

		for (int i = 0; i < _count; i++)
		{
			int entry = readC();
			int type = readC(); // 1 = skill, 3 = action, 4 = shortcut
			int d1 = readD(); // skill or page number for shortcuts
			int d2 = readC();
			String command = readS();

			_commandsLenght += command.length();
			_commands[i] = new L2MacroCmd(entry, type, d1, d2, command);

			if (Config.DEBUG)
			{
				_log.info("entry:" + entry + "\ttype:" + type + "\td1:" + d1 + "\td2:" + d2 + "\tcommand:" + command);
			}
		}
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}

		if (_name.isEmpty())
		{
			player.sendPacket(new SystemMessage(SystemMessage.ENTER_THE_MACRO_NAME));
			return;
		}

		if (_desc.length() > 32)
		{
			player.sendPacket(new SystemMessage(SystemMessage.MACRO_DESCRIPTION_MAX_32_CHARS));
			return;
		}
		
		if (_commandsLenght > 255)
		{
			player.sendPacket(new SystemMessage(SystemMessage.INVALID_MACRO));
			return;
		}

		if (player.getMacroses().getAllMacroses().length > 24)
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_MAY_CREATE_UP_TO_24_MACROS));
			return;
		}

		player.registerMacro(new L2Macro(_id, _icon, _name, _desc, _acronym, _commands));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__C1_REQUESTMAKEMACRO;
	}
}