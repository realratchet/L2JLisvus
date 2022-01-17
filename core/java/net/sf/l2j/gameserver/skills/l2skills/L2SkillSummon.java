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
package net.sf.l2j.gameserver.skills.l2skills;

import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2CubicInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeSummonInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;

public class L2SkillSummon extends L2Skill
{
	public static final int SKILL_CUBIC_MASTERY = 143;
	
	private final int _npcId;
	private final float _expPenalty;
	private final boolean _isCubic;
	private final boolean _saveCubicOnExit;
	
	// Activation time for a cubic
	private final int _activationTime;
	// Activation chance for a cubic.
	private final int _activationChance;
	
	// What is the total lifetime of summons (in millis)
	private final int _summonTotalLifeTime;
	// How much lifetime is lost per second of idleness (non-fighting)
	private final int _summonTimeLostIdle;
	// How much time is lost per second of activity (fighting)
	private final int _summonTimeLostActive;
	
	// Item consume count over time
	private final int _itemConsumeOT;
	// Item consume id over time
	private final int _itemConsumeIdOT;
	// Item consume time in milliseconds
	private final int _itemConsumeTime;
	// How many times to consume an item
	private final int _itemConsumeSteps;
	
	public L2SkillSummon(StatsSet set)
	{
		super(set);
		
		_npcId = set.getInteger("npcId", 0); // Default for non-described skills
		_expPenalty = set.getFloat("expPenalty", 0.f);
		_isCubic = set.getBool("isCubic", false);
		_saveCubicOnExit = set.getBool("saveCubicOnExit", false);
		
		_activationTime = set.getInteger("activationTime", 8);
		_activationChance = set.getInteger("activationChance", 30);
		
		_summonTotalLifeTime = set.getInteger("summonTotalLifeTime", 1200000); // 20 minutes default
		_summonTimeLostIdle = set.getInteger("summonTimeLostIdle", 0);
		_summonTimeLostActive = set.getInteger("summonTimeLostActive", 0);
		
		_itemConsumeOT = set.getInteger("itemConsumeCountOT", 0);
		_itemConsumeIdOT = set.getInteger("itemConsumeIdOT", 0);
		_itemConsumeTime = set.getInteger("itemConsumeTime", 0);
		_itemConsumeSteps = set.getInteger("itemConsumeSteps", 0);
	}
	
