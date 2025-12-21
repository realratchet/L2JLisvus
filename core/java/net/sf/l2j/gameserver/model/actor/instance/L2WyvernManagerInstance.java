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
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class L2WyvernManagerInstance extends L2CastleChamberlainInstance
{
	private static final int CRYSTAL_COUNT = 10;
	
	public L2WyvernManagerInstance(int objectId, L2NpcTemplate template)
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
			
			if (player.isMounted())
			{
				player.sendMessage("Cannot ride a wyvern while mounted.");
				return;
			}
			
			L2Summon summon = player.getPet();
			if (summon == null || (summon.getNpcId() != 12526) && (summon.getNpcId() != 12527) && (summon.getNpcId() != 12528))
			{
				player.sendMessage("Please summon your strider.");
				return;
			}
			
			if (summon.getLevel() < 55)
			{
				player.sendMessage("Your strider's level is lower than 55.");
				return;
			}
			
			if (!player.destroyItemByItemId("Wyvern", 1460, CRYSTAL_COUNT, player.getTarget(), false))
			{
				player.sendMessage("You need " + CRYSTAL_COUNT + " Crystal(s): B Grade.");
				return;
			}
			
			summon.unSummon(player);
			
			if (player.mount(12621, 0, true))
			{
				
				player.addSkill(SkillTable.getInstance().getInfo(4289, 1));
				player.sendMessage("You have successfully mounted a wyvern.");
			}
		}
		super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(L2PcInstance player)
	{
		String filename;
		
		int condition = validateCondition(player);
		
		// Clan owns castle, show owner message window
		if (condition == COND_CASTLE_OWNER)
		{
			filename = "data/html/wyvernmanager/wyvernmanager.htm";
		}
		else
		{
			filename = "data/html/wyvernmanager/wyvernmanager-no.htm";
		}
		
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}