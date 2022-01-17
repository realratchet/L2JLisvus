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
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2SiegeClan;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeFlagInstance;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.templates.StatsSet;

public class L2SkillSiegeFlag extends L2Skill
{
	private final int _npcId;

	public L2SkillSiegeFlag(StatsSet set)
	{
		super(set);
		_npcId = set.getInteger("npcId", 0);
	}

	@Override
	public boolean checkCondition(L2Character activeChar, L2Object target, boolean itemOrWeapon)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return false;
		}
		
		final L2PcInstance player = (L2PcInstance) activeChar;
		final Siege siege = SiegeManager.getInstance().getSiege(player);

		if (siege == null)
		{
			player.sendMessage("You may only place a Siege Headquarter during a siege.");
			return false;
		}

		if ((player.getClan() == null) || !player.isClanLeader())
		{
			player.sendMessage("Only clan leaders may place a Siege Headquarter.");
			return false;
		}

		if (siege.getAttackerClan(player.getClan()) == null)
		{
			player.sendMessage("You may only place a Siege Headquarter provided that you are an attacker.");
			return false;
		}

		if (player.isInsideZone(L2Character.ZONE_NO_HQ))
		{
			player.sendMessage("You may not place a Siege Headquarter inside a castle.");
			return false;
		}

		if (siege.getAttackerClan(player.getClan()).getNumFlags() >= SiegeManager.getInstance().getFlagMaxCount())
		{
			player.sendMessage("You have already placed a Siege Headquarter.");
			return false;
		}

		return super.checkCondition(activeChar, target, itemOrWeapon);
	}

	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets, boolean isFirstCritical)
	{
		if (!(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		final L2PcInstance player = (L2PcInstance) activeChar;
		final Siege siege = SiegeManager.getInstance().getSiege(player);
		
		if (siege == null || !siege.getIsInProgress())
		{
			return;
		}
		
		final L2SiegeClan sc = siege.getAttackerClan(player.getClan());
		if (sc == null)
		{
			return;
		}
		
		// Spawn a new flag
		L2SiegeFlagInstance flag = new L2SiegeFlagInstance(player, IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(_npcId));
	
		// Build Advanced Headquarters
		if (getId() == 326)
		{
			flag.setMaxSiegeHp(flag.getMaxHp() * 2);
		}
	
		flag.setTitle(player.getClan().getName());
	
		flag.setCurrentHpMp(flag.getMaxHp(), flag.getMaxMp());
		flag.setHeading(player.getHeading());
		flag.spawnMe(player.getX(), player.getY(), player.getZ() + 50);
		sc.getFlags().add(flag);
	}
}