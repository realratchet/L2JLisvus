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
package net.sf.l2j.gameserver.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.ChangePassword;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.TvTCommand;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.VoiceExperience;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.Wedding;

/**
 * This class ...
 *
 * @version $Revision: 1.1.4.5 $ $Date: 2005/03/27 15:30:09 $
 */
public class VoicedCommandHandler
{
	private static Logger _log = Logger.getLogger(VoicedCommandHandler.class.getName());
	
	private final Map<String, IVoicedCommandHandler> _dataTable = new HashMap<>();
	
	public static VoicedCommandHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void load()
	{
		registerVoicedCommandHandler(new ChangePassword());
		registerVoicedCommandHandler(new TvTCommand());
		registerVoicedCommandHandler(new VoiceExperience());
		registerVoicedCommandHandler(new Wedding());

		_log.config("VoicedCommandHandler: Loaded " + _dataTable.size() + " handlers.");
	}
	
	public void registerVoicedCommandHandler(IVoicedCommandHandler handler)
	{
		String[] ids = handler.getVoicedCommandList();
		for (String id : ids)
		{
			if (Config.DEBUG) _log.fine("Adding handler for command " + id);
			_dataTable.put(id, handler);
		}
	}
	
	public IVoicedCommandHandler getVoicedCommandHandler(String voicedCommand)
	{
		String command = voicedCommand;
		if (voicedCommand.indexOf(" ") != -1)
		{
			command = voicedCommand.substring(0, voicedCommand.indexOf(" "));
		}
		
		if (Config.DEBUG)
			_log.fine("Getting handler for command: " + command + " -> " + (_dataTable.containsKey(command)));
		return _dataTable.get(command);
	}
	
	private static class SingletonHolder
	{
		protected static final VoicedCommandHandler _instance = new VoicedCommandHandler();
	}
}