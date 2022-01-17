# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

KASHA_WOLF_FANG = 1473
NECKLACE_OF_VALOR = 1507
NECKLACE_OF_COURAGE = 1506

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [KASHA_WOLF_FANG]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7577-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      if st.getQuestItemsCount(NECKLACE_OF_COURAGE) or st.getQuestItemsCount(NECKLACE_OF_VALOR) :
        htmltext = "7577-07.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if id == State.COMPLETED :
     htmltext = "7577-06.htm"
   elif st.getInt("cond") == 0 :
     if st.getPlayer().getRace().ordinal() != 3 :
        htmltext = "7577-00.htm"
        st.exitQuest(1)
     else :
        if st.getPlayer().getLevel() < 4 :
           htmltext = "7577-01.htm"
           st.exitQuest(1)
        else :
           htmltext = "7577-02.htm"
   elif st.getInt("cond") == 1 :
     htmltext = "7577-04.htm"
   elif st.getQuestItemsCount(KASHA_WOLF_FANG) >= 50 :
     st.set("cond","0")
     st.setState(State.COMPLETED)
     st.playSound("ItemSound.quest_finish")
     st.takeItems(KASHA_WOLF_FANG,-1)
     if st.getRandom(100) <= 13 :
        st.giveItems(NECKLACE_OF_VALOR,1)
     else :
        st.giveItems(NECKLACE_OF_COURAGE,1)
     htmltext = "7577-05.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("271_ProofOfValor")
   if st :
     if st.getState() != State.STARTED : return
     count = st.getQuestItemsCount(KASHA_WOLF_FANG)
     if count < 50 :
        numItems, chance = divmod(125*Config.RATE_DROP_QUEST,100)
        if st.getRandom(100) <= chance :
           numItems += 1
        numItems = int(numItems)
        if numItems != 0 :
           if 50 <= (count + numItems) :
              numItems = 50 - count
              st.playSound("ItemSound.quest_middle")
              st.set("cond","2")
           else :
              st.playSound("ItemSound.quest_itemget")
           st.giveItems(KASHA_WOLF_FANG,numItems)
   return

QUEST = Quest(271,"271_ProofOfValor","Proof Of Valor")
QUEST.addStartNpc(7577)

QUEST.addTalkId(7577)

QUEST.addKillId(475)