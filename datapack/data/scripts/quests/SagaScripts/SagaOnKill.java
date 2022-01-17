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
package quests.SagaScripts;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author DnR
 *
 */
public class SagaOnKill extends Quest
{
	private static final int[] ARCHON_HALISHA_NORM = new int[]
	{
		13047,
		13049,
		13050,
		13051,
		13052
	};
	private static final int[] ARCHON_HALISHA_MINIONS = new int[]
	{
		1646,
		1647,
		1648,
		1649,
		1650,
		1651
	};
	private static final int[] GUARDIAN_ANGELS = new int[]
	{
		5214,
		5215,
		5216
	};
	
	public static void main(String[] args)
	{
		// Quest class
		new SagaOnKill();
	}
	
	public SagaOnKill()
	{
		super(-1, SagaOnKill.class.getSimpleName(), "Saga Scripts");
		
		for (int id : ARCHON_HALISHA_NORM)
		{
			addKillId(id);
		}
		
		for (int id : ARCHON_HALISHA_MINIONS)
		{
			addKillId(id);
		}
		
		for (int id : GUARDIAN_ANGELS)
		{
			addKillId(id);
		}
	}
	
	private QuestState findQuest(L2PcInstance player)
	{
		// Invalid class
		if (player.getClassId().level() != 2)
		{
			return null;
		}
		
		QuestState[] questStates = player.getAllQuestStates();
		for (QuestState qs : questStates)
		{
			// Check if conditions are met
			if (!qs.isStarted() || qs.isCompleted() || qs.getQuest().getQuestIntId() >= 1999)
			{
				continue;
			}
			
			if (qs.getQuest() instanceof SagaSuperclass && player.getClassId().getId() == ((SagaSuperclass) qs.getQuest())._prevClassId)
			{
				return qs;
			}
		}
		
		return null;
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
	{
		final int npcId = npc.getNpcId();
		final QuestState st = findQuest(player);
		
		if (Util.contains(ARCHON_HALISHA_MINIONS, npcId))
		{
			L2Party party = player.getParty();
			if (party != null)
			{
				List<QuestState> partyQuestMembers = new ArrayList<>();
				for (L2PcInstance member : party.getPartyMembers())
				{
					QuestState memberSt = null;
					if (member.getObjectId() == player.getObjectId())
					{
						memberSt = st;
					}
					else
					{
						if (member.isInsideRadius(player, 1500, true, false))
						{
							memberSt = findQuest(member);
						}
					}
					
					if (memberSt != null)
					{
						if (memberSt.getInt("cond") == 15)
						{
							partyQuestMembers.add(memberSt);
						}
					}
				}
				
				if (!partyQuestMembers.isEmpty())
				{
					QuestState memberSt = partyQuestMembers.get(Rnd.get(partyQuestMembers.size()));
					if (memberSt.getQuest() instanceof SagaSuperclass)
					{
						((SagaSuperclass)memberSt.getQuest()).giveHallishaMark(memberSt);
					}
				}
			}
			else
			{
				if (st != null && st.getQuest() instanceof SagaSuperclass)
				{
					if (st.getInt("cond") == 15)
					{
						((SagaSuperclass)st.getQuest()).giveHallishaMark(st);
					}
				}
			}
		}
		else if (Util.contains(ARCHON_HALISHA_NORM, npcId))
		{
			if (st != null && st.getQuest() instanceof SagaSuperclass)
			{
				SagaSuperclass saga = (SagaSuperclass) st.getQuest();
				if (st.getInt("cond") == 15 && player.getClassId().getId() == saga._prevClassId)
				{
					// This is just a guess....not really sure what it actually says, if anything
					saga.autoChat(npc, saga._texts[4].replace("PLAYERNAME", st.getPlayer().getName()));
					st.giveItems(saga._items[6], 1);
					st.takeItems(saga._items[1], -1);
					st.set("cond", "16");
					st.playSound(QuestState.SOUND_MIDDLE);
				}
			}
		}
		else if (Util.contains(GUARDIAN_ANGELS, npcId))
		{
			if (st != null && st.getQuest() instanceof SagaSuperclass)
			{
				SagaSuperclass saga = (SagaSuperclass) st.getQuest();
				if (st.getInt("cond") == 6 && player.getClassId().getId() == saga._prevClassId)
				{
					if (st.getInt("kills") < 9)
					{
						st.set("kills", String.valueOf(st.getInt("kills") + 1));
					}
					else
					{
						st.playSound(QuestState.SOUND_MIDDLE);
						st.giveItems(saga._items[3], 1);
						st.set("cond", "7");
					}
				}
			}
		}
		return super.onKill(npc, player, isPet);
	}
}
