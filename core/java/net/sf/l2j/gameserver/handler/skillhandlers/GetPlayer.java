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
package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.TeleportToLocation;
import net.sf.l2j.util.Rnd;

/*
 * Mobs can teleport players to them
 */
public class GetPlayer implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.GET_PLAYER
	};

	/**
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#useSkill(L2Character, L2Skill, L2Object[], boolean)
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}

		for (L2Object target : targets)
		{
			if (target instanceof L2PcInstance)
			{
				L2PcInstance trg = (L2PcInstance) target;
				if (trg.isAlikeDead() || trg.isTeleporting() || trg.inOfflineMode())
				{
					continue;
				}

				// Stop movement
				trg.stopMove(null);
				trg.abortAttack();
				trg.abortCast();

				trg.setIsTeleporting(true);
				trg.setIsSummoned(true);
				trg.setTarget(null);

				int x = activeChar.getX() + Rnd.get(-10, 10);
				int y = activeChar.getY() + Rnd.get(-10, 10);
				int z = activeChar.getZ();

				trg.broadcastPacket(new TeleportToLocation(trg, x, y, z));
				trg.setXYZ(x, y, z);

				if (trg.getWorldRegion() != null)
				{
					trg.getWorldRegion().revalidateZones(trg);
				}
			}
		}
	}

	/**
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#getSkillIds()
	 */
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}