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
import net.sf.l2j.gameserver.datatables.SkillTreeTable;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.AquireSkillDone;
import net.sf.l2j.gameserver.network.serverpackets.AquireSkillList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class L2FishermanInstance extends L2FolkInstance
{
    /**
	 * @param objectId
	 * @param template
	 */
	public L2FishermanInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
        
		if (val == 0)
			pom = "" + npcId;
		else 
			pom = npcId + "-" + val;
		
		return "data/html/fisherman/" + pom + ".htm";
	}
    
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("FishSkillList"))
		{
			player.setSkillLearningClassId(player.getClassId());
			showSkillList(player);
		}		
		else 
		{
			super.onBypassFeedback(player, command);
		}
	}	
    
	public void showSkillList(L2PcInstance player)
	{		
		L2SkillLearn[] skills = SkillTreeTable.getInstance().getAvailableSkills(player);
		AquireSkillList asl = new AquireSkillList(true);
        
		int count = 0;

        for (L2SkillLearn s : skills)
		{
			L2Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
            
			if (sk == null)
				continue;	
            
			count++;
			asl.addSkill(s.getId(), s.getLevel(), s.getLevel(), s.getSpCost(), 1);
		}

		if (count == 0)
		{
		    int minlevel = SkillTreeTable.getInstance().getMinLevelForNewSkill(player);
		    if (minlevel > 0)
            {
                // No more skills to learn, come back when you level.
		        SystemMessage sm = new SystemMessage(SystemMessage.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN);
		        sm.addNumber(minlevel);
		        player.sendPacket(sm);
		    }
            else
            {
            	player.sendPacket(new SystemMessage(750));
		    }
		    
			// Close skill learn list
			player.sendPacket(new AquireSkillDone());
		}
		else
		{
			// Send skill learn list
			player.sendPacket(asl);
		}

		player.sendPacket(new ActionFailed());
	}
}