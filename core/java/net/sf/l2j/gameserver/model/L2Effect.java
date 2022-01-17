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
package net.sf.l2j.gameserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExOlympiadSpelledInfo;
import net.sf.l2j.gameserver.network.serverpackets.MagicEffectIcons;
import net.sf.l2j.gameserver.network.serverpackets.PartySpelled;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.effects.EffectTemplate;
import net.sf.l2j.gameserver.skills.funcs.Func;
import net.sf.l2j.gameserver.skills.funcs.FuncTemplate;
import net.sf.l2j.gameserver.skills.funcs.Lambda;

/**
 * This class ...
 *
 * @version $Revision: 1.1.2.1.2.12 $ $Date: 2005/04/11 10:06:07 $
 */
public abstract class L2Effect
{
	private static final Logger _log = Logger.getLogger(L2Effect.class.getName());

	public static enum EffectState
	{
		CREATED,
		ACTING,
		FINISHING
	}

	public static enum EffectType
	{
		BUFF,
		DEBUFF,
		DMG_OVER_TIME,
		HEAL_OVER_TIME,
		COMBAT_POINT_HEAL_OVER_TIME,
		MANA_DMG_OVER_TIME,
		MANA_HEAL_OVER_TIME,
		RELAXING,
		STUN,
		ROOT,
		SLEEP,
		HATE,
		FAKE_DEATH,
		CONFUSION,
		CONFUSE_MOB_ONLY,
		MUTE,
		FEAR,
		SILENT_MOVE,
		SEED,
		PARALYZE,
		STUN_SELF,
		PHYSICAL_MUTE,
		SILENCE_MAGIC_PHYSICAL,
		PETRIFICATION,
		NOBLESSE_BLESSING,
		MP_CONSUME_PER_LEVEL,
		BLUFF,
		REMOVE_TARGET,
		CHARM_OF_LUCK,
		THROW_UP
	}

	private static final Func[] _emptyFunctionSet = new Func[0];

	// member _effector is the instance of L2Character that cast/used the spell/skill that is
	// causing this effect. Do not confuse with the instance of L2Character that
	// is being affected by this effect.
	private final L2Character _effector;

	// member _effected is the instance of L2Character that was affected
	// by this effect. Do not confuse with the instance of L2Character that
	// used this effect.
	private final L2Character _effected;

	// The skill that was used.
	private final L2Skill _skill;

	// The value of an update
	private final Lambda _lambda;

	// The current state
	private EffectState _state;

	// Period, seconds
	private int _period;
	private int _altPeriod1;
	private int _altPeriod2;
	private int _periodStartTicks;
	private int _periodFirstTime;

	// Function templates
	private final FuncTemplate[] _funcTemplates;

	// Initial count
	private final int _totalCount;

	// Counter
	private int _count;

	// Abnormal effect mask
	private final int _abnormalEffect;

	// Show icon
	private final boolean _icon;

	private boolean _isPreventExitUpdate;
	private boolean _isSelfEffect = false;

	public final class EffectTask implements Runnable
	{
		protected final int delay;
		protected final int rate;

		EffectTask(int pDelay, int pRate)
		{
			delay = pDelay;
			rate = pRate;
		}

		@Override
		public void run()
		{
			try
			{
				if (getPeriodFirstTime() == 0)
				{
					setPeriodStartTicks(GameTimeController.getInstance().getGameTicks());
				}
				else
				{
					setPeriodFirstTime(0);
				}
				L2Effect.this.scheduleEffect();
			}
			catch (Throwable e)
			{
				_log.log(Level.SEVERE, "", e);
			}
		}
	}

	private ScheduledFuture<?> _currentFuture;
	private EffectTask _currentTask;

	/** The Identifier of the stack group */
	private final String _stackType;

	/** The position of the effect in the stack group */
	private final float _stackOrder;

	private boolean _inUse = false;
	private boolean _startConditionsCorrect = true;
	
	/**
	 * Restore type 0 is for common effects.
	 * Restore type -1 is for AIO Buffer effects.
	 * Restore type -2 is for NPC Buffer effects.
	 */
	private byte _restoreType;

