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
package quests.Q014_WhereaboutsOfTheArchaeologist;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q014_WhereaboutsOfTheArchaeologist extends Quest
{
    // NPCs
    private static final int LIESEL = 8263;
    private static final int GHOST_OF_ADVENTURER = 8538;
    
    // Items
    private static final int LETTER = 7253;

    public static void main(String[] args)
    {
        new Q014_WhereaboutsOfTheArchaeologist();
    }
    
    public Q014_WhereaboutsOfTheArchaeologist()
    {
        super(14, Q014_WhereaboutsOfTheArchaeologist.class.getSimpleName(), "Whereabouts of the Archaeologist");
        
        setItemsIds(LETTER);
        
        addStartNpc(LIESEL);
        addTalkId(LIESEL, GHOST_OF_ADVENTURER);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("8263-2.htm"))
        {
            st.setState(State.STARTED);
            st.set("cond", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
            st.giveItems(LETTER, 1);
        }
        else if (event.equalsIgnoreCase("8538-1.htm"))
        {
            st.takeItems(LETTER, 1);
            st.rewardItems(57, 113228);
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
                htmltext = (player.getLevel() < 74) ? "8263-1.htm" : "8263-0.htm";
                break;
            
            case State.STARTED:
                switch (npc.getNpcId())
                {
                    case LIESEL:
                        htmltext = "8263-2.htm";
                        break;
                    
                    case GHOST_OF_ADVENTURER:
                        htmltext = "8538-0.htm";
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
