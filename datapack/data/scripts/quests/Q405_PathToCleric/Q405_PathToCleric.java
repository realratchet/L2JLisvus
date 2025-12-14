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
package quests.Q405_PathToCleric;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Quest 405 Path to Cleric
 * @author Karakan
 */
public class Q405_PathToCleric extends Quest
{
    private static final int LETTER_OF_ORDER1 = 1191;
    private static final int LETTER_OF_ORDER2 = 1192;
    private static final int BOOK_OF_LIONEL = 1193;
    private static final int BOOK_OF_VIVI = 1194;
    private static final int BOOK_OF_SIMLON = 1195;
    private static final int BOOK_OF_PRAGA = 1196;
    private static final int CERTIFICATE_OF_GALLINT = 1197;
    private static final int PENDANT_OF_MOTHER = 1198;
    private static final int NECKLACE_OF_MOTHER = 1199;
    private static final int LIONEL_COVENANT = 1200;
    private static final int MARK_OF_FAITH = 1201;
    
    public static void main(String[] args)
    {
        new Q405_PathToCleric();
    }
    
    public Q405_PathToCleric()
    {
        super(405, Q405_PathToCleric.class.getSimpleName(), "Path to Cleric");
        
        setItemsIds(LETTER_OF_ORDER1, LETTER_OF_ORDER2, BOOK_OF_LIONEL, BOOK_OF_VIVI, BOOK_OF_SIMLON, BOOK_OF_PRAGA, CERTIFICATE_OF_GALLINT, PENDANT_OF_MOTHER, NECKLACE_OF_MOTHER, LIONEL_COVENANT);
        
        addStartNpc(7022);
        addTalkId(7017, 7022, 7030, 7253, 7333, 7408);
        addKillId(26, 29);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        QuestState st = player.getQuestState(getName());
        String htmltext = event;
        if (st == null)
            return htmltext;
        
        if (event.equals("1"))
        {
            if (st.getPlayer().getLevel() >= 19 && st.getPlayer().getClassId().getId() == 0x0a && st.getQuestItemsCount(MARK_OF_FAITH) == 0)
            {
                st.set("cond", "1");
                st.setState(State.STARTED);
                st.playSound(QuestState.SOUND_ACCEPT);
                st.giveItems(LETTER_OF_ORDER1, 1);
                htmltext = "7022-05.htm";
            }
            else if (st.getPlayer().getClassId().getId() != 0x0a)
            {
                htmltext = (st.getPlayer().getClassId().getId() == 0x0f) ? "7022-02a.htm" : "7022-02.htm";
            }
            else if (st.getPlayer().getLevel() < 19 && st.getPlayer().getClassId().getId() == 0x0a)
            {
                htmltext = "7022-03.htm";
            }
            else if (st.getPlayer().getLevel() >= 19 && st.getPlayer().getClassId().getId() == 0x0a && st.getQuestItemsCount(MARK_OF_FAITH) == 1)
            {
                htmltext = "7022-04.htm";
            }
        }
        return htmltext;
    }
    
