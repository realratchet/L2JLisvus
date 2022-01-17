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
package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.effects.EffectSeed;

/**
 * @author Advi
 *
 */
public class ConditionElementSeed extends Condition
{
    static int[] seedSkills = {1285, 1286, 1287};
    final int[] _requiredSeeds;

    public ConditionElementSeed(int[] seeds)
    {
        _requiredSeeds = seeds;
    }
    
    ConditionElementSeed(int fire, int water, int wind, int various, int any)
    {
        _requiredSeeds = new int[5];
        _requiredSeeds[0] = fire;
        _requiredSeeds[1] = water;
        _requiredSeeds[2] = wind;
        _requiredSeeds[3] = various;
        _requiredSeeds[4] = any;
    }
    
    @Override
	public boolean testImpl(Env env, Object owner)
    {
        int[] seeds = new int[3];
        for (int i = 0; i < seeds.length; i++)
        {
            L2Effect effect = env.player.getFirstEffect(seedSkills[i]);
            seeds[i] = (effect instanceof EffectSeed ? ((EffectSeed)effect).getPower() : 0);
            if (seeds[i] >= _requiredSeeds[i]) 
                seeds[i] -= _requiredSeeds[i];
            else
                return false;
        }

        if (_requiredSeeds[3] > 0)
        {
            int count = 0;
            for (int i = 0; i < seeds.length && count < _requiredSeeds[3]; i++)
            {
                if (seeds[i] > 0)
                {
                    seeds[i]--;
                    count++;
                }
            }

            if (count < _requiredSeeds[3])
                return false;
        }

        if (_requiredSeeds[4] > 0)
        {
            int count = 0;
            for (int i = 0; i < seeds.length && count < _requiredSeeds[4]; i++)
            {
                count += seeds[i];
            }
            if (count < _requiredSeeds[4])
                return false;
        }

        return true;
    }
}