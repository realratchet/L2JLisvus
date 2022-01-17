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
package quests.Q104_SpiritOfMirrors;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

public class Q104_SpiritOfMirrors extends Quest
{
	// Items
	private static final int GALLINS_OAK_WAND = 748;
	private static final int WAND_SPIRITBOUND_1 = 1135;
	private static final int WAND_SPIRITBOUND_2 = 1136;
	private static final int WAND_SPIRITBOUND_3 = 1137;
	
	// Rewards
	private static final int SPIRITSHOT_NO_GRADE = 2509;
	private static final int SOULSHOT_NO_GRADE = 1835;
	private static final int WAND_OF_ADEPT = 747;
	private static final int SPIRITSHOT_FOR_BEGINNERS = 5790;
	private static final int SOULSHOT_FOR_BEGINNERS = 5789;
	private static final int LESSER_HEALING_POT = 1060;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	
	// NPCs
	private static final int GALLINT = 7017;
	private static final int ARNOLD = 7041;
	private static final int JOHNSTONE = 7043;
	private static final int KENYOS = 7045;
	
	public static void main(String[] args)
    {
		new Q104_SpiritOfMirrors();
    }
	
	public Q104_SpiritOfMirrors()
	{
		super(104, Q104_SpiritOfMirrors.class.getSimpleName(), "Spirit of Mirrors");
		
		setItemsIds(GALLINS_OAK_WAND, WAND_SPIRITBOUND_1, WAND_SPIRITBOUND_2, WAND_SPIRITBOUND_3);
		
		addStartNpc(GALLINT);
		addTalkId(GALLINT, ARNOLD, JOHNSTONE, KENYOS);
		
		addKillId(5003, 5004, 5005);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("7017-03.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(GALLINS_OAK_WAND, 1);
			st.giveItems(GALLINS_OAK_WAND, 1);
			st.giveItems(GALLINS_OAK_WAND, 1);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				if (player.getRace() != Race.HUMAN)
					htmltext = "7017-01.htm";
				else if (player.getLevel() < 10)
					htmltext = "7017-06.htm";
				else
					htmltext = "7017-02.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case GALLINT:
						if (cond == 1 || cond == 2)
							htmltext = "7017-04.htm";
						else if (cond == 3)
						{
							htmltext = "7017-05.htm";
							
							st.takeItems(WAND_SPIRITBOUND_1, -1);
							st.takeItems(WAND_SPIRITBOUND_2, -1);
							st.takeItems(WAND_SPIRITBOUND_3, -1);
							
							st.giveItems(WAND_OF_ADEPT, 1);
							st.rewardItems(LESSER_HEALING_POT, 100);
							
							if (player.isMageClass())
								st.giveItems(SPIRITSHOT_NO_GRADE, 500);
							else
								st.giveItems(SOULSHOT_NO_GRADE, 1000);
							
							if (player.isNewbie())
							{
								st.showQuestionMark(26);
								if (player.isMageClass())
								{
									st.playTutorialVoice("tutorial_voice_027");
									st.giveItems(SPIRITSHOT_FOR_BEGINNERS, 3000);
								}
								else
								{
									st.playTutorialVoice("tutorial_voice_026");
									st.giveItems(SOULSHOT_FOR_BEGINNERS, 7000);
								}
							}
							
							st.giveItems(ECHO_BATTLE, 10);
							st.giveItems(ECHO_LOVE, 10);
							st.giveItems(ECHO_SOLITUDE, 10);
							st.giveItems(ECHO_FEAST, 10);
							st.giveItems(ECHO_CELEBRATION, 10);
							player.broadcastPacket(new SocialAction(player.getObjectId(), 3));
							st.playSound(QuestState.SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case KENYOS:
					case JOHNSTONE:
					case ARNOLD:
						htmltext = npc.getNpcId() + "-01.htm";
						if (cond == 1)
						{
							st.set("cond", "2");
							st.playSound(QuestState.SOUND_MIDDLE);
						}
						break;
				}
				break;
			
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		final QuestState st = checkPlayerState(killer, npc, State.STARTED);
		if (st == null)
			return null;
		
		if (st.getItemEquipped(Inventory.PAPERDOLL_RHAND) == GALLINS_OAK_WAND)
		{
			switch (npc.getNpcId())
			{
				case 5003:
					if (!st.hasQuestItems(WAND_SPIRITBOUND_1))
					{
						st.takeItems(GALLINS_OAK_WAND, 1);
						st.giveItems(WAND_SPIRITBOUND_1, 1);
						
						if (st.hasQuestItems(WAND_SPIRITBOUND_2, WAND_SPIRITBOUND_3))
						{
							st.set("cond", "3");
							st.playSound(QuestState.SOUND_MIDDLE);
						}
						else
							st.playSound(QuestState.SOUND_ITEMGET);
					}
					break;
				
				case 5004:
					if (!st.hasQuestItems(WAND_SPIRITBOUND_2))
					{
						st.takeItems(GALLINS_OAK_WAND, 1);
						st.giveItems(WAND_SPIRITBOUND_2, 1);
						
						if (st.hasQuestItems(WAND_SPIRITBOUND_1, WAND_SPIRITBOUND_3))
						{
							st.set("cond", "3");
							st.playSound(QuestState.SOUND_MIDDLE);
						}
						else
							st.playSound(QuestState.SOUND_ITEMGET);
					}
					break;
				
				case 5005:
					if (!st.hasQuestItems(WAND_SPIRITBOUND_3))
					{
						st.takeItems(GALLINS_OAK_WAND, 1);
						st.giveItems(WAND_SPIRITBOUND_3, 1);
						
						if (st.hasQuestItems(WAND_SPIRITBOUND_1, WAND_SPIRITBOUND_2))
						{
							st.set("cond", "3");
							st.playSound(QuestState.SOUND_MIDDLE);
						}
						else
							st.playSound(QuestState.SOUND_ITEMGET);
					}
					break;
			}
		}
		
		return null;
	}
}
