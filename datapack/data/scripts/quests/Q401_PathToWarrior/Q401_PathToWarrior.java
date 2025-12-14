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
package quests.Q401_PathToWarrior;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Quest 401 Path to Warrior
 * Converted to Java
 * @author Karakan
 */
public class Q401_PathToWarrior extends Quest {
    
    private static final int EINS_LETTER_ID = 1138;
    private static final int WARRIOR_GUILD_MARK_ID = 1139;
    private static final int RUSTED_BRONZE_SWORD1_ID = 1140;
    private static final int RUSTED_BRONZE_SWORD2_ID = 1141;
    private static final int RUSTED_BRONZE_SWORD3_ID = 1142;
    private static final int SIMPLONS_LETTER_ID = 1143;
    private static final int POISON_SPIDER_LEG2_ID = 1144;
    private static final int MEDALLION_OF_WARRIOR_ID = 1145;
    
	public static void main(String[] args)
    {
        new Q401_PathToWarrior();
    }
	
	public Q401_PathToWarrior()
	{
        super(401, Q401_PathToWarrior.class.getSimpleName(), "Path to Warrior");
        
        setItemsIds(EINS_LETTER_ID, WARRIOR_GUILD_MARK_ID, RUSTED_BRONZE_SWORD1_ID, RUSTED_BRONZE_SWORD2_ID, SIMPLONS_LETTER_ID, POISON_SPIDER_LEG2_ID, RUSTED_BRONZE_SWORD3_ID);

		addStartNpc(7010);
		addTalkId(7010, 7253);

		addKillId(35, 38, 42, 43);
    }

