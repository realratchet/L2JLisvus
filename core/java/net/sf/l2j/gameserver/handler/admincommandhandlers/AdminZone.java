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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.util.List;
import java.util.StringTokenizer;

import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2WorldRegion;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.util.StringUtil;

public class AdminZone implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_zone_check",
		"admin_zone_reload",
		"admin_zone_visual",
		"admin_zone_visual_clear",
	};
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IAdminCommandHandler#useAdminCommand(java.lang.String, net.sf.l2j.gameserver.model.L2PcInstance)
	 */
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (activeChar == null)
		{
			return false;
		}
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		if (actualCommand.equalsIgnoreCase("admin_zone_check"))
		{
			showHtml(activeChar);
			activeChar.sendMessage("MapRegion: x:" + MapRegionTable.getInstance().getMapRegionX(activeChar.getX()) + " y:" + MapRegionTable.getInstance().getMapRegionY(activeChar.getY()) + " ("+MapRegionTable.getInstance().getMapRegion(activeChar.getX(),activeChar.getY())+")");
			getGeoRegionXY(activeChar);
			activeChar.sendMessage("Closest Town: " + MapRegionTable.getInstance().getClosestTownName(activeChar));
			
			Location loc;
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.Castle);
			if (loc != null)
			{
				activeChar.sendMessage("TeleToLocation (Castle): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			}
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.ClanHall);
			if (loc != null)
			{
				activeChar.sendMessage("TeleToLocation (ClanHall): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			}
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.SiegeFlag);
			if (loc != null)
			{
				activeChar.sendMessage("TeleToLocation (SiegeFlag): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			}
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.Town);
			if (loc != null)
			{
				activeChar.sendMessage("TeleToLocation (Town): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			}
		}
		else if (actualCommand.equalsIgnoreCase("admin_zone_reload"))
		{
			ZoneManager.getInstance().reload();
			activeChar.sendMessage("All Zones have been reloaded.");
		}
		else if (actualCommand.equalsIgnoreCase("admin_zone_visual"))
		{
			String next = st.nextToken();
			if (next.equalsIgnoreCase("all"))
			{
				List<L2ZoneType> zones = ZoneManager.getInstance().getZones(activeChar);
				for (L2ZoneType zone : zones)
				{
					zone.visualizeZone(activeChar.getZ());
				}
				showHtml(activeChar);
			}
			else
			{
				int zoneId = Integer.parseInt(next);
				ZoneManager.getInstance().getZoneById(zoneId).visualizeZone(activeChar.getZ());
			}
		}
		else if (actualCommand.equalsIgnoreCase("admin_zone_visual_clear"))
		{
			ZoneManager.getInstance().clearDebugItems();
			showHtml(activeChar);
		}
		return true;
	}
	
	private void showHtml(L2PcInstance activeChar)
	{
		final String htmContent = HtmCache.getInstance().getHtm("data/html/admin/zone.htm");
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(0);
		adminReply.setHtml(htmContent);
		adminReply.replace("%PEACE%", (activeChar.isInsideZone(L2Character.ZONE_PEACE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%PVP%", (activeChar.isInsideZone(L2Character.ZONE_PVP) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%SIEGE%", (activeChar.isInsideZone(L2Character.ZONE_SIEGE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%TOWN%", (activeChar.isInsideZone(L2Character.ZONE_TOWN) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%NOHQ%", (activeChar.isInsideZone(L2Character.ZONE_NO_HQ) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%CLANHALL%", (activeChar.isInsideZone(L2Character.ZONE_CLAN_HALL) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%NOLAND%", (activeChar.isInsideZone(L2Character.ZONE_NO_LANDING) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%BOSS%", (activeChar.isInsideZone(L2Character.ZONE_BOSS) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%WATER%", (activeChar.isInsideZone(L2Character.ZONE_WATER) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%MOTHERTREE%", (activeChar.isInsideZone(L2Character.ZONE_MOTHER_TREE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%MONSTERTRACK%", (activeChar.isInsideZone(L2Character.ZONE_MONSTER_TRACK) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%DANGER%", (activeChar.isInsideZone(L2Character.ZONE_DANGER_AREA) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%NOSTORE%", (activeChar.isInsideZone(L2Character.ZONE_NO_STORE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%JAIL%", (activeChar.isInsideZone(L2Character.ZONE_JAIL) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		
		StringBuilder zones = new StringBuilder(100);
		L2WorldRegion region = L2World.getInstance().getRegion(activeChar.getX(), activeChar.getY());
		for (L2ZoneType zone : region.getZones())
		{
			if (zone.isCharacterInZone(activeChar))
			{
				String name = zone.getName() != null ? zone.getName() : zone.getClass().getSimpleName().substring(2);
				StringUtil.append(zones, name);
				if (zone.getId() < 300000)
				{
					StringUtil.append(zones, " (", String.valueOf(zone.getId()), ")");
				}
				StringUtil.append(zones, "<br1>");
			}
		}
		adminReply.replace("%ZLIST%", zones.toString());
		activeChar.sendPacket(adminReply);
	}
	
	private void getGeoRegionXY(L2PcInstance activeChar)
	{
		int worldX = activeChar.getX();
		int worldY = activeChar.getY();
		int geoX = (worldX - L2World.MAP_MIN_X) / L2World.TILE_SIZE + L2World.TILE_X_MIN;
		int geoY = (worldY - L2World.MAP_MIN_Y) / L2World.TILE_SIZE + L2World.TILE_Y_MIN;
		activeChar.sendMessage("GeoRegion: " + geoX + "_" + geoY + "");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IAdminCommandHandler#getAdminCommandList()
	 */
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}