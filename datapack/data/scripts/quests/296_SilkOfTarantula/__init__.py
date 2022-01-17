# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

TARANTULA_SPIDER_SILK = 1493
TARANTULA_SPINNERETTE = 1494
RING_OF_RACCOON = 1508
RING_OF_FIREFLY = 1509
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [TARANTULA_SPIDER_SILK, TARANTULA_SPINNERETTE]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7519-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7519-06.htm" :
      st.takeItems(TARANTULA_SPINNERETTE,-1)
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    elif event == "7548-02.htm" :
      if st.getQuestItemsCount(TARANTULA_SPINNERETTE) :
        htmltext = "7548-03.htm"
        st.giveItems(TARANTULA_SPIDER_SILK,15+st.getRandom(9))
        st.takeItems(TARANTULA_SPINNERETTE,1)
    elif event == "7519-09.htm" :
      st.exitQuest(1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if npcId == 7519 :
     if st.getInt("cond")==0 :
       if st.getPlayer().getLevel() >= 15 :
         if st.getQuestItemsCount(RING_OF_RACCOON)==st.getQuestItemsCount(RING_OF_FIREFLY)==0 :
           htmltext = "7519-08.htm"
         else:
           htmltext = "7519-02.htm"
       else:
         htmltext = "7519-01.htm"
         st.exitQuest(1)
     else :
       count = st.getQuestItemsCount(TARANTULA_SPIDER_SILK)
       if count == 0 :
         htmltext = "7519-04.htm"
       else :
         htmltext = "7519-05.htm"
         st.giveItems(ADENA,count*20)
         st.takeItems(TARANTULA_SPIDER_SILK,count)
   else :
     htmltext = "7548-01.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("296_SilkOfTarantula")
   if st :
     if st.getState() != State.STARTED : return
     n = st.getRandom(100)
     if n > 95 :
       st.giveItems(TARANTULA_SPINNERETTE,1)
       st.playSound("ItemSound.quest_itemget")
     elif n > 45 :
       st.giveItems(TARANTULA_SPIDER_SILK,1)
       st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(296,"296_SilkOfTarantula","Silk Of Tarantula")
QUEST.addStartNpc(7519)

QUEST.addTalkId(7519)
QUEST.addTalkId(7548)

QUEST.addKillId(394)
QUEST.addKillId(403)
QUEST.addKillId(508)