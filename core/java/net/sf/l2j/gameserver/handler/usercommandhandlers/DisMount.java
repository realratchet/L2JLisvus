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

package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * Support for /dismount command.  
 * @author Micht
 */
public class DisMount implements IUserCommandHandler
{
    private static final int[] COMMAND_IDS = { 62 }; 
	
    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#useUserCommand(int, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    @Override
	public synchronized boolean useUserCommand(int id, L2PcInstance activeChar)
    {
        if (activeChar.isRentedPet())
            activeChar.stopRentPet();
        else if (activeChar.isMounted())
            activeChar.dismount();

        return true;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#getUserCommandList()
     */
    @Override
	public int[] getUserCommandList()
    {
        return COMMAND_IDS;
    }
}