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

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.RecipeController;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class RequestRecipeBookOpen extends L2GameClientPacket
{
	private static final String _C__AC_REQUESTRECIPEBOOKOPEN = "[C] AC RequestRecipeBookOpen";
	private static Logger _log = Logger.getLogger(RequestRecipeBookOpen.class.getName());
	
	private boolean isDwarvenCraft;
	
	@Override
	protected void readImpl()
	{
		isDwarvenCraft = (readD() == 0);
		if (Config.DEBUG)
		{
			_log.info("RequestRecipeBookOpen : " + (isDwarvenCraft ? "dwarvenCraft" : "commonCraft"));
		}
	}
	
	@Override
	public void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (activeChar.isInStoreMode())
		{
			activeChar.sendMessage("Cannot use recipe book while trading.");
			return;
		}
		
		RecipeController.getInstance().requestBookOpen(activeChar, isDwarvenCraft);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__AC_REQUESTRECIPEBOOKOPEN;
	}
}