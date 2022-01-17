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

import java.util.Set;

import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.AutoSpawnHandler;
import net.sf.l2j.gameserver.model.AutoSpawnHandler.AutoSpawnInstance;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Admin Command Handler for Mammon NPCs
 * 
 * @author Tempy
 */
public class AdminMammon implements IAdminCommandHandler
{
    private static String[] _adminCommands = 
    {
    	"admin_mammon_find",
    	"admin_mammon_respawn",
        "admin_list_spawns",
        "admin_msg"
    };
    
    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        boolean _isSealValidation = SevenSigns.getInstance().isSealValidationPeriod();
        AutoSpawnInstance blackSpawnInst = AutoSpawnHandler.getInstance().getAutoSpawnInstance(SevenSigns.MAMMON_BLACKSMITH_ID, false);
        AutoSpawnInstance merchSpawnInst = AutoSpawnHandler.getInstance().getAutoSpawnInstance(SevenSigns.MAMMON_MERCHANT_ID, false);

        if (command.startsWith("admin_mammon_find"))
        {
            try
            {
            	int teleportIndex = -1;
                if (command.length() > 17)
                {
                	teleportIndex = Integer.parseInt(command.substring(18));
                }
                
                if (!_isSealValidation)
                {
                    activeChar.sendMessage("The competition period is currently in effect.");
                    return true;
                }

                L2NpcInstance[] blackInst = blackSpawnInst.getNPCInstanceList();
                L2NpcInstance[] merchInst = merchSpawnInst.getNPCInstanceList();

                if (blackInst.length > 0)
                {
                    activeChar.sendMessage("Blacksmith of Mammon: " + blackInst[0].getX() + " " + blackInst[0].getY() + " " + blackInst[0].getZ());
                    if (teleportIndex == 0)
                        activeChar.teleToLocation(blackInst[0].getX(), blackInst[0].getY(), blackInst[0].getZ(), true);
                }
                else
                {
                	activeChar.sendMessage("Blacksmith of Mammon: No spawns found.");
                }

                if (merchInst.length > 0)
                {
                    activeChar.sendMessage("Merchant of Mammon: " + merchInst[0].getX() + " " + merchInst[0].getY() + " " + merchInst[0].getZ());
                    if (teleportIndex == 1)
                        activeChar.teleToLocation(merchInst[0].getX(), merchInst[0].getY(), merchInst[0].getZ(), true);
                }
                else
                {
                	activeChar.sendMessage("Merchant of Mammon: No spawns found.");
                }
            }
            catch (Exception e)
            {
                activeChar.sendMessage("Command format is //mammon_find <teleportIndex> (where 1 = Blacksmith, 2 = Merchant)");
            }
        }
        
        else if (command.startsWith("admin_mammon_respawn"))
        {
            if (!_isSealValidation)
            {
                activeChar.sendMessage("The competition period is currently in effect.");
                return true;
            }

            long blackRespawn = AutoSpawnHandler.getInstance().getTimeToNextSpawn(blackSpawnInst);
            long merchRespawn = AutoSpawnHandler.getInstance().getTimeToNextSpawn(merchSpawnInst);

            activeChar.sendMessage("The Merchant of Mammon will respawn in " + (merchRespawn / 60000) + " minute(s).");
            activeChar.sendMessage("The Blacksmith of Mammon will respawn in " + (blackRespawn / 60000) + " minute(s).");
        }
        
        else if (command.startsWith("admin_list_spawns"))
        {
            try
            {
            	String[] params = command.split(" ");
                int npcId = Integer.parseInt(params[1]);

                int teleportIndex = -1;
                if (params.length > 2)
                {
                	teleportIndex = Integer.parseInt(params[2]);
                }
                
                Set<L2Spawn> spawns = SpawnTable.getInstance().getSpawns(npcId);
                
                int i = 0;
                for (L2Spawn spawn : spawns)
                {
                	i++;
                	if (teleportIndex > -1)
                	{
                		if (teleportIndex == i)
                		{
                            activeChar.teleToLocation(spawn.getLocX(), spawn.getLocY(), spawn.getLocZ(), true);
                		}
                	}
                	else
                    {
                        activeChar.sendMessage(i + " - " + spawn.getTemplate().name + " (" + spawn.getId() + "): " + spawn.getLocX() + " " + spawn.getLocY() + " " + spawn.getLocZ());
                    }
                }
            }
            catch (Exception e)
            {
                activeChar.sendPacket(SystemMessage.sendString("Command format is //list_spawns <NPC_ID> <TELE_INDEX>"));
            }
        }
        // Used for testing SystemMessage IDs	- Use //msg <ID>
        else if (command.startsWith("admin_msg"))
        {
            int msgId = -1;
            try
            {
                msgId = Integer.parseInt(command.substring(10).trim());
            }
            catch (Exception e)
            {
                activeChar.sendMessage("Command format: //msg <SYSTEM_MSG_ID>");
                return true;
            }

            activeChar.sendPacket(new SystemMessage(msgId));
        }

        return true;
    }

    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}