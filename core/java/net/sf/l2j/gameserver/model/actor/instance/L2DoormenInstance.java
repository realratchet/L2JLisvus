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

import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * This class ...
 * 
 * @version $Revision$ $Date$
 */
public class L2DoormenInstance extends L2FolkInstance
{
    private ClanHall _ClanHall;

    /**
     * @param objectID 
     * @param template
     */
    public L2DoormenInstance(int objectID, L2NpcTemplate template)
    {
        super(objectID, template);
    }

    public final ClanHall getClanHall()
    {
        if (_ClanHall == null)
            _ClanHall = ClanHallManager.getInstance().getNearbyClanHall(getX(), getY(), 500);
        return _ClanHall;
    }

    @Override
	public void onBypassFeedback(L2PcInstance player, String command)
    {
        int condition = validateCondition(player);
        if (condition <= COND_ALL_FALSE)
        {
        	return;
        }
        
        if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
        {
        	return;
        }
        
        if (condition == COND_CASTLE_OWNER || condition == COND_HALL_OWNER)
        {
            if (command.startsWith("Chat"))
            {
                showChatWindow(player);
                return;
            }
            else if (command.startsWith("open_doors"))
            {
                if (condition == COND_HALL_OWNER)
                {
                	if ((player.getClanPrivileges() & L2Clan.CP_CH_OPEN_DOOR) != L2Clan.CP_CH_OPEN_DOOR)
					{
                		player.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
                		return;
					}
                	
                    getClanHall().openCloseDoors(true);
                    player.sendPacket(new NpcHtmlMessage(getObjectId(), "<html><body>You have <font color=\"LEVEL\">opened</font> the clan hall door.<br>Outsiders may enter the clan hall while the door is open. Please close it when you've finished your business.<br><center><button value=\"Close\" action=\"bypass -h npc_" + getObjectId()                    	+ "_close_doors\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></body></html>"));
                }
                else
                {
                    StringTokenizer st = new StringTokenizer(command.substring(10), ", ");
                    st.nextToken(); // Bypass first value since its castle id / hall id

                    if (condition == 2)
                    {
                        while (st.hasMoreTokens())
                        {
                            getCastle().openDoor(player, Integer.parseInt(st.nextToken()));
                        }
                        return;
                    }
                }
            }
            else if (command.startsWith("close_doors"))
            {
                if (condition == COND_HALL_OWNER)
                {
                	if ((player.getClanPrivileges() & L2Clan.CP_CH_OPEN_DOOR) != L2Clan.CP_CH_OPEN_DOOR)
					{
                		player.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
                		return;
					}
                	
                    getClanHall().openCloseDoors(false);
                    player.sendPacket(new NpcHtmlMessage(getObjectId(), "<html><body>You have <font color=\"LEVEL\">closed</font> the clan hall door.<br>Good day!<br><center><button value=\"To Begining\" action=\"bypass -h npc_" + getObjectId()                    	+ "_Chat\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></body></html>"));
                }
                else
                {
                    StringTokenizer st = new StringTokenizer(command.substring(11), ", ");
                    st.nextToken(); // Bypass first value since its castle id/hall id

                    if (condition == 2)
                    {
                        while (st.hasMoreTokens())
                        {
                            getCastle().closeDoor(player, Integer.parseInt(st.nextToken()));
                        }
                        return;
                    }
                }
            }
        }

        super.onBypassFeedback(player, command);
    }

    @Override
	public void showChatWindow(L2PcInstance player)
    {
        String filename = "data/html/doormen/" + getTemplate().npcId + "-no.htm";

        int condition = validateCondition(player);
        if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
        	filename = "data/html/doormen/" + getTemplate().npcId + "-busy.htm"; // Busy because of siege
        else if (condition == COND_CASTLE_OWNER) // Clan owns castle
            filename = "data/html/doormen/" + getTemplate().npcId + ".htm"; // Owner message window

        // Prepare doormen for clan hall
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        String str;
        if (getClanHall() != null)
        {
            if (condition == COND_HALL_OWNER)
            {
                str = "<html><body>Hello!<br><font color=\"55FFFF\">" + player.getName()
                    + "</font>, I am honored to serve your clan.<br>How may i serve you?<br>";
                str += "<center><table><tr><td><button value=\"Open Door\" action=\"bypass -h npc_%objectId%_open_doors\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br1></td></tr></table><br>";
                str += "<table><tr><td><button value=\"Close Door\" action=\"bypass -h npc_%objectId%_close_doors\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table></center></body></html>";
            }
            else
            {
                L2Clan owner = ClanTable.getInstance().getClan(getClanHall().getOwnerId());
                if (owner != null && owner.getLeader() != null)
                {
                    str = "<html><body>Hello there!<br>This clan hall is owned by <font color=\"55FFFF\">"
                        + owner.getLeader().getName() + " who is the Lord of the ";
                    str += owner.getName() + "</font> clan.<br>";
                    str += "I am sorry, but only the clan members who belong to the <font color=\"55FFFF\">"
                        + owner.getName() + "</font> clan can enter the clan hall.</body></html>";
                }
                else str = "<html><body>" + getName() + ":<br1>Clan hall <font color=\"LEVEL\">"
                    + getClanHall().getName()
                    + "</font> has no owner.<br>You can rent it at auctioneers.</body></html>";
            }
            html.setHtml(str);
        }
        else
        	html.setFile(filename);

        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
        
        player.sendPacket(new ActionFailed());
    }

    @Override
	protected int validateCondition(L2PcInstance player)
    {
        if (player.getClan() != null)
        {
            // Prepare doormen for clan hall
            if (getClanHall() != null)
            {
                if (player.getClanId() == getClanHall().getOwnerId())
                    return COND_HALL_OWNER;
				return COND_ALL_FALSE;
            }
            if (getCastle() != null && getCastle().getCastleId() > 0)
            {
                if (getCastle().getSiege().getIsInProgress())
                    return COND_BUSY_BECAUSE_OF_SIEGE; // Busy because of siege
                else if (getCastle().getOwnerId() == player.getClanId()) // Clan owns castle
                    return COND_CASTLE_OWNER; // Owner
            }
        }

        return super.validateCondition(player);
    }
}