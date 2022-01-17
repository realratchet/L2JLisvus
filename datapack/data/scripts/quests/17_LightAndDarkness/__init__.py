# Made with contributions from :
# disKret, Skeleton & DrLecter (adapted for L2JLisvus by roko91).
# this script is part of the Official L2J Datapack Project.
# Visit http://forum.l2jdp.com for more details.

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "17_LightAndDarkness"

#NPC
HIERARCH = 8517
SAINT_ALTAR_1 = 8508
SAINT_ALTAR_2 = 8509
SAINT_ALTAR_3 = 8510
SAINT_ALTAR_4 = 8511

#ITEMS
BLOOD_OF_SAINT = 7168

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond")
   blood = st.getQuestItemsCount(BLOOD_OF_SAINT)
   if event == "8517-02.htm" :
     if st.getPlayer().getLevel() >= 61 :
       st.giveItems(BLOOD_OF_SAINT,4)
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
     else :
       htmltext = "8517-02a.htm"
       st.exitQuest(1)
   if event == "8508-02.htm" and cond == 1 and blood :
     htmltext = "8508-01.htm"
     st.takeItems(BLOOD_OF_SAINT,1)
     st.set("cond","2")
     st.playSound("ItemSound.quest_middle")
   elif event == "8509-02.htm" and cond == 2 and blood :
     htmltext = "8509-01.htm"
     st.takeItems(BLOOD_OF_SAINT,1)
     st.set("cond","3")
     st.playSound("ItemSound.quest_middle")
   elif event == "8510-02.htm" and cond == 3 and blood :
     htmltext = "8510-01.htm"
     st.takeItems(BLOOD_OF_SAINT,1)
     st.set("cond","4")
     st.playSound("ItemSound.quest_middle")
   elif event == "8511-02.htm" and cond == 4 and blood :
     htmltext = "8511-01.htm"
     st.takeItems(BLOOD_OF_SAINT,1)
     st.set("cond","5")
     st.playSound("ItemSound.quest_middle")
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   cond = st.getInt("cond")
   blood = st.getQuestItemsCount(BLOOD_OF_SAINT)
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == HIERARCH :
     if cond == 0 :
        htmltext = "8517-00.htm"
     elif cond < 5 :
        if blood == 5 :
           htmltext = "8517-04.htm"
        else :
           htmltext = "8517-05.htm"
           st.exitQuest(1)
           st.playSound("ItemSound.quest_giveup")
     else :
        st.addExpAndSp(105527,0)
        st.unset("cond")
        st.setState(State.COMPLETED)
        st.playSound("ItemSound.quest_finish")
        htmltext = "8517-03.htm"
   elif id == State.STARTED :    
     if npcId == SAINT_ALTAR_1 :
        if cond == 1 :
          if blood :
             htmltext = "8508-00.htm"
          else :
             htmltext = "8508-02.htm"
        elif cond > 1 :
          htmltext = "8508-03.htm"
     elif npcId == SAINT_ALTAR_2 :
        if cond == 2 :
          if blood :
             htmltext = "8509-00.htm"
          else :
             htmltext = "8509-02.htm"
        elif cond > 2 :
          htmltext = "8509-03.htm"
     elif npcId == SAINT_ALTAR_3 :
        if cond == 3 :
          if blood :
             htmltext = "8510-00.htm"
          else :
             htmltext = "8510-02.htm"
        elif cond > 3 :
          htmltext = "8510-03.htm"
     elif npcId == SAINT_ALTAR_4 :
        if cond == 4 :
          if blood :
             htmltext = "8511-00.htm"
          else :
             htmltext = "8511-02.htm"
        elif cond > 4 :
          htmltext = "8511-03.htm"
   return htmltext

QUEST = Quest(17,qn,"Light and Darkness")
QUEST.addStartNpc(HIERARCH)

QUEST.addTalkId(HIERARCH)

for altars in range(8508,8512):
  QUEST.addTalkId(altars)