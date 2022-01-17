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

import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.instancemanager.TownManager;
import net.sf.l2j.gameserver.model.L2TeleportLocation;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author NightMarez
 * @version $Revision: 1.3.2.2.2.5 $ $Date: 2005/03/27 15:29:32 $
 *
 */
public final class L2TeleporterInstance extends L2FolkInstance
{
    /**
     * @param objectId 
     * @param template
     */
    public L2TeleporterInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
	public void onBypassFeedback(L2PcInstance player, String command)
    {
        player.sendPacket(new ActionFailed());

        int condition = validateCondition(player);

        StringTokenizer st = new StringTokenizer(command, " ");
        String actualCommand = st.nextToken(); // Get actual command

        if (actualCommand.equalsIgnoreCase("goto"))
        {
            if (st.countTokens() <= 0)
            {
                return;
            }
            int whereTo = Integer.parseInt(st.nextToken());
            if (condition == COND_REGULAR)
            {
                doTeleport(player, whereTo);
                return;
            }
            else if (condition == COND_CASTLE_OWNER)
            {
                int minPrivilegeLevel = 0; // NOTE: Replace 0 with highest level when privilege level is implemented
                if (st.countTokens() >= 1)
                {
                    minPrivilegeLevel = Integer.parseInt(st.nextToken());
                }
                if (minPrivilegeLevel <= 10) // NOTE: Replace 10 with privilege level of player
                {
                	doTeleport(player, whereTo);
                }
                else
                {
                	player.sendMessage("You do not have the sufficient access level to teleport there.");
                }
                return;
            }
        }

        super.onBypassFeedback(player, command);
    }

    @Override
	public String getHtmlPath(int npcId, int val)
    {
        String pom = "";
        if (val == 0)
        {
            pom = "" + npcId;
        }
        else
        {
            pom = npcId + "-" + val;
        }

        return "data/html/teleporter/" + pom + ".htm";
    }

    @Override
	public void showChatWindow(L2PcInstance player)
    {
        String filename = "data/html/teleporter/castleteleporter-no.htm";

        int condition = validateCondition(player);
        if (condition == COND_REGULAR)
        {
            super.showChatWindow(player);
            return;
        }
        else if (condition > COND_ALL_FALSE)
        {
            if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
            {
            	filename = "data/html/teleporter/castleteleporter-busy.htm"; // Busy because of siege
            }
            else if (condition == COND_CASTLE_OWNER) // Clan owns castle
            {
                filename = getHtmlPath(getNpcId(), 0); // Owner message window
            }
        }
        else if (condition == COND_ALL_FALSE)
        {
        	// Seven signs teleporters
        	if (isNecropolisTeleporter() || isCatacombTeleporter())
        	{
        		filename = SevenSigns.SEVEN_SIGNS_HTML_PATH;
        		filename += isNecropolisTeleporter() ? "necro_no.htm" : "cata_no.htm";
        	}
        }

        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%npcname%", getName());
        player.sendPacket(html);
    }

