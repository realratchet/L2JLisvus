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
package quests.Q213_TrialOfSeeker;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Made by Mr. Have fun!
 * Version 0.3 by DrLecter
 */
public class Q213_TrialOfSeeker extends Quest
{
    // Item IDs
    private static final int DUFNERS_LETTER_ID = 2647;
    private static final int TERYS_ORDER1_ID = 2648;
    private static final int TERYS_ORDER2_ID = 2649;
    private static final int TERYS_LETTER_ID = 2650;
    private static final int VIKTORS_LETTER_ID = 2651;
    private static final int HAWKEYES_LETTER_ID = 2652;
    private static final int MYSTERIOUS_RUNESTONE_ID = 2653;
    private static final int OL_MAHUM_RUNESTONE_ID = 2654;
    private static final int TUREK_RUNESTONE_ID = 2655;
    private static final int ANT_RUNESTONE_ID = 2656;
    private static final int TURAK_BUGBEAR_RUNESTONE_ID = 2657;
    private static final int TERYS_BOX_ID = 2658;
    private static final int VIKTORS_REQUEST_ID = 2659;
    private static final int MEDUSAS_SCALES_ID = 2660;
    private static final int SILENS_RUNESTONE_ID = 2661;
    private static final int ANALYSIS_REQUEST_ID = 2662;
    private static final int MARINAS_LETTER_ID = 2663;
    private static final int EXPERIMENT_TOOLS_ID = 2664;
    private static final int ANALYSIS_RESULT_ID = 2665;
    private static final int TERYS_ORDER3_ID = 2666;
    private static final int LIST_OF_HOST_ID = 2667;
    private static final int ABYSS_RUNESTONE1_ID = 2668;
    private static final int ABYSS_RUNESTONE2_ID = 2669;
    private static final int ABYSS_RUNESTONE3_ID = 2670;
    private static final int ABYSS_RUNESTONE4_ID = 2671;
    private static final int TERYS_REPORT_ID = 2672;
    private static final int MARK_OF_SEEKER_ID = 2673;
    
    // NPC IDs
    private static final int DUFNER = 7106;
    private static final int TERRY = 7064;
    private static final int VIKTOR = 7684;
    private static final int MARINA = 7715;
    private static final int BRUNON = 7526;
    
    // Monster IDs
    private static final int NEER_GHOUL_BERSERKER = 198;
    private static final int OL_MAHUM_CAPTAIN = 211;
    private static final int TUREK_ORC_WARLORD = 495;
    private static final int ANT_WARRIOR_CAPTAIN = 80;
    private static final int TURAK_BUGBEAR_WARRIOR = 249;
    private static final int BREKA_ORC_OVERLORD = 234;
    private static final int LETO_LIZARDMAN_WARRIOR = 270;
    private static final int MEDUSA = 158;
    private static final int TAMLIN_ORC = 580;
    private static final int TAMLIN_ORC_ARCHER = 88;
    
    // Drop list structure: npcId -> [required_item, drop_item, chance, maxqty]
    private static final Map<Integer, int[]> DROPLIST = new HashMap<>();
    
