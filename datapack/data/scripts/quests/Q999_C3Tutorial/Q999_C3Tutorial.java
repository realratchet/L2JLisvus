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
package quests.Q999_C3Tutorial;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q999_C3Tutorial extends Quest
{
    private static final int RECOMMENDATION_01 = 1067;
    private static final int RECOMMENDATION_02 = 1068;
    private static final int LEAF_OF_MOTHERTREE = 1069;
    private static final int BLOOD_OF_JUNDIN = 1070;
    private static final int LICENSE_OF_MINER = 1498;
    private static final int VOUCHER_OF_FLAME = 1496;
    private static final int SOULSHOT_NOVICE = 5789;
    private static final int SPIRITSHOT_NOVICE = 5790;
    private static final int BLUE_GEM = 6353;

    private static final int[] TALK_IDS = { 7008, 7009, 7017, 7019, 7129, 7131, 7404, 7056, 7011, 7012, 7401, 7403, 7402, 7018, 7021, 7020, 7574, 7370, 7400, 7528, 7530, 7573, 7575, 7600, 7601, 7602, 7598, 7599 };
    
    private static final Map<String, Object[]> EVENTS = new HashMap<>();
    private static final Map<Integer, Object[]> TALKS = new HashMap<>();
    
    static
    {
        EVENTS.put("7008_02", new Object[] { "7008-03.htm", -84058, 243239, -3730, RECOMMENDATION_01, 0x00, SOULSHOT_NOVICE, 200, 0, 0, 0 });
        EVENTS.put("7017_02", new Object[] { "7017-03.htm", -84058, 243239, -3730, RECOMMENDATION_02, 0x0a, SPIRITSHOT_NOVICE, 100, 0, 0, 0 });
        EVENTS.put("7370_02", new Object[] { "7370-03.htm", 45491, 48359, -3086, LEAF_OF_MOTHERTREE, 0x19, SPIRITSHOT_NOVICE, 100, 0x12, SOULSHOT_NOVICE, 200 });
        EVENTS.put("7129_02", new Object[] { "7129-03.htm", 12116, 16666, -4610, BLOOD_OF_JUNDIN, 0x26, SPIRITSHOT_NOVICE, 100, 0x1f, SOULSHOT_NOVICE, 200 });
        EVENTS.put("7528_02", new Object[] { "7528-03.htm", 115642, -178046, -941, LICENSE_OF_MINER, 0x35, SOULSHOT_NOVICE, 200, 0, 0, 0 });
        EVENTS.put("7573_02", new Object[] { "7573-03.htm", -45067, -113549, -235, VOUCHER_OF_FLAME, 0x31, SPIRITSHOT_NOVICE, 100, 0x2c, SOULSHOT_NOVICE, 200 });
        TALKS.put(7017, new Object[] { 0, new String[] { "7017-01.htm", "7017-02.htm", "7017-04.htm" }, 0, 0 });
        TALKS.put(7008, new Object[] { 0, new String[] { "7008-01.htm", "7008-02.htm", "7008-04.htm" }, 0, 0 });
        TALKS.put(7370, new Object[] { 1, new String[] { "7370-01.htm", "7370-02.htm", "7370-04.htm" }, 0, 0 });
        TALKS.put(7129, new Object[] { 2, new String[] { "7129-01.htm", "7129-02.htm", "7129-04.htm" }, 0, 0 });
        TALKS.put(7573, new Object[] { 3, new String[] { "7573-01.htm", "7573-02.htm", "7573-04.htm" }, 0, 0 });
        TALKS.put(7528, new Object[] { 4, new String[] { "7528-01.htm", "7528-02.htm", "7528-04.htm" }, 0, 0 });
        TALKS.put(7018, new Object[] { 0, new String[] { "7131-01.htm", null, "7019-03a.htm", "7019-04.htm" }, 1, RECOMMENDATION_02 });
        TALKS.put(7019, new Object[] { 0, new String[] { "7131-01.htm", null, "7019-03a.htm", "7019-04.htm" }, 1, RECOMMENDATION_02 });
        TALKS.put(7020, new Object[] { 0, new String[] { "7131-01.htm", null, "7019-03a.htm", "7019-04.htm" }, 1, RECOMMENDATION_02 });
        TALKS.put(7021, new Object[] { 0, new String[] { "7131-01.htm", null, "7019-03a.htm", "7019-04.htm" }, 1, RECOMMENDATION_02 });
        TALKS.put(7009, new Object[] { 0, new String[] { "7530-01.htm", "7009-03.htm", null, "7009-04.htm" }, 1, RECOMMENDATION_01 });
        TALKS.put(7011, new Object[] { 0, new String[] { "7530-01.htm", "7009-03.htm", null, "7009-04.htm" }, 1, RECOMMENDATION_01 });
        TALKS.put(7012, new Object[] { 0, new String[] { "7530-01.htm", "7009-03.htm", null, "7009-04.htm" }, 1, RECOMMENDATION_01 });
        TALKS.put(7056, new Object[] { 0, new String[] { "7530-01.htm", "7009-03.htm", null, "7009-04.htm" }, 1, RECOMMENDATION_01 });
        TALKS.put(7400, new Object[] { 1, new String[] { "7131-01.htm", "7400-03.htm", "7400-03a.htm", "7400-04.htm" }, 1, LEAF_OF_MOTHERTREE });
        TALKS.put(7401, new Object[] { 1, new String[] { "7131-01.htm", "7400-03.htm", "7400-03a.htm", "7400-04.htm" }, 1, LEAF_OF_MOTHERTREE });
        TALKS.put(7402, new Object[] { 1, new String[] { "7131-01.htm", "7400-03.htm", "7400-03a.htm", "7400-04.htm" }, 1, LEAF_OF_MOTHERTREE });
        TALKS.put(7403, new Object[] { 1, new String[] { "7131-01.htm", "7400-03.htm", "7400-03a.htm", "7400-04.htm" }, 1, LEAF_OF_MOTHERTREE });
        TALKS.put(7131, new Object[] { 2, new String[] { "7131-01.htm", "7131-03.htm", "7131-03a.htm", "7131-04.htm" }, 1, BLOOD_OF_JUNDIN });
        TALKS.put(7404, new Object[] { 2, new String[] { "7131-01.htm", "7131-03.htm", "7131-03a.htm", "7131-04.htm" }, 1, BLOOD_OF_JUNDIN });
        TALKS.put(7574, new Object[] { 3, new String[] { "7575-01.htm", "7575-03.htm", "7575-03a.htm", "7575-04.htm" }, 1, VOUCHER_OF_FLAME });
        TALKS.put(7575, new Object[] { 3, new String[] { "7575-01.htm", "7575-03.htm", "7575-03a.htm", "7575-04.htm" }, 1, VOUCHER_OF_FLAME });
        TALKS.put(7530, new Object[] { 4, new String[] { "7530-01.htm", "7530-03.htm", null, "7530-04.htm" }, 1, LICENSE_OF_MINER });
    }
    
    public static void main(String[] args)
    {
        new Q999_C3Tutorial();
    }
    
    public Q999_C3Tutorial()
    {
        super(999, Q999_C3Tutorial.class.getSimpleName(), "C3 Tutorial");
        
        for (int id : TALK_IDS)
        {
            addStartNpc(id);
            addFirstTalkId(id);
            addTalkId(id);
        }
        addKillId(1, 5198);
    }
    
    @Override
    public String onEvent(String event, QuestState st)
    {
        String htmltext = event;
        
        QuestState tutorialSt = st.getPlayer().getQuestState("255_Tutorial");
        if (tutorialSt == null)
            return null;
        
        int ex = tutorialSt.getInt("Ex");
        int classId = st.getPlayer().getClassId().getId();
        
        if (event.equals("TimerEx_NewbieHelper"))
        {
            if (ex == 0)
            {
                if (st.getPlayer().getClassId().isMage())
                    st.playTutorialVoice("tutorial_voice_009b");
                else
                    st.playTutorialVoice("tutorial_voice_009a");
                
                tutorialSt.set("Ex", "1");
            }
            else if (ex == 3)
            {
                st.playTutorialVoice("tutorial_voice_010a");
                tutorialSt.set("Ex", "4");
            }
            return null;
        }
        else if (event.equals("TimerEx_GrandMaster"))
        {
            if (ex >= 4)
            {
                st.showQuestionMark(7);
                st.playSound("ItemSound.quest_tutorial");
                st.playTutorialVoice("tutorial_voice_025");
            }
            return null;
        }
        else
        {
            Object[] data = EVENTS.get(event);
            if (data == null)
                return null;
            
            String htmlfile = (String) data[0];
            int radarX = (Integer) data[1];
            int radarY = (Integer) data[2];
            int radarZ = (Integer) data[3];
            int item = (Integer) data[4];
            int classId1 = (Integer) data[5];
            int gift1 = (Integer) data[6];
            int count1 = (Integer) data[7];
            int classId2 = (Integer) data[8];
            int gift2 = (Integer) data[9];
            int count2 = (Integer) data[10];
            
            st.addRadar(radarX, radarY, radarZ);
            htmltext = htmlfile;
            
            if (st.getQuestItemsCount(item) > 0 && st.getInt("onlyone") == 0)
            {
                st.addExpAndSp(0, 50);
                st.startQuestTimer("TimerEx_GrandMaster", 60000);
                st.takeItems(item, 1);
                
                if (ex <= 3)
                    tutorialSt.set("Ex", "4");
                
                if (classId == classId1)
                {
                    st.giveItems(gift1, count1);
                    if (gift1 == SPIRITSHOT_NOVICE)
                        st.playTutorialVoice("tutorial_voice_027");
                    else
                        st.playTutorialVoice("tutorial_voice_026");
                }
                else if (classId == classId2)
                {
                    if (gift2 != 0)
                    {
                        st.giveItems(gift2, count2);
                        st.playTutorialVoice("tutorial_voice_026");
                    }
                }
                
                st.unset("step");
                st.set("onlyone", "1");
            }
        }
        return htmltext;
    }
    
    @Override
    public String onFirstTalk(L2NpcInstance npc, L2PcInstance player)
    {
        QuestState tutorialSt = player.getQuestState("255_Tutorial");
        if (tutorialSt == null)
        {
            npc.showChatWindow(player);
            return null;
        }
        
        QuestState st = player.getQuestState(getName());
        if (st == null)
            st = newQuestState(player);
        
        String htmltext = "";
        int ex = tutorialSt.getInt("Ex");
        int npcId = npc.getNpcId();
        int step = st.getInt("step");
        int onlyone = st.getInt("onlyone");
        
        int npcType;
        int raceId;
        String[] htmlfiles;
        int item;
        
        if (TALKS.containsKey(npcId))
        {
            Object[] data = TALKS.get(npcId);
            raceId = (Integer) data[0];
            htmlfiles = (String[]) data[1];
            npcType = (Integer) data[2];
            item = (Integer) data[3];
        }
        else
        {
            npcType = 0;
            raceId = 0;
            htmlfiles = null;
            item = 0;
        }
        
        if ((player.getLevel() >= 10 || onlyone != 0) && npcType == 1)
        {
            htmltext = "7575-05.htm";
        }
        else if (npcId == 7600 || npcId == 7601 || npcId == 7602 || npcId == 7598 || npcId == 7599)
        {
            int reward = tutorialSt.getInt("reward");
            if (reward == 0)
            {
                if (player.getClassId().isMage())
                {
                    st.playTutorialVoice("tutorial_voice_027");
                    st.giveItems(SPIRITSHOT_NOVICE, 100);
                }
                else
                {
                    st.playTutorialVoice("tutorial_voice_026");
                    st.giveItems(SOULSHOT_NOVICE, 200);
                }
                tutorialSt.set("reward", "1");
                st.setState(State.COMPLETED);
            }
            npc.showChatWindow(player);
            return null;
        }
        else if (onlyone == 0 && player.getLevel() < 10)
        {
            if (player.getRace().ordinal() == raceId)
            {
                if (npcType == 1)
                {
                    final boolean isMage = player.getClassId().isMage();
                    
                    // Fighters get the melee version of the helper texts
                    htmltext = isMage ? htmlfiles[0] : "7530-01.htm";
                    
                    if (step == 0)
                    {
                        if (ex < 0)
                        {
                            tutorialSt.set("Ex", "0");
                            st.startQuestTimer("TimerEx_NewbieHelper", 30000);
                        }
                        
                        st.set("step", "1");
                        if (st.getState() != State.COMPLETED)
                            st.setState(State.STARTED);
                        st.playSound("ItemSound.quest_tutorial");
                        step = 1;
                        
                        // Show the mission first, unless the gem was already recovered
                        if (st.getQuestItemsCount(BLUE_GEM) == 0)
                            return htmltext;
                    }
                    
                    if (step == 1 && st.getQuestItemsCount(item) == 0)
                    {
                        if (st.getQuestItemsCount(BLUE_GEM) > 0)
                        {
                            st.takeItems(BLUE_GEM, st.getQuestItemsCount(BLUE_GEM));
                            st.giveItems(item, 1);
                            st.set("step", "2");
                            tutorialSt.set("Ex", "3");
                            st.startQuestTimer("TimerEx_NewbieHelper", 30000);
                            tutorialSt.set("ucMemo", "3");
                            st.playSound("ItemSound.quest_middle");
                            
                            if (isMage)
                            {
                                st.playTutorialVoice("tutorial_voice_027");
                                st.giveItems(SPIRITSHOT_NOVICE, 100);
                                htmltext = htmlfiles[2];
                                if (htmltext == null)
                                    htmltext = "<html><body>I am sorry. I only help warriors. Please go to another Newbie Helper who may assist you.</body></html>";
                            }
                            else
                            {
                                st.playTutorialVoice("tutorial_voice_026");
                                st.giveItems(SOULSHOT_NOVICE, 200);
                                htmltext = htmlfiles[1];
                                if (htmltext == null)
                                    htmltext = "<html><body>I am sorry. I only help mystics. Please go to another Newbie Helper who may assist you.</body></html>";
                            }
                        }
                        else if (isMage)
                            htmltext = player.getRace().ordinal() == 3 ? "7575-02.htm" : "7131-02.htm";
                        else
                            htmltext = "7530-02.htm";
                    }
                    else if (step >= 1)
                    {
                        htmltext = htmlfiles[3];
                    }
                }
                else if (npcType == 0)
                {
                    // Before meeting the newbie helper, keep the regular dialog (and its quests) available
                    if (step == 1)
                        htmltext = htmlfiles[0];
                    else if (step == 2)
                        htmltext = htmlfiles[1];
                    else if (step == 3)
                        htmltext = htmlfiles[2];
                }
            }
        }
        else if (st.getState() != State.COMPLETED && npcType == 0)
        {
            htmltext = npc.getNpcId() + "-04.htm";
        }
        
        if (htmltext == null || htmltext.isEmpty())
        {
            npc.showChatWindow(player);
        }
        
        return htmltext;
    }
    
    @Override
    public String onTalk(L2NpcInstance npc, L2PcInstance player)
    {
        return getNoQuestMsg();
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
        QuestState st = player.getQuestState(getName());
        if (st != null)
        {
            QuestState tutorialSt = player.getQuestState("255_Tutorial");
            if (tutorialSt == null)
                return null;
            
            int ex = tutorialSt.getInt("Ex");
            
            if (ex == 0 || ex == 1)
            {
                st.playTutorialVoice("tutorial_voice_011");
                st.showQuestionMark(3);
                tutorialSt.set("Ex", "2");
            }
            
            if ((ex == 0 || ex == 1 || ex == 2) && st.getQuestItemsCount(BLUE_GEM) == 0)
            {
                if (npc instanceof L2Attackable && st.getRandom(100) < 50)
                {
                    st.dropItem((L2Attackable) npc, player, BLUE_GEM, 1);
                    st.playSound("ItemSound.quest_tutorial");
                }
            }
        }
        
        return null;
    }
}
