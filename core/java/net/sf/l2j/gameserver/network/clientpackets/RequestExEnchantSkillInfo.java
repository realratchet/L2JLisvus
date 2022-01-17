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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SkillTreeTable;
import net.sf.l2j.gameserver.model.L2EnchantSkillLearn;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExEnchantSkillInfo;

/**
 * Format chdd c: (id) 0xD0 h: (subid) 0x06 d: skill id d: skill lvl
 * @author -Wooden-
 */
public class RequestExEnchantSkillInfo extends L2GameClientPacket
{
	private static final String _C__D0_06_REQUESTEXENCHANTSKILLINFO = "[C] D0:06 RequestExEnchantSkillInfo";
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(RequestExEnchantSkillInfo.class.getName());
	@SuppressWarnings("unused")
	private int _skillID;
	@SuppressWarnings("unused")
	private int _skillLvl;
	
	@Override
	protected void readImpl()
	{
		_skillID = readD();
		_skillLvl = readD();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#runImpl()
	 */
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		if (activeChar.getLevel() < 76)
		{
			return;
		}
		
		L2FolkInstance trainer = activeChar.getLastFolkNPC();
		if (trainer == null)
		{
			return;
		}
		
		if (!activeChar.isInsideRadius(trainer, L2NpcInstance.INTERACTION_DISTANCE, false, false) && !activeChar.isGM())
		{
			return;
		}
		
		L2Skill skill = SkillTable.getInstance().getInfo(_skillID, _skillLvl);
		
		boolean canteach = false;
		
		if ((skill == null) || (skill.getId() != _skillID))
		{
			return;
		}
		
		if (!trainer.getTemplate().canTeach(activeChar.getClassId()))
		{
			return; // cheater
		}
		
		L2EnchantSkillLearn[] skills = SkillTreeTable.getInstance().getAvailableEnchantSkills(activeChar);
		
		for (L2EnchantSkillLearn s : skills)
		{
			if ((s.getId() == _skillID) && (s.getLevel() == _skillLvl))
			{
				canteach = true;
				break;
			}
			
		}
		
		if (!canteach)
		{
			return; // cheater
		}
		
		int requiredSp = SkillTreeTable.getInstance().getSkillSpCost(activeChar, skill);
		int requiredExp = SkillTreeTable.getInstance().getSkillExpCost(activeChar, skill);
		byte rate = SkillTreeTable.getInstance().getSkillRate(activeChar, skill);
		ExEnchantSkillInfo asi = new ExEnchantSkillInfo(skill.getId(), skill.getLevel(), requiredSp, requiredExp, rate);
		
		if (Config.ES_SP_BOOK_NEEDED && ((skill.getLevel() == 101) || (skill.getLevel() == 141)))
		{
			asi.addRequirement(4, 6622, 1, 0);
		}
		
		sendPacket(asi);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_06_REQUESTEXENCHANTSKILLINFO;
	}
}