# Created by Emperorc (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "606_WarWithVarkaSilenos"

#NPC
Kadun = 8370

#Mobs
Varka_Mobs = [ 1350, 1353, 1354, 1355, 1357, 1358, 1360, 1362, 1364, 1365, 1366, 1368, 1369, 1371, 1373 ]
Ketra_Orcs = [ 1324, 1325, 1327, 1328, 1329, 1331, 1332, 1334, 1335, 1336, 1338, 1339, 1340, 1342, 1343, 1344, 1345, 1346, 1347, 1348, 1349 ]

Chance = {
  1350:500,#Recruit
  1353:510,#Scout
  1354:522,#Hunter
  1355:519,#Shaman
  1357:529,#Priest
  1358:529,#Warrior  
  1360:539,#Medium
  1362:568,#Officer
  1364:558,#Seer
  1365:568,#Great Magus
  1366:664,#General
  1368:568,#Great Seer
  1369:548,#Commander
  1371:713,#Head magus
  1373:738 #Prophet
}

#Items
Horn = 7186
Mane = 7233

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [Mane]

 def onEvent (self,event,st) :
     htmltext = event
     manes = st.getQuestItemsCount(Mane)
     if event == "8370-03.htm" :
       if st.getPlayer().getLevel() >= 74 and st.getPlayer().getAllianceWithVarkaKetra() >= 1 : #the alliance check is only temporary, should be done on core side/AI
            st.set("cond","1")
            st.set("id","1")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
            htmltext = "8370-03.htm"
       else :
            htmltext = "8370-02.htm"
            st.exitQuest(1)
     elif event == "8370-06.htm" :
         htmltext = "8370-06.htm"
     elif event == "8370-07.htm" :
         if manes >= 100 :
             htmltext = "8370-07.htm"
             st.takeItems(Mane,100)
             st.giveItems(Horn,20)
         else :
             htmltext = "8370-08.htm"
     elif event == "8370-09.htm" :
         htmltext == "8370-09.htm"
         st.unset("id")
         st.takeItems(Mane,-1)
         st.exitQuest(1)
     return htmltext

 def onTalk (self,npc,st):
     htmltext = JQuest.getNoQuestMsg()
     if st :
        npcId = npc.getNpcId()
        id = st.getInt("id")
        manes = st.getQuestItemsCount(Mane)
        if npcId == Kadun :
         if id == 1 :
             if manes :
                 htmltext = "8370-04.htm"
             else :
                htmltext = "8370-05.htm"
         else :
             htmltext = "8370-01.htm"
     return htmltext

 def onKill(self,npc,player,isPet):
     partyMember = self.getRandomPartyMemberState(player, npc, State.STARTED)
     if not partyMember: return
     st = partyMember.getQuestState(qn)
     if st :
        if st.getState() == State.STARTED :
         npcId = npc.getNpcId()
         count = st.getQuestItemsCount(Mane)
         st2 = partyMember.getQuestState("605_AllianceWithKetraOrcs")
         if npcId in Varka_Mobs and partyMember.getAllianceWithVarkaKetra() >= 1 :
        #see comments in 605 : Alliance with Ketra Orcs for reason for doing st2 check
            if not st2 :
                numItems,chance = divmod(Chance[npcId]*Config.RATE_DROP_QUEST,1000)
                if st.getRandom(1000) < chance :
                    numItems += 1
                numItems = int(numItems)
                if numItems != 0 :
                    if int((count+numItems)/100) > int(count/100) :
                        st.playSound("ItemSound.quest_middle")
                    else :
                        st.playSound("ItemSound.quest_itemget")
                    st.giveItems(Mane,numItems)
         elif npcId in Ketra_Orcs :
             st.unset("id")
             st.takeItems(Mane,-1)
             st.exitQuest(1)
     return

QUEST = Quest(606, qn, "War With Varka Silenos")
QUEST.addStartNpc(Kadun)

QUEST.addTalkId(Kadun)

for mobId in Varka_Mobs :
  QUEST.addKillId(mobId)

for mobId in Ketra_Orcs :
  QUEST.addKillId(mobId)