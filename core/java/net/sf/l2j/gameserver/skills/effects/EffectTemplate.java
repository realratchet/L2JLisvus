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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.funcs.FuncTemplate;
import net.sf.l2j.gameserver.skills.funcs.Lambda;

/**
 * @author mkizub
 */
public final class EffectTemplate
{
	private static final Logger _log = Logger.getLogger(EffectTemplate.class.getName());
	
	private final Class<?> _func;
	private final Constructor<?> _constructor;
	
	private final Condition _attachCond;
	private final Condition _applyCond;
	private final Lambda _lambda;
	private final int _counter;
	private final int _period; // in seconds
	private final int _altPeriod1; // in seconds
	private final int _altPeriod2; // in seconds
	private final int _abnormalEffect;
	
	private final String _stackType;
	private final float _stackOrder;
	private final boolean _icon;

	private List<FuncTemplate> _funcTemplates;
	
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
			_func = Class.forName("net.sf.l2j.gameserver.skills.effects.Effect" + func);
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
			L2Effect effect = (L2Effect) _constructor.newInstance(env, this);
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
			_log.warning("Error creating new instance of Class " + _func + " Exception was:");
			e.getTargetException().printStackTrace();
			return null;
		}
	}
	
	public void attach(FuncTemplate f)
	{
		if (_funcTemplates == null)
		{
			_funcTemplates = new ArrayList<>();
		}
		
		_funcTemplates.add(f);
	}

	public Condition getAttachCondition()
	{
		return _attachCond;
	}

	public Condition getApplyCondition()
	{
		return _applyCond;
	}

	public Lambda getLambda()
	{
		return _lambda;
	}

	public int getCounter()
	{
		return _counter;
	}

	public int getPeriod()
	{
		return _period;
	}

	public int getAltPeriod1()
	{
		return _altPeriod1;
	}

	public int getAltPeriod2()
	{
		return _altPeriod2;
	}

	public int getAbnormalEffect()
	{
		return _abnormalEffect;
	}

	public String getStackType()
	{
		return _stackType;
	}

	public float getStackOrder()
	{
		return _stackOrder;
	}

	public boolean getIcon()
	{
		return _icon;
	}

	public List<FuncTemplate> getFuncTemplates()
	{
		return _funcTemplates;
	}
}