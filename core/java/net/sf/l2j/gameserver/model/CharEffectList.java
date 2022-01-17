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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Effect.EffectType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.serverpackets.ExOlympiadSpelledInfo;
import net.sf.l2j.gameserver.network.serverpackets.MagicEffectIcons;
import net.sf.l2j.gameserver.network.serverpackets.PartySpelled;

public class CharEffectList
{
	private static final L2Effect[] EMPTY_EFFECTS = new L2Effect[0];

	private List<L2Effect> _buffs;
	private List<L2Effect> _debuffs;

	// The table containing the List of all stacked effect in progress for each Stack group Identifier
	private Map<String, List<L2Effect>> _stackedEffects;

	private boolean _queuesInitialized = false;
	private LinkedBlockingQueue<L2Effect> _addQueue;
	private LinkedBlockingQueue<L2Effect> _removeQueue;
	private AtomicBoolean queueLock = new AtomicBoolean();

	// only party icons need to be updated
	private boolean _partyOnly = false;

	// Owner of this list
	private L2Character _owner;

	public CharEffectList(L2Character owner)
	{
		_owner = owner;
	}
	
	/**
	 * Returns all effects affecting stored in this CharEffectList
	 * @return
	 */
	public final L2Effect[] getAllEffects()
	{
		// If no effect is active, return EMPTY_EFFECTS
		if ((_buffs == null || _buffs.isEmpty()) && (_debuffs == null || _debuffs.isEmpty()))
		{
			return EMPTY_EFFECTS;
		}

		// Create a copy of the effects
		List<L2Effect> temp = new ArrayList<>();

		// Add all buffs and all debuffs
		if (_buffs != null) 
		{
			if (!_buffs.isEmpty())
				temp.addAll(_buffs);
		}
		if (_debuffs != null)
		{
			if (!_debuffs.isEmpty())
				temp.addAll(_debuffs);
		}

		// Return all effects in an array
		L2Effect[] tempArray = new L2Effect[temp.size()];
		temp.toArray(tempArray);
		return tempArray;
	}
	
	/**
	 * Returns the first effect matching the given EffectType.
	 * 
	 * @param tp
	 * @return
	 */
	public final L2Effect getFirstEffect(EffectType tp)
	{
		L2Effect effectNotInUse = null;

		if (_buffs != null)
		{
			if (!_buffs.isEmpty())
			{
				for (L2Effect e: _buffs)
				{
					if (e == null)
						continue;
					if (e.getEffectType() == tp)
					{
						if (e.getInUse())
							return e;
						effectNotInUse = e;
					}
				}
			}
		}
		if (effectNotInUse == null && _debuffs != null)
		{
			if (!_debuffs.isEmpty())
			{
				for (L2Effect e: _debuffs)
				{
					if (e == null)
						continue;
					if (e.getEffectType() == tp)
					{
						if (e.getInUse())
							return e;
						effectNotInUse = e;
					}
				}
			}
		}
		return effectNotInUse;
	}
	
	/**
	 * Returns the first effect matching the given L2Skill.
	 * 
	 * @param skill
	 * @return
	 */
	public final L2Effect getFirstEffect(L2Skill skill)
	{
		L2Effect effectNotInUse = null;

		if (skill.isOffensive())
		{
			if (_debuffs == null)
				return null;

			if (_debuffs.isEmpty())
				return null;

			for (L2Effect e: _debuffs)
			{
				if (e == null)
					continue;
				if (e.getSkill() == skill)
				{
					if (e.getInUse())
						return e;
					effectNotInUse = e;
				}
			}
			return effectNotInUse;
		}

		if (_buffs == null)
			return null;

		if (_buffs.isEmpty())
			return null;

		for (L2Effect e: _buffs)
		{
			if (e == null)
				continue;
			if (e.getSkill() == skill)
			{
				if (e.getInUse())
					return e;
				effectNotInUse = e;
			}
		}
		return effectNotInUse;
	}
	
