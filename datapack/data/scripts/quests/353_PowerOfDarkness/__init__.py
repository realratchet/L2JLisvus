# Power of Darkness - Version 0.1 by DrLecter (adapted for L2JLisvus by roko91)
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "353_PowerOfDarkness"

#NPC
GALMAN=8044
#Items
STONE=5862
ADENA=57
#BASE CHANCE FOR DROP
CHANCE = 50

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [STONE]

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond")
   if event == "8044-04.htm" and cond == 0 :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "8044-08.htm" :
     st.exitQuest(1)
     st.playSound("ItemSound.quest_finish")
   return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   level = st.getPlayer().getLevel()
   cond=st.getInt("cond")
   if id == State.CREATED :
     if level>=55 :
       htmltext = "8044-02.htm"
     else:
       htmltext = "8044-01.htm"
       st.exitQuest(1)
   else :
     stone=st.getQuestItemsCount(STONE)
     if not stone :
       htmltext = "8044-05.htm"
     else :
       st.giveItems(ADENA,2500+230*stone)
       st.takeItems(STONE,-1)
       htmltext = "8044-06.htm"
   return htmltext

 def onKill(self,npc,player,isPet):
   st = player.getQuestState(qn)
   if not st : return 
   if st.getState() != State.STARTED :
     return
   if st.getRandom(100) < CHANCE :
     st.giveItems(STONE,1)
     st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(353,qn,"Power of Darkness")
QUEST.addStartNpc(GALMAN)

QUEST.addTalkId(GALMAN)

for mob in [284,245,244,283] :
    QUEST.addKillId(mob)