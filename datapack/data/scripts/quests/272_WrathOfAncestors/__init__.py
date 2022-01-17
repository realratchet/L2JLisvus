# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

GRAVE_ROBBERS_HEAD = 1474
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [GRAVE_ROBBERS_HEAD]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7572-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond") == 0 :
     if st.getPlayer().getRace().ordinal() != 3 :
        htmltext = "7572-00.htm"
        st.exitQuest(1)
     else :
        if st.getPlayer().getLevel() < 5 :
          htmltext = "7572-01.htm"
          st.exitQuest(1)
        else:
          htmltext = "7572-02.htm"
   else :
     if st.getQuestItemsCount(GRAVE_ROBBERS_HEAD) < 50 :
        htmltext = "7572-04.htm"
     else:
        htmltext = "7572-05.htm"
        st.exitQuest(1)
        st.playSound("ItemSound.quest_finish")
        st.giveItems(ADENA,1500)
        st.takeItems(GRAVE_ROBBERS_HEAD,-1)
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("272_WrathOfAncestors")
   if st :
     if st.getState() != State.STARTED : return
     count = st.getQuestItemsCount(GRAVE_ROBBERS_HEAD)  
     if count < 50 :
        st.giveItems(GRAVE_ROBBERS_HEAD,1)
        if count < 49 :
           st.playSound("ItemSound.quest_itemget")
        else:
           st.playSound("ItemSound.quest_middle")
           st.set("cond","2")
   return

QUEST = Quest(272,"272_WrathOfAncestors","Wrath Of Ancestors")
QUEST.addStartNpc(7572)

QUEST.addTalkId(7572)

QUEST.addKillId(319)
QUEST.addKillId(320)