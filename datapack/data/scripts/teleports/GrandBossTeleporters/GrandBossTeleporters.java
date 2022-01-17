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
package teleports.GrandBossTeleporters;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.util.Rnd;

public class GrandBossTeleporters extends Quest
{
	private static final int[] NPC_LIST = new int[]
	{
		8384, // Gatekeeper of Fire Dragon : Opening some doors
		8385, // Heart of Volcano : Teleport into Lair of Valakas
		8540, // Watcher of Valakas Klein : Teleport into Hall of Flames
		8686, // Gatekeeper of Fire Dragon : Opens doors to Heart of Volcano
		8687, // Gatekeeper of Fire Dragon : Opens doors to Heart of Volcano
		8759, // Teleportation Cubic : Teleport out of Lair of Valakas
		12250, // Heart of Warding : Teleport into Lair of Antharas
		12324, // Teleport Cube : Teleport out of Lair of Antharas
	};
	
	private static final int FLOATING_STONE = 7267;
	
	private final Quest _antharasAI;
	private final Quest _valakasAI;
	
	public static void main(String[] args)
	{
		// Quest class
		new GrandBossTeleporters();
	}
	
	public GrandBossTeleporters()
	{
		super(-1, GrandBossTeleporters.class.getSimpleName(), "teleports");
		
		addStartNpc(8540);
		addStartNpc(8759);
		addStartNpc(12250);
		addStartNpc(12324);
		
		for (int id : NPC_LIST)
		{
			addTalkId(id);
		}
		
		_antharasAI = QuestManager.getInstance().getQuest("antharas");
		_valakasAI = QuestManager.getInstance().getQuest("valakas");
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmlText;
		}
		
		int npcId = npc.getNpcId();
		if (npcId == 8540)
		{
			htmlText = "1.htm";
			if (st.getQuestItemsCount(FLOATING_STONE) > 0)
			{
				st.takeItems(FLOATING_STONE, 1);
				player.teleToLocation(183813, -115157, -3303);
				return null;
			}
			// Exit quest only if player hasn't got the stone
			st.exitQuest(true);
		}
		else if (npcId == 12324) // Antharas teleport cube
		{
			int x = 79800 + Rnd.get(600);
			int y = 151200 + Rnd.get(1100);
			player.teleToLocation(x, y, -3534);
			st.exitQuest(true);
			return null;
		}
		else if (npcId == 12250) // Heart of Warding
		{
			htmlText = "12250-01.htm";
			if (_antharasAI != null)
			{
				int status = GrandBossManager.getInstance().getBossStatus(12211);
				if (status == 0 || status == 1) // If entrance to see Antharas is unlocked (he is Dormant or Waiting)
				{
					if (st.getQuestItemsCount(3865) > 0)
					{
						st.takeItems(3865, 1);
						L2BossZone zone = GrandBossManager.getInstance().getZone(179700, 113800, -7709);
						if (zone != null)
						{
							zone.allowPlayerEntry(player, 30000);
						}
						int x = 179700 + Rnd.get(700);
						int y = 113800 + Rnd.get(2100);
						player.teleToLocation(x, y, -7709);
						if (status == 0)
						{
							L2GrandBossInstance antharas = GrandBossManager.getInstance().getBoss(12211);
							_antharasAI.startQuestTimer("waiting", Config.ANTHARAS_WAIT_TIME * 60000L, antharas, null);
							GrandBossManager.getInstance().setBossStatus(12211, 1);
						}
						st.exitQuest(true);
						return null;
					}
					htmlText = "12250-03.htm";
				}
				else if (status == 2)
				{
					htmlText = "12250-02.htm";
				}
			}
			// Quest state is no longer needed in this case
			st.exitQuest(true);
		}
		else if (npcId == 8385) // Heart of Volcano
		{
			htmlText = "8385-01.htm";
			if (_valakasAI != null)
			{
				int status = GrandBossManager.getInstance().getBossStatus(12899);
				if (status == 0 || status == 1) // If entrance to see Valakas is unlocked (he is Dormant or Waiting)
				{
					L2BossZone zone = GrandBossManager.getInstance().getZone(212852, -114842, -1632);
					if (zone != null)
					{
						zone.allowPlayerEntry(player, 30000);
					}
					int x = 204328 + Rnd.get(600);
					int y = -111874 + Rnd.get(600);
					player.teleToLocation(x, y, 70);
					if (status == 0)
					{
						L2GrandBossInstance valakas = GrandBossManager.getInstance().getBoss(12899);
						_valakasAI.startQuestTimer("1001", Config.VALAKAS_WAIT_TIME * 60000L, valakas, null);
						GrandBossManager.getInstance().setBossStatus(12899, 1);
					}
					st.exitQuest(true);
					return null;
				}
				else if (status == 2)
				{
					htmlText = "8385-02.htm";
				}
			}
		}
		else if (npcId == 8384) // Gatekeeper of Fire Dragon
		{
			DoorTable.getInstance().getDoor(24210004).openMe();
			return null;
		}
		else if (npcId == 8686) // Gatekeeper of Fire Dragon
		{
			DoorTable.getInstance().getDoor(24210006).openMe();
			return null;
		}
		else if (npcId == 8687) // Gatekeeper of Fire Dragon
		{
			DoorTable.getInstance().getDoor(24210005).openMe();
			return null;
		}
		else if (npcId == 8759) // Valakas teleport cube
		{
			int x = 150037 + Rnd.get(500);
			int y = -57720 + Rnd.get(500);
			player.teleToLocation(x, y, -2976);
			st.exitQuest(true);
			return null;
		}
		return htmlText;
	}
}