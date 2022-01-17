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
package quests.Q093_SagaOfTheSpectralMaster;

import net.sf.l2j.gameserver.model.Location;
import quests.SagaScripts.SagaSuperclass;

/**
 * @author Emperorc
 *
 */
public class Q093_SagaOfTheSpectralMaster extends SagaSuperclass
{
	public static void main(String[] args)
	{
		// Quest class
		new Q093_SagaOfTheSpectralMaster();
	}
	
	public Q093_SagaOfTheSpectralMaster()
	{
		super(93, Q093_SagaOfTheSpectralMaster.class.getSimpleName(), "Saga of the Spectral Master");
		
		_classId = 111;
		_prevClassId = 41;
		_npcs = new int[] {7175, 8287, 8613, 7175, 8632, 8646, 8649, 8653, 8654, 8655, 8656, 8613};
		_items = new int[] {7606, 7508, 7291, 7322, 7353, 7384, 7415, 7446, 7112, 0};
		_mobs = new int[] {5315, 5242, 5312};
		
		_spawnLocs = new Location[]
		{
			new Location(164650, -74121, -2871),
			new Location(47429, -56923, -2383),
			new Location(47391, -56929, -2370),
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