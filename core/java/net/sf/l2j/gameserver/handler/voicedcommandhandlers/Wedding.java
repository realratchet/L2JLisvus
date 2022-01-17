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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.instancemanager.CoupleManager;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 * @author evill33t
 *
 */
public class Wedding implements IVoicedCommandHandler
{
	private static final Logger _log = Logger.getLogger(Wedding.class.getName());
	
	private static final String[] VOICED_COMMANDS =
	{
		"divorce",
		"engage",
		"gotolove"
	};
	
	/**
	 * 
	 * @see net.sf.l2j.gameserver.handler.IVoicedCommandHandler#useVoicedCommand(java.lang.String, net.sf.l2j.gameserver.model.actor.instance.L2PcInstance, java.lang.String)
	 */
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (!Config.ALLOW_WEDDING)
		{
			return false;
		}
		
		if (command.startsWith("engage"))
		{
			return engage(activeChar);
		}
		else if (command.startsWith("divorce"))
		{
			return divorce(activeChar);
		}
		else if (command.startsWith("gotolove"))
		{
			return goToLove(activeChar);
		}
		return false;
	}
	
	private boolean divorce(L2PcInstance activeChar)
	{
		if (activeChar.getPartnerId() == 0)
		{
			return false;
		}
		
		L2PcInstance partner = L2World.getInstance().getPlayer(activeChar.getPartnerId());
		if (partner != null)
		{
			if (partner.isMarried())
			{
				partner.sendMessage("Your spouse has decided to divorce you.");
			}
			else
			{
				partner.sendMessage("Your fiance has decided to break the engagement with you.");
			}
		}

		if (activeChar.isMarried())
		{
			activeChar.sendMessage("You are now divorced.");
		}
		else
		{
			activeChar.sendMessage("You have broken up as a couple.");
		}
		
		CoupleManager.getInstance().deleteCouple(activeChar.getCoupleId());
		return true;
	}
	
	private boolean engage(L2PcInstance activeChar)
	{
		// Cannot request engage if there is still an engage request to answer
		if (activeChar.isEngageRequested())
		{
			activeChar.sendMessage("You have an engage request to answer.");
			return false;
		}
		
		// Check target
		if (activeChar.getTarget() == null || !(activeChar.getTarget() instanceof L2PcInstance))
		{
			activeChar.sendMessage("Incorrect target.");
			return false;
		}
		
		L2PcInstance pTarget = (L2PcInstance) activeChar.getTarget();
		
		// Check if player is already engaged
		if (activeChar.getPartnerId() != 0)
		{
			activeChar.sendMessage("You are already engaged.");
			return false;
		}
		
		// Check if player target himself
		if (pTarget.getObjectId() == activeChar.getObjectId())
		{
			activeChar.sendMessage("Is there something wrong with you, are you trying to go out with youself?");
			return false;
		}
		
		if (pTarget.isMarried())
		{
			activeChar.sendMessage("Player is already married.");
			return false;
		}
		
		if (pTarget.isEngageRequested())
		{
			activeChar.sendMessage("Player is already asked by someone else.");
			return false;
		}
		
		if (pTarget.getPartnerId() != 0)
		{
			activeChar.sendMessage("Player is already engaged with someone else.");
			return false;
		}
		
		if (pTarget.getAppearance().getSex() == activeChar.getAppearance().getSex() && !Config.WEDDING_SAME_SEX)
		{
			activeChar.sendMessage("Gay marriage is not allowed on this server!");
			return false;
		}
		
		// Check if target has player on friend list
		if (!activeChar.getFriendList().contains(pTarget.getObjectId()))
		{
			activeChar.sendMessage("The player you want to ask is not on your friends list, you must first be on each others friends list before you choose to engage.");
			return false;
		}
		
		pTarget.setEngageRequest(activeChar.getObjectId(), true);
		ConfirmDlg dlg = new ConfirmDlg(SystemMessage.S1_S2);
		dlg.addString(activeChar.getName() + " is asking to engage you. Do you want to start a new relationship?");
		pTarget.sendPacket(dlg);
		activeChar.sendMessage("Engage request has been sent to " + pTarget.getName() + ".");
		return true;
	}
	
	private boolean goToLove(L2PcInstance activeChar)
	{
		if (activeChar.isMovementDisabled() || activeChar.isAlikeDead() || activeChar.isAllSkillsDisabled())
		{
            return false;
		}
		
		if (!activeChar.isMarried())
		{
			activeChar.sendMessage("You are not married.");
			return false;
		}
		
		if (activeChar.getPartnerId() == 0)
		{
			activeChar.sendMessage("Couldn't find your fiance in the Database - Inform a Gamemaster.");
			_log.severe("Married but couldn't find partner for " + activeChar.getName());
			return false;
		}
		
		// Check if partner is reachable
		if (getPartner(activeChar) == null)
		{
			return false;
		}

		if (activeChar.isInJail())
		{
			activeChar.sendMessage("You are in Jail!");
			return false;
		}
		
		if (GrandBossManager.getInstance().getZone(activeChar) != null)
		{
			activeChar.sendMessage("You are inside a Boss Zone.");
			return false;
		}
		
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage("You are in the Olympiad.");
			return false;
		}
		
		L2Event event = activeChar.getEvent();
		if (event != null && event.isStarted())
		{
			activeChar.sendMessage("You are in an event.");
			return false;
		}
		
		if (activeChar.isFestivalParticipant())
		{
			activeChar.sendMessage("You are in a festival.");
			return false;
		}
		
		L2Party party = activeChar.getParty();
		if (party != null && party.isInDimensionalRift())
		{
			activeChar.sendMessage("You are in the dimensional rift.");
			return false;
		}
		
		if (activeChar.inObserverMode())
		{
			activeChar.sendMessage("You are in observation mode.");
			return false;
		}
		
		Siege siege = SiegeManager.getInstance().getSiege(activeChar);
		if (siege != null && siege.getIsInProgress())
		{
			activeChar.sendMessage("You are in a siege, you cannot go to your partner.");
			return false;
		}
		
		// Check if player has enough adena
		if (!activeChar.reduceAdena("Wedding Teleport", Config.WEDDING_TELEPORT_PRICE, null, true))
		{
			return false;
		}
		
		int teleportTimer = Config.WEDDING_TELEPORT_DURATION * 1000;
		activeChar.sendMessage("You will be teleported to your partner in " + teleportTimer / 60000 + " minute(s).");
		
		// SoE Animation section
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		activeChar.disableAllSkills();
		
		MagicSkillUse msk = new MagicSkillUse(activeChar, 1050, 1, teleportTimer, 0);
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, msk, 810000);
		SetupGauge sg = new SetupGauge(SetupGauge.BLUE, teleportTimer);
		activeChar.sendPacket(sg);
		// End SoE Animation section
		
		EscapeFinalizer ef = new EscapeFinalizer(activeChar);
		// Continue execution later
		activeChar.setSkillCast(ThreadPoolManager.getInstance().scheduleGeneral(ef, teleportTimer));
		activeChar.setSkillCastEndTime(10 + GameTimeController.getInstance().getGameTicks() + teleportTimer / GameTimeController.MILLIS_IN_TICK);
		
		return true;
	}
	
	protected L2PcInstance getPartner(L2PcInstance activeChar)
	{
		L2PcInstance partner = L2World.getInstance().getPlayer(activeChar.getPartnerId());
		if (partner == null)
		{
			activeChar.sendMessage("Your partner is not online.");
			return null;
		}
		
		if (partner.isTeleporting())
		{
			activeChar.sendMessage("Your partner is teleporting.");
			return null;
		}
		
		if (partner.isInJail())
		{
			activeChar.sendMessage("Your partner is in Jail.");
			return null;
		}
		
		if (GrandBossManager.getInstance().getZone(partner) != null)
		{
			activeChar.sendMessage("Your partner is inside a Boss Zone.");
			return null;
		}
		
		if (partner.isInOlympiadMode())
		{
			activeChar.sendMessage("Your partner is in the Olympiad.");
			return null;
		}
		
		L2Event event = partner.getEvent();
		if (event != null && event.isStarted())
		{
			activeChar.sendMessage("Your partner is in an event.");
			return null;
		}
		
		if (partner.isFestivalParticipant())
		{
			activeChar.sendMessage("Your partner is in a festival.");
			return null;
		}
		
		L2Party party = partner.getParty();
		if (party != null && party.isInDimensionalRift())
		{
			activeChar.sendMessage("Your partner is in dimensional rift.");
			return null;
		}
		
		if (partner.inObserverMode())
		{
			activeChar.sendMessage("Your partner is in observation mode.");
			return null;
		}
		
		Siege siege = SiegeManager.getInstance().getSiege(partner);
		if (siege != null && siege.getIsInProgress())
		{
			activeChar.sendMessage("Your partner is in a siege, you cannot go to your partner.");
			return null;
		}
		
		return partner;
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
			{
				return;
			}
			
			_activeChar.enableAllSkills();
			
			try
			{
				L2PcInstance partner = getPartner(_activeChar);
				// Check if partner is still reachable
				if (partner != null)
				{
					_activeChar.teleToLocation(partner.getX(), partner.getY(), partner.getZ());
				}
			}
			catch (Throwable e)
            {
            	if (Config.DEBUG)
            		e.printStackTrace();
            }
		}
	}
	
	/**
	 * 
	 * @return 
	 * @see net.sf.l2j.gameserver.handler.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}