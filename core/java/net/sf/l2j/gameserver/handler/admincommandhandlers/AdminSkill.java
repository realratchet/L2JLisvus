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
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SkillTreeTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.ShortCutInit;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.StringUtil;

/**
 * This class handles following admin commands:
 * - show_skills
 * - remove_skills
 * - skill_list
 * - skill_index
 * - add_skill
 * - remove_skill
 * - get_skills
 * - reset_skills
 * - give_all_skills
 * - remove_all_skills
 * - reset_skill_cooltimes
 * 
 * @version $Revision: 1.2.4.7 $ $Date: 2005/04/11 10:06:02 $
 */
public class AdminSkill implements IAdminCommandHandler
{
	private static Logger _log = Logger.getLogger(AdminSkill.class.getName());
	
	private static String[] _adminCommands =
	{
		"admin_show_skills",
		"admin_remove_skills",
		"admin_skill_list",
        "admin_skill_index",
		"admin_add_skill",
		"admin_remove_skill",
		"admin_get_skills",
		"admin_reset_skills",
		"admin_give_all_skills",
        "admin_remove_all_skills",
        "admin_reset_skill_cooltimes"
	};
	
	private L2Skill[] _adminSkills;

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
		String target = (activeChar.getTarget() != null?activeChar.getTarget().getName():"no-target");
        GMAudit.auditGMAction(activeChar.getName(), command, target, "");

        if (command.equals("admin_show_skills"))
		{
			showSkillsPage(activeChar);
		}
        else if (command.startsWith("admin_remove_skills"))
		{
        	try
        	{
        		String val = command.substring(20);
        		removeSkillsPage(activeChar, Integer.parseInt(val));
        	}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
		else if (command.startsWith("admin_skill_list"))
		{
			showHelpPage(activeChar, "skills.htm");
		}
        else if (command.startsWith("admin_skill_index"))
        {
            try
            {
                String val = command.substring(18);
                showHelpPage(activeChar, "skills/" + val + ".htm");
            }
            catch (StringIndexOutOfBoundsException e)
            {
            }
        }
		else if (command.startsWith("admin_add_skill"))
		{
			try
			{
				String val = command.substring(15); 
			    adminAddSkill(activeChar, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				// Case of empty character name
				activeChar.sendMessage("Error while adding skill.");
			}			
		}
		else if (command.startsWith("admin_remove_skill"))
		{
			try
			{
				String val = command.substring(19);
				int id = Integer.parseInt(val);
			    adminRemoveSkill(activeChar, id);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				// Case of empty character name
				activeChar.sendMessage("Error while removing skill.");
			}			
		}
		else if (command.equals("admin_get_skills"))
		{
			adminGetSkills(activeChar);
		}
		else if (command.equals("admin_reset_skills"))
		{
			adminResetSkills(activeChar);
		}
        else if (command.equals("admin_give_all_skills"))
        {
        	adminGiveAllSkills(activeChar);
        }
        else if (command.equals("admin_remove_all_skills"))
        {
        	adminRemoveAllSkills(activeChar);
    	}
        else if (command.equals("admin_reset_skill_cooltimes"))
        {
        	adminResetSkillCooltimes(activeChar);
        }
		return true;
	}

    /**
     * This function will give all the skills that the gm target can have at its level
     * to the target.
     * @param activeChar : the gm char
     */
    private void adminGiveAllSkills(L2PcInstance activeChar)
    {
        L2Object target = activeChar.getTarget();
        if (target == null)
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return;
        }

        L2PcInstance player = null;
        if (target instanceof L2PcInstance)
            player = (L2PcInstance)target;
        else
        {            
        	activeChar.sendMessage("Incorrect target.");
            return;
        }

        int skillCounter = 0;

        L2SkillLearn[] skills = SkillTreeTable.getInstance().getMaxAvailableSkills(player, player.getClassId());
        for (L2SkillLearn s : skills)
        {
            L2Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
            if (sk == null || !sk.getCanLearn(player.getClassId()))
                continue;

            if (player.getSkillLevel(sk.getId()) == -1)
                skillCounter++;

            player.addSkill(sk, true);
        }

        // Notify player and admin
        if (skillCounter > 0)
        {
        	player.sendSkillList();
            player.sendMessage("A GM gave you " + skillCounter + " skills.");
            activeChar.sendMessage("You gave " + skillCounter + " skills to " + player.getName());
        }
    }
    
    /**
     * This function will remove all skills from target player.
     * 
     * @param activeChar : the gm char
     */
    private void adminRemoveAllSkills(L2PcInstance activeChar)
    {
    	L2Object target = activeChar.getTarget();
        if (target == null)
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return;
        }

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

        // Remove all skills
        player.removeAllSkills(true);
        player.sendSkillList();

