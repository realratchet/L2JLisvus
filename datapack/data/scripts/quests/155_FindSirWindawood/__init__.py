# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

OFFICIAL_LETTER_ID = 1019
HASTE_POTION_ID = 734

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [OFFICIAL_LETTER_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      st.set("id","0")
      st.giveItems(OFFICIAL_LETTER_ID,1)
      htmltext = "7042-04.htm"
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7042 :
      if st.getInt("cond") == 0 :
        if st.getPlayer().getLevel() >= 3 :
          htmltext = "7042-03.htm"
          return htmltext
        else:
          htmltext = "7042-02.htm"
          st.exitQuest(1)
      elif st.getInt("cond")==1 and st.getQuestItemsCount(OFFICIAL_LETTER_ID)==1 :
        htmltext = "7042-05.htm"
   elif npcId == 7311 and st.getInt("cond")==1 and st.getQuestItemsCount(OFFICIAL_LETTER_ID)==1 :
      if st.getInt("id") != 155 :
        st.set("id","155")
        st.takeItems(OFFICIAL_LETTER_ID,-1)
        st.giveItems(HASTE_POTION_ID,1)
        st.set("cond","0")
        st.setState(State.COMPLETED)
        st.playSound("ItemSound.quest_finish")
        htmltext = "7311-01.htm"
   return htmltext

QUEST = Quest(155,"155_FindSirWindawood","Find Sir Windawood")
QUEST.addStartNpc(7042)

QUEST.addTalkId(7042)
QUEST.addTalkId(7311)