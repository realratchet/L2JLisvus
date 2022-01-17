# Made by Mr. Have fun! - Version 0.3 by kmarty
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

KASHA_PARASITE_ID = 1480
KASHA_CRYSTAL_ID = 1481
HESTUIS_TOTEM_ID = 1500
LEATHER_PANTS_ID = 29

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [KASHA_CRYSTAL_ID, KASHA_PARASITE_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      htmltext = "7571-03.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if npcId == 7571 and st.getInt("cond")==0 :
      if st.getPlayer().getRace().ordinal() != 3 :
        htmltext = "7571-00.htm"
        st.exitQuest(1)
      elif st.getPlayer().getLevel() < 15 :
        htmltext = "7571-01.htm"
        st.exitQuest(1)
      else:
         htmltext = "7571-02.htm"
   elif npcId == 7571 and st.getInt("cond") :
      if st.getQuestItemsCount(KASHA_CRYSTAL_ID) == 0 :
        htmltext = "7571-04.htm"
      else:
        htmltext = "7571-05.htm"
        st.exitQuest(1)
        st.playSound("ItemSound.quest_finish")
        st.takeItems(KASHA_CRYSTAL_ID,-1)
        st.takeItems(KASHA_PARASITE_ID,-1)
        st.giveItems(HESTUIS_TOTEM_ID,1)
        st.giveItems(LEATHER_PANTS_ID,1)
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("276_HestuiTotem")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 479 :
        if st.getInt("cond")==1 and st.getQuestItemsCount(KASHA_CRYSTAL_ID) == 0 :
          count = st.getQuestItemsCount(KASHA_PARASITE_ID)
          random = st.getRandom(100)
          if (count >= 70 and random < 90) or \
             (count >= 65 and random < 75) or \
             (count >= 60 and random < 60) or \
             (count >= 52 and random < 45) or \
             (count >  50 and random < 30) :
                  st.addSpawn(5044)
                  st.takeItems(KASHA_PARASITE_ID,count)
          else :
                  st.giveItems(KASHA_PARASITE_ID,1)
                  st.playSound("ItemSound.quest_itemget")
     elif npcId == 5044 :
        if st.getInt("cond")==1 and st.getQuestItemsCount(KASHA_CRYSTAL_ID) == 0 :
          st.giveItems(KASHA_CRYSTAL_ID,1)
          st.playSound("ItemSound.quest_middle")
          st.set("cond","2")
   return

QUEST = Quest(276,"276_HestuiTotem","Hestui Totem")
QUEST.addStartNpc(7571)

QUEST.addTalkId(7571)

QUEST.addKillId(479)
QUEST.addKillId(5044)