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
package net.sf.l2j.gameserver.ai;

import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.Territory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2ChestInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FestivalMonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FriendlyMobInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2GuardInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MinionInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2RiftInvaderInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SepulcherMonsterInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * This class manages AI of L2Attackable.<BR>
 * <BR>
 */
public class L2AttackableAI extends L2CharacterAI implements Runnable
{
	// private static final Logger _log = Logger.getLogger(L2AttackableAI.class.getName());
	
	private static final int RANDOM_WALK_RATE = 30;
	private static final int MAX_ATTACK_TIMEOUT = 1200; // int ticks, i.e. 2 minutes
	private static final int MAX_ATTACK_WALK_TIMEOUT = 20; // int ticks, i.e. 2 seconds
	
	/** The L2Attackable AI task executed every 1s (call onEvtThink method) */
	private Future<?> _aiTask;
	
	/** The delay after which the attacked is stopped */
	private int _attackTimeout;
	/** The delay after which actor will start running to attack target */
	private int _attackWalkTimeout;
	
	/** The L2Attackable aggro counter */
	private int _globalAggro;
	
	/** The flag used to indicate that a thinking action is in progress */
	private boolean _thinking; // to prevent recursive thinking
	
	/** For attack AI, analysis of mob and its targets */
	private final SelfAnalysis _selfAnalysis = new SelfAnalysis();
	private final TargetAnalysis _mostHatedAnalysis = new TargetAnalysis();
	private final TargetAnalysis _secondMostHatedAnalysis = new TargetAnalysis();
	
	/**
	 * Constructor of L2AttackableAI.<BR>
	 * <BR>
	 * @param accessor The AI accessor of the L2Character
	 */
	public L2AttackableAI(L2Character.AIAccessor accessor)
	{
		super(accessor);
		
		_selfAnalysis.init();
		_attackTimeout = Integer.MAX_VALUE;
		_attackWalkTimeout = 0;
		_globalAggro = -10; // 10 seconds timeout of ATTACK after respawn
	}
	
	@Override
	public void run()
	{
		// Launch actions corresponding to the Event Think
		onEvtThink();
	}
	
	/**
	 * Return True if the target is autoattackable (depends on the actor type).<BR>
	 * <BR>
	 * <B><U> Actor is a L2GuardInstance</U> :</B><BR>
	 * <BR>
	 * <li>The target isn't a Folk or a Door</li>
	 * <li>The target isn't dead, isn't invulnerable, isn't in silent moving mode AND too far (>100)</li>
	 * <li>The target is in the actor Aggro range and is at the same height</li>
	 * <li>The L2PcInstance target has karma (=PK)</li>
	 * <li>The L2MonsterInstance target is aggressive</li><BR>
	 * <BR>
	 * <B><U> Actor is a L2SiegeGuardInstance</U> :</B><BR>
	 * <BR>
	 * <li>The target isn't a Folk or a Door</li>
	 * <li>The target isn't dead, isn't invulnerable, isn't in silent moving mode AND too far (>100)</li>
	 * <li>The target is in the actor Aggro range and is at the same height</li>
	 * <li>A siege is in progress</li>
	 * <li>The L2PcInstance target isn't a Defender</li><BR>
	 * <BR>
	 * <B><U> Actor is a L2FriendlyMobInstance</U> :</B><BR>
	 * <BR>
	 * <li>The target isn't a Folk, a Door or another L2NpcInstance</li>
	 * <li>The target isn't dead, isn't invulnerable, isn't in silent moving mode AND too far (>100)</li>
	 * <li>The target is in the actor Aggro range and is at the same height</li>
	 * <li>The L2PcInstance target has karma (=PK)</li><BR>
	 * <BR>
	 * <B><U> Actor is a L2MonsterInstance</U> :</B><BR>
	 * <BR>
	 * <li>The target isn't a Folk, a Door or another L2NpcInstance</li>
	 * <li>The target isn't dead, isn't invulnerable, isn't in silent moving mode AND too far (>100)</li>
	 * <li>The target is in the actor aggro range and is at the same height</li>
	 * <li>The actor is Aggressive</li><BR>
	 * <BR>
	 * @param target The targeted L2Object
	 * @return
	 */
	private boolean autoAttackCondition(L2Character target)
	{
		if (target == null || !(_actor instanceof L2Attackable))
		{
			return false;
		}
		
		L2Attackable me = (L2Attackable) _actor;
		
		// Check if the target is an invulnerable GM
		if (target.isGM() && target.isInvul())
		{
			return false;
		}
		
		// Check if the target isn't a Folk or a Door
		if ((target instanceof L2FolkInstance) || (target instanceof L2DoorInstance))
		{
			return false;
		}
		
		// Check if the target isn't dead, is in the Aggro range and is at the same height
		if (target.isAlikeDead() || !me.isInsideRadius(target, me.getAggroRange(), false, false) || (Math.abs(me.getZ() - target.getZ()) > 400))
		{
			return false;
		}
		
		if (_selfAnalysis.cannotMoveOnLand && !target.isInsideZone(L2Character.ZONE_WATER))
		{
			return false;
		}
		
		if (target instanceof L2PlayableInstance)
		{
			// Check if the AI isn't a Raid Boss/Town guard and the target isn't in silent move mode
			if (!(me.isRaid() || (me instanceof L2GuardInstance)) && ((L2PlayableInstance) target).isSilentMoving())
			{
				return false;
			}
		}
		
		// Check if the target is a L2PcInstance
		if (target instanceof L2PcInstance)
		{
			if (me.getFactionId() != null)
			{
				// Check if player is an ally
				if (me.getFactionId().equals("varka_silenos_clan") && ((L2PcInstance) target).isAlliedWithVarka())
				{
					return false;
				}
				
				if (me.getFactionId().equals("ketra_orc_clan") && ((L2PcInstance) target).isAlliedWithKetra())
				{
					return false;
				}
			}
			
			// check if the target is within the grace period for JUST getting up from fake death
			if (((L2PcInstance) target).isRecentFakeDeath())
			{
				return false;
			}
			
			if (target.isInParty() && target.getParty().isInDimensionalRift())
			{
				byte riftType = target.getParty().getDimensionalRift().getType();
				byte riftRoom = target.getParty().getDimensionalRift().getCurrentRoom();
				if ((me instanceof L2RiftInvaderInstance) && !DimensionalRiftManager.getInstance().getRoom(riftType, riftRoom).checkIfInZone(me.getX(), me.getY(), me.getZ()))
				{
					return false;
				}
			}
		}
		
		// Check if the target is a L2Summon
		if (target instanceof L2Summon)
		{
			L2PcInstance owner = ((L2Summon) target).getOwner();
			if (owner != null)
			{
				if (me.getFactionId() != null)
				{
					// Check if player is an ally
					if (me.getFactionId().equals("varka_silenos_clan") && owner.isAlliedWithVarka())
					{
						return false;
					}
					
					if (me.getFactionId().equals("ketra_orc_clan") && owner.isAlliedWithKetra())
					{
						return false;
					}
				}
			}
		}
		
		// Check if the actor is a L2GuardInstance
		if (_actor instanceof L2GuardInstance)
		{
			// Check if the L2PcInstance target has karma (=PK)
			if ((target instanceof L2PcInstance) && (((L2PcInstance) target).getKarma() > 0))
			{
				return GeoData.getInstance().canSeeTarget(me, target); // Los Check
			}
			
			// Check if the L2MonsterInstance target is aggressive
			if (target instanceof L2MonsterInstance && Config.GUARD_ATTACK_AGGRO_MOB)
			{
				return (((L2MonsterInstance) target).isAggressive() && GeoData.getInstance().canSeeTarget(me, target));
			}
			
			return false;
		}
		// Check if the actor is a L2FriendlyMobInstance
		else if (_actor instanceof L2FriendlyMobInstance)
		{
			// Check if the target isn't another L2NpcInstance
			if (target instanceof L2NpcInstance)
			{
				return false;
			}
			
			// Check if the L2PcInstance target has karma (=PK)
			if ((target instanceof L2PcInstance) && (((L2PcInstance) target).getKarma() > 0))
			{
				return GeoData.getInstance().canSeeTarget(me, target); // Los Check
			}
			
			return false;
		}
		// The actor is a L2MonsterInstance
		else
		{
			// Check if the target isn't another L2NpcInstance
			if (target instanceof L2NpcInstance)
			{
				return false;
			}
			
			// depending on config, do not allow mobs to attack _new_ players in peacezones,
			// unless they are already following those players from outside the peacezone.
			if (!Config.ALT_MOB_AGGRO_IN_PEACEZONE && target.isInsideZone(L2Character.ZONE_PEACE))
			{
				return false;
			}
			
			if (Config.CHAMPION_ENABLE && me.isChampion() && Config.CHAMPION_PASSIVE)
			{
				return false;
			}
			
			// Check if the actor is Aggressive
			return (me.isAggressive() && GeoData.getInstance().canSeeTarget(me, target));
		}
	}
	
