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

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;

class EffectRelax extends L2Effect
{		
    public EffectRelax(Env env, EffectTemplate template)
    {
        super(env, template);
    }

    @Override
	public EffectType getEffectType()
    {
        return EffectType.RELAXING;
    }

    /** Notify started */
    @Override
	public boolean onStart()
    {
        if (getEffected().getCurrentHp() == getEffected().getMaxHp())
        {
            if (getSkill().isToggle())
            {
                getEffected().sendPacket(new SystemMessage(175));
                return false;
            }
        }

        if (getEffected() instanceof L2PcInstance)
        {
            ((L2PcInstance)getEffected()).sitDown(false);
        }
        else
        {
        	getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_REST);
        }
        return super.onStart();
    }

    @Override
	public void onExit()
    {
        super.onExit();
    }

    @Override
	public boolean onActionTime()
    {
        if (getEffected().isDead())
            return false;
        if (!((L2PcInstance)getEffected()).isSitting())
            return false;
        if (getEffected().getCurrentHp() == getEffected().getMaxHp())
        {
            if (getSkill().isToggle())
            {
                getEffected().sendPacket(new SystemMessage(175));
                return false;
            }
        }

        double manaDam = calc();
        if (manaDam > getEffected().getCurrentMp())
        {
            if (getSkill().isToggle())
            {
                getEffected().sendPacket(new SystemMessage(140));
                return false;
            }
        }
        getEffected().reduceCurrentMp(manaDam);
        return true;
    }
}