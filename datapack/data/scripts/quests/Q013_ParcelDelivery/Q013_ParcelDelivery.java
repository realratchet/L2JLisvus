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
package quests.Q013_ParcelDelivery;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q013_ParcelDelivery extends Quest
{
    // NPCs
    private static final int FUNDIN = 8274;
    private static final int VULCAN = 8539;
    
    // Item
    private static final int PACKAGE = 7263;

    public static void main(String[] args)
    {
        new Q013_ParcelDelivery();
    }
    
    public Q013_ParcelDelivery()
    {
        super(13, Q013_ParcelDelivery.class.getSimpleName(), "Parcel Delivery");
        
        setItemsIds(PACKAGE);
        
        addStartNpc(FUNDIN);
        addTalkId(FUNDIN, VULCAN);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("8274-2.htm"))
        {
            st.setState(State.STARTED);
            st.set("cond", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
            st.giveItems(PACKAGE, 1);
        }
        else if (event.equalsIgnoreCase("8539-1.htm"))
        {
            st.takeItems(PACKAGE, 1);
            st.rewardItems(57, 82656);
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
                htmltext = (player.getLevel() < 74) ? "8274-1.htm" : "8274-0.htm";
                break;
            
            case State.STARTED:
                switch (npc.getNpcId())
                {
                    case FUNDIN:
                        htmltext = "8274-2.htm";
                        break;
                    
                    case VULCAN:
                        htmltext = "8539-0.htm";
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
