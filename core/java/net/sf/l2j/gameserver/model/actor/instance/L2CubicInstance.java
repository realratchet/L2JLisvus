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
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillDrain;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillSummon;
import net.sf.l2j.gameserver.taskmanager.AttackStanceTaskManager;
import net.sf.l2j.util.Rnd;

public class L2CubicInstance
{
	private static final Logger _log = Logger.getLogger(L2CubicInstance.class.getName());

	public static final int STORM_CUBIC = 1;
	public static final int VAMPIRIC_CUBIC = 2;
	public static final int LIFE_CUBIC = 3;
	public static final int VIPER_CUBIC = 4;
	public static final int POLTERGEIST_CUBIC = 5;
	public static final int BINDING_CUBIC = 6;
	public static final int AQUA_CUBIC = 7;
	public static final int SPARK_CUBIC = 8;
	
	// Cubic skills
	public static final int SKILL_CUBIC_HEAL = 4051;
	
	// Max range of cubic skills
	public static final int MAX_MAGIC_RANGE = 900;

	protected L2PcInstance _owner;
	protected L2SkillSummon _summonSkill;
	protected L2Character _target;

	protected final int _id;
	protected final int _mAtk;
	protected final int _activationTime;
	protected final int _activationChance;
	protected final boolean _givenByOther;
	protected final long _summonTime;
	
	private boolean _active;

	protected List<L2Skill> _skills = new ArrayList<>();

	private Future<?> _disappearTask;
	private Future<?> _actionTask;

