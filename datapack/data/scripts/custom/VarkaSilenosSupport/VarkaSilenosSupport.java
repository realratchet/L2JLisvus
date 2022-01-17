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
package custom.VarkaSilenosSupport;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.WareHouseWithdrawalList;
import net.sf.l2j.util.StringUtil;

public class VarkaSilenosSupport extends Quest
{
	private static final int ASHAS = 8377; // Hierarch
	private static final int NARAN = 8378; // Messenger
	private static final int UDAN = 8379; // Buffer
	private static final int DIYABU = 8380; // Grocer
	private static final int HAGOS = 8381; // Warehouse Keeper
	private static final int SHIKON = 8382; // Trader
	private static final int TERANU = 8383; // Teleporter
	
	private static final int[] NPCS = {ASHAS, NARAN, UDAN, DIYABU, HAGOS, SHIKON, TERANU};
	
	private static final int SEED = 7187;
	
	private static final int[][] BUFF =
	{
		{
			4359,
			1,
			2
		}, // Focus: Requires 2 Buffalo Horns
		{
			4360,
			1,
			2
		}, // Death Whisper: Requires 2 Buffalo Horns
		{
			4345,
			1,
			3
		}, // Might: Requires 3 Buffalo Horns
		{
			4355,
			1,
			3
		}, // Acumen: Requires 3 Buffalo Horns
		{
			4352,
			1,
			3
		}, // Berserker: Requires 3 Buffalo Horns
		{
			4354,
			1,
			3
		}, // Vampiric Rage: Requires 3 Buffalo Horns
		{
			4356,
			1,
			6
		}, // Empower: Requires 6 Buffalo Horns
		{
			4357,
			1,
			6
		}
		// Haste: Requires 6 Buffalo Horns
	};

	public static void main(String[] args)
    {
        // Quest class
        new VarkaSilenosSupport();
    }
	
	public VarkaSilenosSupport()
	{
		super(-1, VarkaSilenosSupport.class.getSimpleName(), "custom");
		
		for (int id : NPCS)
		{
			addFirstTalkId(id);
		}
		
		addTalkId(UDAN);
		addTalkId(HAGOS);
		addTalkId(TERANU);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		if (StringUtil.isDigit(event))
		{
			final int[] buffInfo = BUFF[Integer.parseInt(event)];
			if (st.getQuestItemsCount(SEED) >= buffInfo[1])
			{
				htmltext = "8379-4.htm";
				st.takeItems(SEED, buffInfo[1]);
				npc.setTarget(player);
				npc.doCast(SkillTable.getInstance().getInfo(buffInfo[0], 1));
				npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp());
			}
		}
		else if (event.equals("Withdraw"))
		{
			if (player.getWarehouse().getSize() == 0)
				htmltext = "8381-0.htm";
			else
			{
				player.sendPacket(new ActionFailed());
				player.setActiveWarehouse(player.getWarehouse());
				player.sendPacket(new WareHouseWithdrawalList(player, 1));
			}
		}
		else if (event.equals("Teleport"))
		{
			switch (player.getAllianceWithVarkaKetra())
			{
				case -4:
					htmltext = "8383-4.htm";
					break;
				case -5:
					htmltext = "8383-5.htm";
					break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		
		final int allianceLevel = player.getAllianceWithVarkaKetra();
		
		switch (npc.getNpcId())
		{
			case ASHAS:
				if (allianceLevel < 0)
					htmltext = "8377-friend.htm";
				else
					htmltext = "8377-no.htm";
				break;
			
			case NARAN:
				if (allianceLevel < 0)
					htmltext = "8378-friend.htm";
				else
					htmltext = "8378-no.htm";
				break;
			
			case UDAN:
				st.setState(State.STARTED);
				if (allianceLevel > -1)
					htmltext = "8379-3.htm";
				else if (allianceLevel > -3 && allianceLevel < 0)
					htmltext = "8379-1.htm";
				else if (allianceLevel < -2)
				{
					if (st.getQuestItemsCount(SEED) > 0)
						htmltext = "8379-4.htm";
					else
						htmltext = "8379-2.htm";
				}
				break;
			
			case DIYABU:
				if (player.getKarma() >= 1)
					htmltext = "8380-pk.htm";
				else if (allianceLevel >= 0)
					htmltext = "8380-no.htm";
				else if (allianceLevel == -1 || allianceLevel == -2)
					htmltext = "8380-1.htm";
				else
					htmltext = "8380-2.htm";
				break;
			
			case HAGOS:
				switch (allianceLevel)
				{
					case -1:
						htmltext = "8381-1.htm";
						break;
					case -2:
					case -3:
						htmltext = "8381-2.htm";
						break;
					default:
						if (allianceLevel >= 0)
							htmltext = "8381-no.htm";
						else if (player.getWarehouse().getSize() == 0)
							htmltext = "8381-3.htm";
						else
							htmltext = "8381-4.htm";
						break;
				}
				break;
			
			case SHIKON:
				switch (allianceLevel)
				{
					case -2:
						htmltext = "8382-1.htm";
						break;
					case -3:
					case -4:
						htmltext = "8382-2.htm";
						break;
					case -5:
						htmltext = "8382-3.htm";
						break;
					default:
						htmltext = "8382-no.htm";
						break;
				}
				break;
			
			case TERANU:
				if (allianceLevel >= 0)
					htmltext = "8383-no.htm";
				else if (allianceLevel < 0 && allianceLevel > -4)
					htmltext = "8383-1.htm";
				else if (allianceLevel == -4)
					htmltext = "8383-2.htm";
				else
					htmltext = "8383-3.htm";
				break;
		}
		
		return htmltext;
	}
}
