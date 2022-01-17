# Made by Mr. Have fun! - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

GOBLIN_CLUB = 1335
SILVERY_LEAF = 1340

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [GOBLIN_CLUB]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "12092-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "12092-06.htm" :
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getRace().ordinal() != 1 :
       htmltext = "12092-00.htm"
       st.exitQuest(1)
     elif st.getPlayer().getLevel()<4 :
       htmltext = "12092-01.htm"
       st.exitQuest(1)
     else :
       htmltext = "12092-02.htm"
   else :
     count=st.getQuestItemsCount(GOBLIN_CLUB)
     if count :
       st.giveItems(SILVERY_LEAF,count)
       st.takeItems(GOBLIN_CLUB,-1)
       htmltext = "12092-05.htm"
     else:
       htmltext = "12092-04.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("267_WrathOfVerdure")
   if st :
     if st.getState() != State.STARTED : return
     if st.getRandom(10)<5 :
       st.giveItems(GOBLIN_CLUB,1)
       st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(267,"267_WrathOfVerdure","Wrath Of Verdure")
QUEST.addStartNpc(12092)

QUEST.addTalkId(12092)

QUEST.addKillId(325)