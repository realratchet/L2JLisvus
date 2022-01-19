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
package quests.Q002_WhatWomenWant;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q002_WhatWomenWant extends Quest
{
    // NPCs
    private static final int ARUJIEN = 7223;
    private static final int MIRABEL = 7146;
    private static final int HERBIEL = 7150;
    private static final int GREENIS = 7157;
    
    // Items
    private static final int ARUJIEN_LETTER_1 = 1092;
    private static final int ARUJIEN_LETTER_2 = 1093;
    private static final int ARUJIEN_LETTER_3 = 1094;
    private static final int POETRY_BOOK = 689;
    private static final int GREENIS_LETTER = 693;
    
    // Rewards
    private static final int BEGINNERS_POTION = 1073;
    
    public static void main(String[] args)
    {
        new Q002_WhatWomenWant();
    }

    public Q002_WhatWomenWant()
    {
        super(2, Q002_WhatWomenWant.class.getSimpleName(), "What Women Want");
        
        setItemsIds(ARUJIEN_LETTER_1, ARUJIEN_LETTER_2, ARUJIEN_LETTER_3, POETRY_BOOK, GREENIS_LETTER);
        
        addStartNpc(ARUJIEN);
        addTalkId(ARUJIEN, MIRABEL, HERBIEL, GREENIS);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("7223-04.htm"))
        {
            st.setState(State.STARTED);
            st.set("cond", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
            st.giveItems(ARUJIEN_LETTER_1, 1);
        }
        else if (event.equalsIgnoreCase("7223-08.htm"))
        {
            st.set("cond", "4");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.takeItems(ARUJIEN_LETTER_3, 1);
            st.giveItems(POETRY_BOOK, 1);
        }
        else if (event.equalsIgnoreCase("7223-09.htm"))
        {
            st.takeItems(ARUJIEN_LETTER_3, 1);
            st.rewardItems(57, 450);
            st.playSound(QuestState.SOUND_FINISH);
            st.exitQuest(false);
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
                if (player.getRace() != Race.ELF && player.getRace() != Race.HUMAN)
                    htmltext = "7223-00.htm";
                else if (player.getLevel() < 2)
                    htmltext = "7223-01.htm";
                else
                    htmltext = "7223-02.htm";
                break;
            
            case State.STARTED:
                int cond = st.getInt("cond");
                switch (npc.getNpcId())
                {
                    case ARUJIEN:
                        if (st.hasQuestItems(ARUJIEN_LETTER_1))
                            htmltext = "7223-05.htm";
                        else if (st.hasQuestItems(ARUJIEN_LETTER_3))
                            htmltext = "7223-07.htm";
                        else if (st.hasQuestItems(ARUJIEN_LETTER_2))
                            htmltext = "7223-06.htm";
                        else if (st.hasQuestItems(POETRY_BOOK))
                            htmltext = "7223-11.htm";
                        else if (st.hasQuestItems(GREENIS_LETTER))
                        {
                            htmltext = "7223-10.htm";
                            st.takeItems(GREENIS_LETTER, 1);
                            st.giveItems(BEGINNERS_POTION, 5);
                            st.playSound(QuestState.SOUND_FINISH);
                            st.exitQuest(false);
                        }
                        break;
                    
                    case MIRABEL:
                        if (cond == 1)
                        {
                            htmltext = "7146-01.htm";
                            st.set("cond", "2");
                            st.playSound(QuestState.SOUND_MIDDLE);
                            st.takeItems(ARUJIEN_LETTER_1, 1);
                            st.giveItems(ARUJIEN_LETTER_2, 1);
                        }
                        else if (cond > 1)
                            htmltext = "7146-02.htm";
                        break;
                    
                    case HERBIEL:
                        if (cond == 2)
                        {
                            htmltext = "7150-01.htm";
                            st.set("cond", "3");
                            st.playSound(QuestState.SOUND_MIDDLE);
                            st.takeItems(ARUJIEN_LETTER_2, 1);
                            st.giveItems(ARUJIEN_LETTER_3, 1);
                        }
                        else if (cond > 2)
                            htmltext = "7150-02.htm";
                        break;
                    
                    case GREENIS:
                        if (cond < 4)
                            htmltext = "7157-01.htm";
                        else if (cond == 4)
                        {
                            htmltext = "7157-02.htm";
                            st.set("cond", "5");
                            st.playSound(QuestState.SOUND_MIDDLE);
                            st.takeItems(POETRY_BOOK, 1);
                            st.giveItems(GREENIS_LETTER, 1);
                        }
                        else if (cond == 5)
                            htmltext = "7157-03.htm";
                        break;
                }
                break;
            
            case State.COMPLETED:
                htmltext = getAlreadyCompletedMsg();
                break;
        }
        
        return htmltext;
    }
}