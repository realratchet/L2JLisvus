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
import net.sf.l2j.gameserver.model.L2RecipeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.network.serverpackets.RecipeBookItemList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class RequestRecipeBookDestroy extends L2GameClientPacket
{
	private static final String _C__AC_REQUESTRECIPEBOOKDESTROY = "[C] AD RequestRecipeBookDestroy";
	// private static Logger _log = Logger.getLogger(RequestSellItem.class.getName());
	
	private int _RecipeID;
	
	@Override
	protected void readImpl()
	{
		_RecipeID = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("RecipeDestroy"))
		{
			return;
		}
		
		if (activeChar.getPrivateStoreType() == PrivateStoreType.DWARVEN_MANUFACTURE 
			|| activeChar.getPrivateStoreType() == PrivateStoreType.GENERAL_MANUFACTURE 
			|| activeChar.getPrivateStoreType() == PrivateStoreType.DWARVEN_MANUFACTURE_MANAGE 
			|| activeChar.getPrivateStoreType() == PrivateStoreType.GENERAL_MANUFACTURE_MANAGE)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING));
			return;
		}
		
		L2RecipeList rp = RecipeController.getInstance().getRecipeList(_RecipeID - 1);
		if (rp == null)
		{
			return;
		}
		
		activeChar.unregisterRecipeList(_RecipeID);
		
		RecipeBookItemList response = new RecipeBookItemList(rp.isDwarvenRecipe(), activeChar.getMaxMp());
		if (rp.isDwarvenRecipe())
		{
			response.addRecipes(activeChar.getDwarvenRecipeBook());
		}
		else
		{
			response.addRecipes(activeChar.getCommonRecipeBook());
		}
		
		activeChar.sendPacket(response);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__AC_REQUESTRECIPEBOOKDESTROY;
	}
}