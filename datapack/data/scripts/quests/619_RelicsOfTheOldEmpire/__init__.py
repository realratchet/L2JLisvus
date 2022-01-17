# Created by t0rm3nt0r (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "619_RelicsOfTheOldEmpire"

RELICS = 7254
ENTRANCE = 7075
GHOST = 8538
MOBS = [ 1396,1397,1398,1399,1400,1401,1402,1403,1404,1405,1406,1407,1408,1409,1410,1411,1412,1413,1414, \
1415,1416,1417,1418,1419,1420,1421,1422,1423,1424,1425,1426,1427,1428,1429,1430,1431,1432,1433,1434,1798, \
1799,1800,12955,12956,12957,12958,12959,12960,12961,12962,12963,12964,12965,12966,12967,12968,12969,12970, \
12971,12972,12973,12974,12975,12976,12977,12978,12979,12980,12981,12982,12983,12984,12985,12986,12987,12988, \
12989,12990,12991,12992,12993,12994,12995,12996,12997,12998,12999,13000,13001,13002,13003,13004,13005,13006, \
13007,13008,13009,13010,13011,13012,13013,13014,13015,13016,13017,13018,13019,13020,13021,13022,13023,13024, \
13025,13026,13027,13028,13029,13030,13031,13032,13033,13034,13035,13036,13037,13038,13039,13040,13041,13042, \
13043,13044,13045,13046,13047,13048,13049,13050,13051,13052,13053,13054,13055,13056,13057,13058,13059,13060, \
13061,13062,13063,13064,13065,13066,13067,13068,13069,13070,13071,13072,13073,13074,13075,13076,13077,13078, \
13079,13080,13081,13082,13083,13084,13085,13086,13087,13088,13089,13090,13091]

REWARDS = [ 6881,6883,6885,6887,6891,6893,6895,6897,6899,7580 ]
REWARDS2= [ 6882,6884,6886,6888,6892,6894,6896,6898,6900,7581 ]

#Change this value to 1 if you wish 100% recipes, default 60%
ALT_RP100=0

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [RELICS, ENTRANCE]

 def onEvent (self,event,st) :
     htmltext = event
     relics = st.getQuestItemsCount(RELICS)
     if event == "8538-03.htm" :
       if st.getPlayer().getLevel() >= 74 :
          st.set("cond","1")
          st.setState(State.STARTED)
          st.playSound("ItemSound.quest_accept")
       else :
          htmltext = "8538-02.htm"
          st.exitQuest(1)
     elif event == "8538-07.htm" :
       if relics >= 1000 :
          htmltext = "8538-07.htm"
          st.takeItems(RELICS,1000)
          if ALT_RP100 == 1:
             st.giveItems(REWARDS2[st.getRandom(len(REWARDS2))],1)
          else:
             st.giveItems(REWARDS[st.getRandom(len(REWARDS))],1)
       else :
          htmltext = "8538-05.htm"
     elif event == "8538-08.htm" :
         st.exitQuest(1)
     return htmltext    

 def onTalk (self,npc,st):
     htmltext = JQuest.getNoQuestMsg()
     if st :
        id = st.getState()
        cond = st.getInt("cond")
        relics = st.getQuestItemsCount(RELICS)
        entrance = st.getQuestItemsCount(ENTRANCE)
        if id==State.CREATED:
           if st.getPlayer().getLevel() >= 74 :
              htmltext="8538-01.htm"
           else :
              htmltext="8538-02.htm"
              st.exitQuest(1)
        else :
           if cond == 1 and relics >= 1000 :
              htmltext = "8538-04.htm"
           elif entrance :
              htmltext = "8538-05.htm"
           else :
              htmltext = "8538-05a.htm"
     return htmltext

 def onKill(self,npc,player,isPet):
     partyMember = self.getRandomPartyMember(player, npc, "1")
     if not partyMember: return
     st = partyMember.getQuestState(qn)
     if st :
       if st.getState() == State.STARTED :
         numItems, chance = divmod(100*Config.RATE_DROP_QUEST,100)
         if st.getRandom(100) < chance :
            numItems += 1
         st.giveItems(RELICS,int(numItems))
         st.playSound("ItemSound.quest_itemget")
         if st.getRandom(100) < (5*Config.RATE_DROP_QUEST) :
             st.giveItems(ENTRANCE,1)
             st.playSound("ItemSound.quest_middle")
     return

QUEST = Quest(619, qn, "Relics of the Old Empire")
QUEST.addStartNpc(GHOST)

QUEST.addTalkId(GHOST)

for mobId in MOBS :
  QUEST.addKillId(mobId)