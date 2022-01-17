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
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.datatables.BufferTable;
import net.sf.l2j.gameserver.datatables.BufferTable.BuffInfo;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.taskmanager.AttackStanceTaskManager;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.util.Util;

public class L2NpcBufferInstance extends L2FolkInstance
{
	private static final Pattern _pattern = Pattern.compile("%fore_\\d+%");
	
    public L2NpcBufferInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
    public void showChatWindow(L2PcInstance player, int val)
    {
    	// There is a scheme that is still being edited
    	if (player.getEditingSchemeName() != null)
        {
        	showSchemeWindow(player, 1); // Buffs window
        	return;
        }

        String htmContent = HtmCache.getInstance().getHtm("data/html/mods/npcbuffer/start.htm");
        if (htmContent == null || !Config.NPC_BUFFER_ENABLED)
        {
            htmContent = HtmCache.getInstance().getHtm("data/html/npcdefault.htm");
        }
        else if (val > 0)
        {
            htmContent = HtmCache.getInstance().getHtm("data/html/mods/npcbuffer/" + val + ".htm");
        }

        if (htmContent != null)
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            html.setHtml(htmContent);
            html.replace("%objectId%", String.valueOf(getObjectId()));
            html.replace("%playerName%", player.getName());
            player.sendPacket(html);
        }
        player.sendPacket(new ActionFailed());
    }
    
    private void showSchemeMenu(L2PcInstance player)
    {
    	NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile("data/html/mods/npcbuffer/scheme-start.htm");
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%playerName%", player.getName());
        
        // Generate scheme list options
        Set<String> schemeNames = player.getSchemes().keySet();
        html.replace("%schemeCount%", "(" + schemeNames.size() + "/" + Config.SCHEMES_MAX_AMOUNT + ")");
        html.replace("%schemes%", schemeNames.isEmpty() ? "" : String.join(";", schemeNames));
        player.sendPacket(html);
        player.sendPacket(new ActionFailed());
    }
    
    private void showSchemeWindow(L2PcInstance player, int val)
    {
    	List<String> buffs = player.getSchemes().get(player.getEditingSchemeName() + "_temp");
    	if (buffs == null)
    	{
    		showSchemeMenu(player);
    		return;
    	}
    	
    	String htmContent = HtmCache.getInstance().getHtm("data/html/mods/npcbuffer/scheme-" + val + ".htm");
    	if (htmContent == null)
    	{
    		htmContent = HtmCache.getInstance().getHtm("data/html/npcdefault.htm");
    	}
    	
    	if (htmContent != null)
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            
            // Set scheme buttons state
            Matcher matcher = _pattern.matcher(htmContent);
        	while (matcher.find())
        	{
        		String toReplace = matcher.group();
        		String skillId = toReplace.substring(6, toReplace.length() - 1);
        		htmContent = htmContent.replace(toReplace, buffs.contains(skillId) ? "L2UI_ch3.bigbutton_down" : "L2UI_ch3.bigbutton");
        	}
            
            html.setHtml(htmContent);
            html.replace("%objectId%", String.valueOf(getObjectId()));
            html.replace("%schemeInfo%", player.getEditingSchemeName() + " (" + buffs.size() + "/" + Config.BUFFS_MAX_AMOUNT + ")");
            player.sendPacket(html);
        }
        player.sendPacket(new ActionFailed());
    }

    @Override
    public void onBypassFeedback(L2PcInstance player, String command)
    {
    	if (!Config.NPC_BUFFER_ENABLED)
    	{
    		return;
    	}
    	
        L2NpcInstance npc = player.getLastFolkNPC();
        if (npc == null || npc.getObjectId() != getObjectId())
        {
        	return;
        }

        if (player.isInOlympiadMode())
        {
            return;
        }

        L2Event event = player.getEvent();
        if (event != null && event.isStarted())
        {
        	return;
        }
        
        // Get command parameters
        StringTokenizer st = new StringTokenizer(command, " ");
        command = st.nextToken();

        L2Character target = player;
        L2Summon summon = player.getPet();
        if (command.startsWith("Pet"))
        {
            if (summon == null)
            {
                player.sendMessage("Please summon your pet.");
                if (command.contains("Scheme"))
                {
                	showSchemeMenu(player);
                }
                else
                {
                	showChatWindow(player, 0); // 0 = Main window
                }
                return;
            }
            target = summon;
        }
        
        if (command.startsWith("Chat"))
        {
            int val = Integer.parseInt(st.nextToken());
            showChatWindow(player, val);
        }
        else if (command.startsWith("SchemeMenu"))
        {
        	showSchemeMenu(player);
        }
        else if (command.startsWith("SchemeChat"))
        {
        	int val = Integer.parseInt(st.nextToken());
        	showSchemeWindow(player, val);
        }
        else if (command.startsWith("SchemeCreate"))
        {
        	if (!st.hasMoreTokens())
        	{
        		player.sendMessage("Incorrect scheme name.");
        		showSchemeMenu(player);
        		return;
        	}
        	
        	// Player cannot create any more schemes
        	if (player.getSchemes().size() >= Config.SCHEMES_MAX_AMOUNT)
        	{
        		player.sendMessage("You have reached the maximum amount of schemes per character.");
        		showSchemeMenu(player);
        		return;
        	}
        	
            String schemeName = st.nextToken();
            // Name already exists
            if (player.getSchemes().containsKey(schemeName))
            {
            	player.sendMessage("This scheme name is already used.");
            	showSchemeMenu(player);
            	return;
            }
            
            // Invalid name
            if (schemeName.length() > 16 || !Util.isAlphaNumeric(schemeName) || !Util.isValidName(schemeName, Config.SCHEME_NAME_TEMPLATE))
            {
            	player.sendMessage("Scheme name must not exceed 16 characters or contain any symbols and spaces.");
            	showSchemeMenu(player);
            	return;
            }
            
            // Create new scheme
            if (player.getEditingSchemeName() == null)
            {
            	player.setEditingSchemeName(schemeName);
            	player.getSchemes().put(schemeName + "_temp", new ArrayList<>());
            }
            showSchemeWindow(player, 1); // Buffs window
        }
        else if (command.startsWith("SchemeEdit"))
        {
        	if (!st.hasMoreTokens())
        	{
        		player.sendMessage("No scheme has been selected.");
        		showSchemeMenu(player);
        		return;
        	}
        	
        	String schemeName = st.nextToken();
        	if (!player.getSchemes().containsKey(schemeName))
        	{
        		player.sendMessage("Incorrect scheme.");
        		showSchemeMenu(player);
        		return;
        	}
        	
        	// Edit scheme
        	if (player.getEditingSchemeName() == null)
            {
        		player.setEditingSchemeName(schemeName);
        		player.getSchemes().put(schemeName + "_temp", new ArrayList<>(player.getSchemes().get(schemeName)));
            }
			showSchemeWindow(player, 1); // Buffs window
        }
        else if (command.startsWith("SchemeDelete"))
        {
        	if (!st.hasMoreTokens())
        	{
        		player.sendMessage("No scheme has been selected.");
        		showSchemeMenu(player);
        		return;
        	}
            
        	// Delete scheme
        	String schemeName = st.nextToken();
            player.getSchemes().remove(schemeName);
            player.deleteScheme(schemeName);
            showSchemeMenu(player);
        }
        else if (command.startsWith("SchemeBuffSelection"))
        {
        	List<String> buffs = player.getSchemes().get(player.getEditingSchemeName() + "_temp");
        	if (buffs != null)
        	{
        		// Manage requested buff
        		String skillId = st.nextToken();
        		if (buffs.contains(skillId))
        		{
        			buffs.remove(skillId);
        		}
        		else
        		{
        			if (buffs.size() >= Config.BUFFS_MAX_AMOUNT)
        			{
        				player.sendMessage("You have reached the maximum amount of buffs.");
        			}
        			else
        			{
        				buffs.add(skillId);
        			}
        		}
        	}
        	
        	// Show HTML window
            if (st.hasMoreTokens())
            {
            	showSchemeWindow(player, Integer.parseInt(st.nextToken()));
            }
        }
        else if (command.startsWith("SchemeSave"))
        {
        	String schemeName = player.getEditingSchemeName();
        	// Save scheme changes
        	if (player.getSchemes().containsKey(schemeName + "_temp"))
        	{
        		List<String> temp = player.getSchemes().get(schemeName + "_temp");
        		if (temp.isEmpty())
        		{
        			if (st.hasMoreTokens())
        			{
        				showSchemeWindow(player, Integer.parseInt(st.nextToken()));
        			}
        			player.sendMessage("Scheme is empty. Please select buffs of your choice before saving.");
        			return;
        		}
        		
        		player.getSchemes().put(schemeName, temp);
        		player.getSchemes().remove(schemeName + "_temp");
        		// Save scheme changes to database
        		player.storeScheme(schemeName);
        	}
        	player.setEditingSchemeName(null);
        	showSchemeMenu(player);
        }
        else if (command.startsWith("SchemeCancel"))
        {
        	String schemeName = player.getEditingSchemeName();
        	// Cancel scheme changes
        	player.getSchemes().remove(schemeName + "_temp");
        	player.setEditingSchemeName(null);
        	showSchemeMenu(player);
        }
        else if (command.startsWith("Buff") || command.startsWith("PetBuff"))
        {
        	String[] buffGroup;
        	// Scheme support
        	if (command.contains("Scheme"))
        	{
        		if (!st.hasMoreTokens())
            	{
            		player.sendMessage("No scheme has been selected.");
            		showSchemeMenu(player);
            		return;
            	}
        		
        		List<String> buffs = player.getSchemes().get(st.nextToken());
        		if (buffs == null || buffs.isEmpty())
        		{
        			player.sendMessage("This scheme is incorrect.");
        			showSchemeMenu(player);
            		return;
        		}
        		
            	buffGroup = buffs.toArray(new String[buffs.size()]);
        	}
        	// Common buffs or buff sets
        	else
        	{
        		if (!st.hasMoreTokens())
                {
                    _log.warning("NPC Buffer Warning: NPC Buffer has no skill ID set in the bypass for the selected buff.");
                    return;
                }
        		buffGroup = st.nextToken().split(",");
        	}
        	
            for (String id : buffGroup)
            {
            	// Check if player can still interact with NPC
            	if (!canTarget(player) || !canInteract(player))
            	{
            		return;
            	}
            	
            	int skillId = Integer.valueOf(id);
            	
                BuffInfo buff = BufferTable.getInstance().getNPCBuffs().get(skillId);
                if (buff == null)
                {
                    _log.warning("Player: " + player.getName() + " has tried to use a skill (" + skillId + ") not assigned to the NPC Buffer!");
                    continue;
                }

                if (buff.getSkillFeeId() != 0)
                {
                    L2ItemInstance itemInstance = player.getInventory().getItemByItemId(buff.getSkillFeeId());
                    if (itemInstance == null || (!itemInstance.isStackable() && player.getInventory().getInventoryItemCount(buff.getSkillFeeId(), -1) < buff.getSkillFeeAmount()))
                    {
                        SystemMessage sm = new SystemMessage(SystemMessage.NOT_ENOUGH_ITEMS);
                        player.sendPacket(sm);
                        continue;
                    }

                    if (itemInstance.isStackable())
                    {
                        if (!player.destroyItemByItemId("Npc Buffer", buff.getSkillFeeId(), buff.getSkillFeeAmount(), player.getTarget(), true))
                        {
                            continue;
                        }
                    }
                    else
                    {
                        for (int i = 0; i < buff.getSkillFeeAmount(); i++)
                        {
                            player.destroyItemByItemId("Npc Buffer", buff.getSkillFeeId(), 1, player.getTarget(), true);
                        }
                    }
                }

                L2Skill skill = SkillTable.getInstance().getInfo(skillId, buff.getSkillLevel());
                if (skill != null)
                {
                    skill.getEffects(this, target);
                }
            }
            
            // Show HTML window
            if (command.contains("Scheme"))
            {
            	showSchemeMenu(player);
            }
            else if (st.hasMoreTokens())
            {
            	showChatWindow(player, Integer.parseInt(st.nextToken()));
            }
        }
        else if (command.startsWith("Heal") || command.startsWith("PetHeal"))
        {
            if (!target.isInCombat() && !AttackStanceTaskManager.getInstance().getAttackStanceTask(target))
            {
                String[] healArray = st.nextToken().split(",");
                for (String healType : healArray)
                {
                	if (healType.equalsIgnoreCase("CP"))
                    {
                        target.setCurrentCp(target.getMaxCp());
                    }
                	
                	if (healType.equalsIgnoreCase("HP"))
                    {
                        target.setCurrentHp(target.getMaxHp());
                    }
                	
                    if (healType.equalsIgnoreCase("MP"))
                    {
                        target.setCurrentMp(target.getMaxMp());
                    }
                }
                
                // Show HTML window
                if (st.hasMoreTokens())
                {
                	showChatWindow(player, Integer.parseInt(st.nextToken()));
                }
            }
        }
        else if (command.startsWith("RemoveBuffs") || command.startsWith("PetRemoveBuffs"))
        {
        	// Remove all effects from target
            target.stopAllEffects();
            
            // Show HTML window
            if (st.hasMoreTokens())
            {
            	showChatWindow(player, Integer.parseInt(st.nextToken()));
            }
        }
        else
        {
            super.onBypassFeedback(player, command);
        }
    }

    @Override
    public boolean isBuffer()
    {
        return true;
    }
}