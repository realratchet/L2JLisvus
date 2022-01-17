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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2ExtractableItem;
import net.sf.l2j.gameserver.model.L2ExtractableProductItem;

/**
 * 
 * @author FBIagent
 * 
 */
public class ExtractableItemsData
{
	private static Logger _log = Logger.getLogger(ExtractableItemsData.class.getName());

    private Map<Integer, L2ExtractableItem> _items;

    public static ExtractableItemsData getInstance()
    {
        return SingletonHolder._instance;
    }

    public ExtractableItemsData()
    {
        _items = new HashMap<>();

        try (Scanner s = new Scanner(new File(Config.DATAPACK_ROOT + "/data/extractable_items.csv")))
        {
            int lineCount = 0;

            while (s.hasNextLine())
            {
                lineCount++;

                String line = s.nextLine();
                if (line.isEmpty())
                    continue;
                if (line.startsWith("#"))
                    continue;

                String[] lineSplit = line.split(";");
                boolean ok = true;
                int itemID = 0;

                try
                {
            	    itemID = Integer.parseInt(lineSplit[0]);
                }
                catch (Exception e)
                {
                    _log.warning(getClass().getSimpleName() + ": Error in line " + lineCount + " -> invalid item id or wrong separator after item id!");
                    _log.warning(line);
            	    ok = false;            	
                }

                if (!ok)
            	    continue;

                List<L2ExtractableProductItem> productTemp = new ArrayList<>(); 

                for (int i = 0; i < lineSplit.length-1; i++)
                {
            	    ok = true;

            	    String[] lineSplit2 = lineSplit[i+1].split(",");

                    if (lineSplit2.length < 3)
                    {
                    	_log.warning(getClass().getSimpleName() + ": Error in line " + lineCount + " -> wrong separator!");
                    	_log.warning(line);
                        ok = false;
                    }

                    if (!ok)
                    {
                        continue;
                    }

                    int[] production = null;
    				int[] amount = null;
    				int chance = 0;

            	    try
            	    {
            	    	int k = 0;
            	    	int count = lineSplit2.length - 1;
    					production = new int[count / 2];
    					amount = new int[count / 2];
    					for (int j = 0; j < count; j++)
    					{
    						production[k] = Integer.parseInt(lineSplit2[j]);
    						amount[k] = Integer.parseInt(lineSplit2[j += 1]);
    						k++;
    					}

    					chance = Integer.parseInt(lineSplit2[count]);
            	    }
            	    catch (Exception e)
            	    {
            	    	_log.warning(getClass().getSimpleName() + ": Error in line " + lineCount + " -> incomplete/invalid production data or wrong seperator!");
            	    	_log.warning("		" + line);
                        ok = false;
            	    }

            	    if (!ok)
            	    {
                        continue;
            	    }

            	    L2ExtractableProductItem product = new L2ExtractableProductItem(production, amount, chance);
            	    productTemp.add(product);            	
                }

                int fullChances = 0;

                for (L2ExtractableProductItem pi : productTemp)
                {
                	fullChances += pi.getChance();
                }

                if (fullChances > 100)
                {
                	_log.warning(getClass().getSimpleName() + ": Error in line " + lineCount + " -> all chances together are more than 100!");
                	_log.warning("		" + line);
            	    continue;
                }
                
                L2ExtractableItem product = new L2ExtractableItem(itemID, productTemp);
                _items.put(itemID, product);
            }

            _log.config(getClass().getSimpleName() + ": Loaded " + _items.size() + " extractable items!");
        }
        catch (Exception e)
        {
        	_log.warning(getClass().getSimpleName() + ": Cannot find '" + Config.DATAPACK_ROOT + "/data/extractable_items.csv'");
        }
    }

    public L2ExtractableItem getExtractableItem(int itemID)
    {
        return _items.get(itemID);
    }

    public int[] itemIDs()
    {
        int size = _items.size();
        int[] result = new int[size];
        int i = 0;
        for (L2ExtractableItem ei : _items.values())
        {
            result[i] = ei.getItemId();
            i++;
        }
        return result;
    }
    
    private static class SingletonHolder
	{
		protected static final ExtractableItemsData _instance = new ExtractableItemsData();
	}
}