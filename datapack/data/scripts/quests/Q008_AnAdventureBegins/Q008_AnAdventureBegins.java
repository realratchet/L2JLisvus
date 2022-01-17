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
package quests.Q008_AnAdventureBegins;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q008_AnAdventureBegins extends Quest
{
    // NPCs
    private static final int JASMINE = 7134;
    private static final int ROSELYN = 7355;
    private static final int HARNE = 7144;
    
    // Items
    private static final int ROSELYN_NOTE = 7573;
    
    // Rewards
    private static final int SOE_GIRAN = 7559;
    private static final int MARK_TRAVELER = 7570;
    
    public static void main(String[] args)
    {
        new Q008_AnAdventureBegins();
    }

    public Q008_AnAdventureBegins()
    {
        super(8, Q008_AnAdventureBegins.class.getSimpleName(), "An Adventure Begins");
        
        setItemsIds(ROSELYN_NOTE);
        
        addStartNpc(JASMINE);
        addTalkId(JASMINE, ROSELYN, HARNE);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("7134-03.htm"))
        {
            st.setState(State.STARTED);
            st.set("cond", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
        }
        else if (event.equalsIgnoreCase("7355-02.htm"))
        {
            st.set("cond", "2");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.giveItems(ROSELYN_NOTE, 1);
        }
        else if (event.equalsIgnoreCase("7144-02.htm"))
        {
            st.set("cond", "3");
            st.playSound(QuestState.SOUND_MIDDLE);
            st.takeItems(ROSELYN_NOTE, 1);
        }
        else if (event.equalsIgnoreCase("7134-06.htm"))
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
                if (player.getLevel() >= 3 && player.getRace() == Race.DARK_ELF)
                    htmltext = "7134-02.htm";
                else
                    htmltext = "7134-01.htm";
                break;
            
            case State.STARTED:
                int cond = st.getInt("cond");
                switch (npc.getNpcId())
                {
                    case JASMINE:
                        if (cond == 1 || cond == 2)
                            htmltext = "7134-04.htm";
                        else if (cond == 3)
                            htmltext = "7134-05.htm";
                        break;
                    
                    case ROSELYN:
                        if (cond == 1)
                            htmltext = "7355-01.htm";
                        else if (cond == 2)
                            htmltext = "7355-03.htm";
                        break;
                    
                    case HARNE:
                        if (cond == 2)
                            htmltext = "7144-01.htm";
                        else if (cond == 3)
                            htmltext = "7144-03.htm";
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
