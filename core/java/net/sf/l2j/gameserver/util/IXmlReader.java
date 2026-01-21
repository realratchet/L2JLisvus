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
package net.sf.l2j.gameserver.util;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import net.sf.l2j.Config;

/**
 * Interface for XML parsers.
 * @author Zoey76, DnR
 */
public interface IXmlReader
{
    static final Logger _log = Logger.getLogger(IXmlReader.class.getName());
    
    public static boolean isXmlFile(File f)
    {
        return f.isFile() && f.getName().endsWith(".xml");
    }
    
    /**
     * Loads all XML files from given directory and parses each one of them.
     * @param dir the directory object to scan.
     * @param recursive parses all sub folders if there is.
     * @return False if it fails to find the directory, true otherwise.
     */
    default boolean parseDirectory(File dir, boolean recursive)
    {
        if (!dir.exists())
        {
            _log.warning(getClass().getSimpleName() + ": Folder " + dir.getAbsolutePath() + " doesn't exist!");
            return false;
        }
        
        final File[] files = dir.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if (recursive && file.isDirectory())
                {
                    parseDirectory(file, recursive);
                }
                else if (isXmlFile(file))
                {
                    parseFile(file);
                }
            }
        }
        return true;
    }
    
    default boolean parseDatapackDirectory(String path, boolean recursive)
    {
        return parseDirectory(new File(Config.DATAPACK_ROOT, path), recursive);
    }
    
    default void parseDatapackFile(String path)
    {
        parseFile(new File(Config.DATAPACK_ROOT, path));
    }
    
    /**
     * Parses a single XML file.<br>
     * If the file was successfully parsed, call {@link #parseDocument(Document, File)} for the parsed document.<br>
     * @param f the XML file to parse.
     */
    default void parseFile(File f)
    {
        if (!isXmlFile(f))
        {
            _log.warning(getClass().getSimpleName() + ": Could not parse " + f.getName() + " is not a file or it doesn't exist!");
            return;
        }
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        
        try
        {
            parseDocument(factory.newDocumentBuilder().parse(f), f);
        }
        catch (SAXParseException e)
        {
            _log.warning(getClass().getSimpleName() + ": Could not parse file " + f.getName() + " at line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e);
        }
        catch (Exception e)
        {
            _log.warning(getClass().getSimpleName() + ": Could not parse file " + f.getName() + ": " + e);
        }
    }
    
    /**
     * Abstract method that when implemented will parse the current document.<br>
     * Is expected to be call from {@link #parseFile(File)}.
     * @param doc the current document to parse
     * @param f the file that is currently being parsed
     */
    void parseDocument(Document doc, File f);
}
