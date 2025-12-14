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
package quests.Q214_TrialOfScholar;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Made by Mr. Have fun! Version 0.2
 */
public class Q214_TrialOfScholar extends Quest
{
    // Quest Items
    private static final int MARK_OF_SCHOLAR_ID = 2674;
    private static final int MIRIENS_SIGIL1_ID = 2675;
    private static final int MIRIENS_SIGIL2_ID = 2676;
    private static final int MIRIENS_SIGIL3_ID = 2677;
    private static final int MIRIENS_INSTRUCTION_ID = 2678;
    private static final int MARIAS_LETTER1_ID = 2679;
    private static final int SYMBOL_OF_JUREK_ID = 2698;
    private static final int SYMBOL_OF_SYLVAIN_ID = 2693;
    private static final int SYMBOL_OF_CRONOS_ID = 2720;
    private static final int HIGHPRIESTS_SIGIL_ID = 2689;
    private static final int SYLVAINS_LETTER_ID = 2692;
    private static final int CRYSTAL_OF_PURITY1_ID = 2688;
    private static final int LUCILLAS_HANDBAG_ID = 2682;
    private static final int CRETAS_LETTER1_ID = 2683;
    private static final int BROWN_SCROLL_SCRAP_ID = 2687;
    private static final int CRETAS_PAINTING3_ID = 2686;
    private static final int MARIAS_LETTER2_ID = 2680;
    private static final int LUKAS_LETTER_ID = 2681;
    private static final int CRETAS_PAINTING2_ID = 2685;
    private static final int CRETAS_PAINTING1_ID = 2684;
    private static final int CRYSTAL_OF_PURITY2_ID = 2714;
    private static final int VALKONS_REQUEST_ID = 2710;
    private static final int JUREKS_LIST_ID = 2694;
    private static final int GMAGISTERS_SIGIL_ID = 2690;
    private static final int MEYEDESTROYERS_SKIN_ID = 2695;
    private static final int SHAMANS_NECKLACE_ID = 2696;
    private static final int SHACKLES_SCALP_ID = 2697;
    private static final int CRETAS_LETTER2_ID = 2701;
    private static final int DIETERS_KEY_ID = 2700;
    private static final int CRONOS_SIGIL_ID = 2691;
    private static final int CRONOS_LETTER_ID = 2699;
    private static final int SCRIPTURE_CHAPTER_1_ID = 2706;
    private static final int SCRIPTURE_CHAPTER_2_ID = 2707;
    private static final int SCRIPTURE_CHAPTER_3_ID = 2708;
    private static final int SCRIPTURE_CHAPTER_4_ID = 2709;
    private static final int TRIFFS_RING_ID = 2705;
    private static final int DIETERS_DIARY_ID = 2703;
    private static final int DIETERS_LETTER_ID = 2702;
    private static final int RAUTS_LETTER_ENVELOPE_ID = 2704;
    private static final int STRONG_LIQUOR_ID = 2713;
    private static final int POITANS_NOTES_ID = 2711;
    private static final int CASIANS_LIST_ID = 2715;
    private static final int GHOULS_SKIN_ID = 2716;
    private static final int MEDUSAS_BLOOD_ID = 2717;
    private static final int FETTEREDSOULS_ICHOR_ID = 2718;
    private static final int ENCHT_GARGOYLES_NAIL_ID = 2719;

    public static void main(String[] args)
    {
        new Q214_TrialOfScholar();
    }
    
    public Q214_TrialOfScholar()
    {
        super(214, Q214_TrialOfScholar.class.getSimpleName(), "Trial Of Scholar");
        
        setItemsIds(CRYSTAL_OF_PURITY2_ID, CASIANS_LIST_ID, GHOULS_SKIN_ID, MEDUSAS_BLOOD_ID, FETTEREDSOULS_ICHOR_ID, ENCHT_GARGOYLES_NAIL_ID, SYMBOL_OF_CRONOS_ID);
        
        addStartNpc(7461);
        
        addTalkId(7070);
        addTalkId(7071);
        addTalkId(7103);
        addTalkId(7111);
        addTalkId(7115);
        addTalkId(7230);
        addTalkId(7316);
        addTalkId(7458);
        addTalkId(7461);
        addTalkId(7608);
        addTalkId(7609);
        addTalkId(7610);
        addTalkId(7611);
        addTalkId(7612);
        
        addKillId(158);
        addKillId(201);
        addKillId(235);
        addKillId(269);
        addKillId(552);
        addKillId(554);
        addKillId(567);
        addKillId(580);
        addKillId(68);
    }
    
