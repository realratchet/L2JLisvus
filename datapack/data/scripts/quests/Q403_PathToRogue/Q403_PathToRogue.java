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
package quests.Q403_PathToRogue;

import java.util.HashMap;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

/**
 * Quest 403 Path to Rogue
 * Converted to Java
 * @author Karakan
 */
public class Q403_PathToRogue extends Quest 
{
    private static final int BEZIQUES_LETTER_ID = 1180;
    private static final int SPATOIS_BONES_ID = 1183;
    private static final int HORSESHOE_OF_LIGHT_ID = 1184;
    private static final int WANTED_BILL_ID = 1185;
    private static final int STOLEN_JEWELRY_ID = 1186;
    private static final int STOLEN_TOMES_ID = 1187;
    private static final int STOLEN_RING_ID = 1188;
    private static final int STOLEN_NECKLACE_ID = 1189;
    private static final int BEZIQUES_RECOMMENDATION_ID = 1190;
    private static final int NETIS_BOW_ID = 1181;
    private static final int NETIS_DAGGER_ID = 1182;
    
	public static void main(String[] args)
    {
        new Q403_PathToRogue();
    }
	
    public Q403_PathToRogue() 
    {
    	 super(403, Q403_PathToRogue.class.getSimpleName(), "Path to Rogue");

    	 addStartNpc(7379);
    	 addTalkId(7379, 7425);
    	 addKillId(5038, 35, 42, 45, 51, 54, 60);
    }

    private static final int[] STOLEN_ITEMS = {
        STOLEN_JEWELRY_ID,
        STOLEN_TOMES_ID,
        STOLEN_RING_ID,
        STOLEN_NECKLACE_ID
    };
    
    private static final HashMap<Integer, Integer> DROP_CHANCE = new HashMap<>();
    {{
    	DROP_CHANCE.put(35, 2);
    	DROP_CHANCE.put(42, 3);
    	DROP_CHANCE.put(45, 2);
    	DROP_CHANCE.put(51, 2);
    	DROP_CHANCE.put(54, 8);
    	DROP_CHANCE.put(60, 8);
    }}

    private boolean haveAllStolenItems(QuestState st) {
        for (int stolenItemId : STOLEN_ITEMS) {
            if (st.getQuestItemsCount(stolenItemId) == 0) {
                return false;
            }
        }
        return true;
    }

	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
    	QuestState st = player.getQuestState(getName());
        String htmltext = event;
		if (st == null)
			return htmltext;
		
