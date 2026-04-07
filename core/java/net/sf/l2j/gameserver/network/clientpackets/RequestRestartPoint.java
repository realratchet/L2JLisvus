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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.MapRegionManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2SiegeClan;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.quest.L2EventScript;
import net.sf.l2j.gameserver.util.IllegalPlayerAction;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.7.2.3.2.6 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestRestartPoint extends L2GameClientPacket
{
	private static final String _C__6d_REQUESTRESTARTPOINT = "[C] 6d RequestRestartPoint";
	private static Logger _log = Logger.getLogger(RequestRestartPoint.class.getName());
	
	private int _requestPointType;
	
	@Override
	protected void readImpl()
	{
		_requestPointType = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (activeChar.isFakeDeath())
		{
			activeChar.stopFakeDeath(true);
			return;
		}
		
		if (!activeChar.isDead())
		{
			_log.warning("Living player [" + activeChar.getName() + "] called RestartPointPacket! Ban this player!");
			return;
		}
		
		L2EventScript event = activeChar.getEvent();
		if (event != null && event.isStarted())
		{
			_log.warning("Event player [" + activeChar.getName() + "] called RestartPointPacket to escape from event arena! Ban this player!");
			return;
		}
		
		if (activeChar.isInParty() && activeChar.getParty().isInDimensionalRift())
		{
			activeChar.sendMessage("You have been sent to the waiting room.");
			activeChar.getParty().removePartyMember(activeChar, true);
			return;
		}
		
		Siege siege = SiegeManager.getInstance().getSiege(activeChar);
		if (siege != null)
		{
			if (activeChar.getClan() != null && siege.checkIsAttacker(activeChar.getClan()))
			{
				if (siege.getAttackerRespawnDelay() > 0)
				{
					activeChar.sendMessage("You will be re-spawned in " + (siege.getAttackerRespawnDelay() / 1000) + " seconds");
					
					// Schedule respawn delay for attacker
					ThreadPoolManager.getInstance().scheduleGeneral(() -> {
						if (activeChar.isOnline())
						{
							portPlayer(activeChar, _requestPointType);
						}
					}, siege.getAttackerRespawnDelay());
					return;
				}
			}
		}
		portPlayer(activeChar, _requestPointType);
	}
	
	private void portPlayer(L2PcInstance activeChar, int requestPointType)
	{
		Location loc = null;
		
		if (activeChar.isFestivalParticipant())
		{
			requestPointType = 4;
		}
		
		switch (requestPointType)
		{
			case 1: // to clanhall
			{
				L2Clan clan = activeChar.getClan();
				if (clan == null || clan.getHasHideout() == 0)
				{
					activeChar.sendMessage("You may not use this respawn point!");
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " used respawn cheat.", IllegalPlayerAction.PUNISH_KICK);
					return;
				}
				
				ClanHall clanHall = ClanHallManager.getInstance().getClanHallByOwner(clan);
				
				loc = MapRegionManager.getInstance().getTeleToLocation(activeChar, MapRegionManager.TeleportWhereType.ClanHall);
				
				if (clanHall != null && clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP) != null)
				{
					activeChar.restoreExp(clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP).getLvl());
				}
				break;
			}
			case 2: // to castle
			{
				L2Clan clan = activeChar.getClan();
				if (clan == null)
				{
					activeChar.sendMessage("You may not use this respawn point!");
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " used respawn cheat.", IllegalPlayerAction.PUNISH_KICK);
					return;
				}
				
				boolean isInDefense = false;
				Castle castle = CastleManager.getInstance().getCastle(activeChar);
				if ((castle != null) && castle.getSiege().getIsInProgress())
				{
					// siege in progress
					if (castle.getSiege().checkIsDefender(clan))
					{
						isInDefense = true;
					}
				}
				
				if (clan.getHasCastle() == 0 && !isInDefense)
				{
					activeChar.sendMessage("You may not use this respawn point!");
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " used respawn cheat.", IllegalPlayerAction.PUNISH_KICK);
					return;
				}
				
				loc = MapRegionManager.getInstance().getTeleToLocation(activeChar, MapRegionManager.TeleportWhereType.Castle);
				break;
			}
			case 3: // to siege HQ
			{
				L2SiegeClan siegeClan = null;
				Castle castle = CastleManager.getInstance().getCastle(activeChar);
				
				if (castle != null && castle.getSiege().getIsInProgress())
				{
					siegeClan = activeChar.getClan() != null ? castle.getSiege().getAttackerClan(activeChar.getClan()) : null;
				}
				
				if (siegeClan == null || siegeClan.getFlags().isEmpty())
				{
					activeChar.sendMessage("You may not use this respawn point!");
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " used respawn cheat.", IllegalPlayerAction.PUNISH_KICK);
					return;
				}
				
				loc = MapRegionManager.getInstance().getTeleToLocation(activeChar, MapRegionManager.TeleportWhereType.SiegeFlag);
				break;
			}
			case 4: // Fixed or Player is a festival participant
			{
				if (!activeChar.isGM() && !activeChar.isFestivalParticipant())
				{
					// cheater
					activeChar.sendMessage("You may not use this respawn point!");
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " used respawn cheat.", IllegalPlayerAction.PUNISH_KICK);
					return;
				}
				loc = new Location(activeChar.getX(), activeChar.getY(), activeChar.getZ()); // spawn them where they died
				break;
			}
			default:
			{
				loc = MapRegionManager.getInstance().getTeleToLocation(activeChar, MapRegionManager.TeleportWhereType.Town);
				break;
			}
		}
		
		// Teleport and revive
		if (loc != null)
		{
			activeChar.setIsPendingRevive(true);
			activeChar.teleToLocation(loc, true);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__6d_REQUESTRESTARTPOINT;
	}
}