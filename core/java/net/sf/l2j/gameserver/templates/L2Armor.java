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
import java.util.List;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.funcs.Func;
import net.sf.l2j.gameserver.skills.funcs.FuncTemplate;

/**
 * This class is dedicated to the management of armors.
 * @version $Revision: 1.2.2.1.2.6 $ $Date: 2005/03/27 15:30:10 $
 */
public final class L2Armor extends L2Item
{
	private L2ArmorType _type;

	private L2Skill _itemSkill = null; // for passive skill
	
	/**
	 * Constructor for Armor.
	 * 
	 * @param set : StatsSet designating the set of couples (key,value) characterizing the armor
	 * @see L2Item constructor
	 */
	public L2Armor(StatsSet set)
	{
		super(set);

		_type = set.getEnum("armor_type", L2ArmorType.class, L2ArmorType.NONE);
		
		if (getBodyPart() == L2Item.SLOT_NECK || getBodyPart() == L2Item.SLOT_HAIR || (getBodyPart() & L2Item.SLOT_L_EAR) != 0 || (getBodyPart() & L2Item.SLOT_L_FINGER) != 0)
		{
			_type1 = L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE;
			_type2 = L2Item.TYPE2_ACCESSORY;
		}
		else
		{
			if (_type == L2ArmorType.NONE && getBodyPart() == L2Item.SLOT_L_HAND) // retail define shield as NONE
				_type = L2ArmorType.SHIELD;

			_type1 = L2Item.TYPE1_SHIELD_ARMOR;
			_type2 = L2Item.TYPE2_SHIELD_ARMOR;
		}
	}
	
	/**
	 * Returns the type of the armor.
	 * @return L2ArmorType
	 */
	@Override
	public L2ArmorType getItemType()
	{
		return _type;
	}
	
	/**
	 * Returns the ID of the item after applying the mask.
	 * @return int : ID of the item
	 */
	@Override
	public final int getItemMask()
	{
		return getItemType().mask();
	}
	
	/**
	 * Returns passive skill linked to that armor
	 * @return
	 */
	public L2Skill getSkill()
	{
		return _itemSkill;
	}
	
	/**
	 * Returns array of Func objects containing the list of functions used by the armor
	 * @param instance : L2ItemInstance pointing out the armor
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
					funcs.add(f);
			}
		}
		return funcs.toArray(new Func[funcs.size()]);
	}
}
