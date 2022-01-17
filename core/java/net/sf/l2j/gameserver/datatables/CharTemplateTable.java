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
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.templates.L2PcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * This class ...
 * 
 * @version $Revision: 1.6.2.1.2.10 $ $Date: 2005/03/29 14:00:54 $
 */
public class CharTemplateTable
{
    private static Logger _log = Logger.getLogger(CharTemplateTable.class.getName());

    private final Map<Integer, String> _classList = new TreeMap<>();
    private final Map<Integer, L2PcTemplate> _templates = new TreeMap<>();
    
    public static CharTemplateTable getInstance()
    {
        return SingletonHolder._instance;
    }

    private CharTemplateTable()
    {
        try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(new File(Config.DATAPACK_ROOT + "/data/stats/classTemplates.xml"));
			
			for (Node li = doc.getFirstChild(); li != null; li = li.getNextSibling())
            {
				if (!li.getNodeName().equalsIgnoreCase("list"))
                {
                	continue;
                }
                
				for (Node cl = li.getFirstChild(); cl != null; cl = cl.getNextSibling())
				{
					if (!cl.getNodeName().equalsIgnoreCase("class"))
					{
						continue;
					}
					
					NamedNodeMap attrs = cl.getAttributes();
					StatsSet set = new StatsSet();
					
					// Class-specific stats
					set.set("classId", attrs.getNamedItem("id").getNodeValue());
					set.set("classBaseLevel", attrs.getNamedItem("level").getNodeValue());
					set.set("className", attrs.getNamedItem("name").getNodeValue());
					
					Node items = null;
					for (Node ch = cl.getFirstChild(); ch != null; ch = ch.getNextSibling())
					{
						if (!ch.getNodeName().equalsIgnoreCase("set"))
						{
							// Check if node is related to starting items
							if (ch.getNodeName().equalsIgnoreCase("items"))
							{
								items = ch;
							}
							continue;
						}
						
						attrs = ch.getAttributes();
						String name = attrs.getNamedItem("name").getNodeValue().trim();
						String value = attrs.getNamedItem("val").getNodeValue().trim();
						set.set(name, value);
					}

					L2PcTemplate ct = new L2PcTemplate(set);
					
					// Starting items
					if (items != null)
					{
						for (Node it = items.getFirstChild(); it != null; it = it.getNextSibling())
						{
							if (!it.getNodeName().equalsIgnoreCase("item"))
							{
								continue;
							}
							
							attrs = it.getAttributes();
							final int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue().trim());
							final int count = attrs.getNamedItem("count") != null ? Integer.parseInt(attrs.getNamedItem("count").getNodeValue().trim()) : 1;
							final boolean equipped = attrs.getNamedItem("equipped") != null ? Boolean.parseBoolean(attrs.getNamedItem("equipped").getNodeValue().trim()) : false;
							final int shortcutPage = attrs.getNamedItem("shortcutPage") != null ? Integer.parseInt(attrs.getNamedItem("shortcutPage").getNodeValue().trim()) : -1;
							final int shortcutSlot = attrs.getNamedItem("shortcutSlot") != null ? Integer.parseInt(attrs.getNamedItem("shortcutSlot").getNodeValue().trim()) : -1;
							
							// Add item
							ct.addItem(id, count, equipped, shortcutPage, shortcutSlot);
						}
					}
					
					// Add class and template
					_classList.put(ct.classId.getId(), ct.className);
	                _templates.put(ct.classId.getId(), ct);
				}
            }
			
			_log.config(getClass().getSimpleName() + ": Loaded " + _templates.size() + " Character Templates.");
		}
		catch (Exception e)
		{
			_log.severe(getClass().getSimpleName() + ": Error reading classTemplates.xml file: " + e.getMessage());
		}
    }

    public Map<Integer, String> getClassList()
    {
    	return _classList;
    }
    
    public String getClassNameById(int classId)
    {
    	return _classList.containsKey(classId) ? _classList.get(classId) : "";
    }
    
    public L2PcTemplate getTemplate(ClassId classId)
    {
        return getTemplate(classId.getId());
    }

    public L2PcTemplate getTemplate(int classId)
    {
        return _templates.get(classId);
    }
    
    private static class SingletonHolder
	{
		protected static final CharTemplateTable _instance = new CharTemplateTable();
	}
}