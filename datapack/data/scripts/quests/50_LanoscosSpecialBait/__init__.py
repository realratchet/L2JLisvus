# This is essentially a DrLecter's copy&paste from
# a Kilkenny's contribution to the Official L2J Datapack Project(adapted for L2JLisvus by roko91).
# Visit http://www.l2jdp.com/trac if you find a bug.

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "50_LanoscosSpecialBait"

#NPC
LANOSCO = 8570
#ITEMS
ESSENCE_OF_WIND = 7621
#REWARDS
WIND_FISHING_LURE = 7610
#MOB
SINGING_WIND = 1026

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ESSENCE_OF_WIND]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "8570-03.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "8570-07.htm" and st.getQuestItemsCount(ESSENCE_OF_WIND) == 100 :
     htmltext = "8570-06.htm"
     st.giveItems(WIND_FISHING_LURE,4)
     st.takeItems(ESSENCE_OF_WIND,-1)
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
      if st.getPlayer().getLevel() >= 27 :
         htmltext = "8570-01.htm"
      else:
         htmltext = "8570-02.htm"
         st.exitQuest(1)
   elif id == State.STARTED :
      if st.getQuestItemsCount(ESSENCE_OF_WIND) == 100 :
         htmltext = "8570-04.htm"
      else :
         htmltext = "8570-05.htm"
   return htmltext

 def onKill(self,npc,player,isPet):
   partyMember = self.getRandomPartyMember(player,npc,"1")
   if not partyMember : return
   st = partyMember.getQuestState(qn)
   if st :
      count = st.getQuestItemsCount(ESSENCE_OF_WIND)
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
            st.giveItems(ESSENCE_OF_WIND,1)
   return

QUEST = Quest(50,qn,"Lanosco's Special Bait")
QUEST.addStartNpc(LANOSCO)
QUEST.addTalkId(LANOSCO)

QUEST.addKillId(SINGING_WIND)