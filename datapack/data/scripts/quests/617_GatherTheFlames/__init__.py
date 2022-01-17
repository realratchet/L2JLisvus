# Created by t0rm3nt0r (adapted for L2JLisvus by roko91)
# Drop rates and last reorganization by DrLecter
# for the Official L2J Datapack Project.
# Visit http://forum.l2jdp.com for more details.

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "617_GatherTheFlames"

TORCH = 7264

VULCAN = 8539

DROPLIST = {1381:51,1653:51,1387:53,1655:53,1390:56,1656:69,1389:55,1388:53,\
            1383:51,1392:56,1382:60,1654:52,1384:64,1394:51,1395:56,1385:52,\
            1391:55,1393:58,1657:57,1386:52,1652:49,1378:49,1376:48,1377:48,\
            1379:59,1380:49}

REWARDS = [ 6881,6883,6885,6887,6891,6893,6895,6897,6899,7580 ]
REWARDS2= [ 6882,6884,6886,6888,6892,6894,6896,6898,6900,7581 ]

#Change this value to 1 if you wish 100% recipes, default 60%
ALT_RP100=0

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [TORCH]

 def onEvent (self,event,st) :
     htmltext = event
     torches = st.getQuestItemsCount(TORCH)
     if event == "8539-03.htm" :
       if st.getPlayer().getLevel() >= 74 :
         st.set("cond","1")
         st.setState(State.STARTED)
         st.playSound("ItemSound.quest_accept")
       else :
         htmltext = "8539-02.htm"
         st.exitQuest(1)
     elif event == "8539-05.htm" and torches >= 1000 :
       htmltext = "8539-07.htm"
       st.takeItems(TORCH,1000)
       if ALT_RP100 == 1:
         st.giveItems(REWARDS2[st.getRandom(len(REWARDS2))],1)
       else:
         st.giveItems(REWARDS[st.getRandom(len(REWARDS))],1)
     elif event == "8539-08.htm" :
       st.takeItems(TORCH,-1)
       st.exitQuest(1)
     return htmltext    

 def onTalk (self,npc,st):
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     cond = st.getInt("cond")
     torches = st.getQuestItemsCount(TORCH)
     npcId = npc.getNpcId()
     if npcId == VULCAN :
       if id == State.CREATED :
         if st.getPlayer().getLevel() < 74 :
            st.exitQuest(1)
            htmltext = "8539-02.htm"
         else :
            htmltext = "8539-01.htm"
       elif torches < 1000 :
         htmltext = "8539-05.htm"
       else :
         htmltext = "8539-04.htm"
     return htmltext

 def onKill(self,npc,player,isPet):
     partyMember = self.getRandomPartyMemberState(player, npc, State.STARTED)
     if not partyMember: return
     st = partyMember.getQuestState(qn)
     if not st : return
     torches = st.getQuestItemsCount(TORCH)
     chance = DROPLIST[npc.getNpcId()]
     drop = st.getRandom(100)
     qty,chance = divmod(chance*Config.RATE_DROP_QUEST,100)
     if drop < chance : qty += 1
     qty = int(qty)
     if qty :
        st.giveItems(TORCH,qty)
        if divmod(torches+1,1000)[1] == 0 :
          st.playSound("ItemSound.quest_middle")
        else :
          st.playSound("ItemSound.quest_itemget")
     return

QUEST = Quest(617, qn, "Gather The Flames")
QUEST.addStartNpc(VULCAN)

QUEST.addTalkId(VULCAN)

for mob in DROPLIST.keys() :
  QUEST.addKillId(mob)