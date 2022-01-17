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
package custom.KetraOrcSupport;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.WareHouseWithdrawalList;
import net.sf.l2j.util.StringUtil;

public class KetraOrcSupport extends Quest
{
	private static final int KADUN = 8370; // Hierarch
	private static final int WAHKAN = 8371; // Messenger
	private static final int ASEFA = 8372; // Soul Guide
	private static final int ATAN = 8373; // Grocer
	private static final int JAFF = 8374; // Warehouse Keeper
	private static final int JUMARA = 8375; // Trader
	private static final int KURFA = 8376; // Teleporter
	
	private static final int[] NPCS = {KADUN, WAHKAN, ASEFA, ATAN, JAFF, JUMARA, KURFA};

	private static final int HORN = 7186;
	
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
        new KetraOrcSupport();
    }
	
	public KetraOrcSupport()
	{
		super(-1, KetraOrcSupport.class.getSimpleName(), "custom");
		
		for (int id : NPCS)
		{
			addFirstTalkId(id);
		}
		
		addTalkId(ASEFA);
		addTalkId(JAFF);
		addTalkId(KURFA);
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
			if (st.getQuestItemsCount(HORN) >= buffInfo[2])
			{
				htmltext = "8372-4.htm";
				st.takeItems(HORN, buffInfo[2]);
				npc.setTarget(player);
				npc.doCast(SkillTable.getInstance().getInfo(buffInfo[0], buffInfo[1]));
				npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp());
			}
		}
		else if (event.equals("Withdraw"))
		{
			if (player.getWarehouse().getSize() == 0)
				htmltext = "8374-0.htm";
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
				case 4:
					htmltext = "8376-4.htm";
					break;
				case 5:
					htmltext = "8376-5.htm";
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
			case KADUN:
				if (allianceLevel > 0)
					htmltext = "8370-friend.htm";
				else
					htmltext = "8370-no.htm";
				break;
			
			case WAHKAN:
				if (allianceLevel > 0)
					htmltext = "8371-friend.htm";
				else
					htmltext = "8371-no.htm";
				break;
			
			case ASEFA:
				st.setState(State.STARTED);
				if (allianceLevel < 1)
					htmltext = "8372-3.htm";
				else if (allianceLevel < 3 && allianceLevel > 0)
					htmltext = "8372-1.htm";
				else if (allianceLevel > 2)
				{
					if (st.getQuestItemsCount(HORN) > 0)
						htmltext = "8372-4.htm";
					else
						htmltext = "8372-2.htm";
				}
				break;
			
			case ATAN:
				if (player.getKarma() >= 1)
					htmltext = "8373-pk.htm";
				else if (allianceLevel <= 0)
					htmltext = "8373-no.htm";
				else if (allianceLevel == 1 || allianceLevel == 2)
					htmltext = "8373-1.htm";
				else
					htmltext = "8373-2.htm";
				break;
			
			case JAFF:
				switch (allianceLevel)
				{
					case 1:
						htmltext = "8374-1.htm";
						break;
					case 2:
					case 3:
						htmltext = "8374-2.htm";
						break;
					default:
						if (allianceLevel <= 0)
							htmltext = "8374-no.htm";
						else if (player.getWarehouse().getSize() == 0)
							htmltext = "8374-3.htm";
						else
							htmltext = "8374-4.htm";
						break;
				}
				break;
			
			case JUMARA:
				switch (allianceLevel)
				{
					case 2:
						htmltext = "8375-1.htm";
						break;
					case 3:
					case 4:
						htmltext = "8375-2.htm";
						break;
					case 5:
						htmltext = "8375-3.htm";
						break;
					default:
						htmltext = "8375-no.htm";
						break;
				}
				break;
			
			case KURFA:
				if (allianceLevel <= 0)
					htmltext = "8376-no.htm";
				else if (allianceLevel > 0 && allianceLevel < 4)
					htmltext = "8376-1.htm";
				else if (allianceLevel == 4)
					htmltext = "8376-2.htm";
				else
					htmltext = "8376-3.htm";
				break;
		}
		
		return htmltext;
	}
}
