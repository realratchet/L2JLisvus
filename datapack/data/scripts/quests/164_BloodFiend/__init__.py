# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

KIRUNAK_SKULL_ID = 1044
ADENA_ID = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [KIRUNAK_SKULL_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("id","0")
        htmltext = "7149-04.htm"
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
   elif npcId == 7149 and st.getInt("cond")==0 :
      if st.getPlayer().getRace().ordinal() != 1 and st.getPlayer().getRace().ordinal() != 3 and st.getPlayer().getRace().ordinal() != 4 and st.getPlayer().getRace().ordinal() != 0 :
        htmltext = "7149-00.htm"
      elif st.getPlayer().getLevel() >= 21 :
        htmltext = "7149-03.htm"
        return htmltext
      else:
        htmltext = "7149-02.htm"
        st.exitQuest(1)
   elif npcId == 7149 and st.getInt("cond") == 1 :
      if st.getQuestItemsCount(KIRUNAK_SKULL_ID)<1 :
        htmltext = "7149-05.htm"
      elif st.getQuestItemsCount(KIRUNAK_SKULL_ID) >= 1 :
          if st.getInt("id") != 164 :
            st.set("id","164")
            htmltext = "7149-06.htm"
            st.giveItems(ADENA_ID,42000)
            st.takeItems(KIRUNAK_SKULL_ID,1)
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("164_BloodFiend")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 5021 :
        st.set("id","0")
        if st.getInt("cond") == 1 and st.getQuestItemsCount(KIRUNAK_SKULL_ID) == 0 :
          st.giveItems(KIRUNAK_SKULL_ID,1)
          st.playSound("ItemSound.quest_middle")
   return

QUEST = Quest(164,"164_BloodFiend","Blood Fiend")
QUEST.addStartNpc(7149)

QUEST.addTalkId(7149)

QUEST.addKillId(5021)