        // Notify player and admin
        if (activeChar != player)
        {
        	player.sendMessage("A GM removed all your skills.");
        }
        activeChar.sendMessage("All your skills have been removed.");
    }

	private void removeSkillsPage(L2PcInstance activeChar, int page)
	{
		L2Object target = activeChar.getTarget();
        if (target == null)
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return;
        }
		
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
		
		L2Skill[] skills = player.getAllSkills();
				
		int maxSkillsPerPage = 10;
		int maxPages = skills.length / maxSkillsPerPage;
		if (skills.length > maxSkillsPerPage * maxPages)
			maxPages++;
		
		if (page > maxPages)
			page = maxPages;
		
		int SkillsStart = maxSkillsPerPage*page;
		int SkillsEnd = skills.length;
		if (SkillsEnd - SkillsStart > maxSkillsPerPage)
		    SkillsEnd = SkillsStart + maxSkillsPerPage;
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);		
		StringBuilder replyMSG = new StringBuilder(3000);
		StringUtil.append(replyMSG, "<html><body>");
		StringUtil.append(replyMSG, "<table width=260><tr>");
		StringUtil.append(replyMSG, "<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		StringUtil.append(replyMSG, "<td width=180><center>Character Selection Menu</center></td>");
		StringUtil.append(replyMSG, "<td width=40><button value=\"Back\" action=\"bypass -h admin_show_skills\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		StringUtil.append(replyMSG, "</tr></table>");
		StringUtil.append(replyMSG, "<br><br>");
		StringUtil.append(replyMSG, "<center>Editing <font color=\"LEVEL\">" + player.getName() + "</font></center>");
		StringUtil.append(replyMSG, "<br><table width=270><tr><td>Lv: " + player.getLevel() + " " + player.getTemplate().className + "</td></tr></table>");
		StringUtil.append(replyMSG, "<br><table width=270><tr><td>Note: Dont forget that modifying players skills can</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>ruin the game...</td></tr></table>");
		StringUtil.append(replyMSG, "<br><center>Click on the skill you wish to remove:</center>");
		StringUtil.append(replyMSG, "<br>");
		String pages = "<center><table width=270><tr>";
		for (int x = 0; x < maxPages; x++)
		{
			int pagenr = x + 1;
			pages += "<td><a action=\"bypass -h admin_remove_skills " + x + "\">Page " + pagenr + "</a></td>";
		}
		pages += "</tr></table></center>";
		StringUtil.append(replyMSG, pages);
		StringUtil.append(replyMSG, "<br><table width=270>");		
		StringUtil.append(replyMSG, "<tr><td width=80>Name:</td><td width=60>Level:</td><td width=40>Id:</td></tr>");
		
		for (int i = SkillsStart; i < SkillsEnd; i++)
		{
			StringUtil.append(replyMSG, "<tr><td width=80><a action=\"bypass -h admin_remove_skill "+skills[i].getId()+"\">"+skills[i].getName()+"</a></td><td width=60>"+skills[i].getLevel()+"</td><td width=40>"+skills[i].getId()+"</td></tr>");
		}
		StringUtil.append(replyMSG, "</table>");
		StringUtil.append(replyMSG, "<br><center><table>");
		StringUtil.append(replyMSG, "Remove custom skill:");
		StringUtil.append(replyMSG, "<tr><td>Id: </td>");
		StringUtil.append(replyMSG, "<td><edit var=\"id_to_remove\" width=110></td></tr>");
		StringUtil.append(replyMSG, "</table></center>");		
		StringUtil.append(replyMSG, "<center><button value=\"Remove skill\" action=\"bypass -h admin_remove_skill $id_to_remove\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		StringUtil.append(replyMSG, "<br><center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15></center>");
		StringUtil.append(replyMSG, "</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	private void showSkillsPage(L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
        if (target == null)
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return;
        }
        
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
			"<table width=260><tr>",
			"<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
			"<td width=180><center>Character Selection Menu</center></td>",
			"<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
			"</tr></table>",
			"<br><br>",
        	"<center>Editing <font color=\"LEVEL\">" + player.getName() + "</font></center>",
			"<br><table width=270><tr><td>Lv: " + player.getLevel() + " " + player.getTemplate().className + "</td></tr></table>",
			"<br><table width=270><tr><td>Note: Dont forget that modifying players skills can</td></tr>",
			"<tr><td>ruin the game...</td></tr></table>",
			"<br><center><table>",
			"<tr><td><button value=\"Add skills\" action=\"bypass -h admin_skill_list\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
			"<td><button value=\"Get skills\" action=\"bypass -h admin_get_skills\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
			"<tr><td><button value=\"Delete skills\" action=\"bypass -h admin_remove_skills 0\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
			"<td><button value=\"Reset skills\" action=\"bypass -h admin_reset_skills\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
        	"<tr><td><button value=\"Give All Skills\" action=\"bypass -h admin_give_all_skills\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td><button value=\"Delete All skills\" action=\"bypass -h admin_remove_all_skills\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>",
			"</table></center>",
			"</body></html>");
		
		adminReply.setHtml(replyMSG);
		activeChar.sendPacket(adminReply);
	}
	
	private void adminGetSkills(L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
        if (target == null)
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return;
        }
        
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

		if (player.getName().equals(activeChar.getName()))
		{
			activeChar.sendMessage("There is no point in doing it on your character.");
		}
		else
		{
			L2Skill[] skills = player.getAllSkills();
			_adminSkills = activeChar.getAllSkills();
			for (int i = 0; i < _adminSkills.length;i++)
			{
				activeChar.removeSkill(_adminSkills[i]);
			}
			for (int i=0;i<skills.length;i++)
			{
				activeChar.addSkill(skills[i], true);
			}
			activeChar.sendMessage("You now have all the skills of  "+player.getName()+".");
		}
		showSkillsPage(activeChar);
	}
	
	private void adminResetSkills(L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
        if (target == null)
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return;
        }
        
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

		if (_adminSkills == null)
		{
			activeChar.sendMessage("You must first get the skills of someone to do this.");
		}
		else
		{
			L2Skill[] skills = player.getAllSkills();
			for (int i = 0; i < skills.length; i++)
			{
				player.removeSkill(skills[i]);
			}
			for (int i = 0; i < activeChar.getAllSkills().length; i++)
			{
				player.addSkill(activeChar.getAllSkills()[i], true);
			}
			for (int i = 0; i < skills.length; i++)
			{
				activeChar.removeSkill(skills[i]);
			}
			for (int i = 0; i < _adminSkills.length; i++)
			{
				activeChar.addSkill(_adminSkills[i], true);
			}
			player.sendMessage("[GM]"+activeChar.getName()+" has updated your skills.");
			activeChar.sendMessage("You now have all your skills back.");
			_adminSkills = null;
		}
		showSkillsPage(activeChar);
	}
	
	private void adminAddSkill(L2PcInstance activeChar, String val)
	{
		L2Object target = activeChar.getTarget();
        if (target == null)
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return;
        }
        
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

		StringTokenizer st = new StringTokenizer(val);
		if (st.countTokens() != 2)
		{
			showSkillsPage(activeChar);
		}
		else
		{
			String id = st.nextToken();
			String level = st.nextToken();		
			int idval = Integer.parseInt(id);
			int levelval = Integer.parseInt(level);
			
			L2Skill skill = SkillTable.getInstance().getInfo(idval,levelval);		
			
			if (skill != null)
			{
				player.sendMessage("Admin gave you the skill "+skill.getName()+".");
				player.addSkill(skill, true);
				
				// Admin information	
				activeChar.sendMessage("You gave the skill "+skill.getName()+" to "+player.getName()+".");

				if (Config.DEBUG)
					_log.fine("[GM]"+activeChar.getName()+" gave the skill "+skill.getName() + " to "+player.getName()+".");
			}
			else
			{
				activeChar.sendMessage("Error: There is no such skill.");
			}		
			showSkillsPage(activeChar); //Back to start
		}
	}
	
	private void adminRemoveSkill(L2PcInstance activeChar, int idval)
	{
		L2Object target = activeChar.getTarget();
        if (target == null)
        {
        	activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_CANT_FOUND));
            return;
        }
        
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
				
		L2Skill skill = SkillTable.getInstance().getInfo(idval,player.getSkillLevel(idval));
				
		if (skill != null)
		{
			player.sendMessage("Admin removed the skill "+skill.getName()+".");
			player.removeSkill(skill);
			
			// Admin information	
			activeChar.sendMessage("You removed the skill "+skill.getName()+" from "+player.getName()+".");
			
			if (Config.DEBUG)
				_log.fine("[GM]"+activeChar.getName()+" removed the skill "+skill.getName()+ " from "+player.getName()+".");
		}
		else
		{
			activeChar.sendMessage("Error: There is no such skill.");
		}
		removeSkillsPage(activeChar, 0); //Back to start	
	}
	
	private void adminResetSkillCooltimes(L2PcInstance activeChar)
	{
		final L2Object target = activeChar.getTarget();

        L2PcInstance player = null;
        if (target == null)
        {
        	player = activeChar;
        }
        else if (target instanceof L2PcInstance)
        {
            player = (L2PcInstance)target;
        }
        else
        {            
        	activeChar.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
            return;
        }
        
        // Enable all disabled skills
		for (L2Skill skill : player.getAllSkills())
		{
			if (player.isSkillDisabled(skill.getId()))
			{
				player.enableSkill(skill.getId());
			}
		}
		
		player.sendSkillList();
		player.sendPacket(new ShortCutInit(player));
		
		if (activeChar != player)
		{
			activeChar.sendMessage("Skill cooltimes have been reset for character " + player.getName() + ".");
		}
		player.sendMessage("Your skill cooltimes have been reset by GM.");
	}
	
	@Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}