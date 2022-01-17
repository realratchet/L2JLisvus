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
package teleports.OracleTeleport;

import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.util.Util;

public class OracleTeleport extends Quest
{
	private static final int[] TOWN_DAWN = {8078, 8079, 8080, 8081, 8083, 8084, 8082, 8692, 8694, 8168};
	private static final int[] TOWN_DUSK = {8085, 8086, 8087, 8088, 8090, 8091, 8089, 8693, 8695, 8169};
	private static final int[] TEMPLE_PRIEST = {8127, 8128, 8129, 8130, 8131, 8137, 8138, 8139, 8140, 8141};
	
	private static final Location[] RETURN_LOCS = 
	{
		// Dawn
		new Location(-80555, 150337, -3040),
		new Location(-13953, 121404, -2984),
		new Location(16354, 142820, -2696),
		new Location(83369, 149253, -3400),
		new Location(83106, 53965, -1488),
		new Location(146983, 26595, -2200),
		new Location(111386, 220858, -3544),
		new Location(148297, -55478, -2781),
		new Location(45664, -50318, -800),
		new Location(115136, 74717, -2608),
		// Dusk
		new Location(-82368, 151568, -3120),
		new Location(-14748, 123995, -3112),
		new Location(18482, 144576, -3056),
		new Location(81623, 148556, -3464),
		new Location(82819, 54607, -1520),
		new Location(147570, 28877, -2264),
		new Location(112486, 220123, -3592),
		new Location(149888, -56574, -2979),
		new Location(44528, -48370, -800),
		new Location(116642, 77510, -2688)
	};
	
	public static void main(String[] args)
    {
        // Quest class
        new OracleTeleport();
    }
	
	public OracleTeleport()
	{
		super(-1, OracleTeleport.class.getSimpleName(), "teleports");
		
		for (int id : TOWN_DAWN)
		{
			addStartNpc(id);
			addTalkId(id);
		}
		for (int id : TOWN_DUSK)
		{
			addStartNpc(id);
			addTalkId(id);
		}
		for (int id : TEMPLE_PRIEST)
		{
			addTalkId(id);
		}
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		int npcId = npc.getNpcId();
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		
		int index = Util.indexOf(TOWN_DAWN, npcId);
		if (index != -1)
		{
			st.setState(State.STARTED);
			// Set teleport id
			st.set("id", String.valueOf(index));
			player.teleToLocation(-80157, 111344, -4901);
			return null;
		}
		
		index = Util.indexOf(TOWN_DUSK, npcId);
		if (index != -1)
		{
			st.setState(State.STARTED);
			// Set teleport id
			st.set("id", String.valueOf(TOWN_DAWN.length + index));
			player.teleToLocation(-81261, 86531, -5157);
			return null;
		}
		
		if (Util.contains(TEMPLE_PRIEST, npcId) && st.getState() == State.STARTED)
		{
			Location loc = RETURN_LOCS[st.getInt("id")];
			player.teleToLocation(loc.getX(), loc.getY(), loc.getZ());
			st.exitQuest(true);
		}
		return null;
	}
}