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

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;

/**
 * This class handles following admin commands:
 * - set_camera <x> <y> <z> <tx> <ty> <tz> [time] [duration]: sets camera at (x,y,z) looking at (tx,ty,tz)
 * - camera_off: returns player to normal view and original position
 */
public class AdminCamera implements IAdminCommandHandler
{
	private static String[] _adminCommands =
	{
		"admin_set_camera",
		"admin_camera_off"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_set_camera"))
		{
			try
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken(); // skip command
				
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				double z = Double.parseDouble(st.nextToken());
				double tx = Double.parseDouble(st.nextToken());
				double ty = Double.parseDouble(st.nextToken());
				double tz = Double.parseDouble(st.nextToken());
				
				int time = 0;
				if (st.hasMoreTokens())
				{
					time = Integer.parseInt(st.nextToken());
				}
				
				int duration = 10000;
				if (st.hasMoreTokens())
				{
					duration = Integer.parseInt(st.nextToken());
				}
				
				// 1. Move player to target position
				if (!activeChar.inObserverMode())
				{
					activeChar.enterObserverMode((int) tx, (int) ty, (int) tz);
				}
				else
				{
					activeChar.teleToLocation((int) tx, (int) ty, (int) tz, false);
				}
				
				// 2. Calculate vector from target to camera
				double dx = x - tx;
				double dy = y - ty;
				double dz = z - tz;
				
				double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
				
				// Calculate Yaw (angle in XY plane)
				// L2 uses degrees for SpecialCamera. 0-360.
				double yaw = Math.atan2(dy, dx) * 180.0 / Math.PI;
				
				// Calculate Pitch (angle from XY plane to Z)
				double pitch = Math.atan2(dz, Math.sqrt(dx * dx + dy * dy)) * 180.0 / Math.PI;
				
				// 3. Send SpecialCamera packet
				activeChar.sendPacket(new SpecialCamera(activeChar.getObjectId(), (int) dist, (int) yaw, (int) pitch, time, duration));
				
				activeChar.sendMessage("Camera set to (" + (int) x + ", " + (int) y + ", " + (int) z + ") looking at (" + (int) tx + ", " + (int) ty + ", " + (int) tz + ")");
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //set_camera <x> <y> <z> <tx> <ty> <tz> [time] [duration]");
			}
		}
		else if (command.equals("admin_camera_off"))
		{
			if (activeChar.inObserverMode())
			{
				activeChar.leaveObserverMode();
				activeChar.sendMessage("Camera mode disabled.");
			}
			else
			{
				activeChar.sendMessage("You are not in camera mode.");
			}
		}
		
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}
