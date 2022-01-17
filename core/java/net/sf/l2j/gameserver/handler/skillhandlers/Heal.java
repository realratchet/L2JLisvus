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
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeFlagInstance;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Stats;

/**
 * This class ...
 * @version $Revision: 1.1.2.2.2.4 $ $Date: 2005/04/06 16:13:48 $
 */
public class Heal implements ISkillHandler
{
	// all the items ids that this handler knows
	// private static Logger _log = Logger.getLogger(Heal.class.getName());

	private static SkillType[] _skillIds =
	{
		SkillType.HEAL,
		SkillType.HEAL_PERCENT,
		SkillType.HEAL_STATIC
	};
	
	/**
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#useSkill(net.sf.l2j.gameserver.model.L2Character, net.sf.l2j.gameserver.model.L2Skill, net.sf.l2j.gameserver.model.L2Object[], boolean)
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		// Check for other effects
		try
		{
			ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(SkillType.BUFF);
			if (handler != null)
			{
				handler.useSkill(activeChar, skill, targets, isFirstCritical);
			}
		}
		catch (Exception e)
		{
		}

		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		
		for (L2Object trg : targets)
		{
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;

			// We should not heal if char is dead
			if (target.isDead())
			{
				continue;
			}

			// We should not heal walls, doors, headquarters, and grand bosses
			if ((target instanceof L2DoorInstance) || (target instanceof L2GrandBossInstance) || (target instanceof L2SiegeFlagInstance))
			{
				continue;
			}

			double hp = skill.getPower();

			if (skill.getSkillType() == SkillType.HEAL_PERCENT)
			{
				hp = (target.getMaxHp() * hp) / 100.0;
			}
			else
			{
				// Added effect of SpS and Bsps
				if (weaponInst != null)
				{
					if (weaponInst.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
					{
						hp *= 1.5;
					}
					else if (weaponInst.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
					{
						hp *= 1.3;
					}
				}
				// If there is no weapon equipped, check for an active summon.
				else if (activeChar instanceof L2Summon)
				{
					L2Summon activeSummon = (L2Summon) activeChar;

					if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
					{
						hp *= 1.5;
					}
					else if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
					{
						hp *= 1.3;
					}
				}
				else if (activeChar instanceof L2NpcInstance)
				{
					if (((L2NpcInstance) activeChar).isUsingShot(false))
					{
						hp *= 1.5;
					}
				}
			}

			if (skill.getSkillType() == SkillType.HEAL_STATIC)
			{
				hp = skill.getPower();
			}
			else if (skill.getSkillType() != SkillType.HEAL_PERCENT)
			{
				hp *= target.calcStat(Stats.HEAL_EFFECTIVNESS, 100, null, null) / 100;
			}

			target.setCurrentHp(hp + target.getCurrentHp());
			StatusUpdate su = new StatusUpdate(target.getObjectId());
			su.addAttribute(StatusUpdate.CUR_HP, (int) target.getCurrentHp());
			target.sendPacket(su);

			if (target instanceof L2PcInstance)
			{
				if (skill.getId() == 4051)
				{
					SystemMessage sm = new SystemMessage(SystemMessage.REJUVENATING_HP);
					target.sendPacket(sm);
				}
				else
				{
					if ((activeChar instanceof L2PcInstance) && (activeChar != target))
					{
						SystemMessage sm = new SystemMessage(SystemMessage.S2_HP_RESTORED_BY_S1);
						sm.addString(activeChar.getName());
						sm.addNumber((int) hp);
						target.sendPacket(sm);
					}
					else
					{
						SystemMessage sm = new SystemMessage(SystemMessage.S1_HP_RESTORED);
						sm.addNumber((int) hp);
						target.sendPacket(sm);
					}
				}
			}
		}
	}

	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}