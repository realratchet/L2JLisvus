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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.templates.L2SoulCrystal;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author DnR
 */
public class SoulCrystalData
{
	private static Logger _log = Logger.getLogger(SoulCrystalData.class.getName());
	
	public static final int BREAK_CHANCE = 10;
	public static final int LEVEL_CHANCE = 32;
	
	private final List<L2SoulCrystal> _soulCrystals = new ArrayList<>();
	
	public static SoulCrystalData getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private SoulCrystalData()
	{
		load();
	}
	
	public void load()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(new File(Config.DATAPACK_ROOT + "/data/soulCrystals.xml"));
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if (d.getNodeName().equalsIgnoreCase("item"))
						{
							StatsSet set = new StatsSet();
							NamedNodeMap attrs = d.getAttributes();
							int length = attrs.getLength();
							
							for (int i = 0; i < length; i++)
							{
								Node item = attrs.item(i);
								if (item != null)
								{
									set.set(item.getNodeName(), item.getNodeValue());
								}
							}
							_soulCrystals.add(new L2SoulCrystal(set));
						}
					}
				}
			}
			_log.info(getClass().getSimpleName() + ": Loaded " + _soulCrystals.size() + " soul crystals.");
		}
		catch (Exception e)
		{
			_log.severe(getClass().getSimpleName() + ": Error reading soulCrystals.xml file: " + e);
		}
	}
	
	public List<L2SoulCrystal> getSoulCrystals()
	{
		return _soulCrystals;
	}
	
	private static class SingletonHolder
	{
		protected static final SoulCrystalData _instance = new SoulCrystalData();
	}
}
