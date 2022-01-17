# Made by Mr. Have fun! Version 0.2
# version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

COLLETTE_LETTER = 1076
NORMANS_LETTER = 1106
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [COLLETTE_LETTER, NORMANS_LETTER]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7350-04.htm" :
      st.giveItems(COLLETTE_LETTER,1)
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7255-03.htm" :
      st.set("cond","2")
      st.takeItems(COLLETTE_LETTER,1)
      st.giveItems(NORMANS_LETTER,1)
      st.giveItems(ADENA,2000)
    elif event == "7255-04.htm" :
      st.takeItems(COLLETTE_LETTER,1)
      st.giveItems(ADENA,3000)
      st.unset("cond")
      st.setState(State.COMPLETED)
      st.playSound("ItemSound.quest_finish")
    elif event == "7210-02.htm" :
      st.takeItems(NORMANS_LETTER,1)
      st.giveItems(ADENA,20000)
      st.unset("cond")
      st.setState(State.COMPLETED)
      st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if id==State.COMPLETED :
     htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7350 :
     if st.getInt("cond")==0 :
       if st.getPlayer().getLevel() >= 15 :
         htmltext = "7350-03.htm"
       else:
         htmltext = "7350-02.htm"
         st.exitQuest(1)
     elif st.getInt("cond")==1 and st.getQuestItemsCount(COLLETTE_LETTER) :
       htmltext = "7350-05.htm"
   elif npcId == 7255 :
     if st.getInt("cond")==1 and st.getQuestItemsCount(COLLETTE_LETTER) :
       htmltext = "7255-01.htm"
     elif st.getInt("cond")==2 and st.getQuestItemsCount(NORMANS_LETTER) :
       htmltext = "7255-05.htm"
   elif npcId == 7210 and st.getInt("cond")==2 and st.getQuestItemsCount(NORMANS_LETTER) :
      htmltext = "7210-01.htm"
   return htmltext

QUEST = Quest(167,"167_DwarvenKinship","Dwarven Kinship")
QUEST.addStartNpc(7350)

QUEST.addTalkId(7210)
QUEST.addTalkId(7255)
QUEST.addTalkId(7350)