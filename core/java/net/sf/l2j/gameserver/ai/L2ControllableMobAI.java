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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Character.AIAccessor;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.MobGroup;
import net.sf.l2j.gameserver.model.MobGroupTable;
import net.sf.l2j.gameserver.model.actor.instance.L2ControllableMobInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author littlecrow
 * AI for controllable mobs
 *
 */
public class L2ControllableMobAI extends L2AttackableAI 
{
	private static final Logger _log = Logger.getLogger(L2ControllableMobAI.class.getName());
	
    public static final int AI_IDLE = 1;
    public static final int AI_NORMAL = 2;
    public static final int AI_FORCEATTACK = 3;
    public static final int AI_FOLLOW = 4;
    public static final int AI_CAST = 5;
    public static final int AI_ATTACK_GROUP = 6;

    private int _alternateAI;

    private boolean _thinking; // to prevent thinking recursively
    private boolean _isNotMoving;

    private L2Character _forcedTarget;
    private MobGroup _targetGroup;

    protected void thinkFollow() 
    {
        L2Attackable me = (L2Attackable)_actor;

        if (!Util.checkIfInRange(MobGroupTable.FOLLOW_RANGE, me, getForcedTarget(), true)) 
        {
            int signX = (Rnd.nextInt(2) == 0) ? -1 : 1;
            int signY = (Rnd.nextInt(2) == 0) ? -1 : 1;
            int randX = Rnd.nextInt(MobGroupTable.FOLLOW_RANGE);
            int randY = Rnd.nextInt(MobGroupTable.FOLLOW_RANGE);

            moveTo(getForcedTarget().getX() + signX * randX, getForcedTarget().getY() + signY * randY, getForcedTarget().getZ());
	}
    }

    @Override
	protected void onEvtThink() 
    {
        if (_thinking || _actor.isAllSkillsDisabled())
            return;

        _thinking = true;

        try
        {
            switch (getAlternateAI())
            {
                case AI_IDLE: 
                    if (getIntention() != CtrlIntention.AI_INTENTION_ACTIVE)
                        setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                    break;
                case AI_FOLLOW:
                    thinkFollow();
                    break;
                case AI_CAST:
                    thinkCast();
                    break;
                case AI_FORCEATTACK:
                    thinkForceAttack();
                    break;
                case AI_ATTACK_GROUP: 
                    thinkAttackGroup();
                    break;
                default:
                    if (getIntention() == AI_INTENTION_ACTIVE)
                        thinkActive();
                    else if (getIntention() == AI_INTENTION_ATTACK)
                        thinkAttack();
                    break;
            }
        } 
        finally
        {
        	_thinking = false;
        }
    }

    protected void thinkCast() 
    {
        L2Attackable npc = (L2Attackable)_actor;

        if (getAttackTarget() == null || getAttackTarget().isAlikeDead()) 
        {
            setAttackTarget(findNextRndTarget());
            clientStopMoving(null);
        }
	
        if (getAttackTarget() == null)
            return;

        npc.setTarget(getAttackTarget());

        if (!_actor.isMuted()) 
        {
            int max_range = 0;
            // check distant skills

            L2Skill[] skills = _actor.getAllSkills();
            for (L2Skill sk : skills) 
            {
                if (Util.checkIfInRange(sk.getCastRange(), _actor, getAttackTarget(), true)
                        && !_actor.isSkillDisabled(sk.getId())
                        && _actor.getCurrentMp() > _actor.getStat().getMpConsume(sk)) 
                {
                    _accessor.doCast(sk);
                    return;
                }

                max_range = Math.max(max_range, sk.getCastRange());
            }

            if (!isNotMoving())
                moveToPawn(getAttackTarget(), max_range);

            return;
        }
    }

    protected void thinkAttackGroup() 
    {
        L2Character target = getForcedTarget();
        if (target == null || target.isAlikeDead())
        {
            // try to get next group target
            setForcedTarget(findNextGroupTarget());
            clientStopMoving(null);
        }

        if (target == null)
            return;

        double dist2 = 0;
        int range = 0;
        int max_range = 0;

        _actor.setTarget(target);
        // as a response, we put the target in a forced attack mode
        L2ControllableMobInstance theTarget = (L2ControllableMobInstance)target;
        L2ControllableMobAI ctrlAi = (L2ControllableMobAI)theTarget.getAI();
        ctrlAi.forceAttack(_actor);

        try
        {
            dist2 = _actor.getPlanDistanceSq(target.getX(), target.getY());
            range = _actor.getPhysicalAttackRange() + (int)(_actor.getTemplate().collisionRadius + target.getTemplate().collisionRadius);
            max_range = range;
        } 
        catch (NullPointerException e)
        {
            _log.warning("Encountered Null Value.");
            e.printStackTrace();
        }

        if (!_actor.isMuted() && dist2 > (range + 20) * (range + 20)) 
        {
            // Check distant skills
        	L2Skill[] skills = _actor.getAllSkills();
            for (L2Skill sk : skills) 
            {
                int castRange = sk.getCastRange();

                if (castRange * castRange >= dist2
                        && !_actor.isSkillDisabled(sk.getId())
                        && _actor.getCurrentMp() > _actor.getStat().getMpConsume(sk)) 
                {
                    _accessor.doCast(sk);
                    return;
                }

                max_range = Math.max(max_range, castRange);
            }

            if (!isNotMoving())
                moveToPawn(target, range);

            return;
        }
        _accessor.doAttack(target);
    }

