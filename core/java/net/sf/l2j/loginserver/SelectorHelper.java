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
package net.sf.l2j.loginserver;

import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sf.l2j.loginserver.serverpackets.Init;
import net.sf.l2j.mmocore.IAcceptFilter;
import net.sf.l2j.mmocore.IClientFactory;
import net.sf.l2j.mmocore.IMMOExecutor;
import net.sf.l2j.mmocore.MMOConnection;
import net.sf.l2j.mmocore.ReceivablePacket;
import net.sf.l2j.mmocore.SendablePacket;
import net.sf.l2j.util.IPv4Filter;

/**
 * @author KenM
 */
public class SelectorHelper implements IMMOExecutor<L2LoginClient>, IClientFactory<L2LoginClient>, IAcceptFilter
{
	private static final Logger _log = Logger.getLogger(SelectorHelper.class.getName());
	
	private final ThreadPoolExecutor _generalPacketsThreadPool;
	private final IPv4Filter _ipv4filter;
	
	public SelectorHelper()
	{
		_generalPacketsThreadPool = new ThreadPoolExecutor(4, 6, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		_ipv4filter = new IPv4Filter();
	}
	
	/**
	 * @see net.sf.l2j.mmocore.IMMOExecutor#execute(net.sf.l2j.mmocore.ReceivablePacket)
	 */
	@Override
	public void execute(ReceivablePacket<L2LoginClient> packet)
	{
		_generalPacketsThreadPool.execute(packet);
	}
	
	/**
	 * @see net.sf.l2j.mmocore.IClientFactory#create(net.sf.l2j.mmocore.MMOConnection)
	 */
	@Override
	public L2LoginClient create(MMOConnection<L2LoginClient> con)
	{
		L2LoginClient client = new L2LoginClient(con);
		// Counted here so that plain and WebSocket connections are both counted exactly once
		if (!LoginController.getInstance().addConnectedClient(client))
		{
			_log.warning("Too many connections from " + con.getInetAddress().getHostAddress() + ", closing.");
			con.close((SendablePacket<L2LoginClient>) null);
			return client;
		}
		client.sendPacket(new Init(client));
		return client;
	}
	
	/**
	 * @see net.sf.l2j.mmocore.IAcceptFilter#accept(java.net.InetAddress)
	 */
	@Override
	public boolean accept(InetAddress address)
	{
		return _ipv4filter.accept(address) && !LoginController.getInstance().isBannedAddress(address.getHostAddress()) && LoginController.getInstance().canAcceptConnection(address);
	}
}