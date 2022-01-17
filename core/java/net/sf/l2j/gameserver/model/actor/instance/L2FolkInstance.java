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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SkillTreeTable;
import net.sf.l2j.gameserver.model.L2EnchantSkillLearn;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.AquireSkillDone;
import net.sf.l2j.gameserver.network.serverpackets.AquireSkillList;
import net.sf.l2j.gameserver.network.serverpackets.ExEnchantSkillList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.StringUtil;

public class L2FolkInstance extends L2NpcInstance 
{
    private final ClassId[] _classesToTeach;
    
    // Folk conditions
    protected static final int COND_ALL_FALSE = 0;
    protected static final int COND_BUSY_BECAUSE_OF_SIEGE = 1;
    protected static final int COND_CASTLE_OWNER = 2;
    protected static final int COND_HALL_OWNER = 3;
    protected static final int COND_REGULAR = 4;

    public L2FolkInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
        _classesToTeach = template.getTeachInfo();
    }

    /**
     * This method displays SkillList to the player.
     * 
     * @param player
     * @param classId 
     */
    public void showSkillList(L2PcInstance player, ClassId classId)
    {
        if (Config.DEBUG) 
            _log.fine("SkillList activated on: "+getObjectId());

        int npcId = getTemplate().npcId;
        if (_classesToTeach == null)
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            String sb = StringUtil.concat(
            	"<html><body>",
            	"I cannot teach you. My class list is empty.<br> Ask admin to fix it. Need add my npcid and classes to skill_learn.sql.<br>NpcId:"+npcId+", Your classId:"+player.getClassId().getId()+"<br>",
            	"</body></html>");
            html.setHtml(sb);
            player.sendPacket(html);
            return;
        }
        if (!getTemplate().canTeach(classId))
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            String sb = StringUtil.concat(
            	"<html><body>",
            	"I cannot teach you any skills.<br> You must find your current class teachers.",
            	"</body></html>");
            html.setHtml(sb);
            player.sendPacket(html);
            return;
        }

        L2SkillLearn[] skills = SkillTreeTable.getInstance().getAvailableSkills(player, classId);
        AquireSkillList asl = new AquireSkillList(false);
        int count = 0;

        for (L2SkillLearn s: skills)
        {			
            L2Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
            if (sk == null || !sk.getCanLearn(player.getClassId()) || !sk.canTeachBy(npcId))
                continue;

            int cost = SkillTreeTable.getInstance().getSkillCost(player, sk);
            count++;

            asl.addSkill(s.getId(), s.getLevel(), s.getLevel(), cost, 0);
        }

        if (count == 0)
        {
            int minLevel = SkillTreeTable.getInstance().getMinLevelForNewSkill(player, classId);
            if (minLevel > 0)
            {
                SystemMessage sm = new SystemMessage(SystemMessage.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN);
                sm.addNumber(minLevel);
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
    /**
     * This displays EnchantSkillList to the player.
     * @param player
     * @param classId 
     */
    public void showEnchantSkillList(L2PcInstance player, ClassId classId)
    {
        if (Config.DEBUG) 
            _log.fine("EnchantSkillList activated on: "+getObjectId());

        int npcId = getTemplate().npcId;
        if (_classesToTeach == null)
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            String sb = StringUtil.concat(
            	"<html><body>",
            	"I cannot teach you. My class list is empty.<br> Ask admin to fix it. Need add my npcid and classes to skill_learn.sql.<br>NpcId:"+npcId+", Your classId:"+player.getClassId().getId()+"<br>",
            	"</body></html>");
            html.setHtml(sb);
            player.sendPacket(html);
            return;
        }

        if (!getTemplate().canTeach(classId))
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            String sb = StringUtil.concat(
            	"<html><body>",
            	"I cannot teach you any skills.<br> You must find your current class teachers.",
            	"</body></html>");
            html.setHtml(sb);
            player.sendPacket(html);
            return;
        }

        if (player.getClassId().level() < 3) 
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            String sb = StringUtil.concat("<html><body>You must have 3rd class change quest completed.</body></html>");
            html.setHtml(sb);
            player.sendPacket(html);
            return;
        }
        
        int level = player.getLevel();
        if (level >= 76)
        {
        	L2EnchantSkillLearn[] skills = SkillTreeTable.getInstance().getAvailableEnchantSkills(player);
            ExEnchantSkillList esl = new ExEnchantSkillList();
            int count = 0;

            for (L2EnchantSkillLearn s: skills)
            {           
                L2Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
                if (sk == null)
                    continue;
                count++;
                esl.addSkill(s.getId(), s.getLevel(), s.getSpCost(), s.getExp());
            }
            
            if (count == 0)
            {
                player.sendPacket(new SystemMessage(SystemMessage.THERE_IS_NO_SKILL_THAT_ENABLES_ENCHANT));
                
                // Close skill learn list
    			player.sendPacket(new AquireSkillDone());
            }
            else
            {
                player.sendPacket(esl);
            }
        }
        else
        {
        	player.sendPacket(new SystemMessage(SystemMessage.THERE_IS_NO_SKILL_THAT_ENABLES_ENCHANT));
        }
        
        player.sendPacket(new ActionFailed());
    }

    @Override
	public void onBypassFeedback(L2PcInstance player, String command)
    {
        if (command.startsWith("SkillList"))
        {
            player.setSkillLearningClassId(player.getClassId());
            showSkillList(player, player.getClassId());
        }
        else if (command.startsWith("EnchantSkillList"))
        {
            showEnchantSkillList(player, player.getClassId());
        }
        else 
        {
            // This class don't know any other commands, let forward
            // the command to the parent class
            super.onBypassFeedback(player, command);
        }
    }
    
    /**
	 * Overridden method.
	 * 
	 * @return
	 */
	public boolean isWarehouse()
	{
		return false;
	}
    
    protected int validateCondition(L2PcInstance player)
    {
		return COND_ALL_FALSE;
    }
}