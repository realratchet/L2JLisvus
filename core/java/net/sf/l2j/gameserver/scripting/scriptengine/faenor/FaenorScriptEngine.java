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
package net.sf.l2j.gameserver.scripting.scriptengine.faenor;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptContext;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.scripting.scriptengine.Parser;
import net.sf.l2j.gameserver.scripting.scriptengine.ParserNotCreatedException;
import net.sf.l2j.gameserver.scripting.scriptengine.ScriptDocument;
import net.sf.l2j.gameserver.scripting.scriptengine.ScriptEngine;

/**
 * @author Luis Arias
 *
 */
public class FaenorScriptEngine extends ScriptEngine
{
    private static Logger _log = Logger.getLogger(FaenorScriptEngine.class.getName());
    
    public final static String PACKAGE_DIRECTORY = "data/faenor/";
    public final static boolean DEBUG = true;

    public static FaenorScriptEngine getInstance()
    {
        return SingletonHolder._instance;
    }

    private FaenorScriptEngine()
    {
    	final File packDirectory = new File(Config.DATAPACK_ROOT, PACKAGE_DIRECTORY);
    	
    	FileFilter fileFilter = new FileFilter()
        {
            @Override
			public boolean accept(File file)
            {
            	if (file == null || !file.isFile())
        		{
        			return false;
        		}
        		return file.getName().toLowerCase().endsWith(".xml");
            }
        };
    	
		final File[] files = packDirectory.listFiles(fileFilter);
		if (files != null)
		{
			for (File file : files)
			{
				try (InputStream in = new FileInputStream(file))
				{
					parseScript(new ScriptDocument(file.getName(), in), null);
				}
				catch (IOException e)
				{
					_log.log(Level.WARNING, e.getMessage(), e);
				}
			}
		}
    }

    public void parseScript(ScriptDocument script, ScriptContext context)
    {
    	Document doc = script.getDocument();
    	if (doc == null)
    	{
    		return;
    	}
    	
    	for (Node par = doc.getFirstChild(); par != null; par = par.getNextSibling())
        {
			if (par.getNodeName().equals("Event") || par.getNodeName().equals("Quest"))
            {
				Parser parser = null;
				String cl = "faenor.Faenor" + par.getNodeName() + "Parser";
				try
				{
					parser = createParser(cl);
				}
				catch (ParserNotCreatedException e)
				{
					_log.log(Level.WARNING, "ERROR: No parser registered for Script: " + cl + ": " + e.getMessage(), e);
				}
				
				if (parser == null)
				{
					_log.warning("Unknown Script Type: " + script.getName());
					continue;
				}
				
				try
				{
					parser.parseScript(par, context);
					_log.info(getClass().getSimpleName() + ": Loaded " + script.getName() + " successfully.");
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "Script Parsing Failed: " + e.getMessage(), e);
				}
            }
        }
    }
    
    private static class SingletonHolder
	{
		protected static final FaenorScriptEngine _instance = new FaenorScriptEngine();
	}
}