# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

UNDRES_LETTER_ID = 1088
CEREMONIAL_DAGGER_ID = 1089
DREVIANT_WINE_ID = 1090
GARMIELS_SCRIPTURE_ID = 1091
ADENA_ID = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [CEREMONIAL_DAGGER_ID, DREVIANT_WINE_ID, GARMIELS_SCRIPTURE_ID, UNDRES_LETTER_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7130-04.htm" :
       st.giveItems(UNDRES_LETTER_ID,1)
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
     if st.getPlayer().getRace().ordinal() == 2 :
        if st.getPlayer().getLevel() >= 2 :
           htmltext = "7130-03.htm"
           return htmltext
        else :
           htmltext = "7130-00.htm"
           st.exitQuest(1)
     else:
        htmltext = "7130-02.htm"
        st.exitQuest(1)
   elif id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7130 :
      if st.getInt("cond") and st.getQuestItemsCount(UNDRES_LETTER_ID):
         if (st.getQuestItemsCount(GARMIELS_SCRIPTURE_ID)+st.getQuestItemsCount(DREVIANT_WINE_ID)+st.getQuestItemsCount(CEREMONIAL_DAGGER_ID)==0) :
            htmltext = "7130-05.htm"
         elif st.getQuestItemsCount(CEREMONIAL_DAGGER_ID)==st.getQuestItemsCount(DREVIANT_WINE_ID)==st.getQuestItemsCount(GARMIELS_SCRIPTURE_ID)==1 :
            htmltext = "7130-06.htm"
            st.takeItems(CEREMONIAL_DAGGER_ID,1)
            st.takeItems(DREVIANT_WINE_ID,1)
            st.takeItems(GARMIELS_SCRIPTURE_ID,1)
            st.takeItems(UNDRES_LETTER_ID,1)
            st.giveItems(ADENA_ID,500)
            st.addExpAndSp(500,0)
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
   elif npcId == 7135 :
      if st.getQuestItemsCount(UNDRES_LETTER_ID) :
         if not st.getQuestItemsCount(CEREMONIAL_DAGGER_ID) :
            st.giveItems(CEREMONIAL_DAGGER_ID,1)
            htmltext = "7135-01.htm"
         else :
            htmltext = "7135-02.htm"
   elif npcId == 7139 :
      if st.getQuestItemsCount(UNDRES_LETTER_ID) :
         if not st.getQuestItemsCount(DREVIANT_WINE_ID) :
            st.giveItems(DREVIANT_WINE_ID,1)
            htmltext = "7139-01.htm"
         else :
            htmltext = "7139-02.htm"
   elif npcId == 7143 :
      if st.getQuestItemsCount(UNDRES_LETTER_ID) :
         if not st.getQuestItemsCount(GARMIELS_SCRIPTURE_ID) :
            st.giveItems(GARMIELS_SCRIPTURE_ID,1)
            htmltext = "7143-01.htm"
         else :
            htmltext = "7143-02.htm"
   return htmltext

QUEST = Quest(166,"166_DarkMass","Dark Mass")
QUEST.addStartNpc(7130)

QUEST.addTalkId(7130)
QUEST.addTalkId(7135)
QUEST.addTalkId(7139)
QUEST.addTalkId(7143)