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

import net.sf.l2j.gameserver.RecipeController;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.util.Util;

/**
 * @author Administrator TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class RequestRecipeShopMakeItem extends L2GameClientPacket
{
	private static final String _C__AF_REQUESTRECIPESHOPMAKEITEM = "[C] B6 RequestRecipeShopMakeItem";
	
	private int _id;
	private int _recipeId;
	@SuppressWarnings("unused")
	private int _unknow;
	
	@Override
	protected void readImpl()
	{
		_id = readD();
		_recipeId = readD();
		_unknow = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!getClient().getFloodProtectors().getManufacture().tryPerformAction("RecipeShopMake"))
		{
			return;
		}
		
		L2PcInstance manufacturer = L2World.getInstance().getPlayer(_id);
		if (manufacturer == null)
		{
			return;
		}
		
		if (activeChar.isInStoreMode())
		{
			activeChar.sendMessage("Cannot make items while trading.");
			return;
		}
		
		if (manufacturer.getPrivateStoreType() != PrivateStoreType.DWARVEN_MANUFACTURE && manufacturer.getPrivateStoreType() != PrivateStoreType.GENERAL_MANUFACTURE)
		{
			return;
		}
		
		if (activeChar.isInCraftMode() || manufacturer.isInCraftMode())
		{
			activeChar.sendMessage("Currently in Craft Mode.");
			return;
		}
		
		if (Util.checkIfInRange(150, activeChar, manufacturer, true))
		{
			RecipeController.getInstance().requestManufactureItem(manufacturer, _recipeId, activeChar);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__AF_REQUESTRECIPESHOPMAKEITEM;
	}
}