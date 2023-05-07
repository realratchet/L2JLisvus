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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.effects.EffectTemplate;
import net.sf.l2j.gameserver.skills.funcs.Func;
import net.sf.l2j.gameserver.skills.funcs.FuncTemplate;

/**
 * This class contains all informations concerning the item (weapon, armor, etc).<BR>
 * Mother class of :
 * <LI>L2Armor</LI>
 * <LI>L2EtcItem</LI>
 * <LI>L2Weapon</LI>
 * @version $Revision: 1.7.2.2.2.5 $ $Date: 2005/04/06 18:25:18 $
 */
public abstract class L2Item
{
	private static final Map<String, Integer> _slots = new HashMap<>();
	{
		_slots.put("chest", L2Item.SLOT_CHEST);
        _slots.put("fullarmor", L2Item.SLOT_FULL_ARMOR);
        _slots.put("head", L2Item.SLOT_HEAD);
        _slots.put("hair", L2Item.SLOT_HAIR);
        _slots.put("underwear", L2Item.SLOT_UNDERWEAR);
        _slots.put("back", L2Item.SLOT_BACK);
        _slots.put("neck", L2Item.SLOT_NECK);
        _slots.put("legs", L2Item.SLOT_LEGS);
        _slots.put("feet", L2Item.SLOT_FEET);
        _slots.put("gloves", L2Item.SLOT_GLOVES);
        _slots.put("chest;legs", L2Item.SLOT_CHEST | L2Item.SLOT_LEGS);
        _slots.put("rhand", L2Item.SLOT_R_HAND);
        _slots.put("lhand", L2Item.SLOT_L_HAND);
        _slots.put("lrhand", L2Item.SLOT_LR_HAND);
        _slots.put("rear;lear", L2Item.SLOT_R_EAR | L2Item.SLOT_L_EAR);
        _slots.put("rfinger;lfinger", L2Item.SLOT_R_FINGER | L2Item.SLOT_L_FINGER);
        _slots.put("none", L2Item.SLOT_NONE);
        _slots.put("wolf", L2Item.SLOT_WOLF); // for wolf
        _slots.put("hatchling", L2Item.SLOT_HATCHLING); // for hatchling
        _slots.put("strider", L2Item.SLOT_STRIDER); // for strider
	}

	public static final int TYPE1_WEAPON_RING_EARRING_NECKLACE = 0;
	public static final int TYPE1_SHIELD_ARMOR = 1;
	public static final int TYPE1_ITEM_QUESTITEM_ADENA = 4;
	
	public static final int TYPE2_WEAPON = 0;
	public static final int TYPE2_SHIELD_ARMOR = 1;
	public static final int TYPE2_ACCESSORY = 2;
	public static final int TYPE2_QUEST = 3;
	public static final int TYPE2_MONEY = 4;
	public static final int TYPE2_OTHER = 5;
	
	public static final int SLOT_NONE = 0x0000;
	public static final int SLOT_UNDERWEAR = 0x0001;
	public static final int SLOT_R_EAR = 0x0002;
	public static final int SLOT_L_EAR = 0x0004;
	public static final int SLOT_NECK = 0x0008;
	public static final int SLOT_R_FINGER = 0x0010;
	public static final int SLOT_L_FINGER = 0x0020;
	public static final int SLOT_HEAD = 0x0040;
	public static final int SLOT_R_HAND = 0x0080;
	public static final int SLOT_L_HAND = 0x0100;
	public static final int SLOT_GLOVES = 0x0200;
	public static final int SLOT_CHEST = 0x0400;
	public static final int SLOT_LEGS = 0x0800;
	public static final int SLOT_FEET = 0x1000;
	public static final int SLOT_BACK = 0x2000;
	public static final int SLOT_LR_HAND = 0x4000;
	public static final int SLOT_FULL_ARMOR = 0x8000;
	public static final int SLOT_HAIR = 0x010000;
	public static final int SLOT_WOLF = 0x020000;
	public static final int SLOT_HATCHLING = 0x040000;
	public static final int SLOT_STRIDER = 0x080000;
	
	private final int _itemId;
	private final String _name;
	private final int _weight;
	
	private final MaterialType _materialType;
	private final int _bodyPart;
	private final int _referencePrice;
	private final CrystalType _crystalType; // default to no-grade
	private final int _crystalCount;

	private final int _reuseDelay;

	private final boolean _stackable;
	private final boolean _sellable;
	private final boolean _dropable;
	private final boolean _destroyable;
	private final boolean _tradable;
	
