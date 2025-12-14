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
package quests.Q402_PathToKnight;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Quest 401 Path to Knight
 * Converted to Java
 * @author Karakan
 */
public class Q402_PathToKnight extends Quest
{
    private static final int MARK_OF_ESQUIRE_ID = 1271;
    private static final int SWORD_OF_RITUAL_ID = 1161;
    private static final int COIN_OF_LORDS1_ID = 1162;
    private static final int COIN_OF_LORDS2_ID = 1163;
    private static final int COIN_OF_LORDS3_ID = 1164;
    private static final int COIN_OF_LORDS4_ID = 1165;
    private static final int COIN_OF_LORDS5_ID = 1166;
    private static final int COIN_OF_LORDS6_ID = 1167;
    private static final int GLUDIO_GUARDS_MARK1_ID = 1168;
    private static final int BUGBEAR_NECKLACE_ID = 1169;
    private static final int EINHASAD_CHURCH_MARK1_ID = 1170;
    private static final int EINHASAD_CRUCIFIX_ID = 1171;
    private static final int GLUDIO_GUARDS_MARK2_ID = 1172;
    private static final int POISON_SPIDER_LEG1_ID = 1173;
    private static final int EINHASAD_CHURCH_MARK2_ID = 1174;
    private static final int LIZARDMAN_TOTEM_ID = 1175;
    private static final int GLUDIO_GUARDS_MARK3_ID = 1176;
    private static final int GIANT_SPIDER_HUSK_ID = 1177;
    private static final int EINHASAD_CHURCH_MARK3_ID = 1178;
    private static final int HORRIBLE_SKULL_ID = 1179;
    
    public static void main(String[] args)
    {
        new Q402_PathToKnight();
    }
    
