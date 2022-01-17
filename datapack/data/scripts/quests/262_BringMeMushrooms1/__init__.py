# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

FUNGUS_SAC = 707
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [FUNGUS_SAC]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7137-03.htm" :
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
     if st.getPlayer().getLevel() >= 8 :
       htmltext = "7137-02.htm"
     else:
       htmltext = "7137-01.htm"
       st.exitQuest(1)
   else :
     if st.getQuestItemsCount(FUNGUS_SAC)<10 :
       htmltext = "7137-04.htm"
     else :
       st.giveItems(ADENA,3000)
       st.takeItems(FUNGUS_SAC,-1)
       st.exitQuest(1)
       st.playSound("ItemSound.quest_finish")
       htmltext = "7137-05.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("262_BringMeMushrooms1")
   if st :
     if st.getState() != State.STARTED : return
     count = st.getQuestItemsCount(FUNGUS_SAC)
     chance = 3
     if npc.getNpcId() == 400 : chance += 1
     if count < 10 and st.getRandom(10) < chance :
       st.giveItems(FUNGUS_SAC,1)
       if count == 9 :
         st.playSound("ItemSound.quest_middle")
         st.set("cond","2")
       else :
         st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(262,"262_BringMeMushrooms1","Bring Me Mushrooms1")
QUEST.addStartNpc(7137)

QUEST.addTalkId(7137)

QUEST.addKillId(400)
QUEST.addKillId(7)