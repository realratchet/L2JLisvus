# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

DELIVERY_LIST_ID = 1012
HEAVY_WOOD_BOX_ID = 1013
CLOTH_BUNDLE_ID = 1014
CLAY_POT_ID = 1015
JACKSONS_RECEIPT_ID = 1016
SILVIAS_RECEIPT_ID = 1017
RANTS_RECEIPT_ID = 1018
LESSER_HEALING_POTION_ID = 1060
RING_ID = 875

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [HEAVY_WOOD_BOX_ID, CLOTH_BUNDLE_ID, CLAY_POT_ID, DELIVERY_LIST_ID, JACKSONS_RECEIPT_ID, SILVIAS_RECEIPT_ID, RANTS_RECEIPT_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("id","0")
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        if st.getQuestItemsCount(DELIVERY_LIST_ID) == 0 :
          st.giveItems(DELIVERY_LIST_ID,1)
        if st.getQuestItemsCount(HEAVY_WOOD_BOX_ID) == 0 :
          st.giveItems(HEAVY_WOOD_BOX_ID,1)
        if st.getQuestItemsCount(CLOTH_BUNDLE_ID) == 0 :
          st.giveItems(CLOTH_BUNDLE_ID,1)
        if st.getQuestItemsCount(CLAY_POT_ID) == 0 :
          st.giveItems(CLAY_POT_ID,1)
        htmltext = "7041-04.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
        htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7041 and st.getInt("cond")==0 :
        if st.getPlayer().getLevel() >= 2 :
          htmltext = "7041-03.htm"
          return htmltext
        else:
          htmltext = "7041-02.htm"
          st.exitQuest(1)
   elif npcId == 7041 and st.getInt("cond")!=0 and (st.getQuestItemsCount(JACKSONS_RECEIPT_ID)!=0 and st.getQuestItemsCount(SILVIAS_RECEIPT_ID)!=0 and st.getQuestItemsCount(RANTS_RECEIPT_ID)!=0)==0 :
        htmltext = "7041-05.htm"
   elif npcId == 7002 and st.getInt("cond")!=0 and st.getQuestItemsCount(HEAVY_WOOD_BOX_ID)!=0 :
        st.takeItems(HEAVY_WOOD_BOX_ID,st.getQuestItemsCount(HEAVY_WOOD_BOX_ID))
        if st.getQuestItemsCount(JACKSONS_RECEIPT_ID) == 0 :
          st.giveItems(JACKSONS_RECEIPT_ID,1)
        htmltext = "7002-01.htm"
   elif npcId == 7002 and st.getInt("cond")!=0 and st.getQuestItemsCount(JACKSONS_RECEIPT_ID)!=0 :
        htmltext = "7002-02.htm"
   elif npcId == 7003 and st.getInt("cond")!=0 and st.getQuestItemsCount(CLOTH_BUNDLE_ID)!=0 :
        st.takeItems(CLOTH_BUNDLE_ID,st.getQuestItemsCount(CLOTH_BUNDLE_ID))
        if st.getQuestItemsCount(SILVIAS_RECEIPT_ID) == 0 :
          st.giveItems(SILVIAS_RECEIPT_ID,1)
          st.giveItems(LESSER_HEALING_POTION_ID,1)
        htmltext = "7003-01.htm"
   elif npcId == 7003 and st.getInt("cond")!=0 and st.getQuestItemsCount(SILVIAS_RECEIPT_ID)!=0 :
        htmltext = "7003-02.htm"
   elif npcId == 7054 and st.getInt("cond")!=0 and st.getQuestItemsCount(CLAY_POT_ID)!=0 :
        st.takeItems(CLAY_POT_ID,st.getQuestItemsCount(CLAY_POT_ID))
        if st.getQuestItemsCount(RANTS_RECEIPT_ID) == 0 :
          st.giveItems(RANTS_RECEIPT_ID,1)
        htmltext = "7054-01.htm"
   elif npcId == 7054 and st.getInt("cond")!=0 and st.getQuestItemsCount(RANTS_RECEIPT_ID)!=0 :
        htmltext = "7054-02.htm"
   elif npcId == 7041 and st.getInt("cond")!=0 and (st.getQuestItemsCount(JACKSONS_RECEIPT_ID)!=0 and st.getQuestItemsCount(SILVIAS_RECEIPT_ID)!=0 and st.getQuestItemsCount(RANTS_RECEIPT_ID)!=0)!=0 :
        if st.getInt("id") != 153 :
          st.set("id","153")
          st.takeItems(DELIVERY_LIST_ID,st.getQuestItemsCount(DELIVERY_LIST_ID))
          st.takeItems(JACKSONS_RECEIPT_ID,st.getQuestItemsCount(JACKSONS_RECEIPT_ID))
          st.takeItems(SILVIAS_RECEIPT_ID,st.getQuestItemsCount(SILVIAS_RECEIPT_ID))
          st.takeItems(RANTS_RECEIPT_ID,st.getQuestItemsCount(RANTS_RECEIPT_ID))
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
          st.giveItems(RING_ID,2)
          htmltext = "7041-06.htm"
   return htmltext

QUEST = Quest(153,"153_WharfOldtimersFavor","Wharf Oldtimers Favor")
QUEST.addStartNpc(7041)

QUEST.addTalkId(7002)
QUEST.addTalkId(7003)
QUEST.addTalkId(7041)
QUEST.addTalkId(7054)