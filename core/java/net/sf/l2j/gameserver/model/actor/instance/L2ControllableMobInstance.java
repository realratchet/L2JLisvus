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
import net.sf.l2j.gameserver.ai.L2ControllableMobAI;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author littlecrow
 *
 */
public class L2ControllableMobInstance extends L2MonsterInstance
{
	protected class ControllableAIAcessor extends AIAccessor 
    {
		@Override
		public void detachAI() 
        {
			// do nothing, AI of controllable mobs can't be detached automatically
		}
	}

	@Override
	public boolean isAggressive()
    {
		return true;
	}

	@Override
	public int getAggroRange() 
    {
		// force mobs to be aggro
		return 500;
	}

	public L2ControllableMobInstance(int objectId, L2NpcTemplate template) 
    {
		super(objectId, template);
	}

	@Override
	public L2ControllableMobAI initAI()
	{
		return new L2ControllableMobAI(new ControllableAIAcessor());
	}

	public void reduceCurrentHp(double i, L2Character attacker, boolean awake) 
    {
		if (isInvul() || isDead())
			return;
		
		if (awake)
		{
			stopSleeping(true);
		}
		
		i = getCurrentHp() - i;
		
		if (i < 0)
			i = 0;
		
		setCurrentHp(i);
		
		if (getCurrentHp() < 0.5)
		{
			// first die (and calculate rewards), if currentHp < 0,
			// then overhit may be calculated
			if (Config.DEBUG) _log.fine("char is dead.");
			
			stopMove(null);
			
			// Start the doDie process
			doDie(attacker);
			
			// now reset currentHp to zero
			setCurrentHp(0);
		}
	}

	@Override
	public boolean doDie(L2Character killer) 
    {
        if (!super.doDie(killer))
            return false;

		setAI(null);
        return true;
	}

	@Override
	public void deleteMe() 
    {
		setAI(null);
		super.deleteMe();
	}
}