# Made by Emperorc (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets import NpcSay
from net.sf.l2j.gameserver.ai import CtrlIntention

qn = "22_TragedyInVonHellmannForest"

#NPCS
INNOCENTIN = 8328
TIFAREN = 8334
WELL = 8527
GHOST_PRIEST = 8528
GHOST_ADVENTURER = 8529
NPCS = range(8527,8530) + [8328, 8334]

#MOBS
SOUL_OF_WELL = 5217
MOBS = range(1553,1557) + [1561]

#ITEMS 
CROSS, SKULL, LETTER, JEWEL1, JEWEL2, SEALED_BOX, BOX = range(7141,7148)

def AutoChat(npc,text) :
    chars = npc.getKnownList().getKnownPlayers().values()
    if chars != None:
       for pc in chars :
          sm = NpcSay(npc.getObjectId(), 0, npc.getNpcId(), text)
          pc.sendPacket(sm)

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(7141,7148)
     self.priest = ""
     self.tifaren = 0
     self.soul = 0
     self.well = 0

 def onAdvEvent (self,event,npc,player) :
   st = player.getQuestState(qn)
   if not st: return
   htmltext = event
   if event == "8334-02.htm" :
       st2 = player.getQuestState("21_HiddenTruth")
       if st2 :
           if not (st2.getState().getName() == 'Completed' and st.getPlayer().getLevel() >= 63) :
               htmltext = "8334-03.htm"
               st.exitQuest(1)
       else :
           htmltext = "8334-03.htm"
           st.exitQuest(1)
   elif event == "8334-04.htm" :
       st.set("cond","1")
       st.set("id","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
   elif event == "8334-06.htm" :
       if st.getQuestItemsCount(CROSS) == 0 :
           htmltext = "8334-07.htm"
           st.set("cond","2")
   elif event == "8334-08.htm" :
       st.set("cond","4")
       st.set("id","2")
   elif event == "8334-13.htm" :
       if st.getInt("id") == 2 and st.getQuestItemsCount(CROSS) > 0 and st.getQuestItemsCount(SKULL) > 0 :
           if self.tifaren == 1 :
               htmltext = "8334-14.htm"
               st.set("cond","6")
           else :
               self.tifaren = 1
               st.set("cond","7")
               st.set("id","4")
               st.takeItems(SKULL,-1)
               priest = st.addSpawn(GHOST_PRIEST,38354,-49777,-1128)
               st.startQuestTimer("Despawn Ghost Priest",120000,priest)
               AutoChat(priest,player.getName()+", you have awoken me...")
               self.priest = player.getName()
       elif st.getInt("id") == 4 and st.getQuestItemsCount(CROSS) > 0 :
           if self.tifaren == 1 :
               htmltext = "8334-14.htm"
               st.set("cond","6")
           else :
               self.tifaren = 1
               st.takeItems(SKULL,-1)
               priest = st.addSpawn(GHOST_PRIEST,38354,-49777,-1128)
               st.startQuestTimer("Despawn Ghost Priest",120000,priest)
               AutoChat(priest,player.getName()+", you have awoken me...")
               self.priest = player.getName()
   elif event == "8528-05.htm" :
       st.playSound("AmbSound.d_horror_03")
   elif event == "8528-09.htm" :
       st.set("id","5")
       st.set("cond","8")
       st.startQuestTimer("Despawn Ghost Priest 2",3000,npc)
   elif event == "8328-04.htm" :
       st.takeItems(CROSS,-1)
       st.set("id","6")
   elif event == "8328-10.htm" :
       st.giveItems(LETTER,1)
       st.set("id","7")
       st.set("cond","9")
   elif event == "8529-03.htm" :
       st.takeItems(LETTER,-1)
       st.set("id","8")
   elif event == "8529-09.htm" :
       st.set("id","9")
   elif event == "8529-12.htm" :
       st.giveItems(JEWEL1,1)
       st.set("id","10")
       st.set("cond","10")
   elif event == "8527-02.htm" :
       if self.well == 0 :
           self.well = 1
           soul = st.addSpawn(SOUL_OF_WELL,34706,-54590,-2054)
           self.soul = 0
           st.playSound("SkillSound3.antaras_fear")
           st.startQuestTimer("Soul of Well 1",90000,soul)
           st.startQuestTimer("Soul of Well Despawn",120000,soul)
           soul.addDamageHate(player,0,99999)
           soul.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK,player,None)
       else :
           htmltext = "8527-03.htm"
   elif event == "8328-13.htm" :
       st.takeItems(BOX,-1)
       st.set("id","13")
       st.set("cond","15")
   elif event == "8328-21.htm" :
       st.set("id","14")
       st.set("cond","16")
   elif event == "Despawn Ghost Priest" :
       npc.doDie(npc)
       self.tifaren = 0
       if st.getQuestTimer("Despawn Ghost Priest 2") :
           st.getQuestTimer("Despawn Ghost Priest 2").cancel()
       return
   elif event == "Despawn Ghost Priest 2" :
       npc.doDie(npc)
       self.tifaren = 0
       AutoChat(npc,"My train of thought is chaotic. It goes back to the beginning of time...")
       if st.getQuestTimer("Despawn Ghost Priest") :
           st.getQuestTimer("Despawn Ghost Priest").cancel()
       return
   elif event == "Soul of Well 1" :
       self.soul = 1
       return
   elif event == "Soul of Well Despawn" :
       npc.doDie(npc)
       self.well = 0
       return
   return htmltext 

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   state = st.getState()
   id = st.getInt("id")
   ex = st.getInt("ex")
   cond = st.getInt("cond") 
   onlyone = st.getInt("onlyone")
   if state == State.COMPLETED :
       htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == INNOCENTIN :
       if state == State.CREATED :
           st2 = st.getPlayer().getQuestState("21_HiddenTruth")
           if st2 :
               if st2.getState().getName() == 'Completed' :
                   htmltext = "8328-00.htm"
       if id < 5 :
           if st.getQuestItemsCount(CROSS) == 0 :
               st.giveItems(CROSS,1)
               st.set("cond","3")
               htmltext = "8328-01.htm"
           else :
               htmltext = "8328-02.htm"
       elif id == 5 :
           htmltext = "8328-03.htm"
       elif id == 6 :
           htmltext = "8328-05.htm"
       elif id == 7 :
           htmltext = "8328-11.htm"
       elif id == 12 and st.getQuestItemsCount(BOX) > 0 :
           htmltext = "8328-12.htm"
       elif id == 13 :
           htmltext = "8328-14.htm"
       elif id == 14 :
           st.playSound("ItemSound.quest_finish")
           st.setState(State.COMPLETED)
           st.unset("cond")
           st.unset("id")
           if st.getPlayer().getLevel() < 64 :
               htmltext = "8328-23.htm"
           else :
               htmltext = "8328-22.htm"
   elif npcId == TIFAREN :
       if state == State.CREATED :
           htmltext = "8334-01.htm"
       elif id == 1 :
           htmltext = "8334-05.htm"
       elif id == 2 :
           if st.getQuestItemsCount(CROSS) >= 1 and st.getQuestItemsCount(SKULL) > 0 :
               if self.tifaren == 1 :
                   htmltext = "8334-11.htm"
               else :
                   htmltext = "8334-10.htm"
           else :
               htmltext = "8334-09.htm"
       elif id == 4 :
           if self.tifaren == 1 :
               if str(self.priest) == st.getPlayer().getName() :
                   htmltext = "8334-15.htm"
               else :
                   htmltext = "8334-16.htm"
                   st.set("cond","6")
           else :
               htmltext = "8334-17.htm"
       elif id == 5 :
           htmltext = "8334-18.htm"
   elif npcId == GHOST_PRIEST :
       st.playSound("AmbSound.d_horror_15")
       if str(self.priest) == st.getPlayer().getName() :
           htmltext = "8528-01.htm"
       else :
           htmltext = "8528-02.htm"
   elif npcId == GHOST_ADVENTURER :
       if id == 7 and st.getQuestItemsCount(LETTER) > 0 :
           htmltext = "8529-01.htm"
       elif id == 8 :
           htmltext = "8529-04.htm"
       elif id == 9 :
           htmltext = "8529-11.htm"
       elif id == 10 and st.getQuestItemsCount(JEWEL1) > 0 :
           htmltext = "8529-13.htm"
       elif id == 11 and st.getQuestItemsCount(JEWEL1) > 0 :
           htmltext = "8529-16.htm"
       elif id == 11 and st.getQuestItemsCount(JEWEL2) > 0 :
           if st.getQuestItemsCount(SEALED_BOX) == 0 :
               htmltext = "8529-17.htm"
               st.set("cond","12")
           else :
               st.takeItems(JEWEL2,-1)
               st.takeItems(SEALED_BOX,-1)
               st.giveItems(BOX,1)
               st.set("id","12")
               st.set("cond","14")
               htmltext = "8529-18.htm"
       elif id == 12 :
           htmltext = "8529-19.htm"
   elif npcId == WELL :
       if (id == 10 or id == 11) and st.getQuestItemsCount(JEWEL1) > 0 :
           htmltext = "8527-01.htm"
           st.playSound("AmbSound.dd_horror_01")
       elif id == 11 and st.getQuestItemsCount(JEWEL2) > 0 :
           if st.getQuestItemsCount(SEALED_BOX) == 0 :
               htmltext = "8527-04.htm"
               st.giveItems(SEALED_BOX,1)
               st.set("cond","13")
           else :
               htmltext = "8527-05.htm"
       elif id > 11 :
           htmltext = "8527-05.htm"
   return htmltext

 def onAttack (self,npc,player,damage,isPet):
   st = player.getQuestState(qn)
   if st :
       npcId = npc.getNpcId()
       id = st.getInt("id")
       if npcId == SOUL_OF_WELL :
           if id == 10 and st.getQuestItemsCount(JEWEL1) > 0 :
               st.set("id","11")
           elif id == 11 and st.getQuestItemsCount(JEWEL1) > 0 :
               st.takeItems(JEWEL1,-1)
               st.giveItems(JEWEL2,1)
               st.playSound("ItemSound.quest_itemget")
               st.set("cond","11")
   return

 def onKill(self,npc,player,isPet):
   npcId = npc.getNpcId()
   st = player.getQuestState(qn)
   if st :
       if npcId == SOUL_OF_WELL :
           self.well = 0
       elif npcId in MOBS :
           if st.getRandom(10) < 1 and st.getQuestItemsCount(SKULL) < 1:
               st.giveItems(SKULL,1)
               st.playSound("ItemSound.quest_itemget")
               st.set("cond","5")
   return

QUEST = Quest(22,qn,"Tragedy In Von Hellmann Forest")
QUEST.addStartNpc(INNOCENTIN)
QUEST.addStartNpc(TIFAREN)

for npcid in NPCS :
    QUEST.addTalkId(npcid)

QUEST.addAttackId(SOUL_OF_WELL)

for mobid in MOBS + [SOUL_OF_WELL] :
    QUEST.addKillId(mobid)