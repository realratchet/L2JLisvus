# Made by Mr. Have fun! Version 0.2.1 cheked & fix by Ryo Saeba
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

CRYSTAL_MEDALLION_ID = 1231
MONEY_OF_SWINDLER_ID = 1232
DAIRY_OF_ALLANA_ID = 1233
LIZARD_CAPTAIN_ORDER_ID = 1234
LEAF_OF_ORACLE_ID = 1235
HALF_OF_DAIRY_ID = 1236
TAMATOS_NECKLACE_ID = 1275

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [MONEY_OF_SWINDLER_ID, DAIRY_OF_ALLANA_ID, LIZARD_CAPTAIN_ORDER_ID, CRYSTAL_MEDALLION_ID, HALF_OF_DAIRY_ID, TAMATOS_NECKLACE_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("id","0")
        if st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x19 and st.getQuestItemsCount(LEAF_OF_ORACLE_ID) == 0 :
          st.set("cond","1")
          st.setState(State.STARTED)
          st.playSound("ItemSound.quest_accept")
          st.giveItems(CRYSTAL_MEDALLION_ID,1)
          htmltext = "7293-05.htm"
        elif st.getPlayer().getClassId().getId() != 0x19 :
            if st.getPlayer().getClassId().getId() == 0x1d :
              htmltext = "7293-02a.htm"
            else:
              htmltext = "7293-02.htm"
        elif st.getPlayer().getLevel()<19 and st.getPlayer().getClassId().getId() == 0x19 :
            htmltext = "7293-03.htm"
        elif st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x19 and st.getQuestItemsCount(LEAF_OF_ORACLE_ID) == 1 :
            htmltext = "7293-04.htm"
    elif event == "7424-08.htm" :
        if st.getInt("cond") :
           st.addSpawn(5032)
           st.addSpawn(5033)
           st.addSpawn(5034)
           st.set("cond","2")
    elif event == "7424_1" :
        htmltext=""
    elif event == "7428_1" :
        if st.getInt("cond") :
           htmltext = "7428-02.htm"
    elif event == "7428_2" :
        if st.getInt("cond") :
           htmltext = "7428-03.htm"
    elif event == "7428_3" :
        if st.getInt("cond") :
           st.addSpawn(5035)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7293 and id != State.STARTED :
        return htmltext
   if npcId == 7293 and st.getInt("cond")==0 :
        if st.getQuestItemsCount(LEAF_OF_ORACLE_ID) == 0 :
          htmltext = "7293-01.htm"
        else:
          htmltext = "7293-04.htm"
   elif npcId == 7293 and st.getInt("cond") and st.getQuestItemsCount(CRYSTAL_MEDALLION_ID) :
        if st.getQuestItemsCount(MONEY_OF_SWINDLER_ID) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA_ID) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) == 0 and st.getQuestItemsCount(HALF_OF_DAIRY_ID) == 0 :
          if st.getInt("cond") :
            htmltext = "7293-09.htm"
          else:
            htmltext = "7293-06.htm"
        else:
          if st.getQuestItemsCount(MONEY_OF_SWINDLER_ID) == 1 and st.getQuestItemsCount(DAIRY_OF_ALLANA_ID) == 1 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY_ID) == 0 :
            htmltext = "7293-08.htm"
            st.takeItems(MONEY_OF_SWINDLER_ID,1)
            st.takeItems(DAIRY_OF_ALLANA_ID,1)
            st.takeItems(LIZARD_CAPTAIN_ORDER_ID,1)
            st.takeItems(CRYSTAL_MEDALLION_ID,1)
            st.giveItems(LEAF_OF_ORACLE_ID,1)
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
          else:
            htmltext = "7293-07.htm"
   elif npcId == 7424 and st.getInt("cond") and st.getQuestItemsCount(CRYSTAL_MEDALLION_ID) :
        if st.getQuestItemsCount(MONEY_OF_SWINDLER_ID) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA_ID) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) == 0 and st.getQuestItemsCount(HALF_OF_DAIRY_ID) == 0 :
          if st.getInt("cond") > 2:
            htmltext = "7424-05.htm"
          else:
            htmltext = "7424-01.htm"
        elif st.getQuestItemsCount(MONEY_OF_SWINDLER_ID) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA_ID) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY_ID) == 0 :
            htmltext = "7424-02.htm"
            st.giveItems(HALF_OF_DAIRY_ID,1)
            st.set("cond","4")
        elif st.getQuestItemsCount(MONEY_OF_SWINDLER_ID) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA_ID) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY_ID) == 1 :
              if st.getInt("cond") and st.getQuestItemsCount(TAMATOS_NECKLACE_ID) == 0 :
                htmltext = "7424-06.htm"
              else:
                htmltext = "7424-03.htm"
        elif st.getQuestItemsCount(MONEY_OF_SWINDLER_ID) == 1 and st.getQuestItemsCount(DAIRY_OF_ALLANA_ID) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY_ID) == 1 :
                htmltext = "7424-04.htm"
                st.takeItems(HALF_OF_DAIRY_ID,1)
                st.giveItems(DAIRY_OF_ALLANA_ID,1)
                st.set("cond","7")
        else:
                if st.getQuestItemsCount(MONEY_OF_SWINDLER_ID) == 1 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY_ID) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA_ID) :
                  htmltext = "7424-05.htm"
   elif npcId == 7428 and st.getInt("cond") and st.getQuestItemsCount(CRYSTAL_MEDALLION_ID) and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) :
        if st.getQuestItemsCount(TAMATOS_NECKLACE_ID) == 1 :
          st.giveItems(MONEY_OF_SWINDLER_ID,1)
          st.takeItems(TAMATOS_NECKLACE_ID,1)
          st.set("cond","6")
          htmltext = "7428-04.htm"
        else:
          if st.getQuestItemsCount(MONEY_OF_SWINDLER_ID)>0 :
            htmltext = "7428-05.htm"
          else:
            if st.getInt("cond") > 4 :
              htmltext = "7428-06.htm"
            else:
              htmltext = "7428-01.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("409_PathToOracle")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 5032 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER_ID) == 0 :
          st.giveItems(LIZARD_CAPTAIN_ORDER_ID,1)
          st.playSound("ItemSound.quest_middle")
          st.set("cond","3")
     elif npcId == 5035 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(TAMATOS_NECKLACE_ID) == 0 :
          st.giveItems(TAMATOS_NECKLACE_ID,1)
          st.playSound("ItemSound.quest_middle")
          st.set("cond","5")
   return

QUEST = Quest(409,"409_PathToOracle","Path To Oracle")
QUEST.addStartNpc(7293)

QUEST.addTalkId(7293)
QUEST.addTalkId(7424)
QUEST.addTalkId(7428)

QUEST.addKillId(5032)
QUEST.addKillId(5033)
QUEST.addKillId(5034)
QUEST.addKillId(5035)