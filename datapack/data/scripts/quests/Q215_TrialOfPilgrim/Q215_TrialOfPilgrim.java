package quests.Q215_TrialOfPilgrim;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Made by Mr. Have fun! Version 0.2
 * Updated by ElgarL
 * Improved a lil' bit by DrLecter
 */
public class Q215_TrialOfPilgrim extends Quest
{
    // Item IDs
    private static final int MARK_OF_PILGRIM_ID = 2721;
    private static final int BOOK_OF_SAGE_ID = 2722;
    private static final int VOUCHER_OF_TRIAL_ID = 2723;
    private static final int SPIRIT_OF_FLAME_ID = 2724;
    private static final int ESSENSE_OF_FLAME_ID = 2725;
    private static final int BOOK_OF_GERALD_ID = 2726;
    private static final int GREY_BADGE_ID = 2727;
    private static final int PICTURE_OF_NAHIR_ID = 2728;
    private static final int HAIR_OF_NAHIR_ID = 2729;
    private static final int STATUE_OF_EINHASAD_ID = 2730;
    private static final int BOOK_OF_DARKNESS_ID = 2731;
    private static final int DEBRIS_OF_WILLOW_ID = 2732;
    private static final int TAG_OF_RUMOR_ID = 2733;
    
    // NPC IDs
    private static final int[] NPCS =
    {
        7036,
        7117,
        7362,
        7550,
        7571,
        7612,
        7648,
        7649,
        7650,
        7651,
        7652
    };
    
    // Monster IDs
    private static final int[] MOBS =
    {
        5116,
        5117,
        5118
    };

    public static void main(String[] args)
    {
        new Q215_TrialOfPilgrim();
    }
    
    public Q215_TrialOfPilgrim()
    {
        super(215, Q215_TrialOfPilgrim.class.getSimpleName(), "Trial Of Pilgrim");
        
        addStartNpc(7648);
        
        for (int npc : NPCS)
            addTalkId(npc);
        
        for (int mob : MOBS)
            addKillId(mob);
        
        setItemsIds(BOOK_OF_SAGE_ID, VOUCHER_OF_TRIAL_ID, SPIRIT_OF_FLAME_ID, ESSENSE_OF_FLAME_ID, BOOK_OF_GERALD_ID, GREY_BADGE_ID, PICTURE_OF_NAHIR_ID, HAIR_OF_NAHIR_ID, STATUE_OF_EINHASAD_ID, BOOK_OF_DARKNESS_ID, DEBRIS_OF_WILLOW_ID, TAG_OF_RUMOR_ID);
    }
    