	protected L2Effect(Env env, EffectTemplate template)
	{
		_state = EffectState.CREATED;
		_skill = env.skill;
		_effected = env.target;
		_effector = env.player;
		_lambda = template._lambda;
		_funcTemplates = template._funcTemplates;
		_count = template._counter;
		_totalCount = _count;
		_period = template._period;
		_altPeriod1 = template._altPeriod1;
		_altPeriod2 = template._altPeriod2;

		// Effector is buffer, so set custom buff duration
		if (_effector.isBuffer())
		{
			// Player AIO Buffer
			if (_effector instanceof L2PcInstance)
			{
				if (_altPeriod1 > 0)
				{
					_period = _altPeriod1;
				}
				
				_restoreType = -1;
			}
			// NPC Buffer
			else
			{
				if (_altPeriod2 > 0)
				{
					_period = _altPeriod2;
				}
				
				_restoreType = -2;
			}
		}
		else
		{
			_restoreType = 0;
		}
		
		if (env.skillMastery)
		{
			_period *= 2;
		}

		_abnormalEffect = template._abnormalEffect;
		_stackType = template._stackType;
		_stackOrder = template._stackOrder;
		_periodStartTicks = GameTimeController.getInstance().getGameTicks();
		_periodFirstTime = 0;
		_icon = template._icon;
		scheduleEffect();
	}

	public int getCount()
	{
		return _count;
	}

	public int getTotalCount()
	{
		return _totalCount;
	}

	public void setCount(int newcount)
	{
		_count = newcount;
	}

	public void setFirstTime(int newFirstTime)
	{
		if (_currentFuture != null)
		{
			_periodStartTicks = GameTimeController.getInstance().getGameTicks() - (newFirstTime * GameTimeController.TICKS_PER_SECOND);
			_currentFuture.cancel(false);
			_currentFuture = null;
			_currentTask = null;
			_periodFirstTime = newFirstTime;
			int duration = _period - _periodFirstTime;
			_currentTask = new EffectTask(duration * 1000, -1);
			_currentFuture = ThreadPoolManager.getInstance().scheduleEffect(_currentTask, duration * 1000);
		}
	}

	public boolean getShowIcon()
	{
		return _icon;
	}

	public int getPeriod()
	{
		return _period;
	}

	public void setPeriod(int period)
	{
		_period = period;
	}
	
	public int getAltPeriod1()
	{
		return _altPeriod1;
	}
	
	public int getAltPeriod2()
	{
		return _altPeriod2;
	}

	public int getTime()
	{
		return (GameTimeController.getInstance().getGameTicks() - _periodStartTicks) / GameTimeController.TICKS_PER_SECOND;
	}

	public int getTaskTime()
	{
		if (_count == _totalCount)
		{
			return 0;
		}
		return (Math.abs((_count - _totalCount) + 1) * _period) + getTime() + 1;
	}
	
	public int getEffectDuration()
	{
		return _totalCount * _period;
	}
	
	public int getRemainingTime()
    {
        return (getEffectDuration() - getTime());
    }

	public boolean getInUse()
	{
		return _inUse;
	}

	public boolean setInUse(boolean inUse)
	{
		_inUse = inUse;
		if (_inUse)
			_startConditionsCorrect = onStart();
		else
			onExit();

		return _startConditionsCorrect;
	}

	public String getStackType()
	{
		return _stackType;
	}

	public float getStackOrder()
	{
		return _stackOrder;
	}

	public final L2Skill getSkill()
	{
		return _skill;
	}

	public final L2Character getEffector()
	{
		return _effector;
	}

	public final L2Character getEffected()
	{
		return _effected;
	}

	public boolean isSelfEffect()
	{
		return _isSelfEffect;
	}

	public byte getRestoreType()
	{
		return _restoreType;
	}
	
	public void setRestoreType(byte val)
	{
		_restoreType = val;
	}
	
	public void setSelfEffect()
	{
		_isSelfEffect = true;
	}

	public final double calc()
	{
		Env env = new Env();
		env.player = _effector;
		env.target = _effected;
		env.skill = _skill;
		return _lambda.calc(env);
	}

