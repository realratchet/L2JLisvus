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
package quests.Q211_TrialOfChallenger;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Fix by Cromir for Kilah
 */
public class Q211_TrialOfChallenger extends Quest
{
    // Items
    private static final int LETTER_OF_KASH_ID = 2628;
    private static final int SCROLL_OF_SHYSLASSY_ID = 2631;
    private static final int WATCHERS_EYE1_ID = 2629;
    private static final int BROKEN_KEY_ID = 2632;
    private static final int MITHRIL_SCALE_GAITERS_MATERIAL_ID = 2918;
    private static final int BRIGANDINE_GAUNTLET_PATTERN_ID = 2927;
    private static final int MANTICOR_SKIN_GAITERS_PATTERN_ID = 1943;
    private static final int GAUNTLET_OF_REPOSE_OF_THE_SOUL_PATTERN_ID = 1946;
    private static final int IRON_BOOTS_DESIGN_ID = 1940;
    private static final int TOME_OF_BLOOD_PAGE_ID = 2030;
    private static final int ELVEN_NECKLACE_BEADS_ID = 1904;
    private static final int WHITE_TUNIC_PATTERN_ID = 1936;
    private static final int MARK_OF_CHALLENGER_ID = 2627;
    private static final int WATCHERS_EYE2_ID = 2630;
    
    // NPCs
    private static final int NPC_KASH = 7644;
    private static final int NPC_7535 = 7535;
    private static final int NPC_7645 = 7645;
    private static final int NPC_7646 = 7646;
    private static final int NPC_7647 = 7647;
    
    // Mobs
    private static final int MOB_5110 = 5110;
    private static final int MOB_5112 = 5112;
    private static final int MOB_5113 = 5113;
    private static final int MOB_5114 = 5114;

    public static void main(String[] args)
    {
        new Q211_TrialOfChallenger();
    }
    