    @Override
    public String onEvent(String event, QuestState st)
    {
        String htmltext = event;
        
        if (event.equals("1"))
        {
            htmltext = "7648-04.htm";
            st.set("cond", "1");
            st.setState(State.STARTED);
            st.playSound("ItemSound.quest_accept");
            st.giveItems(VOUCHER_OF_TRIAL_ID, 1);
        }
        else if (event.equals("7648_1"))
        {
            htmltext = "7648-05.htm";
        }
        else if (event.equals("7648_2"))
        {
            htmltext = "7648-06.htm";
        }
        else if (event.equals("7648_3"))
        {
            htmltext = "7648-07.htm";
        }
        else if (event.equals("7648_4"))
        {
            htmltext = "7648-08.htm";
        }
        else if (event.equals("7648_5"))
        {
            htmltext = "7648-05.htm";
        }
        else if (event.equals("7649_1"))
        {
            htmltext = "7649-04.htm";
            st.giveItems(SPIRIT_OF_FLAME_ID, 1);
            st.takeItems(ESSENSE_OF_FLAME_ID, 1);
            st.set("cond", "5");
        }
        else if (event.equals("7650_1"))
        {
            int adenaRequired = 100000 * (int) Config.RATE_DROP_ADENA;
            
            if (st.getQuestItemsCount(Inventory.ADENA_ID) >= adenaRequired)
            {
                htmltext = "7650-02.htm";
                st.giveItems(BOOK_OF_GERALD_ID, 1);
                st.takeItems(Inventory.ADENA_ID, adenaRequired);
                st.set("cond", "7");
            }
            else
            {
                htmltext = "7650-03.htm";
            }
        }
        else if (event.equals("7650_2"))
        {
            htmltext = "7650-03.htm";
        }
        else if (event.equals("7362_1"))
        {
            htmltext = "7362-05.htm";
            st.takeItems(BOOK_OF_DARKNESS_ID, 1);
            st.set("cond", "16");
        }
        else if (event.equals("7362_2"))
        {
            htmltext = "7362-04.htm";
            st.set("cond", "16");
        }
        else if (event.equals("7652_1"))
        {
            htmltext = "7652-02.htm";
            st.giveItems(BOOK_OF_DARKNESS_ID, 1);
            st.takeItems(DEBRIS_OF_WILLOW_ID, 1);
            st.set("cond", "15");
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
        
        if (state == State.COMPLETED)
        {
            htmltext = getAlreadyCompletedMsg();
        }
        else if (npcId == 7648 && st.getInt("cond") == 0)
        {
            int classId = player.getClassId().getId();
            
            if (classId == 0x0f || classId == 0x1d || classId == 0x2a || classId == 0x32)
            {
                if (player.getLevel() >= 35)
                {
                    htmltext = "7648-03.htm";
                }
                else
                {
                    htmltext = "7648-01.htm";
                    st.exitQuest(true);
                }
            }
            else
            {
                htmltext = "7648-02.htm";
                st.exitQuest(true);
            }
        }
        else if (npcId == 7648 && st.getInt("cond") == 1 && st.getQuestItemsCount(VOUCHER_OF_TRIAL_ID) > 0)
        {
            htmltext = "7648-09.htm";
        }
        else if (npcId == 7648 && st.getInt("cond") == 17 && st.getQuestItemsCount(BOOK_OF_SAGE_ID) > 0)
        {
            st.addExpAndSp(77832, 16000);
            st.giveItems(7562, 8);
            htmltext = "7648-10.htm";
            st.takeItems(BOOK_OF_SAGE_ID, 1);
            st.giveItems(7562, 8);
            st.giveItems(MARK_OF_PILGRIM_ID, 1);
            st.setState(State.COMPLETED);
            st.playSound("ItemSound.quest_finish");
            st.set("cond", "0");
        }
        else if (npcId == 7571 && st.getInt("cond") == 1 && st.getQuestItemsCount(VOUCHER_OF_TRIAL_ID) > 0)
        {
            htmltext = "7571-01.htm";
            st.takeItems(VOUCHER_OF_TRIAL_ID, 1);
            st.set("cond", "2");
        }
        else if (npcId == 7571 && st.getInt("cond") == 2)
        {
            htmltext = "7571-02.htm";
        }
        else if (npcId == 7571 && st.getInt("cond") == 5 && st.getQuestItemsCount(SPIRIT_OF_FLAME_ID) > 0)
        {
            htmltext = "7571-03.htm";
        }
        else if (npcId == 7649 && st.getInt("cond") == 2)
        {
            htmltext = "7649-01.htm";
            st.set("cond", "3");
        }
        else if (npcId == 7649 && st.getInt("cond") == 3)
        {
            htmltext = "7649-02.htm";
        }
        else if (npcId == 7649 && st.getInt("cond") == 4 && st.getQuestItemsCount(ESSENSE_OF_FLAME_ID) > 0)
        {
            htmltext = "7649-03.htm";
        }
        else if (npcId == 7550 && st.getInt("cond") == 5 && st.getQuestItemsCount(SPIRIT_OF_FLAME_ID) > 0)
        {
            htmltext = "7550-01.htm";
            st.giveItems(TAG_OF_RUMOR_ID, 1);
            st.set("cond", "6");
        }
        else if (npcId == 7550 && st.getInt("cond") == 6)
        {
            htmltext = "7550-02.htm";
        }
        else if (npcId == 7650 && st.getInt("cond") == 6 && st.getQuestItemsCount(TAG_OF_RUMOR_ID) > 0)
        {
            int requiredAdena = 100000 * (int) Config.RATE_DROP_ADENA;
            htmltext = st.showHtmlFile("7650-01.htm").replace("RequiredAdena", String.valueOf(requiredAdena));
        }
        else if (npcId == 7650 && st.getInt("cond") >= 8 && st.getQuestItemsCount(GREY_BADGE_ID) > 0 && st.getQuestItemsCount(BOOK_OF_GERALD_ID) > 0)
        {
            htmltext = "7650-04.htm";
            
            int rate = (int) Config.RATE_QUEST_REWARD;
            if (rate == 0)
                rate = 1;
            
            st.giveItems(Inventory.ADENA_ID, (100000 * (int) Config.RATE_DROP_ADENA) / rate);
            st.takeItems(BOOK_OF_GERALD_ID, 1);
        }
        else if (npcId == 7651 && st.getInt("cond") == 6 && st.getQuestItemsCount(TAG_OF_RUMOR_ID) > 0)
        {
            htmltext = "7651-01.htm";
            st.giveItems(GREY_BADGE_ID, 1);
            st.takeItems(TAG_OF_RUMOR_ID, 1);
            st.set("cond", "8");
        }
        else if (npcId == 7651 && st.getInt("cond") == 7 && st.getQuestItemsCount(TAG_OF_RUMOR_ID) > 0)
        {
            htmltext = "7651-02.htm";
            st.giveItems(GREY_BADGE_ID, 1);
            st.takeItems(TAG_OF_RUMOR_ID, 1);
            st.set("cond", "8");
        }
        else if (npcId == 7651 && st.getInt("cond") == 8)
        {
            htmltext = "7651-03.htm";
        }
        else if (npcId == 7117 && st.getInt("cond") == 8)
        {
            htmltext = "7117-01.htm";
            st.set("cond", "9");
        }
        else if (npcId == 7117 && st.getInt("cond") == 9)
        {
            htmltext = "7117-02.htm";
        }
        else if (npcId == 7036 && st.getInt("cond") == 9)
        {
            htmltext = "7036-01.htm";
            st.giveItems(PICTURE_OF_NAHIR_ID, 1);
            st.set("cond", "10");
        }
        else if (npcId == 7036 && st.getInt("cond") == 10)
        {
            htmltext = "7036-02.htm";
        }
        else if (npcId == 7036 && st.getInt("cond") == 11)
        {
            htmltext = "7036-03.htm";
            st.giveItems(STATUE_OF_EINHASAD_ID, 1);
            st.takeItems(PICTURE_OF_NAHIR_ID, 1);
            st.takeItems(HAIR_OF_NAHIR_ID, 1);
            st.set("cond", "12");
        }
        else if (npcId == 7036 && st.getInt("cond") == 12 && st.getQuestItemsCount(STATUE_OF_EINHASAD_ID) > 0)
        {
            htmltext = "7036-04.htm";
        }
        else if (npcId == 7362 && st.getInt("cond") == 12)
        {
            htmltext = "7362-01.htm";
            st.set("cond", "13");
        }
        else if (npcId == 7362 && st.getInt("cond") == 13)
        {
            htmltext = "7362-02.htm";
        }
        else if (npcId == 7362 && st.getInt("cond") == 15 && st.getQuestItemsCount(BOOK_OF_DARKNESS_ID) > 0)
        {
            htmltext = "7362-03.htm";
        }
        else if (npcId == 7362 && st.getInt("cond") == 16)
        {
            htmltext = "7362-06.htm";
        }
        else if (npcId == 7362 && st.getInt("cond") == 15 && st.getQuestItemsCount(BOOK_OF_DARKNESS_ID) == 0)
        {
            htmltext = "7362-07.htm";
        }
        else if (npcId == 7652 && st.getInt("cond") == 14 && st.getQuestItemsCount(DEBRIS_OF_WILLOW_ID) > 0)
        {
            htmltext = "7652-01.htm";
        }
        else if (npcId == 7652 && st.getInt("cond") == 15 && st.getQuestItemsCount(BOOK_OF_DARKNESS_ID) > 0)
        {
            htmltext = "7652-03.htm";
        }
        else if (npcId == 7612 && st.getInt("cond") == 16)
        {
            htmltext = "7612-01.htm";
            st.giveItems(BOOK_OF_SAGE_ID, 1);
            
            if (st.getQuestItemsCount(BOOK_OF_DARKNESS_ID) > 0)
                st.takeItems(BOOK_OF_DARKNESS_ID, 1);
            
            st.set("cond", "17");
            st.takeItems(GREY_BADGE_ID, 1);
            st.takeItems(SPIRIT_OF_FLAME_ID, 1);
            st.takeItems(STATUE_OF_EINHASAD_ID, 1);
        }
        else if (npcId == 7612 && st.getInt("cond") == 17)
        {
            htmltext = "7612-02.htm";
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
        int cond = st.getInt("cond");
        
        if (npcId == 5116 && cond == 3 && !st.hasQuestItems(ESSENSE_OF_FLAME_ID))
        {
            if (st.getRandom(5) == 1)
            {
                st.giveItems(ESSENSE_OF_FLAME_ID, 1);
                st.set("cond", "4");
                st.playSound("ItemSound.quest_middle");
            }
        }
        else if (npcId == 5117 && cond == 10 && !st.hasQuestItems(HAIR_OF_NAHIR_ID))
        {
            st.giveItems(HAIR_OF_NAHIR_ID, 1);
            st.set("cond", "11");
            st.playSound("ItemSound.quest_middle");
        }
        else if (npcId == 5118 && cond == 13 && !st.hasQuestItems(DEBRIS_OF_WILLOW_ID))
        {
            if (st.getRandom(5) == 1)
            {
                st.giveItems(DEBRIS_OF_WILLOW_ID, 1);
                st.set("cond", "14");
                st.playSound("ItemSound.quest_middle");
            }
        }
        return null;
    }
}