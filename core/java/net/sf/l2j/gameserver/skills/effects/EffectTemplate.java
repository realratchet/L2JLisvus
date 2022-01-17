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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.funcs.FuncTemplate;
import net.sf.l2j.gameserver.skills.funcs.Lambda;

/**
 * @author mkizub
 *
 */
public final class EffectTemplate
{
    private static final Logger _log = Logger.getLogger(EffectTemplate.class.getName());

	private final Class<?> _func;
	private final Constructor<?> _constructor;

	public final Condition _attachCond;
	public final Condition _applyCond;
	public final Lambda _lambda;
	public final int _counter;
	public final int _period; // in seconds
	public final int _altPeriod1; // in seconds
	public final int _altPeriod2; // in seconds
	public final int _abnormalEffect;
	public FuncTemplate[] _funcTemplates;
	
	public final String _stackType;
	public final float _stackOrder;
    public final boolean _icon;
	
	public EffectTemplate(Condition attachCond, Condition applyCond, String func, Lambda lambda, int counter, int period, int altPeriod1, int altPeriod2, int abnormalEffect, String stackType, float stackOrder, boolean showIcon)
	{
		_attachCond = attachCond;
		_applyCond = applyCond;
		_lambda = lambda;
		_counter = counter;
		_period = period;
		_altPeriod1 = altPeriod1;
		_altPeriod2 = altPeriod2;
		_abnormalEffect = abnormalEffect;
		_stackType = stackType;
		_stackOrder = stackOrder;
        _icon = showIcon;

		try
        {
			_func = Class.forName("net.sf.l2j.gameserver.skills.effects.Effect"+func);
		}
        catch (ClassNotFoundException e)
        {
			throw new RuntimeException(e);
		}
		try
        {
			_constructor = _func.getConstructor(Env.class, EffectTemplate.class);
		}
        catch (NoSuchMethodException e)
        {
			throw new RuntimeException(e);
		}
	}
	
	public L2Effect getEffect(Env env, Object owner)
	{
		if (_attachCond != null && !_attachCond.test(env, owner))
			return null;
		try
        {
			L2Effect effect = (L2Effect)_constructor.newInstance(env, this);
			return effect;
		}
        catch (IllegalAccessException e)
        {
			e.printStackTrace();
			return null;
		}
        catch (InstantiationException e)
        {
			e.printStackTrace();
			return null;
		}
        catch (InvocationTargetException e)
        {
            _log.warning("Error creating new instance of Class "+_func+" Exception was:");
			e.getTargetException().printStackTrace();
			return null;
		}
	}

    public void attach(FuncTemplate f)
    {
    	if (_funcTemplates == null)
    	{
    		_funcTemplates = new FuncTemplate[]{f};
    	}
    	else
    	{
    		int len = _funcTemplates.length;
    		FuncTemplate[] tmp = new FuncTemplate[len+1];
    		System.arraycopy(_funcTemplates, 0, tmp, 0, len);
    		tmp[len] = f;
    		_funcTemplates = tmp;
    	}
    }
}