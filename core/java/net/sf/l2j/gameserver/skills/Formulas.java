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
package net.sf.l2j.gameserver.skills;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.SevenSignsFestival;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2SiegeClan;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2CubicInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeFlagInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerState;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerState.CheckPlayerState;
import net.sf.l2j.gameserver.skills.conditions.ConditionUsingItemType;
import net.sf.l2j.gameserver.skills.funcs.Func;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2PcTemplate;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.templates.L2WeaponType;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * Global calculations, can be modified by server admins
 */
public final class Formulas
{
	private static final Logger _log = Logger.getLogger(Formulas.class.getName());
	
	/** Regenerate Task period */
	private static final int HP_REGENERATE_PERIOD = 3000; // 3 secs
	
	static class FuncAddLevel3 extends Func
	{
		static final FuncAddLevel3[] _instances = new FuncAddLevel3[Stats.NUM_STATS];
		
		static Func getInstance(Stats stat)
		{
			int pos = stat.ordinal();
			if (_instances[pos] == null)
			{
				_instances[pos] = new FuncAddLevel3(stat);
			}
			return _instances[pos];
		}
		
		private FuncAddLevel3(Stats stat)
		{
			super(stat, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			env.value += env.player.getLevel() / 3;
		}
	}
	
	static class FuncMultLevelMod extends Func
	{
		static final FuncMultLevelMod[] _instances = new FuncMultLevelMod[Stats.NUM_STATS];
		
		static Func getInstance(Stats stat)
		{
			int pos = stat.ordinal();
			if (_instances[pos] == null)
			{
				_instances[pos] = new FuncMultLevelMod(stat);
			}
			return _instances[pos];
		}
		
		private FuncMultLevelMod(Stats stat)
		{
			super(stat, 0x20, null);
		}
		
		@Override
		public void calc(Env env)
		{
			env.value *= env.player.getLevelMod();
		}
	}
	
	static class FuncMultRegenResting extends Func
	{
		static final FuncMultRegenResting[] _instances = new FuncMultRegenResting[Stats.NUM_STATS];
		
		/**
		 * Return the Func object corresponding to the state concerned.<BR>
		 * <BR>
		 * @param stat
		 * @return
		 */
		static Func getInstance(Stats stat)
		{
			int pos = stat.ordinal();
			
			if (_instances[pos] == null)
			{
				_instances[pos] = new FuncMultRegenResting(stat);
			}
			
			return _instances[pos];
		}
		
		/**
		 * Constructor of the FuncMultRegenResting.<BR>
		 * <BR>
		 * @param stat
		 */
		private FuncMultRegenResting(Stats stat)
		{
			super(stat, 0x20, null);
			setCondition(new ConditionPlayerState(CheckPlayerState.RESTING, true));
		}
		
		/**
		 * Calculate the modifier of the state concerned.<BR>
		 * <BR>
		 */
		@Override
		public void calc(Env env)
		{
			if (!_cond.test(env, _funcOwner))
			{
				return;
			}
			
			env.value *= 1.45;
		}
	}
	
	static class FuncPAtkMod extends Func
	{
		static final FuncPAtkMod _instance = new FuncPAtkMod();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncPAtkMod()
		{
			super(Stats.POWER_ATTACK, 0x30, null);
		}
		
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.STR.calcBonus(env.player) * env.player.getLevelMod();
		}
	}
	
	static class FuncMAtkMod extends Func
	{
		static final FuncMAtkMod _instance = new FuncMAtkMod();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncMAtkMod()
		{
			super(Stats.MAGIC_ATTACK, 0x20, null);
		}
		
		@Override
		public void calc(Env env)
		{
			double intb = BaseStats.INT.calcBonus(env.player);
			double lvlb = env.player.getLevelMod();
			env.value *= (lvlb * lvlb) * (intb * intb);
		}
	}
	
	static class FuncMDefMod extends Func
	{
		static final FuncMDefMod _instance = new FuncMDefMod();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncMDefMod()
		{
			super(Stats.MAGIC_DEFENCE, 0x20, null);
		}
		
