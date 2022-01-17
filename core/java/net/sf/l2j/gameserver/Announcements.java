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
package net.sf.l2j.gameserver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.scripting.scriptengine.DateRange;
import net.sf.l2j.util.StringUtil;

/**
 * This class ...
 * 
 * @version $Revision: 1.5.2.1.2.7 $ $Date: 2005/03/29 23:15:14 $
 */
public class Announcements
{
    private static Logger _log = Logger.getLogger(Announcements.class.getName());

    private List<String> _announcements = new ArrayList<>();
    private List<DateRange> _eventAnnouncements = new ArrayList<>();

    public Announcements()
    {
        loadAnnouncements();
    }

    public static Announcements getInstance()
    {
        return SingletonHolder._instance;
    }

    public void loadAnnouncements()
    {
        _announcements.clear();
        File file = new File(Config.DATAPACK_ROOT, "data/announcements.txt");
        if (file.exists())
            readFromDisk(file);
        else
            _log.config("data/announcements.txt doesn't exist");
    }

    public void showAnnouncements(L2PcInstance activeChar)
    {
    	for (String announce : _announcements)
        {
            CreatureSay cs = new CreatureSay(0, Say2.ANNOUNCEMENT, activeChar.getName(), announce);
            activeChar.sendPacket(cs);
        }

    	Date currentDate = new Date();
    	for (DateRange eventDate : _eventAnnouncements)
        {
    		if (!eventDate.isValid() || eventDate.isWithinRange(currentDate))
            {
    			if (eventDate.getMessages() != null)
            	{
            		for (String msg : eventDate.getMessages())
            		{
            			activeChar.sendMessage(msg);
            		}
            	}
            }  
        }
    }

    public void addEventAnnouncement(DateRange eventDate)
    {
    	_eventAnnouncements.add(eventDate);
    }

    public void listAnnouncements(L2PcInstance activeChar)
    {		
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder(500);
        StringUtil.append(replyMSG, "<html><body>",
        	"<table width=260><tr>",
        	"<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"<td width=180><center>Announcement Menu</center></td>",
        	"<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>",
        	"</tr></table>",
        	"<br><br>",
        	"<center>Add or announce a new announcement:</center>",
        	"<center><multiedit var=\"new_announcement\" width=240 height=30></center><br>",
        	"<center><table><tr><td>",
        	"<button value=\"Add\" action=\"bypass -h admin_add_announcement $new_announcement\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
        	"<button value=\"Announce\" action=\"bypass -h admin_announce_menu $new_announcement\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>",
        	"<button value=\"Reload\" action=\"bypass -h admin_announce_announcements\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">",
        	"</td></tr></table></center>",
        	"<br>");
        
        String content = "";
        for (int i = 0; i < _announcements.size(); i++)
        {
        	content += "<table width=260><tr><td width=220>" + _announcements.get(i).toString() + "</td><td width=40>";
        	content += "<button value=\"Delete\" action=\"bypass -h admin_del_announcement " + i + "\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>";
        }
        StringUtil.append(replyMSG, content + "</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    public void addAnnouncement(String text)
    {
        _announcements.add(text);
        saveToDisk();
    }

    public void delAnnouncement(int line)
    {
        _announcements.remove(line);
        saveToDisk();
    }

    private void readFromDisk(File file)
    {
        try (FileReader fr = new FileReader(file);
            LineNumberReader lnr = new LineNumberReader(fr))
        {
            String line = null;
            while ((line = lnr.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line,"\n\r");
                if (st.hasMoreTokens())
                {
                    String announcement = st.nextToken();
                    _announcements.add(announcement);
                }
            }

            _log.config("Announcements: Loaded " + _announcements.size() + " Announcements.");
        }
        catch (IOException e1)
        {
            _log.log(Level.SEVERE, "Error reading announcements", e1);
        }
    }

    private void saveToDisk()
    {
        File file = new File("data/announcements.txt");
        try (FileWriter save = new FileWriter(file))
        {
            for (int i = 0; i < _announcements.size(); i++)
            {
                save.write(_announcements.get(i).toString());
                save.write("\r\n");
            }
            save.flush();
        }
        catch (IOException e)
        {
            _log.warning("saving the announcements file has failed: " + e);
        }
    }

    public void announceToAll(String text)
    {
        CreatureSay cs = new CreatureSay(0, Say2.ANNOUNCEMENT, "", text);

        for (L2PcInstance player : L2World.getInstance().getAllPlayers())
        {
            if (player == null)
                continue;

            player.sendPacket(cs);
        }
    }

    public void announceToAll(SystemMessage sm)
    {
        for (L2PcInstance player : L2World.getInstance().getAllPlayers())
        {
            if (player == null)
                continue;

            player.sendPacket(sm);
        }
    }

    // Method for handling announcements from admin
    public void handleAnnounce(String command, int lengthToTrim)
    {
        try
        {
            // Announce string to everyone on server
            String text = command.substring(lengthToTrim);
            Announcements.getInstance().announceToAll(text);
        }
        catch (StringIndexOutOfBoundsException e)
        {
            // Nobody cares!
            // empty message.. ignore
        }
    }
    
    private static class SingletonHolder
	{
		protected static final Announcements _instance = new Announcements();
	}
}