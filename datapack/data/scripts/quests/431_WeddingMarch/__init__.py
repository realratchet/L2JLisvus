# Created by CubicVirtuoso
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

MELODY_MAESTRO_KANTABILON_ID = 8042
SILVER_CRYSTAL_ID = 7540
LIENRIKS_ID = 786
LIENRIKS_LAD_ID = 787
WEDDING_ECHO_CRYSTAL_ID = 7062

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [SILVER_CRYSTAL_ID]
 
 def onEvent (self,event,st) :
     htmltext = event
     if event == "1" :
         htmltext = "8042-02.htm"
         st.set("cond","1")
         st.setState(State.STARTED)
         st.playSound("ItemSound.quest_accept")
     elif event == "3" :
         st.giveItems(WEDDING_ECHO_CRYSTAL_ID,25)
         st.takeItems(SILVER_CRYSTAL_ID,50)
         htmltext = "8042-05.htm"
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
         htmltext = "8042-01.htm"
     elif npcId == 8042 and st.getInt("cond")==1 :
         htmltext = "8042-03.htm"
     elif npcId == 8042 and st.getInt("cond")==2 :
         htmltext = "8042-04.htm"
     return htmltext
 
 def onKill(self,npc,player,isPet):
     partyMember = self.getRandomPartyMember(player,npc,"1")
     if not partyMember : return
     st = partyMember.getQuestState("431_WeddingMarch")
     if st :
       npcId = npc.getNpcId()
       if npcId == 786 or npcId == 787 :
         if st.getQuestItemsCount(SILVER_CRYSTAL_ID)<50 :
             st.giveItems(SILVER_CRYSTAL_ID,1)
             if st.getQuestItemsCount(SILVER_CRYSTAL_ID) == 50 :
                 st.playSound("ItemSound.quest_middle")
                 st.set("cond","2")
             else :
                 st.playSound("ItemSound.quest_itemget")
     return
 
QUEST = Quest(431,"431_WeddingMarch","Wedding March")
QUEST.addStartNpc(8042)

QUEST.addTalkId(8042)

QUEST.addKillId(786)
QUEST.addKillId(787)