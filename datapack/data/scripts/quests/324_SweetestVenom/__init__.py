# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

VENOM_SAC = 1077
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [VENOM_SAC]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7351-04.htm" :
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
     if st.getPlayer().getLevel() >= 18 :
       htmltext = "7351-03.htm"
     else:
       htmltext = "7351-02.htm"
       st.exitQuest(1)
   else :
     if st.getQuestItemsCount(VENOM_SAC)<10 :
       htmltext = "7351-05.htm"
     else :
       st.takeItems(VENOM_SAC,-1)
       st.giveItems(ADENA,5810)
       st.exitQuest(1)
       st.playSound("ItemSound.quest_finish")
       htmltext = "7351-06.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("324_SweetestVenom")
   if st :
     if st.getState() != State.STARTED : return
     chance=12*(1+((npc.getNpcId()^34)/4))
     count=st.getQuestItemsCount(VENOM_SAC)
     if count < 10 and st.getRandom(100) < chance :
       st.giveItems(VENOM_SAC,1)
       if count == 9 :
         st.playSound("ItemSound.quest_middle")
         st.set("cond","2")
       else :
         st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(324,"324_SweetestVenom","Sweetest Venom")
QUEST.addStartNpc(7351)

QUEST.addTalkId(7351)

QUEST.addKillId(34)
QUEST.addKillId(38)
QUEST.addKillId(43)