    private void doTeleport(L2PcInstance player, int val)
    {
        L2TeleportLocation list = TeleportLocationTable.getInstance().getTemplate(val);
        if (list != null)
        {
            // You cannot teleport to village that is in siege
            if (!Config.ALLOW_SIEGE_TELEPORT && !list.getIsForNoble() && SiegeManager.getInstance().getSiege(list.getLocX(), list.getLocY(), list.getLocZ()) != null)
            {
                player.sendPacket(new SystemMessage(SystemMessage.CANNOT_TELEPORT_TO_A_VILLAGE_THAT_IS_IN_A_SIEGE));
                return;
            }
            else if (!Config.ALLOW_SIEGE_TELEPORT && !list.getIsForNoble() && TownManager.townHasCastleInSiege(list.getLocX(), list.getLocY()) && getIsInCastleTown())
            {
                player.sendPacket(new SystemMessage(SystemMessage.CANNOT_TELEPORT_TO_A_VILLAGE_THAT_IS_IN_A_SIEGE));
                return;
            }
            else if (player.getKarma() > 0 && !Config.ALT_GAME_KARMA_PLAYER_CAN_USE_GK)
            {
                player.sendMessage("Go away, you're not welcome here.");
                return;
            }
            else if (player.getPvpFlag() > 0 && !Config.ALT_GAME_FLAGGED_PLAYER_CAN_USE_GK)
            {
                player.sendMessage("You cannot use teleporting services while flagged.");
                return;
            }
            else if (list.getIsForNoble() && !player.isNoble())
            {
                String filename = "data/html/teleporter/nobleteleporter-no.htm";
                NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
                html.setFile(filename);
                html.replace("%objectId%", String.valueOf(getObjectId()));
                html.replace("%npcname%", getName());
                player.sendPacket(html);
                return;
            }
            else if (player.isAlikeDead())
            {
                return;
            }
            else if (!list.getIsForNoble() && (Config.ALT_GAME_FREE_TELEPORT || player.reduceAdena("Teleport", list.getPrice(), this, true)))
            {
                if (Config.DEBUG)
                    _log.fine("Teleporting player " + player.getName() + " to new location: "
                        + list.getLocX() + ":" + list.getLocY() + ":" + list.getLocZ());
                player.teleToLocation(list.getLocX(), list.getLocY(), list.getLocZ(), true);
            }
            else if(list.getIsForNoble() && (Config.ALT_GAME_FREE_TELEPORT || player.destroyItemByItemId("Noble Teleport", 6651, list.getPrice(), this, true)))
            {
                if (Config.DEBUG)
                    _log.fine("Teleporting player " + player.getName() + " to new location: "
                        + list.getLocX() + ":" + list.getLocY() + ":" + list.getLocZ());
                player.teleToLocation(list.getLocX(), list.getLocY(), list.getLocZ(), true);
            }
        }
        else
        {
            _log.warning("No teleport destination with id:" + val);
        }
        player.sendPacket(new ActionFailed());
    }

    public boolean isNecropolisTeleporter()
    {
    	return getNpcId() >= 8095 && getNpcId() <= 8102;
    }
    
    public boolean isCatacombTeleporter()
    {
    	return getNpcId() >= 8114 && getNpcId() <= 8119;
    }
    
    @Override
	protected int validateCondition(L2PcInstance player)
    {
    	// Seven signs dungeon teleporters
    	boolean isNecropolisTeleporter = isNecropolisTeleporter();
    	boolean isCatacombTeleporter = isCatacombTeleporter();
    	if (isNecropolisTeleporter || isCatacombTeleporter)
    	{
    		if (Config.SEVEN_SIGNS_DUNGEON_NPC_ACCESS)
    		{
    			return COND_REGULAR; // Regular access
    		}
    		
    		boolean isSealValidationPeriod = SevenSigns.getInstance().isSealValidationPeriod();
    		int playerCabal = SevenSigns.getInstance().getPlayerCabal(player);
    		if (isSealValidationPeriod)
			{
    			int compWinner = SevenSigns.getInstance().getCabalHighestScore();
    			int sealOwner = isNecropolisTeleporter ? SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_AVARICE) : SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_GNOSIS);
				if (playerCabal == SevenSigns.CABAL_NULL || playerCabal != compWinner || sealOwner != compWinner)
				{
					switch (compWinner)
					{
						case SevenSigns.CABAL_DAWN:
							player.sendPacket(new SystemMessage(SystemMessage.CAN_BE_USED_BY_DAWN));
							break;
						case SevenSigns.CABAL_DUSK:
							player.sendPacket(new SystemMessage(SystemMessage.CAN_BE_USED_BY_DUSK));
							break;
						case SevenSigns.CABAL_NULL:
							player.sendPacket(new SystemMessage(SystemMessage.CAN_BE_USED_DURING_QUEST_EVENT_PERIOD));
							break;
					}
				}
				else
				{
					return COND_REGULAR; // Regular access
				}
			}
			else
			{
				if (playerCabal != SevenSigns.CABAL_NULL)
				{
					return COND_REGULAR; // Regular access
				}
			}
    	}
    	else if (CastleManager.getInstance().getCastleIndex(this) < 0) // Teleporter isn't on castle ground
        {
        	return COND_REGULAR; // Regular access
        }
        else if (getCastle() != null && getCastle().getSiege().getIsInProgress()) // Teleporter is on castle ground and siege is in progress
        {
        	return COND_BUSY_BECAUSE_OF_SIEGE; // Busy because of siege
        }
        else if (player.getClan() != null) // Teleporter is on castle ground and player is in a clan
        {
            if (getCastle().getOwnerId() == player.getClanId()) // Clan owns castle
                return COND_CASTLE_OWNER; // Owner
        }

        return super.validateCondition(player);
    }
}