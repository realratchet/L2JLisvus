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
import net.sf.l2j.gameserver.network.serverpackets.AdminForgePacket;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.util.StringUtil;

/**
 * This class handles commands for gm to forge packets
 * 
 * @author Maktakien
 *
 */
public class AdminPForge implements IAdminCommandHandler
{
    private static String[] _adminCommands = {"admin_forge","admin_forge2","admin_forge3" };
    
    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.equals("admin_forge"))
        {
        	showMainPage(activeChar);
        }
        else if (command.startsWith("admin_forge2"))
        {
        	try
    		{
        		StringTokenizer st = new StringTokenizer(command);
        		st.nextToken();
        		String format = st.nextToken();
        		showPage2(activeChar,format);
    		}
        	catch(Exception ex)
        	{
        	}
        }
        else if (command.startsWith("admin_forge3"))
        {
        	  try
              {
                  StringTokenizer st = new StringTokenizer(command);
                  st.nextToken();
                  String format = st.nextToken();
                  boolean broadcast = false;
                  if(format.toLowerCase().equals("broadcast"))
                  {
                	  format = st.nextToken();
                	  broadcast = true;
                  }
                  AdminForgePacket sp = new AdminForgePacket();
                  for(int i = 0; i < format.length();i++)
          		  {
                	  String val = st.nextToken();
                	  if(val.toLowerCase().equals("$objid"))
                	  {
                		 val = String.valueOf(activeChar.getObjectId());
                	  }
                	  else if(val.toLowerCase().equals("$tobjid"))
                	  {
                		  val = String.valueOf(activeChar.getTarget().getObjectId());
                	  }
                	  else if(val.toLowerCase().equals("$bobjid"))
                	  {
                		  if(activeChar.getBoat() != null)
                		  {
                			  val = String.valueOf(activeChar.getBoat().getObjectId());
                		  }                		  
                	  }
                	  else if(val.toLowerCase().equals("$clanid"))
                	  {
                		  val = String.valueOf(activeChar.getCharId());
                	  }
                	  else if(val.toLowerCase().equals("$allyid"))
                	  {
                		  val = String.valueOf(activeChar.getAllyId());
                	  }
                	  else if(val.toLowerCase().equals("$tclanid"))
                	  {
                		  val = String.valueOf(((L2PcInstance) activeChar.getTarget()).getCharId());
                	  }
                	  else if(val.toLowerCase().equals("$tallyid"))
                	  {
                		  val = String.valueOf(((L2PcInstance) activeChar.getTarget()).getAllyId());
                	  }
                	  else if(val.toLowerCase().equals("$x"))
                	  {
                 		 val = String.valueOf(activeChar.getX());
                 	  }
                	  else if(val.toLowerCase().equals("$y"))
                	  {
                 		 val = String.valueOf(activeChar.getY());
                 	  }
                	  else if(val.toLowerCase().equals("$z"))
                	  {
                 		 val = String.valueOf(activeChar.getZ());
                 	  } 
                	  else if(val.toLowerCase().equals("$heading"))
                	  {
                 		 val = String.valueOf(activeChar.getHeading());
                 	  } 
                	  else if(val.toLowerCase().equals("$tx"))
                	  {
                 		 val = String.valueOf(activeChar.getTarget().getX());
                 	  }
                	  else if(val.toLowerCase().equals("$ty"))
                	  {
                 		 val = String.valueOf(activeChar.getTarget().getY());
                 	  }
                	  else if(val.toLowerCase().equals("$tz"))
                	  {
                 		 val = String.valueOf(activeChar.getTarget().getZ());
                 	  }
                	  else if(val.toLowerCase().equals("$theading"))
                	  {
                 		 val = String.valueOf(((L2PcInstance) activeChar.getTarget()).getHeading());
                 	  } 
                	  
                	  sp.addPart(format.getBytes()[i],val);
          		  }
                  if(broadcast == true)
                  {
                	  activeChar.broadcastPacket(sp);
                  }
                  else
                  {
                	  activeChar.sendPacket(sp);
                  }
                  showPage3(activeChar,format,command);
              }
              catch(Exception ex)
              {
            	  ex.printStackTrace();
              }            
        }
        return true;
    }
    
	public void showMainPage(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

		String replyMSG = StringUtil.concat(
			"<html><body>",
			"<center>L2J Forge Panel</center><br>",
			"Format:<edit var=\"format\" width=100><br>",
			"<button value=\"Step2\" action=\"bypass -h admin_forge2 $format\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>",
        	"Only c h d f s b or x work<br>",
        	"</body></html>");

        adminReply.setHtml(replyMSG);
        activeChar.sendPacket(adminReply); 
	}
	
	public void showPage3(L2PcInstance activeChar,String format,String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

		String replyMSG = StringUtil.concat(
			"<html><body>",
			"<center>L2J Forge Panel 3</center><br>",
			"GG !! If you can see this, there was no critical :)<br>",
			"and packet ("+format+") was sent<br><br>",
			"<button value=\"Try again ?\" action=\"bypass -h admin_forge\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
			"<br><br>Debug: cmd string :"+command+"<br>",
        	"</body></html>");

        adminReply.setHtml(replyMSG);
        activeChar.sendPacket(adminReply); 
	}
	
	public void showPage2(L2PcInstance activeChar,String format)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder(500);
		StringUtil.append(replyMSG, "<html><body>");
		StringUtil.append(replyMSG, "<center>L2J Forge Panel 2</center><br>Format:"+format);
		StringUtil.append(replyMSG, "<br>No spaces in values please ;)<br>Decimal values for c h d, a float (with point) for f, a string for s and for x/b the hexadecimal value");
		StringUtil.append(replyMSG, "<br>Values<br>");
		for(int i = 0; i < format.length();i++)
		{
			StringUtil.append(replyMSG, format.charAt(i)+" : <edit var=\"v"+i+"\" width=100> <br>");
		}
		StringUtil.append(replyMSG, "<br><button value=\"Send\" action=\"bypass -h admin_forge3 "+format);
		for(int i = 0; i < format.length();i++)
		{
			StringUtil.append(replyMSG, " $v"+i);
		}		
		StringUtil.append(replyMSG, "\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		
		StringUtil.append(replyMSG, "<br><button value=\"Broadcast\" action=\"bypass -h admin_forge3 broadcast "+format);
		for(int i = 0; i < format.length();i++)
		{
			StringUtil.append(replyMSG, " $v"+i);
		}		
		StringUtil.append(replyMSG, "\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		StringUtil.append(replyMSG, "</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply); 
	}
	
    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}