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
package quests.Q003_WillTheSealBeBroken;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q003_WillTheSealBeBroken extends Quest
{
    // Items
    private static final int ONYX_BEAST_EYE = 1081;
    private static final int TAINT_STONE = 1082;
    private static final int SUCCUBUS_BLOOD = 1083;
    
    public static void main(String[] args)
    {
        new Q003_WillTheSealBeBroken();
    }

    public Q003_WillTheSealBeBroken()
    {
        super(3, Q003_WillTheSealBeBroken.class.getSimpleName(), "Will the Seal be Broken?");
        
        setItemsIds(ONYX_BEAST_EYE, TAINT_STONE, SUCCUBUS_BLOOD);
        
        addStartNpc(7141); // Talloth
        addTalkId(7141);
        
        addKillId(31, 41, 46, 48, 52, 57);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("7141-03.htm"))
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
        QuestState st = player.getQuestState(getName());
        String htmltext = getNoQuestMsg();
        if (st == null)
            return htmltext;
        
        switch (st.getState())
        {
            case State.CREATED:
                if (player.getRace() != Race.DARK_ELF)
                    htmltext = "7141-00.htm";
                else if (player.getLevel() < 16)
                    htmltext = "7141-01.htm";
                else
                    htmltext = "7141-02.htm";
                break;
            
            case State.STARTED:
                int cond = st.getInt("cond");
                if (cond == 1)
                    htmltext = "7141-04.htm";
                else if (cond == 2)
                {
                    htmltext = "7141-06.htm";
                    st.takeItems(ONYX_BEAST_EYE, 1);
                    st.takeItems(SUCCUBUS_BLOOD, 1);
                    st.takeItems(TAINT_STONE, 1);
                    st.giveItems(57, 4900);
                    st.addExpAndSp(5000, 0);
                    st.playSound(QuestState.SOUND_FINISH);
                    st.exitQuest(false);
                }
                break;
            
            case State.COMPLETED:
                htmltext = getAlreadyCompletedMsg();
                break;
        }
        
        return htmltext;
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
    {
        final QuestState st = checkPlayerCondition(killer, npc, "cond", "1");
        if (st == null)
            return null;
        
        switch (npc.getNpcId())
        {
            case 31:
                if (st.dropItemsAlways(ONYX_BEAST_EYE, 1, 1) && st.hasQuestItems(TAINT_STONE, SUCCUBUS_BLOOD))
                    st.set("cond", "2");
                break;
            case 41:
            case 46:
                if (st.dropItemsAlways(TAINT_STONE, 1, 1) && st.hasQuestItems(ONYX_BEAST_EYE, SUCCUBUS_BLOOD))
                    st.set("cond", "2");
                break;
            case 48:
            case 52:
            case 57:
                if (st.dropItemsAlways(SUCCUBUS_BLOOD, 1, 1) && st.hasQuestItems(ONYX_BEAST_EYE, TAINT_STONE))
                    st.set("cond", "2");
                break;
        }
        
        return null;
    }
}
