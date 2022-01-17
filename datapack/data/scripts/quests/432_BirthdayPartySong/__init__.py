# Created by CubicVirtuoso
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

MELODY_MAESTRO_OCTAVIA_ID = 8043
RED_CRYSTALS_ID = 7541
ROUGH_HEWN_ROCK_GOLEMS_ID = 1103
BIRTHDAY_ECHO_CRYSTAL_ID = 7061

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [RED_CRYSTALS_ID]
 
 def onEvent (self,event,st) :
     htmltext = event
     if event == "1" :
         htmltext = "8043-02.htm"
         st.set("cond","1")
         st.setState(State.STARTED)
         st.playSound("ItemSound.quest_accept")
     elif event == "3" :
         st.giveItems(BIRTHDAY_ECHO_CRYSTAL_ID,25)
         st.takeItems(RED_CRYSTALS_ID,50)
         htmltext = "8043-05.htm"
         st.set("cond","0")
         st.setState(State.COMPLETED)
         st.playSound("ItemSound.quest_finish")
     return htmltext
 
 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     if id == State.CREATED :
         st.set("cond","0")
         st.set("onlyone","0")
         htmltext = "8043-01.htm"
     elif npcId == 8043 and st.getInt("cond")==1 :
         htmltext = "8043-03.htm"
     elif npcId == 8043 and st.getInt("cond")==2 :
         htmltext = "8043-04.htm"
     
     return htmltext
 
 def onKill(self,npc,player,isPet):
     st = player.getQuestState("432_BirthdayPartySong")
     if st :
       if st.getState() != State.STARTED : return
       npcId = npc.getNpcId()     
       if npcId == 1103 :
         if st.getInt("cond")==1 and st.getQuestItemsCount(RED_CRYSTALS_ID) < 50 :
             st.giveItems(RED_CRYSTALS_ID,1)
             if st.getQuestItemsCount(RED_CRYSTALS_ID) == 50 :
                 st.playSound("ItemSound.quest_middle")
                 st.set("cond","2")
             else :
                 st.playSound("ItemSound.quest_itemget")
     return
 
QUEST = Quest(432,"432_BirthdayPartySong","Birthday Party Song")
QUEST.addStartNpc(8043)

QUEST.addTalkId(8043)

QUEST.addKillId(1103)