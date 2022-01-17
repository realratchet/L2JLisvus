# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

JENNIES_LETTER_ID = 1153
SENTRY_BLADE1_ID = 1154
SENTRY_BLADE2_ID = 1155
SENTRY_BLADE3_ID = 1156
OLD_BRONZE_SWORD_ID = 1157
ADENA_ID = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [SENTRY_BLADE1_ID, OLD_BRONZE_SWORD_ID, JENNIES_LETTER_ID, SENTRY_BLADE2_ID, SENTRY_BLADE3_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      st.set("id","0")
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      htmltext = "7349-03.htm"
      st.giveItems(JENNIES_LETTER_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7349 and st.getInt("cond")==0 :
      if st.getPlayer().getRace().ordinal() != 2 :
        htmltext = "7349-00.htm"
      elif st.getPlayer().getLevel() >= 3 :
        htmltext = "7349-02.htm"
        return htmltext
      else:
        htmltext = "7349-01.htm"
        st.exitQuest(1)
   elif npcId == 7349 and st.getInt("cond")==1 and st.getQuestItemsCount(JENNIES_LETTER_ID) :
        htmltext = "7349-04.htm"
   elif npcId == 7349 and st.getInt("cond")==1 and st.getQuestItemsCount(SENTRY_BLADE1_ID)==1 and st.getQuestItemsCount(SENTRY_BLADE2_ID)==1 and st.getQuestItemsCount(SENTRY_BLADE3_ID)==1 :
        htmltext = "7349-05.htm"
        st.takeItems(SENTRY_BLADE1_ID,1)
   elif npcId == 7349 and st.getInt("cond")==1 and st.getQuestItemsCount(SENTRY_BLADE1_ID)==0 and (st.getQuestItemsCount(SENTRY_BLADE2_ID)==1 or st.getQuestItemsCount(SENTRY_BLADE3_ID)==1) :
        htmltext = "7349-07.htm"
   elif npcId == 7349 and st.getInt("cond")==1 and st.getQuestItemsCount(OLD_BRONZE_SWORD_ID)==2 :
        if st.getInt("id") != 168 :
          st.set("id","168")
          htmltext = "7349-06.htm"
          st.takeItems(OLD_BRONZE_SWORD_ID,2)
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
          st.giveItems(ADENA_ID,820)
   elif npcId == 7360 and st.getInt("cond")==1 and st.getQuestItemsCount(JENNIES_LETTER_ID)==1 :
        htmltext = "7360-01.htm"
        st.takeItems(JENNIES_LETTER_ID,1)
        st.giveItems(SENTRY_BLADE1_ID,1)
        st.giveItems(SENTRY_BLADE2_ID,1)
        st.giveItems(SENTRY_BLADE3_ID,1)
   elif npcId == 7360 and st.getInt("cond")==1 and (st.getQuestItemsCount(SENTRY_BLADE1_ID)+st.getQuestItemsCount(SENTRY_BLADE2_ID)+st.getQuestItemsCount(SENTRY_BLADE3_ID))>0 :
        htmltext = "7360-02.htm"
   elif npcId == 7355 and st.getInt("cond")==1 and st.getQuestItemsCount(SENTRY_BLADE2_ID)==1 and st.getQuestItemsCount(SENTRY_BLADE1_ID)==0 :
        htmltext = "7355-01.htm"
        st.takeItems(SENTRY_BLADE2_ID,1)
        st.giveItems(OLD_BRONZE_SWORD_ID,1)
   elif npcId == 7355 and st.getInt("cond")==1 and st.getQuestItemsCount(SENTRY_BLADE2_ID)==0 :
        htmltext = "7355-02.htm"
        st.takeItems(SENTRY_BLADE2_ID,1)
   elif npcId == 7357 and st.getInt("cond")==1 and st.getQuestItemsCount(SENTRY_BLADE3_ID)==1 and st.getQuestItemsCount(SENTRY_BLADE1_ID)==0 :
        htmltext = "7357-01.htm"
        st.takeItems(SENTRY_BLADE3_ID,1)
        st.giveItems(OLD_BRONZE_SWORD_ID,1)
   elif npcId == 7357 and st.getInt("cond")==1 and st.getQuestItemsCount(SENTRY_BLADE3_ID)==0 :
        htmltext = "7357-02.htm"
        st.takeItems(SENTRY_BLADE3_ID,1)
   return htmltext

QUEST = Quest(168,"168_DeliverSupplies","Deliver Supplies")
QUEST.addStartNpc(7349)

QUEST.addTalkId(7349)
QUEST.addTalkId(7355)
QUEST.addTalkId(7357)
QUEST.addTalkId(7360)