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

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.util.Util;

/**
 * @author mkizub
 */
public class ConditionTargetRace extends Condition
{
	private final Race[] _races;
	
	public ConditionTargetRace(Race[] races)
	{
		_races = races;
	}
	
	@Override
	public boolean testImpl(Env env, Object owner)
	{
		if (!(env.player instanceof L2PcInstance))
		{
			return false;
		}
		
		return Util.contains(_races, env.player.getActingPlayer().getRace());
	}
}