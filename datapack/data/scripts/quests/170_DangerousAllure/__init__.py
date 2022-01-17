# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

NIGHTMARE_CRYSTAL = 1046

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [NIGHTMARE_CRYSTAL]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
       htmltext = "7305-04.htm"
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   cond=st.getInt("cond")
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif cond == 0 :
      if st.getPlayer().getRace().ordinal() != 2 :
         htmltext = "7305-00.htm"
         st.exitQuest(1)
      elif st.getPlayer().getLevel() > 20 :
         htmltext = "7305-03.htm"
      else:
         htmltext = "7305-02.htm"
         st.exitQuest(1)
   elif cond :
      if st.getQuestItemsCount(NIGHTMARE_CRYSTAL) :
         htmltext = "7305-06.htm"
         st.giveItems(57,102680)
         st.takeItems(NIGHTMARE_CRYSTAL,-1)
         st.setState(State.COMPLETED)
         st.playSound("ItemSound.quest_finish")
      else :
         htmltext = "7305-05.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("170_DangerousAllure")
   if st :
      npcId = npc.getNpcId()
      if st.getInt("cond") == 1 :
         st.giveItems(NIGHTMARE_CRYSTAL,1)
         st.playSound("ItemSound.quest_middle")
         st.set("cond","2")
   return

QUEST = Quest(170,"170_DangerousAllure","Dangerous Allure")
QUEST.addStartNpc(7305)

QUEST.addTalkId(7305)

QUEST.addKillId(5022)