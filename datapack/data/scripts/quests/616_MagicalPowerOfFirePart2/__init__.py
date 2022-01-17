#Made by Emperorc (adapted for L2JLisvus by roko91)

import sys
from java.lang import System
from net.sf.l2j import Config
from net.sf.l2j.gameserver.datatables import SpawnTable
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets import NpcSay
from net.sf.l2j.util import Rnd

qn = "616_MagicalPowerOfFirePart2"

#NPC
Udan = 8379
Alter = 8558

#MOBS
Varka_Mobs = [ 1350, 1351, 1353, 1354, 1355, 1357, 1358, 1360, 1361, \
1362, 1369, 1370, 1364, 1365, 1366, 1368, 1371, 1372, 1373, 1374, 1375 ]
Nastron = 10306

#ITEMS
Totem2 = 7243
Fire_Heart = 7244

def AutoChat(npc,text) :
    chars = npc.getKnownList().getKnownPlayers().values()
    if chars != None:
       for pc in chars :
          sm = NpcSay(npc.getObjectId(), 0, npc.getNpcId(), text)
          pc.sendPacket(sm)

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [Fire_Heart]
     test = self.loadGlobalQuestVar("616_respawn")
     if test.isdigit() :
        remain = long(test) - System.currentTimeMillis()
        if remain <= 0 :
           self.addSpawn(8558,142368,-82512,-6487,58000, False, 0)
        else :
           self.startQuestTimer("spawn_npc", remain, None, None)
     else:
        self.addSpawn(8558,142368,-82512,-6487,58000, False, 0)

 def onAdvEvent (self,event,npc,player) :
   if event == "Soul of Fire Nastron has despawned" :
       npc.doDie(npc)
       self.addSpawn(8558,142368,-82512,-6487,58000, False, 0)
       AutoChat(npc,"The fetter strength is weaken Your consciousness has been defeated!")
       return
   elif event == "spawn_npc" :
       self.addSpawn(8558,142368,-82512,-6487,58000, False, 0)
       return
   st = player.getQuestState(qn)
   if not st: return
   cond = st.getInt("cond")
   id = st.getInt("id")
   Green_Totem = st.getQuestItemsCount(Totem2)
   Heart = st.getQuestItemsCount(Fire_Heart)
   htmltext = event
   if event == "8379-04.htm" :
       if st.getPlayer().getLevel() >= 75 and st.getPlayer().getAllianceWithVarkaKetra() <= -2 :
           if Green_Totem :
                st.set("cond","1")
                st.set("id","1")
                st.setState(State.STARTED)
                st.playSound("ItemSound.quest_accept")
                htmltext = "8379-04.htm"
           else :
                htmltext = "8379-02.htm"
                st.exitQuest(1)
       else :
           htmltext = "8379-03.htm"
           st.exitQuest(1)
   elif event == "8379-08.htm" :
       if Heart:
           htmltext = "8379-08.htm"
           st.takeItems(Fire_Heart,-1)
           st.addExpAndSp(10000,0)
           st.unset("id")
           st.unset("cond")
           st.playSound("ItemSound.quest_finish")
           st.exitQuest(1)
       else :
           htmltext = "8379-09.htm"
   elif event == "8558-02.htm" :
       if Green_Totem == 0 :
           htmltext = "8558-04.htm"
       else:
           spawnedNpc = st.addSpawn(Nastron,142528,-82528,-6496)
           st.takeItems(Totem2,1)
           st.set("id","2")
           npc.deleteMe()
           st.set("cond","2")
           self.startQuestTimer("Soul of Fire Nastron has despawned",1200000,spawnedNpc,None)
           AutoChat(spawnedNpc,"The fire charm then is the flame and the lava strength! Opposes with it only has the blind alley!")
   return htmltext

 def onTalk (self,npc,st):
    htmltext = JQuest.getNoQuestMsg()
    if st :
        npcId = npc.getNpcId()
        cond = st.getInt("cond")
        id = st.getInt("id")
        Green_Totem = st.getQuestItemsCount(Totem2)
        Heart = st.getQuestItemsCount(Fire_Heart)
        if npcId == Udan :
            if st.getState()== State.CREATED :
                htmltext = "8379-01.htm"
            elif id == 1 or id == 2 :
                htmltext = "8379-05.htm"
            elif id == 3:
                if Heart :
                    htmltext = "8379-06.htm"
                else :
                    htmltext = "8379-07.htm"
        elif npcId == Alter :
            htmltext = "8558-01.htm"
    return htmltext

 def onKill(self,npc,player,isPet):
    npcId = npc.getNpcId()
    if npcId == Nastron :
        respawnMinDelay = 43200000  * int(Config.RAID_MIN_RESPAWN_MULTIPLIER)
        respawnMaxDelay = 129600000 * int(Config.RAID_MAX_RESPAWN_MULTIPLIER)
        respawn_delay = Rnd.get(respawnMinDelay,respawnMaxDelay)
        self.saveGlobalQuestVar("616_respawn", str(System.currentTimeMillis()+respawn_delay))
        self.startQuestTimer("spawn_npc", respawn_delay, None, None)
        self.cancelQuestTimer("Soul of Fire Nastron has despawned",npc,None)
        party = player.getParty()
        if party :
            PartyQuestMembers = []
            for player1 in party.getPartyMembers() :
                st1 = player1.getQuestState(qn)
                if st1 :
                    if st1.getState() == State.STARTED and (st1.getInt("cond") == 1 or st1.getInt("cond") == 2) :
                        PartyQuestMembers.append(st1)
            if len(PartyQuestMembers) == 0 : return
            st = PartyQuestMembers[Rnd.get(len(PartyQuestMembers))]
            if st.getQuestItemsCount(Totem2) > 0 :
                st.takeItems(Totem2,1)
            st.giveItems(Fire_Heart,1) 
            st.set("cond","3")
            st.set("id","3")
            st.playSound("ItemSound.quest_middle")
        else :
            st = player.getQuestState(qn)
            if not st : return
            if st.getState() == State.STARTED and (st.getInt("cond") == 1 or st.getInt("cond") == 2) :
                if st.getQuestItemsCount(Totem2) > 0 :
                    st.takeItems(Totem2,1)
                st.giveItems(Fire_Heart,1) 
                st.set("cond","3")
                st.set("id","3")
                st.playSound("ItemSound.quest_middle")
    elif npcId in Varka_Mobs :
        st = player.getQuestState(qn)
        if st :
            if st.getQuestItemsCount(Fire_Heart) :
                st.takeItems(Fire_Heart,-1)
            st.unset("cond")
            st.unset("id")
            st.exitQuest(1)
    return

QUEST = Quest(616,qn,"Magical Power of Fire - Part 2")
QUEST.addStartNpc(Udan)

QUEST.addTalkId(Udan)
QUEST.addTalkId(Alter)

QUEST.addKillId(Nastron)

for mobId in Varka_Mobs:
    QUEST.addKillId(mobId)