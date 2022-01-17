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
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeSummonInstance;
import net.sf.l2j.gameserver.network.serverpackets.BeginRotation;
import net.sf.l2j.gameserver.network.serverpackets.StopRotation;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author decad
 * 
 * Implementation of the Bluff Effect
 */
final class EffectBluff extends L2Effect
{
    public EffectBluff(Env env, EffectTemplate template)
    {
        super(env, template);
    }

    @Override
	public EffectType getEffectType()
    {
        return EffectType.BLUFF;
    }

    @Override
	public boolean onStart()
    {
        if (getEffected() instanceof L2FolkInstance)
            return false;

        if (getEffected() instanceof L2NpcInstance && ((L2NpcInstance)getEffected()).getNpcId() == 12024)
            return false;

        if (getEffected() instanceof L2SiegeSummonInstance)
            return false;

        getEffected().broadcastPacket(new BeginRotation(getEffected().getObjectId(), getEffected().getHeading(), 1, 65535));
        getEffected().broadcastPacket(new StopRotation(getEffected().getObjectId(), getEffector().getHeading(), 65535));
        getEffected().setHeading(getEffector().getHeading());
        getEffected().setTarget(null);
        getEffected().abortAttack();
        getEffected().abortCast();
        getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, getEffector());
        return true;
    }

    /**
     * 
     * @see net.sf.l2j.gameserver.model.L2Effect#onActionTime()
     */
    @Override
	public boolean onActionTime()
    {
        return false;
    }
}