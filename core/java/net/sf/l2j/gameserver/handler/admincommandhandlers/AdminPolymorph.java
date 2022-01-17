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

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;

public class AdminPolymorph implements IAdminCommandHandler
{
    private static String[] _adminCommands = { "admin_polymorph" };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_polymorph"))
        {
            StringTokenizer st = new StringTokenizer(command);
            try
            {
                st.nextToken();
                String type = st.nextToken();
                String id = st.nextToken();
                L2Object target = activeChar.getTarget();
                doPolymorph(activeChar, target, id, type);
            }
            catch (Exception e)
            {
            	activeChar.sendMessage("Usage: //polymorph item|npc <npcId>");
            }
        }
        return true;
    }
    
    private void doPolymorph(L2PcInstance activeChar, L2Object obj, String id, String type)
    {
        if (obj != null)
        {
            obj.getPoly().setPolyInfo(type, id);
            
            // Animation
            if (obj instanceof L2Character)
			{
				L2Character character = (L2Character) obj;
				MagicSkillUse msk = new MagicSkillUse(character, 1008, 1, 4000, 0);
				character.broadcastPacket(msk);
				SetupGauge sg = new SetupGauge(SetupGauge.BLUE, 4000);
				character.sendPacket(sg);
			}
            // End of animation
            
            obj.decayMe();
            obj.spawnMe(obj.getX(),obj.getY(),obj.getZ());
        }
        else
        {
            activeChar.sendMessage("Incorrect target.");
        }
    }
    
    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}