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
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2SiegeClan;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
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
	
	protected int requestedPointType;
	protected boolean continuation;
	
	@Override
	protected void readImpl()
	{
		requestedPointType = readD();
	}
	
	class DeathTask implements Runnable
	{
		L2PcInstance activeChar;
		
		DeathTask(L2PcInstance _activeChar)
		{
			activeChar = _activeChar;
		}
		
		@Override
		public void run()
		{
			try
			{
				Location loc = null;
				if (activeChar.isInJail())
				{
					requestedPointType = 27;
				}
				else if (activeChar.isFestivalParticipant())
				{
					requestedPointType = 4;
				}
				else if (DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getX(), activeChar.getY(), activeChar.getZ(), true))
				{
					requestedPointType = 5;
				}
				switch (requestedPointType)
				{
					case 1: // to clanhall
					{
						if (activeChar.getClan().getHasHideout() == 0)
						{
							// cheater
							activeChar.sendMessage("You may not use this respawn point!");
							Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " used respawn cheat.", IllegalPlayerAction.PUNISH_KICK);
							return;
						}
						loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.ClanHall);
						
						if ((ClanHallManager.getInstance().getClanHallByOwner(activeChar.getClan()) != null) && (ClanHallManager.getInstance().getClanHallByOwner(activeChar.getClan()).getFunction(ClanHall.FUNC_RESTORE_EXP) != null))
						{
							activeChar.restoreExp(ClanHallManager.getInstance().getClanHallByOwner(activeChar.getClan()).getFunction(ClanHall.FUNC_RESTORE_EXP).getLvl());
						}
						break;
					}
					case 2: // to castle
					{
						boolean isInDefense = false;
						Castle castle = CastleManager.getInstance().getCastle(activeChar);
						if ((castle != null) && castle.getSiege().getIsInProgress())
						{
							// siege in progress
							if (castle.getSiege().checkIsDefender(activeChar.getClan()))
							{
								isInDefense = true;
							}
						}
						
						if ((activeChar.getClan().getHasCastle() == 0) && !isInDefense)
						{
							// cheater
							activeChar.sendMessage("You may not use this respawn point!");
							Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " used respawn cheat.", IllegalPlayerAction.PUNISH_KICK);
							return;
						}
						loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.Castle);
						break;
					}
					case 3: // to siege HQ
					{
						L2SiegeClan siegeClan = null;
						Castle castle = CastleManager.getInstance().getCastle(activeChar);
						
						if ((castle != null) && castle.getSiege().getIsInProgress())
						{
							siegeClan = castle.getSiege().getAttackerClan(activeChar.getClan());
						}
						if ((siegeClan == null) || (siegeClan.getFlags().isEmpty()))
						{
							// cheater
							activeChar.sendMessage("You may not use this respawn point!");
							Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " used respawn cheat.", IllegalPlayerAction.PUNISH_KICK);
							return;
						}
						loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.SiegeFlag);
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
					case 5: // Rift zone
					{
						DimensionalRiftManager.getInstance().teleportToWaitingRoom(activeChar);
						break;
					}
					case 27: // to jail
					{
						if (!activeChar.isInJail())
						{
							return;
						}
						loc = new Location(-114356, -249645, -2984);
						break;
					}
					default:
					{
						loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.Town);
						break;
					}
				}
				
				// Teleport and revive
				activeChar.setIsPendingRevive(true);
				if (loc != null)
				{
					activeChar.teleToLocation(loc, true);
				}
			}
			
			catch (Throwable e)
			{
			}
		}
		
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
		
		L2Event event = activeChar.getEvent();
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
			if ((activeChar.getClan() != null) && siege.checkIsAttacker(activeChar.getClan()))
			{
				// Schedule respawn delay for attacker
				ThreadPoolManager.getInstance().scheduleGeneral(new DeathTask(activeChar), siege.getAttackerRespawnDelay());
				
				if (siege.getAttackerRespawnDelay() > 0)
				{
					activeChar.sendMessage("You will be re-spawned in " + (siege.getAttackerRespawnDelay() / 1000) + " seconds");
				}
				return;
			}
		}
		new DeathTask(activeChar).run();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__6d_REQUESTRESTARTPOINT;
	}
}