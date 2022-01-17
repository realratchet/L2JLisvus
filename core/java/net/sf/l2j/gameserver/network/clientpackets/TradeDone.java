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
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.6.2.2.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class TradeDone extends L2GameClientPacket
{
	private static final String _C__17_TRADEDONE = "[C] 17 TradeDone";
	private static Logger _log = Logger.getLogger(TradeDone.class.getName());
	
	private int _response;
	
	@Override
	protected void readImpl()
	{
		_response = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		TradeList trade = player.getActiveTradeList();
		if (trade == null)
		{
			_log.warning("player.getTradeList == null in " + getType() + " for player " + player.getName());
			return;
		}
		
		if (trade.isLocked())
		{
			return;
		}
		
		if (_response != 1)
		{
			player.cancelActiveTrade();
			return;
		}
		
		if (player.getAccessLevel() > 0 && player.getAccessLevel() < Config.GM_TRANSACTION)
		{
			player.cancelActiveTrade();
			player.sendMessage("Transactions are disabled for your Access Level.");
			return;
		}
		
		if (trade.getPartner() == null || L2World.getInstance().getPlayer(trade.getPartner().getObjectId()) == null)
		{
			// Trade partner not found, cancel trade
			player.cancelActiveTrade();
			SystemMessage msg = new SystemMessage(SystemMessage.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			player.sendPacket(msg);
			msg = null;
			return;
		}
		
		if (trade.getOwner().getActiveEnchantItem() != null || trade.getPartner().getActiveEnchantItem() != null)
		{
			player.cancelActiveTrade();
			return;
		}
		
		if (trade.getOwner().isInStoreMode() || trade.getPartner().isInStoreMode())
		{
			player.cancelActiveTrade();
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			return;
		}
		
		if (Util.calculateDistance(player, trade.getPartner(), true) > 150)
		{
			player.cancelActiveTrade();
			return;
		}
		
		trade.confirm();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__17_TRADEDONE;
	}
}