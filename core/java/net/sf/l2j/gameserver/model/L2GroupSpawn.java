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
package net.sf.l2j.gameserver.model;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.Territory;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2ControllableMobInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;

/**
 * @author littlecrow
 * A special spawn implementation to spawn controllable mob
 */
public class L2GroupSpawn extends L2Spawn 
{
    private L2NpcTemplate _template;
	
    public L2GroupSpawn(L2NpcTemplate mobTemplate) throws SecurityException, ClassNotFoundException, NoSuchMethodException
    {
        super(mobTemplate);
        _template = mobTemplate;

        setAmount(1);
    }

    public L2NpcInstance doGroupSpawn() 
    {
        try
        {
            if (_template.type.equalsIgnoreCase("L2Pet") || 
                    _template.type.equalsIgnoreCase("L2Minion"))
                return null;

            int newlocx, newlocy, newlocz;

            if  (getLocX() == 0 && getLocY() == 0)
            {
                if (getLocation() == 0) 
                    return null;

                int p[] = Territory.getInstance().getRandomPoint(getLocation());
                newlocx = p[0];
                newlocy = p[1];
                newlocz = p[2];
            } 
            else 
            {
                newlocx = getLocX();
                newlocy = getLocY();
                newlocz = getLocZ();
            }

            final L2NpcInstance mob = new L2ControllableMobInstance(IdFactory.getInstance().getNextId(), _template);

            mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp());

            if (getHeading() == -1)
                mob.setHeading(Rnd.nextInt(61794));	
            else 
                mob.setHeading(getHeading());

            mob.setSpawn(this);
            mob.spawnMe(newlocx, newlocy, newlocz);
            mob.onSpawn();

            if (Config.DEBUG) 
                _log.finest("spawned Mob ID: "+_template.npcId+" ,at: "+mob.getX()+" x, "+mob.getY()+" y, "+mob.getZ()+" z");

            return mob;
        }
        catch (Exception e)
        {
            _log.warning("NPC class not found: " + e);
            return null;
        }		
    }
}