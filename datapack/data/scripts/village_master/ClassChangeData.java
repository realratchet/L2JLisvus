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
package village_master;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.QuestState;

/**
 * @author DnR
 *
 */
public class ClassChangeData
{
	private final ClassId _newClass;
	private final ClassId _requiredClass;
	private final Race _requiredRace;
	private final String[] _states;
	private final int[] _requiredItems;
	
	public ClassChangeData(ClassId newClass, ClassId requiredClass, Race requiredRace, String[] states, int[] requiredItems)
	{
		_newClass = newClass;
		_requiredClass = requiredClass;
		_requiredRace = requiredRace;
		_states = states;
		_requiredItems = requiredItems;
	}
	
	public final void changeClass(QuestState st, L2PcInstance player)
	{
		for (int itemId : _requiredItems)
		{
			st.takeItems(itemId, 1);
		}
		st.playSound("ItemSound.quest_fanfare_2");
		player.setClassId(_newClass.getId());
		player.setBaseClass(_newClass);
		player.broadcastUserInfo();
	}
	
	public final ClassId getNewClass()
	{
		return _newClass;
	}

	/**
	 * @return the _requiredClass
	 */
	public ClassId getRequiredClass()
	{
		return _requiredClass;
	}

	/**
	 * @return the _requiredRace
	 */
	public Race getRequiredRace()
	{
		return _requiredRace;
	}

	/**
	 * @return the _states
	 */
	public String[] getStates()
	{
		return _states;
	}

	/**
	 * @return the _requiredItems
	 */
	public int[] getRequiredItems()
	{
		return _requiredItems;
	}
}
