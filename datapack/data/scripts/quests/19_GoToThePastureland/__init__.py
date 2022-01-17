# Made by disKret
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#NPC
VLADIMIR = 8302
TUNATUN = 8537

#ITEMS
BEAST_MEAT = 7547

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [BEAST_MEAT]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "8302-1.htm" :
     st.giveItems(BEAST_MEAT,1)
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   if event == "8537-1.htm" :
     st.takeItems(BEAST_MEAT,1)
     st.giveItems(57,30000)
     st.unset("cond")
     st.setState(State.COMPLETED)
     st.playSound("ItemSound.quest_finish")
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   cond = st.getInt("cond")
   if npcId == VLADIMIR :
     if cond == 0 :
       if id == State.COMPLETED :
         htmltext = JQuest.getAlreadyCompletedMsg()
       elif st.getPlayer().getLevel() >= 63 :
         htmltext = "8302-0.htm"
       else:
         htmltext = "<html><body>Quest for characters of level 63 or above.</body></html>"
         st.exitQuest(1)
     else :
       htmltext = "8302-2.htm"
   else :
       htmltext = "8537-0.htm"
   return htmltext

QUEST = Quest(19,"19_GoToThePastureland","Go To The Pastureland")
QUEST.addStartNpc(VLADIMIR)

QUEST.addTalkId(VLADIMIR)
QUEST.addTalkId(TUNATUN)