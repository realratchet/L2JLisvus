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
package quests.Q016_TheComingDarkness;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q016_TheComingDarkness extends Quest
{
    // NPCs
    private static final int HIERARCH = 8517;
    private static final int EVIL_ALTAR_1 = 8512;
    private static final int EVIL_ALTAR_2 = 8513;
    private static final int EVIL_ALTAR_3 = 8514;
    private static final int EVIL_ALTAR_4 = 8515;
    private static final int EVIL_ALTAR_5 = 8516;
    
    // Item
    private static final int CRYSTAL_OF_SEAL = 7167;
    
    public static void main(String[] args)
    {
        new Q016_TheComingDarkness();
    }

    public Q016_TheComingDarkness()
    {
        super(16, Q016_TheComingDarkness.class.getSimpleName(), "The Coming Darkness");
        
        setItemsIds(CRYSTAL_OF_SEAL);
        
        addStartNpc(HIERARCH);
        addTalkId(HIERARCH, EVIL_ALTAR_1, EVIL_ALTAR_2, EVIL_ALTAR_3, EVIL_ALTAR_4, EVIL_ALTAR_5);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("8517-2.htm"))
        {
            st.setState(State.STARTED);
            st.set("cond", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
            st.giveItems(CRYSTAL_OF_SEAL, 5);
        }
        else if (event.equalsIgnoreCase("8512-1.htm"))
        {
            st.set("cond", "2");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.takeItems(CRYSTAL_OF_SEAL, 1);
        }
        else if (event.equalsIgnoreCase("8513-1.htm"))
        {
            st.set("cond", "3");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.takeItems(CRYSTAL_OF_SEAL, 1);
        }
        else if (event.equalsIgnoreCase("8514-1.htm"))
        {
            st.set("cond", "4");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.takeItems(CRYSTAL_OF_SEAL, 1);
        }
        else if (event.equalsIgnoreCase("8515-1.htm"))
        {
            st.set("cond", "5");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.takeItems(CRYSTAL_OF_SEAL, 1);
        }
        else if (event.equalsIgnoreCase("8516-1.htm"))
        {
            st.set("cond", "6");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.takeItems(CRYSTAL_OF_SEAL, 1);
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
                htmltext = (player.getLevel() < 62) ? "8517-0a.htm" : "8517-0.htm";
                break;
            
            case State.STARTED:
                int cond = st.getInt("cond");
                int npcId = npc.getNpcId();
                
                switch (npcId)
                {
                    case HIERARCH:
                        if (cond == 6)
                        {
                            htmltext = "8517-4.htm";
                            st.addExpAndSp(221958, 0);
                            st.playSound(QuestState.SOUND_FINISH);
                            st.exitQuest(false);
                        }
                        else
                        {
                            if (st.hasQuestItems(CRYSTAL_OF_SEAL))
                                htmltext = "8517-3.htm";
                            else
                            {
                                htmltext = "8517-3a.htm";
                                st.exitQuest(true);
                            }
                        }
                        break;
                    
                    case EVIL_ALTAR_1:
                    case EVIL_ALTAR_2:
                    case EVIL_ALTAR_3:
                    case EVIL_ALTAR_4:
                    case EVIL_ALTAR_5:
                        final int condAltar = npcId - 8511;
                        
                        if (cond == condAltar)
                        {
                            if (st.hasQuestItems(CRYSTAL_OF_SEAL))
                                htmltext = npcId + "-0.htm";
                            else
                                htmltext = "altar_nocrystal.htm";
                        }
                        else if (cond > condAltar)
                            htmltext = npcId + "-2.htm";
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