		@Override
		public void calc(Env env)
		{
			if (env.player instanceof L2PcInstance)
			{
				L2PcInstance p = (L2PcInstance) env.player;
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LFINGER) != null)
				{
					env.value -= 5;
				}
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RFINGER) != null)
				{
					env.value -= 5;
				}
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEAR) != null)
				{
					env.value -= 9;
				}
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_REAR) != null)
				{
					env.value -= 9;
				}
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_NECK) != null)
				{
					env.value -= 13;
				}
			}
			env.value *= BaseStats.MEN.calcBonus(env.player) * env.player.getLevelMod();
		}
	}
	
	static class FuncPDefMod extends Func
	{
		static final FuncPDefMod _instance = new FuncPDefMod();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncPDefMod()
		{
			super(Stats.POWER_DEFENCE, 0x20, null);
		}
		
		@Override
		public void calc(Env env)
		{
			if (env.player instanceof L2PcInstance)
			{
				L2PcInstance p = (L2PcInstance) env.player;
				
				boolean hasMagePDef = (p.getClassId().isMage() || (p.getClassId().getId() == 0x31));
				
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_HEAD) != null)
				{
					env.value -= 12;
				}
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST) != null)
				{
					env.value -= hasMagePDef ? 15 : 31;
				}
				if ((p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEGS) != null) || ((p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST) != null) && (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST).getItem().getBodyPart() == L2Item.SLOT_FULL_ARMOR)))
				{
					env.value -= hasMagePDef ? 8 : 18;
				}
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_GLOVES) != null)
				{
					env.value -= 8;
				}
				if (p.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FEET) != null)
				{
					env.value -= 7;
				}
			}
			env.value *= env.player.getLevelMod();
		}
	}
	
	static class FuncBowAtkRange extends Func
	{
		private static final FuncBowAtkRange _instance = new FuncBowAtkRange();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncBowAtkRange()
		{
			super(Stats.POWER_ATTACK_RANGE, 0x10, null);
			setCondition(new ConditionUsingItemType(L2WeaponType.BOW.mask()));
		}
		
		@Override
		public void calc(Env env)
		{
			if (!_cond.test(env, _funcOwner))
			{
				return;
			}
			env.value += 450;
		}
	}
	
	static class FuncAtkAccuracy extends Func
	{
		static final FuncAtkAccuracy _instance = new FuncAtkAccuracy();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncAtkAccuracy()
		{
			super(Stats.ACCURACY_COMBAT, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			L2Character p = env.player;
			// [Square(DEX)]*6 + lvl + weapon hit bonus;
			env.value += Math.sqrt(p.getDEX()) * 6;
			env.value += p.getLevel();
			if (p instanceof L2Summon)
			{
				env.value += (p.getLevel() < 60) ? 4 : 5;
			}
		}
	}
	
	static class FuncAtkEvasion extends Func
	{
		static final FuncAtkEvasion _instance = new FuncAtkEvasion();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncAtkEvasion()
		{
			super(Stats.EVASION_RATE, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			L2Character p = env.player;
			// [Square(DEX)]*6 + lvl;
			env.value += Math.sqrt(p.getDEX()) * 6;
			env.value += p.getLevel();
		}
	}
	
	static class FuncAtkCritical extends Func
	{
		static final FuncAtkCritical _instance = new FuncAtkCritical();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncAtkCritical()
		{
			super(Stats.CRITICAL_RATE, 0x30, null);
		}
		
		@Override
		public void calc(Env env)
		{
			L2Character p = env.player;
			if (p instanceof L2SummonInstance)
			{
				env.value = 40;
			}
			else if (p instanceof L2PcInstance)
			{
				if (p.getActiveWeaponInstance() == null)
				{
					env.value = 40;
				}
				else
				{
					env.value *= BaseStats.DEX.calcBonus(p);
					env.value *= 10;
					
				}
			}
			env.baseValue = env.value;
		}
	}
	
	static class FuncMAtkCritical extends Func
	{
		static final FuncMAtkCritical _instance = new FuncMAtkCritical();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncMAtkCritical()
		{
			super(Stats.MCRITICAL_RATE, 0x30, null);
		}
		
		@Override
		public void calc(Env env)
		{
			L2Character p = env.player;
			if (p instanceof L2Summon)
			{
				env.value = 5;
			}
			else if ((p instanceof L2PcInstance) && (p.getActiveWeaponInstance() != null))
			{
				env.value *= BaseStats.WIT.calcBonus(p);
			}
		}
	}
	
	static class FuncMoveSpeed extends Func
	{
		static final FuncMoveSpeed _instance = new FuncMoveSpeed();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncMoveSpeed()
		{
			super(Stats.RUN_SPEED, 0x30, null);
		}
		
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.DEX.calcBonus(env.player);
		}
	}
	
	static class FuncPAtkSpeed extends Func
	{
		static final FuncPAtkSpeed _instance = new FuncPAtkSpeed();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncPAtkSpeed()
		{
			super(Stats.POWER_ATTACK_SPEED, 0x20, null);
		}
		
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.DEX.calcBonus(env.player);
		}
	}
	
	static class FuncMAtkSpeed extends Func
	{
		static final FuncMAtkSpeed _instance = new FuncMAtkSpeed();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncMAtkSpeed()
		{
			super(Stats.MAGIC_ATTACK_SPEED, 0x20, null);
		}
		
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.WIT.calcBonus(env.player);
		}
	}
	
	static class FuncHennaSTR extends Func
	{
		static final FuncHennaSTR _instance = new FuncHennaSTR();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncHennaSTR()
		{
			super(Stats.STAT_STR, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			// L2PcTemplate t = (L2PcTemplate)env.player.getTemplate();
			L2PcInstance pc = (L2PcInstance) env.player;
			if (pc != null)
			{
				env.value += pc.getHennaStatSTR();
			}
		}
	}
	
	static class FuncHennaDEX extends Func
	{
		static final FuncHennaDEX _instance = new FuncHennaDEX();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncHennaDEX()
		{
			super(Stats.STAT_DEX, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			// L2PcTemplate t = (L2PcTemplate)env.player.getTemplate();
			L2PcInstance pc = (L2PcInstance) env.player;
			if (pc != null)
			{
				env.value += pc.getHennaStatDEX();
			}
		}
	}
	
	static class FuncHennaINT extends Func
	{
		static final FuncHennaINT _instance = new FuncHennaINT();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncHennaINT()
		{
			super(Stats.STAT_INT, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			// L2PcTemplate t = (L2PcTemplate)env.player.getTemplate();
			L2PcInstance pc = (L2PcInstance) env.player;
			if (pc != null)
			{
				env.value += pc.getHennaStatINT();
			}
		}
	}
	
	static class FuncHennaMEN extends Func
	{
		static final FuncHennaMEN _instance = new FuncHennaMEN();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncHennaMEN()
		{
			super(Stats.STAT_MEN, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			// L2PcTemplate t = (L2PcTemplate)env.player.getTemplate();
			L2PcInstance pc = (L2PcInstance) env.player;
			if (pc != null)
			{
				env.value += pc.getHennaStatMEN();
			}
		}
	}
	
	static class FuncHennaCON extends Func
	{
		static final FuncHennaCON _instance = new FuncHennaCON();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncHennaCON()
		{
			super(Stats.STAT_CON, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			// L2PcTemplate t = (L2PcTemplate)env.player.getTemplate();
			L2PcInstance pc = (L2PcInstance) env.player;
			if (pc != null)
			{
				env.value += pc.getHennaStatCON();
			}
		}
	}
	
	static class FuncHennaWIT extends Func
	{
		static final FuncHennaWIT _instance = new FuncHennaWIT();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncHennaWIT()
		{
			super(Stats.STAT_WIT, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			// L2PcTemplate t = (L2PcTemplate)env.player.getTemplate();
			L2PcInstance pc = (L2PcInstance) env.player;
			if (pc != null)
			{
				env.value += pc.getHennaStatWIT();
			}
		}
	}
	
	static class FuncMaxHpAdd extends Func
	{
		static final FuncMaxHpAdd _instance = new FuncMaxHpAdd();
		
		static Func getInstance()
		{
			return _instance;
		}
		
		private FuncMaxHpAdd()
		{
			super(Stats.MAX_HP, 0x10, null);
		}
		
		@Override
		public void calc(Env env)
		{
			L2PcTemplate t = (L2PcTemplate) env.player.getTemplate();
			int lvl = env.player.getLevel() - t.classBaseLevel;
			double hpmod = t.lvlHpMod * lvl;
			double hpmax = (t.lvlHpAdd + hpmod) * lvl;
			double hpmin = (t.lvlHpAdd * lvl) + hpmod;
			env.value += (hpmax + hpmin) / 2;
		}
	}

	static class FuncMaxHpMul extends Func
	{
		static final FuncMaxHpMul _instance = new FuncMaxHpMul();

		static Func getInstance()
		{
			return _instance;
		}

		private FuncMaxHpMul()
		{
			super(Stats.MAX_HP, 0x20, null);
		}

		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.CON.calcBonus(env.player);
		}
	}

	static class FuncMaxCpAdd extends Func
	{
		static final FuncMaxCpAdd _instance = new FuncMaxCpAdd();

		static Func getInstance()
		{
			return _instance;
		}

		private FuncMaxCpAdd()
		{
			super(Stats.MAX_CP, 0x10, null);
		}

		@Override
		public void calc(Env env)
		{
			L2PcTemplate t = (L2PcTemplate) env.player.getTemplate();
			int lvl = env.player.getLevel() - t.classBaseLevel;
			double cpmod = t.lvlCpMod * lvl;
			double cpmax = (t.lvlCpAdd + cpmod) * lvl;
			double cpmin = (t.lvlCpAdd * lvl) + cpmod;
			env.value += (cpmax + cpmin) / 2;
		}
	}

	static class FuncMaxCpMul extends Func
	{
		static final FuncMaxCpMul _instance = new FuncMaxCpMul();

		static Func getInstance()
		{
			return _instance;
		}

		private FuncMaxCpMul()
		{
			super(Stats.MAX_CP, 0x20, null);
		}

		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.CON.calcBonus(env.player);
		}
	}

	static class FuncMaxMpAdd extends Func
	{
		static final FuncMaxMpAdd _instance = new FuncMaxMpAdd();

		static Func getInstance()
		{
			return _instance;
		}

		private FuncMaxMpAdd()
		{
			super(Stats.MAX_MP, 0x10, null);
		}

		@Override
		public void calc(Env env)
		{
			L2PcTemplate t = (L2PcTemplate) env.player.getTemplate();
			int lvl = env.player.getLevel() - t.classBaseLevel;
			double mpmod = t.lvlMpMod * lvl;
			double mpmax = (t.lvlMpAdd + mpmod) * lvl;
			double mpmin = (t.lvlMpAdd * lvl) + mpmod;
			env.value += (mpmax + mpmin) / 2;
		}
	}

	static class FuncMaxMpMul extends Func
	{
		static final FuncMaxMpMul _instance = new FuncMaxMpMul();

		static Func getInstance()
		{
			return _instance;
		}

		private FuncMaxMpMul()
		{
			super(Stats.MAX_MP, 0x20, null);
		}

		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.MEN.calcBonus(env.player);
		}
	}

	public static Formulas getInstance()
	{
		return SingletonHolder._instance;
	}

	/**
	 * Return the period between 2 regeneration tasks (3s for L2Character, 5 min for L2DoorInstance).<BR>
	 * <BR>
	 * @param cha
	 * @return
	 */
	public int getRegeneratePeriod(L2Character cha)
	{
		if (cha instanceof L2DoorInstance)
		{
			return HP_REGENERATE_PERIOD * 100; // 5 mins
		}

		return HP_REGENERATE_PERIOD; // 3s
	}

	/**
	 * Return the standard NPC Calculator set containing ACCURACY_COMBAT and EVASION_RATE.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * A calculator is created to manage and dynamically calculate the effect of a character property (ex : MAX_HP, REGENERATE_HP_RATE...). In fact, each calculator is a table of Func object in which each Func represents a mathematic function : <BR>
	 * <BR>
	 * FuncAtkAccuracy -> Math.sqrt(_player.getDEX())*6+_player.getLevel()<BR>
	 * <BR>
	 * To reduce cache memory use, L2NPCInstances who don't have skills share the same Calculator set called <B>NPC_STD_CALCULATOR</B>.<BR>
	 * <BR>
	 * @return
	 */
	public Calculator[] getStdNPCCalculators()
	{
		Calculator[] std = new Calculator[Stats.NUM_STATS];
		
		// Add the FuncAtkAccuracy to the Standard Calculator of ACCURACY_COMBAT
		std[Stats.ACCURACY_COMBAT.ordinal()] = new Calculator();
		std[Stats.ACCURACY_COMBAT.ordinal()].addFunc(FuncAtkAccuracy.getInstance());
		
		// Add the FuncAtkEvasion to the Standard Calculator of EVASION_RATE
		std[Stats.EVASION_RATE.ordinal()] = new Calculator();
		std[Stats.EVASION_RATE.ordinal()].addFunc(FuncAtkEvasion.getInstance());
		
		return std;
	}
	
	/**
	 * Add basics Func objects to L2PcInstance and L2Summon.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * A calculator is created to manage and dynamically calculate the effect of a character property (ex : MAX_HP, REGENERATE_HP_RATE...). In fact, each calculator is a table of Func object in which each Func represents a mathematic function : <BR>
	 * <BR>
	 * FuncAtkAccuracy -> Math.sqrt(_player.getDEX())*6+_player.getLevel()<BR>
	 * <BR>
	 * @param cha L2PcInstance or L2Summon that must obtain basic Func objects
	 */
	public void addFuncsToNewCharacter(L2Character cha)
	{
		if (cha instanceof L2PcInstance)
		{
			cha.addStatFunc(FuncMaxHpAdd.getInstance());
			cha.addStatFunc(FuncMaxHpMul.getInstance());
			cha.addStatFunc(FuncMaxCpAdd.getInstance());
			cha.addStatFunc(FuncMaxCpMul.getInstance());
			cha.addStatFunc(FuncMaxMpAdd.getInstance());
			cha.addStatFunc(FuncMaxMpMul.getInstance());
			// cha.addStatFunc(FuncMultRegenResting.getInstance(Stats.REGENERATE_HP_RATE));
			// cha.addStatFunc(FuncMultRegenResting.getInstance(Stats.REGENERATE_CP_RATE));
			// cha.addStatFunc(FuncMultRegenResting.getInstance(Stats.REGENERATE_MP_RATE));
			cha.addStatFunc(FuncBowAtkRange.getInstance());
			
			// cha.addStatFunc(FuncMultLevelMod.getInstance(Stats.POWER_ATTACK));
			// cha.addStatFunc(FuncMultLevelMod.getInstance(Stats.POWER_DEFENCE));
			// cha.addStatFunc(FuncMultLevelMod.getInstance(Stats.MAGIC_DEFENCE));
			cha.addStatFunc(FuncPAtkMod.getInstance());
			cha.addStatFunc(FuncMAtkMod.getInstance());
			cha.addStatFunc(FuncPDefMod.getInstance());
			cha.addStatFunc(FuncMDefMod.getInstance());
			cha.addStatFunc(FuncAtkCritical.getInstance());
			cha.addStatFunc(FuncMAtkCritical.getInstance());
			cha.addStatFunc(FuncAtkAccuracy.getInstance());
			cha.addStatFunc(FuncAtkEvasion.getInstance());
			cha.addStatFunc(FuncPAtkSpeed.getInstance());
			cha.addStatFunc(FuncMAtkSpeed.getInstance());
			cha.addStatFunc(FuncMoveSpeed.getInstance());
			
			cha.addStatFunc(FuncHennaSTR.getInstance());
			cha.addStatFunc(FuncHennaDEX.getInstance());
			cha.addStatFunc(FuncHennaINT.getInstance());
			cha.addStatFunc(FuncHennaMEN.getInstance());
			cha.addStatFunc(FuncHennaCON.getInstance());
			cha.addStatFunc(FuncHennaWIT.getInstance());
		}
		else if (cha instanceof L2PetInstance)
		{
			cha.addStatFunc(FuncPAtkMod.getInstance());
			cha.addStatFunc(FuncMAtkMod.getInstance());
			cha.addStatFunc(FuncPDefMod.getInstance());
			cha.addStatFunc(FuncMDefMod.getInstance());
			cha.addStatFunc(FuncAtkCritical.getInstance());
		}
		else if (cha instanceof L2Summon)
		{
			// cha.addStatFunc(FuncMultRegenResting.getInstance(Stats.REGENERATE_HP_RATE));
			// cha.addStatFunc(FuncMultRegenResting.getInstance(Stats.REGENERATE_MP_RATE));
			cha.addStatFunc(FuncAtkCritical.getInstance());
			cha.addStatFunc(FuncMAtkCritical.getInstance());
			cha.addStatFunc(FuncAtkAccuracy.getInstance());
			cha.addStatFunc(FuncAtkEvasion.getInstance());
		}
	}
	
	/**
	 * Calculate the HP regen rate (base + modifiers).<BR>
	 * <BR>
	 * @param cha
	 * @return
	 */
	public final double calcHpRegen(L2Character cha)
	{
		double init = cha.getTemplate().baseHpReg;
		double hpRegenMultiplier = cha.isRaid() ? Config.RAID_HP_REGEN_MULTIPLIER : Config.HP_REGEN_MULTIPLIER;
		double hpRegenBonus = 0;
		
		if (Config.CHAMPION_ENABLE && cha.isChampion())
		{
			hpRegenMultiplier *= Config.CHAMPION_HP_REGEN;
		}
		
		if (cha instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) cha;
			
			// Calculate correct baseHpReg value for certain level of PC
			init += (player.getLevel() > 10) ? ((player.getLevel() - 1) / 10) : 0.5;
			
			// SevenSigns Festival modifier
			if (SevenSignsFestival.getInstance().isFestivalInProgress() && player.isFestivalParticipant())
			{
				hpRegenMultiplier *= calcFestivalRegenModifier(player);
			}
			else
			{
				double siegeModifier = calcSiegeRegenModifer(player);
				if (siegeModifier > 0)
				{
					hpRegenMultiplier *= siegeModifier;
				}
			}
			
			if (player.isInsideZone(L2Character.ZONE_CLAN_HALL) && (player.getClan() != null))
			{
				int clanHallIndex = player.getClan().getHasHideout();
				if (clanHallIndex > 0)
				{
					ClanHall clansHall = ClanHallManager.getInstance().getClanHallById(clanHallIndex);
					if (clansHall != null)
					{
						if (clansHall.getFunction(ClanHall.FUNC_RESTORE_HP) != null)
						{
							hpRegenMultiplier *= 1 + ((double) clansHall.getFunction(ClanHall.FUNC_RESTORE_HP).getLvl() / 100);
						}
					}
				}
			}
			
			// Mother Tree effect is calculated at last
			if (player.isInsideZone(L2Character.ZONE_MOTHER_TREE))
			{
				hpRegenBonus += 2;
			}
			
			// Calculate Movement bonus
			if (player.isSitting())
			{
				hpRegenMultiplier *= 1.5; // Sitting
			}
			else if (!player.isMoving())
			{
				hpRegenMultiplier *= 1.1; // Staying
			}
			else if (player.isRunning())
			{
				hpRegenMultiplier *= 0.7; // Running
			}
			
			// Apply CON bonus
			init *= cha.getLevelMod() * BaseStats.CON.calcBonus(cha);
		}
		else if (cha instanceof L2PetInstance)
		{
			init = ((L2PetInstance) cha).getPetData().getPetRegenHP();
		}
		
		if (init < 1)
		{
			init = 1;
		}
		
		return (cha.calcStat(Stats.REGENERATE_HP_RATE, init, null, null) * hpRegenMultiplier) + hpRegenBonus;
	}
	
	/**
	 * Calculate the MP regen rate (base + modifiers).<BR>
	 * <BR>
	 * @param cha
	 * @return
	 */
	public final double calcMpRegen(L2Character cha)
	{
		double init = cha.getTemplate().baseMpReg;
		double mpRegenMultiplier = cha.isRaid() ? Config.RAID_MP_REGEN_MULTIPLIER : Config.MP_REGEN_MULTIPLIER;
		double mpRegenBonus = 0;
		
		if (cha instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) cha;
			
			// Calculate correct baseMpReg value for certain level of PC
			init += 0.3 * ((player.getLevel() - 1) / 10);
			
			// SevenSigns Festival modifier
			if (SevenSignsFestival.getInstance().isFestivalInProgress() && player.isFestivalParticipant())
			{
				mpRegenMultiplier *= calcFestivalRegenModifier(player);
			}
			
			// Mother Tree effect is calculated at last
			if (player.isInsideZone(L2Character.ZONE_MOTHER_TREE))
			{
				mpRegenBonus += 1;
			}
			
			if (player.isInsideZone(L2Character.ZONE_CLAN_HALL) && (player.getClan() != null))
			{
				int clanHallIndex = player.getClan().getHasHideout();
				if (clanHallIndex > 0)
				{
					ClanHall clansHall = ClanHallManager.getInstance().getClanHallById(clanHallIndex);
					if (clansHall != null)
					{
						if (clansHall.getFunction(ClanHall.FUNC_RESTORE_MP) != null)
						{
							mpRegenMultiplier *= 1 + ((double) clansHall.getFunction(ClanHall.FUNC_RESTORE_MP).getLvl() / 100);
						}
					}
				}
			}
			
			// Calculate Movement bonus
			if (player.isSitting())
			{
				mpRegenMultiplier *= 1.5; // Sitting
			}
			else if (!player.isMoving())
			{
				mpRegenMultiplier *= 1.1; // Staying
			}
			else if (player.isRunning())
			{
				mpRegenMultiplier *= 0.7; // Running
			}
			
			// Apply MEN bonus
			init *= cha.getLevelMod() * BaseStats.MEN.calcBonus(cha);
		}
		else if (cha instanceof L2PetInstance)
		{
			init = ((L2PetInstance) cha).getPetData().getPetRegenMP();
		}
		
		if (init < 1)
		{
			init = 1;
		}
		
		return (cha.calcStat(Stats.REGENERATE_MP_RATE, init, null, null) * mpRegenMultiplier) + mpRegenBonus;
	}
	
	/**
	 * Calculate the CP regen rate (base + modifiers).<BR>
	 * <BR>
	 * @param cha
	 * @return
	 */
	public final double calcCpRegen(L2Character cha)
	{
		double init = cha.getTemplate().baseHpReg;
		double cpRegenMultiplier = Config.CP_REGEN_MULTIPLIER;
		double cpRegenBonus = 0;
		
		if (cha instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) cha;
			
			// Calculate correct baseHpReg value for certain level of PC
			init += (player.getLevel() > 10) ? ((player.getLevel() - 1) / 10) : 0.5;
			
			// Calculate Movement bonus
			if (player.isSitting())
			{
				cpRegenMultiplier *= 1.5; // Sitting
			}
			else if (!player.isMoving())
			{
				cpRegenMultiplier *= 1.1; // Staying
			}
			else if (player.isRunning())
			{
				cpRegenMultiplier *= 0.7; // Running
			}
		}
		else
		{
			// Calculate Movement bonus
			if (!cha.isMoving())
			{
				cpRegenMultiplier *= 1.1; // Staying
			}
			else if (cha.isRunning())
			{
				cpRegenMultiplier *= 0.7; // Running
			}
		}
		
		// Apply CON bonus
		init *= cha.getLevelMod() * BaseStats.CON.calcBonus(cha);
		if (init < 1)
		{
			init = 1;
		}
		
		return (cha.calcStat(Stats.REGENERATE_CP_RATE, init, null, null) * cpRegenMultiplier) + cpRegenBonus;
	}
	
	@SuppressWarnings("deprecation")
	public final double calcFestivalRegenModifier(L2PcInstance activeChar)
	{
		final int[] festivalInfo = SevenSignsFestival.getInstance().getFestivalForPlayer(activeChar);
		final int oracle = festivalInfo[0];
		final int festivalId = festivalInfo[1];
		int[] festivalCenter;
		
		// If the player isn't found in the festival, leave the regen rate as it is.
		if (festivalId < 0)
		{
			return 0;
		}
		
		// Retrieve the X and Y coords for the center of the festival arena the player is in.
		if (oracle == SevenSigns.CABAL_DAWN)
		{
			festivalCenter = SevenSignsFestival.festivalDawnPlayerSpawns[festivalId];
		}
		else
		{
			festivalCenter = SevenSignsFestival.festivalDuskPlayerSpawns[festivalId];
		}
		
		// Check the distance between the player and the player spawn point, in the center of the arena.
		double distToCenter = activeChar.getDistance(festivalCenter[0], festivalCenter[1]);
		
		if (Config.DEBUG)
		{
			_log.info("Distance: " + distToCenter + ", RegenMulti: " + ((distToCenter * 2.5) / 50));
		}
		
		return 1.0 - (distToCenter * 0.0005); // Maximum Decreased Regen of ~ -65%;
	}
	
	public final double calcSiegeRegenModifer(L2PcInstance activeChar)
	{
		if ((activeChar == null) || (activeChar.getClan() == null))
		{
			return 0;
		}
		
		Siege siege = SiegeManager.getInstance().getSiege(activeChar);
		if ((siege == null) || !siege.getIsInProgress())
		{
			return 0;
		}
		
		L2SiegeClan siegeClan = siege.getAttackerClan(activeChar.getClan().getClanId());
		if (siegeClan == null)
		{
			return 0;
		}
		
		// Check if player is near a siege flag
		boolean isNearFlag = false;
		for (L2NpcInstance flag : siegeClan.getFlags())
		{
			if (Util.checkIfInRange(200, activeChar, flag, true))
			{
				isNearFlag = true;
				break;
			}
		}
		
		if (!isNearFlag)
		{
			return 0;
		}
		
		return 1.5; // If all is true, then modifer will be 50% more
	}
	
	/**
	 * Calculate blow damage based on cAtk
	 * @param attacker
	 * @param target
	 * @param skill
	 * @param shld
	 * @param ss
	 * @return
	 */
	public double calcBlowDamage(L2Character attacker, L2Character target, L2Skill skill, boolean shld, boolean ss)
	{
		double power = skill.getPower();
		double damage = attacker.getPAtk(target);
		double defence = target.getPDef(attacker);
		if (ss)
		{
			damage *= 2;
		}
		
		if (shld)
		{
			if ((100 - Config.ALT_PERFECT_SHLD_BLOCK) < Rnd.get(100))
			{
				target.sendPacket(new SystemMessage(SystemMessage.YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS));
				return 1;
			}
			defence += target.getShldDef();
			target.sendPacket(new SystemMessage(SystemMessage.SHIELD_DEFENCE_SUCCESSFULL));
		}
		
		damage = (attacker.calcStat(Stats.CRITICAL_DAMAGE, (damage + power), target, skill) + (attacker.calcStat(Stats.CRITICAL_DAMAGE_ADD, 0, target, skill) * 6.5));
		
		if (target instanceof L2NpcInstance)
		{
			damage *= ((L2NpcInstance) target).getTemplate().getVulnerability(Stats.DAGGER_WPN_VULN);
		}
		
		// get the vulnerability for the instance due to skills (buffs, passives, toggles, etc)
		damage = target.calcStat(Stats.DAGGER_WPN_VULN, damage, target, null);
		damage *= 70 / defence;
		damage += Rnd.get() * attacker.getRandomDamage(target);
		
		// Damage bonus in pvp
		if ((attacker instanceof L2PlayableInstance) && (target instanceof L2PlayableInstance))
		{
			damage = attacker.calcStat(Stats.PVP_PHYS_SKILL_DMG, damage, target, skill);
		}
		
		// Damage bonus
		damage = attacker.calcStat(Stats.PHYS_SKILL_DMG, damage, target, skill);
		
		return damage < 1 ? 1. : damage;
	}
	
	/**
	 * Calculated damage caused by ATTACK of attacker on target, called separately for each weapon, if dual-weapon is used.
	 * @param attacker player or NPC that makes ATTACK
	 * @param target player or NPC, target of ATTACK
	 * @param skill
	 * @param shld
	 * @param crit if the ATTACK have critical success
	 * @param dual if dual weapon is used
	 * @param ss if weapon item was charged by soulshot
	 * @return damage points
	 */
	public final double calcPhysDam(L2Character attacker, L2Character target, L2Skill skill, boolean shld, boolean crit, boolean dual, boolean ss)
	{
		double damage = attacker.getPAtk(target);
		double defence = target.getPDef(attacker);
		if (ss)
		{
			damage *= 2;
		}
		if (skill != null)
		{
			damage += skill.getPower(attacker);
		}

		// defence modifier depending of the attacker weapon
		L2Weapon weapon = attacker.getActiveWeaponItem();
		Stats stat = null;
		if (weapon != null)
		{
			switch (weapon.getItemType())
			{
				case BOW:
					stat = Stats.BOW_WPN_VULN;
					break;
				case BLUNT:
					stat = Stats.BLUNT_WPN_VULN;
					break;
				case DAGGER:
					stat = Stats.DAGGER_WPN_VULN;
					break;
				case DUAL:
					stat = Stats.DUAL_WPN_VULN;
					break;
				case DUALFIST:
					stat = Stats.DUALFIST_WPN_VULN;
					break;
				case ETC:
					stat = Stats.ETC_WPN_VULN;
					break;
				case FIST:
					stat = Stats.FIST_WPN_VULN;
					break;
				case POLE:
					stat = Stats.POLE_WPN_VULN;
					break;
				case SWORD:
					stat = Stats.SWORD_WPN_VULN;
					break;
			}
		}
		
		if (shld)
		{
			if ((100 - Config.ALT_PERFECT_SHLD_BLOCK) < Rnd.get(100))
			{
				target.sendPacket(new SystemMessage(SystemMessage.YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS));
				return 1;
			}
			defence += target.getShldDef();
			target.sendPacket(new SystemMessage(SystemMessage.SHIELD_DEFENCE_SUCCESSFULL));
		}
		
		if (crit)
		{
			// Finally retail like formula
			damage = 2 * attacker.calcStat(Stats.CRITICAL_DAMAGE, 1, target, skill) * ((70 * damage) / defence);
			
			// Crit dmg add is almost useless in normal hits...
			damage += ((attacker.calcStat(Stats.CRITICAL_DAMAGE_ADD, 0, target, skill) * 70) / defence);
		}
		else
		{
			damage = (70 * damage) / defence;
		}
		
		if (stat != null)
		{
			// get the vulnerability due to skills (buffs, passives, toggles, etc)
			damage = target.calcStat(stat, damage, target, skill);
			if (target instanceof L2NpcInstance)
			{
				// get the natural vulnerability for the template
				damage *= ((L2NpcInstance) target).getTemplate().getVulnerability(stat);
			}
		}
		
		damage += (Rnd.nextDouble() * damage) / 10;
		
		if (attacker instanceof L2NpcInstance)
		{
			int raceId = ((L2NpcInstance) attacker).getTemplate().race;
			// Skill Race : Undead
			if (raceId == 4290)
			{
				damage /= attacker.getPDefUndead(target);
			}
			
			// For amulets of Valakas
			if (((L2NpcInstance) attacker).getNpcId() == 12899)
			{
				damage = target.calcStat(Stats.VALAKAS_VULN, damage, target, skill);
			}
		}
		if (target instanceof L2NpcInstance)
		{
			int raceId = ((L2NpcInstance) target).getTemplate().race;
			// Skill Race : Undead
			if (raceId == 4290)
			{
				damage *= attacker.getPAtkUndead(target);
			}
			// Skill Race : Beast
			if (raceId == 4292)
			{
				damage *= attacker.getPAtkMonsters(target);
			}
			// Skill Race : Animal
			if (raceId == 4293)
			{
				damage *= attacker.getPAtkAnimals(target);
			}
			// Skill Race : Plant
			if (raceId == 4294)
			{
				damage *= attacker.getPAtkPlants(target);
			}
			// Skill Race : Dragon
			if (raceId == 4299)
			{
				damage *= attacker.getPAtkDragons(target);
			}
			// Skill Race : Giant
			if (raceId == 4300)
			{
				damage *= attacker.getPAtkGiants(target);
			}
			// Skill Race : Bug
			if (raceId == 4301)
			{
				damage *= attacker.getPAtkInsects(target);
			}
			
			// Skill Race : Magic Creatures
			if (raceId == 4291)
			{
				damage *= attacker.getPAtkMCreatures(target);
			}
			
			// For amulets of Valakas
			if (((L2NpcInstance) target).getNpcId() == 12899)
			{
				damage *= attacker.calcStat(Stats.VALAKAS_PHYSICAL_DMG, 1, target, skill);
			}
		}
		
		if ((damage > 0) && (damage < 1))
		{
			damage = 1;
		}
		else if (damage < 0)
		{
			damage = 0;
		}
		
		// Damage bonus in pvp
		if ((attacker instanceof L2PlayableInstance) && (target instanceof L2PlayableInstance))
		{
			damage = attacker.calcStat(skill == null ? Stats.PVP_PHYSICAL_DMG : Stats.PVP_PHYS_SKILL_DMG, damage, target, skill);
		}
		
		// Damage bonus
		damage = attacker.calcStat(skill == null ? Stats.PHYSICAL_DMG : Stats.PHYS_SKILL_DMG, damage, target, skill);
		
		return damage;
	}
	
	public final double calcMagicDam(L2Character attacker, L2Character target, L2Skill skill, boolean ss, boolean bss, boolean mcrit)
	{
		double mAtk = attacker.getMAtk(target, skill);
		double mDef = target.getMDef(attacker, skill);
		if (bss)
		{
			mAtk *= 4;
		}
		else if (ss)
		{
			mAtk *= 2;
		}
		
		double damage = ((91 * Math.sqrt(mAtk)) / mDef) * skill.getPower(attacker) * calcElementalVulnerability(target, skill);
		
		// Failure calculation
		if (Config.ALT_GAME_MAGICFAILURES && !calcMagicSuccess(attacker, target, skill))
		{
			if (attacker instanceof L2PcInstance)
			{
				if (calcMagicSuccess(attacker, target, skill) && ((target.getLevel() - attacker.getLevel()) <= 9))
				{
					if (skill.getSkillType() == SkillType.DRAIN)
					{
						attacker.sendPacket(new SystemMessage(SystemMessage.DRAIN_HALF_SUCCESFUL));
					}
					else
					{
						attacker.sendPacket(new SystemMessage(SystemMessage.ATTACK_FAILED));
					}
					
					damage /= 2;
				}
				else
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
					sm.addString(target.getName());
					sm.addSkillName(skill);
					attacker.sendPacket(sm);

					damage = 1;
				}
			}

			if (target instanceof L2PcInstance)
			{
				if (skill.getSkillType() == SkillType.DRAIN)
				{
					SystemMessage sm = new SystemMessage(SystemMessage.RESISTED_S1_DRAIN);
					sm.addString(attacker.getName());
					target.sendPacket(sm);
				}
				else
				{
					SystemMessage sm = new SystemMessage(SystemMessage.RESISTED_S1_MAGIC);
					sm.addString(attacker.getName());
					target.sendPacket(sm);
				}
			}
		}
		else if (mcrit)
		{
			damage *= 4;
		}

		// Damage bonus in pvp
		if ((attacker instanceof L2PlayableInstance) && (target instanceof L2PlayableInstance))
		{
			damage = attacker.calcStat(skill.isMagic() ? Stats.PVP_MAGICAL_DMG : Stats.PVP_PHYS_SKILL_DMG, damage, target, skill);
		}
		
		// Damage bonus
		damage = attacker.calcStat(skill.isMagic() ? Stats.MAGICAL_DMG : Stats.PHYS_SKILL_DMG, damage, target, skill);
		
		return damage;
	}

	public final double calcMagicDam(L2CubicInstance attacker, L2Character target, L2Skill skill, boolean mcrit)
	{
		final L2PcInstance owner = attacker.getOwner();
		double mAtk = attacker.getMAtk();
		double mDef = target.getMDef(owner, skill);
		
		double damage = ((91 * Math.sqrt(mAtk)) / mDef) * skill.getPower() * calcElementalVulnerability(target, skill);
		
		// Failure calculation
		if (Config.ALT_GAME_MAGICFAILURES && !calcMagicSuccess(owner, target, skill))
		{
			if (calcMagicSuccess(owner, target, skill) && (target.getLevel() - skill.getMagicLevel()) <= 9)
			{
				if (skill.getSkillType() == SkillType.DRAIN)
				{
					owner.sendPacket(new SystemMessage(SystemMessage.DRAIN_HALF_SUCCESFUL));
				}
				else
				{
					owner.sendPacket(new SystemMessage(SystemMessage.ATTACK_FAILED));
				}
				
				damage /= 2;
			}
			else
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_UNAFFECTED_BY_S2);
				sm.addString(target.getName());
				sm.addSkillName(skill);
				owner.sendPacket(sm);

				damage = 1;
			}

			if (target instanceof L2PcInstance)
			{
				if (skill.getSkillType() == SkillType.DRAIN)
				{
					SystemMessage sm = new SystemMessage(SystemMessage.RESISTED_S1_DRAIN);
					sm.addString(owner.getName());
					target.sendPacket(sm);
				}
				else
				{
					SystemMessage sm = new SystemMessage(SystemMessage.RESISTED_S1_MAGIC);
					sm.addString(owner.getName());
					target.sendPacket(sm);
				}
			}
		}
		else if (mcrit)
		{
			damage *= 4;
		}

		return damage;
	}
	
	public final boolean calcBlow(L2Character activeChar, L2Character target, int chance)
	{
		return activeChar.calcStat(Stats.BLOW_RATE, chance * (1.0 + ((activeChar.getDEX() - 20) / 100)), target, null) > Rnd.get(100);
	}

	/**
	 * Returns true in case of critical hit
	 * @param rate
	 * @return
	 */
	public final boolean calcCrit(double rate)
	{
		return rate > Rnd.get(1000);
	}

	public final boolean calcMCrit(double mRate)
	{
		return mRate > Rnd.get(1000);
	}

	/**
	 * Returns true in case when ATTACK is canceled due to hit.
	 * @param target
	 * @param dmg
	 * @return
	 */
	public final boolean calcAtkBreak(L2Character target, double dmg)
	{
		if (target.isRaid() || target.isInvul())
		{
			return false; // No attack break
		}

		double init = 0;

		if (Config.ALT_GAME_CANCEL_CAST && target.isCastingNow())
		{
			init = 15;
		}
		if (Config.ALT_GAME_CANCEL_BOW && target.isAttackingNow())
		{
			L2Weapon wpn = target.getActiveWeaponItem();
			if ((wpn != null) && (wpn.getItemType() == L2WeaponType.BOW))
			{
				init = 15;
			}
		}

		if (init <= 0)
		{
			return false; // No attack break
		}

		// Chance of break is higher with higher dmg
		init += Math.sqrt(13 * dmg);

		// Chance is affected by target MEN
		init -= ((BaseStats.MEN.calcBonus(target) * 100) - 100);

		// Calculate all modifiers for ATTACK_CANCEL
		double rate = target.calcStat(Stats.ATTACK_CANCEL, init, null, null);

		// Adjust the rate to be between 1 and 99
		if (rate > 99)
		{
			rate = 99;
		}
		else if (rate < 1)
		{
			rate = 1;
		}

		return Rnd.get(100) < rate;
	}

	/**
	 * Calculate delay (in milliseconds) before next ATTACK
	 * @param attacker
	 * @param target
	 * @param rate
	 * @return
	 */
	public final int calcPAtkSpd(L2Character attacker, L2Character target, double rate)
	{
		// measured Oct 2006 by Tank6585, formula by Sami
		if (rate < 2)
		{
			return 2700;
		}
		return (int) (470000 / rate);
	}

	/**
	 * Calculate delay (in milliseconds) for skills cast
	 * @param attacker
	 * @param target
	 * @param skill
	 * @param skillTime
	 * @return
	 */
	public final int calcMAtkSpd(L2Character attacker, L2Character target, L2Skill skill, double skillTime)
	{
		if (skill.isMagic())
		{
			return (int) ((skillTime * 333) / attacker.getMAtkSpd());
		}
		return (int) ((skillTime * 333) / attacker.getPAtkSpd());
	}

	/**
	 * Calculate delay (in milliseconds) for skills cast
	 * @param attacker
	 * @param skill
	 * @param skillTime
	 * @return
	 */
	public final int calcMAtkSpd(L2Character attacker, L2Skill skill, double skillTime)
	{
		if (skill.isMagic())
		{
			return (int) ((skillTime * 333) / attacker.getMAtkSpd());
		}
		return (int) ((skillTime * 333) / attacker.getPAtkSpd());
	}

	/**
	 * Formula based on http://l2p.l2wh.com/nonskillattacks.html
	 * @param attacker
	 * @param target
	 * @return {@code true} if hit missed (target evaded), {@code false} otherwise.
	 */
	public boolean calcHitMiss(L2Character attacker, L2Character target)
	{
		int chance = (80 + (2 * (attacker.getAccuracy() - target.getEvasionRate(attacker)))) * 10;
		
		// Get additional bonus from the conditions when you are attacking
		chance *= attacker.getHitMissConditionBonus(target);
		
		chance = Math.max(chance, 200);
		chance = Math.min(chance, 980);

		return chance < Rnd.get(1000);
	}
	
	/**
	 * Returns true if shield defence successful
	 * @param attacker
	 * @param target
	 * @return
	 */
	public boolean calcShldUse(L2Character attacker, L2Character target)
	{
		L2Weapon at_weapon = attacker.getActiveWeaponItem();
		double shldRate = target.calcStat(Stats.SHIELD_RATE, 0, attacker, null) * BaseStats.DEX.calcBonus(target);
		if (shldRate == 0.0)
		{
			return false;
		}
		int degreeside = (int) target.calcStat(Stats.SHIELD_DEFENCE_ANGLE, 0, attacker, null) + 120;
		if ((degreeside < 360) && (!target.isFacing(attacker, degreeside)))
		{
			return false;
		}

		// if attacker use bow and target wear shield, shield block rate is multiplied by 1.3 (30%)
		if ((at_weapon != null) && (at_weapon.getItemType() == L2WeaponType.BOW))
		{
			shldRate *= 1.3;
		}
		return shldRate > Rnd.get(100);
	}

	public double calcSkillVulnerability(L2Character target, L2Skill skill)
	{
		double multiplier = 1; // initialize...

		// Get the skill type to calculate its effect in function of base stats
		// of the L2Character target
		if (skill != null)
		{
			// first, get the natural template vulnerability values for the target
			Stats stat = skill.getStat();
			if (stat != null)
			{
				switch (stat)
				{
					case AGGRESSION:
						multiplier *= target.getTemplate().baseAggressionVuln;
						break;
					case BLEED:
						multiplier *= target.getTemplate().baseBleedVuln;
						break;
					case POISON:
						multiplier *= target.getTemplate().basePoisonVuln;
						break;
					case STUN:
						multiplier *= target.getTemplate().baseStunVuln;
						break;
					case ROOT:
						multiplier *= target.getTemplate().baseRootVuln;
						break;
					case MOVEMENT:
						multiplier *= target.getTemplate().baseMovementVuln;
						break;
					case CONFUSION:
						multiplier *= target.getTemplate().baseConfusionVuln;
						break;
					case SLEEP:
						multiplier *= target.getTemplate().baseSleepVuln;
						break;
				}
			}

			// Finally, calculate skilltype vulnerabilities
			SkillType type = skill.getSkillType();

			// Used mostly for additional effects on PDAM skills (like STUN, SHOCK...)
			if (skill.getEffectType() != null)
			{
				type = skill.getEffectType();
			}

			if (type != null)
			{
				switch (type)
				{
					case BLEED:
						multiplier = target.calcStat(Stats.BLEED_VULN, multiplier, target, null);
						break;
					case POISON:
						multiplier = target.calcStat(Stats.POISON_VULN, multiplier, target, null);
						break;
					case STUN:
						multiplier = target.calcStat(Stats.STUN_VULN, multiplier, target, null);
						break;
					case PARALYZE:
						multiplier = target.calcStat(Stats.PARALYZE_VULN, multiplier, target, null);
						break;
					case ROOT:
						multiplier = target.calcStat(Stats.ROOT_VULN, multiplier, target, null);
						break;
					case SLEEP:
						multiplier = target.calcStat(Stats.SLEEP_VULN, multiplier, target, null);
						break;
					case MUTE:
					case FEAR:
					case AGGREDUCE_CHAR:
						multiplier = target.calcStat(Stats.DERANGEMENT_VULN, multiplier, target, null);
						break;
					case CONFUSION:
					case CONFUSE_MOB_ONLY:
						multiplier = target.calcStat(Stats.CONFUSION_VULN, multiplier, target, null);
						break;
					case DEBUFF:
					case WEAKNESS:
						multiplier = target.calcStat(Stats.DEBUFF_VULN, multiplier, target, null);
						break;
					case CANCEL:
					case MAGE_BANE:
					case WARRIOR_BANE:
						multiplier = target.calcStat(Stats.CANCEL_VULN, multiplier, target, null);
						break;
				}
			}
		}
		return multiplier;
	}

	public double calcElementalVulnerability(L2Character target, L2Skill skill)
	{
		double multiplier = 1; // initialize...

		// Get the skill type to calculate its effect in function of base stats
		// of the L2Character target
		if (skill != null)
		{
			// first, get the natural template vulnerability values for the target
			Stats stat = skill.getStat();
			if (stat != null)
			{
				switch (stat)
				{
					case FIRE:
						multiplier *= target.getTemplate().baseFireVuln;
						break;
					case WIND:
						multiplier *= target.getTemplate().baseWindVuln;
						break;
					case WATER:
						multiplier *= target.getTemplate().baseWaterVuln;
						break;
					case EARTH:
						multiplier *= target.getTemplate().baseEarthVuln;
						break;
					case HOLY:
						multiplier *= target.getTemplate().baseHolyVuln;
						break;
					case DARK:
						multiplier *= target.getTemplate().baseDarkVuln;
						break;
				}
			}
			
			// Next, calculate the elemental vulnerabilities
			switch (skill.getElement())
			{
				case L2Skill.ELEMENT_EARTH:
					multiplier = target.calcStat(Stats.EARTH_VULN, multiplier, target, skill);
					break;
				case L2Skill.ELEMENT_FIRE:
					multiplier = target.calcStat(Stats.FIRE_VULN, multiplier, target, skill);
					break;
				case L2Skill.ELEMENT_WATER:
					multiplier = target.calcStat(Stats.WATER_VULN, multiplier, target, skill);
					break;
				case L2Skill.ELEMENT_WIND:
					multiplier = target.calcStat(Stats.WIND_VULN, multiplier, target, skill);
					break;
				case L2Skill.ELEMENT_HOLY:
					multiplier = target.calcStat(Stats.HOLY_VULN, multiplier, target, skill);
					break;
				case L2Skill.ELEMENT_DARK:
					multiplier = target.calcStat(Stats.DARK_VULN, multiplier, target, skill);
					break;
			}
		}
		return multiplier;
		
	}
	
	public static double calcSkillStatModifier(SkillType type, BaseStats saveVs, L2Character target)
	{
		if (saveVs == null)
		{
			switch (type)
			{
				case STUN:
				case BLEED:
					saveVs = BaseStats.CON;
					break;
				case POISON:
					saveVs = BaseStats.MEN;
					break;
				case SLEEP:
				case DEBUFF:
				case WEAKNESS:
				case ROOT:
				case MUTE:
				case FEAR:
				case CONFUSION:
				case CONFUSE_MOB_ONLY:
				case AGGREDUCE_CHAR:
				case PARALYZE:
					saveVs = BaseStats.WIT;
					break;
				default:
					return 1;
			}
		}
		
		double multiplier = 2 - Math.sqrt(saveVs.calcBonus(target));
		if (multiplier < 0)
		{
			multiplier = 0;
		}
		
		return multiplier;
	}
	
	public boolean calcSkillSuccess(L2Character attacker, L2Character target, L2Skill skill, boolean ss, boolean sps, boolean bss)
	{
		SkillType type = skill.getSkillType();
		int lvlDepend = skill.getLevelDepend();
		int value = (int) skill.getPower();
		
		// Used mostly for additional effects on PDAM skills (like STUN, SHOCK...)
		if (skill.getEffectType() != null)
		{
			type = skill.getEffectType();
			value = skill.getEffectPower();
		}
		
		// Fix for skills with Power = 0 or LevelDepend not set
		if (value == 0)
		{
			value = (type == SkillType.PARALYZE) ? 50 : (type == SkillType.FEAR) ? 40 : 80;
		}
		
		if (lvlDepend == 0)
		{
			lvlDepend = ((type == SkillType.PARALYZE) || (type == SkillType.FEAR)) ? 1 : 2;
		}
		
		double statmodifier = calcSkillStatModifier(type, skill.getSaveVs(), target);
		double resmodifier = calcSkillVulnerability(target, skill);
		
		int ssmodifier = 100;
		if (bss)
		{
			ssmodifier = 200;
		}
		else if (sps)
		{
			ssmodifier = 150;
		}
		else if (ss)
		{
			ssmodifier = 150;
		}
		
		int rate = (int) (value * statmodifier);
		
		if (skill.isMagic())
		{
			rate = (int) (rate * Math.pow((double) attacker.getMAtk(target, skill) / target.getMDef(attacker, skill), 0.2));
		}
		
		if (ssmodifier != 100)
		{
			if (rate > (10000 / (100 + ssmodifier)))
			{
				rate = 100 - (((100 - rate) * 100) / ssmodifier);
			}
			else
			{
				rate = (rate * ssmodifier) / 100;
			}
		}
		
		if (lvlDepend > 0)
		{
			double delta = 0;
			int attackerLvlmod = attacker.getLevel();
			int targetLvlmod = target.getLevel();
			
			if (attackerLvlmod >= 70)
			{
				attackerLvlmod = ((attackerLvlmod - 69) * 2) + 70;
			}
			if (targetLvlmod >= 70)
			{
				targetLvlmod = ((targetLvlmod - 69) * 2) + 70;
			}
			
			if (skill.getMagicLevel() == 0)
			{
				delta = attackerLvlmod - targetLvlmod;
			}
			else
			{
				delta = ((skill.getMagicLevel() + attackerLvlmod) / 2) - targetLvlmod;
			}
			
			double deltamod = 1;
			if ((delta + 3) < 0)
			{
				if (delta <= -20)
				{
					deltamod = 0.05;
				}
				else
				{
					deltamod = 1 - ((-1) * (delta / 20));
					if (deltamod >= 1)
					{
						deltamod = 0.05;
					}
				}
			}
			else
			{
				deltamod = 1 + ((delta + 3) / 75);
			}
			
			if (deltamod < 0)
			{
				deltamod *= -1;
			}
			
			rate *= deltamod;
		}
		
		if (rate > 99)
		{
			rate = 99;
		}
		else if (rate < 1)
		{
			rate = 1;
		}
		
		// Finally apply resists.
		rate *= resmodifier;
		
		if (Config.DEVELOPER)
		{
			_log.info(skill.getName() + ": " + value + ", " + statmodifier + ", " + resmodifier + ", " 
				+ ((int) (Math.pow((double) attacker.getMAtk(target, skill) / target.getMDef(attacker, skill), 0.2) * 100) - 100) 
				+ ", " + ssmodifier + " ==> " + rate);
		}
		
		return (Rnd.get(100) < rate);
	}
	
	public boolean calcCubicSkillSuccess(L2CubicInstance attacker, L2Character target, L2Skill skill)
	{
		SkillType type = skill.getSkillType();
		int lvlDepend = skill.getLevelDepend();
		int value = (int) skill.getPower();
		
		// Used mostly for additional effects on PDAM skills (like STUN, SHOCK...)
		if (skill.getEffectType() != null)
		{
			type = skill.getEffectType();
			value = skill.getEffectPower();
		}
		
		// Fix for skills with Power = 0 or LevelDepend not set
		if (value == 0)
		{
			value = (type == SkillType.PARALYZE) ? 50 : (type == SkillType.FEAR) ? 40 : 80;
		}
		
		if (lvlDepend == 0)
		{
			lvlDepend = ((type == SkillType.PARALYZE) || (type == SkillType.FEAR)) ? 1 : 2;
		}

		double statmodifier = calcSkillStatModifier(type, skill.getSaveVs(), target);
		double resmodifier = calcSkillVulnerability(target, skill);
		
		int rate = (int) ((value * statmodifier) * resmodifier);
		if (skill.isMagic())
		{
			rate += (int) (Math.pow((double) attacker.getMAtk() / target.getMDef(attacker.getOwner(), skill), 0.2) * 100) - 100;
		}
		
		// lvl modifier.
		if (lvlDepend > 0)
		{
			double delta = 0;
			int attackerLvlmod = attacker.getOwner().getLevel();
			int targetLvlmod = target.getLevel();
			
			if (attackerLvlmod >= 70)
			{
				attackerLvlmod = ((attackerLvlmod - 69) * 2) + 70;
			}
			if (targetLvlmod >= 70)
			{
				targetLvlmod = ((targetLvlmod - 69) * 2) + 70;
			}
			
			if (skill.getMagicLevel() == 0)
			{
				delta = attackerLvlmod - targetLvlmod;
			}
			else
			{
				delta = ((skill.getMagicLevel() + attackerLvlmod) / 2) - targetLvlmod;
			}
			
			double deltamod = 1;
			if (delta + 3 < 0)
			{
				if (delta <= -20)
				{
					deltamod = 0.05;
				}
				else
				{
					deltamod = 1 - ((-1) * (delta / 20));
					if (deltamod >= 1)
					{
						deltamod = 0.05;
					}
				}
			}
			else
			{
				deltamod = 1 + ((delta + 3) / 75);
			}
				
			if (deltamod < 0)
			{
				deltamod *= -1;
			}
			
			rate *= deltamod;
		}

		if (rate > 99)
		{
			rate = 99;
		}
		else if (rate < 1)
		{
			rate = 1;
		}
		
		if (Config.DEVELOPER)
		{
			_log.info(skill.getName() + ": " + value + ", " + statmodifier + ", " + resmodifier + ", "
				+ ((int) (Math.pow((double) attacker.getMAtk() / target.getMDef(attacker.getOwner(), skill), 0.2) * 100) - 100) + " ==> " + rate);
		}
		return (Rnd.get(100) < rate);
	}
	
	public boolean calcMagicSuccess(L2Character attacker, L2Character target, L2Skill skill)
	{
		double lvlDifference = (target.getLevel() - (skill.getMagicLevel() > 0 ? skill.getMagicLevel() : attacker.getLevel()));
		int rate = Math.round((float) (Math.pow(1.3, lvlDifference) * 100));
		
		return (Rnd.get(10000) > rate);
	}
	
	public boolean calculateUnlockChance(L2Skill skill)
	{
		int level = skill.getLevel();
		int chance = 0;
		switch (level)
		{
			case 1:
				chance = 30;
				break;
			case 2:
				chance = 50;
				break;
			case 3:
				chance = 75;
				break;
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
				chance = 100;
				break;
		}
		
		if (Rnd.get(100) > chance)
		{
			return false;
		}
		
		return true;
	}
	
	public double calculateSkillResurrectRestorePercent(double baseRestorePercent, L2Character caster)
	{
		double restorePercent = baseRestorePercent;
		double modifier = BaseStats.WIT.calcBonus(caster);
		
		if ((restorePercent != 100) && (restorePercent != 0))
		{
			restorePercent = baseRestorePercent * modifier;
			
			if ((restorePercent - baseRestorePercent) > 20.0)
			{
				restorePercent = baseRestorePercent + 20.0;
			}
		}
		
		if (restorePercent > 100)
		{
			restorePercent = 100;
		}
		if (restorePercent < baseRestorePercent)
		{
			restorePercent = baseRestorePercent;
		}
		
		return restorePercent;
	}
	
	public boolean calculateSkillReflect(L2Skill skill, L2Character effector, L2Character effected)
	{
		if (effector.isRaid())
		{
			return false;
		}
		
		double reflect = 0;
		
		if (skill.isMagic())
		{
			reflect = effected.calcStat(Stats.REFLECT_SKILL_MAGIC, 0, effector, null);
		}
		else
		{
			reflect = effected.calcStat(Stats.REFLECT_SKILL_PHYSIC, 0, effector, null);
		}
		
		return (Rnd.get(100) < reflect);
		
	}
	
	public void calcLethalStrike(L2Character activeChar, L2Character target, int magiclvl)
	{
		if (target.isRaid() || (target instanceof L2DoorInstance) || (target instanceof L2SiegeFlagInstance))
		{
			return;
		}
		
		double chance = 0;
		if (magiclvl > 0)
		{
			int delta = ((magiclvl + activeChar.getLevel()) / 2) - 1 - target.getLevel();

			// delta [-3,infinite)
			if (delta >= -3)
			{
				chance = (2 * ((double) activeChar.getLevel() / target.getLevel()));
			}
			// delta [-9, -3[
			else if ((delta < -3) && (delta >= -9))
			{
				chance = (-3) * (2 / (delta));
			}
			// delta [-infinite,-9[
			else
			{
				chance = 2 / 15;
			}
		}
		else
		{
			chance = (2 * ((double) activeChar.getLevel() / target.getLevel()));
		}
		
		if (Rnd.get(100) < chance)
		{
			if (target instanceof L2PcInstance)
			
			{
				((L2PcInstance) target).setCurrentCp(1);
				((L2PcInstance) target).setCurrentHp(1);
				
			}
			else
			{
				target.reduceCurrentHp(target.getCurrentHp() - 1, activeChar);
			}
			
			activeChar.sendPacket(new SystemMessage(1667));
		}
	}
	
	public boolean calcSkillMastery(L2Character actor, L2Skill sk)
	{
		if (sk.getSkillType() == SkillType.FISHING)
		{
			return false;
		}
		
		if (sk.isPotion())
		{
			return false;
		}
		
		double val = actor.getStat().calcStat(Stats.SKILL_MASTERY, 0, null, null);
		
		if (actor instanceof L2PcInstance)
		{
			if (((L2PcInstance) actor).isMageClass())
			{
				val *= BaseStats.INT.calcBonus(actor);
			}
			else
			{
				val *= BaseStats.STR.calcBonus(actor);
			}
		}
		
		return Rnd.get(100) < val;
	}
	
	public double calcFallDam(L2Character cha, int fallHeight)
	{
		if (!Config.ENABLE_FALLING_DAMAGE || (fallHeight < 0))
		{
			return 0;
		}
		
		final double damage = cha.calcStat(Stats.FALL, (fallHeight * cha.getMaxHp()) / 1000, null, null);
		return damage;
	}
	
	public double calcManaDam(L2Character attacker, L2Character target, L2Skill skill, boolean ss, boolean bss)
	{
		// Mana Drain = (SQR(M.Atk)*Power*(Target Max MP/97))/M.Def
		double mAtk = attacker.getMAtk(target, skill);
		double mDef = target.getMDef(attacker, skill);
		double mp = target.getMaxMp();
		
		if (bss)
		{
			mAtk *= 4;
		}
		else if (ss)
		{
			mAtk *= 2;
		}
		
		double damage = (Math.sqrt(mAtk) * skill.getPower(attacker) * (mp / 97)) / mDef;
		return damage;
	}
	
	private static class SingletonHolder
	{
		protected static final Formulas _instance = new Formulas();
	}
}