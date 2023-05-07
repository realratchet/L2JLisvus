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
package net.sf.l2j.gameserver.datatables.document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.templates.L2Item;

/**
 * @author mkizub
 */
public class DocumentEngine
{
    protected static Logger _log = Logger.getLogger(DocumentEngine.class.getName());
    
    private List<File> _itemFiles = new ArrayList<>();
    private List<File> _skillFiles = new ArrayList<>();
    
    public static DocumentEngine getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private DocumentEngine()
    {
        hashFiles("data/stats/items", _itemFiles);
        hashFiles("data/stats/skills", _skillFiles);
    }
    
    private void hashFiles(String dirname, List<File> hash)
    {
        File dir = new File(Config.DATAPACK_ROOT, dirname);
        
        if (!dir.exists())
        {
            _log.config("Dir " + dir.getAbsolutePath() + " not exists");
            return;
        }
        
        File[] files = dir.listFiles();
        for (File f : files)
        {
            if (f.getName().endsWith(".xml"))
            {
                if (!f.getName().startsWith("custom"))
                    hash.add(f);
            }
        }
        File customfile = new File(Config.DATAPACK_ROOT, dirname + "/custom.xml");
        if (customfile.exists())
            hash.add(customfile);
    }
    
    public List<L2Skill> loadSkills(File file)
    {
        if (file == null)
        {
            _log.config(getClass().getSimpleName() + ": Skill file not found.");
            return null;
        }
        DocumentSkill doc = new DocumentSkill(file);
        doc.parse();
        return doc.getSkills();
    }
    
    public void loadAllSkills(Map<Integer, L2Skill> allSkills)
    {
        int count = 0;
        for (File file : _skillFiles)
        {
            List<L2Skill> s = loadSkills(file);
            if (s == null)
                continue;
            
            for (L2Skill skill : s)
            {
                allSkills.put(SkillTable.getSkillHashCode(skill), skill);
                count++;
            }
        }
        _log.config(getClass().getSimpleName() + ": Loaded " + count + " skill templates from XML files.");
    }
    
    public List<L2Item> loadItems()
    {
        List<L2Item> list = new ArrayList<>();
        for (File f : _itemFiles)
        {
            DocumentItem document = new DocumentItem(f);
            document.parse();
            list.addAll(document.getItemList());
        }
        return list;
    }
    
    private static class SingletonHolder
    {
        protected static final DocumentEngine _instance = new DocumentEngine();
    }
}