    protected void thinkForceAttack() 
    {
        if (getForcedTarget() == null || getForcedTarget().isAlikeDead()) 
        {
            clientStopMoving(null);
            setIntention(AI_INTENTION_ACTIVE);
            setAlternateAI(AI_IDLE);
        }

        double dist2 = 0;
        int range = 0;
        int max_range = 0;

        try
        {
	        _actor.setTarget(getForcedTarget());
	        dist2 = _actor.getPlanDistanceSq(getForcedTarget().getX(), getForcedTarget().getY());
	        range = _actor.getPhysicalAttackRange() + (int)(_actor.getTemplate().collisionRadius + getForcedTarget().getTemplate().collisionRadius);
	        max_range = range;
        }
        catch (NullPointerException e)
        {
            _log.warning("Encountered Null Value.");
            e.printStackTrace();
        }

        if (!_actor.isMuted() && dist2 > (range + 20) * (range + 20))
        {
            // check distant skills
        	L2Skill[] skills = _actor.getAllSkills();
            for (L2Skill sk : skills) 
            {
                int castRange = sk.getCastRange();

                if (castRange * castRange >= dist2
                         && !_actor.isSkillDisabled(sk.getId())
                         && _actor.getCurrentMp() > _actor.getStat().getMpConsume(sk)) 
                {
                    _accessor.doCast(sk);
                    return;
                }

                max_range = Math.max(max_range, castRange);
            }

            if (!isNotMoving())
                moveToPawn(getForcedTarget(), _actor.getPhysicalAttackRange());

            return;
        }

        _accessor.doAttack(getForcedTarget());
    }

