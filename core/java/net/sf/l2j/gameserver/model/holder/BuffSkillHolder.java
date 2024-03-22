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
package net.sf.l2j.gameserver.model.holder;

public class BuffSkillHolder extends SkillHolder
{
    private final int _duration, _skillFeeId, _skillFeeAmount;
    
    public BuffSkillHolder(int id, int level, int duration, int skillFeeId, int skillFeeAmount)
    {
        super(id, level);
        
        _duration = duration;
        _skillFeeId = skillFeeId;
        _skillFeeAmount = skillFeeAmount;
    }
    
    public int getDuration()
    {
        return _duration;
    }
    
    public int getSkillFeeId()
    {
        return _skillFeeId;
    }
    
    public int getSkillFeeAmount()
    {
        return _skillFeeAmount;
    }

    @Override
    public String toString()
    {
        return super.toString() + ", duration: " + _duration + ", fee ID: " + _skillFeeId + ", fee amount: " + _skillFeeAmount;
    }
}