    static
    {
        DROPLIST.put(198, new int[]
        {
            TERYS_ORDER1_ID,
            MYSTERIOUS_RUNESTONE_ID,
            10,
            1
        });
        DROPLIST.put(211, new int[]
        {
            TERYS_ORDER2_ID,
            OL_MAHUM_RUNESTONE_ID,
            25,
            1
        });
        DROPLIST.put(495, new int[]
        {
            TERYS_ORDER2_ID,
            TUREK_RUNESTONE_ID,
            25,
            1
        });
        DROPLIST.put(80, new int[]
        {
            TERYS_ORDER2_ID,
            ANT_RUNESTONE_ID,
            25,
            1
        });
        DROPLIST.put(249, new int[]
        {
            TERYS_ORDER2_ID,
            TURAK_BUGBEAR_RUNESTONE_ID,
            25,
            1
        });
        DROPLIST.put(234, new int[]
        {
            LIST_OF_HOST_ID,
            ABYSS_RUNESTONE1_ID,
            25,
            1
        });
        DROPLIST.put(270, new int[]
        {
            LIST_OF_HOST_ID,
            ABYSS_RUNESTONE2_ID,
            25,
            1
        });
        DROPLIST.put(88, new int[]
        {
            LIST_OF_HOST_ID,
            ABYSS_RUNESTONE3_ID,
            25,
            1
        });
        DROPLIST.put(580, new int[]
        {
            LIST_OF_HOST_ID,
            ABYSS_RUNESTONE4_ID,
            25,
            1
        });
        DROPLIST.put(158, new int[]
        {
            VIKTORS_REQUEST_ID,
            MEDUSAS_SCALES_ID,
            30,
            10
        });
    }

    public static void main(String[] args)
    {
        new Q213_TrialOfSeeker();
    }
    
