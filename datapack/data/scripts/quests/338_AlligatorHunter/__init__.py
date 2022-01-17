# Made by mtrix
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ADENA = 57
ALLIGATOR = 135
ALLIGATOR_PELTS = 4337
CHANCE = 90

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ALLIGATOR_PELTS]

 def onEvent (self,event,st) :
     htmltext = event
     if event == "7892-00a.htm" :
         htmltext = "7892-00a.htm"
         st.exitQuest(1)
     elif event == "7892-02.htm" :
         st.setState(State.STARTED)
         st.set("cond","1")
         st.playSound("ItemSound.quest_accept")
     elif event == "2" :
         st.exitQuest(1)
         st.playSound("ItemSound.quest_finish")
     return htmltext

 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     level = st.getPlayer().getLevel()
     cond = st.getInt("cond")
     amount = st.getQuestItemsCount(ALLIGATOR_PELTS)*40
     if id == State.CREATED :
        if level>=40 :
           htmltext = "7892-01.htm"
        else :
           htmltext = "7892-00.htm"
     elif cond==1 :
        if amount :
           htmltext = "7892-03.htm"
           st.giveItems(ADENA,amount)
           st.takeItems(ALLIGATOR_PELTS,-1)
        else :
           htmltext = "7892-04.htm"
     return htmltext

 def onKill (self,npc,player,isPet):
     st = player.getQuestState("338_AlligatorHunter")
     if st :
       if st.getState() != State.STARTED : return
       npcId = npc.getNpcId()
       if st.getRandom(100)<CHANCE :
           st.giveItems(ALLIGATOR_PELTS,1)
           st.playSound("ItemSound.quest_itemget")
     return

QUEST = Quest(338,"338_AlligatorHunter","Alligator Hunter")
QUEST.addStartNpc(7892)

QUEST.addTalkId(7892)

QUEST.addKillId(ALLIGATOR)