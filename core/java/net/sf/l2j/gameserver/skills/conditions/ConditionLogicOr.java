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

import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConditionLogicOr extends Condition
{
	private static Condition[] emptyConditions = new Condition[0]; 
	public Condition[] _conditions = emptyConditions;
	
	public void add(Condition condition)
	{
		if (condition == null)
			return;
		if (getListener() != null)
			condition.setListener(this);
		final int len = _conditions.length; 
		final Condition[] tmp = new Condition[len+1];
		System.arraycopy(_conditions, 0, tmp, 0, len);
		tmp[len] = condition;
		_conditions = tmp;
	}
	
	@Override
	protected void setListener(ConditionListener listener)
	{
		if (listener != null) {
			for (Condition c : _conditions)
				c.setListener(this);
		} else {
			for (Condition c : _conditions)
				c.setListener(null);
		}
		super.setListener(listener);
	}
	
	@Override
	public boolean testImpl(Env env, Object owner)
	{
		for (Condition c : _conditions)
		{
			if (c.test(env, owner))
				return true;
		}
		return false;
	}

}