	public void startAITask()
	{
		// If not idle - create an AI task (schedule onEvtThink repeatedly)
		if (_aiTask == null)
		{
			_aiTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(this, 1000, 1000);
		}
	}
	
	@Override
	public void stopAITask()
	{
		if (_aiTask != null)
		{
			_aiTask.cancel(false);
			_aiTask = null;
		}
		super.stopAITask();
	}
	
	/**
	 * Set the Intention of this L2CharacterAI and create an AI Task executed every 1s (call onEvtThink method) for this L2Attackable.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : If actor _knowPlayer isn't EMPTY, AI_INTENTION_IDLE will be change in AI_INTENTION_ACTIVE</B></FONT><BR>
	 * <BR>
	 * @param intention The new Intention to set to the AI
	 * @param arg0 The first parameter of the Intention
	 * @param arg1 The second parameter of the Intention
	 */
	@Override
	protected synchronized void changeIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		if ((intention == AI_INTENTION_IDLE) || (intention == AI_INTENTION_ACTIVE))
		{
			// Check if actor is not dead
			if (!_actor.isAlikeDead())
			{
				L2Attackable npc = (L2Attackable) _actor;
				
				// If its _knownPlayer isn't empty set the Intention to AI_INTENTION_ACTIVE
				if (!npc.getKnownList().getKnownPlayers().isEmpty())
				{
					intention = AI_INTENTION_ACTIVE;
				}
				else
				{
					if (npc.getSpawn() != null)
					{
						if (!npc.isInsideRadius(npc.getSpawn().getLocX(), npc.getSpawn().getLocY(), npc.getSpawn().getLocZ(), Config.MAX_DRIFT_RANGE + Config.MAX_DRIFT_RANGE, true, false))
						{
							intention = AI_INTENTION_ACTIVE;
						}
					}
				}
			}
			
			if (intention == AI_INTENTION_IDLE)
			{
				// Set the Intention of this L2AttackableAI to AI_INTENTION_IDLE
				super.changeIntention(AI_INTENTION_IDLE, null, null);
				
				// Stop AI task and detach AI from NPC
				if (_aiTask != null)
				{
					_aiTask.cancel(true);
					_aiTask = null;
				}
				
				// Cancel the AI
				_accessor.detachAI();
				
				return;
			}
		}
		
		// Set the Intention of this L2AttackableAI to intention
		super.changeIntention(intention, arg0, arg1);
		
