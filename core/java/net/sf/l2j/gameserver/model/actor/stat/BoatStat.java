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
package net.sf.l2j.gameserver.model.actor.stat;

import net.sf.l2j.gameserver.model.actor.instance.L2BoatInstance;

public class BoatStat extends CharStat
{
    // =========================================================
    // Data Field
    private float _moveSpeed = 0;
    private int _rotationSpeed = 0;

    // =========================================================
    // Constructor
    public BoatStat(L2BoatInstance activeChar)
    {
        super(activeChar);
    }

    // =========================================================
    // Property - Public
    @Override
	public L2BoatInstance getActiveChar()
    {
        return (L2BoatInstance)super.getActiveChar();
    }

    @Override
    public final float getMoveSpeed()
    {
        return _moveSpeed;
    }

    public final void setMoveSpeed(float speed)
    {
        _moveSpeed = speed;
    }

    public final int getRotationSpeed()
    {
        return _rotationSpeed;
    }

    public final void setRotationSpeed(int speed)
    {
        _rotationSpeed = speed;
    }
}