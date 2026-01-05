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
package net.sf.l2j.gameserver.instancemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.scripting.L2ScriptEngineManager;
import net.sf.l2j.gameserver.scripting.ScriptManager;

public class QuestManager extends ScriptManager<Quest>
{
    private final static Logger _log = Logger.getLogger(QuestManager.class.getName());
    
    public static final QuestManager getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private final Map<String, Quest> _quests = new ConcurrentHashMap<>();
    private final Map<String, Quest> _otherScripts = new ConcurrentHashMap<>();
    
    public QuestManager()
    {
        _log.info("Initializing QuestManager");
    }
    
    public final boolean reload(String questFolder)
    {
        Quest q = getQuest(questFolder);
        if (q == null)
            return false;
        
        return q.reload();
    }
    
    /**
     * Reloads the quest given by questId.<BR>
     * <B>NOTICE: Will only work if the quest name is equal to the quest folder name</B>
     * @param questId The id of the quest to be reloaded
     * @return true if reload was successful, false otherwise
     */
    public final boolean reload(int questId)
    {
        Quest q = getQuest(questId);
        if (q == null)
            return false;
        
        return q.reload();
    }
    
    public final void reloadAllQuests()
    {
        _log.info("Reloading Server Scripts");
        
        // Unload all quests
        for (Quest quest : _quests.values())
        {
            if (quest != null)
                quest.unload(false);
        }
        _quests.clear();
        
        // Unload all other scripts
        for (Quest script : _otherScripts.values())
        {
            if (script != null)
                script.unload(false);
        }
        _otherScripts.clear();
        
        // Preconfigure jython path
        L2ScriptEngineManager.getInstance().preConfigure();
        
        // now load all scripts
        File scripts = new File(Config.DATAPACK_ROOT + "/data/scripts.cfg");
        L2ScriptEngineManager.getInstance().executeScriptList(scripts);
        
        report();
    }
    
    public final void report()
    {
        _log.info("Loaded: " + _quests.size() + " quests");

        if (!_otherScripts.isEmpty())
        {
            _log.info("Loaded: " + _otherScripts.size() + " scripts");
        }
    }
    
    public final void save()
    {
        for (Quest q : _quests.values())
        {
            // Cancel all pending quest timers
            q.cancelAllQuestTimers();
            
            // Save global data if any
            q.saveGlobalData();
        }
        
        for (Quest s : _otherScripts.values())
        {
            // Cancel all pending script timers
            s.cancelAllQuestTimers();
            
            // Save global data if any
            s.saveGlobalData();
        }
    }
    
    public final Quest getQuest(String name)
    {
        if (_quests.containsKey(name))
        {
            return _quests.get(name);
        }
        return _otherScripts.get(name);
    }
    
    public final Quest getQuest(int questId)
    {
        for (Quest q : _quests.values())
        {
            if (q.getQuestIntId() == questId)
                return q;
        }
        return null;
    }
    
    public final void addScript(Quest newScript)
    {
        if (newScript == null)
        {
            throw new IllegalArgumentException("Script argument cannot be null");
        }
        
        Map<String, Quest> scripts = newScript.getQuestIntId() != 0 ? _quests : _otherScripts;
        Quest old = scripts.get(newScript.getName());
        
        // FIXME: unloading the old quest at this point is a tad too late.
        // the new quest has already initialized itself and read the data, starting
        // an unpredictable number of tasks with that data. The old quest will now
        // save data which will never be read.
        // However, requesting the newQuest to re-read the data is not necessarily a
        // good option, since the newQuest may have already started timers, spawned NPCs
        // or taken any other action which it might re-take by re-reading the data.
        // the current solution properly closes the running tasks of the old quest but
        // ignores the data; perhaps the least of all evils...
        if (old != null)
        {
            old.unload(true);
            _log.info("Replaced: (" + old.getName() + ") with a new version (" + newScript.getName() + ")");
        }
        scripts.put(newScript.getName(), newScript);
    }
    
    public final void removeScript(Quest script)
    {
        if (_quests.containsKey(script.getName()))
        {
            _quests.remove(script.getName());
        }
        else if (_otherScripts.containsKey(script.getName()))
        {
            _otherScripts.remove(script.getName());
        }
    }
    
    /**
     * @see net.sf.l2j.gameserver.scripting.ScriptManager#getAllManagedScripts()
     */
    @Override
    public Iterable<Quest> getAllManagedScripts()
    {
        List<Quest> scripts = new ArrayList<>();

        scripts.addAll(_quests.values());
        scripts.addAll(_otherScripts.values());
        return scripts;
    }

    public Map<String, Quest> getQuests()
    {
        return _quests;
    }
    
    public Map<String, Quest> getOtherScripts()
    {
        return _otherScripts;
    }

    /**
     * @see net.sf.l2j.gameserver.scripting.ScriptManager#unload(net.sf.l2j.gameserver.scripting.ManagedScript)
     */
    @Override
    public void unload(Quest ms)
    {
        ms.saveGlobalData();
        removeScript(ms);
    }
    
    /**
     * @see net.sf.l2j.gameserver.scripting.ScriptManager#getScriptManagerName()
     */
    @Override
    public String getScriptManagerName()
    {
        return "QuestManager";
    }
    
    private static class SingletonHolder
    {
        protected static final QuestManager _instance = new QuestManager();
    }
}