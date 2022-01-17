# Made by mtrix
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

BEAR_SKIN = 4259
ADENA = 57
CHANCE = 400000

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [BEAR_SKIN]

 def onEvent (self,event,st) :
     htmltext = event
     if event == "7078-02.htm" :
        st.setState(State.STARTED)
        st.set("cond","1")
        st.playSound("ItemSound.quest_accept")
     return htmltext

 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     level = st.getPlayer().getLevel()
     cond = st.getInt("cond")
     if id == State.CREATED :
         if level>=20 :
             htmltext = "7078-01.htm"
         else:
             htmltext = "<html><body>This quest can only be taken by characters of level 20 and higher!</body></html>"
             st.exitQuest(1)
     elif cond==1 :
         if st.getQuestItemsCount(BEAR_SKIN)>=20 :
            htmltext = "7078-04.htm"
            st.giveItems(ADENA,3710)
            st.takeItems(BEAR_SKIN,-1)
            st.playSound("ItemSound.quest_finish")
            st.exitQuest(1)
         else :
            htmltext = "7078-03.htm"
     return htmltext

 def onKill (self,npc,player,isPet):
     st = player.getQuestState("341_HuntingForWildBeasts")
     if st :
       if st.getState() != State.STARTED : return
       npcId = npc.getNpcId()
       cond = st.getInt("cond")
       if cond==1 :
           st.dropItems(BEAR_SKIN,1,20,CHANCE,1)
     return

QUEST = Quest(341,"341_HuntingForWildBeasts","Hunting For Wild Beasts")
QUEST.addStartNpc(7078)

QUEST.addTalkId(7078)

QUEST.addKillId(21)
QUEST.addKillId(203)
QUEST.addKillId(310)
QUEST.addKillId(335)