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
package quests.Q212_TrialOfDuty;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Made by Mr. Have fun! Version 0.2
 * Fixed by Artful (http://L2PLanet.ru Lineage2 C3 Server)
 */
public class Q212_TrialOfDuty extends Quest
{
    // Items
    private static final int MARK_OF_DUTY_ID = 2633;
    private static final int LETTER_OF_DUSTIN_ID = 2634;
    private static final int KNIGHTS_TEAR_ID = 2635;
    private static final int MIRROR_OF_ORPIC_ID = 2636;
    private static final int TEAR_OF_CONFESSION_ID = 2637;
    private static final int REPORT_PIECE_ID = 2638;
    private static final int TALIANUSS_REPORT_ID = 2639;
    private static final int TEAR_OF_LOYALTY_ID = 2640;
    private static final int MILITAS_ARTICLE_ID = 2641;
    private static final int SAINTS_ASHES_URN_ID = 2642;
    private static final int ATEBALTS_SKULL_ID = 2643;
    private static final int ATEBALTS_RIBS_ID = 2644;
    private static final int ATEBALTS_SHIN_ID = 2645;
    private static final int LETTER_OF_WINDAWOOD_ID = 2646;
    private static final int OLD_KNIGHT_SWORD_ID = 3027;
    
    // NPCs
    private static final int NPC_DUSTIN = 7109;
    private static final int NPC_7116 = 7116;
    private static final int NPC_7311 = 7311;
    private static final int NPC_7653 = 7653;
    private static final int NPC_7654 = 7654;
    private static final int NPC_7655 = 7655;
    private static final int NPC_7656 = 7656;
    
    // Mobs
    private static final int MOB_144 = 144;
    private static final int MOB_190 = 190;
    private static final int MOB_191 = 191;
    private static final int MOB_200 = 200;
    private static final int MOB_201 = 201;
    private static final int MOB_270 = 270;
    private static final int MOB_5119 = 5119;
    private static final int MOB_577 = 577;
    private static final int MOB_578 = 578;
    private static final int MOB_579 = 579;
    private static final int MOB_580 = 580;
    private static final int MOB_581 = 581;
    private static final int MOB_582 = 582;
    
    public Q212_TrialOfDuty()
    {
        super(212, Q212_TrialOfDuty.class.getSimpleName(), "Trial Of Duty");
        
        setItemsIds(LETTER_OF_DUSTIN_ID, KNIGHTS_TEAR_ID, MIRROR_OF_ORPIC_ID, TEAR_OF_CONFESSION_ID, REPORT_PIECE_ID, TALIANUSS_REPORT_ID, TEAR_OF_LOYALTY_ID, MILITAS_ARTICLE_ID, SAINTS_ASHES_URN_ID, ATEBALTS_SKULL_ID, ATEBALTS_RIBS_ID, ATEBALTS_SHIN_ID, LETTER_OF_WINDAWOOD_ID, OLD_KNIGHT_SWORD_ID);
        
        addStartNpc(NPC_DUSTIN);
        addTalkId(NPC_DUSTIN, NPC_7116, NPC_7311, NPC_7653, NPC_7654, NPC_7655, NPC_7656);
        addKillId(MOB_144, MOB_190, MOB_191, MOB_200, MOB_201, MOB_270, MOB_5119, MOB_577, MOB_578, MOB_579, MOB_580, MOB_581, MOB_582);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return null;
        
        String htmltext = event;
        
        switch (event)
        {
            case "1":
                htmltext = "7109-04.htm";
                st.setState(State.STARTED);
                st.playSound("ItemSound.quest_accept");
                st.set("cond", "1");
                break;
            case "7116_1":
                htmltext = "7116-02.htm";
                break;
            case "7116_2":
                htmltext = "7116-03.htm";
                break;
            case "7116_3":
                htmltext = "7116-04.htm";
                break;
            case "7116_4":
                htmltext = "7116-05.htm";
                st.takeItems(TEAR_OF_LOYALTY_ID, 1);
                st.set("cond", "14");
                break;
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
        int cond = st.getInt("cond");
        
        if (st.getState() == State.CREATED)
        {
            st.set("cond", "0");
            st.set("onlyone", "0");
            st.set("id", "0");
        }
        
        // Dustin
        if (npcId == NPC_DUSTIN)
        {
            if (cond == 0 && st.getInt("onlyone") == 0)
            {
                int cls = player.getClassId().ordinal();
                if (cls == 0x04 || cls == 0x13 || cls == 0x20)
                {
                    if (player.getLevel() >= 35)
                        htmltext = "7109-03.htm";
                    else
                    {
                        htmltext = "7109-01.htm";
                        st.exitQuest(true);
                    }
                }
                else
                {
                    htmltext = "7109-02.htm";
                    st.exitQuest(true);
                }
            }
            else if (cond == 18 && st.getQuestItemsCount(LETTER_OF_DUSTIN_ID) > 0)
            {
                st.addExpAndSp(79832, 3750);
                st.takeItems(LETTER_OF_DUSTIN_ID, 1);
                st.giveItems(MARK_OF_DUTY_ID, 1);
                st.giveItems(7562, 8);
                st.setState(State.COMPLETED);
                st.playSound("ItemSound.quest_finish");
                st.set("onlyone", "1");
                st.set("cond", "0");
                htmltext = "7109-05.htm";
            }
            else if (cond == 1)
                htmltext = "7109-04.htm";
            else if (st.getInt("onlyone") == 1)
                htmltext = getAlreadyCompletedMsg();
        }
        
        // NPC 7653
        else if (npcId == NPC_7653)
        {
            switch (cond)
            {
                case 1:
                    htmltext = "7653-01.htm";
                    if (st.getQuestItemsCount(OLD_KNIGHT_SWORD_ID) == 0)
                        st.giveItems(OLD_KNIGHT_SWORD_ID, 1);
                    st.set("cond", "2");
                    break;
                case 2:
                    if (st.getQuestItemsCount(KNIGHTS_TEAR_ID) == 0)
                        htmltext = "7653-02.htm";
                    break;
                case 3:
                    if (st.getQuestItemsCount(KNIGHTS_TEAR_ID) > 0 && st.getQuestItemsCount(OLD_KNIGHT_SWORD_ID) > 0)
                    {
                        htmltext = "7653-03.htm";
                        st.takeItems(KNIGHTS_TEAR_ID, 1);
                        st.takeItems(OLD_KNIGHT_SWORD_ID, 1);
                        st.set("cond", "4");
                    }
                    break;
                case 4:
                    htmltext = "7653-04.htm";
                    break;
            }
        }
        
        // NPC 7654
        else if (npcId == NPC_7654)
        {
            switch (cond)
            {
                case 4:
                    htmltext = "7654-01.htm";
                    st.set("cond", "5");
                    break;
                case 5:
                    if (st.getQuestItemsCount(TALIANUSS_REPORT_ID) == 0)
                        htmltext = "7654-02.htm";
                    break;
                case 6:
                    if (st.getQuestItemsCount(TALIANUSS_REPORT_ID) > 0)
                    {
                        htmltext = "7654-03.htm";
                        st.giveItems(MIRROR_OF_ORPIC_ID, 1);
                        st.set("cond", "7");
                    }
                    break;
                case 7:
                    htmltext = "7654-04.htm";
                    break;
                case 9:
                    if (st.getQuestItemsCount(TEAR_OF_CONFESSION_ID) > 0)
                    {
                        htmltext = "7654-05.htm";
                        st.takeItems(TEAR_OF_CONFESSION_ID, 1);
                        st.set("cond", "10");
                    }
                    break;
                case 10:
                    htmltext = "7654-06.htm";
                    break;
            }
        }
        
        // NPC 7656
        else if (npcId == NPC_7656 && cond == 8 && st.getQuestItemsCount(MIRROR_OF_ORPIC_ID) > 0)
        {
            htmltext = "7656-01.htm";
            st.takeItems(MIRROR_OF_ORPIC_ID, 1);
            st.takeItems(TALIANUSS_REPORT_ID, 1);
            st.giveItems(TEAR_OF_CONFESSION_ID, 1);
            st.set("cond", "9");
        }
        
        // NPC 7655
        else if (npcId == NPC_7655)
        {
            switch (cond)
            {
                case 10:
                    if (player.getLevel() >= 36)
                    {
                        htmltext = "7655-02.htm";
                        st.set("cond", "11");
                    }
                    else
                        htmltext = "7655-01.htm";
                    break;
                case 11:
                    htmltext = "7655-03.htm";
                    break;
                case 12:
                    htmltext = "7655-04.htm";
                    st.takeItems(MILITAS_ARTICLE_ID, st.getQuestItemsCount(MILITAS_ARTICLE_ID));
                    st.giveItems(TEAR_OF_LOYALTY_ID, 1);
                    st.set("cond", "13");
                    break;
                case 13:
                    htmltext = "7655-05.htm";
                    break;
            }
        }
        
        // NPC 7116
        else if (npcId == NPC_7116)
        {
            switch (cond)
            {
                case 13:
                    if (st.getQuestItemsCount(TEAR_OF_LOYALTY_ID) > 0)
                        htmltext = "7116-01.htm";
                    break;
                case 14:
                    if (!(st.getQuestItemsCount(ATEBALTS_SKULL_ID) > 0 && st.getQuestItemsCount(ATEBALTS_RIBS_ID) > 0 && st.getQuestItemsCount(ATEBALTS_SHIN_ID) > 0))
                        htmltext = "7116-06.htm";
                    break;
                case 15:
                    htmltext = "7116-07.htm";
                    st.takeItems(ATEBALTS_SKULL_ID, 1);
                    st.takeItems(ATEBALTS_RIBS_ID, 1);
                    st.takeItems(ATEBALTS_SHIN_ID, 1);
                    st.giveItems(SAINTS_ASHES_URN_ID, 1);
                    st.set("cond", "16");
                    break;
                case 16:
                    htmltext = "7116-09.htm";
                    break;
                case 17:
                    if (st.getQuestItemsCount(LETTER_OF_WINDAWOOD_ID) > 0)
                    {
                        htmltext = "7116-08.htm";
                        st.takeItems(LETTER_OF_WINDAWOOD_ID, 1);
                        st.giveItems(LETTER_OF_DUSTIN_ID, 1);
                        st.set("cond", "18");
                    }
                    break;
                case 18:
                    htmltext = "7116-10.htm";
                    break;
            }
        }
        
        // NPC 7311
        else if (npcId == NPC_7311)
        {
            switch (cond)
            {
                case 16:
                    if (st.getQuestItemsCount(SAINTS_ASHES_URN_ID) > 0)
                    {
                        htmltext = "7311-01.htm";
                        st.takeItems(SAINTS_ASHES_URN_ID, 1);
                        st.giveItems(LETTER_OF_WINDAWOOD_ID, 1);
                        st.set("cond", "17");
                    }
                    break;
                case 17:
                    htmltext = "7311-02.htm";
                    break;
            }
        }
        
        return htmltext;
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
        QuestState st = player.getQuestState(getName());
        if (st == null || st.getState() != State.STARTED)
            return null;
        
        int npcId = npc.getNpcId();
        
        if ((npcId == MOB_190 || npcId == MOB_191) && st.getInt("cond") == 2 && st.getRandom(50) < 2)
        {
            st.addSpawn(MOB_5119, npc, true, 0);
            st.playSound("Itemsound.quest_before_battle");
        }
        else if (npcId == MOB_5119 && st.getInt("cond") == 2 && player.getActiveWeaponItem() != null && player.getActiveWeaponItem().getItemId() == OLD_KNIGHT_SWORD_ID && st.getQuestItemsCount(OLD_KNIGHT_SWORD_ID) > 0)
        {
            st.giveItems(KNIGHTS_TEAR_ID, 1);
            st.playSound("ItemSound.quest_middle");
            st.set("cond", "3");
        }
        else if ((npcId == MOB_200 || npcId == MOB_201) && st.getInt("cond") == 5 && st.getQuestItemsCount(REPORT_PIECE_ID) < 10 && st.getQuestItemsCount(TALIANUSS_REPORT_ID) == 0)
        {
            if (st.getQuestItemsCount(REPORT_PIECE_ID) == 9)
            {
                if (st.getRandom(2) == 1)
                {
                    st.takeItems(REPORT_PIECE_ID, st.getQuestItemsCount(REPORT_PIECE_ID));
                    st.giveItems(TALIANUSS_REPORT_ID, 1);
                    st.set("cond", "6");
                    st.playSound("ItemSound.quest_middle");
                }
            }
            else if (st.getRandom(2) == 1)
            {
                st.giveItems(REPORT_PIECE_ID, 1);
                st.playSound("ItemSound.quest_itemget");
            }
        }
        else if (npcId == MOB_144 && st.getInt("cond") == 7 && st.getRandom(100) < 33)
        {
            st.addSpawn(NPC_7656, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
            st.set("cond", "8");
            st.playSound("ItemSound.quest_middle");
        }
        else if ((npcId >= MOB_577 && npcId <= MOB_582) && st.getInt("cond") == 11 && st.getQuestItemsCount(MILITAS_ARTICLE_ID) < 20)
        {
            if (st.getQuestItemsCount(MILITAS_ARTICLE_ID) == 19)
            {
                st.giveItems(MILITAS_ARTICLE_ID, 1);
                st.set("cond", "12");
                st.playSound("ItemSound.quest_middle");
            }
            else
            {
                st.giveItems(MILITAS_ARTICLE_ID, 1);
                st.playSound("ItemSound.quest_itemget");
            }
        }
        else if (npcId == MOB_270 && st.getInt("cond") == 14 && st.getRandom(2) == 1)
        {
            if (st.getQuestItemsCount(ATEBALTS_SKULL_ID) == 0)
                st.giveItems(ATEBALTS_SKULL_ID, 1);
            else if (st.getQuestItemsCount(ATEBALTS_RIBS_ID) == 0)
                st.giveItems(ATEBALTS_RIBS_ID, 1);
            else if (st.getQuestItemsCount(ATEBALTS_SHIN_ID) == 0)
            {
                st.giveItems(ATEBALTS_SHIN_ID, 1);
                st.set("cond", "15");
            }
            st.playSound("ItemSound.quest_itemget");
        }
        
        return null;
    }
    
    public static void main(String[] args)
    {
        new Q212_TrialOfDuty();
    }
}