    protected void thinkAttack() 
    {
        L2Character attackTarget = getAttackTarget();

        if (attackTarget == null || attackTarget.isAlikeDead()) 
        {
            if (attackTarget != null) 
            {
                // stop hating
                L2Attackable npc = (L2Attackable) _actor;
                npc.stopHating(attackTarget);
            }

            setIntention(AI_INTENTION_ACTIVE);
        }
        else
        {
            // notify aggression
            if (((L2NpcInstance) _actor).getFactionId() != null && !(attackTarget instanceof L2Attackable)) 
            {
                String factionId = ((L2NpcInstance) _actor).getFactionId();

                for (L2Object obj : _actor.getKnownList().getKnownObjects().values()) 
                {
                    if (!(obj instanceof L2NpcInstance))
                        continue;

                    L2NpcInstance npc = (L2NpcInstance) obj;
                    
                    if (!factionId.equals(npc.getFactionId()))
                        continue;

                    if (_actor.isInsideRadius(npc, (npc.getFactionRange() + npc.getAggroRange()), false, true) 
                            && Math.abs(attackTarget.getZ() - npc.getZ()) < 600
                            && _actor.hasAttackerInAttackByList(attackTarget)
                            && npc.getAI() != null
                            && npc.getAI()._intention != CtrlIntention.AI_INTENTION_ATTACK)
                        npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attackTarget, 1);
                }
            }

            double dist2 = 0;
            int range = 0;
            int max_range = 0;

            try
            {
	            _actor.setTarget(attackTarget);
	            dist2 = _actor.getPlanDistanceSq(attackTarget.getX(), attackTarget.getY());
	            range = _actor.getPhysicalAttackRange() + (int)(_actor.getTemplate().collisionRadius + attackTarget.getTemplate().collisionRadius);
	            max_range = range;
            }
            catch (NullPointerException e)
            {
                _log.warning("Encountered Null Value.");
                e.printStackTrace();
            }

            L2Skill[] skills = _actor.getAllSkills();
            if (!_actor.isMuted() && dist2 > (range + 20) * (range + 20)) 
            {
                // check distant skills
                for (L2Skill sk : skills) 
                {
                    int castRange = sk.getCastRange();

                    if (castRange * castRange >= dist2
                            && !_actor.isSkillDisabled(sk.getId())
                            && _actor.getCurrentMp() > _actor.getStat().getMpConsume(sk)) 
                    {
                        _accessor.doCast(sk);
                        return;
                    }

                    max_range = Math.max(max_range, castRange);
                }

                moveToPawn(attackTarget, range);
                return;
            }

            // Force mobs to attack anybody if confused.
            L2Character hated;

            if (_actor.isConfused())
                hated = findNextRndTarget();
            else
                hated = attackTarget;

            if (hated == null) 
            {
                setIntention(AI_INTENTION_ACTIVE);
                return;
            }

            if (hated != attackTarget)
            {
                setAttackTarget(hated);
                attackTarget = hated;
            }

            if (!_actor.isMuted() && skills.length > 0 && Rnd.nextInt(5) == 3) 
            {
                for (L2Skill sk : skills) 
                {
                    int castRange = sk.getCastRange();

                    if (castRange * castRange >= dist2
                            && !_actor.isSkillDisabled(sk.getId())
                            && _actor.getCurrentMp() < _actor.getStat().getMpConsume(sk)) 
                    {
                        _accessor.doCast(sk);
                        return;
                    }
                }
            }

            _accessor.doAttack(attackTarget);
        }
    }

    private void thinkActive() 
    {
        setAttackTarget(findNextRndTarget());
        L2Character hated;

        if (_actor.isConfused())
            hated = findNextRndTarget();
        else
            hated = getAttackTarget();

        if (hated != null) 
        {
            _actor.setRunning();
            setIntention(CtrlIntention.AI_INTENTION_ATTACK, hated);
        }
    }

    private boolean autoAttackCondition(L2Character target) 
    {
        if (target == null || !(_actor instanceof L2Attackable))
            return false;

        L2Attackable me = (L2Attackable)_actor;

        if (target instanceof L2FolkInstance || target instanceof L2DoorInstance)
            return false;

        if (target.isAlikeDead()
                 || !me.isInsideRadius(target, me.getAggroRange(), false, false) 
                 || Math.abs(_actor.getZ() - target.getZ()) > 100)
            return false;

        // Check if the target is an invulnerable GM
        if (target.isGM() && target.isInvul())
            return false;

        // Check if the target is a L2PlayableInstance
        if (target instanceof L2PlayableInstance)
        {
            // Check if the target isn't in silent move mode
            if (((L2PlayableInstance)target).isSilentMoving())
                return false;
        }

        if (target instanceof L2NpcInstance)
        	return false;

        return me.isAggressive();
    }

    private L2Character findNextRndTarget() 
    {
        if (getAttackTarget() == null)
            return null;

        int aggroRange  = ((L2Attackable)_actor).getAggroRange();
        L2Attackable npc = (L2Attackable)_actor;
        int npcX, npcY, targetX, targetY;
        double dy, dx;
        double dblAggroRange = aggroRange*aggroRange;

        List<L2Character> potentialTarget = new ArrayList<>();

        for (L2Object obj : npc.getKnownList().getKnownObjects().values()) 
        {
            if (!(obj instanceof L2Character))
                continue;

            npcX    = npc.getX();
            npcY    = npc.getY();
            targetX = obj.getX();
            targetY = obj.getY();

            dx      = npcX - targetX;
            dy      = npcY - targetY;

            if (dx*dx + dy*dy > dblAggroRange)
                continue;

            L2Character target = (L2Character) obj;

            if (autoAttackCondition(target)) // check aggression
                potentialTarget.add(target);
        }

        if (potentialTarget.isEmpty()) // nothing to do
            return null;

        // we choose a random target
        int choice = Rnd.nextInt(potentialTarget.size());
        L2Character target = potentialTarget.get(choice);

        return target;
    }

    private L2ControllableMobInstance findNextGroupTarget() 
    {
        return getGroupTarget().getRandomMob();
    }

    public L2ControllableMobAI(AIAccessor accessor) 
    {
        super(accessor);
        setAlternateAI(AI_IDLE);
    }

    public int getAlternateAI() 
    {
        return _alternateAI;
    }

    public void setAlternateAI(int _alternateai) 
    {
        _alternateAI = _alternateai;
    }

    public void forceAttack(L2Character target) 
    {
        setAlternateAI(AI_FORCEATTACK);
        setForcedTarget(target);
    }

    public void forceAttackGroup(MobGroup group) 
    {
        setForcedTarget(null);
        setGroupTarget(group);
        setAlternateAI(AI_ATTACK_GROUP);
    }

    public void stop() 
    {
        setAlternateAI(AI_IDLE);
        clientStopMoving(null);
    }

    public void move(int x, int y, int z) 
    {
        moveTo(x, y, z);
    }

    public void follow(L2Character target) 
    {
        setAlternateAI(AI_FOLLOW);
        setForcedTarget(target);
    }

    public boolean isNotMoving() 
    {
        return _isNotMoving;
    }

    public void setNotMoving(boolean isNotMoving) 
    {
        _isNotMoving = isNotMoving;
    }

    private L2Character getForcedTarget()
    {
        return _forcedTarget;
    }

    private MobGroup getGroupTarget()
    {
        return _targetGroup;
    }

    private void setForcedTarget(L2Character forcedTarget)
    {
        _forcedTarget = forcedTarget;
    }

    private void setGroupTarget(MobGroup targetGroup)
    {
        _targetGroup = targetGroup;
    }
}