	/**
	 * Returns the first effect matching the given skillId.
	 * 
	 * @param skillId 
	 * @return
	 */
	public final L2Effect getFirstEffect(int skillId)
	{
		L2Effect effectNotInUse = null;

		if (_buffs != null)
		{
			if (!_buffs.isEmpty())
			{
				for (L2Effect e: _buffs)
				{
					if (e == null)
						continue;
					if (e.getSkill().getId() == skillId)
					{
						if (e.getInUse())
							return e;
						effectNotInUse = e;
					}
				}
			}
		}
		
		if (effectNotInUse == null && _debuffs != null)
		{
			if (!_debuffs.isEmpty())
			{
				for (L2Effect e: _debuffs)
				{
					if (e == null)
						continue;
					if (e.getSkill().getId() == skillId)
					{
						if (e.getInUse())
							return e;
						effectNotInUse = e;
					}
				}
			}
		}
		
		return effectNotInUse;
	}
	
	/**
	 * Checks if the given skill stacks with an existing one.
	 *
	 * @param checkSkill the skill to be checked
	 *
	 * @return Returns whether or not this skill will stack
	 */
	private boolean doesStack(L2Skill checkSkill)
	{
		if ( (_buffs == null || _buffs.isEmpty()) ||
				checkSkill._effectTemplates == null ||
				checkSkill._effectTemplates.length < 1 ||
				checkSkill._effectTemplates[0]._stackType == null ||
				"none".equals(checkSkill._effectTemplates[0]._stackType))
		{
			return false;
		}

		String stackType = checkSkill._effectTemplates[0]._stackType;

		for (L2Effect e : _buffs)
		{
			if (e.getStackType() != null && e.getStackType().equals(stackType))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean isIncludedInBuffCount(L2Effect effect)
	{
		return effect.getShowIcon() && !effect.getSkill().isToggle() && !effect.getStackType().equals("hp_recover");
	}
	
	/**
	 * Return the number of buffs in this CharEffectList.
	 * 
	 * @return
	 */
	public int getBuffCount()
	{
		if (_buffs == null)
			return 0;

		int effectCount = 0;

		if (_buffs.isEmpty())
			return 0;

		for (L2Effect e : _buffs)
		{
			if (e != null && isIncludedInBuffCount(e))
			{
				effectCount++;
			}
		}

		return effectCount;
	}
	
	/**
	 * Return the number of debuffs in this CharEffectList.
	 * 
	 * @return
	 */
	public int getDebuffCount()
	{
		if (_debuffs == null)
			return 0;

		int effectCount = 0;

		if (_debuffs.isEmpty())
			return 0;

		for (L2Effect e : _debuffs)
		{
			if (e != null && e.getShowIcon())
			{
				effectCount++;
			}
		}

		return effectCount;
	}
	
	/**
	 * Return the number of Songs/Dances in this CharEffectList.
	 * 
	 * @return
	 */
	public int getDanceCount()
	{
		if (_buffs == null)
			return 0;

		int effectCount = 0;

		if (_buffs.isEmpty())
			return 0;

		for (L2Effect e : _buffs)
		{
			if (e != null && e.getSkill().isDance() && e.getInUse())
				effectCount++;
		}
		return effectCount;
	}
	
	/**
	 * Exits all effects in this CharEffectList.
	 */
	public final void stopAllEffects()
	{
		// Get all active skills effects from this list
		L2Effect[] effects = getAllEffects();

		// Remove them
		for (L2Effect e : effects)
		{
			if (e != null)
			{
				e.exit(true);
			}
		}
 	}
	
	/**
	 * Exit all effects having a specified type.
	 * 
	 * @param type
	 */
	public final void stopEffects(EffectType type)
	{
		// Go through all active skills effects
		List<L2Effect> temp = new ArrayList<>();
		if (_buffs != null) 
		{
			if (!_buffs.isEmpty())
			{
				for (L2Effect e : _buffs)
				{
					// Get active skills effects of the selected type
					if (e != null && e.getEffectType() == type)
						temp.add(e);
				}
			}
		}
		if (_debuffs != null) 
		{
			if (!_debuffs.isEmpty())
			{
				for (L2Effect e : _debuffs)
				{
					// Get active skills effects of the selected type
					if (e != null && e.getEffectType() == type)
						temp.add(e);
				}
			}
		}

		if (!temp.isEmpty())
		{
			for (L2Effect e : temp)
			{
				if (e != null)
					e.exit();
			}
		}
	}
	
	/**
	 * Exits all effects created by a specific skillId.
	 * 
	 * @param skillId
	 */
	public final void stopSkillEffects(int skillId)
	{
		// Go through all active skills effects
		List<L2Effect> temp = new ArrayList<>();
		if (_buffs != null) 
		{
			if (!_buffs.isEmpty())
			{
				for (L2Effect e : _buffs)
				{
					if (e != null && e.getSkill().getId() == skillId)
						temp.add(e);
				}
			}
		}
		if (_debuffs != null) 
		{
			if (!_debuffs.isEmpty())
			{
				for (L2Effect e : _debuffs)
				{
					if (e != null && e.getSkill().getId() == skillId)
						temp.add(e);
				}
			}
		}

		if (!temp.isEmpty())
		{
			for (L2Effect e : temp)
			{
				if (e != null)
					e.exit();
			}
		}
	}
	
	public void updateEffectIcons(boolean partyOnly)
	{
		if (_buffs == null && _debuffs == null)
			return;

		if (partyOnly)
			_partyOnly = true;
		
		queueRunner();
	}
	
	public void queueEffect(L2Effect effect, boolean remove)
	{
		if (effect == null) return;

		if (!_queuesInitialized)
			init();

		if (remove)
		{
			_removeQueue.offer(effect);
		}
		else
		{
			_addQueue.offer(effect);
		}
		queueRunner();
	}

	private synchronized void init()
	{
		if (_queuesInitialized)
		{
			return;
		}

		_addQueue = new LinkedBlockingQueue<>();
		_removeQueue = new LinkedBlockingQueue<>();
		_queuesInitialized = true;
	}

	private void queueRunner()
	{
		if (!queueLock.compareAndSet(false, true))
			return;

		try
		{
			L2Effect effect;
			do
			{
				// Remove has more priority than add, so removing all effects from queue first
				while ((effect = _removeQueue.poll()) != null)
				{
					removeEffectFromQueue(effect);
					_partyOnly = false;
				}

				if ((effect = _addQueue.poll()) != null)
				{
					addEffectFromQueue(effect);
					_partyOnly = false;
				}
			}
			while (!_addQueue.isEmpty() || !_removeQueue.isEmpty());
			
			updateEffectIcons();
		}
		finally
		{
			queueLock.set(false);
		}
	}
	
	protected void removeEffectFromQueue(L2Effect effect)
	{
		if (effect == null) return;

		List<L2Effect> effectList;
		
		if (effect.getSkill().isOffensive())
		{
			if (_debuffs == null)
				return;
			effectList = _debuffs;
		}
		else
		{
			if (_buffs == null)
				return;
			effectList = _buffs;
		}

		if ("none".equals(effect.getStackType()))
		{
			// Remove Func added by this effect from the L2Character Calculator
			_owner.removeStatsOwner(effect);
		}
		else
		{
			if (_stackedEffects == null)
				return;

			// Get the list of all stacked effects corresponding to the stack type of the L2Effect to add
			List<L2Effect> stackQueue = _stackedEffects.get(effect.getStackType());
			if (stackQueue == null || stackQueue.isEmpty())
				return;

			int index = stackQueue.indexOf(effect);

			// Remove the effect from the stack group
			if (index >= 0)
			{
				stackQueue.remove(effect);
				// Check if the first stacked effect was the effect to remove
				if (index == 0)
				{
					// Remove all its Func objects from the L2Character calculator set
					_owner.removeStatsOwner(effect);

					// Check if there's another effect in the Stack Group
					if (!stackQueue.isEmpty())
					{
						L2Effect newStackedEffect = listsContains(stackQueue.get(0));
						if (newStackedEffect != null)
						{
							// Set the effect to In Use
							if (newStackedEffect.setInUse(true))
							{
								// Add its list of Funcs to the Calculator set of the L2Character
								_owner.addStatFuncs(newStackedEffect.getStatFuncs());
							}
						}
					}
				}
				if (stackQueue.isEmpty())
					_stackedEffects.remove(effect.getStackType());
				else
					// Update the Stack Group table _stackedEffects of the L2Character
					_stackedEffects.put(effect.getStackType(), stackQueue);
			}
		}
		
		// Remove the active skill L2effect from _effects of the L2Character
		effectList.remove(effect);
	}
	
	protected void addEffectFromQueue(L2Effect newEffect)
	{
		if (newEffect == null)
			return;
		
		// Get amount of buffs that will be removed now
		int removedBuffCount = 0;
		
		L2Skill newSkill = newEffect.getSkill();
		if (newSkill.isOffensive())
		{
			if (_debuffs == null)
			{
				_debuffs = new CopyOnWriteArrayList<>();
			}

			for (L2Effect e : _debuffs)
			{
				if (e != null && e.getEffectType() == newEffect.getEffectType()
						&& e.getStackOrder() == newEffect.getStackOrder()
						&& (e.getSkill().getId() == newEffect.getSkill().getId() 
						|| !e.getStackType().equals("none") && e.getStackType().equals(newEffect.getStackType())))
				{
					// Started scheduled timer needs to be cancelled.
					newEffect.stopEffectTask(); 
					return; 
				}
			}
		}
		else
		{
			if (_buffs == null)
			{
				_buffs = new CopyOnWriteArrayList<>();
			}

			for (L2Effect e : _buffs)
			{
				if (e != null && e.getEffectType() == newEffect.getEffectType()
						&& e.getStackOrder() == newEffect.getStackOrder()
						&& (e.getSkill().getId() == newEffect.getSkill().getId() 
						|| !e.getStackType().equals("none") && e.getStackType().equals(newEffect.getStackType())))
				{
					// If restore type is less than 0, then this effect is an AIO/NPC buff
					if (e.getRestoreType() < 0)
					{
						/**
				         * Some servers set custom effect durations, so this is for players' convenience when trying 
				         * to use the same or a stackable effect that has less duration than old effect.
				         */
						if (e.getRemainingTime() > newEffect.getEffectDuration())
						{
							return;
						}
					}
					e.exit();
					
					if (isIncludedInBuffCount(e))
					{
						removedBuffCount++;
					}
				}
			}
		}

		// Check if any effects should be removed
		if (!doesStack(newSkill) && !newSkill.isToggle() && !newEffect.getStackType().equals("hp_recover") && newEffect.getShowIcon())
		{
			int buffCount = getBuffCount() - removedBuffCount;
			int debuffCount = getDebuffCount();
			
			// Check if debuff slot limit has been exceeded
			if (debuffCount > Config.DEBUFFS_MAX_AMOUNT)
			{
				// If yes, allow debuffs to consume buff slots
				buffCount += debuffCount - Config.DEBUFFS_MAX_AMOUNT;
			}
				
			// Remove first buff(s) when buff list is full
			if (buffCount >= Config.BUFFS_MAX_AMOUNT && (!newSkill.isOffensive() || debuffCount >= Config.DEBUFFS_MAX_AMOUNT))
			{
				int effectsToRemove = buffCount - Config.BUFFS_MAX_AMOUNT;
				if (effectsToRemove >= 0)
				{
					for (L2Effect e : _buffs)
					{
						if (e == null || e.getSkill().isOffensive())
							continue;
		
						e.exit();
						effectsToRemove--;
						if (effectsToRemove < 0)
							break;
					}
				}
			}
		}
		
		// Add effect to an appropriate position
		if (newSkill.isOffensive())
		{
			// Add debuff to list now, so we can calculate over-flown slots later
			_debuffs.add(newEffect);
		}
		else
		{
			// Toggle and 7 signs effects
			if (newSkill.isToggle() || (newSkill.getId() > 4360  && newSkill.getId() < 4367))
			{
				_buffs.add(newEffect);
			}
			// Common buffs
			else
			{
				int pos=0;
				for (L2Effect e : _buffs)
				{
					if (e == null)
					{
						continue;
					}
	
					int skillid = e.getSkill().getId();
					if (e.getSkill().isToggle() || (skillid > 4360  && skillid < 4367))
					{
						break;
					}
					pos++;
				}
				_buffs.add(pos, newEffect);
			}
		}
		
		// Check if a stack group is defined for this effect
		if ("none".equals(newEffect.getStackType()))
		{
			// Set this L2Effect to In Use
			if (newEffect.setInUse(true))
			{
				// Add Funcs of this effect to the Calculator set of the L2Character
				_owner.addStatFuncs(newEffect.getStatFuncs());
			}
			return;
		}

		List<L2Effect> stackQueue;
		L2Effect effectToAdd = null;
		L2Effect effectToRemove = null;
		if (_stackedEffects == null)
			_stackedEffects = new HashMap<>();
					
		// Get the list of all stacked effects corresponding to the stack type of the L2Effect to add
		stackQueue = _stackedEffects.get(newEffect.getStackType());
		
		if (stackQueue != null)
		{
			int pos = 0;
			if (!stackQueue.isEmpty())
			{
				// Get the first stacked effect of the Stack group selected
				effectToRemove = listsContains(stackQueue.get(0));

				// Create an Iterator to go through the list of stacked effects in progress on the L2Character
				Iterator<L2Effect> queueIterator = stackQueue.iterator();

				while (queueIterator.hasNext())
				{
		            if (newEffect.getStackOrder() < queueIterator.next().getStackOrder())
		            	pos++;
		            else
		            	break;
		        }
				// Add the new effect to the Stack list in function of its position in the Stack group
				stackQueue.add(pos, newEffect);

				// skill.exit() could be used, if the users don't wish to see "effect
				// removed" always when a timer goes off, even if the buff isn't active
				// any more (has been replaced). but then check e.g. npc hold and raid petrification.
				if (Config.EFFECT_CANCELLING && stackQueue.size() > 1)
				{
					if (newSkill.isOffensive())
					{
						_debuffs.remove(stackQueue.remove(1));
					}
					else
					{
						_buffs.remove(stackQueue.remove(1));
					}
				}
			}
			else
				stackQueue.add(0, newEffect);
		}
		else
		{
			stackQueue = new ArrayList<>();
			stackQueue.add(0, newEffect);
		}

		// Update the Stack Group table _stackedEffects of the L2Character
		_stackedEffects.put(newEffect.getStackType(), stackQueue);

		// Get the first stacked effect of the Stack group selected
		if (!stackQueue.isEmpty())
		{
			effectToAdd = listsContains(stackQueue.get(0));
		}

		if (effectToRemove != effectToAdd)
		{
			if (effectToRemove != null)
			{
				// Remove all Func objects corresponding to this stacked effect from the Calculator set of the L2Character
				_owner.removeStatsOwner(effectToRemove);

				// Set the L2Effect to Not In Use
				effectToRemove.setInUse(false);
			}
			if (effectToAdd != null)
			{
				// Set this L2Effect to In Use
				if (effectToAdd.setInUse(true))
				{
					// Add all Func objects corresponding to this stacked effect to the Calculator set of the L2Character
					_owner.addStatFuncs(effectToAdd.getStatFuncs());
				}
			}
		}
	}
	
	protected void updateEffectIcons()
	{
		if (_owner == null || !(_owner instanceof L2PlayableInstance))
			return;

		MagicEffectIcons mi = null;
		PartySpelled ps = null;
		ExOlympiadSpelledInfo os = null;
		
		if (_owner instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) _owner;

			if (_partyOnly)
				_partyOnly = false;
			else
				mi = new MagicEffectIcons();

			if (_owner.isInParty())
				ps = new PartySpelled(_owner);

			if (player.isInOlympiadMode() && player.isOlympiadStart())
			{
				os = new ExOlympiadSpelledInfo(player);
			}
		}
		else
		{
			if (_owner instanceof L2Summon)
				ps = new PartySpelled(_owner);
		}

		if (mi == null && ps == null && os == null)
		{
			return; // Nothing to do (should not happen)
		}
		
		L2Effect shortBuff = null;
		
		if (_buffs != null && !_buffs.isEmpty())
		{
			for (L2Effect e : _buffs)
			{
				if (e == null || !e.getShowIcon())
					continue;
				
				if (e.getInUse())
				{
					if (mi != null)
					{
						// Potion HOT effects have their own slot in user interface
						if (_owner instanceof L2PcInstance && e.getStackType().equals("hp_recover"))
						{
							if (e != ((L2PcInstance)_owner).getShortBuff())
							{
								shortBuff = e;
							}
						}
						else
						{
							e.addIcon(mi);
						}
					}
					
					if (ps != null)
					{
						e.addPartySpelledIcon(ps);
					}
					
					if (os != null)
					{
						e.addOlympiadSpelledIcon(os);
					}
				}
			}
		}

		if (_debuffs != null && !_debuffs.isEmpty())
		{
			for (L2Effect e : _debuffs)
			{
				if (e == null || !e.getShowIcon())
					continue;

				if (e.getInUse())
				{
					if (mi != null)
					{
						e.addIcon(mi);
					}
					
					if (ps != null)
					{
						e.addPartySpelledIcon(ps);
					}
					
					if (os != null)
					{
						e.addOlympiadSpelledIcon(os);
					}
				}
			}
		}

		if (mi != null)
		{
			_owner.sendPacket(mi);
		}

		if (ps != null)
		{
			if (_owner instanceof L2Summon)
			{
				L2PcInstance summonOwner = ((L2Summon)_owner).getOwner();
				if (summonOwner != null)
				{
					summonOwner.sendPacket(ps);
				}
			}
			else
			{
				if (_owner instanceof L2PcInstance && _owner.isInParty())
					_owner.getParty().broadcastToPartyMembers(ps);
			}
		}
		
		if (_owner instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) _owner;

			// Short buff packet
			if (shortBuff != null)
			{
				player.shortBuffStatusUpdate(shortBuff);
			}
			
			// Olympiad Spelled packet
			if (os != null)
			{
				List<L2PcInstance> spectators = Olympiad.getInstance().getSpectators(player.getOlympiadGameId());
				if (spectators != null)
				{
					for (L2PcInstance spectator : spectators)
					{
						if (spectator == null)
						{
							continue;
						}
						
						spectator.sendPacket(os);
					}
				}
			}
		}
	}

	/**
	 * Returns effect if contains in _buffs or _debuffs and null if not found
	 * @param effect
	 * @return
	 */
	private L2Effect listsContains(L2Effect effect)
	{
		if (_buffs != null && !_buffs.isEmpty()&& _buffs.contains(effect))
			return effect;
		if (_debuffs != null && !_debuffs.isEmpty() && _debuffs.contains(effect))
			return effect;
		return null;
	}

	/**
	 * Clear and null all queues and lists
	 * Use only during delete character from the world.
	 */
	public void clear()
	{
		try
		{
			if (_addQueue != null)
			{
				_addQueue.clear();
				_addQueue = null;
			}
			if (_removeQueue != null)
			{
				_removeQueue.clear();
				_removeQueue = null;
			}
			_queuesInitialized = false;

			if (_buffs != null)
			{
				_buffs.clear();
				_buffs = null;
			}
			if (_debuffs != null)
			{
				_debuffs.clear();
				_debuffs = null;
			}

			if (_stackedEffects != null)
			{
				_stackedEffects.clear();
				_stackedEffects = null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}