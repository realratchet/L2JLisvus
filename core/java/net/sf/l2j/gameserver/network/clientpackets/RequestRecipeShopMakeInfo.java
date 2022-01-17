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

import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.network.serverpackets.RecipeShopItemInfo;

/**
 * This class ... cdd
 * @version $Revision: 1.1.2.1.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestRecipeShopMakeInfo extends L2GameClientPacket
{
	private static final String _C__B5_RequestRecipeShopMakeInfo = "[C] b5 RequestRecipeShopMakeInfo";
	
	private int _playerObjectId;
	private int _recipeId;
	
	@Override
	protected void readImpl()
	{
		_playerObjectId = readD();
		_recipeId = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		final L2PcInstance shop = L2World.getInstance().getPlayer(_playerObjectId);
		if (shop == null || shop.getPrivateStoreType() != PrivateStoreType.DWARVEN_MANUFACTURE && shop.getPrivateStoreType() != PrivateStoreType.GENERAL_MANUFACTURE)
		{
			return;
		}
		
		player.sendPacket(new RecipeShopItemInfo(_recipeId, shop));
	}
	
	@Override
	public String getType()
	{
		return _C__B5_RequestRecipeShopMakeInfo;
	}
}
