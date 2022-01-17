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
package teleports.RaceTrack;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * @authors DraX, mr.
 *
 */
public class RaceTrack extends Quest
{
	private static final int RACE_MANAGER = 7995;
	
	private static final Map<Integer, Location> RETURN_LOCATIONS = new HashMap<>();
	{
		RETURN_LOCATIONS.put(7320, new Location(-80826, 149775, -3043));	// RICHLIN
		RETURN_LOCATIONS.put(7256, new Location(-12672, 122776, -3116)); 	// BELLA
		RETURN_LOCATIONS.put(7059, new Location(15670, 142983, -2705)); 	// TRISHA
		RETURN_LOCATIONS.put(7080, new Location(83400, 147943, -3404)); 	// CLARISSA
		RETURN_LOCATIONS.put(7899, new Location(111409, 219364, -3545)); 	// FLAUEN
		RETURN_LOCATIONS.put(7177, new Location(82956, 53162, -1495)); 		// VALENTIA
		RETURN_LOCATIONS.put(7848, new Location(146331, 25762, -2018)); 	// ELISA
		RETURN_LOCATIONS.put(7233, new Location(116819, 76994, -2714)); 	// ESMERALDA
		RETURN_LOCATIONS.put(8320, new Location(43835, -47749, -792)); 		// ILYANA
		RETURN_LOCATIONS.put(8275, new Location(147930, -55281, -2728)); 	// TATIANA
		RETURN_LOCATIONS.put(7727, new Location(85335, 16177, -3694)); 		// VERONA
		RETURN_LOCATIONS.put(7836, new Location(105857, 109763, -3202)); 	// MINERVA
		RETURN_LOCATIONS.put(8210, new Location(12882, 181053, -3560)); 	// RACE TRACK GK
	}
	
	public static void main(String[] args)
    {
        // Quest class
        new RaceTrack();
    }
	
	public RaceTrack()
	{
		super(-1, RaceTrack.class.getSimpleName(), "teleports");
		
		for (int id : RETURN_LOCATIONS.keySet())
		{
			addStartNpc(id);
			addTalkId(id);
		}
		addTalkId(RACE_MANAGER);
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		L2PcInstance player = st.getPlayer();
		
		if (RETURN_LOCATIONS.containsKey(npc.getNpcId()))
		{
			player.teleToLocation(12661, 181687, -3560);
			st.setState(State.STARTED);
			st.set("id", Integer.toString(npc.getNpcId()));
		}
		else if (st.isStarted() && npc.getNpcId() == RACE_MANAGER)
		{
			if (RETURN_LOCATIONS.containsKey(st.getInt("id")))
			{
				Location loc = RETURN_LOCATIONS.get(st.getInt("id"));
				player.teleToLocation(loc.getX(), loc.getY(), loc.getZ());
				st.exitQuest(true);
			}
		}
		
		return null;
	}
}