    public Q211_TrialOfChallenger()
    {
        super(211, Q211_TrialOfChallenger.class.getSimpleName(), "Trial Of Challenger");
        
        setItemsIds(SCROLL_OF_SHYSLASSY_ID, LETTER_OF_KASH_ID, WATCHERS_EYE1_ID, BROKEN_KEY_ID, WATCHERS_EYE2_ID);
        
        addStartNpc(NPC_KASH);
        addTalkId(NPC_7535, NPC_KASH, NPC_7645, NPC_7646, NPC_7647);
        
        addKillId(MOB_5110, MOB_5112, MOB_5113, MOB_5114);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return null;
        
        String htmltext = event;
        
        if ("1".equals(event))
        {
            htmltext = "7644-05.htm";
            st.set("cond", "1");
            st.setState(State.STARTED);
            st.playSound("ItemSound.quest_accept");
        }
        else if ("7644_1".equals(event))
        {
            htmltext = "7644-04.htm";
        }
        else if ("7645_1".equals(event))
        {
            htmltext = "7645-02.htm";
            st.takeItems(LETTER_OF_KASH_ID, 1);
            st.set("cond", "4");
            st.playSound("Itemsound.quest_middle");
        }
        else if ("7647_1".equals(event))
        {
            if (st.getQuestItemsCount(BROKEN_KEY_ID) == 1)
            {
                st.giveItems(SCROLL_OF_SHYSLASSY_ID, 1);
                st.playSound("Itemsound.quest_middle");
                if (st.getRandom(10) < 2)
                {
                    htmltext = "7647-03.htm";
                    st.takeItems(BROKEN_KEY_ID, 1);
                    st.playSound("ItemSound.quest_jackpot");
                    int n = st.getRandom(100);
                    if (n > 90)
                    {
                        st.giveItems(MITHRIL_SCALE_GAITERS_MATERIAL_ID, 1);
                        st.giveItems(BRIGANDINE_GAUNTLET_PATTERN_ID, 1);
                        st.giveItems(MANTICOR_SKIN_GAITERS_PATTERN_ID, 1);
                        st.giveItems(GAUNTLET_OF_REPOSE_OF_THE_SOUL_PATTERN_ID, 1);
                        st.giveItems(IRON_BOOTS_DESIGN_ID, 1);
                        st.playSound("Itemsound.quest_middle");
                    }
                    else if (n > 70)
                    {
                        st.giveItems(TOME_OF_BLOOD_PAGE_ID, 1);
                        st.giveItems(ELVEN_NECKLACE_BEADS_ID, 1);
                        st.playSound("Itemsound.quest_middle");
                    }
                    else if (n > 40)
                    {
                        st.giveItems(WHITE_TUNIC_PATTERN_ID, 1);
                        st.playSound("Itemsound.quest_middle");
                    }
                    else
                    {
                        st.giveItems(IRON_BOOTS_DESIGN_ID, 1);
                        st.playSound("Itemsound.quest_middle");
                    }
                }
                else
                {
                    htmltext = "7647-02.htm";
                    int n = st.getRandom(1000) + 1;
                    st.takeItems(BROKEN_KEY_ID, 1);
                    st.giveItems(Inventory.ADENA_ID, n);
                    st.playSound("Itemsound.quest_middle");
                }
            }
            else
            {
                htmltext = "7647-04.htm";
                st.takeItems(BROKEN_KEY_ID, 1);
            }
        }
        else if ("7646_1".equals(event))
        {
            htmltext = "7646-02.htm";
        }
        else if ("7646_2".equals(event))
        {
            htmltext = "7646-03.htm";
        }
        else if ("7646_3".equals(event))
        {
            htmltext = "7646-04.htm";
            st.set("cond", "8");
            st.takeItems(WATCHERS_EYE2_ID, 1);
        }
        else if ("7646_4".equals(event))
        {
            htmltext = "7646-06.htm";
            st.set("cond", "8");
            st.takeItems(WATCHERS_EYE2_ID, 1);
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
        int state = st.getState();
        
        if (state == State.CREATED)
        {
            st.set("cond", "0");
            if (npcId == NPC_KASH)
            {
                int cls = player.getClassId().ordinal();
                if (cls == 0x01 || cls == 0x13 || cls == 0x20 || cls == 0x2d || cls == 0x2f)
                {
                    if (player.getLevel() >= 35)
                        htmltext = "7644-03.htm";
                    else
                    {
                        htmltext = "7644-01.htm";
                        st.exitQuest(true);
                    }
                }
                else
                {
                    htmltext = "7644-02.htm";
                    st.exitQuest(true);
                }
            }
        }
        else if (npcId == NPC_KASH && state == State.COMPLETED)
        {
            htmltext = getAlreadyCompletedMsg();
        }
        else if (npcId == NPC_KASH && st.getInt("cond") == 1)
        {
            htmltext = "7644-06.htm";
        }
        else if (npcId == NPC_KASH && st.getInt("cond") == 2 && st.getQuestItemsCount(SCROLL_OF_SHYSLASSY_ID) == 1)
        {
            htmltext = "7644-07.htm";
            st.takeItems(SCROLL_OF_SHYSLASSY_ID, 1);
            st.giveItems(LETTER_OF_KASH_ID, 1);
            st.set("cond", "3");
            st.playSound("Itemsound.quest_middle");
        }
        else if (npcId == NPC_KASH && st.getInt("cond") == 1 && st.getQuestItemsCount(LETTER_OF_KASH_ID) == 1)
        {
            htmltext = "7644-08.htm";
        }
        else if (npcId == NPC_KASH && st.getInt("cond") >= 7)
        {
            htmltext = "7644-09.htm";
        }
        else if (npcId == NPC_7645 && st.getInt("cond") == 3 && st.getQuestItemsCount(LETTER_OF_KASH_ID) == 1)
        {
            htmltext = "7645-01.htm";
        }
        else if (npcId == NPC_7645 && st.getInt("cond") == 4 && st.getQuestItemsCount(WATCHERS_EYE1_ID) == 0)
        {
            htmltext = "7645-03.htm";
        }
        else if (npcId == NPC_7645 && st.getInt("cond") == 5 && st.getQuestItemsCount(WATCHERS_EYE1_ID) > 0)
        {
            htmltext = "7645-04.htm";
            st.takeItems(WATCHERS_EYE1_ID, 1);
            st.set("cond", "6");
            st.playSound("Itemsound.quest_middle");
        }
        else if (npcId == NPC_7645 && st.getInt("cond") == 6)
        {
            htmltext = "7645-05.htm";
        }
        else if (npcId == NPC_7645 && st.getInt("cond") >= 7)
        {
            htmltext = "7645-06.htm";
        }
        else if (npcId == NPC_7647 && st.getInt("cond") == 2)
        {
            htmltext = "7647-01.htm";
        }
        else if (npcId == NPC_7646 && st.getInt("cond") == 7 && st.getQuestItemsCount(WATCHERS_EYE2_ID) > 0)
        {
            htmltext = "7646-01.htm";
        }
        else if (npcId == NPC_7646 && st.getInt("cond") == 7)
        {
            htmltext = "7646-06a.htm";
        }
        else if (npcId == NPC_7646 && st.getInt("cond") == 10)
        {
            st.addExpAndSp(72394, 11250);
            st.giveItems(7562, 8);
            htmltext = "7646-07.htm";
            st.takeItems(BROKEN_KEY_ID, 1);
            st.giveItems(7562, 8);
            st.giveItems(MARK_OF_CHALLENGER_ID, 1);
            st.setState(State.COMPLETED);
            st.playSound("ItemSound.quest_finish");
            st.set("cond", "0");
        }
        else if (npcId == NPC_7535 && st.getInt("cond") == 7)
        {
            if (player.getLevel() >= 36)
            {
                htmltext = "7535-01.htm";
                st.addRadar(176560, -184969, -3729);
                st.set("cond", "8");
                st.playSound("Itemsound.quest_middle");
            }
            else
            {
                htmltext = "7535-03.htm";
            }
        }
        else if (npcId == NPC_7535 && st.getInt("cond") == 8)
        {
            htmltext = "7535-02.htm";
            st.addRadar(176560, -184969, -3729);
            st.set("cond", "9");
            st.playSound("Itemsound.quest_middle");
        }
        
        return htmltext;
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return null;
        
        int npcId = npc.getNpcId();
        
        if (npcId == MOB_5110)
        {
            if (st.getInt("cond") == 1 && st.getQuestItemsCount(BROKEN_KEY_ID) == 0)
            {
                st.giveItems(BROKEN_KEY_ID, 1);
                st.addSpawn(NPC_7647, npc, true, 0);
                st.playSound("ItemSound.quest_middle");
                st.set("cond", "2");
            }
        }
        else if (npcId == MOB_5112)
        {
            if (st.getInt("cond") == 4 && st.getQuestItemsCount(WATCHERS_EYE1_ID) == 0)
            {
                st.giveItems(WATCHERS_EYE1_ID, 1);
                st.set("cond", "5");
                st.playSound("ItemSound.quest_middle");
            }
        }
        else if (npcId == MOB_5113)
        {
            if (st.getInt("cond") == 6 && st.getQuestItemsCount(WATCHERS_EYE2_ID) == 0)
            {
                st.giveItems(WATCHERS_EYE2_ID, 1);
                st.playSound("ItemSound.quest_middle");
                st.set("cond", "7");
                st.addSpawn(NPC_7646, npc, true, 0);
            }
        }
        else if (npcId == MOB_5114)
        {
            if (st.getInt("cond") == 9)
            {
                st.set("cond", "10");
                st.playSound("ItemSound.quest_middle");
                st.addSpawn(NPC_7646, npc, true, 0);
            }
        }
        
        return null;
    }
}