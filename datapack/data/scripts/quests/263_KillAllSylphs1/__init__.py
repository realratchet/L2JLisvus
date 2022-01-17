# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ORC_AMULET = 1116
ORC_NECKLACE = 1117
ADENA_ID = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ORC_AMULET, ORC_NECKLACE]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7346-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7346-06.htm" :
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getRace().ordinal() != 2 :
       htmltext = "7346-00.htm"
       st.exitQuest(1)
     elif st.getPlayer().getLevel()<8 :
       htmltext = "7346-01.htm"
       st.exitQuest(1)
     else :
       htmltext = "7346-02.htm"
   else :
     amulet = st.getQuestItemsCount(ORC_AMULET)
     necklace = st.getQuestItemsCount(ORC_NECKLACE)
     if amulet == necklace == 0 :
       htmltext = "7346-04.htm"
     else :
       htmltext = "7346-05.htm"
       st.giveItems(ADENA_ID,amulet*20+necklace*30)
       st.takeItems(ORC_AMULET,-1)
       st.takeItems(ORC_NECKLACE,-1)
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("263_KillAllSylphs1")
   if st :
     if st.getState() != State.STARTED : return
     item=ORC_NECKLACE
     if npc.getNpcId() == 385 :
       item = ORC_AMULET
     if st.getRandom(10)>4 :
       st.giveItems(item,1)
       st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(263,"263_KillAllSylphs1","Kill All Sylphs1")
QUEST.addStartNpc(7346)

QUEST.addTalkId(7346)

QUEST.addKillId(385)
QUEST.addKillId(386)
QUEST.addKillId(387)
QUEST.addKillId(388)