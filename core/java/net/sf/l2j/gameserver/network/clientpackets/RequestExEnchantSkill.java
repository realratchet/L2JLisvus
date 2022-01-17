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
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.network.serverpackets.ShortCutRegister;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.IllegalPlayerAction;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * Format chdd c: (id) 0xD0 h: (subid) 0x06 d: skill id d: skill lvl
 * @author -Wooden-
 */
public class RequestExEnchantSkill extends L2GameClientPacket
{
	private static final String _C__D0_07_REQUESTEXENCHANTSKILL = "[C] D0:07 RequestExEnchantSkill";
	private static Logger _log = Logger.getLogger(RequestExEnchantSkill.class.getName());
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
	
	/**
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		L2FolkInstance trainer = player.getLastFolkNPC();
		if (trainer == null)
		{
			return;
		}
		
		int npcid = trainer.getNpcId();
		
		if (!player.isInsideRadius(trainer, L2NpcInstance.INTERACTION_DISTANCE, false, false) && !player.isGM())
		{
			return;
		}
		
		if (player.getSkillLevel(_skillID) >= _skillLvl)
		{
			return;
		}
		
		if (player.getClassId().getId() < 88)
		{
			return;
		}
		
		if (player.getLevel() < 76)
		{
			return;
		}
		
		L2Skill skill = SkillTable.getInstance().getInfo(_skillID, _skillLvl);
		
		int count = 0;
		int _requiredSp = 10000000;
		int _requiredExp = 100000;
		byte _rate = 0;
		
		int _baseLvl = 1;
		
		L2EnchantSkillLearn[] skills = SkillTreeTable.getInstance().getAvailableEnchantSkills(player);
		
		for (L2EnchantSkillLearn s : skills)
		{
			L2Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
			if ((sk == null) || (sk != skill) || !sk.getCanLearn(player.getClassId()) || !sk.canTeachBy(npcid))
			{
				continue;
			}
			
			count++;
			_requiredSp = s.getSpCost();
			_requiredExp = s.getExp();
			_rate = s.getRate(player);
			_baseLvl = s.getBaseLevel();
		}
		
		if (count == 0)
		{
			player.sendMessage("You are trying to learn a skill that you can't.");
			Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to learn a skill that he can't!!!", IllegalPlayerAction.PUNISH_KICK);
			return;
		}
		
		if (player.getSp() >= _requiredSp)
		{
			long _expAfter = player.getExp() - _requiredExp;
			if ((player.getExp() >= _requiredExp) && (_expAfter >= Experience.LEVEL[player.getLevel()]))
			{
				
				if (Config.ES_SP_BOOK_NEEDED && ((_skillLvl == 101) || (_skillLvl == 141))) // only first lvl requires book
				{
					
					L2ItemInstance spb = player.getInventory().getItemByItemId(6622);
					
					if (spb == null)
					{
						// No spellbook
						player.sendPacket(new SystemMessage(SystemMessage.ITEM_MISSING_TO_LEARN_SKILL));
						return;
					}
					
					// ok
					player.destroyItem("Consume", spb, trainer, true);
				}
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_DONT_HAVE_ENOUGH_EXP_TO_ENCHANT_THAT_SKILL));
				return;
			}
		}
		else
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_DONT_HAVE_ENOUGH_SP_TO_ENCHANT_THAT_SKILL));
			return;
		}
		
		if (Rnd.get(100) <= _rate)
		{
			player.addSkill(skill, true);
			
			if (Config.DEBUG)
			{
				_log.fine("Learned skill " + _skillID + " for " + _requiredSp + " SP.");
			}
			
			SystemMessage sm = new SystemMessage(SystemMessage.YOU_HAVE_SUCCEEDED_IN_ENCHANTING_THE_SKILL_S1);
			sm.addSkillName(skill);
			player.sendPacket(sm);
			
		}
		else
		{
			
			if (skill.getLevel() > 100)
			{
				_skillLvl = _baseLvl;
				player.addSkill(SkillTable.getInstance().getInfo(_skillID, _skillLvl), true);
			}
			
			SystemMessage sm = new SystemMessage(SystemMessage.YOU_HAVE_FAILED_TO_ENCHANT_THE_SKILL_S1);
			sm.addSkillName(_skillID, _skillLvl);
			player.sendPacket(sm);
		}
		
		player.getStat().removeExpAndSp(_requiredExp, _requiredSp, false);
		
		StatusUpdate su = new StatusUpdate(player.getObjectId());
		su.addAttribute(StatusUpdate.EXP, Experience.getVisualExp(player.getLevel(), player.getExp()));
		su.addAttribute(StatusUpdate.SP, player.getSp());
		player.sendPacket(su);
		
		trainer.showEnchantSkillList(player, player.getClassId());
		
		// update all the shortcuts to this skill
		L2ShortCut[] allShortCuts = player.getAllShortCuts();
		
		for (L2ShortCut sc : allShortCuts)
		{
			if (sc == null)
			{
				continue;
			}
			
			if ((sc.getId() == _skillID) && (sc.getType() == L2ShortCut.TYPE_SKILL))
			{
				L2ShortCut newsc = new L2ShortCut(sc.getSlot(), sc.getPage(), sc.getType(), sc.getId(), _skillLvl, 1);
				player.sendPacket(new ShortCutRegister(newsc));
				player.registerShortCut(newsc);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_07_REQUESTEXENCHANTSKILL;
	}
}