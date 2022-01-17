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
package net.sf.l2j.gameserver.taskmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 * 
 * @author nBd
 */
public class AutoAnnounceTaskManager
{
    protected static final Logger _log = Logger.getLogger(AutoAnnounceTaskManager.class.getName());

    private List<AutoAnnouncement> _announcements = new ArrayList<>();

    public static AutoAnnounceTaskManager getInstance()
    {
        return SingletonHolder._instance;
    }

    public AutoAnnounceTaskManager()
    {
        restore();
    }

    public void restore()
    {
        if (!_announcements.isEmpty())
        {
            for (AutoAnnouncement a : _announcements)
            {
                a.stopAnnounce();
            }

            _announcements.clear();
        }

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
        	PreparedStatement statement = con.prepareStatement("SELECT id, initial, delay, cycle, memo FROM auto_announcements");
        	ResultSet rset = statement.executeQuery())
        {
            while(rset.next())
            {
                int id = rset.getInt("id");
                long initial = rset.getLong("initial");
                long delay = rset.getLong("delay");
                int repeat = rset.getInt("cycle");
                String memo = rset.getString("memo");
                String[] text = memo.split("/n");
                
                AutoAnnouncement a = new AutoAnnouncement(id, delay, repeat, text);
                _announcements.add(a);
                ThreadPoolManager.getInstance().scheduleGeneral(a, initial);
            }
        }
        catch (Exception e)
        {
            _log.log(Level.SEVERE, getClass().getSimpleName() + ": Failed to load announcements data.", e);
        }
        _log.log(Level.INFO, getClass().getSimpleName() + ": Loaded " + _announcements.size() + " Auto Announcement Data.");
    }

    private class AutoAnnouncement implements Runnable
    {
        private int _id;
        private long _delay;
        private int _repeat = -1;
        private String[] _memo;
        private boolean _stopped = false;

        public AutoAnnouncement(int id, long delay, int repeat, String[] memo)
        {
            _id = id;
            _delay = delay;
            _repeat = repeat;
            _memo = memo;
        }
        
        public int getId()
        {
        	return _id;
        }

        public void stopAnnounce()
        {
            _stopped = true;
        }

		@Override
		public void run()
		{
			if (!_stopped && _repeat != 0)
			{
				for (String text : _memo)
				{
					announce(text);
				}
				
				if (_repeat > 0)
				{
					_repeat--;
				}
				ThreadPoolManager.getInstance().scheduleGeneral(this, _delay);
			}
		}
    }

    public void announce(String text)
    {
        Broadcast.announceToOnlinePlayers(text);
        _log.warning("AutoAnnounce: " + text);
    }
    
    private static class SingletonHolder
	{
		protected static final AutoAnnounceTaskManager _instance = new AutoAnnounceTaskManager();
	}
}