	private final boolean _heroItem;
	
	protected int _type1; // needed for item list (inventory)
	protected int _type2; // different lists for armor, weapon, etc
	
	protected FuncTemplate[] _funcTemplates;
	protected List<Condition> _preConditions;
	protected EffectTemplate[] _effectTemplates;
	protected L2Skill[] _skills;
	
	private static final Func[] _emptyFunctionSet = new Func[0];
	protected static final L2Effect[] _emptyEffectSet = new L2Effect[0];
	
	/**
	 * Constructor of the L2Item that fill class variables.
	 * 
	 * @param set : StatsSet corresponding to a set of couples (key,value) for description of the item
	 */
	protected L2Item(StatsSet set)
	{
		_itemId = set.getInteger("item_id");
		_name = set.getString("name");
		_weight = set.getInteger("weight", 0);
		
		_materialType = set.getEnum("material", MaterialType.class, MaterialType.STEEL);
		_bodyPart = _slots.get(set.getString("bodypart", "none"));
		_referencePrice = set.getInteger("price", 0);
		_crystalType = set.getEnum("crystal_type", CrystalType.class, CrystalType.NONE); // default to no-grade
		_crystalCount = set.getInteger("crystal_count", 0);

		_reuseDelay = set.getInteger("reuse_delay", 0);

		_stackable = set.getBool("is_stackable", false);
		_sellable = set.getBool("is_sellable", true);
		_dropable = set.getBool("is_dropable", true);
		_destroyable = set.getBool("is_destroyable", true);
		_tradable = set.getBool("is_tradable", true);
		_heroItem = (_itemId >= 6611 && _itemId <= 6621) || _itemId == 6842;
	}
	
	/**
	 * Returns the itemType.
	 * @return Enum
	 */
	public abstract Enum<?> getItemType();
	
	/**
	 * Returns the ID of the item.
	 * @return int
	 */
	public final int getItemId()
	{
		return _itemId;
	}
	
	public abstract int getItemMask();
	
	/**
	 * Return the type of material of the item
	 * @return MaterialType
	 */
	public final MaterialType getMaterialType()
	{
		return _materialType;
	}
	
	/**
	 * Returns the type 2 of the item
	 * @return int
	 */
	public final int getType2()
	{
		return _type2;
	}
	
	/**
	 * Returns the weight of the item
	 * @return int
	 */
	public final int getWeight()
	{
		return _weight;
	}
	
	/**
	 * Returns if the item is crystallizable
	 * @return boolean
	 */
	public final boolean isCrystallizable()
	{
		return _crystalType != CrystalType.NONE && _crystalCount > 0;
	}
	
	/**
	 * Return the type of crystal if item is crystallizable
	 * @return CrystalType
	 */
	public final CrystalType getCrystalType()
	{
		return _crystalType;
	}
	
	/**
	 * Return the type of crystal if item is crystallizable
	 * @return int
	 */
	public final int getCrystalItemId()
	{
		return _crystalType.getCrystalId();
	}
	
	/**
	 * Returns the grade of the item.<BR>
	 * <BR>
	 * <U><I>Concept :</I></U><BR>
	 * In fact, this function returns the type of crystal of the item.
	 * @return CrystalType
	 */
	public final CrystalType getItemGrade()
	{
		return getCrystalType();
	}
	
	/**
	 * Returns the quantity of crystals for crystallization
	 * @return int
	 */
	public final int getCrystalCount()
	{
		return _crystalCount;
	}
	
	/**
	 * Returns the quantity of crystals for crystallization on specific enchant level
	 * @param enchantLevel
	 * @return
	 */
	public final int getCrystalCount(int enchantLevel)
	{
		if (enchantLevel > 3)
			switch (_type2)
			{
				case TYPE2_SHIELD_ARMOR:
				case TYPE2_ACCESSORY:
					return _crystalCount + _crystalType.getCrystalEnchantBonusArmor() * (3 * enchantLevel - 6);
				case TYPE2_WEAPON:
					return _crystalCount + _crystalType.getCrystalEnchantBonusWeapon() * (2 * enchantLevel - 3);
				default:
					return _crystalCount;
			}
		else if (enchantLevel > 0)
			switch (_type2)
			{
				case TYPE2_SHIELD_ARMOR:
				case TYPE2_ACCESSORY:
					return _crystalCount + _crystalType.getCrystalEnchantBonusArmor() * enchantLevel;
				case TYPE2_WEAPON:
					return _crystalCount + _crystalType.getCrystalEnchantBonusWeapon() * enchantLevel;
				default:
					return _crystalCount;
			}
		else
			return _crystalCount;
	}