	@Override
	public boolean checkCondition(L2Character activeChar, L2Object target, boolean itemOrWeapon)
	{
		if (activeChar instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) activeChar;
			if (player.inObserverMode())
			{
				return false;
			}
			
			if (!isCubic())
			{
				if (player.getPet() != null || player.isMounted())
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_HAVE_A_PET));
					return false;
				}
			}
		}
		return super.checkCondition(activeChar, target, itemOrWeapon);
	}
	
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead() || !(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance player = (L2PcInstance) activeChar;
		if (_npcId == 0)
		{
			player.sendMessage("Summon skill " + getId() + " not described yet.");
			return;
		}
		
		if (_isCubic)
		{
			for (L2Object obj : targets)
			{
				if (!(obj instanceof L2PcInstance))
				{
					continue;
				}
				
				L2PcInstance target = (L2PcInstance)obj;
				
				int slots = target.getSkillLevel(SKILL_CUBIC_MASTERY);
				slots = slots > 0 ? (slots + 1) : 1;
				
				L2CubicInstance oldCubic = null;
				for (L2CubicInstance cubic : target.getCubics())
				{
					// Get first cubic or a cubic that has the same id with the new cubic
					if (oldCubic == null || cubic.getId() == _npcId)
					{
						oldCubic = cubic;
						
						if (cubic.getId() == _npcId)
						{
							break;
						}
					}
				}
				
				if (oldCubic != null)
				{
					// Remove a cubic if there is no room for more or in case of replacing one with a newer version of it
	                if (target.getCubics().size() >= slots || oldCubic.getId() == _npcId)
	                {
	                    oldCubic.stopAction();
	                    oldCubic.cancelDisappear();
	                    target.getCubics().remove(oldCubic);
	                }
				}
				
				// Make sure to check if cubic is given by other player
				target.addCubic(this, 0, player != target);
				target.broadcastUserInfo();
			}
			// End of cubics handling
			return;
		}
		
		// Decay targets in the case of corpse mob skills
		if (targets.length > 0 && (getTargetType() == SkillTargetType.TARGET_CORPSE_MOB || getTargetType() == SkillTargetType.TARGET_AREA_CORPSE_MOB))
		{
			L2Object trg = targets[0];
			if (trg instanceof L2Character)
			{
				((L2Character)trg).endDecayTask();
			}
		}
		
		L2NpcTemplate summonTemplate = NpcTable.getInstance().getTemplate(_npcId);
		
		L2SummonInstance summon;
		if (summonTemplate.type.equalsIgnoreCase("L2SiegeSummon"))
		{
			summon = new L2SiegeSummonInstance(IdFactory.getInstance().getNextId(), summonTemplate, player, this);
		}
		else
		{
			summon = new L2SummonInstance(IdFactory.getInstance().getNextId(), summonTemplate, player, this);
		}
		
		summon.setName(summonTemplate.name);
		summon.setTitle(player.getName());
		summon.setExpPenalty(_expPenalty);
		if (summon.getLevel() >= Experience.LEVEL.length)
		{
			summon.getStat().setExp(Experience.LEVEL[Experience.LEVEL.length - 1]);
			_log.warning("Summon (" + summon.getName() + ") NpcID: " + summon.getNpcId() + " has a level above 78. Please rectify.");
		}
		else
		{
			summon.getStat().setExp(Experience.LEVEL[(summon.getLevel() % Experience.LEVEL.length)]);
		}
		
		summon.setCurrentHp(summon.getMaxHp());
		summon.setCurrentMp(summon.getMaxMp());
		summon.setHeading(player.getHeading());
		summon.setRunning();
		player.setPet(summon);
		
		L2World.getInstance().storeObject(summon);
		// Use the position that was already set in L2Summon constructor
		summon.spawnMe(summon.getX(), summon.getY(), summon.getZ());
	}
	
	/**
	 * @return Returns true if skill is cubic skill.
	 */
	public final boolean isCubic()
	{
		return _isCubic;
	}
	
	/**
	 * @return Returns true if cubic should be saved on exit.
	 */
	public final boolean isSaveCubicOnExit()
	{
		return _saveCubicOnExit;
	}
	
	/**
	 * @return Returns the summon NPC ID.
	 */
	public final int getNpcId()
	{
		return _npcId;
	}
	
	/**
	 * @return Returns the cubic activation time.
	 */
	public final int getActivationTime()
	{
		return _activationTime;
	}
	
	/**
	 * @return Returns the cubic activation chance.
	 */
	public final int getActivationChance()
	{
		return _activationChance;
	}
	
	/**
	 * @return Returns the itemConsume count over time.
	 */
	public final int getItemConsumeOT()
	{
		return _itemConsumeOT;
	}

	/**
	 * @return Returns the itemConsumeId over time.
	 */
	public final int getItemConsumeIdOT()
	{
		return _itemConsumeIdOT;
	}

	/**
	 * @return Returns the itemConsume time in milliseconds.
	 */
	public final int getItemConsumeTime()
	{
		return _itemConsumeTime;
	}

	/**
	 * @return Returns the itemConsume count over time.
	 */
	public final int getItemConsumeSteps()
	{
		return _itemConsumeSteps;
	}

	/**
	 * @return Returns the itemConsume count over time.
	 */
	public final int getTotalLifeTime()
	{
		return _summonTotalLifeTime;
	}

	/**
	 * @return Returns the itemConsume count over time.
	 */
	public final int getTimeLostIdle()
	{
		return _summonTimeLostIdle;
	}

	/**
	 * @return Returns the itemConsumeId over time.
	 */
	public final int getTimeLostActive()
	{
		return _summonTimeLostActive;
	}
}