# This is essentially a DrLecter's copy&paste from
# a Kilkenny's contribution to the Official L2J Datapack Project(adapted for L2JLisvus by roko91).
# Visit http://www.l2jdp.com/trac if you find a bug.

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "52_WilliesSpecialBait"

#NPC
WILLIE = 8574
#ITEMS
TARLK_EYE = 7623
#REWARDS
EARTH_FISHING_LURE = 7612
#MOB
TARLK_BASILISK = 573

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [TARLK_EYE]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "8574-03.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "8574-07.htm" and st.getQuestItemsCount(TARLK_EYE) == 100 :
     htmltext = "8574-06.htm"
     st.giveItems(EARTH_FISHING_LURE,4)
     st.takeItems(TARLK_EYE,-1)
     st.playSound("ItemSound.quest_finish")
     st.unset("cond")
     st.setState(State.COMPLETED)
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   cond = st.getInt("cond")
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif cond == 0 :
      if st.getPlayer().getLevel() >= 48 :
         htmltext = "8574-01.htm"
      else:
         htmltext = "8574-02.htm"
         st.exitQuest(1)
   elif id == State.STARTED :
      if st.getQuestItemsCount(TARLK_EYE) == 100 :
         htmltext = "8574-04.htm"
      else :
         htmltext = "8574-05.htm"
   return htmltext

 def onKill(self,npc,player,isPet):
   partyMember = self.getRandomPartyMember(player,npc,"1")
   if not partyMember : return
   st = partyMember.getQuestState(qn)
   if st :
      count = st.getQuestItemsCount(TARLK_EYE)
      if st.getInt("cond") == 1 and count < 100 :
         chance = 33 * Config.RATE_DROP_QUEST
         numItems, chance = divmod(chance,100)
         if st.getRandom(100) < chance :
            numItems += 1
         if numItems :
            if count + numItems >= 100 :
               numItems = 100 - count
               st.playSound("ItemSound.quest_middle")
               st.set("cond","2")
            else:
               st.playSound("ItemSound.quest_itemget")
            st.giveItems(TARLK_EYE,1)
   return

QUEST = Quest(52,qn,"Willie's Special Bait")
QUEST.addStartNpc(WILLIE)

QUEST.addTalkId(WILLIE)

QUEST.addKillId(TARLK_BASILISK)