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
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 * Class handling the Mana damage skill
 * @author slyce
 */
public class ManaDam implements ISkillHandler
{
	private static SkillType[] _skillIds =
	{
		SkillType.MANADAM
	};

	/**
	 * @see net.sf.l2j.gameserver.handler.ISkillHandler#useSkill(net.sf.l2j.gameserver.model.L2Character, net.sf.l2j.gameserver.model.L2Skill, net.sf.l2j.gameserver.model.L2Object[], boolean)
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}

		boolean sps = false;
		boolean bss = false;

		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		if (weaponInst != null)
		{
			if (weaponInst.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
			{
				bss = true;
			}
			else if (weaponInst.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
			{
				sps = true;
			}
		}
		// If there is no weapon equipped, check for an active summon.
		else if (activeChar instanceof L2Summon)
		{
			L2Summon activeSummon = (L2Summon) activeChar;
			
			if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
			{
				bss = true;
			}
			else if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
			{
				sps = true;
			}
		}
		else if (activeChar instanceof L2NpcInstance)
		{
			sps = ((L2NpcInstance) activeChar).isUsingShot(true);
			bss = ((L2NpcInstance) activeChar).isUsingShot(false);
		}

		for (L2Object trg : targets)
		{
			if (!(trg instanceof L2Character))
			{
				continue;
			}
			
			L2Character target = (L2Character) trg;
			if (target.isInvul())
			{
				return;
			}

			double damage = Formulas.getInstance().calcManaDam(activeChar, target, skill, sps, bss);
			if (damage > 0)
			{
				double mp = (damage > target.getCurrentMp() ? target.getCurrentMp() : damage);
				target.reduceCurrentMp(mp);

				if (target instanceof L2PcInstance)
				{
					StatusUpdate sump = new StatusUpdate(target.getObjectId());
					sump.addAttribute(StatusUpdate.CUR_MP, (int) target.getCurrentMp());
					// [L2J_JP EDIT START - TSL]
					target.sendPacket(sump);

					SystemMessage sm = new SystemMessage(SystemMessage.S2_MP_HAS_BEEN_DRAINED_BY_S1);
					sm.addString(activeChar.getName());
					sm.addNumber((int) mp);
					target.sendPacket(sm);
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