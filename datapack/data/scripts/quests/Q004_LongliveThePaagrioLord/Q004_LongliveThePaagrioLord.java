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
package quests.Q004_LongliveThePaagrioLord;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q004_LongliveThePaagrioLord extends Quest
{
    private static final Map<Integer, Integer> NPC_GIFTS = new HashMap<>();
    {
        NPC_GIFTS.put(7585, 1542);
        NPC_GIFTS.put(7566, 1541);
        NPC_GIFTS.put(7562, 1543);
        NPC_GIFTS.put(7560, 1544);
        NPC_GIFTS.put(7559, 1545);
        NPC_GIFTS.put(7587, 1546);
    }

    public static void main(String[] args)
    {
        new Q004_LongliveThePaagrioLord();
    }
    
    public Q004_LongliveThePaagrioLord()
    {
        super(4, Q004_LongliveThePaagrioLord.class.getSimpleName(), "Long live the Pa'agrio Lord!");
        
        setItemsIds(1541, 1542, 1543, 1544, 1545, 1546);
        
        addStartNpc(7578); // Nakusin
        addTalkId(7578, 7585, 7566, 7562, 7560, 7559, 7587);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("7578-03.htm"))
        {
            st.setState(State.STARTED);
            st.set("cond", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
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
                if (player.getRace() != Race.ORC)
                    htmltext = "7578-00.htm";
                else if (player.getLevel() < 2)
                    htmltext = "7578-01.htm";
                else
                    htmltext = "7578-02.htm";
                break;
            
            case State.STARTED:
                int cond = st.getInt("cond");
                int npcId = npc.getNpcId();
                
                if (npcId == 7578)
                {
                    if (cond == 1)
                        htmltext = "7578-04.htm";
                    else if (cond == 2)
                    {
                        htmltext = "7578-06.htm";
                        st.giveItems(4, 1);
                        for (int item : NPC_GIFTS.values())
                            st.takeItems(item, -1);
                        
                        st.playSound(QuestState.SOUND_FINISH);
                        st.exitQuest(false);
                    }
                }
                else
                {
                    int i = NPC_GIFTS.get(npcId);
                    if (st.hasQuestItems(i))
                        htmltext = npcId + "-02.htm";
                    else
                    {
                        st.giveItems(i, 1);
                        htmltext = npcId + "-01.htm";
                        
                        int count = 0;
                        for (int item : NPC_GIFTS.values())
                            count += st.getQuestItemsCount(item);
                        
                        if (count == 6)
                        {
                            st.set("cond", "2");
                            st.playSound(QuestState.SOUND_MIDDLE);
                        }
                        else
                            st.playSound(QuestState.SOUND_ITEMGET);
                    }
                }
                break;
            
            case State.COMPLETED:
                htmltext = getAlreadyCompletedMsg();
                break;
        }
        
        return htmltext;
    }
}
