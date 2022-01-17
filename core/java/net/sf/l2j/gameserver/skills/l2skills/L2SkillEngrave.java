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

import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2ArtefactInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.templates.StatsSet;

public class L2SkillEngrave extends L2Skill
{
	public L2SkillEngrave(StatsSet set)
	{
		super(set);
	}
	
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean isFirstCritical)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		final L2PcInstance player = (L2PcInstance) activeChar;
		Siege siege = SiegeManager.getInstance().getSiege(player);
		if (siege == null || !siege.getIsInProgress())
		{
			return;
		}
		
		for (L2Object target : targets)
		{
			if (target instanceof L2ArtefactInstance)
			{
				siege.getCastle().engrave(player.getClan(), target);
			}
		}
	}
}