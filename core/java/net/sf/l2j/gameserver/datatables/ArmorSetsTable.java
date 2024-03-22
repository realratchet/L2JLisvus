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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2ArmorSet;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * 
 *
 * @author  Luno
 */
public class ArmorSetsTable
{
    private static Logger _log = Logger.getLogger(ArmorSetsTable.class.getName());

    private final Map<Integer, L2ArmorSet> _armorSets = new HashMap<>();

    public static ArmorSetsTable getInstance()
    {
        return SingletonHolder._instance;
    }

    private ArmorSetsTable()
    {
    	load();
    }

    private void load()
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(new File(Config.DATAPACK_ROOT + "/data/armorSets.xml"));
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if ("list".equalsIgnoreCase(n.getNodeName()))
                {
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if (d.getNodeName().equalsIgnoreCase("armorSet"))
						{
							StatsSet set = new StatsSet(d.getAttributes());
							_armorSets.put(set.getInteger("chest", 0), new L2ArmorSet(set));
						}
					}
                }
            }

            _log.info(getClass().getSimpleName() + ": Loaded "+_armorSets.size()+" armor sets.");
        }
        catch (Exception e)
        {
            _log.severe(getClass().getSimpleName() + ": Error reading ArmorSets table: " + e);
        }
    }

    public boolean checkIfSetExists(int chestId)
    {
        return _armorSets.containsKey(chestId);
    }

    public L2ArmorSet getSet(int chestId)
    {
        return _armorSets.get(chestId);
    }
    
    private static class SingletonHolder
	{
		protected static final ArmorSetsTable _instance = new ArmorSetsTable();
	}
}