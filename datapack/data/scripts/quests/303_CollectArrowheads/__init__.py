# Made by Mr. Have fun! - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ORCISH_ARROWHEAD = 963
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ORCISH_ARROWHEAD]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7029-04.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getLevel() >= 10 :
        htmltext = "7029-03.htm"
     else:
        htmltext = "7029-02.htm"
        st.exitQuest(1)
   else :
     if st.getQuestItemsCount(ORCISH_ARROWHEAD)<10 :
       htmltext = "7029-05.htm"
     else :
       st.takeItems(ORCISH_ARROWHEAD,-1)
       st.playSound("ItemSound.quest_finish")
       st.giveItems(ADENA,1000)
       st.addExpAndSp(2000,0)
       htmltext = "7029-06.htm"
       st.exitQuest(1)
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("303_CollectArrowheads")
   if st :
     if st.getState() != State.STARTED : return
     count=st.getQuestItemsCount(ORCISH_ARROWHEAD)
     if count<10 and st.getRandom(100)<40 :
       st.giveItems(ORCISH_ARROWHEAD,1)
       if count == 9 :
         st.set("cond","2") 
         st.playSound("ItemSound.quest_middle")
       else:
         st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(303,"303_CollectArrowheads","Collect Arrowheads")
QUEST.addStartNpc(7029)

QUEST.addTalkId(7029)

QUEST.addKillId(361)