	private synchronized void startEffectTask(int duration)
	{
		stopEffectTask();
		_currentTask = new EffectTask(duration, -1);
		_currentFuture = ThreadPoolManager.getInstance().scheduleEffect(_currentTask, duration);
		if (_state == EffectState.ACTING)
		{
			_effected.addEffect(this);
		}
	}

	private synchronized void startEffectTaskAtFixedRate(int delay, int rate)
	{
		stopEffectTask();
		_currentTask = new EffectTask(delay, rate);
		_currentFuture = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(_currentTask, delay, rate);
		if (_state == EffectState.ACTING)
		{
			_effected.addEffect(this);
		}
	}

	/**
	 * Stop the L2Effect task and send Server->Client update packet.<BR><BR>
	 *
	 * <B><U> Actions</U> :</B><BR><BR>
	 * <li>Cancel the effect in the the abnormal effect map of the L2Character </li>
	 * <li>Stop the task of the L2Effect, remove it and update client magic icone </li><BR><BR>
	 *
	 */
	public final void exit()
	{
		exit(false);
	}

	public final void exit(boolean isPreventUpdate)
	{
		_isPreventExitUpdate = isPreventUpdate;
		_state = EffectState.FINISHING;
		scheduleEffect();
	}

	/**
	 * Stop the task of the L2Effect, remove it and update client magic icon.<BR><BR>
	 *
	 * <B><U> Actions</U> :</B><BR><BR>
	 * <li>Cancel the task </li>
	 * <li>Stop and remove L2Effect from L2Character and update client magic icon </li><BR><BR>
	 *
	 */
	public void stopEffectTask()
	{
		if (_currentFuture != null)
		{
			// Cancel the task
			_currentFuture.cancel(false);
			ThreadPoolManager.getInstance().removeEffect(_currentTask);

			_currentFuture = null;
			_currentTask = null;

			if (_effected != null)
			{
				_effected.removeEffect(this);
			}
		}
	}

	/** returns effect type 
	 * @return
	 */
	public abstract EffectType getEffectType();

	/**
	 * Notify started.
	 * 
	 * @return
	 */
	public boolean onStart()
	{
		if (_abnormalEffect != 0)
		{
			getEffected().startAbnormalEffect(_abnormalEffect);
		}
		
		return true;
	}

	/**
	 * Cancel the effect in the the abnormal effect map of the effected L2Character.<BR><BR>
	 */
	public void onExit()
	{
		if (_abnormalEffect != 0)
		{
			_effected.stopAbnormalEffect(_abnormalEffect);
		}
	}

	/** Return true for continuation of this effect 
	 * @return
	 */
	public abstract boolean onActionTime();

	public final void rescheduleEffect()
	{
		if (_state != EffectState.ACTING)
		{
			scheduleEffect();
		}
		else
		{
			if (_count > 1)
			{
				startEffectTaskAtFixedRate(5, _period * 1000);
				return;
			}
			if (_period > 0)
			{
				startEffectTask(_period * 1000);
				return;
			}
		}
	}

	public final void scheduleEffect()
	{
		if (_state == EffectState.CREATED)
		{
			_state = EffectState.ACTING;

			if (_skill.isPvpSkill() && _icon && getEffected() instanceof L2PcInstance)
			{
				SystemMessage smsg = new SystemMessage(110);
				smsg.addString(_skill.getName());
				getEffected().sendPacket(smsg);
			}
			if (_count > 1)
			{
				startEffectTaskAtFixedRate(5, _period * 1000);
				return;
			}

			if (_period > 0)
			{
				startEffectTask(_period * 1000);
				return;
			}

			// Effects not having count or period should start
			_startConditionsCorrect = onStart();
		}

		if (_state == EffectState.ACTING)
		{
			// Act only if start conditions were correct
			if (!_startConditionsCorrect)
			{
				_count = 0;
			}
			
			if (_count > 0)
			{
				_count--;
				
				// Effect has to be in use
				if (getInUse() && _count > 0)
				{
					if (onActionTime())
					{
						return;
					}
				}
				else if (_count > 0)
				{
					return;
				}
			}
			_state = EffectState.FINISHING;
		}

		if (_state == EffectState.FINISHING)
		{
			// Cancel the effect in the the abnormal effect map of the L2Character
			if (getInUse() || !(_count > 1 || _period > 0))
			{
				if (_startConditionsCorrect)
				{
					onExit();
				}
			}
			//If the time left is equal to zero, send the message
			if (_count == 0 && _icon && getEffected() instanceof L2PcInstance)
			{
				SystemMessage smsg3 = new SystemMessage(92);
				smsg3.addString(_skill.getName());
				getEffected().sendPacket(smsg3);
			}
			if (_currentFuture == null && _effected != null)
			{
				_effected.removeEffect(this);
			}

			// Stop the task of the L2Effect, remove it and update client magic icon
			stopEffectTask();
		}
	}

