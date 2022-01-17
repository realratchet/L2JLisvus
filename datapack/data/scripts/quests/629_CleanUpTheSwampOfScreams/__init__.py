# Made by Hawkin (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "629_CleanUpTheSwampOfScreams"

#NPC
CAPTAIN = 8553
#ITEMS
CLAWS = 7250
COIN = 7251
#CHANCES
MAX=1000
CHANCE={
    1508:500,
    1509:431,
    1510:521,
    1511:576,
    1512:746,
    1513:530,
    1514:538,
    1515:545,
    1516:553,
    1517:560
}

default=JQuest.getNoQuestMsg()

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [CLAWS]
 
 def onEvent (self,event,st) :
   htmltext = event
   if event == "8553-1.htm" :
     if st.getPlayer().getLevel() >= 66 :
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
     else:
       htmltext=default
       st.exitQuest(1)
   elif event == "8553-3.htm" :
     if st.getQuestItemsCount(CLAWS) >= 100 :
       st.takeItems(CLAWS,100)
       st.giveItems(COIN,20)
     else :
       htmltext = "8553-3a.htm"
   elif event == "8553-5.htm" :
     st.playSound("ItemSound.quest_finish")
     st.exitQuest(1)
   return htmltext

 def onTalk (self,npc,st):
   htmltext = default
   if st :
       npcId = npc.getNpcId()
       id = st.getState()
       cond = st.getInt("cond")
       if (st.getQuestItemsCount(7246) or st.getQuestItemsCount(7247)) :
         if cond == 0 :
           if st.getPlayer().getLevel() >= 66 :
             htmltext = "8553-0.htm"
           else:
             htmltext = "8553-0a.htm"
             st.exitQuest(1)
         elif id == State.STARTED :
             if st.getQuestItemsCount(CLAWS) >= 100 :
               htmltext = "8553-2.htm"
             else :
               htmltext = "8553-1a.htm"
       else :
         htmltext = "8553-6.htm"
         st.exitQuest(1)
   return htmltext

 def onKill(self,npc,player,isPet):
    partyMember = self.getRandomPartyMemberState(player, npc, State.STARTED)
    if not partyMember : return
    st = partyMember.getQuestState(qn)
    if st :
        if st.getState() == State.STARTED :
            prevItems = st.getQuestItemsCount(CLAWS)
            random = st.getRandom(MAX)
            chance = CHANCE[npc.getNpcId()]*Config.RATE_DROP_QUEST
            numItems, chance = divmod(chance,MAX)
            if random<chance :
                numItems += 1
            st.giveItems(CLAWS,int(numItems))
            if int(prevItems+numItems)/100 > int(prevItems)/100 :
                st.playSound("ItemSound.quest_middle")
            else:
                st.playSound("ItemSound.quest_itemget")
    return

QUEST = Quest(629,qn,"Clean Up the Swamp of Screams")
QUEST.addStartNpc(CAPTAIN)

QUEST.addTalkId(CAPTAIN)

for mobs in range(1508,1518) :
  QUEST.addKillId(mobs)