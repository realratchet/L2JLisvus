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
package net.sf.l2j.gameserver.skills.effects;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author littlecrow
 *
 * Implementation of the Confusion Effect
 */
final class EffectConfuseMob extends L2Effect
{
    public EffectConfuseMob(Env env, EffectTemplate template)
    {
        super(env, template);
    }

    @Override
	public EffectType getEffectType()
    {
        return EffectType.CONFUSE_MOB_ONLY;
    }

    /** Notify started */
    @Override
	public boolean onStart()
    {
        getEffected().startConfused();
        onActionTime();
        return true;
    }

    /** Notify exited */
    @Override
	public void onExit()
    {
        getEffected().stopConfused(false);
    }

    @Override
	public boolean onActionTime()
    {
        List<L2Character> targetList = new ArrayList<>();

        // Getting the possible targets
        for (L2Object obj : getEffected().getKnownList().getKnownObjects().values())
        {
            if (obj instanceof L2Attackable && obj != getEffected() && Util.checkIfInShortRadius(600, getEffected(), obj, true))
                targetList.add((L2Character)obj);
        }

        // if there is no target, exit function
        if (targetList.isEmpty())
            return true;

        // Choosing randomly a new target
        int nextTargetIdx = Rnd.nextInt(targetList.size());
        L2Object target = targetList.get(nextTargetIdx);

        // Attacking the target
        getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
    	return true;
    }
}