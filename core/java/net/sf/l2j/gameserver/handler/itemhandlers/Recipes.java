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
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.RecipeController;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2RecipeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.1.2.5.2.5 $ $Date: 2005/04/06 16:13:51 $
 */

public class Recipes implements IItemHandler
{
	private static int[] _itemIds = null;
	
	public Recipes()
	{
		RecipeController rc = RecipeController.getInstance();
		_itemIds = new int[rc.getRecipesCount()];
		for (int i = 0; i < rc.getRecipesCount(); i++)
		{
			_itemIds[i] = rc.getRecipeList(i).getRecipeId();
		}
	}
	
	@Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		if (!(playable instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance activeChar = (L2PcInstance) playable;
		switch (activeChar.getPrivateStoreType())
		{
			case DWARVEN_MANUFACTURE:
			case GENERAL_MANUFACTURE:
			case DWARVEN_MANUFACTURE_MANAGE:
			case GENERAL_MANUFACTURE_MANAGE:
				activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING));
				return;
			default:
				break;
		}
		
		L2RecipeList rp = RecipeController.getInstance().getRecipeByItemId(item.getItemId());
		if (rp == null)
		{
			return;
		}
		
		if (activeChar.hasRecipeList(rp.getId()))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.RECIPE_ALREADY_REGISTERED));
		}
		else
		{
			if (rp.isDwarvenRecipe())
			{
				if (activeChar.hasDwarvenCraft())
				{
					if (rp.getLevel() > activeChar.getDwarvenCraft())
					{
						// can't add recipe, because create item level too low
						activeChar.sendPacket(new SystemMessage(404));
					}
					else if (activeChar.getDwarvenRecipeBook().length >= activeChar.getDwarfRecipeLimit())
					{
						// Up to $s1 recipes can be registered.
						SystemMessage sm = new SystemMessage(894);
						sm.addNumber(activeChar.getDwarfRecipeLimit());
						activeChar.sendPacket(sm);
					}
					else
					{
						activeChar.registerDwarvenRecipeList(rp, true);
						activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false);
						
						RecipeController.getInstance().requestBookOpen(activeChar, true);
						SystemMessage sm = new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED);
						sm.addItemName(item.getItemId());
						activeChar.sendPacket(sm);
					}
				}
				else
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_REGISTER_NO_ABILITY_TO_CRAFT));
				}
			}
			else
			{
				if (activeChar.hasCommonCraft())
				{
					if (rp.getLevel() > activeChar.getCommonCraft())
					{
						// can't add recipe, because create item level too low
						activeChar.sendPacket(new SystemMessage(404));
					}
					else if (activeChar.getCommonRecipeBook().length >= activeChar.getCommonRecipeLimit())
					{
						// Up to $s1 recipes can be registered.
						SystemMessage sm = new SystemMessage(894);
						sm.addNumber(activeChar.getCommonRecipeLimit());
						activeChar.sendPacket(sm);
					}
					else
					{
						activeChar.registerCommonRecipeList(rp, true);
						activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false);
						
						RecipeController.getInstance().requestBookOpen(activeChar, false);
						SystemMessage sm = new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED);
						sm.addItemName(item.getItemId());
						activeChar.sendPacket(sm);
					}
				}
				else
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_REGISTER_NO_ABILITY_TO_CRAFT));
				}
			}
		}
	}
	
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}