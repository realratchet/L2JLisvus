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
package net.sf.l2j.gameserver.communitybbs.Manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ShowBoard;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.StringUtil;

public class RegionBBSManager extends BaseBBSManager
{
    private static Logger _logChat = Logger.getLogger("chat");

    private int _onlineCount = 0;
    private int _onlineCountGm = 0;
    private static Map<Integer, List<L2PcInstance>> _onlinePlayers = new ConcurrentHashMap<>();
    private static Map<Integer, Map<String, String>> _communityPages = new ConcurrentHashMap<>();

    /**
     * @return
     */
    public static RegionBBSManager getInstance()
    {
        return SingletonHolder._instance;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager#parsecmd(java.lang.String, net.sf.l2j.gameserver.model.actor.instance.L2PcInstance)
     */
    @Override
    public void parsecmd(String command, L2PcInstance activeChar)
    {
        if (command.equals("_bbsloc"))
            showOldCommunity(activeChar, 1);
        else if (command.startsWith("_bbsloc;page;"))
        {
            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();
            st.nextToken();
            int page = 0;
            try
            {
                page = Integer.parseInt(st.nextToken());
            }
            catch (NumberFormatException nfe)
            {
            }
            showOldCommunity(activeChar, page);	
        }
        else if (command.startsWith("_bbsloc;playerinfo;"))
        {
            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();
            st.nextToken();
            String name = st.nextToken();

            showOldCommunityPI(activeChar, name);
        }
        else
        {
            if (Config.COMMUNITY_TYPE == 1)
                showOldCommunity(activeChar, 1);
            else
            {
                ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: "+command+" is not implemented yet</center><br><br></body></html>","101");
                activeChar.sendPacket(sb);
                activeChar.sendPacket(new ShowBoard(null, "102"));
                activeChar.sendPacket(new ShowBoard(null,"103"));
            }
        }
    }

    /**
     * @param activeChar
     * @param name
     */
    private void showOldCommunityPI(L2PcInstance activeChar, String name)
    {
        StringBuilder htmlCode = new StringBuilder(1000);
        StringUtil.append(htmlCode, 
        	"<html><body><br>",
        	"<table border=0><tr><td FIXWIDTH=15></td><td align=center>Community Board<img src=\"sek.cbui355\" width=610 height=1></td></tr><tr><td FIXWIDTH=15></td><td>");

        L2PcInstance player = L2World.getInstance().getPlayer(name);
        if (player != null)
        {
            String sex = "Male";
            if (player.getAppearance().getSex())
                sex = "Female";

            String levelApprox = "low";
            if (player.getLevel() >= 60)
                levelApprox = "very high";
            else if (player.getLevel() >= 40)
                levelApprox = "high";
            else if (player.getLevel() >= 20)
                levelApprox = "medium";

            StringUtil.append(htmlCode, 
            	"<table border=0><tr><td>"+player.getName()+" ("+sex+" "+player.getTemplate().className+"):</td></tr>",
            	"<tr><td>Level: "+levelApprox+"</td></tr>",
            	"<tr><td><br></td></tr>");

            if (activeChar != null && (activeChar.isGM() || player.getObjectId() == activeChar.getObjectId() || Config.SHOW_LEVEL_COMMUNITYBOARD))
            {
                long nextLevelExp = 0;
                long nextLevelExpNeeded = 0;
                if (player.getLevel() < (Config.MAX_PLAYER_LEVEL - 1))
                {
                    nextLevelExp = Experience.LEVEL[player.getLevel() + 1];
                    nextLevelExpNeeded = nextLevelExp-player.getExp();
                }

                StringUtil.append(htmlCode, 
                	"<tr><td>Level: "+player.getLevel()+"</td></tr>",
                	"<tr><td>Experience: "+player.getExp()+"/"+nextLevelExp+"</td></tr>",
                	"<tr><td>Experience needed for level up: "+nextLevelExpNeeded+"</td></tr>",
                	"<tr><td><br></td></tr>");
            }

            int uptime = (int)player.getUptime()/1000;
            int h = uptime/3600;
            int m = (uptime-(h*3600))/60;
            int s = ((uptime-(h*3600))-(m*60));

            StringUtil.append(htmlCode, 
            	"<tr><td>Uptime: "+h+"h "+m+"m "+s+"s</td></tr>",
            	"<tr><td><br></td></tr>");

            if (player.getClan() != null)
            {
            	StringUtil.append(htmlCode, 
            		"<tr><td>Clan: "+player.getClan().getName()+"</td></tr>",
            		"<tr><td><br></td></tr>");
            }

            StringUtil.append(htmlCode, 
            	"<tr><td><multiedit var=\"pm\" width=240 height=40><button value=\"Send PM\" action=\"Write Region PM "+player.getName()+" pm pm pm\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr><tr><td><br><button value=\"Back\" action=\"bypass _bbsloc\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>",
            	"</td></tr></table>",
            	"</body></html>");
            separateAndSend(htmlCode.toString(),activeChar);
        }
        else
        {
            ShowBoard sb = new ShowBoard("<html><body><br><br><center>No player with name "+name+"</center><br><br></body></html>","101");
            activeChar.sendPacket(sb);
            activeChar.sendPacket(new ShowBoard(null,"102"));
            activeChar.sendPacket(new ShowBoard(null,"103"));  
        }
    }

    /**
     * @param activeChar
     * @param page 
     */
    private void showOldCommunity(L2PcInstance activeChar, int page)
    {
        separateAndSend(getCommunityPage(page, activeChar.isGM() ? "gm" : "pl"), activeChar);
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager#parsewrite(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, net.sf.l2j.gameserver.model.actor.instance.L2PcInstance)
     */
    @Override
    public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
    {
        if (activeChar == null)
            return;

        if (ar1.equals("PM"))
        {			
            StringBuilder htmlCode = new StringBuilder(500);
            StringUtil.append(htmlCode, "<html><body><br>",
            	"<table border=0><tr><td FIXWIDTH=15></td><td align=center>Community Board<img src=\"sek.cbui355\" width=610 height=1></td></tr><tr><td FIXWIDTH=15></td><td>");

            try
            {
            	L2PcInstance receiver = L2World.getInstance().getPlayer(ar2);
            	if (receiver == null)
            	{
            		StringUtil.append(htmlCode, 
            			"Player not found!<br><button value=\"Back\" action=\"bypass _bbsloc;playerinfo;"+ar2+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
            			"</td></tr></table></body></html>");
                    separateAndSend(htmlCode.toString(), activeChar);
                    return;
            	}

                if (Config.JAIL_DISABLE_CHAT && receiver.isInJail())
                {
                    activeChar.sendMessage("Player is in jail.");
                    return;
                }

                if (receiver.isChatBanned())
                {
                    activeChar.sendMessage("Player is chat-banned.");
                    return;
                }

                if (activeChar.isInJail() && Config.JAIL_DISABLE_CHAT)
                {
                    activeChar.sendMessage("You cannot chat while in jail.");
                    return;
                }

            	if (Config.LOG_CHAT)  
            	{ 
                    LogRecord record = new LogRecord(Level.INFO, ar3); 
                    record.setLoggerName("chat"); 
                    record.setParameters(new Object[]
                    {
                        "TELL", "[" + activeChar.getName() + " to "+receiver.getName()+"]"
                    }); 
                    _logChat.log(record);
                }

            	CreatureSay cs = new CreatureSay(activeChar.getObjectId(), Say2.TELL, activeChar.getName(), ar3);
            	if (!BlockList.isBlocked(receiver, activeChar))
                {
                    if (!receiver.getMessageRefusal())
                    {
                        receiver.sendPacket(cs);
                        activeChar.sendPacket(new CreatureSay(activeChar.getObjectId(), Say2.TELL, "->" + receiver.getName(), ar3));
                        StringUtil.append(htmlCode, 
                        	"Message Sent<br><button value=\"Back\" action=\"bypass _bbsloc;playerinfo;"+receiver.getName()+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
                        	"</td></tr></table></body></html>");
                        separateAndSend(htmlCode.toString(),activeChar);
                    }
                    else
                    {
                        SystemMessage sm = new SystemMessage(SystemMessage.THE_PERSON_IS_IN_MESSAGE_REFUSAL_MODE);        
                        activeChar.sendPacket(sm);
                        parsecmd("_bbsloc;playerinfo;"+receiver.getName(), activeChar);
                    }
                }
                else
                {
                    SystemMessage sm = new SystemMessage(SystemMessage.S1_IS_NOT_ONLINE);
                    sm.addString(receiver.getName());
                    activeChar.sendPacket(sm);
                    sm = null;
                }
            }
            catch (StringIndexOutOfBoundsException e)
            {
                // ignore
            }
        }
        else
        {
            ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: "+ar1+" is not implemented yet</center><br><br></body></html>","101");
            activeChar.sendPacket(sb);
            activeChar.sendPacket(new ShowBoard(null,"102"));
            activeChar.sendPacket(new ShowBoard(null,"103"));  
        }
    }

    public synchronized void changeCommunityBoard()
    {
        if (Config.COMMUNITY_TYPE == 0)
            return;

        Collection<L2PcInstance> players = L2World.getInstance().getAllPlayers();
        List<L2PcInstance> sortedPlayers = new ArrayList<>();
        sortedPlayers.addAll(players);
        players = null;

        Collections.sort(sortedPlayers, new Comparator<L2PcInstance>()
        {
            @Override
			public int compare(L2PcInstance p1, L2PcInstance p2)
            {
                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        });

        _onlinePlayers.clear();
        _onlineCount = 0;
        _onlineCountGm = 0;

        for (L2PcInstance player : sortedPlayers)
        {
            addOnlinePlayer(player);
        }

        _communityPages.clear();
        writeCommunityPages();
    }

    private void addOnlinePlayer(L2PcInstance player)
    {
        boolean added = false;

        for (List<L2PcInstance> page : _onlinePlayers.values())
        {
            if (page.size() < Config.NAME_PAGE_SIZE_COMMUNITYBOARD)
            {
                if (!page.contains(player))
                {
                    page.add(player);
                    if (!player.getAppearance().getInvisible())
                        _onlineCount++;
                    _onlineCountGm++;
                }
                added = true;
                break;
            }
            else if (page.contains(player))
            {
                added = true;
                break;
            }
        }

        if (!added)
        {
            List<L2PcInstance> temp = new ArrayList<>();
            int page = _onlinePlayers.size() + 1;
            if (temp.add(player))
            {
                _onlinePlayers.put(page, temp);
                if (!player.getAppearance().getInvisible())
                    _onlineCount++;
                _onlineCountGm++;
            }
        }
    }

    private void writeCommunityPages()
    {
        for (int page : _onlinePlayers.keySet())
        {
            Map<String, String> communityPage = new HashMap<>();
            StringBuilder htmlCode = new StringBuilder(2000);
            String tdClose = "</td>";
            String tdOpen = "<td align=left valign=top>";
            String trClose = "</tr>";
            String trOpen = "<tr>";
            String colSpacer = "<td FIXWIDTH=15></td>";
            
            StringUtil.append(htmlCode, 
            	"<html><body><br>",
            	"<table>",
            	trOpen,
            	tdOpen + "Server Restarted: " + GameServer.getDateTimeServerStarted().getTime() + tdClose,
            	trClose,
            	"</table>",
            	"<table>",
            	trOpen,
            	tdOpen + "XP Rate: x" + Config.RATE_XP + tdClose,
            	colSpacer,
            	tdOpen + "Party XP Rate: x" + Config.RATE_XP * Config.RATE_PARTY_XP + tdClose,
            	colSpacer,
            	tdOpen + "XP Exponent: " + Config.ALT_GAME_EXPONENT_XP + tdClose,
            	trClose,
            	trOpen,
            	tdOpen + "SP Rate: x" + Config.RATE_SP + tdClose,
            	colSpacer,
            	tdOpen + "Party SP Rate: x" + Config.RATE_SP * Config.RATE_PARTY_SP + tdClose,
            	colSpacer,
            	tdOpen + "SP Exponent: " + Config.ALT_GAME_EXPONENT_SP + tdClose,
            	trClose,
            	trOpen,
            	tdOpen + "Drop Rate: x" + Config.RATE_DROP_ITEMS + tdClose,
            	colSpacer,
            	tdOpen + "Boss Drop Rate: x" + Config.RATE_BOSS_DROP_ITEMS + tdClose,
            	colSpacer,
            	tdOpen + "Spoil Rate: x" + Config.RATE_DROP_SPOIL + tdClose,
            	colSpacer,
            	tdOpen + "Adena Rate: x" + Config.RATE_DROP_ADENA + tdClose,
            	trClose,
            	"</table>",
            	"<table>",
            	trOpen,
            	"<td><img src=\"sek.cbui355\" width=600 height=1><br></td>",
            	trClose,
            	trOpen,
            	tdOpen + L2World.getInstance().getAllVisibleObjectsCount() + " Object count</td>",
            	trClose,
            	trOpen,
            	tdOpen + getOnlineCount("gm") + " Player(s) Online</td>",
            	trClose,
            	"</table>");

            int cell = 0;
            if (Config.BBS_SHOW_PLAYERLIST)
            {
                StringUtil.append(htmlCode, 
                	"<table border=0>",
                	"<tr><td><table border=0>");

                for (L2PcInstance player : getOnlinePlayers(page))
                {
                    cell++;

                    if (cell == 1)
                        htmlCode.append(trOpen);

                    StringUtil.append(htmlCode, "<td align=left valign=top FIXWIDTH=110><a action=\"bypass _bbsloc;playerinfo;" + player.getName() + "\">");

                    if (player.isGM())
                    	StringUtil.append(htmlCode, "<font color=\"LEVEL\">" + player.getName() + "</font>");
                    else
                    	StringUtil.append(htmlCode, player.getName());

                    StringUtil.append(htmlCode, "</a></td>");

                    if (cell < Config.NAME_PER_ROW_COMMUNITYBOARD)
                    	StringUtil.append(htmlCode, colSpacer);

                    if (cell == Config.NAME_PER_ROW_COMMUNITYBOARD)
                    {
                        cell = 0;
                        StringUtil.append(htmlCode, trClose);
                    }
                }

                if (cell > 0 && cell < Config.NAME_PER_ROW_COMMUNITYBOARD)
                	StringUtil.append(htmlCode, trClose);
                StringUtil.append(htmlCode, "</table></td></tr>");

                StringUtil.append(htmlCode, 
                	trOpen,
                	"<td><img src=\"sek.cbui355\" width=600 height=1><br></td>",
                	trClose,
                	"</table>");
            }

            if (getOnlineCount("gm") > Config.NAME_PAGE_SIZE_COMMUNITYBOARD)
            {
            	StringUtil.append(htmlCode, "<table border=0 width=600>", "<tr>");
            	
                if (page == 1)
                	StringUtil.append(htmlCode, "<td align=right width=190><button value=\"Prev\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
                else
                	StringUtil.append(htmlCode, "<td align=right width=190><button value=\"Prev\" action=\"bypass _bbsloc;page;" + (page - 1) + "\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");

                StringUtil.append(htmlCode, "<td FIXWIDTH=10></td>");
                StringUtil.append(htmlCode, "<td align=center valign=top width=200>Displaying " + (((page - 1) * Config.NAME_PAGE_SIZE_COMMUNITYBOARD) + 1) + " - " + (((page - 1) * Config.NAME_PAGE_SIZE_COMMUNITYBOARD) + getOnlinePlayers(page).size()) + " player(s)</td>");
                StringUtil.append(htmlCode, "<td FIXWIDTH=10></td>");
                if (getOnlineCount("gm") <= (page * Config.NAME_PAGE_SIZE_COMMUNITYBOARD))
                	StringUtil.append(htmlCode, "<td width=190><button value=\"Next\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
                else
                	StringUtil.append(htmlCode, "<td width=190><button value=\"Next\" action=\"bypass _bbsloc;page;" + (page + 1) + "\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");

                StringUtil.append(htmlCode, "</tr>", "</table>");
            }

            StringUtil.append(htmlCode, "</body></html>");

            communityPage.put("gm", htmlCode.toString());

            htmlCode.setLength(0);
            StringUtil.append(htmlCode, 
            	"<html><body><br>",
            	"<table>",
            	trOpen,
            	tdOpen + "Server Restarted: " + GameServer.getDateTimeServerStarted().getTime() + tdClose,
            	trClose,
            	"</table>",
            	"<table>",
            	trOpen,
            	tdOpen + "XP Rate: x" + Config.RATE_XP + tdClose,
            	colSpacer,
            	tdOpen + "Party XP Rate: x" + Config.RATE_XP * Config.RATE_PARTY_XP + tdClose,
            	colSpacer,
            	tdOpen + "XP Exponent: " + Config.ALT_GAME_EXPONENT_XP + tdClose,
            	trClose,
            	trOpen,
            	tdOpen + "SP Rate: x" + Config.RATE_SP + tdClose,
            	colSpacer,
            	tdOpen + "Party SP Rate: x" + Config.RATE_SP * Config.RATE_PARTY_SP + tdClose,
            	colSpacer,
            	tdOpen + "SP Exponent: " + Config.ALT_GAME_EXPONENT_SP + tdClose,
            	trClose,
            	trOpen,
            	tdOpen + "Drop Rate: x" + Config.RATE_DROP_ITEMS + tdClose,
            	colSpacer,
            	tdOpen + "Boss Drop Rate: x" + Config.RATE_BOSS_DROP_ITEMS + tdClose,
            	colSpacer,
            	tdOpen + "Spoil Rate: x" + Config.RATE_DROP_SPOIL + tdClose,
            	colSpacer,
            	tdOpen + "Adena Rate: x" + Config.RATE_DROP_ADENA + tdClose,
            	trClose,
            	"</table>",
            	"<table>",
            	trOpen,
            	"<td><img src=\"sek.cbui355\" width=600 height=1><br></td>",
            	trClose,
            	trOpen,
            	tdOpen + getOnlineCount("gm") + " Player(s) Online</td>",
            	trClose,
            	"</table>");

            if (Config.BBS_SHOW_PLAYERLIST)
            {
            	StringUtil.append(htmlCode, "<table border=0>");
            	StringUtil.append(htmlCode, "<tr><td><table border=0>");

                cell = 0;
                for (L2PcInstance player : getOnlinePlayers(page))
                {
                    if (player == null || player.getAppearance().getInvisible())
                        continue; // Go to next

                    cell++;

                    if (cell == 1)
                    	StringUtil.append(htmlCode, trOpen);

                    StringUtil.append(htmlCode, "<td align=left valign=top FIXWIDTH=110><a action=\"bypass _bbsloc;playerinfo;" + player.getName() + "\">");

                    if (player.isGM())
                    	StringUtil.append(htmlCode, "<font color=\"LEVEL\">" + player.getName() + "</font>");
                    else
                    	StringUtil.append(htmlCode, player.getName());

                    StringUtil.append(htmlCode, "</a></td>");

                    if (cell < Config.NAME_PER_ROW_COMMUNITYBOARD)
                    	StringUtil.append(htmlCode, colSpacer);

                    if (cell == Config.NAME_PER_ROW_COMMUNITYBOARD)
                    {
                        cell = 0;
                        StringUtil.append(htmlCode, trClose);
                    }
                }

                if (cell > 0 && cell < Config.NAME_PER_ROW_COMMUNITYBOARD)
                	StringUtil.append(htmlCode, trClose);
                StringUtil.append(htmlCode, "</table><br></td></tr>");

                StringUtil.append(htmlCode, 
                	trOpen,
                	"<td><img src=\"sek.cbui355\" width=600 height=1><br></td>",
                	trClose,
                	"</table>");
            }

            if (getOnlineCount("pl") > Config.NAME_PAGE_SIZE_COMMUNITYBOARD)
            {
            	StringUtil.append(htmlCode, "<table border=0 width=600>", "<tr>");

            	if (page == 1)
            		StringUtil.append(htmlCode, "<td align=right width=190><button value=\"Prev\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
                else
                	StringUtil.append(htmlCode, "<td align=right width=190><button value=\"Prev\" action=\"bypass _bbsloc;page;" + (page - 1) + "\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");

            	StringUtil.append(htmlCode, "<td FIXWIDTH=10></td>");
            	StringUtil.append(htmlCode, "<td align=center valign=top width=200>Displaying " + (((page - 1) * Config.NAME_PAGE_SIZE_COMMUNITYBOARD) + 1) + " - " + (((page - 1) * Config.NAME_PAGE_SIZE_COMMUNITYBOARD) + getOnlinePlayers(page).size()) + " player(s)</td>");
            	StringUtil.append(htmlCode, "<td FIXWIDTH=10></td>");

                if (getOnlineCount("pl") <= (page * Config.NAME_PAGE_SIZE_COMMUNITYBOARD))
                	StringUtil.append(htmlCode, "<td width=190><button value=\"Next\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
                else
                	StringUtil.append(htmlCode, "<td width=190><button value=\"Next\" action=\"bypass _bbsloc;page;" + (page + 1) + "\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");

                StringUtil.append(htmlCode, "</tr>", "</table>");
            }

            StringUtil.append(htmlCode, "</body></html>");
            communityPage.put("pl", htmlCode.toString());

            _communityPages.put(page, communityPage);
        }
    }

    private int getOnlineCount(String type)
    {
        if (type.equalsIgnoreCase("gm"))
             return _onlineCountGm;
        return _onlineCount;
    }

    private List<L2PcInstance> getOnlinePlayers(int page)
    {
        return _onlinePlayers.get(page);
    }

    public String getCommunityPage(int page, String type)
    {
        if (_communityPages.get(page) != null)
            return _communityPages.get(page).get(type);
        return null;
    }
    
    private static class SingletonHolder
	{
		protected static final RegionBBSManager _instance = new RegionBBSManager();
	}
}