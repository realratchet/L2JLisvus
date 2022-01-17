# Created by Emperorc (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "612_WarWithKetraOrcs"

#NPC
Ashas = 8377

#Mobs
Varka_Mobs = [ 1350, 1351, 1353, 1354, 1355, 1357, 1358, 1360, 1361, 1362, 1369, 1370, 1364, 1365, 1366, 1368, 1371, 1372, 1373, 1374, 1375 ]
Ketra_Orcs = [ 1324, 1327, 1328, 1329, 1331, 1332, 1334, 1336, 1338, 1339, 1340, 1342, 1343, 1345, 1347 ]


Chance = {
  1324:500,
  1327:510,
  1328:522,
  1329:519,
  1331:529,
  1332:664,
  1334:539,
  1336:529,
  1338:558,
  1339:568,
  1340:568,
  1342:578,
  1343:548,
  1345:713,
  1347:738
}

#Items
Seed = 7187
Molar = 7234

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [Molar]

 def onEvent (self,event,st) :
     htmltext = event
     Molars = st.getQuestItemsCount(Molar)
     if event == "8377-03.htm" :
       if st.getPlayer().getLevel() >= 74 and st.getPlayer().getAllianceWithVarkaKetra() <= -1 : #the alliance check is only temporary, should be done on core side/AI
            st.set("cond","1")
            st.set("id","1")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
            htmltext = "8377-03.htm"
       else :
            htmltext = "8377-02.htm"
            st.exitQuest(1)
     elif event == "8377-06.htm" :
         htmltext = "8377-06.htm"
     elif event == "8377-07.htm" :
         if Molars >= 100 :
             htmltext = "8377-07.htm"
             st.takeItems(Molar,100)
             st.giveItems(Seed,20)
         else :
             htmltext = "8377-08.htm"
     elif event == "8377-09.htm" :
         htmltext == "8377-09.htm"
         st.unset("id")
         st.takeItems(Molar,-1)
         st.exitQuest(1)
     return htmltext

 def onTalk (self,npc,st):
     htmltext = JQuest.getNoQuestMsg()
     if st :
         npcId = npc.getNpcId()
         id = st.getInt("id")
         Molars = st.getQuestItemsCount(Molar)
         if npcId == Ashas and st.getPlayer().getAllianceWithVarkaKetra() <= -1 : #the alliance check is only temporary, should be done on core side/AI
             if id == 1 :
                 if Molars :
                     htmltext = "8377-04.htm"
                 else :
                    htmltext = "8377-05.htm"
             else :
                 htmltext = "8377-01.htm"
     return htmltext

 def onKill(self,npc,player,isPet):
     partyMember = self.getRandomPartyMemberState(player,npc,State.STARTED)
     if not partyMember : return
     st = partyMember.getQuestState(qn)
     npcId = npc.getNpcId()
     count = st.getQuestItemsCount(Molar)
     st2 = partyMember.getQuestState("611_AllianceWithVarkaSilenos")
     if npcId in Ketra_Orcs and partyMember.getAllianceWithVarkaKetra() <= -1 :
    #see comments in 611 : Alliance with Varka Silenos for reason for doing st2 check
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
            st.giveItems(Molar,numItems)
     elif npcId in Varka_Mobs :
         st.unset("id")
         st.takeItems(Molar,-1)
         st.exitQuest(1)
     return

QUEST = Quest(612, qn, "War With Ketra Orcs")
QUEST.addStartNpc(Ashas)

QUEST.addTalkId(Ashas)

for mobId in Ketra_Orcs :
  QUEST.addKillId(mobId)

for mobId in Varka_Mobs :
  QUEST.addKillId(mobId)