	/**
	 * Gets the item reuse delay time in milliseconds.
	 * 
	 * @return the reuse delay time
	 */
	public int getReuseDelay() {
		return _reuseDelay;
	}
	
	/**
	 * Returns the name of the item
	 * @return String
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * Return the part of the body used with the item.
	 * @return int
	 */
	public final int getBodyPart()
	{
		return _bodyPart;
	}

	/**
	 * Returns the price of reference of the item
	 * @return int
	 */
	public final int getReferencePrice()
	{
		return (isConsumable() ? (int) (_referencePrice * Config.RATE_CONSUMABLE_COST) : _referencePrice);
	}
	
	/**
	 * Returns the type 1 of the item
	 * @return int
	 */
	public final int getType1()
	{
		return _type1;
	}
	
	/**
	 * Returns if the item is consumable
	 * @return boolean
	 */
	public boolean isConsumable()
	{
		return false;
	}
	
	/**
	 * Returns if the item is a mercenary posting ticket
	 * @return boolean
	 */
	public boolean isMercenaryTicket()
	{
		return false;
	}

	/**
	 * Returns if the item is stackable
	 * @return boolean
	 */
	public final boolean isStackable()
	{
		return _stackable;
	}
	
	/**
	 * Returns if the item can be sold
	 * @return boolean
	 */
	public final boolean isSellable()
	{
		return _sellable;
	}
	
	/**
	 * Returns if the item can be dropped
	 * @return boolean
	 */
	public final boolean isDropable()
	{
		return _dropable;
	}
	
	/**
	 * Returns if the item can be destroyed
	 * @return boolean
	 */
	public final boolean isDestroyable()
	{
		return _destroyable;
	}
	
	/**
	 * Returns if the item can be traded
	 * @return boolean
	 */
	public final boolean isTradable()
	{
		return _tradable;
	}
	
	/**
	 * Returns if item is hero-only
	 * @return
	 */
	public final boolean isHeroItem()
	{
		return _heroItem;
	}
	
	/**
	 * Returns if item is for hatchling
	 * @return boolean
	 */
	public boolean isForHatchling()
	{
		return _bodyPart == SLOT_HATCHLING;
	}
	
	/**
	 * Returns if item is for strider
	 * @return boolean
	 */
	public boolean isForStrider()
	{
		return _bodyPart == SLOT_STRIDER;
	}
	
	/**
	 * Returns if item is for wolf
	 * @return boolean
	 */
	public boolean isForWolf()
	{
		return _bodyPart == SLOT_WOLF;
	}
	
	/**
	 * Returns array of Func objects containing the list of functions used by the item
	 * @param instance : L2ItemInstance pointing out the item
	 * @param player : L2Character pointing out the player
	 * @return Func[] : array of functions
	 */
	public Func[] getStatFuncs(L2ItemInstance instance, L2Character player)
	{
		if (_funcTemplates == null)
			return _emptyFunctionSet;
		List<Func> funcs = new ArrayList<>();
		for (FuncTemplate t : _funcTemplates)
		{
			Env env = new Env();
			env.player = player;
			env.target = player;
			Func f = t.getFunc(env, this); // Skill is owner
			if (f != null)
				funcs.add(f);
		}
		if (funcs.isEmpty())
			return _emptyFunctionSet;
		return funcs.toArray(new Func[funcs.size()]);
	}
	
	/**
	 * Returns the effects associated with the item.
	 * @param instance : L2ItemInstance pointing out the item
	 * @param player : L2Character pointing out the player
	 * @return L2Effect[] : array of effects generated by the item
	 */
	public L2Effect[] getEffects(L2ItemInstance instance, L2Character player)
	{
		if (_effectTemplates == null)
			return _emptyEffectSet;
		List<L2Effect> effects = new ArrayList<>();
		for (EffectTemplate et : _effectTemplates)
		{
			Env env = new Env();
			env.player = player;
			env.target = player;
			L2Effect e = et.getEffect(env, this);
			if (e != null)
				effects.add(e);
		}
		if (effects.isEmpty())
			return _emptyEffectSet;
		return effects.toArray(new L2Effect[effects.size()]);
	}
	
