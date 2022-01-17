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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.ai.L2CharacterAI;
import net.sf.l2j.gameserver.ai.L2SummonAI;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.handler.ItemHandler;
import net.sf.l2j.gameserver.model.L2Attackable.AggroInfo;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.knownlist.SummonKnownList;
import net.sf.l2j.gameserver.model.actor.stat.SummonStat;
import net.sf.l2j.gameserver.model.actor.status.SummonStatus;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.model.itemcontainer.PetInventory;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcInfo;
import net.sf.l2j.gameserver.network.serverpackets.PetDelete;
import net.sf.l2j.gameserver.network.serverpackets.PetInfo;
import net.sf.l2j.gameserver.network.serverpackets.PetItemList;
import net.sf.l2j.gameserver.network.serverpackets.PetStatusShow;
import net.sf.l2j.gameserver.network.serverpackets.PetStatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.RelationChanged;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.util.Rnd;

public abstract class L2Summon extends L2PlayableInstance
{
	// private static final Logger _log = Logger.getLogger(L2Summon.class.getName());
	
	private L2PcInstance _owner;
	
	private int _attackRange = 36; // Melee range
	private boolean _follow = true;
	public boolean _isSiegeGolem = false;
	
	private int _chargedSoulShot;
	private int _chargedSpiritShot;
	
	public class AIAccessor extends L2Character.AIAccessor
	{
		protected AIAccessor()
		{
		}
		
		public L2Summon getSummon()
		{
			return L2Summon.this;
		}
		
		public boolean isAutoFollow()
		{
			return L2Summon.this.getFollowStatus();
		}
		
		public void doPickupItem(L2Object object)
		{
			L2Summon.this.doPickupItem(object);
		}
	}
	
	public L2Summon(int objectId, L2NpcTemplate template, L2PcInstance owner)
	{
		super(objectId, template);
		getKnownList();
		getStat();
		getStatus();
		
		_showSummonAnimation = true;
		_owner = owner;
		_ai = new L2SummonAI(new L2Summon.AIAccessor());
		
		// This position will also be used for summon spawn
		setXYZInvisible(owner.getX() + Rnd.get(-100, 100), owner.getY() + Rnd.get(-100, 100), owner.getZ());
	}
	
	@Override
	public void onSpawn()
	{
		setFollowStatus(true);
		
		updateAndBroadcastStatus(0);
		
		_owner.sendPacket(new RelationChanged(this, _owner.getRelation(_owner), false));
		
		for (L2PcInstance player : _owner.getKnownList().getKnownPlayersInRadius(800))
		{
			player.sendPacket(new RelationChanged(this, _owner.getRelation(player), isAutoAttackable(player)));
		}
		
		super.onSpawn();
	}
	
	@Override
	public final SummonKnownList getKnownList()
	{
		if ((super.getKnownList() == null) || !(super.getKnownList() instanceof SummonKnownList))
		{
			setKnownList(new SummonKnownList(this));
		}
		return (SummonKnownList) super.getKnownList();
	}
	
	@Override
	public SummonStat getStat()
	{
		if ((super.getStat() == null) || !(super.getStat() instanceof SummonStat))
		{
			setStat(new SummonStat(this));
		}
		return (SummonStat) super.getStat();
	}
	
	@Override
	public SummonStatus getStatus()
	{
		if ((super.getStatus() == null) || !(super.getStatus() instanceof SummonStatus))
		{
			setStatus(new SummonStatus(this));
		}
		return (SummonStatus) super.getStatus();
	}
	
	@Override
	public L2CharacterAI getAI()
	{
		if (_ai == null)
		{
			synchronized (this)
			{
				if (_ai == null)
				{
					_ai = new L2SummonAI(new L2Summon.AIAccessor());
				}
			}
		}
		
		return _ai;
	}
	
	@Override
	public L2NpcTemplate getTemplate()
	{
		return (L2NpcTemplate) super.getTemplate();
	}
	
	// this defines the action buttons, 1 for Summon, 2 for Pets
	public abstract int getSummonType();
	
	@Override
	public void updateAbnormalEffect()
	{
		for (L2PcInstance player : getKnownList().getKnownPlayers().values())
		{
			player.sendPacket(new NpcInfo(this, player, 1));
		}
	}
	
	/**
	 * @return Returns the mountable.
	 */
	public boolean isMountable()
	{
		return false;
	}
	
