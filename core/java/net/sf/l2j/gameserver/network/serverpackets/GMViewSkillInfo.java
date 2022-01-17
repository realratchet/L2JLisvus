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
package net.sf.l2j.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class GMViewSkillInfo extends L2GameServerPacket
{
	private static final String _S__91_GMViewSkillInfo = "[S] 91 GMViewSkillInfo";
	
	private final L2PcInstance _cha;
	private final List<L2Skill> _skills;
	
	public GMViewSkillInfo(L2PcInstance cha, List<L2Skill> skills)
	{
		_cha = cha;
		_skills = skills;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x91);
		writeS(_cha.getName());
		writeD(_skills.size());
		
		for (L2Skill temp : _skills)
        {
			if (temp.getId() > 9000 && temp.getId() < 9007)
			{
				continue; // Fake skills to change base stats
			}
			
            writeD(temp.isPassive() ? 1 : 0);
            writeD(temp.getLevel());
            writeD(temp.getId());
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__91_GMViewSkillInfo;
	}
}