    @Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player) {
    	QuestState st = player.getQuestState(getName());
        String htmltext = event;
		if (st == null)
			return htmltext;
		
        switch (event) {
            case "401_1":
                if (st.getPlayer().getClassId().getId() == 0x00) {
                    if (st.getPlayer().getLevel() >= 19) {
                        if (st.getQuestItemsCount(MEDALLION_OF_WARRIOR_ID) > 0) {
                            htmltext = "7010-04.htm";
                        } else {
                            htmltext = "7010-05.htm";
                        }
                    } else {
                        htmltext = "7010-02.htm";
                    }
                } else if (st.getPlayer().getClassId().getId() == 0x01) {
                    htmltext = "7010-02a.htm";
                } else {
                    htmltext = "7010-03.htm";
                }
                break;
            case "401_2":
                htmltext = "7010-10.htm";
                break;
            case "401_3":
                htmltext = "7010-11.htm";
                st.takeItems(SIMPLONS_LETTER_ID, 1);
                st.takeItems(RUSTED_BRONZE_SWORD2_ID, 1);
                st.giveItems(RUSTED_BRONZE_SWORD3_ID, 1);
                st.set("cond", "5");
                break;
            case "1":
                st.set("id", "0");
                if (st.getQuestItemsCount(EINS_LETTER_ID) == 0) {
                    st.set("cond", "1");
                    st.setState(State.STARTED);
                    st.playSound(QuestState.SOUND_ACCEPT);
                    st.giveItems(EINS_LETTER_ID, 1);
                    htmltext = "7010-06.htm";
                }
                break;
            case "7253_1":
                htmltext = "7253-02.htm";
                st.takeItems(EINS_LETTER_ID, 1);
                st.giveItems(WARRIOR_GUILD_MARK_ID, 1);
                st.set("cond", "2");
                break;
        }
        return htmltext;
    }

    @Override
    public String onTalk(L2NpcInstance npc, L2PcInstance player) {
    	QuestState st = player.getQuestState(getName());
        int npcId = npc.getNpcId();
        String htmltext = getNoQuestMsg();
        int id = st.getState();
        
        if (npcId != 7010 && id != State.STARTED) {
            return htmltext;
        }
        
        if (npcId == 7010) 
        {
            if (id == State.COMPLETED) {
                htmltext = getAlreadyCompletedMsg(); 
            } else if (st.getInt("cond") == 0) {
                htmltext = "7010-01.htm";
            } else if (st.getInt("cond") > 0 && st.getQuestItemsCount(EINS_LETTER_ID) > 0) {
                htmltext = "7010-07.htm";
            } else if (st.getInt("cond") > 0 && st.getQuestItemsCount(WARRIOR_GUILD_MARK_ID) == 1) {
                htmltext = "7010-08.htm";
            } else if (st.getInt("cond") > 0 && st.getQuestItemsCount(SIMPLONS_LETTER_ID) > 0) {
                htmltext = "7010-09.htm";
            } else if (st.getInt("cond") > 0 && st.getQuestItemsCount(RUSTED_BRONZE_SWORD3_ID) > 0) {
                if (st.getQuestItemsCount(POISON_SPIDER_LEG2_ID) < 20) {
                    htmltext = "7010-12.htm";
                } else {
                    st.takeItems(POISON_SPIDER_LEG2_ID, st.getQuestItemsCount(POISON_SPIDER_LEG2_ID));
                    st.takeItems(RUSTED_BRONZE_SWORD3_ID, 1);
                    st.giveItems(MEDALLION_OF_WARRIOR_ID, 1);
                    htmltext = "7010-13.htm";
                    st.set("cond", "0");
                    st.setState(State.COMPLETED);
                    st.playSound(QuestState.SOUND_FINISH);
                }
            }
        }
        else if (npcId == 7253) 
        {
            if (st.getInt("cond") > 0 && st.getQuestItemsCount(EINS_LETTER_ID) > 0) {
                htmltext = "7253-01.htm";
            } else if (st.getInt("cond") > 0 && st.getQuestItemsCount(WARRIOR_GUILD_MARK_ID) > 0) {
                if (st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) < 1) {
                    htmltext = "7253-03.htm";
                } else if (st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) < 10) {
                    htmltext = "7253-04.htm";
                } else {
                    st.takeItems(WARRIOR_GUILD_MARK_ID, 1);
                    st.takeItems(RUSTED_BRONZE_SWORD1_ID, st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID));
                    st.giveItems(RUSTED_BRONZE_SWORD2_ID, 1);
                    st.giveItems(SIMPLONS_LETTER_ID, 1);
                    st.set("cond", "4");
                    htmltext = "7253-05.htm";
                }
            } else if (st.getInt("cond") > 0 && st.getQuestItemsCount(SIMPLONS_LETTER_ID) > 0) {
                htmltext = "7253-06.htm";
            }
        }
        return htmltext;
    }

    @Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)  
    {
    	final QuestState st = checkPlayerState(killer, npc, State.STARTED);
        if (st != null && st.getState() == State.STARTED) 
        {
        	switch (npc.getNpcId())
    		{
    			case 35: //Tracker Skeleton
    			case 42: //Tracker Skeleton Leader
                    if (st.getInt("cond") == 2 && st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) < 10) {
                        if (st.getRandom(10) < 7) {
                            st.giveItems(RUSTED_BRONZE_SWORD1_ID, 1);
                            if (st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) == 10) {
                            	st.playSound(QuestState.SOUND_MIDDLE);
                                st.set("cond", "3");
                            } else {
                            	st.playSound(QuestState.SOUND_ITEMGET);
                            }
                        }
                    }
    				break;
    		
    			case 43: //Arachnid Spider
    			case 38: //Poison Spider
                    if (st.getInt("cond") > 0 && st.getQuestItemsCount(POISON_SPIDER_LEG2_ID) < 20 
                        && st.getQuestItemsCount(RUSTED_BRONZE_SWORD3_ID) == 1 
                        && st.getItemEquipped(7) == RUSTED_BRONZE_SWORD3_ID) {
                        st.giveItems(POISON_SPIDER_LEG2_ID, 1);
                        if (st.getQuestItemsCount(POISON_SPIDER_LEG2_ID) == 20) {
                        	 st.playSound(QuestState.SOUND_MIDDLE);
                            st.set("cond", "6");
                        } else {
                            st.playSound(QuestState.SOUND_ITEMGET);
                        }
                    }
                    break;
            }
        }
		return null;
    }
}