	public Func[] getStatFuncs()
	{
		if (_funcTemplates == null)
		{
			return _emptyFunctionSet;
		}
		
		List<Func> funcs = new ArrayList<>();
		for (FuncTemplate t : _funcTemplates)
		{
			Env env = new Env();
			env.player = getEffector();
			env.target = getEffected();
			env.skill = getSkill();
			Func f = t.getFunc(env, this); // effect is owner
			if (f != null)
			{
				funcs.add(f);
			}
		}
		if (funcs.size() == 0)
		{
			return _emptyFunctionSet;
		}
		return funcs.toArray(new Func[funcs.size()]);
	}

	public final void addIcon(MagicEffectIcons mi)
	{
		EffectTask task = _currentTask;
		ScheduledFuture<?> future = _currentFuture;
		if (task == null || future == null)
		{
			return;
		}
		
		if (_state == EffectState.FINISHING || _state == EffectState.CREATED)
		{
			return;
		}
		
		L2Skill sk = getSkill();
		if (task.rate > -1)
		{
			if (sk.isPotion())
			{
				int duration = _totalCount * _period;
				mi.addEffect(sk.getId(), getLevel(), duration - getTaskTime());
			}
			else
			{
				mi.addEffect(sk.getId(), getLevel(), -1);
			}
		}
		else
		{
			mi.addEffect(sk.getId(), getLevel(), (int) future.getDelay(TimeUnit.SECONDS));
		}
	}

	public final void addPartySpelledIcon(PartySpelled ps)
	{
		EffectTask task = _currentTask;
		ScheduledFuture<?> future = _currentFuture;
		if (task == null || future == null)
		{
			return;
		}
		
		if (_state == EffectState.FINISHING || _state == EffectState.CREATED)
		{
			return;
		}
		
		L2Skill sk = getSkill();
		ps.addPartySpelledEffect(sk.getId(), getLevel(), (int) future.getDelay(TimeUnit.SECONDS));
	}

	public final void addOlympiadSpelledIcon(ExOlympiadSpelledInfo os)
	{
		EffectTask task = _currentTask;
		ScheduledFuture<?> future = _currentFuture;
		if (task == null || future == null)
		{
			return;
		}
		
		if (_state == EffectState.FINISHING || _state == EffectState.CREATED)
		{
			return;
		}
		
		L2Skill sk = getSkill();
		// Toggle effects
		if (task.rate > -1)
		{
			os.addEffect(sk.getId(), getLevel(), -1);
		}
		else
		{
			os.addEffect(sk.getId(), getLevel(), (int) future.getDelay(TimeUnit.SECONDS));
		}
	}
	
	public int getLevel()
	{
		return getSkill().getLevel();
	}

	public int getPeriodFirstTime()
	{
		return _periodFirstTime;
	}

	public void setPeriodFirstTime(int periodFirstTime)
	{
		_periodFirstTime = periodFirstTime;
	}

	public int getPeriodStartTicks()
	{
		return _periodStartTicks;
	}

	public void setPeriodStartTicks(int periodStartTicks)
	{
		_periodStartTicks = periodStartTicks;
	}

	/**
	 * @return the _isPreventExitUpdate
	 */
	public boolean isPreventExitUpdate()
	{
		return _isPreventExitUpdate;
	}

	/**
	 * @param isPreventExitUpdate the preventExitUpdate to set
	 */
	public void setIsPreventExitUpdate(boolean isPreventExitUpdate)
	{
		_isPreventExitUpdate = isPreventExitUpdate;
	}
}