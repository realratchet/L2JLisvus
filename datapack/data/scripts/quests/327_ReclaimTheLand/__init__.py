# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ADENA = 57

TUREK_DOGTAG,        TUREK_MEDALLION,     CLAY_URN_FRAGMENT,    \
BRASS_TRINKET_PIECE, BRONZE_MIRROR_PIECE, JADE_NECKLACE_BEAD,   \
ANCIENT_CLAY_URN,    ANCIENT_BRASS_TIARA, ANCIENT_BRONZE_MIRROR,\
ANCIENT_JADE_NECKLACE = range(1846,1856)

EXP = {
ANCIENT_CLAY_URN:913,
ANCIENT_BRASS_TIARA:1065,
ANCIENT_BRONZE_MIRROR:1065,
ANCIENT_JADE_NECKLACE:1294
}

DROPLIST = {
501:[TUREK_MEDALLION,12],
500:[TUREK_DOGTAG,7],
499:[TUREK_DOGTAG,8],
498:[TUREK_DOGTAG,10],
497:[TUREK_MEDALLION,11],
496:[TUREK_DOGTAG,9],
495:[TUREK_MEDALLION,13]
}

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [CLAY_URN_FRAGMENT, BRASS_TRINKET_PIECE, BRONZE_MIRROR_PIECE, JADE_NECKLACE_BEAD, TUREK_DOGTAG, TUREK_MEDALLION]

 def onEvent (self,event,st) :
    htmltext = event
    n=st.getRandom(100)
    if event == "7597-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7597-06.htm" :
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    elif event == "7313-02.htm" :
      if st.getQuestItemsCount(CLAY_URN_FRAGMENT) >= 5 :
        st.takeItems(CLAY_URN_FRAGMENT,5)
        if n < 80 :
          htmltext = "7313-03.htm"
          st.giveItems(ANCIENT_CLAY_URN,1)
        else:
          htmltext = "7313-10.htm"
    elif event == "7313-04.htm" :
      if st.getQuestItemsCount(BRASS_TRINKET_PIECE) >= 5 :
        st.takeItems(BRASS_TRINKET_PIECE,5)
        if n < 80 :
          htmltext = "7313-05.htm"
          st.giveItems(ANCIENT_BRASS_TIARA,1)
        else:
          htmltext = "7313-10.htm"
    elif event == "7313-06.htm" :
      if st.getQuestItemsCount(BRONZE_MIRROR_PIECE) >= 5 :
        st.takeItems(BRONZE_MIRROR_PIECE,5)
        if n < 80 :
          htmltext = "7313-07.htm"
          st.giveItems(ANCIENT_BRONZE_MIRROR,1)
        else:
          htmltext = "7313-10.htm"
    elif event == "7313-08.htm" :
      if st.getQuestItemsCount(JADE_NECKLACE_BEAD) >= 5 :
        st.takeItems(JADE_NECKLACE_BEAD,5)
        if n < 80 :
          htmltext = "7313-09.htm"
          st.giveItems(ANCIENT_JADE_NECKLACE,1)
        else:
          htmltext = "7313-10.htm"
    elif event == "7034-03.htm" :
      n = st.getQuestItemsCount(CLAY_URN_FRAGMENT)
      if n == 0 :
        htmltext = "7034-02.htm"
      else:
        st.takeItems(CLAY_URN_FRAGMENT,n)
        st.addExpAndSp(n*152,0)
        st.playSound("ItemSound.quest_itemget")
    elif event == "7034-04.htm" :
      n = st.getQuestItemsCount(BRASS_TRINKET_PIECE)
      if n == 0 :
        htmltext = "7034-02.htm"
      else:
        st.takeItems(BRASS_TRINKET_PIECE,n)
        st.addExpAndSp(n*182,0)
        st.playSound("ItemSound.quest_itemget")
    elif event == "7034-05.htm" :
      n = st.getQuestItemsCount(BRONZE_MIRROR_PIECE)
      if n == 0 :
        htmltext = "7034-02.htm"
      else:
        st.takeItems(BRONZE_MIRROR_PIECE,n)
        st.addExpAndSp(n*182,0)
        st.playSound("ItemSound.quest_itemget")
    elif event == "7034-06.htm" :
      n = st.getQuestItemsCount(JADE_NECKLACE_BEAD)
      if n < 1 :
        htmltext = "7034-02.htm"
      else:
       st.takeItems(JADE_NECKLACE_BEAD,n)
       st.addExpAndSp(n*182,0)
       st.playSound("ItemSound.quest_itemget")
    elif event == "7034-07.htm" :
      n1 = 0
      for i in range(1852,1856) :
         n=st.getQuestItemsCount(i)
         if n :
           n1 = 1
           st.takeItems(i,n)
           st.addExpAndSp(n*EXP[i],0)
           st.playSound("ItemSound.quest_itemget")
      if not n1 :
        htmltext = "7034-02.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if npcId == 7597 :
     if st.getInt("cond")==0 :
       if st.getPlayer().getLevel() < 25 :
         htmltext = "7597-01.htm"
         st.exitQuest(1)
       else :
         htmltext = "7597-02.htm"
     else :
      dogtag = st.getQuestItemsCount(TUREK_DOGTAG)
      medallion = st.getQuestItemsCount(TUREK_MEDALLION)
      if dogtag + medallion == 0 :
        htmltext = "7597-04.htm"
      else:
        htmltext = "7597-05.htm"
        st.giveItems(ADENA,dogtag*40+medallion*50)
        st.takeItems(TUREK_DOGTAG,dogtag)
        st.takeItems(TUREK_MEDALLION,medallion)
   else :
      htmltext = str(npcId)+"-01.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("327_ReclaimTheLand")
   if st :
     if st.getState() != State.STARTED : return
     item,chance=DROPLIST[npc.getNpcId()]
     st.giveItems(item,1)
     st.playSound("ItemSound.quest_itemget")
     if st.getRandom(100)<chance :
       n = st.getRandom(100)
       if n < 25 :
          st.giveItems(CLAY_URN_FRAGMENT,1)
       elif n < 50 :
          st.giveItems(BRASS_TRINKET_PIECE,1)
       elif n < 75 :
          st.giveItems(BRONZE_MIRROR_PIECE,1)
       else:
          st.giveItems(JADE_NECKLACE_BEAD,1)
   return

QUEST = Quest(327,"327_ReclaimTheLand","Reclaim The Land")
QUEST.addStartNpc(7597)

QUEST.addTalkId(7034)
QUEST.addTalkId(7313)
QUEST.addTalkId(7597)

for i in range(495,502) :
    QUEST.addKillId(i)