	/**
	 * Returns effects of skills associated with the item.
	 * @param caster : L2Character pointing out the caster
	 * @param target : L2Character pointing out the target
	 * @return L2Effect[] : array of effects generated by the skill
	 */
	public L2Effect[] getSkillEffects(L2Character caster, L2Character target)
	{
		if (_skills == null)
			return _emptyEffectSet;
		List<L2Effect> effects = new ArrayList<>();
		
		for (L2Skill skill : _skills)
		{
			if (!skill.checkCondition(caster, target))
				continue; // Skill condition not met
				
			if (target.getFirstEffect(skill.getId()) != null)
				target.removeEffect(target.getFirstEffect(skill.getId()));
			for (L2Effect e : skill.getEffects(caster, target))
				effects.add(e);
		}
		if (effects.isEmpty())
			return _emptyEffectSet;
		return effects.toArray(new L2Effect[effects.size()]);
	}
	
	/**
	 * Add the FuncTemplate f to the list of functions used with the item
	 * @param f : FuncTemplate to add
	 */
	public void attach(FuncTemplate f)
	{
		// If _functTemplates is empty, create it and add the FuncTemplate f in it
		if (_funcTemplates == null)
		{
			_funcTemplates = new FuncTemplate[]
			{
				f
			};
		}
		else
		{
			int len = _funcTemplates.length;
			FuncTemplate[] tmp = new FuncTemplate[len + 1];
			// Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
			// number of components to be copied)
			System.arraycopy(_funcTemplates, 0, tmp, 0, len);
			tmp[len] = f;
			_funcTemplates = tmp;
		}
	}
	
	/**
	 * Add the EffectTemplate effect to the list of effects generated by the item
	 * @param effect : EffectTemplate
	 */
	public void attach(EffectTemplate effect)
	{
		if (_effectTemplates == null)
		{
			_effectTemplates = new EffectTemplate[]
			{
				effect
			};
		}
		else
		{
			int len = _effectTemplates.length;
			EffectTemplate[] tmp = new EffectTemplate[len + 1];
			// Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
			// number of components to be copied)
			System.arraycopy(_effectTemplates, 0, tmp, 0, len);
			tmp[len] = effect;
			_effectTemplates = tmp;
		}
	}
	
	/**
	 * Add the L2Skill skill to the list of skills generated by the item
	 * @param skill : L2Skill
	 */
	public void attach(L2Skill skill)
	{
		if (_skills == null)
		{
			_skills = new L2Skill[]
			{
				skill
			};
		}
		else
		{
			int len = _skills.length;
			L2Skill[] tmp = new L2Skill[len + 1];
			// Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
			// number of components to be copied)
			System.arraycopy(_skills, 0, tmp, 0, len);
			tmp[len] = skill;
			_skills = tmp;
		}
	}
	
	public final void attach(Condition c)
	{
		if (_preConditions == null)
		{
			_preConditions = new ArrayList<>(1);
		}
		if (!_preConditions.contains(c))
		{
			_preConditions.add(c);
		}
	}
	
	public boolean checkCondition(L2Character activeChar, L2Object target, boolean sendMessage)
	{
		if (activeChar instanceof L2PcInstance)
		{
			if (activeChar.isGM() && ((L2PcInstance) activeChar).getAccessLevel() >= Config.GM_ITEM_RESTRICTION)
			{
				return true;
			}
		}
		
		if (_preConditions == null || _preConditions.isEmpty())
		{
			return true;
		}
		
		Env env = new Env();
		env.player = activeChar;
		env.target = target != null && target instanceof L2Character ? (L2Character) target : null;
		
		for (Condition preCondition : _preConditions)
		{
			if (preCondition == null)
				return true;
			
			if (!preCondition.test(env, this))
			{
				if (activeChar instanceof L2Summon)
				{
					activeChar.getActingPlayer().sendPacket(new SystemMessage(SystemMessage.PET_CANNOT_USE_ITEM));
					return false;
				}
				
				if (sendMessage)
				{
					String msg = preCondition.getMessage();
					int msgId = preCondition.getMessageId();
					if (msg != null)
					{
						activeChar.sendMessage(msg);
					}
					else if (msgId != 0)
					{
						SystemMessage sm = new SystemMessage(msgId);
						if (preCondition.isAddName())
						{
							sm.addItemName(_itemId);
						}
						activeChar.sendPacket(sm);
					}
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the name of the item
	 * @return String
	 */
	@Override
	public String toString()
	{
		return _name;
	}
}
