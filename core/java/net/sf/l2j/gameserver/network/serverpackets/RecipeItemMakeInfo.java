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
package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.RecipeController;
import net.sf.l2j.gameserver.model.L2RecipeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * format dddd
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class RecipeItemMakeInfo extends L2GameServerPacket
{
	private static final String _S__D7_RECIPEITEMMAKEINFO = "[S] D7 RecipeItemMakeInfo";
	
	private final int _recipeId;
	private final L2PcInstance _player;
	private final int _status;
	
	public RecipeItemMakeInfo(int recipeId, L2PcInstance player, int status)
	{
		_recipeId = recipeId;
		_player = player;
		_status = status;
	}
	
	public RecipeItemMakeInfo(int recipeId, L2PcInstance player)
	{
		_recipeId = recipeId;
		_player = player;
		_status = -1;
	}
	
	@Override
	protected final void writeImpl()
	{
		L2RecipeList recipe = RecipeController.getInstance().getRecipeById(_recipeId);
		if (recipe != null)
		{
			writeC(0xd7);
			writeD(_recipeId);
			writeD(recipe.isDwarvenRecipe() ? 0 : 1); // 0 = Dwarven - 1 = Common
			writeD((int) _player.getCurrentMp());
			writeD(_player.getMaxMp());
			writeD(_status);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__D7_RECIPEITEMMAKEINFO;
	}
}