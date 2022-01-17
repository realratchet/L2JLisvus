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
package net.sf.l2j.gameserver.scripting.scriptengine;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/**
 * @author DnR
 *
 */
public abstract class AbstractScriptEngineFactory implements ScriptEngineFactory
{
	protected final List<String> _names = new ArrayList<>();
	protected final List<String> _extensions = new ArrayList<>();
	protected final List<String> _mimeTypes = new ArrayList<>();
	
	protected final void addName(String name, ScriptEngineManager manager)
	{
		_names.add(name);
		manager.registerEngineName(name, this);
	}
	
	protected final void addExtension(String extension, ScriptEngineManager manager)
	{
		_extensions.add(extension);
		manager.registerEngineExtension(extension, this);
	}
	
	protected final void addMimeType(String mimeType, ScriptEngineManager manager)
	{
		_mimeTypes.add(mimeType);
		manager.registerEngineMimeType(mimeType, this);
	}
}
