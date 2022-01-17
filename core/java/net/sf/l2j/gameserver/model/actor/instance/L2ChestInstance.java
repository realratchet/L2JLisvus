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

import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;

/**
 * @author Julian
 *
 * This class manages all chest. 
 */
public final class L2ChestInstance extends L2MonsterInstance
{
    private volatile boolean _isInteracted;
    private volatile boolean _specialDrop;

    public L2ChestInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
        _isInteracted = false;
        _specialDrop = false;
    }

    @Override
	public void onSpawn()
    {
        super.onSpawn();
        _isInteracted = false;
        _specialDrop = false;
        setMustRewardExpSp(true);
    }

    @Override
	public boolean hasRandomAnimation()
    {
        return false;
    }

    public synchronized boolean isInteracted()
    {
        return _isInteracted;
    }

    public synchronized void setInteracted()
    {
        _isInteracted = true;
    }

    public synchronized boolean isSpecialDrop()
    {
        return _specialDrop;
    }

    public synchronized void setSpecialDrop()
    {
        _specialDrop = true;
    }

    @Override
	public void doItemDrop(L2NpcTemplate npcTemplate, L2Character lastAttacker)
    {
        int id = getTemplate().npcId;
        if (_specialDrop)
        {
            if (id >= 1801 && id <= 1822)
                id += 11299;

            switch (id)
            {
                case 1671:
                    id = 13213;
                    break;
                case 1694:
                    id = 13215;
                    break;
                case 1717:
                    id = 13217;
                    break;
                case 1740:
                    id = 13219;
                    break;
                case 1763:
                    id = 13221;
                    break;
                case 1786:
                    id = 13223;
                    break;
            }
        }

        super.doItemDrop(NpcTable.getInstance().getTemplate(id),lastAttacker);
    }

    // cast - trap chest
    public void chestTrap(L2Character player)
    {
        int trapSkillId = 0;
        int rnd = Rnd.get(120);

        if (getTemplate().level >= 61)
        {
            if (rnd >= 90)
                trapSkillId = 4139; // explosion
            else if (rnd >= 50)
                trapSkillId = 4118; // area paralysis 
            else if (rnd >= 20)
                trapSkillId = 1167; // poison cloud
            else
                trapSkillId = 223;  // sting
        }
        else if (getTemplate().level >= 41)
        {
            if (rnd >= 90)
                trapSkillId = 4139; // explosion
            else if (rnd >= 60)
                trapSkillId = 96;   // bleed 
            else if (rnd >= 20)
                trapSkillId = 1167; // poison cloud
            else
                trapSkillId = 4118; // area paralysis
        }
        else if (getTemplate().level >= 21)
        {
            if (rnd >= 80)
                trapSkillId = 4139; // explosion
            else if (rnd >= 50)
                trapSkillId = 96;   // bleed 
            else if (rnd >= 20)
                trapSkillId = 1167; // poison cloud
            else
                trapSkillId = 129;  // poison
        }
        else
        {
            if (rnd >= 80)
                trapSkillId = 4139; // explosion
            else if (rnd >= 50)
                trapSkillId = 96;   // bleed 
            else
                trapSkillId = 129;  // poison
        }

        handleCast(player, trapSkillId);
    }

    private boolean handleCast(L2Character player, int skillId)
    {
        int skillLevel = 1;

        int lvl = getTemplate().level;
        if (lvl > 20 && lvl <= 40)
            skillLevel = 3;
        else if (lvl > 40 && lvl <= 60)
            skillLevel = 5;
        else if (lvl > 60)
            skillLevel = 6;

        if (player.isDead() || !player.isVisible() || !player.isInsideRadius(this, getDistanceToWatchObject(player), false, false))
            return false;

        L2Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);

        if (player.getFirstEffect(skill) == null)
        {
            skill.getEffects(this, player);
            broadcastPacket(new MagicSkillUse(this, player, skill.getId(), skillLevel, skill.getHitTime(), 0));
            return true;
        }
        return false;
    }

    @Override
	public boolean isMovementDisabled()
    {
        return super.isMovementDisabled() || !isInteracted();
    }
}