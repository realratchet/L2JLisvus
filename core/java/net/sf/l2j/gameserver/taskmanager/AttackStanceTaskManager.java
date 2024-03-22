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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2CubicInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.AutoAttackStop;

/**
 * This class ...
 * @version $Revision: $ $Date: $
 * @author Luca Baldi
 */
public class AttackStanceTaskManager
{
    protected static Logger _log = Logger.getLogger(AttackStanceTaskManager.class.getName());
    
    protected final Map<L2Character, Long> _attackStanceTasks = new ConcurrentHashMap<>();
    
    public AttackStanceTaskManager()
    {
        ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new FightModeScheduler(), 0, 1000);
    }
    
    public static AttackStanceTaskManager getInstance()
    {
        return SingletonHolder._instance;
    }
    
    public void addAttackStanceTask(L2Character actor)
    {
        if (actor instanceof L2Summon)
        {
            actor = ((L2Summon) actor).getOwner();
        }
        
        // Cubic interaction
        if (actor instanceof L2PcInstance)
        {
            L2PcInstance player = (L2PcInstance) actor;
            for (L2CubicInstance cubic : player.getCubics())
            {
                if (cubic.getId() != L2CubicInstance.LIFE_CUBIC)
                {
                    cubic.doAction();
                }
            }
        }
        
        _attackStanceTasks.put(actor, System.currentTimeMillis());
    }
    
    public void removeAttackStanceTask(L2Character actor)
    {
        if (actor instanceof L2Summon)
        {
            L2Summon summon = (L2Summon) actor;
            actor = summon.getOwner();
        }
        _attackStanceTasks.remove(actor);
    }
    
    public boolean getAttackStanceTask(L2Character actor)
    {
        if (actor instanceof L2Summon)
        {
            L2Summon summon = (L2Summon) actor;
            actor = summon.getOwner();
        }
        return _attackStanceTasks.containsKey(actor);
    }
    
    private class FightModeScheduler implements Runnable
    {
        @Override
        public void run()
        {
            long current = System.currentTimeMillis();
            try
            {
                final Iterator<Entry<L2Character, Long>> iter = _attackStanceTasks.entrySet().iterator();
                Entry<L2Character, Long> e;
                L2Character actor;
                
                while (iter.hasNext())
                {
                    e = iter.next();
                    if ((current - e.getValue()) > 15000)
                    {
                        actor = e.getKey();
                        if (actor != null)
                        {
                            actor.broadcastPacket(new AutoAttackStop(actor.getObjectId()));
                            if (actor instanceof L2PcInstance && ((L2PcInstance) actor).getPet() != null)
                            {
                                actor.getPet().broadcastPacket(new AutoAttackStop(actor.getPet().getObjectId()));
                            }
                            
                            actor.getAI().setAutoAttacking(false);
                        }
                        
                        iter.remove();
                    }
                }
            }
            catch (Exception e)
            {
                // Unless caught here, players remain in attack positions
                _log.warning(e.toString());
            }
        }
    }
    
    private static class SingletonHolder
    {
        protected static final AttackStanceTaskManager _instance = new AttackStanceTaskManager();
    }
}