    public Q402_PathToKnight()
    {
        super(402, Q402_PathToKnight.class.getSimpleName(), "Path to Knight");
        
        setItemsIds(COIN_OF_LORDS1_ID, COIN_OF_LORDS2_ID, COIN_OF_LORDS3_ID, COIN_OF_LORDS4_ID, COIN_OF_LORDS5_ID, COIN_OF_LORDS6_ID, GLUDIO_GUARDS_MARK1_ID, GLUDIO_GUARDS_MARK2_ID, GLUDIO_GUARDS_MARK3_ID, EINHASAD_CHURCH_MARK1_ID, EINHASAD_CHURCH_MARK2_ID, EINHASAD_CHURCH_MARK3_ID, BUGBEAR_NECKLACE_ID, EINHASAD_CRUCIFIX_ID, POISON_SPIDER_LEG1_ID, LIZARDMAN_TOTEM_ID, GIANT_SPIDER_HUSK_ID, HORRIBLE_SKULL_ID, MARK_OF_ESQUIRE_ID);
        
        addStartNpc(7417);
        addTalkId(7031, 7037, 7039, 7289, 7311, 7332, 7379, 7417, 7653);
        
        addKillId(103, 106, 108, 24, 27, 30, 38, 404, 43, 50, 5024, 775);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        QuestState st = player.getQuestState(getName());
        String htmltext = event;
        if (st == null)
            return htmltext;
        
        switch (event)
        {
            case "7417_1":
                if (st.getPlayer().getClassId().getId() == 0x00)
                {
                    if (st.getPlayer().getLevel() >= 19)
                    {
                        if (st.getQuestItemsCount(SWORD_OF_RITUAL_ID) > 0)
                        {
                            htmltext = "7417-04.htm";
                        }
                        else
                        {
                            htmltext = "7417-05.htm";
                        }
                    }
                    else
                    {
                        htmltext = "7417-02.htm";
                    }
                }
                else if (st.getPlayer().getClassId().getId() == 0x04)
                {
                    htmltext = "7417-02a.htm";
                }
                else
                {
                    htmltext = "7417-03.htm";
                }
                break;
            case "7417_2":
                htmltext = "7417-07.htm";
                break;
            case "1":
                st.set("id", "0");
                st.set("cond", "1");
                st.setState(State.STARTED);
                st.playSound(QuestState.SOUND_ACCEPT);
                st.giveItems(MARK_OF_ESQUIRE_ID, 1);
                htmltext = "7417-08.htm";
                break;
            case "7332_1":
                htmltext = "7332-02.htm";
                st.giveItems(GLUDIO_GUARDS_MARK1_ID, 1);
                break;
            case "7289_1":
                htmltext = "7289-03.htm";
                st.giveItems(EINHASAD_CHURCH_MARK1_ID, 1);
                break;
            case "7379_1":
                htmltext = "7379-02.htm";
                st.giveItems(GLUDIO_GUARDS_MARK2_ID, 1);
                break;
            case "7037_1":
                htmltext = "7037-02.htm";
                st.giveItems(EINHASAD_CHURCH_MARK2_ID, 1);
                break;
            case "7039_1":
                htmltext = "7039-02.htm";
                st.giveItems(GLUDIO_GUARDS_MARK3_ID, 1);
                break;
            case "7031_1":
                htmltext = "7031-02.htm";
                st.giveItems(EINHASAD_CHURCH_MARK3_ID, 1);
                break;
            case "7417_3":
                htmltext = "7417-15.htm";
                break;
            case "7417_4":
                htmltext = "7417-13.htm";
                st.takeItems(COIN_OF_LORDS1_ID, -1);
                st.takeItems(COIN_OF_LORDS2_ID, -1);
                st.takeItems(COIN_OF_LORDS3_ID, -1);
                st.takeItems(COIN_OF_LORDS4_ID, -1);
                st.takeItems(COIN_OF_LORDS5_ID, -1);
                st.takeItems(COIN_OF_LORDS6_ID, -1);
                st.takeItems(GLUDIO_GUARDS_MARK1_ID, -1);
                st.takeItems(GLUDIO_GUARDS_MARK2_ID, -1);
                st.takeItems(GLUDIO_GUARDS_MARK3_ID, -1);
                st.takeItems(EINHASAD_CHURCH_MARK1_ID, -1);
                st.takeItems(EINHASAD_CHURCH_MARK2_ID, -1);
                st.takeItems(EINHASAD_CHURCH_MARK3_ID, -1);
                st.takeItems(BUGBEAR_NECKLACE_ID, -1);
                st.takeItems(EINHASAD_CRUCIFIX_ID, -1);
                st.takeItems(POISON_SPIDER_LEG1_ID, -1);
                st.takeItems(LIZARDMAN_TOTEM_ID, -1);
                st.takeItems(GIANT_SPIDER_HUSK_ID, -1);
                st.takeItems(HORRIBLE_SKULL_ID, -1);
                st.takeItems(MARK_OF_ESQUIRE_ID, -1);
                st.giveItems(SWORD_OF_RITUAL_ID, 1);
                st.set("cond", "0");
                st.setState(State.COMPLETED);
                st.playSound(QuestState.SOUND_FINISH);
                break;
            case "7417_5":
                htmltext = "7417-14.htm";
                st.takeItems(COIN_OF_LORDS1_ID, -1);
                st.takeItems(COIN_OF_LORDS2_ID, -1);
                st.takeItems(COIN_OF_LORDS3_ID, -1);
                st.takeItems(COIN_OF_LORDS4_ID, -1);
                st.takeItems(COIN_OF_LORDS5_ID, -1);
                st.takeItems(COIN_OF_LORDS6_ID, -1);
                st.takeItems(GLUDIO_GUARDS_MARK1_ID, -1);
                st.takeItems(GLUDIO_GUARDS_MARK2_ID, -1);
                st.takeItems(GLUDIO_GUARDS_MARK3_ID, -1);
                st.takeItems(EINHASAD_CHURCH_MARK1_ID, -1);
                st.takeItems(EINHASAD_CHURCH_MARK2_ID, -1);
                st.takeItems(EINHASAD_CHURCH_MARK3_ID, -1);
                st.takeItems(BUGBEAR_NECKLACE_ID, -1);
                st.takeItems(EINHASAD_CRUCIFIX_ID, -1);
                st.takeItems(POISON_SPIDER_LEG1_ID, -1);
                st.takeItems(LIZARDMAN_TOTEM_ID, -1);
                st.takeItems(GIANT_SPIDER_HUSK_ID, -1);
                st.takeItems(HORRIBLE_SKULL_ID, -1);
                st.giveItems(SWORD_OF_RITUAL_ID, 1);
                st.set("cond", "0");
                st.setState(State.COMPLETED);
                st.playSound(QuestState.SOUND_FINISH);
                break;
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
        
        int npcId = npc.getNpcId();
        switch (npcId)
        {
            case 7417:
                if (st.getInt("cond") == 0)
                {
                    htmltext = "7417-01.htm";
                }
                else if (st.getInt("cond") == 1)
                {
                    if (st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && (st.getQuestItemsCount(COIN_OF_LORDS1_ID) + st.getQuestItemsCount(COIN_OF_LORDS2_ID) + st.getQuestItemsCount(COIN_OF_LORDS3_ID) + st.getQuestItemsCount(COIN_OF_LORDS4_ID) + st.getQuestItemsCount(COIN_OF_LORDS5_ID) + st.getQuestItemsCount(COIN_OF_LORDS6_ID)) < 3)
                    {
                        htmltext = "7417-09.htm";
                    }
                    else if (st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && (st.getQuestItemsCount(COIN_OF_LORDS1_ID) + st.getQuestItemsCount(COIN_OF_LORDS2_ID) + st.getQuestItemsCount(COIN_OF_LORDS3_ID) + st.getQuestItemsCount(COIN_OF_LORDS4_ID) + st.getQuestItemsCount(COIN_OF_LORDS5_ID) + st.getQuestItemsCount(COIN_OF_LORDS6_ID)) == 3)
                    {
                        htmltext = "7417-10.htm";
                    }
                    else if (npcId == 7417 && st.getInt("cond") == 1 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && (st.getQuestItemsCount(COIN_OF_LORDS1_ID) + st.getQuestItemsCount(COIN_OF_LORDS2_ID) + st.getQuestItemsCount(COIN_OF_LORDS3_ID) + st.getQuestItemsCount(COIN_OF_LORDS4_ID) + st.getQuestItemsCount(COIN_OF_LORDS5_ID) + st.getQuestItemsCount(COIN_OF_LORDS6_ID)) > 3 && (st.getQuestItemsCount(COIN_OF_LORDS1_ID) + st.getQuestItemsCount(COIN_OF_LORDS2_ID) + st.getQuestItemsCount(COIN_OF_LORDS3_ID) + st.getQuestItemsCount(COIN_OF_LORDS4_ID) + st.getQuestItemsCount(COIN_OF_LORDS5_ID) + st.getQuestItemsCount(COIN_OF_LORDS6_ID)) < 6)
                    {
                        htmltext = "7417-11.htm";
                    }
                    else if (npcId == 7417 && st.getInt("cond") == 1 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && (st.getQuestItemsCount(COIN_OF_LORDS1_ID) + st.getQuestItemsCount(COIN_OF_LORDS2_ID) + st.getQuestItemsCount(COIN_OF_LORDS3_ID) + st.getQuestItemsCount(COIN_OF_LORDS4_ID) + st.getQuestItemsCount(COIN_OF_LORDS5_ID) + st.getQuestItemsCount(COIN_OF_LORDS6_ID)) == 6)
                    {
                        htmltext = "7417-12.htm";
                        st.takeItems(COIN_OF_LORDS1_ID, -1);
                        st.takeItems(COIN_OF_LORDS2_ID, -1);
                        st.takeItems(COIN_OF_LORDS3_ID, -1);
                        st.takeItems(COIN_OF_LORDS4_ID, -1);
                        st.takeItems(COIN_OF_LORDS5_ID, -1);
                        st.takeItems(COIN_OF_LORDS6_ID, -1);
                        st.takeItems(GLUDIO_GUARDS_MARK1_ID, -1);
                        st.takeItems(GLUDIO_GUARDS_MARK2_ID, -1);
                        st.takeItems(GLUDIO_GUARDS_MARK3_ID, -1);
                        st.takeItems(EINHASAD_CHURCH_MARK1_ID, -1);
                        st.takeItems(EINHASAD_CHURCH_MARK2_ID, -1);
                        st.takeItems(EINHASAD_CHURCH_MARK3_ID, -1);
                        st.takeItems(BUGBEAR_NECKLACE_ID, -1);
                        st.takeItems(EINHASAD_CRUCIFIX_ID, -1);
                        st.takeItems(POISON_SPIDER_LEG1_ID, -1);
                        st.takeItems(LIZARDMAN_TOTEM_ID, -1);
                        st.takeItems(GIANT_SPIDER_HUSK_ID, -1);
                        st.takeItems(HORRIBLE_SKULL_ID, -1);
                        st.giveItems(SWORD_OF_RITUAL_ID, 1);
                        st.set("cond", "0");
                        st.setState(State.COMPLETED);
                        st.playSound(QuestState.SOUND_FINISH);
                    }
                }
                break;
            case 7332:
                if (st.getInt("cond") == 1)
                {
                    if (st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID) == 0 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && st.getQuestItemsCount(COIN_OF_LORDS1_ID) == 0)
                    {
                        htmltext = "7332-01.htm";
                    }
                    else if (st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID) > 0)
                    {
                        if (st.getQuestItemsCount(BUGBEAR_NECKLACE_ID) < 10)
                        {
                            htmltext = "7332-03.htm";
                        }
                        else
                        {
                            htmltext = "7332-04.htm";
                            st.takeItems(BUGBEAR_NECKLACE_ID, st.getQuestItemsCount(BUGBEAR_NECKLACE_ID));
                            st.takeItems(GLUDIO_GUARDS_MARK1_ID, 1);
                            st.giveItems(COIN_OF_LORDS1_ID, 1);
                        }
                    }
                    else if (st.getQuestItemsCount(COIN_OF_LORDS1_ID) > 0)
                    {
                        htmltext = "7332-05.htm";
                    }
                }
                break;
            case 7289:
                if (st.getInt("cond") == 1)
                {
                    if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID) == 0 && st.getQuestItemsCount(COIN_OF_LORDS2_ID) == 0 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0)
                    {
                        htmltext = "7289-01.htm";
                    }
                    else if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID) > 0)
                    {
                        if (st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID) < 12)
                        {
                            htmltext = "7289-04.htm";
                        }
                        else
                        {
                            htmltext = "7289-05.htm";
                            st.takeItems(EINHASAD_CRUCIFIX_ID, st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID));
                            st.takeItems(EINHASAD_CHURCH_MARK1_ID, 1);
                            st.giveItems(COIN_OF_LORDS2_ID, 1);
                        }
                    }
                    else if (st.getQuestItemsCount(COIN_OF_LORDS2_ID) > 0)
                    {
                        htmltext = "7289-06.htm";
                    }
                }
                break;
            case 7379:
                if (st.getInt("cond") == 1)
                {
                    if (st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && st.getQuestItemsCount(COIN_OF_LORDS3_ID) == 0 && st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID) == 0)
                    {
                        htmltext = "7379-01.htm";
                    }
                    else if (st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID) > 0)
                    {
                        if (st.getQuestItemsCount(POISON_SPIDER_LEG1_ID) < 20)
                        {
                            htmltext = "7379-03.htm";
                        }
                        else
                        {
                            htmltext = "7379-04.htm";
                            st.takeItems(POISON_SPIDER_LEG1_ID, st.getQuestItemsCount(POISON_SPIDER_LEG1_ID));
                            st.takeItems(GLUDIO_GUARDS_MARK2_ID, 1);
                            st.giveItems(COIN_OF_LORDS3_ID, 1);
                        }
                    }
                    else if (st.getQuestItemsCount(COIN_OF_LORDS3_ID) > 0)
                    {
                        htmltext = "7379-05.htm";
                    }
                }
                break;
            case 7037:
                if (st.getInt("cond") == 1)
                {
                    if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID) == 0 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && st.getQuestItemsCount(COIN_OF_LORDS4_ID) == 0)
                    {
                        htmltext = "7037-01.htm";
                    }
                    else if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID) > 0)
                    {
                        if (st.getQuestItemsCount(LIZARDMAN_TOTEM_ID) < 20)
                        {
                            htmltext = "7037-03.htm";
                        }
                        else
                        {
                            htmltext = "7037-04.htm";
                            st.takeItems(LIZARDMAN_TOTEM_ID, st.getQuestItemsCount(LIZARDMAN_TOTEM_ID));
                            st.takeItems(EINHASAD_CHURCH_MARK2_ID, 1);
                            st.giveItems(COIN_OF_LORDS4_ID, 1);
                        }
                    }
                    else if (st.getQuestItemsCount(COIN_OF_LORDS4_ID) > 0)
                    {
                        
                        htmltext = "7037-05.htm";
                    }
                }
                break;
            case 7039:
                if (st.getInt("cond") == 1)
                {
                    if (st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID) == 0 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && st.getQuestItemsCount(COIN_OF_LORDS5_ID) == 0)
                    {
                        htmltext = "7039-01.htm";
                    }
                    else if (st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID) > 0)
                    {
                        if (st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID) < 20)
                        {
                            htmltext = "7039-03.htm";
                        }
                        else
                        {
                            htmltext = "7039-04.htm";
                            st.takeItems(GIANT_SPIDER_HUSK_ID, st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID));
                            st.takeItems(GLUDIO_GUARDS_MARK3_ID, 1);
                            st.giveItems(COIN_OF_LORDS5_ID, 1);
                        }
                    }
                    else if (st.getQuestItemsCount(COIN_OF_LORDS5_ID) > 0)
                    {
                        htmltext = "7039-05.htm";
                    }
                }
                break;
            case 7031:
                if (st.getInt("cond") == 1)
                {
                    if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID) == 0 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0 && st.getQuestItemsCount(COIN_OF_LORDS6_ID) == 0)
                    {
                        htmltext = "7031-01.htm";
                    }
                    else if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID) > 0)
                    {
                        if (st.getQuestItemsCount(HORRIBLE_SKULL_ID) < 10)
                        {
                            htmltext = "7031-03.htm";
                        }
                        else
                        {
                            htmltext = "7031-04.htm";
                            st.takeItems(HORRIBLE_SKULL_ID, st.getQuestItemsCount(HORRIBLE_SKULL_ID));
                            st.takeItems(EINHASAD_CHURCH_MARK3_ID, 1);
                            st.giveItems(COIN_OF_LORDS6_ID, 1);
                        }
                    }
                    else if (npcId == 7031 && st.getInt("cond") == 1 && st.getQuestItemsCount(COIN_OF_LORDS6_ID) > 0)
                    {
                        htmltext = "7031-05.htm";
                    }
                }
                break;
            case 7311:
                if (st.getInt("cond") == 1)
                {
                    if (st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0)
                    {
                        htmltext = "7311-01.htm";
                    }
                }
                break;
            case 7653:
                if (st.getInt("cond") == 1)
                {
                    if (npcId == 7653 && st.getInt("cond") == 1 && st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) > 0)
                    {
                        htmltext = "7653-01.htm";
                    }
                }
                break;
        }
        return htmltext;
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
    {
        final QuestState st = checkPlayerState(killer, npc, State.STARTED);
        if (st != null && st.getState() == State.STARTED)
        {
            switch (npc.getNpcId())
            {
                case 775:
                    if (st.getInt("cond") == 1 && st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID) > 0)
                    {
                        st.giveItems(BUGBEAR_NECKLACE_ID, 1);
                        if (st.getQuestItemsCount(BUGBEAR_NECKLACE_ID) == 10)
                        {
                            st.playSound(QuestState.SOUND_MIDDLE);
                        }
                        else
                        {
                            st.playSound(QuestState.SOUND_ITEMGET);
                        }
                    }
                    break;
                
                case 5024:
                    if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID) > 0)
                    {
                        st.giveItems(EINHASAD_CRUCIFIX_ID, 1);
                        if (st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID) == 12)
                        {
                            st.playSound(QuestState.SOUND_MIDDLE);
                        }
                        else
                        {
                            st.playSound(QuestState.SOUND_ITEMGET);
                        }
                    }
                    break;
                
                case 38:
                case 43:
                case 50:
                    if (st.getInt("cond") > 0 && st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID) > 0 && st.getQuestItemsCount(POISON_SPIDER_LEG1_ID) < 20)
                    {
                        st.giveItems(POISON_SPIDER_LEG1_ID, 1);
                        if (st.getQuestItemsCount(POISON_SPIDER_LEG1_ID) == 20)
                        {
                            st.playSound(QuestState.SOUND_MIDDLE);
                        }
                        else
                        {
                            st.playSound(QuestState.SOUND_ITEMGET);
                        }
                    }
                    break;
                
                case 30:
                case 27:
                case 24:
                    if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID) > 0 && st.getQuestItemsCount(LIZARDMAN_TOTEM_ID) < 20 && st.getRandom(10) < 5)
                    {
                        st.giveItems(LIZARDMAN_TOTEM_ID, 1);
                        if (st.getQuestItemsCount(LIZARDMAN_TOTEM_ID) == 20)
                        {
                            st.playSound(QuestState.SOUND_MIDDLE);
                        }
                        else
                        {
                            st.playSound(QuestState.SOUND_ITEMGET);
                        }
                    }
                    break;
                
                case 103:
                case 106:
                case 108:
                    if (st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID) > 0 && st.getRandom(10) < 4)
                    {
                        st.giveItems(GIANT_SPIDER_HUSK_ID, 1);
                        if (st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID) == 20)
                        {
                            st.playSound(QuestState.SOUND_MIDDLE);
                        }
                        else
                        {
                            st.playSound(QuestState.SOUND_ITEMGET);
                        }
                    }
                    break;
                
                case 404:
                    if (st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID) > 0)
                    {
                        st.giveItems(HORRIBLE_SKULL_ID, 1);
                        if (st.getQuestItemsCount(HORRIBLE_SKULL_ID) == 10)
                        {
                            st.playSound(QuestState.SOUND_MIDDLE);
                        }
                        else
                        {
                            st.playSound(QuestState.SOUND_ITEMGET);
                        }
                    }
                    break;
            }
        }
        return null;
    }
}