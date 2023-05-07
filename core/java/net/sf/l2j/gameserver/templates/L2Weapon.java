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
package net.sf.l2j.gameserver.templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.conditions.ConditionGameChance;
import net.sf.l2j.gameserver.skills.funcs.Func;
import net.sf.l2j.gameserver.skills.funcs.FuncTemplate;

/**
 * This class is dedicated to the management of weapons.
 * @version $Revision: 1.4.2.3.2.5 $ $Date: 2005/04/02 15:57:51 $
 */
public final class L2Weapon extends L2Item
{
	private final L2WeaponType _type;
	private final int _soulShotCount;
	private final int _spiritShotCount;
	private final int _rndDam;
	private final int _atkReuse;
	private final int _mpConsume;
	private final int _baseAttackRange;
	private final boolean _isMagical;

	private L2Skill _itemSkill = null; // for passive skill

	// Attached skills for Special Abilities
	protected L2Skill[] _skillsOnCast;
	protected L2Skill[] _skillsOnCrit;

	public L2Skill _castSkill;
	public int _castChance;
	public L2Skill _critSkill;
	public int _critChance;

	/**
	 * Constructor for Weapon.
	 * 
	 * @param set : StatsSet designating the set of couples (key,value) characterizing the armor
	 * @see L2Item constructor
	 */
	public L2Weapon(StatsSet set)
	{
		super(set);

		_type = set.getEnum("weapon_type", L2WeaponType.class, L2WeaponType.NONE);

		_type1 = L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE;
		_type2 = L2Item.TYPE2_WEAPON;

		_soulShotCount = set.getInteger("soulshots", 0);
		_spiritShotCount = set.getInteger("spiritshots", 0);
		_rndDam = set.getInteger("rnd_dam", 0);
		_atkReuse = set.getInteger("reuse_delay", 0);
		_mpConsume = set.getInteger("mp_consume", 0);
		_baseAttackRange = set.getInteger("attack_range", 40);
		_isMagical = set.getBool("is_magical", false);

		// int sId = set.getInteger("item_skill_id");
		// int sLv = set.getInteger("item_skill_lvl");
		// if ((sId > 0) && (sLv > 0))
		// {
		// 	_itemSkill = SkillTable.getInstance().getInfo(sId, sLv);
		// }

		// sId = set.getInteger("onCast_skill_id");
		// sLv = set.getInteger("onCast_skill_lvl");
		// int sCh = set.getInteger("onCast_skill_chance");
		// if ((sId > 0) && (sLv > 0) && (sCh > 0))
		// {
		// 	L2Skill skill = SkillTable.getInstance().getInfo(sId, sLv);
		// 	_castSkill = skill;
		// 	_castChance = sCh;
		// 	skill.attach(new ConditionGameChance(sCh), true);
		// 	attachOnCast(skill);
		// }

		// sId = set.getInteger("onCrit_skill_id");
		// sLv = set.getInteger("onCrit_skill_lvl");
		// sCh = set.getInteger("onCrit_skill_chance");
		// if ((sId > 0) && (sLv > 0) && (sCh > 0))
		// {
		// 	L2Skill skill = SkillTable.getInstance().getInfo(sId, sLv);
		// 	_critSkill = skill;
		// 	_critChance = sCh;
		// 	skill.attach(new ConditionGameChance(sCh), true);
		// 	attachOnCrit(skill);
		// }
	}

	/**
	 * Returns the type of Weapon
	 * @return L2WeaponType
	 */
	@Override
	public L2WeaponType getItemType()
	{
		return _type;
	}

	/**
	 * Returns the ID of the Etc item after applying the mask.
	 * @return int : ID of the Weapon
	 */
	@Override
	public int getItemMask()
	{
		return getItemType().mask();
	}
	
	/**
	 * Returns the quantity of SoulShot used.
	 * @return int
	 */
	public int getSoulShotCount()
	{
		return _soulShotCount;
	}
	
	/**
	 * Returns the quatity of SpiritShot used.
	 * @return int
	 */
	public int getSpiritShotCount()
	{
		return _spiritShotCount;
	}
	
	/**
	 * Returns the random damage inflicted by the weapon
	 * @return int
	 */
	public int getRandomDamage()
	{
		return _rndDam;
	}
	
	/**
	 * Return the Attack Reuse Delay of the L2Weapon.<BR>
	 * <BR>
	 * @return int
	 */
	public int getAttackReuseDelay()
	{
		return _atkReuse;
	}

	/**
	 * Returns the MP consumption with the weapon
	 * @return int
	 */
	public int getMpConsume()
	{
		return _mpConsume;
	}

	/**
	 * Returns the weapon attack range
	 * @return int
	 */
	public int getBaseAttackRange()
	{
		return _baseAttackRange;
	}
	
	public boolean isMagical()
	{
		return _isMagical;
	}

	/**
	 * Returns passive skill linked to that weapon
	 * @return
	 */
	public L2Skill getSkill()
	{
		return _itemSkill;
	}

	/**
	 * Returns array of Func objects containing the list of functions used by the weapon
	 * @param instance : L2ItemInstance pointing out the weapon
	 * @param player : L2Character pointing out the player
	 * @return Func[] : array of functions
	 */
	@Override
	public Func[] getStatFuncs(L2ItemInstance instance, L2Character player)
	{
		List<Func> funcs = new ArrayList<>();
		if (_funcTemplates != null)
		{
			for (FuncTemplate t : _funcTemplates)
			{
				Env env = new Env();
				env.player = player;
				Func f = t.getFunc(env, instance);
				if (f != null)
				{
					funcs.add(f);
				}
			}
		}
		return funcs.toArray(new Func[funcs.size()]);
	}

