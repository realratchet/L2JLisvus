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
package teleports.HuntingGroundsTeleport;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;

/**
 * @author Deniska Spectr
 */
public class HuntingGroundsTeleport extends Quest
{
	private static final Map<Integer, String> NPC_LIST = new HashMap<>();
	
	static
	{
		NPC_LIST.put(8078, "hg_gludin.htm");
		NPC_LIST.put(8085, "hg_gludin.htm");
		NPC_LIST.put(8079, "hg_gludio.htm");
		NPC_LIST.put(8086, "hg_gludio.htm");
		NPC_LIST.put(8080, "hg_dion.htm");
		NPC_LIST.put(8087, "hg_dion.htm");
		NPC_LIST.put(8081, "hg_giran.htm");
		NPC_LIST.put(8088, "hg_giran.htm");
		NPC_LIST.put(8082, "hg_heine.htm");
		NPC_LIST.put(8089, "hg_heine.htm");
		NPC_LIST.put(8083, "hg_oren.htm");
		NPC_LIST.put(8090, "hg_oren.htm");
		NPC_LIST.put(8084, "hg_aden.htm");
		NPC_LIST.put(8091, "hg_aden.htm");
		NPC_LIST.put(8168, "hg_hv.htm");
		NPC_LIST.put(8169, "hg_hv.htm");
		NPC_LIST.put(8692, "hg_goddard.htm");
		NPC_LIST.put(8693, "hg_goddard.htm");
		NPC_LIST.put(8694, "hg_rune.htm");
		NPC_LIST.put(8695, "hg_rune.htm");
	}
	
	public static void main(String[] args)
	{
		// Quest class
		new HuntingGroundsTeleport();
	}
	
	public HuntingGroundsTeleport()
	{
		super(-1, HuntingGroundsTeleport.class.getSimpleName(), "teleports");
		
		for (int id : NPC_LIST.keySet())
		{
			addStartNpc(id);
			addTalkId(id);
		}
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = "hg_wrong.htm";
		int npcId = npc.getNpcId();
		if (NPC_LIST.containsKey(npcId))
		{
			htmlText = NPC_LIST.get(npcId);
		}
		return htmlText;
	}
}