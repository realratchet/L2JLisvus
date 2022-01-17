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
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.base.Experience;

public class PlayableStat extends CharStat
{
	// =========================================================
	// Data Field
	private int _oldMaxCp; // stats watch
	private int _oldMaxHp; // stats watch
	private int _oldMaxMp; // stats watch
	
	// =========================================================
	// Constructor
	public PlayableStat(L2PlayableInstance activeChar)
	{
		super(activeChar);
	}
	
	// =========================================================
	// Method - Public
	public boolean addExp(long value)
	{
		final byte minimumLevel = 1;
		final byte maximumLevel = getActiveChar() instanceof L2PetInstance ? Experience.PET_MAX_LEVEL : Config.MAX_PLAYER_LEVEL;
		
		if (value > 0 && getExp() == (getExpForLevel(maximumLevel) - 1))
		{
			return true;
		}
		
		final long minLvlExp = getExpForLevel(minimumLevel);
		if ((getExp() + value) < minLvlExp)
		{
			value = minLvlExp - getExp();
		}
		
		if (value == 0)
		{
			return false;
		}
		
		if ((getExp() + value) >= getExpForLevel(maximumLevel))
		{
			value = getExpForLevel(maximumLevel) - 1 - getExp();
		}
		
		setExp(getExp() + value);
		
		byte level = minimumLevel; // minimum level
		
		for (byte tmp = level; tmp <= maximumLevel; tmp++)
		{
			if (getExp() >= getExpForLevel(tmp))
			{
				continue;
			}
			level = --tmp;
			break;
		}
		
		if ((level != getLevel()) && (level >= minimumLevel))
		{
			addLevel((byte) (level - getLevel()));
		}
		
		return true;
	}
	
	public boolean removeExp(long value)
	{
		final byte minimumLevel = 1;
		final byte maximumLevel = getActiveChar() instanceof L2PetInstance ? Experience.PET_MAX_LEVEL : Config.MAX_PLAYER_LEVEL;
		
		final long minLvlExp = getExpForLevel(minimumLevel);
		if ((getExp() - value) < minLvlExp)
		{
			value = getExp() - minLvlExp;
		}
		
		if (value == 0)
		{
			return false;
		}
		
		setExp(getExp() - value);
		
		byte level = minimumLevel;
		for (byte tmp = level; tmp <= maximumLevel; tmp++)
		{
			if (getExp() >= getExpForLevel(tmp))
			{
				continue;
			}
			level = --tmp;
			break;
		}
		if ((level != getLevel()) && (level >= minimumLevel))
		{
			addLevel((byte) (level - getLevel()));
		}
		return true;
	}
	
	public boolean addExpAndSp(long addToExp, int addToSp)
	{
		boolean expAdded = false;
		boolean spAdded = false;
		if (addToExp >= 0)
		{
			expAdded = addExp(addToExp);
		}
		if (addToSp >= 0)
		{
			spAdded = addSp(addToSp);
		}
		
		return expAdded || spAdded;
	}
	
	public boolean removeExpAndSp(long removeExp, int removeSp)
	{
		boolean expRemoved = false;
		boolean spRemoved = false;
		if (removeExp > 0)
		{
			expRemoved = removeExp(removeExp);
		}
		if (removeSp > 0)
		{
			spRemoved = removeSp(removeSp);
		}
		
		return expRemoved || spRemoved;
	}
	
	public boolean addLevel(byte value)
	{
		final byte maximumLevel = getActiveChar() instanceof L2PetInstance ? Experience.PET_MAX_LEVEL : Config.MAX_PLAYER_LEVEL;
		if ((getLevel() + value) > (maximumLevel - 1))
		{
			if (getLevel() < (maximumLevel - 1))
			{
				value = (byte) (maximumLevel - 1 - getLevel());
			}
			else
			{
				return false;
			}
		}
		
		boolean levelIncreased = ((getLevel() + value) > getLevel());
		value += getLevel();
		setLevel(value);
		
		// Sync up exp with current level
		if ((getExp() >= getExpForLevel(getLevel() + 1)) || (getExpForLevel(getLevel()) > getExp()))
		{
			setExp(getExpForLevel(getLevel()));
		}
		
		if (!levelIncreased)
		{
			return false;
		}
		
		getActiveChar().getStatus().setCurrentHp(getActiveChar().getStat().getMaxHp());
		getActiveChar().getStatus().setCurrentMp(getActiveChar().getStat().getMaxMp());
		
		return true;
	}
	
	public boolean addSp(int value)
	{
		if (value < 0)
		{
			return false;
		}
		
		int currentSp = getSp();
		if (currentSp == Integer.MAX_VALUE)
		{
			return false;
		}
		
		if (currentSp > (Integer.MAX_VALUE - value))
		{
			value = Integer.MAX_VALUE - currentSp;
		}
		
		setSp(currentSp + value);
		return true;
	}
	
	public boolean removeSp(int value)
	{
		int currentSp = getSp();
		if (currentSp < value)
		{
			value = currentSp;
		}
		setSp(getSp() - value);
		return true;
	}
	
	public long getExpForLevel(int level)
	{
		return level;
	}
	
	@Override
	public int getMaxCp()
	{
		// Get the Max CP (base+modifier) of the L2PcInstance
		int val = super.getMaxCp();
		if (val != _oldMaxCp)
		{
			_oldMaxCp = val;
			
			// Launch a regen task if the new Max HP is higher than the old one
			if (getActiveChar().getStatus().getCurrentCp() != val)
			{
				getActiveChar().getStatus().setCurrentCp(getActiveChar().getStatus().getCurrentCp()); // trigger start of regeneration
			}
		}
		
		return val;
	}
	
	@Override
	public int getMaxHp()
	{
		// Get the Max HP (base+modifier) of the L2PcInstance
		int val = super.getMaxHp();
		if (val != _oldMaxHp)
		{
			_oldMaxHp = val;
			
			// Launch a regen task if the new Max HP is higher than the old one
			if (getActiveChar().getStatus().getCurrentHp() != val)
			{
				getActiveChar().getStatus().setCurrentHp(getActiveChar().getStatus().getCurrentHp()); // trigger start of regeneration
			}
		}
		
		return val;
	}
	
	@Override
	public int getMaxMp()
	{
		// Get the Max MP (base+modifier) of the L2PcInstance
		int val = super.getMaxMp();
		if (val != _oldMaxMp)
		{
			_oldMaxMp = val;
			
			// Launch a regen task if the new Max MP is higher than the old one
			if (getActiveChar().getStatus().getCurrentMp() != val)
			{
				getActiveChar().getStatus().setCurrentMp(getActiveChar().getStatus().getCurrentMp()); // trigger start of regeneration
			}
		}
		
		return val;
	}
	
	@Override
	public float getMoveSpeed()
	{
		float val = super.getMoveSpeed();
		// GMs should not be limited by this setting
		if ((getActiveChar() instanceof L2PcInstance) && ((L2PcInstance) getActiveChar()).isGM())
		{
			return val;
		}
		
		if (val > Config.MAX_RUN_SPEED)
		{
			val = Config.MAX_RUN_SPEED;
		}
		
		return val;
	}
	
	// =========================================================
	// Method - Private
	
	// =========================================================
	// Property - Public
	@Override
	public L2PlayableInstance getActiveChar()
	{
		return (L2PlayableInstance) super.getActiveChar();
	}
}