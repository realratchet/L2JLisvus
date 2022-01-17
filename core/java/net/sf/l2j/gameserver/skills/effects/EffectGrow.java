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

import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.skills.Env;

final class EffectGrow extends L2Effect
{
	public EffectGrow(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public EffectType getEffectType()
	{
		return EffectType.BUFF;
	}

	@Override
	public boolean onStart()
    {
		if (getEffected() instanceof L2NpcInstance)
		{
			L2NpcInstance npc = (L2NpcInstance) getEffected();
			npc.setCollisionRadius(npc.getCollisionRadius() * 1.19);
			getEffected().startAbnormalEffect(L2Character.ABNORMAL_EFFECT_GROW);
			return true;
		}
		return false;
	}

	@Override
	public void onExit()
    {
		if (getEffected() instanceof L2NpcInstance)
		{
			L2NpcInstance npc = (L2NpcInstance) getEffected();
			if (npc.getTemplate() != null)
			{
				npc.setCollisionRadius(npc.getTemplate().collisionRadius);
			}
			getEffected().stopAbnormalEffect(L2Character.ABNORMAL_EFFECT_GROW);
		}
	}

	@Override
	public boolean onActionTime()
    {
		return false;
	}
}