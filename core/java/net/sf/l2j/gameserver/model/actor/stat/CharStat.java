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
package net.sf.l2j.gameserver.model.actor.stat;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.skills.Calculator;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.templates.L2WeaponType;

public class CharStat
{
    // =========================================================
    // Data Field
    private L2Character _activeChar;
    private long _exp = Experience.LEVEL[Config.STARTING_LEVEL];
    private int _sp = 0;
    private byte _level = Config.STARTING_LEVEL;

    // =========================================================
    // Constructor
    public CharStat(L2Character activeChar)
    {
        _activeChar = activeChar;
    }

    // =========================================================
    // Method - Public
    /**
     * Calculate the new value of the state with modifiers that will be applied on the targeted L2Character.<BR><BR>
     *
     * <B><U> Concept</U> :</B><BR><BR>
     * A L2Character owns a table of Calculators called <B>_calculators</B>.
     * Each Calculator (a calculator per state) own a table of Func object.
     * A Func object is a mathematical function that permit to calculate the modifier of a state (ex : REGENERATE_HP_RATE...) : <BR><BR>
     *
     * FuncAtkAccuracy -> Math.sqrt(_player.getDEX())*6+_player.getLevel()<BR><BR>
     *
     * When the calc method of a calculator is launched, each mathematical function is called according to its priority <B>_order</B>.
     * Indeed, Func with lowest priority order is executed first and Funcs with the same order are executed in unspecified order.
     * The result of the calculation is stored in the value property of an Env class instance.<BR><BR>
     *
     * @param stat The stat to calculate the new value with modifiers
     * @param init The initial value of the stat before applying modifiers
     * @param target The L2Charcater whose properties will be used in the calculation (ex : CON, INT...)
     * @param skill The L2Skill whose properties will be used in the calculation (ex : Level...)
     * @return 
     *
     */
    public final double calcStat(Stats stat, double init, L2Character target, L2Skill skill)
    {
        if (_activeChar == null)
            return init;

        int id = stat.ordinal();

        Calculator c = _activeChar.getCalculators()[id];

        // If no Func object found, no modifier is applied
        if (c == null || c.size() == 0)
            return init;

        // Create and init an Env object to pass parameters to the Calculator
        Env env = new Env();
        env.player = _activeChar;
        env.target = target;
        env.skill = skill;
        env.value = init;

        // Launch the calculation
        c.calc(env);
        // avoid some troubles with negative stats (some stats should never be negative)
        if (env.value < 1 &&
                ((stat == Stats.MAX_HP) ||
                (stat == Stats.MAX_MP) ||
                (stat == Stats.MAX_CP) ||
                (stat == Stats.MAGIC_DEFENCE) ||
                (stat == Stats.POWER_DEFENCE) ||
                (stat == Stats.POWER_ATTACK) ||
                (stat == Stats.MAGIC_ATTACK) ||
                (stat == Stats.POWER_ATTACK_SPEED) ||
                (stat == Stats.MAGIC_ATTACK_SPEED) ||
                (stat == Stats.SHIELD_DEFENCE) ||
                (stat == Stats.STAT_CON) ||
                (stat == Stats.STAT_DEX) ||
                (stat == Stats.STAT_INT) ||
                (stat == Stats.STAT_MEN) ||
                (stat == Stats.STAT_STR) ||
                (stat == Stats.STAT_WIT)))
        {
            env.value = 1;
        }
        
        return env.value;
    }

    // =========================================================
    // Method - Private

    // =========================================================
    // Property - Public
    /** 
     * Return the Accuracy (base+modifier) of the L2Character. 
     * @return
     */
    public int getAccuracy()
    {
        if (_activeChar == null)
            return 0;

        return (int) Math.round(calcStat(Stats.ACCURACY_COMBAT, 0, null, null));
    }

    public L2Character getActiveChar()
    {
        return _activeChar;
    }
    
