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
package quests.Q083_SagaOfTheMoonlightSentinel;

import net.sf.l2j.gameserver.model.Location;
import quests.SagaScripts.SagaSuperclass;

/**
 * @author Emperorc
 *
 */
public class Q083_SagaOfTheMoonlightSentinel extends SagaSuperclass
{
	public static void main(String[] args)
	{
		// Quest class
		new Q083_SagaOfTheMoonlightSentinel();
	}
	
	public Q083_SagaOfTheMoonlightSentinel()
	{
		super(83, Q083_SagaOfTheMoonlightSentinel.class.getSimpleName(), "Saga of the Moonlight Sentinel");
		
		_classId = 102;
		_prevClassId = 24;
		_npcs = new int[] {7702, 8627, 8604, 8640, 8634, 8646, 8648, 8652, 8654, 8655, 8658, 8641};
		_items = new int[] {7520, 7498, 7281, 7312, 7343, 7374, 7405, 7436, 7106, 0};
		_mobs = new int[] {5297, 5232, 5306};
		
		_spawnLocs = new Location[]
		{
			new Location(161719, -92823, -1893),
			new Location(181227, 36703, -4816),
			new Location(181215, 36676, -4812),
		};
		
		_texts = new String[]
		{
			"PLAYERNAME! Pursued to here! However, I jumped out of the Banshouren boundaries! You look at the giant as the sign of power!",
			"... Oh ... good! So it was ... let's begin!",
			"I do not have the patience ..! I have been a giant force ...! Cough chatter ah ah ah!",
			"Paying homage to those who disrupt the orderly will be PLAYERNAME's death!",
			"Now, my soul freed from the shackles of the millennium, Halixia, to the back side I come ...",
			"Why do you interfere others' battles?",
			"This is a waste of time.. Say goodbye...!",
			"...That is the enemy",
			"...Goodness! PLAYERNAME you are still looking?",
			"PLAYERNAME ... Not just to whom the victory. Only personnel involved in the fighting are eligible to share in the victory.",
			"Your sword is not an ornament. Don't you think, PLAYERNAME?",
			"Goodness! I no longer sense a battle there now.",
			"let...",
			"Only engaged in the battle to bar their choice. Perhaps you should regret.",
			"The human nation was foolish to try and fight a giant's strength.",
			"Must...Retreat... Too...Strong.",
			"PLAYERNAME. Defeat...by...retaining...and...Mo...Hacker",
			"....! Fight...Defeat...It...Fight...Defeat...It..."
		};
		
		// Finally, register all events to be triggered appropriately, using the overridden values.
		registerNPCs();
	}
}