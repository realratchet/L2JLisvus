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
package quests.Q081_SagaOfTheGhostHunter;

import net.sf.l2j.gameserver.model.Location;
import quests.SagaScripts.SagaSuperclass;

/**
 * @author Emperorc
 *
 */
public class Q081_SagaOfTheGhostHunter extends SagaSuperclass
{
	public static void main(String[] args)
	{
		// Quest class
		new Q081_SagaOfTheGhostHunter();
	}
	
	public Q081_SagaOfTheGhostHunter()
	{
		super(81, Q081_SagaOfTheGhostHunter.class.getSimpleName(), "Saga of the Ghost Hunter");
		
		_classId = 108;
		_prevClassId = 36;
		_npcs = new int[] {8603, 8624, 8286, 8615, 8617, 8646, 8649, 8653, 8654, 8655, 8656, 8616};
		_items = new int[] {7518, 7496, 7279, 7310, 7341, 7372, 7403, 7434, 7104, 0};
		_mobs = new int[] {5301, 5230, 5304};
		
		_spawnLocs = new Location[]
		{
			new Location(164650, -74121, -2871),
			new Location(47391, -56929, -2370),
			new Location(47429, -56923, -2383),
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