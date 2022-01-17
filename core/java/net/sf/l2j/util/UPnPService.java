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

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.logging.Logger;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.xml.sax.SAXException;

import net.sf.l2j.Config;
import net.sf.l2j.Server;

/**
 * @author UnAfraid
 */
public class UPnPService
{
	private static final Logger _log = Logger.getLogger(UPnPService.class.getName());
	private static final String PROTOCOL = "TCP";
	
	private final GatewayDiscover _gatewayDiscover = new GatewayDiscover();
	private GatewayDevice _activeGW;
	
	public static UPnPService getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected UPnPService()
	{
		try
		{
			load();
		}
		catch (Exception e)
		{
			_log.warning(getClass().getSimpleName() + ": There was a problem while initializing the UPnP Service!" + e);
		}
	}
	
	private void load() throws Exception
	{
		if (!Config.ENABLE_UPNP)
		{
			_log.info("UPnP Service is disabled.");
			return;
		}
		
		_log.info("Looking for UPnP Gateway Devices...");
		
		final Map<InetAddress, GatewayDevice> gateways = _gatewayDiscover.discover();
		if (gateways.isEmpty())
		{
			_log.info("No UPnP gateways found.");
			return;
		}
		
		// choose the first active gateway for the tests
		_activeGW = _gatewayDiscover.getValidGateway();
		if (_activeGW != null)
		{
			_log.info("Using UPnP gateway: " + _activeGW.getFriendlyName());
		}
		else
		{
			_log.info("No active UPnP gateway found.");
			return;
		}
		
		_log.info("Using local address: " + _activeGW.getLocalAddress().getHostAddress() + " External address: " + _activeGW.getExternalIPAddress());
		
		if (Server.SERVER_MODE == Server.MODE_GAMESERVER)
		{
			addPortMapping(Config.PORT_GAME, "L2JLisvus Game Server");
		}
		else if (Server.SERVER_MODE == Server.MODE_LOGINSERVER)
		{
			addPortMapping(Config.PORT_LOGIN, "L2JLisvus Login Server");
		}
	}
	
	public void removeAllPorts() throws Exception
	{
		if (_activeGW != null)
		{
			if (Server.SERVER_MODE == Server.MODE_GAMESERVER)
			{
				deletePortMapping(Config.PORT_GAME);
			}
			else if (Server.SERVER_MODE == Server.MODE_LOGINSERVER)
			{
				deletePortMapping(Config.PORT_LOGIN);
			}
		}
	}
	
	private void addPortMapping(int port, String description) throws IOException, SAXException
	{
		final PortMappingEntry portMapping = new PortMappingEntry();
		final InetAddress localAddress = _activeGW.getLocalAddress();
		
		// Attempt to re-map
		if (_activeGW.getSpecificPortMappingEntry(port, PROTOCOL, portMapping))
		{
			_activeGW.deletePortMapping(port, PROTOCOL);
		}
		
		if (_activeGW.addPortMapping(port, port, localAddress.getHostAddress(), PROTOCOL, description))
		{
			_log.info("Mapping successful on " + localAddress.getHostAddress() + ":" + port);
		}
		else
		{
			_log.info("Mapping failed on [" + localAddress.getHostAddress() + ":" + port + "] - Already mapped?");
		}
	}
	
	private void deletePortMapping(int port) throws IOException, SAXException
	{
		if (_activeGW.deletePortMapping(port, PROTOCOL))
		{
			_log.info("Mapping was deleted from [" + _activeGW.getLocalAddress().getHostAddress() + ":" + port + "]");
		}
	}
	
	private static class SingletonHolder
	{
		protected static final UPnPService _instance = new UPnPService();
	}
}
