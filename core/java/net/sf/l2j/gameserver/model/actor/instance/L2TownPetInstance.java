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
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;

/**
 * @author Kerberos
 */
public class L2TownPetInstance extends L2NpcInstance
{    
    private int _randomX, _randomY, _spawnX, _spawnY;

    public L2TownPetInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);

        if (Config.ALLOW_PET_WALKERS)
            ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new RandomWalkTask(), 2000, 4000);
    }

    @Override
    public void onInteract(L2PcInstance player)
    {
    	// Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
        player.sendPacket(new ActionFailed());
    }
    
    @Override
    public void onSpawn()
    {
        super.onSpawn();
        _spawnX = getX();
        _spawnY = getY();
    }
    public class RandomWalkTask implements Runnable
    {
        @Override
		public void run()
        {
            if (!isInActiveRegion())
                return; // but rather the AI should be turned off completely..

            _randomX = _spawnX + Rnd.get(2*50)-50;
            _randomY = _spawnY + Rnd.get(2*50)-50;
            setRunning();

            if ((_randomX != getX()) && (_randomY != getY()))
            {
            	getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(_randomX, _randomY, getZ()));
            }
        }
    }
}