	/**
	 * Returns effects of skills associated with the item to be triggered onHit.
	 * @param caster : L2Character pointing out the caster
	 * @param target : L2Character pointing out the target
	 * @return L2Effect[] : array of effects generated by the skill
	 */
	@Override
	public L2Effect[] getSkillEffects(L2Character caster, L2Character target)
	{
		if (_skillsOnCrit == null)
		{
			return _emptyEffectSet;
		}

		L2ItemInstance weaponInst = caster.getActiveWeaponInstance();
		if (weaponInst == null)
		{
			return _emptyEffectSet;
		}
		
		// Keep old charges here
		int chargedSoulshot = weaponInst.getChargedSoulShot();
		int chargedSpiritshot = weaponInst.getChargedSpiritShot();
		
		// Discharge weapon, so that chance skills do not use ss/sps success bonus
		weaponInst.setChargedSoulShot(L2ItemInstance.CHARGED_NONE);
		weaponInst.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);

		for (L2Skill skill : _skillsOnCrit)
		{
			// if (!skill.checkCondition(caster, target, true))
			// {
			// 	continue;
			// }

			L2Character[] targets = new L2Character[] {target};
			try
			{
				// Get the skill handler corresponding to the skill type
				ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());
				// Launch the magic skill and calculate its effects
				if (handler != null)
				{
					handler.useSkill(caster, skill, targets);
				}
				else
				{
					skill.useSkill(caster, targets);
				}
			}
			catch (IOException e)
			{
			}
		}

		// Now, restore old charges
		weaponInst.setChargedSoulShot(chargedSoulshot);
		weaponInst.setChargedSpiritShot(chargedSpiritshot);

		return _emptyEffectSet;
	}

	/**
	 * Returns effects of skills associated with the item to be triggered onCast.
	 * @param caster : L2Character pointing out the caster
	 * @param target : L2Character pointing out the target
	 * @param trigger : L2Skill pointing out the skill triggering this action
	 * @return L2Effect[] : array of effects generated by the skill
	 */
	public L2Effect[] getSkillEffects(L2Character caster, L2Character target, L2Skill trigger)
	{
		if (_skillsOnCast == null)
		{
			return _emptyEffectSet;
		}

		L2ItemInstance weaponInst = caster.getActiveWeaponInstance();
		if (weaponInst == null)
		{
			return _emptyEffectSet;
		}
		
		// Keep old charges here
		int chargedSoulshot = weaponInst.getChargedSoulShot();
		int chargedSpiritshot = weaponInst.getChargedSpiritShot();
		
		// Discharge weapon, so that chance skills do not use ss/sps success bonus
		weaponInst.setChargedSoulShot(L2ItemInstance.CHARGED_NONE);
		weaponInst.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);
		
		for (L2Skill skill : _skillsOnCast)
		{
			// if (!skill.checkCondition(caster, target, true))
			// {
			// 	continue;
			// }

			if (trigger.isOffensive() != skill.isOffensive() || trigger.isMagic() != skill.isMagic())
			{
				continue; // Trigger only same type of skill
			}

			if (trigger.isToggle() && skill.getSkillType() == SkillType.BUFF)
			{
				continue; // No buffing with toggle skills
			}

			L2Character[] targets = new L2Character[] {target};
			try
			{
				// Get the skill handler corresponding to the skill type
				ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());
				// Launch the magic skill and calculate its effects
				if (handler != null)
				{
					handler.useSkill(caster, skill, targets);
				}
				else
				{
					skill.useSkill(caster, targets);
				}
			}
			catch (IOException e)
			{
			}
		}
		
		// Now, restore old charges
		weaponInst.setChargedSoulShot(chargedSoulshot);
		weaponInst.setChargedSpiritShot(chargedSpiritshot);

		return _emptyEffectSet;
	}

	/**
	 * Add the L2Skill skill to the list of skills generated by the item triggered by critical hit
	 * @param skill : L2Skill
	 */
	public void attachOnCrit(L2Skill skill)
	{
		if (_skillsOnCrit == null)
		{
			_skillsOnCrit = new L2Skill[]
			{
				skill
			};
		}
		else
		{
			int len = _skillsOnCrit.length;
			L2Skill[] tmp = new L2Skill[len + 1];
			// Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
			// number of components to be copied)
			System.arraycopy(_skillsOnCrit, 0, tmp, 0, len);
			tmp[len] = skill;
			_skillsOnCrit = tmp;
		}
	}

	/**
	 * Add the L2Skill skill to the list of skills generated by the item triggered by casting spell
	 * @param skill : L2Skill
	 */
	public void attachOnCast(L2Skill skill)
	{
		if (_skillsOnCast == null)
		{
			_skillsOnCast = new L2Skill[]
			{
				skill
			};
		}
		else
		{
			int len = _skillsOnCast.length;
			L2Skill[] tmp = new L2Skill[len + 1];
			// Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
			// number of components to be copied)
			System.arraycopy(_skillsOnCast, 0, tmp, 0, len);
			tmp[len] = skill;
			_skillsOnCast = tmp;
		}
	}
}