    @Override
    public String onTalk(L2NpcInstance npc, L2PcInstance player)
    {
        QuestState st = player.getQuestState(getName());
        String htmltext = getNoQuestMsg();
        if (st == null)
            return htmltext;
        
        int id = st.getState();
        int npcId = npc.getNpcId();
        int cond = st.getInt("cond");
        
        int letterOfOrder1Count = st.getQuestItemsCount(LETTER_OF_ORDER1);
        int letterOfOrder2Count = st.getQuestItemsCount(LETTER_OF_ORDER2);
        int lionelCovenantCount = st.getQuestItemsCount(LIONEL_COVENANT);
        int bookOfViviCount = st.getQuestItemsCount(BOOK_OF_VIVI);
        int bookOfSimlonCount = st.getQuestItemsCount(BOOK_OF_SIMLON);
        int bookOfPragaCount = st.getQuestItemsCount(BOOK_OF_PRAGA);
        int necklaceOfMotherCount = st.getQuestItemsCount(NECKLACE_OF_MOTHER);
        int pendantOfMotherCount = st.getQuestItemsCount(PENDANT_OF_MOTHER);
        int bookOfLionelCount = st.getQuestItemsCount(BOOK_OF_LIONEL);
        int certificateOfGallintCount = st.getQuestItemsCount(CERTIFICATE_OF_GALLINT);
        
        switch (npcId)
        {
            case 7022:
                if (id == State.COMPLETED)
                {
                    htmltext = getAlreadyCompletedMsg();
                }
                else if (cond == 0)
                {
                    htmltext = (st.getQuestItemsCount(MARK_OF_FAITH) == 0) ? "7022-01.htm" : "7022-04.htm";
                }
                else if (cond > 0)
                {
                    if (letterOfOrder2Count == 1 && lionelCovenantCount == 0)
                    {
                        htmltext = "7022-07.htm";
                    }
                    else if (letterOfOrder1Count == 1)
                    {
                        if (bookOfViviCount == 1 && bookOfSimlonCount > 0 && bookOfPragaCount == 1)
                        {
                            htmltext = "7022-08.htm";
                            st.takeItems(BOOK_OF_PRAGA, 1);
                            st.takeItems(BOOK_OF_VIVI, 1);
                            st.takeItems(BOOK_OF_SIMLON, 3);
                            st.takeItems(LETTER_OF_ORDER1, 1);
                            st.giveItems(LETTER_OF_ORDER2, 1);
                            st.set("cond", "3");
                        }
                        else
                        {
                            htmltext = "7022-06.htm";
                        }
                    }
                    else if (letterOfOrder2Count == 1 && lionelCovenantCount == 1)
                    {
                        htmltext = "7022-09.htm";
                        st.takeItems(LETTER_OF_ORDER2, 1);
                        st.takeItems(LIONEL_COVENANT, 1);
                        st.giveItems(MARK_OF_FAITH, 1);
                        st.set("cond", "0");
                        st.setState(State.COMPLETED);
                        st.playSound(QuestState.SOUND_FINISH);
                    }
                }
                break;
            
            case 7253:
                if (cond > 0 && letterOfOrder1Count == 1)
                {
                    if (bookOfSimlonCount == 0)
                    {
                        htmltext = "7253-01.htm";
                        st.giveItems(BOOK_OF_SIMLON, 3);
                    }
                    else
                    {
                        htmltext = "7253-02.htm";
                    }
                }
                break;
            
            case 7030:
                if (cond > 0 && letterOfOrder1Count == 1)
                {
                    if (bookOfViviCount == 0)
                    {
                        htmltext = "7030-01.htm";
                        st.giveItems(BOOK_OF_VIVI, 1);
                    }
                    else if (bookOfViviCount == 1)
                    {
                        htmltext = "7030-02.htm";
                    }
                }
                break;
            
            case 7333:
                if (cond > 0 && letterOfOrder1Count == 1)
                {
                    if (bookOfPragaCount == 0 && necklaceOfMotherCount == 0)
                    {
                        htmltext = "7333-01.htm";
                        st.giveItems(NECKLACE_OF_MOTHER, 1);
                    }
                    else if (bookOfPragaCount == 0 && necklaceOfMotherCount == 1 && pendantOfMotherCount == 0)
                    {
                        htmltext = "7333-02.htm";
                    }
                    else if (bookOfPragaCount == 0 && necklaceOfMotherCount == 1 && pendantOfMotherCount == 1)
                    {
                        htmltext = "7333-03.htm";
                        st.takeItems(NECKLACE_OF_MOTHER, 1);
                        st.takeItems(PENDANT_OF_MOTHER, 1);
                        st.giveItems(BOOK_OF_PRAGA, 1);
                        st.set("cond", "2");
                    }
                    else if (bookOfPragaCount > 0)
                    {
                        htmltext = "7333-04.htm";
                    }
                }
                break;
            
            case 7408:
                if (cond > 0)
                {
                    if (letterOfOrder2Count == 0)
                    {
                        htmltext = "7408-02.htm";
                    }
                    else if (letterOfOrder2Count == 1 && bookOfLionelCount == 0 && lionelCovenantCount == 0 && certificateOfGallintCount == 0)
                    {
                        htmltext = "7408-01.htm";
                        st.giveItems(BOOK_OF_LIONEL, 1);
                        st.set("cond", "4");
                    }
                    else if (letterOfOrder2Count == 1 && bookOfLionelCount == 1 && lionelCovenantCount == 0 && certificateOfGallintCount == 0)
                    {
                        htmltext = "7408-03.htm";
                    }
                    else if (letterOfOrder2Count == 1 && bookOfLionelCount == 0 && lionelCovenantCount == 0 && certificateOfGallintCount == 1)
                    {
                        htmltext = "7408-04.htm";
                        st.takeItems(CERTIFICATE_OF_GALLINT, 1);
                        st.giveItems(LIONEL_COVENANT, 1);
                        st.set("cond", "6");
                    }
                    else if (letterOfOrder2Count == 1 && bookOfLionelCount == 0 && lionelCovenantCount == 1 && certificateOfGallintCount == 0)
                    {
                        htmltext = "7408-05.htm";
                    }
                }
                break;
            
            case 7017:
                if (cond > 0 && letterOfOrder2Count == 1 && lionelCovenantCount == 0)
                {
                    if (bookOfLionelCount == 1 && certificateOfGallintCount == 0)
                    {
                        htmltext = "7017-01.htm";
                        st.takeItems(BOOK_OF_LIONEL, 1);
                        st.giveItems(CERTIFICATE_OF_GALLINT, 1);
                        st.set("cond", "5");
                    }
                    else if (bookOfLionelCount == 0 && certificateOfGallintCount == 1)
                    {
                        htmltext = "7017-02.htm";
                    }
                }
                break;
        }
        
        return htmltext;
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
    {
        final QuestState st = checkPlayerState(killer, npc, State.STARTED);
        int npcId = npc.getNpcId();
        if (npcId == 26 || npcId == 29)
        {
            if (st.getInt("cond") > 0 && st.getQuestItemsCount(PENDANT_OF_MOTHER) == 0)
            {
                st.giveItems(PENDANT_OF_MOTHER, 1);
                st.playSound(QuestState.SOUND_MIDDLE);
            }
        }
        return null;
    }
}