    /** 
     * Return the Attack Speed multiplier (base+modifier) of the L2Character to get proper animations. 
     * @return 
     */
    public final float getAttackSpeedMultiplier()
    {
        if (_activeChar == null)
            return 1;

        return (float) ((1.1) * getPAtkSpd() / _activeChar.getTemplate().basePAtkSpd);
    }

    /** 
     * Return the CON of the L2Character (base+modifier). 
     * @return 
     */
    public final int getCON()
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.STAT_CON, _activeChar.getTemplate().baseCON, null, null);
    }

    /** 
     * Return the Critical Damage rate (base+modifier) of the L2Character. 
     * @param target 
     * @param init 
     * @return 
     */
    public final double getCriticalDmg(L2Character target, double init) { return calcStat(Stats.CRITICAL_DAMAGE, init, target, null); }

    /** 
     * Return the Critical Hit rate (base+modifier) of the L2Character. 
     * @param target 
     * @param skill 
     * @return
     */
    public int getCriticalHit(L2Character target, L2Skill skill)
    {
        if (_activeChar == null)
            return 1;

        int criticalHit = (int) Math.round(calcStat(Stats.CRITICAL_RATE, _activeChar.getTemplate().baseCritRate, target, skill));

        if (criticalHit > Config.MAX_PCRIT_RATE)
            criticalHit = Config.MAX_PCRIT_RATE;

        return criticalHit;
    }

    /** 
     * Return the DEX of the L2Character (base+modifier). 
     * @return
     */
    public final int getDEX()
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.STAT_DEX, _activeChar.getTemplate().baseDEX, null, null);
    }

    /** 
     * Return the attack evasion rate (base+modifier) of the L2Character. 
     * @param target 
     * @return
     */
    public int getEvasionRate(L2Character target)
    {
        if (_activeChar == null)
            return 1;

        return (int) Math.round(calcStat(Stats.EVASION_RATE, 0, target, null));
    }

    public long getExp()
    {
        return _exp;
    }

    public void setExp(long value)
    {
        _exp = value;
    }

    /** 
     * Return the INT of the L2Character (base+modifier). 
     * @return
     */
    public int getINT()
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.STAT_INT, _activeChar.getTemplate().baseINT, null, null);
    }

    public byte getLevel()
    {
        return _level;
    }

    public void setLevel(byte value)
    {
        _level = value;
    }

    /** 
     * Return the Magical Attack range (base+modifier) of the L2Character. 
     * @param skill 
     * @return
     */
    public final int getMagicalAttackRange(L2Skill skill)
    {
        if (skill != null)
            return (int)calcStat(Stats.MAGIC_ATTACK_RANGE, skill.getCastRange(), null, skill);

        if (_activeChar == null)
            return 1;

        return _activeChar.getTemplate().baseAtkRange;
    }

    public int getMaxCp()
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.MAX_CP, _activeChar.getTemplate().baseCpMax, null, null);
    }

    public int getMaxHp()
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.MAX_HP, _activeChar.getTemplate().baseHpMax, null, null);
    }

    public int getMaxMp()
    {
        if (_activeChar == null)
            return 1;

        float baseMpMax = _activeChar.isBuffer() && Config.BUFFER_BASE_MP_MAX > 0 ? Config.BUFFER_BASE_MP_MAX : _activeChar.getTemplate().baseMpMax;
        return (int)calcStat(Stats.MAX_MP, baseMpMax, null, null);
    }

    /**
     * Return the MAtk (base+modifier) of the L2Character for a skill used in function of abnormal effects in progress.<BR><BR>
     *
     * <B><U> Example of use </U> :</B><BR><BR>
     * <li> Calculate Magic damage </li><BR><BR>
     *
     * @param target The L2Character targeted by the skill
     * @param skill The L2Skill used against the target
     * @return 
     */
    public int getMAtk(L2Character target, L2Skill skill)
    {
        if (_activeChar == null)
            return 1;

        float bonus = 1;
        if (_activeChar.isRaid())
        {
        	bonus = Config.RAID_ATK_MULTIPLIER;
        }
        
        if (Config.CHAMPION_ENABLE && _activeChar.isChampion())
        {
            bonus = Config.CHAMPION_ATK;
        }

        // Get the base MAtk of the L2Character
        double attack = _activeChar.getTemplate().baseMAtk * bonus;

        // Calculate modifiers Magic Attack
        return (int)calcStat(Stats.MAGIC_ATTACK, attack, target, skill);
    }

    /** 
     * Return the MAtk Speed (base+modifier) of the L2Character. 
     * @return
     */
    public int getMAtkSpd()
    {
        if (_activeChar == null)
            return 1;

        float bonus = 1;
        if (Config.CHAMPION_ENABLE && _activeChar.isChampion())
        {
            bonus = Config.CHAMPION_SPD_ATK;
        }

        double val = calcStat(Stats.MAGIC_ATTACK_SPEED, _activeChar.getTemplate().baseMAtkSpd * bonus, null, null);
        if (_activeChar instanceof L2PcInstance)
        {
            if (val > Config.MAX_MATK_SPEED && !((L2PcInstance)_activeChar).isGM())
                val = Config.MAX_MATK_SPEED;
        }
        return (int)val;
    }

    /** 
     * Return the Magic Critical Hit rate (base+modifier) of the L2Character. 
     * @param target 
     * @param skill 
     * @return 
     */
    public final int getMCriticalHit(L2Character target, L2Skill skill)
    {
        if (_activeChar == null)
            return 1;

        double mrate = calcStat(Stats.MCRITICAL_RATE, _activeChar.getTemplate().baseMCritRate, target, skill);

        if (mrate > Config.MAX_MCRIT_RATE)
            mrate = Config.MAX_MCRIT_RATE;

        return (int)mrate;
    }

    /**
     * Return the MDef (base+modifier) of the L2Character against a skill in function of abnormal effects in progress.<BR><BR>
     *
     * <B><U> Example of use </U> :</B><BR><BR>
     * <li> Calculate Magic damage </li><BR><BR>
     *
     * @param target The L2Character targeted by the skill
     * @param skill The L2Skill used against the target
     * @return 
     */
    public int getMDef(L2Character target, L2Skill skill)
    {
        if (_activeChar == null)
            return 1;

        // Get the base MAtk of the L2Character
        double defense = _activeChar.getTemplate().baseMDef;

        // Calculate modifier for Raid Bosses
        if (_activeChar.isRaid())
        {
            defense *= Config.RAID_DEF_MULTIPLIER;
        }

        // Calculate modifiers Magic Attack
        return (int)calcStat(Stats.MAGIC_DEFENCE, defense, target, skill);
    }

    /** 
     * Return the MEN of the L2Character (base+modifier). 
     * @return
     */
    public final int getMEN()
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.STAT_MEN, _activeChar.getTemplate().baseMEN, null, null);
    }

    public int getBaseMoveSpeed()
    {
    	if (_activeChar == null)
            return 0;
    	
    	return _activeChar.isRunning() ? getBaseRunSpeed() : getBaseWalkSpeed();
    }
    
    public int getBaseRunSpeed()
    {
    	if (_activeChar == null)
            return 0;
    	
    	return _activeChar.getTemplate().baseRunSpd;
    }
    
    public int getBaseWalkSpeed()
    {
    	if (_activeChar == null)
            return 0;
    	
    	return _activeChar.getTemplate().baseWalkSpd;
    }
    
    public final float getMovementSpeedMultiplier()
    {
        if (_activeChar == null)
            return 1;
        
        return getMoveSpeed() / getBaseMoveSpeed();
    }

    /** 
     * Return the RunSpeed (base+modifier) or WalkSpeed (base+modifier) of the L2Character in function of the movement type.
     * @return
     */
    public float getMoveSpeed()
    {
        if (_activeChar == null)
            return 0;
        
        // Do not proceed further
        int baseMoveSpeed = getBaseMoveSpeed();
        if (baseMoveSpeed <= 0)
        {
        	return 0;
        }

        return (float) calcStat(Stats.RUN_SPEED, baseMoveSpeed, null, null);
    }

    /** 
     * Return the MReuse rate (base+modifier) of the L2Character. 
     * @param skill 
     * @return
     */
    public final double getMReuseRate(L2Skill skill)
    {
        if (_activeChar == null)
            return 1;

        return calcStat(Stats.MAGIC_REUSE_RATE, _activeChar.getTemplate().baseMReuseRate, null, skill);
    }

    /** 
     * Return the PReuse rate (base+modifier) of the L2Character. 
     * @param skill 
     * @return 
     */
    public final double getPReuseRate(L2Skill skill)
    {
        if (_activeChar == null)
            return 1;

        return calcStat(Stats.PHYSICAL_REUSE_RATE, _activeChar.getTemplate().baseMReuseRate, null, skill);
    }

    /** 
     * Return the PAtk (base+modifier) of the L2Character. 
     * @param target 
     * @return 
     */
    public int getPAtk(L2Character target)
    {
        if (_activeChar == null)
            return 1;

        float bonus = 1;
        if (_activeChar.isRaid())
        {
        	bonus = Config.RAID_ATK_MULTIPLIER;
        }
        
        if (Config.CHAMPION_ENABLE && _activeChar.isChampion())
        {
            bonus = Config.CHAMPION_ATK;
        }
        
        return (int)calcStat(Stats.POWER_ATTACK, _activeChar.getTemplate().basePAtk * bonus, target, null);
    }

    /** 
     * Return the PAtk Modifier against animals. 
     * @param target 
     * @return 
     */
    public final double getPAtkAnimals(L2Character target)
    {
        return calcStat(Stats.PATK_ANIMALS, 1, target, null);
    }

    /** 
     * Return the PAtk Modifier against dragons. 
     * @param target 
     * @return 
     */
    public final double getPAtkDragons(L2Character target)
    {
        return calcStat(Stats.PATK_DRAGONS, 1, target, null);
    }

    /** 
     * Return the PAtk Modifier against insects. 
     * @param target 
     * @return 
     */
    public final double getPAtkInsects(L2Character target)
    {
        return calcStat(Stats.PATK_INSECTS, 1, target, null);
    }

    /** 
     * Return the PAtk Modifier against monsters. 
     * @param target 
     * @return 
     */
    public final double getPAtkMonsters(L2Character target)
    {
        return calcStat(Stats.PATK_MONSTERS, 1, target, null);
    }

    /** 
     * Return the PAtk Modifier against plants. 
     * @param target 
     * @return 
     */
    public final double getPAtkPlants(L2Character target)
    {
        return calcStat(Stats.PATK_PLANTS, 1, target, null);
    }

    /** 
     * Return the PAtk Modifier against giants. 
     * @param target 
     * @return 
     */
    public final double getPAtkGiants(L2Character target)
    {
        return calcStat(Stats.PATK_GIANTS, 1, target, null);
    }

    /** 
     * Return the PAtk Modifier against magic creatures. 
     * @param target 
     * @return 
     */
    public final double getPAtkMCreatures(L2Character target)
    {
        return calcStat(Stats.PATK_MCREATURES, 1, target, null);
    }
    /** 
     * Return the PAtk Modifier against undead. 
     * @param target 
     * @return 
     */
    public final double getPAtkUndead(L2Character target)
    {
        return calcStat(Stats.PATK_UNDEAD, 1, target, null);
    }

    public final double getPDefUndead(L2Character target)
    {
        return calcStat(Stats.PDEF_UNDEAD, 1, target, null);
    }

    /** 
     * Return the PAtk Speed (base+modifier) of the L2Character. 
     * @return 
     */
    public int getPAtkSpd()
    {
        if (_activeChar == null)
            return 1;

        float bonus = 1;
        if (Config.CHAMPION_ENABLE && _activeChar.isChampion())
        {
            bonus = Config.CHAMPION_SPD_ATK;
        }

        int val = (int) Math.round(calcStat(Stats.POWER_ATTACK_SPEED, _activeChar.getTemplate().basePAtkSpd * bonus, null, null));
        if (_activeChar instanceof L2PcInstance)
        {
            if (val > Config.MAX_PATK_SPEED && !((L2PcInstance)_activeChar).isGM())
                val = Config.MAX_PATK_SPEED;
        }
        return val;
    }

    /** 
     * Return the PDef (base+modifier) of the L2Character. 
     * @param target 
     * @return 
     */
    public int getPDef(L2Character target)
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.POWER_DEFENCE, (_activeChar.isRaid()) ? _activeChar.getTemplate().basePDef * Config.RAID_DEF_MULTIPLIER : _activeChar.getTemplate().basePDef, target, null);
    }

    /** 
     * Return the Physical Attack range (base+modifier) of the L2Character. 
     * @return 
     */
    public final int getPhysicalAttackRange()
    {
        if (_activeChar == null)
            return 1;

        // Polearm handled here for now.
        L2Weapon weaponItem = _activeChar.getActiveWeaponItem();
        if (weaponItem != null && weaponItem.getItemType() == L2WeaponType.POLE)
            return (int) calcStat(Stats.POWER_ATTACK_RANGE, 66, null, null);

        return (int)calcStat(Stats.POWER_ATTACK_RANGE, _activeChar.getTemplate().baseAtkRange, null, null);
    }

    /** 
     * Return the weapon reuse modifier. 
     * @param target 
     * @return 
     */
    public final double getWeaponReuseModifier(L2Character target)
    {
        return calcStat(Stats.ATK_REUSE, 1, target, null);
    }

    /** 
     * Return the ShieldDef rate (base+modifier) of the L2Character. 
     * @return 
     */
    public final int getShldDef()
    {
        return (int)calcStat(Stats.SHIELD_DEFENCE, 0, null, null);
    }

    public int getSp()
    {
        return _sp;
    }

    public void setSp(int value)
    {
        _sp = value;
    }

    /** 
     * Return the STR of the L2Character (base+modifier). 
     * @return 
     */
    public final int getSTR()
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.STAT_STR, _activeChar.getTemplate().baseSTR, null, null);
    }

    /** 
     * Return the WIT of the L2Character (base+modifier). 
     * @return 
     */
    public final int getWIT()
    {
        if (_activeChar == null)
            return 1;

        return (int)calcStat(Stats.STAT_WIT, _activeChar.getTemplate().baseWIT, null, null);
    }

    /** 
     * Return the mpConsume. 
     * @param skill 
     * @return 
     */
    public final int getMpConsume(L2Skill skill)
    {
        if (skill == null)
            return 1;

        double mpConsume = skill.getMpConsume();
        if (skill.getNextDanceMpCost() > 0 && _activeChar.getDanceCount() > 0)
            mpConsume += _activeChar.getDanceCount() * skill.getNextDanceMpCost();

        mpConsume = calcStat(Stats.MP_CONSUME, mpConsume, null, skill);
        if (skill.isDance())
        {
            return (int)calcStat(Stats.DANCE_MP_CONSUME_RATE, mpConsume, null, null);
        }
        else if (skill.isMagic())
        {
            return (int)calcStat(Stats.MAGICAL_MP_CONSUME_RATE, mpConsume, null, null);
        }

		return (int)calcStat(Stats.PHYSICAL_MP_CONSUME_RATE, mpConsume, null, null);
    }
    
    /** 
     * Return the mpInitialConsume. 
     * @param skill 
     * @return 
     */
    public final int getMpInitialConsume(L2Skill skill)
    {
        if (skill == null)
            return 1;

    	double mpConsume = calcStat(Stats.MP_CONSUME, skill.getMpInitialConsume(), null, skill);
    	if (skill.isDance())
        {
            return (int)calcStat(Stats.DANCE_MP_CONSUME_RATE, mpConsume, null, null);
        }
        else if (skill.isMagic())
        {
            return (int)calcStat(Stats.MAGICAL_MP_CONSUME_RATE, mpConsume, null, null);
        }

		return (int)calcStat(Stats.PHYSICAL_MP_CONSUME_RATE, mpConsume, null, null);
    }
}