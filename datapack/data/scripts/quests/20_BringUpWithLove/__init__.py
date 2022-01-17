# Made by Emperorc (adapted for L2JLisvus by roko91)
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "20_BringUpWithLove"

#NPCs
TUNATUN = 8537

#ITEMS 
GEM = 7185

#NOTE: This quest requires the giving of item GEM upon successful growth and taming of a wild beast, so the rewarding of
# the gem is handled by the feedable_beasts ai script.

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onEvent (self,event,st) :
   htmltext = event
   if event == "8537-09.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "8537-12.htm" :
       st.giveItems(57,68500)
       st.takeItems(GEM,-1)
       st.playSound("ItemSound.quest_finish")
       st.setState(State.COMPLETED)
       st.set("onlyone","1")
   return htmltext 

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   level = st.getPlayer().getLevel()
   cond = st.getInt("cond") 
   onlyone = st.getInt("onlyone")
   GEM_COUNT = st.getQuestItemsCount(GEM)
   if id == State.COMPLETED :
       htmltext = JQuest.getAlreadyCompletedMsg()
   elif id == State.CREATED and onlyone == 0 :
     if level >= 65 :
         htmltext = "8537-01.htm"
     else:
         htmltext = "8537-02.htm"
         st.exitQuest(1)
   elif id == State.STARTED :
       if GEM_COUNT < 1 :
           htmltext = "8537-10.htm"
       else :
           htmltext = "8537-11.htm"
   return htmltext

QUEST     = Quest(20,qn,"Bring up with Love")
QUEST.addStartNpc(TUNATUN)

QUEST.addTalkId(TUNATUN)