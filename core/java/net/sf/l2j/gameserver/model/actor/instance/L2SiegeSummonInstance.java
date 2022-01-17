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

import net.sf.l2j.gameserver.skills.l2skills.L2SkillSummon;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
 
public class L2SiegeSummonInstance extends L2SummonInstance
{
    public L2SiegeSummonInstance(int objectId, L2NpcTemplate template, L2PcInstance owner, L2SkillSummon skill)
    {
        super(objectId, template, owner, skill);

        if (skill.getId() == 13)
            _isSiegeGolem = true;
    }

    @Override
    public void onSpawn()
    {
        super.onSpawn();

        if (!isInsideZone(ZONE_SIEGE))
            unSummon(getOwner());
    }
}