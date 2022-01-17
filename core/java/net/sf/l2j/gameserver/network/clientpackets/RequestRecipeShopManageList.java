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

import net.sf.l2j.gameserver.model.L2ManufactureList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.RecipeShopManageList;

/**
 * This class ...
 * @version $Revision: 1.1.2.1.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestRecipeShopManageList extends L2GameClientPacket
{
	private static final String _C__B0_RequestRecipeShopManageList = "[C] b0 RequestRecipeShopManageList";
	
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		// Player shouldn't be able to set stores if he/she is alike dead (dead or fake death)
		if (player.isAlikeDead())
		{
			sendPacket(new ActionFailed());
			return;
		}
		
		if (player.getPrivateStoreType() == PrivateStoreType.DWARVEN_MANUFACTURE || player.getPrivateStoreType() == PrivateStoreType.DWARVEN_MANUFACTURE_MANAGE)
		{
			player.setPrivateStoreType(PrivateStoreType.NONE);
		}

		if (player.getPrivateStoreType() == PrivateStoreType.NONE)
		{
			if (player.isSitting())
			{
				player.standUp();
			}
			
			if (player.getCreateList() == null)
			{
				player.setCreateList(new L2ManufactureList());
			}

			player.setPrivateStoreType(PrivateStoreType.DWARVEN_MANUFACTURE_MANAGE);
			player.broadcastUserInfo();
			player.sendPacket(new RecipeShopManageList(player, true));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__B0_RequestRecipeShopManageList;
	}
}