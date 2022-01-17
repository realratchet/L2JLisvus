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

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class L2WyvernManagerInstance extends L2CastleChamberlainInstance
{
    public L2WyvernManagerInstance (int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
	public void onBypassFeedback(L2PcInstance player, String command)
    {
        if (command.startsWith("RideWyvern"))
        {
            if (!player.isClanLeader())
            {
                player.sendMessage("You are not the lord of this castle.");
                return;
            }

        	if (player.getPet() == null) 
        	{   
        		if (player.isMounted())
        		{
        			player.sendMessage("Already Have a Pet or Mounted.");
        			return;
        		}
        		player.sendMessage("Summon your Strider.");
				return;
        	}            
        	else if ((player.getPet().getNpcId() == 12526) || (player.getPet().getNpcId() == 12527) || (player.getPet().getNpcId() == 12528))
            {
        		if (player.getInventory().getItemByItemId(1460) != null && player.getInventory().getItemByItemId(1460).getCount() >= 10)
        		{
        			if (player.getPet().getLevel() < 55)
        			{
        				player.sendMessage("Your strider's level is lower than 55.");
                		return;                
        			}
					player.getPet().unSummon(player);
					if (player.mount(12621, 0, true))
	                {
	                    player.getInventory().destroyItemByItemId("Wyvern", 1460, 10, player, player.getTarget());
	                    player.addSkill(SkillTable.getInstance().getInfo(4289, 1));
	                    player.sendMessage("The Wyvern has been successfully summoned.");
	                }
	                return;
        		}
        		player.sendMessage("You need 10 Crystals: B Grade.");
				return;
            }
        	else
        	{
        		player.sendMessage("Unsummon your pet.");
        		return;
        	}
        }
		super.onBypassFeedback(player, command);
    }

    @Override
	public void showChatWindow(L2PcInstance player)
    {
        String filename = "data/html/wyvernmanager/wyvernmanager-no.htm";
        
        int condition = validateCondition(player);
        if (condition > COND_ALL_FALSE)
        {
            if (condition == COND_CASTLE_OWNER)                                     // Clan owns castle
                filename = "data/html/wyvernmanager/wyvernmanager.htm";      // Owner message window
        }
        NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setFile(filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%npcname%", getName());
        player.sendPacket(html);
        player.sendPacket(new ActionFailed());
    } 
}