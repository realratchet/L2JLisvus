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
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.DayNightSpawnManager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.StringUtil;

/**
 * This class handles following admin commands: - show_spawns = shows menu -
 * spawn_index lvl = shows menu for monsters with respective level -
 * spawn_monster id = spawns monster id on target
 * 
 * @version $Revision: 1.2.2.5.2.5 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminSpawn implements IAdminCommandHandler
{
	public static Logger _log = Logger.getLogger(AdminSpawn.class.getName());
	
    private static String[] _adminCommands =
    {
        "admin_show_spawns",
        "admin_spawn",
        "admin_spawn_monster",
        "admin_spawn_once",
        "admin_spawn_index",
        "admin_unspawnall",
        "admin_respawnall",
        "admin_spawn_reload",
        "admin_npc_index",
        "admin_show_npcs",
        "admin_teleport_reload",
        "admin_spawnnight",
        "admin_spawnday"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.equals("admin_show_spawns"))
        {
            showHelpPage(activeChar, "spawns.htm");
        }
        else if (command.startsWith("admin_spawn_index"))
        {
            StringTokenizer st = new StringTokenizer(command, " ");
            try
            {
                st.nextToken();
            	final int level = Integer.parseInt(st.nextToken());
                final int from = st.hasMoreTokens() ? Integer.parseInt(st.nextToken()) : 0;
                showMonsters(activeChar, level, from);
            }
            catch (Exception e)
            {
                showHelpPage(activeChar, "spawns.htm");
            }
        }
        else if (command.equals("admin_show_npcs"))
        {
            showHelpPage(activeChar, "npcs.htm");
        }
        else if (command.startsWith("admin_npc_index"))
        {
            StringTokenizer st = new StringTokenizer(command, " ");
            try
            {
                st.nextToken();
                final String letter = st.nextToken();
                final int from = st.hasMoreTokens() ? Integer.parseInt(st.nextToken()) : 0;
                showNpcs(activeChar, letter, from);
            }
            catch (Exception e)
            {
                showHelpPage(activeChar, "npcs.htm");
            }
        }
        else if (command.startsWith("admin_spawn ") || command.startsWith("admin_spawn_monster") || command.startsWith("admin_spawn_once"))
        {
            StringTokenizer st = new StringTokenizer(command, " ");
            try
            {
            	String cmd = st.nextToken();
                String id = st.nextToken();
                int respawnTime = 0;
                int mobCount = 1;
                
                if (st.hasMoreTokens())
                {
                    mobCount = Integer.parseInt(st.nextToken());
                }
                if (st.hasMoreTokens())
                {
                    respawnTime = Integer.parseInt(st.nextToken());
                }
                
                if (cmd.equalsIgnoreCase("admin_spawn_once"))
                {
                	spawnMonster(activeChar, id, respawnTime, mobCount, false);
                }
                else
                {
                	spawnMonster(activeChar, id, respawnTime, mobCount, respawnTime > 0);
                }
            }
            catch (Exception e)
            {
                // Case of wrong monster data
            }
        }
        else if (command.startsWith("admin_unspawnall"))
        {
            for (L2PcInstance player : L2World.getInstance().getAllPlayers())
            {
                player.sendPacket(new SystemMessage(SystemMessage.NPC_SERVER_NOT_OPERATING));
            }

            RaidBossSpawnManager.getInstance().cleanUp();
            DayNightSpawnManager.getInstance().cleanUp();
            L2World.getInstance().deleteVisibleNpcSpawns();
            GmListTable.getInstance().broadcastMessageToGMs("NPC Unspawn completed!");
        }
        else if (command.startsWith("admin_spawnday"))
        {
        	DayNightSpawnManager.getInstance().changeMode(0);
            
        }
        else if (command.startsWith("admin_spawnnight"))
        {
        	DayNightSpawnManager.getInstance().changeMode(1);
        }
        else if (command.startsWith("admin_respawnall") || command.startsWith("admin_spawn_reload"))
        {
            // make sure all spawns are deleted
            RaidBossSpawnManager.getInstance().cleanUp();
            DayNightSpawnManager.getInstance().cleanUp();
            L2World.getInstance().deleteVisibleNpcSpawns();
        	
            // now respawn all
            NpcTable.getInstance().reloadAllNpc();
            SpawnTable.getInstance().reloadAll();
            RaidBossSpawnManager.getInstance().reloadBosses();
            DayNightSpawnManager.getInstance().notifyChangeMode();
            QuestManager.getInstance().reloadAllQuests();
            GmListTable.getInstance().broadcastMessageToGMs("NPC Respawn completed!");
        }
        else if (command.startsWith("admin_teleport_reload"))
        {
            TeleportLocationTable.getInstance().reloadAll();
            GmListTable.getInstance().broadcastMessageToGMs("Teleport List Table reloaded.");
        }
        return true;
    }

    private void spawnMonster(L2PcInstance activeChar, String monsterId, int respawnTime, int mobCount, boolean respawn)
    {
        L2Object target = activeChar.getTarget();
        if (target == null)
            target = activeChar;

        L2NpcTemplate template1;
        if (monsterId.matches("[0-9]*"))
        {
            //First parameter was an ID number
            int monsterTemplate = Integer.parseInt(monsterId);
            template1 = NpcTable.getInstance().getTemplate(monsterTemplate);
        }
        else
        {
            // First parameter wasn't just numbers so go by name not ID
            monsterId = monsterId.replace('_', ' ');
            template1 = NpcTable.getInstance().getTemplateByName(monsterId);
        }

        try
        {
            L2Spawn spawn = new L2Spawn(template1);
            if (Config.SAVE_GMSPAWN_ON_CUSTOM)
                spawn.setCustom(true);

            spawn.setLocX(target.getX());
            spawn.setLocY(target.getY());
            spawn.setLocZ(target.getZ());
            spawn.setAmount(mobCount);
            spawn.setHeading(activeChar.getHeading());
            spawn.setRespawnDelay(respawnTime);

            if (RaidBossSpawnManager.getInstance().isDefined(spawn.getNpcId()))
                activeChar.sendMessage("You cannot spawn another instance of " + template1.name + ".");
            else
            {
                if (RaidBossSpawnManager.getInstance().getValidTemplate(spawn.getNpcId()) != null)
                {
                    spawn.setRespawnMinDelay(43200);
                    spawn.setRespawnMaxDelay(129600);
                    RaidBossSpawnManager.getInstance().addNewSpawn(spawn, 0, template1.baseHpMax, template1.baseMpMax, true);
                }
                else
                {
                    SpawnTable.getInstance().addNewSpawn(spawn, true);
                    spawn.init();
                }
                
                if (!respawn)
                {
                	spawn.stopRespawn();
                }

                activeChar.sendMessage("Created " + template1.name + " on " + target.getObjectId() + ".");
            } 
        }
        catch (Exception e)
        {
            activeChar.sendMessage("Target is not ingame.");
        }
    }

    private void showMonsters(L2PcInstance activeChar, int level, int from)
    {
    	L2NpcTemplate[] mobs = NpcTable.getInstance().getAllMonstersOfLevel(level);
    	
    	StringBuilder sb = new StringBuilder(500 + mobs.length * 80);

    	// Start
    	StringUtil.append(sb, "<html><title>Spawn Monster:</title><body><p> Level "+level+":<br>Total Npc's : "+mobs.length+"<br>");
    	String end1 = "<br><center><button value=\"Next\" action=\"bypass -h admin_spawn_index "+level+" $from$\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></body></html>";
    	String end2 = "<br><center><button value=\"Back\" action=\"bypass -h admin_show_spawns\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></body></html>";

    	// Loop
    	boolean ended = true;
    	for (int i=from; i<mobs.length; i++)
    	{
            String txt = "<a action=\"bypass -h admin_spawn_monster "+mobs[i].npcId+"\">"+mobs[i].name+"</a><br1>";

            if ((sb.length() + txt.length() + end2.length()) > 8192)
            {
                end1 = end1.replace("$from$", ""+i);
                ended = false;
                break;
            }

            StringUtil.append(sb, txt);
        }

        // End
        if (ended)
        	StringUtil.append(sb, end2);
        else
        	StringUtil.append(sb, end1);

        activeChar.sendPacket(new NpcHtmlMessage(5, sb.toString()));
    }

    private void showNpcs(L2PcInstance activeChar, String starting, int from)
    {
        L2NpcTemplate[] mobs = NpcTable.getInstance().getAllNpcStartingWith(starting);
        
        StringBuilder sb = new StringBuilder(500 + mobs.length * 80);

        // Start
        StringUtil.append(sb, "<html><title>Spawn NPC:</title><body><p> There are "+mobs.length+" NPCs whose name starts with "+starting+":<br>");
        String end1 = "<br><center><button value=\"Next\" action=\"bypass -h admin_npc_index "+starting+" $from$\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></body></html>";
        String end2 = "<br><center><button value=\"Back\" action=\"bypass -h admin_show_npcs\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></body></html>";

        // Loop
        boolean ended = true;
        for (int i=from; i<mobs.length; i++)
        {
            String txt = "<a action=\"bypass -h admin_spawn_monster "+mobs[i].npcId+"\">"+mobs[i].name+"</a><br1>";

            if ((sb.length() + txt.length() + end2.length()) > 8192)
            {
                end1 = end1.replace("$from$", ""+i);
                ended = false;
                break;
            }
            StringUtil.append(sb, txt);
        }

        // End
        if (ended)
        	StringUtil.append(sb, end2);
        else
        	StringUtil.append(sb, end1);

        activeChar.sendPacket(new NpcHtmlMessage(5, sb.toString()));
    }
    
    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}