		// If not idle - create an AI task (schedule onEvtThink repeatedly)
		startAITask();
	}
	
	/**
	 * Manage the Attack Intention : Stop current Attack (if necessary), Calculate attack timeout, Start a new Attack and Launch Think Event.<BR>
	 * <BR>
	 * @param target The L2Character to attack
	 */
	@Override
	protected void onIntentionAttack(L2Character target)
	{
		// Calculate the attack timeout
		_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
		
		// self and buffs
		if ((_selfAnalysis.lastBuffTick + 100) < GameTimeController.getInstance().getGameTicks())
		{
			for (L2Skill sk : _selfAnalysis.buffSkills)
			{
				if (_actor.getFirstEffect(sk.getId()) == null)
				{
					if (_actor.getCurrentMp() < sk.getMpConsume())
					{
						continue;
					}
					
					if (_actor.isSkillDisabled(sk.getId()))
					{
						continue;
					}
					
					// no clan buffs here?
					if (sk.getTargetType() == L2Skill.SkillTargetType.TARGET_CLAN)
					{
						continue;
					}
					
					L2Object OldTarget = _actor.getTarget();
					_actor.setTarget(_actor);
					clientStopMoving(null);
					_accessor.doCast(sk);
					
					// forcing long reuse delay so if cast get interrupted or there would be several buffs, doesn't cast again
					_selfAnalysis.lastBuffTick = GameTimeController.getInstance().getGameTicks();
					_actor.setTarget(OldTarget);
				}
			}
		}
		
		// Manage the Attack Intention : Stop current Attack (if necessary), Start a new Attack and Launch Think Event
		super.onIntentionAttack(target);
	}
	
	/**
	 * Manage AI standard thinks of a L2Attackable (called by onEvtThink).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Update every 1s the _globalAggro counter to come close to 0</li>
	 * <li>If the actor is Aggressive and can attack, add all autoAttackable L2Character in its Aggro Range to its _aggroList, chose a target and order to attack it</li>
	 * <li>If the actor is a L2GuardInstance that can't attack, order to it to return to its home location</li>
	 * <li>If the actor is a L2MonsterInstance that can't attack, order to it to random walk (1/100)</li><BR>
	 * <BR>
	 */
	private void thinkActive()
	{
		L2Attackable npc = (L2Attackable) _actor;
		
		// Update every 1s the _globalAggro counter to come close to 0
		if (_globalAggro != 0)
		{
			if (_globalAggro < 0)
			{
				_globalAggro++;
			}
			else
			{
				_globalAggro--;
			}
		}
		
		// Add all autoAttackable L2Character in L2Attackable Aggro Range to its _aggroList with 0 damage and 1 hate
		// A L2Attackable isn't aggressive during 10s after its spawn because _globalAggro is set to -10
		if (_globalAggro >= 0)
		{
			// Get all visible objects inside its Aggro Range
			// L2Object[] objects = L2World.getInstance().getVisibleObjects(_actor, ((L2NpcInstance)_actor).getAggroRange());
			
			// Go through visible objects
			for (L2Object obj : npc.getKnownList().getKnownObjects().values())
			{
				if (!(obj instanceof L2Character))
				{
					continue;
				}
				
				L2Character target = (L2Character) obj;
				
				/*
				 * Check to see if this is a festival mob spawn. If it is, then check to see if the aggro trigger is a festival participant...if so, move to attack it.
				 */
				if ((_actor instanceof L2FestivalMonsterInstance) && (obj instanceof L2PcInstance))
				{
					L2PcInstance targetPlayer = (L2PcInstance) obj;
					
					if (!(targetPlayer.isFestivalParticipant()))
					{
						continue;
					}
				}
				
				// For each L2Character check if the target is autoattackable
				if (autoAttackCondition(target)) // check aggression
				{
					// Get the hate level of the L2Attackable against this L2Character target contained in _aggroList
					int hating = npc.getHating(target);
					
					// Add the attacker to the L2Attackable _aggroList with 0 damage and 1 hate
					if (hating == 0)
					{
						npc.addDamageHate(target, 0, 1);
					}
				}
			}
			
			// Choose a target from its aggroList
			L2Character hated;
			if (_actor.isConfused())
			{
				hated = getAttackTarget(); // effect handles selection
			}
			else
			{
				hated = npc.getMostHated();
			}
			
			// Order to the L2Attackable to attack the target
			if ((hated != null) && !npc.isCoreAIDisabled())
			{
				// Get the hate level of the L2Attackable against this L2Character target contained in _aggroList
				int aggro = npc.getHating(hated);
				
				if ((aggro + _globalAggro) > 0)
				{
					// Set the L2Character movement type to run and send Server->Client packet ChangeMoveType to all others L2PcInstance
					if (!_actor.isRunning())
					{
						// 40% chance to set walk timeout
						if (Rnd.get(100) <= 40 && MAX_ATTACK_WALK_TIMEOUT > 0)
						{
							// Calculate a new walk timeout
							_attackWalkTimeout = MAX_ATTACK_WALK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
						}
						else
						{
							_actor.setRunning();
						}
					}
					
					// Set the AI Intention to AI_INTENTION_ATTACK
					setIntention(CtrlIntention.AI_INTENTION_ATTACK, hated);
				}
				
				return;
			}
		}
		
		// Check if the actor is a L2GuardInstance
		if (_actor instanceof L2GuardInstance)
		{
			// Order to the L2GuardInstance to return to its home location because there's no target to attack
			((L2GuardInstance) _actor).returnHome();
		}
		
		// If this is a festival monster, then it remains in the same location.
		if (_actor instanceof L2FestivalMonsterInstance)
		{
			return;
		}
		
		// Minions following leader
		if ((_actor instanceof L2MinionInstance) && (((L2MinionInstance) _actor).getLeader() != null))
		{
			int offset;
			if (_actor.isRaid())
			{
				offset = 500; // For Raids - need correction
			}
			else
			{
				offset = 200; // For normal minions - need correction :)
			}
			
			if (((L2MinionInstance) _actor).getLeader().isRunning())
			{
				_actor.setRunning();
			}
			else
			{
				_actor.setWalking();
			}
			
			if (_actor.getPlanDistanceSq(((L2MinionInstance) _actor).getLeader()) > (offset * offset))
			{
				int x1, y1, z1;
				x1 = (((L2MinionInstance) _actor).getLeader().getX() + Rnd.nextInt((offset - 30) * 2)) - (offset - 30);
				y1 = (((L2MinionInstance) _actor).getLeader().getY() + Rnd.nextInt((offset - 30) * 2)) - (offset - 30);
				z1 = ((L2MinionInstance) _actor).getLeader().getZ();
				// Move the actor to Location (x,y,z) server side AND client side by sending Server->Client packet CharMoveToLocation (broadcast)
				moveTo(x1, y1, z1);
				return;
			}
			else if (Rnd.nextInt(RANDOM_WALK_RATE) == 0)
			{
				// self and clan buffs
				for (L2Skill sk : _selfAnalysis.buffSkills)
				{
					if (_actor.getFirstEffect(sk.getId()) == null)
					{
						// if clan buffs, don't buff every time
						if ((sk.getTargetType() != L2Skill.SkillTargetType.TARGET_SELF) && (Rnd.nextInt(2) != 0))
						{
							continue;
						}

						if (_actor.getCurrentMp() < sk.getMpConsume())
						{
							continue;
						}
						
						if (_actor.isSkillDisabled(sk.getId()))
						{
							continue;
						}
						
						L2Object OldTarget = _actor.getTarget();
						_actor.setTarget(_actor);
						clientStopMoving(null);
						_accessor.doCast(sk);
						_actor.setTarget(OldTarget);
						return;
					}
				}
			}
		}
		// Order to the L2MonsterInstance to random walk (1/100)
		else if ((npc.getSpawn() != null) && (Rnd.nextInt(RANDOM_WALK_RATE) == 0) && !((_actor instanceof L2RaidBossInstance) 
			|| (_actor instanceof L2MinionInstance) || (_actor instanceof L2GrandBossInstance) || (_actor instanceof L2ChestInstance) 
			|| (_actor instanceof L2GuardInstance) || (_actor instanceof L2SepulcherMonsterInstance) || npc.isQuestMonster()))
		{
			int x1, y1, z1;
			
			int range = Config.MAX_DRIFT_RANGE;
			
			// self and clan buffs
			for (L2Skill sk : _selfAnalysis.buffSkills)
			{
				if (_actor.getFirstEffect(sk.getId()) == null)
				{
					// if clan buffs, don't buff every time
					if ((sk.getTargetType() != L2Skill.SkillTargetType.TARGET_SELF) && (Rnd.nextInt(2) != 0))
					{
						continue;
					}
					
					if (_actor.getCurrentMp() < sk.getMpConsume())
					{
						continue;
					}
					
					if (_actor.isSkillDisabled(sk.getId()))
					{
						continue;
					}
					
					L2Object OldTarget = _actor.getTarget();
					_actor.setTarget(_actor);
					clientStopMoving(null);
					_accessor.doCast(sk);
					_actor.setTarget(OldTarget);
					return;
				}
			}
			
			// If NPC with random coord in territory
			if ((npc.getSpawn().getLocX() == 0) && (npc.getSpawn().getLocY() == 0))
			{
				// Calculate a destination point in the spawn area
				int p[] = Territory.getInstance().getRandomPoint(npc.getSpawn().getLocation());
				x1 = p[0];
				y1 = p[1];
				z1 = p[2];
				
				// Calculate the distance between the current position of the L2Character and the target (x,y)
				double distance2 = _actor.getPlanDistanceSq(x1, y1);
				
				if (distance2 > (range * range))
				{
					npc.setIsReturningToSpawnPoint(true);
					float delay = (float) Math.sqrt(distance2) / range;
					x1 = _actor.getX() + (int) ((x1 - _actor.getX()) / delay);
					y1 = _actor.getY() + (int) ((y1 - _actor.getY()) / delay);
				}
				
				// If NPC with random fixed coord, don't move (unless needs to return to spawnpoint)
				if ((Territory.getInstance().getProcMax(npc.getSpawn().getLocation()) > 0) && !npc.isReturningToSpawnPoint())
				{
					return;
				}
			}
			else
			{
				// If NPC with fixed coord
				x1 = npc.getSpawn().getLocX();
				y1 = npc.getSpawn().getLocY();
				z1 = npc.getSpawn().getLocZ();
				
				if (!_actor.isInsideRadius(x1, y1, z1, Config.MAX_DRIFT_RANGE + Config.MAX_DRIFT_RANGE, true, false))
				{
					if (npc.isRunning())
					{
						npc.setWalking();
					}
					npc.setIsReturningToSpawnPoint(true);
				}
				else
				{
					// If NPC with fixed coord
					x1 += Rnd.nextInt(range * 2) - range;
					y1 += Rnd.nextInt(range * 2) - range;
					z1 = npc.getZ();
				}
			}
			
			// Move the actor to Location (x,y,z) server side AND client side by sending Server->Client packet CharMoveToLocation (broadcast)
			moveTo(x1, y1, z1);
		}
	}
	
	/**
	 * Manage AI attack thinks of a L2Attackable (called by onEvtThink).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Update the attack timeout if actor is running</li>
	 * <li>If target is dead or timeout is expired, stop this attack and set the Intention to AI_INTENTION_ACTIVE</li>
	 * <li>Call all L2Object of its Faction inside the Faction Range</li>
	 * <li>Chose a target and order to attack it with magic skill or physical attack</li><BR>
	 * <BR>
	 * TODO: Manage casting rules to healer mobs (like Ant Nurses)
	 */
	private void thinkAttack()
	{
		L2Character originalAttackTarget = getAttackTarget();
		// Check if target is dead or if timeout is expired to stop this attack
		if ((originalAttackTarget == null) || originalAttackTarget.isAlikeDead() || (_attackTimeout < GameTimeController.getInstance().getGameTicks()))
		{
			// Stop hating this target after the attack timeout or if target is dead
			if (originalAttackTarget != null)
			{
				L2Attackable npc = (L2Attackable) _actor;
				npc.stopHating(originalAttackTarget);
			}
			
			// Cancel target and timeout
			_attackTimeout = Integer.MAX_VALUE;
			
			// Set the AI Intention to AI_INTENTION_ACTIVE
			setIntention(AI_INTENTION_ACTIVE);
			
			_actor.setWalking();
			return;
		}
		
		if (!(originalAttackTarget instanceof L2Attackable))
		{
			// Minions should assist their clan when in need
			if (_actor instanceof L2MinionInstance  && !_actor.isAttackingDisabled() && (_selfAnalysis.hasResurrect || _selfAnalysis.canHealOthers))
			{
				// Get leader and use it to get all minions
				L2MonsterInstance leader = ((L2MinionInstance) _actor).getLeader();
				if (leader != null)
				{
					List<L2NpcInstance> members = new ArrayList<>();
					members.add(leader); // Leader should always be checked first
					
					// Add all minions
					if (leader.hasMinions())
					{
						members.addAll(leader.getSpawnedMinions());
					}
					
					// Get the member in need. Leader is checked first
					for (L2NpcInstance member : members)
					{
						// Actor does not help itself here
						if (_actor == member)
						{
							continue;
						}
						
						// Allow use of resurrect skills on leader
						if (_selfAnalysis.hasResurrect && member.isDead() && member == leader)
						{
							for (L2Skill sk : _selfAnalysis.resurrectSkills)
							{
								if (_actor.getCurrentMp() < sk.getMpConsume())
								{
									continue;
								}
								
								if (_actor.isSkillDisabled(sk.getId()))
								{
									continue;
								}
								
								if (!Util.checkIfInRange(sk.getCastRange(), _actor, member, true))
								{
									continue;
								}
								
								// Chance to fail
								if (Rnd.get(100) < 10)
								{
									continue;
								}
								
								if (!GeoData.getInstance().canSeeTarget(_actor, member))
								{
									break;
								}
								
								L2Object OldTarget = _actor.getTarget();
								_actor.setTarget(member);
								// would this ever be fast enough for the decay not to run?
								// giving some extra seconds
								DecayTaskManager.getInstance().cancelDecayTask(member);
								DecayTaskManager.getInstance().addDecayTask(member);
								clientStopMoving(null);
								_accessor.doCast(sk);
								_actor.setTarget(OldTarget);
								return;
							}
						}
						// Heal target if needed
						else if (_selfAnalysis.canHealOthers && member.isInCombat() && (member.getCurrentHp() <= (member.getMaxHp() * 0.6)) && (_actor.getCurrentHp() > (_actor.getMaxHp() / 2)) && (_actor.getCurrentMp() > (_actor.getMaxMp() / 2)))
						{
							for (L2Skill sk : _selfAnalysis.healSkills)
							{
								if (_actor.getCurrentMp() < sk.getMpConsume())
								{
									continue;
								}
								
								if (_actor.isSkillDisabled(sk.getId()))
								{
									continue;
								}
								
								if (!Util.checkIfInRange(sk.getCastRange(), _actor, member, true))
								{
									continue;
								}
								
								// This check is useful for effects like Chant of Life
								if (sk.hasEffects() && member.getFirstEffect(sk) != null)
								{
									continue;
								}
								
								// Chance to fail
								int chance = (member == leader) ? 10 : 5;
								if (Rnd.get(100) < chance)
								{
									continue;
								}
								
								if (!GeoData.getInstance().canSeeTarget(_actor, member))
								{
									break;
								}
								
								L2Object OldTarget = _actor.getTarget();
								_actor.setTarget(member);
								clientStopMoving(null);
								_accessor.doCast(sk);
								_actor.setTarget(OldTarget);
								return;
							}
						}
					}
				}
			}
			// Handle all L2Object of its Faction inside the Faction Range
			else if (((L2NpcInstance) _actor).getFactionId() != null)
			{
				String factionId = ((L2NpcInstance) _actor).getFactionId();
				
				// Go through all L2Object that belong to its faction
				for (L2Object obj : _actor.getKnownList().getKnownObjects().values())
				{
					if (obj instanceof L2NpcInstance)
					{
						L2NpcInstance npc = (L2NpcInstance) obj;
						
						// Do not call chests for help
						if (npc instanceof L2ChestInstance)
						{
							continue;
						}
						
						String factionNpc = npc.getFactionId();
						boolean sevenSignFaction = false;
	
						// Catacomb mobs should assist lilim and nephilim other than dungeon
						if ("c_dungeon_clan".equals(factionId) && ("c_dungeon_lilim".equals(factionNpc) || "c_dungeon_nephi".equals(factionNpc)))
						{
							sevenSignFaction = true;
						}
						// Lilim mobs should assist other Lilim and catacomb mobs
						else if ("c_dungeon_lilim".equals(factionId) && "c_dungeon_clan".equals(factionNpc))
						{
							sevenSignFaction = true;
						}
						// Nephilim mobs should assist other Nephilim and catacomb mobs
						else if ("c_dungeon_nephi".equals(factionId) && "c_dungeon_clan".equals(factionNpc))
						{
							sevenSignFaction = true;
						}
						
						if (!factionId.equals(factionNpc) && !sevenSignFaction)
						{
							continue;
						}
						
						L2CharacterAI ai = npc.getAI();
						
						// Check if the L2Object is inside the Faction Range of the actor
						if (ai != null && _actor.isInsideRadius(npc, (npc.getFactionRange() + npc.getAggroRange()), false, true))
						{
							if ((Math.abs(originalAttackTarget.getZ() - npc.getZ()) < 600) && _actor.getAttackByList().contains(originalAttackTarget) 
								&& ((ai._intention == CtrlIntention.AI_INTENTION_IDLE) || (ai._intention == CtrlIntention.AI_INTENTION_ACTIVE)) 
								&& GeoData.getInstance().canSeeTarget(_actor, npc))
							{
								L2PcInstance player = originalAttackTarget.getActingPlayer();
								if (player != null)
								{
									if (npc.getTemplate().getEventQuests(Quest.QuestEventType.ON_FACTION_CALL) != null)
									{
										for (Quest quest : npc.getTemplate().getEventQuests(Quest.QuestEventType.ON_FACTION_CALL))
										{
											quest.notifyFactionCall(npc, (L2NpcInstance) _actor, player, (originalAttackTarget instanceof L2Summon));
										}
									}
								}
								
								if ((originalAttackTarget instanceof L2PcInstance) && originalAttackTarget.isInParty() && originalAttackTarget.getParty().isInDimensionalRift())
								{
									byte riftType = originalAttackTarget.getParty().getDimensionalRift().getType();
									byte riftRoom = originalAttackTarget.getParty().getDimensionalRift().getCurrentRoom();
									if ((_actor instanceof L2RiftInvaderInstance) && !DimensionalRiftManager.getInstance().getRoom(riftType, riftRoom).checkIfInZone(npc.getX(), npc.getY(), npc.getZ()))
									{
										continue;
									}
								}
								
								// Notify the L2Object AI with EVT_AGGRESSION
								ai.notifyEvent(CtrlEvent.EVT_AGGRESSION, originalAttackTarget, 1);
							}
							
							// Assist friends
							if (_selfAnalysis.canHealOthers && !_actor.isAttackingDisabled() && (npc.getCurrentHp() <= (npc.getMaxHp() * 0.6)) && (_actor.getCurrentHp() > (_actor.getMaxHp() / 2)) && (_actor.getCurrentMp() > (_actor.getMaxMp() / 2)))
							{
								if (npc.isInCombat())
								{
									for (L2Skill sk : _selfAnalysis.healSkills)
									{
										if (_actor.getCurrentMp() < sk.getMpConsume())
										{
											continue;
										}
										
										if (_actor.isSkillDisabled(sk.getId()))
										{
											continue;
										}
										
										if (!Util.checkIfInRange(sk.getCastRange(), _actor, npc, true))
										{
											continue;
										}
										
										// This check is useful for effects like Chant of Life
										if (sk.hasEffects() && npc.getFirstEffect(sk) != null)
										{
											continue;
										}
										
										// Chance to fail
										if (Rnd.get(100) < 5)
										{
											continue;
										}
										
										if (!GeoData.getInstance().canSeeTarget(_actor, npc))
										{
											break;
										}
										
										L2Object OldTarget = _actor.getTarget();
										_actor.setTarget(npc);
										clientStopMoving(null);
										_accessor.doCast(sk);
										_actor.setTarget(OldTarget);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (_actor.isAttackingDisabled())
		{
			return;
		}
		
		// Get 2 most hated chars
		List<L2Character> hated = ((L2Attackable) _actor).get2MostHated();
		if (_actor.isConfused())
		{
			if (hated != null)
			{
				hated.set(0, originalAttackTarget); // Effect handles selection
			}
			else
			{
				hated = new ArrayList<>();
				hated.add(originalAttackTarget);
				hated.add(null);
			}
		}
		
		if ((hated == null) || (hated.get(0) == null))
		{
			setIntention(AI_INTENTION_ACTIVE);
			return;
		}
		
		if (hated.get(0) != originalAttackTarget)
		{
			setAttackTarget(hated.get(0));
		}
		
		_mostHatedAnalysis.update(hated.get(0));
		_secondMostHatedAnalysis.update(hated.get(1));
		
		// Get all information needed to choose between physical or magical attack
		_actor.setTarget(_mostHatedAnalysis.character);
		double dist2 = _actor.getPlanDistanceSq(_mostHatedAnalysis.character.getX(), _mostHatedAnalysis.character.getY());
		final int collision = (int) _actor.getTemplate().collisionRadius;
		final int combinedCollision = collision + (int) _mostHatedAnalysis.character.getTemplate().collisionRadius;
		int range = _actor.getPhysicalAttackRange() + combinedCollision;
		
		// Reconsider target if _actor hasn't got hits in for last 16 sec
		if (!_actor.isMuted() && ((_attackTimeout - 160) < GameTimeController.getInstance().getGameTicks()) && (_secondMostHatedAnalysis.character != null))
		{
			if (Util.checkIfInRange(900, _actor, hated.get(1), true))
			{
				// take off 2* the amount the aggro is larger than second most
				int aggro = 2 * (((L2Attackable) _actor).getHating(hated.get(0)) - ((L2Attackable) _actor).getHating(hated.get(1)));
				onEvtAggression(hated.get(0), -aggro);
				// Calculate a new attack timeout
				_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
			}
		}
		
		// Reconsider target during next round if actor is rooted and cannot reach mostHated but can
		// reach secondMostHated
		if (_actor.isRooted() && (_secondMostHatedAnalysis.character != null))
		{
			if (_selfAnalysis.isMage && (dist2 > (_selfAnalysis.maxCastRange * _selfAnalysis.maxCastRange)) 
				&& (_actor.getPlanDistanceSq(_secondMostHatedAnalysis.character.getX(), _secondMostHatedAnalysis.character.getY()) < (_selfAnalysis.maxCastRange * _selfAnalysis.maxCastRange)))
			{
				int aggro = 1 + (((L2Attackable) _actor).getHating(hated.get(0)) - ((L2Attackable) _actor).getHating(hated.get(1)));
				onEvtAggression(hated.get(0), -aggro);
			}
			else if ((dist2 > (range * range)) && (_actor.getPlanDistanceSq(_secondMostHatedAnalysis.character.getX(), _secondMostHatedAnalysis.character.getY()) < (range * range)))
			{
				int aggro = 1 + (((L2Attackable) _actor).getHating(hated.get(0)) - ((L2Attackable) _actor).getHating(hated.get(1)));
				onEvtAggression(hated.get(0), -aggro);
			}
		}
		
		// Considering, if bigger range will be attempted
		if (dist2 < (10000 + (combinedCollision * combinedCollision)) && !_selfAnalysis.isFighter && !_selfAnalysis.isBalanced 
			&& (_selfAnalysis.hasLongRangeSkills && (_mostHatedAnalysis.character.isRooted() || _mostHatedAnalysis.isSlower) || _selfAnalysis.isArcher) 
			&& (_mostHatedAnalysis.isBalanced || _mostHatedAnalysis.isFighter) && Rnd.get(100) <= 25) // Chance
		{
			final int posX = _actor.getX() + ((_mostHatedAnalysis.character.getX() < _actor.getX()) ? 300 : -300);
			final int posY = _actor.getY() + ((_mostHatedAnalysis.character.getY() < _actor.getY()) ? 300 : -300);
			final int posZ = _actor.getZ() + 30;
			
			if (GeoData.getInstance().canMove(_actor.getX(), _actor.getY(), _actor.getZ(), posX, posY, posZ))
			{
				setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(posX, posY, posZ));
			}
			return;
		}

		if (_mostHatedAnalysis.character.isMoving())
		{
			range += 50;
		}

		// Check if the actor is far from target
		if (dist2 > (range * range))
		{
			if (!_actor.isMuted() && (_selfAnalysis.hasLongRangeSkills || !_selfAnalysis.healSkills.isEmpty()))
			{
				// Check for long ranged skills and heal/buff skills
				if (!_mostHatedAnalysis.isCancelled)
				{
					if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
					{
						for (L2Skill sk : _selfAnalysis.cancelSkills)
						{
							int castRange = sk.getCastRange() + combinedCollision;
							if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
							{
								continue;
							}
							
							if (Rnd.nextInt(100) <= 8)
							{
								clientStopMoving(null);
								_accessor.doCast(sk);
								_mostHatedAnalysis.isCancelled = true;
								_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
								return;
							}
						}
					}
				}
				
				if ((_selfAnalysis.lastDebuffTick + 60) < GameTimeController.getInstance().getGameTicks())
				{
					if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
					{
						for (L2Skill sk : _selfAnalysis.debuffSkills)
						{
							int castRange = sk.getCastRange() + combinedCollision;
							if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
							{
								continue;
							}
	
							int chance = 8;
							if (_selfAnalysis.isFighter && _mostHatedAnalysis.isMage)
							{
								chance = 3;
							}
							if (_selfAnalysis.isFighter && _mostHatedAnalysis.isArcher)
							{
								chance = 12;
							}
							if (_selfAnalysis.isMage && !_mostHatedAnalysis.isMage)
							{
								chance = 10;
							}
							if (_mostHatedAnalysis.isMagicResistant)
							{
								chance /= 2;
							}
							
							if (Rnd.nextInt(100) <= chance)
							{
								clientStopMoving(null);
								_accessor.doCast(sk);
								_selfAnalysis.lastDebuffTick = GameTimeController.getInstance().getGameTicks();
								_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
								return;
							}
						}
					}
				}
				
				if (!_mostHatedAnalysis.character.isMuted())
				{
					if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
					{
						int chance = 8;
						if (!(_mostHatedAnalysis.isMage || _mostHatedAnalysis.isBalanced))
						{
							chance = 3;
						}
						
						for (L2Skill sk : _selfAnalysis.muteSkills)
						{
							int castRange = sk.getCastRange() + combinedCollision;
							if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
							{
								continue;
							}
							
							if (Rnd.nextInt(100) <= chance)
							{
								clientStopMoving(null);
								_accessor.doCast(sk);
								_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
								return;
							}
						}
					}
				}
				
				if ((_secondMostHatedAnalysis.character != null) && !_secondMostHatedAnalysis.character.isMuted() && (_secondMostHatedAnalysis.isMage || _secondMostHatedAnalysis.isBalanced))
				{
					if (GeoData.getInstance().canSeeTarget(_actor, _secondMostHatedAnalysis.character))
					{
						double secondHatedDist2 = _actor.getPlanDistanceSq(_secondMostHatedAnalysis.character.getX(), _secondMostHatedAnalysis.character.getY());
						for (L2Skill sk : _selfAnalysis.muteSkills)
						{
							int castRange = sk.getCastRange() + combinedCollision;
							if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (secondHatedDist2 > (castRange * castRange)))
							{
								continue;
							}
							
							if (Rnd.nextInt(100) <= 2)
							{
								_actor.setTarget(_secondMostHatedAnalysis.character);
								clientStopMoving(null);
								_accessor.doCast(sk);
								_actor.setTarget(_mostHatedAnalysis.character);
								return;
							}
						}
					}
				}
				
				if (!_mostHatedAnalysis.character.isSleeping())
				{
					if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
					{
						for (L2Skill sk : _selfAnalysis.sleepSkills)
						{
							int castRange = sk.getCastRange() + combinedCollision;
							if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
							{
								continue;
							}
							
							if (Rnd.nextInt(100) <= 1)
							{
								clientStopMoving(null);
								_accessor.doCast(sk);
								_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
								return;
							}
						}
					}
				}
				
				if ((_secondMostHatedAnalysis.character != null) && !_secondMostHatedAnalysis.character.isSleeping())
				{
					if (GeoData.getInstance().canSeeTarget(_actor, _secondMostHatedAnalysis.character))
					{
						double secondHatedDist2 = _actor.getPlanDistanceSq(_secondMostHatedAnalysis.character.getX(), _secondMostHatedAnalysis.character.getY());
						for (L2Skill sk : _selfAnalysis.sleepSkills)
						{
							int castRange = sk.getCastRange() + combinedCollision;
							if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (secondHatedDist2 > (castRange * castRange)))
							{
								continue;
							}

							if (Rnd.nextInt(100) <= 3)
							{
								_actor.setTarget(_secondMostHatedAnalysis.character);
								clientStopMoving(null);
								_accessor.doCast(sk);
								_actor.setTarget(_mostHatedAnalysis.character);
								return;
							}
						}
					}
				}
				
				if (!_mostHatedAnalysis.character.isRooted())
				{
					if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
					{
						for (L2Skill sk : _selfAnalysis.rootSkills)
						{
							int castRange = sk.getCastRange() + combinedCollision;
							if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
							{
								continue;
							}
							
							if (Rnd.nextInt(100) <= (_mostHatedAnalysis.isSlower ? 3 : 8))
							{
								clientStopMoving(null);
								_accessor.doCast(sk);
								_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
								return;
							}
						}
					}
				}
				
				if (!_mostHatedAnalysis.character.isAttackingDisabled())
				{
					if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
					{
						for (L2Skill sk : _selfAnalysis.generalDisablers)
						{
							int castRange = sk.getCastRange() + combinedCollision;
							if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
							{
								continue;
							}
							
							if (Rnd.nextInt(100) <= ((_selfAnalysis.isFighter && _actor.isRooted()) ? 15 : 7))
							{
								clientStopMoving(null);
								_accessor.doCast(sk);
								_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
								return;
							}
						}
					}
				}
				
				if (_actor.getCurrentHp() < (_actor.getMaxHp() * 0.4))
				{
					for (L2Skill sk : _selfAnalysis.healSkills)
					{
						if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)))
						{
							continue;
						}
						
						// This check is useful for effects like Chant of Life
						if (sk.hasEffects() && _actor.getFirstEffect(sk) != null)
						{
							continue;
						}
						
						int chance = 7;
						if (_mostHatedAnalysis.character.isAttackingDisabled())
						{
							chance += 10;
						}
						
						if ((_secondMostHatedAnalysis.character == null) || _secondMostHatedAnalysis.character.isAttackingDisabled())
						{
							chance += 10;
						}
						
						if (Rnd.nextInt(100) <= chance)
						{
							_actor.setTarget(_actor);
							clientStopMoving(null);
							_accessor.doCast(sk);
							_actor.setTarget(_mostHatedAnalysis.character);
							return;
						}
					}
				}
				
				if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
				{
					// chance decision for launching long range skills
					int castingChance = 5;
					if (_selfAnalysis.isMage)
					{
						castingChance = 50; // mages
					}
					
					if (_selfAnalysis.isBalanced)
					{
						if (!_mostHatedAnalysis.isFighter)
						{
							castingChance = 15;
						}
						else
						{
							castingChance = 25; // stay away from fighters
						}
					}
					
					if (_selfAnalysis.isFighter)
					{
						if (_mostHatedAnalysis.isMage)
						{
							castingChance = 3;
						}
						else
						{
							castingChance = 7;
						}
						
						if (_actor.isRooted())
						{
							castingChance = 20; // doesn't matter if no success first round
						}
					}
					
					for (L2Skill sk : _selfAnalysis.generalSkills)
					{
						int castRange = sk.getCastRange() + combinedCollision;
						if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
						{
							continue;
						}
						
						if (Rnd.nextInt(100) <= castingChance)
						{
							clientStopMoving(null);
							_accessor.doCast(sk);
							_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
							return;
						}
					}
				}
			}
			
			// Move the actor to Pawn server side AND client side by sending Server->Client packet MoveToPawn (broadcast)
			if (_selfAnalysis.isMage)
			{
				if (_actor.isMuted() && GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
				{
					return;
				}
			}
			
			if (_mostHatedAnalysis.character.isMoving())
			{
				range -= 100;
			}
			if (range < 5)
			{
				range = 5;
			}
			moveToPawn(_mostHatedAnalysis.character, range);
			return;
		}
		// **************************************************
		// Else, if this is close enough for physical attacks
		// In case many mobs are trying to hit from same place, move a bit,
		// circling around the target
		if (!_actor.isMovementDisabled() && Rnd.nextInt(100) <= 33)
		{
			for (L2Object nearby : _actor.getKnownList().getKnownObjects().values())
			{
				if (nearby instanceof L2Attackable && _actor.isInsideRadius(nearby, collision, false, false) && nearby != _mostHatedAnalysis.character)
				{
					int newX = combinedCollision + Rnd.get(40);
					if (Rnd.nextBoolean())
					{
						newX = _mostHatedAnalysis.character.getX() + newX;
					}
					else
					{
						newX = _mostHatedAnalysis.character.getX() - newX;
					}

					int newY = combinedCollision + Rnd.get(40);
					if (Rnd.nextBoolean())
					{
						newY = _mostHatedAnalysis.character.getY() + newY;
					}
					else
					{
						newY = _mostHatedAnalysis.character.getY() - newY;
					}
					
					if (!_actor.isInsideRadius(newX, newY, collision, false))
					{
						int newZ = _actor.getZ() + 30;
						if (GeoData.getInstance().canMove(_actor.getX(), _actor.getY(), _actor.getZ(), newX, newY, newZ))
						{
							moveTo(newX, newY, newZ);
						}
					}
					return;
				}
			}
		}
		
		// Calculate a new attack timeout.
		_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
		
		// Check for close combat skills && heal/buff skills
		if (!_mostHatedAnalysis.isCancelled)
		{
			if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
			{
				for (L2Skill sk : _selfAnalysis.cancelSkills)
				{
					if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
					{
						continue;
					}
					
					int castRange = sk.getCastRange() + combinedCollision;
					if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
					{
						continue;
					}

					if (Rnd.nextInt(100) <= 8)
					{
						clientStopMoving(null);
						_accessor.doCast(sk);
						_mostHatedAnalysis.isCancelled = true;
						return;
					}
				}
			}
		}
		
		if ((_selfAnalysis.lastDebuffTick + 60) < GameTimeController.getInstance().getGameTicks())
		{
			if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
			{
				for (L2Skill sk : _selfAnalysis.debuffSkills)
				{
					if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
					{
						continue;
					}
					
					int castRange = sk.getCastRange() + combinedCollision;
					if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
					{
						continue;
					}
	
					int chance = 5;
					if (_selfAnalysis.isFighter && _mostHatedAnalysis.isMage)
					{
						chance = 3;
					}
					if (_selfAnalysis.isFighter && _mostHatedAnalysis.isArcher)
					{
						chance = 3;
					}
					if (_selfAnalysis.isMage && !_mostHatedAnalysis.isMage)
					{
						chance = 4;
					}
					if (_mostHatedAnalysis.isMagicResistant)
					{
						chance /= 2;
					}
					if (sk.getCastRange() < 200)
					{
						chance += 3;
					}
					
					if (Rnd.nextInt(100) <= chance)
					{
						clientStopMoving(null);
						_accessor.doCast(sk);
						_selfAnalysis.lastDebuffTick = GameTimeController.getInstance().getGameTicks();
						return;
					}
				}
			}
		}
		
		if (!_mostHatedAnalysis.character.isMuted() && (_mostHatedAnalysis.isMage || _mostHatedAnalysis.isBalanced))
		{
			if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
			{
				for (L2Skill sk : _selfAnalysis.muteSkills)
				{
					if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
					{
						continue;
					}
					
					int castRange = sk.getCastRange() + combinedCollision;
					if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
					{
						continue;
					}
	
					if (Rnd.nextInt(100) <= 7)
					{
						clientStopMoving(null);
						_accessor.doCast(sk);
						return;
					}
				}
			}
		}
		
		if ((_secondMostHatedAnalysis.character != null) && !_secondMostHatedAnalysis.character.isMuted() && (_secondMostHatedAnalysis.isMage || _secondMostHatedAnalysis.isBalanced))
		{
			if (GeoData.getInstance().canSeeTarget(_actor, _secondMostHatedAnalysis.character))
			{
				double secondHatedDist2 = _actor.getPlanDistanceSq(_secondMostHatedAnalysis.character.getX(), _secondMostHatedAnalysis.character.getY());
				for (L2Skill sk : _selfAnalysis.muteSkills)
				{
					if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
					{
						continue;
					}
					
					int castRange = sk.getCastRange() + combinedCollision;
					if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (secondHatedDist2 > (castRange * castRange)))
					{
						continue;
					}
					
					if (Rnd.nextInt(100) <= 3)
					{
						_actor.setTarget(_secondMostHatedAnalysis.character);
						clientStopMoving(null);
						_accessor.doCast(sk);
						_actor.setTarget(_mostHatedAnalysis.character);
						return;
					}
				}
			}
		}
		
		if ((_secondMostHatedAnalysis.character != null) && !_secondMostHatedAnalysis.character.isSleeping())
		{
			if (GeoData.getInstance().canSeeTarget(_actor, _secondMostHatedAnalysis.character))
			{
				double secondHatedDist2 = _actor.getPlanDistanceSq(_secondMostHatedAnalysis.character.getX(), _secondMostHatedAnalysis.character.getY());
				for (L2Skill sk : _selfAnalysis.sleepSkills)
				{
					if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
					{
						continue;
					}
					
					int castRange = sk.getCastRange() + combinedCollision;
					if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (secondHatedDist2 > (castRange * castRange)))
					{
						continue;
					}
					
					if (Rnd.nextInt(100) <= 4)
					{
						_actor.setTarget(_secondMostHatedAnalysis.character);
						clientStopMoving(null);
						_accessor.doCast(sk);
						_actor.setTarget(_mostHatedAnalysis.character);
						return;
					}
				}
			}
		}
		
		if (!_mostHatedAnalysis.character.isRooted() && _mostHatedAnalysis.isFighter && !_selfAnalysis.isFighter)
		{
			if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
			{
				for (L2Skill sk : _selfAnalysis.rootSkills)
				{
					if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
					{
						continue;
					}
					
					int castRange = sk.getCastRange() + combinedCollision;
					if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
					{
						continue;
					}
	
					if (Rnd.nextInt(100) <= 4)
					{
						clientStopMoving(null);
						_accessor.doCast(sk);
						return;
					}
				}
			}
		}
		
		if (!_mostHatedAnalysis.character.isAttackingDisabled())
		{
			if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
			{
				for (L2Skill sk : _selfAnalysis.generalDisablers)
				{
					if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
					{
						continue;
					}
					
					int castRange = sk.getCastRange() + combinedCollision;
					if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
					{
						continue;
					}
					
					if (Rnd.nextInt(100) <= ((sk.getCastRange() < 200) ? 10 : 7))
					{
						clientStopMoving(null);
						_accessor.doCast(sk);
						return;
					}
				}
			}
		}
		
		if (_actor.getCurrentHp() < (_actor.getMaxHp() * 0.4))
		{
			for (L2Skill sk : _selfAnalysis.healSkills)
			{
				if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
				{
					continue;
				}
				
				if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)))
				{
					continue;
				}
				
				// This check is useful for effects like Chant of Life
				if (sk.hasEffects() && _actor.getFirstEffect(sk) != null)
				{
					continue;
				}
				
				int chance = 7;
				if (_mostHatedAnalysis.character.isAttackingDisabled())
				{
					chance += 10;
				}
				if ((_secondMostHatedAnalysis.character == null) || _secondMostHatedAnalysis.character.isAttackingDisabled())
				{
					chance += 10;
				}
				
				if (Rnd.nextInt(100) <= chance)
				{
					_actor.setTarget(_actor);
					clientStopMoving(null);
					_accessor.doCast(sk);
					_actor.setTarget(_mostHatedAnalysis.character);
					return;
				}
			}
		}
		
		if (GeoData.getInstance().canSeeTarget(_actor, _mostHatedAnalysis.character))
		{
			for (L2Skill sk : _selfAnalysis.generalSkills)
			{
				if ((_actor.isMuted() && sk.isMagic()) || (_actor.isPhysicalMuted() && !sk.isMagic()))
				{
					continue;
				}
				
				int castRange = sk.getCastRange() + combinedCollision;
				if (_actor.isSkillDisabled(sk.getId()) || (_actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) || (dist2 > (castRange * castRange)))
				{
					continue;
				}
	
				// chance decision for launching general skills in melee fight
				// close range skills should be higher, long range lower
				int castingChance = 5;
				if (_selfAnalysis.isMage)
				{
					if (sk.getCastRange() < 200)
					{
						castingChance = 35;
					}
					else
					{
						castingChance = 25; // mages
					}
				}
				
				if (_selfAnalysis.isBalanced)
				{
					if (sk.getCastRange() < 200)
					{
						castingChance = 12;
					}
					else
					{
						if (_mostHatedAnalysis.isMage)
						{
							castingChance = 2;
						}
						else
						{
							castingChance = 5;
						}
					}
				}
				
				if (_selfAnalysis.isFighter)
				{
					if (sk.getCastRange() < 200)
					{
						castingChance = 12;
					}
					else
					{
						if (_mostHatedAnalysis.isMage)
						{
							castingChance = 1;
						}
						else
						{
							castingChance = 3;
						}
					}
				}
				
				if (Rnd.nextInt(100) <= castingChance)
				{
					clientStopMoving(null);
					_accessor.doCast(sk);
					return;
				}
			}
		}
		
		// Finally, physical attacks
		clientStopMoving(null);
		_accessor.doAttack(getAttackTarget());
	}
	
	/**
	 * Launch actions corresponding to the Event Attacked.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Init the attack : Calculate the attack timeout, Set the _globalAggro to 0, Add the attacker to the actor _aggroList</li>
	 * <li>Set the L2Character movement type to run and send Server->Client packet ChangeMoveType to all others L2PcInstance</li>
	 * <li>Set the Intention to AI_INTENTION_ATTACK</li><BR>
	 * <BR>
	 * @param attacker The L2Character that attacks the actor
	 */
	@Override
	protected void onEvtAttacked(L2Character attacker)
	{
		L2Attackable me = (L2Attackable) _actor;
		
		// Calculate the attack timeout
		_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
		
		// Set the _globalAggro to 0 to permit attack even just after spawn
		if (_globalAggro < 0)
		{
			_globalAggro = 0;
		}
		
		// Add the attacker to the _aggroList of the actor
		me.addDamageHate(attacker, 0, 1);
		
		// Set the L2Character movement type to run and send Server->Client packet ChangeMoveType to all others L2PcInstance
		if (!_actor.isRunning())
		{
			// 40% chance to set walk timeout
			if (Rnd.get(100) <= 40 && MAX_ATTACK_WALK_TIMEOUT > 0)
			{
				// Calculate a new walk timeout
				_attackWalkTimeout = MAX_ATTACK_WALK_TIMEOUT + GameTimeController.getInstance().getGameTicks();
			}
			else
			{
				_actor.setRunning();
			}
		}
		
		// Set the Intention to AI_INTENTION_ATTACK
		if (getIntention() != AI_INTENTION_ATTACK)
		{
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
		}
		else if (me.getMostHated() != getAttackTarget())
		{
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
		}
		
		// If this attackable is a L2MonsterInstance and it has spawned minions, call its minions to battle
		if (me instanceof L2MonsterInstance)
		{
			L2MonsterInstance master = (L2MonsterInstance) me;
			if (me instanceof L2MinionInstance)
			{
				master = ((L2MinionInstance) me).getLeader();
				if ((master != null) && !master.isInCombat() && !master.isDead())
				{
					master.addDamageHate(attacker, 0, 1);
					master.callMinionsToAssist(attacker);
				}
			}
			else if (master.hasMinions())
			{
				master.callMinionsToAssist(attacker);
			}
		}
		
		super.onEvtAttacked(attacker);
	}
	
	/**
	 * Launch actions corresponding to the Event Aggression.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Add the target to the actor _aggroList or update hate if already present</li>
	 * <li>Set the actor Intention to AI_INTENTION_ATTACK (if actor is L2GuardInstance check if it isn't too far from its home location)</li><BR>
	 * <BR>
	 * @param aggro The value of hate to add to the actor against the target
	 */
	@Override
	protected void onEvtAggression(L2Character target, int aggro)
	{
		L2Attackable me = (L2Attackable) _actor;
		
		if (target != null)
		{
			// Add the target to the actor _aggroList or update hate if already present
			me.addDamageHate(target, 0, aggro);
			
			// Get the hate of the actor against the target
			// only if hate is definitely reduced
			if (aggro < 0)
			{
				if (me.getHating(target) <= 0)
				{
					if (me.getMostHated() == null)
					{
						_globalAggro = -25;
						me.clearAggroList();
						setIntention(AI_INTENTION_ACTIVE);
						_actor.setWalking();
					}
				}
				return;
			}
			
			// Set the L2Character movement type to run and send Server->Client packet ChangeMoveType to all others L2PcInstance
			if (!_actor.isRunning())
			{
				// Reset walk timeout
				if (_attackWalkTimeout != 0)
				{
					_attackWalkTimeout = 0;
				}
				_actor.setRunning();
			}
			
			// Set the actor AI Intention to AI_INTENTION_ATTACK
			if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
			{
				setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
			}
		}
		else
		{
			// currently only for setting lower general aggro
			if (aggro >= 0)
			{
				return;
			}
			
			L2Character mostHated = me.getMostHated();
			if (mostHated == null)
			{
				_globalAggro = -25;
				return;
			}

			for (L2Character aggroed : me.getAggroList().keySet())
			{
				me.addDamageHate(aggroed, 0, aggro);
			}
			
			aggro = me.getHating(mostHated);
			if (aggro <= 0)
			{
				_globalAggro = -25;
				me.clearAggroList();
				setIntention(AI_INTENTION_ACTIVE);
				_actor.setWalking();
			}
		}
	}
	
	/**
	 * Manage AI thinking actions of a L2Attackable.<BR>
	 * <BR>
	 */
	@Override
	protected void onEvtThink()
	{
		// Check if a thinking action isn't already in progress
		if (_thinking)
		{
			return;
		}
		
		L2Object target = _actor.getTarget();
		// Stop skill cast if actor cannot see target
		if (_actor.isCastingNow() && target != null && target != _actor)
		{
			if (!GeoData.getInstance().canSeeTarget(_actor, target))
			{
				_actor.abortCast();
			}
		}
		
		// Check if the actor can't use skills
		if (_actor.isAllSkillsDisabled())
		{
			return;
		}
		
		// Start thinking action
		_thinking = true;
		
		// Walk timeout, start running
		if (_attackWalkTimeout != 0)
		{
			if (_attackWalkTimeout < GameTimeController.getInstance().getGameTicks())
			{
				_actor.setRunning();
				// Set it to zero mostly for comparison reasons
				_attackWalkTimeout = 0;
			}
		}
		
		try
		{
			// Manage AI thinks of a L2Attackable
			if (getIntention() == AI_INTENTION_ACTIVE)
			{
				thinkActive();
			}
			else if (getIntention() == AI_INTENTION_ATTACK)
			{
				thinkAttack();
			}
		}
		finally
		{
			// Stop thinking action
			_thinking = false;
		}
	}
}