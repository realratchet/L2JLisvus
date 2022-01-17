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
package net.sf.l2j.util;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.l2j.mmocore.IAcceptFilter;

/**
 * Formatted Forsaiken's IPv4Filter [DrHouse]
 * @author Forsaiken
 */
public class IPv4Filter implements IAcceptFilter, Runnable
{
	private final Map<Integer, Flood> _ipFloodMap;
	
	private static final long SLEEP_TIME = 5000;
	
	public IPv4Filter()
	{
		_ipFloodMap = new HashMap<>();
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * @param ip
	 * @return
	 */
	private static final int hash(byte[] ip)
	{
		return (ip[0] & 0xFF) | ((ip[1] << 8) & 0xFF00) | ((ip[2] << 16) & 0xFF0000) | ((ip[3] << 24) & 0xFF000000);
	}
	
	protected static final class Flood
	{
		protected long lastAccess;
		
		protected int tries;
		
		Flood()
		{
			lastAccess = System.currentTimeMillis();
			tries = 0;
		}
	}
	
	@Override
	public boolean accept(SocketChannel sc)
	{
		InetAddress addr = sc.socket().getInetAddress();
		int h = hash(addr.getAddress());
		
		long current = System.currentTimeMillis();
		Flood f;
		
		synchronized (_ipFloodMap)
		{
			f = _ipFloodMap.get(h);
		}
		
		if (f != null)
		{
			if (f.tries == -1)
			{
				f.lastAccess = current;
				return false;
			}
			
			if ((f.lastAccess + 1000) > current)
			{
				f.lastAccess = current;
				
				if (f.tries >= 3)
				{
					f.tries = -1;
					return false;
				}
				
				f.tries++;
			}
			else
			{
				f.lastAccess = current;
			}
		}
		else
		{
			synchronized (_ipFloodMap)
			{
				_ipFloodMap.put(h, new Flood());
			}
		}
		
		return true;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			long reference = System.currentTimeMillis() - (1000 * 300);
			
			synchronized (_ipFloodMap)
			{
				Iterator<Entry<Integer, Flood>> it = _ipFloodMap.entrySet().iterator();
				while (it.hasNext())
				{
					Flood f = it.next().getValue();
					if (f.lastAccess < reference)
					{
						it.remove();
					}
				}
			}
			
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}