    @Override
    public String onEvent(String event, QuestState st)
    {
        String htmltext = event;
        
        if (event.equals("1"))
        {
            htmltext = "7461-04.htm";
            st.set("cond", "1");
            st.setState(State.STARTED);
            st.playSound("ItemSound.quest_accept");
            st.giveItems(MIRIENS_SIGIL1_ID, 1);
        }
        else if (event.equals("7461_1"))
        {
            if (st.getPlayer().getLevel() < 36)
            {
                htmltext = "7461-09.htm";
                st.takeItems(SYMBOL_OF_JUREK_ID, 1);
                st.giveItems(MIRIENS_INSTRUCTION_ID, 1);
                st.takeItems(MIRIENS_SIGIL2_ID, 1);
            }
            else
            {
                htmltext = "7461-10.htm";
                st.takeItems(SYMBOL_OF_JUREK_ID, 1);
                st.giveItems(MIRIENS_SIGIL3_ID, 1);
                st.takeItems(MIRIENS_SIGIL2_ID, 1);
            }
        }
        else if (event.equals("7070_1"))
        {
            htmltext = "7070-02.htm";
            st.giveItems(HIGHPRIESTS_SIGIL_ID, 1);
            st.giveItems(SYLVAINS_LETTER_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7608_1"))
        {
            htmltext = "7608-02.htm";
            st.giveItems(MARIAS_LETTER1_ID, 1);
            st.takeItems(SYLVAINS_LETTER_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7608_2"))
        {
            htmltext = "7608-07.htm";
        }
        else if (event.equals("7608_3"))
        {
            htmltext = "7608-08.htm";
            st.giveItems(LUCILLAS_HANDBAG_ID, 1);
            st.takeItems(CRETAS_LETTER1_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7608_4"))
        {
            htmltext = "7608-14.htm";
            st.takeItems(BROWN_SCROLL_SCRAP_ID, st.getQuestItemsCount(BROWN_SCROLL_SCRAP_ID));
            st.giveItems(CRYSTAL_OF_PURITY1_ID, 1);
            st.takeItems(CRETAS_PAINTING3_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7115_1"))
        {
            htmltext = "7115-02.htm";
        }
        else if (event.equals("7115_2"))
        {
            htmltext = "7115-03.htm";
            st.giveItems(JUREKS_LIST_ID, 1);
            st.giveItems(GMAGISTERS_SIGIL_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7071_1"))
        {
            htmltext = "7071-04.htm";
            st.giveItems(CRETAS_PAINTING3_ID, 1);
            st.takeItems(CRETAS_PAINTING2_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7609_1"))
        {
            htmltext = "7609-02.htm";
        }
        else if (event.equals("7609_2"))
        {
            htmltext = "7609-03.htm";
        }
        else if (event.equals("7609_3"))
        {
            htmltext = "7609-04.htm";
        }
        else if (event.equals("7609_4"))
        {
            htmltext = "7609-05.htm";
            st.giveItems(CRETAS_LETTER1_ID, 1);
            st.takeItems(MARIAS_LETTER2_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7609_5"))
        {
            htmltext = "7609-08.htm";
        }
        else if (event.equals("7609_6"))
        {
            htmltext = "7609-09.htm";
            st.giveItems(CRETAS_PAINTING1_ID, 1);
            st.takeItems(LUCILLAS_HANDBAG_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7609_7"))
        {
            htmltext = "7609-13.htm";
        }
        else if (event.equals("7609_8"))
        {
            htmltext = "7609-14.htm";
            st.giveItems(CRETAS_LETTER2_ID, 1);
            st.takeItems(DIETERS_KEY_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7610_1"))
        {
            htmltext = "7610-02.htm";
        }
        else if (event.equals("7610_2"))
        {
            htmltext = "7610-03.htm";
        }
        else if (event.equals("7610_3"))
        {
            htmltext = "7610-04.htm";
        }
        else if (event.equals("7610_4"))
        {
            htmltext = "7610-05.htm";
        }
        else if (event.equals("7610_5"))
        {
            htmltext = "7610-06.htm";
        }
        else if (event.equals("7610_6"))
        {
            htmltext = "7610-07.htm";
        }
        else if (event.equals("7610_7"))
        {
            htmltext = "7610-08.htm";
        }
        else if (event.equals("7610_8"))
        {
            htmltext = "7610-09.htm";
        }
        else if (event.equals("7610_9"))
        {
            htmltext = "7610-10.htm";
            st.giveItems(CRONOS_SIGIL_ID, 1);
            st.giveItems(CRONOS_LETTER_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7610_10"))
        {
            htmltext = "7610-13.htm";
        }
        else if (event.equals("7610_11"))
        {
            htmltext = "7610-14.htm";
            st.takeItems(SCRIPTURE_CHAPTER_1_ID, 1);
            st.takeItems(SCRIPTURE_CHAPTER_2_ID, 1);
            st.takeItems(SCRIPTURE_CHAPTER_3_ID, 1);
            st.takeItems(SCRIPTURE_CHAPTER_4_ID, 1);
            st.takeItems(CRONOS_SIGIL_ID, 1);
            st.takeItems(TRIFFS_RING_ID, 1);
            st.giveItems(SYMBOL_OF_CRONOS_ID, 1);
            st.takeItems(DIETERS_DIARY_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7111_1"))
        {
            htmltext = "7111-02.htm";
        }
        else if (event.equals("7111_2"))
        {
            htmltext = "7111-03.htm";
        }
        else if (event.equals("7111_3"))
        {
            htmltext = "7111-04.htm";
        }
        else if (event.equals("7111_4"))
        {
            htmltext = "7111-05.htm";
            st.giveItems(DIETERS_KEY_ID, 1);
            st.takeItems(CRONOS_LETTER_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7111_5"))
        {
            htmltext = "7111-08.htm";
        }
        else if (event.equals("7111_6"))
        {
            htmltext = "7111-09.htm";
            st.giveItems(DIETERS_LETTER_ID, 1);
            st.takeItems(CRETAS_LETTER2_ID, 1);
            st.giveItems(DIETERS_DIARY_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7230_1"))
        {
            htmltext = "7230-02.htm";
            st.giveItems(RAUTS_LETTER_ENVELOPE_ID, 1);
            st.takeItems(DIETERS_LETTER_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7316_1"))
        {
            htmltext = "7316-02.htm";
            st.giveItems(SCRIPTURE_CHAPTER_1_ID, 1);
            st.takeItems(RAUTS_LETTER_ENVELOPE_ID, 1);
            st.giveItems(STRONG_LIQUOR_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7611_1"))
        {
            htmltext = "7611-02.htm";
        }
        else if (event.equals("7611_2"))
        {
            htmltext = "7611-03.htm";
        }
        else if (event.equals("7611_3"))
        {
            htmltext = "7611-04.htm";
            st.giveItems(TRIFFS_RING_ID, 1);
            st.takeItems(STRONG_LIQUOR_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7103_1"))
        {
            htmltext = "7103-02.htm";
        }
        else if (event.equals("7103_2"))
        {
            htmltext = "7103-03.htm";
        }
        else if (event.equals("7103_3"))
        {
            htmltext = "7103-04.htm";
            st.giveItems(VALKONS_REQUEST_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7612_1"))
        {
            htmltext = "7612-03.htm";
        }
        else if (event.equals("7612_2"))
        {
            htmltext = "7612-04.htm";
            st.giveItems(CASIANS_LIST_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (event.equals("7612_3"))
        {
            htmltext = "7612-07.htm";
            st.giveItems(SCRIPTURE_CHAPTER_4_ID, 1);
            st.takeItems(CASIANS_LIST_ID, 1);
            st.takeItems(GHOULS_SKIN_ID, st.getQuestItemsCount(GHOULS_SKIN_ID));
            st.takeItems(MEDUSAS_BLOOD_ID, st.getQuestItemsCount(MEDUSAS_BLOOD_ID));
            st.takeItems(FETTEREDSOULS_ICHOR_ID, st.getQuestItemsCount(FETTEREDSOULS_ICHOR_ID));
            st.takeItems(ENCHT_GARGOYLES_NAIL_ID, st.getQuestItemsCount(ENCHT_GARGOYLES_NAIL_ID));
            st.takeItems(POITANS_NOTES_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        
        return htmltext;
    }
    
    @Override
    public String onTalk(L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = Quest.getNoQuestMsg();
        
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        int npcId = npc.getNpcId();
        byte id = st.getState();
        
        if (id == State.COMPLETED)
        {
            htmltext = Quest.getAlreadyCompletedMsg();
        }
        else if (npcId == 7461 && st.getInt("cond") == 0)
        {
            if (player.getClassId().getId() == 0x0b || player.getClassId().getId() == 0x1a || player.getClassId().getId() == 0x27)
            {
                if (player.getLevel() >= 35)
                {
                    htmltext = "7461-03.htm";
                }
                else
                {
                    htmltext = "7461-02.htm";
                    st.exitQuest(true);
                }
            }
            else
            {
                htmltext = "7461-01.htm";
                st.exitQuest(true);
            }
        }
        else if (npcId == 7461 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) == 1 && st.getQuestItemsCount(SYMBOL_OF_SYLVAIN_ID) == 0)
        {
            htmltext = "7461-05.htm";
        }
        else if (npcId == 7461 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) == 1 && st.getQuestItemsCount(SYMBOL_OF_SYLVAIN_ID) == 1)
        {
            htmltext = "7461-06.htm";
            st.takeItems(SYMBOL_OF_SYLVAIN_ID, 1);
            st.giveItems(MIRIENS_SIGIL2_ID, 1);
            st.takeItems(MIRIENS_SIGIL1_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 7461 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL2_ID) == 1 && st.getQuestItemsCount(SYMBOL_OF_JUREK_ID) == 0)
        {
            htmltext = "7461-07.htm";
        }
        else if (npcId == 7461 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL2_ID) == 1 && st.getQuestItemsCount(SYMBOL_OF_JUREK_ID) == 1)
        {
            htmltext = "7461-08.htm";
        }
        else if (npcId == 7461 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_INSTRUCTION_ID) == 1)
        {
            if (player.getLevel() < 36)
            {
                htmltext = "7461-11.htm";
            }
            else
            {
                htmltext = "7461-12.htm";
                st.giveItems(MIRIENS_SIGIL3_ID, 1);
                st.takeItems(MIRIENS_INSTRUCTION_ID, 1);
                st.playSound("ItemSound.quest_middle");
            }
        }
        else if (npcId == 7461 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) == 1)
        {
            if (st.getQuestItemsCount(SYMBOL_OF_CRONOS_ID) == 0)
            {
                htmltext = "7461-13.htm";
            }
            else
            {
                st.addExpAndSp(80265, 30000);
                st.giveItems(7562, 8);
                htmltext = "7461-14.htm";
                st.set("cond", "0");
                st.setState(State.COMPLETED);
                st.playSound("ItemSound.quest_finish");
                st.takeItems(SYMBOL_OF_CRONOS_ID, 1);
                st.takeItems(MIRIENS_SIGIL3_ID, 1);
                st.giveItems(7562, 8);
                st.giveItems(MARK_OF_SCHOLAR_ID, 1);
            }
        }
        else if (npcId == 7070 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) == 1 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) == 0 && st.getQuestItemsCount(SYMBOL_OF_SYLVAIN_ID) == 0)
        {
            htmltext = "7070-01.htm";
        }
        else if (npcId == 7070 && st.getInt("cond") == 1 && st.getQuestItemsCount(CRYSTAL_OF_PURITY1_ID) == 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0)
        {
            htmltext = "7070-03.htm";
        }
        else if (npcId == 7070 && st.getInt("cond") == 1 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(CRYSTAL_OF_PURITY1_ID) > 0)
        {
            htmltext = "7070-04.htm";
            st.giveItems(SYMBOL_OF_SYLVAIN_ID, 1);
            st.takeItems(HIGHPRIESTS_SIGIL_ID, 1);
            st.takeItems(CRYSTAL_OF_PURITY1_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 7070 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(SYMBOL_OF_SYLVAIN_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) == 0)
        {
            htmltext = "7070-05.htm";
        }
        else if (npcId == 7070 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL2_ID) > 0 || st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0))
        {
            htmltext = "7070-06.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(SYLVAINS_LETTER_ID) > 0))
        {
            htmltext = "7608-01.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(MARIAS_LETTER1_ID) > 0))
        {
            htmltext = "7608-03.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(LUKAS_LETTER_ID) > 0))
        {
            htmltext = "7608-04.htm";
            st.giveItems(MARIAS_LETTER2_ID, 1);
            st.takeItems(LUKAS_LETTER_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(MARIAS_LETTER2_ID) > 0))
        {
            htmltext = "7608-05.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_LETTER1_ID) > 0))
        {
            htmltext = "7608-06.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(LUCILLAS_HANDBAG_ID) > 0))
        {
            htmltext = "7608-09.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_PAINTING1_ID) > 0))
        {
            htmltext = "7608-10.htm";
            st.giveItems(CRETAS_PAINTING2_ID, 1);
            st.takeItems(CRETAS_PAINTING1_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_PAINTING2_ID) > 0))
        {
            htmltext = "7608-11.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_PAINTING3_ID) > 0) && st.getQuestItemsCount(BROWN_SCROLL_SCRAP_ID) < 5)
        {
            htmltext = "7608-12.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_PAINTING3_ID) > 0) && st.getQuestItemsCount(BROWN_SCROLL_SCRAP_ID) >= 5)
        {
            htmltext = "7608-13.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRYSTAL_OF_PURITY1_ID) > 0))
        {
            htmltext = "7608-15.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && (st.getQuestItemsCount(SYMBOL_OF_SYLVAIN_ID) > 0 || st.getQuestItemsCount(MIRIENS_SIGIL2_ID) > 0))
        {
            htmltext = "7608-16.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) == 1 && st.getQuestItemsCount(VALKONS_REQUEST_ID) == 0)
        {
            htmltext = "7608-17.htm";
        }
        else if (npcId == 7608 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) == 1 && st.getQuestItemsCount(VALKONS_REQUEST_ID) == 1)
        {
            htmltext = "7608-18.htm";
            st.giveItems(CRYSTAL_OF_PURITY2_ID, 1);
            st.takeItems(VALKONS_REQUEST_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 7115 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL2_ID) == 1 && st.getQuestItemsCount(GMAGISTERS_SIGIL_ID) == 0 && st.getQuestItemsCount(SYMBOL_OF_JUREK_ID) == 0)
        {
            htmltext = "7115-01.htm";
        }
        else if (npcId == 7115 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL2_ID) == 1 && st.getQuestItemsCount(JUREKS_LIST_ID) == 1)
        {
            if (st.getQuestItemsCount(MEYEDESTROYERS_SKIN_ID) + st.getQuestItemsCount(SHAMANS_NECKLACE_ID) + st.getQuestItemsCount(SHACKLES_SCALP_ID) < 12)
            {
                htmltext = "7115-04.htm";
            }
            else
            {
                htmltext = "7115-05.htm";
                st.takeItems(JUREKS_LIST_ID, 1);
                st.takeItems(MEYEDESTROYERS_SKIN_ID, st.getQuestItemsCount(MEYEDESTROYERS_SKIN_ID));
                st.takeItems(SHAMANS_NECKLACE_ID, st.getQuestItemsCount(SHAMANS_NECKLACE_ID));
                st.takeItems(SHACKLES_SCALP_ID, st.getQuestItemsCount(SHACKLES_SCALP_ID));
                st.giveItems(SYMBOL_OF_JUREK_ID, 1);
                st.takeItems(GMAGISTERS_SIGIL_ID, 1);
                st.playSound("ItemSound.quest_middle");
            }
        }
        else if (npcId == 7115 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL2_ID) > 0 && st.getQuestItemsCount(SYMBOL_OF_JUREK_ID) > 0) && st.getQuestItemsCount(GMAGISTERS_SIGIL_ID) == 0)
        {
            htmltext = "7115-06.htm";
        }
        else if (npcId == 7115 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 || st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0))
        {
            htmltext = "7115-07.htm";
        }
        else if (npcId == 7071 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(MARIAS_LETTER1_ID) > 0)
        {
            htmltext = "7071-01.htm";
            st.giveItems(LUKAS_LETTER_ID, 1);
            st.takeItems(MARIAS_LETTER1_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 7071 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && (st.getQuestItemsCount(MARIAS_LETTER2_ID) > 0 || st.getQuestItemsCount(CRETAS_LETTER1_ID) > 0 || st.getQuestItemsCount(LUCILLAS_HANDBAG_ID) > 0 || st.getQuestItemsCount(CRETAS_PAINTING1_ID) > 0 || st.getQuestItemsCount(LUKAS_LETTER_ID) > 0))
        {
            htmltext = "7071-02.htm";
        }
        else if (npcId == 7071 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_PAINTING2_ID) > 0)
        {
            htmltext = "7071-03.htm";
        }
        else if (npcId == 7071 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_PAINTING3_ID) > 0)
        {
            if (st.getQuestItemsCount(BROWN_SCROLL_SCRAP_ID) < 5)
            {
                htmltext = "7071-05.htm";
            }
            else
            {
                htmltext = "7071-06.htm";
            }
        }
        else if (npcId == 7071 && st.getInt("cond") == 1 && (st.getQuestItemsCount(SYMBOL_OF_SYLVAIN_ID) > 0 || st.getQuestItemsCount(MIRIENS_SIGIL2_ID) > 0 || st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 || st.getQuestItemsCount(CRYSTAL_OF_PURITY1_ID) > 0))
        {
            htmltext = "7071-07.htm";
        }
        else if (npcId == 7609 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(MARIAS_LETTER2_ID) > 0)
        {
            htmltext = "7609-01.htm";
        }
        else if (npcId == 7609 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_LETTER1_ID) > 0)
        {
            htmltext = "7609-06.htm";
        }
        else if (npcId == 7609 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && st.getQuestItemsCount(LUCILLAS_HANDBAG_ID) > 0)
        {
            htmltext = "7609-07.htm";
        }
        else if (npcId == 7609 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) > 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) > 0 && (st.getQuestItemsCount(CRETAS_PAINTING1_ID) > 0 || st.getQuestItemsCount(CRETAS_PAINTING2_ID) > 0 || st.getQuestItemsCount(CRETAS_PAINTING3_ID) > 0))
        {
            htmltext = "7609-10.htm";
        }
        else if (npcId == 7609 && st.getInt("cond") == 1 && (st.getQuestItemsCount(CRYSTAL_OF_PURITY1_ID) > 0 || st.getQuestItemsCount(SYMBOL_OF_SYLVAIN_ID) > 0 || st.getQuestItemsCount(MIRIENS_SIGIL2_ID) > 0))
        {
            htmltext = "7609-11.htm";
        }
        else if (npcId == 7609 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 && st.getQuestItemsCount(DIETERS_KEY_ID) > 0)
        {
            htmltext = "7609-12.htm";
        }
        else if (npcId == 7609 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) == 1 && st.getQuestItemsCount(DIETERS_KEY_ID) == 0)
        {
            htmltext = "7609-15.htm";
        }
        else if (npcId == 7610 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) == 1 && st.getQuestItemsCount(CRONOS_SIGIL_ID) == 0 && st.getQuestItemsCount(SYMBOL_OF_CRONOS_ID) == 0)
        {
            htmltext = "7610-01.htm";
        }
        else if (npcId == 7610 && st.getInt("cond") == 1 && (st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 || st.getQuestItemsCount(CRONOS_SIGIL_ID) > 0))
        {
            if (st.getQuestItemsCount(SCRIPTURE_CHAPTER_1_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_2_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_3_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_4_ID) > 0)
            {
                htmltext = "7610-12.htm";
            }
            else
            {
                htmltext = "7610-11.htm";
            }
        }
        else if (npcId == 7610 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) == 1 && st.getQuestItemsCount(SYMBOL_OF_CRONOS_ID) == 1 && st.getQuestItemsCount(CRONOS_SIGIL_ID) == 0)
        {
            htmltext = "7610-15.htm";
        }
        else if (npcId == 7111 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 && st.getQuestItemsCount(CRONOS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRONOS_LETTER_ID) > 0)
        {
            htmltext = "7111-01.htm";
        }
        else if (npcId == 7111 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 && st.getQuestItemsCount(CRONOS_SIGIL_ID) > 0 && st.getQuestItemsCount(DIETERS_KEY_ID) > 0)
        {
            htmltext = "7111-06.htm";
        }
        else if (npcId == 7111 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 && st.getQuestItemsCount(CRONOS_SIGIL_ID) > 0 && st.getQuestItemsCount(CRETAS_LETTER2_ID) > 0)
        {
            htmltext = "7111-07.htm";
        }
        else if (npcId == 7111 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 && st.getQuestItemsCount(CRONOS_SIGIL_ID) > 0 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(DIETERS_LETTER_ID) > 0)
        {
            htmltext = "7111-10.htm";
        }
        else if (npcId == 7111 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 && st.getQuestItemsCount(CRONOS_SIGIL_ID) > 0 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(RAUTS_LETTER_ENVELOPE_ID) > 0)
        {
            htmltext = "7111-11.htm";
        }
        else if (npcId == 7111 && st.getInt("cond") == 1 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) > 0 && st.getQuestItemsCount(CRONOS_SIGIL_ID) > 0 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(DIETERS_LETTER_ID) == 0 && st.getQuestItemsCount(RAUTS_LETTER_ENVELOPE_ID) == 0)
        {
            if (st.getQuestItemsCount(SCRIPTURE_CHAPTER_1_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_2_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_3_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_4_ID) > 0)
            {
                htmltext = "7111-13.htm";
            }
            else
            {
                htmltext = "7111-12.htm";
            }
        }
        else if (npcId == 7111 && st.getInt("cond") == 1 && st.getQuestItemsCount(SYMBOL_OF_CRONOS_ID) == 1)
        {
            htmltext = "7111-15.htm";
        }
        else if (npcId == 7230 && st.getInt("cond") == 1 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(DIETERS_LETTER_ID) > 0)
        {
            htmltext = "7230-01.htm";
        }
        else if (npcId == 7230 && st.getInt("cond") == 1 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(RAUTS_LETTER_ENVELOPE_ID) > 0)
        {
            htmltext = "7230-03.htm";
        }
        else if (npcId == 7230 && st.getInt("cond") == 1 && st.getQuestItemsCount(DIETERS_DIARY_ID) == 1 && (st.getQuestItemsCount(STRONG_LIQUOR_ID) > 0 || st.getQuestItemsCount(TRIFFS_RING_ID) > 0))
        {
            htmltext = "7230-04.htm";
        }
        else if (npcId == 7316 && st.getInt("cond") == 1 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(RAUTS_LETTER_ENVELOPE_ID) > 0)
        {
            htmltext = "7316-01.htm";
        }
        else if (npcId == 7316 && st.getInt("cond") == 1 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_1_ID) > 0 && st.getQuestItemsCount(STRONG_LIQUOR_ID) > 0)
        {
            htmltext = "7316-04.htm";
        }
        else if (npcId == 7316 && st.getInt("cond") == 1 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_1_ID) > 0 && st.getQuestItemsCount(TRIFFS_RING_ID) > 0)
        {
            htmltext = "7316-05.htm";
        }
        else if (npcId == 7611 && st.getInt("cond") == 1 && st.getQuestItemsCount(DIETERS_DIARY_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_1_ID) > 0 && st.getQuestItemsCount(STRONG_LIQUOR_ID) > 0)
        {
            htmltext = "7611-01.htm";
        }
        else if (npcId == 7611 && st.getInt("cond") == 1 && (st.getQuestItemsCount(TRIFFS_RING_ID) > 0 || st.getQuestItemsCount(SYMBOL_OF_CRONOS_ID) > 0))
        {
            htmltext = "7611-05.htm";
        }
        else if (npcId == 7103 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) == 1 && st.getQuestItemsCount(VALKONS_REQUEST_ID) == 0 && st.getQuestItemsCount(CRYSTAL_OF_PURITY2_ID) == 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_2_ID) == 0)
        {
            htmltext = "7103-01.htm";
        }
        else if (npcId == 7103 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) == 1 && st.getQuestItemsCount(VALKONS_REQUEST_ID) == 1 && st.getQuestItemsCount(CRYSTAL_OF_PURITY2_ID) == 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_2_ID) == 0)
        {
            htmltext = "7103-05.htm";
        }
        else if (npcId == 7103 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) == 1 && st.getQuestItemsCount(VALKONS_REQUEST_ID) == 0 && st.getQuestItemsCount(CRYSTAL_OF_PURITY2_ID) == 1 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_2_ID) == 0)
        {
            htmltext = "7103-06.htm";
            st.giveItems(SCRIPTURE_CHAPTER_2_ID, 1);
            st.takeItems(CRYSTAL_OF_PURITY2_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 7103 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) == 1 && st.getQuestItemsCount(VALKONS_REQUEST_ID) == 0 && st.getQuestItemsCount(CRYSTAL_OF_PURITY2_ID) == 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_2_ID) == 1)
        {
            htmltext = "7103-07.htm";
        }
        else if (npcId == 7458 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) == 1 && st.getQuestItemsCount(POITANS_NOTES_ID) == 0 && st.getQuestItemsCount(CASIANS_LIST_ID) == 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_4_ID) == 0)
        {
            htmltext = "7458-01.htm";
            st.giveItems(POITANS_NOTES_ID, 1);
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 7458 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) > 0 && st.getQuestItemsCount(POITANS_NOTES_ID) > 0 && st.getQuestItemsCount(CASIANS_LIST_ID) == 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_4_ID) == 0)
        {
            htmltext = "7458-02.htm";
        }
        else if (npcId == 7458 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) > 0 && st.getQuestItemsCount(POITANS_NOTES_ID) > 0 && st.getQuestItemsCount(CASIANS_LIST_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_4_ID) == 0)
        {
            htmltext = "7458-03.htm";
        }
        else if (npcId == 7458 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_4_ID) > 0 && st.getQuestItemsCount(POITANS_NOTES_ID) == 0 && st.getQuestItemsCount(CASIANS_LIST_ID) == 0)
        {
            htmltext = "7458-04.htm";
        }
        else if (npcId == 7612 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) > 0 && st.getQuestItemsCount(POITANS_NOTES_ID) > 0 && st.getQuestItemsCount(CASIANS_LIST_ID) == 0)
        {
            if (st.getQuestItemsCount(SCRIPTURE_CHAPTER_1_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_2_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_3_ID) > 0)
            {
                htmltext = "7612-02.htm";
            }
            else
            {
                htmltext = "7612-01.htm";
            }
        }
        else if (npcId == 7612 && st.getInt("cond") == 1 && st.getQuestItemsCount(TRIFFS_RING_ID) > 0 && st.getQuestItemsCount(POITANS_NOTES_ID) > 0 && st.getQuestItemsCount(CASIANS_LIST_ID) > 0)
        {
            if (st.getQuestItemsCount(GHOULS_SKIN_ID) + st.getQuestItemsCount(MEDUSAS_BLOOD_ID) + st.getQuestItemsCount(FETTEREDSOULS_ICHOR_ID) + st.getQuestItemsCount(ENCHT_GARGOYLES_NAIL_ID) < 32)
            {
                htmltext = "7612-05.htm";
            }
            else
            {
                htmltext = "7612-06.htm";
            }
        }
        else if (npcId == 7612 && st.getInt("cond") == 1 && st.getQuestItemsCount(POITANS_NOTES_ID) == 0 && st.getQuestItemsCount(CASIANS_LIST_ID) == 0 && st.getQuestItemsCount(TRIFFS_RING_ID) == 1 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_1_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_2_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_3_ID) > 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_4_ID) > 0)
        {
            htmltext = "7612-08.htm";
        }
        
        return htmltext;
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
        QuestState st = player.getQuestState(getName());
        if (st == null || st.getState() != State.STARTED)
        {
            return null;
        }
        
        int npcId = npc.getNpcId();
        if (npcId == 580)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(MIRIENS_SIGIL1_ID) != 0 && st.getQuestItemsCount(HIGHPRIESTS_SIGIL_ID) != 0 && st.getQuestItemsCount(CRETAS_PAINTING3_ID) != 0 && st.getQuestItemsCount(BROWN_SCROLL_SCRAP_ID) < 5)
            {
                if (st.getRandom(100) < 50)
                {
                    st.giveItems(BROWN_SCROLL_SCRAP_ID, 1);
                    if (st.getQuestItemsCount(BROWN_SCROLL_SCRAP_ID) < 5)
                    {
                        st.playSound("ItemSound.quest_itemget");
                    }
                    else
                    {
                        st.playSound("ItemSound.quest_middle");
                    }
                }
            }
        }
        else if (npcId == 68)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(MIRIENS_SIGIL2_ID) != 0 && st.getQuestItemsCount(GMAGISTERS_SIGIL_ID) != 0 && st.getQuestItemsCount(JUREKS_LIST_ID) != 0 && st.getQuestItemsCount(MEYEDESTROYERS_SKIN_ID) < 5)
            {
                if (st.getRandom(100) < 50)
                {
                    st.giveItems(MEYEDESTROYERS_SKIN_ID, 1);
                    if (st.getQuestItemsCount(MEYEDESTROYERS_SKIN_ID) < 5)
                    {
                        st.playSound("ItemSound.quest_itemget");
                    }
                    else
                    {
                        st.playSound("ItemSound.quest_middle");
                    }
                }
            }
        }
        else if (npcId == 269)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(MIRIENS_SIGIL2_ID) != 0 && st.getQuestItemsCount(GMAGISTERS_SIGIL_ID) != 0 && st.getQuestItemsCount(JUREKS_LIST_ID) != 0 && st.getQuestItemsCount(SHAMANS_NECKLACE_ID) < 5)
            {
                if (st.getRandom(100) < 50)
                {
                    st.giveItems(SHAMANS_NECKLACE_ID, 1);
                    if (st.getQuestItemsCount(SHAMANS_NECKLACE_ID) < 5)
                    {
                        st.playSound("ItemSound.quest_itemget");
                    }
                    else
                    {
                        st.playSound("ItemSound.quest_middle");
                    }
                }
            }
        }
        else if (npcId == 235)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(MIRIENS_SIGIL2_ID) != 0 && st.getQuestItemsCount(GMAGISTERS_SIGIL_ID) != 0 && st.getQuestItemsCount(JUREKS_LIST_ID) != 0 && st.getQuestItemsCount(SHACKLES_SCALP_ID) < 2)
            {
                st.giveItems(SHACKLES_SCALP_ID, 1);
                if (st.getQuestItemsCount(SHACKLES_SCALP_ID) < 2)
                {
                    st.playSound("ItemSound.quest_itemget");
                }
                else
                {
                    st.playSound("ItemSound.quest_middle");
                }
            }
        }
        else if (npcId == 554)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(MIRIENS_SIGIL3_ID) != 0 && st.getQuestItemsCount(CRONOS_SIGIL_ID) != 0 && st.getQuestItemsCount(TRIFFS_RING_ID) != 0 && st.getQuestItemsCount(SCRIPTURE_CHAPTER_3_ID) == 0)
            {
                if (st.getRandom(100) < 30)
                {
                    st.giveItems(SCRIPTURE_CHAPTER_3_ID, 1);
                    st.playSound("ItemSound.quest_middle");
                }
            }
        }
        else if (npcId == 201)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(TRIFFS_RING_ID) != 0 && st.getQuestItemsCount(POITANS_NOTES_ID) != 0 && st.getQuestItemsCount(CASIANS_LIST_ID) != 0 && st.getQuestItemsCount(GHOULS_SKIN_ID) < 10)
            {
                st.giveItems(GHOULS_SKIN_ID, 1);
                if (st.getQuestItemsCount(GHOULS_SKIN_ID) < 10)
                {
                    st.playSound("ItemSound.quest_itemget");
                }
                else
                {
                    st.playSound("ItemSound.quest_middle");
                }
            }
        }
        else if (npcId == 158)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(TRIFFS_RING_ID) != 0 && st.getQuestItemsCount(POITANS_NOTES_ID) != 0 && st.getQuestItemsCount(CASIANS_LIST_ID) != 0 && st.getQuestItemsCount(MEDUSAS_BLOOD_ID) < 12)
            {
                st.giveItems(MEDUSAS_BLOOD_ID, 1);
                if (st.getQuestItemsCount(MEDUSAS_BLOOD_ID) < 12)
                {
                    st.playSound("ItemSound.quest_itemget");
                }
                else
                {
                    st.playSound("ItemSound.quest_middle");
                }
            }
        }
        else if (npcId == 552)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(TRIFFS_RING_ID) != 0 && st.getQuestItemsCount(POITANS_NOTES_ID) != 0 && st.getQuestItemsCount(CASIANS_LIST_ID) != 0 && st.getQuestItemsCount(FETTEREDSOULS_ICHOR_ID) < 5)
            {
                st.giveItems(FETTEREDSOULS_ICHOR_ID, 1);
                if (st.getQuestItemsCount(FETTEREDSOULS_ICHOR_ID) < 5)
                {
                    st.playSound("ItemSound.quest_itemget");
                }
                else
                {
                    st.playSound("ItemSound.quest_middle");
                }
            }
        }
        else if (npcId == 567)
        {
            if (st.getInt("cond") != 0 && st.getQuestItemsCount(TRIFFS_RING_ID) != 0 && st.getQuestItemsCount(POITANS_NOTES_ID) != 0 && st.getQuestItemsCount(CASIANS_LIST_ID) != 0 && st.getQuestItemsCount(ENCHT_GARGOYLES_NAIL_ID) < 5)
            {
                st.giveItems(ENCHT_GARGOYLES_NAIL_ID, 1);
                if (st.getQuestItemsCount(ENCHT_GARGOYLES_NAIL_ID) < 5)
                {
                    st.playSound("ItemSound.quest_itemget");
                }
                else
                {
                    st.playSound("ItemSound.quest_middle");
                }
            }
        }
        
        return null;
    }
}