	/**
	 * Cubic constructor.
	 * 
	 * @param owner
	 * @param skill
	 * @param passedLifeTime
	 * @param givenByOther
	 */
	public L2CubicInstance(L2PcInstance owner, L2SkillSummon skill, long passedLifeTime, boolean givenByOther)
	{
		_owner = owner;
		_summonSkill = skill;
		_id = skill.getNpcId();
		_mAtk = (int) skill.getPower();
		_activationTime = skill.getActivationTime() * 1000;
		_activationChance = skill.getActivationChance();
		_active = false;
		_givenByOther = givenByOther;
		_summonTime = (System.currentTimeMillis() - passedLifeTime);

		switch (_id)
		{
			case STORM_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4049, skill.getLevel()));
				break;
			case VAMPIRIC_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4050, skill.getLevel()));
				break;
			case LIFE_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(SKILL_CUBIC_HEAL, skill.getLevel()));
				doAction();
				break;
			case VIPER_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4052, skill.getLevel()));
				break;
			case POLTERGEIST_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4053, skill.getLevel()));
				_skills.add(SkillTable.getInstance().getInfo(4054, skill.getLevel()));
				_skills.add(SkillTable.getInstance().getInfo(4055, skill.getLevel()));
				break;
			case BINDING_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4164, skill.getLevel()));
				break;
			case AQUA_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4165, skill.getLevel()));
				break;
			case SPARK_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4166, skill.getLevel()));
				break;
		}

		_disappearTask = ThreadPoolManager.getInstance().scheduleGeneral(new Disappear(), (skill.getTotalLifeTime() - passedLifeTime)); // Disappear
	}

	public synchronized void doAction()
	{
		if (_active)
		{
			return;
		}
		_active = true;
		
		switch (_id)
		{
			case AQUA_CUBIC:
			case BINDING_CUBIC:
			case SPARK_CUBIC:
			case STORM_CUBIC:
			case POLTERGEIST_CUBIC:
			case VAMPIRIC_CUBIC:
			case VIPER_CUBIC:
				_actionTask = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Action(_activationChance), 0, _activationTime);
				break;
			case LIFE_CUBIC:
				_actionTask = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Heal(), 0, _activationTime);
				break;
		}
	}

	public int getId()
	{
		return _id;
	}

	public L2PcInstance getOwner()
	{
		return _owner;
	}
	
	public L2SkillSummon getSummonSkill()
	{
		return _summonSkill;
	}

	public boolean givenByOther()
	{
		return _givenByOther;
	}
	
	public int getMAtk()
	{
		return _mAtk;
	}

	public long getPassedLifeTime()
	{
		return (System.currentTimeMillis() - _summonTime);
	}
	
	public void stopAction()
	{
		_target = null;
		if (_actionTask != null)
		{
			_actionTask.cancel(true);
			_actionTask = null;
		}
		_active = false;
	}
	
	public void cancelDisappear()
	{
		if (_disappearTask != null)
		{
			_disappearTask.cancel(true);
			_disappearTask = null;
		}
	}

	/**
	 * This method sets the enemy target for a cubic.
	 */
	public void getCubicTarget()
	{
		try
		{
			_target = null;
			L2Object ownerTarget = _owner.getTarget();
			if (ownerTarget == null)
			{
				return;
			}
			
			// Events
			L2Event event = _owner.getEvent();
			if (event != null && event.isStarted())
			{
				L2PcInstance target = ownerTarget.getActingPlayer();
				if (target != null && target.getEvent() == event && (_owner.getEventTeam() == 0 || _owner.getEventTeam() != target.getEventTeam()))
				{
					_target = (L2Character)ownerTarget;
					// Dead target
					if (_target.isDead())
					{
						_target = null;
					}
				}
				return;
			}

			// Olympiad targeting
			if (_owner.isInOlympiadMode())
			{
				if (_owner.isOlympiadStart())
				{
					L2PcInstance target = ownerTarget.getActingPlayer();
					if (target != null && _owner.getOlympiadGameId() == target.getOlympiadGameId() && _owner.getOlympiadSide() != target.getOlympiadSide())
					{
						_target = (L2Character) ownerTarget;
					}
				}
				return;
			}
			
			// test owners target if it is valid then use it
			if (ownerTarget instanceof L2Character && ownerTarget != _owner.getPet() && ownerTarget != _owner)
			{
				// target mob which has aggro on you or your summon
				if (ownerTarget instanceof L2Attackable)
				{
					if (((L2Attackable) ownerTarget).getAggroList().get(_owner) != null && !((L2Attackable) ownerTarget).isDead())
					{
						_target = (L2Character) ownerTarget;
						return;
					}
					
					L2Summon ownerSummon = _owner.getPet();
					if (ownerSummon != null)
					{
						if (((L2Attackable) ownerTarget).getAggroList().get(ownerSummon) != null && !((L2Attackable) ownerTarget).isDead())
						{
							_target = (L2Character) ownerTarget;
							return;
						}
					}
				}
				
				// Get target in pvp or in siege
				L2PcInstance enemy = null;
				if ((_owner.getPvpFlag() > 0 && !_owner.isInsideZone(L2Character.ZONE_PEACE)) || _owner.isInsideZone(L2Character.ZONE_PVP))
				{
					if (ownerTarget instanceof L2Character && !((L2Character) ownerTarget).isDead())
						enemy = ownerTarget.getActingPlayer();
					
					if (enemy != null)
					{
						boolean targetIt = true;
						
						L2Party ownerParty = _owner.getParty();
						if (ownerParty != null)
						{
							if (ownerParty.getPartyMembers().contains(enemy))
							{
								targetIt = false;
							}
							else if (ownerParty.getCommandChannel() != null)
							{
								if (ownerParty.getCommandChannel().getMembers().contains(enemy))
								{
									targetIt = false;
								}
							}
						}
						
						L2Clan ownerClan = _owner.getClan();
						if (ownerClan != null && !_owner.isInsideZone(L2Character.ZONE_PVP))
						{
							if (ownerClan.isMember(enemy.getObjectId()))
							{
								targetIt = false;
							}
							
							if (_owner.getAllyId() > 0 && enemy.getAllyId() > 0)
							{
								if (_owner.getAllyId() == enemy.getAllyId())
								{
									targetIt = false;
								}
							}
						}
						
						if (enemy.getPvpFlag() == 0 && !enemy.isInsideZone(L2Character.ZONE_PVP))
						{
							targetIt = false;
						}
						
						if (enemy.isInsideZone(L2Character.ZONE_PEACE))
						{
							targetIt = false;
						}
						
						// Defending alliances
						if (_owner.getSiegeState() != 0 && _owner.getSiegeState() == enemy.getSiegeState() && _owner.getSiegeSide() == enemy.getSiegeSide()
							&& _owner.isInsideZone(L2Character.ZONE_SIEGE))
						{
							targetIt = false;
						}
						
						if (!enemy.isVisible() || enemy.getAppearance().getInvisible())
						{
							targetIt = false;
						}
						
						if (targetIt)
						{
							_target = enemy;
							return;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "", e);
		}
	}
	
	private class Action implements Runnable
	{
		private final int _chance;

		Action(int chance)
		{
			_chance = chance;
		}

		@Override
		public void run()
		{
			try
			{
				if (_owner.isDead() || !_owner.isOnline())
				{
					stopAction();
					_owner.getCubics().remove(L2CubicInstance.this);
					_owner.broadcastUserInfo();
					cancelDisappear();
					return;
				}
				
				if (!AttackStanceTaskManager.getInstance().getAttackStanceTask(_owner))
				{
					stopAction();
					return;
				}
				
				if (Rnd.get(1, 100) < _chance)
				{
					final L2Skill skill = _skills.get(Rnd.get(_skills.size()));
					if (skill != null)
					{
						if (skill.getId() == SKILL_CUBIC_HEAL)
						{
							// Friendly skill, so we look a target in owner's party
							cubicTargetForHeal();
						}
						else
						{
							// Offensive skill, we look for an enemy target
							getCubicTarget();
							if (!isInCubicRange(_owner, _target))
								_target = null;
						}
						
						if (_target != null && !_target.isDead())
						{
							_owner.broadcastPacket(new MagicSkillUse(_owner, _target, skill.getId(), skill.getLevel(), 0, 0));

							final SkillType type = skill.getSkillType();
							final ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());
							L2Character[] targets = { _target };
							
							if (type == SkillType.PARALYZE || type == SkillType.STUN || type == SkillType.ROOT)
							{
								useCubicDisabler(type, L2CubicInstance.this, skill, targets);
							}
							else if (type == SkillType.MDAM)
							{
								useCubicMdam(L2CubicInstance.this, skill, targets);
							}
							else if (type == SkillType.POISON || type == SkillType.DEBUFF || type == SkillType.DOT)
							{
								useCubicContinuous(L2CubicInstance.this, skill, targets);
							}
							else if (type == SkillType.DRAIN)
							{
								((L2SkillDrain) skill).useCubicSkill(L2CubicInstance.this, targets);
							}
							else
							{
								handler.useSkill(_owner, skill, targets);
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "", e);
			}
		}
	}

	public void useCubicContinuous(L2CubicInstance activeCubic, L2Skill skill, L2Object[] targets)
	{
		for (L2Object obj : targets)
		{
			if (obj == null || !(obj instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) obj;
			if (target.isDead())
				continue;

			if (skill.isOffensive())
			{
				boolean acted = Formulas.getInstance().calcCubicSkillSuccess(activeCubic, target, skill);
				if (!acted)
				{
					activeCubic.getOwner().sendPacket(new SystemMessage(SystemMessage.ATTACK_FAILED));
					continue;
				}
				
			}
			skill.getEffects(activeCubic.getOwner(), target);
		}
	}
	
	public void useCubicMdam(L2CubicInstance activeCubic, L2Skill skill, L2Object[] targets)
	{
		for (L2Object obj : targets)
		{
			if (obj == null || !(obj instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) obj;
			if (target.isDead())
			{
				continue;
			}
			
			if (target.isFakeDeath())
			{
				target.stopFakeDeath(true);
			}
			
			boolean mcrit = Formulas.getInstance().calcMCrit(activeCubic.getOwner().getMCriticalHit(target, skill));
			int damage = (int) Formulas.getInstance().calcMagicDam(activeCubic, target, skill, mcrit);
			if (damage > 0)
			{
				// Manage attack or cast break of the target (calculating rate, sending message...)
				if (Formulas.getInstance().calcAtkBreak(target, damage))
				{
					target.breakAttack();
					target.breakCast();
				}
				
				activeCubic.getOwner().sendDamageMessage(target, damage, mcrit, false, false);
				
				if (skill.hasEffects())
				{
					// Activate attached effects, if any
					target.stopSkillEffects(skill.getId());
					if (target.getFirstEffect(skill) != null)
					{
						target.removeEffect(target.getFirstEffect(skill));
					}
					
					if (Formulas.getInstance().calcCubicSkillSuccess(activeCubic, target, skill))
					{
						skill.getEffects(activeCubic.getOwner(), target);
					}
				}
				
				target.reduceCurrentHp(damage, activeCubic.getOwner());
			}
		}
	}
	
	public void useCubicDisabler(SkillType type, L2CubicInstance activeCubic, L2Skill skill, L2Object[] targets)
	{
		for (L2Object obj : targets)
		{
			if (obj == null || !(obj instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) obj;
			if (target.isDead()) // Bypass if target is null or dead
				continue;

			switch (type)
			{
				 // Use the same formula for all of them for now
				case STUN:
				case PARALYZE:
				case ROOT:
				{
					if (Formulas.getInstance().calcCubicSkillSuccess(activeCubic, target, skill))
					{
						skill.getEffects(activeCubic.getOwner(), target);
					}
					break;
				}
			}
		}
	}
	
	/**
	 * Returns true if the target is inside of the owner's max Cubic range.
	 * @param owner 
	 * @param target 
	 * @return 
	 */
	public boolean isInCubicRange(L2Character owner, L2Character target)
	{
		if (owner == null || target == null)
		{
			return false;
		}
		
		int x, y, z;
		// Temporary range check until real behavior of cubics is known/coded
		int range = MAX_MAGIC_RANGE;
		
		x = (owner.getX() - target.getX());
		y = (owner.getY() - target.getY());
		z = (owner.getZ() - target.getZ());
		
		return ((x * x) + (y * y) + (z * z) <= (range * range));
	}

	/**
	 * This method sets the friendly target for a cubic.
	 */
	public void cubicTargetForHeal()
	{
		L2Character target = null;
		double percentleft = 100.0;
		
		L2Party party = _owner.getParty();
		if (party != null && !_owner.isInOlympiadMode())
		{
			// Get all visible objects in a spherical area near the L2Character
			// Get a list of Party Members
			List<L2PcInstance> partyList = party.getPartyMembers();
			for (L2Character partyMember : partyList)
			{
				if (!partyMember.isDead())
				{
					// If party member not dead, check if he is in cast range of heal cubic
					if (isInCubicRange(_owner, partyMember))
					{
						/**
						 * Member is in cubic casting range, check if he need heal and if he have
						 * the lowest HP.
						 */
						if (partyMember.getCurrentHp() < partyMember.getMaxHp())
						{
							if (percentleft > (partyMember.getCurrentHp() / partyMember.getMaxHp()))
							{
								percentleft = (partyMember.getCurrentHp() / partyMember.getMaxHp());
								target = partyMember;
							}
						}
					}
				}
				
				L2Summon summon = partyMember.getPet();
				if (summon != null)
				{
					if (summon.isDead())
					{
						continue;
					}
					
					// If party member's pet not dead, check if it is in cast range of heal cubic
					if (!isInCubicRange(_owner, summon))
					{
						continue;
					}
					
					/**
					 * Member's pet is in cubic casting range, check if he need heal and if he have
					 * the lowest HP.
					 */
					if (summon.getCurrentHp() < summon.getMaxHp())
					{
						if (percentleft > (summon.getCurrentHp() / summon.getMaxHp()))
						{
							percentleft = (summon.getCurrentHp() / summon.getMaxHp());
							target = summon;
						}
					}
				}
			}
		}
		else
		{
			if (_owner.getCurrentHp() < _owner.getMaxHp())
			{
				percentleft = (_owner.getCurrentHp() / _owner.getMaxHp());
				target = _owner;
			}
			
			L2Summon summon = _owner.getPet();
			if (summon != null && !summon.isDead() && summon.getCurrentHp() < summon.getMaxHp()
				&& percentleft > (summon.getCurrentHp() / summon.getMaxHp()) && isInCubicRange(_owner, summon))
			{
				target = summon;
			}
		}
		
		_target = target;
	}
	
	private class Heal implements Runnable
	{
		Heal()
		{
			// Run task
		}
		
		@Override
		public void run()
		{
			if (_owner.isDead() && !_owner.isOnline())
			{
				stopAction();
				_owner.getCubics().remove(L2CubicInstance.this);
				_owner.broadcastUserInfo();
				cancelDisappear();
				return;
			}
			
			try
			{
				L2Skill skill = null;
				for (L2Skill sk : _skills)
				{
					if (sk.getId() == SKILL_CUBIC_HEAL)
					{
						skill = sk;
						break;
					}
				}
				
				if (skill != null)
				{
					cubicTargetForHeal();
					L2Character target = _target;
					if (target != null && !target.isDead())
					{
						if (target.getMaxHp() - target.getCurrentHp() > skill.getPower())
						{
							L2Character[] targets = { target };
							ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());
							if (handler != null)
							{
								handler.useSkill(_owner, skill, targets);
							}
							else
							{
								skill.useSkill(_owner, targets);
							}
							
							MagicSkillUse msu = new MagicSkillUse(_owner, target, skill.getId(), skill.getLevel(), 0, 0);
							_owner.broadcastPacket(msu);
						}
					}
				}
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "", e);
			}
		}
	}
	
	private class Disappear implements Runnable
	{
		Disappear()
		{
			// run task
		}

		@Override
		public void run()
		{
			stopAction();
			_owner.getCubics().remove(L2CubicInstance.this);
			_owner.broadcastUserInfo();
		}
	}
}