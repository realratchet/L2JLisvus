# Made by Mr. Have fun! Version 0.2
# Fixed by Pela Version 0.3 - Enough credits, but DrLecter was here :D
import sys 
from net.sf.l2j.gameserver.model.quest import State 
from net.sf.l2j.gameserver.model.quest import QuestState 
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest 

STARSTONE1_ID = 1572
GATEKEEPER_CHARM_ID = 1658

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [STARSTONE1_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
       if st.getPlayer().getLevel() >= 15 :
          htmltext = "7576-03.htm"
          st.set("cond","1")
          st.setState(State.STARTED)
          st.playSound("ItemSound.quest_accept")
       else :
          htmltext = "7576-01.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
      st.set("cond","0")
   if npcId == 7576 :
      if st.getInt("cond")==0 :
         htmltext = "7576-02.htm"
      elif st.getInt("cond")==1 and st.getQuestItemsCount(STARSTONE1_ID)<20 :
         htmltext = "7576-04.htm"
      elif st.getInt("cond")==2 and st.getQuestItemsCount(STARSTONE1_ID)>=20 :
         htmltext = "7576-05.htm"
         st.takeItems(STARSTONE1_ID,-1)
         st.giveItems(GATEKEEPER_CHARM_ID,2)
         st.exitQuest(1)
         st.playSound("ItemSound.quest_finish")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("277_GatekeepersOffering")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 333 :
        if st.getInt("cond") == 1 and st.getQuestItemsCount(STARSTONE1_ID) < 20 :
           if st.getRandom(2) == 0 :
              st.giveItems(STARSTONE1_ID,1)
              if st.getQuestItemsCount(STARSTONE1_ID) == 20 :
                 st.playSound("ItemSound.quest_middle")
                 st.set("cond","2")
              else :
                 st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(277,"277_GatekeepersOffering","Gatekeepers Offering")
QUEST.addStartNpc(7576)

QUEST.addTalkId(7576)

QUEST.addKillId(333)