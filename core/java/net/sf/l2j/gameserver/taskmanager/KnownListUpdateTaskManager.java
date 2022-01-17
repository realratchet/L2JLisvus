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
package net.sf.l2j.gameserver.taskmanager;

import java.util.Collection;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2WorldRegion;
import net.sf.l2j.gameserver.model.actor.instance.L2GuardInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;

public class KnownListUpdateTaskManager
{
    protected static final Logger _log = Logger.getLogger(KnownListUpdateTaskManager.class.getName());

    private static final int FULL_UPDATE_TIMER = 100;
    
    private boolean updatePass = true;

    // Do full update every FULL_UPDATE_TIMER * KNOWNLIST_UPDATE_INTERVAL
    private int _fullUpdateTimer = FULL_UPDATE_TIMER;
    
    public static KnownListUpdateTaskManager getInstance()
    {
        return SingletonHolder._instance;
    }
    
    public KnownListUpdateTaskManager()
    {
    	ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new KnownListUpdate(), 1000, Config.KNOWNLIST_UPDATE_INTERVAL);
    }

    private class KnownListUpdate implements Runnable
    {
    	protected KnownListUpdate()
    	{
            // Do nothing
    	}

        @Override
		public void run()
        {
            try
            {
                for (L2WorldRegion regions[] : L2World.getInstance().getAllWorldRegions())
                {
                    for (L2WorldRegion r : regions) // go through all world regions
                    {
                        try
                        {
                            if (r.isActive()) // and check only if the region is active
                                updateRegion(r, (_fullUpdateTimer == FULL_UPDATE_TIMER), updatePass);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                updatePass = !updatePass;
                if (_fullUpdateTimer > 0)
                    _fullUpdateTimer--;
                else
                    _fullUpdateTimer = FULL_UPDATE_TIMER;
            }
            catch (Exception e)
            {
                _log.warning(e.toString());
            }
        }
    }

    public void updateRegion(L2WorldRegion region, boolean fullUpdate, boolean forgetObjects)
    {
    	Collection<L2Object> allVisibleObjects = region.getVisibleObjects().values();
        for (L2Object object : allVisibleObjects) // and for all members in region
        {
            if (object == null || !object.isVisible())
                continue;   // skip dying objects

            if (forgetObjects)
            {
                object.getKnownList().forgetObjects((Config.GUARD_ATTACK_AGGRO_MOB && object instanceof L2GuardInstance) || fullUpdate);
                continue;
            }

            if (object instanceof L2PlayableInstance || (Config.GUARD_ATTACK_AGGRO_MOB && object instanceof L2GuardInstance) || fullUpdate)
            {
                for (L2WorldRegion regi : region.getSurroundingRegions()) // offer members of this and surrounding regions
                {
                	Collection<L2Object> otherVisibleObjects = regi.getVisibleObjects().values();
                    for (L2Object obj : otherVisibleObjects) 
                    {
                        if (obj != object)
                            object.getKnownList().addKnownObject(obj);
                    }
                }
            }
            else if (object instanceof L2Character)
            {
                for (L2WorldRegion regi : region.getSurroundingRegions()) // offer members of this and surrounding regions
                {
                    if (regi.isActive())
                    {
                    	Collection<L2PlayableInstance> otherVisiblePlayables = regi.getVisiblePlayable().values();
                        for (L2Object obj : otherVisiblePlayables)
                        {
                            if (obj != object)
                                object.getKnownList().addKnownObject(obj);
                        }
                    }
                }
            }
        }
    }
    
    private static class SingletonHolder
	{
		protected static final KnownListUpdateTaskManager _instance = new KnownListUpdateTaskManager();
	}
}