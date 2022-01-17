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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Broadcast;

public class Escape implements IUserCommandHandler
{
    private static final int[] COMMAND_IDS = { 52 };

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#useUserCommand(int, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    @Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
    {   
        if (activeChar.isMovementDisabled() || activeChar.isAlikeDead() || activeChar.isAllSkillsDisabled() || activeChar.isInOlympiadMode())
            return false;
        
        int unstuckTimer = (activeChar.getAccessLevel() >= Config.GM_ESCAPE ? 5000 : Config.UNSTUCK_INTERVAL*1000);

        // Check to see if the player is in a festival.
        if (activeChar.isFestivalParticipant()) 
        {
            activeChar.sendPacket(SystemMessage.sendString("You may not use an escape command in a festival."));
            return false;
        }

        if (activeChar.isInParty() && activeChar.getParty().isInDimensionalRift()
                || DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getX(), activeChar.getY(), activeChar.getZ(), true))
        {
            activeChar.sendMessage("Once a party is ported in another dimension, its members cannot get out of it.");
            return false;
        }

        // Check to see if player is in jail
        if (activeChar.isInJail())
        {
            activeChar.sendPacket(SystemMessage.sendString("You cannot escape from jail."));
            return false;
        }

        L2Event event = activeChar.getEvent();
		if (event != null && event.isStarted())
        {
            activeChar.sendMessage("You may not use an escape skill in events.");
            return false;
        }

        activeChar.sendMessage("You use Escape: 5 minutes.");
        
        // SoE Animation section
        activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
        activeChar.disableAllSkills();
        
        MagicSkillUse msk = new MagicSkillUse(activeChar, 1050, 1, unstuckTimer, 0);
        Broadcast.toSelfAndKnownPlayersInRadius(activeChar, msk, 810000);
        SetupGauge sg = new SetupGauge(SetupGauge.BLUE, unstuckTimer);
        activeChar.sendPacket(sg);
        // End SoE Animation section

        EscapeFinalizer ef = new EscapeFinalizer(activeChar);
        // Continue execution later
        activeChar.setSkillCast(ThreadPoolManager.getInstance().scheduleGeneral(ef, unstuckTimer));
        activeChar.setSkillCastEndTime(10+GameTimeController.getInstance().getGameTicks()+unstuckTimer/GameTimeController.MILLIS_IN_TICK);
        
        return true;
    }

    class EscapeFinalizer implements Runnable
    {
        private L2PcInstance _activeChar;
        
        EscapeFinalizer(L2PcInstance activeChar)
        {
            _activeChar = activeChar;
        }
        
        @Override
		public void run()
        {
            if (_activeChar.isDead()) 
                return; 
            
            _activeChar.enableAllSkills();
            
            try 
            {
                _activeChar.teleToLocation(MapRegionTable.TeleportWhereType.Town);
            }
            catch (Throwable e)
            {
            	if (Config.DEBUG)
            		e.printStackTrace();
            }
        }
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