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
package net.sf.l2j.gameserver.scripting.scriptengine.java;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.sf.l2j.gameserver.scripting.scriptengine.AbstractScriptEngineFactory;

/**
 * This is script engine factory for "Java" script engine.
 * 
 * @author A. Sundararajan
 */
public class JavaScriptEngineFactory extends AbstractScriptEngineFactory
{
	private long _nextClassNum = 0L;
	
	public JavaScriptEngineFactory(ScriptEngineManager manager)
	{
		addName("java", manager);
		addExtension("java", manager);
	}
	
	@Override
	public String getEngineName()
	{
		return "java";
	}
	
	@Override
	public String getEngineVersion()
	{
		return JavaCompiler.JAVA_LANGUAGE_VERSION;
	}
	
	@Override
	public List<String> getExtensions()
	{
		return _extensions;
	}
	
	@Override
	public String getLanguageName()
	{
		return "java";
	}
	
	@Override
	public String getLanguageVersion()
	{
		return JavaCompiler.JAVA_LANGUAGE_VERSION;
	}
	
	@Override
	public String getMethodCallSyntax(String obj, String m, String... args)
	{
		StringBuilder buf = new StringBuilder();
		buf.append(obj);
		buf.append('.');
		buf.append(m);
		buf.append('(');
		if (args.length != 0)
		{
			int i = 0;
			for (; i < (args.length - 1); i++)
			{
				buf.append(args[i] + ", ");
			}
			buf.append(args[i]);
		}
		buf.append(')');
		return buf.toString();
	}
	
	@Override
	public List<String> getMimeTypes()
	{
		return _mimeTypes;
	}
	
	@Override
	public List<String> getNames()
	{
		return _names;
	}
	
	@Override
	public String getOutputStatement(String toDisplay)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("System.out.print(\"");
		int len = toDisplay.length();
		for (int i = 0; i < len; i++)
		{
			char ch = toDisplay.charAt(i);
			switch (ch)
			{
				case 34: // '"'
					buf.append("\\\"");
					break;
				case 92: // '\\'
					buf.append("\\\\");
					break;
				default:
					buf.append(ch);
					break;
			}
		}
		buf.append("\");");
		return buf.toString();
	}
	
	@Override
	public String getParameter(String key)
	{
		if (key.equals("javax.script.engine"))
		{
			return getEngineName();
		}
		if (key.equals("javax.script.engine_version"))
		{
			return getEngineVersion();
		}
		if (key.equals("javax.script.name"))
		{
			return getEngineName();
		}
		if (key.equals("javax.script.language"))
		{
			return getLanguageName();
		}
		if (key.equals("javax.script.language_version"))
		{
			return getLanguageVersion();
		}
		if (key.equals("THREADING"))
		{
			return "MULTITHREADED";
		}
		return null;
	}
	
	@Override
	public String getProgram(String... statements)
	{
		// we generate a Main class with main method
		// that contains all the given statements
		
		StringBuilder buf = new StringBuilder();
		buf.append("class ");
		buf.append(getClassName());
		buf.append("{\n");
		buf.append("	public static void main(String[] args) {\n");
		if (statements.length != 0)
		{
			for (String statement : statements)
			{
				buf.append("		");
				buf.append(statement);
				buf.append(";\n");
			}
		}
		buf.append("	}\n");
		buf.append("}\n");
		return buf.toString();
	}
	
	@Override
	public ScriptEngine getScriptEngine()
	{
		JavaScriptEngine engine = new JavaScriptEngine();
		engine.setFactory(this);
		return engine;
	}
	
	// used to generate a unique class name in getProgram
	private String getClassName()
	{
		return "com_sun_script_java_Main$" + getNextClassNumber();
	}
	
	private synchronized long getNextClassNumber()
	{
		return _nextClassNum++;
	}
}
