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
package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;

public class CharAppearanceChange implements ISkillHandler
{
    private static SkillType[] SKILL_TYPES =
    {
        L2Skill.SkillType.FACE_LIFT,
        L2Skill.SkillType.HAIR_COLOR,
        L2Skill.SkillType.HAIR_STYLE
    };
    
    @Override
    public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
    {
        if (!(activeChar instanceof L2PcInstance))
        {
            return;
        }
        L2PcInstance player = (L2PcInstance) activeChar;
        
        switch (skill.getSkillType())
        {
            case FACE_LIFT:
                player.getAppearance().setFace((int) skill.getPower());
                break;
            case HAIR_COLOR:
                player.getAppearance().setHairColor((int) skill.getPower());
                break;
            case HAIR_STYLE:
                player.getAppearance().setHairStyle((int) skill.getPower());
                break;
        }

        player.broadcastPacket(new UserInfo(player));
    }
    
    @Override
    public SkillType[] getSkillTypes()
    {
        return SKILL_TYPES;
    }
}