	@Override
	public void onAction(L2PcInstance player)
	{
		if (player.getTarget() != this)
		{
			if (Config.DEBUG)
			{
				_log.fine("new target selected:" + getObjectId());
			}
			
			player.setTarget(this);
			MyTargetSelected my = new MyTargetSelected(getObjectId(), player.getLevel() - getLevel());
			player.sendPacket(my);
			
			player.sendPacket(new ValidateLocation(this));
		}
		else if (player == _owner)
		{
			// Calculate the distance between the L2PcInstance and the L2Character
			if (!canInteract(player))
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
			}
			else
			{
				if (Config.PLAYER_MOVEMENT_BLOCK_TIME > 0 && !player.isGM() && player.getNotMoveUntil() > System.currentTimeMillis())
				{
					player.sendPacket(new ActionFailed());
					return;
				}
				
				// Rotate and interact with character face to face
				player.sendPacket(new MoveToPawn(player, this, L2NpcInstance.INTERACTION_DISTANCE));
				
				player.sendPacket(new PetStatusShow(this));
				
				// This Action Failed packet avoids player getting stuck when clicking three or more times
				player.sendPacket(new ActionFailed());
				
				if (Config.PLAYER_MOVEMENT_BLOCK_TIME > 0 && !player.isGM())
				{
					player.updateNotMoveUntil();
				}
			}
		}
		else
		{
			player.sendPacket(new ValidateLocation(this));
			if (isAutoAttackable(player))
			{
				if (GeoData.getInstance().canSeeTarget(player, this))
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, this);
					player.onActionRequest();
				}
			}
			else
			{
				if (Config.PLAYER_MOVEMENT_BLOCK_TIME > 0 && !player.isGM() && player.getNotMoveUntil() > System.currentTimeMillis())
				{
					player.sendPacket(new ActionFailed());
					return;
				}
				
				// Calculate the distance between the L2PcInstance and the L2Character
				if (canInteract(player))
				{
					// Rotate and interact with character face to face
					player.sendPacket(new MoveToPawn(player, this, L2NpcInstance.INTERACTION_DISTANCE));
					
					if (Config.PLAYER_MOVEMENT_BLOCK_TIME > 0 && !player.isGM())
					{
						player.updateNotMoveUntil();
					}
				}
				
				// This Action Failed packet avoids player getting stuck when clicking three or more times
				player.sendPacket(new ActionFailed());
				
				if (GeoData.getInstance().canSeeTarget(player, this))
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, this);
				}
			}
		}
	}
	
	@Override
	public void onActionShift(L2GameClient client)
	{
		// Get the L2PcInstance corresponding to the thread
		L2PcInstance player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		if (player.getTarget() != this)
		{
			player.setTarget(this);
			MyTargetSelected my = new MyTargetSelected(getObjectId(), player.getLevel() - getLevel());
			player.sendPacket(my);
		}
		
		player.sendPacket(new ValidateLocation(this));
		
		// This Action Failed packet avoids player getting stuck when shift-clicking
		player.sendPacket(new ActionFailed());
	}
	
	public long getExpForThisLevel()
	{
		if (getLevel() >= Experience.LEVEL.length)
		{
			return 0;
		}
		return Experience.LEVEL[getLevel()];
	}
	
	public long getExpForNextLevel()
	{
		if (getLevel() >= (Experience.LEVEL.length - 1))
		{
			return 0;
		}
		return Experience.LEVEL[getLevel() + 1];
	}
	
	public final L2PcInstance getOwner()
	{
		return _owner;
	}
	
	public final int getNpcId()
	{
		return getTemplate().npcId;
	}
	
	@Override
	public void setChargedSoulShot(int shotType)
	{
		_chargedSoulShot = shotType;
	}
	
	@Override
	public void setChargedSpiritShot(int shotType)
	{
		_chargedSpiritShot = shotType;
	}
	
	public final short getSoulShotsPerHit()
	{
		if (getTemplate().ss > 1)
		{
			return getTemplate().ss;
		}
		
		return 1;
	}
	
	public final short getSpiritShotsPerHit()
	{
		if (getTemplate().bss > 1)
		{
			return getTemplate().bss;
		}
		
		return 1;
	}
	
	public void followOwner()
	{
		setFollowStatus(true);
	}
	
	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}
		
		// Check if summon is hated and transfer aggro to owner
		transferHateToOwner();
		
		DecayTaskManager.getInstance().addDecayTask(this);
		return true;
	}
	
	public void stopDecay()
	{
		DecayTaskManager.getInstance().cancelDecayTask(this);
	}
	
	@Override
	public void onDecay()
	{
		deleteMe(_owner);
	}
	
	@Override
	public void broadcastStatusUpdate()
	{
		super.broadcastStatusUpdate();
		updateAndBroadcastStatus(1);
	}
	
	public void updateAndBroadcastStatus(int val)
	{
		if (_owner == null)
		{
			return;
		}
		
		_owner.sendPacket(new PetInfo(this, val));
		_owner.sendPacket(new PetStatusUpdate(this));
		if (isVisible())
		{
			broadcastNpcInfo(val);
		}
		updateEffectIcons(true);
	}
	
	public void broadcastNpcInfo(int val)
	{
		for (L2PcInstance player : getKnownList().getKnownPlayers().values())
		{
			
			if (player == null || player == _owner)
			{
				continue;
			}
			player.sendPacket(new NpcInfo(this, player, val));
			
		}
	}
	
	public void deleteMe(L2PcInstance owner)
	{
		getAI().stopFollow();
		owner.sendPacket(new PetDelete(getSummonType(), getObjectId()));
		
		// Pet will be deleted along with all its items
		if (getInventory() != null)
		{
			getInventory().destroyAllItems("pet deleted", _owner, this);
		}
		decayMe();
		getKnownList().removeAllKnownObjects();
		owner.setPet(null);
		
	}
	
	public void unSummon(L2PcInstance owner)
	{
		if (isVisible())
		{
			// Abort cast
			abortCast();
			
			// stop HP and MP regeneration
			stopHpMpRegeneration();
			
			getAI().stopFollow();
			owner.sendPacket(new PetDelete(getSummonType(), getObjectId()));
			
			store();
			
			giveAllToOwner();
			
			owner.setPet(null);
			
			stopAllEffects();
			
			// Check if summon is hated and transfer aggro to owner
			transferHateToOwner();
			
			L2WorldRegion oldRegion = getWorldRegion();
			
			decayMe();
			if (oldRegion != null)
			{
				oldRegion.removeFromZones(this);
			}
			
			getKnownList().removeAllKnownObjects();
			
			setTarget(null);
			for (int itemId : owner.getAutoSoulShot())
			{
				if ((itemId == 6645) || (itemId == 6646) || (itemId == 6647))
				{
					owner.disableAutoShot(itemId);
				}
			}
		}
	}
	
	private void transferHateToOwner()
	{
		if (_owner != null)
		{
			for (L2Character mob : getKnownList().getKnownCharacters())
			{
				// Get the mobs which have aggro on this instance
				if (mob instanceof L2Attackable)
				{
					if (((L2Attackable) mob).isDead())
					{
						continue;
					}
					
					AggroInfo info = ((L2Attackable) mob).getAggroList().get(this);
					if (info != null)
					{
						((L2Attackable) mob).addDamageHate(_owner, info.damage, info.hate);
					}
				}
			}
		}
	}
	
	public int getAttackRange()
	{
		return _attackRange;
	}
	
	public void setAttackRange(int range)
	{
		if (range < 36)
		{
			range = 36;
		}
		_attackRange = range;
	}
	
	public void setFollowStatus(boolean state)
	{
		_follow = state;
		if (_follow)
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, _owner);
		}
		else
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null);
		}
	}
	
	public boolean getFollowStatus()
	{
		return _follow;
	}
	
	public boolean isSiegeGolem()
	{
		return _isSiegeGolem;
	}
	
	public boolean isHungry()
	{
		return false;
	}
	
	public int getWeapon()
	{
		return 0;
	}
	
	public int getArmor()
	{
		return 0;
	}
	
	@Override
	public boolean isAutoAttackable(L2Character attacker)
	{
		return _owner.isAutoAttackable(attacker);
	}
	
	public int getChargedSoulShot()
	{
		return _chargedSoulShot;
	}
	
	public int getChargedSpiritShot()
	{
		return _chargedSpiritShot;
	}
	
	@Override
	public boolean isInCombat()
	{
		return _owner.isInCombat();
	}
	
	public int getControlItemObjectId()
	{
		return 0;
	}
	
	public int getCurrentFed()
	{
		return 0;
	}
	
	public int getMaxFed()
	{
		return 0;
	}
	
	public L2Weapon getActiveWeapon()
	{
		return null;
	}
	
	@Override
    public PetInventory getInventory()
    {
        return null;
    }
	
	protected void doPickupItem(L2Object object)
	{
		
	}
	
	public void giveAllToOwner()
	{
		
	}
	
	public void store()
	{
		
	}
	
	@Override
	public L2ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	@Override
	public L2Weapon getActiveWeaponItem()
	{
		return null;
	}
	
	@Override
	public L2ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	@Override
	public L2Weapon getSecondaryWeaponItem()
	{
		return null;
	}
	
	/**
	 * Return the L2Party object of its L2PcInstance owner or null.<BR>
	 * <BR>
	 */
	@Override
	public L2Party getParty()
	{
		if (_owner == null)
		{
			return null;
		}
		return _owner.getParty();
	}
	
	@Override
	public boolean isInvul()
	{
		return _isInvul || _isTeleporting || _owner.isSpawnProtected();
	}
	
	/**
	 * Return True if the L2Character has a Party in progress.<BR>
	 * <BR>
	 */
	@Override
	public boolean isInParty()
	{
		return getParty() != null;
	}
	
	/**
	 * Check if the active L2Skill can be casted.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Check if the target is correct</li>
	 * <li>Check if the target is in the skill cast range</li>
	 * <li>Check if the summon owns enough HP and MP to cast the skill</li>
	 * <li>Check if all skills are enabled and this skill is enabled</li><BR>
	 * <BR>
	 * <li>Check if the skill is active</li><BR>
	 * <BR>
	 * <li>Notify the AI with AI_INTENTION_CAST and target</li><BR>
	 * <BR>
	 * @param skill The L2Skill to use
	 * @param forceUse used to force ATTACK on players
	 * @param dontMove used to prevent movement, if not in range
	 */
	public void useMagic(L2Skill skill, boolean forceUse, boolean dontMove)
	{
		if (skill == null || isDead())
		{
			return;
		}
		
		// Check if the skill is active
		if (skill.isPassive())
		{
			// just ignore the passive skill request. why does the client send it anyway ??
			return;
		}
		
		// ************************************* Check Casting in Progress *******************************************
		
		// If a skill is currently being used
		if (isCastingNow())
		{
			return;
		}
		
		// ************************************* Check Target *******************************************
		
		// Get the target for the skill
		L2Object target = null;
		
		switch (skill.getTargetType())
		{
			// OWNER_PET should be cast even if no target has been found
			case TARGET_OWNER_PET:
				target = _owner;
				break;
			// PARTY, AURA, SELF should be cast even if no target has been found
			case TARGET_PARTY:
			case TARGET_AURA:
			case TARGET_SELF:
				target = this;
				break;
			default:
				// Get the first target of the list
				target = skill.getFirstOfTargetList(this);
				break;
		}
		
		// Check the validity of the target
		if (target == null)
		{
			if (_owner != null)
			{
				_owner.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
			}
			return;
		}

		// ************************************* Check skill availability *******************************************
		
		// Check if this skill is enabled (e.g. reuse time)
		if (isSkillDisabled(skill.getId()))
		{
			if (_owner != null)
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_PREPARED_FOR_REUSE);
				sm.addSkillName(skill.getId(), skill.getLevel());
				_owner.sendPacket(sm);
			}
			return;
		}
		
		// Check if all skills are disabled
		if (isAllSkillsDisabled())
		{
			return;
		}
		
		// ************************************* Check Consumables *******************************************
		
		// Check if the summon has enough MP
		if (getCurrentMp() < (getStat().getMpConsume(skill) + getStat().getMpInitialConsume(skill)))
		{
			// Send a System Message to the caster
			if (_owner != null)
			{
				_owner.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_MP));
			}
			return;
		}
		
		// Check if the summon has enough HP
		if (getCurrentHp() <= skill.getHpConsume())
		{
			// Send a System Message to the caster
			if (_owner != null)
			{
				_owner.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_HP));
			}
			return;
		}
		
		// ************************************* Check Summon State *******************************************
		
		// Check if this is offensive magic skill
		if (skill.isOffensive())
		{
			if (isInsidePeaceZone(this, target) && _owner != null && _owner.getAccessLevel() < Config.GM_PEACE_ATTACK)
			{
				// If summon or target is in a peace zone, send a system message TARGET_IN_PEACEZONE
				_owner.sendPacket(new SystemMessage(SystemMessage.TARGET_IN_PEACEZONE));
				return;
			}
			
			if (_owner != null && _owner.isInOlympiadMode() && !_owner.isOlympiadStart())
			{
				// if L2PcInstance is in Olympiad and the match isn't already start, send a Server->Client packet ActionFailed
				sendPacket(new ActionFailed());
				return;
			}
			
			// Check if the target is attackable
			if (target instanceof L2DoorInstance)
			{
				if (!((L2DoorInstance) target).isAttackable(_owner))
				{
					return;
				}
			}
			else
			{
				if (!target.isAttackable() && _owner != null && _owner.getAccessLevel() < Config.GM_PEACE_ATTACK)
				{
					return;
				}
				
				// Check if a Forced ATTACK is in progress on non-attackable target
				if (!target.isAutoAttackable(this) && !forceUse && (skill.getTargetType() != SkillTargetType.TARGET_AURA) && (skill.getTargetType() != SkillTargetType.TARGET_CLAN) && (skill.getTargetType() != SkillTargetType.TARGET_ALLY) && (skill.getTargetType() != SkillTargetType.TARGET_PARTY) && (skill.getTargetType() != SkillTargetType.TARGET_SELF))
				{
					return;
				}
			}
		}
		
		// GeoData Los Check here
		if ((skill.getCastRange() > 0) && !GeoData.getInstance().canSeeTarget(this, target))
		{
			_owner.sendPacket(new SystemMessage(SystemMessage.CANT_SEE_TARGET));
			sendPacket(new ActionFailed());
			return;
		}
		
		// If all conditions are checked, create a new SkillDat object and set the owner _currentPetSkill
		_owner.setCurrentPetSkill(skill, forceUse, dontMove);
		
		// Notify the AI with AI_INTENTION_CAST and target
		getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, skill, target);
	}

	public void setOwner(L2PcInstance newOwner)
	{
		_owner = newOwner;
	}
	
	/**
	 * Servitor skills automatically change their level based on the servitor's level. Until level 70, 
	 * the servitor gets 1 lvl of skill per 10 levels. After that, it is 1 skill level per 5 servitor levels. 
	 * If the resulting skill level doesn't exist use the max that does exist!
	 * @see net.sf.l2j.gameserver.model.L2Character#doCast(net.sf.l2j.gameserver.model.L2Skill)
	 */
	@Override
	public void doCast(L2Skill skill)
	{
		L2Object target = getTarget();
		if (!_owner.checkPvpSkill(target, target, skill, true) && _owner.getAccessLevel() < Config.GM_PEACE_ATTACK)
		{
			_owner.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
			// Send a Server->Client packet ActionFailed to the L2PcInstance
			_owner.sendPacket(new ActionFailed());
			return;
		}
		
		int petLevel = getLevel();
		int skillLevel = petLevel / 10;
		if (petLevel >= 70)
		{
			skillLevel += (petLevel - 65) / 10;
		}
		
		// adjust the level for servitors less than lv 10
		if (skillLevel < 1)
		{
			skillLevel = 1;
		}
		
		L2Skill skillToCast = SkillTable.getInstance().getInfo(skill.getId(), skillLevel);
		if (skillToCast != null)
		{
			super.doCast(skillToCast);
		}
		else
		{
			super.doCast(skill);
		}
	}
	
	@Override
	public void rechargeShots(boolean physical, boolean magic)
	{
		L2ItemInstance item;
		IItemHandler handler;
		
		if (_owner.getAutoSoulShot() == null || _owner.getAutoSoulShot().isEmpty())
		{
			return;
		}
		
		for (int itemId : _owner.getAutoSoulShot())
		{
			item = _owner.getInventory().getItemByItemId(itemId);
			if (item != null)
			{
				if (magic)
				{
					if ((itemId == 6646) || (itemId == 6647))
					{
						handler = ItemHandler.getInstance().getItemHandler(itemId);
						if (handler != null)
						{
							handler.useItem(_owner, item);
						}
					}
				}
				
				if (physical)
				{
					if (itemId == 6645)
					{
						handler = ItemHandler.getInstance().getItemHandler(itemId);
						if (handler != null)
						{
							handler.useItem(_owner, item);
						}
					}
				}
			}
			else
			{
				_owner.removeAutoSoulShot(itemId);
			}
		}
	}
	
	@Override
	public L2PcInstance getActingPlayer()
	{
		return _owner;
	}
	
	@Override
	public void sendInfo(L2PcInstance activeChar)
	{
		// Check if the L2PcInstance is the owner of the Pet
		if (activeChar.equals(getOwner()))
		{
			activeChar.sendPacket(new PetInfo(this, 0));
			updateEffectIcons(true);
			if (this instanceof L2PetInstance)
			{
				activeChar.sendPacket(new PetItemList((L2PetInstance) this));
			}
		}
		else
		{
			activeChar.sendPacket(new NpcInfo(this, activeChar, 0));
		}
	}
}