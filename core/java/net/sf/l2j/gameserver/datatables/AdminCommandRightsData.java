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
package net.sf.l2j.gameserver.datatables;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author DnR
 *
 */
public class AdminCommandRightsData
{
	private static Logger _log = Logger.getLogger(AdminCommandRightsData.class.getName());

    private final Map<String, Integer> _commandRights = new ConcurrentHashMap<>();

    public static AdminCommandRightsData getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private AdminCommandRightsData()
    {
    	load();
    }
    
    public void load()
    {
    	_commandRights.clear();
    	
    	try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(new File(Config.DATAPACK_ROOT + "/data/adminCommandRights.xml"));
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if ("list".equalsIgnoreCase(n.getNodeName()))
                {
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if (d.getNodeName().equalsIgnoreCase("command"))
						{
							NamedNodeMap attrs = d.getAttributes();
							
							String name = attrs.getNamedItem("name").getNodeValue();
							int val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
							
							_commandRights.put(name, val);
						}
					}
                }
            }
			_log.info(getClass().getSimpleName() + ": Loaded " + _commandRights.size() + " admin command rights.");
		}
		catch (Exception e)
		{
			_log.severe(getClass().getSimpleName() + ": Error reading adminCommandRights.xml file: " + e);
		}
    }
    
    /**
     * Checks if player can use given command.
     * 
     * @param activeChar
     * @param adminCommand
     * @return
     */
    public final boolean checkAccess(L2PcInstance activeChar, String adminCommand)
    {
    	// Character is not GM
    	if (!activeChar.isGM())
    	{
    		return false;
    	}
    	
    	String command = adminCommand.split(" ")[0];
        int requiredLevel = _commandRights.containsKey(command) ? _commandRights.get(command) : Config.MASTER_ACCESS_LEVEL;
        
        // Check if player can use command
        if (activeChar.getAccessLevel() < requiredLevel)
        {
           return false;
        }
        
        return true;
    }
    
    private static class SingletonHolder
	{
		protected static final AdminCommandRightsData _instance = new AdminCommandRightsData();
	}
}
