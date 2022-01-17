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
package teleports.NoblesseTeleport;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;

public class NoblesseTeleport extends Quest
{
	private static final int[] NPC_LIST = new int[]
	{
		7006,
		7059,
		7080,
		7134,
		7146,
		7177,
		7233,
		7256,
		7320,
		7540,
		7576,
		7836,
		7848,
		7878,
		7899,
		8275,
		8320
	};
	
	public static void main(String[] args)
	{
		// Quest class
		new NoblesseTeleport();
	}
	
	public NoblesseTeleport()
	{
		super(-1, NoblesseTeleport.class.getSimpleName(), "teleports");
		
		for (int id : NPC_LIST)
		{
			addStartNpc(id);
			addTalkId(id);
		}
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = "nobleteleporter-no.htm";
		if (player.isNoble())
		{
			htmlText = "noble.htm";
		}
		return htmlText;
	}
}