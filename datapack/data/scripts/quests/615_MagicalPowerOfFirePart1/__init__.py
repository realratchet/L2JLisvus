#Made by Emperorc
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets import NpcSay
from java.util import Iterator

qn = "615_MagicalPowerOfFirePart1"

#NPC
Naran = 8378
Udan = 8379
Asefa_Box = 8559
Eye = 8684

#MOBS
Varka_Mobs = [ 1350, 1351, 1353, 1354, 1355, 1357, 1358, 1360, 1361, \
1362, 1369, 1370, 1364, 1365, 1366, 1368, 1371, 1372, 1373, 1374, 1375 ]
Ketra_Orcs = [ 1324, 1325, 1327, 1328, 1329, 1331, 1332, 1334, 1335, \
1336, 1338, 1339, 1340, 1342, 1343, 1344, 1345, 1346, 1347, 1348, 1349 ]

#ITEMS
Key = 1661
Totem = 7242
Wisdom_Stone = 7081
Totem2 = 7243

def AutoChat(npc,text) :
    chars = npc.getKnownList().getKnownPlayers().values()
    if chars != None:
       for pc in chars :
          sm = NpcSay(npc.getObjectId(), 0, npc.getNpcId(), text)
          pc.sendPacket(sm)

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [Totem]

 def onAdvEvent (self, event, npc, player) :
   st = player.getQuestState(qn)
   if not st: return
   cond = st.getInt("cond")
   id = st.getInt("id")
   aggro = st.getInt("aggro")
   Thief_Key = st.getQuestItemsCount(Key)
   htmltext = event
   if event == "8378-04.htm" :
       if st.getPlayer().getLevel() >= 74 and st.getPlayer().getAllianceWithVarkaKetra() <= -2 :
            st.set("cond","1")
            st.set("id","2")
            st.set("aggro","0")
            st.set("spawned","0")
            st.set("npcid","0")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
            htmltext = "8378-04.htm"
       else :
            htmltext = "8378-02.htm"
            st.exitQuest(1)
   elif event == "8559-03.htm" :
       if Thief_Key:
           st.takeItems(Key,1)
           if aggro == 1 :
               htmltext = "8559-04.htm"
           else :
               htmletext = "8559-03.htm"
               st.giveItems(Totem,1)
               st.set("id","5")
               st.set("cond","3")
               st.playSound("ItemSound.quest_middle")
       else :
           htmltext = "8559-02.htm"
   elif event == "Eye of Udan has despawned" :
        AutoChat(npc,"I'll be waiting for your return.")
        npc.doDie(npc)
        st.set("spawned","0")
   return htmltext

 def onTalk (self, npc, st):
    htmltext = JQuest.getNoQuestMsg()
    if st :
        npcId = npc.getNpcId()
        cond = st.getInt("cond")
        id = st.getInt("id")
        aggro = st.getInt("aggro")
        Red_Totem = st.getQuestItemsCount(Totem)
        Stone = st.getQuestItemsCount(Wisdom_Stone)
        if st.getState() == State.CREATED :
            if npcId == Naran :
                htmltext = "8378-01.htm"
        elif st.getState() == State.STARTED :
            if npcId == Naran :
                if id == 2 :
                    htmltext = "8378-05.htm"
            elif npcId == Udan :
                if st.getPlayer().getAllianceWithVarkaKetra() <= -2 :
                    if id == 2 :
                        htmltext = "8379-01.htm"
                        st.set("cond","2")
                        st.set("id","3")
                    elif id == 3 :
                        htmltext = "8379-02.htm"
                    elif id == 4 or aggro == 1 :
                        htmltext = "8379-03.htm"
                        st.set("id","3")
                        st.set("aggro","0")
                        st.set("spawned","0")
                        st.set("npcid","0")
                    elif id == 5 and Red_Totem :
                        htmltext = "8379-04.htm"
                        st.giveItems(Wisdom_Stone,1)
                        st.takeItems(Totem,1)
                        st.giveItems(Totem2,1)
                        st.unset("id")
                        st.unset("aggro")
                        st.playSound("ItemSound.quest_middle")
                        st.exitQuest(1)
            elif npcId == Asefa_Box :
                    if st.getPlayer().getAllianceWithVarkaKetra() <= -2 :
                        if id == 3 :
                            htmltext = "8559-01.htm"
    return htmltext

 def onAttack (self, npc, player, damage, isPet):
    st = player.getQuestState(qn)
    if st:
        if st.getState() == State.STARTED :
            npcId = npc.getNpcId()
            id = st.getInt("id")
            Red_Totem = st.getQuestItemsCount(Totem)
            if st.getInt("spawned") == 0 and npc.getObjectId() != st.getInt("npcid"):
                if npcId in Ketra_Orcs :
                    if id > 2 :
                        xx = int(player.getX())
                        yy = int(player.getY())
                        zz = int(player.getZ())
                        st.set("aggro","1")
                        st.set("cond","1")
                        st.set("id","4")
                        spawnedNpc = st.addSpawn(Eye,xx,yy,zz)
                        st.set("spawned","1")
                        st.set("npcid",str(npc.getObjectId()))
                        AutoChat(spawnedNpc,"You cannot escape Udan's eyes!")#this is only a temp message until we find out what it actually is! string = 61503
                        st.startQuestTimer("Eye of Udan has despawned",10000,spawnedNpc)
                        if Red_Totem :
                            st.takeItems(Totem,-1)
    return

 def onKill(self,npc,player,isPet):
    st = player.getQuestState(qn)
    if st :
       if st.getState() == State.STARTED :
           npcId = npc.getNpcId()
           cond = st.getInt("cond")
           id = st.getInt("id")
           Red_Totem = st.getQuestItemsCount(Totem)
           if npcId in Varka_Mobs :
               st.unset("id")
               st.unset("aggro")
               st.exitQuest(1)
               if Red_Totem:
                   st.takeItems(Totem,-1)
    return


QUEST = Quest(615,qn,"Magical Power of Fire - Part 1")
QUEST.addStartNpc(Naran)

QUEST.addTalkId(Naran)
QUEST.addTalkId(Udan)
QUEST.addTalkId(Asefa_Box)

for mobId in Varka_Mobs:
    QUEST.addKillId(mobId)
for mobId in Ketra_Orcs:
    QUEST.addAttackId(mobId)