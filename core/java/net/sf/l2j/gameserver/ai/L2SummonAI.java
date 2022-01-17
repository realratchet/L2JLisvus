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

import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_CAST;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_INTERACT;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_PICK_UP;

import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Character.AIAccessor;
import net.sf.l2j.gameserver.model.L2Summon;

public class L2SummonAI extends L2CharacterAI
{
	private boolean _thinking; // to prevent recursive thinking
	private boolean _previousFollowStatus = ((L2Summon) _actor).getFollowStatus();
	
	public L2SummonAI(AIAccessor accessor)
	{
		super(accessor);
	}
	
	@Override
	protected void onIntentionIdle()
	{
		stopFollow();
		_previousFollowStatus = false;
		onIntentionActive();
	}
	
	@Override
	protected void onIntentionActive()
	{
		L2Summon summon = (L2Summon) _actor;
		// Don't let summon follow if it's immobilized
		if (_previousFollowStatus && !summon.isMovementDisabled())
		{
			((L2Summon) _actor).setFollowStatus(_previousFollowStatus);
		}
		else
		{
			super.onIntentionActive();
		}
	}
	
	private void thinkAttack()
	{
		L2Character target = getAttackTarget();
		if (target == null)
		{
			return;
		}
		
		if (checkTargetLostOrDead(target))
		{
			setAttackTarget(null);
			return;
		}
		
		if (maybeMoveToPawn(target, _actor.getPhysicalAttackRange()))
		{
			return;
		}
		
		clientStopMoving(null);
		_accessor.doAttack(target);
	}
	
	private void thinkCast()
	{
		L2Summon summon = (L2Summon) _actor;
		if (checkTargetLost(getCastTarget()))
		{
			setCastTarget(null);
			return;
		}
		
		boolean val = _previousFollowStatus;
		
		if (maybeMoveToPawn(getCastTarget(), _actor.getMagicalAttackRange(_skill)))
		{
			return;
		}
		
		clientStopMoving(null);
		summon.setFollowStatus(false);
		setIntention(AI_INTENTION_IDLE);
		_previousFollowStatus = val;
		_accessor.doCast(_skill);
	}
	
	private void thinkPickUp()
	{
		if (_actor.isAllSkillsDisabled())
		{
			return;
		}
		if (checkTargetLost(getTarget()))
		{
			return;
		}
		if (maybeMoveToPawn(getTarget(), 36))
		{
			return;
		}
		setIntention(AI_INTENTION_IDLE);
		((L2Summon.AIAccessor) _accessor).doPickupItem(getTarget());
	}
	
	private void thinkInteract()
	{
		if (_actor.isAllSkillsDisabled())
		{
			return;
		}
		if (checkTargetLost(getTarget()))
		{
			return;
		}
		if (maybeMoveToPawn(getTarget(), 36))
		{
			return;
		}
		setIntention(AI_INTENTION_IDLE);
	}
	
	@Override
	protected void onEvtThink()
	{
		if (_thinking || _actor.isAllSkillsDisabled())
		{
			return;
		}
		
		_thinking = true;
		
		try
		{
			if (getIntention() == AI_INTENTION_ATTACK)
			{
				thinkAttack();
			}
			else if (getIntention() == AI_INTENTION_CAST)
			{
				thinkCast();
			}
			else if (getIntention() == AI_INTENTION_PICK_UP)
			{
				thinkPickUp();
			}
			else if (getIntention() == AI_INTENTION_INTERACT)
			{
				thinkInteract();
			}
		}
		finally
		{
			_thinking = false;
		}
	}
	
	@Override
	protected void onEvtFinishCasting()
	{
		// Change previous follow status now to regain it inside call
		_previousFollowStatus = !_previousFollowStatus;
		notifyFollowStatusChange();
	}
	
	@Override
	protected void onEvtFinishImmobilized()
	{
		// Change previous follow status now to regain it inside call
		_previousFollowStatus = !_previousFollowStatus;
		notifyFollowStatusChange();
	}
	
	public void notifyFollowStatusChange()
	{
		_previousFollowStatus = !_previousFollowStatus;
		switch (getIntention())
		{
			case AI_INTENTION_ACTIVE:
			case AI_INTENTION_FOLLOW:
			case AI_INTENTION_IDLE:
				((L2Summon) _actor).setFollowStatus(_previousFollowStatus);
				break;
		}
	}
	
	public void setStartFollowController(boolean val)
	{
		_previousFollowStatus = val;
	}
}