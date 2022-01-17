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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.TownManager;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.type.L2TownZone;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.StringUtil;

/**
 * This class handles following admin commands:
 * - show_moves
 * - show_teleport
 * - teleport_to_character
 * - move_to
 * - teleport_character
 * 
 * @version $Revision: 1.3.2.6.2.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminTeleport implements IAdminCommandHandler
{
    private static final Logger _log = Logger.getLogger(AdminTeleport.class.getName());

    private static String[] _adminCommands =
    {
        "admin_show_moves",
        "admin_show_moves_other",
        "admin_show_teleport",
        "admin_teleport_to_character",
        "admin_teleportto",
        "admin_instant_move",
        "admin_move_to",
        "admin_teleport_character",
        "admin_recall",
        "admin_walk",
        "admin_recall_npc",
        "admin_gonorth",
        "admin_gosouth",
        "admin_goeast",
        "admin_gowest",
        "admin_goup",
        "admin_godown",
        "admin_tele",
        "admin_teleto",
        "admin_failed",
        "admin_sendhome"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
    	String targetReference = (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target");
        GMAudit.auditGMAction(activeChar.getName(), command, targetReference, "");

        if (command.equals("admin_teleto"))
        {
            activeChar.setTeleMode(1);
        }
        else if (command.equals("admin_instant_move"))
		{
			activeChar.sendMessage("Instant move ready. Click where you want to go.");
			activeChar.setTeleMode(1);
		}
        else if (command.equals("admin_teleto r"))
        {
            activeChar.setTeleMode(2);
        }
        else if (command.equals("admin_teleto end"))
        {
            activeChar.setTeleMode(0);
        }
        else if (command.equals("admin_show_moves"))
        {
            showHelpPage(activeChar, "teleports.htm");
        }
        else if (command.equals("admin_show_moves_other"))
        {
            showHelpPage(activeChar, "tele/other.html");
        }
        else if (command.equals("admin_show_teleport"))
        {
            showTeleportCharWindow(activeChar);
        }
        else if (command.equals("admin_recall_npc"))
        {
            recallNPC(activeChar);
        }
        else if (command.equals("admin_teleport_to_character"))
        {
            teleportToCharacter(activeChar, activeChar.getTarget());
        }
        else if (command.startsWith("admin_walk"))
        {
            try
            {
                String val = command.substring(11);
                StringTokenizer st = new StringTokenizer(val);
                String x1 = st.nextToken();
                int x = Integer.parseInt(x1);
                String y1 = st.nextToken();
                int y = Integer.parseInt(y1);
                String z1 = st.nextToken();
                int z = Integer.parseInt(z1);
                Location loc = new Location(x, y, z);
                activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, loc);
            }
            catch (Exception e)
            {
                if (Config.DEBUG) _log.info("admin_walk: "+e);
            }
        }
        else if (command.startsWith("admin_move_to"))
        {
            try
            {
                String val = command.substring(14);
                teleportTo(activeChar, val);
            }
            catch (StringIndexOutOfBoundsException e)
            {
                // Case of empty coordinates
                activeChar.sendMessage("Wrong or no Co-ordinates given.");
            }		
        }
        else if (command.startsWith("admin_teleport_character"))
        {
            try
            {
                String val = command.substring(25);
                teleportCharacter(activeChar, val);
            }
            catch (StringIndexOutOfBoundsException e)
            {
                // Case of empty coordinates
                activeChar.sendMessage("Wrong or no co-ordinates given.");
                showTeleportCharWindow(activeChar); //back to character teleport
            }
        }
        else if (command.startsWith("admin_teleportto "))
        {
            try
            {
                String targetName = command.substring(17);
                L2PcInstance player = L2World.getInstance().getPlayer(targetName);
                teleportToCharacter(activeChar, player);
            }
            catch (StringIndexOutOfBoundsException e)
            {
            }
        }
        else if (command.startsWith("admin_recall "))
        {
            try
            {
                String[] param = command.split(" ");
                if (param.length != 2)
                {
                    activeChar.sendMessage("Usage: //recall <playername>");
                    return false;
                }
                String targetName = param[1];
                L2PcInstance player = L2World.getInstance().getPlayer(targetName);
                if (player != null)
            	    teleportCharacter(player, activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar);
                else
                    changeCharacterPosition(activeChar, targetName);
            }
            catch (StringIndexOutOfBoundsException e)
            {
            }
        }
        else if (command.startsWith("admin_failed"))
        {
            activeChar.sendMessage("Trying ActionFailed...");
            activeChar.sendPacket(new ActionFailed());
        }
        else if (command.equals("admin_tele"))
        {
            showTeleportWindow(activeChar);
        }
        else if (command.equals("admin_goup"))
        {
            int x = activeChar.getX();
            int y = activeChar.getY();
            int z = activeChar.getZ()+150;
            activeChar.teleToLocation(x, y, z, false);
            showTeleportWindow(activeChar);
        }
        else if (command.startsWith("admin_goup"))
        {
            try
            {
            	String val = command.substring(11);
                int intVal = Integer.parseInt(val);
                int x = activeChar.getX();
                int y = activeChar.getY();
                int z = activeChar.getZ()+intVal;
                activeChar.teleToLocation(x, y, z, false);
                showTeleportWindow(activeChar);
            }
            catch (StringIndexOutOfBoundsException e) {}
            catch (NumberFormatException nfe) {}
        }
        else if (command.equals("admin_godown"))
        {
            int x = activeChar.getX();
            int y = activeChar.getY();
            int z = activeChar.getZ();
            activeChar.teleToLocation(x, y, z - 150, false);
            showTeleportWindow(activeChar);
        }
        else if (command.startsWith("admin_godown"))
        {
            try
            {
            	String val = command.substring(13);
                int intVal = Integer.parseInt(val);
                int x = activeChar.getX();
                int y = activeChar.getY();
                int z = activeChar.getZ()-intVal;
                activeChar.teleToLocation(x, y, z, false);
                showTeleportWindow(activeChar);
            }
            catch (StringIndexOutOfBoundsException e) {}
            catch (NumberFormatException nfe) {}
        }
        else if (command.equals("admin_goeast"))
        {
            int x = activeChar.getX();
            int y = activeChar.getY();
            int z = activeChar.getZ();
            activeChar.teleToLocation(x+150, y, z, false);
            showTeleportWindow(activeChar);
        }
        else if (command.startsWith("admin_goeast"))
        {
            try
            {
            	String val = command.substring(13);
                int intVal = Integer.parseInt(val);
                int x = activeChar.getX()+intVal;
                int y = activeChar.getY();
                int z = activeChar.getZ();
                activeChar.teleToLocation(x, y, z, false);
                showTeleportWindow(activeChar);
            }
            catch (StringIndexOutOfBoundsException e) {}
            catch (NumberFormatException nfe) {}
        }
        else if (command.equals("admin_gowest"))
        {
            int x = activeChar.getX();
            int y = activeChar.getY();
            int z = activeChar.getZ();
            activeChar.teleToLocation(x-150, y, z, false);
            showTeleportWindow(activeChar);
        }
        else if (command.startsWith("admin_gowest"))
        {
            try
            {
            	String val = command.substring(13);
                int intVal = Integer.parseInt(val);
                int x = activeChar.getX()-intVal;
                int y = activeChar.getY();
                int z = activeChar.getZ();
                activeChar.teleToLocation(x, y, z, false);
                showTeleportWindow(activeChar);
            }
            catch (StringIndexOutOfBoundsException e) {}
            catch (NumberFormatException nfe) {}
        }
        else if (command.equals("admin_gosouth"))
        {
            int x = activeChar.getX();
            int y = activeChar.getY()+150;
            int z = activeChar.getZ();
            activeChar.teleToLocation(x, y, z, false);
            showTeleportWindow(activeChar);
        }
        else if (command.startsWith("admin_gosouth"))
        {
            try
            {
            	String val = command.substring(14);
                int intVal = Integer.parseInt(val);
                int x = activeChar.getX();
                int y = activeChar.getY()+intVal;
                int z = activeChar.getZ();
                activeChar.teleToLocation(x, y, z, false);
                showTeleportWindow(activeChar);
            }
            catch (StringIndexOutOfBoundsException e) {}
            catch (NumberFormatException nfe) {}
        }
        else if (command.equals("admin_gonorth"))
        {
            int x = activeChar.getX();
            int y = activeChar.getY();
            int z = activeChar.getZ();
            activeChar.teleToLocation(x, y-150, z, false);
            showTeleportWindow(activeChar);
        }
        else if (command.startsWith("admin_gonorth"))
        {
            try
            {
            	String val = command.substring(14);
                int intVal = Integer.parseInt(val);
                int x = activeChar.getX();
                int y = activeChar.getY()-intVal;
                int z = activeChar.getZ();
                activeChar.teleToLocation(x, y, z, false);
                showTeleportWindow(activeChar);
            }
            catch (StringIndexOutOfBoundsException e) {}
            catch (NumberFormatException nfe) {}
        }
        else if (command.startsWith("admin_sendhome"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken(); // Skip command
			if (st.countTokens() > 1)
			{
				activeChar.sendMessage("Usage: //sendhome <playername>");
			}
			else if (st.countTokens() == 1)
			{
				final String name = st.nextToken();
				final L2PcInstance player = L2World.getInstance().getPlayer(name);
				if (player == null)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_NOT_FOUND_IN_THE_GAME));
					return false;
				}
				teleportHome(player);
			}
			else
			{
				final L2Object target = activeChar.getTarget();
				if (target instanceof L2PcInstance)
				{
					teleportHome(target.getActingPlayer());
				}
				else
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
				}
			}
		}
        
        return true;
    }
    
    private void teleportTo(L2PcInstance activeChar, String coords)
    {
        try
        {
            StringTokenizer st = new StringTokenizer(coords);
            String x1 = st.nextToken();
            int x = Integer.parseInt(x1);
            String y1 = st.nextToken();
            int y = Integer.parseInt(y1);
            String z1 = st.nextToken();
            int z = Integer.parseInt(z1);
            
            activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            activeChar.teleToLocation(x, y, z, false);
            
            activeChar.sendMessage("You have been teleported to " + coords);
        }
        catch (NoSuchElementException nsee)
        {
            activeChar.sendMessage("Wrong or no co-ordinates given.");
        }
    }
    
    private void showTeleportWindow(L2PcInstance activeChar)
    {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5); 
        
        String replyMSG = StringUtil.concat(
        	"<html><body>",
        	"<center><table width=260><tr><td width=40>",
        	"<button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</td><td width=180>",
        	"<center>Teleport Menu</center>",
        	"</td><td width=40>",
        	"</td></tr></table><br>",
        	"<table>",
        	"<tr><td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"North\" action=\"bypass -h admin_gonorth\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"Up\" action=\"bypass -h admin_goup\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"<tr><td><button value=\"West\" action=\"bypass -h admin_gowest\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"East\" action=\"bypass -h admin_goeast\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"<tr><td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"South\" action=\"bypass -h admin_gosouth\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"Down\" action=\"bypass -h admin_godown\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"</table></center>",
        	"</body></html>");
        
        adminReply.setHtml(replyMSG);
        activeChar.sendPacket(adminReply);			
    }
    
    private void showTeleportCharWindow(L2PcInstance activeChar)
    {
        L2Object target = activeChar.getTarget();
        L2PcInstance player = null;
        if (target instanceof L2PcInstance) 
        {
            player = (L2PcInstance)target;
        } 
        else 
        {
            activeChar.sendMessage("Incorrect target.");
            return;
        }
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5); 
        
        String replyMSG = StringUtil.concat(
        	"<html><body>",
        	"<center><table width=260><tr><td width=40>",
        	"<button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</td><td width=180>",
        	"<center>Teleport Character</center>",
        	"</td><td width=40>",
        	"<button value=\"Back\" action=\"bypass -h admin_current_player\" width=45 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</td></tr></table><br><br>",
        	"The character you will teleport is " + player.getName() + ".<br><br>",
        	"Co-ordinate x",
        	"<edit var=\"char_cord_x\" width=110><br>",
        	"Co-ordinate y",
        	"<edit var=\"char_cord_y\" width=110><br>",
        	"Co-ordinate z",
        	"<edit var=\"char_cord_z\" width=110>",
        	"<br>",
        	"<button value=\"Teleport\" action=\"bypass -h admin_teleport_character $char_cord_x $char_cord_y $char_cord_z\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"<button value=\"Teleport near you\" action=\"bypass -h admin_teleport_character " + activeChar.getX() + " " + activeChar.getY() + " " + activeChar.getZ() + "\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</center>",
        	"</body></html>");
        
        adminReply.setHtml(replyMSG);
        activeChar.sendPacket(adminReply);			
    }
    
    private void teleportCharacter(L2PcInstance activeChar, String coords)
    {
        L2Object target = activeChar.getTarget();
        L2PcInstance player = null;
        if (target instanceof L2PcInstance) 
        {
            player = (L2PcInstance)target;
        } 
        else 
        {
            activeChar.sendMessage("Incorrect target.");
            return;
        }
        
        if (player.getObjectId() == activeChar.getObjectId())
        {
            player.sendMessage("You cannot teleport your character.");
        }
        else
        {
            try
            {
                StringTokenizer st = new StringTokenizer(coords);
                String x1 = st.nextToken();
                int x = Integer.parseInt(x1);
                String y1 = st.nextToken();
                int y = Integer.parseInt(y1);
                String z1 = st.nextToken();
                int z = Integer.parseInt(z1);
                teleportCharacter(player, x, y, z, activeChar);
            }
            catch (NoSuchElementException nsee) {}
        }
    }
    
    /**
     * @param player
     * @param x
     * @param y
     * @param z
     * @param activeChar
     */
    private void teleportCharacter(L2PcInstance player, int x, int y, int z, L2PcInstance activeChar)
    {
        if (player != null)
        {
            // Check for jail
            if (player.isInJail())
            {
                activeChar.sendMessage("Sorry, player " + player.getName() + " is in Jail.");
                return;
            }

            // Information
            if (activeChar != null)
            {
                activeChar.sendMessage("You have recalled " + player.getName());
            }
            player.sendMessage("Admin is teleporting you.");
            
            player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            player.teleToLocation(x, y, z, true);
        }
    }

    private void changeCharacterPosition(L2PcInstance activeChar, String name)
    {
        final int x = activeChar.getX();
        final int y = activeChar.getY();
        final int z = activeChar.getZ();

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=? WHERE char_name=?"))
        {
            statement.setInt(1, x);
            statement.setInt(2, y);
            statement.setInt(3, z);
            statement.setString(4, name);
            statement.execute();
            int count = statement.getUpdateCount();
            if (count == 0)
                activeChar.sendMessage("Character not found or position is not altered.");
            else
                activeChar.sendMessage("Player's [" + name + "] position is now set to (" + x + "," + y + "," + z + ").");
        }
        catch (SQLException se)
        {
            activeChar.sendMessage("SQLException while changing offline character's position");
        }
    }

    private void teleportToCharacter(L2PcInstance activeChar, L2Object target)
    {
        L2PcInstance player = null;
        if (target != null && target instanceof L2PcInstance) 
        {
            player = (L2PcInstance)target;
        } 
        else 
        {
            activeChar.sendMessage("Incorrect target.");
            return;
        }
        
        if (player.getObjectId() == activeChar.getObjectId())
        {	
            activeChar.sendMessage("You cannot self teleport.");
        }
        else
        {
            int x = player.getX();
            int y = player.getY();
            int z = player.getZ();
            
            activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            activeChar.teleToLocation(x, y, z, true);
            
            activeChar.sendMessage("You have teleported to character " + player.getName() + ".");
        }
    }
    
    private void recallNPC(L2PcInstance activeChar)
    {
        L2Object obj = activeChar.getTarget();
        if (obj != null && obj instanceof L2NpcInstance)
        {
            L2NpcInstance target = (L2NpcInstance) obj;

            int monsterTemplate = target.getTemplate().npcId;
            L2NpcTemplate template1 = NpcTable.getInstance().getTemplate(monsterTemplate);
            if (template1 == null)
            {
                activeChar.sendMessage("Incorrect monster template.");
                _log.warning("ERROR: NPC " + target.getObjectId() + " has a 'null' template.");
                return;
            }

            L2Spawn spawn = target.getSpawn();
            if (spawn == null)
            {
                activeChar.sendMessage("Incorrect monster spawn.");
                _log.warning("ERROR: NPC " + target.getObjectId() + " has a 'null' spawn.");
                return;
            }

            int respawnTime = spawn.getRespawnDelay();
            
            target.deleteMe();
            spawn.stopRespawn();
            SpawnTable.getInstance().deleteSpawn(spawn, true);
            
            try
            {
                spawn = new L2Spawn(template1);
                spawn.setLocX(activeChar.getX());
                spawn.setLocY(activeChar.getY());
                spawn.setLocZ(activeChar.getZ());
                spawn.setAmount(1);
                spawn.setHeading(activeChar.getHeading());
                spawn.setRespawnDelay(respawnTime);
                SpawnTable.getInstance().addNewSpawn(spawn, true);
                spawn.init();
                
                activeChar.sendMessage("Created " + template1.name + " on " + target.getObjectId() + ".");
                
                if (Config.DEBUG)
                {
                    _log.fine("Spawn at X="+spawn.getLocX()+" Y="+spawn.getLocY()+" Z="+spawn.getLocZ());
                    _log.warning("GM: "+activeChar.getName()+"("+activeChar.getObjectId()+") moved NPC " + target.getObjectId());
                }
            }
            catch (Exception e)
            {
                activeChar.sendMessage("Target is not in game.");
            }
        }
        else
        {
            activeChar.sendMessage("Incorrect target.");
        }
    }
    
    /**
	 * This method sends a player to it's home town.
	 * 
	 * @param player the player to teleport.
	 */
	private void teleportHome(L2PcInstance player)
	{
		int townId = 0;
		switch (player.getRace())
		{
			case ELF:
				townId = 3;
				break;
			case DARK_ELF:
				townId = 1;
				break;
			case ORC:
				townId = 4;
				break;
			case DWARF:
				townId = 6;
				break;
			default:
				townId = 2;
				break;
		}
		
		L2TownZone zone = TownManager.getTown(townId);
		if (zone != null)
		{
			player.teleToLocation(zone.getSpawnLoc(), true);
		}
	}
    
    @Override
	public String[] getAdminCommandList() 
    {
        return _adminCommands;
    }
}