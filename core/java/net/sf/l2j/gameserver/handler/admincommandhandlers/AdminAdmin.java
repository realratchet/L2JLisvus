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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.EventEngine;
import net.sf.l2j.gameserver.TradeController;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.datatables.AdminCommandRightsData;
import net.sf.l2j.gameserver.datatables.BufferTable;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.NpcWalkerRoutesTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.CoupleManager;
import net.sf.l2j.gameserver.instancemanager.Manager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.model.L2Multisell;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.ShortCutInit;
import net.sf.l2j.gameserver.network.serverpackets.SignsSky;
import net.sf.l2j.gameserver.network.serverpackets.SunRise;
import net.sf.l2j.gameserver.network.serverpackets.SunSet;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class handles following admin commands:
 * - admin = shows menu
 * 
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminAdmin implements IAdminCommandHandler
{
    private static String[] _adminCommands =
    {
        "admin_admin","admin_play_sounds","admin_play_sound",
        "admin_gmliston","admin_gmlistoff","admin_silence",
        "admin_atmosphere","admin_diet","admin_tradeoff",
        "admin_reload", "admin_saveolymp", "admin_endolympiad",
        "admin_sethero", "admin_setnoble", "admin_setaio"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.equals("admin_admin"))
        {
            showMainPage(activeChar);
        }
        else if (command.equals("admin_play_sounds"))
        {
            showHelpPage(activeChar, "songs/songs.htm");
        }
        else if (command.startsWith("admin_play_sounds"))
        {
            try
            {
                showHelpPage(activeChar, "songs/songs"+command.substring(17)+".htm");
            }
            catch (StringIndexOutOfBoundsException e) {}
        }
        else if (command.startsWith("admin_play_sound"))
        {
            try
            {
                playAdminSound(activeChar,command.substring(17));
            }
            catch (StringIndexOutOfBoundsException e) {}
        }
        else if (command.startsWith("admin_gmliston"))
        {
            GmListTable.getInstance().showGm(activeChar);
            activeChar.sendMessage("Registered into gm list.");
        }
        else if (command.startsWith("admin_gmlistoff"))
        {
            GmListTable.getInstance().hideGm(activeChar);
            activeChar.sendMessage("Removed from gm list.");
        }
        else if (command.startsWith("admin_silence"))
        {
        	if (activeChar.getMessageRefusal()) // already in message refusal mode
            {
                activeChar.setMessageRefusal(false);
                activeChar.sendPacket(new SystemMessage(SystemMessage.MESSAGE_ACCEPTANCE_MODE));
            }
            else
            {
                activeChar.setMessageRefusal(true);
                activeChar.sendPacket(new SystemMessage(SystemMessage.MESSAGE_REFUSAL_MODE));
            }	    
        }
        else if (command.startsWith("admin_saveolymp"))
        {
            try 
            {
                Olympiad.getInstance().save();
                activeChar.sendMessage("Olympiad data saved!!");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (command.startsWith("admin_endolympiad"))
        {
            try 
            {
                Olympiad.getInstance().manualSelectHeroes();
                activeChar.sendMessage("Heroes were formed.");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (command.startsWith("admin_sethero"))
        {
            L2PcInstance target = activeChar;
            if (activeChar.getTarget() != null && activeChar.getTarget() instanceof L2PcInstance)
                target = (L2PcInstance)activeChar.getTarget();

            target.setHero(target.isHero() ? false : true);
            target.broadcastUserInfo();
        }
        else if (command.startsWith("admin_setnoble"))
        {
            L2PcInstance target = activeChar;
            if (activeChar.getTarget() != null && activeChar.getTarget() instanceof L2PcInstance)
                target = (L2PcInstance)activeChar.getTarget();

            target.setNoble(target.isNoble() ? false : true);

            if (target.isNoble())
                activeChar.sendMessage(target.getName() + " has gained Noblesse status.");
            else
                activeChar.sendMessage(target.getName() + " has lost Noblesse status.");
        }
        else if (command.startsWith("admin_setaio"))
        {
        	if (!Config.AIO_BUFFER_ENABLED)
        	{
        		activeChar.sendMessage("AIO Buffers system is currently disabled. Please enable it in your custom configurations file and try again.");
        		return true;
        	}
        	
        	try
            {
                StringTokenizer st = new StringTokenizer(command);
                st.nextToken();
                String token = st.nextToken();
                
                L2PcInstance target = activeChar;
                if (activeChar.getTarget() != null && activeChar.getTarget() instanceof L2PcInstance)
                    target = (L2PcInstance)activeChar.getTarget();
                
                byte state = Byte.parseByte(token);
                if (state == 1)
                {
                	if (!target.isAIOBuffer())
                	{
                		// Set AIO buffer status and change color name if needed
                		target.setIsAIOBuffer(true);
                		
	                	// Set level to maximum if needed
	                	long pXp = target.getExp();
	            		long tXp = Experience.LEVEL[Config.MAX_PLAYER_LEVEL];
	            		if (pXp < tXp)
	            		{
	            			target.addExpAndSp(tXp - pXp, 0);
	            		}
	            		
	            		// Set name prefix
	            		if (Config.AIO_BUFFER_NAME_PREFIX != null)
	            		{
		            		target.setName(Config.AIO_BUFFER_NAME_PREFIX + target.getName());
		    				
		    				// Store player name
		    				CharNameTable.getInstance().addName(target);
	            		}
	            		
	            		// Restore
	                	target.setCurrentCp(target.getMaxCp());
	                	target.setCurrentHp(target.getMaxHp());
	                	target.setCurrentMp(target.getMaxMp());
                	
	                	if (activeChar != target)
	                    {
	                    	activeChar.sendMessage(target.getName() + " has become an AIO Buffer.");
	                    }
	                    target.sendMessage("You have now become an AIO Buffer.");
                	}
                	else
                	{
                		activeChar.sendMessage(target.getName() + " has already become an AIO Buffer.");
                	}
                }
                else if (state == 0)
                {
                	if (target.isAIOBuffer())
                	{
                		// Remove AIO buffer status
	                	target.setIsAIOBuffer(false);
	                	
                		// Set name prefix
	            		if (Config.AIO_BUFFER_NAME_PREFIX != null)
	            		{
		            		target.setName(target.getName().replace(Config.AIO_BUFFER_NAME_PREFIX, ""));
		    				
		    				// Store player name
		    				CharNameTable.getInstance().addName(target);
	            		}
	            		
	            		// Reset name color
	                	if (!target.isGM())
	                	{
	                		target.getAppearance().setNameColor(0xFFFFFF);
	                	}
	            		
	                	// Remove all character skills for security purposes
	        			target.removeAllSkills(true);
	        			target.restoreShortCuts();
	                	
	                	// Update character skill shortcuts
	                	target.sendPacket(new ShortCutInit(target));
	                	
	                	// Give available skills
	                	target.rewardSkills();
	                	
	                	if (activeChar != target)
	                    {
	                    	activeChar.sendMessage(target.getName() + " has lost AIO Buffer status.");
	                    }
	                    target.sendMessage("You have lost AIO Buffer status.");
                	}
                	else
                	{
                		activeChar.sendMessage(target.getName() + " has already lost AIO Buffer status.");
                	}
                }
                
                // Update skill list and user info
        		target.sendSkillList();
        		target.broadcastUserInfo();
            }
            catch (Exception e)
        	{
            	activeChar.sendMessage("Usage: //setaio 0 - removes AIO Buffer status, 1 - sets AIO Buffer status."); 
        	}
        }
        else if (command.startsWith("admin_atmosphere"))
        {
            try
            {
                StringTokenizer st = new StringTokenizer(command);
                st.nextToken();
                String type = st.nextToken();
                String state = st.nextToken();
                adminAtmosphere(type,state,activeChar);
            }
            catch(Exception ex) {}
        }
        else if (command.startsWith("admin_diet"))
        {
            try
            {
                if (!activeChar.getDietMode())
                {
                    activeChar.setDietMode(true);
                    activeChar.sendMessage("Diet mode on.");
                }
                else
                {
                    activeChar.setDietMode(false);
                    activeChar.sendMessage("Diet mode off.");
                }
                activeChar.refreshOverloaded();
            }
            catch(Exception ex) {}            
        }
        else if (command.startsWith("admin_tradeoff"))
        {
            try
            {
                String mode = command.substring(15);
                if (mode.equalsIgnoreCase("on"))
                {
                    activeChar.setTradeRefusal(true);
                    activeChar.sendMessage("Tradeoff enabled.");
                }
                else if (mode.equalsIgnoreCase("off"))
                {
                    activeChar.setTradeRefusal(false);
                    activeChar.sendMessage("Tradeoff disabled.");
                }
            }
            catch(Exception ex)
            {
                if (activeChar.getTradeRefusal())
                    activeChar.sendMessage("Tradeoff currently enabled.");
                else
                    activeChar.sendMessage("Tradeoff currently disabled.");
            }            
        }
        else if (command.startsWith("admin_reload"))
        {
            StringTokenizer st = new StringTokenizer(command);
            st.nextToken();

            try
            {
                String type = st.nextToken();

                if (type.startsWith("multisell"))
                {
                    L2Multisell.getInstance().reload();
                    activeChar.sendMessage("All Multisells have been reloaded.");
                }
                else if (type.startsWith("teleport"))
                {
                    TeleportLocationTable.getInstance().reloadAll();
                    activeChar.sendMessage("Teleport location table has been reloaded.");
                }
                else if (type.startsWith("skill"))
                {
                    SkillTable.getInstance().reload();
                    activeChar.sendMessage("All Skills have been reloaded.");
                }
                else if (type.startsWith("npcwalker"))
                {
                    NpcWalkerRoutesTable.getInstance().load();
                    activeChar.sendMessage("All NPC walker routes have been reloaded.");
                }
                else if (type.startsWith("npc"))
                {
                    NpcTable.getInstance().reloadAllNpc();
                    activeChar.sendMessage("All NPCs have been reloaded.");
                }
                else if (type.startsWith("htm"))
                {
                    HtmCache.getInstance().reload();
                    activeChar.sendMessage("Cache[HTML]: " + HtmCache.getInstance().getMemoryUsage()  + " megabytes on " + HtmCache.getInstance().getLoadedFiles() + " files loaded.");
                }
                else if (type.startsWith("item"))
                {
                    ItemTable.getInstance().load();
                    activeChar.sendMessage("All Item templates have been reloaded.");
                }
                else if (type.startsWith("buylist"))
                {
                    TradeController.getInstance().load();
                    activeChar.sendMessage("All buylists have been reloaded.");
                }
                else if (type.startsWith("config"))
                {
                    Config.load();
                    activeChar.sendMessage("All config settings have been reloaded.");
                }
                else if (type.startsWith("instancemanager"))
                {
                    Manager.reloadAll();
                    activeChar.sendMessage("All instance managers have been reloaded.");
                }
                else if (type.startsWith("quest"))
                {
                	activeChar.sendMessage("Quests are being reloaded...");
                    QuestManager.getInstance().reloadAllQuests();
                    activeChar.sendMessage("All Quests have been reloaded.");
                }
                else if (type.startsWith("event"))
                {
                    EventEngine.getInstance().load();
                    activeChar.sendMessage("All Events have been reloaded.");
                }
                else if (type.startsWith("aionpcbuff"))
                {
                	if (Config.AIO_BUFFER_ENABLED || Config.NPC_BUFFER_ENABLED)
                	{
                		BufferTable.getInstance().load();
                		activeChar.sendMessage("AIO/NPC Buffs have been reloaded.");
                		SkillTable.getInstance().reload();
                        activeChar.sendMessage("All Skills have been reloaded.");
                	}
                }
                else if (type.startsWith("wedding"))
                {
                	if (Config.ALLOW_WEDDING)
                	{
                		CoupleManager.getInstance().load();
                		activeChar.sendMessage("All Weddings have been reloaded.");
                	}
                }
                else if (type.startsWith("admincommand"))
                {
                	AdminCommandRightsData.getInstance().load();
                	activeChar.sendMessage("All Admin Command Rights have been reloaded.");
                }
            }
            catch(Exception e)
            {
                activeChar.sendMessage("Usage: //reload <multisell|teleport|skill|npc|htm|item|buylist|config"
                	+ "|instancemanager|npcwalker|quest|event|aionpcbuff|wedding|admincommand>");
            }
        }
        return true;
    }

    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }

    /**
     * 
     * @param type - atmosphere type (signsky,sky)
     * @param state - atmosphere state(night,day)
     * @param activeChar 
     */
    public void adminAtmosphere(String type, String state, L2PcInstance activeChar)
    {
        L2GameServerPacket packet = null;
        if (type.equals("signsky"))
        {
            if (state.equals("dawn"))
                packet = new SignsSky(2);
            else if (state.equals("dusk"))
                packet = new SignsSky(1);
        }
        else if(type.equals("sky"))
        {
            if (state.equals("night"))
                packet = new SunSet();
            else if (state.equals("day"))
                packet = new SunRise();
        }
        else
            activeChar.sendMessage("Only sky and signsky atmosphere type allowed, damn you!");

        if (packet != null)
        {
            for (L2PcInstance player : L2World.getInstance().getAllPlayers())
                player.sendPacket(packet);
        }
    }

    public void playAdminSound(L2PcInstance activeChar, String sound)
    {
        PlaySound snd = new PlaySound(1,sound,0,0,0,0,0);
        activeChar.sendPacket(snd);
        activeChar.broadcastPacket(snd);
        showMainPage(activeChar);
        activeChar.sendMessage("Playing "+sound+".");
    }

    public void showMainPage(L2PcInstance activeChar)
    {
        NpcHtmlMessage html = new NpcHtmlMessage(5);
        html.setFile("data/html/admin/adminpanel.htm");
        activeChar.sendPacket(html);
    }
}