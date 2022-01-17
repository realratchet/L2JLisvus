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
package quests.Q007_ATripBegins;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q007_ATripBegins extends Quest
{
    // NPCs
    private static final int MIRABEL = 7146;
    private static final int ARIEL = 7148;
    private static final int ASTERIOS = 7154;
    
    // Items
    private static final int ARIEL_RECO = 7572;
    
    // Rewards
    private static final int MARK_TRAVELER = 7570;
    private static final int SOE_GIRAN = 7559;

    public static void main(String[] args)
    {
        new Q007_ATripBegins();
    }
    
    public Q007_ATripBegins()
    {
        super(7, Q007_ATripBegins.class.getSimpleName(), "A Trip Begins");
        
        setItemsIds(ARIEL_RECO);
        
        addStartNpc(MIRABEL);
        addTalkId(MIRABEL, ARIEL, ASTERIOS);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("7146-03.htm"))
        {
            st.setState(State.STARTED);
            st.set("cond", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
        }
        else if (event.equalsIgnoreCase("7148-02.htm"))
        {
            st.set("cond", "2");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.giveItems(ARIEL_RECO, 1);
        }
        else if (event.equalsIgnoreCase("7154-02.htm"))
        {
            st.set("cond", "3");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.takeItems(ARIEL_RECO, 1);
        }
        else if (event.equalsIgnoreCase("7146-06.htm"))
        {
            st.giveItems(MARK_TRAVELER, 1);
            st.rewardItems(SOE_GIRAN, 1);
            st.playSound(QuestState.SOUND_FINISH);
            st.exitQuest(false);
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
        
        switch (st.getState())
        {
            case State.CREATED:
                if (player.getRace() != Race.ELF)
                    htmltext = "7146-01.htm";
                else if (player.getLevel() < 3)
                    htmltext = "7146-01a.htm";
                else
                    htmltext = "7146-02.htm";
                break;
            
            case State.STARTED:
                int cond = st.getInt("cond");
                switch (npc.getNpcId())
                {
                    case MIRABEL:
                        if (cond == 1 || cond == 2)
                            htmltext = "7146-04.htm";
                        else if (cond == 3)
                            htmltext = "7146-05.htm";
                        break;
                    
                    case ARIEL:
                        if (cond == 1)
                            htmltext = "7148-01.htm";
                        else if (cond == 2)
                            htmltext = "7148-03.htm";
                        break;
                    
                    case ASTERIOS:
                        if (cond == 2)
                            htmltext = "7154-01.htm";
                        else if (cond == 3)
                            htmltext = "7154-03.htm";
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
