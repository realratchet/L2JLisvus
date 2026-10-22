# Made by disKret
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#NPC
HIERARCH = 8517

#ITEMS
BLOOD_OF_SAINT = 7169

#REWARDS
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [BLOOD_OF_SAINT]

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond")
   if event == "8517-1.htm" :
     if cond != 0 or st.getPlayer().getLevel() < 60 : return
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "8517-3.htm" :
     if cond != 2 or st.getQuestItemsCount(BLOOD_OF_SAINT) < 300 : return
     st.takeItems(BLOOD_OF_SAINT,300)
     st.set("cond","3")
   elif event in ["8517-4.htm","8517-5.htm"] :
     if cond != 3 : return
     st.playSound("ItemSound.quest_finish")
     st.exitQuest(1)
     if event == "8517-4.htm" :
       st.addExpAndSp(162773,12500)
     elif event == "8517-5.htm" :
       st.giveItems(ADENA,100000)
   else :
     return
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   cond = st.getInt("cond")
   if cond == 0 :
     if st.getPlayer().getLevel() >= 60 : # and st.getPlayer().getLevel() <= 71
       htmltext = "8517-0.htm"
     else:
       htmltext = "8517-0a.htm"
       st.exitQuest(1)
   elif st.getQuestItemsCount(BLOOD_OF_SAINT) == 300 :
     htmltext = "8517-2.htm"
   elif cond == 3 :
     htmltext = "8517-3.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   partyMember = self.getRandomPartyMember(player,npc,"1")
   if not partyMember : return
   st = partyMember.getQuestState("626_ADarkTwilight")
   if st :
     if st.getState() == State.STARTED :
        count = st.getQuestItemsCount(BLOOD_OF_SAINT)
        if count < 300 :
          st.giveItems(BLOOD_OF_SAINT,1)
          if count == 299 :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","2")
          else:
            st.playSound("ItemSound.quest_itemget")	
   return

QUEST = Quest(626,"626_ADarkTwilight","A Dark Twilight")
QUEST.addStartNpc(8517)

QUEST.addTalkId(8517)

for mobs in range(1520,1541):
  QUEST.addKillId(mobs)