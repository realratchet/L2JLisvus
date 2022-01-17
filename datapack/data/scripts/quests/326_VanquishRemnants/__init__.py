# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

RED_CROSS_BADGE,BLUE_CROSS_BADGE,BLACK_CROSS_BADGE, = range(1359,1362)
ADENA = 57
BLACK_LION_MARK = 1369

DROPLIST={
53:[RED_CROSS_BADGE,25],
437:[RED_CROSS_BADGE,25],
58:[RED_CROSS_BADGE,25],
61:[BLUE_CROSS_BADGE,25],
63:[BLUE_CROSS_BADGE,25],
436:[BLUE_CROSS_BADGE,25],
439:[BLUE_CROSS_BADGE,25],
438:[BLACK_CROSS_BADGE,35],
66:[BLACK_CROSS_BADGE,25],
}

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [RED_CROSS_BADGE, BLUE_CROSS_BADGE, BLACK_CROSS_BADGE]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7435-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7435_1" :
      htmltext = "7435-07.htm"
      st.playSound("ItemSound.quest_finish")
      st.exitQuest(1)
    elif event == "7435_2" :
      htmltext = "7435-08.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getLevel() >= 21 :
       htmltext = "7435-02.htm"
     else:
       htmltext = "7435-01.htm"
       st.exitQuest(1)
   else :
     red=st.getQuestItemsCount(RED_CROSS_BADGE)
     blue=st.getQuestItemsCount(BLUE_CROSS_BADGE)
     black=st.getQuestItemsCount(BLACK_CROSS_BADGE)
     if red+blue+black == 0 :
       htmltext = "7435-04.htm"
     else :
       htmltext = "7435-05.htm"
       st.giveItems(ADENA,60*red+65*blue+70*black)
       st.takeItems(RED_CROSS_BADGE,-1)
       st.takeItems(BLUE_CROSS_BADGE,-1)
       st.takeItems(BLACK_CROSS_BADGE,-1)
       if red+blue+black >= 100 :
         htmltext = "7435-09.htm"
         if st.getQuestItemsCount(BLACK_LION_MARK) ==  0 :
           st.giveItems(BLACK_LION_MARK,1)
           htmltext = "7435-06.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("326_VanquishRemnants")
   if st :
     if st.getState() != State.STARTED : return
     item,chance=DROPLIST[npc.getNpcId()]
     if st.getRandom(100)<chance :
       st.giveItems(item,1)
       st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(326,"326_VanquishRemnants","Vanquish Remnants")
QUEST.addStartNpc(7435)

QUEST.addTalkId(7435)

QUEST.addKillId(436)
QUEST.addKillId(437)
QUEST.addKillId(438)
QUEST.addKillId(439)
QUEST.addKillId(53)
QUEST.addKillId(58)
QUEST.addKillId(61)
QUEST.addKillId(63)
QUEST.addKillId(66)