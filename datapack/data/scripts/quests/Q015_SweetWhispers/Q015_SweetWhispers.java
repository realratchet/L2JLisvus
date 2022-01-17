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
package quests.Q015_SweetWhispers;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q015_SweetWhispers extends Quest
{
    // NPCs
    private static final int VLADIMIR = 8302;
    private static final int HIERARCH = 8517;
    private static final int MYSTERIOUS_NECRO = 8518;
    
    public static void main(String[] args)
    {
        new Q015_SweetWhispers();
    }

    public Q015_SweetWhispers()
    {
        super(15, Q015_SweetWhispers.class.getSimpleName(), "Sweet Whispers");
        
        addStartNpc(VLADIMIR);
        addTalkId(VLADIMIR, HIERARCH, MYSTERIOUS_NECRO);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("8302-01.htm"))
        {
            st.setState(State.STARTED);
            st.set("cond", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
        }
        else if (event.equalsIgnoreCase("8518-01.htm"))
        {
            st.set("cond", "2");
            st.playSound(QuestState.SOUND_MIDDLE);
        }
        else if (event.equalsIgnoreCase("8517-01.htm"))
        {
            st.addExpAndSp(60217, 0);
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
                htmltext = (player.getLevel() < 60) ? "8302-00a.htm" : "8302-00.htm";
                break;
            
            case State.STARTED:
                int cond = st.getInt("cond");
                switch (npc.getNpcId())
                {
                    case VLADIMIR:
                        htmltext = "8302-01a.htm";
                        break;
                    
                    case MYSTERIOUS_NECRO:
                        if (cond == 1)
                            htmltext = "8518-00.htm";
                        else if (cond == 2)
                            htmltext = "8518-01a.htm";
                        break;
                    
                    case HIERARCH:
                        if (cond == 2)
                            htmltext = "8517-00.htm";
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
