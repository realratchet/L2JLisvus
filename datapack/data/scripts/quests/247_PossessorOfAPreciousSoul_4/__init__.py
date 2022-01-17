# Made by disKret
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#NPC
CARADINE = 8740
LADY_OF_LAKE = 8745

#QUEST ITEM
CARADINE_LETTER_LAST = 7679
NOBLESS_TIARA = 7694

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond") 
   if event == "8740-3.htm" :
     if cond == 0 :
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
   elif event == "8740-5.htm" :
     if cond == 1 :
       st.set("cond","2")
       st.takeItems(CARADINE_LETTER_LAST,1)
       st.getPlayer().teleToLocation(143209,43968,-3038)
   elif event == "8745-5.htm" :
     if cond == 2 :
       st.set("cond","0")
       st.getPlayer().setNoble(True)
       st.giveItems(NOBLESS_TIARA,1)
       st.playSound("ItemSound.quest_finish")
       st.setState(State.COMPLETED)
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   if npcId != CARADINE and id != State.STARTED :
     return htmltext
   cond = st.getInt("cond")
   if id == State.CREATED :
     st.set("cond","0")
   if st.getPlayer().isSubClassActive() :
     if npcId == CARADINE :
         if st.getQuestItemsCount(CARADINE_LETTER_LAST) == 1 :
           if cond in [0,1] :
             if id == State.COMPLETED :
               htmltext = JQuest.getAlreadyCompletedMsg()
             elif st.getPlayer().getLevel() < 75 : 
               htmltext = "8740-2.htm"
               st.exitQuest(1)
             elif st.getPlayer().getLevel() >= 75 :
               htmltext = "8740-1.htm"
         elif cond == 2 :
             htmltext = "8740-6.htm"
     elif npcId == LADY_OF_LAKE and cond == 2 :
         htmltext = "8745-1.htm"
   else :
     htmltext = "<html><body>This quest may only be undertaken by sub-class characters of level 75 or above.</body></html>"
   return htmltext

QUEST = Quest(247,"247_PossessorOfAPreciousSoul_4","Possessor Of A Precious Soul - 4")
QUEST.addStartNpc(CARADINE)

QUEST.addTalkId(CARADINE)
QUEST.addTalkId(LADY_OF_LAKE)