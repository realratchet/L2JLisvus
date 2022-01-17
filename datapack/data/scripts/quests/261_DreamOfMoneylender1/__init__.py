# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

GIANT_SPIDER_LEG = 1087
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [GIANT_SPIDER_LEG]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7222-03.htm" :
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
     if st.getPlayer().getLevel() >= 15 :
       htmltext = "7222-02.htm"
     else:
       htmltext = "7222-01.htm"
       st.exitQuest(1)
   else :
     if st.getQuestItemsCount(GIANT_SPIDER_LEG) >= 8 :
       st.giveItems(ADENA,1000)
       st.addExpAndSp(2000,0)
       st.takeItems(GIANT_SPIDER_LEG,-1)
       htmltext = "7222-05.htm"
       st.exitQuest(1)
       st.playSound("ItemSound.quest_finish")
     else:
       htmltext = "7222-04.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("261_DreamOfMoneylender1")
   if st :
     if st.getState() != State.STARTED : return
     count = st.getQuestItemsCount(GIANT_SPIDER_LEG)
     if count < 8 :
       st.giveItems(GIANT_SPIDER_LEG,1)
       if count == 7 :
         st.playSound("ItemSound.quest_middle")
         st.set("cond","2")
       else:
         st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(261,"261_DreamOfMoneylender1","Dream Of Moneylender1")
QUEST.addStartNpc(7222)

QUEST.addTalkId(7222)

QUEST.addKillId(308)
QUEST.addKillId(460)
QUEST.addKillId(466)