    public Q213_TrialOfSeeker()
    {
        super(213, Q213_TrialOfSeeker.class.getSimpleName(), "Trial Of Seeker");
        
        // Register quest items (2647-2672)
        setItemsIds(DUFNERS_LETTER_ID, TERYS_ORDER1_ID, TERYS_ORDER2_ID, TERYS_LETTER_ID, VIKTORS_LETTER_ID, HAWKEYES_LETTER_ID, MYSTERIOUS_RUNESTONE_ID, OL_MAHUM_RUNESTONE_ID, TUREK_RUNESTONE_ID, ANT_RUNESTONE_ID, TURAK_BUGBEAR_RUNESTONE_ID, TERYS_BOX_ID, VIKTORS_REQUEST_ID, MEDUSAS_SCALES_ID, SILENS_RUNESTONE_ID, ANALYSIS_REQUEST_ID, MARINAS_LETTER_ID, EXPERIMENT_TOOLS_ID, ANALYSIS_RESULT_ID, TERYS_ORDER3_ID, LIST_OF_HOST_ID, ABYSS_RUNESTONE1_ID, ABYSS_RUNESTONE2_ID, ABYSS_RUNESTONE3_ID, ABYSS_RUNESTONE4_ID, TERYS_REPORT_ID);
        
        addStartNpc(DUFNER);
        
        addTalkId(TERRY, DUFNER, BRUNON, VIKTOR, MARINA);
        
        addKillId(MEDUSA, NEER_GHOUL_BERSERKER, OL_MAHUM_CAPTAIN, BREKA_ORC_OVERLORD, TURAK_BUGBEAR_WARRIOR, LETO_LIZARDMAN_WARRIOR, TUREK_ORC_WARLORD, TAMLIN_ORC, ANT_WARRIOR_CAPTAIN, TAMLIN_ORC_ARCHER);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equals("7106-05.htm"))
        {
            st.set("cond", "1");
            st.setState(State.STARTED);
            st.playSound("ItemSound.quest_accept");
            st.giveItems(DUFNERS_LETTER_ID, 1);
        }
        else if (event.equals("7064-03.htm"))
        {
            st.giveItems(TERYS_ORDER1_ID, 1);
            st.takeItems(DUFNERS_LETTER_ID, 1);
        }
        else if (event.equals("7064-06.htm"))
        {
            st.takeItems(MYSTERIOUS_RUNESTONE_ID, 1);
            st.giveItems(TERYS_ORDER2_ID, 1);
            st.takeItems(TERYS_ORDER1_ID, 1);
        }
        else if (event.equals("7064-10.htm"))
        {
            st.takeItems(OL_MAHUM_RUNESTONE_ID, 1);
            st.takeItems(TUREK_RUNESTONE_ID, 1);
            st.takeItems(ANT_RUNESTONE_ID, 1);
            st.takeItems(TURAK_BUGBEAR_RUNESTONE_ID, 1);
            st.takeItems(TERYS_ORDER2_ID, 1);
            st.giveItems(TERYS_LETTER_ID, 1);
            st.giveItems(TERYS_BOX_ID, 1);
        }
        else if (event.equals("7064-18.htm"))
        {
            if (st.getPlayer().getLevel() < 36)
            {
                htmltext = "7064-17.htm";
                st.giveItems(TERYS_ORDER3_ID, 1);
                st.takeItems(ANALYSIS_RESULT_ID, 1);
            }
            else
            {
                st.giveItems(LIST_OF_HOST_ID, 1);
                st.takeItems(ANALYSIS_RESULT_ID, 1);
            }
        }
        else if (event.equals("7684-05.htm"))
        {
            st.giveItems(VIKTORS_LETTER_ID, 1);
            st.takeItems(TERYS_LETTER_ID, 1);
        }
        else if (event.equals("7684-11.htm"))
        {
            st.giveItems(VIKTORS_REQUEST_ID, 1);
            st.takeItems(TERYS_LETTER_ID, 1);
            st.takeItems(TERYS_BOX_ID, 1);
            st.takeItems(HAWKEYES_LETTER_ID, 1);
            st.takeItems(VIKTORS_LETTER_ID, st.getQuestItemsCount(VIKTORS_LETTER_ID));
        }
        else if (event.equals("7684-15.htm"))
        {
            st.takeItems(VIKTORS_REQUEST_ID, 1);
            st.takeItems(MEDUSAS_SCALES_ID, st.getQuestItemsCount(MEDUSAS_SCALES_ID));
            st.giveItems(SILENS_RUNESTONE_ID, 1);
            st.giveItems(ANALYSIS_REQUEST_ID, 1);
        }
        else if (event.equals("7715-02.htm"))
        {
            st.takeItems(SILENS_RUNESTONE_ID, 1);
            st.takeItems(ANALYSIS_REQUEST_ID, 1);
            st.giveItems(MARINAS_LETTER_ID, 1);
        }
        else if (event.equals("7715-05.htm"))
        {
            st.takeItems(EXPERIMENT_TOOLS_ID, 1);
            st.giveItems(ANALYSIS_RESULT_ID, 1);
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
        
        int npcId = npc.getNpcId();
        int id = st.getState();
        
        if (id == State.CREATED)
        {
            st.set("cond", "0");
            st.set("onlyone", "0");
            st.set("id", "0");
        }
        
        if (npcId == 7106 && st.getInt("cond") == 0 && st.getInt("onlyone") == 0)
        {
            if (player.getClassId().getId() == 0x07 || player.getClassId().getId() == 0x16 || player.getClassId().getId() == 0x23)
            {
                if (st.getPlayer().getLevel() >= 35)
                {
                    htmltext = "7106-03.htm";
                }
                else
                {
                    htmltext = "7106-02.htm";
                    st.exitQuest(true);
                }
            }
            else
            {
                htmltext = "7106-00.htm";
                st.exitQuest(true);
            }
        }
        else if (npcId == 7106 && st.getInt("cond") == 0 && st.getInt("onlyone") == 1)
        {
            htmltext = getAlreadyCompletedMsg();
        }
        else if (npcId == 7106 && st.getInt("cond") == 1 && st.getInt("onlyone") == 0)
        {
            if (st.getQuestItemsCount(DUFNERS_LETTER_ID) == 1 && st.getQuestItemsCount(TERYS_REPORT_ID) == 0)
            {
                htmltext = "7106-06.htm";
            }
            else if (st.getQuestItemsCount(DUFNERS_LETTER_ID) == 0 && st.getQuestItemsCount(TERYS_REPORT_ID) == 0)
            {
                htmltext = "7106-07.htm";
            }
            else if (st.getQuestItemsCount(DUFNERS_LETTER_ID) == 0 && st.getQuestItemsCount(TERYS_REPORT_ID) == 1)
            {
                st.addExpAndSp(72126, 11000);
                st.giveItems(7562, 8);
                htmltext = "7106-08.htm";
                st.set("cond", "0");
                st.set("onlyone", "1");
                st.setState(State.COMPLETED);
                st.playSound("ItemSound.quest_finish");
                st.takeItems(TERYS_REPORT_ID, 1);
                st.giveItems(7562, 8);
                st.giveItems(MARK_OF_SEEKER_ID, 1);
            }
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(DUFNERS_LETTER_ID) == 1)
        {
            htmltext = "7064-01.htm";
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(TERYS_ORDER1_ID) == 1)
        {
            if (st.getQuestItemsCount(MYSTERIOUS_RUNESTONE_ID) == 0)
            {
                htmltext = "7064-04.htm";
            }
            else
            {
                htmltext = "7064-05.htm";
            }
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(TERYS_ORDER2_ID) == 1)
        {
            if (st.getQuestItemsCount(TERYS_ORDER2_ID) == 1)
            {
                if (st.getQuestItemsCount(OL_MAHUM_RUNESTONE_ID) + st.getQuestItemsCount(TUREK_RUNESTONE_ID) + st.getQuestItemsCount(ANT_RUNESTONE_ID) + st.getQuestItemsCount(TURAK_BUGBEAR_RUNESTONE_ID) < 4)
                {
                    htmltext = "7064-08.htm";
                }
                else
                {
                    htmltext = "7064-09.htm";
                }
            }
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(TERYS_LETTER_ID) == 1)
        {
            htmltext = "7064-11.htm";
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(VIKTORS_LETTER_ID) == 1)
        {
            htmltext = "7064-12.htm";
            st.takeItems(VIKTORS_LETTER_ID, 1);
            st.giveItems(HAWKEYES_LETTER_ID, 1);
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(HAWKEYES_LETTER_ID) == 1)
        {
            htmltext = "7064-13.htm";
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && (st.getQuestItemsCount(VIKTORS_REQUEST_ID) == 1 || st.getQuestItemsCount(ANALYSIS_REQUEST_ID) == 1 || st.getQuestItemsCount(MARINAS_LETTER_ID) == 1 || st.getQuestItemsCount(EXPERIMENT_TOOLS_ID) == 1))
        {
            htmltext = "7064-14.htm";
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(ANALYSIS_RESULT_ID) == 1)
        {
            htmltext = "7064-15.htm";
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(TERYS_ORDER3_ID) == 1)
        {
            if (st.getPlayer().getLevel() < 36)
            {
                htmltext = "7064-20.htm";
            }
            else
            {
                htmltext = "7064-21.htm";
                st.giveItems(LIST_OF_HOST_ID, 1);
                st.takeItems(TERYS_ORDER3_ID, 1);
            }
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(LIST_OF_HOST_ID) == 1)
        {
            if (st.getQuestItemsCount(ABYSS_RUNESTONE1_ID) + st.getQuestItemsCount(ABYSS_RUNESTONE2_ID) + st.getQuestItemsCount(ABYSS_RUNESTONE3_ID) + st.getQuestItemsCount(ABYSS_RUNESTONE4_ID) < 4)
            {
                htmltext = "7064-22.htm";
            }
            else
            {
                htmltext = "7064-23.htm";
                st.giveItems(TERYS_REPORT_ID, 1);
                st.takeItems(LIST_OF_HOST_ID, 1);
                st.takeItems(ABYSS_RUNESTONE1_ID, 1);
                st.takeItems(ABYSS_RUNESTONE2_ID, 1);
                st.takeItems(ABYSS_RUNESTONE3_ID, 1);
                st.takeItems(ABYSS_RUNESTONE4_ID, 1);
            }
        }
        else if (npcId == 7064 && st.getInt("cond") == 1 && st.getQuestItemsCount(TERYS_REPORT_ID) == 1)
        {
            htmltext = "7064-24.htm";
        }
        else if (npcId == 7684 && st.getInt("cond") == 1 && st.getQuestItemsCount(TERYS_LETTER_ID) == 1)
        {
            htmltext = "7684-01.htm";
        }
        else if (npcId == 7684 && st.getInt("cond") == 1 && st.getQuestItemsCount(HAWKEYES_LETTER_ID) == 1)
        {
            htmltext = "7684-12.htm";
        }
        else if (npcId == 7684 && st.getInt("cond") == 1 && st.getQuestItemsCount(VIKTORS_REQUEST_ID) == 1)
        {
            if (st.getQuestItemsCount(MEDUSAS_SCALES_ID) < 10)
            {
                htmltext = "7684-13.htm";
            }
            else
            {
                htmltext = "7684-14.htm";
            }
        }
        else if (npcId == 7684 && st.getInt("cond") == 1 && st.getQuestItemsCount(SILENS_RUNESTONE_ID) == 1 && st.getQuestItemsCount(ANALYSIS_REQUEST_ID) == 1)
        {
            htmltext = "7684-16.htm";
        }
        else if (npcId == 7684 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MARINAS_LETTER_ID) == 1 && st.getQuestItemsCount(EXPERIMENT_TOOLS_ID) == 1 && st.getQuestItemsCount(ANALYSIS_RESULT_ID) == 1 && st.getQuestItemsCount(TERYS_REPORT_ID) == 1))
        {
            htmltext = "7684-17.htm";
        }
        else if (npcId == 7715 && st.getInt("cond") == 1 && st.getQuestItemsCount(SILENS_RUNESTONE_ID) == 1 && st.getQuestItemsCount(ANALYSIS_REQUEST_ID) == 1)
        {
            htmltext = "7715-01.htm";
        }
        else if (npcId == 7715 && st.getInt("cond") == 1 && st.getQuestItemsCount(MARINAS_LETTER_ID) == 1)
        {
            htmltext = "7715-03.htm";
        }
        else if (npcId == 7715 && st.getInt("cond") == 1 && st.getQuestItemsCount(EXPERIMENT_TOOLS_ID) == 1)
        {
            htmltext = "7715-04.htm";
        }
        else if (npcId == 7715 && st.getInt("cond") == 1 && (st.getQuestItemsCount(ANALYSIS_RESULT_ID) == 1 || st.getQuestItemsCount(TERYS_REPORT_ID) == 1))
        {
            htmltext = "7715-06.htm";
        }
        else if (npcId == 7526 && st.getInt("cond") == 1 && st.getQuestItemsCount(MARINAS_LETTER_ID) == 1)
        {
            htmltext = "7526-01.htm";
            st.takeItems(MARINAS_LETTER_ID, 1);
            st.giveItems(EXPERIMENT_TOOLS_ID, 1);
        }
        else if (npcId == 7526 && st.getInt("cond") == 1 && st.getQuestItemsCount(EXPERIMENT_TOOLS_ID) == 1)
        {
            htmltext = "7526-02.htm";
        }
        
        return htmltext;
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return null;
        
        if (st.getState() != State.STARTED)
            return null;
        
        int npcId = npc.getNpcId();
        
        if (DROPLIST.containsKey(npcId))
        {
            int[] dropData = DROPLIST.get(npcId);
            int required = dropData[0];
            int item = dropData[1];
            int chance = dropData[2];
            int maxqty = dropData[3];
            
            long count = st.getQuestItemsCount(item);
            
            if (st.getQuestItemsCount(required) > 0 && count < maxqty)
            {
                if (st.getRandom(100) < chance)
                {
                    st.giveItems(item, 1);
                    if (count + 1 == maxqty)
                    {
                        st.playSound("Itemsound.quest_middle");
                    }
                    else
                    {
                        st.playSound("Itemsound.quest_itemget");
                    }
                }
            }
        }
        
        return null;
    }
}