        switch (event) {
            case "7379_2":
                if (st.getPlayer().getClassId().getId() == 0x00) 
                {
                    if (st.getPlayer().getLevel() >= 19) {
                        if (st.getQuestItemsCount(BEZIQUES_RECOMMENDATION_ID) > 0) 
                        {
                            htmltext = "7379-04.htm";
                        } 
                        else 
                        {
                            htmltext = "7379-05.htm";
                        }
                    } 
                    else 
                    {
                        htmltext = "7379-03.htm";
                    }
                } 
                else if (st.getPlayer().getClassId().getId() == 0x07) 
                {
                    htmltext = "7379-02a.htm";
                } 
                else 
                {
                    htmltext = "7379-02.htm";
                }
                break;
            case "1":
                st.set("id", "0");
                st.set("cond", "1");
                st.setState(State.STARTED);
                st.playSound(QuestState.SOUND_ACCEPT);
                st.giveItems(BEZIQUES_LETTER_ID, 1);
                htmltext = "7379-06.htm";
                break;
            case "7425_1":
                st.takeItems(BEZIQUES_LETTER_ID, 1);
                if (st.getQuestItemsCount(NETIS_BOW_ID) == 0) {
                    st.giveItems(NETIS_BOW_ID, 1);
                }
                if (st.getQuestItemsCount(NETIS_DAGGER_ID) == 0) {
                    st.giveItems(NETIS_DAGGER_ID, 1);
                }
                st.set("cond", "2");
                htmltext = "7425-05.htm";
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
            case 7379:
            	if (st.getInt("cond") == 0)
            	{
            		htmltext = "7379-01.htm";
            	}
                else if (npcId == 7379 && st.getInt("cond") > 0)
                {
                    if (st.getQuestItemsCount(HORSESHOE_OF_LIGHT_ID) == 0 && haveAllStolenItems(st)) 
                    {
                        htmltext = "7379-09.htm";
                        st.giveItems(BEZIQUES_RECOMMENDATION_ID, 1);
                        st.takeItems(NETIS_BOW_ID, 1);
                        st.takeItems(NETIS_DAGGER_ID, 1);
                        st.takeItems(WANTED_BILL_ID, 1);
                        for (int stolenItemId : STOLEN_ITEMS) 
                        {
                            st.takeItems(stolenItemId, -1);
                        }
                        st.set("cond", "0");
                        st.setState(State.COMPLETED);
                        st.playSound(QuestState.SOUND_FINISH); 
                    } 
                    else if (st.getQuestItemsCount(HORSESHOE_OF_LIGHT_ID) == 0 && st.getQuestItemsCount(BEZIQUES_LETTER_ID) > 0) 
                    {
                        htmltext = "7379-07.htm";
                    }
                    else if (st.getQuestItemsCount(HORSESHOE_OF_LIGHT_ID) > 0) 
                    {
                        htmltext = "7379-08.htm";
                        st.takeItems(HORSESHOE_OF_LIGHT_ID, 1);
                        st.giveItems(WANTED_BILL_ID, 1);
                        st.set("cond", "5");
                    }
                    else if (st.getQuestItemsCount(NETIS_BOW_ID) > 0 && st.getQuestItemsCount(NETIS_DAGGER_ID) > 0 && st.getQuestItemsCount(WANTED_BILL_ID) == 0) 
                    {
                        htmltext = "7379-10.htm";
                    } 
                    else if (st.getQuestItemsCount(WANTED_BILL_ID) > 0) 
                    {
                        htmltext = "7379-11.htm";
                    }
                }   
                break;
            case 7425:
                if (st.getInt("cond") > 0 && st.getQuestItemsCount(BEZIQUES_LETTER_ID) > 0) 
                {
                   htmltext = "7425-01.htm";
                }
                else if (st.getInt("cond") > 0 && st.getQuestItemsCount(HORSESHOE_OF_LIGHT_ID) == 0 && st.getQuestItemsCount(BEZIQUES_LETTER_ID) == 0) 
                {
                   if (st.getQuestItemsCount(SPATOIS_BONES_ID) < 10) 
                   {
                       htmltext = "7425-06.htm";
                   } else if (st.getQuestItemsCount(WANTED_BILL_ID) > 0) 
                   {
                       htmltext = "7425-08.htm";
                   } else if (st.getQuestItemsCount(SPATOIS_BONES_ID) >= 10) 
                   {
                       htmltext = "7425-07.htm";
                       st.takeItems(SPATOIS_BONES_ID, st.getQuestItemsCount(SPATOIS_BONES_ID));
                       st.giveItems(HORSESHOE_OF_LIGHT_ID, 1);
                       st.set("cond", "4");
                   }
               }
               else if (st.getInt("cond") > 0 && st.getQuestItemsCount(HORSESHOE_OF_LIGHT_ID) > 0) 
               {
                   htmltext = "7425-08.htm";
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
            int npcId = npc.getNpcId();
            if (st.getItemEquipped(7) == NETIS_BOW_ID || st.getItemEquipped(7) == NETIS_DAGGER_ID) 
            {
                if (DROP_CHANCE.containsKey(npcId)) 
                {
                    st.set("id", "0");
                    if (st.getInt("cond") > 0 && st.getQuestItemsCount(SPATOIS_BONES_ID) < 10 && st.getRandom(10) < DROP_CHANCE.get(npcId)) 
                    {
                        st.giveItems(SPATOIS_BONES_ID, 1);
                        if (st.getQuestItemsCount(SPATOIS_BONES_ID) == 10) 
                        {
                        	st.playSound(QuestState.SOUND_MIDDLE);
                            st.set("cond", "3");
                        } else 
                        {
                        	st.playSound(QuestState.SOUND_ITEMGET);
                        }
                    }
                } 
                else if (npcId == 5038) 
                {
                    st.set("id", "0");
                    if (st.getInt("cond") > 0 && st.getQuestItemsCount(WANTED_BILL_ID) > 0) 
                    {
                        int n = st.getRandom(4);
                        if (st.getQuestItemsCount(STOLEN_ITEMS[n]) == 0) 
                        {
                            st.giveItems(STOLEN_ITEMS[n], 1);
                            if (!haveAllStolenItems(st)) 
                            {
                            	st.playSound(QuestState.SOUND_ITEMGET);
                            } 
                            else 
                            {
                            	st.playSound(QuestState.SOUND_MIDDLE);
                                st.set("cond", "6");
                            }
                        }
                    }
                }
            }
        }
		return null;
    }
}