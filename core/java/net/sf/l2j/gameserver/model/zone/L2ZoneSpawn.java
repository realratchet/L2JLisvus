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
package net.sf.l2j.gameserver.model.zone;

/**
 * Abstract zone with spawn locations
 * @author DS
 */
public abstract class L2ZoneSpawn extends L2ZoneType
{
    private String _restartPointGroup;
    
    public L2ZoneSpawn(int id)
    {
        super(id);
    }
    
    @Override
    public void setParameter(String name, String value)
    {
        if (name.equals("restartPointGroup"))
        {
            _restartPointGroup = value;
        }
    }

    public final String getRestartPointGroup()
    {
        return _restartPointGroup;
    }
}