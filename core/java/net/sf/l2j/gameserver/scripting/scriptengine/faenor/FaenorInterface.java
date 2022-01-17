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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.EventDroplist;
import net.sf.l2j.gameserver.model.L2DropCategory;
import net.sf.l2j.gameserver.model.L2DropData;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.scripting.scriptengine.DateRange;
import net.sf.l2j.gameserver.scripting.scriptengine.EngineInterface;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author Luis Arias
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FaenorInterface implements EngineInterface
{
	private static final Logger _log = Logger.getLogger(FaenorInterface.class.getName());
    
    public static FaenorInterface getInstance()
    {
        return SingletonHolder._instance;
    }
    
    public FaenorInterface()
    {
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.script.EngineInterface#getAllPlayers()
     */
    public List<L2PcInstance> getAllPlayers()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 
     * Adds a new Quest Drop to an NPC
     * 
     * @see net.sf.l2j.gameserver.scripting.scriptengine.EngineInterface#addQuestDrop(int, int, int, int, int, String, String[])
     */
    @Override
	public void addQuestDrop(int npcID, int itemID, int min, int max, int chance, String questID, String[] states)
    {
        L2NpcTemplate npc = _npcTable.getTemplate(npcID);
        if (npc == null)
        {
            throw new NullPointerException();
        }
        L2DropData drop = new L2DropData();
        drop.setItemId(itemID);
        drop.setMinDrop(min);
        drop.setMaxDrop(max);
        drop.setChance(chance);
        drop.setQuestID(questID);
        drop.addStates(states);
        addDrop(npc, drop, false);
    }

    /**
     * 
     * Adds a new Drop to an NPC
     * @param npcID 
     * @param itemID 
     * @param min 
     * @param max 
     * @param sweep 
     * @param chance 
     * @throws NullPointerException 
     * 
     * @see net.sf.l2j.gameserver.scripting.scriptengine.EngineInterface#addQuestDrop(int, int, int, int, int, String, String[])
     */
    public void addDrop(int npcID, int itemID, int min, int max, boolean sweep, int chance) throws NullPointerException
    {
        L2NpcTemplate npc = _npcTable.getTemplate(npcID);
        if (npc == null)
        {
            if (Config.DEBUG)
            	_log.info("NPC does not Exist");
            throw new NullPointerException();
        }
        L2DropData drop = new L2DropData();
        drop.setItemId(itemID);
        drop.setMinDrop(min);
        drop.setMaxDrop(max);
        drop.setChance(chance);

        addDrop(npc, drop, sweep);
    }
    
	/**
	 * Adds a new drop to an NPC.  If the drop is sweep, it adds it to the NPC's Sweep category
	 * If the drop is non-sweep, it creates a new category for this drop. 
	 *  
	 * @param npc
	 * @param drop
	 * @param sweep
	 */
    public void addDrop(L2NpcTemplate npc, L2DropData drop, boolean sweep)
    {
    	if(sweep)
    		addDrop(npc, drop,-1);
    	else
    	{
        	int maxCategory = -1;

            if (npc.getDropData() != null)
            {
	    	    for (L2DropCategory cat : npc.getDropData())
	    	    {
	    	    	if (maxCategory<cat.getCategoryType())
	                    maxCategory = cat.getCategoryType();
	    	    }
            }
	    	maxCategory++;
	    	npc.addDropData(drop, maxCategory);
    	}
    }

	/**
	 * Adds a new drop to an NPC, in the specified category.  If the category does not exist, 
	 * it is created.  
	 *  
	 * @param npc
	 * @param drop
	 * @param category
	 */
    public void addDrop(L2NpcTemplate npc, L2DropData drop, int category)
    {
    	npc.addDropData(drop, category);
    }

    /**
     * @param npcID 
     * @return Returns the _questDrops.
     */
    public List<L2DropData> getQuestDrops(int npcID)
    {
        L2NpcTemplate npc = _npcTable.getTemplate(npcID);
        if (npc == null)
        {
            return null;
        }

        List<L2DropData> questDrops = new ArrayList<>();
        if (npc.getDropData() != null)
        {
            for (L2DropCategory cat : npc.getDropData())
            {
                for (L2DropData drop : cat.getAllDrops())
                {
                    if (drop.getQuestID() != null)
                        questDrops.add(drop);
                }
            }
        }
        return questDrops;
    }
    
    @Override
	public void addEventDrop(int[] items, int[] count, double chance, DateRange range)
    {
        EventDroplist.getInstance().addGlobalDrop(items, count, (int)(chance * L2DropData.MAX_CHANCE), range);
    }
    
    @Override
	public void onPlayerLogin(DateRange validDateRange)
    {
        Announcements.getInstance().addEventAnnouncement(validDateRange);
    }
    
    private static class SingletonHolder
	{
		protected static final FaenorInterface _instance = new FaenorInterface();
	}
}