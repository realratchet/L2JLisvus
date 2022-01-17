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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2HennaInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.templates.L2Henna;

/**
 * This class ...
 * 
 * @version $Revision$ $Date$
 */
public class HennaTreeTable
{
    private static Logger _log = Logger.getLogger(HennaTreeTable.class.getName());
    
    private final Map<ClassId, List<L2HennaInstance>> _hennaTrees = new HashMap<>();

    public static HennaTreeTable getInstance()
    {
        return SingletonHolder._instance;
    }

    private HennaTreeTable()
    {
        int count = 0;
        int lastClassId = 0;

        Set<Integer> classIds = CharTemplateTable.getInstance().getClassList().keySet();
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            List<L2HennaInstance> list;
            for (int classId : classIds)
            {
            	lastClassId = classId;
            	
                list = new ArrayList<>();
                try (PreparedStatement statement2 = con.prepareStatement("SELECT class_id, symbol_id FROM henna_trees where class_id=? ORDER BY symbol_id"))
                {
                    statement2.setInt(1, classId);
                    try (ResultSet hennatree = statement2.executeQuery())
                    {
                        while (hennatree.next())
                        {
                            int id = hennatree.getInt("symbol_id");
                            L2Henna template = HennaTable.getInstance().getTemplate(id);
                            if (template == null)
                                continue;

                            L2HennaInstance temp = new L2HennaInstance(template);
                            temp.setSymbolId(id);
                            temp.setItemIdDye(template.itemId);
                            temp.setAmountDyeRequire(template.amount);
                            temp.setPrice(template.price);
                            temp.setStatINT(template.statINT);
                            temp.setStatSTR(template.statSTR);
                            temp.setStatCON(template.statCON);
                            temp.setStatMEN(template.statMEN);
                            temp.setStatDEX(template.statDEX);
                            temp.setStatWIT(template.statWIT);

                            list.add(temp);
                        }
                    }
                }

                _hennaTrees.put(ClassId.values()[classId], list);
                count   += list.size();
                _log.fine("Henna Tree for Class: " + classId + " has " + list.size() + " Henna Templates.");		
            }
        }
        catch (Exception e)
        {
            _log.warning("Error while creating henna tree for classId " + lastClassId + ". Reason: " + e);
        }

        _log.config("HennaTreeTable: Loaded " + count + " Henna Tree Templates.");
    }

    public L2HennaInstance[] getAvailableHenna(ClassId classId)
    {
        List<L2HennaInstance> result = new ArrayList<>();
        List<L2HennaInstance> henna = _hennaTrees.get(classId);

        if (henna == null)
        {
            // The henna tree for this class is undefined, so we give an empty list
            _log.warning("Henna Tree for class " + classId + " is not defined!");
            return new L2HennaInstance[0];
        }

        for (int i = 0; i < henna.size(); i++)
        {
            L2HennaInstance temp = henna.get(i);
            result.add(temp);
        }

        return result.toArray(new L2HennaInstance[result.size()]);
    }
    
    private static class SingletonHolder
	{
		protected static final HennaTreeTable _instance = new HennaTreeTable();
	}
}