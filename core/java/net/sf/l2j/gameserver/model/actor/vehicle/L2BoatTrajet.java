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

public class L2BoatTrajet
{
    private final int _idWaypoint;
    private final int _ticketId;
    private final int _oustX;
    private final int _oustY;
    private final int _oustZ;
    private final String _announcerName;
    private final String _sysmess10;
    private final String _sysmess5;
    private final String _sysmess1;
    private final String _sysmessBegin;
    private final String _sysmess0;
    private final L2VehiclePathPoint[] _path;
    
    /**
     * @param idWaypoint
     * @param ticketId
     * @param oustX
     * @param oustY
     * @param oustZ
     * @param announcerName
     * @param sysmess10
     * @param sysmess5
     * @param sysmess1
     * @param sysmess0
     * @param sysmessBegin
     * @param path
     */
    public L2BoatTrajet(int idWaypoint, int ticketId, int oustX, int oustY, int oustZ, String announcerName, String sysmess10, String sysmess5, String sysmess1, String sysmess0, String sysmessBegin, L2VehiclePathPoint[] path)
    {
        _idWaypoint = idWaypoint;
        _ticketId = ticketId;
        _oustX = oustX;
        _oustY = oustY;
        _oustZ = oustZ;
        _announcerName = announcerName;
        _sysmess10 = sysmess10;
        _sysmess5 = sysmess5;
        _sysmess1 = sysmess1;
        _sysmessBegin = sysmessBegin;
        _sysmess0 = sysmess0;
        _path = path;
    }

    public int getIdWayPoint()
    {
        return _idWaypoint;
    }

    public int getTicketId()
    {
        return _ticketId;
    }

    public int getOustX()
    {
        return _oustX;
    }

    public int getOustY()
    {
        return _oustY;
    }

    public int getOustZ()
    {
        return _oustZ;
    }

    public String getAnnouncerName()
    {
        return _announcerName;
    }

    public String getSysMessage10()
    {
        return _sysmess10;
    }

    public String getSysMessage5()
    {
        return _sysmess5;
    }

    public String getSysMessage1()
    {
        return _sysmess1;
    }

    public String getSysMessage0()
    {
        return _sysmess0;
    }

    public String getSysMessageBegin()
    {
        return _sysmessBegin;
    }

    public L2VehiclePathPoint[] getPath()
    {
        return _path;
    }
}