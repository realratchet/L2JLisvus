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
package net.sf.l2j.gameserver.model.actor.vehicle;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.actor.instance.L2BoatInstance;

public class L2BoatCaptain implements Runnable
{
    private final int _state;
    private final L2BoatInstance _boat;
    
    /**
     * @param i
     * @param instance
     */
    public L2BoatCaptain(int i, L2BoatInstance instance)
    {
        _state = i;
        _boat = instance;
    }
    
    @Override
    public void run()
    {
        switch (_state)
        {
            case 1:
                _boat.say(5);
                ThreadPoolManager.getInstance().scheduleGeneral(new L2BoatCaptain(2, _boat), 240000);
                break;
            case 2:
                _boat.say(1);
                ThreadPoolManager.getInstance().scheduleGeneral(new L2BoatCaptain(3, _boat), 40000);
                break;
            case 3:
                _boat.say(0);
                ThreadPoolManager.getInstance().scheduleGeneral(new L2BoatCaptain(4, _boat), 20000);
                break;
            case 4:
                _boat.say(-1);
                _boat.begin();
                break;
        }
    }
}