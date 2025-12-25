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
package quests.Q024_InhabitantsOfTheForestOfTheDead;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author Kerberos (adapted for L2JLisvus by roko91)
 */
public class Q024_InhabitantsOfTheForestOfTheDead extends Quest
{
    // Npcs
    private static final int DORIAN = 8389;
    private static final int WIZARD = 8522;
    private static final int TOMBSTONE = 8531;
    private static final int MAID_OF_LIDIA = 8532;
    
    // Items
    private static final int LETTER = 7065;
    private static final int HAIRPIN = 7148;
    private static final int TOTEM = 7151;
    private static final int FLOWER = 7152;
    private static final int SILVER_CROSS = 7153;
    private static final int BROKEN_SILVER_CROSS = 7154;
    private static final int SUSPICIOUS_TOTEM = 7156;
    
    private static final int[] MOBS = new int[]
    {
        1557,
        1558,
        1560,
        1563,
        1564,
        1565,
        1566,
        1567
    };
    
    public static void main(String[] args)
    {
        new Q024_InhabitantsOfTheForestOfTheDead();
    }
    
    public Q024_InhabitantsOfTheForestOfTheDead()
    {
        super(24, Q024_InhabitantsOfTheForestOfTheDead.class.getSimpleName(), "Inhabitants of the Forest of the Dead");
        
        setItemsIds(FLOWER, SILVER_CROSS, BROKEN_SILVER_CROSS, LETTER, HAIRPIN, TOTEM);
        
        addStartNpc(DORIAN);
        addTalkId(DORIAN, TOMBSTONE, MAID_OF_LIDIA, WIZARD);
        
        addCreatureSeeId(10332);
        addKillId(MOBS);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = event;
        
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return htmltext;
        
        if (event.equals("8389-02.htm"))
        {
            st.giveItems(FLOWER, 1);
            st.set("cond", "1");
            st.playSound("ItemSound.quest_accept");
            st.setState(State.STARTED);
        }
        else if (event.equals("8389-11.htm"))
        {
            st.set("cond", "3");
            st.playSound("ItemSound.quest_middle");
            st.giveItems(SILVER_CROSS, 1);
        }
        else if (event.equals("8389-16.htm"))
        {
            st.playSound("InterfaceSound.charstat_open_01");
        }
        else if (event.equals("8389-17.htm"))
        {
            st.takeItems(BROKEN_SILVER_CROSS, -1);
            st.giveItems(HAIRPIN, 1);
            st.set("cond", "5");
        }
        else if (event.equals("8522-03.htm"))
        {
            st.takeItems(TOTEM, -1);
        }
        else if (event.equals("8522-07.htm"))
        {
            st.set("cond", "11");
        }
        else if (event.equals("8522-19.htm"))
        {
            st.giveItems(SUSPICIOUS_TOTEM, 1);
            st.addExpAndSp(242105, 22529);
            st.unset("cond");
            st.setState(State.COMPLETED);
            st.playSound("ItemSound.quest_finish");
        }
        else if (event.equals("8531-02.htm"))
        {
            st.playSound("ItemSound.quest_middle");
            st.set("cond", "2");
            st.takeItems(FLOWER, -1);
        }
        else if (event.equals("8532-04.htm"))
        {
            st.playSound("ItemSound.quest_middle");
            st.giveItems(LETTER, 1);
            st.set("cond", "6");
        }
        else if (event.equals("8532-06.htm"))
        {
            st.takeItems(HAIRPIN, -1);
            st.takeItems(LETTER, -1);
        }
        else if (event.equals("8532-16.htm"))
        {
            st.playSound("ItemSound.quest_middle");
            st.set("cond", "9");
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
        
        int npcId = npc.getNpcId();
        byte state = st.getState();
        
        if (state == State.COMPLETED)
        {
            if (npcId == WIZARD)
                htmltext = "8522-20.htm";
            else
                htmltext = getAlreadyCompletedMsg();
            return htmltext;
        }
        
        int cond = st.getInt("cond");
        
        if (npcId == DORIAN)
        {
            if (state == State.CREATED)
            {
                QuestState st2 = player.getQuestState("Q023_LidiasHeart");
                if (st2 != null && st2.getState() == State.COMPLETED && player.getLevel() >= 65)
                {
                    htmltext = "8389-01.htm";
                }
                else
                {
                    htmltext = "8389-00.htm";
                    st.exitQuest(true);
                }
            }
            else if (cond == 1)
            {
                htmltext = "8389-03.htm";
            }
            else if (cond == 2)
            {
                htmltext = "8389-04.htm";
            }
            else if (cond == 3)
            {
                htmltext = "8389-12.htm";
            }
            else if (cond == 4)
            {
                htmltext = "8389-13.htm";
            }
            else if (cond == 5)
            {
                htmltext = "8389-18.htm";
            }
        }
        else if (npcId == TOMBSTONE)
        {
            if (cond == 1)
            {
                st.playSound("AmdSound.d_wind_loot_02");
                htmltext = "8531-01.htm";
            }
            else if (cond == 2)
            {
                htmltext = "8531-03.htm";
            }
        }
        else if (npcId == MAID_OF_LIDIA)
        {
            if (cond == 5)
            {
                htmltext = "8532-01.htm";
            }
            else if (cond == 6)
            {
                if (st.getQuestItemsCount(LETTER) > 0 && st.getQuestItemsCount(HAIRPIN) > 0)
                    htmltext = "8532-05.htm";
                else
                    htmltext = "8532-07.htm";
            }
            else if (cond == 9)
            {
                htmltext = "8532-16.htm";
            }
        }
        else if (npcId == WIZARD)
        {
            if (cond == 10)
            {
                htmltext = "8522-01.htm";
            }
            else if (cond == 11)
            {
                htmltext = "8522-08.htm";
            }
        }
        
        return htmltext;
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
        QuestState st = player.getQuestState(getName());
        if (st == null)
            return null;
        
        if (st.getState() != State.STARTED)
            return null;
        
        int npcId = npc.getNpcId();
        
        if (st.getQuestItemsCount(TOTEM) == 0 && st.getInt("cond") == 9)
        {
            if (Util.contains(MOBS, npcId) && Rnd.get(100) < 30)
            {
                st.giveItems(TOMBSTONE, 1);
                st.set("cond", "10");
                st.playSound("ItemSound.quest_middle");
            }
        }
        
        return null;
    }
    
    @Override
    public String onCreatureSee(L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
        if (npc.getNpcId() == 10332)
        {
            QuestState st = player.getQuestState(getName());
            if (st != null && st.getQuestItemsCount(SILVER_CROSS) > 0)
            {
                st.takeItems(SILVER_CROSS, -1);
                st.giveItems(BROKEN_SILVER_CROSS, 1);
                st.set("cond", "4");
                npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "That sign!"));
            }
        }

        return null;
    }
}
