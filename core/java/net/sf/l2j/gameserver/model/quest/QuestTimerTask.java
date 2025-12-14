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
package net.sf.l2j.gameserver.model.quest;

import java.util.logging.Logger;

public class QuestTimerTask implements Runnable
{
    private static Logger _log = Logger.getLogger(QuestTimerTask.class.getName());

    private final QuestTimer _timer;
    
    public QuestTimerTask(QuestTimer timer)
    {
        _timer = timer;
    }
    
    @Override
    public void run()
    {
        if (!_timer.isActive())
            return;
        
        try
        {
            if (!_timer.isRepeating())
            {
                _timer.destroy();
            }
            _timer.getQuest().notifyEvent(_timer.getName(), _timer.getNpc(), _timer.getPlayer());
        }
        catch (Exception e)
        {
            _log.warning(e.getMessage());
        }
    }
}